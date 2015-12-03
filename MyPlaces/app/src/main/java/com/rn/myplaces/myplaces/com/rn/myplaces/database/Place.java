package com.rn.myplaces.myplaces.com.rn.myplaces.database;

public class Place {
    private long id;
    private String name;
    private String city;

    public Place(int id, String name, String city){
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Place(String name, String city){
        this.name = name;
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
