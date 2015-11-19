package com.rn.myplaces.myplaces.com.rn.myplaces.database;

public class Place {
    private long id;
    private String place;
    private String city;

    public Place(int id, String place, String city){
        this.id = id;
        this.place = place;
        this.city = city;
    }

    public Place(String place, String city){
        this.place = place;
        this.city = city;
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

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String toString() {
        return place;
    }

}
