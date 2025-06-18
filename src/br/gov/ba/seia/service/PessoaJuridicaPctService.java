package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.PessoaJuridicaPctDAOImpl;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PessoaJuridicaPct;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaJuridicaPctService {

	@EJB
	private PessoaJuridicaPctDAOImpl pessoaJuridicaPctDAOImpl;
	
	public boolean existePessoaJuridicaAssociadaComunidade(PctImovelRural pctImovelRural, PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		return pessoaJuridicaPctDAOImpl.existePessoaJuridicaAssociadaComunidade(pctImovelRural, pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		pessoaJuridicaPctDAOImpl.salvarPessoaJuridicarPct(pessoaJuridicaPct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updatePessoaJuridicarPct(PessoaJuridicaPct pessoaJuridicaPct) throws Exception{
		pessoaJuridicaPctDAOImpl.updatePessoaJuridicarPct(pessoaJuridicaPct);
	}
	
	public List<PessoaJuridicaPct> listarPessoaJuridicaByPct(PctImovelRural pctImovelRural) throws Exception{
		
		return pessoaJuridicaPctDAOImpl.listarPessoaJuridicaByPct(pctImovelRural);
	}
	
	public PessoaJuridicaPct obterPessoaJuridicaPct(Integer idePessoaJuridicaPct) throws Exception{
		return pessoaJuridicaPctDAOImpl.obterPessoaJuridicaPct(idePessoaJuridicaPct);
	}
}
