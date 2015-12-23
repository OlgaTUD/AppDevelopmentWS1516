package com.rn.myplaces.myplaces.com.rn.myplaces.database;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private long id;
    private String name;
    private String city;
    private String lat;
    private String longtitude;

    public Place(int id, String name, String city, String lat,String longtitude){
        this.id = id;
        this.name = name;
        this.city = city;
        this.lat = lat;
        this.longtitude = longtitude;
    }

    public Place(String name, String city,String lat,String longtitude){
        this.name = name;
        this.city = city;
        this.lat = lat;
        this.longtitude = longtitude;
    }

    // Empty constructor
    public Place(){
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public void  setLongtitude(String longtitude){
        this.longtitude = longtitude;}

    public String getLat(){
        return lat;
    }

    public String getLong(){
        return longtitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String place) {
        this.name = place;
    }

    public String toString() {
        return name;
    }

}
