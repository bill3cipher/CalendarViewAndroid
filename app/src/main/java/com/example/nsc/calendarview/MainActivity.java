package com.example.nsc.calendarview;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends Activity {

    CompactCalendarView calendarView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM", Locale.getDefault());
    private Button btnPreviousMonth;
    private Button btnCurrentMonth;
    private Button btnNextMonth;
    private Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
    String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        btnPreviousMonth = (Button) findViewById(R.id.btnPreviousMonth);
        btnCurrentMonth = (Button) findViewById(R.id.btnCurrentMonth);
        btnNextMonth = (Button) findViewById(R.id.btnNextMonth);

        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.setDayColumnNames(dayNames);
        loadEvents();

        /*Event ev1 = new Event(Color.RED, 1502854760000L, "Test Event Day");
        calendarView.addEvent(ev1);

        Event ev2 = new Event(Color.RED, 1503193348000L);
        calendarView.addEvent(ev2);*/

       // List<Event> events = calendarView.getEvents(1502854760000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
        calendar.add(Calendar.MONTH, -1);

        btnCurrentMonth.setText(dateFormatMonth.format(calendarView.getFirstDayOfCurrentMonth()));
        btnPreviousMonth.setText(dateFormatMonth.format(calendar.getTime()));

        calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
        calendar.add(Calendar.MONTH, +1);

        btnNextMonth.setText(dateFormatMonth.format(calendar.getTime()));


        btnCurrentMonth.setText(dateFormatMonth.format(calendarView.getFirstDayOfCurrentMonth()));

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);

                for (Event event : events ) {
                    Toast.makeText(MainActivity.this, event.getData().toString(), Toast.LENGTH_SHORT).show();
                }

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Event Alert");
                alertDialog.setMessage(events.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                Log.d("TAG", "Day was clicked: " + dateClicked + " with events " + events);
                Toast.makeText(MainActivity.this, events.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
                calendar.add(Calendar.MONTH, -1);

                btnCurrentMonth.setText(dateFormatMonth.format(calendarView.getFirstDayOfCurrentMonth()));
                btnPreviousMonth.setText(dateFormatMonth.format(calendar.getTime()));

                calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
                calendar.add(Calendar.MONTH, +1);

                btnNextMonth.setText(dateFormatMonth.format(calendar.getTime()));


                btnCurrentMonth.setText(dateFormatMonth.format(calendarView.getFirstDayOfCurrentMonth()));

            }
        });

        btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.showPreviousMonth();
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.showNextMonth();
            }
        });
    }

    private void loadEvents() {
        addEvent(-1, -1);
        addEvent(Calendar.DECEMBER, -1);
        addEvent(Calendar.AUGUST, -1);

    }

    private void addEvent(int month, int year) {
        currentCalendar.setTime(new Date());
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalendar.getTime();
        for (int i = 0; i < 6; i++) {
            currentCalendar.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalendar.set(Calendar.MONTH, month);
            }
            if (year > -1) {
                currentCalendar.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalendar.set(Calendar.YEAR, year);
            }
            currentCalendar.add(Calendar.DATE, i);
            setToMidnight(currentCalendar);
            long timeInMillis = currentCalendar.getTimeInMillis();
            List<Event> events = getEvents(timeInMillis, i);


            calendarView.addEvents(events);
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

    }

    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.RED,timeInMillis, "Event 1"));
        } else if (day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.RED, timeInMillis, "Event 1"),
                    new Event(Color.RED, timeInMillis, "Event 2"));
        } else {
            return Arrays.asList(
                    new Event(Color.RED, timeInMillis, "Event 1"),
                    new Event(Color.RED, timeInMillis, "Event 2"),
                    new Event(Color.RED, timeInMillis, "Event 3"));
        }
    }

}
