package br.gov.ba.seia.service.facade;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.ComunicacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoReenquadramentoProcessoServiceFacade {

	@Inject
	IDAO<ComunicacaoRequerenteInterface> comunicacaoRequerenteInterfaceDAO;
	
	@Inject
	IDAO<ComunicacaoReenquadramentoProcesso> comunicacaoReenquadramentoProcessoDAO;
	
	@Inject
	ComunicacaoReenquadramentoProcessoDAOImpl comunicacaoReenquadramentoProcessoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComunicacaoRequerenteInterface> carregarComunicacaoDemanda(Integer ideProcessoReenquadramento, int startPage, int maxPage) {
		DetachedCriteria criteria = criteriaCarregaComunicacao(ideProcessoReenquadramento)
			.addOrder(Order.desc("ideProcessoReenquadramento"))
		;
		return comunicacaoRequerenteInterfaceDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer carregarComunicacaoDemandaCount(Integer ideProcessoReenquadramento)  {
		DetachedCriteria criteria = criteriaCarregaComunicacao(ideProcessoReenquadramento);
		return comunicacaoRequerenteInterfaceDAO.count(criteria);
	}
	
	private DetachedCriteria criteriaCarregaComunicacao(Integer ideProcessoReenquadramento){
		return  DetachedCriteria.forClass(ComunicacaoReenquadramentoProcesso.class)
				.createAlias("ideProcessoReenquadramento", "req")
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("desMensagem"), "desMensagem")
								.add(Projections.property("dtcComunicacao"), "dtcComunicacao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ComunicacaoRequerimento.class))
				.add(Restrictions.eq("req.ideProcessoReenquadramento", ideProcessoReenquadramento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComunicacaoReenquadramentoProcesso buscarComunicacaoPorIdeProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComunicacaoReenquadramentoProcesso.class)
				.createAlias("ideProcessoReenquadramento", "Procreeq");
			criteria.add(Restrictions.eq("Procreeq.ideProcessoReenquadramento", ideProcessoReenquadramento));
			
		return comunicacaoReenquadramentoProcessoDAO.buscarPorCriteria(criteria);
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarComunicacaoReenquadramentoProcesso(ComunicacaoReenquadramentoProcesso comunicacaoReenquadramentoProcesso) {
		comunicacaoReenquadramentoProcessoDAOImpl.salvarComunicacaoReenquadramento(comunicacaoReenquadramentoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarEmailDocumentoValidado(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a), \n\n\n");
		msg.append("A documentação do processo de reenquadramento nº " + processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getNumProcesso()+ " foi validada. \n\n");
		msg.append("Aguarde novas orientações.\n\n");
		msg.append("Atenciosamente,\n");
		msg.append("Central de Atendimento/INEMA");
		
		return msg.toString();
	}
	
}
