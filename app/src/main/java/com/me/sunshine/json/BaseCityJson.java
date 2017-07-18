package com.me.sunshine.json;

import org.json.*;


public class BaseCityJson {
	
    private double id;
    private String country;
    private Coord coord;
    private String name;
    
    
	public BaseCityJson () {
		
	}	
        
    public BaseCityJson (JSONObject json) {
    
        this.id = json.optDouble("id");
        this.country = json.optString("country");
        this.coord = new Coord(json.optJSONObject("coord"));
        this.name = json.optString("name");

    }
    
    public double getId() {
        return this.id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Coord getCoord() {
        return this.coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    
}
