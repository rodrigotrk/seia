package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.VwResponsavelDocumentoProcuracao;

@Stateless
public class VwResponsavelDocumentoProcuracaoService {

	@Inject
	private IDAO<VwResponsavelDocumentoProcuracao> vwResponsavelDocumentoProcuracaoDAO;

	public Collection<VwResponsavelDocumentoProcuracao> listarPorRequerimento(RequerimentoUnico requerimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwResponsavelDocumentoProcuracao.class)
				.add(Restrictions.eq("ideRequerimentoDto", requerimento.getIdeRequerimentoUnico()));
		return vwResponsavelDocumentoProcuracaoDAO.listarPorCriteria(criteria);
	}
	
	
}
