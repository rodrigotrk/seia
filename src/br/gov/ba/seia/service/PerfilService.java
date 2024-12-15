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
import br.gov.ba.seia.dao.PerfilDAOImpl;
import br.gov.ba.seia.entity.Perfil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PerfilService {

	@Inject
	IDAO<Perfil> daoPerfil;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Perfil filtrarPerfilById(int id){

		return daoPerfil.carregarGet(id);
	}

	public Collection<Perfil> filtrarListaPerfis(Perfil pPerfil) {

		return new PerfilDAOImpl().getPerfis(pPerfil);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPerfil(Perfil pPerfil)  {

		pPerfil.setDtcCriacao(new Date());

		daoPerfil.salvar(pPerfil);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPerfil(Perfil pPerfil)  {

		daoPerfil.atualizar(pPerfil);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPerfil(Perfil pPerfil)  {

		pPerfil.setIndExcluido(true);
		pPerfil.setDtcExclusao(new Date());

		daoPerfil.atualizar(pPerfil);
	}
}