package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProcuradorRepresentanteDAOImpl;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcuradorRepresentanteService {
	
	@Inject
	private ProcuradorRepresentanteDAOImpl procuradorRepresentanteDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorPessoaJuridica(Pessoa solicitante,Pessoa requerente)  {
		return procuradorRepresentanteDAOImpl.isProcuradorPessoaJuridica(solicitante,requerente);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante)  {
		procuradorRepresentanteDAOImpl.salvarProcuradorRepresentante(procuradorRepresentante);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorRepresentante> getListaProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante)  {
		if (!Util.isNull(procuradorRepresentante)) {
			return procuradorRepresentanteDAOImpl.getListaProcuradorRepresentante(procuradorRepresentante);
		}

		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorRepresentante> listarProcuradorRepresentanteComProjection(ProcuradorRepresentante procuradorRepresentante)  {
		if (!Util.isNull(procuradorRepresentante)) {
			return procuradorRepresentanteDAOImpl.listarProcuradorRepresentanteComProjection(procuradorRepresentante);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante)  {
		procuradorRepresentanteDAOImpl.excluirProcuradorRepresentante(procuradorRepresentante);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepresentante buscarPorIdViaCriteria(ProcuradorRepresentante procurador) {
		return procuradorRepresentanteDAOImpl.buscarPorIdViaCriteria(procurador);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepresentante buscarProcuradorRepresentante(PessoaFisica procurador, PessoaJuridica pj) {
		return procuradorRepresentanteDAOImpl.buscarProcuradorRepresentante(procurador, pj);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorRepresentanteInativado(int ideProcurador, int idePessoaJuridica) {
		return procuradorRepresentanteDAOImpl.isProcuradorRepresentanteInativado(ideProcurador, idePessoaJuridica);

	}
	
	/**
	 * Método que verifica se o usuário logado é o procurador representante do cnpj que está cadastrando 
	 * <li> <b>return </b>1   ::: caso ele seja um dos procuradores</li>
	 * <li> <b>return </b>0   ::: caso o cnpj ainda não tenha procuradores cadastrados</li>
	 * <li> <b>return </b>-1  :: caso ele não seja o procurador</li>
	 * <li> <b>return </b>null  :: caso algum dos parâmetros esteja nulo</li>
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer verificaProcuradorRepresentante(PessoaFisica pessoaValidar, ProcuradorRepresentante procuradorRepresentante)  {
		if(!Util.isNullOuVazio(pessoaValidar) && procuradorRepresentante != null && !Util.isNullOuVazio(procuradorRepresentante.getIdePessoaJuridica())){
			Collection<ProcuradorRepresentante> procuradoresRepresentantes = this.getListaProcuradorRepresentante(procuradorRepresentante);
			if(!Util.isNullOuVazio(procuradoresRepresentantes)){
				for (ProcuradorRepresentante pr : procuradoresRepresentantes) {
					if (pessoaValidar.getIdePessoaFisica().equals(pr.getIdeProcurador().getIdePessoaFisica())) {
						return 1;
					}
				}
				return -1;
			}
			else {
				return 0;
			}
		} 
		return null;
	}
	
	public List<ProcuradorRepresentante> buscarProcuradorRepresentanteByPessoaJuridica(Integer pessoaJuridica){
		return procuradorRepresentanteDAOImpl.buscarProcuradorRepresentanteByPessoaJuridica(pessoaJuridica);
		
	}
}
