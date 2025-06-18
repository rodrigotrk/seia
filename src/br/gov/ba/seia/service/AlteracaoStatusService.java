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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AtoDeclaratorioDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AlteracaoStatusService {

	@Inject
	private IDAO<Requerimento> requerimentoDAO;
	
	@Inject
	private IDAO<TramitacaoRequerimento> tramitacaoRequerimentoDAO;
	
	@EJB
	private FceService fceService;
	
	@EJB
	private LacService lacService;

	@EJB
	private AtoDeclaratorioDAOImpl atoDeclaratorioDAO;
	
	@EJB
	protected AtoAmbientalService atoAmbientalService;
	
	private boolean isStatusAguardandoEnquadramento(StatusRequerimento status) {
		return status.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
	}
	/**
	 * Executa a function 'alterar_status_requerimento', recebendo como parâmetro o requerimento e o ID do status que deve ser setado. 
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param req
	 * @param statusRequerimento
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void alterarStatus(Requerimento req, StatusRequerimento statusRequerimento) throws Exception {
		if(isStatusAguardandoEnquadramento(statusRequerimento)) {
			verificarAndRemoverFce(req);
		}
		Pessoa idePessoa = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
		String sql = "SELECT alterar_status_requerimento(:pRequerimento, :idStatus)";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pRequerimento", req.getNumRequerimento());
		params.put("idStatus", statusRequerimento);
		
		requerimentoDAO.buscarPorNativeQuery(sql, params);
		
		Date dtcMovimentacao = new Date();
		
		TramitacaoRequerimento trRetornoStatus = new TramitacaoRequerimento();
		trRetornoStatus.setIdeRequerimento(req);
		trRetornoStatus.setIdePessoa(idePessoa);
		trRetornoStatus.setIdeStatusRequerimento(new StatusRequerimento(StatusRequerimentoEnum.RETORNO_STATUS.getStatus()));
		trRetornoStatus.setDtcMovimentacao(dtcMovimentacao);
		
		TramitacaoRequerimento trNovoStatus = new TramitacaoRequerimento();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtcMovimentacao);
		calendar.add(Calendar.SECOND, 1);
		
		trNovoStatus.setIdeRequerimento(req);
		trNovoStatus.setIdePessoa(idePessoa);
		trNovoStatus.setIdeStatusRequerimento(statusRequerimento);
		trNovoStatus.setDtcMovimentacao(calendar.getTime());
		
		tramitacaoRequerimentoDAO.salvar(trRetornoStatus);
		tramitacaoRequerimentoDAO.salvar(trNovoStatus);
		
	}
	
	/**
	 * Método que verifica se existem {@link Fce}'s preenchidos/finalizados nesse {@link Requerimento} e as remove do banco de dados.
	 * @author eduardo.fernandes
	 * @since 26/01/2015
	 * @throws Exception
	 * @see Melhoria #6516
	 */
	private void verificarAndRemoverFce(Requerimento requerimento) throws Exception {
		List<Object> listaFull = new ArrayList<Object>();
		listaFull.addAll(fceService.listarFceByIdeRequerimento(requerimento));
		listaFull.addAll(lacService.listarLacByIdeRequerimento(requerimento));
		listaFull.addAll(atoDeclaratorioDAO.listar(requerimento));

		if(!Util.isNullOuVazio(listaFull)){
			for(Object obj : listaFull){
				removerPorTipo(obj, requerimento);
			}
		} else {
			Collection<AtoAmbiental> atosAmbientais = atoAmbientalService.listarAtosEnquadradosByRequerimento(requerimento.getIdeRequerimento());
			for (AtoAmbiental atoAmbiental : atosAmbientais) {
				removerPorTipo(atoAmbiental, requerimento);
			}
		}
	}
	
	/**
	 * Método para remover o {@link Fce} ou {@link Lac} de acordo com a function invocada.
	 * @author eduardo.fernandes
	 * @since 26/01/2015
	 * @param object
	 * @throws Exception
	 * @see Melhoria #6516
	 */
	private void removerPorTipo(Object object, Requerimento requerimento) throws Exception {
		String retorno = retornaFunctionTo(object);
		if (!Util.isNullOuVazio(retorno)) {
			apagarByFunctionAndRequerimento(retorno, requerimento);
		}
	}
	
	/**
	 * Retorna a {@link String} com a function presente no banco de acordo com o tipo de {@link DocumentoObrigatorio} daquele {@link Fce} ou {@link Lac}.
	 * @author eduardo.fernandes
	 * @since 26/01/2015
	 * @param object
	 * @return function
	 * @see Melhoria #6516
	 */
	private String retornaFunctionTo(Object object) {
		if(object instanceof Fce){
			Fce fce = (Fce) object;
			if (isFceAsv(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_asv_by_requerimento(:pRequerimento)";
			}
			else if (isFceBarragem(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_intervencao_barragem_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaDessedentacaoAnimal(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_dessedentacao_animal_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaCaptacaoSuperficial(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_cap_superficial_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaCaptacaoSubterranea(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_cap_subterranea_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaPulverizacao(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_pulverizacao_by_requerimento(:pRequerimento)";
			}
			else if (isFcePerfuracaoPoco(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_loc_geo_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaIrrigacao(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_irrigacao_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaLancamentoEfluentes(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_lancamento_efluente_by_requerimento(:pRequerimento)";
			}
			else if (isFceLicenciamentoAquicultura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_licenciamento_aquicultura_by_requerimento(:pRequerimento)";
			}
			// #7012
			else if (isFceIndustria(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_industria_by_requerimento(:pRequerimento)";
			}
			else if (isFceOutorgaAquicultura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_outorga_aquicultura_by_requerimento(:pRequerimento)";
			}
			else if (isFceTurismo(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_turismo_by_requerimento(:pRequerimento)";
			}
			// #6621 
			else if(isFceOutorgaAbastecimentoIndustrial(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_abastecimento_industrial_by_requerimento(:pRequerimento)";
			}
			else if(isFceAbastecimentoHumano(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_abastecimento_humano_by_requerimento(:pRequerimento)";
			}
			else if(isFceLicenciamentoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_licenciamento_mineracao_by_requerimento(:pRequerimento)";
			}
			else if(isFceSistemaAbastecimentoAgua(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_saa_by_requerimento(:pRequerimento)";
			}
			else if(isFceAutorizacaoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_autorizacao_mineracao_by_requerimento(:pRequerimento)";
			}
			else if(isFceInfraestrutura(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_infraestrutura_por_requerimento(:pRequerimento)";
			}
			else if(isFceCanais(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_canais_by_requerimento(:pRequerimento)";
			}
			else if(isFceIntervencaoMineracao(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_intervencao_mineracao_by_requerimento(:pRequerimento)";
			}
			else if (isFceLinhaEnergiaTrasmissaoEnergia(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_linha_energia_by_requerimento(:pRequerimento)";
			}else if (isFceGeracaoEnergia(fce.getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_geracao_energia_by_requerimento(:pRequerimento)";
			}
			/**(Consultar ticket 110332
			 * 
			 * else if (isFceEPMF(fce.getIdeDocumentoObrigatorio())) {
				return "SELECT remover_fce_epmf_by_requerimento(:pRequerimento)";
			}*/
		} else if(object instanceof Lac){
			Lac lac = (Lac) object;
			if(DocumentoObrigatorioEnum.ERB.getId().equals(lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
				return "SELECT remover_lac_erb_by_requerimento(:pRequerimento)";
			}
			else if (DocumentoObrigatorioEnum.POSTO.getId().equals(lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
				return "SELECT remover_lac_posto_by_requerimento(:pRequerimento)";
			}
			else if (DocumentoObrigatorioEnum.TRANSPORTADORA.getId().equals(lac.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
				return "SELECT remover_lac_transporte_by_requerimento(:pRequerimento)";
			}
		} else if(object instanceof AtoDeclaratorio){
			AtoDeclaratorio atoDeclaratorio = (AtoDeclaratorio) object;
			if(DocumentoObrigatorioEnum.FORMULARIO_DIAP.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_ato_declaratorio_diap_by_requerimento(:pRequerimento)";
				
			} 
			else if(DocumentoObrigatorioEnum.FORMULARIO_DQC.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_ato_declaratorio_dqc_by_requerimento(:pRequerimento)";
				
			} 
			else if(DocumentoObrigatorioEnum.FORMULARIO_RFP.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_ato_declaratorio_rfp_by_requerimento(:pRequerimento)";
				
			}
			else if(DocumentoObrigatorioEnum.FCE_SISTEMA_ABASTECIMENTO_AGUA.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_saa_by_requerimento(:pRequerimento)";
			}	
			else if(DocumentoObrigatorioEnum.FCE_CARACTERIZACAO_SES.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_ses_by_requerimento(:pRequerimento)";
			}
			else if(DocumentoObrigatorioEnum.FCE_GERACAO_ENERGIA.getId().equals(atoDeclaratorio.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
				return "SELECT remover_fce_geracao_energia_by_requerimento(:pRequerimento)";
			}
		} else if (object instanceof AtoAmbiental) {
			AtoAmbiental atoAmbiental = (AtoAmbiental) object;
			
			if (new AtoAmbiental(AtoAmbientalEnum.APE.getId()).equals(atoAmbiental)) {
				return "SELECT remover_ape_by_requerimento(:pRequerimento)";
			}
		}
		return "";
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void apagarByFunctionAndRequerimento(String sql, Requerimento requerimento) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pRequerimento", requerimento.getNumRequerimento());
		requerimentoDAO.buscarPorNativeQuery(sql, params);
	}
}