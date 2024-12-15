package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class RepresentanteLegalDAOImpl {

	@Inject
	private IDAO<RepresentanteLegal> representanteLegalDAO;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRepresentanteLegal(RepresentanteLegal representanteLegal){
		representanteLegalDAO
			.salvarOuAtualizar(representanteLegal);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isRepresentanteLegalPessoaJuridica(Pessoa solicitante, Pessoa requerente)  {
		return
			representanteLegalDAO.isExiste(
				DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica","idePessoaFisica", JoinType.INNER_JOIN)
						.createAlias("idePessoaJuridica","idePessoaJuridica", JoinType.INNER_JOIN)
					.add(Restrictions.eq("indExcluido", false))
					.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", solicitante.getIdePessoa()))
					.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", requerente.getIdePessoa()))

					.setProjection(Projections.projectionList()
							.add(Projections.property("ideRepresentanteLegal"),"ideRepresentanteLegal"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRepresentanteLegalInativado(int idePessoaFisica, int idePessoaJuridica) {
		return
			representanteLegalDAO
				.isExiste(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica", "idePessoaFisica")
						.createAlias("idePessoaJuridica", "idePessoaJuridica")
							.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", idePessoaFisica))
							.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", idePessoaJuridica))
							.add(Restrictions.eq("indExcluido", true))

							.setProjection(Projections.projectionList()
									.add(Projections.property("indExcluido"),"indExcluido"))
							.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class)));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> listarRepresentanteLegalPorPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
						DetachedCriteria.forClass(RepresentanteLegal.class,"representante")
							.createAlias("representante.idePessoaFisica", "pessoaFisica")
							.createAlias("pessoaFisica.pessoa","pessoa")
							.createAlias("representante.idePessoaJuridica","pessoaJuridica")
							.createAlias("pessoaFisica.idePais","pais")
						.add(Restrictions.eq("representante.indExcluido", false))
						.add(Restrictions.eq("pessoaJuridica.idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> listarRepresentanteLegalPorPessoaJuridicaComProjection(PessoaJuridica pessoaJuridica)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
						DetachedCriteria.forClass(RepresentanteLegal.class,"representante")
							.createAlias("representante.idePessoaJuridica", "pessoaJuridica")
							.createAlias("pessoaJuridica.pessoa","pessoa")
						.add(Restrictions.eq("representante.indExcluido", false))
						.add(Restrictions.eq("pessoaJuridica.idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica()))

						.setProjection(Projections.projectionList()
							.add(Projections.property("representante.ideRepresentanteLegal"),"ideRepresentanteLegal")
							.add(Projections.property("pessoa.desEmail"),"idePessoaJuridica.pessoa.desEmail")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class)));
	}



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RepresentanteLegal buscarPorIdViaCriteria(RepresentanteLegal representante) {
		return
			representanteLegalDAO
				.buscarPorCriteria(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.add(Restrictions.eq("ideRepresentanteLegal", representante.getIdeRepresentanteLegal()))
						.add(Restrictions.eq("indExcluido", Boolean.FALSE)));
	}



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RepresentanteLegal> listarRepresentantesLegais(Collection<Integer> idsPj)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
						.createAlias("pf.pessoa", "p",JoinType.INNER_JOIN)
						.createAlias("pf.idePais", "pais", JoinType.INNER_JOIN)
						.createAlias("idePessoaJuridica", "pj", JoinType.INNER_JOIN)
						.setProjection(Projections.projectionList()
							.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
							.add(Projections.property("pf.numCpf"),"idePessoaFisica.numCpf")
							.add(Projections.property("p.desEmail"),"idePessoaFisica.pessoa.desEmail")
							.add(Projections.property("pais.nomPais"),"idePessoaFisica.idePais.nomPais")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class))
					.add(Restrictions.in("pj.idePessoaJuridica", idsPj))
					.add(Restrictions.eq("indExcluido", false)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RepresentanteLegal> listarRepresentantesLegaisPessoaFisica(Collection<Integer> idsPf)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica", "pf")
						.createAlias("pf.pessoa", "p")
						.createAlias("pf.idePais", "pais")
						.createAlias("idePessoaJuridica", "pj")

						.setProjection(Projections.projectionList()
							.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
							.add(Projections.property("pf.numCpf"),"idePessoaFisica.numCpf")
							.add(Projections.property("p.desEmail"),"idePessoaFisica.pessoa.desEmail")
							.add(Projections.property("pais.nomPais"),"idePessoaFisica.idePais.nomPais")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class))
					.add(Restrictions.in("pf.idePessoaFisica", idsPf))
					.add(Restrictions.eq("indExcluido", false)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> buscarRepresentanteLegalByPessoa(Integer ideRepresentante, Integer idePessoaJuridica)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
						.createAlias("idePessoaJuridica", "pj", JoinType.INNER_JOIN)
					.add(Restrictions.eq("indExcluido", false))
					.add(Restrictions.eq("pf.idePessoaFisica", ideRepresentante))
					.add(Restrictions.eq("pj.idePessoaJuridica", idePessoaJuridica)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> buscarRepresentanteLegal(Integer ideRepresentante)  {
		return
			representanteLegalDAO
				.listarPorCriteria(
					DetachedCriteria.forClass(RepresentanteLegal.class)
						.createAlias("idePessoaFisica", "pf")
						.createAlias("idePessoaJuridica", "pj")
					.add(Restrictions.eq("indExcluido", false))
					.add(Restrictions.eq("pf.idePessoaFisica", ideRepresentante)));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> getListaRepresentanteLegalByPessoa(PessoaJuridica pessoaJuridica)  {
		return
			representanteLegalDAO.listarPorCriteria(
				DetachedCriteria.forClass(RepresentanteLegal.class,"representante")
					.createAlias("representante.idePessoaFisica", "pessoaFisica", JoinType.INNER_JOIN)
					.createAlias("pessoaFisica.pessoa","pessoa", JoinType.INNER_JOIN)
					.createAlias("representante.idePessoaJuridica","pessoaJuricia",  JoinType.INNER_JOIN)
					.createAlias("pessoaFisica.idePais","pais", JoinType.INNER_JOIN)

				.add(Restrictions.eq("representante.indExcluido", false))
				.add(Restrictions.eq("pessoaJuricia.idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> getListaRepresentanteLegalPorRequerimento(Integer ideRequerimento)  {
		return
			representanteLegalDAO.listarPorCriteria(
				DetachedCriteria.forClass(RequerimentoPessoa.class)
					.createAlias("requerimento","req",JoinType.INNER_JOIN)
					.createAlias("ideTipoPessoaRequerimento","tipoPessoaReq",JoinType.INNER_JOIN)
					.createAlias("pessoa","p",JoinType.INNER_JOIN)
					.createAlias("p.pessoaFisica","pf",JoinType.INNER_JOIN)
					.createAlias("pf.representanteLegalCollection","rl",JoinType.INNER_JOIN)
					.createAlias("rl.idePessoaJuridica","rlPj",JoinType.INNER_JOIN)
					.createAlias("rl.idePessoaFisica","rlPf",JoinType.INNER_JOIN)
						.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
						.add(Restrictions.eq("tipoPessoaReq.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId()))
						.add(Restrictions.eq("rl.indExcluido", false))
						.setProjection(Projections.projectionList()
							.add(Projections.property("rl.ideRepresentanteLegal"),"ideRepresentanteLegal")
							.add(Projections.property("rl.dtcInicio"),"dtcInicio")
							.add(Projections.property("rl.dtcInicioRepresentacao"),"dtcInicioRepresentacao")
							.add(Projections.property("rl.dtcFimRepresentacao"),"dtcFimRepresentacao")
							.add(Projections.property("rl.dtcExclusao"),"dtcExclusao")
							.add(Projections.property("rl.indExcluido"),"indExcluido")
							.add(Projections.property("rl.dscCaminhoRepresentacao"),"dscCaminhoRepresentacao")
							.add(Projections.property("rlPj.idePessoaJuridica"),"idePessoaJuridica.idePessoaJuridica")
							.add(Projections.property("rlPf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class))

						.add(Subqueries.propertyEq("rl.idePessoaJuridica",
							DetachedCriteria.forClass(RequerimentoPessoa.class)
								.createAlias("pessoa", "p", JoinType.INNER_JOIN)
								.createAlias("p.pessoaJuridica", "pj", JoinType.INNER_JOIN)
									.add(Restrictions.eq("ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
									.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento))
								.setProjection(Projections.projectionList()
									.add(Projections.property("pj.idePessoaJuridica"), "idePessoaJuridica"))
								.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class)))));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> pessoasJuridicasDoRepresentanteLegal(PessoaFisica idePessoaFisica) {				
		DetachedCriteria criteria = DetachedCriteria.forClass(RepresentanteLegal.class)
			.createAlias("idePessoaJuridica", "pj", JoinType.INNER_JOIN)
				.add(Restrictions.eq("idePessoaFisica", idePessoaFisica))
				.add(Restrictions.eq("indExcluido", false))
				.setProjection(Projections.projectionList()
					.add(Projections.property("idePessoaJuridica.idePessoaJuridica"), "idePessoaJuridica.idePessoaJuridica")
					.add(Projections.property("pj.numCnpj"), "idePessoaJuridica.numCnpj")
					.add(Projections.property("pj.nomRazaoSocial"), "idePessoaJuridica.nomRazaoSocial"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class));
		
		return representanteLegalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> cnpjPessoasJuridicasDoRepresentanteLegal(PessoaFisica idPessoaFisica) {
		return
			representanteLegalDAO.listarPorCriteria(
				DetachedCriteria.forClass(RepresentanteLegal.class)
					.createAlias("idePessoaJuridica", "idePessoaJuridica", JoinType.INNER_JOIN)
						.add(Restrictions.eq("idePessoaFisica", idPessoaFisica))
						.add(Restrictions.eq("indExcluido", Boolean.FALSE))
					.setProjection(Projections.projectionList()
						.add(Projections.property("idePessoaJuridica.idePessoaJuridica"), "idePessoaJuridica")
						.add(Projections.property("idePessoaJuridica.numCnpj"), "numCnpj"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class)));
	}


	public void excluirRepresentanteLegal(RepresentanteLegal representanteLegal)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtcExclusao", representanteLegal.getDtcExclusao());
		params.put("indExcluido", representanteLegal.getIndExcluido());
		params.put("ideRepresentanteLegal", representanteLegal.getIdeRepresentanteLegal());
		representanteLegalDAO.executarNamedQuery("RepresentanteLegal.remove", params, new RepresentanteLegal[0]);
	}
}
