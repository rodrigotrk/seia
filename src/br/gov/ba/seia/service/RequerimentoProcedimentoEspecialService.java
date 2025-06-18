package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.RequerimentoProcedimentoEspecialDAOImpl;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoProcedimentoEspecial;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoProcedimentoEspecialService {

	@Inject
	public RequerimentoProcedimentoEspecialDAOImpl requerimentoProcedimentoEspecialDAOImpl;
	
	public void salvarRequerimentoProcedimentoEspecial(RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial) {
		requerimentoProcedimentoEspecialDAOImpl.salvar(requerimentoProcedimentoEspecial);
	}
	
	public void excluirRequerimentoProcedimentoEspecial(RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial) {
		requerimentoProcedimentoEspecialDAOImpl.excluir(requerimentoProcedimentoEspecial);
	}
	
	public RequerimentoProcedimentoEspecial getRequerimentoProcedimentoEspecialByRequerimento(Requerimento requerimento) {
		return requerimentoProcedimentoEspecialDAOImpl.obterRequerimentoProcedimentoEspecialPorRequerimento(requerimento);
	}

}
