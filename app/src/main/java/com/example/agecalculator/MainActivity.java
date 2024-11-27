package com.example.agecalculator;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText dateOfBirthEditText;
    private EditText currentDateEditText;
    private TextView ageTextView;
    private Button calculateButton;
    private SimpleDateFormat formatForDate = new SimpleDateFormat("MM/dd/yyyy");
    private Button nextBirthdaysButton;

   
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize elements
        dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);
        currentDateEditText = findViewById(R.id.editTextCurrentDate);
        ageTextView = findViewById(R.id.resultTextView);
        calculateButton = findViewById(R.id.calculateButton);
        nextBirthdaysButton = findViewById(R.id.nextBirthdaysButton);

        currentDateEditText.setText(formatForDate.format(Calendar.getInstance().getTime()));

        // On click event for calculating age when button you clicked on the button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAge();
            }
        });

        // On click event for opening next birthday activity when one clicks on next bday
        nextBirthdaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextBirthdaysActivity();
            }
        });
    }

    private void calculateAge() {
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String currentDate = currentDateEditText.getText().toString();

        if (dateOfBirth.isEmpty()) {
            dateOfBirthEditText.setError("Enter your birthday.");
            return;
        }

        try {
            Date birthDate = formatForDate.parse(dateOfBirth);
            Date todayDate = formatForDate.parse(currentDate);

            // Calculate age and handle exceptions
            int age = calculateAgeFromDates(birthDate, todayDate);
            ageTextView.setText(String.format("You are %d years old.", age));

            // Added: Get the birth month and play sound based on the month
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);
            int birthMonth = birthCalendar.get(Calendar.MONTH) + 1; // Add 1 because Calendar.MONTH is 0-based

            playSeasonSoundAndChangeBackground(birthMonth);


        } catch (ParseException e) {
            ageTextView.setText("Invalid date format.");
        }
    }

    private int calculateAgeFromDates(Date birthDate, Date todayDate) {
        // Create calendar instance using today's date
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(todayDate);

        // Create calendar instance using entered birthday's year, month, and day
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        int year = todayCalendar.get(Calendar.YEAR);
        int month = todayCalendar.get(Calendar.MONTH);
        int day = todayCalendar.get(Calendar.DAY_OF_MONTH);

        int birthYear = birthCalendar.get(Calendar.YEAR);
        int birthMonth = birthCalendar.get(Calendar.MONTH);
        int birthDay = birthCalendar.get(Calendar.DAY_OF_MONTH);

        // Initial age calculation
        int age = year - birthYear;

        // If birthday hasn't occurred yet this year, subtract age by 1
        if (birthMonth > month || (birthMonth == month && birthDay > day)) {
            age--;
        }

        return age;
    }

    private void openNextBirthdaysActivity() {
        String dateOfBirth = dateOfBirthEditText.getText().toString();

        if (dateOfBirth.isEmpty()) {
            dateOfBirthEditText.setError("Enter your birthday.");
            return;
        }

        Intent intent = new Intent(this, NextBirthdays.class);
        intent.putExtra("birthday", dateOfBirth);
        startActivity(intent);
    }

    // Added Method to play sound and change background based on the user's birth month
    public void playSeasonSoundAndChangeBackground(int month) {

        // initialize variables to store the sound and background image
        //initialize to 0 because when the apps runs it no sound or background is selected yet
        int soundResource = 0;
        int bdayBackgroundImg = 0;

// checks  the month and assign img and sound based on the month user has been born
        if (month == 12 || month == 1 || month == 2) {
            soundResource = R.raw.bell; // Winter
            bdayBackgroundImg = R.drawable.summerbackground;

        } else if (month == 3 || month == 4 || month == 5) {
            soundResource = R.raw.spring; // Spring
            bdayBackgroundImg = R.drawable.spring_background1;

        } else if (month == 6 || month == 7 || month == 8) {
            soundResource = R.raw.summer; // Summer
            bdayBackgroundImg = R.drawable.oceanwaves;

        } else if (month == 9 || month == 10 || month == 11) {
            soundResource = R.raw.fall2; // Fall
            bdayBackgroundImg = R.drawable.fallbackground4;

        }

//see if a sound is playing
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // if theres a sound playing stop it before playing new sound
            mediaPlayer.release();
        }
// initialize to 0 because bc no sound has been assigned just yet
if (soundResource!=0) {
    // if is not 0 it means that a has been chosen based on the user birth month and the sound is playing
    mediaPlayer = MediaPlayer.create(this, soundResource);

    if (mediaPlayer != null) {
        mediaPlayer.start();
        // If a sound has been assigned based on the user's birth month then play it

    }
}
        //change the background
        View rootView = findViewById(R.id.rootLayout);
        if (rootView != null) {
            rootView.setBackgroundResource(bdayBackgroundImg);
            //changes the background based on the user's birth month user selected
        }
    }}