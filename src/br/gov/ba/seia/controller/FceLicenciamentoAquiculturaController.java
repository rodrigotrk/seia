package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceLicenciamentoAquiculturaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pela navegação entre as abas do <b>FCE - Licenciamento para Aquicultura</b>.
 * <pre><b> #6934 </b></pre>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 27/05/2015
 */
@Named("fceLicenciamentoAquiculturaController")
@ViewScoped
public class FceLicenciamentoAquiculturaController extends FceComDocumentoAdicionalController {

	@Inject
	private FceLicenciamentoAquiculturaNavegacaoController navegacaoController;

	@EJB
	protected FceLicenciamentoAquiculturaServiceFacade serviceFacade;

	private static final DocumentoObrigatorio DOCUMENTO_LICENCIAMENTO_AQUICULTURA = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_AQUICULTURA.getId());

	private static final DocumentoObrigatorio DOCUMENTO_LICENCIAMENTO_AQUICULTURA_ADICIONAIS = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_AQUICULTURA_ADICIONAIS.getId());

	private Empreendimento empreendimento;

	private FceAquiculturaLicenca fceAquiculturaLicenca;

	private Boolean shapeEmpreendimentoSalvo;

	private Boolean shapeGalpaoSalvo;

	private boolean comFceOutorgaAquiculturaViveiroEscavado;

	private boolean comFceOutorgaAquiculturaTanqueRede;

	private boolean edicao;

	private List<Tipologia> listaTipologiaFromEmpreendimento;

	private List<AquiculturaTipoAtividadeEnum> listaAtividadeEnum;

	private boolean temPiscicultura;
	private boolean temCarcinicultura;
	private boolean temRanicultura;
	private boolean temAlgicultura;
	private boolean temMalococultura;
	
	private boolean emAnaliseTecnica;
	private boolean emDuplicacao;
	private AnaliseTecnica analiseTecnica;

	@Override
	public void init(){
		limpar();
		if(!isEmAnaliseTecnica()){
			if(!isFceLicenciamentoSemImpedimento()){
				JsfUtil.addWarnMessage(Util.getString("fce_lic_aqui_com_outorga"));
			}
			else {
				carregarAba();
				verificarEdicao();
				if(!super.isFceSalvo()){
					edicao = false;
					super.iniciarFce(DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
					if(Util.isNull(fceAquiculturaLicenca)){
						fceAquiculturaLicenca = new FceAquiculturaLicenca(super.fce);
					}
				}
				else {
					prepararEdicao();
					edicao = (fceAquiculturaLicenca.getIndAquiculturaTanqueRede() || fceAquiculturaLicenca.getIndAquiculturaViveiroEscavado());
				}
			}
			abrirDialog();
		} 
		else {
			abrirFceTecnico();
		}
	}

	public void abrirFceTecnico() {
		edicao = true;
		if(isEmDuplicacao()){
			super.carregarFceDoRequerente(DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
		}
		else {
			super.carregarFceDoTecnico(DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
		}
		carregarAba();
		prepararEdicao();
	}
	
	protected void renderizarAbaViveiro(){
		navegacaoController.renderizarAbaViveiro();
	}
	
	protected void renderizarAbaTanque(){
		navegacaoController.renderizarAbaTanque();
	}
	
	/**
	 * Método que carrega o {@link Empreendimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 */
	private void carregarEmpreendimento() throws Exception{
		empreendimento = serviceFacade.buscarEmpreendimento(requerimento);
	}

	/**
	 * Método que lista as {@link Tipologia} do {@link Empreendimento} e monta a lista de {@link AquiculturaTipoAtividadeEnum}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 26/06/2015
	 */
	public void listarTipologiaAndAtividadeEnum() throws Exception{
		listarTipologiasDoEmpreendimento();
		montarListaAtividadeEnum();
	}

	/**
	 * Método para listar as {@link Tipologia} que foram cadastradas no {@link Empreendimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 26/06/2015
	 */
	private void listarTipologiasDoEmpreendimento() throws Exception{
		if(Util.isNullOuVazio(listaTipologiaFromEmpreendimento)){
			listaTipologiaFromEmpreendimento = serviceFacade.listarTipologiasDoRequerimento(super.requerimento);
		}
	}

	public boolean isTheGeomPersistidoEmEmpreendimento(){
		if(isClassificacaoSecaoShape(empreendimento.getIdeLocalizacaoGeografica())){
			if(Util.isNull(shapeEmpreendimentoSalvo) || !shapeEmpreendimentoSalvo){
				shapeEmpreendimentoSalvo = isTheGeomPersistido(empreendimento);
			}
			return shapeEmpreendimentoSalvo;
		}
		else {
			return false;
		}
	}

	public boolean isTheGeomPersistidoEmGalpao(FceLocalizacaoGeografica fceLocalizacaoGeografica){
		if(isClassificacaoSecaoShape(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica())){
			if(Util.isNull(shapeGalpaoSalvo) || !shapeGalpaoSalvo){
				shapeGalpaoSalvo = isTheGeomPersistido(fceLocalizacaoGeografica);
			}
			return shapeGalpaoSalvo;
		}
		else {
			return false;
		}
	}

	public boolean isTheGeomPersistido(Object object) {
		try {
			LocalizacaoGeografica localizacaoGeografica = null;
			if(object instanceof Empreendimento){
				localizacaoGeografica = ((Empreendimento) object).getIdeLocalizacaoGeografica();
				if(serviceFacade.isLocGeoComShapePersistido(localizacaoGeografica)){
					if(!Util.isNull(fceAquiculturaLicenca)){
						fceAquiculturaLicenca.setNumAreaOcupada((BigDecimal.valueOf(serviceFacade.retonarAreaShapeByGeometria(localizacaoGeografica))));
					}
					return true;
				}
			}
			else {
				if(object instanceof FceLocalizacaoGeografica){
					localizacaoGeografica = ((FceLocalizacaoGeografica) object).getIdeLocalizacaoGeografica();
				}
				else if(object instanceof FceAquiculturaLicencaLocalizacaoGeografica){
					localizacaoGeografica = ((FceAquiculturaLicencaLocalizacaoGeografica) object).getIdeLocalizacaoGeografica();
				}
				return serviceFacade.isTheGeomPersistido(localizacaoGeografica);
			}
			return false;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a geometria da Poligonal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	public boolean isEmpreendimentoCarregado() {
		return !Util.isNull(empreendimento) && !Util.isNullOuVazio(empreendimento.getIdeLocalizacaoGeografica());
	}

	public boolean isClassificacaoSecaoShape(LocalizacaoGeografica localizacaoGeografica){
		if(!Util.isNull(localizacaoGeografica)){
			return localizacaoGeografica.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
		}
		return false;
	}

	/**
	 * Método que imprime o relatório do {@link FceAquicultura}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 * @return
	 * @throws Exception
	 */
	public StreamedContent getImprimirRelatorio(){
		try {
			return getImprimirRelatorio(super.fce, DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Licenciamento_Aquicultura.doc", "Informações Adicionais - FCE Licenciamento para Aquicultura.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public int getSomenteShape() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	@Override
	public void verificarEdicao() {
		super.carregarFceDoRequerente(DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
	}
	
	@Override
	public void carregarAba() {
		try {
			carregarEmpreendimento();
			listarTipologiaAndAtividadeEnum();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void prepararEdicao(){
		try {
			carregarFceAquiculturaLicenca();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFceAquiculturaLicenca() throws Exception{
		fceAquiculturaLicenca = serviceFacade.buscarFceAquiculturaLicencaByFce(getFce());
		
		if(!Util.isNull(fceAquiculturaLicenca.getIdeDocumentoObrigatorioRequerimento())){
			super.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(fceAquiculturaLicenca.getIdeDocumentoObrigatorioRequerimento());
		}
	}

	/**
	 * Verifica se é possível preencher o <b>FCE - Licenciamento para Aquicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 01/06/2015
	 */
	private boolean isFceLicenciamentoSemImpedimento(){
		try {
			return serviceFacade.podeResponderFceLicenciamentoParaAquicultura(super.requerimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean isAbaTanqueAtiva() {
		return navegacaoController.isAbaTanqueRedeAtiva();
	}

	public boolean isAbaViveiroAtiva() {
		return navegacaoController.isAbaViveiroEscavadoAtiva();
	}

	public boolean cadastrarViveiro(){
		return !comFceOutorgaAquiculturaViveiroEscavado && isAquiculturaEmViveiroEscavado();
	}

	public boolean cadastrarTanque(){
		return !comFceOutorgaAquiculturaTanqueRede && isAquiculturaEmTanqueRede();
	}

	public boolean isAquiculturaEmViveiroEscavado(){
		return !Util.isNull(fceAquiculturaLicenca) && !Util.isNull(fceAquiculturaLicenca.getIndAquiculturaViveiroEscavado()) && fceAquiculturaLicenca.getIndAquiculturaViveiroEscavado();
	}

	public boolean isAquiculturaEmTanqueRede(){
		return !Util.isNull(fceAquiculturaLicenca) && !Util.isNull(fceAquiculturaLicenca.getIndAquiculturaTanqueRede()) && fceAquiculturaLicenca.getIndAquiculturaTanqueRede();
	}

	@Override
	public void finalizar() {
		try {
			serviceFacade.finalizar(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		boolean finalizado = true;
		boolean outrosOrganismo = false;
		boolean outrosTipoInstalacao = false;
		boolean outrasEspecies = false;
		boolean outrosCultivos = false;
		
		List<AquiculturaAtividadeDTO> dtosViveiro = null;
		List<AquiculturaAtividadeDTO> dtosTanque = null;
		
		// Finaliza Aba Dados Gerais
		FceLicenciamentoAquiculturaDadosGeraisController dadosGerais = (FceLicenciamentoAquiculturaDadosGeraisController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaDadosGeraisController}", FceLicenciamentoAquiculturaDadosGeraisController.class);
		if(dadosGerais.validarAba()){
			dadosGerais.finalizarAba();
		}
		else {
			finalizado = false;
			navegacaoController.setActiveTab(0);
			return;
		}

		// Finaliza Aba Dados Requerimento
		if(finalizado){
			FceLicenciamentoAquiculturaDadosRequerimentoController dadosRequerimento = (FceLicenciamentoAquiculturaDadosRequerimentoController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaDadosRequerimentoController}", FceLicenciamentoAquiculturaDadosRequerimentoController.class);
			if(!dadosRequerimento.validarAba()){
				finalizado = false;
				navegacaoController.setActiveTab(1);
			}
		}

		// Finaliza Aba Viveiro Escavado
		if(finalizado){
			ViveiroEscavadoController viveiroEscavado = (ViveiroEscavadoController) SessaoUtil.recuperarManagedBean("#{viveiroEscavadoController}", ViveiroEscavadoController.class);
			if(dadosGerais.getFceAquiculturaLicenca().getIndAquiculturaViveiroEscavado()){
				if(viveiroEscavado.validarAba()){
					dtosViveiro = viveiroEscavado.verificarDTOsNaoPreenchidos();
					viveiroEscavado.finalizar();
				}
				else {
					finalizado = false;
					navegacaoController.setActiveTab(2);
					return;
				}
				if(viveiroEscavado.isOutrosOrganismos()) {
					outrosOrganismo = true;
				}
				if(viveiroEscavado.isOutrosTipoInstalacao()){
					outrosTipoInstalacao = true;
				}
				if(viveiroEscavado.isOutrasEspecies()){
					outrasEspecies = true;
				}
			}
			else {
				dadosGerais.getFceAquiculturaLicenca().setNumVazaoCaptacao(null);
				dadosGerais.getFceAquiculturaLicenca().setNumVazaoLancamento(null);
				viveiroEscavado.excluirAba();
			}
		}

		// Finaliza Aba Tanque Rede
		if(finalizado){
			TanqueRedeController tanqueRede = (TanqueRedeController) SessaoUtil.recuperarManagedBean("#{tanqueRedeController}", TanqueRedeController.class);
			if(dadosGerais.getFceAquiculturaLicenca().getIndAquiculturaTanqueRede()){
				if(tanqueRede.validarAba()){
					dtosTanque = tanqueRede.verificarDTOsNaoPreenchidos();
					tanqueRede.finalizar();
				}
				else {
					finalizado = false;
					navegacaoController.setActiveTab(3);
					return;
				}
				if(tanqueRede.isOutrosOrganismos()) {
					outrosOrganismo = true;
				}
				if(tanqueRede.isOutrosTipoInstalacao()){
					outrosTipoInstalacao = true;
				}
				if(tanqueRede.isOutrasEspecies()){
					outrasEspecies = true;
				}
				if(tanqueRede.isOutrosLocalizacaoCultivo()){
					outrosCultivos = true;
				}
			}
			else {
				tanqueRede.excluirAba();
			}
		}

		if(!verificarSeDTOsEstaoTodosPreenchidos(dadosGerais.getFceAquiculturaLicenca().getIndAquiculturaViveiroEscavado(), dtosViveiro,
				dadosGerais.getFceAquiculturaLicenca().getIndAquiculturaTanqueRede(), dtosTanque)){
			finalizado = false;
		}
		
		// Finaliza Aba Dados Adicionais
		if(finalizado){
			if(validarAba()){
				salvarAbaAdicionais();
			}
			else {
				JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
				finalizado = false;
				navegacaoController.setActiveTab(4);
				return;
			}
		}

		// Fecha o Dialog
		if(finalizado){
			super.concluirFce();
			RequestContext.getCurrentInstance().execute("licenciamentoAquicultura.hide()");
			// Verifica se é possível imprimir relatório
			if(!outrosOrganismo && !outrosTipoInstalacao && !outrasEspecies && !outrosCultivos){
				RequestContext.getCurrentInstance().execute("rel_lic_aquicultura.show()");
			} else {
				// Exibe mensagem para "Outros" Organismos.
				if(outrosOrganismo) {
					JsfUtil.addWarnMessage(Util.getString("fce_lic_aqui_outros_organismo"));
				}
				// Exibe mensagem para "Outros" Tipo Instalção.
				if(outrosTipoInstalacao) {
					JsfUtil.addWarnMessage(Util.getString("fce_lic_aqui_outros_instalacao"));
				}
				// Exibe mensagem para "Outros" Espécies.
				if(outrasEspecies) {
					JsfUtil.addWarnMessage(Util.getString("fce_lic_aqui_outras_especie"));
				}
				// Exibe mensagem para "Outros" Localização do Cultivo.
				if(outrosCultivos){
					JsfUtil.addWarnMessage(Util.getString("fce_lic_aqui_outros_cultivo"));
				}
			}
			exibirMensagemSucesso();
		}
	}
	
	private boolean verificarSeDTOsEstaoTodosPreenchidos(boolean temViveiro, List<AquiculturaAtividadeDTO> dtosViveiro, boolean temTanque, List<AquiculturaAtividadeDTO> dtosTanque){
		if(temViveiro && temTanque){
			if(!Util.isNullOuVazio(dtosViveiro) && !Util.isNullOuVazio(dtosTanque)){
				List<String> listaNomeAtividade = new ArrayList<String>();
				for(AquiculturaAtividadeDTO dtoViveiro : dtosViveiro){
					for(AquiculturaAtividadeDTO dtoTanque : dtosTanque){
						if(dtoViveiro.getTipoAtividade().equals(dtoTanque.getTipoAtividade())){
							if(dtoViveiro.getTipoAtividade().getIdeAquiculturaTipoAtividade().equals(AquiculturaTipoAtividadeEnum.PSICULTURA.getId())){
								listaNomeAtividade.add("Piscicultura");
							}
							else if(dtoViveiro.getTipoAtividade().getIdeAquiculturaTipoAtividade().equals(AquiculturaTipoAtividadeEnum.CARCINICULTURA.getId())){
								listaNomeAtividade.add("Carcinicultura");
							}
							else if(dtoViveiro.getTipoAtividade().getIdeAquiculturaTipoAtividade().equals(AquiculturaTipoAtividadeEnum.RANICULTURA.getId())){
								listaNomeAtividade.add("Ranicultura");
							}
							else if(dtoViveiro.getTipoAtividade().getIdeAquiculturaTipoAtividade().equals(AquiculturaTipoAtividadeEnum.ALGICUTURA.getId())){
								listaNomeAtividade.add("Algicultura");
							}
							else if(dtoViveiro.getTipoAtividade().getIdeAquiculturaTipoAtividade().equals(AquiculturaTipoAtividadeEnum.MALOCOCULTURA.getId())){
								listaNomeAtividade.add("Malococultura");
							}
						}
					}
				}
				if(!Util.isNullOuVazio(listaNomeAtividade)){
					int indexUltivaAtividade = listaNomeAtividade.size()-1;
					String aquiculturaNaoCaracterizada = "";
					String s = "";
					String m = "";
					if(indexUltivaAtividade != 0){
						String ultimaAtividade = " e " + listaNomeAtividade.get(indexUltivaAtividade);
						for (int i = 0; i < indexUltivaAtividade; i++) {
							if(Util.isNullOuVazio(aquiculturaNaoCaracterizada)){
								aquiculturaNaoCaracterizada = listaNomeAtividade.get(i);
							} else {
								aquiculturaNaoCaracterizada =  aquiculturaNaoCaracterizada + ", " + listaNomeAtividade.get(i);
							}
						}
						aquiculturaNaoCaracterizada =  aquiculturaNaoCaracterizada + ultimaAtividade;
						s = "(s)";
						m = "(m)";
					}
					else {
						aquiculturaNaoCaracterizada =  listaNomeAtividade.get(indexUltivaAtividade);
					}
					JsfUtil.addErrorMessage("A"+ s +" atividade"+ s + " de "+aquiculturaNaoCaracterizada+" deve"+ m +" ser caracterizada"+ s +" em Viveiro Escavado e/ou Tanque Rede.");
					return false;
				}
			}
		} 
		else {
			String nomeAba = "";
			boolean exibeMsg = false;
		 	if(temViveiro && !Util.isNullOuVazio(dtosViveiro)){
		 		exibeMsg = true;
		 		nomeAba = "Viveiro Escavado";
			} 
		 	if(temTanque && !Util.isNullOuVazio(dtosTanque)){
		 		exibeMsg = true;
		 		nomeAba = "Tanque Rede";
			}
		 	if(exibeMsg){
		 		JsfUtil.addErrorMessage("Todas as atividades da aba "+nomeAba+" devem ser caracterizadas.");
		 		return false;
		 	}
		}
		return true;
	}

	/**
	 * Exibe mensagem de Inclusão/Alteração realizada com sucesso!
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 29/06/2015
	 */
	private void exibirMensagemSucesso() {
		if(!isFceSalvo()){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 12/06/2015
	 */
	public void salvarFceAquiculturaLicenca() throws Exception {
		serviceFacade.salvarFceAquiculturaLicenca(fceAquiculturaLicenca);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 12/06/2015
	 */
	private void salvarAbaAdicionais() throws Exception {
		super.salvarDocumentoAdicional(requerimento, DOCUMENTO_LICENCIAMENTO_AQUICULTURA_ADICIONAIS);
		fceAquiculturaLicenca.setIdeDocumentoObrigatorioRequerimento(super.getDocumentoUpado());
		salvarFceAquiculturaLicenca();
	}

	@Override
	public void limpar() {
		super.limparFce();
		empreendimento = null;
		fceAquiculturaLicenca = null;
		shapeEmpreendimentoSalvo = null;
		shapeGalpaoSalvo = null;
		listaTipologiaFromEmpreendimento = null;
		listaAtividadeEnum = null;
		comFceOutorgaAquiculturaViveiroEscavado = false;
		comFceOutorgaAquiculturaTanqueRede = false;
		edicao = false;
		temPiscicultura = false;
		temCarcinicultura = false;
		temRanicultura = false;
		temAlgicultura = false;
		temMalococultura = false;
		
		emAnaliseTecnica = false;
		emDuplicacao = false;
		
		limparAquiculturaBeans();
		navegacaoController.setActiveTab(0);
		navegacaoController.limparRendered();
	}

	public void limparAquiculturaBeans(){
		SessaoUtil.removerManagedBeanFromViewScoped("fceLicenciamentoAquiculturaDadosGeraisController");
		SessaoUtil.removerManagedBeanFromViewScoped("fceLicenciamentoAquiculturaDadosRequerimentoController");
		SessaoUtil.removerManagedBeanFromViewScoped("viveiroEscavadoController");
		SessaoUtil.removerManagedBeanFromViewScoped("tanqueRedeController");
	}

	@Override
	public boolean validarAba() {
		return super.isArquivoUpado();
	}

	/**
	 * Método que avança de aba quando ela é válida.
	 * @author eduardo.fernandes
	 * @since 01/06/2015
	 */
	public void avancarAba() {
		navegacaoController.avancarAba();
		if(navegacaoController.isAbaViveiroEscavadoAtiva() && !isAquiculturaEmViveiroEscavado()){
			navegacaoController.avancarAba();
		}
		if(isAbaTanqueAtiva() && !isAquiculturaEmTanqueRede()){
			navegacaoController.avancarAba();
		}
	}

	public void voltarAba(){
		navegacaoController.voltarAba();
		if(isAbaTanqueAtiva() && !isAquiculturaEmTanqueRede()){
			navegacaoController.voltarAba();
		}
		if(isAbaViveiroAtiva() && !isAquiculturaEmViveiroEscavado()){
			navegacaoController.voltarAba();
		}
	}

	/**
	 * Método que verifica se as {@link Tipologia} do {@link Requerimento} são relacionadas à <b>Aquicultura</b>, <b>Criação de Animais</b> e/ou <b>Carcinicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/02/2015
	 * @return true or false
	 * @see Melhoria #6590 <br> Mesmo sem o preenchimento da <i>Etapa 7</i> o sistema está salvando {@link RequerimentoTipologia}, portanto faz-se necessário checar o preenchimento de <i>tipologia.getValAtividade()</i>
	 */
	private void montarListaAtividadeEnum(){
		if(!isEmpreendimentoSemTipologia() && !isExisteTipologiaFromEtapa7()){
			listaAtividadeEnum = new ArrayList<AquiculturaTipoAtividadeEnum>();
			for(Tipologia tipologia : listaTipologiaFromEmpreendimento){
				if(!Util.isNullOuVazio(tipologia.getValAtividade()) && isTipologiaPaiEqualsToAquiculturaOrCriacaoAnimaisOrCarcinicultura(tipologia)){
					AquiculturaTipoAtividadeEnum enumTemp = getAtividadeEnum(tipologia);
					if(!Util.isNull(enumTemp) && !listaAtividadeEnum.contains(enumTemp)){
						listaAtividadeEnum.add(enumTemp);
					}
				}
			}
		}
	}

	/**
	 * Método que verifica se a lista de {@link Tipologia} está carregada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return true se <i>listaTipologia</i> estiver carregada
	 */
	private boolean isEmpreendimentoSemTipologia(){
		return Util.isNullOuVazio(listaTipologiaFromEmpreendimento);
	}

	/**
	 * Método que verifica se as {@link Tipologia}Pai da {@link Tipologia} é <b>Aquicultura</b> ou <b>Criação de Animais</b> ou <b>Carcinicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param tipologia
	 * @return <i>true</i> se a {@link Tipologia}Pai for <b>Aquicultura</b> ou <b>Criação de Animais</b>
	 */
	private boolean isTipologiaPaiEqualsToAquiculturaOrCriacaoAnimaisOrCarcinicultura(Tipologia tipologia) {
		return tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.AQUICULTURA.getId())
				|| tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CRIACAO_DE_ANIMAIS.getId())
				|| tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CARCINICULTURA.getId())
				&& !tipologia.getIdeTipologiaPai().getIdeTipologia().equals(TipologiaEnum.CRIACAO_CONFINADA.getId());
	}

	/**
	 * Método para tratar a lista de {@link AquiculturaTipoAtividadeEnum} de acordo com as {@link Tipologia} do {@link RequerimentoTipologia}, cadastradas na <i>Etapa 7</i>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return listaAtividadeEnum
	 */
	private AquiculturaTipoAtividadeEnum getAtividadeEnum(Tipologia tipologia){
		if(tipologia.getCodTipologia().compareTo("A2.3.1") == 0 || tipologia.getCodTipologia().compareTo("A2.3.2") == 0 || tipologia.getCodTipologia().compareTo("A2.3.3") == 0){
			temPiscicultura = true;
			return AquiculturaTipoAtividadeEnum.PSICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.4.1") == 0 || tipologia.getCodTipologia().compareTo("A2.4.2") == 0){
			temCarcinicultura = true;
			return AquiculturaTipoAtividadeEnum.CARCINICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.5") == 0){
			temRanicultura = true;
			return AquiculturaTipoAtividadeEnum.RANICULTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.6") == 0){
			temAlgicultura = true;
			return AquiculturaTipoAtividadeEnum.ALGICUTURA;
		}
		else if(tipologia.getCodTipologia().compareTo("A2.7") == 0){
			temMalococultura = true;
			return AquiculturaTipoAtividadeEnum.MALOCOCULTURA;
		}
		else {
			return null;
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 */
	public boolean isExisteTipologiaFromEtapa7() {
		return !Util.isNullOuVazio(listaAtividadeEnum);
	}

	/*
	 * getters and setters
	 */
	public FceAquiculturaLicenca getFceAquiculturaLicenca() {
		return fceAquiculturaLicenca;
	}

	public void setFceAquiculturaLicenca(FceAquiculturaLicenca fceAquiculturaLicenca) {
		this.fceAquiculturaLicenca = fceAquiculturaLicenca;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public boolean isComFceOutorgaAquiculturaViveiroEscavado() {
		return comFceOutorgaAquiculturaViveiroEscavado;
	}

	public void setComFceOutorgaAquiculturaViveiroEscavado(boolean comFceOutorgaAquicultura) {
		this.comFceOutorgaAquiculturaViveiroEscavado = comFceOutorgaAquicultura;
	}

	@Override
	public boolean isFceSalvo() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public List<Tipologia> getListaTipologiaFromEmpreendimento() {
		return listaTipologiaFromEmpreendimento;
	}

	public List<AquiculturaTipoAtividadeEnum> getListaAtividadeEnum() {
		return listaAtividadeEnum;
	}

	public boolean isTemPiscicultura() {
		return temPiscicultura;
	}

	public boolean isTemCarcinicultura() {
		return temCarcinicultura;
	}

	public boolean isTemRanicultura() {
		return temRanicultura;
	}

	public boolean isTemAlgicultura() {
		return temAlgicultura;
	}

	public boolean isTemMalococultura() {
		return temMalococultura;
	}

	public boolean isComFceOutorgaAquiculturaTanqueRede() {
		return comFceOutorgaAquiculturaTanqueRede;
	}

	public void setComFceOutorgaAquiculturaTanqueRede(boolean comFceOutorgaAquiculturaTanqueRede) {
		this.comFceOutorgaAquiculturaTanqueRede = comFceOutorgaAquiculturaTanqueRede;
	}


	@Override
	public void abrirDialog() {
		navegacaoController.renderizarAbas();
		RequestContext.getCurrentInstance().addPartialUpdateTarget("gridFceLicenciamentoAquicultura");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioLicenciamentoAquicultura");
		RequestContext.getCurrentInstance().execute("licenciamentoAquicultura.show()");
	}

	
	protected LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo){
		try {
			return serviceFacade.duplicarLocalizacaoGeografica(locGeo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}

	public void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica){
		super.iniciarFceTecnico(fce.getIdeDocumentoObrigatorio(), analiseTecnica);
		prepararDuplicacao();
		duplicarFce();
	}
			
	
	@Override
	protected void prepararDuplicacao() {
		fceAquiculturaLicenca.setIdeFce(super.fce);
		fceAquiculturaLicenca.setIdeFceAquiculturaLicenca(null);
	}


	 @Override
	protected void duplicarFce() {
		FceLicenciamentoAquiculturaDadosGeraisController dadosGerais = (FceLicenciamentoAquiculturaDadosGeraisController) SessaoUtil.recuperarManagedBean("#{fceLicenciamentoAquiculturaDadosGeraisController}", FceLicenciamentoAquiculturaDadosGeraisController.class);
		dadosGerais.getFceLocalizacaoGeografica().setIdeFce(getFce());
		dadosGerais.duplicarFce();

		ViveiroEscavadoController viveiroEscavado = (ViveiroEscavadoController) SessaoUtil.recuperarManagedBean("#{viveiroEscavadoController}", ViveiroEscavadoController.class);
		viveiroEscavado.duplicarFce();

		TanqueRedeController tanqueRede = (TanqueRedeController) SessaoUtil.recuperarManagedBean("#{tanqueRedeController}", TanqueRedeController.class);
		tanqueRede.duplicarFce();
		
	}

	@Override
	protected void carregarFceTecnico() {
		super.carregarFceDoTecnico(DOCUMENTO_LICENCIAMENTO_AQUICULTURA);
	}

	public boolean isEmAnaliseTecnica() {
		return emAnaliseTecnica;
	}

	public void setEmAnaliseTecnica(boolean emAnaliseTecnica) {
		this.emAnaliseTecnica = emAnaliseTecnica;
	}

	public AnaliseTecnica getAnaliseTecnica() {
		return analiseTecnica;
	}

	public void setAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		this.analiseTecnica = analiseTecnica;
	}

	public boolean isEmDuplicacao() {
		return emDuplicacao;
	}

	public void setEmDuplicacao(boolean emDuplicacao) {
		this.emDuplicacao = emDuplicacao;
	}

}