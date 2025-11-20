package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.SearchFilters;

public class SearchResultsActivity extends AppCompatActivity {
    private final ArrayList<Room> searchResults = new ArrayList<>();

    MyRoomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        SearchFilters searchFilters =
                (SearchFilters) getIntent().getSerializableExtra("searching");

        adapter = new MyRoomListAdapter(this, searchResults);

        Log.v("getView", "set list adapter in new thread");

        ListView list = findViewById(R.id.list);

        list.post(() -> {
            list.setAdapter(adapter);
            ((ClientApp) getApplicationContext()).getClient().executeSearch(searchFilters, searchResults, adapter);
            Log.v("(SearchResultsActivity) search filters: ", searchFilters.toString());
        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            TextView name = view.findViewById(R.id.roomNameEdit);
            Room room = (Room) name.getTag();
            Log.v("List item click", room.toString());
            Intent nextBookActivity =
                    new Intent(getApplicationContext(), BookActivity.class);
            nextBookActivity.putExtra("room", room);
            startActivity(nextBookActivity);

        });
    }
}