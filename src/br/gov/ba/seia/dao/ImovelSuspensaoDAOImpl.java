package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelSuspensao;

public class ImovelSuspensaoDAOImpl implements ImovelSuspensaoDAOIf {

	@Inject
	IDAO<ImovelSuspensao> imovelSuspensaoDAO;
	
	@Override
	public boolean listarImovelSuspensaoByImovelRural(List<ImovelRural> listaImovelRural){
		if(listaImovelRural.isEmpty()){
			return false;
		}
		StringBuilder ideImovelRural = new StringBuilder();	
		
		for (ImovelRural imovelRural : listaImovelRural) {
			if(ideImovelRural.length() > 0){
				ideImovelRural.append(",");
			}
			ideImovelRural.append(imovelRural.getIdeImovelRural().toString());
		}
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery("SELECT *  FROM imovel_suspensao WHERE ide_imovel_rural in ("+ ideImovelRural.toString() + ") AND ind_excluido = false");
		
		
		return !lQuery.getResultList().isEmpty();
	}
}
