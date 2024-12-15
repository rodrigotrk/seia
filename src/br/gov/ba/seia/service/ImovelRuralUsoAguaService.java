package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.ProcessoUsoAgua;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralUsoAguaService {

	@Inject
	IDAO<ImovelRuralUsoAgua> imovelRuralUsoAguaDAO;
	@Inject
	IDAO<ProcessoUsoAgua> processoUsoAguaDAO;	
	@Inject
	IDAO<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO;
	@Inject
	IDAO<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaDAO;
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarUsoAgua(ImovelRuralUsoAgua pImovel)
			 {
		Collection<ProcessoUsoAgua> processoUsoAguaCollection = null;
		if (pImovel.getIndDispensa() != null
				&& !pImovel.getIndDispensa()
				&& (pImovel.getIndProcesso() == null || !pImovel
						.getIndProcesso() )) {
			processoUsoAguaCollection = pImovel.getProcessoUsoAguaCollection();
		}

		if (!Util.isNullOuVazio(processoUsoAguaCollection)) {
			for (ProcessoUsoAgua p : processoUsoAguaCollection) {
				p.setDtcExclusao(new Date());
				p.setIndExcluido(true);
			}
		}

		imovelRuralUsoAguaDAO.salvarOuAtualizar(pImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRuralUsoAgua> obterTodos()  {

		StringBuilder sql = new StringBuilder();

		sql.append("select I ");
		sql.append("from ImovelRuralUsoAgua I ");
		sql.append("where I.indExcluido ='false' ");


		return imovelRuralUsoAguaDAO.listarPorQuery(sql.toString(), null);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRuralUsoAgua> obter(String pTipoUso, ImovelRural imovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralUsoAgua.class, "ImovelRuralUsoAgua");
		criteria.createAlias("ideLocalizacaoGeografica", "LocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeograficaFinal", "LocalizacaoGeograficaFinal", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);		
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.add(Restrictions.eq("tipoUso", pTipoUso));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("ideImovelRuralUsoAgua"));
		return imovelRuralUsoAguaDAO.listarPorCriteria(criteria);

		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRuralUsoAgua> obterTodosPorImovelRural(ImovelRural imovelRural)
			 {

		StringBuilder sql = new StringBuilder();

		sql.append("select I ");
		sql.append("from ImovelRuralUsoAgua I ");
		sql.append("where I.indExcluido = false and I.ideImovelRural="+imovelRural.getIdeImovelRural());

		return imovelRuralUsoAguaDAO.listarPorQuery(sql.toString(), null);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralUsoAgua obterPorId(ImovelRuralUsoAgua pImovelRuralUsoAgua)
			 {

		StringBuilder sql = new StringBuilder();

		sql.append("select distinct I ");
		sql.append("from ImovelRuralUsoAgua I ");
		sql.append("left join fetch  I.tipoFinalidadeCollection t ");
		sql.append("left join fetch  t.ideTipoFinalidadeUsoAgua  ");
		sql.append("left join I.imovelRuralUsoAguaIntervencaoCollection  ");
		sql.append("left join I.processoUsoAguaCollection  ");
		sql.append("left join I.ideLocalizacaoGeografica  ");
		sql.append("left join I.ideLocalizacaoGeograficaFinal  ");
		sql.append("where I.indExcluido ='false' "); 
		sql.append(" and I.ideImovelRuralUsoAgua="+pImovelRuralUsoAgua.getIdeImovelRuralUsoAgua());
		
		return imovelRuralUsoAguaDAO.buscarPorQuery(sql.toString(), null);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ImovelRuralUsoAgua pimovel)  {
		imovelRuralUsoAguaDAO.remover(pimovel);
	}

	public List<ImovelRuralUsoAgua> obterPorImovelTipo(ImovelRural imovelRural,
			String string)  {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ImovelRuralUsoAgua.class);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.add(Restrictions.eq("indExcluido", false));
		if (string != null) {
			criteria.add(Restrictions.eq("tipoUso", string));
		}
		return imovelRuralUsoAguaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralUsoAgua carregarTudo(ImovelRuralUsoAgua pImovelRuralUsoAgua)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralUsoAgua.class, "ImovelRuralUsoAgua");
		criteria.createAlias("ideLocalizacaoGeografica", "LocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRuralUsoAgua", pImovelRuralUsoAgua.getIdeImovelRuralUsoAgua()));
		criteria.addOrder(Order.asc("ideImovelRuralUsoAgua"));
		return imovelRuralUsoAguaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarImovelRuralUsoAguaTipoFinalidadeUsoAgua(ImovelRuralUsoAguaTipoFinalidadeUsoAgua pImovelRuralUsoAguaFinalidade) {
		this.imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.salvarOuAtualizar(pImovelRuralUsoAguaFinalidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralUsoAguaTipoFinalidadeUsoAgua obterPorId(ImovelRuralUsoAguaTipoFinalidadeUsoAgua pImovelRuralUsoAguaFinalidade)
			 {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralUsoAguaTipoFinalidadeUsoAgua.class);
		criteria.add(Restrictions.eq("ideImovelRuralUsoAguaTipoFinalidadeUsoAgua",pImovelRuralUsoAguaFinalidade.getIdeImovelRuralUsoAguaTipoFinalidadeUsoAgua()));		
		return imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> obterPorIdImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT i.* FROM imovel_rural_uso_agua_tipo_finalidade_uso_agua i "+
				"WHERE i.ide_imovel_rural_uso_agua = "+	pImovelRuralUsoAgua.getIdeImovelRuralUsoAgua());

		return imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.listarClasseComNativeQuery(sb.toString(), ImovelRuralUsoAguaTipoFinalidadeUsoAgua.class);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoFinalidadeUsoAgua obterTipoFinalidadeUsoAguaPorId(TipoFinalidadeUsoAgua pTipoFinalidadeUsoAgua)
			 {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class);
		criteria.add(Restrictions.eq("ideTipoFinalidadeUsoAgua",pTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));		
		return tipoFinalidadeUsoAguaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t.* FROM tipo_finalidade_uso_agua t"+
				" INNER JOIN imovel_rural_uso_agua_tipo_finalidade_uso_agua i "+
				" ON (i.ide_tipo_finalidade_uso_agua = t.ide_tipo_finalidade_uso_agua AND i.ide_imovel_rural_uso_agua = "+
				pImovelRuralUsoAgua.getIdeImovelRuralUsoAgua()+")");
		return tipoFinalidadeUsoAguaDAO.listarClasseComNativeQuery(sb.toString(), TipoFinalidadeUsoAgua.class);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoFinalidadeUsoAguaPorIdImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua) {
		List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> 	listIUATFUA = obterPorIdImovelRuralUsoAgua(pImovelRuralUsoAgua);
		for (ImovelRuralUsoAguaTipoFinalidadeUsoAgua imovelRuralUsoAguaTipoFinalidadeUsoAgua : listIUATFUA) {
			imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.remover(imovelRuralUsoAguaTipoFinalidadeUsoAgua);
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarImovelRuralUsoAguaTipoFinalidadeUsoAguaEmLote(List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> lista) {
		imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.salvarEmLote(lista);
	}

	public List<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> listarImovelRuralUsoAguaTipoFinalidadeUsoAgua(ImovelRuralUsoAgua imovelRuralUsoAgua)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralUsoAguaTipoFinalidadeUsoAgua.class);
		criteria.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRuralUsoAgua", "ideImovelRuralUsoAgua", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRuralUsoAgua.ideImovelRuralUsoAgua", imovelRuralUsoAgua.getIdeImovelRuralUsoAgua()));	
		return imovelRuralUsoAguaTipoFinalidadeUsoAguaDAO.listarPorCriteria(criteria);
	}
}
