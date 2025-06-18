package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.OrgaoExpedidor;

/**
 * 
 * @author rubem.filho
 */

@Stateless
public class TipoOrgaoExpedidorService {

	@Inject
	IDAO<OrgaoExpedidor> daoOrgaoExpedidor;

	public void salvarOcupacao(OrgaoExpedidor orgaoExpedidor) throws Exception {
		daoOrgaoExpedidor.salvar(orgaoExpedidor);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<OrgaoExpedidor> listarOrgaoExpedidor() {
		return daoOrgaoExpedidor.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<OrgaoExpedidor> filtrarListaOrgaoExpedidor(OrgaoExpedidor orgaoExpedidor) {
		return daoOrgaoExpedidor.listarPorExemplo(orgaoExpedidor);
	}

	public void excluirOrgaoExpedidor(OrgaoExpedidor orgaoExpedidor) throws Exception {
		daoOrgaoExpedidor.remover(orgaoExpedidor);
	}

}
