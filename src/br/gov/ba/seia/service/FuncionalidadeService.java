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

import org.hibernate.criterion.DetachedCriteria;

import br.gov.ba.seia.dao.FuncionalidadeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.Secao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionalidadeService {

	@Inject
	IDAO<Funcionalidade> daoFuncionalidade;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Funcionalidade> obterTodos() {
		return daoFuncionalidade.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionalidade filtrarFuncionalidadeById(Funcionalidade pFuncionalidade) {

		return daoFuncionalidade.buscarEntidadePorExemplo(pFuncionalidade);
	}

	public Collection<Funcionalidade> filtrarListaFuncionalidades(Funcionalidade pFuncionalidade) {

		return new FuncionalidadeDAOImpl().getFuncionalidades(pFuncionalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFuncionalidade(Funcionalidade pFuncionalidade)  {

		pFuncionalidade.setDtcCriacao(new Date());

		daoFuncionalidade.salvar(pFuncionalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarFuncionalidade(Funcionalidade pFuncionalidade)  {

		daoFuncionalidade.atualizar(pFuncionalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFuncionalidade(Funcionalidade pFuncionalidade)  {

		pFuncionalidade.setIndExcluido(true);
		pFuncionalidade.setDtcExclusao(new Date());

		daoFuncionalidade.atualizar(pFuncionalidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionalidade> filtrarFuncionalidadePorSecao(Secao secao) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideSecao", secao.getIdeSecao());
		return daoFuncionalidade.buscarPorNamedQuery("Funcionalidade.findBySecao", parametros);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionalidade> listarFuncionalidades() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Funcionalidade.class);
		return daoFuncionalidade.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionalidade> filtrarFuncionalidadePorSecaoPerfil(Secao secao, Perfil perfil) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idePerfil", perfil.getIdePerfil());
		parametros.put("ideSecao", secao.getIdeSecao());
		
		String sql = "select distinct f " +
				     "from RelGrupoPerfilFuncionalidade g join g.funcionalidade f " +
				     "where g.perfil.idePerfil=:idePerfil " +
				     "and f.ideSecao.ideSecao=:ideSecao";
		
		return daoFuncionalidade.listarPorQuery(sql, parametros);
	}
	
	public void excluirFuncionalidadePorSecaoPerfil(Secao secao, Perfil perfil) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idePerfil", perfil.getIdePerfil());
		parametros.put("ideSecao", secao.getIdeSecao());
		
		String sql = "delete " +
		             "from RelGrupoPerfilFuncionalidade rel " +
				     "where rel.funcionalidade in (select distinct f.ideFuncionalidade " +
				                                 "from RelGrupoPerfilFuncionalidade g join g.funcionalidade f " +
				                                 "where g.perfil.idePerfil=:idePerfil " +
				                                 "and f.ideSecao.ideSecao=:ideSecao) " +
				     "and rel.perfil=:idePerfil";
		
		try{
		   daoFuncionalidade.executarQuery(sql, parametros);
		}
		catch(Exception e){
			e.getMessage();
		}
	}
}