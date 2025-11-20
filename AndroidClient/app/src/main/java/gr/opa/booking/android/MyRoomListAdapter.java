package gr.opa.booking.android;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import gr.opa.booking.common.models.Room;

public class MyRoomListAdapter extends ArrayAdapter<Room> {
    private final Activity context;

    private List<Room> rooms;

    public MyRoomListAdapter(Activity context, List<Room> searchResults) {
        super(context, R.layout.room_item, searchResults);
        this.context = context;
        this.rooms = searchResults;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.v("getView", "get view of list");

        MyViewHolder myViewHolder;

        if (convertView == null) {
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.room_item, parent, false);
            myViewHolder.name = convertView.findViewById(R.id.roomNameEdit);
            myViewHolder.area = convertView.findViewById(R.id.areaEdit);
            myViewHolder.people = convertView.findViewById(R.id.peopleEdit);
            myViewHolder.stars = convertView.findViewById(R.id.starsEdit);
            myViewHolder.reviews = convertView.findViewById(R.id.reviewsEdit);
            myViewHolder.price = convertView.findViewById(R.id.priceEdit);
            myViewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            myViewHolder.ratingView = convertView.findViewById(R.id.starsRoom);

            convertView.setTag(myViewHolder);
            myViewHolder.name.setTag(rooms.get(position));
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        Room room = (Room) myViewHolder.name.getTag();

        myViewHolder.name.setText(room.getRoomName());
        myViewHolder.area.setText(room.getArea());

        myViewHolder.people.setText(String.format(Locale.ENGLISH, "%d", room.getNumberOfPersons()));
        myViewHolder.stars.setText(String.format(Locale.ENGLISH, "%.02f", room.getRating()));
        myViewHolder.reviews.setText(String.format(Locale.ENGLISH, "%d", room.getNumberOfReviews()));
        myViewHolder.price.setText(String.format(Locale.ENGLISH, "%.02f", room.getPrice()));
        myViewHolder.ratingView.setText(Strings.repeat("⭐", (int) room.getRating()));

        try {
            InputStream is = context
                    .getApplicationContext()
                    .getResources()
                    .getAssets()
                    .open("images/" + room.getRoomImage());

            myViewHolder.image.setImageBitmap(BitmapFactory.decodeStream(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return convertView;
    }
}

class MyViewHolder {
    public TextView name;
    public TextView area;
    public TextView people;
    public TextView stars;
    public TextView reviews;
    public TextView price;
    public ImageView image;
    public TextView ratingView;
}