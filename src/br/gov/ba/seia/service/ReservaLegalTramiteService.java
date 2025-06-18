package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ReservaLegalTramite;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaLegalTramiteService {
	
	@Inject
	IDAO<ReservaLegalTramite> daoReservaLegalTramite;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReservaLegalTramite reserva )  {
		daoReservaLegalTramite.salvarOuAtualizar(reserva);
	}
	
	
	
	public List<ReservaLegalTramite> filtrarByImovel(ImovelRural imovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegalTramite.class);
		criteria.createAlias("ideImovel", "ideImovel");
		criteria.add(Restrictions.eq("ideImovel.ideImovelRural", imovelRural.getIdeImovelRural()));
		return daoReservaLegalTramite.listarPorCriteria(criteria);
	}
	
	
	

}
