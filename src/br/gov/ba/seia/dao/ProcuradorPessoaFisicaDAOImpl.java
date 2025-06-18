package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcuradorPessoaFisicaDAOImpl extends AbstractDAO<ProcuradorPessoaFisica>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<ProcuradorPessoaFisica> procuradorPessoaFisicaDAO;


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcurarPessoaFisica(PessoaFisica pessoa,PessoaFisica procurador)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
			.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", pessoa.getIdePessoaFisica()))
			.add(Restrictions.eq("ideProcurador.idePessoaFisica", procurador.getIdePessoaFisica()))
			.add(Restrictions.eq("indExcluido", false));
			;

		return procuradorPessoaFisicaDAO.isExiste(detachedCriteria);
	}



	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		procuradorPessoaFisica.setDtcInicio(new Date());
		procuradorPessoaFisicaDAO.salvarOuAtualizar(procuradorPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorPessoaFisica(Pessoa solicitante, Pessoa requerente) {
		return
			procuradorPessoaFisicaDAO.isExiste(
				DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
					.createAlias("idePessoaFisica","idePessoaFisica", JoinType.INNER_JOIN)
					.createAlias("ideProcurador", "ideProcurador", JoinType.INNER_JOIN)

					.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", solicitante.getIdePessoa()))
					.add(Restrictions.eq("ideProcurador.idePessoaFisica", requerente.getIdePessoa()))

					.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcuradorPessoaFisica"),"ideProcuradorPessoaFisica")
						.add(Projections.property("ideProcurador.idePessoaFisica"),"ideProcurador.idePessoaFisica")
						.add(Projections.property("idePessoaFisica.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPessoaFisica.class)));
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPessoaFisica> filtrarProcuradorPessoaFisicaById(PessoaFisica procurador)  {
		return
			procuradorPessoaFisicaDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.createAlias("ideProcurador", "procurador", JoinType.INNER_JOIN)
						.createAlias("idePessoaFisica", "pessoaFisica", JoinType.INNER_JOIN)
						.createAlias("procurador.idePais", "pais", JoinType.INNER_JOIN)
							.add(Restrictions.eq("indExcluido", false))
							.add(Restrictions.eq("procurador.idePessoaFisica", procurador.getIdePessoaFisica())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPessoaFisica> pessoasFisicasDoProcurador(PessoaFisica idPessoaFisica) {
		return
			procuradorPessoaFisicaDAO.listarPorCriteria(
				DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
					.add(Restrictions.eq("ideProcurador", idPessoaFisica))
					.add(Restrictions.eq("indExcluido", Boolean.FALSE))
					.setProjection(Projections.projectionList()
						.add(Projections.property("idePessoaFisica.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPessoaFisica.class)));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPessoaFisica buscarPorIdViaCriteria(ProcuradorPessoaFisica procurador) {
		return
			procuradorPessoaFisicaDAO
				.buscarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.add(Restrictions.eq("ideProcuradorPessoaFisica", procurador.getIdeProcuradorPessoaFisica()))
						.add(Restrictions.eq("indExcluido", Boolean.FALSE)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean procuradorAlreadyProcuradorPF(ProcuradorPessoaFisica pProcuradorPessoaFisica)  {
		return
			procuradorPessoaFisicaDAO.isExiste(
				DetachedCriteria.forClass(ProcuradorPessoaFisica.class, "procpf")
					.createAlias("idePessoaFisica", "pf", JoinType.FULL_JOIN)
					.createAlias("ideProcurador", "proc", JoinType.FULL_JOIN)
						.add(Restrictions.eq("procpf.idePessoaFisica", pProcuradorPessoaFisica.getIdePessoaFisica()))
						.add(Restrictions.eq("procpf.ideProcurador", pProcuradorPessoaFisica.getIdeProcurador()))
						.add(Restrictions.eq("procpf.indExcluido", Boolean.FALSE))
						.setProjection(Projections.projectionList()
								/**
								 * @author alexandre.queiroz
								 *
								 * */

							.add(Projections.property("ideProcuradorPessoaFisica"), "ideProcuradorPessoaFisica")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPessoaFisica.class)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPessoaFisica> cpfPessoasFisicasDoProcurador(PessoaFisica idePessoaFisica) {
		return
			procuradorPessoaFisicaDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.createAlias("idePessoaFisica", "idePessoaFisica", JoinType.INNER_JOIN)
						.add(Restrictions.eq("ideProcurador", idePessoaFisica))
						.add(Restrictions.eq("indExcluido", false))
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideProcuradorPessoaFisica"), "ideProcuradorPessoaFisica")
							.add(Projections.property("idePessoaFisica.numCpf"), "idePessoaFisica.numCpf")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPessoaFisica.class)));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPessoaFisica buscarProcuradorPessoaFisica(PessoaFisica procurador, PessoaFisica pessoaFisica) {
		return
			procuradorPessoaFisicaDAO
				.buscarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.add(Restrictions.eq("ideProcurador", procurador))
						.add(Restrictions.eq("indExcluido", Boolean.FALSE))
						.add(Restrictions.eq("idePessoaFisica", pessoaFisica)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorPessoaFisica> listarProcuradorPessoaFisicaComProjection(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		return
			procuradorPessoaFisicaDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
						.createAlias("ideProcurador", "proc", JoinType.INNER_JOIN)
						.createAlias("proc.pessoa", "pessoa", JoinType.INNER_JOIN)
					.add(Restrictions.eq("pf.idePessoaFisica", procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica()))
					.add(Restrictions.eq("indExcluido", false))

					.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcuradorPessoaFisica"),"ideProcuradorPessoaFisica")
						.add(Projections.property("pessoa.desEmail"),"ideProcurador.pessoa.desEmail")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPessoaFisica.class)));
	}


	@Deprecated //prefira o metodo acima listarProcuradorPessoaFisicaComProjection
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorPessoaFisica> listarProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		return
			procuradorPessoaFisicaDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(ProcuradorPessoaFisica.class)
						.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
						.createAlias("ideProcurador", "proc", JoinType.INNER_JOIN)
						.createAlias("pf.idePais", "paispf", JoinType.LEFT_OUTER_JOIN)
						.createAlias("proc.idePais", "paisproc", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("pf.idePessoaFisica", procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica()))
					.add(Restrictions.eq("indExcluido", false))
			);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcuradorPessoaFisica", procuradorPessoaFisica.getIdeProcuradorPessoaFisica());
		params.put("dtcFinal", new Date());
		params.put("indExcluido", true);
		procuradorPessoaFisicaDAO.executarNamedQuery("ProcuradorPessoaFisica.updateRemoveById", params, new ProcuradorPessoaFisica[0]);
	}



	@Override
	protected IDAO<ProcuradorPessoaFisica> getDAO() {

		return null;
	}





}