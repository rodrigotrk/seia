package br.gov.ba.seia.dao;

import javax.inject.Inject;


public class ProhidrosDAOImpl {
	
	@Inject
	private IDAO<Double> dao;
	
	public Double getIBGEMunicipio(Double longitude, Double latitude) {
		String codIbge = dao.executarFuncaoNativeQuery(new StringBuilder("select sp_get_municipio("+longitude+","+latitude+");").toString(),null);
		
		if(codIbge!=null && !codIbge.isEmpty()){
			return Double.parseDouble(codIbge);
		}

		return new Double(0);
	}
	
	public Double getIBGEMunicipio(Double longitude, Double latitude, String srid) {
		String codIbge = dao.executarFuncaoNativeQuery(new StringBuilder("select sp_get_municipio_with_srid("+longitude+","+latitude+","+srid+");").toString(),null);
		
		if(codIbge!=null && !codIbge.isEmpty()){
			return Double.parseDouble(codIbge);
		}

		return new Double(0);
	}

}