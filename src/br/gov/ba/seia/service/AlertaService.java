package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.entity.Alerta;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoAlerta;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoAlertaEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.GCMNotification;
import br.gov.ba.ws.entity.UsuarioDispositivo;
import br.gov.ba.ws.enumerator.TipoDispositivoEnum;

@Stateless
public class AlertaService {

	@Inject
	private IDAO<Alerta> alertaDAO;
	
	@Inject
	private GCMNotification gmcNotification;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private NotificacaoServiceFacade notificacaoService;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	
	@EJB
	private StatusFluxoService statusFluxoService;
	
	private static final Integer QTD_DIAS_ALERTA_NOTIFICACAO = 5;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarAlerta(TramitacaoRequerimento tramitacao){
		try {
			StringBuilder msg =new StringBuilder();
			StringBuilder titulo = new StringBuilder();
			if(verificarCriacaoAlerta(tramitacao,titulo,msg)){
				List<Usuario> usuarios = usuarioService.listarDispositivosEnvolvidosPorIdeRequerimento(tramitacao.getIdeRequerimento().getIdeRequerimento());
				if(!Util.isNullOuVazio(usuarios)){
					salvarAlerta(msg.toString(),titulo.toString(),usuarios, tramitacao.getIdeRequerimento().getNumRequerimento(),new TipoAlerta(TipoAlertaEnum.REQUERIMENTO.getId()));
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarCriacaoAlerta(TramitacaoRequerimento tramitacao,StringBuilder titulo,StringBuilder msg) {
		if(tramitacao.getIdeStatusRequerimento().isPendenciaValidacao()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " está pendente de validação, favor acessar o SEIA Web para enviar os documentos pendentes.");
			titulo.append("Pendente de Validação");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isEnquadrado()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " foi enquadrado com sucesso.");
			titulo.append("Enquadrado");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isValidado()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " foi validado.");
			titulo.append("Validado");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isBoletoLiberado()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " foi alterado para o status de boleto liberado.");
			titulo.append("Boleto liberado");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isProcessoFormado()){
			msg.append("Foi formado processo para o requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + ".");
			titulo.append("Processo Formado");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isCancelado()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " foi cancelado.");
			titulo.append("Cancelado");
			return true;
		}
		else if(tramitacao.getIdeStatusRequerimento().isPendenciaValidacaoComprovante()){
			msg.append("O requerimento de número " + tramitacao.getIdeRequerimento().getNumRequerimento() + " o comprovante está pendente de validação, favor acessar o SEIA Web para enviar o comprovante pendente.");
			titulo.append("Pendente de Validação do Comprovante");
			return true;
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAlerta(String msg, String titulo, List<Usuario> usuarios, String numDocumento, TipoAlerta tipoAlerta) {
		for(Usuario usuario: usuarios){
			Alerta alerta = new Alerta();
			alerta.setDesMensagem(msg);
			alerta.setDtcEnvio(new Date());
			alerta.setIndLida(false);
			alerta.setNumDocumento(numDocumento);
			alerta.setTipoAlerta(tipoAlerta);
			alerta.setIdeUsuario(usuario);
			alertaDAO.salvar(alerta);
			if(!Util.isNullOuVazio(usuario.getUsuarioDispositivoCollection())){
				enviarAlertaDispositivo(usuario.getUsuarioDispositivoCollection(), titulo,alerta.getDesMensagem(),alerta.getTipoAlerta().getIdeTipoAlerta(),alerta.getIdeAlerta());
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void enviarAlertaDispositivo(Collection<UsuarioDispositivo> usuarioDispositivoCollection,String titulo, String mensagem, Integer tipoAlerta,Integer ideAlerta) {
		for(UsuarioDispositivo dispositivo : usuarioDispositivoCollection){
			if(dispositivo.getIdeTipoDispositivo().getIdeTipoDispositivo().equals(TipoDispositivoEnum.ANDROID.getId())){
				gmcNotification.EnviarAlertaAndroid(dispositivo.getCodDispositivo(), titulo, mensagem,tipoAlerta,ideAlerta);
			}else if(dispositivo.getIdeTipoDispositivo().getIdeTipoDispositivo().equals(TipoDispositivoEnum.IOS.getId())){
				gmcNotification.enviarAlertaIos(mensagem, dispositivo.getCodDispositivo(),tipoAlerta, ideAlerta);
			}
		}
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarAlerta(ControleTramitacao tramitacao){
		StringBuilder msg =new StringBuilder();
		StringBuilder titulo = new StringBuilder();
		StringBuilder numDocumento = new StringBuilder();
		TipoAlerta tipoAlerta = new TipoAlerta();
		if(verificarCriacaoAlertaUsuarioExterno(tramitacao,titulo,msg,numDocumento,tipoAlerta)){
			List<Usuario> usuarios = usuarioService.listarDispositivosEnvolvidosPorIdeProcesso(tramitacao.getIdeProcesso().getIdeProcesso());
			if(!Util.isNullOuVazio(usuarios)){
				salvarAlerta(msg.toString(),titulo.toString(),usuarios, tramitacao.getIdeProcesso().getNumProcesso(),tipoAlerta);
			}
		}
		criarAlertaUsuarioInterno(tramitacao);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isStatusRespondida(ControleTramitacao tramitacao){
		return tramitacao.getIdeStatusFluxo().equals(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void criarAlertaUsuarioInterno(ControleTramitacao tramitacao) {
		StringBuilder msg =new StringBuilder();
		StringBuilder titulo = new StringBuilder();
		List<Usuario> usuarios =  new ArrayList<Usuario>();
		TipoAlerta tipoAlerta = new TipoAlerta(TipoAlertaEnum.PAUTA.getId());
		Usuario usuarioQueNotificou = null;
		tramitacao.setIdeStatusFluxo(statusFluxoService.carregar(tramitacao.getIdeStatusFluxo().getIdeStatusFluxo()));
		/** PROCESSO GERADO || COORDENADOR APROVANDO NOTIFICACAO || NOTIFICACAO RESPONDIDA*/
		if(Util.isNullOuVazio(tramitacao.getIdePauta().getIdePessoaFisica())){
			/**Quando o requerente responder a notificação, o alerta deve ir para o Líder da Equipe.*/
			if(isStatusRespondida(tramitacao)){
				usuarios.add(usuarioService.listarDispositivoUsuarioLiderEquipeProcesso(tramitacao));
				msg.append("A notificação foi respondida e o processo de número "+tramitacao.getIdeProcesso().getNumProcesso()+" foi encaminhado para a sua pauta, com status Análise técnica.");
				titulo.append("Pauta técnica");
			} 
			else {
				Usuario usuario = usuarioService.listarDispositivosResponsavelArea(tramitacao.getIdeArea().getIdeArea());
				if(!Util.isNullOuVazio(usuario)){
					usuarios.add(usuario);	
				}
				msg.append("O processo de número " + tramitacao.getIdeProcesso().getNumProcesso() + " foi encaminhado para a pauta da área, com o status " + tramitacao.getIdeStatusFluxo().getDscStatusFluxo() + ".");
				titulo.append("Pauta da área");
				if(!Util.isNullOuVazio(tramitacao.getIdePessoaFisica())){
					usuarioQueNotificou = new Usuario(tramitacao.getIdePessoaFisica().getIdePessoaFisica()); 
				}
			}
		
		}// PROCESSO DISTRIBUIDO || PROCESSO NOTIFICADO
		else{
			usuarios.add(usuarioService.listarDispositivosUsuarioPauta(tramitacao.getIdePauta().getIdePauta()));
			msg.append("O processo de número " + tramitacao.getIdeProcesso().getNumProcesso() + " foi encaminhado para sua pauta, com o status " + tramitacao.getIdeStatusFluxo().getDscStatusFluxo() + ".");
			titulo.append("Pauta");
			if(!Util.isNullOuVazio(tramitacao.getIdePessoaFisica())){
				usuarioQueNotificou = new Usuario(tramitacao.getIdePessoaFisica().getIdePessoaFisica());	
			}
		}
		if(!Util.isNullOuVazio(usuarios)){
			usuarios.remove(usuarioQueNotificou);
			salvarAlerta(msg.toString(),titulo.toString(),usuarios, tramitacao.getIdeProcesso().getNumProcesso(),tipoAlerta);	
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarCriacaoAlertaUsuarioExterno(ControleTramitacao tramitacao,StringBuilder titulo, StringBuilder msg,StringBuilder numDocumento, TipoAlerta tipoAlerta)  {
		if(tramitacao.getIdeStatusFluxo().isAnalise() && tramitacao.getIndResponsavel()){
			msg.append("O processo de número " + tramitacao.getIdeProcesso().getNumProcesso() + " está em análise técnica.");
			titulo.append("Análise técnica");
			tipoAlerta.setIdeTipoAlerta(TipoAlertaEnum.PROCESSO.getId());
			return true;
		}
		if(tramitacao.getIdeStatusFluxo().isConcluido()){
			msg.append("O processo de número " + tramitacao.getIdeProcesso().getNumProcesso() + " foi concluído.");
			titulo.append("concluído");
			tipoAlerta.setIdeTipoAlerta(TipoAlertaEnum.PROCESSO.getId());
			return true;
		}
		if(tramitacao.getIdeStatusFluxo().isArquivado()){
			msg.append("O processo de número " + tramitacao.getIdeProcesso().getNumProcesso() + " foi arquivado.");
			titulo.append("Arquivado");
			tipoAlerta.setIdeTipoAlerta(TipoAlertaEnum.PROCESSO.getId());
			return true;
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarAlerta(ControleTramitacao tramitacao,	Notificacao notificacaoFinal) {
		if(tramitacao.getIdeStatusFluxo().isNotificacaoExpedida()){
			try {
				String msg = "Notificação de número " + notificacaoFinal.getNumNotificacao() + " foi expedida";
				String titulo = "Notificação Expedida";
				List<Usuario> usuarios = usuarioService.listarDispositivosEnvolvidosPorIdeProcesso(tramitacao.getIdeProcesso().getIdeProcesso());
				Usuario usuarioQueEmitiuNotificacao = usuarioService.buscarUsuarioPorPessoaFisica(tramitacao.getIdePessoaFisica());
				if(usuarios.contains(usuarioQueEmitiuNotificacao)){
					usuarios.remove(usuarioQueEmitiuNotificacao);	
				}
				salvarAlerta(msg,titulo,usuarios, notificacaoFinal.getNumNotificacao(), new TipoAlerta(TipoAlertaEnum.NOTIFICACAO.getId()));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
	}
	/**
	 * Método para buscar os alertas do usuário por tipo, os lidos e o total
	 * @param ideUsuario ide do usuário
	 * @param ideTipoAlerta tipo do alerto a ser buscado
	 * @param naoLido indicador se true busca msg não lidas, se false busca todos.
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(Integer ideUsuario, Integer ideTipoAlerta, boolean naoLido) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Alerta.class);
		criteria.add(Restrictions.eq("ideUsuario.idePessoaFisica",ideUsuario));
		criteria.add(Restrictions.eq("tipoAlerta.ideTipoAlerta",ideTipoAlerta));
		if(naoLido){
			criteria.add(Restrictions.eq("indLida",false));
		}
		return alertaDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Alerta> buscarAlertasUsuarioPorTipo(Integer ideUsuario, Integer ideTipoAlerta, Integer first, Integer max) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct a from Alerta a ");
		lSql.append("INNER JOIN  a.tipoAlerta ");
		lSql.append("WHERE a.ideUsuario.idePessoaFisica = :ideUsuario ");
		lSql.append("AND a.tipoAlerta.ideTipoAlerta = :ideTipoAlerta ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lSql.append(" order by a.dtcEnvio desc ");
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		lQuery.setMaxResults(max);
		lQuery.setFirstResult(first);
		
		if (!Util.isNullOuVazio(ideUsuario)) {
			lQuery.setParameter("ideUsuario", ideUsuario);
		}
		
		if(!Util.isNullOuVazio(ideTipoAlerta)){
			lQuery.setParameter("ideTipoAlerta", ideTipoAlerta);
		}
		
		@SuppressWarnings("unchecked")
		List<Alerta> listAlerta = lQuery.getResultList();
		return listAlerta;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Alerta carregarPorIde(Integer ideAlerta){
		return alertaDAO.carregarGet(ideAlerta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void marcarAlertaLido(Integer ideAlerta){
		Alerta alerta = alertaDAO.carregarGet(ideAlerta);
		alerta.setIndLida(true);
		alerta.setDtcLeitura(new Date());
	    alertaDAO.atualizar(alerta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarAlertaPrazoNotificacao() {
		try {
			List<Notificacao> notificacoes = notificacaoDAOImpl.buscarNotificacaoPorPrazo(QTD_DIAS_ALERTA_NOTIFICACAO);
			for(Notificacao notificacao : notificacoes){
				if(!Util.isNullOuVazio(notificacao.getNumNotificacao())){
					String msg = "Faltam " + QTD_DIAS_ALERTA_NOTIFICACAO +  " dias para o prazo fim do prazo da Notificação de número " + notificacao.getNumNotificacao() + ".";
					String titulo = "Prazo notificação";
					List<Usuario> usuarios = usuarioService.listarDispositivosEnvolvidosPorIdeProcesso(notificacao.getIdeProcesso().getIdeProcesso());
					salvarAlerta(msg,titulo,usuarios, notificacao.getNumNotificacao(), new TipoAlerta(TipoAlertaEnum.NOTIFICACAO.getId()));
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAlertas(String ids){
		StringBuilder lSql = new StringBuilder();
		lSql.append("delete from Alerta a ");
		lSql.append("WHERE a.ide_alerta in " + "("+ids +")");
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString(), Alerta.class);
	
		lQuery.executeUpdate();
	}
}
