package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoConservacaoSoloAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoConservacaoSoloAguaService {
	
	@Inject
	private IDAO<TipoConservacaoSoloAgua> tipoConservacaoSoloAguaIDAO;
	/**
	 * @return
	 * @throws Exception
	 * @INFO Retorna uma lista com todas os objetos do tipo TipoConservacaoSoloAgua
	 */
	public List<TipoConservacaoSoloAgua> buscarTodosTipoConservacaoSoloAgua() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoConservacaoSoloAgua.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoConservacaoSoloAguaIDAO.listarPorCriteria(criteria);
	}
}
