package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CalendarioDAOImpl;

import org.joda.time.DateTime;

import br.gov.ba.seia.entity.Calendario;
import br.gov.ba.seia.enumerator.DiaEnum;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CalendarioService {

	@Inject
	private CalendarioDAOImpl calendarioDAOImpl;
	
	private List<Calendario>listaFeriados;
	
	
	public CalendarioService(){
		listaFeriados = new ArrayList<Calendario>();
		listarFeriados();
	}
	
		
	
	public void listarFeriados(){
		Calendario c = new Calendario();
		c.setDtcFeriado(new Date ());
		listaFeriados.add(c);
		
	}
	
	public Boolean isDiaUtil(Date date) {
		// Essa função deve cachear ao menos os próximos 10 feriados/pt.Facultativos
		if(isFimdeSemana(date)){
			return false;
		}
		if(isFeriado(date)){
			return false;
		}
		
		return true;
	}
	
	
	public Boolean isFimdeSemana(Date date){
		DateTime dt = getDateTime(date);
		if(dt.getDayOfWeek() == DiaEnum.SABADO.getValue() || dt.getDayOfWeek() == DiaEnum.DOMINGO.getValue()){
			return true;
		}
		else { return false; }
	}
	
	public Boolean isFeriado(Date date) {
		return !Util.isNullOuVazio(calendarioDAOImpl.obterFeriadoPorData(date));
	}
	
	public Date proximoDiaUtilApartirHoje() {
		return proximoDiaUtilApartir(new Date());
	}
	
	public Date proximoDiaUtilApartir(Date data) {
		DateTime dt = getDateTime(data);	
		int i = 1;
		for( i=0;;){
			if (isDiaUtil(dt.plusDays(i).toDate())){
			  break;
			}
			i++;
		}
		
		return dt.plusDays(i).toDate();
	}
	
	
	private DateTime getDateTime(Date date){
		return new DateTime(date);
	}
	
	private Boolean assertDateEquals(Date d1,Date d2){
		DateTime dt1 = getDateTime(d1);
		DateTime dt2 = getDateTime(d2);
		
		if (dt1.getMonthOfYear() != dt2.getMonthOfYear()){
			return false;
		}
		
		if(dt1.getDayOfMonth() != dt2.getDayOfMonth()){
			return false;
		}
		
		if(dt1.getYear() != dt2.getYear()){
			return false;
		}
		
		
		return true;
	}

}
