package br.gov.ba.seia.controller;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Level;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.dto.HistoricoDTO;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.enumerator.HistoricoEnum;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.HistoricoUtil;
import br.gov.ba.seia.util.Log4jUtil;

public class HistoricoController  {
	private List<HistoricoDTO> diferencas = new ArrayList<HistoricoDTO>();
	
	@SuppressWarnings("unchecked")
	public void comparar(AbstractEntity anterior, AbstractEntity atual){
		try{

			for(Field f1: anterior.getClass().getDeclaredFields()){
				f1.setAccessible(true);
				
				if(HistoricoUtil.isCampoHistorico(f1)){
					Field f2 = atual.getClass().getDeclaredField(f1.getName());
					f2.setAccessible(true);

					if(f1.get(anterior)!=null || f2.get(atual)!=null){
						
						if(HistoricoUtil.isObjetoFilho(f1, f1.get(anterior), f2.get(atual))){
							AbstractEntity a1 = (AbstractEntity) f1.get(anterior);
							AbstractEntity a2 = (AbstractEntity) f2.get(atual);
							
							if(a1 == null){	a1 = HistoricoUtil.construtorRecursivo(a2); }
							if(a2 == null){ a2 = HistoricoUtil.construtorRecursivo(a1); }
							
							comparar((AbstractEntity)f1.get(anterior), (AbstractEntity) f2.get(atual));
						}
						else if(HistoricoUtil.isCollection(f1)){
							comparar((List<AbstractEntity>) f1.get(anterior), (List<AbstractEntity>) f2.get(atual));
						}
						else{
							if(HistoricoUtil.isPossuiDiferenca(f1.get(anterior), f2.get(atual))){
								batizar(f1, anterior,atual , f1.get(anterior), f2.get(atual));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} 
	}
	
	public void comparar(List<AbstractEntity> anterior, List<AbstractEntity> atual){
		try{
		
			if(!validarListas(anterior, atual)){	return;	}
			if(anterior == null){	anterior = new ArrayList<AbstractEntity>(); }
			if(atual == null)	{	atual = new ArrayList<AbstractEntity>(); 	}
			
			for (AbstractEntity a1 : anterior) {
				AbstractEntity a2 = get(a1, atual);
				
				if(a2 == null){
					a2 = HistoricoUtil.construtorRecursivo(a1);
				}
				System.out.println(a2.getClass().getSimpleName());
				comparar(a1, a2);
			}
			
			for (AbstractEntity a2 : atual) {
				comparar(HistoricoUtil.construtorRecursivo(a2),a2);
			}
		
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	
	public void batizar(Field f, AbstractEntity anterior, AbstractEntity atual, Object o1, Object o2){
		
		String nome = resolverNome(f, anterior, atual);
		
		HistoricoDTO diferenca = new HistoricoDTO(nome, o2, o1, anterior.getClass().getSimpleName());
		
		if(anterior instanceof CerhVazaoSazonalidadeInterface){
			HistoricoDTO sazonalidade =  HistoricoUtil.getSazonalidade(diferencas);
			sazonalidade.getHistoricoObjeto().add(diferenca);
		}else{
			diferencas.add(diferenca);
		}
	}

	private String resolverNome(Field f, AbstractEntity anterior,AbstractEntity atual) {
		String nome = ""; 
		
		if(!f.getAnnotation(Historico.class).name().equals("")){ 
			nome = f.getAnnotation(Historico.class).name();
			
		}else if(!f.getAnnotation(Historico.class).nameMethod().equals("") ){ 
			nome = HistoricoUtil.resolverNomeExterno(f.getAnnotation(Historico.class).nameMethod(), anterior);
		}

		if(anterior instanceof CerhVazaoSazonalidadeInterface ){
			CerhVazaoSazonalidadeInterface sazonalidade;
			
			if(anterior.getId()!=null){
				sazonalidade =(CerhVazaoSazonalidadeInterface)  anterior;
			}
			else{
				sazonalidade =(CerhVazaoSazonalidadeInterface)  atual;
			}
			
			nome ="(" +MesEnum.getMesEnum(sazonalidade.getIdeMes().getId()).getNomMes() + ") " + nome;
		}
		return nome;
	}
	

	
	public AbstractEntity get(AbstractEntity chave, List<AbstractEntity> lista){
		Object chaveMestra = getChave(chave, HistoricoEnum.ANTERIOR);
		Class<?> clazz = null;
		try{
			
			for (AbstractEntity a : lista) {
				clazz = a.getClass();
				if(getChave(a, HistoricoEnum.ATUAL).equals(chaveMestra)){
					lista.remove(a);
					return a;
				}
			}	
		}catch(Exception e){
			System.out.println("Sem objeto pai: " + clazz.getSimpleName());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return null;
	}
	
	private Object getChave(AbstractEntity a, HistoricoEnum historico){
		try{
			
			for(Field f: a.getClass().getDeclaredFields()){
				if(f.getAnnotation(Historico.class) != null && f.getAnnotation(Historico.class).key() == true){
					f.setAccessible(true);
					return (Object) f.get(a);
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			
		try{
			if(HistoricoEnum.ANTERIOR == historico){
				return a.getId();
			}else if(HistoricoEnum.ATUAL == historico) {
				
				if(((AbstractEntityHist)a).getIdeObjetoPai()==null){
					((AbstractEntityHist)a).getIdeObjetoPai();
				}
				
				return ((AbstractEntityHist)a).getIdeObjetoPai();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			System.out.println("Provavelmente o ideObjetoPai não esta carregado");
		}
		
		return null;
	}
	
	private boolean validarListas(List<AbstractEntity> c1, List<AbstractEntity> c2){
		if(c1 == null && c2 == null)		{ return false; }
		if(c1.size() == 0 && c2.size() == 0){ return false; }
		
		return true;
	}
	
	public int getNumeroMaiorLista(List<AbstractEntity> c1, List<AbstractEntity> c2){
		int maiorLista = c1.size();
		
		if(c2.size() > maiorLista){
			maiorLista = c2.size();
		}
		
		return maiorLista;
	}
	
	public void ordernar(){
		Collections.sort(diferencas,new Comparator<HistoricoDTO>(){
			@Override
			public int compare(HistoricoDTO h1, HistoricoDTO h2) {
				return Collator.getInstance().compare(h1.getNome(), h2.getNome());
			}
		});
	}
	
	
	public List<HistoricoDTO> getDiferencas() {
		return diferencas;
	}
}
