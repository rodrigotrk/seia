package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.MotivoEdicaoNotificacao;
import br.gov.ba.seia.entity.Notificacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoEdicaoNotificacaoService {

	@Inject
	IDAO<MotivoEdicaoNotificacao> motivoEdicaoNotificacaoDAO;
	
	public List<MotivoEdicaoNotificacao> listarMotivosEdicaoNotificacao() {
		return motivoEdicaoNotificacaoDAO.listarTodos();
	}
	
	public List<MotivoEdicaoNotificacao> listarMotivosEdicaoNotificacaoPorNotificacao(Notificacao notificacao) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ideNotificacao", notificacao.getIdeNotificacao());
		StringBuilder sql = new StringBuilder();
		sql.append("select m ");
		sql.append("from MotivoEdicaoNotificacao m ");
		sql.append("inner join m.notificacaoCollection notificacao ");
		sql.append("where notificacao.ideNotificacao = :ideNotificacao ");
		return motivoEdicaoNotificacaoDAO.listarPorQuery(sql.toString(), params);
	}
	
	public MotivoEdicaoNotificacao carregarGet(Integer id){
		return motivoEdicaoNotificacaoDAO.carregarGet(id);
	}
}