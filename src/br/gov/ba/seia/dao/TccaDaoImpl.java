package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaDaoImpl {
	
	@Inject
	private IDAO<Tcca> tccaDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Tcca tcca) {
		try {
			tccaDao.salvarOuAtualizar(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Tcca tcca) {
		try {
			tcca.setIndExcluido(true);
			tccaDao.atualizar(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tcca> listarPorNumeroTCCA(String numTCCA) throws Exception {
		return tccaDao.listarPorCriteria(getCriteria(null, null, null, null, numTCCA, true, false));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countConsultaTcca(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, 
			String numLicencaFiltro, String numProcessoFiltro, String numTCCAFiltro) throws Exception {
		
		return tccaDao.count(getCriteria(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro, false, true));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tcca> consultarTcca(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, 
			String numLicencaFiltro, String numProcessoFiltro, String numTCCAFiltro, int first, int pageSize) throws Exception {
		
		return tccaDao.listarPorCriteriaDemanda(
				getCriteria(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro, false, false), first, pageSize);
	}
	
	private DetachedCriteria getCriteria(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, 
			String numLicencaFiltro, String numProcessoFiltro, String numTCCAFiltro, boolean isNumTccaFiltroExato, boolean isCount) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Tcca.class, "tcca")
				.createAlias("tcca.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("tcca.idePessoaRequerente", "pessoa", JoinType.INNER_JOIN)
				.createAlias("pessoa.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pessoaJuridica", JoinType.LEFT_OUTER_JOIN);
		
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.groupProperty("tcca.ideTcca"), "ideTcca")
				.add(Projections.groupProperty("tcca.dtAssinatura"), "dtAssinatura")
				.add(Projections.groupProperty("tcca.dtPublicacao"), "dtPublicacao")
				.add(Projections.groupProperty("tcca.indCancelado"), "indCancelado")
				.add(Projections.groupProperty("tcca.indExcluido"), "indExcluido")
				.add(Projections.groupProperty("tcca.indPossuiTccaOrigem"), "indPossuiTccaOrigem")
				.add(Projections.groupProperty("tcca.indReajustado"), "indReajustado")
				.add(Projections.groupProperty("tcca.indRenovado"), "indRenovado")
				.add(Projections.groupProperty("tcca.indOrigemLicenciamentoEstadual"), "indOrigemLicenciamentoEstadual")
				.add(Projections.groupProperty("tcca.indModalidadeExecucaoDireta"), "indModalidadeExecucaoDireta")
				.add(Projections.groupProperty("tcca.numPrazoValidade"), "numPrazoValidade")
				.add(Projections.groupProperty("tcca.numProcessoLicenca"), "numProcessoLicenca")
				.add(Projections.groupProperty("tcca.numProcessoSema"), "numProcessoSema")
				.add(Projections.groupProperty("tcca.numTcca"), "numTcca")
				.add(Projections.groupProperty("tcca.numTccaOrigem"), "numTccaOrigem")
				.add(Projections.groupProperty("tcca.valGradacaoImpacto"), "valGradacaoImpacto")
				.add(Projections.groupProperty("tcca.valTcca"), "valTcca")
				
				.add(Projections.groupProperty("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
				.add(Projections.groupProperty("emp.nomEmpreendimento"), "ideEmpreendimento.nomEmpreendimento")
				
				.add(Projections.groupProperty("pessoa.idePessoa"), "idePessoaRequerente.idePessoa")
				.add(Projections.groupProperty("pessoaFisica.idePessoaFisica"), "idePessoaRequerente.pessoaFisica.idePessoaFisica")
				.add(Projections.groupProperty("pessoaFisica.nomPessoa"), "idePessoaRequerente.pessoaFisica.nomPessoa")
				.add(Projections.groupProperty("pessoaFisica.numCpf"), "idePessoaRequerente.pessoaFisica.numCpf")
				.add(Projections.groupProperty("pessoaJuridica.idePessoaJuridica"), "idePessoaRequerente.pessoaJuridica.idePessoaJuridica")
				.add(Projections.groupProperty("pessoaJuridica.nomRazaoSocial"), "idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
				.add(Projections.groupProperty("pessoaJuridica.numCnpj"), "idePessoaRequerente.pessoaJuridica.numCnpj");
		criteria.setProjection(projecao).setResultTransformer(new AliasToNestedBeanResultTransformer(Tcca.class));
		
		criteria.add(Restrictions.eq("indExcluido", false));
		
		if(!Util.isNullOuVazio(requerenteFiltro)) {
			criteria.add(Restrictions.eq("pessoa.idePessoa", requerenteFiltro.getIdePessoa()));
		}
		
		if(empreendimentoFiltro != null && !Util.isNullOuVazio(empreendimentoFiltro.getNomEmpreendimento())) {
			criteria.add(Restrictions.ilike("emp.nomEmpreendimento", empreendimentoFiltro.getNomEmpreendimento(), MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(numLicencaFiltro)) {
			criteria.add(Restrictions.ilike("numProcessoLicenca", numLicencaFiltro, MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(numProcessoFiltro)) {
			criteria.add(Restrictions.ilike("numProcessoSema", numProcessoFiltro, MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(numTCCAFiltro)) {
			if(isNumTccaFiltroExato) {
				criteria.add(Restrictions.like("numTcca", numTCCAFiltro, MatchMode.EXACT));
			} else {
				criteria.add(Restrictions.ilike("numTcca", numTCCAFiltro, MatchMode.ANYWHERE));
			}
		}
		
		if(!isCount) {
			criteria.addOrder(Order.desc("tcca.ideTcca"));
		}
		
		return criteria;
	}
}