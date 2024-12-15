package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
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
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoInstalacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 */
@Named("viveiroEscavadoController")
@ViewScoped
public class ViveiroEscavadoController extends BaseFceLicenciamentoAquiculturaController {

	private List<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea;
	private List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaCapSuperficial;
	private List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLancamento;

	@Override
	@PostConstruct
	public void init() {
		super.comportamento = new ComportamentoPadrao();
		this.carregarAba();
	}

	@Override
	public void carregarAba() {
		super.carregarAba();
	}

	/**
	 * Método que lista as {@link Tipologia} que poderão ser utilizadas no cadastramento da {@link LocalizacaoGeografica}
	 * <ul>
	 * 	<li>302 = Captação Superficial</li>
	 * 	<li>303 = Captação Subterrânea</li>
	 * 	<li>304 = Lançamento de Efluentes</li>
	 * </ul>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 */
	@Override
	protected void listarTipologiaToOutorga() throws Exception {
		super.listaTipologia = super.serviceFacade.listarTipologia();
	}

	@Override
	public void finalizar() {
		try {
			super.serviceFacade.finalizarAbaViveiroEscavado(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Viveiro Escavado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception {
		
	}

	@Override
	public void limpar() {
		listaCaptacaoSubterranea = null;
		listaFceOutorgaCapSuperficial = null;
		listaFceOutorgaLancamento = null;
		super.limpar();
	}
	
	public void limparVolumeCultivo(AjaxBehaviorEvent event){
		CaracterizacaoCultivoDTO dto = (CaracterizacaoCultivoDTO) event.getComponent().getAttributes().get("tipoInstalacao");
		for(TipoInstalacao tipoInstalacao : dto.getListaTipoInstalacao()){
			if(tipoInstalacao.isRaceway()){
				if(tipoInstalacao.isRowSelect()){
					dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().setTipoInstalacaoRaceways(true);
				} 
				else {
					dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().setTipoInstalacaoRaceways(false);
					dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().setNumVolumeCultivo(null);
					super.abasAtividades.obterSomatorioVolumeCultivo(null);
				}
			}
		}
		super.abasAtividades.atualizarTipoInstalacao(event);
	}

	@Override
	public boolean validarAba() {
		boolean valido = true;
		if(isPermitirCadastroOutorgaAbaAtiva() && !super.validarLocalizacaoCadastrada()){
			JsfUtil.addErrorMessage("A coordenada da captação/lançamento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!super.isAlgumaAtividadeMarcada()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um Tipo de Atividade.");
			valido = false;
		} 
		else {
			for(AquiculturaAtividadeDTO atividadeDTO : super.getListaAtividadeDTO()){
				if(!Util.isNull(atividadeDTO.getTipoAtividade()) && !Util.isNull(atividadeDTO.getTipoAtividade().getIdeAquiculturaTipoAtividade()) && !super.validarTipoAtividade(atividadeDTO, TipoAquiculturaEnum.VIVEIRO_ESCAVADO)){
					valido = false;
					super.atualizarAtividadeTab(atividadeDTO.getTipoAtividade().getIdeAquiculturaTipoAtividade());
					break;
				}
			}
		}
		return valido;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 26/10/2015
	 * @param dto
	 */
	@Override
	protected boolean validarVolumeCultivo(CaracterizacaoCultivoDTO dto) {
		boolean isRaceway = false;
		for(TipoInstalacao instalacao : dto.getListaTipoInstalacao()){
			if(instalacao.isRaceway() && instalacao.isRowSelect()){
				isRaceway = true;
				break;
			}
		}
		if(isRaceway && Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
			JsfUtil.addErrorMessage("O Volume do cultivo (m³) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}

	@Override
	public void adicionarPoligonalCultivo(){
		super.adicionarPoligonalCultivo(TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}
	
	@Override
	protected void listarOutorgaLocalizacaoGeografica() throws Exception {
		super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaBy(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId()), super.requerimento));
		separarOutorgaLocalizacaoGeografica();

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void separarOutorgaLocalizacaoGeografica() throws Exception {
		Map<String, Object> captacoes = super.separarCaptacoes(super.listaOutorgaLocalizacaoGeografica);
		listaCaptacaoSubterranea = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSubterranea");
		List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSuperficial");
		List<OutorgaLocalizacaoGeografica> listaLancamentoEfluente = separarLancamentoElfuente(listaCaptacaoSuperficial);
		montarListaFceOutorgaLocalizacaoGeografica(listaCaptacaoSuperficial, listaLancamentoEfluente);

	}

	/**
	 * Método que separa os pontos de {@link OutorgaLocalizacaoGeografica} para <i>Lançamento de Efluentes</i> informado no {@link Requerimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 */
	private List<OutorgaLocalizacaoGeografica> separarLancamentoElfuente(List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial) {
		List<OutorgaLocalizacaoGeografica> listaLancamentoEfluente = new ArrayList<OutorgaLocalizacaoGeografica>();
		listaLancamentoEfluente.addAll(super.listaOutorgaLocalizacaoGeografica);
		for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
			if(isCaptaSubterranea(outorgaLocalizacaoGeografica) || isCaptacaoSuperficial(listaCaptacaoSuperficial, outorgaLocalizacaoGeografica)){
				listaLancamentoEfluente.remove(outorgaLocalizacaoGeografica);
			}
		}
		return listaLancamentoEfluente;
	}

	@Override
	protected void montarListaFceOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> primeiraLista, List<OutorgaLocalizacaoGeografica> segundaLista) throws Exception {
		listaFceOutorgaCapSuperficial = listarFceOutorgaLocalizacaoGeograficaBy(primeiraLista);
		listaFceOutorgaLancamento = listarFceOutorgaLocalizacaoGeograficaBy(segundaLista);
	}
	
	/**
	 * Método para listar os {@link FceOutorgaLocalizacaoGeografica} daquela Lista de {@link OutorgaLocalizacaoGeografica}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaOutorgaLocalizacaoGeografica
	 * @return
	 * @throws Exception
	 * @since 02/06/2015
	 */
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica) throws Exception {
		return super.fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaComPontoBy(listaOutorgaLocalizacaoGeografica, super.fce.getIdeDadoOrigem());
	}

	@Override
	protected void listarlistaFceAquiculturaEspecie() throws Exception {
		super.listaFceAquiculturaEspecie = super.serviceFacade.listarFceAquiculturaEspecieToViveiroEscavadoBy(super.requerimento);
	}
	

	@Override
	protected List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividade(AquiculturaTipoAtividadeEnum atividadeEnum) throws Exception {
		return super.serviceFacade.listaFceAquiculturaTipoAtividadeViveiroEscavado(super.getFceAquiculturaLicenca(), atividadeEnum);
		
	}
	
	@Override
	public void salvarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		super.salvarCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade, TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}

	// INI FLAG's
	public boolean isExisteCaptacaoSubterranea(){
		return !Util.isNullOuVazio(listaCaptacaoSubterranea);
	}

	public boolean isExisteCaptacaoSuperifical(){
		return !Util.isNullOuVazio(listaFceOutorgaCapSuperficial);
	}

	/**
	 * Método que verifica se a {@link OutorgaLocalizacaoGeografica} está presente na lista de <i>Captação Superficial</i>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaCaptacaoSuperficial
	 * @param outorgaLocalizacaoGeografica
	 * @return
	 */
	private boolean isCaptacaoSuperficial(List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		return !Util.isNullOuVazio(listaCaptacaoSuperficial) && listaCaptacaoSuperficial.contains(outorgaLocalizacaoGeografica);
	}

	/**
	 * Método que verifica se a {@link OutorgaLocalizacaoGeografica} está presente na lista de <i>Captação Subterrânea</i>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param outorgaLocalizacaoGeografica
	 * @return
	 */
	private boolean isCaptaSubterranea(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		return isExisteCaptacaoSubterranea() && listaCaptacaoSubterranea.contains(outorgaLocalizacaoGeografica);
	}

	public boolean isExisteLancamentoEfluente(){
		return !Util.isNullOuVazio(listaFceOutorgaLancamento);
	}
	// FIM FLAG's

	// Getters && Setters
	public String getBacia(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		if(fceAquiculturaLicencaLocalizacaoGeografica.isCoordenadaSalva()){
			if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia())) {
				try {
					super.serviceFacade.montarBaciaAndSubBacia(fceAquiculturaLicencaLocalizacaoGeografica);
				} catch (Exception e) {
					super.erroGeoBahia();
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		}
		return null;
	}

	public String getSubBacia(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		if(fceAquiculturaLicencaLocalizacaoGeografica.isCoordenadaSalva()){
			if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica())){
				try {
					super.serviceFacade.montarBaciaAndSubBacia(fceAquiculturaLicencaLocalizacaoGeografica);
				} catch (Exception e) {
					super.erroGeoBahia();
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica();
		}
		return null;
	}

	public String getRpga(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica){
		if(fceAquiculturaLicencaLocalizacaoGeografica.isCoordenadaSalva()){
			if(Util.isNullOuVazio(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga())){
				try {
					super.serviceFacade.montarBaciaAndSubBacia(fceAquiculturaLicencaLocalizacaoGeografica);
				} catch (Exception e) {
					super.erroGeoBahia();
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
			return fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga();
		}
		return null;
	}

	public int getPonto() {
		return super.getSomentePonto();
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea() {
		return listaCaptacaoSubterranea;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaCapSuperficial() {
		return listaFceOutorgaCapSuperficial;
	}

	public void setListaFceOutorgaCapSuperficial(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaCapSuperficial) {
		this.listaFceOutorgaCapSuperficial = listaFceOutorgaCapSuperficial;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaLancamento() {
		return listaFceOutorgaLancamento;
	}

	public void setListaFceOutorgaLancamento(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLancamento) {
		this.listaFceOutorgaLancamento = listaFceOutorgaLancamento;
	}

	@Override
	public List<Tipologia> getListaTipologia() {
		return listaTipologia;
	}

	public void setListaTipologia(List<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	@Override
	protected List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listarPoligonalCultivo(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception {
		return super.serviceFacade.listarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicenca, aquiculturaTipoAtividade, TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}

	@Override
	protected void excluirAba() throws Exception {
		super.serviceFacade.excluirAbaViveiroEscavado(this);
		
	}

	@Override
	public boolean isPermitirCadastroOutorgaAbaAtiva() {
		return super.licenciamentoAquiculturaController.cadastrarViveiro();
	}

	@Override
	protected List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadePreenchidos() throws Exception {
		return super.serviceFacade.listarAquiculturaTipoAtividadeObrigatoriasInViveiroEscavado(super.requerimento);		
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
						// super.prepararEdicaoCaracterizacaoCultivo(dto);
						for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : dto.getListaCaracterizacaoCultivo()){
							caracterizacaoCultivo.setIdeFceAquiculturaLicencaTipoAtividade(null);
							caracterizacaoCultivo.setIdeFceAquiculturaLicenca(null);
							if(caracterizacaoCultivo.isProducaoFormaJovem()){
								caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setIdeFceAquiculturaLicencaTipoAtividadeTipoProducao(null);
								caracterizacaoCultivo.getAquiculturaProducaoDTOFormaJovem().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setFceAquiculturaLicencaTipoAtividade(caracterizacaoCultivo);
							} else if(caracterizacaoCultivo.isProducaoEngorda()){
								caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setIdeFceAquiculturaLicencaTipoAtividadeTipoProducao(null);
								caracterizacaoCultivo.getAquiculturaProducaoDTOEngorda().getFceAquiculturaLicencaTipoAtividadeTipoProducao().setFceAquiculturaLicencaTipoAtividade(caracterizacaoCultivo);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " a aba Viveiro Escavado.");
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
			super.serviceFacade.finalizarAbaViveiroEscavado(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

}