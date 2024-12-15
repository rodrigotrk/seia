package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NivelCompetenciaDAOImpl;
import br.gov.ba.seia.dao.OrgaoDAOImpl;
import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.entity.Orgao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrgaoService {

	@Inject
	IDAO<Orgao> daoOrgao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Orgao> listarTodos() {
		return daoOrgao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Orgao carregar(Orgao pOrgao){

		return daoOrgao.carregarGet(pOrgao.getIdeOrgao());
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Orgao carregar(Integer idOrgao){
		return daoOrgao.carregarGet(idOrgao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Orgao filtrarOrgaoById(Orgao pOrgao) {

		return daoOrgao.buscarEntidadePorExemplo(pOrgao);
	}

	public Collection<Orgao> filtrarListaOrgaos(Orgao pOrgao) {

		return new OrgaoDAOImpl().getOrgaos(pOrgao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrgao(Orgao pOrgao)  {

		pOrgao.setDtcCriacao(new Date());

		daoOrgao.salvar(pOrgao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarOrgao(Orgao pOrgao)  {

		daoOrgao.atualizar(pOrgao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOrgao(Orgao pOrgao)  {

		pOrgao.setIndExcluido(true);
		pOrgao.setDtcExclusao(new Date());

		daoOrgao.atualizar(pOrgao);
	}

	//Dependências
	public Collection<NivelCompetencia> filtrarListaNiveisCompetencia(NivelCompetencia pNivelCompetencia) {

		return new NivelCompetenciaDAOImpl().getNiveisCompetencia(pNivelCompetencia);
	}
}