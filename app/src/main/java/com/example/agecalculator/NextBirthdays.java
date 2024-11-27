package com.example.agecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NextBirthdays extends AppCompatActivity {

    private TextView nextBirthdayTextView;
    private TextView ageTextView;
    private TextView monthsLeftTextView;
    private TextView daysLeftTextView;
    private Button goBackButton;
    private SimpleDateFormat formatForDate = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_birthdays);

// initialize elements
        nextBirthdayTextView = findViewById(R.id.nextBirthdayTextView);
        ageTextView = findViewById(R.id.ageTextView);
        monthsLeftTextView = findViewById(R.id.monthsLeftTextView);
        daysLeftTextView = findViewById(R.id.daysLeftTextView);
        goBackButton = findViewById(R.id.goBackButton);


        String birthDateString = getIntent().getStringExtra("birthday");

        if (birthDateString != null && !birthDateString.isEmpty()) {
            try {
                Date birthDate = formatForDate.parse(birthDateString);


                String nextBirthday = calculateNextBirthday(birthDate);
                nextBirthdayTextView.setText("Next Birthday: " + nextBirthday);

                int age = calculateAge(birthDate);
                ageTextView.setText("You are currently " + age + " years old.");

                int monthsLeft = calculateMonthsLeft(birthDate);
                monthsLeftTextView.setText("Months left until next birthday: " + monthsLeft);

                int daysLeft = calculateDaysLeft(birthDate);
                daysLeftTextView.setText("Days left until next birthday: " + daysLeft);

            } catch (Exception e) {
                nextBirthdayTextView.setText("Invalid birth date format.");
            }
        } else {
            nextBirthdayTextView.setText("No birth date provided.");
        }

// goes back to main menu
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String calculateNextBirthday(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();
        int currentYear = currentCalendar.get(Calendar.YEAR);


        birthCalendar.set(Calendar.YEAR, currentYear);


        if (currentCalendar.after(birthCalendar)) {
            birthCalendar.set(Calendar.YEAR, currentYear + 1);
        }


        return formatForDate.format(birthCalendar.getTime());
    }

//calculates user's age
    private int calculateAge(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();

        birthCalendar.setTime(birthDate);
        int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        // If birthday hasn't occurred yet this year, subtract age by 1
        if (currentCalendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (currentCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
    // Calculates the number of months left until the next bday
    private int calculateMonthsLeft(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();


        birthCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));

        if (currentCalendar.after(birthCalendar)) {
            birthCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) + 1);
        }


        int monthsLeft = birthCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH) - 1;

        if (monthsLeft < 0) {
            monthsLeft += 12;
            // If the birthday passed this year, calculate how many months and days left until next bday

        }

        return monthsLeft;
    }
    // Calculate the number of days left until the next bday
    private int calculateDaysLeft(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();


        birthCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));

        // if the birthday has  passed already, set bday  to next year
        if (currentCalendar.after(birthCalendar)) {
            birthCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) + 1);
        }


        long diffInMillis = birthCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        return (int) diffInDays;
    }


}
