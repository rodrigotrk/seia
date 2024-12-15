package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoOrigemCertificado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoOrigemCertificadoService {
	
	@Inject
	IDAO<TipoOrigemCertificado> dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoOrigemCertificado> listarTipoOrigemCertificado() {
		return  dao.listarTodos();
	}
}
