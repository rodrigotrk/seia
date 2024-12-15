package br.gov.ba.seia.dao;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.enumerator.TipoProjetoEnum;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class PessoaJuridicaDAOImpl {

	@Inject
	private IDAO<PessoaJuridica> pessoaJuridicaDAO;
	
	@Inject
	private IDAO<RequerimentoUnico> requerimentoUnicoDAO;
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	
	@Inject
	private PermissaoPerfilService permissaoPerfilService;

	public void excluirPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		Pessoa pessoa = pessoaJuridica.getPessoa();
		pessoaDAOImpl.excluirPessoaNamedQuery(pessoa);
	}
	
	public List<PessoaJuridica> listarPessoaJuridicaConveniadaCERH(PessoaFisica pessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria
			.createAlias("procuradorRepresentanteCollection", "pr", JoinType.INNER_JOIN)
			.createAlias("contratoConvenioCollection", "cc", JoinType.INNER_JOIN)
			.createAlias("cc.ideTipoProjeto", "tp", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pr.indExcluido", false))
			.add(Restrictions.eq("pr.ideProcurador", pessoaFisica))
			.add(Restrictions.eq("tp.ideTipoProjeto", TipoProjetoEnum.PDA_CERH.getId()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoaJuridica"),"idePessoaJuridica")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class));
		
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
	}
	
	public List<PessoaJuridica> listarPessoaJuridicaRepresentada(PessoaFisica pessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria
			.createAlias("procuradorRepresentanteCollection", "pr", JoinType.LEFT_OUTER_JOIN)
			.createAlias("representanteLegalCollection", "rl", JoinType.LEFT_OUTER_JOIN)
		
			.add(Restrictions.disjunction()
				.add(Restrictions.and(
						Restrictions.eq("pr.ideProcurador", pessoaFisica), 
						Restrictions.eq("pr.indExcluido", false)
					)
				)
				.add(Restrictions.and(
						Restrictions.eq("rl.idePessoaFisica", pessoaFisica), 
						Restrictions.eq("rl.indExcluido", false)
					)
				)			
			)
		
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("idePessoaJuridica"),"idePessoaJuridica")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class))
		;
		
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
	}

	public List<PessoaJuridica> listarPessoaJuridica()  {
		List<Integer> idesPessoaJuridicaAptos = permissaoPerfilService.listarIdesPessoaJuridicaAptos();
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria.setFetchMode("ideNaturezaJuridica", FetchMode.JOIN)
		.createAlias("pessoa", "pessoa")
		.add(Restrictions.and(
				Restrictions.eq("pessoa.indExcluido", false), 
				Restrictions.in("idePessoaJuridica", idesPessoaJuridicaAptos)));
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
		
	}

	public List<PessoaJuridica> filtrarPessoaJuridica(Integer firstPage,Integer pageSize, PessoaJuridica pessoaJuridica)  {
		DetachedCriteria criteria = getListaPessoaJuridicaCriteria(pessoaJuridica);
		
		criteria.addOrder(Order.asc("nomRazaoSocial"));
		
		return pessoaJuridicaDAO.listarPorCriteriaDemanda(criteria,firstPage,pageSize);
	}
	
	public List<PessoaJuridica> filtrarPessoaJuridicaSemAcento(PessoaJuridica pessoaJuridica) {
		DetachedCriteria criteria = getListaPessoaJuridicaCriteriaSemAcento(pessoaJuridica);
		
		criteria.addOrder(Order.asc("nomRazaoSocial"));
		
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria getListaPessoaJuridicaCriteria(PessoaJuridica pessoaJuridica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class).setFetchMode("pessoa", FetchMode.JOIN);
		
    	criteria.setProjection(
    			Projections.projectionList()
    			.add(Projections.property("idePessoaJuridica"),"idePessoaJuridica")
    			.add(Projections.property("pessoa.idePessoa"),"pessoa.idePessoa")
    			.add(Projections.property("nomRazaoSocial"),"nomRazaoSocial")
    			.add(Projections.property("numCnpj"),"numCnpj")
    	);
    	criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class));
		
		if (!Util.isNullOuVazio(pessoaJuridica.getNomRazaoSocial())) {
			criteria.add(Restrictions.ilike("nomRazaoSocial", pessoaJuridica.getNomRazaoSocial(), MatchMode.ANYWHERE));
		}
		if (!Util.isNullOuVazio(pessoaJuridica.getNumCnpj())) {
			criteria.add(Restrictions.eq("numCnpj", pessoaJuridica.getNumCnpj()));
		}
		return criteria;
	}
	
	private DetachedCriteria getListaPessoaJuridicaCriteriaSemAcento(PessoaJuridica pessoaJuridica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class).setFetchMode("pessoa", FetchMode.JOIN);
		
    	criteria.setProjection(
    			Projections.projectionList()
    			.add(Projections.property("pj.idePessoaJuridica"),"idePessoaJuridica")
    			.add(Projections.property("pessoa.idePessoa"),"pessoa.idePessoa")
    			.add(Projections.property("pj.nomRazaoSocial"),"nomRazaoSocial")
    			.add(Projections.property("pj.numCnpj"),"numCnpj")
    	);
    	criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class));
		
		if (!Util.isNullOuVazio(pessoaJuridica.getNomRazaoSocial())) {
			String parametro = Normalizer.normalize(pessoaJuridica.getNomRazaoSocial(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase(); 
			criteria.add(Restrictions.sqlRestriction("TRANSLATE(Upper ({alias}.nom_razao_social),'ÂÃÀÁÉÈÊÍÕÓÒÔÚÇ','AAAAEEEIOOOOUC') LIKE  '%" + parametro + "%'")) ;
		}
		if (!Util.isNullOuVazio(pessoaJuridica.getNumCnpj())) {
			criteria.add(Restrictions.eq("numCnpj", pessoaJuridica.getNumCnpj()));
		}
		return criteria;
	}

	public List<PessoaJuridica> filtrarListaPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		Map<String, Object> paramPessoa = new HashMap<String, Object>();
		paramPessoa.put("indExcluido", false);
		paramPessoa.put("numCnpj", pessoaJuridica.getNumCnpj());
		if (!Util.isEmptyString(pessoaJuridica.getNomRazaoSocial())) {
			paramPessoa.put("nomRazaoSocial", "%" + pessoaJuridica.getNomRazaoSocial().toLowerCase() + "%");
		} else {
			paramPessoa.put("nomRazaoSocial", "%");
		}
		paramPessoa.put("idesPessoaJuridica", permissaoPerfilService.listarIdesPessoaJuridicaAptos());
		return pessoaJuridicaDAO.buscarPorNamedQuery("PessoaJuridica.findByCnpjOrRazaoSocialIdesPessoaJuridica", paramPessoa);		
	}

	public PessoaJuridica filtrarPessoaJuridicaByCnpj(PessoaJuridica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria.setFetchMode("ideNaturezaJuridica", FetchMode.JOIN);
		criteria.createAlias("pessoa", "pessoa");
		criteria.add(Restrictions.and(
				Restrictions.eq("pessoa.indExcluido", Boolean.FALSE), 
				Restrictions.eq("numCnpj", pessoa.getNumCnpj())));
		return pessoaJuridicaDAO.buscarPorCriteria(criteria);
	}
	
	public PessoaJuridica filtrarPJbyCNPJlacTransporte(PessoaJuridica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class)
				.createAlias("pessoa", "p")
				.add(Restrictions.and(
						Restrictions.eq("p.indExcluido", Boolean.FALSE), 
						Restrictions.eq("numCnpj", pessoa.getNumCnpj())))
						.setProjection(
								Projections.projectionList()
								.add(Projections.property("idePessoaJuridica"), "idePessoaJuridica")
								.add(Projections.property("nomRazaoSocial"), "nomRazaoSocial")
								.add(Projections.property("numCnpj"), "numCnpj")
								.add(Projections.property("p.idePessoa"), "pessoa.idePessoa")
								.add(Projections.property("p.desEmail"), "pessoa.desEmail")
								)
								.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class))
								;
		return pessoaJuridicaDAO.buscarPorCriteria(criteria);
	}

	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		pessoaJuridica.getPessoa().setPessoaJuridica(null);
		pessoaDAOImpl.salvarPessoa(pessoaJuridica.getPessoa());
		pessoaJuridicaDAO.salvar(pessoaJuridica);
		pessoaJuridica.getPessoa().setPessoaJuridica(pessoaJuridica);
		return pessoaJuridica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica salvarOuAtualizarPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		pessoaJuridica.getPessoa().setPessoaJuridica(null);
		pessoaDAOImpl.salvarAtualizarPessoa(pessoaJuridica.getPessoa());
		pessoaJuridicaDAO.salvarOuAtualizar(pessoaJuridica);
		pessoaJuridica.getPessoa().setPessoaJuridica(pessoaJuridica);
		return pessoaJuridica;
	}

	public Boolean validarRazaoSocial(PessoaJuridica pessoaJuridica) {
		Boolean result = Boolean.TRUE;
		if (Util.isNull(pessoaJuridica) || Util.isNull(pessoaJuridica.getNomRazaoSocial()) || pessoaJuridica.getNomRazaoSocial().trim().isEmpty()) {
			result = Boolean.FALSE;
		}
		return result;
	}

	public Boolean validarCnpj(PessoaJuridica pessoaJuridica) {
		Boolean result = Boolean.TRUE;
		if (Util.isNull(pessoaJuridica) || Util.isNull(pessoaJuridica.getNumCnpj()) || pessoaJuridica.getNumCnpj().trim().isEmpty()) {
			result = Boolean.FALSE;
		}
		return result;
	}

	public List<PessoaJuridica> listarPorCriteriaDemanda(PessoaJuridica pessoaJuridica, Integer startPage, Integer maxPage, List<Integer> idesPessoaJuridicaAptos)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		
		criteria.createAlias("ideNaturezaJuridica", "ideNaturezaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoa", "pessoa");
		criteria.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE));
		
		if (validarRazaoSocial(pessoaJuridica)) {
			criteria.add(Restrictions.sqlRestriction("remover_acentuacao_uppercase(this_.nom_razao_social) ILIKE '%'||remover_acentuacao_uppercase('" + pessoaJuridica.getNomRazaoSocial().replace("'", "''") + "')||'%' ")) ;
		}
		if (validarCnpj(pessoaJuridica)) {
			criteria.add(Restrictions.like("numCnpj", pessoaJuridica.getNumCnpj(), MatchMode.EXACT));
		}
		if (!Util.isNullOuVazio(idesPessoaJuridicaAptos) && !idesPessoaJuridicaAptos.isEmpty()) {
			criteria.add(Restrictions.in("idePessoaJuridica", idesPessoaJuridicaAptos));
		}
		criteria.addOrder(Order.asc("nomRazaoSocial"));
		
		return pessoaJuridicaDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}

	public Integer count(PessoaJuridica pessoaJuridica, List<Integer> idesPessoaJuridicasAptos)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria.setFetchMode("ideNaturezaJuridica", FetchMode.JOIN);
		criteria.createAlias("pessoa", "pessoa");
		criteria.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE));
		if (validarRazaoSocial(pessoaJuridica)) {
			criteria.add(Restrictions.sqlRestriction("remover_acentuacao_uppercase(this_.nom_razao_social) ILIKE '%'||remover_acentuacao_uppercase('" + pessoaJuridica.getNomRazaoSocial().replace("'", "''") + "')||'%' ")) ;
		}
		if (validarCnpj(pessoaJuridica)) {
			criteria.add(Restrictions.like("numCnpj", pessoaJuridica.getNumCnpj(), MatchMode.EXACT));
		}
		if (!Util.isNullOuVazio(idesPessoaJuridicasAptos) && !idesPessoaJuridicasAptos.isEmpty()) {
			criteria.add(Restrictions.in("idePessoaJuridica", idesPessoaJuridicasAptos));
		}
		return pessoaJuridicaDAO.count(criteria);
	}

	public PessoaJuridica obterPessoaJuridicaId(PessoaJuridica pessoaJuridica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class)
				.add(Restrictions.eq("idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica()))
				.setFetchMode("pessoa", FetchMode.JOIN);
		return pessoaJuridicaDAO.buscarPorCriteria(criteria);
	}

	public List<PessoaJuridica> filtrarPessoaJuridicaRequerenteExterno(PessoaJuridica pPessoaJuridica)  {
		DetachedCriteria criteria = getCriteriaConsultaPessoa(pPessoaJuridica);
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
	}
	
	public List<PessoaJuridica> filtrarPessoaJuridicaRequerenteExternoSemAcento(PessoaJuridica pPessoaJuridica)  {
		DetachedCriteria criteria = getCriteriaConsultaPessoaSemAcento(pPessoaJuridica);
		return pessoaJuridicaDAO.listarPorCriteria(criteria);
	}

	public DetachedCriteria getCriteriaConsultaPessoa(PessoaJuridica pPessoaJuridica) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class, "pj")
				.createAlias("pj.pessoa","pessoa")
				.createAlias("pj.procuradorRepresentanteCollection", "procRepres", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.representanteLegalCollection", "represL", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.participacaoAcionariaCollection", "partAc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("represL.idePessoaFisica", "pfRL", JoinType.LEFT_OUTER_JOIN)
				.createAlias("procRepres.ideProcurador", "pfPR", JoinType.LEFT_OUTER_JOIN)
				.createAlias("partAc.idePessoa", "pPA", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pPA.pessoaFisica", "pfPA", JoinType.LEFT_OUTER_JOIN);

		if(!Util.isNullOuVazio(pPessoaJuridica.getNomRazaoSocial()))
			criteria.add(Restrictions.eq("pj.nomRazaoSocial", pPessoaJuridica.getNomRazaoSocial()));
		if(!Util.isNullOuVazio(pPessoaJuridica.getNumCnpj())){
			criteria.add(Restrictions.eq("pj.numCnpj", pPessoaJuridica.getNumCnpj()));
		}
		
		if(!Util.isNullOuVazio(pPessoaJuridica.getPessoa()) && !Util.isNullOuVazio(pPessoaJuridica.getPessoa().getPessoaFisica()) && Util.isNullOuVazio(pPessoaJuridica.getNumCnpj())){
			criteria.add(Restrictions.or(
				Restrictions.eq("pfPR.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()), 
				Restrictions.or(
						Restrictions.eq("pfPA.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()), 
						Restrictions.eq("pfRL.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()))));
		}
		
		criteria.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("pj.idePessoaJuridica"), "idePessoaJuridica")
						.add(Projections.groupProperty("pj.nomRazaoSocial"), "nomRazaoSocial")
						.add(Projections.groupProperty("pj.nomeFantasia"), "nomeFantasia")
						.add(Projections.groupProperty("pj.dtcAbertura"), "dtcAbertura")
						.add(Projections.groupProperty("pj.numCnpj"), "numCnpj")
						.add(Projections.groupProperty("pj.numInscricaoMunicipal"), "numInscricaoMunicipal")
						.add(Projections.groupProperty("pj.numInscricaoEstadual"), "numInscricaoEstadual")
						.add(Projections.groupProperty("pj.ideNaturezaJuridica"), "ideNaturezaJuridica")
						.add(Projections.groupProperty("pessoa.idePessoa"), "pessoa.idePessoa")						
						)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class));
		return criteria;
	}
	
	
	public DetachedCriteria getCriteriaConsultaPessoaSemAcento(PessoaJuridica pPessoaJuridica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class, "pj")
				.createAlias("pj.pessoa","pessoa")
				.createAlias("pj.procuradorRepresentanteCollection", "procRepres", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.representanteLegalCollection", "represL", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.participacaoAcionariaCollection", "partAc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("represL.idePessoaFisica", "pfRL", JoinType.LEFT_OUTER_JOIN)
				.createAlias("procRepres.ideProcurador", "pfPR", JoinType.LEFT_OUTER_JOIN)
				.createAlias("partAc.idePessoa", "pPA", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pPA.pessoaFisica", "pfPA", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.or(
						Restrictions.eq("pfPR.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()), 
						Restrictions.or(
								Restrictions.eq("pfPA.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()), 
								Restrictions.eq("pfRL.idePessoaFisica", pPessoaJuridica.getPessoa().getIdePessoa()))));
		if(!Util.isNullOuVazio(pPessoaJuridica.getNomRazaoSocial())){
			String parametro = Normalizer.normalize(pPessoaJuridica.getNomRazaoSocial(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase(); 
			criteria.add(Restrictions.sqlRestriction("TRANSLATE(Upper ({alias}.nom_razao_social),'ÂÃÀÁÉÈÊÍÕÓÒÔÚÇ','AAAAEEEIOOOOUC') LIKE  '%" + parametro + "%'")) ;
		}if(!Util.isNullOuVazio(pPessoaJuridica.getNumCnpj()))
			criteria.add(Restrictions.eq("pj.numCnpj", pPessoaJuridica.getNumCnpj()));
		
			criteria.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("pj.idePessoaJuridica"), "idePessoaJuridica")
						.add(Projections.groupProperty("pj.nomRazaoSocial"), "nomRazaoSocial")
						.add(Projections.groupProperty("pj.nomeFantasia"), "nomeFantasia")
						.add(Projections.groupProperty("pj.dtcAbertura"), "dtcAbertura")
						.add(Projections.groupProperty("pj.numCnpj"), "numCnpj")
						.add(Projections.groupProperty("pj.numInscricaoMunicipal"), "numInscricaoMunicipal")
						.add(Projections.groupProperty("pj.numInscricaoEstadual"), "numInscricaoEstadual")
						.add(Projections.groupProperty("pj.ideNaturezaJuridica"), "ideNaturezaJuridica")
						.add(Projections.groupProperty("pessoa.idePessoa"), "pessoa.idePessoa")						
						)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridica.class));
		return criteria;
	}

	public int countFiltroPessoaJuridicaSolicitanteExterno(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaDAO.count(getCriteriaConsultaPessoa(pessoaJuridica));
	}

	public int countFiltroPessoaJuridicaSolicitante(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaDAO.count(getListaPessoaJuridicaCriteria(pessoaJuridica));
	}
	
	public Boolean existeRequerimentoPorPJ(PessoaJuridica pPessoaJuridica)  {		
		
		StringBuilder sql = new StringBuilder()
			.append("SELECT DISTINCT req.ide_requerimento ")
			.append("FROM requerimento req ")
			.append("LEFT JOIN requerimento_unico reqUni ON req.ide_requerimento = reqUni.ide_requerimento_unico ")
			.append("INNER JOIN requerimento_pessoa reqPessoa ON req.ide_requerimento = reqPessoa.ide_requerimento ")
			
			.append("WHERE (req.num_requerimento IS NOT NULL) ")
			.append("AND req.ind_excluido = FALSE ")
			.append("AND reqPessoa.ide_pessoa = ")
			.append(pPessoaJuridica.getIdePessoaJuridica());
			
		List<RequerimentoUnico> lista = requerimentoUnicoDAO.buscarPorNativeQuery(sql.toString(), null);

		return !Util.isNullOuVazio(lista);
	}
}