package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.EfetuarValidacaoPreviaAtividadeController;
import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicComunicacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 14/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ValidacaoAtividadeFacade {
	
	@EJB
	private CadastroAtividadeFacade facade;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroSendoValidado(CadastroAtividadeNaoSujeitaLic cadastro) {
		facade.tramitarCadastroSendoValidado(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroPendenciaValidacao(EfetuarValidacaoPreviaAtividadeController controller) {
		try {
			facade.tramitarCadastroPendenciaValidacao(controller.getCadastro());		
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o status do Cadastro como 'Pendência de Validação'.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroConcluido(EfetuarValidacaoPreviaAtividadeController controller) {
		try {
			facade.tramitarCadastroConcluido(controller.getCadastro());		
			salvarComunicacao(controller);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o histórico de comunicação com o Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listarDocumentosApensados(CadastroAtividadeNaoSujeitaLic cadastro){
		return facade.listarDocumentosApensados(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent baixarArquivo(Object obj) {
		return facade.baixarArquivo(obj);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicComunicacao> listarComunicacao(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return facade.listarComunicacao(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComunicacao(EfetuarValidacaoPreviaAtividadeController controller) throws Exception {
		enviarEmail(controller.getComunicacao());
		facade.salvarComunicacao(controller.getComunicacao());
		
		controller.limpar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void enviarEmail(CadastroAtividadeNaoSujeitaLicComunicacao comunicacao) throws Exception{
		comunicacao.setIndEnviado(true);
		new EmailUtil().enviarEmail(facade.obterEmailsDosResponsaveis(comunicacao.getCadastroAtividadeNaoSujeitaLic()), null, null, comunicacao.getDscAssunto(), comunicacao.getDscMensagem());				
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic montarCadastro(CadastroAtividadeNaoSujeitaLic cadastro){
		try {
			return facade.buscar(cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocumentoIdentificacao(CadastroAtividadeNaoSujeitaLic cadastro){
		return facade.getListaDocumentoIdentificacoFrom(cadastro.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocumentoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro){
		List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaTemp = facade.getListaDocumentoRepresentanteLegalFrom(cadastro.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos());
		if(Util.isNullOuVazio(listaTemp)){
			listaTemp = facade.getListaDocumentoProcuradorPJFrom(cadastro.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos());
		}
		return listaTemp;
	}
	
}
