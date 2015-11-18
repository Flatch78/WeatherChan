package com.guillaumek.weatherchannel.Network.Object;

/**
 * Created by flatch on 09/11/15.
 */
public class CityInfoObject {

    int id;
    String name;
    double longitude;
    double latitude;
    String country;
    int favorite;
    String created_at;

    // constructors
    public CityInfoObject() {
    }

    public CityInfoObject(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public CityInfoObject(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public CityInfoObject(int id, String name, double longitude, double latitude, String country) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLatitude(double lat) {
        this.latitude = lat;
    }
    public void setLongitude(double lon) {
        this.longitude = lon;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public String getCountry() {
        return country;
    }
    public int getFavorite() {
        return favorite;
    }
    public String getCreated_at() {
        return created_at;
    }
}
