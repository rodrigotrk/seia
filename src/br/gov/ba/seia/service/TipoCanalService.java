package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoCanal;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCanalService {

	@Inject
	private IDAO<TipoCanal> tipoCanalIdao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCanal> listar() {
		return tipoCanalIdao.buscarPorNamedQuery("TipoCanal.findAll");
	}
 }
