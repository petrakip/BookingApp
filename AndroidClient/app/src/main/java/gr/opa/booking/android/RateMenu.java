package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import gr.opa.booking.common.models.RoomRating;

public class RateMenu extends AppCompatActivity {
    RatingBar rating;
    EditText name;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_menu);

        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(v -> {
            RoomRating ratingModel = new RoomRating();

            rating = findViewById(R.id.ratingBar);
            ratingModel.setRating(rating.getRating());

            name = findViewById(R.id.roomName);
            ratingModel.setRoomName(name.getText().toString());

            ((ClientApp) getApplicationContext()).getClient().sendRate(ratingModel);
            startActivity(new Intent(getApplicationContext(), RateCompletion.class));
        });

    }
}