/*
 * Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.example.weatherforecast;

import java.util.ArrayList;

import jp.library.weatherforecast.WeatherForecast;
import jp.library.weatherforecast.WeatherForecast.*;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {
	WeatherForecast mWeatherForecast = new WeatherForecast();
	int mPosition = -1;
	int mId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		int[] locationIDs = mWeatherForecast.getLocationIDs();
		for (int id: locationIDs) {
			adapter.add(mWeatherForecast.getLocationName(id));
		}
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				mPosition = spinner.getSelectedItemPosition();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		mWeatherForecast.setOnPostExecute(new OnPostExecute(){
			@Override
			public void onPostExecute(){
				TextView textView1 = (TextView) findViewById(R.id.textView1);
				String text;
				text = mWeatherForecast.getPublicDate(MainActivity.this, mId).toString() + "\n";
				text += mWeatherForecast.getLastUpdate(MainActivity.this, mId).toString() + "\n";
				ArrayList<OneDayForecast> oneDayForecasts = mWeatherForecast.getOneDayForecast(MainActivity.this, mId);
				ArrayList<WeeklyForecast> weeklyForecasts = mWeatherForecast.getWeeklyForecast(MainActivity.this, mId);
				for(OneDayForecast oneDayForecast: oneDayForecasts){
					text += oneDayForecast.Hour + oneDayForecast.Forecast + oneDayForecast.Temp + "\n";					
				}
				for(WeeklyForecast weeklyForecast: weeklyForecasts){
					text += weeklyForecast.Date + weeklyForecast.Forecast + weeklyForecast.MaxTemp + weeklyForecast.MinTemp;					
					for(String probability: weeklyForecast.Probabilitys){
						text += probability;					
					}
					text += "\n";					
				}
				textView1.setText(text);
				ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
				int resId = mWeatherForecast.getBitmapResource(weeklyForecasts.get(0).Forecast);
				imageView1.setImageResource(resId);
			}
		});
	}

	public void onClickOKButton(View view) {
		mId = mWeatherForecast.getLocationIDs()[mPosition];
		mWeatherForecast.getForecast(this, mId);
	}
}
