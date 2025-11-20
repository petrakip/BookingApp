package gr.opa.booking.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

import gr.opa.booking.common.models.Booking;

public class MyBookingListAdapter extends ArrayAdapter<Booking> {
    private final Activity context;
    List<Booking> bookings;

   public MyBookingListAdapter(Activity context, List<Booking> results) {
       super(context, R.layout.reservation_item, results);
       this.context = context;
       this.bookings = results;
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyViewHolderB myViewHolderB;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

        if (convertView == null) {
            myViewHolderB = new MyViewHolderB();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.reservation_item, parent, false);
            myViewHolderB.roomName = convertView.findViewById(R.id.roomReservation);
            myViewHolderB.customer = convertView.findViewById(R.id.nameReservation);
            myViewHolderB.from = convertView.findViewById(R.id.fromReservation);
            myViewHolderB.to = convertView.findViewById(R.id.toReservation);
            myViewHolderB.people = convertView.findViewById(R.id.peopleReservation);

            convertView.setTag(myViewHolderB);
            myViewHolderB.roomName.setTag(bookings.get(position));


        } else {
            myViewHolderB = (MyViewHolderB) convertView.getTag();
        }

        myViewHolderB.roomName.setText("Room: " +bookings.get(position).getRoomName());
        myViewHolderB.customer.setText(bookings.get(position).getName());
        myViewHolderB.from.setText(df.format(bookings.get(position).getFrom()));
        myViewHolderB.to.setText(df.format(bookings.get(position).getTo()));
        myViewHolderB.people.setText(String.valueOf(bookings.get(position).getPeople()));


        return convertView;
    }
}

class MyViewHolderB {
    public TextView roomName;
    public TextView customer;
    public TextView from;
    public TextView to;
    public TextView people;

}

