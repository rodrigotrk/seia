package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@SuppressWarnings("unchecked")
public class EmpreendimentoDAOImpl extends AbstractDAO<Empreendimento>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<Empreendimento> getDAO() {
		return empreendimentoDAO;
	}

	@Inject
	private IDAO<Empreendimento> empreendimentoDAO;
	
	@Inject
	private IDAO<VwConsultaEmpreendimento> vwempreendimentoDAO;

	@Inject
	private IDAO<Tipologia> tipologiaDAO;

	
	public List<Tipologia> getTipologias(Tipologia pTipologia) {
		StringBuilder lEjbql = new StringBuilder("select tipologia from Tipologia tipologia, NivelTipologia nivelTipologia where tipologia.ideNivelTipologia = nivelTipologia.ideNivelTipologia ");

		if (!Util.isNull(pTipologia)) {

			lEjbql.append(" AND tipologia.indExcluido = :indExcluido ");
		}

		lEjbql.append(" order by nivelTipologia.numNivelTipologia ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();

		lEntityManager.joinTransaction();

		Query lQuery = lEntityManager.createQuery(lEjbql.toString());

		if (!Util.isNull(pTipologia)) {

			lQuery.setParameter("indExcluido", pTipologia.getIndExcluido());
		}

		return lQuery.getResultList();
	}
	
	public Boolean isEmpreendimentoProcessoFormado(int ideEmpreendimento) {
		
		StringBuilder lEjbql = new StringBuilder();
		
		lEjbql.append(" SELECT ct.ide_status_fluxo FROM controle_tramitacao ct");
		lEjbql.append(" INNER JOIN processo p ON p.ide_processo = ct.ide_processo");
		lEjbql.append(" INNER JOIN empreendimento_requerimento er ON er.ide_requerimento= p.ide_requerimento ");
		lEjbql.append(" WHERE er.ide_empreendimento = " + ideEmpreendimento);
		lEjbql.append(" ORDER BY ct.dtc_tramitacao DESC LIMIT 1");
		
			
		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createNativeQuery(lEjbql.toString());
		if(lQuery.getResultList().size()==0) {
			return false;
		}
		Integer statusTramitacao = (Integer) lQuery.getSingleResult();
		
		
		return !(statusTramitacao == StatusFluxoEnum.CONCLUIDO.getStatus() || statusTramitacao == StatusFluxoEnum.CANCELADO.getStatus() || statusTramitacao == StatusFluxoEnum.ARQUIVADO.getStatus());
	}

	public List<Tipologia> getTipologiasByCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);
		criteria.createAlias("ideNivelTipologia", "nivelTipologia", JoinType.INNER_JOIN);
		criteria.createAlias("tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN);
		
		criteria.addOrder(Order.asc("nivelTipologia.numNivelTipologia"));
		criteria.addOrder(Order.asc("ideTipologiaPai"));
		criteria.addOrder(Order.asc("codTipologia"));
		
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		criteria.add(Restrictions.eq("indAutorizacao", Boolean.FALSE));
		criteria.add(Restrictions.or(Restrictions.eq("tipologiaGrupo.indExcluido", Boolean.FALSE), Restrictions.isNull("tipologiaGrupo.indExcluido")));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		Collection<Tipologia> lista = tipologiaDAO.listarPorCriteria(criteria, Order.asc("codTipologia"));
		return (List<Tipologia>) lista;
	}

	/**
	 * @author micael.coutinho
	 * Lista os Empreendimentos Associados ao Imovel e Ordenado Por Data de Criação onde o imovel é igual ao do parametro.
	 * @param imovel
	 * @return List<Empreendimento>
	 * @throws Exception
	 */
	public List<Empreendimento> listaEmpreendimentoAssociadoAoImovelOrdenadoPorDataCriacao(Imovel imovel) {
		//Está desse jeito pq a tabela não está mapeada.

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT e.ide_empreendimento, e.ide_localizacao_geografica, e.ide_pessoa, e.nom_empreendimento," +
				" e.val_investimento, e.dtc_criacao, e.ind_excluido, e.qtd_funcionarios," +
				" e.des_email, e.num_cadastro_tecnico_federal, e.dtc_validade_ctf, e.ind_correspondencia," +
				" e.dtc_exclusao, e.ind_endereco_correspondencia, e.ind_cessionario,ind_conversao_tcra_lac FROM empreendimento e"+
				" inner join imovel_empreendimento ie on ie.ide_empreendimento = e.ide_empreendimento and ie.ide_imovel = "+imovel.getIdeImovel()+
				" order by ie.dtc_associacao asc, ie.ide_empreendimento" +
				" limit 1;");

		return empreendimentoDAO.listarClasseComNativeQuery(sb.toString(), Empreendimento.class);
	}

	public Empreendimento buscarEmpreendimentoComImovelPessoaELocalizacaoCarregadas(Empreendimento pEmpreendimento){
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class, "emp")
				.createAlias("idePessoa", "pessoa", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("loc.ideSistemaCoordenada", "sisCoo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("loc.ideClassificacaoSecao", "claSec", JoinType.LEFT_OUTER_JOIN);
		
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("ideEmpreendimento"), "ideEmpreendimento")
				.add(Projections.property("nomEmpreendimento"), "nomEmpreendimento")
				.add(Projections.property("valInvestimento"), "valInvestimento")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("qtdFuncionarios"), "qtdFuncionarios")
				.add(Projections.property("desEmail"), "desEmail")
				.add(Projections.property("numCadastroTecnicoFederal"), "numCadastroTecnicoFederal")
				.add(Projections.property("dtcValidadeCtf"), "dtcValidadeCtf")
				.add(Projections.property("indCorrespondencia"), "indCorrespondencia")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				.add(Projections.property("indEnderecoCorrespondencia"), "indEnderecoCorrespondencia")
				.add(Projections.property("indCessionario"), "indCessionario")
				.add(Projections.property("indUnidadeConservacao"), "indUnidadeConservacao")
				.add(Projections.property("indConversaoTcraLac"), "indConversaoTcraLac")
				.add(Projections.property("indBaseOperacional"), "indBaseOperacional")
				
				.add(Projections.property("pessoa.idePessoa"),"idePessoa.idePessoa")

				.add(Projections.property("pf.idePessoaFisica"),"idePessoa.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoa.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.numCpf"),"idePessoa.pessoaFisica.numCpf")

				.add(Projections.property("pj.idePessoaJuridica"),"idePessoa.pessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pj.nomRazaoSocial"),"idePessoa.pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"),"idePessoa.pessoaJuridica.numCnpj")
				
				.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("loc.dtcCriacao"), "ideLocalizacaoGeografica.dtcCriacao")
				.add(Projections.property("loc.indExcluido"), "ideLocalizacaoGeografica.indExcluido")
				.add(Projections.property("loc.dtcExclusao"), "ideLocalizacaoGeografica.dtcExclusao")
				.add(Projections.property("loc.fonteCoordenada"), "ideLocalizacaoGeografica.fonteCoordenada")
				.add(Projections.property("loc.desLocalizacaoGeografica"), "ideLocalizacaoGeografica.desLocalizacaoGeografica")
				
				.add(Projections.property("sisCoo.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("sisCoo.nomSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
				.add(Projections.property("sisCoo.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid")
				
				.add(Projections.property("claSec.ideClassificacaoSecao"), "claSec.ideClassificacaoSecao")
				.add(Projections.property("claSec.nomClassificacaoSecao"), "claSec.nomClassificacaoSecao");
		
		criteria.setProjection(projecao).setResultTransformer(new AliasToNestedBeanResultTransformer(Empreendimento.class));
		
		if (!Util.isNullOuVazio(pEmpreendimento.getIdeEmpreendimento())) {
			criteria.add(Restrictions.eq("emp.ideEmpreendimento", pEmpreendimento.getIdeEmpreendimento()));
		}
		
		return empreendimentoDAO.buscarPorCriteria(criteria);
	}

	public Boolean validarNomePessoa(Pessoa pessoa){
		Boolean result = Boolean.TRUE;
		if(Util.isNull(pessoa) || (!Util.isNull(pessoa.getPessoaFisica()) && pessoa.getPessoaFisica().getNomPessoa().trim().isEmpty())){
			result = Boolean.FALSE;
		}
		return result;
	}

	public Boolean validarMunicipio(Municipio municipio){
		Boolean result = Boolean.TRUE;
		if(Util.isNull(municipio)){
			result = Boolean.FALSE;
		}
		return result;
	}

	public List<VwConsultaEmpreendimento> listarPorCriteriaDemanda(Municipio municipio, Pessoa requerente, String nomeEmpreedimento, List<Integer> listaIdesAptos, Integer startPage, Integer maxPage) {
		DetachedCriteria criteria = getCriteriaConsultaProcuradorRepLegalEmpreendimentos(municipio, requerente, nomeEmpreedimento, listaIdesAptos);
		
		criteria.addOrder(Order.asc("nomEmpreendimento"));
		
		return vwempreendimentoDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
		
	}

	private DetachedCriteria getCriteriaConsultaProcuradorRepLegalEmpreendimentos(Municipio municipio, Pessoa requerente, String nomeEmpreedimento, List<Integer> listaIdesAptos) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwConsultaEmpreendimento.class);
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideEmpreendimento"), "ideEmpreendimento")
				.add(Projections.groupProperty("nomEmpreendimento"), "nomEmpreendimento")
				.add(Projections.groupProperty("idePessoa"), "idePessoa")
				.add(Projections.groupProperty("nomPessoa"), "nomPessoa")
				.add(Projections.groupProperty("nomRazaoSocial"), "nomRazaoSocial")
				.add(Projections.groupProperty("ideMunicipio"), "ideMunicipio")
				.add(Projections.groupProperty("nomMunicipio"), "nomMunicipio"));

		if(validarMunicipio(municipio)){
			criteria.add(Restrictions.eq("ideMunicipio", municipio.getIdeMunicipio()));
		}

		/** REFAZER ESTA CONSULTA*/
		if(!ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			if(validarNomePessoa(requerente)){
				if (!Util.isNull(requerente.getPessoaFisica())) {
					
					if(requerente.getPessoaFisica().getIdePessoaFisica() == null) {
						criteria.add(Restrictions.ilike("nomPessoa",requerente.getPessoaFisica().getNomPessoa().trim().toLowerCase(), MatchMode.ANYWHERE));
					}
					else {
						criteria.add(Restrictions.eq("idePessoa",requerente.getPessoaFisica().getIdePessoaFisica()));
					}
				} 
				else {
					if(requerente.getPessoaJuridica().getIdePessoaJuridica() == null) {
						criteria.add(Restrictions.ilike("nomRazaoSocial",requerente.getPessoaJuridica().getNomRazaoSocial().trim().toLowerCase(), MatchMode.ANYWHERE));
					}
					else {
						criteria.add(Restrictions.eq("idePessoa",requerente.getPessoaJuridica().getIdePessoaJuridica()));
					}
				}
			}
		}

		if(!nomeEmpreedimento.isEmpty()){
			criteria.add(Restrictions.ilike("nomEmpreendimento",nomeEmpreedimento.trim(), MatchMode.ANYWHERE));
		}

		if(!listaIdesAptos.isEmpty()){
			criteria.add(Restrictions.in("idePessoa", listaIdesAptos));
		}
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(VwConsultaEmpreendimento.class));
		
		return criteria;
	}

	public Integer count(Municipio municipio,Pessoa requerente,String nomeEmpreedimento, List<Integer> listaIdesAptos) {
		DetachedCriteria  criteria = getCriteriaConsultaProcuradorRepLegalEmpreendimentos(municipio, requerente, nomeEmpreedimento, listaIdesAptos);

		criteria.setProjection(Projections.count("ideEmpreendimento"));

		return vwempreendimentoDAO.count(criteria);
	}

	public void excluirEmpreendimento(Empreendimento pEmpreendimento) {

		pEmpreendimento.setIndExcluido(Boolean.TRUE);
		pEmpreendimento.setDtcExclusao(new Date());
		empreendimentoDAO.atualizar(pEmpreendimento);
	}
	
	public Empreendimento buscarEmpreendimentoPorRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class);
		
		criteria
			.createAlias("empreendimentoRequerimentoCollection", "er", JoinType.INNER_JOIN)
			.createAlias("er.ideRequerimento","r", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEmpreendimento"), "ideEmpreendimento")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Empreendimento.class))
		;
		
		return empreendimentoDAO.buscarPorCriteria(criteria);
		
	}

	public Empreendimento carregarByIdeRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class)
				.createAlias("idePessoa", "pessoa")
				.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("empreendimentoRequerimentoCollection","empreendimentoRequerimento")
				.createAlias("empreendimentoRequerimento.ideRequerimento","requerimento")
				
				
				//.createAlias("requerimentoCollection","requerimento")
				.createAlias("enderecoEmpreendimentoCollection","enderecoEmpreendimento")
				.createAlias("enderecoEmpreendimento.ideEndereco","endereco")
				.createAlias("endereco.ideLogradouro","logradouro")
				.createAlias("logradouro.ideBairro","bairro")
				.createAlias("logradouro.ideMunicipio","municipio")
				.createAlias("municipio.ideEstado","estado");


		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("ideEmpreendimento"),"ideEmpreendimento")
				.add(Projections.property("nomEmpreendimento"),"nomEmpreendimento")

				.add(Projections.property("pessoa.idePessoa"),"idePessoa.idePessoa")

				.add(Projections.property("pf.idePessoaFisica"),"idePessoa.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoa.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.numCpf"),"idePessoa.pessoaFisica.numCpf")

				.add(Projections.property("pj.idePessoaJuridica"),"idePessoa.pessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pj.nomRazaoSocial"),"idePessoa.pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"),"idePessoa.pessoaJuridica.numCnpj")

				.add(Projections.property("endereco.ideEndereco"),"endereco.ideEndereco")
				.add(Projections.property("endereco.numEndereco"),"endereco.numEndereco")
				.add(Projections.property("endereco.desComplemento"),"endereco.desComplemento")

				.add(Projections.property("logradouro.ideLogradouro"),"endereco.ideLogradouro.ideLogradouro")
				.add(Projections.property("logradouro.nomLogradouro"),"endereco.ideLogradouro.nomLogradouro")
				.add(Projections.property("logradouro.numCep"),"endereco.ideLogradouro.numCep")

				.add(Projections.property("bairro.ideBairro"),"endereco.ideLogradouro.ideBairro.ideBairro")
				.add(Projections.property("bairro.nomBairro"),"endereco.ideLogradouro.ideBairro.nomBairro")

				.add(Projections.property("municipio.ideMunicipio"),"endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
				.add(Projections.property("municipio.nomMunicipio"),"endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
				.add(Projections.property("municipio.coordGeobahiaMunicipio"),"endereco.ideLogradouro.ideBairro.ideMunicipio.coordGeobahiaMunicipio")

				.add(Projections.property("estado.ideEstado"),"endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.property("estado.desSigla"),"endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla");


		criteria
		.setProjection(projecao)
		.setProjection(Projections.distinct(projecao))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Empreendimento.class));

		criteria.add(Restrictions.eq("enderecoEmpreendimento.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));

		return this.empreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	public Collection<Empreendimento> listarEmpreendimentoBy(Pessoa pessoa, String nomEmpreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class)
				.add(Restrictions.eq("idePessoa.idePessoa", pessoa.getIdePessoa()))
				.add(Restrictions.eq("indExcluido", false));
				if(!Util.isNullOuVazio(nomEmpreendimento)){
					criteria.add(Restrictions.ilike("nomEmpreendimento", nomEmpreendimento, MatchMode.ANYWHERE));
				}
		return empreendimentoDAO.listarPorCriteria(criteria, Order.asc("nomEmpreendimento"));
	}
	
	public Empreendimento buscarEmpreendimentoPor(Processo processo, Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("r.empreendimentoRequerimentoCollection", "er", JoinType.INNER_JOIN)
			.createAlias("er.ideEmpreendimento", "e", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()))
			.add(Restrictions.eq("e.ideEmpreendimento", empreendimento.getIdeEmpreendimento()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("e.ideEmpreendimento"),"ideEmpreendimento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer( Empreendimento.class))
		;
		
		return empreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	public Empreendimento buscarEmpreendimentoPorId(Integer ideEmpreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class)
			.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			.createAlias("lg.ideClassificacaoSecao", "cs", JoinType.INNER_JOIN)
			.createAlias("enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
			.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
			.createAlias("ee.ideEndereco", "e", JoinType.INNER_JOIN)
			.createAlias("e.ideLogradouro", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideTipoLogradouro", "tl", JoinType.INNER_JOIN)
			.createAlias("l.ideBairro", "b", JoinType.INNER_JOIN)
			.createAlias("l.ideMunicipio", "m", JoinType.INNER_JOIN)
			.createAlias("m.ideEstado", "uf", JoinType.INNER_JOIN)
			.createAlias("uf.idePais", "p", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ideEmpreendimento", ideEmpreendimento))
			.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideEmpreendimento"),"ideEmpreendimento")
				.add(Projections.groupProperty("nomEmpreendimento"),"nomEmpreendimento")
				.add(Projections.groupProperty("valInvestimento"),"valInvestimento")
				.add(Projections.groupProperty("dtcCriacao"),"dtcCriacao")
				.add(Projections.groupProperty("indExcluido"),"indExcluido")
				.add(Projections.groupProperty("qtdFuncionarios"),"qtdFuncionarios")
				.add(Projections.groupProperty("desEmail"),"desEmail")
				.add(Projections.groupProperty("numCadastroTecnicoFederal"),"numCadastroTecnicoFederal")
				.add(Projections.groupProperty("dtcValidadeCtf"),"dtcValidadeCtf")
				.add(Projections.groupProperty("indCorrespondencia"),"indCorrespondencia")
				.add(Projections.groupProperty("dtcExclusao"),"dtcExclusao")
				.add(Projections.groupProperty("indEnderecoCorrespondencia"),"indEnderecoCorrespondencia")
				.add(Projections.groupProperty("indCessionario"),"indCessionario")
				.add(Projections.groupProperty("indUnidadeConservacao"),"indUnidadeConservacao")
				.add(Projections.groupProperty("indConversaoTcraLac"),"indConversaoTcraLac")
				.add(Projections.groupProperty("indBaseOperacional"),"indBaseOperacional")
				
				.add(Projections.groupProperty("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.sqlGroupProjection("(select ST_Y(ST_centroid(the_geom)) from dado_geografico where ide_localizacao_geografica=lg1_.ide_localizacao_geografica) as pontoLatitude_", "pontoLatitude_", new String[] {"pontoLatitude_"},	new Type[] {StandardBasicTypes.STRING}),"ideLocalizacaoGeografica.pontoLatitude")
				.add(Projections.sqlGroupProjection("(select ST_X(ST_centroid(the_geom)) from dado_geografico where ide_localizacao_geografica=lg1_.ide_localizacao_geografica) as pontoLongitude_", "pontoLongitude_", new String[] {"pontoLongitude_"}, new Type[] {StandardBasicTypes.STRING}),"ideLocalizacaoGeografica.pontoLongitude")
				.add(Projections.groupProperty("sc.ideSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.groupProperty("cs.ideClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")

				.add(Projections.groupProperty("e.ideEndereco"),"endereco.ideEndereco")
				.add(Projections.groupProperty("e.numEndereco"),"endereco.numEndereco")
				.add(Projections.groupProperty("e.indExcluido"),"endereco.indExcluido")
				.add(Projections.groupProperty("e.dtcCriacao"),"endereco.dtcCriacao")
				.add(Projections.groupProperty("e.dtcExclusao"),"endereco.dtcExclusao")
				.add(Projections.groupProperty("e.desComplemento"),"endereco.desComplemento")
				.add(Projections.groupProperty("e.desPontoReferencia"),"endereco.desPontoReferencia")
				.add(Projections.groupProperty("l.ideLogradouro"),"endereco.ideLogradouro.ideLogradouro")
				.add(Projections.groupProperty("l.nomLogradouro"),"endereco.ideLogradouro.nomLogradouro")
				.add(Projections.groupProperty("l.numCep"),"endereco.ideLogradouro.numCep")
				.add(Projections.groupProperty("l.indOrigemCorreio"),"endereco.ideLogradouro.indOrigemCorreio")
				.add(Projections.groupProperty("l.indOrigemApi"),"endereco.ideLogradouro.indOrigemApi")
				.add(Projections.groupProperty("tl.ideTipoLogradouro"),"endereco.ideLogradouro.ideTipoLogradouro.ideTipoLogradouro")
				.add(Projections.groupProperty("tl.sglTipoLogradouro"),"endereco.ideLogradouro.ideTipoLogradouro.sglTipoLogradouro")
				.add(Projections.groupProperty("tl.nomTipoLogradouro"),"endereco.ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
				.add(Projections.groupProperty("b.ideBairro"),"endereco.ideLogradouro.ideBairro.ideBairro")
				.add(Projections.groupProperty("b.nomBairro"),"endereco.ideLogradouro.ideBairro.nomBairro")
				.add(Projections.groupProperty("m.ideMunicipio"),"endereco.ideLogradouro.ideMunicipio.ideMunicipio")
				.add(Projections.groupProperty("m.nomMunicipio"),"endereco.ideLogradouro.ideMunicipio.nomMunicipio")
				.add(Projections.groupProperty("m.numCep"),"endereco.ideLogradouro.ideMunicipio.numCep")
				.add(Projections.groupProperty("uf.ideEstado"),"endereco.ideLogradouro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.groupProperty("uf.nomEstado"),"endereco.ideLogradouro.ideMunicipio.ideEstado.nomEstado")
				.add(Projections.groupProperty("uf.desSigla"),"endereco.ideLogradouro.ideMunicipio.ideEstado.desSigla")
				.add(Projections.groupProperty("p.idePais"),"endereco.ideLogradouro.ideMunicipio.ideEstado.idePais.idePais")
				.add(Projections.groupProperty("p.sglPais"),"endereco.ideLogradouro.ideMunicipio.ideEstado.idePais.sglPais")
				.add(Projections.groupProperty("p.nomPais"),"endereco.ideLogradouro.ideMunicipio.ideEstado.idePais.nomPais")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Empreendimento.class))
		;
		
		return empreendimentoDAO.buscarPorCriteria(criteria);
	}

	

}