package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.model.DataModel;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.controller.NovoRequerimentoController;
import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NovoRequerimentoDAOIf;
import br.gov.ba.seia.dao.TipologiaGrupoDAOImpl;
import br.gov.ba.seia.dao.TramitacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoTipologiaNR;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.TipoProrrogacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.RequerimentoWS;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NovoRequerimentoService {

	@Inject
	private IDAO<TipoProrrogacao> tipoProrrogacaoDAO;
	@Inject
	private IDAO<ProcessoTramite> processoTramiteDAO;
	@Inject
	private IDAO<RequerimentoTipologiaNR> requerimentoTipologiaDAO;
	@Inject
	private TipologiaGrupoDAOImpl tipologiaGrupoDAOImpl;
	@Inject
	private IDAO<Requerimento> requerimentoDAO;
	@Inject
	private NovoRequerimentoDAOIf novoRequerimentoDAOIf;
	@Inject
	private TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoProcessoDAOImpl;
	@Inject
	private IDAO<RequerimentoUnico> requerimentoUnicoDAO;
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	
	@EJB
	private PorteService porteService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	@EJB
	private TramitacaoProcessoService tramitacaoProcessoService;
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	@EJB
	private EmailService emailService;
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoServiceFacade;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoTramite> getListProcessosINEMA(Requerimento requerimento) throws Exception {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoTramite.class);
			
			if(!Util.isNullOuVazio(requerimento) && !Util.isNullOuVazio(requerimento.getIdeRequerimento())) {
				criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
				
			} else {
				criteria.add(Restrictions.isNotNull("ideRequerimento.ideRequerimento"));
			}
			
			criteria.setFetchMode("ideSistema", FetchMode.JOIN);
			
			return processoTramiteDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método responsável por finalizar a edição do formulário do requerimento em um processo de reenquadramento.
	 * Esse método tramita o processo original, processo reenquadramento e notifica o requerente com email.
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarEdicaoParaReenquadramento(RequerimentoDTO requerimentoDTO) throws Exception {
		
		tramitacaoProcessoService.tramitarProcessoAtualizandoAnterior(requerimentoDTO.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcesso().getId(), StatusFluxoEnum.EDITADO_AGUARDANDO_REENQUADRAMENTO.getStatus(), AreaEnum.DIRRE.getId());
		tramitacaoReenquadramentoProcessoServiceFacade
			.criarTramitacaoReenquadramentoProcesso(requerimentoDTO.getProcessoReenquadramentoDTO().getProcessoReenquadramento(),
					new StatusReenquadramento(StatusReenquadramentoEnum.AGUARDANDO_REENQUADRAMENTO),ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		
		String mensagem = "O Requerimento foi editado com sucesso - Aguarde ao reenquadramento do processo!";
		ContextoUtil.getContexto().setObject(mensagem);
		
		notificarRequerenteDoReenquadramento(requerimentoDTO);

 
	}
	
	/**
	 * Método responsável por notificar o requerente após finalizar a edição do requerimento 
	 * que está em processo de reenquadramento
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void notificarRequerenteDoReenquadramento(RequerimentoDTO requerimentoDTO) throws Exception {
		StringBuilder texto = new StringBuilder();
		
		texto.append("Prezado(a),\n\n");
		texto.append("O requerimentodo processo nº "+ requerimentoDTO.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getNumProcesso()+ 
				     " foi editado para fins de reenquadramento. \n\n ");
		
		texto.append("Após análise pela nossa equipe serão enviadas novas orientações.\n\n");
		texto.append("Atenciosamente, \n");
		texto.append("Central de Atendimento/INEMA ");		

		String assunto = "[SEIA] - Requerimento Editado";
		ComunicacaoReenquadramentoProcesso prp = new ComunicacaoReenquadramentoProcesso();
		prp.setDtcComunicacao(new Date());
		prp.setIdeProcessoReenquadramento(requerimentoDTO.getProcessoReenquadramentoDTO().getProcessoReenquadramento());
		prp.setDesMensagem(texto.toString());
		comunicacaoReenquadramentoProcessoServiceFacade.salvarComunicacaoReenquadramentoProcesso(prp);
		
		emailService.enviarEmailsAoRequerente(requerimentoDTO.getRequerimento(), assunto, texto.toString());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoTramite buscarProcessoTramiteByNumProcesso(String numProcesso, Requerimento requerimento)
			 {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoTramite.class);
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("numProcesso", numProcesso));
		criteria.setFetchMode("ideSistema", FetchMode.JOIN);
		return processoTramiteDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoProrrogacao> carregaListaTipoProrrogacao(TipoProrrogacao listTipoProrrogacaoSelecionado) {
		return tipoProrrogacaoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarProcessoTramite(ProcessoTramite processoTramite) {
		processoTramiteDAO.salvarOuAtualizar(processoTramite);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerProcessoTramite(ProcessoTramite processoTramite)  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideProcessoTramite", processoTramite.getIdeProcessoTramite());
		processoTramiteDAO.executarNamedQuery("ProcessoTramite.deleteByIde", parametros);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RequerimentoTipologiaNR> listarRequerimentoTipologiaPorRequerimento(Requerimento requerimento) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimento());
		Collection<RequerimentoTipologiaNR> collRequerimentoTipologia = requerimentoTipologiaDAO.buscarPorNamedQuery(
				"RequerimentoTipologiaNR.findByIdeRequerimento", parametros);
		for (RequerimentoTipologiaNR reqTip : collRequerimentoTipologia) {
			reqTip.setPreenchidoUsuario(Boolean.TRUE);
			String valorAtividade = reqTip.getValAtividade().toString();
			if (valorAtividade.substring(valorAtividade.length() - 2, valorAtividade.length()).contains(".")) {
				valorAtividade = reqTip.getValAtividade().toString().replace(".", ",") + "0";
			} else {
				valorAtividade = reqTip.getValAtividade().toString().replace(".", ",");
			}
			reqTip.setValAtividadeString(valorAtividade);
		}
		return collRequerimentoTipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte verificarPorte(DataModel<RequerimentoTipologiaNR> requerimentoTipologia) throws Exception {
		int porteMaior = 0;
		Porte porte = null;
		Collection<Porte> collPorte = new ArrayList<Porte>();
		for (RequerimentoTipologiaNR requerimentoTipologiaObject : requerimentoTipologia) {
			Integer tipologiaGrupo = requerimentoTipologiaObject.getIdeUnidadeMedidaTipologiaGrupo()
					.getIdeTipologiaGrupo().getIdeTipologiaGrupo();
			BigDecimal valor = BigDecimal.valueOf(requerimentoTipologiaObject.getValAtividade());
			Porte p = porteService.calcularValorPorte(tipologiaGrupo, valor);
			if (p == null) {
				Porte po = new Porte();
				po.setIdePorte(6);
				requerimentoTipologiaObject.setPorte(po);
			} else {
				requerimentoTipologiaObject.setPorte(p);
			}
			collPorte.add(p);
		}
		for (Porte po : collPorte) {
			int valorPorte = 0;
			if (po != null) {
				if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("mi")) {
					valorPorte = 1;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("pe")) {
					valorPorte = 2;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("me")) {
					valorPorte = 3;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("gr")) {
					valorPorte = 4;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("ex")) {
					valorPorte = 5;
				}
			}
			if (valorPorte > porteMaior) {
				porteMaior = valorPorte;
				porte = po;
			}
		}
		if (porte == null) {
			Map<String, Object> parametros = new TreeMap<String, Object>();
			parametros.put("idePorte", 6);
			porte = porteService.buscarPorte(parametros);
			if (!Util.isNull(porte))
				porte.setNomPorte("Porte dispensado de licenciamento Ambiental. Continue com o requerimento para verificar a necessidade de outros atos.");
		}
		return porte;
	}

	public Collection<RequerimentoTipologiaNR> listarRequerimentoTipologia(Empreendimento empreendimento) {
		Collection<TipologiaGrupo> listaTipologiaGrupo = tipologiaGrupoDAOImpl.buscarPorEmpreendimento(empreendimento
				.getIdeEmpreendimento());

		Collection<RequerimentoTipologiaNR> collRequerimentoTipologia = new ArrayList<RequerimentoTipologiaNR>();
		for (TipologiaGrupo tipologiaGrupo : listaTipologiaGrupo) {
			RequerimentoTipologiaNR requerimentoTipologia = new RequerimentoTipologiaNR();
			if (Util.isNullOuVazio(tipologiaGrupo.getUnidadeMedidaTipologiaGrupo())) {
				tipologiaGrupo.setUnidadeMedidaTipologiaGrupo(new UnidadeMedidaTipologiaGrupo());
			}
			tipologiaGrupo.getUnidadeMedidaTipologiaGrupo().setIdeTipologiaGrupo(tipologiaGrupo);
			requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(tipologiaGrupo.getUnidadeMedidaTipologiaGrupo());
			collRequerimentoTipologia.add(requerimentoTipologia);
		}
		return collRequerimentoTipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Requerimento carregarRequerimentoIncompleto(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req")
				.createAlias("req.empreendimentoRequerimentoCollection", "reqEmp")
				.createAlias("reqEmp.ideEmpreendimento", "emp")
				.add(Restrictions.eq("emp.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.isNull("req.numRequerimento"));
		return requerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerRequerimentosIncompletos(Integer ideEmpreendimento) {
		try {
			DAOFactory.getEntityManager().createNativeQuery("select removerNovoRequerimentosSemNumero(:id);").setParameter("id", ideEmpreendimento).getSingleResult();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR,"Erro ao remover requerimentos incompletos.");
		}
	}

	public Boolean verificaRequerimentoEmpreendimentoExistente(Empreendimento empreendimento) throws Exception {
		return novoRequerimentoDAOIf.verificaRequerimentoEmpreendimentoExistente(empreendimento);
	}

	public void inserirRequerimento(Requerimento requerimento) throws Exception {
		Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
		Collection<TramitacaoRequerimento> collTramitacaoRequerimento = requerimento.getTramitacaoRequerimentoCollection();
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimentoService.inserirRequerimento(requerimento);
		requerimentoUnicoService.salvarTramitacaoRequerimento(requerimento, collTramitacaoRequerimento);
		requerimentoUnicoService.salvarPessoasRequerimento(requerimento, collRequerimentoPessoa);
	}
	
	public void atualizarEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento)  {
		requerimentoService.atualizarEmpreendRequerimento(empreendimentoRequerimento);
	}

	public List<RequerimentoDTO> consultaRequerimento(Map<String, Object> params) throws Exception {
		return novoRequerimentoDAOIf.listarRequerimentoUnico(params);
	}

	public Integer countRequerimento(Map<String, Object> params) throws Exception {
		return novoRequerimentoDAOIf.countListarRequerimento(params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoUnico obterRequerimentoUnico(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoUnico.class);
		criteria.add(Restrictions.eq("ideRequerimentoUnico", requerimento.getIdeRequerimento()));
		criteria.setFetchMode("ideRequerimentoUnico", FetchMode.JOIN);
		return requerimentoUnicoDAO.buscarPorCriteria(criteria);
	}

	public boolean verificarExistenciaARLS(RequerimentoPessoa requerente) throws Exception {
		return !Util.isNullOuVazio(this.solicitacaoAdministrativoService.buscarARLS(requerente));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RequerimentoWS> consultaRequerimentoWs(Integer requerente, Integer ideEmpreendimento, String numRequerimento, Integer status, Date dtcInicial,Date dtcFinal, Integer first, Integer max, List<Integer> idesPessoas) throws Exception {
		return novoRequerimentoDAOIf.consultaRequerimentoWs(requerente, ideEmpreendimento, numRequerimento, status,dtcInicial,dtcFinal, first,max, idesPessoas);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long countConsultaRequerimentoWs(Integer requerente, Integer ideEmpreendimento, String numRequerimento, Integer status, Date dtcInicial,Date dtcFinal, List<Integer> idesPessoas) {
		return novoRequerimentoDAOIf.countConsultaRequerimentoWs(requerente, ideEmpreendimento, numRequerimento, status,dtcInicial,dtcFinal, idesPessoas);
	}
	
	public ControleProcessoAto buscarControleProcessoAtoAtualPorProcessoAto(Integer ideProcessoAto){
		try {
			return controleProcessoAtoDAOImpl.buscarUltimoPorProcessoAto(ideProcessoAto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public TramitacaoReenquadramentoProcessoDAOImpl getTramitacaoReenquadramentoProcessoDAOImpl() {
		return tramitacaoReenquadramentoProcessoDAOImpl;
	}

	public void setTramitacaoReenquadramentoProcessoDAOImpl(TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoProcessoDAOImpl) {
		this.tramitacaoReenquadramentoProcessoDAOImpl = tramitacaoReenquadramentoProcessoDAOImpl;
	}
	
	public StringBuilder gerarConsultaRelatorioRequerimento(NovoRequerimentoController params) {
		
	    StringBuilder sql = new StringBuilder();
	    
	    sql.append("SELECT DISTINCT r.ide_requerimento AS ideRequerimento, ")
	       .append("r.num_requerimento AS numeroRequerimento, ")
	       .append("TO_CHAR(r.dtc_finalizacao, 'dd/mm/yyyy') AS dataFinalizacao, ")
	       .append("CASE WHEN p.ide_pessoa = pf.ide_pessoa_fisica THEN pf.nom_pessoa ")
	       .append("ELSE pj.nom_razao_social END AS requerente, ")
	       .append("e.nom_empreendimento AS empreendimento, ")
	       .append("sr.nom_status_requerimento AS statusRequerimento ")
	       .append("FROM requerimento r ")
	       
	       .append("LEFT JOIN requerimento_pessoa rp ON (r.ide_requerimento = rp.ide_requerimento) ")
	       .append("LEFT JOIN pessoa p ON (rp.ide_pessoa = p.ide_pessoa) ")
	       .append("LEFT JOIN pessoa_fisica pf ON (p.ide_pessoa = pf.ide_pessoa_fisica) ")
	       .append("LEFT JOIN pessoa_juridica pj ON (p.ide_pessoa = pj.ide_pessoa_juridica) ")
	       .append("LEFT JOIN empreendimento_requerimento er ON (r.ide_requerimento = er.ide_requerimento) ")
	       .append("LEFT JOIN empreendimento e ON (er.ide_empreendimento = e.ide_empreendimento) ")
	       .append("LEFT JOIN porte p2 ON (er.ide_porte = p2.ide_porte) ")
	       
	       .append("LEFT JOIN tramitacao_requerimento tr ON (r.ide_requerimento = tr.ide_requerimento ")
	       .append("AND tr.ide_tramitacao_requerimento = (SELECT max(tr2.ide_tramitacao_requerimento) ")
	       .append("FROM tramitacao_requerimento tr2 WHERE tr.ide_requerimento = tr2.ide_requerimento)) ")
	       .append("LEFT JOIN status_requerimento sr ON (tr.ide_status_requerimento = sr.ide_status_requerimento) ")
	       
	       .append("LEFT JOIN enquadramento e2 ON (r.ide_requerimento = e2.ide_requerimento) ")
	       .append("LEFT JOIN enquadramento_ato_ambiental eaa ON (e2.ide_enquadramento = eaa.ide_enquadramento) ")
	       .append("LEFT JOIN tipologia tAto ON (eaa.ide_tipologia = tAto.ide_tipologia) ")
	       .append("LEFT JOIN ato_ambiental aa ON (eaa.ide_ato_ambiental = aa.ide_ato_ambiental) ")
	       .append("LEFT JOIN tipo_ato ta ON (aa.ide_tipo_ato = ta.ide_tipo_ato) ")
	       .append("LEFT JOIN requerimento_tipologia rt ON ")
	       .append("(r.ide_requerimento = rt.ide_requerimento) ")
	       .append("LEFT JOIN unidade_medida_tipologia_grupo umtg ON ")
	       .append("(rt.ide_unidade_medida_tipologia_grupo = umtg.ide_unidade_medida_tipologia_grupo) ")
	       .append("LEFT JOIN tipologia_grupo tg ON ")
	       .append("(umtg.ide_tipologia_grupo = tg.ide_tipologia_grupo) ")
	       .append("LEFT JOIN tipologia tReq ON ")
	       .append("(tg.ide_tipologia = tReq.ide_tipologia) ")
	       .append("LEFT JOIN enquadramento_finalidade_uso_agua efua ON (eaa.ide_enquadramento_ato_ambiental = efua.ide_enquadramento_ato_ambiental) ")
	       .append("LEFT JOIN tipo_finalidade_uso_agua tfua ON (efua.ide_tipo_finalidade_uso_agua = tfua.ide_tipo_finalidade_uso_agua) ")
	       .append("LEFT JOIN cumprimento_reposicao_florestal crf ON (r.ide_requerimento = crf.ide_requerimento) ")
	       .append("LEFT JOIN pagamento_reposicao_florestal prf ON (crf.ide_pagamento_reposicao_florestal = prf.ide_pagamento_reposicao_florestal) ")
	       
	       .append("LEFT JOIN endereco_empreendimento ee ON (e.ide_empreendimento = ee.ide_empreendimento AND ee.ide_tipo_endereco = 4) ")
	       .append("LEFT JOIN endereco e3 ON (ee.ide_endereco = e3.ide_endereco) ")
	       .append("LEFT JOIN logradouro l ON (e3.ide_logradouro = l.ide_logradouro) ")
	       .append("LEFT JOIN bairro b ON (l.ide_bairro = b.ide_bairro)")
	       .append("LEFT JOIN municipio m ON (l.ide_municipio = m.ide_municipio OR b.ide_municipio = m.ide_municipio) ")
	       .append("LEFT JOIN area_municipio am ON (m.ide_municipio = am.ide_municipio)")
	       
	       .append("WHERE r.ind_excluido = FALSE AND rp.ide_tipo_pessoa_requerimento = 1");
	    
	    if (!Util.isNullOuVazio(params.getRequerente())) {
	        sql.append("AND p.ide_pessoa = ").append(params.getRequerente()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getEmpreendimentoBusca())) {
	        sql.append("AND e.ide_empreendimento = ").append(params.getEmpreendimentoBusca()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getIdePorte())) {
	        sql.append("AND p2.ide_porte = ").append(params.getIdePorte().getId()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getNumeroRequerimento())) {
	        sql.append("AND r.num_requerimento ILIKE '%").append(params.getNumeroRequerimento()).append("%' ");
	    }
	    if (!Util.isNullOuVazio(params.getStatusRequerimento())) {
	        sql.append("AND sr.ide_status_requerimento = ").append(params.getStatusRequerimento()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getPeriodoInicio()) && !Util.isNullOuVazio(params.getPeriodoFim())) {
	        sql.append("AND r.dtc_finalizacao BETWEEN '").append(params.getPeriodoInicio()).append("' AND '").append(params.getPeriodoFim()).append("' ");
	    }
	    if (!Util.isNullOuVazio(params.getTipoAto())) {
	        sql.append("AND ta.ide_tipo_ato = ").append(params.getTipoAto().getIdeTipoAto()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getAtoAmbiental())) {
	        sql.append("AND aa.ide_ato_ambiental = ").append(params.getAtoAmbiental().getIdeAtoAmbiental()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getTipologia())) {
	        sql.append("AND tAto.ide_tipologia = ").append(params.getTipologia().getIdeTipologia()).append(" ")
	           .append("AND tAto.ind_excluido = FALSE ");
	    }
	    if (!Util.isNullOuVazio(params.getFinalidadeUsoAgua())) {
	        sql.append("AND tfua.ide_tipo_finalidade_uso_agua = ").append(params.getFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getPagamentoReposicaoFlorestal())) {
	    	if (params.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal().equals(1)) {
		        sql.append("AND prf.ide_pagamento_reposicao_florestal IN (21, 22, 23)");
	    	} else {
	    		sql.append("AND prf.ide_pagamento_reposicao_florestal = ").append(params.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal()).append(" ");
	    	}
	    }
	    if (!Util.isNullOuVazio(params.getTipologiaAtividade())) {
	        sql.append("AND tReq.ide_tipologia = ").append(params.getTipologiaAtividade().getIdeTipologia()).append(" ")
        		.append("AND tg.ind_excluido = FALSE ")
        		.append("AND tReq.ind_excluido = FALSE ");
	    }
	    if (!Util.isNullOuVazio(params.getListaTipologiaAtividade()) && !Util.isNullOuVazio(params.getTipologiaDivisao()) && Util.isNullOuVazio(params.getTipologiaAtividade())) {
	        StringBuilder listaTipologias = new StringBuilder();
	        for (Tipologia tipologia : params.getListaTipologiaAtividade()) {
	            if (listaTipologias.length() > 0) {
	                listaTipologias.append(", ");
	            }
	            listaTipologias.append(tipologia.getIdeTipologia());
	        }
	        sql.append("AND tReq.ide_tipologia IN (").append(listaTipologias).append(") ")
	        	.append("AND tg.ind_excluido = FALSE ")
	        	.append("AND tReq.ind_excluido = FALSE ");
	    }
	    if (!Util.isNullOuVazio(params.getUrSelecionada())) {
	        sql.append("AND am.ide_area = ").append(params.getUrSelecionada()).append(" ");
	    }
	    if (!Util.isNullOuVazio(params.getMunicipioSelecionado())) {
	        sql.append("AND m.ide_municipio = ").append(params.getMunicipioSelecionado()).append(" ");
	    }
	    
	    sql.append(" ORDER BY numeroRequerimento DESC");
	    
	    return sql;
	}
	
}