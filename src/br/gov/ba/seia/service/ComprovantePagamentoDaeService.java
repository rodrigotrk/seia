package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ComprovantePagamentoDaeImpl;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.Requerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComprovantePagamentoDaeService {
	
	@Inject
	private ComprovantePagamentoDaeImpl comprovantePagamentoDaeImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ComprovantePagamentoDae comprovantePagamentoDae)  {
		comprovantePagamentoDaeImpl.salvarOuAtualizar(comprovantePagamentoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComprovantePagamentoDae> obterPorRequerimento(Requerimento requerimento)  {
		return comprovantePagamentoDaeImpl.obterPorRequerimento(requerimento); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComprovantePagamentoDae> obterPorIdRequerimento(Integer ideRequerimento)  {
		return comprovantePagamentoDaeImpl.obterPorIdRequerimento(ideRequerimento); 
	}

	public Collection<ComprovantePagamentoDae> listarByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq)  {
		return comprovantePagamentoDaeImpl.listarByIdeRequerimento(ideRequerimento, ideBoletoDaeReq); 
	}

	public ComprovantePagamentoDae carregarCertificadoByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq)  {
		return comprovantePagamentoDaeImpl.carregarCertificadoByIdeRequerimento(ideRequerimento, ideBoletoDaeReq); 
	}
	
	public ComprovantePagamentoDae carregarCertificadoByIdeRequerimentoDae(Integer ideRequerimento, Integer ideBoletoDaeReq)  {
		return comprovantePagamentoDaeImpl.carregarCertificadoByIdeRequerimentoDae(ideRequerimento, ideBoletoDaeReq); 
	}
	
	public ComprovantePagamentoDae carregarVistoriaByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq)  {
		return comprovantePagamentoDaeImpl.carregarVistoriaByIdeRequerimento(ideRequerimento, ideBoletoDaeReq); 
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Collection<ComprovantePagamentoDae> comprovantesDAE)  {
		for (ComprovantePagamentoDae comprovantePagamentoDae : comprovantesDAE) {
			this.salvarOuAtualizar(comprovantePagamentoDae);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ComprovantePagamentoDae comprovantePagamentoDae)  {
		comprovantePagamentoDaeImpl.remover(comprovantePagamentoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComprovantePagamentoDae> consultarPorIdBoletoDaeRequerimento(Integer idBoletoDaeRequerimento)  {
		List<ComprovantePagamentoDae> lista = comprovantePagamentoDaeImpl.consultarPorIdBoletoDaeRequerimento(idBoletoDaeRequerimento) ;
		
		List<ComprovantePagamentoDae> novaLista = new ArrayList<ComprovantePagamentoDae>();
		
		for (ComprovantePagamentoDae comprovantePagamentoDae : lista) {
			if (novaLista.size() < 2){
				if (comprovantePagamentoDae.getDscCaminhoArquivo().contains("vistoria")){
					novaLista.add(comprovantePagamentoDae);
				}
				
				if (comprovantePagamentoDae.getDscCaminhoArquivo().contains("certificado")){
					novaLista.add(comprovantePagamentoDae);
				}
			} else {
				break;
			}
		}
		return novaLista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizar(ComprovantePagamentoDae pComprovantePagamento)  {
		comprovantePagamentoDaeImpl.salvarOuAtualizar(pComprovantePagamento);
	}
}
