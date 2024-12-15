package br.gov.ba.seia.util;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.primefaces.context.RequestContext;

public class ValidarCoordenada {
	
	public static final boolean LATITUDE = true;
	public static final boolean LONGITUDE = false;
	
	public static void validarCampo(String idDoCampo, String valorDoCampo, Integer MIN_VALOR_COORDENADA,Integer MAX_VALOR_COORDENADA, boolean tipoCoodenada) {
		
		if(valorDoCampo.equals("")){
			atualizarValorDoCampo(idDoCampo, 0.0, MIN_VALOR_COORDENADA, MAX_VALOR_COORDENADA);
		}
		else if(Util.canConvertToDouble(valorDoCampo)){
			
			Double coordenada = Double.parseDouble(valorDoCampo);
			
			if (coordenada > MAX_VALOR_COORDENADA|| coordenada < MIN_VALOR_COORDENADA) {
				
				FacesMessage message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				
				message.setSummary("A "+ (tipoCoodenada==LATITUDE ? "latitude" : "longitude") +" deve ser maior que "+MIN_VALOR_COORDENADA+", e menor que "+ MAX_VALOR_COORDENADA);
				
				atualizarValorDoCampo(idDoCampo,coordenada, MIN_VALOR_COORDENADA, MAX_VALOR_COORDENADA);
				
				
				throw new ValidatorException(message);
			}
		}
		else{
			atualizarValorDoCampo(idDoCampo,null,MIN_VALOR_COORDENADA,null);
		}
	}
	
	private static void atualizarValorDoCampo(String idDoCampo, Double valorCoordenada, Integer MIN_VALOR_COORDENADA,Integer MAX_VALOR_COORDENADA) {
		
		String novoValor = retornarNovoValor(valorCoordenada,MIN_VALOR_COORDENADA, MAX_VALOR_COORDENADA);
		RequestContext.getCurrentInstance().execute("$('#"+idDoCampo+"').val('"+novoValor+"');");
	}
	
	private static String retornarNovoValor(Double valorCoordenada, Integer MIN_VALOR_COORDENADA,Integer MAX_VALOR_COORDENADA) {
		
		if(valorCoordenada==null){
			
			String retorno = MIN_VALOR_COORDENADA+",000000";
			return retorno.length()==9 ? retorno : "0"+retorno;
		}
		else if(valorCoordenada>MAX_VALOR_COORDENADA){
			
			if(Util.isInteiro(valorCoordenada)){
				return MAX_VALOR_COORDENADA+"";
			}
			return MAX_VALOR_COORDENADA+",000000";
		}
		else{
			
			if(Util.isInteiro(valorCoordenada)){
				return MIN_VALOR_COORDENADA+"";
			}
			
			String retorno = MIN_VALOR_COORDENADA+",000000";
			
			return retorno.length()==9 ? retorno : "0"+retorno;
		}
	}

}
