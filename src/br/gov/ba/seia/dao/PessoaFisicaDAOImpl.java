package br.gov.ba.seia.dao;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.dto.UsuarioInternoDTO;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaDAOImpl extends AbstractDAO<PessoaFisica>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<PessoaFisica> dao;
	
	@Inject
	private IDAO<RequerimentoUnico> requerimentoUnicoDAO;

	@EJB
	private ProcuradorPessoaFisicaDAOImpl procuradorPessoaFisicaDAOImpl;
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	
	@Override
	protected IDAO<PessoaFisica> getDAO() {
		return dao;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(PessoaFisica pessoaFisica) {
		try {
			pessoaDAOImpl.salvarAtualizarPessoa(pessoaFisica.getPessoa());
			dao.salvarOuAtualizar(pessoaFisica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	
	@Deprecated
    @SuppressWarnings("unchecked")
    public PessoaFisica getPessoaFisica(PessoaFisica pPessoaFisica) {

    	StringBuilder lEjbql = new StringBuilder("select pessoaFisica from PessoaFisica pessoaFisica inner join fetch pessoaFisica.idePais inner join fetch pessoaFisica.ideOcupacao " +
    										   "inner join fetch pessoaFisica.ideEscolaridade ");

    	if (!Util.isNull(pPessoaFisica)) {
    		lEjbql.append("where 1 = 1 ");
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())) lEjbql.append(" AND pessoaFisica.usuario.indTipoUsuario = :indTipoUsuario");
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) lEjbql.append(" AND pessoaFisica.idePessoaFisica = :idePessoaFisica");
    		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) lEjbql.append(" AND pessoaFisica.numCpf = :numCpf");
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) lEjbql.append(" AND pessoaFisica.ideOcupacao = :ideOcupacao");
    		if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())) lEjbql.append(" AND pessoaFisica.funcionario.ideArea = :ideArea");
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getDscLogin())) lEjbql.append(" AND pessoaFisica.usuario.dscLogin = :dscLogin");
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getIdePerfil()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getIdePerfil().getIdePerfil())) lEjbql.append(" AND pessoaFisica.usuario.idePerfil.idePerfil = :idePerfil");
    		if (!Util.isNull(pPessoaFisica.getNomPessoa())) lEjbql.append(" AND lower(pessoaFisica.nomPessoa) LIKE lower(:nomPessoa)");
    	}

    	lEjbql.append(" order by pessoaFisica.nomPessoa");
    	EntityManager lEntityManager = DAOFactory.getEntityManager();
    	lEntityManager.joinTransaction();
    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pPessoaFisica)) {
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())) lQuery.setParameter("indTipoUsuario", pPessoaFisica.getUsuario().getIndTipoUsuario());
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) lQuery.setParameter("idePessoaFisica", pPessoaFisica.getIdePessoaFisica());
    		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) lQuery.setParameter("numCpf", pPessoaFisica.getNumCpf());
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) lQuery.setParameter("ideOcupacao", pPessoaFisica.getIdeOcupacao());
    		if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())) lQuery.setParameter("ideArea", pPessoaFisica.getFuncionario().getIdeArea());
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getDscLogin())) lQuery.setParameter("dscLogin", pPessoaFisica.getUsuario().getDscLogin());
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getIdePerfil()) && !Util.isNullOuVazio(pPessoaFisica.getUsuario().getIdePerfil().getIdePerfil())) lQuery.setParameter("idePerfil", pPessoaFisica.getUsuario().getIdePerfil().getIdePerfil());
    		if (!Util.isNull(pPessoaFisica.getNomPessoa())) lQuery.setParameter("nomPessoa", pPessoaFisica.getNomPessoa() + "%");
    	}

    	Collection<PessoaFisica> lColecaoPessoaFisica = lQuery.getResultList();

    	for (PessoaFisica lPessoaFisica : lColecaoPessoaFisica) {

    		return lPessoaFisica;
    	}

   		return null;
    }


    @Deprecated
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
    public List<PessoaFisica> getPessoasFisica(PessoaFisica pPessoaFisica) {

    	StringBuilder lEjbql = new StringBuilder("select pessoaFisica " +
    			                               "from PessoaFisica pessoaFisica " +
    			                                    "left join fetch pessoaFisica.idePais " +
    			                                    "left join fetch pessoaFisica.ideOcupacao " +
    										        "left join fetch pessoaFisica.ideEscolaridade ");

    	if (!Util.isNull(pPessoaFisica)) {
    		lEjbql.append("where 1 = 1 ");
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())){
    			lEjbql.append(" AND pessoaFisica.usuario.indTipoUsuario = :indTipoUsuario");
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) {
    			lEjbql.append(" AND pessoaFisica.idePessoaFisica = :idePessoaFisica");
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())){
    			lEjbql.append(" AND pessoaFisica.numCpf = :numCpf");
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) {
    			lEjbql.append(" AND pessoaFisica.ideOcupacao = :ideOcupacao");
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())){
    			lEjbql.append(" AND pessoaFisica.funcionario.ideArea = :ideArea");
    		}
    		if (!Util.isNull(pPessoaFisica.getNomPessoa())) {
    			lEjbql.append(" AND lower(pessoaFisica.nomPessoa) LIKE lower(:nomPessoa)");
    		}
    	}

    	lEjbql.append(" order by pessoaFisica.nomPessoa");
    	EntityManager lEntityManager = DAOFactory.getEntityManager();
    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pPessoaFisica)) {
    		if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())) {
    			lQuery.setParameter("indTipoUsuario", pPessoaFisica.getUsuario().getIndTipoUsuario());
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) {
    			lQuery.setParameter("idePessoaFisica", pPessoaFisica.getIdePessoaFisica());
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) {
    			lQuery.setParameter("numCpf", pPessoaFisica.getNumCpf());
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) {
    			lQuery.setParameter("ideOcupacao", pPessoaFisica.getIdeOcupacao());
    		}
    		if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())){
    			lQuery.setParameter("ideArea", pPessoaFisica.getFuncionario().getIdeArea());
    		}
    		if (!Util.isNull(pPessoaFisica.getNomPessoa())){
    			lQuery.setParameter("nomPessoa", "%" + pPessoaFisica.getNomPessoa() + "%");
    		}
    	}

    	return lQuery.getResultList();
    }


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<PessoaFisica> getPessoasFisicaRequerente(Integer firstPage,Integer pageSize,PessoaFisica pPessoaFisica)  {
    	DetachedCriteria criteria = getPessoaFisicaCriteria(pPessoaFisica);
		criteria.addOrder(Order.asc("nomPessoa"));

    	return dao.listarPorCriteriaDemanda(criteria,firstPage,pageSize);
    }
    

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Collection<PessoaFisica> listarProprietarioImovel(Integer ideImovel)  {
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class);
    	
    	criteria
    		.createAlias("ideImovel", "i",JoinType.INNER_JOIN)
    		.createAlias("idePessoa", "p",JoinType.INNER_JOIN)
    		.createAlias("p.pessoaFisica", "pf",JoinType.INNER_JOIN)
    		.add(Restrictions.eq("i.ideImovel", ideImovel))
    		
    		.addOrder(Order.asc("nomPessoa"))
    		
    		.setProjection(Projections.projectionList()
    			.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica")
    			.add(Projections.property("pf.nomPessoa"),"nomPessoa")
    		)
    		
    		.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisica.class))
    	;

    	return dao.listarPorCriteria(criteria);
    }
    
    public List<PessoaFisica> getPessoasFisicaRequerenteSemAcento(PessoaFisica pPessoaFisica)  {
    	DetachedCriteria criteria = getPessoaFisicaCriteriaSemAcento(pPessoaFisica);
		criteria.addOrder(Order.asc("nomPessoa"));

    	return dao.listarPorCriteria(criteria);
    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getPessoaFisicaCriteria(PessoaFisica pPessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
			.createAlias("pessoa", "p", JoinType.INNER_JOIN)
			.createAlias("idePais", "pais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideOcupacao", "ocupacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEscolaridade", "escolaridade", JoinType.LEFT_OUTER_JOIN)
			.createAlias("usuario", "u", JoinType.LEFT_OUTER_JOIN)
			.createAlias("funcionario", "f", JoinType.LEFT_OUTER_JOIN)
			;

    	criteria.setProjection(
			Projections.projectionList()
				.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.property("nomPessoa"),"nomPessoa")
				.add(Projections.property("numCpf"),"numCpf")
    	);
    	
    	criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisica.class));
    	
		if (!Util.isNull(pPessoaFisica)) {
			if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())) {
				criteria.add(Restrictions.eq("u.indTipoUsuario", pPessoaFisica.getUsuario().getIndTipoUsuario()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) {
				criteria.add(Restrictions.eq("idePessoaFisica", pPessoaFisica.getIdePessoaFisica()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) {
				criteria.add(Restrictions.eq("numCpf", pPessoaFisica.getNumCpf()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) {
				criteria.add(Restrictions.eq("ideOcupacao", pPessoaFisica.getIdeOcupacao()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())) {
				criteria.add(Restrictions.eq("f.ideArea", pPessoaFisica.getFuncionario().getIdeArea()));
			}
			if (!Util.isNull(pPessoaFisica.getNomPessoa())) {
				criteria.add(Restrictions.ilike("nomPessoa", pPessoaFisica.getNomPessoa(), MatchMode.ANYWHERE));
			}
		}
		criteria.add(Restrictions.eq("p.indExcluido", false));
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getPessoaFisicaCriteriaSemAcento(PessoaFisica pPessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
    			.createAlias("idePais", "pais", JoinType.LEFT_OUTER_JOIN)
    			.createAlias("ideOcupacao", "ocupacao", JoinType.LEFT_OUTER_JOIN)
    			.createAlias("ideEscolaridade", "escolaridade", JoinType.LEFT_OUTER_JOIN)
    			.createAlias("usuario", "u", JoinType.LEFT_OUTER_JOIN)
    			.createAlias("funcionario", "f", JoinType.LEFT_OUTER_JOIN);

    	criteria.setProjection(
    			Projections.projectionList()
    			.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
    			.add(Projections.property("nomPessoa"),"nomPessoa")
    			.add(Projections.property("numCpf"),"numCpf")
    	);
    	
    	criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisica.class));
    	
		if (!Util.isNull(pPessoaFisica)) {
			if (!Util.isNullOuVazio(pPessoaFisica.getUsuario())) {
				criteria.add(Restrictions.eq("u.indTipoUsuario", pPessoaFisica.getUsuario().getIndTipoUsuario()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getIdePessoaFisica())) {
				criteria.add(Restrictions.eq("idePessoaFisica", pPessoaFisica.getIdePessoaFisica()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) {
				criteria.add(Restrictions.eq("numCpf", pPessoaFisica.getNumCpf()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getIdeOcupacao())) {
				criteria.add(Restrictions.eq("ideOcupacao", pPessoaFisica.getIdeOcupacao()));
			}
			if (!Util.isNullOuVazio(pPessoaFisica.getFuncionario()) && !Util.isNullOuVazio(pPessoaFisica.getFuncionario().getIdeArea())) {
				criteria.add(Restrictions.eq("f.ideArea", pPessoaFisica.getFuncionario().getIdeArea()));
			}
			if (!Util.isNull(pPessoaFisica.getNomPessoa())) {
				String parametro = Normalizer.normalize(pPessoaFisica.getNomPessoa(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase(); 
				criteria.add(Restrictions.sqlRestriction("TRANSLATE(Upper ({alias}.nom_pessoa),'Ã‚ÃƒÃ€ÃÃ‰ÃˆÃŠÃÃ•Ã“Ã’Ã”ÃšÃ‡','AAAAEEEIOOOOUC') LIKE  '%" + parametro + "%'")) ;
			}
		}
		return criteria;
	}
    
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisica(PessoaFisica pessoaFisica)  {
		pessoaDAOImpl.excluirPessoaNamedQuery(pessoaFisica.getPessoa());	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarPessoaFisica(List<Integer> idesPessoaFisicaAptos)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		criteria.setFetchMode("idePais", FetchMode.JOIN);	
		criteria.createAlias("pessoa", "pessoa");
		criteria.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE));
		
		if(!idesPessoaFisicaAptos.isEmpty()){
			criteria.add(Restrictions.in("idePessoaFisica", idesPessoaFisicaAptos));
		}
		
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> filtrarListaPessoaFisica(PessoaFisica pessoa) {
		Map<String, Object> paramPessoa = new HashMap<String, Object>();
		paramPessoa.put("indExcluido", false);
		paramPessoa.put("numCpf", pessoa.getNumCpf());
		return dao.buscarPorNamedQuery("PessoaFisica.findByNumCpf", paramPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpf(PessoaFisica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
			.createAlias("idePais","idePais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa","pessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa.enderecoPessoaCollection", "endPess", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideOcupacao", "ideOcupacao",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE))
			.add(Restrictions.eq("numCpf", pessoa.getNumCpf()));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpfCadastro(PessoaFisica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
			.createAlias("idePais","idePais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa","pessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa.enderecoPessoaCollection", "endPess", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideOcupacao", "ideOcupacao",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN)		
			.add(Restrictions.eq("numCpf", pessoa.getNumCpf()));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica consultarPessoaFisicaByNumCpf(String numCpf) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
			.createAlias("idePais","idePais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa","pessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa.enderecoPessoaCollection", "endPess", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPess.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideOcupacao", "ideOcupacao",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("numCpf", numCpf));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica consultarPessoaFisicaInativaByNumCpf(String numCpf) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class)
				.createAlias("idePais","idePais", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa","pessoa", JoinType.INNER_JOIN)
				.createAlias("pessoa.enderecoPessoaCollection", "endPess", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endPess.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endPess.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideOcupacao", "ideOcupacao",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("numCpf", numCpf))
				.add(Restrictions.eq("pessoa.indExcluido", true));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> consultarPessoaFisicaOrByNomeOrCpf(PessoaFisica pessoa, List<Integer> idesPessoaFisicaAptos) {
		if ((Util.isNull(pessoa.getNomPessoa()) || Util.isEmptyString(pessoa.getNomPessoa())) && (Util.isNull(pessoa.getNumCpf()) || Util.isEmptyString(pessoa.getNumCpf()))){
			return listarPessoaFisica(idesPessoaFisicaAptos);
		}
		else {
			return filtrarPessoasFisicaByCpfOrNome(pessoa, idesPessoaFisicaAptos);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoasFisicaByCpfOrNome(PessoaFisica pessoa, List<Integer> idesPessoaFisicaAptos) {
		Map<String, Object> paramPessoa = new HashMap<String, Object>();
		paramPessoa.put("indExcluido", false);
		paramPessoa.put("numCpf", pessoa.getNumCpf());
		if (!Util.isNull(pessoa.getNomPessoa()) && !Util.isEmptyString(pessoa.getNomPessoa())) { 
			paramPessoa.put("nomPessoa", "%" + pessoa.getNomPessoa().toLowerCase() + "%");
		} else {
			paramPessoa.put("nomPessoa", null);
		}

		paramPessoa.put("idesPessoaFisica", idesPessoaFisicaAptos);
		return dao.buscarPorNamedQuery("PessoaFisica.findByNumCpfOrNomeIdesPessoaFisica", paramPessoa);
	
	}    
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoasFisicas(PessoaFisica pessoa)  {
		Collection<PessoaFisica> pfs;
		DetachedCriteria criteria = getCriteriaFiltrarPessoasFisicas(pessoa);
		pfs = dao.listarPorCriteria(criteria, Order.desc("idePessoaFisica"));
		return pfs;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer filtrarPessoasFisicasCount(PessoaFisica pessoa)  {
		DetachedCriteria criteria = getCriteriaFiltrarPessoasFisicas(pessoa);
		return dao.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoasFisicasPorDemanda(PessoaFisica pessoa, Integer startPage, Integer maxPage)  {
		DetachedCriteria criteria = getCriteriaFiltrarPessoasFisicas(pessoa)
		  .addOrder(Order.asc("nomPessoa"));
		return dao.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaFiltrarPessoasFisicas(PessoaFisica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class, "pessoaFisica");
		criteria.createAlias("usuario", "usuario", JoinType.INNER_JOIN);
		criteria.createAlias("ideOcupacao", "ocupacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideEscolaridade", "escolaridade", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("usuario.indExcluido", false));
		criteria.add(Restrictions.eq("usuario.indTipoUsuario", pessoa.getUsuario().getIndTipoUsuario()));
		if (!Util.isNullOuVazio(pessoa.getNumCpf())) {
			criteria.add(Restrictions.eq("numCpf", pessoa.getNumCpf()));
		} else if (!Util.isNullOuVazio(pessoa.getNomPessoa())) {
			criteria.add(Restrictions.ilike("nomPessoa", pessoa.getNomPessoa().toUpperCase(), MatchMode.ANYWHERE));
		}
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarOuAtualizarPessoaFisica(PessoaFisica pessoaFisica) {
		pessoaFisica.getPessoa().getNomeRazao().trim().replaceAll("  +", " ");
		pessoaFisica.getPessoa().setPessoaFisica(null);
		pessoaDAOImpl.salvarAtualizarPessoa(pessoaFisica.getPessoa());
		dao.salvarOuAtualizar(pessoaFisica);
		return pessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean existeCadastro(PessoaFisica pessoaFisica) {
		PessoaFisica pessoaFisicaVerificacao = new PessoaFisica();
		pessoaFisicaVerificacao.setNumCpf(pessoaFisica.getNumCpf());
		return !Util.isNull(dao.buscarEntidadePorExemplo(pessoaFisicaVerificacao));
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByIdControleAcesso(PessoaFisica pPessoaFisica)  {
		return dao.buscarEntidadePorExemplo(pPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByAreaFuncionarioPerfilTipoUsuario(UsuarioInternoDTO pUsuarioInterno)  {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ideArea", pUsuarioInterno.getFuncionario().getIdeArea().getIdeArea());
		lParametros.put("indTipoUsuario", true);
		lParametros.put("idePerfil", pUsuarioInterno.getUsuario().getIdePerfil().getIdePerfil());

		return dao.buscarEntidadePorNamedQuery("PessoaFisica.findByAreaFuncionarioPerfilTipoUsuario", lParametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarListaPessoasFisicasControleAcesso(PessoaFisica pPessoaFisica) {
		return getPessoasFisica(pPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarPessoaFisicaControleAcesso(PessoaFisica pPessoaFisica)  {
		pessoaDAOImpl.salvarPessoa(pPessoaFisica.getPessoa());
		dao.salvar(pPessoaFisica);
		return pPessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica atualizarPessoaFisicaControleAcesso(PessoaFisica pPessoaFisica)  {
		pessoaDAOImpl.atualizarPessoa(pPessoaFisica.getPessoa());
		dao.atualizar(pPessoaFisica);
		return pPessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicaControleAcesso(PessoaFisica pessoaFisica)  {
		dao.remover(pessoaFisica);
		pessoaDAOImpl.excluirPessoa(pessoaFisica.getPessoa());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica)  {
		pessoaDAOImpl.salvarPessoa(pessoaFisica.getPessoa());
		dao.salvar(pessoaFisica);
		return pessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica atualizarPessoaFisica(PessoaFisica pessoaFisica)  {
		pessoaDAOImpl.atualizarPessoa(pessoaFisica.getPessoa());
		dao.atualizar(pessoaFisica);
		return pessoaFisica;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica carregarPessoaFisicaGet(Integer pIdePessoaFisica){
		return dao.carregarGet(pIdePessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicaSemAdmContainerDaEntidadePessoaFisicaControleAcesso(PessoaFisica pessoaFisica)  {
		dao.remover(pessoaFisica);
		pessoaFisica.getPessoa().setPessoaFisica(null);
		pessoaDAOImpl.excluirPessoaSemAdmContainerDaEntidadePessoa(pessoaFisica.getPessoa());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpfControleAcesso(PessoaFisica pessoa) {
		return dao.buscarEntidadePorExemplo(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> filtrarListaPessoaFisicaControleAcesso(PessoaFisica pessoa) {
		return dao.listarPorExemplo(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPessoaFisicaControleAcessoControleAcesso() {
		return dao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean validarNomePessoaFisica(PessoaFisica pessoaFisica){
		Boolean result = Boolean.TRUE;
		if(Util.isNull(pessoaFisica) || Util.isNull(pessoaFisica.getNomPessoa()) || pessoaFisica.getNomPessoa().trim().isEmpty()){
			result = Boolean.FALSE;
		}
		return result;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean validarCpf(PessoaFisica pessoaFisica){
		Boolean result = Boolean.TRUE;
		if(Util.isNull(pessoaFisica) || Util.isNull(pessoaFisica.getNomPessoa()) || pessoaFisica.getNumCpf().trim().isEmpty()){
			result = Boolean.FALSE;
		}
		
		return result;
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPorCriteriaDemanda(PessoaFisica pessoaFisica, Integer startPage, Integer maxPage) {		
		boolean usuarioExterno = ContextoUtil.getContexto().isUsuarioExterno();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		Integer idPessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
 
		criteria.setFetchMode("idePais", FetchMode.JOIN);
		criteria.createAlias("pessoa", "pessoa", JoinType.INNER_JOIN);
		if(usuarioExterno){
			criteria.createAlias("procuradorPessoaFisicaCollection", "procurador", JoinType.LEFT_OUTER_JOIN,
					Restrictions.sqlRestriction("procurador2_.ide_procurador="+idPessoaFisica+" and procurador2_.ind_excluido = false"));	
		}
		
		criteria.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE));
		
		if(validarNomePessoaFisica(pessoaFisica)){
			criteria.add(Restrictions.sqlRestriction("remover_acentuacao_uppercase(this_.nom_pessoa) ILIKE '%'||remover_acentuacao_uppercase('" + pessoaFisica.getNomPessoa().replace("'", "''") + "')||'%' ")) ;
			
		}
		if(validarCpf(pessoaFisica)){
			criteria.add(Restrictions.like("numCpf", pessoaFisica.getNumCpf(), MatchMode.EXACT));
		}			
		
		if(usuarioExterno){
			criteria.add(Restrictions.sqlRestriction(" ((this_.ide_pessoa_fisica = "+idPessoaFisica+") or (procurador2_.ide_procurador = "+idPessoaFisica+"))"));
		}
		criteria.addOrder(Order.asc("nomPessoa"));
		
		List<PessoaFisica> lista = dao.listarPorCriteriaDemanda(criteria, startPage, maxPage);
		
		/*
		 * if(usuarioExterno && !Util.isNullOuVazio(lista) && lista.size() > 1) {
		 * List<PessoaFisica> lista2 = new ArrayList<PessoaFisica>(); for(PessoaFisica
		 * pessoaFisica2:lista) {
		 * if(!Util.isNullOuVazio(pessoaFisica2.getProcuradorPessoaFisicaCollection()))
		 * { for(ProcuradorPessoaFisica
		 * procuradorPessoaFisica:pessoaFisica2.getProcuradorPessoaFisicaCollection()) {
		 * Hibernate.initialize(procuradorPessoaFisica.getIdePessoaFisica());
		 * lista2.add(procuradorPessoaFisica.getIdePessoaFisica()); } } }
		 * lista.addAll(lista2); }
		 */
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(PessoaFisica pessoaFisica) {
		boolean usuarioInterno = ContextoUtil.getContexto().isUsuarioExterno();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		criteria.setFetchMode("idePais", FetchMode.JOIN);
		criteria.createAlias("pessoa", "pessoa", JoinType.INNER_JOIN);
		if(usuarioInterno){
			criteria.createAlias("procuradorPessoaFisicaCollection", "procuradorpessoafisica", JoinType.LEFT_OUTER_JOIN);	
		}
		
		criteria.add(Restrictions.eq("pessoa.indExcluido", Boolean.FALSE));
		
		if(validarNomePessoaFisica(pessoaFisica)){
			criteria.add(Restrictions.sqlRestriction("remover_acentuacao_uppercase(this_.nom_pessoa) ILIKE '%'||remover_acentuacao_uppercase('" + pessoaFisica.getNomPessoa().replace("'", "''") + "')||'%' ")) ;
			
		}
		if(validarCpf(pessoaFisica)){
			criteria.add(Restrictions.like("numCpf", pessoaFisica.getNumCpf(), MatchMode.EXACT));
		}			
		
		if(usuarioInterno){
			Integer idPessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
			criteria.add(Restrictions.sqlRestriction(" ((this_.ide_pessoa_fisica = "+idPessoaFisica+") or (procurador2_.ide_procurador = "+idPessoaFisica+" and procurador2_.ind_excluido = false))"));
		}
		
		return dao.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPessoasFisicaFuncionario(PessoaFisica pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class,"pf");
		criteria.createAlias("pf.idePais", "pais",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.ideOcupacao", "ocupacao",JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("pf.funcionario", "funcionario",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.usuario", "usuario",JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("funcionario.ideArea", "area",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.procuradorPessoaFisicaCollection", "ppf", JoinType.LEFT_OUTER_JOIN);
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getNumCpf())) {
			criteria.add(Restrictions.eq("numCpf", pessoa.getNumCpf()));
		}
		if(!Util.isNullOuVazio(pessoa) &&!Util.isNullOuVazio(pessoa.getNomPessoa())) {
			criteria.add(Restrictions.ilike("nomPessoa", pessoa.getNomPessoa(),MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getUsuario()) && !Util.isNullOuVazio(pessoa.getUsuario().getIndExcluido()) ){
			criteria.add(Restrictions.eq("usuario.indExcluido",pessoa.getUsuario().getIndExcluido() ));
		}
		
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getFuncionario())){
			criteria.add(Restrictions.eq("usuario.indTipoUsuario",pessoa.getFuncionario() ));
		}
		
		
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPessoasFisicaFuncionarioControleDeAcesso(PessoaFisica pessoa, Boolean indTipoUsuario) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class,"pf");
		criteria.createAlias("pf.idePais", "pais",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.ideOcupacao", "ocupacao",JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("pf.funcionario", "funcionario",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.usuario", "usuario",JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("funcionario.ideArea", "area",JoinType.LEFT_OUTER_JOIN);		
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getNumCpf())) {
			criteria.add(Restrictions.eq("numCpf", pessoa.getNumCpf()));
		}
		if(!Util.isNullOuVazio(pessoa) &&!Util.isNullOuVazio(pessoa.getNomPessoa())) {
			criteria.add(Restrictions.ilike("nomPessoa", pessoa.getNomPessoa(),MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getUsuario()) && !Util.isNullOuVazio(pessoa.getUsuario().getIndExcluido()) ){
			criteria.add(Restrictions.eq("usuario.indExcluido",pessoa.getUsuario().getIndExcluido() ));
		}
		
		if(!Util.isNullOuVazio(pessoa) && !Util.isNullOuVazio(pessoa.getFuncionario())){
			criteria.add(Restrictions.eq("usuario.indTipoUsuario",pessoa.getFuncionario() ));
		}
		if (!Util.isNull(indTipoUsuario)) {
			criteria.add(Restrictions.eq("usuario.indTipoUsuario", indTipoUsuario));
		}
		
		return  dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countFiltroPessoaFisicaSolicitante(PessoaFisica pPessoa)  {
		return dao.count(getPessoaFisicaCriteria(pPessoa));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean existeRequerimentoPorPF(PessoaFisica pPessoaFisica)  {		
		
		StringBuilder sql = new StringBuilder(
			"SELECT DISTINCT req.ide_requerimento " +
			
			"FROM requerimento req " +
			"LEFT JOIN requerimento_unico reqUni ON req.ide_requerimento = reqUni.ide_requerimento_unico " +
			"INNER JOIN requerimento_pessoa reqPessoa ON req.ide_requerimento = reqPessoa.ide_requerimento " +
			
			"WHERE (req.num_requerimento IS NOT NULL) " +
			"AND req.ind_excluido = FALSE " +
			"AND reqPessoa.ide_pessoa = " + pPessoaFisica.getIdePessoaFisica());
				
		List<RequerimentoUnico> lista = requerimentoUnicoDAO.buscarPorNativeQuery(sql.toString(), null);
		
		Boolean retorno = false;
		
		if(!Util.isNullOuVazio(lista)) {
			retorno = true;
		} 
		
		return retorno;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPorCPF(PessoaFisica pessoaFisica) {
		try {
			
			return dao.buscarPorCriteria(
					DetachedCriteria.forClass(PessoaFisica.class)
						.createAlias("pessoa", "pessoa",JoinType.INNER_JOIN)	
						.createAlias("idePais", "pais",JoinType.LEFT_OUTER_JOIN)	
						.createAlias("ideOcupacao", "ocup",JoinType.LEFT_OUTER_JOIN)	
						.createAlias("ideEstadoCivil", "estCivil",JoinType.LEFT_OUTER_JOIN)	
						.createAlias("ideEscolaridade", "esc",JoinType.LEFT_OUTER_JOIN)	
						
						.add(Restrictions.eq("numCpf", pessoaFisica.getNumCpf()))
						
						.setProjection(Projections.projectionList()
							.add(Projections.property("idePessoaFisica"), "idePessoaFisica")
							.add(Projections.property("nomPessoa"), "nomPessoa")
							.add(Projections.property("tipSexo"), "tipSexo")
							.add(Projections.property("dtcNascimento"), "dtcNascimento")
							.add(Projections.property("desNaturalidade"), "desNaturalidade")
							.add(Projections.property("nomPai"), "nomPai")
							.add(Projections.property("nomMae"), "nomMae")
							.add(Projections.property("numCpf"), "numCpf")
							
							.add(Projections.property("pessoa.idePessoa"), "pessoa.idePessoa")
							.add(Projections.property("pessoa.dtcCriacao"), "pessoa.dtcCriacao")
							.add(Projections.property("pessoa.indExcluido"), "pessoa.indExcluido")
							.add(Projections.property("pessoa.desEmail"), "pessoa.desEmail")
							.add(Projections.property("pessoa.dtcExclusao"), "pessoa.dtcExclusao")
							
							.add(Projections.property("pais.idePais"), "idePais.idePais")
							.add(Projections.property("pais.nomPais"), "idePais.nomPais")
							
							.add(Projections.property("ocup.ideOcupacao"), "ideOcupacao.ideOcupacao")
							
							.add(Projections.property("estCivil.ideEstadoCivil"), "ideEstadoCivil.ideEstadoCivil")
							
							.add(Projections.property("esc.ideEscolaridade"), "ideEscolaridade.ideEscolaridade")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaFisica.class))
				);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}



}