package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class ProcuradorRepresentanteDAOImpl extends AbstractDAO<ProcuradorRepresentante> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<ProcuradorRepresentante> procuradorRepresentanteDAO;

	@Override
	protected IDAO<ProcuradorRepresentante> getDAO() {
		return procuradorRepresentanteDAO;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante) {
		procuradorRepresentanteDAO
			.salvarOuAtualizar(procuradorRepresentante);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean isProcuradorRepresentante(PessoaFisica pessoaValidar,PessoaFisica pessoaFisica) {
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorPessoaJuridica(Pessoa solicitante,Pessoa requerente)  {
		return
			procuradorRepresentanteDAO
				.isExiste(
					DetachedCriteria.forClass(ProcuradorRepresentante.class)
						.add(Restrictions.eq("idePessoaJuridica.idePessoa", solicitante.getIdePessoa()))
						.add(Restrictions.eq("ideProcurador.idePessoa", requerente.getIdePessoa()))
						.add(Restrictions.eq("indExcluido",false))
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideProcuradorRepresentante"),"ideProcuradorRepresentante")
						));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepresentante buscarPorIdViaCriteria(ProcuradorRepresentante procurador) {
		return
			procuradorRepresentanteDAO.buscarPorCriteria(
				DetachedCriteria.forClass(ProcuradorRepresentante.class)
					.add(Restrictions.eq("ideProcuradorRepresentante", procurador.getIdeProcuradorRepresentante()))
					.add(Restrictions.eq("indExcluido", Boolean.FALSE)));
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorRepresentante> getListaProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante)  {
		return
			procuradorRepresentanteDAO.listarPorCriteria(
				DetachedCriteria.forClass(ProcuradorRepresentante.class,"pr")
					.createAlias("pr.ideProcurador", "pf")
					.createAlias("pf.pessoa", "p")
					.createAlias("pf.idePais", "pais")

					.add(Restrictions.eq("pr.indExcluido", false))
					.add(Restrictions.eq("pr.idePessoaJuridica", procuradorRepresentante.getIdePessoaJuridica())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorRepresentante> listarProcuradorRepresentanteComProjection(ProcuradorRepresentante procuradorRepresentante) {
		return
			procuradorRepresentanteDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorRepresentante.class)
						.createAlias("ideProcurador", "pf")
						.createAlias("pf.pessoa", "p")

						.add(Restrictions.eq("indExcluido", false))
						.add(Restrictions.eq("idePessoaJuridica", procuradorRepresentante.getIdePessoaJuridica()))

						.setProjection(Projections.projectionList()
							.add(Projections.property("ideProcuradorRepresentante"),"ideProcuradorRepresentante")
							.add(Projections.property("pf.idePessoaFisica"),"ideProcurador.idePessoaFisica")
							.add(Projections.property("pf.idePessoaFisica"),"ideProcurador.idePessoaFisica")
							.add(Projections.property("p.idePessoa"),"ideProcurador.pessoa.idePessoa")
							.add(Projections.property("p.desEmail"),"ideProcurador.pessoa.desEmail")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorRepresentante.class)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorRepresentante> pessoasJuridicasDoProcurador(PessoaFisica idPessoaFisica) {
		return
			procuradorRepresentanteDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorRepresentante.class)
						.createAlias("idePessoaJuridica", "idePessoaJuridica", JoinType.INNER_JOIN)
							.add(Restrictions.eq("ideProcurador.idePessoaFisica", idPessoaFisica.getIdePessoaFisica()))
							.add(Restrictions.eq("indExcluido", false))
							.setProjection(Projections.projectionList()
								.add(Projections.property("ideProcuradorRepresentante"),"ideProcuradorRepresentante")
								.add(Projections.property("idePessoaJuridica.idePessoaJuridica"),"idePessoaJuridica.idePessoaJuridica")
							).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorRepresentante.class)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorRepresentante> cnpjPessoasJuridicasDoProcurador(PessoaFisica idePessoaFisica) {
		return
			procuradorRepresentanteDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorRepresentante.class)
						.add(Restrictions.eq("ideProcurador.idePessoaFisica", idePessoaFisica.getIdePessoaFisica()))
						.add(Restrictions.eq("indExcluido", false))
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideProcuradorRepresentante"),"ideProcuradorRepresentante")
							.add(Projections.property("idePessoaJuridica.cnpj"),"idePessoaJuridica.cnpj")
						 ).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorRepresentante.class)));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepresentante buscarProcuradorRepresentante(PessoaFisica procurador, PessoaJuridica pj) {
		return
			procuradorRepresentanteDAO.buscarPorCriteria(
				DetachedCriteria.forClass(ProcuradorRepresentante.class)
					.add(Restrictions.eq("ideProcurador.idePessoaFisica", procurador.getIdePessoaFisica()))
					.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", pj.getIdePessoaJuridica()))
					.add(Restrictions.eq("indExcluido", false)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorRepresentanteInativado(int ideProcurador, int idePessoaJuridica) {
		return
			procuradorRepresentanteDAO
				.isExiste(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("ideProcurador", "ideProcurador")
						.createAlias("idePessoaJuridica", "idePessoaJuridica")

						.add(Restrictions.eq("ideProcurador.ideProcurador", ideProcurador))
						.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", idePessoaJuridica))
						.add(Restrictions.eq("indExcluido", true))

						.setProjection(Projections.projectionList()
								.add(Projections.property("indExcluido"),"indExcluido"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class))
						);


	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcuradorRepresentante", procuradorRepresentante.getIdeProcuradorRepresentante());
		params.put("dtcExclusao", new Date());
		params.put("indExcluido", true);
		procuradorRepresentanteDAO.executarNamedQuery("ProcuradorRepresentante.remove", params, new ProcuradorRepresentante[0]);
	}

	public List<ProcuradorRepresentante> buscarProcuradorRepresentanteByPessoaJuridica(Integer pessoaJuridica) {
		// TODO Auto-generated method stub
		return
				procuradorRepresentanteDAO.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorRepresentante.class)	
					.createAlias("ideProcurador", "ideProcurador", JoinType.INNER_JOIN)
						.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica",pessoaJuridica))
						.add(Restrictions.eq("indExcluido", false)));
	}




}
