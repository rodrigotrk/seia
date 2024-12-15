package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceBarragemController;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAtoPK;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovelPK;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceOutorgaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FceIntervencaoBarragemService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceOutorgaLocalizacaoGeograficaBuilder;

/**
 * 08/01/14
 * @author eduardo.fernandes
 */
@Named("fceIntervencaoBarragemControllerSemPonto")
@ViewScoped
public class FceIntervencaoBarragemControllerSemPonto extends BaseFceBarragemController {

	@Inject
	private LocalizacaoGeograficaGenericController locGeoController;

	@EJB
	private FceIntervencaoBarragemService barragemService;
	@EJB
	private FceOutorgaServiceFacade fceOutorgaServiceFacade;
	@EJB
	private ImovelService imovelService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private EnquadramentoService enquadramentoService;

	private static DocumentoAto DOCUMENTO_ATO_BARRAGEM = new DocumentoAto(154);
	private Enquadramento enquadramento;
	private EnquadramentoDocumentoAto enquadramentoDocumentoAto;

	/**
	 * Flag a ser usado no fecharDialogExibirMsg(int) para fechar o dialog quando o tipo de Barragem for REGULARIZACAO
	 */
	private static final int DIALOG_REGULARIZACAO = 1;
	/**
	 * Flag a ser usada no fecharDialogExibirMsg(int) quando o usuÃ¡rio clica em FINALIZAR
	 */
	private static final int DIALOG_PRINCIPAL = 2;

	private static DocumentoObrigatorio DOC_OBRIGATORIO_BARRAGEM = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_INTERVENCAO_BARRAGEM_SEM_PONTO.getId());
	private static DocumentoObrigatorio DOC_OBRIGATORIO_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.ESTUDO_HIDROLOGICO.getId());

	private List<FceIntervencaoBarragem> listaFceIntervencaoBarragem;
	private FceIntervencaoBarragem intervencaoBarragem;

	private List<OutorgaLocalizacaoGeografica> listaOutorgarLocGeo;
	private OutorgaLocalizacaoGeografica outorgaLocGeoSelecionada;
	private Outorga outorga;
	private List<String> listaAno;

	private boolean modoEdicao;
	private boolean desabilitaCampos;

	private Empreendimento empreendimento;
	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeograficaParaDialog;
	private List<Imovel>listaImovel;
	private List<Imovel> listaImovelSelecionado;
	private Imovel imovelSelecionado;
	private List<TipoBarragem> listaTipoBarragem;

	@Override
	public void init() {
		intervencaoBarragem = new FceIntervencaoBarragem();
		listaFceIntervencaoBarragem = new ArrayList<FceIntervencaoBarragem>();
		iniciarOutorgaLocalizacaoGeografica();
		imovelSelecionado = new Imovel();
		carregarAba();
		verificarEdicao();
		if(!isFceSalvo()){
			iniciarFce(DOC_OBRIGATORIO_BARRAGEM);
		}
		if(!modoEdicao){
			listaOutorgarLocGeo = new ArrayList<OutorgaLocalizacaoGeografica>();
			outorga = new Outorga(new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId()), requerimento, new ModalidadeOutorga(ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade()));
		} else {
			carregarListaOutorgaLocGeo();
			if(isFceSalvo()){
				carregarListaFceIntervencao();
			}
		}
	}

	private void iniciarOutorgaLocalizacaoGeografica(){
		outorgaLocalizacaoGeograficaParaDialog = new OutorgaLocalizacaoGeografica();
		outorgaLocalizacaoGeograficaParaDialog.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
	}

	/**
	 * Método que verifica se jÃ¡ existe uma FCE cadastrada no BD para aquele Requerimento.
	 * @return !Util.isNullOuVazio(fce)
	 * @author eduardo.fernandes
	 */
	@Override
	public void verificarEdicao(){
		if(!Util.isNullOuVazio(requerimento)){
			try {
				listaOutorgarLocGeo = fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaTipoIntervencaoBarragem(super.requerimento);
				modoEdicao = !isSemPontoAdicionado();
				if(modoEdicao){
					carregarFceDoRequerente(DOC_OBRIGATORIO_BARRAGEM);
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Ocorreu um erro ao carregar a Intervenção de Barragem.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Quando existir uma FCE salva no banco, deve-se buscar o FceIntervencaoBarragem que contenha ela.
	 * @author eduardo.fernandes
	 */
	public void carregarListaFceIntervencao(){
		try {
			listaFceIntervencaoBarragem = barragemService.listarFceIntervencaoBarragemByIdeFce(super.fce);
			if(super.isFceTecnico()){
				for (FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem) {
					fceIntervencaoBarragem.setConfirmar(true);
					tratarDadoGeografico(fceIntervencaoBarragem.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					super.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(fceIntervencaoBarragem.getIdeDocumentoObrigatorioRequerimento());
				}
			}
				
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar o FCE de Intervenção em Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarFceIntervencaoAndDocumentoUpado(){
		FceIntervencaoBarragem fceIntervencaoBarragem = buscarFceIntervencaoBy(outorgaLocGeoSelecionada);
		if(Util.isNullOuVazio(fceIntervencaoBarragem)){
			intervencaoBarragem = new FceIntervencaoBarragem(super.fce, outorgaLocGeoSelecionada);
		} else {
			intervencaoBarragem = fceIntervencaoBarragem;
			try {
				carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(intervencaoBarragem.getIdeDocumentoObrigatorioRequerimento());
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE de Intervenção em Barragem.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		desabilitaCampos = true;
	}

	@Override
	public void carregarAba(){
		carregarEmpreendimento();
		iniciarAndListarImoveis();
		carregarTipoBarragem();
		listarAno();
		buscarEnquadramento();
	}

	private void carregarEmpreendimento(){
		try {
			List<Empreendimento> coll = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
			empreendimento = empreendimentoService.carregarById(coll.get(0).getIdeEmpreendimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar os dados do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void iniciarAndListarImoveis(){
		if(Util.isNullOuVazio(listaImovel)){
			listaImovel = new ArrayList<Imovel>();
		}
		listarImovel(empreendimento);
	}

	/**
	 * MetÃ³do usado para buscar a lista de imÃ³veis salvos no banco de acordo com o empreendimento selecionado.
	 * @return listaImovel
	 * @author eduardo.fernandes
	 */
	private void listarImovel(Empreendimento empreendimento){
		try{
			listaImovel = imovelService.filtrarListaImovelPorEmpreendimento(empreendimento);
			if(!Util.isNullOuVazio(listaImovel)) {
				if(isApenasUmImovel()){
					listaImovelSelecionado = listaImovel;
				}
				imovelSelecionado = listaImovel.get(0);
			}
		} catch(Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar os Imóveis");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean isApenasUmImovel(){
		return !Util.isNull(listaImovel) && listaImovel.size() == 1;
	}

	public boolean isImovelUrbano(){
		return !Util.isNull(listaImovel.iterator().next().getImovelUrbano());
	}

	public boolean isSemPontoAdicionado(){
		return Util.isNullOuVazio(listaOutorgarLocGeo);
	}

	private void carregarTipoBarragem(){
		try{
			listaTipoBarragem = fceOutorgaServiceFacade.listarTipoBarragem();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar a lista de Tipo Barragem");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaOutorgaLocGeo(){
		try {
			if(!isSemPontoAdicionado()){
				outorga = listaOutorgarLocGeo.get(0).getIdeOutorga();
				for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgarLocGeo){
					if(!isTipoBarragemuNull(outorgaLocalizacaoGeografica)){
						outorgaLocalizacaoGeografica.setDesabilitaTipoBarragem(true);
					}
					tratarDadoGeografico(outorgaLocalizacaoGeografica);
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void listarAno() {
		Integer anoInicial = Calendar.getInstance().get(Calendar.YEAR) - 30;
		Integer anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		listaAno = new ArrayList<String>();
		anoAtual = (anoAtual - anoInicial) + 1;
		for (int x = 0; x < anoAtual; x++){
			listaAno.add(new Integer(anoInicial + x).toString());
		}
		Collections.reverse(listaAno);
	}

	public void salvarPontoInserido(){
		salvarOutorga();
		if(!Util.isNullOuVazio(outorga.getIdeOutorga())){
			preparaOutorgaLocalizacaoGeografica(outorga);
			if(isOutorgaLocGeoAdicionada()){
				salvarOutorgaLocalizacaoGeograficaImovel();
				tratarDadoGeografico(outorgaLocalizacaoGeograficaParaDialog);
				listaOutorgarLocGeo.add(outorgaLocalizacaoGeograficaParaDialog);
				iniciarOutorgaLocalizacaoGeografica();
				if(!isApenasUmImovel()){
					listaImovelSelecionado.clear();
				}
				if(!modoEdicao && !isEnquadramentoDocumentoAtoSalvo()){
					salvarDocumentoParaEnquadramento();
				}
				super.exibirMensagem001();
			}
		}
	}

	private void tratarDadoGeografico(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		tratarDadoGeografico(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	
	public void tratarDadoGeografico(LocalizacaoGeografica localizacaoGeografica){
		for(DadoGeografico dadoGeografico : localizacaoGeografica.getDadoGeograficoCollection()){
			localizacaoGeografica.setLatitudeInicial(locGeoController.getLatitude(dadoGeografico));
			localizacaoGeografica.setLongitudeInicial(locGeoController.getLongitude(dadoGeografico));
		}
	}

	public void armazenaOLG(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		outorgaLocGeoSelecionada = new OutorgaLocalizacaoGeografica();
		outorgaLocGeoSelecionada = outorgaLocalizacaoGeografica;
	}
	
	public void armazenaFceOutorgaLocalizacaoGeografica(FceIntervencaoBarragem fceIntervencaoBarragem){
		outorgaLocGeoSelecionada = new OutorgaLocalizacaoGeografica();
		outorgaLocGeoSelecionada = fceIntervencaoBarragem.getIdeOutorgaLocalizacaoGeografica();
	}

	public void changeTipoBarragem(ValueChangeEvent e){
		if(!Util.isNullOuVazio(e.getOldValue())){
			if(e.getOldValue().equals(TipoBarragemEnum.REGULARIZACAO)){
				if(!Util.isNullOuVazio(buscarFceIntervencaoBy(outorgaLocGeoSelecionada))){
					RequestContext.getCurrentInstance().execute("changeTipoBarragemSemPonto.show()");
				}
			} 
			else if(e.getOldValue().equals(TipoBarragemEnum.NIVEL)){
				if(!Util.isNullOuVazio(buscarFceIntervencaoBy(outorgaLocGeoSelecionada))){
					excluirFceIntervencao(outorgaLocGeoSelecionada);
				}
			}
		}
	}

	public void confirmarChange(){
		excluirFceIntervencao(outorgaLocGeoSelecionada);
	}

	public void naoConfirmarChange(){
		outorgaLocGeoSelecionada.setIdeTipoBarragem(listaTipoBarragem.get(1));
	}


	private void salvarOutorga(){
		if(Util.isNullOuVazio(outorga.getIdeOutorga())){
			try {
				fceOutorgaServiceFacade.salvarOrAtualizarOutorga(outorga);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Outorga.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void preparaOutorgaLocalizacaoGeografica(Outorga outorga){
		outorgaLocalizacaoGeograficaParaDialog.setIdeOutorga(outorga);
		outorgaLocalizacaoGeograficaParaDialog.setTipoIntervencao(new TipoIntervencao(TipoIntervencaoEnum.CONSTRUCAO_BARRAGEM.getId()));
		salvarOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeograficaParaDialog);
	}

	private void salvarOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		try {
			fceOutorgaServiceFacade.salvarOrAtualizarOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarOutorgaLocalizacaoGeograficaImovel(){
		try {
			if(!Util.isNullOuVazio(listaImovelSelecionado)){
				buscarRemoverOLGImovel();
				for(Imovel imovel : listaImovelSelecionado){
					OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel = new OutorgaLocalizacaoGeograficaImovel(new OutorgaLocalizacaoGeograficaImovelPK(outorgaLocalizacaoGeograficaParaDialog.getIdeOutorgaLocalizacaoGeografica(), imovel.getIdeImovel()));
					outorgaLocalizacaoGeograficaImovel.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeograficaParaDialog);
					outorgaLocalizacaoGeograficaImovel.setIdeImovel(imovel);
					fceOutorgaServiceFacade.salvarOrAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Outorga Localização Geográfica Imóvel.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void buscarRemoverOLGImovel(){
		try {
			List<OutorgaLocalizacaoGeograficaImovel> listaDeOLGImovel = fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaParaDialog);
			if(!Util.isNullOuVazio(listaDeOLGImovel)){
				fceOutorgaServiceFacade.excluirOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaParaDialog);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Lista de Outorga Localização Geográfica Imóvel.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluir(){
		if(!Util.isNullOuVazio(listaOutorgarLocGeo) && !Util.isNullOuVazio(outorgaLocGeoSelecionada)){
			excluirFceIntervencao(outorgaLocGeoSelecionada);
			excluirOutorgaLocalizacaoGeografica();
			super.exibirMensagem005();
		}
	}

	private void excluirOutorgaLocalizacaoGeografica(){
		try {
			listaOutorgarLocGeo.remove(outorgaLocGeoSelecionada);
			fceOutorgaServiceFacade.excluirOutorgaLocalizacaoGeograficaImovel(outorgaLocGeoSelecionada);
			fceOutorgaServiceFacade.excluirOutorgaLocalizacaoGeografica(outorgaLocGeoSelecionada);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Lista de Outorga Localização Geográfica Imóvel.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluirFceIntervencao(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		FceIntervencaoBarragem fceIntervencaoBarragem = buscarFceIntervencaoBy(outorgaLocalizacaoGeografica);
		if(!Util.isNullOuVazio(fceIntervencaoBarragem)){
			listaFceIntervencaoBarragem.remove(fceIntervencaoBarragem);
			excluirFceIntervencao(fceIntervencaoBarragem);
		}
	}

	/**
	 * Metodo para facilitar a busca de FceIntervencao pela OutorgaLocalizacaoGeografica
	 * @param outorgaLocalizacaoGeografica
	 * @return fceIntervencaoBarragem
	 */
	private FceIntervencaoBarragem buscarFceIntervencaoBy(Object object){
		try {
			if(object instanceof OutorgaLocalizacaoGeografica){
				return barragemService.buscarFceIntervencaoBarragemByIdeOutorgaLocalizacaoGeografica((OutorgaLocalizacaoGeografica) object);
			} 
			else if(object instanceof FceIntervencaoBarragem){
				return barragemService.buscarFceIntervencaoBarragemByIde((FceIntervencaoBarragem) object);			
			}
			return null;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar o FCE Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void excluirFceIntervencao(FceIntervencaoBarragem fceIntervencaoBarragem){
		try {
			String docToDelete = "";
			if(!Util.isNullOuVazio(fceIntervencaoBarragem.getIdeDocumentoObrigatorioRequerimento())){
				docToDelete = fceIntervencaoBarragem.getIdeDocumentoObrigatorioRequerimento().getDscCaminhoArquivo();
			}
			barragemService.excluirFceIntervencaoBarragem(fceIntervencaoBarragem);
			excluirDocumentoUpadoAnteriormente(docToDelete);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir")+" o FCE Intervenção de Barragem");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método chamado no clique do botÃ£o Salvar no dialogFceBarragemIntervencao.
	 * @author eduardo.fernandes
	 */
	public void salvarDialogFceBarragemRegularizacao(){
		if(validarDialogFceIntervencaoBarragemRegularizacao()){
			salvar();
		}
	}

	/**
	 * Método que verifica se o usuÃ¡rio preencheu devidamente o dialog de FCE INTERVENCAO.
	 * @return TRUE or FALSE
	 * @author eduardo.fernandes
	 */
	private boolean validarDialogFceIntervencaoBarragemRegularizacao(){
		boolean valido = true;

		boolean volMaximoNull = true;
		boolean volMinimoNull = true;
		boolean volMaximorumNull = true;

		if(Util.isNullOuVazio(intervencaoBarragem.getNumBaciaHidrografica())){
			valido = false;
			JsfUtil.addErrorMessage("A Área de Drenagem da Bacia HidrogrÃ¡fica " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumAlturaMaxima())){
			valido = false;
			JsfUtil.addErrorMessage("A Altura Máxima(da base à crista) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumComprimentoTotal())){
			valido = false;
			JsfUtil.addErrorMessage("O Comprimento Total " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumLarguraBase())){
			valido = false;
			JsfUtil.addErrorMessage("A Largura da Base " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumLarguraCoroamento())){
			valido = false;
			JsfUtil.addErrorMessage("A Largura do Coroamento " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMaxima())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água máximo operacional " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMaximoNull = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMinima())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água mínimo operacional " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMinimoNull = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumNivelAguaMaximorum())){
			valido = false;
			JsfUtil.addErrorMessage("O Nível d'água máximo maximorum " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else {
			volMaximorumNull = false;
		}
		if(!volMaximoNull && !volMaximorumNull){
			if(intervencaoBarragem.getNumNivelAguaMaxima() >= intervencaoBarragem.getNumNivelAguaMaximorum()){
				valido = false;
				JsfUtil.addErrorMessage("O Nível d'água máximo operacional não pode ser maior ou igual ao Nível d'água máximo maximorum.");
			}
			if(!volMinimoNull){
				if(intervencaoBarragem.getNumNivelAguaMinima() > intervencaoBarragem.getNumNivelAguaMaximorum() || intervencaoBarragem.getNumNivelAguaMinima() > intervencaoBarragem.getNumNivelAguaMaxima()){
					valido = false;
					JsfUtil.addErrorMessage("O Nível d'água mínimo operacional não pode ser maior que o Nível d'água máximo operacional e/ou Nível d'água máximo maximorum.");
				}
			}
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumVolumeMaximoAcumulado())){
			valido = false;
			JsfUtil.addErrorMessage("O Volume máximo acumulado " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("A Vazão regularizada com 90% de garantia " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(!validarDatas()){
			valido = false;
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getNumGarantiaAtendimentoVazao())){
			valido = false;
			JsfUtil.addErrorMessage("A Garantia de atendimento da vazão regularizadora " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		} else if(intervencaoBarragem.getNumGarantiaAtendimentoVazao() > 100){
			valido = false;
			JsfUtil.addErrorMessage("O percentual permitido para a garantia de atendimento da vazão regularizada, é de no máximo 100%.");
		}  else if(intervencaoBarragem.getNumGarantiaAtendimentoVazao() < 80.0){
			valido = false;
			JsfUtil.addErrorMessage("O percentual permitido para a garantia de atendimento da vazão regularizada, é de no mínimo 80%.");
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getIndDescargaFundo())){
			valido = false;
			JsfUtil.addErrorMessage("A resposta da pergunta 'Possui descarga de fundo?' " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(!isArquivoUpado()){
			valido = false;
			JsfUtil.addErrorMessage("O Upload do extrato do estudo hidrológico é obrigatório.");
		}
		return valido;
	}

	/**
	 * Método que verifica se as datas foram informadas e se a data inicial vem antes da data final.
	 * @return valido
	 * @author eduardo.fernandes
	 */
	public boolean validarDatas(){
		boolean valido = true;
		if(Util.isNullOuVazio(intervencaoBarragem.getDtcIniVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("O Início do período utilizado para definir a vazão " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(Util.isNullOuVazio(intervencaoBarragem.getDtcFimVazaoRegularizada())){
			valido = false;
			JsfUtil.addErrorMessage("O Fim do período utilizado para definir a vazão " + Util.getString("msg_generica_preenchimento_obrigatorio"));
		}
		if(valido){
			if(intervencaoBarragem.getDtcFimVazaoRegularizada() < intervencaoBarragem.getDtcIniVazaoRegularizada()){
				valido = false;
				JsfUtil.addErrorMessage("O Fim do período utilizado para definir a vazão não pode ser anterior ao Início do período.");
			}
		}

		return valido;
	}

	/**
	 * Método para salvar tudo aquilo necessÃ¡rio.
	 * @param outorgaLocalizacaoGeografica
	 */
	private void salvar(){
		if(super.isFceSalvo() && isRegularizacao(getTipoBarragem())){
			if(isRegularizacao(getTipoBarragem())){
				salvarDocumentoAdicional();
				intervencaoBarragem.setIdeDocumentoObrigatorioRequerimento(getDocumentoUpado());
			} 
			if(!Util.isNull(outorgaLocGeoSelecionada)){
				intervencaoBarragem.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeoSelecionada);
			}
		} 
		else if(!isRegularizacao(getTipoBarragem()) && !Util.isNull(outorgaLocGeoSelecionada)){
			intervencaoBarragem = new FceIntervencaoBarragem();
			intervencaoBarragem.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeoSelecionada);
			intervencaoBarragem.setIdeFce(super.fce);
		}
		salvarFceIntervencaoBarragem(intervencaoBarragem);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 17/12/2014
	 * @throws Exception
	 */
	private void salvarDocumentoAdicional() {
		try {
			salvarDocumentoAdicional(requerimento, DOC_OBRIGATORIO_UPLOAD);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o extrato do estudo hidrológico");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Usado pelo salvar(outorgaLocalizacaoGeografica)
	 * @param fceIntervencaoBarragem
	 * @author eduardo.fernandes
	 */
	private void salvarFceIntervencaoBarragem(FceIntervencaoBarragem fceIntervencaoBarragem){
		try {
			barragemService.salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
			if(!Util.isNullOuVazio(fceIntervencaoBarragem.getIdeFceIntervencaoBarragem())){
				if(!listaFceIntervencaoBarragem.contains(fceIntervencaoBarragem)){
					listaFceIntervencaoBarragem.add(fceIntervencaoBarragem);
				}
			}
			if(isRegularizacao(getTipoBarragem())){
				fecharDialogExibirMsg(DIALOG_REGULARIZACAO);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void finalizar(){
		try {
			barragemService.finalizarSemPonto(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if(super.isFceTecnico()){
			finalizarFceTecnico();
		} 
		else {
			finalizarFceRequerente();
		}
	}

	private void finalizarFceTecnico() {
		try {
			if(validarAbaFceTecnico()){
				super.concluirFce();
				for (FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem) {
					intervencaoBarragem = fceIntervencaoBarragem;
					salvar();
				}
				fecharDialogExibirMsg(DIALOG_PRINCIPAL);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarAbaFceTecnico(){
		boolean valido = true;
		for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
			if(!Util.isNull(fceIntervencaoBarragem.getIdeTipoBarragem())){
				if(!fceIntervencaoBarragem.isConfirmar()){
					valido = false;
					JsfUtil.addErrorMessage(Util.getString("fce_outorga_erro_finalizar_sem_confirmar"));
					break;
				}
				else {
					if(isRegularizacao(fceIntervencaoBarragem.getIdeTipoBarragem()) && Util.isNullOuVazio(buscarFceIntervencaoBy(fceIntervencaoBarragem))){
						valido = false;
						JsfUtil.addErrorMessage(Util.getString("fce_outorga_erro_finalizar_intervencao"));
						break;
					}
				}
			} 
			else {
				valido = false;
				JsfUtil.addErrorMessage("É obrigatória a seleção de um tipo de barragem");
				break;
			}
		}
		return valido;
	}
	
	private void finalizarFceRequerente() throws Exception {
		if(validarAba()){
			super.concluirFce();
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgarLocGeo){
				if(!isRegularizacao(outorgaLocalizacaoGeografica.getIdeTipoBarragem()) && Util.isNullOuVazio(buscarFceIntervencaoBy(outorgaLocalizacaoGeografica))){
					outorgaLocGeoSelecionada = outorgaLocalizacaoGeografica;
					salvar();
				}
			}
			fecharDialogExibirMsg(DIALOG_PRINCIPAL);
		}
	}

	@Override
	public boolean validarAba(){
		boolean valido = true;

		boolean naoSelecionado = false;
		boolean naoUpado= false;
		boolean naoConfirmado = false;

		if(!isSemPontoAdicionado()){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgarLocGeo){
				if(!Util.isNullOuVazio(outorgaLocalizacaoGeografica.getIdeTipoBarragem())){
					if(!outorgaLocalizacaoGeografica.isDesabilitaTipoBarragem()){
						valido = false;
						if(!naoConfirmado){
							JsfUtil.addErrorMessage(Util.getString("fce_outorga_erro_finalizar_sem_confirmar"));
						}
						naoConfirmado = true;
					}
					else {
						if(isRegularizacao(outorgaLocalizacaoGeografica.getIdeTipoBarragem()) && Util.isNullOuVazio(buscarFceIntervencaoBy(outorgaLocalizacaoGeografica))){
							valido = false;
							if(!naoUpado){
								JsfUtil.addErrorMessage(Util.getString("fce_outorga_erro_finalizar_intervencao"));
							}
							naoUpado = true;
						}
					}
				} else {
					valido = false;
					if(!naoSelecionado){
						JsfUtil.addErrorMessage("É obrigatória a seleção de um tipo de barragem");
					}
					naoSelecionado = true;
				}
			}
		}
		else {
			valido = false;
			JsfUtil.addErrorMessage("A inclusão da Localização Geográfica é obrigatória");
		}
		return valido;
	}

	public void confirmarTipoBarragem(){
		if(!isTipoBarragemuNull(outorgaLocGeoSelecionada)){
			outorgaLocGeoSelecionada.setDesabilitaTipoBarragem(true);
			salvarOutorgaLocalizacaoGeografica(outorgaLocGeoSelecionada);
		}
	}
	
	public void confirmar(){
		if(!Util.isNull(intervencaoBarragem.getIdeTipoBarragem())){
			intervencaoBarragem.setConfirmar(true);
		} 
	}
	
	public void editar(){
		intervencaoBarragem.setConfirmar(false);
	}
	
	public void editarTipoBarragem(){
		outorgaLocGeoSelecionada.setDesabilitaTipoBarragem(false);
	}

	public boolean isTipoBarragemuNull(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		return Util.isNullOuVazio(outorgaLocalizacaoGeografica.getIdeTipoBarragem());
	}

	private void fecharDialogExibirMsg(int opcao){
		if(opcao == DIALOG_REGULARIZACAO){
			Html.esconder("barragemRegularizacaoSemPonto");
		} 
		else if(opcao == DIALOG_PRINCIPAL){
			Html.esconder("tipoBarragemSemPonto");
			Html.exibir("rel_barragemSemPonto");
		}
		super.exibirMensagem001();
	}

	/**
	 * Método para limpar o objeto FceIntervencaoBarragem e anular o tipoBarragemSelecioando.
	 * @author eduardo.fernandes
	 */
	@Override
	public void limpar(){
		intervencaoBarragem = new FceIntervencaoBarragem();
		outorgaLocGeoSelecionada = null;
		limparDocumentoUpado();
		desabilitaCampos = false;
	}

	public void limparFceIntervencaoBarragem(){
		limpar();
		listaFceIntervencaoBarragem = new ArrayList<FceIntervencaoBarragem>();
	}

	/**
	 * Método baixar o arquivo .doc que o usuÃ¡rio deve preencher e depois enviar.
	 * @return FILE
	 * @throws FileNotFoundException
	 * @author eduardo.fernandes
	 */
	public StreamedContent getEstudoHidrologico() {
		try {
			return getDadosAdicionais("Extrato_Estudo_Hidrologico.doc", "Estudo Hidrológico.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_extrato_hidrologico"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_BARRAGEM);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para verificar se o Tipo Barragem da OutorgaLocalizacaoGeografica é REGULARIZACAO
	 * @param outorgaLocalizacaoGeografica
	 * @return TRUE se for TipoBarragem for Regularização
	 * @author eduardo.fernandes
	 */
	public boolean isRegularizacao(TipoBarragem tipoBarragem){
		return !Util.isNullOuVazio(tipoBarragem) && tipoBarragem.equals(new TipoBarragem(TipoBarragemEnum.REGULARIZACAO.getId()));
	}

	/**
	 * Método para verificar se a OutorgaLocalizacaoGeografica foi confirmada
	 * @param outorgaLocalizacaoGeografica
	 * @return outorgaLocalizacaoGeografica.isDesabilitaTipoBarragem()
	 * @author eduardo.fernandes
	 */
	public boolean isConfirmado(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		return !Util.isNullOuVazio(outorgaLocalizacaoGeografica) && outorgaLocalizacaoGeografica.isDesabilitaTipoBarragem();
	}

	public int getSomentePontos() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public boolean isOutorgaLocGeoAdicionada(){
		return !Util.isNullOuVazio(outorgaLocalizacaoGeograficaParaDialog) && !Util.isNullOuVazio(outorgaLocalizacaoGeograficaParaDialog.getIdeLocalizacaoGeografica());
	}

	public void buscarEnquadramento(){
		try {
			enquadramento = enquadramentoService.buscarUltimoEnquadramentoRequerimento(requerimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Enquadramento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private Collection<EnquadramentoDocumentoAto> prepararEnquandramentoDocumentoAto(){
		Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtos = new ArrayList<EnquadramentoDocumentoAto>();
		enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
		enquadramentoDocumentoAto.setEnquadramentoDocumentoAtoPK(new EnquadramentoDocumentoAtoPK(enquadramento.getIdeEnquadramento(), DOCUMENTO_ATO_BARRAGEM.getIdeDocumentoAto()));
		enquadramentoDocumentoAto.setEnquadramento(enquadramento);
		enquadramentoDocumentoAto.setDocumentoAto(DOCUMENTO_ATO_BARRAGEM);
		enquadramentoDocumentoAtos.add(enquadramentoDocumentoAto);
		return enquadramentoDocumentoAtos;
	}

	private void salvarDocumentoParaEnquadramento(){
		try {
			enquadramentoService.salvarEnquadramentoDocumentoAto(prepararEnquandramentoDocumentoAto());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o enquadramento");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private boolean isEnquadramentoDocumentoAtoSalvo(){
		return !Util.isNullOuVazio(enquadramentoDocumentoAto) && !Util.isNullOuVazio(enquadramentoDocumentoAto.getEnquadramentoDocumentoAtoPK());
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().execute("tipoBarragemSemPonto.show();");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("panelBarragemSemPonto");		
	}

	@Override
	protected void prepararDuplicacao() {
		listaOutorgaConcedida = new ArrayList<OutorgaConcedida>();
		for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
			try {
				fceIntervencaoBarragem.setIdeFce(super.fce);
				fceIntervencaoBarragem.setIdeFceIntervencaoBarragem(null);
				OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceIntervencaoBarragem.getIdeOutorgaLocalizacaoGeografica();
				LocalizacaoGeografica locGeoDuplicada = super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(outorgaLocGeoOriginal.getIdeLocalizacaoGeografica());

				FceOutorgaLocalizacaoGeograficaBuilder builder = new FceOutorgaLocalizacaoGeograficaBuilder()
				.comFce(super.fce)
				.comTipologia(TipologiaEnum.INTERVENCAO_CORPO_HIDRICO)
				.comLocalizacaoGeografica(locGeoDuplicada);
				fceIntervencaoBarragem.setIdeTipoBarragem(outorgaLocGeoOriginal.getIdeTipoBarragem());
				fceIntervencaoBarragem.setIdeFceOutorgaLocalizacaoGeografica(builder.construirFceOutorgaLocalizacaoGeografica());
				fceIntervencaoBarragem.setIdeOutorgaLocalizacaoGeografica(null);
				listaOutorgaConcedida.add(new OutorgaConcedida(fceIntervencaoBarragem.getIdeFceOutorgaLocalizacaoGeografica(), outorgaLocGeoOriginal));
				
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " a Localização Geográfica.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			for(FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem){
				salvarFceOutorgaLocalizacaoGeografica(fceIntervencaoBarragem);
				barragemService.salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
			}
			super.salvarListaOutorgaConcedida(listaOutorgaConcedida);
			super.tratarPontos(listaFceIntervencaoBarragem);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}		
	}
	
	private void salvarFceOutorgaLocalizacaoGeografica(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception {
		if(!super.isFceSalvo()){
			super.salvarFce();
		}
		fceOutorgaServiceFacade.salvarFceOutorgaLocGeo(fceIntervencaoBarragem.getIdeFceOutorgaLocalizacaoGeografica());
	}

	@Override
	protected void carregarFceTecnico() {
		carregarTipoBarragem();
		listarAno();
		buscarEnquadramento();
		carregarListaFceIntervencao();
	}
	
	// Getters && Setters
	public FceIntervencaoBarragem getIntervencaoBarragem() {
		return intervencaoBarragem;
	}

	public void setIntervencaoBarragem(FceIntervencaoBarragem intervencaoBarragem) {
		this.intervencaoBarragem = intervencaoBarragem;
	}

	public List<String> getListaAno() {
		return listaAno;
	}

	public void setListaAno(List<String> listaAno) {
		this.listaAno = listaAno;
	}

	public List<OutorgaLocalizacaoGeografica> getListaOutorgarLocGeo() {
		return listaOutorgarLocGeo;
	}

	public void setListaOutorgarLocGeo(List<OutorgaLocalizacaoGeografica> listaOutorgarLocGeo) {
		this.listaOutorgarLocGeo = listaOutorgarLocGeo;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocGeoSelecionada() {
		return outorgaLocGeoSelecionada;
	}

	public void setOutorgaLocGeoSelecionada(OutorgaLocalizacaoGeografica outorgaLocGeoSelecionada) {
		this.outorgaLocGeoSelecionada = outorgaLocGeoSelecionada;
	}

	public List<FceIntervencaoBarragem> getListaFceIntervencaoBarragem() {
		return listaFceIntervencaoBarragem;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public boolean isDesabilitaCampos() {
		return desabilitaCampos;
	}

	public void setDesabilitaCampos(boolean desabilitaCampos) {
		this.desabilitaCampos = desabilitaCampos;
	}

	public Outorga getOutorga() {
		return outorga;
	}

	public void setOutorga(Outorga outorga) {
		this.outorga = outorga;
	}

	public List<Imovel> getListaImovel() {
		return listaImovel;
	}

	public void setListaImovel(List<Imovel> listaImovel) {
		this.listaImovel = listaImovel;
	}

	public List<Imovel> getListaImovelSelecionado() {
		return listaImovelSelecionado;
	}

	public void setListaImovelSelecionado(List<Imovel> listaImovelSelecionado) {
		this.listaImovelSelecionado = listaImovelSelecionado;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Imovel getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(Imovel imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeograficaParaDialog() {
		return outorgaLocalizacaoGeograficaParaDialog;
	}

	public void setOutorgaLocalizacaoGeograficaParaDialog(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeograficaParaDialog) {
		this.outorgaLocalizacaoGeograficaParaDialog = outorgaLocalizacaoGeograficaParaDialog;
	}

	public List<TipoBarragem> getListaTipoBarragem() {
		return listaTipoBarragem;
	}

	public void setListaTipoBarragem(List<TipoBarragem> listaTipoBarragem) {
		this.listaTipoBarragem = listaTipoBarragem;
	}

	public TipoBarragem getTipoBarragem(){
		TipoBarragem tipoBarragem = null;
		if(super.isFceTecnico()){
			tipoBarragem = intervencaoBarragem.getIdeTipoBarragem();
		} 
		else {
			tipoBarragem = outorgaLocGeoSelecionada.getIdeTipoBarragem();
		}
		return tipoBarragem;
	}
}