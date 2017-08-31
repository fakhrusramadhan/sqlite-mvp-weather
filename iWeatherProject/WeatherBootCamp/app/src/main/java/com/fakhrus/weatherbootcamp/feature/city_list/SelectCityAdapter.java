package com.fakhrus.weatherbootcamp.feature.city_list;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fakhrus.weatherbootcamp.feature.main.MainActivity;
import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.SQLiteHandler;
import com.fakhrus.weatherbootcamp.utils.SharedPref;
import com.fakhrus.weatherbootcamp.model.ItemOfWeatherModel;

import java.util.List;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.SelectCityViewHolder> {

    Activity activity;
    List<ItemOfWeatherModel> itemWeather;
//    String temperatureSymbol;

    public SelectCityAdapter(Activity activity, List<ItemOfWeatherModel> itemWeather) {
        this.activity = activity;
        this.itemWeather = itemWeather;
//        this.temperatureSymbol = temperatureSymbol;
    }

    @Override
    public SelectCityAdapter.SelectCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_city, parent, false);

        SelectCityViewHolder selectCityViewHolder = new SelectCityViewHolder(view);

        return selectCityViewHolder;
    }

    @Override
    public void onBindViewHolder(SelectCityAdapter.SelectCityViewHolder holder, final int position) {

        holder.bindView(itemWeather.get(position).cityName, itemWeather.get(position).getTemperature(),
                        itemWeather.get(position).getWeatherType(), itemWeather.get(position).getWeatherDescription());

        holder.rl_itemWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPref.remove("city_name");
                SharedPref.remove("gps_latitude");
                SharedPref.remove("gps_longitude");
                SharedPref.saveString("city_name", itemWeather.get(position).getCityName());

                Intent toMainActivity = new Intent(activity, MainActivity.class);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                activity.startActivity(toMainActivity);

                activity.finish();


            }
        });

        holder.rl_itemWeather.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Hapus Cuaca");
                builder.setMessage("Apakah anda ingin menghapus cuaca ini ?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        SQLiteHandler db = new SQLiteHandler(activity);
                        db.deleteWeather(position+1);

                        dialog.dismiss();

                        Intent refreshListCity = new Intent(activity, SelectCityActivity.class);
                        refreshListCity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(refreshListCity);
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                });

                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemWeather.size();
    }

    public class SelectCityViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_cityName, tv_temperature, tv_weatherDescription;
        private final ImageView iv_weatherIcon;
        private final ImageView iv_blurBackground;
        private final RelativeLayout rl_itemWeather;

        public SelectCityViewHolder(View itemView) {
            super(itemView);

            tv_cityName = (TextView) itemView.findViewById(R.id.city_itemListCity);
            tv_temperature = (TextView) itemView.findViewById(R.id.tv_temperature_itemListCity);
            tv_weatherDescription = (TextView) itemView.findViewById(R.id.tv_weatherDescription_itemListCity);
            iv_weatherIcon = (ImageView) itemView.findViewById(R.id.iv_wheather_icon_itemListCity);
            iv_blurBackground = (ImageView) itemView.findViewById(R.id.iv_blur_background_itemListCity);
            rl_itemWeather = (RelativeLayout) itemView.findViewById(R.id.rl_itemWeather_listCity);
        }

        public void bindView(String cityName, String temperature, String weatherType, String weatherDescription){

            int weatherIconDrawable;
            int blurBackground;

            if (weatherType.equals("Clear")){
                weatherIconDrawable = R.drawable.ic_sunny;
                blurBackground = R.drawable.sunny;
            }else if (weatherType.equals("Rain")){
                weatherIconDrawable = R.drawable.ic_rainy;
                blurBackground = R.drawable.rainy;
            }else if (weatherType.equals("Clouds")) {
                weatherIconDrawable = R.drawable.ic_cloudy;
                blurBackground = R.drawable.clouds;
            }else if (weatherType.equals("Mist") || weatherType.equals("Haze")){
                weatherIconDrawable = R.drawable.ic_fog;
                blurBackground = R.drawable.fog;
            }else {
                weatherIconDrawable = R.drawable.ic_cloudy_sunny;
                blurBackground = R.drawable.other;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), blurBackground);
            Bitmap blurredBitmap = BlurBuilder.blur(activity, bitmap);
            iv_blurBackground.setImageBitmap(blurredBitmap);

            tv_cityName.setText(cityName);
            tv_temperature.setText(temperature);
            tv_weatherDescription.setText(weatherDescription);
            iv_weatherIcon.setImageResource(weatherIconDrawable);

        }
    }
}
