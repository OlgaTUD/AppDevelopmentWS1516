package com.rn.myplaces.myplaces.weather;

import java.io.Serializable;

/**
 * Created by Uto4ko on 23.01.2016.
 */
public class GooglePlace  implements Serializable {

    private String placeid;

    private String opennow;

    public String getNow(){return opennow;}
    public void setNow(String now){opennow = now;}

    public String getPlaceId(){return placeid;}
    public void setPlaceId(String id){placeid = id;}

}
