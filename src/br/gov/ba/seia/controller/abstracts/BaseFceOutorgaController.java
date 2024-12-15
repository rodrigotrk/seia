package br.gov.ba.seia.controller.abstracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import br.gov.ba.seia.controller.FceController;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.facade.FceOutorgaServiceFacade;
import br.gov.ba.seia.util.Util;

public abstract class BaseFceOutorgaController extends FceController {

	@EJB
	protected FceOutorgaServiceFacade fceOutorgaServiceFacade;

	protected List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica;

	protected List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeograficaCapSub;

	protected List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeograficaCapSup;

	
	public void limparDadosOutorga(){
		listaOutorgaLocalizacaoGeografica = null;
		listaFceOutorgaLocalizacaoGeograficaCapSup = null;
		listaFceOutorgaLocalizacaoGeograficaCapSub = null;
		super.limparFce();
	}	
	
	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaOutorgaLocalizacaoGeografica
	 * @throws Exception
	 * @since 13/04/2015
	 */
	public List<OutorgaLocalizacaoGeografica> obterInformacoesGeoBahia(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica) throws Exception {
		fceOutorgaServiceFacade.tratarPontosListaOutorgaLocalizacaoGeografica(listaOutorgaLocalizacaoGeografica);
		return listaOutorgaLocalizacaoGeografica;
	}

	/**
	 * Método que separa em duas listas as {@link OutorgaLocalizacaoGeografica} cadastradas em Captações Superficiais e Captações Subterrâneas cadastradas na ETAPA 4 do {@link Requerimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaOutorgaLocalizacaoGeografica
	 * @return captacoesSuperficial, captacoesSubterranea
	 * @since 08/04/2015
	 */
	public Map<String, Object> separarCaptacoes(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica){
		Map<String, Object> map = new HashMap<String, Object>();
		if(!Util.isNullOuVazio(listaOutorgaLocalizacaoGeografica)){
			List<OutorgaLocalizacaoGeografica> captacaoSuperficial = new ArrayList<OutorgaLocalizacaoGeografica>();
			List<OutorgaLocalizacaoGeografica> captacaoSubterranea = new ArrayList<OutorgaLocalizacaoGeografica>();
			for(OutorgaLocalizacaoGeografica outorgaLocGeo : listaOutorgaLocalizacaoGeografica){
				if(outorgaLocGeo.getIdeOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals( ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade())){
					if(outorgaLocGeo.getIdeOutorga().getTipoCaptacao().getIdeTipoCaptacao().equals( TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId())){
						captacaoSuperficial.add(outorgaLocGeo);
					} else if(outorgaLocGeo.getIdeOutorga().getTipoCaptacao().getIdeTipoCaptacao().equals( TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId())){
						captacaoSubterranea.add(outorgaLocGeo);
					}
				}
			}
			map.put("captacoesSuperficial", captacaoSuperficial);
			map.put("captacoesSubterranea", captacaoSubterranea);
		}
		return map;
	}

	// Análise Técnica
	protected List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaDaAnaliseTecnica() throws Exception{
		return fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaAnaliseTecnicaByIdeFce(super.fce);
	}
	
	/**
	 * Método para ser chamado quando existir FCE com os dois tipos de captações. 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 12/01/2016
	 */
	protected void carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) throws Exception{
		carregarListaFceOutorgaLocGeoSubterranea(finalidadeUsoAguaEnum);
		carregarListaFceOutorgaLocGeoSuperficial(finalidadeUsoAguaEnum);
	}
	
	/**
	 * Método para ser chamado quando existir FCE apenas com <b>Captação Subterrânea</b>.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 12/01/2016
	 */
	protected void carregarListaFceOutorgaLocGeoSubterranea(TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) throws Exception{
		listaFceOutorgaLocalizacaoGeograficaCapSub = fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaCaptacaoSubterraneaByFinalidade(super.requerimento, finalidadeUsoAguaEnum);
		if(!isListaCapSubterraneaVazia()){
			fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeograficaCapSub);
		}
	}

	private boolean isListaCapSubterraneaVazia(){
		return Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeograficaCapSub);
	}
	
	/**
	 * Método para ser chamado quando existir FCE apenas com <b>Captação Superficial</b>.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 12/01/2016
	 */
	protected void carregarListaFceOutorgaLocGeoSuperficial(TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) throws Exception{
		listaFceOutorgaLocalizacaoGeograficaCapSup = fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaCaptacaoSuperficialByFinalidade(super.requerimento, finalidadeUsoAguaEnum);
		if(!isListaCapSuperficialVazia()){
			fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeograficaCapSup);
		}
	}

	private boolean isListaCapSuperficialVazia(){
		return Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeograficaCapSup);
	}
	
	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaLocalizacaoGeograficaCapSub() {
		return listaFceOutorgaLocalizacaoGeograficaCapSub;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaLocalizacaoGeograficaCapSup() {
		return listaFceOutorgaLocalizacaoGeograficaCapSup;
	}

	public Integer getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}
	
}