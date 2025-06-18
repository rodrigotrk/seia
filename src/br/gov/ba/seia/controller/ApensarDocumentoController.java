package br.gov.ba.seia.controller;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.facade.ApensarDocumentoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("apensarDocumentoController")
@ViewScoped
public class ApensarDocumentoController extends SeiaControllerAb implements Serializable {

	private static final long serialVersionUID = 6106288637116030442L;
	private VwConsultaProcesso vwProcesso;
	private CategoriaDocumento categoriaDocumento;
	private ArquivoProcesso arquivoSelecionado;
	private Boolean existeShape;
	private Boolean desabilitarUpload;
	private ArquivoProcesso arquivoProcessoSelecionado;
	private List<ArquivoProcesso> listaArquivosProcesso;
	private List<CategoriaDocumento> listaCategoriaDocumento;
	private Collection<Imovel> listaImovel;
	private Notificacao notificacao;
	private int qtdArquivosProcessoOriginal;
	
	@EJB
	private ApensarDocumentoServiceFacade apensarDocumentoServiceFacade;

	@PostConstruct
	public void init() {
		try {
			existeShape = false;
			qtdArquivosProcessoOriginal = 0;
			if(isUsuarioExterno()){
				desabilitarUpload = false;
				categoriaDocumento = new CategoriaDocumento(CategoriaDocumentoEnum.RESPOSTA.getId());
			}
			else{
				desabilitarUpload = true;
				categoriaDocumento = null;
			}
			addAtributoSessao("GEO_REFERENCIAVEL", null);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean isUsuarioExterno(){
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	public void verificarPreenchimento(ValueChangeEvent pValueChangeEvent){
		if((CategoriaDocumento) pValueChangeEvent.getNewValue() == null) desabilitarUpload = true;
		else desabilitarUpload = false;
	}
	
	public void onRowEdit(RowEditEvent event) {

		ArquivoProcesso arq = (ArquivoProcesso) event.getObject();
		
		for(ArquivoProcesso ap: listaArquivosProcesso ){
			if(arq.getNomeArquivo().equals(ap.getNomeArquivo()) &&
			   arq.getDtcCriacao().equals(ap.getDtcCriacao())&&
			   !Util.isNullOuVazio(arq.getDescricao())){
				ap.setPermiteDownload(true);
			}			
		}
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formUploadDocumento:dataTableDocumentos");
	}
	
	public void carregarListaCategoriaDocumento() {
		try {
			Usuario uPessoaLogada = ContextoUtil.getContexto().getUsuarioLogado();
			Funcionario fPessoaLogada = apensarDocumentoServiceFacade.buscarFuncionarioPorPessoaFisica(uPessoaLogada.getPessoaFisica());
			
			if (fPessoaLogada != null && fPessoaLogada.getIdeArea().getNomArea().contains("SELIC")) {
				listaCategoriaDocumento = apensarDocumentoServiceFacade
						.listarCategoriaDocumentoPorArea(fPessoaLogada.getIdeArea());
				return;
			}
			
			
			listaCategoriaDocumento = apensarDocumentoServiceFacade.listarCategoriaDocumentoPorPerfil(uPessoaLogada.getIdePerfil());
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void load(ActionEvent evt) {
		try {
			
			loadSemExibirDialog(evt);
			
			Html.exibir("dlgApensarDocumento");
			Html.atualizar("formUploadDocumento");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void loadSemExibirDialog(ActionEvent evt) {
		try {
			vwProcesso = (VwConsultaProcesso) evt.getComponent().getAttributes().get("vwProcesso");
			notificacao = (Notificacao) evt.getComponent().getAttributes().get("notificacao");
			
			if(!Util.isNullOuVazio(notificacao)) carregarDadosNotificacao();
			
			if(Util.isNull(vwProcesso) && Util.isNull(notificacao)) {
				throw new Exception("Não foi possível carregar o processo.");
				
			} else if(Util.isNullOuVazio(vwProcesso)) {
				vwProcesso = apensarDocumentoServiceFacade.buscarVwCosultaProcesso(notificacao);
			}
			
			carregarListaCategoriaDocumento();
			
			verificarExistenciaShape();
			
			Integer idePessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
			
			listaArquivosProcesso = apensarDocumentoServiceFacade.listaArquivoProcessoPor(vwProcesso.getIdeProcesso(), idePessoaFisica);
			
			if (!Util.isNullOuVazio(listaArquivosProcesso)) {
				qtdArquivosProcessoOriginal = listaArquivosProcesso.size();
			}
			
			Html.atualizar("formUploadDocumento");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro("Erro ao carregar a tela.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void carregarDadosNotificacao() throws Exception {
		notificacao = apensarDocumentoServiceFacade.carregarNotificacaoMotivoNotificacao(notificacao);
		
		for (NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
			nmn.getIdeNotificacao().setArquivoProcessoCollection(apensarDocumentoServiceFacade.carregarListaArquivoShapeComDadoGeografico(notificacao));
		}
		
		listaImovel = apensarDocumentoServiceFacade.listarImoveis(notificacao);
	}
	
	public boolean existeShapeNaNotificacao(NotificacaoMotivoNotificacao nmn) {
		if(nmn != null && nmn.getIdeNotificacao() != null && !Util.isNullOuVazio(nmn.getIdeNotificacao().getArquivoProcessoCollection())) {
			Html.atualizar("formShapeLoc");
			return true;
		}
		
		return false;
	}

	public void removerGeoReferenciavel(){
		addAtributoSessao("GEO_REFERENCIAVEL", null);
	}
	
	public String criarUrlShape(MotivoNotificacao motivoNotificacao, Imovel imovel) {
		try {
			notificacao.setMotivoNotificacaoSelecionado(motivoNotificacao);
			ArquivoProcesso shape = apensarDocumentoServiceFacade.carregarArquivoShape(notificacao,imovel);
			
			if(shape != null) return Util.criarUrlShape(shape.getIdeLocalizacaoGeografica());
			else return null;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarShapeMotivo(MotivoNotificacao motivoNotificacao, Imovel imovel) throws Exception {
		notificacao.setMotivoNotificacaoSelecionado(motivoNotificacao);
		verificarExistenciaShape();
		ArquivoProcesso arquivoShape = null;
		if (existeShape) {
			arquivoShape = apensarDocumentoServiceFacade.carregarArquivoShape(notificacao,imovel);
			if (Util.isNull(arquivoShape)) {
				arquivoShape = new ArquivoProcesso();
				arquivoShape.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
				arquivoShape.setDtcCriacao(new Date());
				arquivoShape.setIdeProcesso(new Processo(vwProcesso.getIdeProcesso(), vwProcesso.getIdeRequerimento()));
				arquivoShape.setLocalizacaoGeografica(new LocalizacaoGeografica());
				arquivoShape.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId()));
				arquivoShape.setCategoriaDocumento(new CategoriaDocumento(CategoriaDocumentoEnum.RESPOSTA.getId()));
				arquivoShape.setMotivoNotificacao(notificacao.getMotivoNotificacaoSelecionado());
				arquivoShape.setIdeNotificacao(notificacao);
				arquivoShape.setIdeImovel(imovel);
			}
			LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController = 
					(LocalizacaoGeograficaGenericController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaGenericController}", LocalizacaoGeograficaGenericController.class);
			localizacaoGeograficaGenericController.setEmpreendimento(new Empreendimento(vwProcesso.getIdeEmpreendimento()));
			localizacaoGeograficaGenericController.setGeoReferenciavel(arquivoShape);
			localizacaoGeograficaGenericController.setIdDoComponenteParaSerAtualizado("formShapeLoc");
			localizacaoGeograficaGenericController.setMetodoAtualizarExterno(new MetodoUtil(this, this.getClass().getMethod("carregarDadosNotificacao", null)));
			localizacaoGeograficaGenericController.carregarTela();
		}
	}

	private void verificarExistenciaShape() {
		if (Util.isNullOuVazio(notificacao)) {
			existeShape = false;
		}
		else {
			existeShape = apensarDocumentoServiceFacade.verificarExistenciaShape(notificacao);		
		}
	}
	
	public void salvarArquivos() {
		try {
			if(validaListaArquivos()) {
				salvar();
				JsfUtil.addSuccessMessage("Arquivo Salvo com sucesso.");
				Html.esconder("dlgApensarDocumento");
				init();
			}
		} catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao gravar arquivos.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean validaArquivo(boolean preenchimentoShapeInvalido){
		
		if(Util.isNullOuVazio(listaArquivosProcesso) && preenchimentoShapeInvalido || isListaNaoModificadaPeloUsuarioExterno()){
			return false;
		}
		
		if(isNecessarioEnvioDocumento() && isListaNaoModificadaPeloUsuarioExterno()) {
			return false;
		}
		
		return true;
	}
	
	private boolean validaShape(boolean preenchimentoShapeInvalido){
		boolean retorno = true;
		if(!existeShape || preenchimentoShapeInvalido) {
			retorno = false;
			throw new SeiaValidacaoRuntimeException("Obrigatório selecionar ao menos um arquivo para upload ou enviar um shape.");
		}
		return retorno;
	}
	
	private boolean validaArquivoOuShape(boolean preenchimentoShapeInvalido){
		
		boolean retorno = true;
		
		retorno = validaArquivo(preenchimentoShapeInvalido);
		if(!retorno || Util.isNullOuVazio(listaArquivosProcesso)){
			retorno = validaShape(preenchimentoShapeInvalido);
		}
				
		return retorno;
	}
	
	private boolean validaListaArquivos() {
		
		boolean preenchimentoShapeInvalido = false;
		preenchimentoShapeInvalido = existeShapeSemLocalizacao();
	
		if(validaArquivoOuShape(preenchimentoShapeInvalido)) {
		
			for (ArquivoProcesso arquivoProcesso : listaArquivosProcesso) {
				if (Util.isNullOuVazio(arquivoProcesso.getDscArquivoProcesso())) {
					throw new SeiaValidacaoRuntimeException("O campo Descrição é de preenchimento obrigatório.");
				} else {
					if((arquivoProcesso.getDscArquivoProcesso().length() > 100)) {
						throw new SeiaValidacaoRuntimeException("O campo Descrição permite no máximo 100(cem) caracteres, por favor reduza a descrição.");
					} else {
						if((arquivoProcesso.getDscCaminhoArquivo().length() > 200)) {
							throw new SeiaValidacaoRuntimeException("O nome do arquivo permite no máximo 100(cem) caracteres, por favor reduza o nome do arquivo.");
						}
					}
				}
			}
			
			return true;
		}
		
		return false;
	}

	private boolean isNecessarioEnvioDocumento() {
		if(!Util.isNullOuVazio(notificacao) && !Util.isNullOuVazio(notificacao.getNotificacaoMotivoNotificacaoCollection())) {
			for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
				if(!nmn.getIdeMotivoNotificacao().getIndEnvioShape()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isMotivoShape(MotivoNotificacao motivo){
		
		if(Util.isNull(motivo)){
			return false;
		}
		
		int length = MotivoNotificacaoEnum.SHAPE.getIds().length;
		
		for (int i = 0; i < length; i++) {
			if(motivo.equals(new MotivoNotificacao(MotivoNotificacaoEnum.SHAPE.getIds()[i])))
				return true;
		}
		
		if(motivo.equals(new MotivoNotificacao(MotivoNotificacaoEnum.SHAPE_ASV.getId()))) {
			return true;
		}
		
		return false;
		
	}

	/**
	 * Método que verifica se o <b>Usuário Externo</b> adicionou novo arquivo na lista de arquivos do processo.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 15/09/2015	 
	 * @see #7246 | #7186
	 * @return
	 */
	private boolean isListaNaoModificadaPeloUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno() && listaArquivosProcesso.size() == qtdArquivosProcessoOriginal;
	}

	public boolean existeShapeSemLocalizacao(){
		
		if(Util.isNull(notificacao) || !existeShape){
			return true;
		}
		
		Notificacao not = apensarDocumentoServiceFacade.carregarMotivosNotificacao(notificacao);
		List<ArquivoProcesso> listaArquivoProcessoShapes = apensarDocumentoServiceFacade.carregarListaArquivoShapeComDadoGeografico(not);
		if (existeShape && Util.isNullOuVazio(listaArquivoProcessoShapes)) {
			return true;
		}
		
		return false;
	}

	private void salvar() throws Exception {
		
		if (Util.isNullOuVazio(notificacao)) {
			apensarDocumentoServiceFacade.salvarListaArquivos(listaArquivosProcesso);
		} else {
			apensarDocumentoServiceFacade.salvarComNotificacao(listaArquivosProcesso, notificacao);
		}
	}

	public StreamedContent getArquivoBaixar() {
		try {
			StreamedContent arquivoBaixar = null;
			
			if (!Util.isNullOuVazio(arquivoSelecionado) && !Util.isNullOuVazio(arquivoSelecionado.getDescricao())) {
					arquivoBaixar = apensarDocumentoServiceFacade.getContentFile(arquivoSelecionado);
			}
			else if(Util.isNullOuVazio(arquivoSelecionado.getDescricao())){
				JsfUtil.addWarnMessage("É preciso adicionar a descrição do arquivo antes de realizar o download.");
			}
			
			return arquivoBaixar;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void limpaArquivosPreviosNaoSalvos(){
		if(!Util.isNullOuVazio(listaArquivosProcesso)){
			RequestContext.getCurrentInstance().addPartialUpdateTarget(":formUploadDocumento");
		}
		
	}
	
	public void trataArquivo(FileUploadEvent event)  {
		try{
			String caminhoArquivo = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString()+vwProcesso.getIdeProcesso()+File.separator);
		
			ArquivoProcesso arq = new ArquivoProcesso();
			
			arq.setDscCaminhoArquivo(caminhoArquivo);
			arq.setIdeProcesso(new Processo(vwProcesso.getIdeProcesso()));
			arq.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			arq.setDtcCriacao(new Date());		
			arq.setPermiteDownload(false);
			
			CategoriaDocumento ctgDocumento = null;
		
			ctgDocumento = apensarDocumentoServiceFacade.carregarCategoriaDocumento(categoriaDocumento.getIdeCategoriaDocumento());
			arq.setCategoriaDocumento(ctgDocumento);
			listaArquivosProcesso.add(arq);	
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formUploadDocumento:dataTableDocumentos");
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possí­vel enviar o arquivo.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void deletarArquivo() {
		try {
			if (!Util.isNull(arquivoProcessoSelecionado.getIdeArquivoProcesso())) {
				apensarDocumentoServiceFacade.deletarArquivo(arquivoProcessoSelecionado);
			}
			for (int i = 0; i < listaArquivosProcesso.size(); i++) {
				if (listaArquivosProcesso.get(i).getNomeArquivo().equals(arquivoProcessoSelecionado.getNomeArquivo())) {
					this.listaArquivosProcesso.remove(i);
					break;
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRenderedUploadShapeMotivoNotificacao() {
		return existeShape && Util.isNullOuVazio(listaImovel);
	}
	
	public boolean isRenderedUploadShapeMotivoNotificacaoImovel() {
		return existeShape && !Util.isNullOuVazio(listaImovel); 
	}

	public List<ArquivoProcesso> getListaArquivosProcesso() {
		return listaArquivosProcesso;
	}

	public void setListaArquivosProcesso(List<ArquivoProcesso> listaArquivosProcesso) {
		this.listaArquivosProcesso = listaArquivosProcesso;
	}

	public ArquivoProcesso getArquivoSelecionado() {
		return arquivoSelecionado;
	}

	public void setArquivoSelecionado(ArquivoProcesso arquivoSelecionado) {
		this.arquivoSelecionado = arquivoSelecionado;
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public boolean isRenderedDataTableDocumentos() {
		return !Util.isNullOuVazio(listaArquivosProcesso);
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public ArquivoProcesso getArquivoProcessoSelecionado() {
		return arquivoProcessoSelecionado;
	}

	public void setArquivoProcessoSelecionado(ArquivoProcesso arquivoProcessoSelecionado) {
		this.arquivoProcessoSelecionado = arquivoProcessoSelecionado;
	}

	public Boolean getExisteShape() {
		return existeShape;
	}

	public void setExisteShape(Boolean existeShape) {
		this.existeShape = existeShape;
	}

	public CategoriaDocumento getCategoriaDocumento() {
		return categoriaDocumento;
	}

	public void setCategoriaDocumento(CategoriaDocumento categoriaDocumento) {
		this.categoriaDocumento = categoriaDocumento;		
	}

	public Boolean getDesabilitarUpload() {
		return desabilitarUpload;
	}

	public void setDesabilitarUpload(Boolean desabilitarUpload) {
		this.desabilitarUpload = desabilitarUpload;
	}

	public List<CategoriaDocumento> getListaCategoriaDocumento() {
		return listaCategoriaDocumento;
	}

	public void setListaCategoriaDocumento(List<CategoriaDocumento> listaCategoriaDocumento) {
		this.listaCategoriaDocumento = listaCategoriaDocumento;
	}

	public Collection<Imovel> getListaImovel() {
		return listaImovel;
	}

	public void setListaImovel(Collection<Imovel> listaImovel) {
		this.listaImovel = listaImovel;
	}

	public String getNomeImovel(Imovel imovel) {
		if(Util.isNullOuVazio(imovel.getNomeImovelRural())) {
			return vwProcesso.getNomEmpreendimento(); 
		}
		return imovel.getNomeImovelRural();
	}
	
}