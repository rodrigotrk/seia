package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ImportacaoCdaCefir;

public class ImportacaoCdaCefirDAOImpl {
	
	@Inject
	IDAO<ImportacaoCdaCefir> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImportacaoCdaCefir filtrarById(ImportacaoCdaCefir pImportacaoCdaCefir) {
		return dao.buscarEntidadePorExemplo(pImportacaoCdaCefir);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImportacaoCdaCefir pImportacaoCdaCefir)  {
		dao.salvar(pImportacaoCdaCefir);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImportacaoCdaCefir pImportacaoCdaCefir)  {
		dao.atualizar(pImportacaoCdaCefir);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImportacaoCdaCefir> listarImportacaoCdaCefir()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImportacaoCdaCefir.class, "ImportacaoCdaCefir");
		criteria.createAlias("ideUsuarioImportacao", "ideUsuarioImportacao", JoinType.INNER_JOIN);
		return dao.listarPorCriteria(criteria, Order.desc("ideImportacaoCdaCefir"));		
	}
}