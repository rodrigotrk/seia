package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Portaria;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TipoPortaria;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoPortariaEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.facade.FluxoAlternativoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.PortariaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named("fluxoAlternativoController")
@ViewScoped
public class FluxoAlternativoController {
	
	@EJB
	private ProcessoService processoService;
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	@EJB
	private OrgaoService orgaoService;
	@EJB
	private AreaService areaService;
	@EJB
	private FluxoAlternativoFacade fluxoAlternativoFacade;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private PautaService pautaService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdmService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private PortariaService portariaService;
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private FlorestalService florestalService;
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	@EJB
	private FceService fceService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocalizacaoGeograficaService;
	
	private Processo processo;
	private VwConsultaProcesso vwConsultaProcesso;
	private Collection<Orgao> listaOrgaos;
	private Collection<Area> listaAreas;
	
	private String obs;
	private String caminhoArquivo;
	private Boolean temArquivo = false;
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	private List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento;
	
	// Controle de tela
	private boolean visualizaAreas;
	private boolean isPautaArea;
	private boolean isPautaTecnico;
	
	private Orgao orgao;
	private Area area;
	private Area areaSelecionada;
	private boolean visualizarPortaria;
	private String tipoFluxo;
	private Portaria portaria;
	private List<Imovel> listaImovel;
	private Boolean isImovelUrbano = Boolean.FALSE;
	
	@PostConstruct
	public void init() {
		limparTela();
	}
	
	public void limparTela() {
		visualizaAreas = false;
		carregarOrgaos();
		this.obs = "";
		this.areaSelecionada = new Area();
		this.area = new Area();
		this.orgao = new Orgao();
		this.portaria = new Portaria();
	}
	
	public boolean getPodeConcluir() {
		PessoaFisica pessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		return funcionarioService.validarPossibilidadeConclusaoProcesso(processo, pessoaFisica);
	}
	
	public StreamedContent getImprimirPortaria() {
		try {
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("ideProcesso", vwConsultaProcesso.getIdeProcesso());
			return new RelatorioUtil("portaria.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void loadReenquadramento(Boolean pautaArea, Boolean pautaTecnico, ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		try {
			limparTela();
			this.processoReenquadramentoDTO = processoReenquadramentoDTO;
			
			this.vwConsultaProcesso = vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(
					processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getIdeProcesso(), false);
			
			this.processo = processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso();
			setPautaArea(pautaArea);
			setPautaTecnico(pautaTecnico);
			tipoFluxo = "3";
			carregarTextoPortaria();
			
			this.listaDocumentoObrigatorioEnquadramento = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTOReenquadramento(
					processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcessoReenquadramento(), true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void load(Boolean pautaArea, Boolean pautaTecnico, VwConsultaProcesso vwConsultaProcesso) {
		limparTela();
		this.vwConsultaProcesso = vwConsultaProcesso;
		carregarProcesso();
		setPautaArea(pautaArea);
		setPautaTecnico(pautaTecnico);
		tipoFluxo = "3";
		carregarTextoPortaria();
	}
	
	public void verificarDadoConcedidoECarregarFluxoAlternativo(Boolean pautaArea, Boolean pautaTecnico, VwConsultaProcesso vwConsultaProcesso) {
		try {
			load(pautaArea, pautaTecnico, vwConsultaProcesso);
			
			List<Fce> listFce = fceService.listarFcePor(vwConsultaProcesso.getIdeProcesso(), DadoOrigemEnum.TECNICO);
			
			if(!Util.isNullOuVazio(listFce)) {
				for (Fce fce : listFce) {
					
					if(fce.getIdeDadoOrigem().getIdeDadoOrigem().equals(DadoOrigemEnum.TECNICO.getId()) 
							&& (fce.isIndConcluido() == null || !fce.isIndConcluido())){
						
						Html.exibir("dialogAlertaDadoConcedido");
						return;
					}
				}
			}
			
			if(!Util.isNullOuVazio(this.vwConsultaProcesso.getListProcessoAto())){
				
				for (ProcessoAto pa : this.vwConsultaProcesso.getListProcessoAto()) {
					
					if(processoAtoConcedidoDAOImpl.existeProcessoAtoConcedido(pa.getIdeProcessoAto())) {
						ControleProcessoAto cpa = controleProcessoAtoDAOImpl.buscarUltimoDeferidoOuIndeferidoPorProcessoAto(pa.getIdeProcessoAto());
						
						if(!Util.isNullOuVazio(cpa) && !Util.isNullOuVazio(cpa.getIdeStatusProcessoAto())
								&& !cpa.getIdeStatusProcessoAto().isDeferidoPeloTecnico() && !cpa.getIdeStatusProcessoAto().isIndeferidoPeloTecnico()) {
							
							Html.exibir("dialogAlertaDadoConcedido");
							return;
						}
					}
				}
			}
			
			Html.exibir("dialogEncaminhar");
			Html.atualizar("pnlFluxoAlternativo");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarTextoPortaria() {
		try {
			Collection<AtoAmbiental> atosAmbientais = atoAmbientalService.listarAtosPorProcesso(processo.getIdeProcesso());
			
			Requerimento requerimento = this.requerimentoService.carregarDadosBasicos(this.processo.getIdeRequerimento().getIdeRequerimento());
			Pessoa requerente = this.pessoaService.carregarDadosRequerente(requerimento.getIdeRequerimento(), null);
			Pessoa diretor = this.carregarDiretorDIREG();
			
			boolean isAlrs = isALRS(atosAmbientais);
			boolean isTla = isTLA(atosAmbientais);
			boolean isAmc = isAMC(atosAmbientais);
			boolean isAsv = isASV(atosAmbientais);
			
			/*visualizarPortaria = false;
			portaria.setTextoObrigatorio(false);*/
			
			if (isAlrs || isTla || isAmc) {
				visualizarPortaria = true;
				portaria.setTextoObrigatorio(true);
			} else {
				visualizarPortaria = true;
				portaria.setTextoObrigatorio(false);
			}
			
			this.portaria = portariaService.buscarPortariaByProcesso(processo);
			
			if (Util.isNullOuVazio(portaria)) {
				portaria = new Portaria();
				
				if (isAlrs) {
					SolicitacaoAdministrativo alrs = this.solicitacaoAdmService.obterSolicitacaoAdministrativa(processo.getIdeRequerimento(),
							TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL);
					carregarPortariaAlrs(alrs, requerente, diretor);
					
				} else if (isTla) {
					SolicitacaoAdministrativo tla = this.solicitacaoAdmService.obterSolicitacaoAdministrativa(processo.getIdeRequerimento(),
							TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);
					
					if (tla != null) {
						Collection<AtoAmbiental> atosSolicitados = atoAmbientalService.listarAtoAmbientalSolicitacaoAdministrativo(tla);
						carregarPortariaTla(tla, requerimento, atosSolicitados, requerente, diretor);
					}
				} else if (isAmc) {
					try {
						listaImovel = imovelService.filtrarListaImovelPorEmpreendimento(requerimento.getUltimoEmpreendimento());
					} catch (Exception e) {
						JsfUtil.addErrorMessage("Erro ao carregar a lista de imóveis:" + e.getMessage());
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
					
					if (!Util.isNullOuVazio(listaImovel) && !Util.isNull(listaImovel.iterator().next().getImovelUrbano())) {
						isImovelUrbano = true;
					}
					
					Florestal florestal = florestalService.obterFlorestalByRequerimento(requerimento.getIdeRequerimento());
					
					carregarPortariaAmc(requerimento, requerente, diretor, florestal, isImovelUrbano);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isPossuiPortaria() {
		boolean retorno = false;
		
		try {
			if (Util.isNullOuVazio(portariaService.buscarPortariaByProcesso(new Processo(vwConsultaProcesso.getIdeProcesso())))) {
				retorno = false;
			} else {
				retorno = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return retorno;
	}
	
	public void carregarPortariaAlrs(SolicitacaoAdministrativo alrs, Pessoa requerente, Pessoa diretor) {
		
		StringBuilder textoPortaria = new StringBuilder();
		textoPortaria.append("PORTARIA Nº [XXX] de [DIA] de [MÊS] de [ANO]");
		textoPortaria.append("O INSTITUTO DO MEIO AMBIENTE E RECURSOS HÍDRICOS - INEMA, com fulcro nas atribuições e competências");
		textoPortaria.append(" que lhe foram delegadas pela Lei Estadual n° 12.212/11 e Lei Estadual n° 10.431/06, alterada pela Lei nº 12.377/11,");
		textoPortaria.append(" regulamentada pelo Decreto Estadual n° 14.024/12 e, tendo em vista o que consta do Processo nº ");
		textoPortaria.append(this.processo.getNumProcesso());
		textoPortaria.append(", RESOLVE: Art. 1º - Alterar nos registros do Instituto do Meio Ambiente e Recursos Hídricos – INEMA, a Razão Social da ");
		textoPortaria.append(requerente.getNomeRazao());
		textoPortaria.append(" inscrita no CNPJ sob o nº ");
		textoPortaria.append(requerente.getCpfCnpjFormatado());
		textoPortaria.append(", para ");
		textoPortaria.append(alrs.getNomRazaoSocialNova());
		textoPortaria.append(", inscrita no CNPJ sob o nº ");
		textoPortaria.append(requerente.getCpfCnpjFormatado());
		textoPortaria.append(". Art. 2º - Esta Portaria entrará em vigor na data de sua publicação.");
		textoPortaria.append(diretor.getNomeRazao());
		textoPortaria.append(" – Diretora Geral");
		
		portaria.setTextoPortaria(textoPortaria.toString());
		portaria.setIdeTipoPortaria(new TipoPortaria(TipoPortariaEnum.ARLS.getId()));
	}
	
	public void carregarPortariaAmc(Requerimento requerimento, Pessoa requerente, Pessoa diretor, Florestal florestal, Boolean isImovelUrbano) {
		
		String nomePessoa = "";
		String cpfCnpj = "";
		
		if (!Util.isNull(requerente.getPessoaJuridica())) {
			nomePessoa = requerente.getPessoaJuridica().getNomRazaoSocial();
			cpfCnpj = Util.formatarCNPJ(requerente.getPessoaJuridica().getNumCnpj());
		} else {
			nomePessoa = requerente.getPessoaFisica().getNomPessoa();
			cpfCnpj = requerente.getPessoaFisica().getNumCpfFormatado();
		}
		
		StringBuilder textoPortaria = new StringBuilder();
		textoPortaria.append("PORTARIA Nº 12.211 DE 13 DE AGOSTO DE 2016. O INSTITUTO DO MEIO AMBIENTE E RECURSOS HÍDRICOS - INEMA");
		textoPortaria.append(" com fulcro nas atribuições e competências que lhe foram delegadas pela Lei Estadual n° 12.212/11 e Lei Estadual n° 10.431/06");
		textoPortaria.append(" alterada pela Lei nº 12.377/11, regulamentada pelo Decreto Estadual n° 14.024/12 e,");
		textoPortaria.append(" tendo em vista o que consta do Processo nº 2016-000153/TEC/AMC-0001, RESOLVE: Art. 1.º - Conceder AUTORIZAÇÃO DE MANEJO DA CABRUCA,");
		textoPortaria.append(" válida pelo prazo de 1 (um) ano, à ");
		
		textoPortaria.append(nomePessoa + " inscrito(a) no CPF/CNPJ ");
		textoPortaria.append(cpfCnpj + ", com sede no(a), ");
		textoPortaria.append(requerimento.getUltimoEmpreendimento().getEndereco().getEnderecoCompleto());
		textoPortaria.append(" para Autorização de Manejo de Cabruca com o objetivo de recuperar ");
		
		textoPortaria.append(florestal.getNumAreaManejoCabruca() + " ha de cacau cabruca,");
		textoPortaria.append(" com as seguintes coordenadas geográfica : 39º 21 45 W, 14º43 23 S datum SIRGA2000,");
		
		if (isImovelUrbano) {
			textoPortaria.append(" zona urbana");
		} else {
			textoPortaria.append(" zona rural");
		}
		
		textoPortaria.append(" no município de ");
		textoPortaria.append(requerimento.getUltimoEmpreendimento().getEndereco().getIdeLogradouro().getIdeMunicipio().getNomMunicipio());
		
		textoPortaria.append(", mediante o cumprimento da legislação vigente e dos seguintes condicionantes: I. garantir a integridade da APP,");
		textoPortaria.append(" sendo permitida a implantação de corredor de acesso de pessoas e animais para obtenção de água,");
		textoPortaria.append(" conforme Resolução CONAMA nº 369/06;");
		textoPortaria.append(" II. atender no prazo de 120 dias o disposto na Portaria nº 10.225/2015 Art. 5º inciso III com o plantio de 12 (doze) mudas de indivíduos nativos,");
		textoPortaria.append(" priorizando a área de interferência do PTMC; III. encaminhar no prazo de 120 dias,");
		textoPortaria.append(" justificativa técnica referente à proposta de cumprimento parcial da Portaria nº 10.225/2015 Art. 6º inciso III alínea “f”.");
		textoPortaria.append(" Prazo: 120 dias ; Freqüência: mensal. IV. manter durante a supressão de vegetação,pessoal habilitado para realizar capturas de animais que venham a se ferir,");
		textoPortaria.append(" dando a estes toda a assistência e os devidos encaminhamentos para a sua plena recuperação e devolução ao seu habitat natural;"
						+ " V. não explorar espécies florestais ameaçadas de extinção, conforme Instrução Normativa MMA 06/08, Portaria IBAMA nº 113/95,"
						+ " Instrução Normativa IBAMA nº 147/07 e Resolução CEPRAM 1009/94; VI. realizar o registro obrigatório no RAF,"
						+ " com forme disposto na Portaria nº 11.340/2009, publicada no Diário Oficial do Estado da Bahia em 1º e 2 de agosto de 2009;"
						+ " VII.  atender o disposto na lei 9.974 de 06 de junho de 2000, no que diz respeito ao transporte,"
						+ " o armazenamento,  a utilização e destino final dos resíduos e embalagens, de agrotóxicos; VIII. "
						+ " garantir a integridade da área da Reserva Legal de acordo com a Lei Federal nº 12.651/2012 e Lei Estadual nº 10.431/06 alterada pela Lei 12.377/2011,"
						+ " cercar e/ou manter a área de reserva legal cercadas e isoladas da área de pasto, não permitindo o acesso dos animais de criação a área de Reserva Legal;"
						+ " IX. gerenciar a movimentação de máquinas, veículos e pessoas nas operações de supressão das árvores  no sentido de minimizar os impactos causados a fauna,"
						+ " em especial aquelas ameaçadas de extinção constante no Livro Vermelho da Fauna Brasileira Ameaçada de Extinção, Vol. II (MMA/2008);"
						+ " X. manter à disposição da fiscalização ambiental relatório anual sobre o andamento das atividades e outras ações referentes ao projeto,"
						+ " acompanhado de ART do profissional habilitado;"
						+ " Art. 2.º - O volume rendimento do material lenhoso gerado foi estimado em um volume total de 43,07 m³ ou 64,605 st ou 21,535 mdc,"
						+ " deste, 40,787 m³ ou 61,1805 st ou 20,3935 mdc serão utilizados na forma de lenha e 0,9252 m³ de toras de Louro (Nectrandra sp1 (morta)) indivíduo nº 44,"
						+ " 0,2322 m³ de toras de Grão de Burro (Swartzia macrostachya), 0,5672 m³ de toras de Jenipapo (Genipa americana),"
						+ " 0,5584 m³ de toras de Grão de Burro (Swartzia macrostachya);"
						+ " Art. 3.º - As coordenadas geográficas dos indivíduos censiados de acordo com o inventário florestal apresentado,"
						+ " constam no corpo do processo; Art. 4.º - Esta portaria aprova o plano de Salvamento da Fauna, incluindo seu manejo e seu transporte,"
						+ " quando necessário; Art. 5.º - Esta atividade esta vinculada a dispensa de licença ambiental, conforme anexo IV do regulamento da lei 10.431/2006, aprovado pelo decreto"
						+ " 14.024/2012; Art. 6.º - Os produtos e subprodutos originados da atividade autorizada deverão ser aproveitados conforme estabelecido"
						+ " no Art. 115 da lei 10431/2006 sujeitando-se o transporte ao Art. 144 da mesma, bem como à portaria MMA nº 253/2006,"
						+ " que dispõe sobre a necessidade de registro de tais produtos no “Sistema – DOF” para o controle informatizado do transporte e armazenamento;"
						+ " Art. 7.º - Esta autorização bem como cópia dos documentos relativos ao cumprimento dos condicionantes acima citados,"
						+ " devem ser mantidas disponíveis a fiscalização dos órgãos do Sistema Estadual de Meio Ambiente – SISEMA;"
						+ " Art. 8.º - Esta licença refere-se a análise de viabilidade ambiental de competência do Instituto do Meio Ambiente e Recursos Hídricos  - cabendo ao interessado"
						+ " obter a Anuência e/ou Autorizações das outras instâncias no Âmbito Federal, Estadual ou Municipal quando couber, para que a mesma alcance seus efeitos legais;");
		
		textoPortaria.append(" Art. 9.º - Esta portaria entrará em vigor na data de sua publicação.");
		
		portaria.setTextoPortaria(textoPortaria.toString());
		portaria.setIdeTipoPortaria(new TipoPortaria(TipoPortariaEnum.AMC.getId()));
	}
	
	public void carregarPortariaTla(SolicitacaoAdministrativo tla, Requerimento requerimento, Collection<AtoAmbiental> atosSolicitados, 
			Pessoa requerente, Pessoa diretor) {
		
		StringBuilder textoPortaria = new StringBuilder();
		
		textoPortaria.append("PORTARIA Nº [XXX] de [DIA] de [MÊS] de [ANO] ");
		textoPortaria.append("O INSTITUTO DO MEIO AMBIENTE E RECURSOS HÍDRICOS - INEMA, com fulcro nas atribuições e competências ");
		textoPortaria.append("que lhe foram delegadas pela Lei Estadual n° 12.212/11 e Lei Estadual n° 10.431/06, alterada pela Lei nº 12.377/11, ");
		textoPortaria.append("regulamentada pelo Decreto Estadual n° 14.024/12 e, tendo em vista o que consta do Processo nº ");
		textoPortaria.append(this.processo.getNumProcesso());
		textoPortaria.append(", RESOLVE: Art. 1º - Transferir, nos registros do Instituto do Meio Ambiente e Recursos Hídricos – INEMA, as titularidades d(o)(a) ");
		
		for (Iterator<AtoAmbiental> iterator = (Iterator<AtoAmbiental>) atosSolicitados.iterator(); ((java.util.Iterator<AtoAmbiental>) iterator).hasNext();) {
			AtoAmbiental a = (AtoAmbiental) ((java.util.Iterator<AtoAmbiental>) iterator).next();
			textoPortaria.append(a.getNomAtoAmbiental() + ", ");
		}
		
		textoPortaria.append("concedido(a)s através da Portaria INEMA nº ");
		textoPortaria.append(tla.getNumPortaria());
		textoPortaria.append(", publicada no D.O.E de [DATA DE PUBLICAÇÃO DO DOE], em nome de ");
		
		if (tla.getIndDetentorLicenca()) {
			textoPortaria.append(requerente.getNomeRazao());
			textoPortaria.append(", inscrita no CPF/CNPJ sob o n° ");
			textoPortaria.append(requerente.getCpfCnpjFormatado());
			textoPortaria.append(", para ");
			textoPortaria.append(tla.getIdePessoaNovoTitular().getNomeRazao());
			textoPortaria.append(", inscrita no CPF/CNPJ sob o nº ");
			textoPortaria.append(tla.getIdePessoaNovoTitular().getCpfCnpjFormatado());
		} else {
			textoPortaria.append(tla.getIdePessoaDetentorLicenca().getNomeRazao());
			textoPortaria.append(", inscrita no CPF/CNPJ sob o n° ");
			textoPortaria.append(tla.getIdePessoaDetentorLicenca().getCpfCnpjFormatado());
			textoPortaria.append(", para ");
			textoPortaria.append(requerente.getNomeRazao());
			textoPortaria.append(", inscrita no CPF/CNPJ sob o nº ");
			textoPortaria.append(requerente.getCpfCnpjFormatado());
		}
		
		textoPortaria.append(", referente a(o) ");
		textoPortaria.append(tla.getIdeEmpreendimento().getNomEmpreendimento());
		textoPortaria.append(", localizado em ");
		textoPortaria.append(requerimento.getUltimoEmpreendimento().getEndereco().getEnderecoCompleto());
		textoPortaria.append(" Art. 2º - Esta Portaria entrará em vigor na data de sua publicação.");
		textoPortaria.append(diretor.getNomeRazao());
		textoPortaria.append(" – Diretora Geral");
		
		portaria.setTextoPortaria(textoPortaria.toString());
		portaria.setIdeTipoPortaria(new TipoPortaria(TipoPortariaEnum.TLA.getId()));
	}
	
	private boolean isALRS(Collection<AtoAmbiental> atosAmbientais) {
		return atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ARLS.getId()));
	}
	
	private boolean isTLA(Collection<AtoAmbiental> atosAmbientais) {
		return atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.TLA.getId()));
	}
	
	private boolean isAMC(Collection<AtoAmbiental> atosAmbientais) {
		return atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.AMC.getId()));
	}
	
	private boolean isASV(Collection<AtoAmbiental> atosAmbientais) {
		return atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ASV.getId()));
	}
	
	public Pessoa carregarDiretorDIREG() throws Exception {
		Funcionario areaDireg = areaService.obterPessoaFisicaCoordenadorPorIdeArea(AreaEnum.DIREG.getId());
		return pessoaService.carregarGet(areaDireg.getIdePessoaFisica());
	}
	
	public void carregarProcesso() {
		try {
			this.processo = processoService.carregarProcesso(vwConsultaProcesso.getIdeProcesso());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizarPortaria() {
		if (validarPortaria()) {
			portaria.setIdeProcesso(new Processo(vwConsultaProcesso.getIdeProcesso()));
			portaria.setDtcPortaria(new Date());
			portaria.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			
			try {
				portariaService.salvarOuAtualizar(portaria);
				
				lancaAviso("Portaria salva com sucesso", "SUCESS");
				RequestContext.getCurrentInstance().addPartialUpdateTarget("formPortaria:portariaImprimirPortaria");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	private boolean validarPortaria() {
		
		boolean valido = true;
		String tipoAviso = "WARRING";
		
		if (portaria.isTextoObrigatorio() && portaria.getTextoPortaria().isEmpty()) {
			lancaAviso("Insira o texto da portaria.", tipoAviso);
			valido = false;
		}
		
		if (portaria.getDtcPublicacaoPortaria() == null) {
			lancaAviso("Insira a data de publicação da portaria.", tipoAviso);
			valido = false;
		}
		
		if (portaria.getNumPortaria().isEmpty()) {
			lancaAviso("Insira o número da portaria.", tipoAviso);
			valido = false;
		}
		
		if (portaria.getNumPortaria().length() > 50) {
			lancaAviso("O campo N° portaria aceita somente 50 caracteres.", tipoAviso);
			valido = false;
		}
		
		return valido;
	}
	
	private void lancaAviso(String textoAviso, String tipoAtivo) {
		if (tipoAtivo.equals("WARRING")) {
			JsfUtil.addWarnMessage(textoAviso);
		} else if (tipoAtivo.equals("SUCESS")) {
			JsfUtil.addSuccessMessage(textoAviso);
		}
	}
	
	public void avisoPortaria() {
		lancaAviso("É preciso salvar a Portaria antes de imprimir.", "WARRING");
	}
	
	private void carregarOrgaos() {
		try {
			Orgao orgao = new Orgao();
			orgao.setIndExcluido(false);
			listaOrgaos = orgaoService.filtrarListaOrgaos(orgao);
			listaAreas = new ArrayList<Area>();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void mudaOrgao(ValueChangeEvent event) {
		this.orgao = (Orgao) event.getNewValue();
		carregarOrgaosPorArea();
	}
	
	public void mudaArea(ValueChangeEvent event) throws Exception {
		this.areaSelecionada = areaService.carregar((Area) event.getNewValue());
	}
	
	public void mudaArea(AjaxBehaviorEvent event) throws Exception {
		this.areaSelecionada = (Area) event.getComponent().getAttributes().get("value");
		this.areaSelecionada = areaService.carregar(areaSelecionada);
	}
	
	public void carregarOrgaosPorArea() {
		if (!Util.isNullOuVazio(this.orgao)) {
			try {
				Collection<Area> areasOrgao = new ArrayList<Area>();
				areasOrgao = areaService.filtrarAreasPorOrgao(this.orgao);
				Area areaDoUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getFuncionario().getIdeArea();
				
				if (isPautaTecnico) {
					if (areasOrgao.contains(areaDoUsuarioLogado)) {
						listaAreas.add(areaDoUsuarioLogado);
					}
					
					Processo processoProcurado = new Processo(vwConsultaProcesso.getIdeProcesso());
					ControleTramitacao tramitacaoAtual = controleTramitacaoService.buscarUltimoPorProcesso(processoProcurado);
					
					if (!listaAreas.contains(tramitacaoAtual.getIdeArea()) && areasOrgao.contains(tramitacaoAtual.getIdeArea())) {
						listaAreas.add(tramitacaoAtual.getIdeArea());
					}
					
					if (isDirucAreaSecundaria()) {
						Area coges = new Area(AreaEnum.COGES.getId());
						
						if (areasOrgao.contains(coges) && !listaAreas.contains(coges)) {
							for (Area area : areasOrgao) {
								
								if (area.equals(coges)) {
									listaAreas.add(area);
									break;
								}
							}
						}
					}
				} else {
					listaAreas = areasOrgao;
					listaAreas.remove(areaDoUsuarioLogado);
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public boolean isDirucAreaSecundaria() throws Exception {
		Collection<ControleTramitacao> controleTramitacao = 
				controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(vwConsultaProcesso.getIdeProcesso());
		
		if (!Util.isNullOuVazio(controleTramitacao) && controleTramitacao.size() > 1) {
			for (ControleTramitacao tramitacao : controleTramitacao) {
				
				if (!Util.isNull(tramitacao.getIndAreaSecundaria()) && tramitacao.getIndAreaSecundaria()) {
					return true;
				}
			}
		}
		
		Processo processo = new Processo(vwConsultaProcesso.getIdeProcesso());
		List<ProcessoAto> listaProcessoAto = processoAtoDAOImpl.listarAtosPorProcesso(processo);
		
		for (ProcessoAto processoAto : listaProcessoAto) {
			if (new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()).equals(processoAto.getAtoAmbiental())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void salvar() {
		fluxoAlternativoFacade.salvar(this);
	}
	
	public ControleTramitacao montarObjetoTramitacao() {
		ControleTramitacao tramitacao = new ControleTramitacao();
		tramitacao.setIdeProcesso(processo);
		tramitacao.setDscComentarioInterno(this.obs);
		tramitacao.setIdeArea(this.area);
		Integer idePessoaFisicaLogada = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
		tramitacao.setIdePessoaFisica(new PessoaFisica(idePessoaFisicaLogada));
		return tramitacao;
	}
	
	public ArquivoProcesso montarArquivo() {
		ArquivoProcesso arquivo = new ArquivoProcesso();
		arquivo.setIdeProcesso(this.processo);
		arquivo.setDscCaminhoArquivo(this.caminhoArquivo);
		return arquivo;
	}
	
	public void trataArquivo(FileUploadEvent event) {
		String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString());
		this.caminhoArquivo = caminho;
		temArquivo = true;
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
	}
	
	public boolean isReenquadramento() {
		return !Util.isNull(processoReenquadramentoDTO);
	}

	public boolean isVisualizaAreas() {
		if (listaAreas.isEmpty()) {
			visualizaAreas = false;
		} else {
			visualizaAreas = true;
		}
		return visualizaAreas;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Processo getProcesso() {
		return processo;
	}
	
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	
	public Collection<Orgao> getListaOrgaos() {
		return listaOrgaos;
	}
	
	public void setListaOrgaos(Collection<Orgao> listaOrgaos) {
		this.listaOrgaos = listaOrgaos;
	}
	
	public Collection<Area> getListaAreas() {
		return listaAreas;
	}
	
	public void setListaAreas(Collection<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	
	public Orgao getOrgao() {
		return orgao;
	}
	
	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public void setVisualizaAreas(boolean visualizaAreas) {
		this.visualizaAreas = visualizaAreas;
	}
	
	public String getObs() {
		return obs;
	}
	
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public VwConsultaProcesso getVwConsultaProcesso() {
		return vwConsultaProcesso;
	}
	
	public void setVwConsultaProcesso(VwConsultaProcesso vwConsultaProcesso) {
		this.vwConsultaProcesso = vwConsultaProcesso;
	}
	
	public boolean isPautaArea() {
		return isPautaArea;
	}
	
	public void setPautaArea(boolean isPautaArea) {
		this.isPautaArea = isPautaArea;
	}
	
	public Area getAreaSelecionada() {
		return areaSelecionada;
	}
	
	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}
	
	public boolean isPautaTecnico() {
		return isPautaTecnico;
	}
	
	public void setPautaTecnico(boolean isPautaTecnico) {
		this.isPautaTecnico = isPautaTecnico;
	}
	
	public String getTipoFluxo() {
		return tipoFluxo;
	}
	
	public void setTipoFluxo(String tipoFluxo) {
		this.tipoFluxo = tipoFluxo;
	}
	
	public boolean isVisualizarPortaria() {
		return visualizarPortaria;
	}
	
	public void setVisualizarPortaria(boolean visualizarPortaria) {
		this.visualizarPortaria = visualizarPortaria;
	}
	
	public Portaria getPortaria() {
		return portaria;
	}
	
	public void setPortaria(Portaria portaria) {
		this.portaria = portaria;
	}
	
	public Boolean getTemArquivo() {
		return temArquivo;
	}
	
	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}
	
	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}
	
	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}
	
	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}
	
	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
	
	public List<DocumentoObrigatorioEnquadramentoDTO> getListaDocumentoObrigatorioEnquadramento() {
		return listaDocumentoObrigatorioEnquadramento;
	}
	
	public void setListaDocumentoObrigatorioEnquadramento(List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramento) {
		this.listaDocumentoObrigatorioEnquadramento = listaDocumentoObrigatorioEnquadramento;
	}
}
