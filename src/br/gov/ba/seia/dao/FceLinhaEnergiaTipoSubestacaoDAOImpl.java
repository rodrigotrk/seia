package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLinhaEnergiaTipoSubestacao;

@Stateless
public class FceLinhaEnergiaTipoSubestacaoDAOImpl {

	@Inject
	private IDAO<FceLinhaEnergiaTipoSubestacao> fceLinhaEnergiaTipoSubestacaoIDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoSubestacao(FceLinhaEnergiaTipoSubestacao fceLinhaEnergiaTipoSubestacao) {
	
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLinhaEnergia", fceLinhaEnergiaTipoSubestacao.getIdeFceLinhaEnergia());
		parameters.put("ideTipoSubestacao", fceLinhaEnergiaTipoSubestacao.getIdeTipoSubestacao());
		
		fceLinhaEnergiaTipoSubestacaoIDAO.executarNamedQuery("FceLinhaEnergiaTipoSubestacao.removeFceLinhaEnergiaTipoSubestacao", parameters);
	}
}
