package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ItemCronograma;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ItemCronogramaService {

	@Inject
	private IDAO<ItemCronograma> daoItemCronograma;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ItemCronograma obterPorId(Integer ideItemCronograma){
		return daoItemCronograma.carregarGet(ideItemCronograma);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ItemCronograma> listarTodos() {
		return daoItemCronograma.listarTodos();
	}
}