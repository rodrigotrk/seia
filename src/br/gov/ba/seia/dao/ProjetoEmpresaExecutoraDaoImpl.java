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
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProjetoEmpresaExecutora;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjetoEmpresaExecutoraDaoImpl {
	
	@Inject
	private IDAO<ProjetoEmpresaExecutora> projetoEmpresaExecutoraDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProjetoEmpresaExecutora projetoEmpresaExecutora) {
		try {
			projetoEmpresaExecutoraDao.salvarOuAtualizar(projetoEmpresaExecutora);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public List<ProjetoEmpresaExecutora> listarPorProjeto(TccaProjeto projeto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoEmpresaExecutora.class, "pee")
				.createAlias("pee.idePessoaExecutora", "pessoa", JoinType.INNER_JOIN)
				.createAlias("pessoa.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pessoaJuridica", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("pee.idePessoaFisicaInativadora", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pee.ideTccaProjeto", "projeto", JoinType.INNER_JOIN);
				
			ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("pee.ideProjetoEmpresaExecutora"), "ideProjetoEmpresaExecutora")
				.add(Projections.property("pee.dtVigenciaContratoFim"), "dtVigenciaContratoFim")
				.add(Projections.property("pee.dtVigenciaContratoInicio"), "dtVigenciaContratoInicio")
				.add(Projections.property("pee.dtcInativa"), "dtcInativa")
				.add(Projections.property("pee.indInativa"), "indInativa")
				
				.add(Projections.property("pessoa.idePessoa"), "idePessoaExecutora.idePessoa")
				.add(Projections.property("pessoaFisica.idePessoaFisica"), "idePessoaExecutora.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaExecutora.pessoaFisica.nomPessoa")
				.add(Projections.property("pessoaFisica.numCpf"), "idePessoaExecutora.pessoaFisica.numCpf")
				.add(Projections.property("pessoaJuridica.idePessoaJuridica"), "idePessoaExecutora.pessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pessoaJuridica.nomRazaoSocial"), "idePessoaExecutora.pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pessoaJuridica.numCnpj"), "idePessoaExecutora.pessoaJuridica.numCnpj")
				
				.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisicaInativadora.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"), "idePessoaFisicaInativadora.nomPessoa")
				.add(Projections.property("pf.numCpf"), "idePessoaFisicaInativadora.numCpf")
				
				.add(Projections.property("projeto.ideTccaProjeto"), "ideTccaProjeto.ideTccaProjeto");
			
			criteria.setProjection(projecao).setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoEmpresaExecutora.class));
				
			criteria.add(Restrictions.eq("projeto.ideTccaProjeto", projeto.getIdeTccaProjeto()));
		
			return projetoEmpresaExecutoraDao.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public ProjetoEmpresaExecutora buscarAtivaPorProjeto(TccaProjeto projeto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoEmpresaExecutora.class, "pee")
				.createAlias("pee.idePessoaExecutora", "pessoa", JoinType.INNER_JOIN)
				.createAlias("pessoa.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pessoaJuridica", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("pee.idePessoaFisicaInativadora", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pee.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
			
				.setProjection(Projections.projectionList()
					.add(Projections.property("pee.ideProjetoEmpresaExecutora"), "ideProjetoEmpresaExecutora")
					.add(Projections.property("pee.dtVigenciaContratoFim"), "dtVigenciaContratoFim")
					.add(Projections.property("pee.dtVigenciaContratoInicio"), "dtVigenciaContratoInicio")
					.add(Projections.property("pee.dtcInativa"), "dtcInativa")
					.add(Projections.property("pee.indInativa"), "indInativa")
					
					.add(Projections.property("pessoa.idePessoa"), "idePessoaExecutora.idePessoa")
					.add(Projections.property("pessoaFisica.idePessoaFisica"), "idePessoaExecutora.pessoaFisica.idePessoaFisica")
					.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaExecutora.pessoaFisica.nomPessoa")
					.add(Projections.property("pessoaFisica.numCpf"), "idePessoaExecutora.pessoaFisica.numCpf")
					.add(Projections.property("pessoaJuridica.idePessoaJuridica"), "idePessoaExecutora.pessoaJuridica.idePessoaJuridica")
					.add(Projections.property("pessoaJuridica.nomRazaoSocial"), "idePessoaExecutora.pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("pessoaJuridica.numCnpj"), "idePessoaExecutora.pessoaJuridica.numCnpj")
					
					.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisicaInativadora.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"), "idePessoaFisicaInativadora.nomPessoa")
					.add(Projections.property("pf.numCpf"), "idePessoaFisicaInativadora.numCpf")
					
					.add(Projections.property("projeto.ideTccaProjeto"), "ideTccaProjeto.ideTccaProjeto")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoEmpresaExecutora.class))
			
				.add(Restrictions.eq("projeto.ideTccaProjeto", projeto.getIdeTccaProjeto()))
				.add(Restrictions.eq("pee.indInativa", false));
			
			return projetoEmpresaExecutoraDao.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}