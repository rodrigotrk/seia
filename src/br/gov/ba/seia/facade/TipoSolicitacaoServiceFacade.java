package br.gov.ba.seia.facade;

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
import javax.faces.event.ActionEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.IdentificarPapelController;
import br.gov.ba.seia.controller.IdentificarTipoSolicitacaoController;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
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
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSolicitacaoServiceFacade {
	
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
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoAposEndereco(ActionEvent event, IdentificarTipoSolicitacaoController identificarTipoSolicitacaoController, IdentificarPapelController identificarPapelController) {
	
		Requerimento requerimento = identificarTipoSolicitacaoController.getRequerimento();
		Empreendimento empreendimento = identificarTipoSolicitacaoController.getEmpreendimento();
		Pessoa pessoa = identificarTipoSolicitacaoController.getPessoa();
		
		try {
			if (identificarTipoSolicitacaoController.isRequerentePJ()) {
				identificarPapelController.setPessoaJuridica(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica());
			}
			
			if (identificarTipoSolicitacaoController.isRequerentePF() || (identificarTipoSolicitacaoController.isRequerentePJ() && identificarPapelController.validarPJ())) {
				
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
	
						return;
					} else {
						identificarTipoSolicitacaoController.setDesabilitarTudo(false);
					}
					
					boolean requerimentoAberto = novoRequerimentoService.verificaRequerimentoEmpreendimentoExistente(empreendimento);
					
					if (requerimentoAberto) {
						identificarTipoSolicitacaoController.setDesabilitarTudo(false);
						identificarTipoSolicitacaoController.setMostraQuestionarioAposSalvar(false);
						JsfUtil.addWarnMessage("Não será possível abrir um novo requerimento para este empreendimento, pois o mesmo já apresenta um requerimento em aberto aguardando a formação do processo.");
						return;
					}
					
					identificarTipoSolicitacaoController.setMostraQuestionarioAposSalvar(Boolean.TRUE);
					Requerimento temp = novoRequerimentoService.carregarRequerimentoIncompleto(empreendimento);
					
					if (temp != null) {
						if (!tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(temp.getIdeRequerimento()).getIdeStatusRequerimento().getIdeStatusRequerimento()
								.equals(StatusRequerimentoEnum.CANCELADO.getStatus())) {
							if (!Util.isNullOuVazio(temp) && Util.isNullOuVazio(temp.getNumRequerimento())) {
								requerimento.setIdeRequerimento(temp.getIdeRequerimento());
								identificarTipoSolicitacaoController.getRequerimentoSelecionado().setIdeRequerimento(requerimento.getIdeRequerimento());
								identificarTipoSolicitacaoController.setModoEdicao(true);
							}
						}
					}
					
					temp = null;
					
					novoRequerimentoService.removerRequerimentosIncompletos(empreendimento.getIdeEmpreendimento());
					
					if (identificarTipoSolicitacaoController.getEnderecoContato()) {
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
							
							identificarTipoSolicitacaoController.getPergunta0_1().setDtcResposta(new Date());
							identificarTipoSolicitacaoController.getPergunta0_1().setIdeRequerimento(requerimento);
							identificarTipoSolicitacaoController.getPergunta0_1().setIndResposta(false);
							perguntaRequerimentoService.salvarOuAtualizarPerguntaReq(identificarTipoSolicitacaoController.getPergunta0_1());
							
							JsfUtil.addSuccessMessage("Requerimento salvo com sucesso.");
						}
					}
				} else {
					JsfUtil.addWarnMessage("Selecione um Empreendimento já cadastrado!");
				}
			}
		} catch (Exception e) {
			identificarTipoSolicitacaoController.setMostraQuestionarioAposSalvar(false);
			identificarTipoSolicitacaoController.setDesabilitarTudo(false);
			identificarTipoSolicitacaoController.setModoEdicao(false);
			identificarTipoSolicitacaoController.getRequerimento().setIdeRequerimento(null);
			JsfUtil.addErrorMessage("Ocorreu um erro ao tentar salvar o requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void pessoasRequerimento(Requerimento requerimento, Empreendimento empreendimento, Pessoa pessoa) {
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
	
	public void adicionarTramitacao(StatusRequerimentoEnum status, Requerimento requerimento, Pessoa pessoa) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(pessoa);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}
	
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(Tipologia tipologia, AtoAmbiental atoAmbiental){
		return tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologiaAndAto(new EnquadramentoAtoAmbiental(tipologia, atoAmbiental));
	}
}