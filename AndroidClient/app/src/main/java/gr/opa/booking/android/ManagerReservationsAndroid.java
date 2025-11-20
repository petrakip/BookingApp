package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import gr.opa.booking.common.models.RoomReservationsCriteria;

public class ManagerReservationsAndroid extends AppCompatActivity {
    Button reservationSearch;
    EditText manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reservations_android);

        reservationSearch = findViewById(R.id.reservationBtn);
        reservationSearch.setOnClickListener(view -> {
            manager = findViewById(R.id.managerNameR);
            String name = manager.getText().toString();
            RoomReservationsCriteria criteria = new RoomReservationsCriteria();
            criteria.setManagerName(name);
            Log.v("ManagerReservationsAndroid: ", name);

            Intent nextReservationResultsActivity = new Intent(getApplicationContext(), RoomReservationsResultsAndroid.class);
            nextReservationResultsActivity.putExtra("reservations", criteria);
            startActivity(nextReservationResultsActivity);
        });
    }
}