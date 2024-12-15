package br.gov.ba.seia.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProjetoStatus;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.TccaProjetoTipoStatus;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjetoStatusDaoImpl {
	
	@Inject
	private IDAO<ProjetoStatus> projetoStatusDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaProjeto projeto, TccaProjetoTipoStatusEnum statusEnum) {
		try {
			ProjetoStatus status = new ProjetoStatus();
			
			status.setIdeTccaProjeto(projeto);
			status.setIdeTccaProjetoTipoStatus(new TccaProjetoTipoStatus(statusEnum));
			status.setIdePessoaFisicaProjetoStatus(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			status.setDtcProjetoStatus(new Date());
			
			projetoStatusDao.salvarOuAtualizar(status);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoStatus buscarUltimoPorProjeto(TccaProjeto tccaProjeto)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoStatus.class, "status")
			.createAlias("status.ideTccaProjetoTipoStatus", "pts", JoinType.INNER_JOIN)
			.createAlias("status.idePessoaFisicaProjetoStatus", "pf", JoinType.INNER_JOIN)
			.createAlias("status.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
			
			.add(Property.forName("ideProjetoStatus").eq(
				DetachedCriteria.forClass(ProjetoStatus.class, "status")
					.createAlias("status.ideTccaProjetoTipoStatus", "pts", JoinType.INNER_JOIN)
					.createAlias("status.idePessoaFisicaProjetoStatus", "pf", JoinType.INNER_JOIN)
					.createAlias("status.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList().add(Projections.max("status.ideProjetoStatus"), "ideProjetoStatus"))
				.add(Restrictions.eq("ideTccaProjeto.ideTccaProjeto", tccaProjeto.getIdeTccaProjeto()))
			))
				
			.setProjection(Projections.projectionList()
				.add(Projections.property("status.ideProjetoStatus"), "ideProjetoStatus")
				.add(Projections.property("status.dtcProjetoStatus"), "dtcProjetoStatus")
				
				.add(Projections.property("pts.ideTccaProjetoTipoStatus"), "ideTccaProjetoTipoStatus.ideTccaProjetoTipoStatus")
				.add(Projections.property("pts.nomTccaTipoStatus"), "ideTccaProjetoTipoStatus.nomTccaTipoStatus")
				
				.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisicaProjetoStatus.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"), "idePessoaFisicaProjetoStatus.nomPessoa")
				
				.add(Projections.property("projeto.ideTccaProjeto"), "ideTccaProjeto.ideTccaProjeto"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoStatus.class));
		
		return projetoStatusDao.buscarPorCriteria(criteria);
	}
}