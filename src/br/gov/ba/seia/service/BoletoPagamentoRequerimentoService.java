package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.BoletoPagamentoRequerimentoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoBoletoPagamento;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoPagamentoRequerimentoService {

	@Inject
	private IDAO<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoDAO;	
	@Inject
	private IDAO<DetalhamentoBoleto> detalhamentoDAO;
	@Inject
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@Inject
	private ComunicacaoProcessoService comunicacaoProcessoService;
	@Inject
	private EmailUtil sendEmail;
	@Inject
	private BoletoPagamentoRequerimentoDAOImpl boletoPagamentoRequerimentoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoPagamentoRequerimento consultar(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento) {
		return boletoPagamentoRequerimentoDAO.buscarEntidadePorExemplo(pBoletoPagamentoRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> carregarByRequerimento(Integer ideRequerimento, Boolean isBoletoComplementar) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req");
		detachedCriteria.createAlias("ideTipoBoletoPagamento", "tipoBoletoPagamento");
		
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
				.add(Projections.property("valBoleto"),"valBoleto")
				.add(Projections.property("valBoletoOutorga"),"valBoletoOutorga")
				.add(Projections.property("dtcEmissao"),"dtcEmissao")
				.add(Projections.property("dtcVencimento"),"dtcVencimento")
				.add(Projections.property("numBoleto"),"numBoleto")
				.add(Projections.property("indBoletoGeradoManualmente"),"indBoletoGeradoManualmente")
				.add(Projections.property("desCaminhoBoleto"),"desCaminhoBoleto")
				.add(Projections.property("indBoletoRegistrado"),"indBoletoRegistrado")
				.add(Projections.property("indIsento"),"indIsento")
				.add(Projections.property("tipoBoletoPagamento.ideTipoBoletoPagamento"),"ideTipoBoletoPagamento.ideTipoBoletoPagamento")
		);
		
		detachedCriteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		if(isBoletoComplementar) {
			detachedCriteria.add(Restrictions.ne("tipoBoletoPagamento.ideTipoBoletoPagamento", 1));
		}else {
			detachedCriteria.add(Restrictions.eq("tipoBoletoPagamento.ideTipoBoletoPagamento", 1));
		}
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class));
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideBoletoPagamentoRequerimento"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoPagamentoRequerimento carregarById(Integer ideBoleto) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		
		detachedCriteria.createAlias("ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcesso", "processo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideTipoBoletoPagamento", "tipoBoletoPagamento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoPagamentoHistoricoCollection", "boletoPagamentoHistorico", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoPagamentoHistorico.ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("ideBoletoPagamentoRequerimento", ideBoleto));
		
		return boletoPagamentoRequerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DetalhamentoBoleto> carregarDetalhamentosDoBoleto(Integer ideBoleto) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DetalhamentoBoleto.class)
				.createAlias("ideBoletoPagamentoRequerimento", "boletoPagamentoRequerimento", JoinType.INNER_JOIN)
				.createAlias("ideAtoAmbiental", "atoAmbiental", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipologia","tipologia", JoinType.LEFT_OUTER_JOIN )
				.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("boletoPagamentoRequerimento.ideBoletoPagamentoRequerimento", ideBoleto))
				.add(Restrictions.or(Restrictions.eq("tipologiaGrupo.indExcluido", false), Restrictions.isNull("tipologiaGrupo.indExcluido")));
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDetalhamentoBoleto"),"ideDetalhamentoBoleto")
				.add(Projections.property("ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
				.add(Projections.property("ideBoletoDaeRequerimento"),"ideBoletoDaeRequerimento")
				.add(Projections.property("valor"),"valor")
				
				.add(Projections.property("boletoPagamentoRequerimento.valBoleto"),"ideBoletoPagamentoRequerimento.valBoleto")
				.add(Projections.property("boletoPagamentoRequerimento.valBoletoOutorga"),"ideBoletoPagamentoRequerimento.valBoletoOutorga")
				.add(Projections.property("boletoPagamentoRequerimento.dtcEmissao"),"ideBoletoPagamentoRequerimento.dtcEmissao")
				.add(Projections.property("boletoPagamentoRequerimento.dtcVencimento"),"ideBoletoPagamentoRequerimento.dtcVencimento")
				.add(Projections.property("boletoPagamentoRequerimento.numBoleto"),"ideBoletoPagamentoRequerimento.numBoleto")
				.add(Projections.property("boletoPagamentoRequerimento.indBoletoGeradoManualmente"),"ideBoletoPagamentoRequerimento.indBoletoGeradoManualmente")
				.add(Projections.property("boletoPagamentoRequerimento.desCaminhoBoleto"),"ideBoletoPagamentoRequerimento.desCaminhoBoleto")
				.add(Projections.property("boletoPagamentoRequerimento.indBoletoRegistrado"),"ideBoletoPagamentoRequerimento.indBoletoRegistrado")
				
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("atoAmbiental.ideAtoAmbiental"),"ideAtoAmbiental.ideAtoAmbiental")
		);
		
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DetalhamentoBoleto.class));
			
		return detalhamentoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDetalhamentoBoleto"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento) {
		boletoPagamentoRequerimentoDAO.salvar(pBoletoPagamentoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(BoletoPagamentoRequerimento pBoletoPagamentoRequerimento) {
		boletoPagamentoRequerimentoDAO.salvarOuAtualizar(pBoletoPagamentoRequerimento);
	}
	
	public void salvar(Collection<DetalhamentoBoleto> detalhamentoBoletoCollection) {
		this.detalhamentoDAO.salvarEmLote((List<DetalhamentoBoleto>) detalhamentoBoletoCollection);
	}
	
	public BoletoPagamentoRequerimento consultarPorId(Integer ideBoletoPagamentoRequerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("proc.ideRequerimento", "reqProc", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcessoReenquadramento", "ideProcessoReenquadramento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.add(Restrictions.eq("ideBoletoPagamentoRequerimento", ideBoletoPagamentoRequerimento));
		
		return boletoPagamentoRequerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	public BoletoPagamentoRequerimento consultarPorNumeroBoleto(String numeroBoleto) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("numBoleto", numeroBoleto);
		return boletoPagamentoRequerimentoDAO.buscarEntidadePorNamedQuery("BoletoPagamentoRequerimento.findByNumeroBoleto", parametros);
	}
	
	/**
	 * Envia um e-mail informando que o boleto de pagamento do requerimento já está disponível para pagamento.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param nome - O nome da pessoa que aparecerá no corpo do e-mail 
	 * @param requerimento - Será utilizado o número desse requerimento no corpo do e-mail
	 * @param pEmail - E-mail a ser encaminhada a mensagem
	 * @
	 */
	public void enviarEmailBoletoRequerimento(String assunto, String msg, Requerimento requerimento, List<String> listaEmailsRequerimento)  {
		
		try {
			
			sendEmail.enviarEmailHtml(null, null, listaEmailsRequerimento, assunto, msg);
			
			ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
			comunicacaoRequerimento.setAssunto(assunto);
			comunicacaoRequerimento.setDesMensagem(msg);
			comunicacaoRequerimento.setDtcComunicacao(new Date());
			comunicacaoRequerimento.setIdeRequerimento(requerimento);
			comunicacaoRequerimento.setIndEnviado(true);
			comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Envia um e-mail informando que o boleto de pagamento do processo já está disponível para pagamento.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param nome - O nome da pessoa que aparecerá no corpo do e-mail
	 * @param processo - Será utilizado o número desse processo no corpo do e-mail
	 * @param pEmail - E-mail a ser encaminhada a mensagem
	 * @
	 */
	public void enviarEmailBoletoProcesso(String assunto, String msg, Processo processo, List<String> listaEmailsProcesso)  {
		
		try {

			sendEmail.enviarEmailHtml(null, null, listaEmailsProcesso, assunto, msg);
			
			ComunicacaoProcesso comunicacaoProcesso = new ComunicacaoProcesso();
			comunicacaoProcesso.setDtcComunicacao(new Date());
			comunicacaoProcesso.setDesMensagem(msg);
			comunicacaoProcesso.setIdeProcesso(processo);
			comunicacaoProcessoService.salvar(comunicacaoProcesso);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Retorna uma lista de {@link BoletoPagamentoRequerimento} do {@link TipoBoletoPagamento} igual a 1, filtrando pelo ID do {@link Requerimento}
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param ideRequerimento - ID do {@link Requerimento} que deve ser procurado
	 * @return Lista de boletos
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> carregarListaBoletoByRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria detachedCriteria = getCriteriaBoletoByRequerimento(ideRequerimento);
		detachedCriteria.add(Restrictions.eq("ideTipoBoletoPagamento.ideTipoBoletoPagamento", 1));
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideBoletoPagamentoRequerimento"));
	}
	
	/**
	 * Retorna TRUE quando existe algum boleto complementar com status diferente de pago ou cancelado.
	 * @param ideRequerimento
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean temBoletoPendente(Integer ideRequerimento) {
		//CRITERIA PARA PEGAR O TOTAL DE BOLETOS PAGOS OU CANCELADOS
		DetachedCriteria detachedCriteria = getCriteriaBoletoByRequerimento(ideRequerimento);
		
		detachedCriteria.add(Restrictions.eq("ideTipoBoletoPagamento.ideTipoBoletoPagamento", 2));

		detachedCriteria.createAlias("boletoPagamentoHistoricoCollection", "bph");
		
		detachedCriteria.add(Restrictions.in("bph.ideStatusBoletoPagamento.ideStatusBoletoPagamento", new Integer[]{ StatusBoletoEnum.PAGO.getId(), StatusBoletoEnum.CANCELADO.getId()}));
		
		detachedCriteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoHistorico.class));
		
		Integer countBoletosPagosOuCancelados = (boletoPagamentoRequerimentoDAO.count(detachedCriteria));
		
		//CRITERIA PARA PEGAR O TOTAL DE BOLETOS DAQUELE REQUERIMENTO
		DetachedCriteria detachedCriteria2 = getCriteriaBoletoByRequerimento(ideRequerimento);
		
		detachedCriteria2.add(Restrictions.eq("ideTipoBoletoPagamento.ideTipoBoletoPagamento", 2));
		
		detachedCriteria2.createAlias("boletoPagamentoHistoricoCollection", "bph");
		
		detachedCriteria2.add(Restrictions.eq("bph.ideStatusBoletoPagamento.ideStatusBoletoPagamento", 1));

		detachedCriteria2.setProjection(Projections.projectionList().add(Projections.rowCount()));
		detachedCriteria2.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoHistorico.class));
		
		Integer countTotalDeBoletos = (boletoPagamentoRequerimentoDAO.count(detachedCriteria2));
		
		return (countTotalDeBoletos > countBoletosPagosOuCancelados); 
		
	}

	/**
	 * Retorna um criteria que busca o {@link BoletoPagamentoRequerimento} pelo ID do {@link Requerimento}.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param ideRequerimento - ID do {@link Requerimento} que deve ser procurado
	 * @return criteria
	 */
	private DetachedCriteria getCriteriaBoletoByRequerimento(Integer ideRequerimento) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req");
		detachedCriteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		return detachedCriteria;
	}
	
	/**
	 * Busca um {@link BoletoPagamentoRequerimento} pelo ID do {@link Processo}.
	 * 
	 * @param ideProcesso
	 * @return
	 * @
	 */
	public BoletoPagamentoRequerimento consultarPorIdeProcesso(Integer ideProcesso) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideProcesso", ideProcesso);
		return boletoPagamentoRequerimentoDAO.buscarEntidadePorNamedQuery("BoletoPagamentoRequerimento.findByIdeProcesso", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosPorLote(LoteBoleto loteBoleto)  {
		return boletoPagamentoRequerimentoDAOImpl.listarBoletosPorLote(loteBoleto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosPorIdeRequerimento(Integer ideRequerimento)  {
		return boletoPagamentoRequerimentoDAOImpl.listarBoletosPorIdeRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosComplementarPorIdeRequerimento(Integer ideRequerimento)  {
		return boletoPagamentoRequerimentoDAOImpl.listarBoletosComplementarPorIdeRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoPagamentoRequerimento consultarPorIdeProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("proc.ideRequerimento", "reqProc", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcessoReenquadramento", "ideProcessoReenquadramento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.add(Restrictions.eq("ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento));
		
		return boletoPagamentoRequerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarPorIdeProcesso(Integer ideProcesso) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		detachedCriteria.createAlias("ideTipoBoletoPagamento", "tipo", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("ideProcesso", "proc", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideProcessoReenquadramento", "ideProcessoReenquadramento", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("proc.ideProcesso", ideProcesso));
		detachedCriteria.add(Restrictions.eq("tipo.ideTipoBoletoPagamento", 7));
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(detachedCriteria);
	}
}