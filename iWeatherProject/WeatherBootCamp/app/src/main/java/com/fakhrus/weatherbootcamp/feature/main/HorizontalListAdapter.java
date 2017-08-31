package com.fakhrus.weatherbootcamp.feature.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.model.WeatherFiveDaysModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.HorizontalViewHolder> {

    private List<WeatherFiveDaysModel.ListData> itemList;
    private Context context;
    private String temperatureSymbol;

    public HorizontalListAdapter(List<WeatherFiveDaysModel.ListData> itemList, Context context, String temperatureSymbol) {
        this.itemList = itemList;
        this.context = context;
        this.temperatureSymbol = temperatureSymbol;
    }

    @Override
    public HorizontalListAdapter.HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_horizontal, parent, false);

        HorizontalViewHolder holder = new HorizontalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalListAdapter.HorizontalViewHolder holder, int position) {

//        if (position <= 5){
//            holder.bindViews(itemList.get(position).getMain().getTemp(),
//                    itemList.get(position).getWeather().get(0).getMain());
//        }

        holder.bindViews(itemList.get(position).getMain().getTemp(),
                itemList.get(position).getWeather().get(0).getMain(),
                itemList.get(position).getDtTxt());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_iconWeather;
        private final TextView tv_temperature, tv_dateTime;

        public HorizontalViewHolder(View itemView) {
            super(itemView);

            iv_iconWeather = (ImageView) itemView.findViewById(R.id.iv_icon_weather_horizontalList);
            tv_temperature = (TextView) itemView.findViewById(R.id.tv_temperature_horizontalList);
            tv_dateTime = (TextView) itemView.findViewById(R.id.tv_dateTime_horizontalList);
        }

        public void bindViews(int temp, String main, String dtTxt) {

            int weatherIconDrawable;

            if (main.equals("Clear")){
                weatherIconDrawable = R.drawable.ic_sunny;
            }else if (main.equals("Rain")){
                weatherIconDrawable = R.drawable.ic_rainy;
            }else if (main.equals("Clouds")){
                weatherIconDrawable = R.drawable.ic_cloudy;
            }else if (main.equals("Haze") || main.equals("Mist")){
                weatherIconDrawable = R.drawable.ic_fog;
            }else {
                weatherIconDrawable = R.drawable.ic_cloudy_sunny;
            }

            Toast.makeText(context, "SAMPE SINI WEATHER "+main, Toast.LENGTH_LONG);
            iv_iconWeather.setImageResource(weatherIconDrawable);
            tv_temperature.setText(temp+temperatureSymbol);

            String time = null;

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = inputFormat.parse(dtTxt);
                SimpleDateFormat output = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));
                time = output.format(date);

            } catch (Exception e) {
                Log.e("kesalahan", e.getMessage());
            }

                tv_dateTime.setText(""+time);

        }
    }
}
