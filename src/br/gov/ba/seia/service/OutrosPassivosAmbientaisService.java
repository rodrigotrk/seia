package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.OutrosPassivosAmbientaisDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.OutrosPassivosAmbientais;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutrosPassivosAmbientaisService {

	@Inject
	OutrosPassivosAmbientaisDAOImpl daoImpl;

	public OutrosPassivosAmbientais filtrarById(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		return this.daoImpl.filtrarById(pOutrosPassivosAmbientais);
	}
	
	public OutrosPassivosAmbientais carregarTudo(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		return this.daoImpl.carregarTudo(pOutrosPassivosAmbientais);
	}

	public OutrosPassivosAmbientais buscaOutrosPassivosAmbientaisByImovelRural(ImovelRural imovelRural) {
		
		return this.daoImpl.buscaOutrosPassivosAmbientaisByImovelRural(imovelRural);
	}

	public void salvar(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		this.daoImpl.salvar(pOutrosPassivosAmbientais);
	}

	public void atualizar(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		this.daoImpl.atualizar(pOutrosPassivosAmbientais);
	}
	
	public void remover(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		this.daoImpl.remover(pOutrosPassivosAmbientais);
	}
}
