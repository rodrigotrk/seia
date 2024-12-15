package br.gov.ba.seia.service.facade;

import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoMotivoNotificacaoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.MotivoNotificacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConsultarNotificacaoServiceFacade {
	
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	@EJB
	private NotificacaoServiceFacade notificacaoServiceFacade;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private NotificacaoMotivoNotificacaoDAOImpl notificacaoMotivoNotificacaoDAOImpl;
	@EJB
	private AreaService areaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarCoordenacoes()  {
		return areaService.listarCoordenacoes();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Notificacao> filtrarlistaNotificacao(Integer first, Integer pageSize, Map<String,Object> params)  {
		return notificacaoDAOImpl.filtrarlistaNotificacao(first, pageSize, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String statusNumNotificacao(Notificacao notificacao){
		return notificacaoServiceFacade.statusNumNotificacao(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countNotificacao(Map<String,Object> params) {
		return notificacaoDAOImpl.countNotificacao(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacao() {
		return motivoNotificacaoService.listarMotivoNotificacao();
	}
}