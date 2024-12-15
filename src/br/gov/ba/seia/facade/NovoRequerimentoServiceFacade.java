package br.gov.ba.seia.facade;

import java.util.ArrayList;
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
import javax.faces.event.ActionEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.IdentificarPapelController;
import br.gov.ba.seia.controller.IdentificarTipoSolicitacaoController;
import br.gov.ba.seia.controller.NovoRequerimentoController;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.ReportUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NovoRequerimentoServiceFacade {
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private NovoRequerimentoService novoRequerimentoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoAposEndereco(ActionEvent event, NovoRequerimentoController novoRequerimentoController, IdentificarPapelController identificarPapelController) {
	
		Requerimento requerimento = novoRequerimentoController.getRequerimento();
		Empreendimento empreendimento = novoRequerimentoController.getEmpreendimento();
		Pessoa pessoa = novoRequerimentoController.getPessoa();
		
		try {
			if (novoRequerimentoController.isRequerentePJ()) {
				identificarPapelController.setPessoaJuridica(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica());
			}
			
			if (novoRequerimentoController.isRequerentePF() || (novoRequerimentoController.isRequerentePJ() && identificarPapelController.validarPJ())) {
				
				boolean valoresContatoValidados = true;
				
				if (Util.isNullOuVazio(requerimento.getNomContato())) {
					valoresContatoValidados = false;
					JsfUtil.addWarnMessage("Nome inválido! ");
				}
				
				if (Util.isNullOuVazio(requerimento.getNumTelefone())) {
					valoresContatoValidados = false;
					JsfUtil.addWarnMessage("Telefone inválido! ");
				}
				
				if (Util.isNullOuVazio(requerimento.getDesEmail()) || !Util.validaEmail(requerimento.getDesEmail())) {
					valoresContatoValidados = false;
					JsfUtil.addWarnMessage("E-mail inválido! ");
				}
				
				if (!valoresContatoValidados) return;
				
				if (!Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())) {
					
					if (!empreendimentoService.isEmpreendimentoValido(empreendimento)) {
						// desabilitarTudo = true;
						
						novoRequerimentoController.setVisualizaSelectEmpreendimento(true);
						// visualizaSelectEmpreendimento = true;
						novoRequerimentoController.setVisualizaSelectEmpreendimento(false);
						// mostraQuestionarioAposSalvar = false;
						return;
						// JsfUtil.addErrorMessage(BUNDLE.getString("empreendimento_msg_invalido"));
					} else {
						novoRequerimentoController.setDesabilitarTudo(false);
						// desabilitarTudo = false;
					}
					
					boolean requerimentoAberto = novoRequerimentoService.verificaRequerimentoEmpreendimentoExistente(empreendimento);
					
					if (requerimentoAberto) {
						novoRequerimentoController.setDesabilitarTudo(false);
						novoRequerimentoController.setMostraQuestionarioAposSalvar(false);
						JsfUtil.addWarnMessage("Não será possível abrir um novo requerimento para este empreendimento, pois o mesmo já apresenta um requerimento em aberto aguardando a formação do processo.");
						return;
					}
					
					novoRequerimentoController.setMostraQuestionarioAposSalvar(Boolean.TRUE);
					Requerimento temp = novoRequerimentoService.carregarRequerimentoIncompleto(empreendimento);
					
					if (temp != null) {
						if (!tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(temp.getIdeRequerimento()).getIdeStatusRequerimento().getIdeStatusRequerimento()
								.equals(StatusRequerimentoEnum.CANCELADO.getStatus())) {
							if (!Util.isNullOuVazio(temp) && Util.isNullOuVazio(temp.getNumRequerimento())) {
								requerimento.setIdeRequerimento(temp.getIdeRequerimento());
								novoRequerimentoController.getRequerimentoSelecionado().setIdeRequerimento(requerimento.getIdeRequerimento());
								novoRequerimentoController.setModoEdicao(true);
							}
						}
					}
					
					temp = null;
					
					novoRequerimentoService.removerRequerimentosIncompletos(empreendimento.getIdeEmpreendimento());
					
					if (novoRequerimentoController.getEnderecoContato()) {
						requerimento.setIdeEnderecoContato(ContextoUtil.getContexto().getRequerimentoEndereco());
					} else {
						requerimento.setIdeEnderecoContato(null);
					}
					
					Orgao orgao = new Orgao();
					orgao.setIdeOrgao(1);
					orgao = requerimentoUnicoService.recuperarOrgao(orgao);
					
					TipoRequerimento tipoRequerimento = new TipoRequerimento();
					tipoRequerimento.setIdeTipoRequerimento(1);
					requerimento.setIdeOrgao(orgao);
					requerimento.setIdeTipoRequerimento(tipoRequerimento);
					requerimento.setDtcCriacao(new Date());
					requerimento.setIndDeclaracao(true);
					requerimento.setIndExcluido(false);
					
					if (!Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())) {
						requerimento.setEmpreendimentoRequerimentoCollection(new ArrayList<EmpreendimentoRequerimento>());
						EmpreendimentoRequerimento er = new EmpreendimentoRequerimento();
						er.setIdeEmpreendimento(empreendimento);
						er.setIdeRequerimento(requerimento);
						requerimento.getEmpreendimentoRequerimentoCollection().add(er);
					}
					
					adicionarTramitacao(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO, requerimento, pessoa);
					
					pessoasRequerimento(requerimento, empreendimento, pessoa);
					
					if (!Util.isNullOuVazio(empreendimento)) {
						
						if (!Util.isNullOuVazio(requerimento) 
								&& !Util.isNullOuVazio(requerimentoService.existeRequerimento(requerimento))
								&& requerimentoUnicoService.existeEmpreendimentoRequerimento(requerimento)) {
							
							requerimentoUnicoService.atualizarRequerimento(requerimento);
							JsfUtil.addSuccessMessage("Requerimento atualizado com sucesso.");
						} else {
							requerimentoUnicoService.inserirRequerimento(requerimento);
							
							novoRequerimentoController.getPergunta0_1().setDtcResposta(new Date());
							novoRequerimentoController.getPergunta0_1().setIdeRequerimento(requerimento);
							novoRequerimentoController.getPergunta0_1().setIndResposta(false);
							perguntaRequerimentoService.salvarOuAtualizarPerguntaReq(novoRequerimentoController.getPergunta0_1());
							
							JsfUtil.addSuccessMessage("Requerimento salvo com sucesso.");
						}
					}
				} else {
					JsfUtil.addWarnMessage("Selecione um Empreendimento já cadastrado!");
				}
			}
		} catch (Exception e) {
			novoRequerimentoController.setMostraQuestionarioAposSalvar(false);
			novoRequerimentoController.setDesabilitarTudo(false);
			novoRequerimentoController.setModoEdicao(false);
			novoRequerimentoController.getRequerimento().setIdeRequerimento(null);
			JsfUtil.addErrorMessage("Ocorreu um erro ao tentar salvar o requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void pessoasRequerimento(Requerimento requerimento, Empreendimento empreendimento, Pessoa pessoa) {
		try {

			List<RequerimentoPessoa> pessoasRequerimentos = new ArrayList<RequerimentoPessoa>();
			RequerimentoPessoa reqPessoaAtendente = ContextoUtil.getContexto().getReqPapeisDTO().getAtendente();
			RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			RequerimentoPessoa reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();
			
			pessoasRequerimentos.add(reqPessoaRequerente);
			
			if (!reqPessoaRequerente.isIndSolicitante()) {
				pessoasRequerimentos.add(reqPessoaSolicitante);
			}
			if (!reqPessoaRequerente.isIndUsuarioLogado() && !reqPessoaSolicitante.isIndUsuarioLogado()) {
				pessoasRequerimentos.add(reqPessoaAtendente);
			}
			
			List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = requerimentoUnicoService.recuperarProcuradorPessoaFisicaAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa,empreendimento);
			for (ProcuradorPessoaFisica procuradorPessoaFisica : collProcuradorPessoaFisica) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
				pessoasRequerimentos.add(requerimentoPessoa);
			}
			
			List<ProcuradorRepresentante> collProcuradorRepresentante = requerimentoUnicoService.recuperarProcuradorRepresentanteAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa,empreendimento);
			for (ProcuradorRepresentante procuradorRepresentante : collProcuradorRepresentante) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorRepresentante.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
				
				boolean temPessoaRequerimento = false;
				
				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento()) && reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
						temPessoaRequerimento = true;
						break;
					}
				}
				
				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}
			
			List<RepresentanteLegal> collRepresentanteLegal = requerimentoUnicoService.recuperarRepresentanteLegalAssinaturaObrigatoriaPorRequerente(pessoa);
			for (RepresentanteLegal representantelegal : collRepresentanteLegal) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(representantelegal.getIdePessoaFisica().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
				
				boolean temPessoaRequerimento = false;
				
				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
						temPessoaRequerimento = true;
						break;
					}
				}
				
				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}
			
			for (RequerimentoPessoa reqPess : pessoasRequerimentos) {
				reqPess.setRequerimento(requerimento);
			}
			
			requerimento.setRequerimentoPessoaCollection(pessoasRequerimentos);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void adicionarTramitacao(StatusRequerimentoEnum status, Requerimento requerimento, Pessoa pessoa) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(pessoa);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}
	
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(Tipologia tipologia, AtoAmbiental atoAmbiental) throws Exception{
		return tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologiaAndAto(new EnquadramentoAtoAmbiental(tipologia, atoAmbiental));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String carregarReenquadramento(IdentificarTipoSolicitacaoController ctrl, Requerimento requerimento) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("requerimento", requerimento);
		params.put("first", 0);
		params.put("pageSize", 10);
		
		params.put("isPagination", true);
		List<RequerimentoDTO> lista = novoRequerimentoService.consultaRequerimento(params);
		
		RequerimentoDTO requerimentoDTO = null;
		if (!Util.isNullOuVazio(lista)){
			requerimentoDTO = lista.get(0);
			requerimentoDTO.setVisualizar(Boolean.FALSE);
			requerimentoDTO.setUrlVoltar("/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml");
			ContextoUtil.getContexto().setObject(requerimentoDTO);
		}
		
		gerarResumoRequerimentoPdf(requerimento);
		
		return ctrl.redirecionarVisualizacao(requerimentoDTO);
	}
	
	
	
	public void gerarResumoRequerimentoPdf(Requerimento requerimento) {
		try {
			Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimentoRetornandoImoveis(requerimento.getIdeRequerimento());
			List<Imovel> listaImovel = (List<Imovel>) empreendimento.getImovelCollection();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
			
			if(this.requerimentoService.isRequerimentoAntigo(requerimento.getIdeRequerimento())){
				RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
				lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			}else{
				new ReportUtil().salvar("/opt/ARQUIVOS/REQUERIMENTO/", "aa.pdf", "resumo_requerimento.jasper", params);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String carregarReenquadramento(IdentificarTipoSolicitacaoController ctrl, ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		Requerimento requerimento = requerimentoService.buscarEntidadeCarregadaPorId(processoReenquadramentoDTO.getRequerimento());
		
		if (Util.isNullOuVazio(requerimento.getRequerimentoUnico())){
			if (Util.isNullOuVazio(requerimento.getDesCaminhoResumoOriginal())){
				String caminho = gerarResumoRequerimentoPdfReenquadramento(requerimento);
				
				requerimento.setDesCaminhoResumoOriginal(caminho);
				requerimento.setDtcFinalizacaoReenquadramento(new Date());
				
				requerimentoService.atualizarRequerimento(requerimento);
			}
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("requerimento", processoReenquadramentoDTO.getRequerimento());
			params.put("first", 0);
			params.put("pageSize", 10);
			
			params.put("isPagination", true);
			List<RequerimentoDTO> lista = novoRequerimentoService.consultaRequerimento(params);
			
			RequerimentoDTO requerimentoDTO = null;
			if (!Util.isNullOuVazio(lista)){
				requerimentoDTO = lista.get(0);
				requerimentoDTO.setUrlVoltar("/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml");
				requerimentoDTO.setProcessoReenquadramentoDTO(processoReenquadramentoDTO);
				ContextoUtil.getContexto().setObject(requerimentoDTO);
				if(!processoReenquadramentoDTO.isVisualizar() && Boolean.TRUE.equals(processoReenquadramentoDTO.getProcessoReenquadramento().getIndAceiteRequerente())) {
					requerimentoDTO.setVisualizar(false);
				}
				else {
					requerimentoDTO.setVisualizar(true);
				}
			}
			
			return ctrl.redirecionarVisualizacao(requerimentoDTO);
		}
		
		return null;
	}
	
	public String gerarResumoRequerimentoPdfReenquadramento(Requerimento requerimento) {
		try {
			Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimentoRetornandoImoveis(requerimento.getIdeRequerimento());
			List<Imovel> listaImovel = (List<Imovel>) empreendimento.getImovelCollection();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
			
			RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);

			String caminho = "/opt/ARQUIVOS/REQUERIMENTO/".concat(requerimento.getIdeRequerimento().toString()).concat("/");
			String nomeArquivo = "resumo_requerimento_original_".concat(requerimento.getIdeRequerimento().toString()).concat(".pdf");
			
			return lRelatorio.salvar(
					RelatorioUtil.RELATORIO_PDF, 
					true, 
					this.requerimentoService.isRequerimentoAntigo(requerimento.getIdeRequerimento()), 
					caminho, 
					nomeArquivo
				);
	
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public ProcessoAto buscarProcessoAtoPorProcessoEAtoAmbientalETipologia(Processo processo, AtoAmbiental atoAmbiental, Tipologia ideTipologia){
		try {
			return processoAtoService.getProcessoAtoByProcessoByAtoAmbiental(processo, atoAmbiental, ideTipologia);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}