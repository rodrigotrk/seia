package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class PessoaDAOImpl implements PessoaDAOIf {

	@Inject
	private IDAO<Pessoa> pessoaDAO;
	
	@EJB
	private UsuarioService usuarioService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscar(Pessoa pessoa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
			.createAlias("pessoaFisica"	 ,"idePessoaFisica"	 ,JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica.idePais", "idePais", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoa"), "idePessoa")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("desEmail"), "desEmail")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				
				.add(Projections.property("idePessoaFisica.idePessoaFisica"), "pessoaFisica.idePessoaFisica")
				.add(Projections.property("idePessoaFisica.nomPessoa"), "pessoaFisica.nomPessoa")
				.add(Projections.property("idePessoaFisica.tipSexo"), "pessoaFisica.tipSexo")
				.add(Projections.property("idePessoaFisica.dtcNascimento"), "pessoaFisica.dtcNascimento")
				.add(Projections.property("idePessoaFisica.desNaturalidade"), "pessoaFisica.desNaturalidade")
				.add(Projections.property("idePessoaFisica.nomPai"), "pessoaFisica.nomPai")
				.add(Projections.property("idePessoaFisica.nomMae"), "pessoaFisica.nomMae")
				.add(Projections.property("idePessoaFisica.numCpf"), "pessoaFisica.numCpf")
				.add(Projections.property("idePessoaFisica.idePais"), "pessoaFisica.idePais")
				
				.add(Projections.property("idePais.idePais"), "pessoaFisica.idePais.idePais")
				.add(Projections.property("idePais.nomPais"), "pessoaFisica.idePais.nomPais")
				
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));
		
		return pessoaDAO.buscarPorCriteria(getRestrictions(pessoa, criteria));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarPessoasRequerimento(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class)
				
				.createAlias("requerimentoPessoaCollection", "rp", JoinType.INNER_JOIN)
				.createAlias("rp.ideTipoPessoaRequerimento", "tpr", JoinType.INNER_JOIN)
				.createAlias("rp.pessoa", "p", JoinType.INNER_JOIN)
				.createAlias("p.pessoaFisica", "pf", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.in("tpr.ideTipoPessoaRequerimento", new Integer[] {
					TipoPessoaRequerimentoEnum.REQUERENTE.getId(),
					TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId(),
					TipoPessoaRequerimentoEnum.PROCURADOR.getId()
				}))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("p.idePessoa"), "idePessoa")
					.add(Projections.property("p.dtcCriacao"), "dtcCriacao")
					.add(Projections.property("p.indExcluido"), "indExcluido")
					.add(Projections.property("p.desEmail"), "desEmail")
					.add(Projections.property("p.dtcExclusao"), "dtcExclusao")
					
					.add(Projections.property("pf.idePessoaFisica"), "pessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"), "pessoaFisica.nomPessoa")
					.add(Projections.property("pf.tipSexo"), "pessoaFisica.tipSexo")
					.add(Projections.property("pf.dtcNascimento"), "pessoaFisica.dtcNascimento")
					.add(Projections.property("pf.desNaturalidade"), "pessoaFisica.desNaturalidade")
					.add(Projections.property("pf.nomPai"), "pessoaFisica.nomPai")
					.add(Projections.property("pf.nomMae"), "pessoaFisica.nomMae")
					.add(Projections.property("pf.numCpf"), "pessoaFisica.numCpf")
						
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));
		
		return pessoaDAO.listarPorCriteria(criteria);
	}
	
	private DetachedCriteria getRestrictions(Pessoa pessoa,DetachedCriteria criteria ){
		if(pessoa.getIdePessoa()!=null){
			criteria
				.add(Restrictions.eq("idePessoa", pessoa.getIdePessoa()));
		}
		if(pessoa.getPessoaFisica() != null && pessoa.getPessoaFisica().getNumCpf()!=null){
			criteria
				.add(Restrictions.eq("idePessoaFisica.numCpf", pessoa.getPessoaFisica().getNumCpf()));
		}
		
		return criteria;
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public Pessoa getPessoa(Pessoa pPessoa) {

    	StringBuilder lEjbql = new StringBuilder("select pessoa from Pessoa pessoa ");

    	if (!Util.isNull(pPessoa)) {
    		lEjbql.append("where pessoa.indExcluido = :indExcluido ");
    		if (!Util.isNull(pPessoa.getIdePessoa())) lEjbql.append(" AND pessoa.idePessoa = :idePessoa");
    		if (!Util.isNull(pPessoa.getDesEmail())) lEjbql.append(" AND lower(pessoa.desEmail) = lower(:desEmail)");
    	}

    	Query lQuery = DAOFactory.getEntityManager().createQuery(lEjbql.toString());

    	if (!Util.isNull(pPessoa)) {
    		lQuery.setParameter("indExcluido", pPessoa.getIndExcluido());
    		if (!Util.isNull(pPessoa.getIdePessoa())) lQuery.setParameter("idePessoa", pPessoa.getIdePessoa());
    		if (!Util.isNull(pPessoa.getDesEmail())) lQuery.setParameter("desEmail", pPessoa.getDesEmail());
    	}

    	Collection<Pessoa> lColecaoPessoa = lQuery.getResultList();

    	for (Pessoa lPessoa : lColecaoPessoa) {
    		return lPessoa;
    	}

   		return null;
    }

    @Override
	@SuppressWarnings("unchecked")
    public List<Pessoa> getPessoas(Pessoa pPessoa) {

    	StringBuilder lEjbql = new StringBuilder("select pessoa from Pessoa pessoa ");

    	if (!Util.isNull(pPessoa)) {
    		lEjbql.append("where pessoa.indExcluido = :indExcluido ");
    		if (!Util.isNull(pPessoa.getIdePessoa())) lEjbql.append(" AND pessoa.idePessoa = :idePessoa");
    		if (!Util.isNull(pPessoa.getDesEmail())) lEjbql.append(" AND lower(pessoa.desEmail) LIKE lower(:desEmail)");
    	}

    	lEjbql.append(" order by pessoa.desEmail");
    	EntityManager lEntityManager = DAOFactory.getEntityManager();
    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pPessoa)) {
    		lQuery.setParameter("indExcluido", pPessoa.getIndExcluido());
    		if (!Util.isNull(pPessoa.getIdePessoa())) lQuery.setParameter("idePessoa", pPessoa.getIdePessoa());
    		if (!Util.isNull(pPessoa.getDesEmail())) lQuery.setParameter("desEmail", pPessoa.getDesEmail() + "%");
    	}

    	return lQuery.getResultList();
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoa(Pessoa pPessoa)  {
		
		if (Util.isNullOuVazio(pPessoa)) {
			pPessoa.setDtcCriacao(new Date());
		}
		
		pessoaDAO.salvar(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarPessoa(Pessoa pPessoa)  {
		pessoaDAO.salvarOuAtualizar(pPessoa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPessoa(Pessoa pPessoa)  {	
		Usuario usuario = null;
		
		if(!Util.isNullOuVazio(pPessoa.getPessoaFisica()) && !Util.isNullOuVazio(pPessoa.getPessoaFisica().getUsuario())){
			usuario = 	pPessoa.getPessoaFisica().getUsuario();
			pPessoa.getPessoaFisica().setUsuario(null);
		}
		
		pessoaDAO.atualizar(pPessoa);
		
	
		if (!Util.isNullOuVazio(usuario) && Util.isNullOuVazio(usuario.getIdePessoaFisica())) {
			usuario.setPessoaFisica(pPessoa.getPessoaFisica());
		}

		if(!Util.isNullOuVazio(usuario)) usuarioService.atualizarUsuario(usuario);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoa(Pessoa pPessoa) {
		pessoaDAO.remover(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaNamedQuery(Pessoa pessoa) {		
		Map<String, Object> paramPessoa = new HashMap<String, Object>();
		paramPessoa.put("idePessoa", pessoa.getIdePessoa());
		paramPessoa.put("dtcExclusao", new Date());		
		pessoaDAO.executarNamedQuery("Pessoa.remove", paramPessoa, new Pessoa[0]);	
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaSemAdmContainerDaEntidadePessoa(Pessoa pPessoa) {
		pessoaDAO.remover(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa carregarGet(Integer pIdePessoa){
		return pessoaDAO.carregarGet(pIdePessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Pessoa carregarLoad(Integer pIdePessoa)  {
		return pessoaDAO.carregarLoad(pIdePessoa);
	}
	
	public Pessoa carregarDadosTipoRequerentePessoa(Integer ideRequerimento, TipoPessoaRequerimentoEnum tprEnum, PessoaFisica pessoaFisica)  {
		return pessoaDAO.buscarPorCriteria(getCriteriaParaRequerimentoPessoa(ideRequerimento, tprEnum, pessoaFisica));
	}

	public Pessoa carregarDadosRequerenteCerh(Pessoa pessoa)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
			.createAlias("pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("idePessoa", pessoa.getIdePessoa()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoa"), "idePessoa")
				.add(Projections.property("desEmail"), "desEmail")
				.add(Projections.property("pf.nomPessoa"), "pessoaFisica.nomPessoa")
				.add(Projections.property("pf.numCpf"), "pessoaFisica.numCpf")
				.add(Projections.property("pj.nomRazaoSocial"), "pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"), "pessoaJuridica.numCnpj")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class))
		;
		
		return pessoaDAO.buscarPorCriteria(criteria);
	}
	
	public Pessoa carregarDadosRequerente(Integer ideRequerimento, PessoaFisica pessoaFisica)  {
		return pessoaDAO.buscarPorCriteria(getCriteriaParaRequerimentoPessoa(ideRequerimento, TipoPessoaRequerimentoEnum.REQUERENTE, pessoaFisica));
	}

	private DetachedCriteria getCriteriaParaRequerimentoPessoa(Integer ideRequerimento, TipoPessoaRequerimentoEnum tipoPessoaRequerimento, PessoaFisica pessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("requerimentoPessoaCollection", "rpc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pf.ideOcupacao", "ocup",JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoaCollection", "enderecoPessoa", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoa.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoa.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideTipoLogradouro", "tipoLog", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN);
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("requerimentoPessoaCollection", "rpc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.requerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoaCollection", "enderecoPessoa", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoa.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoa.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.property("enderecoPessoa.ideEndereco"))
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("endereco.indExcluido", false));

		if (!Util.isNullOuVazio(pessoaFisica)){
			criteria.add(Restrictions.eq("idePessoa", pessoaFisica.getIdePessoaFisica()));
		}
				
		criteria.add(Restrictions.or(
						Restrictions.eq("tpr.ideTipoPessoaRequerimento", tipoPessoaRequerimento.getId()),
						Restrictions.isNull("tpr.ideTipoPessoaRequerimento")))
				.add(Restrictions.or(
						Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.RESIDENCIAL.getId()),
						Restrictions.isNull("tipoEndereco.ideTipoEndereco")));
		
		criteria.setProjection(
				Projections
					.projectionList()
						.add(Projections.property("idePessoa"), "idePessoa")
						.add(Projections.property("pf.nomPessoa"), "pessoaFisica.nomPessoa")
						.add(Projections.property("pf.numCpf"), "pessoaFisica.numCpf")
						.add(Projections.property("pf.desNaturalidade"), "pessoaFisica.desNaturalidade")
						.add(Projections.property("ocup.nomOcupacao"), "pessoaFisica.ideOcupacao.nomOcupacao")
						
						.add(Projections.property("pj.nomRazaoSocial"), "pessoaJuridica.nomRazaoSocial")
						.add(Projections.property("pj.numCnpj"), "pessoaJuridica.numCnpj")

						.add(Projections.property("endereco.ideEndereco"), "endereco.ideEndereco")
						.add(Projections.property("endereco.numEndereco"), "endereco.numEndereco")
						.add(Projections.property("endereco.desComplemento"), "endereco.desComplemento")

						.add(Projections.property("logradouro.nomLogradouro"), "endereco.ideLogradouro.nomLogradouro")
						.add(Projections.property("logradouro.numCep"), "endereco.ideLogradouro.numCep")

						.add(Projections.property("tipoLog.ideTipoLogradouro"),
								"endereco.ideLogradouro.ideTipoLogradouro.ideTipoLogradouro")
						.add(Projections.property("tipoLog.nomTipoLogradouro"),
								"endereco.ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
						.add(Projections.property("tipoLog.sglTipoLogradouro"),
								"endereco.ideLogradouro.ideTipoLogradouro.sglTipoLogradouro")

						.add(Projections.property("bairro.ideBairro"), "endereco.ideLogradouro.ideBairro.ideBairro")
						.add(Projections.property("bairro.nomBairro"), "endereco.ideLogradouro.ideBairro.nomBairro")

						.add(Projections.property("municipio.ideMunicipio"),
								"endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
						.add(Projections.property("municipio.nomMunicipio"),
								"endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")

						.add(Projections.property("estado.ideEstado"),
								"endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
						.add(Projections.property("estado.desSigla"),
								"endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.or(
				Restrictions.eq("tpr.ideTipoPessoaRequerimento", tipoPessoaRequerimento.getId()),
				Restrictions.isNull("tpr.ideTipoPessoaRequerimento")));
		criteria.add(Restrictions.or(
				Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.RESIDENCIAL.getId()),
				Restrictions.isNull("tipoEndereco.ideTipoEndereco")));
		
		criteria.add(Subqueries.propertyIn("enderecoPessoa.ideEndereco", subCriteria));
		

		return criteria;
	}

	public Pessoa carregarUsuarioSEIA() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class).createAlias("pessoaFisica", "pf");

		criteria.setProjection(Projections.projectionList().add(Projections.property("idePessoa"), "idePessoa"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));

		criteria.add(Restrictions.eq("pf.nomPessoa", "SEIA"));

		return pessoaDAO.buscarPorCriteria(criteria);
	}

	public Pessoa buscarPessoaByDocumento(String documento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaFisica", "pf",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaJuridica", "pj",JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoa"), "idePessoa")
				.add(Projections.property("pf.idePessoaFisica"), "pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.numCpf"), "pessoaFisica.numCpf")
				.add(Projections.property("pf.nomPessoa"), "pessoaFisica.nomPessoa")
				.add(Projections.property("pj.nomRazaoSocial"), "pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"), "pessoaJuridica.numCnpj"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));

		criteria.add(Restrictions.or(
				Restrictions.like("pf.numCpf", documento,MatchMode.EXACT), 
				Restrictions.like("pj.numCnpj", documento,MatchMode.EXACT)));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		return pessoaDAO.buscarPorCriteria(criteria);
	}
	
	public List<Pessoa> listarPessoasPorDemanda (String nome, String cpf, int startPage, int maxPage) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaJuridica", "pj",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaFisica", "pf",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.ilike("pf.nomPessoa", nome, MatchMode.ANYWHERE));
		criteria.add(Restrictions.ilike("pf.numCpf", cpf, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("indExcluido", true));
		criteria.addOrder(Order.asc("pf.nomPessoa"));
		
		return pessoaDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}
	
	
	public List<Pessoa> listarPessoasJuridicaPorDemanda (String razao, String cnpj, int startPage, int maxPage) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaJuridica", "pj",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaFisica", "pf",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.ilike("pj.nomRazaoSocial", razao, MatchMode.ANYWHERE));
		criteria.add(Restrictions.ilike("pj.numCnpj", cnpj, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("indExcluido", true));
		
		return pessoaDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}
	
	public Integer getRowsCountPessoaFisica(String nome, String cpf) {
		
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaJuridica", "pj",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaFisica", "pf",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.ilike("pf.nomPessoa", nome, MatchMode.ANYWHERE));
		criteria.add(Restrictions.ilike("pf.numCpf", cpf, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("indExcluido", true)); 
	    return pessoaDAO.count(criteria);

	}
	
	public Integer getRowsCountPessoaJuridica (String razao, String cnpj) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaJuridica", "pj",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaFisica", "pf",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.ilike("pj.nomRazaoSocial", razao, MatchMode.ANYWHERE));
		criteria.add(Restrictions.ilike("pj.numCnpj", cnpj, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("indExcluido", true));
		
	    return pessoaDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarResponsaveisByCaepog(Caepog caepog)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Caepog.class)
			.createAlias("ideEmpreendimento", "ideEmpreendimento")
			.createAlias("ideEmpreendimento.responsavelEmpreendimentoCollection", "ideResponsavelEmpreendimento")
			.createAlias("ideResponsavelEmpreendimento.idePessoaFisica", "idePessoaFisica")
			.createAlias("idePessoaFisica.pessoa", "idePessoa")
			
			.setProjection(
				Projections.projectionList()
				.add(Property.forName("idePessoa.desEmail"), "desEmail"))
				
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class))	
			.add(Restrictions.eq("ideCaepog", caepog.getIdeCaepog()));
		
		
		return pessoaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscaProprietarioPorImovelRural(Integer ideImovelRural)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class, "pessoa")
			.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa.imovelRuralCollection", "imovelRural", JoinType.INNER_JOIN)
			.createAlias("imovelRural.reservaLegal", "reservaLegal", JoinType.LEFT_OUTER_JOIN)
	
			.add(Restrictions.eq("imovelRural.ideImovelRural", ideImovelRural))
			.add(Restrictions.isNotNull("reservaLegal.ideDocumentoAprovacao"));		
		return pessoaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarResponsaveisBy(CadastroAtividadeNaoSujeitaLic cadastro)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLic.class)
				.createAlias("ideEmpreendimento", "ideEmpreendimento")
				.createAlias("ideEmpreendimento.responsavelEmpreendimentoCollection", "ideResponsavelEmpreendimento")
				.createAlias("ideResponsavelEmpreendimento.idePessoaFisica", "idePessoaFisica")
				.createAlias("idePessoaFisica.pessoa", "idePessoa")
				
				.setProjection(
					Projections.projectionList()
					.add(Property.forName("idePessoa.desEmail"), "desEmail"))
					
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class))	
				.add(Restrictions.eq("ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
			
		return pessoaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscarPessoaPorId(Integer idePessoa)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class);
		criteria
			.createAlias("pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				
			.add(Restrictions.eq("idePessoa", idePessoa))
			
			.setProjection(Projections.projectionList()
					.add(Projections.property("idePessoa"),"idePessoa")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("desEmail"),"desEmail")
					.add(Projections.property("dtcExclusao"),"dtcExclusao")
					.add(Projections.property("pf.idePessoaFisica"),"pessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"),"pessoaFisica.nomPessoa")
					.add(Projections.property("pf.dtcNascimento"),"pessoaFisica.dtcNascimento")
					.add(Projections.property("pf.desNaturalidade"),"pessoaFisica.desNaturalidade")
					.add(Projections.property("pf.nomPai"),"pessoaFisica.nomPai")
					.add(Projections.property("pf.nomMae"),"pessoaFisica.nomMae")
					.add(Projections.property("pf.numCpf"),"pessoaFisica.numCpf")
					.add(Projections.property("pj.idePessoaJuridica"),"pessoaJuridica.idePessoaJuridica")
					.add(Projections.property("pj.nomRazaoSocial"),"pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("pj.nomeFantasia"),"pessoaJuridica.nomeFantasia")
					.add(Projections.property("pj.dtcAbertura"),"pessoaJuridica.dtcAbertura")
					.add(Projections.property("pj.numCnpj"),"pessoaJuridica.numCnpj")
					.add(Projections.property("pj.numInscricaoMunicipal"),"pessoaJuridica.numInscricaoMunicipal")
					.add(Projections.property("pj.numInscricaoEstadual"),"pessoaJuridica.numInscricaoEstadual")
					.add(Projections.property("pj.dscCaminhoArquivo"),"pessoaJuridica.dscCaminhoArquivo")
			)
				
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class))
				
		;
		return pessoaDAO.buscarPorCriteria(criteria);
	}

	
}