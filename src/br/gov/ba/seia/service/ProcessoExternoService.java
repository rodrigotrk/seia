package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ProcessoExternoDAOImpl;
import br.gov.ba.seia.entity.ProcessoExterno;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoExternoService {

	@Inject
	private ProcessoExternoDAOImpl processoExternoDaoImpl;
	
	@Inject
	private IDAO<ProcessoExterno> processoExternoDAO;
	
	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno, List<String> listaSistemas)  {
		return processoExternoDaoImpl.buscarProcessoExternoBySistemaNumero(tratarNumeroDoProcesso(processoExterno), listaSistemas);
	}
	
	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno)  {
		return processoExternoDaoImpl.buscarProcessoExternoBySistemaNumero( tratarNumeroDoProcesso(processoExterno));
	}	

	public ProcessoExterno buscarProcessoExternoByNumeroProcessoCNPJ(String numeroProcesso, String documentoCpfCnpj)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoExterno.class);
		criteria.add(Restrictions.and(Restrictions.eq("processo", numeroProcesso), Restrictions.eq("documentoCpfCnpj", documentoCpfCnpj)));
		return processoExternoDAO.buscarPorCriteria(criteria);
	}
	
	public ProcessoExterno buscarProcessoExternoByNumeroProcesso(String numeroProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoExterno.class);
		criteria.add(Restrictions.ilike("processo", numeroProcesso, MatchMode.EXACT));
		return processoExternoDAO.buscarPorCriteria(criteria);
	}
	
	private ProcessoExterno tratarNumeroDoProcesso(ProcessoExterno processoExterno) {
		// recebe numprocesso vindo da tela no formato ####-######
		if (processoExterno.getSistema().equals("CERBERUS")) {
			String numProcesso = processoExterno.getProcesso();
			if (numProcesso.startsWith("9")) {
				String numProcessoTratado = null;
				int posicaoTraco = numProcesso.indexOf("-");
				String primeiraParte = numProcesso.substring(0, posicaoTraco);
				String segundaParte = numProcesso.substring(++posicaoTraco);
				int lengthSP = segundaParte.length();
				String dpoisBarra = segundaParte.substring(--lengthSP);
				String antesBarra = segundaParte.substring(0, lengthSP);
				segundaParte = antesBarra + "/" + dpoisBarra;
				numProcessoTratado = primeiraParte + segundaParte;
				processoExterno.setProcesso(numProcessoTratado);
			}
		}
		return processoExterno;
	}

}
