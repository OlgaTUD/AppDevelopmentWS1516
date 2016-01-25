package com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Uto4ko on 23.01.2016.
 */
public class GooglePlace  implements Serializable {

    private String placeid;
    private String opennow;
    private ArrayList<String> types;

    public String getNow(){return opennow;}
    public void setNow(String now){opennow = now;}

    public String getPlaceId(){return placeid;}
    public void setPlaceId(String id){placeid = id;}

    public ArrayList<String> getTypes(){return types;}
    public void setTypes(ArrayList<String> array){
        types = array;
    }

}
