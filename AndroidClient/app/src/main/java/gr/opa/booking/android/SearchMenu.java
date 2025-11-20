package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gr.opa.booking.common.models.Range;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.SearchFilters;

public class SearchMenu extends AppCompatActivity {
    EditText area;
    EditText fromDate;
    EditText toDate;
    EditText people;
    EditText fromPrice;
    EditText toPrice;
    EditText toStars;
    EditText fromStars;
    Button searchBtn;
    SearchFilters searchFilters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        searchBtn = findViewById(R.id.searchFilterButton);
        searchBtn.setOnClickListener(view -> {
            searchFilters = new SearchFilters();

            area = findViewById(R.id.areaBlank);
            String areaValue = area.getText().toString();
            if(!areaValue.isEmpty()){
                searchFilters.setArea(areaValue);
                Log.v("Area: ",areaValue);
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
            Range<Date> dateFilter = new Range<>();

            try {
                fromDate = findViewById(R.id.fromDateBlank);
                String fromDateValue = fromDate.getText().toString();
                if (!fromDateValue.isEmpty()) {
                    dateFilter.setFrom(df.parse(fromDateValue));
                    Log.v("(Date) from: ",fromDate.getText().toString());
                }

                toDate = findViewById(R.id.toDateBlank);
                String toDateValue = toDate.getText().toString();
                if (!toDateValue.isEmpty()) {
                    dateFilter.setTo(df.parse(toDateValue));
                    searchFilters.setDateRange(dateFilter);
                    Log.v("(Date) to: ",toDate.getText().toString());
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            people = findViewById(R.id.peopleNumberBlank);
            String peopleValue = people.getText().toString();
            if(!peopleValue.isEmpty()) {
                searchFilters.setNumberOfCustomers(Integer.parseInt(peopleValue));
                Log.v("People number: ",peopleValue);
            }

            Range<BigDecimal> priceFilter = new Range<>();
            fromPrice = findViewById(R.id.fromPriceBlank);
            String fromPriceValue = fromPrice.getText().toString();
            if(!fromPriceValue.isEmpty()) {
                priceFilter.setFrom(BigDecimal.valueOf(Double.parseDouble(fromPriceValue)));
                Log.v("(Price) from: ", fromPriceValue);
            }

            toPrice = findViewById(R.id.toPriceBlank);
            String toPriceValue = toPrice.getText().toString();
            if(!toPriceValue.isEmpty()) {
                priceFilter.setTo(BigDecimal.valueOf(Double.parseDouble(toPriceValue)));
                searchFilters.setPriceRange(priceFilter);
                Log.v("(Price) to: ", toPriceValue);
            }

            Range<Float> starsFilter = new Range<>();
            fromStars = findViewById(R.id.fromRateBlank);
            String fromStarsValue = fromStars.getText().toString();
            if(!fromStarsValue.isEmpty()) {
                starsFilter.setFrom(Float.valueOf(fromStarsValue));
                Log.v("(Stars) from: ", fromStarsValue);
            }

            toStars = findViewById(R.id.toRateBlank);
            String toStarsValue = toStars.getText().toString();
            if(!toStarsValue.isEmpty()) {
                starsFilter.setTo(Float.valueOf(toStarsValue));
                searchFilters.setRatingRange(starsFilter);Log.v("(Stars) to: ", toStarsValue);
            }

            Log.v("search filters: ", searchFilters.toString());
            List<Room> results = new ArrayList<>();

            Intent nextSearchResultsActivity =
                    new Intent(getApplicationContext(), SearchResultsActivity.class);
            nextSearchResultsActivity.putExtra("searching",  searchFilters);
            startActivity(nextSearchResultsActivity);
        });


    }
}