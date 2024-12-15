package br.gov.ba.seia.controller.abstracts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaController;
import br.gov.ba.seia.controller.GerenciadorAbasAtividades;
import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Especie;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoAquicultura;
import br.gov.ba.seia.entity.TipoLocalizacaoCultivo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.facade.FceLicenciamentoAquiculturaServiceFacade;
import br.gov.ba.seia.interfaces.AtividadeAbaComportamentoInterface;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * Abstração do <b>FCE - Licenciamento Aquicultura</b>.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 27/05/2015
 */
public abstract class BaseFceLicenciamentoAquiculturaController extends BaseFceOutorgaController {

	@Inject
	protected FceLicenciamentoAquiculturaController licenciamentoAquiculturaController;

	@EJB
	protected FceLicenciamentoAquiculturaServiceFacade serviceFacade;

	protected AtividadeAbaComportamentoInterface comportamento;
	
	protected List<FceAquiculturaEspecie> listaFceAquiculturaEspecie;
	
	protected List<FceAquiculturaLicencaLocalizacaoGeografica> listaFceAquiculturaLicencaLocGeo;
	
	private FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica;
	
	protected List<Tipologia> listaTipologia;

	protected GerenciadorAbasAtividades abasAtividades;
	
	protected List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividadesPreenchidas;
	
	private boolean preencherPiscicultura;
	private boolean pisciculturaDaOutorga;
	
	private boolean preencherCarcinicultura;
	private boolean carciniculturaDaOutorga;
	
	private boolean preencherRanicultura;
	private boolean raniculturaDaOutorga;
	
	private boolean preencherAlgicultura;
	private boolean algiculturaDaOutorga;
	
	private boolean preencherMalococultura;
	private boolean malococulturaDaOutorga;
	
	private boolean outrosOrganismos;
	private boolean outrosTipoInstalacao;
	private boolean outrasEspecies;
	// FIM - Variáveis

	protected abstract void adicionarPoligonalCultivo();
	
	/**
	 * Método para excluir as informações que foram salva(s) na(s) <b>ABA Viveiro Escavado</b> e/ou <b>ABA Tanque Rede</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 26/06/2015
	 */
	protected abstract void excluirAba() throws Exception;
	
	protected abstract void listarlistaFceAquiculturaEspecie() throws Exception;
	
	/**
	 * Método para buscar os dados de {@link OutorgaLocalizacaoGeografica} cadastrados na <b>Etapa 4</b> do Requerimento.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 02/06/2015
	 */
	protected abstract void listarOutorgaLocalizacaoGeografica() throws Exception;

	protected abstract List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listarPoligonalCultivo(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception;
	
	/**
	 * Método para preencher a lista de {@link Tipologia} que podem ser usadas para cadastro do  {@link FceAquiculturaLicencaLocalizacaoGeografica}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 02/06/2015
	 */
	protected abstract void listarTipologiaToOutorga() throws Exception;

	/**
	 * Método que deve ser utilizado para criar as listas de {@link FceOutorgaLocalizacaoGeografica} na aba <b>Viveiro Escavado</b> e/ou <b>Tanque Rede</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param primeiraLista
	 * @param segundaLista
	 * @throws Exception
	 * @since 02/06/2015
	 */
	protected abstract void montarListaFceOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> primeiraLista, List<OutorgaLocalizacaoGeografica> segundaLista) throws Exception;
	
	/**
	 * Método para separar as {@link OutorgaLocalizacaoGeografica} de acordo com a aba do <b>FCE - Licenciamento para Aquicultura</b> em:
	 *  <ul>
	 *  	<li>Captação Superficial</li>
	 * 		<li>Captação Subterrânea </li>
	 *  	<li>Lançamento de Efluentes </li>
	 *  	<li>Intervenção em Corpo Hídrico - Barragem </li>
	 *  	<li>Intervenção em Corpo Hídrico - Rio </li>
	 *  </ul>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 02/06/2015
	 */
	protected abstract void separarOutorgaLocalizacaoGeografica() throws Exception;
	
	public void adicionarEspecie() {
		comportamento.adicionarEspecie(getAquiculturaAtividadeDTO(), getFceAquiculturaLicenca());
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 09/06/2015
	 */
	public void adicionarLocalizacao(){
		if(Util.isNullOuVazio(listaFceAquiculturaLicencaLocGeo)){
			listaFceAquiculturaLicencaLocGeo = new ArrayList<FceAquiculturaLicencaLocalizacaoGeografica>();
		}
		FceAquiculturaLicencaLocalizacaoGeografica intervencao = null;
		if(listaTipologia.size() < 2){
			intervencao = new FceAquiculturaLicencaLocalizacaoGeografica(getFceAquiculturaLicenca(), listaTipologia.get(0));
		} 
		else {
			intervencao = new FceAquiculturaLicencaLocalizacaoGeografica(getFceAquiculturaLicenca());
		}
		listaFceAquiculturaLicencaLocGeo.add(intervencao);
	}
	
	/**
	 * Método para criar uma nova <i>Poligonal da Área de Cultivo</i>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 09/06/2015
	 */
	protected void adicionarPoligonalCultivo(TipoAquiculturaEnum aquiculturaEnum){
		if(Util.isNullOuVazio(getAquiculturaAtividadeDTO().getListaPoligonalCultivo())){
			getAquiculturaAtividadeDTO().setListaPoligonalCultivo(new ArrayList<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica>());
		}
		comportamento.adicionarPoligonal(getAquiculturaAtividadeDTO().getListaPoligonalCultivo(), getAquiculturaAtividadeDTO().getTipoAtividade(), getFceAquiculturaLicenca(), aquiculturaEnum);
		getAquiculturaAtividadeDTO().setSomatorioAreaPoligonal(new BigDecimal(0));
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 06/11/2015
	 */
	protected void atualizarAtividadeTab(Integer abaAtiva) {
		if(!Util.isNull(abaAtiva)){
			abasAtividades.setActiveTab(abaAtiva - 1);
		}
		String toUpdate = "licenciamentoAquiculturaAbas::tabsAtividade";
		if(licenciamentoAquiculturaController.isAbaViveiroAtiva()){
			toUpdate = toUpdate.replace("::", ":formLicenciamentoAquiculturaAbaViveiro:");
		}
		else if(licenciamentoAquiculturaController.isAbaTanqueAtiva()){
			toUpdate = toUpdate.replace("::", ":formLicenciamentoAquiculturaTanque:");
		}
		RequestContext.getCurrentInstance().addPartialUpdateTarget(toUpdate);
	}

	/**
	 * Método que avança de aba quando ela é válida.
	 * @author eduardo.fernandes
	 * @since 01/06/2015
	 */
	public void avancarAba(){
		if(validarAba()){
			finalizar();
			licenciamentoAquiculturaController.avancarAba();
		}
	}
	
	private void carregarAbaComFceOutorga() throws Exception{
		listarOutorgaLocalizacaoGeografica();
		listarlistaFceAquiculturaEspecie();
		listarAquiculturaAtividadesDaOutorga();
		// As atividades que foram selecionadas no FCE - Outorga para Aquicultura devem vir marcadas e com suas já espécies adicionadas.
		for(AquiculturaTipoAtividadeEnum atividadeEnum : licenciamentoAquiculturaController.getListaAtividadeEnum()){
			for(AquiculturaTipoAtividade aquiculturaTipoAtividade : listaAquiculturaTipoAtividadesPreenchidas){
				if(aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade().equals(atividadeEnum.getId())){
					marcarAtividadeBy(atividadeEnum, true);
					montarDTO(atividadeEnum, false);
					break;
				}
			}
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 23/11/2015
	 */
	private void montarDTO(AquiculturaTipoAtividadeEnum atividadeEnum, boolean devePreencherNaLicenca) {
		AquiculturaAtividadeDTO dto = getListaAtividadeDTO().get(getIndexDTO(atividadeEnum));
		dto.setPodePreencherNaLicenca(devePreencherNaLicenca);
		prepararDTOBy(dto, atividadeEnum);
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 23/11/2015
	 */
	private void montarDTOeditado(AquiculturaTipoAtividadeEnum atividadeEnum) {
		AquiculturaAtividadeDTO dto = getListaAtividadeDTO().get(getIndexDTO(atividadeEnum));
		if(Util.isNull(dto.getTipoAtividade())){
			marcarAtividadeBy(atividadeEnum, null);
			prepararDTOBy(dto, atividadeEnum);
		}
	}
	
	private void carregarAbaSemFceOutorga() throws Exception{
		prepararCadastroDasAquiculturaOutorga();
		if(this.isFceSalvo()){
			listarLocalizacoesByFceAquiculturaLicenca();
		}
	}

	public void carregarAba() {
		try {
			super.requerimento = licenciamentoAquiculturaController.getRequerimento();
			super.fce = licenciamentoAquiculturaController.getFce();
			prepararGerenciadorAbasAtividade();
			if(isPermitirCadastroOutorgaAbaAtiva()){
				carregarAbaSemFceOutorga();
			}
			else {
				carregarAbaComFceOutorga();
			}
			if(this.isFceSalvo()){
				// Percorre a lista de Atividades cadastradas no Empreendimento / FCE - Outorga para Aquicultura
				for(AquiculturaTipoAtividadeEnum atividadeEnum : licenciamentoAquiculturaController.getListaAtividadeEnum()){
					List<FceAquiculturaLicencaTipoAtividade> listaCaracterizacaoCultivoSalva = listarFceAquiculturaLicencaTipoAtividade(atividadeEnum);
					if(!Util.isNullOuVazio(listaCaracterizacaoCultivoSalva)){
						montarDTOeditado(atividadeEnum);
						AquiculturaAtividadeDTO dto = getListaAtividadeDTO().get(getIndexDTO(atividadeEnum));
						if(dto.isPodePreencherNaLicenca()){
							montarEspeciesSelecionadasInLicenca(dto, listaCaracterizacaoCultivoSalva);
						}
						dto.setListaPoligonalCultivo(listarPoligonalCultivo(getFceAquiculturaLicenca(), dto.getTipoAtividade()));
						getAreaPoligonalCultivo(dto.getListaPoligonalCultivo());
						if(!Util.isNullOuVazio(dto.getListaCaracterizacaoCultivo())){
							for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade : dto.getListaCaracterizacaoCultivo()){
								if(dto.getListaEspecie().contains(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade())){
									dto.getListaEspecie().remove(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade());
								}
							}
							for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividadeComIde : listaCaracterizacaoCultivoSalva){
								FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividadeToRemove = null;
								for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividadeSemIde : dto.getListaCaracterizacaoCultivo()){
									if(fceAquiculturaLicencaTipoAtividadeComIde.getIdeEspecieAquiculturaTipoAtividade().equals(fceAquiculturaLicencaTipoAtividadeSemIde.getIdeEspecieAquiculturaTipoAtividade())){
										fceAquiculturaLicencaTipoAtividadeToRemove = fceAquiculturaLicencaTipoAtividadeSemIde;
										break;
									}
								}
								if(!Util.isNull(fceAquiculturaLicencaTipoAtividadeToRemove)){
									dto.getListaCaracterizacaoCultivo().remove(fceAquiculturaLicencaTipoAtividadeToRemove);
									dto.getListaCaracterizacaoCultivo().add(fceAquiculturaLicencaTipoAtividadeComIde);
									Collections.sort(dto.getListaCaracterizacaoCultivo());
								}
							}
						}
						prepararEdicaoCaracterizacaoCultivo(dto);
						abasAtividades.obterSomatorioDaAtividade(dto);
					}
				}
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações das Espécies cadastras no FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void prepararEdicaoCaracterizacaoCultivo(AquiculturaAtividadeDTO dto) throws Exception {
		serviceFacade.prepararEdicaoCaracterizacaoCultivo(dto.getListaCaracterizacaoCultivo());
	}

	public void changeAlgicutura(ValueChangeEvent event){
		if((Boolean) event.getNewValue()){
			prepararDTOBy(null, AquiculturaTipoAtividadeEnum.ALGICUTURA);
		} 
		else {
			limparDTOBy(AquiculturaTipoAtividadeEnum.ALGICUTURA);
		}
	}
	
	public void changeCarcinicultura(ValueChangeEvent event){
		if((Boolean) event.getNewValue()){
			prepararDTOBy(null,AquiculturaTipoAtividadeEnum.CARCINICULTURA);
		} else {
			limparDTOBy(AquiculturaTipoAtividadeEnum.CARCINICULTURA);
		}
	}

	public void changeMalococultura(ValueChangeEvent event){
		if((Boolean) event.getNewValue()){
			prepararDTOBy(null, AquiculturaTipoAtividadeEnum.MALOCOCULTURA);
		} 
		else {
			limparDTOBy(AquiculturaTipoAtividadeEnum.MALOCOCULTURA);
		}
	}
	
	public void changePiscicultura(ValueChangeEvent event){
		if((Boolean) event.getNewValue()){
			prepararDTOBy(null, AquiculturaTipoAtividadeEnum.PSICULTURA);
		} else {
			limparDTOBy(AquiculturaTipoAtividadeEnum.PSICULTURA);		
		}
	}

	public void changeRanicultura(ValueChangeEvent event){
		if((Boolean) event.getNewValue()){
			prepararDTOBy(null, AquiculturaTipoAtividadeEnum.RANICULTURA);
		} 
		else {
			limparDTOBy(AquiculturaTipoAtividadeEnum.RANICULTURA);
		}
	}
	
	/**
	 * Método para bloquear o {@link FceAquiculturaLicencaLocalizacaoGeografica} na tabela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 26/06/2015
	 */
	public void confirmarLocalizacao(FceAquiculturaLicencaLocalizacaoGeografica intervencao){
		if(validarLocalizacao(intervencao)){
			intervencao.setConfirmado(true);
		}
	}
	
	public void editarLocalizacao(FceAquiculturaLicencaLocalizacaoGeografica intervencao){
		intervencao.setConfirmado(false);
	}
	
	public void erroGeoBahia(){
		JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Geobahia.");
	}
	// FIM = Métodos
	
	public void excluirCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		try {
			if(!Util.isNullOuVazio(fceAquiculturaLicencaTipoAtividade)){
				serviceFacade.excluirCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade);
			}
			getAquiculturaAtividadeDTO().getListaEspecie().add(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade());
			Collections.sort(getAquiculturaAtividadeDTO().getListaEspecie());
			getAquiculturaAtividadeDTO().getListaCaracterizacaoCultivo().remove(fceAquiculturaLicencaTipoAtividade);
			abasAtividades.obterSomatorioDaAtividade(null);
			super.exibirMensagem005();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a caracterização do cultivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para excluir a Coordenada ou Intervenção cadastrada(s) no FCE - Licenciamento Aquícola.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 26/06/2015
	 */
	public void excluirLocalizacao(){
		try {
			listaFceAquiculturaLicencaLocGeo.remove(fceAquiculturaLicencaLocalizacaoGeografica);
			excluirLocalizacaoGeografica(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			serviceFacade.excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(fceAquiculturaLicencaLocalizacaoGeografica);
			fceAquiculturaLicencaLocalizacaoGeografica = null;
			super.exibirMensagem005();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a localização cadastrada.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para remover do banco de dados os regitros daquela {@link LocalizacaoGeografica}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception 
	 * @since 26/06/2015
	 */
	protected void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		if(!Util.isNullOuVazio(localizacaoGeografica)){
			serviceFacade.excluirLocalizacaoGeograficaBy(localizacaoGeografica);
		}
	}

	/**
	 * Método para excluir a {@link LocalizacaoGeografica} que foi salva na inclusão do Arquivo Shape.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 09/06/2015
	 */
	public void excluirPoligonalCultivo(){
		try {
			if(!Util.isNullOuVazio(getAquiculturaAtividadeDTO().getListaPoligonalCultivo()) 
					&& getAquiculturaAtividadeDTO().getListaPoligonalCultivo().contains(getAquiculturaAtividadeDTO().getPoligonalCultivo())){
				getAquiculturaAtividadeDTO().getListaPoligonalCultivo().remove(getAquiculturaAtividadeDTO().getPoligonalCultivo());
			}
			if(!Util.isNullOuVazio(getAquiculturaAtividadeDTO().getPoligonalCultivo().getIdeLocalizacaoGeografica())){
				excluirLocalizacaoGeografica(getAquiculturaAtividadeDTO().getPoligonalCultivo().getIdeLocalizacaoGeografica());
				serviceFacade.excluirFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(getAquiculturaAtividadeDTO().getPoligonalCultivo());
				getAquiculturaAtividadeDTO().setPoligonalCultivo(null);
			}
			super.exibirMensagem005();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a poligonal da área do cultivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public GerenciadorAbasAtividades getAbasAtividades() {
		return abasAtividades;
	}

	/**
	 * {@link AquiculturaAtividadeDTO} da aba ativa no momento.
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 */
	public AquiculturaAtividadeDTO getAquiculturaAtividadeDTO() {
		return abasAtividades.getAtividadeDTO();
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/11/2015
	 */
	protected void getAreaPoligonalCultivo(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listaPoligonal) throws Exception {
		if(!Util.isNullOuVazio(listaPoligonal)){
			for(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo : listaPoligonal){
				BigDecimal areaPoligonal = new BigDecimal(serviceFacade.retonarAreaShapeByGeometria(poligonalCultivo.getIdeLocalizacaoGeografica()));
				poligonalCultivo.setAreaPoligonal(areaPoligonal);
			}
		}
	}

	/*
	 * Getters && Setters
	 */
	public FceAquiculturaLicenca getFceAquiculturaLicenca(){
		return licenciamentoAquiculturaController.getFceAquiculturaLicenca();
	}

	public FceAquiculturaLicencaLocalizacaoGeografica getFceAquiculturaLicencaLocalizacaoGeografica() {
		return fceAquiculturaLicencaLocalizacaoGeografica;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 27/10/2015
	 */
	private int getIndexDTO(AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum) {
		return aquiculturaTipoAtividadeEnum.getId() - 1;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 28/10/2015
	 */
	public List<AquiculturaAtividadeDTO> getListaAtividadeDTO() {
		return abasAtividades.getListaAquiculturaAtividadeDTO();
	}
	
	public List<FceAquiculturaLicencaLocalizacaoGeografica> getListaFceAquiculturaLicencaLocGeo() {
		return listaFceAquiculturaLicencaLocGeo;
	}

	public List<Tipologia> getListaTipologia() {
		return listaTipologia;
	}
	
	public boolean isAlgiculturaDaOutorga() {
		return algiculturaDaOutorga;
	}

	public boolean isAlgumaAtividadeMarcada(){
		return preencherAlgicultura || preencherCarcinicultura || preencherMalococultura || preencherPiscicultura || preencherRanicultura;
	}

	public boolean isAtividadeAlgicultura() {
		return licenciamentoAquiculturaController.isTemAlgicultura();
	}

	
	public boolean isAtividadeCarcinicultura() {
		return licenciamentoAquiculturaController.isTemCarcinicultura();
	}
	
	public boolean isAtividadeMalococultura() {
		return licenciamentoAquiculturaController.isTemMalococultura();
	}
	

	public boolean isAtividadePsicultura() {
		return licenciamentoAquiculturaController.isTemPiscicultura();
	}
	
	public boolean isAtividadeRanicultura() {
		return licenciamentoAquiculturaController.isTemRanicultura();
	}

	public boolean isCarciniculturaDaOutorga() {
		return carciniculturaDaOutorga;
	}

	/*
	 * Flag's
	 */
	@Override
	public boolean isFceSalvo(){
		return licenciamentoAquiculturaController.isFceSalvo();
	}

	public boolean isEspecieAdicionada(){
		return !Util.isNull(getAquiculturaAtividadeDTO()) && !Util.isNullOuVazio(getAquiculturaAtividadeDTO().getListaCaracterizacaoCultivo());
	}

	public boolean isLocalizacaoAdicionada(){
		return !Util.isNullOuVazio(listaFceAquiculturaLicencaLocGeo);
	}
	
	public boolean isMalococulturaDaOutorga() {
		return malococulturaDaOutorga;
	}

	public boolean isOutrasEspecies() {
		return outrasEspecies;
	}		
	
	public boolean isOutrosOrganismos() {
		return outrosOrganismos;
	}
	
	
	public boolean isOutrosTipoInstalacao() {
		return outrosTipoInstalacao;
	}
	
	public abstract boolean isPermitirCadastroOutorgaAbaAtiva();
	
	public boolean isPisciculturaDaOutorga() {
		return pisciculturaDaOutorga;
	}
	
	public boolean isPoligonalAdicionada(){
		return !Util.isNull(getAquiculturaAtividadeDTO()) && !Util.isNullOuVazio(getAquiculturaAtividadeDTO().getListaPoligonalCultivo());
	}
	
	public boolean isPreencherAlgicultura() {
		return preencherAlgicultura;
	}
	
	public boolean isPreencherCarcinicultura() {
		return preencherCarcinicultura;
	}

	public boolean isPreencherMalococultura() {
		return preencherMalococultura;
	}
	
	public boolean isPreencherPiscicultura() {
		return preencherPiscicultura;
	}
	
	public boolean isPreencherRanicultura() {
		return preencherRanicultura;
	}

	public boolean isRaniculturaDaOutorga() {
		return raniculturaDaOutorga;
	}

	public boolean isTipologiaBlocked(){
		return !Util.isNullOuVazio(listaTipologia) && listaTipologia.size() == 1;
	}

	@Override
	public void limpar(){
		listaFceAquiculturaEspecie = null;
		listaFceAquiculturaLicencaLocGeo = null;
		fceAquiculturaLicencaLocalizacaoGeografica = null;
		listaTipologia = null;
		abasAtividades = null;
		preencherPiscicultura = false;
		preencherCarcinicultura = false;
		preencherRanicultura = false;
		preencherAlgicultura = false;
		preencherMalococultura = false;
		outrosOrganismos = false;
		outrosTipoInstalacao = false;
		outrasEspecies = false;
	}
	
	private void limparDTO(AquiculturaAtividadeDTO dto){
		dto.setListaCaracterizacaoCultivo(new ArrayList<FceAquiculturaLicencaTipoAtividade>());
		dto.setListaEspecie(new ArrayList<EspecieAquiculturaTipoAtividade>());
		dto.setListaEspecieSelecionada(new ArrayList<EspecieAquiculturaTipoAtividade>());
		dto.setTipoAtividade(new AquiculturaTipoAtividade());
		dto.setListaPoligonalCultivo(new ArrayList<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica>());
		dto.setPoligonalCultivo(new FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica());
		dto.setSomatorioAreaPoligonal(new BigDecimal(0));
		dto.setSomatorioAreaCultivo(null);
		dto.setSomatorioVolume(null);
		dto.setListaTipoLocalizacaoCultivo(new ArrayList<TipoLocalizacaoCultivo>());
	}

	private void limparDTOBy(AquiculturaTipoAtividadeEnum atividadeEnum){
		limparDTO(getListaAtividadeDTO().get(getIndexDTO(atividadeEnum)));
		for(AquiculturaAtividadeDTO atividadeDTO : getListaAtividadeDTO()){
			if(!Util.isNullOuVazio(atividadeDTO.getTipoAtividade())){
				atualizarAtividadeTab(atividadeDTO.getTipoAtividade().getIdeAquiculturaTipoAtividade());
				break;
			} 
		}
		atualizarAtividadeTab(null);
	}
	
	private void listarAquiculturaAtividadesDaOutorga() throws Exception{
		listaAquiculturaTipoAtividadesPreenchidas = listarAquiculturaTipoAtividadePreenchidos();
	}

	protected abstract List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadePreenchidos() throws Exception;

	public void listarDependenciaEngorda(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		try {
			if(fceAquiculturaLicencaTipoAtividade.isProducaoEngorda() && Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda())){
				serviceFacade.listarDependentesEngorda(fceAquiculturaLicencaTipoAtividade);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações de produção para Engorda / Crescimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void listarDependenciaFormaJovem(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		try {
			if(fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem() && Util.isNull(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem())){
				serviceFacade.listarDependentesFormasJovens(fceAquiculturaLicencaTipoAtividade);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações de produção para Formas Jovens.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/11/2015
	 */
	protected List<EspecieAquiculturaTipoAtividade> listarEspecieBy(AquiculturaTipoAtividade aquiculturaAtividade) throws Exception {
		return serviceFacade.listarEspecieAquiculturaTivoAtividadeByTipoAtividade(aquiculturaAtividade);
	}
	
	/**
	 * Método que lista todas as {@link Especie} cadastradas para {@link AquiculturaTipoAtividade} passada no parâmetro.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 07/11/2015
	 */
	private void listarEspecieByAtividadeEnum(AquiculturaAtividadeDTO dto){
		try {
			dto.setListaEspecie(listarEspecieBy(dto.getTipoAtividade()));
			atualizarAtividadeTab(dto.getTipoAtividade().getIdeAquiculturaTipoAtividade());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de espécies.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 02/06/2015
	 */
	protected abstract List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividade(AquiculturaTipoAtividadeEnum atividadeEnum) throws Exception;

	/**
	 * Método que busca a lista de {@link FceAquiculturaLicencaLocalizacaoGeografica} cadastrada na(s) <b>ABA Viveiro Escavado</b> e/ou <b>ABA Tanque Rede</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 26/06/2015
	 */
	public void listarLocalizacoesByFceAquiculturaLicenca() throws Exception{
		listaFceAquiculturaLicencaLocGeo = serviceFacade.listarFceAquiculturaLicencaLocalizacaoGeograficaBy(getFceAquiculturaLicenca(), listaTipologia);
	}

	private void marcarAtividadeBy(AquiculturaTipoAtividadeEnum atividadeEnum, Boolean preenchiNaOutorga){
		if(atividadeEnum.equals(AquiculturaTipoAtividadeEnum.PSICULTURA)){
			preencherPiscicultura = true;
			if(!Util.isNull(preenchiNaOutorga)){
				pisciculturaDaOutorga = preenchiNaOutorga;
			}
		}
		else if(atividadeEnum.equals(AquiculturaTipoAtividadeEnum.CARCINICULTURA)){
			preencherCarcinicultura = true;
			if(!Util.isNull(preenchiNaOutorga)){
				carciniculturaDaOutorga = preenchiNaOutorga;
			}
		}
		else if(atividadeEnum.equals(AquiculturaTipoAtividadeEnum.RANICULTURA)){
			preencherRanicultura = true;
			if(!Util.isNull(preenchiNaOutorga)){
				raniculturaDaOutorga = preenchiNaOutorga;
			}
		}
		else if(atividadeEnum.equals(AquiculturaTipoAtividadeEnum.ALGICUTURA)){
			preencherAlgicultura = true;
			if(!Util.isNull(preenchiNaOutorga)){
				algiculturaDaOutorga = preenchiNaOutorga;
			}
		}
		else if(atividadeEnum.equals(AquiculturaTipoAtividadeEnum.MALOCOCULTURA)){
			preencherMalococultura = true;
			if(!Util.isNull(preenchiNaOutorga)){
				malococulturaDaOutorga = preenchiNaOutorga;
			}
		}
	}

	protected void montarEspeciesSelecionadasInLicenca(AquiculturaAtividadeDTO dto, List<FceAquiculturaLicencaTipoAtividade> listaCaracterizacaoCultivo){
		dto.setListaEspecieSelecionada(new ArrayList<EspecieAquiculturaTipoAtividade>());
		for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade : listaCaracterizacaoCultivo){
			if(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade().equals(dto.getTipoAtividade())){
				dto.getListaEspecieSelecionada().add(fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade());
			}
		}
		comportamento.adicionarEspecie(dto, getFceAquiculturaLicenca());
	}

	private void montarListaEspeciesSelecionadasInOutorga(AquiculturaAtividadeDTO dto){
		// Caso essa atividade tenha espécies cadastradas no FCE - Outorga para Aquicultura, deve-se adicioná-las para Caraceterização de Cultivo. 
		dto.setListaEspecieSelecionada(new ArrayList<EspecieAquiculturaTipoAtividade>());
		for(FceAquiculturaEspecie fceAquiculturaEspecie : this.listaFceAquiculturaEspecie){
			if(fceAquiculturaEspecie.getIdeEspecieAquiculturaTipoAtividade().getIdeAquiculturaTipoAtividade().equals(dto.getTipoAtividade())){
				if(!dto.getListaEspecieSelecionada().contains(fceAquiculturaEspecie.getIdeEspecieAquiculturaTipoAtividade())){
					dto.getListaEspecieSelecionada().add(fceAquiculturaEspecie.getIdeEspecieAquiculturaTipoAtividade());
				}
			}
		}
		comportamento.adicionarEspecie(dto, getFceAquiculturaLicenca());
	}

	/**
	 * Método que prepara o cadastro das informações que não foram feitas no <b>FCE - Outorga para Aquicultura</b>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 07/08/2015
	 */
	public void prepararCadastroDasAquiculturaOutorga() {
		try {
			listarTipologiaToOutorga();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a(s) tipologia(s).");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void prepararDTOBy(AquiculturaAtividadeDTO dto, AquiculturaTipoAtividadeEnum atividadeEnum){
		if(Util.isNull(dto)){
			dto = getListaAtividadeDTO().get(getIndexDTO(atividadeEnum));
		}
		dto.setTipoAtividade(new AquiculturaTipoAtividade(atividadeEnum));
		listarEspecieByAtividadeEnum(dto);
		if(!dto.isPodePreencherNaLicenca()){
			montarListaEspeciesSelecionadasInOutorga(dto);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 23/10/2015
	 */
	private void prepararGerenciadorAbasAtividade() {
		if(Util.isNull(abasAtividades)){
			abasAtividades = new GerenciadorAbasAtividades();
		}
		int tamanhoArray = 5;
		abasAtividades.setListaAquiculturaAtividadeDTO(new ArrayList<AquiculturaAtividadeDTO>(tamanhoArray));
		int index = 0;
		for (index = 0; index <= tamanhoArray; index++) {
			getListaAtividadeDTO().add(new AquiculturaAtividadeDTO());
		}
	}

	public abstract void salvarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception;

	protected void salvarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade, TipoAquiculturaEnum tipoAquiculturaEnum){
		try {
			if(validarCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade, tipoAquiculturaEnum)){
				boolean isEdicao = !Util.isNull(fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicencaTipoAtividade());
				if(Util.isNull(fceAquiculturaLicencaTipoAtividade.getIdeTipoAquicultura())){
					fceAquiculturaLicencaTipoAtividade.setIdeTipoAquicultura(new TipoAquicultura(tipoAquiculturaEnum.getId()));
				}
				serviceFacade.salvarCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade);
				abasAtividades.obterSomatorioDaAtividade(null);
				if(!isEdicao){
					licenciamentoAquiculturaController.exibirMensagem001();
				} else {
					licenciamentoAquiculturaController.exibirMensagem002();
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a caracterização do cultivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void setAbasAtividades(GerenciadorAbasAtividades abasAtividades) {
		this.abasAtividades = abasAtividades;
	}

	public void setFceAquiculturaLicencaLocalizacaoGeografica(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica) {
		this.fceAquiculturaLicencaLocalizacaoGeografica = fceAquiculturaLicencaLocalizacaoGeografica;
	}

	public void setListaFceAquiculturaLicencaLocGeo(List<FceAquiculturaLicencaLocalizacaoGeografica> listaFceAquiculturaLicencaLocGeo) {
		this.listaFceAquiculturaLicencaLocGeo = listaFceAquiculturaLicencaLocGeo;
	}

	public void setPreencherAlgicultura(boolean preencherAlgicultura) {
		this.preencherAlgicultura = preencherAlgicultura;
	}

	public void setPreencherCarcinicultura(boolean preencherCarcinicultura) {
		this.preencherCarcinicultura = preencherCarcinicultura;
	}

	public void setPreencherMalococultura(boolean preencherMalococultura) {
		this.preencherMalococultura = preencherMalococultura;
	}

	public void setPreencherPiscicultura(boolean preencherPiscicultura) {
		this.preencherPiscicultura = preencherPiscicultura;
	}

	public void setPreencherRanicultura(boolean preencherRanicultura) {
		this.preencherRanicultura = preencherRanicultura;
	}
	
	private void povoarListaDTOnaoPreenchido(List<AquiculturaAtividadeDTO> listaDtos, AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum){
		boolean semAtividade = true;
		for(AquiculturaAtividadeDTO dto : getListaAtividadeDTO()){
			if(!Util.isNull(dto.getTipoAtividade()) && dto.getTipoAtividade().equals(new AquiculturaTipoAtividade(aquiculturaTipoAtividadeEnum))){
				semAtividade = false;
			}
		}
		if(semAtividade){
			listaDtos.add(new AquiculturaAtividadeDTO(aquiculturaTipoAtividadeEnum));
		}
	}
	
	public List<AquiculturaAtividadeDTO> verificarDTOsNaoPreenchidos(){
		List<AquiculturaAtividadeDTO> dtosNaoPreenchidos = new ArrayList<AquiculturaAtividadeDTO>();
		if(licenciamentoAquiculturaController.isTemAlgicultura()){
			povoarListaDTOnaoPreenchido(dtosNaoPreenchidos, AquiculturaTipoAtividadeEnum.ALGICUTURA);
		}
		if(licenciamentoAquiculturaController.isTemCarcinicultura()){
			povoarListaDTOnaoPreenchido(dtosNaoPreenchidos, AquiculturaTipoAtividadeEnum.CARCINICULTURA);
		}
		if(licenciamentoAquiculturaController.isTemMalococultura()){
			povoarListaDTOnaoPreenchido(dtosNaoPreenchidos, AquiculturaTipoAtividadeEnum.MALOCOCULTURA);
		}
		if(licenciamentoAquiculturaController.isTemPiscicultura()){
			povoarListaDTOnaoPreenchido(dtosNaoPreenchidos, AquiculturaTipoAtividadeEnum.PSICULTURA);
		}
		if(licenciamentoAquiculturaController.isTemRanicultura()){
			povoarListaDTOnaoPreenchido(dtosNaoPreenchidos, AquiculturaTipoAtividadeEnum.RANICULTURA);
		}
		return dtosNaoPreenchidos;
	}

	/**
	 * <b>RN 00138 – Área do Cultivo</b>
	 * <i>
	 * O sistema deverá permitir o cadastro da área de cultivo em hectares (ha), com quatro casas decimais, para cada espécie selecionada. 
	 * O somatório das áreas da atividade não poderá ser superior ao somatório das áreas das poligonais de cultivo.
	 * </i>
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 27/10/2015
	 */
	private boolean validarAreaCultivo(AquiculturaAtividadeDTO dto, BigDecimal areaCultivoPoligonal){
		if(!Util.isNullOuVazio(dto.getSomatorioAreaCultivo()) && !Util.isNullOuVazio(areaCultivoPoligonal)){
			if(dto.getSomatorioAreaCultivo().compareTo(areaCultivoPoligonal) == 1){
				return false;
			}
		} 
		return true;
	}

	private boolean validarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade, TipoAquiculturaEnum tipoAquiculturaEnum){
		boolean valido = true;
		if(!fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem() && !fceAquiculturaLicencaTipoAtividade.isProducaoEngorda()){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Tipo de Produção.");
		}
		else {
			if(fceAquiculturaLicencaTipoAtividade.isProducaoFormaJovem()){
				if(!comportamento.validarCaracterizacaoCultivoFormasJovens(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem(), tipoAquiculturaEnum)){
					valido = false;
				} else {
					if(fceAquiculturaLicencaTipoAtividade.isTipoInstalacaoOutros(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOFormaJovem())){
						outrosTipoInstalacao = true;
					}
					if(fceAquiculturaLicencaTipoAtividade.isOrganismosOutros()){
						outrosOrganismos = true;
					}
				}
			}
			if(fceAquiculturaLicencaTipoAtividade.isProducaoEngorda()){
				if(!comportamento.validarCaracterizacaoCultivoEngorda(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda(), tipoAquiculturaEnum)){
					valido = false;
				} else {
					if(fceAquiculturaLicencaTipoAtividade.isTipoInstalacaoOutros(fceAquiculturaLicencaTipoAtividade.getAquiculturaProducaoDTOEngorda())){
						outrosTipoInstalacao = true;
					}
				}
			}
		}
		return valido;
	}

	/**
	 * Método que valida a {@link FceAquiculturaLicencaLocalizacaoGeografica}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 26/06/2015
	 */
	private boolean validarLocalizacao(FceAquiculturaLicencaLocalizacaoGeografica intervencao){
		boolean valido = true;
		if(Util.isNullOuVazio(intervencao.getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("A coordenada dos pontos " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(intervencao.getNomRio()) && !intervencao.isSemCorpoHidrico()){
			JsfUtil.addErrorMessage("O nome do corpo hídrico " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(intervencao.getIdeTipologia())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma tipologia.");
			valido = false;
		}
		return valido;
	}

	protected boolean validarLocalizacaoCadastrada(){
		if(Util.isNullOuVazio(listaFceAquiculturaLicencaLocGeo)){
			return false;
		} 
		else {
			for(FceAquiculturaLicencaLocalizacaoGeografica captacaoIntervencao : listaFceAquiculturaLicencaLocGeo){
				if(!captacaoIntervencao.isConfirmado()){
					return false;
				}
			}
		}
		return true;
	}

	protected boolean validarTipoAtividade(AquiculturaAtividadeDTO atividadeDTO, TipoAquiculturaEnum tipoAquiculturaEnum){
		boolean poligonalValida = true;
		boolean caracterizacaoValida = true;
		BigDecimal somatorioAreaPoligonal = new BigDecimal(0);

		if(Util.isNullOuVazio(atividadeDTO.getListaPoligonalCultivo())){
			JsfUtil.addErrorMessage("A poligonal da área de cultivo " + Util.getString("lac_dadosGerais_msg003"));
			poligonalValida = false;
		} 
		else {
			for(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonal : atividadeDTO.getListaPoligonalCultivo()){
				if(!poligonal.isShapeSalvo()) {
					JsfUtil.addErrorMessage("O arquivo Shape da poligonal da área do cultivo " + Util.getString("msg_generica_preenchimento_obrigatorio"));
					poligonalValida = false;
					break;
				} 
				else {
					// ESSA VALIDAÇÃO SÓ É NECESSÁRIA NA ABA DE VIVEIRO ESCAVADO.
					if(licenciamentoAquiculturaController.isAbaViveiroAtiva() && !verificarPoligonalNoEmpreendimento(poligonal)){
						JsfUtil.addErrorMessage("A poligonal da área do cultivo deve estar dentro do limite do empreendimento.");
						poligonalValida = false;
						break;
					} 
					else {
						if(poligonal.getAreaPoligonal().compareTo(getFceAquiculturaLicenca().getNumAreaOcupada()) == 1){
							JsfUtil.addErrorMessage("A poligonal da área do cultivo não pode ter área superior ao limite do empreendimento.");
							poligonalValida = false;
							break;
						} 
						else {
							somatorioAreaPoligonal = somatorioAreaPoligonal.add(poligonal.getAreaPoligonal());
						}
					}
				}
			}
		}
		if(Util.isNullOuVazio(atividadeDTO.getListaCaracterizacaoCultivo())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma espécie.");
			caracterizacaoValida = false;
		} 
		else {
			for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : atividadeDTO.getListaCaracterizacaoCultivo()){
				if(!caracterizacaoCultivo.isOutrasEspecies()){
					if(!caracterizacaoCultivo.isProducaoFormaJovem() && !caracterizacaoCultivo.isProducaoEngorda()){
						JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Tipo de Produção.");
						caracterizacaoValida = false;
					} 
					else {
						if(!validarCaracterizacaoCultivo(caracterizacaoCultivo, tipoAquiculturaEnum)){
							caracterizacaoValida = false;
						}
					}
					if(!caracterizacaoValida){
						break;
					}
				} else {
					outrasEspecies = true;
				}
			}
			if(caracterizacaoValida && poligonalValida){
				if(!validarAreaCultivo(atividadeDTO, somatorioAreaPoligonal)){
					JsfUtil.addErrorMessage(Util.getString("fce_lic_aqui_somatorio_area_cultivo"));
					return false;
				}
			}

		}
		return poligonalValida && caracterizacaoValida;
	}

	protected abstract boolean validarVolumeCultivo(CaracterizacaoCultivoDTO dto);

	@Override
	public void verificarEdicao() {

	}

	/**
 	 * <b>RN 00131 – Poligonal do Cultivo</b>
 	 * <br>
	 * <i>
	 * O sistema irá permitir o cadastro de uma ou mais Poligonal(ais) de Cultivo, mesmo sobrepostas, por atividade selecionada.
	 * A poligonal inserida na aba Viveiro Escavado deverá estar contida na “Poligonal do Perímetro Externo do Empreendimento”, não podendo ter área superior à área do empreendimento.
	 * </i>
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/11/2015
	 */
	protected boolean verificarPoligonalNoEmpreendimento(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonal){
		try{
			return serviceFacade.isValidarPoligonalCultivoInEmpreendimento(poligonal, licenciamentoAquiculturaController.getEmpreendimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao verificar a sobreposição da poligonal da área de cultivo no empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String visualizarLocalizacao(FceAquiculturaLicencaLocalizacaoGeografica intervencao){
		if(!Util.isNullOuVazio(intervencao.getIdeLocalizacaoGeografica())){
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(intervencao.getIdeLocalizacaoGeografica());
		}
		return "";
	}

	/**
	 * Método que retorna a URL para visualizar a <i>Poligonal da Área do Cultivo</i> no GEOBAHIA.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicencaLocalizacaoGeografica
	 * @return
	 * @since 09/06/2015
	 */
	public String visualizarPoligonalCultivo(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(poligonalCultivo.getIdeLocalizacaoGeografica());
	}

	public void voltarAba(){
		licenciamentoAquiculturaController.voltarAba();
	}

	@Override
	public void abrirDialog() {
		licenciamentoAquiculturaController.abrirDialog();
	}


	@Override
	protected void carregarFceTecnico() {
		// super.carregarFceDoTecnico(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_AQUICULTURA.getId()));
	}
}