package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProcessoReenquadramentoHistAtoDAOImpl;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoReenquadramentoHistAtoServiceFacade {

	@Inject
	private ProcessoReenquadramentoHistAtoDAOImpl processoReenquadramentoHistAtoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcessoReenquadramentoHistAto processoReenquadramentoHistAto) {
		processoReenquadramentoHistAtoDAOImpl.salvarOuAtualizar(processoReenquadramentoHistAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramentoHistAto obterProcessoReenquadramentoHistAtoReenquadrado(ProcessoReenquadramento processoReenquadramento, EnquadramentoAtoAmbiental enquadramentoAtoAmbiental){
		return processoReenquadramentoHistAtoDAOImpl.obterProcessoReenquadramentoHistAtoReenquadrado(processoReenquadramento, enquadramentoAtoAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String mensagemObservaoReenquadramento(ProcessoReenquadramento processoReenquadramento, AtoAmbiental atoAmbiental, Tipologia tipologia) {
		
		ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = obterProReenHistAtoPorProcessoReenquadramento(processoReenquadramento, atoAmbiental, tipologia);
		
		StringBuilder msg = new StringBuilder();
		
		if (!Util.isNullOuVazio(processoReenquadramentoHistAto)){
			if (!Util.isNullOuVazio(processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal())){
				msg.append("O ato \"".concat(processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal().getNomAtoAmbiental()));
				
				if (!Util.isNull(processoReenquadramentoHistAto.getIdeTipologiaOriginal())){
					msg.append(" - ".concat(processoReenquadramentoHistAto.getIdeTipologiaOriginal().getDesTipologia()));
				}
				
				msg.append("\" foi reenquadrado para \""
						.concat(processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getAtoAmbiental().getNomAtoAmbiental()));
				
				if (!Util.isNull(processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getTipologia())){
					msg.append(" - ".concat(processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getTipologia().getDesTipologia()));
				}
				
				msg.append("\"");
			} else {
				
					msg.append("Este ato foi incluído por reenquadramento");
				
			}
		} 
		
		return msg.toString();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramentoHistAto obterProReenHistAtoPorProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento, AtoAmbiental atoAmbiental, Tipologia tipologia) {
		
		List<ProcessoReenquadramentoHistAto> lista = processoReenquadramentoHistAtoDAOImpl.obterProReenHistAtoPorProcessoReenquadramento(processoReenquadramento);
		
		for (ProcessoReenquadramentoHistAto processoReenquadramentoHistAto : lista) {
			if (!Util.isNullOuVazio(tipologia)){
				if (processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getAtoAmbiental().equals(atoAmbiental) && processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getTipologia().equals(tipologia)){
					return processoReenquadramentoHistAto;
				}
			} else {
				if (processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getAtoAmbiental().equals(atoAmbiental) && Util.isNullOuVazio( processoReenquadramentoHistAto.getIdeEnquadramentoAtoAmbientalReenquadrado().getTipologia())){
					return processoReenquadramentoHistAto;
				}
			}
		}
		return null;
	} 
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoReenquadramentoHistAto> obterProReenHistAtoPorProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento) {
		if (!Util.isNullOuVazio(processoReenquadramento)){
			return processoReenquadramentoHistAtoDAOImpl.obterProReenHistAtoPorProcessoReenquadramento(processoReenquadramento);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramentoHistAto obterProReenHistAtoAlteradoPorProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento, AtoAmbiental atoAmbiental, Tipologia tipologia) {
		
		List<ProcessoReenquadramentoHistAto> lista = processoReenquadramentoHistAtoDAOImpl.obterProReenHistAtoPorProcessoReenquadramento(processoReenquadramento);
		
		for (ProcessoReenquadramentoHistAto processoReenquadramentoHistAto : lista) {
			if (!Util.isNullOuVazio(tipologia)){
				if (processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal().equals(atoAmbiental) && processoReenquadramentoHistAto.getIdeTipologiaOriginal().equals(tipologia)){
					return processoReenquadramentoHistAto;
				}
			} else {
				if (!Util.isNull(processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal())){
					if ( processoReenquadramentoHistAto.getIdeAtoAmbientalOriginal().equals(atoAmbiental) && Util.isNullOuVazio( processoReenquadramentoHistAto.getIdeTipologiaOriginal())){
						return processoReenquadramentoHistAto;
					}
				}
			}
		}
		return null;
	} 
}
