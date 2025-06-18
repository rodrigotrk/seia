package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.TipoCaracterizacaoProjeto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AgrossilvopastorilService {

	@Inject
	private IDAO<FceAgrossilvopastoril> fceAgrossilvopastorilIDAO;
	@Inject
	private IDAO<TipoCaracterizacaoProjeto> tipoCaracterizacaoProjetoIDAO;


	/**
	 * @param fceAgrossilvopastoril
	 * @throws Exception
	 * @INFO salva ou atualiza FceAgrossilvopastoril
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarAgrossilvopastoril(FceAgrossilvopastoril fceAgrossilvopastoril) {
		fceAgrossilvopastorilIDAO.salvarOuAtualizar(fceAgrossilvopastoril);
	}

	/**
	 * @throws Exception
	 * @INFO Busca a FceAgrossilvopastoril pelo ID da Fce
	 * @return FceAgrossilvopastoril
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAgrossilvopastoril buscarFceAgrossilvopastorilByIdFce(Fce fce){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAgrossilvopastoril.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.setFetchMode("ideTipoCaracterizacaoProjeto", FetchMode.JOIN);
		criteria.setFetchMode("ideClassificacaoAdubacao", FetchMode.JOIN);
		return fceAgrossilvopastorilIDAO.buscarPorCriteria(criteria);
	}
	/**
	 * @return
	 * @throws Exception
	 * @INFO retorna uma lista dos objetos TipoCaracterizacaoProjeto disponiveis
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCaracterizacaoProjeto> listarTipoCaracterizacaoProjeto(){
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoCaracterizacaoProjeto.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoCaracterizacaoProjetoIDAO.listarPorCriteria(criteria);
	}

}
