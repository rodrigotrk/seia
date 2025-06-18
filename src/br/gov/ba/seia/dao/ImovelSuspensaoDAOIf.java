package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.ImovelRural;


public interface ImovelSuspensaoDAOIf {

	public boolean listarImovelSuspensaoByImovelRural(List<ImovelRural> listaImovelRural);

}
     
