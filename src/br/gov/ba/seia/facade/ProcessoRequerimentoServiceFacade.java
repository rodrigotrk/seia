package br.gov.ba.seia.facade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoRequerimentoServiceFacade {
	
	@EJB
	private ProcessoService processoService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private PautaService pautaService;
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private OrgaoService orgaoService;
	@EJB
	private ParametroService parametroService;
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	
	/* ZCR/RN0031. 
	 * A op��o �Gerar relat�rio de ato declarat�rio� s� ser� exibida caso exista
	 * um processo relacionado ao Requerimento, o status do processo seja
	 * Conclu�do, e TODOS os atos relacionados ao processo sejam Declarat�rios.
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validaRelatorioAtoDeclaratorio(RequerimentoUnicoDTO pRequerimentoUnicoDTO) {
		
		// Verifica se o requerimento foi pago, ou seja, gerou o processo.
		if(!pRequerimentoUnicoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus())) {
			// se n�o existe processo, retorna falso.
			return false;
		}
	  // Verifica se o ato ambiental do enquadramento foi RFP.
			for (AtoAmbiental	lAtoAmbiental : pRequerimentoUnicoDTO.getRequerimentoUnico().getAtosAmbientais()) {
				if (lAtoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId())) {
					return false;
				}
			}
			
		// Obtem o processo do Requerimento
		Exception erro =null;	
		try {
			
			Processo lProcesso = processoService.buscarPorRequerimento(pRequerimentoUnicoDTO.getRequerimentoUnico().getRequerimento().getIdeRequerimento());
			if(!Util.isNull(lProcesso)) {
					// Verifica o status atual do processo
					ControleTramitacao tramitacao = controleTramitacaoService.buscarUltimoPorProcesso(lProcesso);
					// Verifica status concluido
				
					if(!Util.isNullOuVazio(tramitacao) && StatusFluxoEnum.CONCLUIDO.getStatus() == tramitacao.getIdeStatusFluxo().getIdeStatusFluxo().intValue()) {
						// verifica se todos os atos sao declaratorios
						return verificarAtosDeclaratoriosPorProcessoAto(lProcesso);
					} else {
						return false;
					}
			} else {
				// Se nao existe processo
				return false;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return false;
	}
	
	public boolean validaRelatorioAtoDeclaratorio(RequerimentoDTO pRequerimentoUnicoDTO) {
		
		// Verifica se o requerimento foi pago, ou seja, gerou o processo.
		if(!pRequerimentoUnicoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus())) {
			// se nao existe processo, retorna falso.
			return false;
		}
			
		// Obtem o processo do Requerimento
		try {
			Processo lProcesso = processoService.buscarPorRequerimento(pRequerimentoUnicoDTO.getRequerimento().getIdeRequerimento());
			if(!Util.isNull(lProcesso)) {
				// Verifica o status atual do processo
				ControleTramitacao tramitacao = controleTramitacaoService.buscarUltimoPorProcesso(lProcesso);
				// Verifica status concluido
			
				if(!Util.isNullOuVazio(tramitacao) && StatusFluxoEnum.CONCLUIDO.getStatus() == tramitacao.getIdeStatusFluxo().getIdeStatusFluxo().intValue()) {
					// verifica se existe ato declaratorio
					return verificarAtosDeclaratoriosPorProcessoAto(lProcesso);
				} else {
					return false;
				}
			} else {
				// Se nao existe processo
				return false;
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	// Se pelo menos um ato for declaratorio, exibe o certificado de ato declaratório.
	private boolean verificarAtosDeclaratoriosPorProcessoAto(Processo processo) {
		for (ProcessoAto processoAto : processoAtoService.listarAtosPorProcesso(processo)) {
			if(processoAto.getAtoAmbiental().getIndDeclaratorio()) {
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcessoComDispensaDePagamento(Requerimento requerimento, List<Requerimento> requerimentosDispensaveis, Area pArea) throws Exception {
		Parametro parametroIdOrgaoInema = parametroService.obterPorEnum(ParametroEnum.ORGAO_INEMA);
		Integer idOrgao = parametroService.obterValorInt(parametroIdOrgaoInema);
		Orgao org = orgaoService.carregar(new Orgao(idOrgao));
		
		// Cria o processo
		Processo processo = new Processo();
		processo.setIdeOrgao(org);
		processo.setDtcFormacao(new Date());
		processo.setIdeRequerimento(requerimento);
		
		
		// Busca Atos Ambientais
		List<AtoAmbiental> atos = carregaAtosPorRequerimento(requerimentosDispensaveis.iterator().next());
		
		// Obtem n�mero do processo e salva processo
		String numeroProcesso = gerarNumeroProcesso(processo, atos);
		processo.setNumProcesso(numeroProcesso);
		processoService.salvarProcesso(processo);
		
		List<ProcessoAto> processoAtos = preencheProcessoAtos(processo, atos);
		

		// Salva os atos do processo
		processoAtoService.salvarEmLote(processoAtos);
		processo.setProcessoAtoCollection(processoAtos);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcesso(Requerimento pRequerimento, Area pArea) throws Exception {
		
		//Obtem o org�o do processo
		Parametro parametroIdOrgaoInema = parametroService.obterPorEnum(ParametroEnum.ORGAO_INEMA);
		Integer idOrgao = parametroService.obterValorInt(parametroIdOrgaoInema);
		Orgao org = orgaoService.carregar(new Orgao(idOrgao));
		
		// Cria o processo
		Processo processo = new Processo();
		processo.setIdeOrgao(org);
		processo.setDtcFormacao(new Date());
		processo.setIdeRequerimento(pRequerimento);
		
		// Busca Atos Ambientais
		List<AtoAmbiental> atos = carregaAtosPorRequerimento(pRequerimento);
		
		boolean isConcluido = false;
		// Verifica atos para definir status - ZCR/RN0032
		if(verificarAtosDeclaratorios(atos)) {
			// se todos os atos forem declarat�rios, settar status processo como conclu�do
			isConcluido = true;
		}
		
		// Obtem n�mero do processo e salva processo
		String numeroProcesso = gerarNumeroProcesso(processo, atos);
		processo.setNumProcesso(numeroProcesso);
		processoService.salvarProcesso(processo);
		
		processo.setProcessoAtoCollection(preencheProcessoAtos(processo, atos));
		
		// Salva os atos do processo
		processoAtoService.salvarEmLote(processo.getProcessoAtoCollection());
		salvarControleProcessoAto(processo.getProcessoAtoCollection());
		
		// Cria Tramitacao
		controleTramitacaoService.salvar(this.criarTramitacao(processo, pArea, isConcluido));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarControleProcessoAto(Collection<ProcessoAto> listaProcessoAto) {
		for(ProcessoAto processoAto : listaProcessoAto) {
			ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
			controleProcessoAto.setIdeProcessoAto(processoAto);
			controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.AGUARDANDO_ANALISE.getId()));
			controleProcessoAto.setDtcControleProcessoAto(new Date());
			controleProcessoAto.setIndAprovado(true);
			controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
		}
	}
	
	/*
	 * Verifica se todos os atos s�o do tipo declaratorio.
	 * ZCR/RN0032.
	 */
	private boolean verificarAtosDeclaratorios(List<AtoAmbiental> pAtos) {
		
		for (AtoAmbiental atoAmbiental : pAtos) {
			if(!atoAmbiental.getIndDeclaratorio()) {
				return false;
			}
		}
		// se todos forem declaratorios retorna true
		return true;
	}
	
	private List<AtoAmbiental> carregaAtosPorRequerimento(Requerimento pRequerimento){
		// Carrega o enquadramento
		Enquadramento enquadramento = new Enquadramento();
		enquadramento = enquadramentoService.buscarUltimoEnquadramentoRequerimento(pRequerimento);
		
		if(Util.isNull(enquadramento)) {
			return new ArrayList<AtoAmbiental>();
		}
		
		return (List<AtoAmbiental>) enquadramento.getAtoAmbientalCollection();
	}
	
	private List<ProcessoAto> preencheProcessoAtos(Processo pProcesso, List<AtoAmbiental> pAtos){
		
		List<ProcessoAto> processoAtos = new ArrayList<ProcessoAto>();
		if(!Util.isNullOuVazio(pAtos)) {
			for(AtoAmbiental ato : pAtos) {
				ProcessoAto item = new ProcessoAto(pProcesso.getIdeProcesso(), ato.getIdeAtoAmbiental());
				processoAtos.add(item);
			}
		}
		pProcesso.setProcessoAtoCollection(processoAtos);
		return processoAtos;
	}
	
	private ControleTramitacao criarTramitacao(Processo pProcesso, Area pArea, boolean pIsConcluido) {
		ControleTramitacao tramitacao = new ControleTramitacao();
		tramitacao.setDtcTramitacao(new Date());
		
		if(!pIsConcluido) {
			tramitacao.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.FORMADO.getStatus()));
		} else {
			tramitacao.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.CONCLUIDO.getStatus()));
		}
		
		tramitacao.setIndFimDaFila(true);
		tramitacao.setIdeProcesso(pProcesso);
		// Associa Area
		tramitacao.setIdeArea(pArea);
		// Associa Pauta
		tramitacao.setIdePauta(pautaService.obtemPautaArea(pArea));
		return tramitacao;
	}
	
	private String gerarNumeroProcesso(Processo processo, List<AtoAmbiental> pAtos) {

		StringBuilder numeroProcesso = new StringBuilder(32);
		
		/*
		 * Regra de neg�cio = ZCR/RN0028
		 * Formato = AAAA.xxx.YYYYYY/SIGLA_ORGAO/SIGLA_GRUPO_PROCESSO-ZZZZZ
		 * 2011.001.000001/INE/LINC-00001
		 * AAAA.xxx.YYYYYY/SSOO/SSGG-ZZZZZ
		 * 
		 * A  = Ano da data de abertura.
		 * x  = C�digo do Org�o.
		 * Y  = Sequencial de processos de mesmo ano e org�o. Obter o Y e somar 1.
		 * SO = Sigla do Org�o do processo.
		 * SG = Sigla do Grupo - Obter um ato ambiental e atrav�s do tipo obter o grupo do processo.
		 * Z  = Sequencial - Buscar processo de mesmo ano, org�o e grupo. Obter Z e somar 1.
		 */
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String ano = simpleDateFormat.format(processo.getDtcFormacao());
		
		// Gerando numero.
		numeroProcesso.append(ano);
		numeroProcesso.append('.');
		String x = processo.getIdeOrgao().getCodOrgao().toString();
		x = Util.lpad(x, '0', 3);
		numeroProcesso.append(x);
		numeroProcesso.append('.');

		// Y  = Sequencial de processos de mesmo ano e org�o. Obter o Y e somar 1.
		String y = null;
		// Z  = Sequencial - Buscar processo de mesmo ano, org�o e grupo. Obter Z e somar 1.
		String z = null;
		List<Processo> processosAnoOrgao = (List<Processo>) processoService.buscarPorAnoECodigoOrgao(ano, processo.getIdeOrgao());
		if(!Util.isNullOuVazio(processosAnoOrgao)) {
			Processo p = processosAnoOrgao.get(0);
			String aux = p.getNumProcesso();
			if(aux.length() >= 16) {
				aux = aux.substring(9, 15);
				int i = Integer.parseInt(aux) + 1;
				y = "" + i;
			}
			aux = p.getNumProcesso();
			if(aux.length() > 0) {
				aux = aux.substring(aux.lastIndexOf('-')+1);
				int i = Integer.parseInt(aux) + 1;
				z = "" + i;
			}
		}
		if(Util.isNullOuVazio(y)) {
			y = "1";
		}
		if(Util.isNullOuVazio(z)) {
			z = "1";
		}
		y = Util.lpad(y, '0', 6);
		numeroProcesso.append(y);
		numeroProcesso.append('/');
		String so = processo.getIdeOrgao().getDscSiglaOrgao();
		if(!Util.isNull(so)) {
			numeroProcesso.append(so);
		}
		numeroProcesso.append('/');

		// SG = Sigla do Grupo - Obter um ato ambiental e atrav�s do tipo obter o grupo do processo.
		String sg = null;
		if(!Util.isNullOuVazio(pAtos)) {
			AtoAmbiental ato = pAtos.get(0);
			sg = ato.getIdeTipoAto().getIdeGrupoProcesso().getDscSiglaGrupoProcesso();
		}
		if(!Util.isNull(sg)) {
			numeroProcesso.append(sg);
		}
		numeroProcesso.append('-');
		
		z = Util.lpad(z, '0', 5);
		numeroProcesso.append(z);
		return numeroProcesso.toString();
	}

	public boolean hasProcesso(Integer ideRequerimentoUnico){
		return this.processoService.hasProcesso(ideRequerimentoUnico);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcessoCumprimentoReposicaoFlorestal(Requerimento pRequerimento, Area pArea, TramitacaoRequerimentoService tramitacaoRequerimentoService) throws Exception {
		
		//Obtem o org�o do processo
		Parametro parametroIdOrgaoInema = parametroService.obterPorEnum(ParametroEnum.ORGAO_INEMA);
		Integer idOrgao = parametroService.obterValorInt(parametroIdOrgaoInema);
		Orgao org = orgaoService.carregar(new Orgao(idOrgao));
		
		// Cria o processo
		Processo processo = new Processo();
		processo.setIdeOrgao(org);
		processo.setDtcFormacao(new Date());
		processo.setIdeRequerimento(pRequerimento);
		
		// Busca Atos Ambientais
		List<AtoAmbiental> atos = carregaAtosPorRequerimento(pRequerimento);
		
		boolean isConcluido = false;
		// Verifica atos para definir status - ZCR/RN0032
		if(verificarAtosDeclaratorios(atos)) {
			// se todos os atos forem declarat�rios, settar status processo como conclu�do
			isConcluido = true;
		}
		
		// Obtem n�mero do processo e salva processo
		String numeroProcesso = gerarNumeroProcesso(processo, atos);
		processo.setNumProcesso(numeroProcesso);
		processoService.salvarProcesso(processo);
		
		processo.setProcessoAtoCollection(preencheProcessoAtos(processo, atos));
		
		// Salva os atos do processo
		processoAtoService.salvarEmLote(processo.getProcessoAtoCollection());
		salvarControleProcessoAtoStatusEmitido(processo.getProcessoAtoCollection());
		
		// Cria Tramitacao
		controleTramitacaoService.salvar(this.criarTramitacao(processo, pArea, isConcluido));
		
		tramitacaoRequerimentoService.tramitarAutomaticamente(pRequerimento, StatusRequerimentoEnum.FORMADO);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarControleProcessoAtoStatusEmitido(Collection<ProcessoAto> listaProcessoAto) {
		for(ProcessoAto processoAto : listaProcessoAto) {
			ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
			controleProcessoAto.setIdeProcessoAto(processoAto);
			controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.EMITIDO.getId()));
			controleProcessoAto.setDtcControleProcessoAto(new Date());
			controleProcessoAto.setIndAprovado(true);
			controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
		}
	}
}