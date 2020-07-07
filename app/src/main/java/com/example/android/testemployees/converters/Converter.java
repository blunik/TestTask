package com.example.android.testemployees.converters;

import androidx.room.TypeConverter;

import com.example.android.testemployees.pojo.Speciality;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    @TypeConverter
    public String listSpecialityToString(List<Speciality> specialities) {
        return  new Gson().toJson(specialities);
    }

    @TypeConverter
    public List<Speciality> stringToListSpeciality(String specialitiesAsString){
        Gson gson = new Gson();
        ArrayList objects = gson.fromJson(specialitiesAsString, ArrayList.class);
        ArrayList<Speciality> specialities = new ArrayList<>();
        for (Object o: objects){
            specialities.add(gson.fromJson(o.toString(), Speciality.class));
        }
        return specialities;
    }
}
