package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import br.gov.ba.seia.util.GeoUtil;

public class SemiCoordenadaGeografica implements Serializable, Comparable<SemiCoordenadaGeografica>{
	private static final long serialVersionUID = 1L;

	private BigDecimal grau;
	private BigDecimal minuto;
	private BigDecimal segundo;

	public SemiCoordenadaGeografica() {
		this.grau = new BigDecimal(0);
		this.minuto = new BigDecimal(0);
		this.segundo = new BigDecimal(0);
	}

	public BigDecimal getGrau() {
		return grau;
	}

	public void setGrau(BigDecimal grau) {
		this.grau = grau;
	}

	public BigDecimal getMinuto() {
		return minuto;
	}

	public void setMinuto(BigDecimal minuto) {
		this.minuto = minuto;
	}

	public BigDecimal getSegundo() {
		return segundo;
	}

	public void setSegundo(BigDecimal segundo) {
		this.segundo = segundo;
	}

	public String getAsGD(){
		return GeoUtil.converterGMSParaDecimal(this);
	}
	
	public String getAsGMS(){
		return grau + "º"+ minuto + "'" + segundo + "''";
	}
	
	@Override
	public String toString() {
		return this.grau + "°" + this.minuto + "'" + this.segundo + "''";
	}

	@Override
	public int compareTo(SemiCoordenadaGeografica coordenada) {
		
		if(coordenada.getGrau().compareTo(grau)==0 && 
		   coordenada.getMinuto().compareTo(minuto)==0 && 
		   coordenada.getSegundo().compareTo(segundo) == 0){
			return 0;
		}
		
		return 1;
	}
}