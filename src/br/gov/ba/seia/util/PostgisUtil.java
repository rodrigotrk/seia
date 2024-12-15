package br.gov.ba.seia.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class PostgisUtil {

	public static Point criarPonto(Double Coordinatex, Double Coordinatey){

		GeometryFactory gf = new GeometryFactory();
		Coordinate coordinate = new Coordinate();
		coordinate.x = Coordinatex;
		coordinate.y = Coordinatey;

		return gf.createPoint(coordinate);
	}

	public static Double converterGrauEmDecimo(Integer grau, Integer min, Double seg){
		int sinal = 1;
		if (grau < 0) {
			grau = grau * -1;
			sinal = -1;
		}
		Double dec = (min + seg / 60.0) / 60.0;
		return (grau + dec) * sinal;
	}
	
	
	public String getTratamentoPonto(Point ponto){
		String inicio="";
		String fim=".";
		
		if(!ponto.toString().contains(".") || ponto.toString().indexOf(".") == ponto.toString().lastIndexOf(".") ){
			if(ponto.toString().charAt(10) != '.'){
				inicio = ponto.toString().substring(0, 10);
				inicio += ".O ";
			}
			if(!fim.contains(".") || ponto.toString().lastIndexOf(".")==10 ){
				fim = ponto.toString().substring(ponto.toString().lastIndexOf("-"), ponto.toString().indexOf(")")-1);
			    fim += ".0)";			
			}
		
		}
		
		return inicio+fim;
	}
	
	public static boolean validaVerticeUTM(String longitudeUTM, String latitudeUTM) {
		Boolean valido = true;
		if(latitudeUTM != null && !latitudeUTM.isEmpty() && latitudeUTM.length() < 7){
			JsfUtil.addErrorMessage("A latitude em UTM, na Bahia, não tem valores menores que 1.000.000.");
			valido = false;
		}
		if(longitudeUTM != null && !longitudeUTM.isEmpty() && longitudeUTM.length() < 6){
			JsfUtil.addErrorMessage("A longitude em UTM, na Bahia, não tem valores menores que 100.000.");
			valido = false;
		}
		
		return valido;
	}
	
	public static Double calculaFracaoGrauLatitude(String grausLatitude, String minutosLatitude, String segundosLatitude){
		Double fracaoGrauLatitude = null;
		if (grausLatitude != null) {
			Integer grau = (!grausLatitude.isEmpty() ? Integer.parseInt(grausLatitude) : 0);
			Integer min = (minutosLatitude != null && !minutosLatitude.isEmpty() ? Integer.parseInt(minutosLatitude) : 0);
			Double seg = (segundosLatitude != null && !segundosLatitude.isEmpty() ? Double.parseDouble(segundosLatitude) : 0d);
			fracaoGrauLatitude = Double.parseDouble(converterGrauEmDecimo(grau, min, seg).toString());			
		}
		return fracaoGrauLatitude;
	}
	
	public static Double calculaFracaoGrauLongitude(String grausLongitude, String minutosLongitude, String segundosLongitude){
		Double fracaoGrauLongitude = null;
		if (grausLongitude != null) {
			Integer grau = (!grausLongitude.isEmpty() ? Integer.parseInt(grausLongitude) : 0);
			Integer min = (minutosLongitude != null && !minutosLongitude.isEmpty() ? Integer.parseInt(minutosLongitude) : 0);
			Double seg = (segundosLongitude != null && !segundosLongitude.isEmpty() ? Double.parseDouble(segundosLongitude) : 0d);
			fracaoGrauLongitude = Double.parseDouble(converterGrauEmDecimo(grau, min, seg).toString());
		}
		return fracaoGrauLongitude;
	}	
}
