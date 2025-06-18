package br.gov.ba.seia.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.SemiCoordenadaGeografica;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;

/**
 *  @author Alexandre Queiroz
 *
 **/
public class GeoUtil {
	public static CoordenadaGeografica converterPointParaCoordenadaGeograficaSirgas(String coordGeoNumerica) {
		coordGeoNumerica = coordGeoNumerica.replaceAll("POINT ", "").replace("(","").replace(")", "");
		return new CoordenadaGeografica(decimalParaGMS(coordGeoNumerica.split(" ")[0]),decimalParaGMS(coordGeoNumerica.split(" ")[1]));
	}
	
	public static CoordenadaGeografica converterPointParaCoordenadaGeografica(String coordGeoNumerica) {
		coordGeoNumerica = coordGeoNumerica.replaceAll("POINT ", "").replace("(","").replace("-", "").replace(")", "");
		coordGeoNumerica = coordGeoNumerica.replaceAll("POINT", "");
		return new CoordenadaGeografica(decimalParaGMS(coordGeoNumerica.split(" ")[0]),decimalParaGMS(coordGeoNumerica.split(" ")[1]));
	}
	
   /** @author Alexandre Queiroz
   	*
   	*	Para converter do formatro GMS para Decimais, deve-se usar a seguinte formula:
	*
	*	decimais = graus +(minutos/60) + (segundos/3600)
	*
	*	Tomando-se o exemplo (12º 57' 18,490"), temos:
	*	
	*	Decimais = 12 + (57/60) + (18,490/3600)  
	*	Decimais = 12 + 0,95 +  0,005136111
	*	Decimais = 12,955136111
	*
	*	Convecionamente, no Seia Usa-se sempre seis casas decimais, então o resultado será: 
	*
	*	Decimais = [12,955136]
	*
	**/
	public static String converterGMSParaDecimal(SemiCoordenadaGeografica coordenada) {
		BigDecimal grau = coordenada.getGrau();
		BigDecimal minuto = coordenada.getMinuto();
		BigDecimal segundo = coordenada.getSegundo();
		BigDecimal decimal = BigDecimal.ZERO; 
		
		BigDecimal sessenta = new BigDecimal(60);
		
		minuto = minuto.divide(sessenta,6, RoundingMode.HALF_UP);
		segundo = segundo.divide(sessenta.multiply(sessenta), 6, RoundingMode.HALF_UP);
		
		decimal = decimal.add(grau).add(minuto).add(segundo);
		
		if(decimal.compareTo(BigDecimal.ZERO)>0){
			decimal = decimal.multiply(new BigDecimal(-1));
		}

		return new DecimalFormat("#0.000000").format(BigDecimalUtil.truncar(decimal, 6)).replace(',', '.');
	}
	
   /** @author Alexandre Queiroz
   	*
   	*	Para converter do formatro Decimais para GMS, deve-se usar a seguinte formula:
	*
	*	Declarando Variaveis:
	*
	*	Considere o seguinte valor: [-12.955136], será dividida em duas partes Inteior[I] e Fracionario[F]
	*	onde [-12] será  a parte inteira, 
	*	onde [0.955136] será a parte fracionaria.
	*
	*	1º Graus:
	*	
	*	Então: 
	* 
	* 	[Gº] = [I]
	* 	[Gº] = [-12]
	* 
	*	2º Minutos:
	* 	
	* 	A parte fracionaria deve ser mutiplicada por 60, a parte inteira da mutiplicação serão os minutos:
	* 
	*  	[M'] = [F] * 60
	* 	[M'] = [0.955136] * 60
	* 	[M'] = 57,30816 	//TODA VIA LEMBRA-SE QUE SOMENTE A PARTE INTEIRA É USADA NOS MINUTOS!
	* 
	* 	[M'] = 57
	* 
	* 	3º Segundos:
	*
	*	A parte fracionaria do calculo dos minutos é mutiplicado por 60, onde consideram-se 3 casas decimais. 
	*
	*	[M',f] = 0,30816
	*
	*	[S"] = [M',f] * 60
	*	[S"] = 	0,30816 * 60
	*	[S"] = 	18,489
	*
	*	Resultando-se em :
	*
	*	Decimais = [Gº] [M'] [S"]
	*	Decimais = [-12º] [57'] [18,489"]
	*
	**/
	public static SemiCoordenadaGeografica decimalParaGMS(String decimal){
		SemiCoordenadaGeografica coodenada = new SemiCoordenadaGeografica();
		
		BigDecimal grau= BigDecimal.ZERO; 
		BigDecimal minuto = BigDecimal.ZERO; 
		BigDecimal segundo= BigDecimal.ZERO; 
		
		if (decimal.contains(".")) {
			grau = new BigDecimal(decimal.substring(0, decimal.indexOf(".")),MathContext.DECIMAL32);
		} else {
			grau = new BigDecimal(decimal,MathContext.DECIMAL32);
		}
		
		minuto = new BigDecimal(decimal).remainder(BigDecimal.ONE);
		minuto = minuto.multiply(new BigDecimal(60,MathContext.DECIMAL32));
		
		segundo = minuto.remainder(BigDecimal.ONE);
		segundo = segundo.multiply(new BigDecimal(60),MathContext.DECIMAL32).setScale(2,RoundingMode.HALF_UP);
		
		coodenada.setGrau(grau);
		coodenada.setMinuto(BigDecimalUtil.truncar(minuto, 0));
		coodenada.setSegundo(segundo);
		
		return coodenada;
	}
	
	public static SemiCoordenadaGeografica converterGMSParaDecimal(String gms) {
		SemiCoordenadaGeografica coordenada = new SemiCoordenadaGeografica();
		
		if(gms == null || gms.isEmpty()){
			throw new SeiaValidacaoRuntimeException("Coordenada Invalida.");
		}
		
		if(!gms.contains("º")){
			throw new SeiaValidacaoRuntimeException("Coordenada Invalida.");
		}else{
			coordenada.setGrau(new BigDecimal(gms.substring(0, gms.indexOf("º"))));
			coordenada.setMinuto(new BigDecimal(gms.substring(gms.indexOf("º")+1,gms.indexOf("'"))));
			coordenada.setSegundo(new BigDecimal(gms.substring(gms.indexOf("'")+1, gms.length()).replace("'", "")));
		}
		
		return coordenada;
	}
	
	public static SemiCoordenadaGeografica criarCoordenada(String graus, String minutos ,String segundos) {
		SemiCoordenadaGeografica coodenada = new SemiCoordenadaGeografica();
		
		coodenada.setGrau(graus != null && !graus.isEmpty() ? new BigDecimal(graus.replace(",", ".")) : new BigDecimal(0d));
		coodenada.setMinuto(minutos != null && !minutos.isEmpty() ? new BigDecimal(minutos.replace(",", ".")) : new BigDecimal(0d));
		coodenada.setSegundo(segundos != null && !segundos.isEmpty() ? new BigDecimal(segundos.replace(",", ".")) : new BigDecimal(0d));
		
		return coodenada;
	}
	
}