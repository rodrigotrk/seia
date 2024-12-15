package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhHistoricoDTO;
import br.gov.ba.seia.dto.HistoricoDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.facade.CerhFacade;
import br.gov.ba.seia.util.HistoricoUtil;
import br.gov.ba.seia.util.Log4jUtil;

public class CerhExibirHistoricoController {

	@EJB
	private CerhFacade facade;
	
	public CerhHistoricoDTO prepararHistorico(Cerh anterior, Cerh atual) {
		CerhHistoricoDTO cerhHistoricoDTO = new CerhHistoricoDTO();
		
		try {//deixa isso ai rpz!
			if(anterior.getId() > atual.getId()){
				Cerh temp =  anterior.clone();
				anterior = atual;
				atual = temp;
			}
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		cerhHistoricoDTO.setAbaDadosGerais(getAbaDadosGerais(anterior, atual));
		cerhHistoricoDTO.setAbaBarragem(prepararHistoricoCerhLocalizacaoGeografica(anterior, atual,TipoUsoRecursoHidricoEnum.BARRAGEM));
		cerhHistoricoDTO.setAbaIntervencao(prepararHistoricoCerhLocalizacaoGeografica(anterior, atual,TipoUsoRecursoHidricoEnum.INTERVENCAO));
		cerhHistoricoDTO.setAbaCaptacaoSuperficial(prepararHistoricoCerhLocalizacaoGeografica(anterior, atual,TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL));
		cerhHistoricoDTO.setAbaCaptacaoSubteranea(prepararHistoricoCerhLocalizacaoGeografica(anterior, atual,TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA));
		cerhHistoricoDTO.setAbaLancamentoEfluente(prepararHistoricoCerhLocalizacaoGeografica(anterior, atual,TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE));
		
		return cerhHistoricoDTO;
	}
	
	private List<HistoricoDTO> getAbaDadosGerais(Cerh anterior, Cerh atual){

		HistoricoController historico = new HistoricoController();
		historico.comparar((AbstractEntity) facade.buscarParaHistorico(anterior),(AbstractEntity) facade.buscarParaHistorico(atual));
		carregarCerhProcesso(historico.getDiferencas(), anterior, atual);

		return historico.getDiferencas();
	}
	
	public List<HistoricoDTO> prepararHistoricoCerhLocalizacaoGeografica(Cerh c1, Cerh  c2,  TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum ){
		HistoricoController historico = new HistoricoController();
		
		List<AbstractEntity> cl1 = new ArrayList<AbstractEntity>();
		List<AbstractEntity> cl2 = new ArrayList<AbstractEntity>();
		
		cl1.addAll(facade.listarParaHistorico(c1, tipoUsoRecursoHidricoEnum));
		cl2.addAll(facade.listarParaHistorico(c2, tipoUsoRecursoHidricoEnum));
		
		historico.comparar(cl1, cl2);
		historico.ordernar();
		
		return historico.getDiferencas();
	}
	
	private void carregarCerhProcesso(List<HistoricoDTO> abaDadosGerais, Cerh c1, Cerh c2){
		HistoricoController hist = new HistoricoController();
	
		for (HistoricoDTO h : abaDadosGerais) {
			if(h.getNome().equals("Número do processo")){
				String numeroProcesso = h.getAntigo();
				if(numeroProcesso.equals("-")){
					numeroProcesso = h.getNovo();
				}
				
				CerhProcesso anterior = facade.buscarCerhProcesso(numeroProcesso, c1);
				CerhProcesso atual = facade.buscarCerhProcesso(numeroProcesso, c2);
				
				if(anterior  == null){
					anterior = (CerhProcesso) HistoricoUtil.construtorRecursivo(atual);
				}
				
				if(atual  == null){
					atual = (CerhProcesso) HistoricoUtil.construtorRecursivo(anterior);
				}
				
				hist.comparar(anterior, atual);
				h.setHistoricoObjeto(new ArrayList<HistoricoDTO>());
				for (HistoricoDTO subHistorico : hist.getDiferencas()) {
					h.getHistoricoObjeto().add(new HistoricoDTO(subHistorico.getNome(), subHistorico.getNovo(), subHistorico.getAntigo(), subHistorico.getNomeClasse()));
				}
				
				hist.ordernar();
			}
			
			
		}
	}

}