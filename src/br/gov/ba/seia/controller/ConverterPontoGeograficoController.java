package br.gov.ba.seia.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;

import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.service.SistemaCoordenadaService;
import br.gov.ba.seia.util.GeoUtil;

public abstract class ConverterPontoGeograficoController {

	@EJB
	private SistemaCoordenadaService sistemaCoordenadaService;
	
	protected void converterPontoGeografico(Collection<LocalizacaoGeografica> listaLocalizacaoGeografica) {
		for(LocalizacaoGeografica lg :listaLocalizacaoGeografica) {
			if(!isCoordenadaUTM(lg)) {
				if(lg.getLatitudeInicial().contains("º")) {
					CoordenadaGeografica coordenadaGeografica = new CoordenadaGeografica(GeoUtil.converterGMSParaDecimal(lg.getLatitudeInicial()), GeoUtil.converterGMSParaDecimal(lg.getLongitudeInicial()));
					lg.setLatitudeInicial(coordenadaGeografica.getLatitude().getAsGD());
					lg.setLongitudeInicial(coordenadaGeografica.getLongitude().getAsGD());
				} 
				else {
					DadoGeografico dg = ((List<DadoGeografico>) lg.getDadoGeograficoCollection()).get(0);
					
					CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(dg.getCoordGeoNumerica());
					lg.setLatitudeInicial(coordenadaGeografica.getLatitude().getAsGMS());
					lg.setLongitudeInicial(coordenadaGeografica.getLongitude().getAsGMS());
				}
			}else {
				
				if(lg.getLatitudeInicial().contains("º")) {
					DadoGeografico dg = ((List<DadoGeografico>) lg.getDadoGeograficoCollection()).get(0);
					
					String sistemaEnum = verificarSistemaCoordenada(lg);
					dg.setCoordGeoNumerica(sistemaCoordenadaService.converterPoint(dg.getCoordGeoNumerica(),SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid(),sistemaEnum,true));
					
					String[] points = dg.getCoordGeoNumerica().replaceAll("POINT\\(|POINT \\(|\\) ?", "").trim().split(" ");
					lg.setLatitudeInicial(String.format("%."+(9-points[0].substring(0,points[0].indexOf(".")).length())+"f", Double.parseDouble(points[0])).replace(",","."));
					lg.setLongitudeInicial(String.format("%."+(9-points[1].substring(0,points[1].indexOf(".")).length())+"f", Double.parseDouble(points[1])).replace(",","."));
					
				} 
				else {
					DadoGeografico dg = ((List<DadoGeografico>) lg.getDadoGeograficoCollection()).get(0);
					
					String sistemaEnum = verificarSistemaCoordenada(lg);
					dg.setCoordGeoNumerica(sistemaCoordenadaService.converterPoint(dg.getCoordGeoNumerica(),sistemaEnum,SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid(),false));

					CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(dg.getCoordGeoNumerica());
					lg.setLatitudeInicial(coordenadaGeografica.getLatitude().getAsGMS());
					lg.setLongitudeInicial(coordenadaGeografica.getLongitude().getAsGMS());
				}
			}
		}
	}

	private String verificarSistemaCoordenada(LocalizacaoGeografica lg) {
		
		String sistemEnum = "";
		
		if(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId())) {
			sistemEnum = SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getSrid();
		}else if(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId())) {
			sistemEnum =  SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getSrid();
		}else if(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SAD69.getId())) {
			sistemEnum =  SistemaCoordenadaEnum.UTM_24_SAD69.getSrid();
		}else if(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SAD69.getId())) {
			sistemEnum =  SistemaCoordenadaEnum.UTM_23_SAD69.getSrid();
		}
		
		return sistemEnum;
	}
	
	private boolean isCoordenadaUTM(LocalizacaoGeografica lg) {
		return 
			Arrays.asList(
				SistemaCoordenadaEnum.UTM_23_SAD69.getId(),
				SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId(),
				SistemaCoordenadaEnum.UTM_24_SAD69.getId(),
				SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId()
			
		).contains(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada());
	}
	
}