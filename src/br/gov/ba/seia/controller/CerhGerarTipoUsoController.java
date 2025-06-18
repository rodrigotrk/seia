package br.gov.ba.seia.controller;

import java.util.ArrayList;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhAbaDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.dto.CerhIncluirProcessoDTO;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.CerhDadosGeraisServiceFacade;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**	
 * 	Tipo Uso são gerados em 3 condições
 * 
 *  1º) Quando o ato ambiental tem tipologia, se escolhe o tipo uso adquado para aquela tipologia, excerto somente 
 *  para a tipologia de aproveitamento hidroeletrico (Que é um caso especial)
 * 
 * 	2º) Quando o ato não tem tipologia, se usa as regras de/para para escolher quais tipo uso são aceitos para aquela ato ambiental
 * 
 *  3º) Quando a tipologia não tem um tipo uso definido (Aproveitamento Hidroeletrico)
 *
 * */

public class CerhGerarTipoUsoController {

	@EJB
	private CerhDadosGeraisServiceFacade cerhDadosGeraisServiceFacade;
	
	private CerhIncluirProcessoDTO dto;
	private CerhDTO cerhDTO;
	
	
	public void gerar(CerhIncluirProcessoDTO dto, CerhDTO cerhDTO) throws Exception {
		this.cerhDTO = cerhDTO;
		this.dto = dto;
		
		if (Util.isNullOuVazio(dto.getCerhProcesso().getCerhTipoUsoCollection())) {
			dto.getCerhProcesso().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
		}

		if(dto.getProcesso()!=null){
			for (ProcessoAto pa : dto.getProcesso().getProcessoAtoCollection()){
				
				if(pa.getStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.DEFERIDO.getId())
						|| pa.getStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId())) {
					
					if(pa.getTipologia()!=null){
						gerarTipoUsoPorTipologia(dto,pa);
						dto.setExibirLabelTipoUso(false);
					}else{
						gerarTipoUsoPorAtoAmbiental(dto, pa);
						dto.setExibirLabelTipoUso(true);
					}
				}
			}
		}

		dto = this.dto;
	}
	
	
	private void gerarTipoUsoPorAtoAmbiental(CerhIncluirProcessoDTO dto, ProcessoAto pa){
		dto.setProcessoSemTipologia(true);
		
		if(CerhAtosAmbientaisElegiveis.atosElegiveisLancamento().contains(pa.getAtoAmbiental())){
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())){
					tipoUsoRecursoHidrico.setEditar(true);
				}
			}
		}
		
		if(CerhAtosAmbientaisElegiveis.atosElegiveisCaptacao().contains(pa.getAtoAmbiental())){
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())||
				   tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())){
					tipoUsoRecursoHidrico.setEditar(true);
				}
			}
		}		
		
		if(CerhAtosAmbientaisElegiveis.atosElegiveisIntervencao().contains(pa.getAtoAmbiental())){
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())){
					tipoUsoRecursoHidrico.setEditar(true);
				}
			}
		}
		
		if(CerhAtosAmbientaisElegiveis.atosElegiveisBarragem().contains(pa.getAtoAmbiental())){
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId())){
					tipoUsoRecursoHidrico.setEditar(true);
				}
			}
		}
		
		if(CerhAtosAmbientaisElegiveis.atosLivreEscolha().contains(pa.getAtoAmbiental())){
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				tipoUsoRecursoHidrico.setEditar(true);
			}
		}
		
		for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
			tipoUsoRecursoHidrico.setVisualizar(tipoUsoRecursoHidrico.isEditar());
		}
	}
	
	private void gerarTipoUsoPorTipologia(CerhIncluirProcessoDTO dto, ProcessoAto pa){
		if(pa.getTipologia().getIdeTipologia().equals(TipologiaEnum.APROVEITAMENTO_HIDRELETRICO.getId())){
			dto.setProcessoSemTipologia(true);
			adicionarTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.BARRAGEM);
			adicionarTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.INTERVENCAO);
			
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
				tipoUsoRecursoHidrico.setVisualizar(tipoUsoRecursoHidrico.isEditar());
			}
		} else {
			CerhTipoUso ctu = null;
			TipoUsoRecursoHidrico tipoUsoRecursoHidrico = retornaTipoRecursoHidricoPorTipologia(pa);
			
			if(!isProcessoPossuiTipoUsoRecursoHidrico(tipoUsoRecursoHidrico, dto.getCerhProcesso())) {
				ctu = cerhDadosGeraisServiceFacade.carregarCerhTipoUsoPorNumProcesso(dto.getCerhProcesso().getNumProcesso(), tipoUsoRecursoHidrico);
				
				if (Util.isNullOuVazio(ctu)) {
					ctu = new CerhTipoUso();
				}
				
				ctu.setIdeCerh(dto.getCerhProcesso().getIdeCerh());
				ctu.setIdeCerhProcesso(dto.getCerhProcesso());
				ctu.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
				
				preencherDadoConcedido(ctu, TipologiaEnum.getEnum(pa.getTipologia().getIdeTipologia()));
				
				boolean contemCTU = false;
				
				for (CerhTipoUso cerhTU : dto.getCerhProcesso().getCerhTipoUsoCollection()) {
					if(cerhTU.getIdeCerhTipoUso().equals(ctu.getIdeCerhTipoUso())) {
						contemCTU = true;
						break;
					}
				}
				
				if(!contemCTU) dto.getCerhProcesso().getCerhTipoUsoCollection().add(ctu);
			}
		}
	}
	
	
	private void preencherDadoConcedido(CerhTipoUso ctu, TipologiaEnum tipologiaEnum) {
		Tipologia tipologia = retornarTipologia(tipologiaEnum);
		if(Util.isNull(tipologia)) {
			dto.getCerhProcesso().setIndDadosConcedidos(false);
		}
		else {
			dto.getCerhProcesso().setIndDadosConcedidos(true);
			for(CerhLocalizacaoGeografica clg: tipologia.getListaCerhLocalizacaoGeografica()) {
				clg.setIdeCerhTipoUso(ctu);
				clg.setIdeCerhProcesso(dto.getCerhProcesso());
				prepararAbasCaracterizacao(clg);
			}
			ctu.setCerhLocalizacaoGeograficaCollection(tipologia.getListaCerhLocalizacaoGeografica());
		}
	}

	private Tipologia retornarTipologia(TipologiaEnum tipologiaEnum) {
		if(!Util.isNullOuVazio(dto.getListaTipologiaDadosConcedidos())) { 
			for(Tipologia tipologia : dto.getListaTipologiaDadosConcedidos()) {
				if(new Tipologia(tipologiaEnum).equals(tipologia)) {
					return  tipologia;
				}
			}
		}
		return null;
	}
	
	private void prepararAbasCaracterizacao(CerhLocalizacaoGeografica clg) {
		CerhAbaDTO cerhAbaDTO = cerhDTO.getAba(clg.getIdeCerhTipoUso().getIdeTipoUsoRecursoHidrico());
		if(!Util.isNull(cerhAbaDTO) && Util.isNull(clg.getIdeCerhTipoUso().getIdeCerhTipoUso())){
			if(Util.isNullOuVazio(cerhAbaDTO.getListaCaracterizacaoDTO())) {
				cerhAbaDTO.setListaCaracterizacaoDTO(new ArrayList<CerhCaracterizacaoDTO>());
			}
			cerhAbaDTO.getListaCaracterizacaoDTO().add(new CerhCaracterizacaoDTO(clg, dto.getCerhProcesso()));
		}
	}

	
	private boolean isProcessoPossuiTipoUsoRecursoHidrico (TipoUsoRecursoHidrico tipoUsoRecursoHidrico, CerhProcesso cerhProcesso) {
		boolean retorno = false;
		for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
			if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidrico) ) {
				retorno = true;
				break;
			}
		}

		return retorno;
	}
	
	
	
	
	/** Essas são a opções disponiveis para o usuario, não os tipo uso já selecionados */
	private void adicionarTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum){
		boolean possuiTipoUso = false;
		
		for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidrico()) {
			if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidricoEnum.getId())){
				tipoUsoRecursoHidrico.setEditar(true);
				possuiTipoUso = true;
			}
		}
		
		if(!possuiTipoUso){
			TipoUsoRecursoHidrico tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.BARRAGEM);
			tipoUsoRecursoHidrico.setEditar(true);
			dto.getListaTipoUsoRecursoHidrico().add(tipoUsoRecursoHidrico);
		}
		
		dto.setPossuiCerhTipoUsoEditavel(true);
	}
	
	private TipoUsoRecursoHidrico retornaTipoRecursoHidricoPorTipologia(ProcessoAto pa) {
		TipoUsoRecursoHidrico tipoUsoRecursoHidrico = null;
		
		if(pa.getTipologia().equals(new Tipologia(TipologiaEnum.CAPTACAO_SUPERFICIAL))) {
			tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL);
		}
		else if(pa.getTipologia().equals(new Tipologia(TipologiaEnum.CAPTACAO_SUBTERRANEA))) {
			tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA);
		}
		else if(pa.getTipologia().equals(new Tipologia(TipologiaEnum.LANCAMENTO_EFLUENTES))) {
			tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE);
		}
		
		else if(pa.getTipologia().equals(new Tipologia(TipologiaEnum.INTERVENCAO_CORPO_HIDRICO))) {
			if(isProcessoConstrucaoBarragem(pa)){
				tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.BARRAGEM);
			} 
			else {
				tipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.INTERVENCAO);
			}
		}
		
		return tipoUsoRecursoHidrico;
	}
	
	private boolean isProcessoConstrucaoBarragem(ProcessoAto pa) {
		try {
			return !Util.isNullOuVazio(cerhDadosGeraisServiceFacade.listarLocalizacaoGeograficaByProcessoAto(pa));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
