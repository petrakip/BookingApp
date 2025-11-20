package gr.opa.booking.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyStatisticListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> areaStat;
    List<Integer> counterStat;
    public MyStatisticListAdapter(Activity context, List<String> areaStat, List<Integer> counterStat) {
        super(context, R.layout.statistic_item, areaStat);
        this.context = context;
        this.areaStat = areaStat;
        this.counterStat = counterStat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyViewHolderS myViewHolderS;

        if (convertView == null) {
            myViewHolderS = new MyViewHolderS();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.statistic_item, parent, false);
            myViewHolderS.areaName = convertView.findViewById(R.id.areaName);
            myViewHolderS.counter = convertView.findViewById(R.id.bookCounter);

            convertView.setTag(myViewHolderS);
            myViewHolderS.areaName.setTag(areaStat.get(position));
            myViewHolderS.counter.setTag(counterStat.get(position));

        } else {
            myViewHolderS = (MyViewHolderS) convertView.getTag();
        }

        myViewHolderS.areaName.setText(areaStat.get(position));
        myViewHolderS.counter.setText(String.valueOf(counterStat.get(position)));

        return convertView;
    }
}

class MyViewHolderS {
    public TextView areaName;
    public TextView counter;


}