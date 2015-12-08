package com.templecs.ryding.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.templecs.ryding.model.Bus;
import com.templecs.ryding.model.BusStop;

public class ReadJSON {

    public ArrayList<Bus> readBusJSON(InputStream in) throws IOException {
        ArrayList<Bus> busList = new ArrayList<Bus>();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String tempStr = "";
        while((tempStr = bufferedReader.readLine()) != null){
            stringBuilder.append(tempStr);
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("buses");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject busObject = jsonArray.getJSONObject(i);
                Bus bus = new Bus();
                bus.setBusNumber(busObject.getInt("BusNumber"));
                bus.setBusID(busObject.getInt("BusID"));
                bus.setBusRoute(busObject.getString("BusRoute"));
                busList.add(bus);
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return busList;
    }

    public ArrayList<BusStop> readBusStopJSON(InputStream in) throws IOException {
        ArrayList<BusStop> busStopList = new ArrayList<BusStop>();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String tempStr = "";
        while((tempStr = bufferedReader.readLine()) != null){
            stringBuilder.append(tempStr);
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("points");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject busStopObject = jsonArray.getJSONObject(i);
                BusStop busStop = new BusStop();
                busStop.setBusNumber(busStopObject.getInt("BusNumber"));
                busStop.setBusID(busStopObject.getInt("BusID"));
                busStop.setName(busStopObject.getString("StopName"));
                busStop.setGeoLat(busStopObject.getDouble("Latitude"));
                busStop.setGeoLong(busStopObject.getDouble("Longitude"));
                busStopList.add(busStop);
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return busStopList;
    }

    public ArrayList<BusStop> readTravelTimeJSON(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String tempStr = "";
        ArrayList<BusStop> busStops = new ArrayList<BusStop>();
        while((tempStr = bufferedReader.readLine()) != null){
            stringBuilder.append(tempStr);
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("allstops");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject busStopObject = jsonArray.getJSONObject(i);
                BusStop busStop = new BusStop();
                busStop.setTime(busStopObject.getDouble("Time"));
                busStop.setName(busStopObject.getString("StopName"));
                busStop.setGeoLat(busStopObject.getDouble("Latitude"));
                busStop.setGeoLong(busStopObject.getDouble("Longitude"));
                busStop.setBusNumber(busStopObject.getInt("BusNumber"));
                busStop.setBusID(busStopObject.getInt("BusID"));
                busStops.add(busStop);
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return busStops;
    }

    public Bus readBusLocationJSON(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String tempStr = "";
        Bus bus = new Bus();
        while((tempStr = bufferedReader.readLine()) != null){
            stringBuilder.append(tempStr);
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            bus.setGeoLat(jsonObject.getDouble("Latitude"));
            bus.setGeoLong(jsonObject.getDouble("Longitude"));
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return bus;
    }

    public String readBusLocationUpdateJSON(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String tempStr = "";
        while((tempStr = bufferedReader.readLine()) != null){
            stringBuilder.append(tempStr);
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            tempStr = jsonObject.getString("status");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return tempStr;
    }
}