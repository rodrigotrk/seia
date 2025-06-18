package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicComunicacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.DocumentoValidacao;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.facade.ValidacaoAtividadeFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 13/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
 */
@Named("efetuarValidacaoPreviaAtividadeController")
@ViewScoped
public class EfetuarValidacaoPreviaAtividadeController {
	
	@EJB
	private ValidacaoAtividadeFacade facade;

	private CadastroAtividadeNaoSujeitaLic cadastro;
	private CadastroAtividadeNaoSujeitaLicComunicacao comunicacao;

	private Collection<DocumentoValidacao> documentosPendentes;
	
	private List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaDocIdentificacao;
	private List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaDocRepresentacao;

	/**
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 */
	public void carregarDialog() {
		
		cadastro = facade.montarCadastro(cadastro);
		
		if(isPossivelValidar()){
			
			facade.tramitarCadastroSendoValidado(cadastro);
			
			carregarComunicacaoCadastro();
			
			carregarDocumentosEstudos();
			
			
			carregarDocumentosIdentificacao();
			carregarDocumentosRepresentacao();
			
			Html.atualizar("formDialogEfetuarValidacao");
			Html.exibir("dialogEfetuarValidacao");
		} 
		else {
			JsfUtil.addWarnMessage("Este cadastro já está sendo validado por outro usuário.");
		}
		
	}
	
	private boolean isPossivelValidar(){
		if(cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus().getIdeCadastroAtividadeNaoSujeitaLicTipoStatus().equals(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.SENDO_VALIDADO.getIde())){
			return ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(cadastro.getCadastroUltimoStatus().getIdePessoaFisica().getIdePessoaFisica());
		}
		return true;  
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 */
	private void carregarComunicacaoCadastro() {
		try {
			cadastro.setCadastroAtividadeNaoSujeitaLicComunicacaos(facade.listarComunicacao(cadastro));
		} catch (Exception e) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	
	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 */
	private void carregarDocumentosIdentificacao() {
		listaDocIdentificacao = facade.getListaDocumentoIdentificacao(cadastro);
	}
	
	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 */
	private void carregarDocumentosRepresentacao() {
		listaDocRepresentacao = facade.getListaDocumentoRepresentacao(cadastro);
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @throws Exception
	 */
	private void carregarDocumentosEstudos() {
		try {
			
			List<CadastroAtividadeNaoSujeitaLicDocApensado> docList = new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>();
			
			for(CadastroAtividadeNaoSujeitaLicDocApensado doc : facade.listarDocumentosApensados(cadastro)){
				
				if(!Util.isNullOuVazio(doc.getUrlDocumento())){
					docList.add(doc);
				}
			}
			
			cadastro.setCadastroAtividadeNaoSujeitaLicDocApensados(docList);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * 
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @param caminho
	 * @return
	 */
	public StreamedContent getArquivoBaixar(Object obj) {
		return facade.baixarArquivo(obj);
	}
	

	/**
	 * 
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @return
	 */
	private boolean existePendencia() {
		return !Util.isNullOuVazio(documentosPendentes);
	}

	/**
	 * 
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 */
	public void salvar() {
		obterDocumentosPendentes();
		
		comunicacao = new CadastroAtividadeNaoSujeitaLicComunicacao(cadastro);
		
		Html.esconder("dialogEfetuarValidacao");
		
		if (existePendencia()) {
			comunicacao.setDscAssunto("Email de pendência de validação dos documentos e estudos");
			comunicacao.setDscMensagem(getMensagemPendencia());

			facade.tramitarCadastroPendenciaValidacao(this);
			Html.exibir("confirmationEmail");
		}
		else {
			comunicacao.setDscAssunto("Cadastro de Atividades de Atividades não sujeitas a Licenciamento Ambiental Validado");
			comunicacao.setDscMensagem(getMensagemValidado());
			
			facade.tramitarCadastroConcluido(this);
		}
		
		Html.atualizar("formAtividadesSemLicenciamento:dataTableCadastro");
		
	}

	public void enviar() {
		try {
			facade.salvarComunicacao(this);
			JsfUtil.addSuccessMessage("Notificação enviada com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG DE ERRO*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	private String getMensagemPendencia(){
		return "Prezado(a), \n\n" +
				"Favor acessar o SEIA para verificar pendências na documentação enviada. \n" +
				"DOCUMENTOS PENDENTES: \n\n" +
					getNomDocumentosPendentes() +
			     "Atte., \n" + 
				 "Central de Atendimento/INEMA";
	}
	
	private String getMensagemValidado(){
		return "Prezado(a) "  + cadastro.getIdePessoaRequerente().getNomeRazao() + ", \n\n" +
				 "Seu cadastro de número " + cadastro.getNumCadastro() + ", foi validado com sucesso. \n" + 
				 "Acesse o SEIA para gerar o certificado. \n\n" +
			     "Atte., \n" + 
				 "Central de Atendimento/INEMA";
	}

	private void obterDocumentosPendentes() {
		documentosPendentes = new ArrayList<DocumentoValidacao>();
		
		for(CadastroAtividadeNaoSujeitaLicDocApensado docApensado : cadastro.getCadastroAtividadeNaoSujeitaLicDocApensados()){
			if(Util.isNull(docApensado.getIndValidado()) || !docApensado.getIndValidado()){
				documentosPendentes.add(docApensado);
			}
		}
		
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : listaDocIdentificacao){
			if(Util.isNull(doc.getIndDocumentoValidado()) || !doc.getIndDocumentoValidado()){
				documentosPendentes.add(doc);
			}
		}
		
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : listaDocRepresentacao){
			if(Util.isNull(doc.getIndDocumentoValidado()) || !doc.getIndDocumentoValidado()){
				documentosPendentes.add(doc);
			}
		}
		
	}
	
	private String getNomDocumentosPendentes(){
		StringBuilder docs = new StringBuilder();
		int i = 0;
		for(DocumentoValidacao doc : documentosPendentes){
			i++;
			docs.append(i + " - " + doc.getDescricao() + ". \n\n");
		}
		return docs.toString();
	}
	
	public void limpar() {
		cadastro = null;
		comunicacao = null;
		documentosPendentes = null;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public CadastroAtividadeNaoSujeitaLicComunicacao getComunicacao() {
		return comunicacao;
	}
	
	public void setComunicacao(CadastroAtividadeNaoSujeitaLicComunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocIdentificacao() {
		return listaDocIdentificacao;
	}

	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocRepresentacao() {
		return listaDocRepresentacao;
	}
}
