package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.util.Log4jUtil;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SistemaCoordenadaService {
	
	@Inject
	private IDAO<SistemaCoordenada> sistemaCoordenadaIdao;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<SistemaCoordenada> listar() {
		return sistemaCoordenadaIdao.listarTodos();
	}
	
	public String converterPoint(String pointUtm, String sridAtual, String sridFuturo, boolean isGrau){
		Object stringRetorno = null;
		 Map<String,Object> params;
		try {

			if(sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid()) ||
					sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getSrid()) ||
					sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getSrid())) {
				
			  params = new HashMap<String, Object>();
			  params.put("point", pointUtm);
			  
			  String array[] = pointUtm.split(" "); String point = array[0];
			  
			  String pointCompleto = inverterPointString(pointUtm, array, point);
			  
			  stringRetorno = sistemaCoordenadaIdao.
			  obterPorNativeQuery("select ST_AsText(ST_Transform(GeomFromText('"+pointCompleto+"',"+sridAtual+"),"+sridFuturo+"))", null);
				 
			}else {
				params = new HashMap<String, Object>();
				params.put("point", pointUtm);
			
				stringRetorno = 
						sistemaCoordenadaIdao.obterPorNativeQuery("select ST_AsText(ST_Transform(GeomFromText('"+pointUtm+"',"+sridAtual+"),"+sridFuturo+"))", null);
			}

		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if(sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid()) ||
				sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getSrid()) ||
				sridAtual.equalsIgnoreCase(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getSrid())) {
			if(isGrau) {
				String array[] = stringRetorno.toString().split(" ");
				String pointVolta = array[0];
				String pointCompletoVolta = inverterPointStringGrau(stringRetorno.toString(), array, pointVolta);
				return pointCompletoVolta;
			}else {
				String array[] = stringRetorno.toString().replace("POINT(", "POINT (").split(" ");
				String pointVolta = array[0];

				String pointCompletoVolta = inverterPointString(stringRetorno.toString(), array, pointVolta);
				return pointCompletoVolta;
			}
		}else {
			return stringRetorno.toString();
		}
		
		
	}

	private String inverterPointString(String pointUtm, String[] array, String point) {
		String numeroUm;
		String numeroDois;
		String primeiraParte;
		String segundaParte;
		String pointCompleto;
		if(point.equalsIgnoreCase("POINT")) {
			numeroUm = array[1];
			numeroDois = array[2];
			String array2[] = numeroUm.split("\\(");
			String array3[] = numeroDois.split("\\)");
			
			primeiraParte = point + " (" + array3[0].toString();
			segundaParte = array2[1].toString() + ")";
			pointCompleto = primeiraParte + " " + segundaParte;
		}else {
			pointCompleto = pointUtm;
		}
		return pointCompleto;
	}
	
	private String inverterPointStringGrau(String pointUtm, String[] array, String point) {
		String numeroUm;
		String numeroDois;
		String primeiraParte;
		String segundaParte;
		String pointCompleto;

		numeroUm = array[0];
		numeroDois = array[1];
		String array2[] = numeroUm.split("\\(");
		String array3[] = numeroDois.split("\\)");
		
		primeiraParte = array2[0] + " (" + array3[0].toString();
		segundaParte = array2[1].toString() + ")";
		pointCompleto = primeiraParte + " " + segundaParte;

			
		return pointCompleto;
	}
	
}
