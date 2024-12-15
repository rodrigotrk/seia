package br.gov.ba.seia.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjetoTipoStatus;
import br.gov.ba.seia.entity.TccaStatus;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaStatusDaoImpl {
	
	@Inject
	private IDAO<TccaStatus> tccaStatusDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Tcca tcca, TccaProjetoTipoStatusEnum statusEnum) {
		try {
			TccaStatus status = new TccaStatus();
			
			status.setIdeTcca(tcca);
			status.setIdeTccaProjetoTipoStatus(new TccaProjetoTipoStatus(statusEnum));
			status.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			status.setDtcTccaStatus(new Date());
			
			tccaStatusDao.salvarOuAtualizar(status);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaStatus> listarPorTcca(Tcca tcca)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TccaStatus.class, "status")
				.createAlias("status.ideTccaProjetoTipoStatus", "pts", JoinType.INNER_JOIN)
				.createAlias("status.idePessoaFisica", "pf", JoinType.INNER_JOIN)
				.createAlias("status.ideTcca", "tcca", JoinType.INNER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("status.ideTccaStatus"), "ideTccaStatus")
				.add(Projections.property("status.dtcTccaStatus"), "dtcTccaStatus")
				
				.add(Projections.property("pts.ideTccaProjetoTipoStatus"), "ideTccaProjetoTipoStatus.ideTccaProjetoTipoStatus")
				.add(Projections.property("pts.nomTccaTipoStatus"), "ideTccaProjetoTipoStatus.nomTccaTipoStatus")
				
				.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"), "idePessoaFisica.nomPessoa")
				
				.add(Projections.property("tcca.ideTcca"), "ideTcca.ideTcca");
				
		
		criteria.setProjection(projecao)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TccaStatus.class));
		
		criteria.add(Restrictions.eq("ideTcca.ideTcca", tcca.getIdeTcca()));		
		
		return tccaStatusDao.listarPorCriteria(criteria, Order.desc("ideTccaStatus"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TccaStatus buscarUltimoPorTcca(Tcca tcca)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TccaStatus.class, "status")
			.createAlias("status.ideTccaProjetoTipoStatus", "pts", JoinType.INNER_JOIN)
			.createAlias("status.idePessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("status.ideTcca", "tcca", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideTccaStatus").eq(
				DetachedCriteria.forClass(TccaStatus.class, "status")
					.createAlias("status.ideTccaProjetoTipoStatus", "pts", JoinType.INNER_JOIN)
					.createAlias("status.idePessoaFisica", "pf", JoinType.INNER_JOIN)
					.createAlias("status.ideTcca", "tcca", JoinType.INNER_JOIN)
					
				.setProjection(Projections.projectionList().add(Projections.max("ideTccaStatus"), "ideTccaStatus"))
				.add(Restrictions.eq("ideTcca.ideTcca", tcca.getIdeTcca()))
			))
		
		.setProjection(Projections.projectionList()
			.add(Projections.property("status.ideTccaStatus"), "ideTccaStatus")
			.add(Projections.property("status.dtcTccaStatus"), "dtcTccaStatus")
			
			.add(Projections.property("pts.ideTccaProjetoTipoStatus"), "ideTccaProjetoTipoStatus.ideTccaProjetoTipoStatus")
			.add(Projections.property("pts.nomTccaTipoStatus"), "ideTccaProjetoTipoStatus.nomTccaTipoStatus")
			
			.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
			.add(Projections.property("pf.nomPessoa"), "idePessoaFisica.nomPessoa")
			
			.add(Projections.property("tcca.ideTcca"), "ideTcca.ideTcca"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(TccaStatus.class));
		
		return tccaStatusDao.buscarPorCriteria(criteria);
	}
}