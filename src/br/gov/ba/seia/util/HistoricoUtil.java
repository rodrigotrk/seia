package br.gov.ba.seia.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import org.apache.log4j.Level;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.dto.HistoricoDTO;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

public class HistoricoUtil {

	public static AbstractEntity construtorRecursivo(AbstractEntity original){
			
		try {
			AbstractEntity novo = (AbstractEntity) original.getClass().getConstructor().newInstance();
			
			for (Field f: original.getClass().getDeclaredFields()) {
				if(f.getAnnotation(Historico.class)!=null){
					f.setAccessible(true);
					
					if(f.get(original) != null){
						
						if(isObjetoFilho(f, novo)){
							f.set(novo , construtorRecursivo((AbstractEntity) f.get(original)));
						}
						else if(isCollection(f)){
							f.set(novo, new ArrayList<AbstractEntity>());
						}
						else{
							if(f.get(original) != null){
								f.set(novo,getType(f.get(original)));
							}
						}
						
					}
				}
			}
			
			return novo;
		
		} catch (Exception e) {
			Log4jUtil.log(HistoricoUtil.class.getName(),Level.ERROR, e);
		} 
		
		throw new RuntimeException();
	}
	
	public static Object getType(Object object){
		if(object instanceof String){
			return "";
		}else if(object instanceof Boolean){	
			return false;
		}else{
			return null;
		}
	}
	
	public static String resolverNomeExterno(String nameMethod, Object o) {
		try {
			
			AbstractEntityHist a = ((AbstractEntityHist) (o.getClass().getMethod(nameMethod)).invoke(o));
			
			for(Field f: a.getClass().getDeclaredFields()){
				f.setAccessible(true);
				if(f.getAnnotation(Historico.class) != null  && !f.getAnnotation(Historico.class).nameKey().equals("")){
					return (String) f.get(a);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(HistoricoUtil.class.getName(),Level.ERROR, e);
		} 
		
		throw new RuntimeException();
	}
	
	public static AbstractEntity get(Integer chave, List<AbstractEntity> lista){
		for (AbstractEntity ae : lista) {
			AbstractEntityHist aeh = (AbstractEntityHist) ae;
			if(chave.equals(aeh.getIdeObjetoPai())){
				lista.remove(aeh);
				return aeh;
			}
		}
		return null;
	}
	
	
	
	public static boolean isObjetoFilho(Field f, Object um){
		return (f.getName().startsWith("ide") && (um instanceof AbstractEntity) ) ;
	}
	
	public static boolean isObjetoFilho(Field f, Object um, Object dois){
		return (f.getName().startsWith("ide") && (um instanceof AbstractEntity || dois instanceof AbstractEntity)) ;
	}
	
	public static boolean isCollection(Field f){
		return (f.getType() == List.class) || (f.getType() == Collection.class);
	}
	
	public static boolean isCampoHistorico(Field f){
		return  (f.getAnnotation(Historico.class)!=null);
	}

	public static boolean isValid(Field f){
		return (f.getAnnotation(Transient.class)==null);
	}
	
	public static boolean isPossuiDiferenca(Object f1, Object f2){
		if(f1 == null && f2 == null){ return false; }
		if((f1 == null && f2 != null) || f1 != null && f2 == null){	return true; }
		if(!f1.equals(f2)){	return true;}
		return false;
	}

	public static HistoricoDTO getSazonalidade(List<HistoricoDTO> diferencas) {
		
		for (HistoricoDTO historicoDTO : diferencas) {
			if(historicoDTO.getNome().equals("Sazonalidade")){
				return  historicoDTO;
			}
		}
		
		HistoricoDTO historico = new HistoricoDTO();
		historico.setNome("Sazonalidade");
		historico.setHistoricoObjeto(new ArrayList<HistoricoDTO>());
		diferencas.add(historico);
		
		return historico;
	}

	public static HistoricoDTO getSubTable(String subTableName, List<HistoricoDTO> diferencas) {

		for (HistoricoDTO historicoDTO : diferencas) {
			if(historicoDTO.getNome().equals(subTableName)){
				return  historicoDTO;
			}
		}

		HistoricoDTO historico = new HistoricoDTO();
		historico.setNome(subTableName);
		historico.setHistoricoObjeto(new ArrayList<HistoricoDTO>());
		diferencas.add(historico);
		
		return historico;
	}
	
	public static String isSubTable(AbstractEntity ae) {
		for (Field f: ae.getClass().getDeclaredFields()) {
			if(f.getAnnotation(Historico.class)!=null && f.getAnnotation(Historico.class).subTable()){
				return f.getAnnotation(Historico.class).subTableName();
			}
		}
		
		return "";
	}
}