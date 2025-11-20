package gr.opa.booking.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gr.opa.booking.common.models.Booking;
import gr.opa.booking.common.models.Room;

public class BookActivity extends AppCompatActivity implements SuccessOrFailureHandler {

    EditText name;
    EditText people;
    EditText from;
    EditText to;
    Button book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Room room = (Room) getIntent().getSerializableExtra("room");

        book = findViewById(R.id.bookButton);
        book.setOnClickListener(view -> {
            Booking b = new Booking();
            b.setRoomName(room.getRoomName());

            name = findViewById(R.id.bookName);
            String nameValue = name.getText().toString();
            if(!nameValue.isEmpty()){
                b.setName(nameValue);
                Log.v("Book Name: ", nameValue);
            }

            people = findViewById(R.id.numberOfPeopleBook);
            String peopleValue = people.getText().toString();
            if(!peopleValue.isEmpty()){
                b.setPeople(Integer.parseInt(peopleValue));
                Log.v("Book People: ", peopleValue);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

            from = findViewById(R.id.dateFromBook);
            String fromValue = from.getText().toString();
            if(!fromValue.isEmpty()){
                try {
                    b.setFrom(dateFormat.parse(fromValue));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            to = findViewById(R.id.dateToBook);
            String toValue = to.getText().toString();
            if(!toValue.isEmpty()){
                try {
                    b.setTo(dateFormat.parse(toValue));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            ((ClientApp) getApplicationContext()).getClient().sendBooking(b, this);
        });
    }

    public void onSuccess() {
        startActivity(new Intent(getApplicationContext(), SuccessfulReply.class));
    }

    public void onFailure() {
        startActivity(new Intent(getApplicationContext(), FailureReply.class));
    }
}