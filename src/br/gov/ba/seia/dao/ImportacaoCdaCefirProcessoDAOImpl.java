package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import br.gov.ba.seia.entity.ImportacaoCdaCefirProcesso;

public class ImportacaoCdaCefirProcessoDAOImpl {
	
	@Inject
	IDAO<ImportacaoCdaCefirProcesso> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImportacaoCdaCefirProcesso filtrarById(ImportacaoCdaCefirProcesso pImportacaoCdaCefirProcesso) {
		return dao.buscarEntidadePorExemplo(pImportacaoCdaCefirProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImportacaoCdaCefirProcesso pImportacaoCdaCefirProcesso)  {
		dao.salvar(pImportacaoCdaCefirProcesso);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImportacaoCdaCefirProcesso> listarImportacaoCdaCefirProcesso()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImportacaoCdaCefirProcesso.class, "ImportacaoCdaCefirProcesso");
		return dao.listarPorCriteria(criteria, Order.desc("numProcessoImportacaoCdaCefir"));		
	}
}