package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LacErbEquipamentoDAOImpl;
import br.gov.ba.seia.entity.LacErbEquipamento;
import br.gov.ba.seia.entity.LacErbEquipamentoPK;
import br.gov.ba.seia.util.Util;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacErbEquipamentoService {
	
	@Inject
	LacErbEquipamentoDAOImpl lacErbEquipamentoDAOImpl;

	@EJB
	CertificadoService certificadoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(LacErbEquipamento pLacErbEquipamento)  {
		this.lacErbEquipamentoDAOImpl.salvar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacErbEquipamento pLacErbEquipamento)  {
		this.lacErbEquipamentoDAOImpl.atualizar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacErbEquipamento pLacErbEquipamento)  {
		this.lacErbEquipamentoDAOImpl.excluir(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacErbEquipamento carregarByIde(LacErbEquipamentoPK pLacErbEquipamentoPK)  {
		return this.lacErbEquipamentoDAOImpl.carregarByIde(pLacErbEquipamentoPK);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacErbEquipamento> carregarByIde(int pIde)  {
		return this.lacErbEquipamentoDAOImpl.carregarByIde(pIde);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideRequerimento)  {
		return Util.isNull(this.certificadoService.carregarByIdRequerimento(ideRequerimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacErbEquipamento pLacErbEquipamento)  {
		this.lacErbEquipamentoDAOImpl.salvarOuAtualizar(pLacErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(LacErbEquipamentoPK pLacErbEquipamentoPK)  {
		this.lacErbEquipamentoDAOImpl.remover(pLacErbEquipamentoPK);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Collection<LacErbEquipamento> pLacErbEquipamentos)  {
		this.lacErbEquipamentoDAOImpl.remover(pLacErbEquipamentos);
	}
	

}