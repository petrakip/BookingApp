package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gr.opa.booking.common.models.BookingStatisticsCriteria;

public class StatisticsActivity extends AppCompatActivity {

    EditText from;
    EditText to;
    Button statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        statistics = findViewById(R.id.statisticsButton);
        statistics.setOnClickListener(view -> {
            BookingStatisticsCriteria criteria = new BookingStatisticsCriteria();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

            from = findViewById(R.id.fromDateStatistics);
            String fromValue = from.getText().toString();
            if(!fromValue.isEmpty()){
                try {
                    criteria.setBookingPeriodFrom(dateFormat.parse(fromValue));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            to = findViewById(R.id.toDateStatistics);
            String toValue = to.getText().toString();
            if(!toValue.isEmpty()){
                try {
                    criteria.setBookingPeriodTo(dateFormat.parse(toValue));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            Intent nextStatisticResultsActivity =
                    new Intent(getApplicationContext(), StatisticResults.class);
            nextStatisticResultsActivity.putExtra("criteria", criteria);
            startActivity(nextStatisticResultsActivity);

        });


    }
}