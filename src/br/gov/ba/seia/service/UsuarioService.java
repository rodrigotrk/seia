package br.gov.ba.seia.service;

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

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.UsuarioDAOImpl;
import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.FuncionalidadeControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioService {

	@Inject
	IDAO<Usuario> daoUsuario;
	
	private static UsuarioService instance;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario filtrarUsuarioById(Usuario pUsuario) {
		return daoUsuario.buscarEntidadePorExemplo(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarUsuarioPorPessoaFisica(PessoaFisica idePessoaFisica)  {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("idePessoaFisica", idePessoaFisica.getIdePessoaFisica());

		StringBuilder sql = new StringBuilder();
		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("     inner join fetch u.idePerfil ");
		sql.append("where u.idePessoaFisica = :idePessoaFisica ");

		return daoUsuario.buscarPorQuery(sql.toString(), params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario retornarUsuarioDoGestorQuePermitiuAcessoNaSuaPauta(PessoaFisica pessoaFisica, Area area)  {
		
		if(Util.isNull(area)) {
			return null;			
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idePessoaFisica", pessoaFisica);
		params.put("ideArea", area);
		params.put("ideFuncionalidade",new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PERMISSAO_DE_ACESSO_A_PAUTA.getId()));

		StringBuilder sql = new StringBuilder();

		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("     inner join fetch u.idePerfil perfil ");
		sql.append("where u.idePessoaFisica = (select func.pessoaFisica ");
		sql.append("                           from FuncionalidadeAcaoPessoaFisica fapf ");
		sql.append("                                inner join fapf.funcionalidadeAcaoPessoaFisicaPautaCollection fapfPauta ");
		sql.append("                                inner join fapfPauta.idePauta pauta ");
		sql.append("                                inner join pauta.idePessoaFisica func ");
		sql.append("                           where fapf.idePessoaFisica = :idePessoaFisica ");
		sql.append("                                 and fapf.ideFuncionalidade = :ideFuncionalidade ");
		sql.append("                                 and func.ideArea = :ideArea) ");

		return daoUsuario.buscarPorQuery(sql.toString(), params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario retornarUsuarioSubstitutoDoCoordenador(Integer idePessoaFisica)  {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idePessoaFisica", idePessoaFisica);

		StringBuilder sql = new StringBuilder();

		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("where u.idePessoaFisica = (select fapf.idePessoaFisica ");
		sql.append("                           from FuncionalidadeAcaoPessoaFisica fapf ");
		sql.append("                                inner join fapf.funcionalidadeAcaoPessoaFisicaPautaCollection fapfPauta ");
		sql.append("                                inner join fapfPauta.idePauta pauta ");
		sql.append("                                inner join pauta.idePessoaFisica func ");
		sql.append("                           where func.idePessoaFisica = :idePessoaFisica ");
		sql.append("                                 and fapfPauta.indSubstituto = true )");

		return daoUsuario.buscarPorQuery(sql.toString(), params);
	}

	public Collection<Usuario> filtrarListaUsuarios(Usuario pUsuario) {
		return new UsuarioDAOImpl().getUsuarios(pUsuario);
	}

	public Usuario filtrarUsuario(Usuario pUsuario) {
		return new UsuarioDAOImpl().getUsuario(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuario(Usuario pUsuario)  {
		pUsuario.setDtcCriacao(new Date());
		pUsuario.setDtcUltimaSenha(new Date());
		pUsuario.setDscLogin(pUsuario.getDscLogin().trim());
		daoUsuario.salvar(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarUsuario(Usuario pUsuario)  {
		pUsuario.setDscLogin(pUsuario.getDscLogin().trim());
		daoUsuario.salvarOuAtualizar(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuario(Usuario pUsuario)  {
		daoUsuario.remover(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Usuario ativarUsuario(Usuario pUsuario) throws Exception  {
		try {
			pUsuario = this.filtrarUsuario(pUsuario);
		} catch (Exception e) {
			pUsuario = null;
		}
		if (!Util.isNullOuVazio(pUsuario)) {
			if (pUsuario.getIndValidacao() == true) {
				throw new Exception("O usuário já esta ativo.");
			} else {
				try {
					pUsuario.setIndValidacao(true);
					daoUsuario.atualizar(pUsuario);
				} catch (Exception e) {
					throw new Exception("O usuário não foi ativado.");
				}
			}
		} else {
			throw new Exception("O usuário não existe.");
		}
		return pUsuario;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuarioSemAdmContainerDaEntidadeUsuario(Usuario pUsuario)  {
		daoUsuario.remover(pUsuario);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario carregarPorCriteria(Usuario usuario) {
		Usuario user = daoUsuario.carregarGet(usuario.getIdePessoaFisica());
		Hibernate.initialize(user.getPessoaFisica());
		Hibernate.initialize(user.getPessoaFisica().getIdePais());
		return daoUsuario.carregarGet(usuario.getIdePessoaFisica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario carregar(Usuario usuario)  {

		if(!Util.isNullOuVazio(usuario)){
			DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class);
		    criteria.createAlias("pessoaFisica", "pf");
	    	criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		    criteria.add(Restrictions.eq("idePessoaFisica", usuario.getIdePessoaFisica()));
		    return daoUsuario.buscarPorCriteria(criteria);
		}
		return null;
		/*
		 * Usuario user = daoUsuario.carregarGet(usuario.getIdePessoaFisica()); Hibernate.initialize(user.getPessoaFisica()); Hibernate.initialize(user.getPessoaFisica().getIdePais()); return daoUsuario.carregarGet(usuario.getIdePessoaFisica());
		 */
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Usuario> filtrarPorLogin(String pLogin) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dscLogin", pLogin);
		return daoUsuario.buscarPorNamedQuery("Usuario.findByDscLogin", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario filtrarPorLoginCompleto(String pLogin)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class);
	    criteria.createAlias("pessoaFisica", "pf");
	    criteria.add(Restrictions.eq("dscLogin", pLogin));

	    return daoUsuario.buscarPorCriteria(criteria);
	}

	public Collection<Usuario> listarPorEmailCPF(UsuarioExternoDTO pUsuarioExterno)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class)
				.createAlias("pessoaFisica", "pf").createAlias("pf.pessoa", "pes")
				.add(Restrictions.eq("pes.desEmail", pUsuarioExterno.getPessoaFisica().getPessoa().getDesEmail()))
				.add(Restrictions.eq("pf.numCpf", pUsuarioExterno.getPessoaFisica().getNumCpf()))
				.add(Restrictions.eq("indValidacao", Boolean.FALSE));
		return daoUsuario.listarPorCriteria(criteria, Order.asc("pf.nomPessoa"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarPorLogin(String pLogin) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dscLogin", pLogin);
		return daoUsuario.obterPorNamedQuery("Usuario.findByDscLogin", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Usuario> listarDispositivosEnvolvidosPorIdeRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class, "usuario")
				.createAlias("usuario.pessoaFisica", "pessoaFisica")
				.createAlias("pessoaFisica.pessoa", "pessoa")
				.createAlias("pessoa.requerimentoPessoaCollection", "reqPessoa")
				.createAlias("reqPessoa.requerimento", "requerimento")
				.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento))
				.add((Restrictions.ne("reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.ATENDENTE.getId())));
	    List<Usuario> usuarios = daoUsuario.listarPorCriteria(criteria);
	    for(Usuario usuario : usuarios){
	    	if(!Util.isNullOuVazio(usuario) && !Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())) {
				usuario.getUsuarioDispositivoCollection().iterator().next();
			}
	    }
	    return usuarios;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Usuario> listarDispositivosEnvolvidosPorIdeProcesso(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Usuario.class, "usuario")
				.createAlias("usuario.pessoaFisica", "pessoaFisica")
				.createAlias("pessoaFisica.pessoa", "pessoa")
				.createAlias("pessoa.requerimentoPessoaCollection", "reqPessoa")
				.createAlias("reqPessoa.requerimento", "requerimento")
				.createAlias("requerimento.processoCollection", "processo")
				.add(Restrictions.eq("processo.ideProcesso", ideProcesso))
				.add((Restrictions.ne("reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.ATENDENTE.getId())));
	    List<Usuario> usuarios = daoUsuario.listarPorCriteria(criteria);
	    for(Usuario usuario : usuarios){
	    	if(!Util.isNullOuVazio(usuario) && !Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())) {
				usuario.getUsuarioDispositivoCollection().iterator().next();
			}
	    }
	    return usuarios;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario listarDispositivosResponsavelArea(Integer ideArea) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ideArea", ideArea);
		StringBuilder sql = new StringBuilder();

		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("where u.idePessoaFisica  = (select f.pessoaFisica ");
		sql.append("                           from Area a ");
		sql.append("                                inner join a.idePessoaFisica f ");
		sql.append("                                where a.ideArea = :ideArea) ");

		Usuario usuario = daoUsuario.buscarPorQuery(sql.toString(), params);
	    	if(!Util.isNullOuVazio(usuario) && !Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())) {
			usuario.getUsuarioDispositivoCollection().iterator().next();
			}
	    return usuario;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario listarDispositivosUsuarioPauta(Integer idePauta) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idePauta", idePauta);
		StringBuilder sql = new StringBuilder();

		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("where u.idePessoaFisica  = (select f.pessoaFisica ");
		sql.append("                           from Pauta p ");
		sql.append("                                inner join p.idePessoaFisica f ");
		sql.append("                                where p.idePauta = :idePauta) ");

		Usuario usuario = daoUsuario.buscarPorQuery(sql.toString(), params);
	    	if(!Util.isNullOuVazio(usuario) && !Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())) {
			usuario.getUsuarioDispositivoCollection().iterator().next();
			}
	    return usuario;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario listarDispositivoUsuarioLiderEquipeProcesso(ControleTramitacao tramitacao) {

		Map<String,Object> params = new HashMap<String,Object>();

		params.put("ideProcesso", tramitacao.getIdeProcesso());
		params.put("indLiderEquipe", true);
		params.put("ideArea", tramitacao.getIdeArea());

		StringBuilder sql = new StringBuilder();
		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("where u.idePessoaFisica  = (select f.pessoaFisica ");
		sql.append("                           from EquipeProcesso ep ");
		sql.append("                                inner join ep.idePessoaFisica f ");
		sql.append("                                where ep.ideProcesso = :ideProcesso and ep.indLiderEquipe = :indLiderEquipe and ep.ideArea = :ideArea) ");

		Usuario usuario = daoUsuario.buscarPorQuery(sql.toString(), params);

		if(!Util.isNullOuVazio(usuario) && !Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())) {
			usuario.getUsuarioDispositivoCollection().iterator().next();
		}

	    return usuario;
	}
	
	public boolean verificaUsuarioInativado(int idePessoaFisica)  {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idePessoaFisica", idePessoaFisica);
		StringBuilder sql = new StringBuilder();		
		      
		sql.append("select u ");
		sql.append("from Usuario u ");
		sql.append("where u.idePessoaFisica  = :idePessoaFisica ");
		
		Usuario usuario = daoUsuario.buscarPorQuery(sql.toString(), params);
		
		if(usuario != null && usuario.getIndExcluido()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void apagarRegistroTentativa(Usuario user) {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM usuario_bloqueio WHERE dsc_login= :param1 ");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("param1", user.getDscLogin());
		daoUsuario.executarNativeQuery(sql.toString(), param);
		
	}
	
	public static synchronized UsuarioService getInstance() {
		if (instance == null) {
			instance = new UsuarioService();
		}
		return instance;
	}
}