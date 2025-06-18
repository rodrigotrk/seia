package br.gov.ba.seia.entity;

import java.io.Serializable;

import br.gov.ba.seia.util.GeoUtil;

public class CoordenadaGeografica implements Serializable, Comparable<CoordenadaGeografica>{

	private static final long serialVersionUID = 1L;

	private SemiCoordenadaGeografica latitude;
	private SemiCoordenadaGeografica longitude;
	
	public CoordenadaGeografica() {
	}
	
	public CoordenadaGeografica(SemiCoordenadaGeografica latitude, SemiCoordenadaGeografica longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public SemiCoordenadaGeografica getLatitude() {
		return latitude;
	}
	
	public void setLatitude(SemiCoordenadaGeografica latitude) {
		this.latitude = latitude;
	}
	
	public SemiCoordenadaGeografica getLongitude() {
		return longitude;
	}
	
	public void setLongitude(SemiCoordenadaGeografica longitude) {
		this.longitude = longitude;
	}

	public String getAsGD(){
		return latitude.getAsGD() +" "+ longitude.getAsGD();
	}
	
	public String getAsGMS(){
		return latitude.getAsGMS() +" "+ longitude.getAsGMS();
	}
	
	@Override
	public String toString() {
		return "GMS: "+ latitude.toString() +" "+ longitude.toString() + " GD" + GeoUtil.converterGMSParaDecimal(latitude) +" "+ GeoUtil.converterGMSParaDecimal(longitude);
	}

	@Override
	public int compareTo(CoordenadaGeografica CoordenadaGeografica) {
		if(CoordenadaGeografica.getLatitude().compareTo(latitude) ==0 && CoordenadaGeografica.getLongitude().compareTo(longitude) ==0 ){
			return 0;
		}
		return -1;
	}
	
}