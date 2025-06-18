package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.NomePopularEspecieDAOImpl;
import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.EspecieSupressao;
import br.gov.ba.seia.entity.NomePopularEspecie;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NomePopularEspecieServiceFacade {
	
	@EJB
	private NomePopularEspecieDAOImpl nomePopularEspecieDAO;
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieFlorestal especieFlorestal)  {
		return nomePopularEspecieDAO.listarNomePopularEspecie(especieFlorestal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NomePopularEspecie> listarNomePopularEspecie(EspecieSupressao especieSupressao)  {
		return nomePopularEspecieDAO.listarNomePopularEspecie(especieSupressao);
	}
	
}