package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.abstracts.BaseFceLicenciamentoAquiculturaController;
import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoLocalizacaoCultivo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 08/09/2015
 */
@Named("tanqueRedeController")
@ViewScoped
public class TanqueRedeController extends BaseFceLicenciamentoAquiculturaController {

	private List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaIntervencaoRio;

	private List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaIntervencaoBarragem;
	
	private List<TipoLocalizacaoCultivo> listaTipoLocalizacaoCultivo;
	
	private boolean outrosLocalizacaoCultivo;
	
	@Override
	@PostConstruct
	public void init() {
		super.comportamento = new ComportamentoPadrao();
		this.carregarAba();
	}

	@Override
	public void carregarAba(){
		carregarListaIpoLocalizacaoCultivo();
		super.carregarAba();
		if(super.isFceSalvo()){
			carregarListaTipoLocalizacaoCultivoRespondida();
		}
	}
	
	private void carregarListaTipoLocalizacaoCultivoRespondida(){
		try {
			super.serviceFacade.montarListaTipoLocalizacaoCultivoSelecionada(getFceAquiculturaLicenca(), getListaAtividadeDTO());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Localização do Cultivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListaIpoLocalizacaoCultivo(){
		try {
			listaTipoLocalizacaoCultivo = super.serviceFacade.listarTipoLocalizacaoCultivo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Localização do Cultivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void listarlistaFceAquiculturaEspecie() throws Exception {
		super.listaFceAquiculturaEspecie = super.serviceFacade.listarFceAquiculturaEspecieToTanqueRedeBy(super.requerimento);
	}

	@Override
	public void finalizar() {
		try {
			super.serviceFacade.finalizarAbaTanqueRede(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Tanque Rede.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception {
		
	}
	
	@Override
	public void salvarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		super.salvarCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade, TipoAquiculturaEnum.TANQUE_REDE);
	}

	@Override
	public void limpar() {
		listaFceOutorgaIntervencaoBarragem = null;
		listaFceOutorgaIntervencaoRio = null;
		listaTipoLocalizacaoCultivo = null;
		outrosLocalizacaoCultivo = false;
		super.limpar();
	}

	@Override
	public boolean validarAba() {
		boolean valido = true;
		if(isPermitirCadastroOutorgaAbaAtiva() && !super.validarLocalizacaoCadastrada()){
			JsfUtil.addErrorMessage("A intervenção " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!super.isAlgumaAtividadeMarcada()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um Tipo de Atividade.");
			valido = false;
		} 
		else {
			for(AquiculturaAtividadeDTO atividadeDTO : super.getListaAtividadeDTO()){
				if(!Util.isNull(atividadeDTO.getTipoAtividade()) && !Util.isNull(atividadeDTO.getTipoAtividade().getIdeAquiculturaTipoAtividade())){
					if(!super.validarTipoAtividade(atividadeDTO, TipoAquiculturaEnum.TANQUE_REDE)){
						valido = false;
					}
					if(Util.isNullOuVazio(atividadeDTO.getListaTipoLocalizacaoCultivo())){
						JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Tipo de Localização do Cultivo.");
						valido = false;
					}
					if(!valido){
						super.atualizarAtividadeTab(atividadeDTO.getTipoAtividade().getIdeAquiculturaTipoAtividade());
						break;
					}
				}
			}
		}
		return valido;
	}
	
	@SuppressWarnings("unchecked")
	public void verificarLocalizacaoCultivoOutros(ValueChangeEvent event){
		List<TipoLocalizacaoCultivo> lista = (List<TipoLocalizacaoCultivo>) event.getNewValue();
		if(!Util.isNullOuVazio(lista)){
			for(TipoLocalizacaoCultivo tipoLocalizacaoCultivo : lista){
				if(tipoLocalizacaoCultivo.isOutros()){
					outrosLocalizacaoCultivo = true;
					break;
				}
			}
		}
		outrosLocalizacaoCultivo = false;
	}
	
	@Override
	protected boolean validarVolumeCultivo(CaracterizacaoCultivoDTO dto) {
		if(Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
			JsfUtil.addErrorMessage("O Volume do cultivo (m³) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}
	
	public String visualizarFceOutorgaLocalicalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		return "";
	}

	@Override
	protected void listarOutorgaLocalizacaoGeografica() throws Exception {
		super.listaOutorgaLocalizacaoGeografica = super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaTipoIntervencaoAquicultura(super.requerimento);
		separarOutorgaLocalizacaoGeografica();

	}

	@Override
	protected void separarOutorgaLocalizacaoGeografica() throws Exception {
		List<OutorgaLocalizacaoGeografica> listaIntervenacoRio = new ArrayList<OutorgaLocalizacaoGeografica>();
		List<OutorgaLocalizacaoGeografica> listaIntervenacoBarragem = new ArrayList<OutorgaLocalizacaoGeografica>();
		for(OutorgaLocalizacaoGeografica olg : super.listaOutorgaLocalizacaoGeografica) {
			try {
				Boolean resposta = serviceFacade.isIntervencaoBarragem(super.requerimento.getIdeRequerimento(), olg);
				if(!Util.isNull(resposta)){
					if(resposta){
						listaIntervenacoBarragem.add(olg);
					}
					else {
						listaIntervenacoRio.add(olg);
					}
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a pergunta do requerimento.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		montarListaFceOutorgaLocalizacaoGeografica(listaIntervenacoRio, listaIntervenacoBarragem);
	}
	
	@Override
	protected void montarListaFceOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> primeiraLista, List<OutorgaLocalizacaoGeografica> segundaLista) throws Exception {
		listaFceOutorgaIntervencaoRio = listarFceOutorgaLocalizacaoGeograficaBy(primeiraLista);
		listaFceOutorgaIntervencaoBarragem = listarFceOutorgaLocalizacaoGeograficaBy(segundaLista);
	}

	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica) throws Exception {
		return super.fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaBy(listaOutorgaLocalizacaoGeografica, super.fce.getIdeDadoOrigem());
	}

	@Override
	protected void listarTipologiaToOutorga() throws Exception {
		super.listaTipologia = new ArrayList<Tipologia>();
		super.listaTipologia.add(super.serviceFacade.buscarTipologia());
	}

	@Override
	protected List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividade(AquiculturaTipoAtividadeEnum atividadeEnum) throws Exception {
		return super.serviceFacade.listaFceAquiculturaTipoAtividadeTanqueRede(super.getFceAquiculturaLicenca(), atividadeEnum);
	}
	
	@Override
	public void adicionarPoligonalCultivo(){
		super.adicionarPoligonalCultivo(TipoAquiculturaEnum.TANQUE_REDE);
	}
	
	@Override
	protected List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listarPoligonalCultivo(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception {
		return super.serviceFacade.listarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicenca, aquiculturaTipoAtividade, TipoAquiculturaEnum.TANQUE_REDE);
	}
	
	public String getBacia(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getBaciaHidrografica())) {
					fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().setBaciaHidrografica(super.serviceFacade.getBacia(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				}
			}
			return fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getBaciaHidrografica();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	public String getSubBacia(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getSubBacia())) {
					fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().setSubBacia(super.serviceFacade.getSubBacia(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				}
			}
			return fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getSubBacia();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public String getRpga(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getRpga())) {
					fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().setRpga(super.serviceFacade.getBacia(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				}
			}
			return fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getRpga();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public String getBaciaIncluida(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica())) {
					fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().setBaciaHidrografica(super.serviceFacade.getBacia(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String getSubBaciaIncluida(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia())) {
					fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().setSubBacia(super.serviceFacade.getSubBacia(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String getRpgaIncluida(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		try {
			if(super.serviceFacade.isLocGeoComShapePersistido(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica())){
				if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga())) {
					fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().setRpga(super.serviceFacade.getRpga(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		} catch (Exception e) {
			super.erroGeoBahia();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public List<TipoLocalizacaoCultivo> getListaTipoLocalizacaoCultivo() {
		return listaTipoLocalizacaoCultivo;
	}

	public void setListaTipoLocalizacaoCultivo(List<TipoLocalizacaoCultivo> listaTipoLocalizacaoCultivo) {
		this.listaTipoLocalizacaoCultivo = listaTipoLocalizacaoCultivo;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaIntervencaoRio() {
		return listaFceOutorgaIntervencaoRio;
	}

	public void setListaFceOutorgaIntervencaoRio(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaIntervencaoRio) {
		this.listaFceOutorgaIntervencaoRio = listaFceOutorgaIntervencaoRio;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaIntervencaoBarragem() {
		return listaFceOutorgaIntervencaoBarragem;
	}

	public void setListaFceOutorgaIntervencaoBarragem(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaIntervencaoBarragem) {
		this.listaFceOutorgaIntervencaoBarragem = listaFceOutorgaIntervencaoBarragem;
	}

	@Override
	protected void excluirAba() throws Exception {
		super.serviceFacade.excluirAbaTanqueRede(this);
	}

	public boolean isOutrosLocalizacaoCultivo() {
		return outrosLocalizacaoCultivo;
	}
	
	public boolean isExisteIntervencaoRio(){
		return !Util.isNullOuVazio(listaFceOutorgaIntervencaoRio);
	}

	public boolean isExisteIntervencaoBarragem(){
		return !Util.isNullOuVazio(listaFceOutorgaIntervencaoBarragem);
	}

	@Override
	public boolean isPermitirCadastroOutorgaAbaAtiva() {
		return super.licenciamentoAquiculturaController.cadastrarTanque();
	}

	@Override
	protected List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadePreenchidos() throws Exception {
		return super.serviceFacade.listarAquiculturaTipoAtividadeObrigatoriasInTanqueRede(super.requerimento);
	}

	@Override
	public void prepararDuplicacao() {
		try {
			if(isPermitirCadastroOutorgaAbaAtiva()){
				for(FceAquiculturaLicencaLocalizacaoGeografica fallg : listaFceAquiculturaLicencaLocGeo){
					fallg.setIdeFceAquiculturaLicencaLocalizacaoGeografica(null);
					fallg.setIdeFceAquiculturaLicenca(null);
				}
			}
			for(AquiculturaAtividadeDTO dto : super.getListaAtividadeDTO()){
				if(!Util.isNullOuVazio(dto.getListaPoligonalCultivo())){
					for(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica faltallg : dto.getListaPoligonalCultivo()){
						faltallg.setIdeFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(null);
						faltallg.setIdeFceAquiculturaLicenca(null);
						LocalizacaoGeografica localizacaoGeografica = faltallg.getIdeLocalizacaoGeografica();
						faltallg.setIdeLocalizacaoGeografica(super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(localizacaoGeografica));
					}
					if(!Util.isNullOuVazio(dto.getListaCaracterizacaoCultivo())){
						super.prepararEdicaoCaracterizacaoCultivo(dto);
						for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : dto.getListaCaracterizacaoCultivo()){
							caracterizacaoCultivo.setIdeFceAquiculturaLicencaTipoAtividade(null);
							caracterizacaoCultivo.setIdeFceAquiculturaLicenca(null);
							if(caracterizacaoCultivo.isProducaoFormaJovem()){
								caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setFceAquiculturaLicencaTipoAtividade(caracterizacaoCultivo);
							} else if(caracterizacaoCultivo.isProducaoEngorda()){
								caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setFceAquiculturaLicencaTipoAtividade(caracterizacaoCultivo);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " a aba Tanque Rede.");
		}
	}

	@Override
	public void duplicarFce() {
		try {
			if(!Util.isNull(listaFceAquiculturaLicencaLocGeo)){
				for(FceAquiculturaLicencaLocalizacaoGeografica fallg : listaFceAquiculturaLicencaLocGeo){
					fallg.setIdeFceAquiculturaLicenca(getFceAquiculturaLicenca());
				}
			}
			for(AquiculturaAtividadeDTO dto : super.getListaAtividadeDTO()){
				if(!Util.isNullOuVazio(dto.getListaPoligonalCultivo())){
					for(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica faltallg : dto.getListaPoligonalCultivo()){
						faltallg.setIdeFceAquiculturaLicenca(getFceAquiculturaLicenca());
					}
					if(!Util.isNullOuVazio(dto.getListaCaracterizacaoCultivo())){
						for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : dto.getListaCaracterizacaoCultivo()){
							caracterizacaoCultivo.setIdeFceAquiculturaLicenca(getFceAquiculturaLicenca());
						}
					}
				}
			}
			super.serviceFacade.finalizarAbaTanqueRede(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

}