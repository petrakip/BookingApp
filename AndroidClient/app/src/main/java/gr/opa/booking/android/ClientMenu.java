package gr.opa.booking.android;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ClientMenu extends AppCompatActivity {
    ImageView clientImage;
    Button searchBtn;
    Button rateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);

        clientImage = findViewById(R.id.client_logo);
        clientImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.client_logo));

        searchBtn = findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(view -> {
            ((ClientApp) getApplicationContext()).getClient();
            Intent nextSearchActivity = new Intent(getApplicationContext(), SearchMenu.class);
            startActivity(nextSearchActivity);
        });

        rateBtn = findViewById(R.id.rateButton);
        rateBtn.setOnClickListener(view -> {
            ((ClientApp) getApplicationContext()).getClient();
            Intent nextRateActivity = new Intent(getApplicationContext(), RateMenu.class);
            startActivity(nextRateActivity);
        });
    }
}