package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AreaDAOImpl;
import br.gov.ba.seia.dao.AreaMunicipioDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.MunicipioDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AreaMunicipio;
import br.gov.ba.seia.entity.AreaMunicipioPK;
import br.gov.ba.seia.entity.Municipio;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AreaMunicipioService {
	
	@Inject
	IDAO<AreaMunicipio> daoAreaMunicipio;	

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaMunicipio filtrarPermissaoById(AreaMunicipio pAreaMunicipio) {

		return daoAreaMunicipio.buscarEntidadePorExemplo(pAreaMunicipio);
	}

	public Collection<AreaMunicipio> filtrarListaAreasMunicipios(AreaMunicipio pAreaMunicipio) {

		return new AreaMunicipioDAOImpl().getAreasMunicipios(pAreaMunicipio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAreaMunicipio(AreaMunicipio pAreaMunicipio) {

		pAreaMunicipio.setAreaMunicipioPK(new AreaMunicipioPK());
		pAreaMunicipio.getAreaMunicipioPK().setIdeArea(pAreaMunicipio.getArea().getIdeArea());
		pAreaMunicipio.getAreaMunicipioPK().setIdeMunicipio(pAreaMunicipio.getMunicipio().getIdeMunicipio());
		daoAreaMunicipio.salvar(pAreaMunicipio);
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarAreaMunicipio(AreaMunicipio pAreaMunicipio)  {

		daoAreaMunicipio.atualizar(pAreaMunicipio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAreaMunicipio(AreaMunicipio pAreaMunicipio)  {

		daoAreaMunicipio.remover(pAreaMunicipio);
	}

//	Depend�ncias
	public Collection<Area> filtrarListaAreas(Area pArea) {

		return new AreaDAOImpl().getAreas(pArea);
	}

	public Collection<Municipio> filtrarListaMunicipios(Municipio pMunicipio) {

		return new MunicipioDAOImpl().getMunicipios(pMunicipio);
	}
}
