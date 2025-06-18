package br.gov.ba.seia.entity;

import java.io.Serializable;


/**
 *
 * @author carlos.cruz
 */

public class GeoJsonSicar implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String geoJson;
    
    private Double geoArea;

    public GeoJsonSicar() {
    }

    public GeoJsonSicar(String geoJson, Double geoArea) {
    	this.geoJson = geoJson;
    	this.geoArea = geoArea;
    }

	public String getGeoJson() {
		return geoJson;
	}

	public void setGeoJson(String geoJson) {
		this.geoJson = geoJson;
	}

	public Double getGeoArea() {
		return geoArea;
	}

	public void setGeoArea(Double geoArea) {
		this.geoArea = geoArea;
	}

    
    
}
