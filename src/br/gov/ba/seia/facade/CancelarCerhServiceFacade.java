package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.CerhCadastroCanceladoDAOImpl;
import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCadastroCancelado;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 30/04/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CancelarCerhServiceFacade {

	@Inject
	private IDAO<CerhCadastroCancelado> cerhCadastroCanceladoDAO;
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	
	@EJB
	private CerhDAOImpl cerhDAO;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private GerenciaArquivoService arquivoService;
	
	@EJB
	private CerhCadastroCanceladoDAOImpl cerhCadastroCanceladoDAOImpl;
	
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;

	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa carregarPessoaRequerente(Cerh cerh){
		return pessoaDAOImpl.carregarDadosRequerenteCerh(cerh.getIdePessoaRequerente());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarEmpreendimento(Cerh cerh) {
		return empreendimentoService.buscar(cerh.getIdeEmpreendimento());
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCadastroCancelado carregarCerhCadastroCancelado(Cerh cerh) throws Exception {
	 	CerhCadastroCancelado cerhCadastroCancelado =  cerhCadastroCanceladoDAOImpl.buscar(Restrictions.eq("ideCerh.ideCerh", cerh.getId()));

	 	
	 	if(cerhCadastroCancelado!=null){
	 		PessoaFisica pessoafisica = pessoaFisicaDAOImpl.buscar(cerhCadastroCancelado.getIdePessoaFisicaCancelamento());
	 		pessoafisica.setPessoa(pessoaDAOImpl.buscar(pessoafisica.getPessoa()));
	 		pessoafisica.getPessoa().setTelefoneCollection(telefoneService.buscarTelefonesPorPessoa(pessoafisica.getPessoa()));
	 		cerhCadastroCancelado.setIdePessoaFisicaCancelamento(pessoafisica);
	 	}
	 	
	 	
	 	
	 	
	 	return cerhCadastroCancelado;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh carregarDadosBasicos(Cerh cerh) throws Exception {
		Cerh c = cerhDAO.buscar(cerh);
		
		Cerh requerente = cerhDAO.buscarCerhDadosRequerente(cerh.getIdeCerh());
		if(requerente!=null){
			c.setIdePessoaRequerente(requerente.getIdePessoaRequerente());
			
		}
		
		c.getIdePessoaRequerente().setTelefoneCollection(telefoneService.listarTelefone(c.getIdePessoaRequerente()));
		return c;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvar(Cerh cerh) {
		try {
			cerh.setIdeCerhCadastroCancelado(null);
			cerhDAO.salvar(cerh);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelar(CerhCadastroCancelado cadastroCancelado)  {
		salvarCadastroCancelado(cadastroCancelado);
		salvarStatusCancelado(cadastroCancelado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCadastroCancelado(CerhCadastroCancelado cadastroCancelado)  {
		cerhCadastroCanceladoDAO.salvarOuAtualizar(cadastroCancelado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarStatusCancelado(CerhCadastroCancelado cadastroCancelado){
		Cerh cerh = cadastroCancelado.getIdeCerh();
		cerh.setIdeCerhStatus(new CerhStatus(CerhStatusEnum.CANCELADO));
		
		if(cerh.getIdePessoaFisicaCadastro() == null){
			cerh.setIdePessoaFisicaCadastro(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		}
		
		cerhDAO.salvar(cerh);
	}
	
	public void deletarArquivo(String url) {
		if(!Util.isNullOuVazio(url)) {
			arquivoService.deletarArquivo(url);
		}
	}

	public StreamedContent baixarArquivo(String urlDocumento) throws Exception {
		return arquivoService.getContentFile(urlDocumento);
	}


	


	

	



	
	
}
