package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.JustificativaRejeicao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JustificativaRejeicaoService {
	
	
	@Inject
	private IDAO<JustificativaRejeicao> justificativaRejeicaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<JustificativaRejeicao> listarTodos() {
		return justificativaRejeicaoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<JustificativaRejeicao> listarByIdeNotificacao(Integer ideNotificacao) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideNotificacao", ideNotificacao);
		return justificativaRejeicaoDAO.buscarPorNamedQuery("JustificativaRejeicao.findByIdeNotificacao",parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JustificativaRejeicao carregar(JustificativaRejeicao justificativaRejeicao){		
			return justificativaRejeicaoDAO.carregarGet(justificativaRejeicao.getIdeJustificativaRejeicao());		 
	}
	
}
