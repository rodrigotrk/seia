package br.gov.ba.seia.service;

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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.FuncionarioDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.enumerator.AcaoControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.FuncionalidadeControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionarioService {

	@Inject
	IDAO<Funcionario> daoFuncionario;

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private ParametroService parametroService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario buscarFuncionario(Integer idePessoaFisica, AreaEnum areaEnum)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		
		criteria
			.add(Restrictions.eq("ideArea.ideArea", areaEnum.getId()))
			.add(Restrictions.eq("idePessoaFisica", idePessoaFisica))
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.property("ideArea.ideArea"),"ideArea.ideArea")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Funcionario.class))
		;

		return daoFuncionario.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario filtrarFuncionarioById(Funcionario pFuncionario) {
		
		return daoFuncionario.buscarEntidadePorExemplo(pFuncionario);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionario> listarFuncionarioPor(Area area, String nomeFuncionario)  {
		DetachedCriteria criteria = getCriteriaListarPorArea(area);
		criteria.add(Restrictions.ilike("pf.nomPessoa", nomeFuncionario,MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("u.indTipoUsuario", true));
		return daoFuncionario.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionario> listarFuncionarioPorArea(Area area)  {
		DetachedCriteria criteria = getCriteriaListarPorArea(area);		
		return daoFuncionario.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteriaListarPorArea(Area area) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		criteria
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("pessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("pf.usuario","u", JoinType.INNER_JOIN)
			.createAlias("pf.pessoa", "p", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("a.ideArea", area.getIdeArea()))
			.add(Restrictions.eq("p.indExcluido", false))
			.addOrder(Order.asc("pf.nomPessoa"))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"),"pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"pessoaFisica.nomPessoa")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Funcionario.class))
		;
		return criteria;
	}

	public Collection<Funcionario> filtrarListaFuncionarios(Funcionario pFuncionario) {

		return new FuncionarioDAOImpl().getFuncionarios(pFuncionario);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionario> listarPessoaFisicaCoordenadoresDiretores()  {
		List<Perfil> perfisDeGestor = new ArrayList<Perfil>();
		Map<String,Object> params = new HashMap<String, Object>();

		perfisDeGestor.add(new Perfil(PerfilEnum.COORDENADOR.getId()));
		perfisDeGestor.add(new Perfil(PerfilEnum.COORD_CTGA.getId()));
		perfisDeGestor.add(new Perfil(PerfilEnum.DIRETOR.getId()));
		perfisDeGestor.add(new Perfil(PerfilEnum.COORDENADOR_E_TECNICO.getId()));

		params.put("indTipoUsuario", true);
		params.put("ideGestores", perfisDeGestor);

		StringBuilder sql = new StringBuilder();

		sql.append("select func ");
		sql.append("from Funcionario func ");
		sql.append("     inner join fetch func.pessoaFisica pf");
		sql.append("     inner join pf.usuario u ");
		sql.append("where u.indTipoUsuario = :indTipoUsuario ");
		sql.append("      and u.idePerfil in :ideGestores ");
		sql.append("order by pf.nomPessoa asc ");

		return daoFuncionario.listarPorQuery(sql.toString(), params);
	}
	
	
	
	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean validarPossibilidadeConclusaoProcesso(Processo processo, PessoaFisica pessoaLogada) {
		try{
			StringBuilder sql = new StringBuilder()
				.append("select a.idePessoaFisica ")
				.append("from Area a ")
				.append("where a in :lArea ")
				.append("and a.idePessoaFisica.idePessoaFisica = :idePessoaFisica ");
	
			List<Area> lArea = new ArrayList<Area>();
			lArea.add(new Area(AreaEnum.DIRRE.getId()));
			lArea.add(new Area(AreaEnum.SELIC.getId()));
	
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("lArea", lArea);
			parametros.put("idePessoaFisica", pessoaLogada.getIdePessoaFisica());

			Funcionario funcionarioAutorizado = daoFuncionario.buscarPorQuery(sql.toString(), parametros);

			if(funcionarioAutorizado != null){
				return true;
			} else{
				sql = new StringBuilder();

				List<Funcionalidade> listaFuncionalidadePermAcessPauta = new ArrayList<Funcionalidade>();
				listaFuncionalidadePermAcessPauta.add(new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA.getId()));
				listaFuncionalidadePermAcessPauta.add(new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DO_GESTOR.getId()));

				parametros.put("idesFuncionalidade", listaFuncionalidadePermAcessPauta);
				parametros.put("ideAcao", new Acao(AcaoControlePermissaoAcessoPautaEnum.CONCLUIR_PROCESSO.getId()));
				parametros.remove("lArea");

				sql.append("select f ");
				sql.append("from Funcionario f ");
				sql.append("     inner join f.pessoaFisica pf ");
				sql.append("     inner join pf.funcionalidadeAcaoPessoaFisicaCollection fapf ");
				sql.append("     inner join fapf.funcionalidadeAcaoPessoaFisicaPautaCollection fapfPauta ");
				sql.append("     inner join fapfPauta.idePauta pauta ");
				sql.append("where fapf.ideFuncionalidade in :idesFuncionalidade ");
				sql.append("      and fapf.ideAcao = :ideAcao ");
				sql.append("      and fapf.idePessoaFisica.idePessoaFisica = :idePessoaFisica ");

				funcionarioAutorizado = daoFuncionario.buscarPorQuery(sql.toString(), parametros);

				if(funcionarioAutorizado != null){
					return true;
				}
				return false;
			}
		} catch(Exception e){
			return false;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarCoordenadorSelic(PessoaFisica p) {

		StringBuilder sql = new StringBuilder();

		sql.append("select a.idePessoaFisica ");
		sql.append("from Area a ");
		sql.append("where upper(a.nomArea) = :nomArea ");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nomArea", "SELIC");

		try{
			Funcionario funcionario = daoFuncionario.buscarPorQuery(sql.toString(), parametros);

			if(p.getIdePessoaFisica().equals(funcionario.getIdePessoaFisica())){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Funcionario buscarFuncionarioPorPessoaFisica(PessoaFisica p) {

		StringBuilder sql = new StringBuilder();

		sql.append("select f ");
		sql.append("from Funcionario f ");
		sql.append("where f.idePessoaFisica = :idePessoaFisica ");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idePessoaFisica", p.getIdePessoaFisica());

		try{
			Funcionario funcionario = daoFuncionario.buscarPorQuery(sql.toString(), parametros);
			return funcionario;
		}
		catch(Exception e){
			return null;
		}
	}

	public Funcionario FuncionarioBuscarFuncionartioByIdePessoaFisica(int idePessoaFisica){

		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class)
				.createAlias("pessoaFisica", "p")
				.add(Restrictions.eq("p.idePessoaFisica", idePessoaFisica));

		return daoFuncionario.buscarPorCriteria(criteria);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionario> listarFuncionarios(Funcionario pFuncionario) throws Exception  {

		Parametro lParametroPerfilCoordenador = parametroService.obterPorId(ParametroEnum.IDE_PERFIL_COORDENADOR.getIdeParametro());

		if (!Util.isNullOuVazio(lParametroPerfilCoordenador)) {

			pFuncionario.getPessoaFisica().getUsuario().setIdePerfil(new Perfil(Integer.valueOf(lParametroPerfilCoordenador.getDscValor())));
			pFuncionario.getPessoaFisica().getUsuario().setIndTipoUsuario(true);

			return listarFuncionariosPorPerfilTipoUsuario(pFuncionario);
		}
		else {

			throw new Exception("Não foi possível obter o parâmetro do perfil do coordenador.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Funcionario> listarFuncionariosPorPerfilTipoUsuario(Funcionario pFuncionario) throws Exception  {

		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("idePerfil", pFuncionario.getPessoaFisica().getUsuario().getIdePerfil().getIdePerfil());
		lParametros.put("indTipoUsuario", pFuncionario.getPessoaFisica().getUsuario().getIndTipoUsuario());

		Collection<Funcionario> lColFuncionarios = daoFuncionario.buscarPorNamedQuery("Funcionario.findByPerfil", lParametros);

		for (Funcionario lFuncionario : lColFuncionarios) {

			lFuncionario.setPessoaFisica(pessoaFisicaService.carregarPessoaFisicaGet(lFuncionario.getIdePessoaFisica()));
		}

		return lColFuncionarios;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Funcionario> listarFuncionariosPorPerfil(Funcionario pFuncionario)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		criteria.createAlias("pessoaFisica", "pessoaFisica");
		criteria.createAlias("pessoaFisica.usuario", "usuario");
		criteria.createAlias("usuario.idePerfil", "perfil");
		criteria.add(Restrictions.eq("perfil.idePerfil", pFuncionario.getPessoaFisica().getUsuario().getIdePerfil().getIdePerfil()));
		return daoFuncionario.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Funcionario> listarTecnicosAutoComplete(Funcionario pFuncionario)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		criteria.createAlias("pessoaFisica", "pessoaFisica");
		criteria.createAlias("pessoaFisica.usuario", "usuario");
		criteria.createAlias("usuario.idePerfil", "perfil");
		criteria.createAlias("ideArea", "area");
		criteria.add(Restrictions.eq("perfil.idePerfil", 3));
		criteria.add(Restrictions.eq("indExcluido", false));
		if(!Util.isNullOuVazio(pFuncionario.getIdeArea())){
			criteria.add(Restrictions.eq("area.ideArea", pFuncionario.getIdeArea().getIdeArea()));
		}
		criteria.add(Restrictions.ilike("pessoaFisica.nomPessoa", pFuncionario.getPessoaFisica().getNomPessoa(), MatchMode.START));
		criteria.addOrder(Order.asc("pessoaFisica.nomPessoa"));
		return daoFuncionario.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Area buscarAreaFuncionario(Integer ideFuncionario) {
    	StringBuilder lEjbql = new StringBuilder();
    	
    	
    	lEjbql.append(" select ideArea from Funcionario funcionario ");
    	lEjbql.append(" where funcionario.idePessoaFisica = :idePessoaFisica ");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());
    	
    	lQuery.setParameter("idePessoaFisica", ideFuncionario);
    	
    	return (Area) lQuery.getSingleResult();
    	
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFuncionario(Funcionario pFuncionario)  {

		daoFuncionario.salvarOuAtualizar(pFuncionario);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFuncionarioSomente(Funcionario pFuncionario)  {

		daoFuncionario.salvar(pFuncionario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarFuncionario(Funcionario pFuncionario)  {

		daoFuncionario.salvarOuAtualizar(pFuncionario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFuncionario(Funcionario pFuncionario)  {

		daoFuncionario.remover(pFuncionario);
	}

	public void excluirFuncionarioSemAdmContainerDaEntidadeFuncionario(Funcionario pFuncionario)  {

		daoFuncionario.remover(pFuncionario);
	}

	public Funcionario carregaFuncionario(Integer funcionario){
		return daoFuncionario.carregarGet(funcionario);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario obterCoordenadorPorIdeArea(Area area)  {
		if(!Util.isNull(area.getIdePessoaFisica())){
			DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
			criteria.add(Restrictions.eq("idePessoaFisica", area.getIdePessoaFisica().getIdePessoaFisica()));
			return daoFuncionario.buscarPorCriteria(criteria);
		}
		return null;
	}
}