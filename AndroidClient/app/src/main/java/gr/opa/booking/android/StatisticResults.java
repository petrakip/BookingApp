package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import gr.opa.booking.common.models.BookingStatisticsCriteria;

public class StatisticResults extends AppCompatActivity {
    List<String> areaStat = new ArrayList<>();
    List<Integer> counterStat = new ArrayList<>();
    MyStatisticListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_results);

        BookingStatisticsCriteria criteria =
                (BookingStatisticsCriteria) getIntent().getSerializableExtra("criteria");

        adapter = new MyStatisticListAdapter(this, areaStat, counterStat);

        ListView list = findViewById(R.id.statisticsList);

        list.post(() -> {
            list.setAdapter(adapter);
            ((ClientApp) getApplicationContext()).getClient().sendStatistics(criteria, areaStat, counterStat, adapter);
            Log.v("(StatisticResults) criteria", criteria.toString());

        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent backMainMenuActivity =
                    new Intent(getApplicationContext(), MainMenu.class);
            startActivity(backMainMenuActivity);

        });


    }
}