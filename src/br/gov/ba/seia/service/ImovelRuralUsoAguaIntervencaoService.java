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
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaIntervencao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralUsoAguaIntervencaoService {
	@Inject
	IDAO<ImovelRuralUsoAguaIntervencao> imovelRuralUsoAguaIntervencaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarImovelRuralUsoAguaIntervencao(ImovelRuralUsoAguaIntervencao pImovel) {
		imovelRuralUsoAguaIntervencaoDAO.salvarOuAtualizar(pImovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ImovelRuralUsoAguaIntervencao pimovel)  {
		imovelRuralUsoAguaIntervencaoDAO.remover(pimovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralUsoAguaIntervencao obterPorImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralUsoAguaIntervencao.class);
		criteria .add(Restrictions.eq("ideImovelRuralUsoAgua", pImovelRuralUsoAgua));
		return imovelRuralUsoAguaIntervencaoDAO.buscarPorCriteria(criteria);
	}
	
}
