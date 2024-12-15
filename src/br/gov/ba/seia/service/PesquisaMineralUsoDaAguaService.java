package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PesquisaMineralUsoDaAguaDAOImpl;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;

/**
 * Serviço da classe {@link PesquisaMineralUsoDaAgua}
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralUsoDaAguaService {

	@Inject
	private PesquisaMineralUsoDaAguaDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua)  {
		dao.salvar(pesquisaMineralUsoDaAgua);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<PesquisaMineralUsoDaAgua> listaPesquisaMineralUsoDaAgua)  {
		List<PesquisaMineralUsoDaAgua> listaTemp = new ArrayList<PesquisaMineralUsoDaAgua>();
		for(PesquisaMineralUsoDaAgua usoAgua : listaPesquisaMineralUsoDaAgua){
			if(usoAgua.isSelecionado()){
				listaTemp.add(usoAgua);
			}
		}
		dao.salvar(listaTemp);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralUsoDaAgua> listar(PesquisaMineral pesquisaMineral)  {
		return dao.listar(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		dao.excluir(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineralUsoDaAgua usoAgua)  {
		dao.excluir(usoAgua);
	}
}
