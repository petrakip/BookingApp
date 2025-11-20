package gr.opa.booking.android;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    ImageView logoImage;
    Button managerBtn;
    Button clientBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        logoImage = findViewById(R.id.imageView);
        logoImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo));

        managerBtn = findViewById(R.id.managerButton);
        clientBtn = findViewById(R.id.clientButton);

        managerBtn.setOnClickListener(view -> {
            Intent nextActivityOfManager =
                    new Intent(getApplicationContext(), ManagerMenu.class);

            startActivity(nextActivityOfManager);
        });

        clientBtn.setOnClickListener(view -> {
            Intent nextActivityOfClient =
                    new Intent(getApplicationContext(), ClientMenu.class);

            startActivity(nextActivityOfClient);
        });
    }
}