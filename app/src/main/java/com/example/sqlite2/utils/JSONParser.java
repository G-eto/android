package com.example.sqlite2.utils;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    public WeatherInfo WeatherParse (String weatherString) {
        WeatherInfo wi = new WeatherInfo();
        try {
            JSONObject jo = new JSONObject(weatherString);
            JSONObject joWeather = jo.getJSONObject("weatherinfo");
            wi.setCity(joWeather.getString("city"));
            wi.setLowTemp(joWeather.getString("temp2"));
            wi.setHighTemp(joWeather.getString("temp1"));
            wi.setDescription(joWeather.getString("weather"));
            wi.setPublishTime(joWeather.getString("ptime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wi;
    }
}