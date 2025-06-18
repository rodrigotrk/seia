package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.EstadoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.OrgaoExpedidorService;
import br.gov.ba.seia.service.PaisService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.TipoIdentificacaoService;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IdentificarPapelSolicitacaoFacade {

	@EJB 
	private PessoaService pessoaService;
	
	@EJB 
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;

	@EJB
	private EstadoService estadoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private OrgaoExpedidorService orgaoExpedidorService;

	@EJB
	private PaisService paisService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private TipoIdentificacaoService tipoIdentificacaoService;
	
	
	public Pessoa buscarPessoa(Pessoa pessoa) {
		try{
			return pessoaService.buscar(pessoa);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("Erro ao consultar pessoa");
	}
	
	
	public List<DocumentoIdentificacao> carregarDocumentos(Pessoa requerente) {
		try{
			return documentoIdentificacaoService.listar(requerente);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		throw new RuntimeException("");
	}
	
	public void salvarDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		try{
			documentoIdentificacaoService .salvarDocumentoIdentificacao(documentoIdentificacao);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void excluirDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao)  {
		try{
			documentoIdentificacaoService.excluirDocumentoIdentificacao(documentoIdentificacao);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public List<Estado> listarEstado() {
		try{
			return estadoService.listar();
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}

	public StreamedContent getArquivo(String dscCaminhoArquivo)  {
		try{
			return gerenciaArquivoService .getContentFile(dscCaminhoArquivo);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}
	
	public List<OrgaoExpedidor> listarOrgaoExpedidor()  {
		try{
			return orgaoExpedidorService.listar();
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}

	public boolean isProcuradorPessoaFisica(Pessoa solicitante, Pessoa requerente) {
		try{
			return procuradorPessoaFisicaService.isProcuradorPessoaFisica(solicitante, requerente);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}

	public boolean isRepresentanteLegalPessoaJuridica(Pessoa solicitante, Pessoa requerente) {
		try{
			return representanteLegalService.isRepresentanteLegalPessoaJuridica(solicitante, requerente);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}
	
	public boolean isProcuradorPessoaJuridica(Pessoa solicitante, Pessoa requerente){
		try{
			return procuradorRepresentanteService.isProcuradorPessoaJuridica(solicitante,requerente);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}
	
	public void salvarPessoa(Pessoa pessoa) {

		try{
			pessoaService.salvarPessoa(pessoa);
		}
		catch (ConstraintViolationException e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public Pessoa buscarRequerente(Pessoa pessoa){
		try{
			return pessoaService.buscar(pessoa);
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
	}

	public List<Pais> listarPais() throws Exception {
		try{
			return paisService.listar();
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("");
		
	}

	public List<TipoIdentificacao> listarTipoIdentificacao()  {
		try{
			return tipoIdentificacaoService.listar();
		}catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new RuntimeException("Não foi possivel listar os tipo de documento de identificação");
	}
}