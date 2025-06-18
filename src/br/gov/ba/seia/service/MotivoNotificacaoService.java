package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.MotivoNotificacaoDAOImpl;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoNotificacaoService {
	
	@EJB
	private MotivoNotificacaoDAOImpl motivoNotificacaoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarExistenciaShape(Notificacao notificacao)  {
		Collection<MotivoNotificacao> lista = motivoNotificacaoDAOImpl.listarMotivoNotificacaoComEnvioShape(notificacao.getIdeNotificacao());
		return !Util.isNullOuVazio(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacao()  {
		return motivoNotificacaoDAOImpl.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacaoSemEnvioShape(Notificacao notificacao)  {
		return motivoNotificacaoDAOImpl.listarMotivoNotificacaoSemEnvioShape(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacao(Notificacao notificacao)  {
		return motivoNotificacaoDAOImpl.listarMotivoNotificacao(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacaoEnvioShape(Notificacao notificacao)  {
		return motivoNotificacaoDAOImpl.listarMotivoNotificacaoEnvioShape(notificacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarByIdeNotificacao(Integer ideNotificacao)  {
		return motivoNotificacaoDAOImpl.listarByIdeNotificacao(ideNotificacao);
	}

	public MotivoNotificacao carregar(Integer id)  {
		return motivoNotificacaoDAOImpl.carregar(id);
	}
	
}