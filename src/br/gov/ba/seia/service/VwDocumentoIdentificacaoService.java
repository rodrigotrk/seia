/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.VwDocumentoIdentificacao;


/**
 * @author MJunior
 *
 */
@Stateless
public class VwDocumentoIdentificacaoService {
	
	@Inject
	IDAO<VwDocumentoIdentificacao> vwDocumentoIdentificacaoDAO;
	
	public Collection<VwDocumentoIdentificacao>filtrarDocumentosIdentificacaoRequerimentoUnicoById(RequerimentoUnico requerimento) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimentoUnico());
		return vwDocumentoIdentificacaoDAO.buscarPorNamedQuery("VwDocumentoIdentificacao.findByIdeRequerimento", parametros);
	}
	
}
