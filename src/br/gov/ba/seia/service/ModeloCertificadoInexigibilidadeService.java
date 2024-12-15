package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.entity.AtividadeInexigivelModeloCertificadoInexigibilidade;
import br.gov.ba.seia.entity.ModeloCertificadoInexigibilidade;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModeloCertificadoInexigibilidadeService {

	@Inject
	private IDAO<ModeloCertificadoInexigibilidade> modeloCertificadoInexigibilidadeDAO;
	
	@Inject
	private IDAO<AtividadeInexigivelModeloCertificadoInexigibilidade> atividadeInexigivelModeloCertificadoInexigibilidadeDAO;
	
	@EJB
	private GerenciaArquivoService arquivoService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtividadeInexigivelModeloCertificadoInexigibilidade obterModeloCertificadoPorAtividadeInexigivel(AtividadeInexigivel atividadeInexigivel)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtividadeInexigivelModeloCertificadoInexigibilidade.class)
				.createAlias("atividadeInexigivel", "atividade")
				
				.add(Restrictions.eq("atividade.ideAtividadeInexigivel", atividadeInexigivel.getIdeAtividadeInexigivel()));
		
		return atividadeInexigivelModeloCertificadoInexigibilidadeDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ModeloCertificadoInexigibilidade> listarModeloCertificado()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ModeloCertificadoInexigibilidade.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("descricao"));
		return modeloCertificadoInexigibilidadeDAO.listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent baixarArquivo(String caminhoArquivo) {
		try {
			return arquivoService.getContentFile(caminhoArquivo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AtividadeInexigivelModeloCertificadoInexigibilidade atividadeInexigivelModeloCertificadoInexigibilidade) {
		atividadeInexigivelModeloCertificadoInexigibilidadeDAO.salvar(atividadeInexigivelModeloCertificadoInexigibilidade);
	}
	
}
