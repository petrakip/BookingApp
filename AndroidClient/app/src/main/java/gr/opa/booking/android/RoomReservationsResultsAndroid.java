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
import java.util.List;

import gr.opa.booking.common.models.Booking;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.RoomReservationsCriteria;

public class RoomReservationsResultsAndroid extends AppCompatActivity {
    MyBookingListAdapter adapter;
    private final List<Booking> bookings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_reservations_results_android);

        RoomReservationsCriteria criteria = (RoomReservationsCriteria) getIntent().
                getSerializableExtra("reservations");

        adapter = new MyBookingListAdapter(this, bookings);

        ListView list = findViewById(R.id.reservationList);

        list.post(() -> {
            list.setAdapter(adapter);
            ((ClientApp) getApplicationContext()).getClient().sendReservations(criteria,
                    bookings, adapter);
            Log.v("Manager Name: ", criteria.getManagerName().toString());
        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent backMainMenuActivity =
                    new Intent(getApplicationContext(), MainMenu.class);
            startActivity(backMainMenuActivity);

        });
    }
}