package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.TipoIdentificacaoDAOImpl;
import br.gov.ba.seia.entity.TipoIdentificacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoIdentificacaoService {
	
	@Inject
	private IDAO<TipoIdentificacao> tipoIdentificacaoDAO;
	
	@EJB
	private TipoIdentificacaoDAOImpl tipoIdentificacaoDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoIdentificacao> listar() throws Exception {
		return tipoIdentificacaoDAOImpl.listar();
	}
	
	/*mover para o dao*/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoIdentificacao carregarTipoIdentificacao(Integer id){
		return tipoIdentificacaoDAO.carregarGet(id);
	}	
	
	/*mover para o dao*/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipoIdentificacao(TipoIdentificacao tipoIdentificacao) throws Exception {
		tipoIdentificacaoDAO.salvar(tipoIdentificacao);
	}
	
	/*mover para o dao*/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoIdentificacao> filtrarListaTipoIdentificacao(TipoIdentificacao tipoIdentificacao) {
		return tipoIdentificacaoDAO.listarPorExemplo(tipoIdentificacao);
	}
	
	/*mover para o dao*/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoIdentificacao(TipoIdentificacao tipoIdentificacao) throws Exception {
		tipoIdentificacaoDAO.remover(tipoIdentificacao);
	}
	
}
