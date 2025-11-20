package gr.opa.booking.android;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerMenu extends AppCompatActivity {

    ImageView managerImage;
    Button addRoomBtn;
    Button statisticsBtn;
    Button reservationsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        managerImage = findViewById(R.id.managerImage);
        managerImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.manager_logo));

        addRoomBtn = findViewById(R.id.addRoomButton);
        addRoomBtn.setOnClickListener(view -> {
            ((ClientApp) getApplicationContext()).getClient().sendAddRoomsRequest();
            Intent nextCompleteActivity =
                    new Intent(getApplicationContext(), AddingRoomCompleted.class);
            startActivity(nextCompleteActivity);
        });

        statisticsBtn = findViewById(R.id.statisticsButton);
        statisticsBtn.setOnClickListener(view -> {
            Intent nextStatisticActivity =
                    new Intent(getApplicationContext(), StatisticsActivity.class);
            startActivity(nextStatisticActivity);
        });

        reservationsBtn = findViewById(R.id.reservationButton);
        reservationsBtn.setOnClickListener(view -> {
            Intent nextReservationActivity =
                    new Intent(getApplicationContext(), ManagerReservationsAndroid.class);
            startActivity(nextReservationActivity);
        });
    }
}