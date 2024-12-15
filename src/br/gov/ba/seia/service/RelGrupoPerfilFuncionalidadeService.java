package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.RelGrupoPerfilFuncionalidadeDAOImpl;
import br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidade;
import br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidadePK;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelGrupoPerfilFuncionalidadeService {

	@Inject
	IDAO<RelGrupoPerfilFuncionalidade> daoRelGrupoPerfilFuncionalidade;	

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RelGrupoPerfilFuncionalidade filtrarPermissaoById(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

		return daoRelGrupoPerfilFuncionalidade.buscarEntidadePorExemplo(pRelGrupoPerfilFuncionalidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RelGrupoPerfilFuncionalidade filtrarPermissaoByIdCriteria(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RelGrupoPerfilFuncionalidade.class);
		criteria.createAlias("acao", "acao");
		criteria.createAlias("funcionalidade", "funcionalidade");
		criteria.createAlias("perfil", "perfil");
		criteria.add(Restrictions.eq("acao.ideAcao",pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao()));
		criteria.add(Restrictions.eq("funcionalidade.ideFuncionalidade", pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade()));
		criteria.add(Restrictions.eq("perfil.idePerfil", pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil()));
		return daoRelGrupoPerfilFuncionalidade.buscarPorCriteria(criteria);
	}

	public Collection<RelGrupoPerfilFuncionalidade> filtrarListaPermissoes(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

		return new RelGrupoPerfilFuncionalidadeDAOImpl().getPermissoes(pRelGrupoPerfilFuncionalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPermissao(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

		pRelGrupoPerfilFuncionalidade.setRelGrupoPerfilFuncionalidadePK(new RelGrupoPerfilFuncionalidadePK());
		pRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdeAcao(pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao());
		pRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdeFuncionalidade(pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade());
		pRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdePerfil(pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil());
		
		RelGrupoPerfilFuncionalidade relGrupoPerfilFuncionalidadeExistente = filtrarPermissaoByIdCriteria(pRelGrupoPerfilFuncionalidade);
		
		if (!Util.isNull(relGrupoPerfilFuncionalidadeExistente)) {
			throw new AppExceptionError("Não foi possível realizar a operação, pois já existe essa permissão para o perfil de usuário escolhido.");
		} else {
			daoRelGrupoPerfilFuncionalidade.salvarOuAtualizar(pRelGrupoPerfilFuncionalidade);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPermissoes(Collection<RelGrupoPerfilFuncionalidade> pColRelGrupoPerfilFuncionalidade) {

		for (RelGrupoPerfilFuncionalidade lRelGrupoPerfilFuncionalidade : pColRelGrupoPerfilFuncionalidade) {

			lRelGrupoPerfilFuncionalidade.setRelGrupoPerfilFuncionalidadePK(new RelGrupoPerfilFuncionalidadePK());
			lRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdeAcao(lRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao());
			lRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdeFuncionalidade(lRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade());
			lRelGrupoPerfilFuncionalidade.getRelGrupoPerfilFuncionalidadePK().setIdePerfil(lRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil());

			daoRelGrupoPerfilFuncionalidade.salvar(lRelGrupoPerfilFuncionalidade);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPermissao(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

		daoRelGrupoPerfilFuncionalidade.atualizar(pRelGrupoPerfilFuncionalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPermissao(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("idePerfil", pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil());
		lParametros.put("ideFuncionalidade", pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade());
		lParametros.put("ideAcao", pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao());

		daoRelGrupoPerfilFuncionalidade.executarNamedQuery("RelGrupoPerfilFuncionalidade.removerRelGrupoPerfilFuncionalidade", lParametros);
	}
}