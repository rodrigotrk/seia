package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.enumerator.BancoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DadoBancarioService {

	@Inject
	IDAO<DadoBancario> dao;

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DadoBancario carregarBancoBoleto(BancoEnum banco) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DadoBancario.class)
			.createAlias("ideBanco", "banco")
		.add(Restrictions.eq("banco.codBanco", banco.getCodigo()));
		
		return this.dao.buscarPorCriteria(detachedCriteria);
	}
	
}
