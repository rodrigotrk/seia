package br.gov.ba.seia.service;

import static br.gov.ba.seia.util.fce.FceUtil.isFceAbastecimentoHumano;
import static br.gov.ba.seia.util.fce.FceUtil.isFceAsv;
import static br.gov.ba.seia.util.fce.FceUtil.isFceAutorizacaoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceBarragem;
import static br.gov.ba.seia.util.fce.FceUtil.isFceCanais;
import static br.gov.ba.seia.util.fce.FceUtil.isFceGeracaoEnergia;
import static br.gov.ba.seia.util.fce.FceUtil.isFceIndustria;
import static br.gov.ba.seia.util.fce.FceUtil.isFceInfraestrutura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceIntervencaoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLicenciamentoAquicultura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLicenciamentoMineracao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceLinhaEnergiaTrasmissaoEnergia;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaAbastecimentoIndustrial;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaAquicultura;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaCaptacaoSubterranea;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaCaptacaoSuperficial;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaDessedentacaoAnimal;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaIrrigacao;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaLancamentoEfluentes;
import static br.gov.ba.seia.util.fce.FceUtil.isFceOutorgaPulverizacao;
import static br.gov.ba.seia.util.fce.FceUtil.isFcePerfuracaoPoco;
import static br.gov.ba.seia.util.fce.FceUtil.isFceSistemaAbastecimentoAgua;
import static br.gov.ba.seia.util.fce.FceUtil.isFceTurismo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.AtoDeclaratorioDAOImpl;
import br.gov.ba.seia.dao.EnquadramentoAtoAmbientalDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;
/***
 * Classe Serviço reponsável por apoiar o controller de AlterarStatusReenquadramento 
 * na alteração do status 
 * @author diegoraian
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusReenquadramentoService {
	
	@Inject
	private
	IDAO<StatusReenquadramento> daoStatusReenquadramento;
	
	@Inject
	private
	IDAO<ReenquadramentoProcesso> reenquadramentoDAO;
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	
	@EJB
	private FceService fceService;
	
	@EJB
	private LacService lacService;

	@EJB
	private TramitacaoProcessoService tramitacaoProcessoService;

	@EJB
	private AtoDeclaratorioDAOImpl atoDeclaratorioDAO;
	
	@EJB
	private EnquadramentoAtoAmbientalDAOImpl enquadramentoAtoAmbientalDAOImpl;
	
	@EJB
	protected AtoAmbientalService atoAmbientalService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	/**
	 * 
	 * @param listaStatus
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<StatusReenquadramento> listarStatusRequerimentoQueRealizamAlteracaoDeStatus(List<Long> listaStatus) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusReenquadramento.class);
		criteria
			.setProjection(Projections.projectionList()
					.add(Projections.alias(Projections.property("nomStatusReenquadramento"),"nomStatusReenquadramento"))
					.add(Projections.alias(Projections.property("ideStatusReenquadramento"),"ideStatusReenquadramento")))
			.add(Restrictions.in("ideStatusReenquadramento", listaStatus))			
			.add(Restrictions.eq("indAtivo", true))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusReenquadramento.class));
		return getDaoStatusReenquadramento().listarPorCriteria(criteria);
	}
	
	/**
	 * Valida se o status é um status do tipo Aguardando reenquadramento
	 * @author diegoraian
	 * @param status
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private boolean isStatusAguardandoEdicaoDoReenquadramento(StatusReenquadramentoEnum status) {
		return StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO.getId().equals(status.getId());
	}
	
	
	/**
	 * Funcão principal que realiza a alteração do status
	 * @author diegoraian
	 * @param processoReenquadramento
	 * @param status
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void alterarStatus(ProcessoReenquadramentoDTO processoReenquadramento, StatusReenquadramentoEnum status) throws Exception{
		if(isStatusAguardandoEdicaoDoReenquadramento(status)) {
			verificarERemoverFCE(processoReenquadramento.getProcessoReenquadramento());
		}
		String sql = "SELECT alterar_status_reenquadramento(:pReenquadramento, :idStatus)";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pReenquadramento", processoReenquadramento.getProcessoReenquadramento().getNumProcesso());
		params.put("idStatus", Integer.parseInt(status.getId().toString()));
		reenquadramentoDAO.buscarPorNativeQuery(sql, params);
		tramitarProcessos(processoReenquadramento,status);
		
		
	}
	
	/**
	 * Realiza a tramitação dos processos de acordo com o status
	 * As tramitações possíveis são para o status de "Validado" e "Aguardando reenquadramento do processo"
	 * @author diegoraian
	 * @param processoReenquadramento
	 * @param status
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarProcessos(ProcessoReenquadramentoDTO processoReenquadramento, StatusReenquadramentoEnum status) throws Exception {
		String observacao= BUNDLE.getString("reenquadramento_msg_retorno_status");
		
		StatusReenquadramento statusReenquadramento = daoStatusReenquadramento.carregarGet(StatusReenquadramentoEnum.RETORNO_DE_STATUS.getId());
		PessoaFisica buscarPessoa = pessoaFisicaService.buscarPessoaFisicaByIde(SecurityService.getUser().getIdePessoaFisica());
		tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcesso(processoReenquadramento.getProcessoReenquadramento(), 
				statusReenquadramento,buscarPessoa.getPessoa() );
		

		if (isStatusAguardandoEdicaoDoReenquadramento(status)) {
			
			statusReenquadramento = daoStatusReenquadramento.carregarGet(StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO.getId());
			//Tramitação do processo de reenquadramento
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcesso(processoReenquadramento.getProcessoReenquadramento(), 
					statusReenquadramento,buscarPessoa.getPessoa() );
			//Tramitação do processo de originador
			tramitacaoProcessoService.tramitarProcessoAtualizandoAnterior(processoReenquadramento.getProcessoReenquadramento().getIdeProcesso().getId(),
					StatusFluxoEnum.AGUARDANDO_REENQUADRAMENTO_PROCESSO.getStatus(), AreaEnum.ATEND.getId(),observacao,observacao);
		} else if(isStatusValidado(status)) {
			statusReenquadramento = daoStatusReenquadramento.carregarGet(StatusReenquadramentoEnum.VALIDADO.getId());
			//Tramitação do processo de reenquadramento			
			tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcesso(processoReenquadramento.getProcessoReenquadramento(), 
					statusReenquadramento,buscarPessoa.getPessoa() );
			//Tramitação do processo de originador
			tramitacaoProcessoService.tramitarProcessoAtualizandoAnterior(processoReenquadramento.getProcessoReenquadramento().getIdeProcesso().getId(),
					StatusFluxoEnum.REENQUADRAMENTO_EM_TRAMITE.getStatus(), AreaEnum.ATEND.getId(),observacao,observacao);
		}
	}
	/**
	 * Verifica se o status selecionado é o  Validado
	 * @param status
	 * @return
	 */
	
	private boolean isStatusValidado(StatusReenquadramentoEnum status) {
		return StatusReenquadramentoEnum.VALIDADO.getId().equals(status.getId());
	}

	/**
	 * Verifica os dados das FCE's que estão cadastrados e remove
	 * @author diegoraian
	 * @param reenquadramento
	 * @throws Exception
	 */
	private void verificarERemoverFCE(ProcessoReenquadramento reenquadramento) throws Exception {

		List<Object> listaFull = new ArrayList<Object>();
		Integer ideProcesso = reenquadramento.getIdeProcesso().getIdeProcesso();
		listaFull.addAll(fceService.listarFcePorProcessoReenquadramento(ideProcesso));


		if(!Util.isNullOuVazio(listaFull)){
			for(Object obj : listaFull){
				removerPorTipo(obj, reenquadramento);
			}
		} else {
			Collection<AtoAmbiental> atosAmbientais = atoAmbientalService.listarAtosEnquadradosByReenquadramento(reenquadramento);
			if(!Util.isNull(atosAmbientais)) {
				for (AtoAmbiental atoAmbiental : atosAmbientais) {
					removerPorTipo(atoAmbiental, reenquadramento);
				}
			}
		}
	}
	
	/**
	 * Retorna a função de acordo com o FCE e chama a função que apaga
	 * @param obj
	 * @param reenquadramento
	 * @throws Exception
	 */
	private void removerPorTipo(Object obj, ProcessoReenquadramento reenquadramento) throws Exception {
		String retorno = retornaFunctionTo(obj);
		if (!Util.isNullOuVazio(retorno)) {
			apagarByFunctionAndReenquadramento(retorno, reenquadramento);
		}
		
	}

	/**
	 * Realiza a chamada da função passando como parâmetro o número do processo reenquadramento 
	 * @author diegoraian  
	 * @param sql
	 * @param reenquadramento
	 * @throws Exception
	 */
	private void apagarByFunctionAndReenquadramento(String sql, ProcessoReenquadramento reenquadramento) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pReenquadramento", reenquadramento.getNumProcesso());
		daoStatusReenquadramento.buscarPorNativeQuery(sql, params);
	}

	/***
	 * Retorna a função de remoção do para um respéctivo FCE
	 * @author diegoraian
	 * @param object
	 * @return
	 */
	private String retornaFunctionTo(Object object) {
		if(object instanceof Fce){
			Fce fce = (Fce) object;
			if (isFceAsv(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_asv_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceBarragem(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_intervencao_barragem_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaDessedentacaoAnimal(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_dessedentacao_animal_by_reenquadramento(:pReenquadramento)";//feito
			}
			else if (isFceOutorgaCaptacaoSuperficial(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_cap_superficial_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaCaptacaoSubterranea(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_cap_subterranea_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaPulverizacao(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_pulverizacao_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFcePerfuracaoPoco(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_loc_geo_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaIrrigacao(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_irrigacao_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaLancamentoEfluentes(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_lancamento_efluente_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceLicenciamentoAquicultura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_licenciamento_aquicultura_by_reenquadramento(:pReenquadramento)";  //feito
			}
			else if (isFceIndustria(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_industria_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceOutorgaAquicultura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_outorga_aquicultura_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceTurismo(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_turismo_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if(isFceOutorgaAbastecimentoIndustrial(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_abastecimento_industrial_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if(isFceAbastecimentoHumano(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_abastecimento_humano_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if(isFceLicenciamentoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_licenciamento_mineracao_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if(isFceSistemaAbastecimentoAgua(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_saa_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if(isFceAutorizacaoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_autorizacao_mineracao_by_reenquadramento(:pReenquadramento)"; //FEITO
			}
			else if(isFceInfraestrutura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_infraestrutura_por_reenquadramento(:pReenquadramento)"; //FEITO
			}
			else if(isFceCanais(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_canais_by_reenquadramento(:pReenquadramento)";
			}
			else if(isFceIntervencaoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_intervencao_mineracao_by_reenquadramento(:pReenquadramento)";  //feito
			}
			else if (isFceLinhaEnergiaTrasmissaoEnergia(fce.getIdeDocumentoObrigatorio()))
			{
				return "SELECT remover_fce_linha_energia_by_reenquadramento(:pReenquadramento)"; //feito
			}
			else if (isFceGeracaoEnergia(fce.getIdeDocumentoObrigatorio())) 
			{
				return "SELECT remover_fce_geracao_energia_by_reenquadramento(:pReenquadramento)";
			}

		}
		return "";
	}


	public IDAO<StatusReenquadramento> getDaoStatusReenquadramento() {
		return daoStatusReenquadramento;
	}

	public void setDaoStatusReenquadramento(IDAO<StatusReenquadramento> daoStatusReenquadramento) {
		this.daoStatusReenquadramento = daoStatusReenquadramento;
	}



}
