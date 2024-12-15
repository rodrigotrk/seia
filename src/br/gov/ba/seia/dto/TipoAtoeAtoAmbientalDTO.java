package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.TipoAto;

public class TipoAtoeAtoAmbientalDTO {
	
	private  Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	
	
	public String getNome(){
		
		if (object instanceof TipoAto){
			return ((TipoAto)object).getNomTipoAto();
		}else 
			return ((AtoAmbiental)object).getNomAtoAmbiental();
	}
	
	
	public Integer getId() {
		if (object instanceof TipoAto){
			return ((TipoAto)object).getIdeTipoAto();
		}else 
			return ((AtoAmbiental)object).getIdeAtoAmbiental();
	}
	

	
	
	
}
