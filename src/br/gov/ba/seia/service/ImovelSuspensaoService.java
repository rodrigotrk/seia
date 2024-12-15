package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ImovelSuspensaoDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelSuspensaoService {
	
	@Inject
	private ImovelSuspensaoDAOImpl imovelSuspensaoImplDao;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	/**
	 * 
	 * Método que retorna se os imóveis sobrepostos pela theGeom estão suspenso. 
	 *
	 * @author danilo.santos
	 *
	 * @param theGeom
	 * @return String
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean listaImoveisSobrepostos(String theGeom, ImovelRural imovel) {
		List<ImovelRural> listaImoveisSobrepostos = imovelRuralService.listarImoveisSobrepostosPorId(localizacaoGeograficaServiceFacade.getCollectionLocalizacaoGeograficaSobrepostaBy(theGeom, imovel.getIdeImovelRural()), imovel);
		return imovelSuspensaoImplDao.listarImovelSuspensaoByImovelRural(listaImoveisSobrepostos);
	}
	
	
	
}