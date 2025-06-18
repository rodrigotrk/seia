package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.MunicipioDAOImpl;
import br.gov.ba.seia.dao.OrgaoDAOImpl;
import br.gov.ba.seia.dao.RelOrgaoMunicipioDAOImpl;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.RelOrgaoMunicipio;
import br.gov.ba.seia.entity.RelOrgaoMunicipioPK;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelOrgaoMunicipioService {

	@Inject
	IDAO<RelOrgaoMunicipio> daoRelOrgaoMunicipio;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RelOrgaoMunicipio filtrarPermissaoById(RelOrgaoMunicipio pRelOrgaoMunicipio) {

		return daoRelOrgaoMunicipio.buscarEntidadePorExemplo(pRelOrgaoMunicipio);
	}

	public Collection<RelOrgaoMunicipio> filtrarListaRelOrgaosMunicipios(RelOrgaoMunicipio pRelOrgaoMunicipio) {

		return new RelOrgaoMunicipioDAOImpl().getOrgaosMunicipios(pRelOrgaoMunicipio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrgaoMunicipio(RelOrgaoMunicipio pRelOrgaoMunicipio)  {

		pRelOrgaoMunicipio.setRelOrgaoMunicipioPK(new RelOrgaoMunicipioPK());
		pRelOrgaoMunicipio.getRelOrgaoMunicipioPK().setIdeOrgao(pRelOrgaoMunicipio.getOrgao().getIdeOrgao());
		pRelOrgaoMunicipio.getRelOrgaoMunicipioPK().setIdeMunicipio(pRelOrgaoMunicipio.getMunicipio().getIdeMunicipio());

		daoRelOrgaoMunicipio.salvar(pRelOrgaoMunicipio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarOrgaoMunicipio(RelOrgaoMunicipio pRelOrgaoMunicipio)  {

		daoRelOrgaoMunicipio.atualizar(pRelOrgaoMunicipio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOrgaoMunicipio(RelOrgaoMunicipio pRelOrgaoMunicipio)  {

		daoRelOrgaoMunicipio.remover(pRelOrgaoMunicipio);
	}

//	Depend�ncias
	public Collection<Orgao> filtrarListaOrgaos(Orgao pOrgao) {

		return new OrgaoDAOImpl().getOrgaos(pOrgao);
	}

	public Collection<Municipio> filtrarListaMunicipios(Municipio pMunicipio) {

		return new MunicipioDAOImpl().getMunicipios(pMunicipio);
	}
}