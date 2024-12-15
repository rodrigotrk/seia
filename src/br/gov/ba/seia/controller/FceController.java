package br.gov.ba.seia.controller;

import javax.ejb.EJB;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.interfaces.FceInterface;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * 04/10/13
 *
 * @author eduardo.fernandes
 */
public abstract class FceController implements FceInterface {

	@EJB
	protected FceServiceFacade fceServiceFacade;

	protected Fce fce;

	protected Requerimento requerimento;
	protected ProcessoReenquadramento processoReenquadramento;

	protected boolean desabilitarTudo;

	/**
	 * Método para ser implementado por cada FCE para que o técnico possa
	 * realizar à Análise.
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2015
	 */
	protected abstract void prepararDuplicacao();

	/**
	 * Método para ser implementado por cada FCE para que se duplique o FCE e o
	 * técnico possa analisa-lo.
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2015
	 */
	protected abstract void duplicarFce();

	/**
	 * Método para carregar o {@link Fce} preenchido na {@link AnaliseTecnica},
	 * similar ao método <i>init()</i>.
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2015
	 */
	protected abstract void carregarFceTecnico();

	/**
	 * Método para inicializar um novo {@link Fce} e setar os valores do
	 * {@link Requerimento} e o {@link DocumentoObrigatorio} a qual ele
	 * pertence.
	 *
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @author eduardo.fernandes
	 */
	public void iniciarFce(DocumentoObrigatorio documentoObrigatorio) {
		try {
			fce = new Fce(documentoObrigatorio, requerimento, null, processoReenquadramento);
			fceServiceFacade.ajustarFceParaReenquadramento(requerimento, fce, null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void iniciarFceTecnico(DocumentoObrigatorio documentoObrigatorio, AnaliseTecnica analiseTecnica) {
		try {
			fce = new Fce(documentoObrigatorio, requerimento, analiseTecnica, processoReenquadramento);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @author eduardo.fernandes
	 */
	public void carregarFceDoRequerente(DocumentoObrigatorio documentoObrigatorio) {
		try {
			DadoOrigemEnum dadoOrigem = fceServiceFacade.ajustarFceParaReenquadramento(requerimento, fce, null);
			fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, dadoOrigem);
			
			if (Util.isNullOuVazio(fce)){
				fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, DadoOrigemEnum.REQUERIMENTO);
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar o FCE.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega o {@link Fce} preenchido pelo Técnico durante a
	 * Análise Técnica do {@link Processo}.
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 07/12/2015
	 */
	public void carregarFceDoTecnico(DocumentoObrigatorio documentoObrigatorio) {
		try {
			fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, documentoObrigatorio, DadoOrigemEnum.TECNICO);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar o FCE para Análise Técnica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para salvar o FCE
	 *
	 * @param fce
	 * @author eduardo.fernandes
	 */
	public void salvarFce() throws Exception {
		if (!isFceTecnico()) {
			fceServiceFacade.ajustarFceParaReenquadramento(requerimento, fce, null);
		} else {
			fceServiceFacade.adicionarProcessoReenquadramento(requerimento, fce);
		}
		
		fceServiceFacade.salvarFce(fce);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 15/12/2015
	 */
	protected void concluirFce() throws Exception {
		fce.setIndConcluido(true);
		
		// Verificar se aquele FCE está sendo preenchido na Análise Técnica
		if (!Util.isNull(fce.getIdeAnaliseTecnica()) && ContextoUtil.getContexto().getUsuarioLogado().isTecnico()) {
			// Técnico responsável pela análise
			Funcionario tecnico = carregarFuncionarioByPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			fce.setIdePessoaFisica(tecnico);
			
			ProcessoAnaliseTecnicaController processoAnaliseTecnicaController = (ProcessoAnaliseTecnicaController) SessaoUtil.recuperarManagedBean("#{processoAnaliseTecnicaController}", ProcessoAnaliseTecnicaController.class);
			if(processoAnaliseTecnicaController != null){
				if(processoAnaliseTecnicaController.getListaDadoConcedido() != null && !processoAnaliseTecnicaController.getListaDadoConcedido().isEmpty()){
					
					for (DadoConcedidoInterface item : processoAnaliseTecnicaController.getListaDadoConcedido()) {
						if(item instanceof DadoConcedidoFceImpl){
							DadoConcedidoFceImpl fceImpl = (DadoConcedidoFceImpl) item;
							fceImpl.setFce(((DadoConcedidoFceImpl) item).getFce());
						}
					}
				}
			}
			RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlAnaliseTecnica");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("frmAnaliseTecnica:gridFce");
		}
		salvarFce();
	}

	@Deprecated
	public void excluirFce(Fce fce) {
		try {
			fceServiceFacade.excluirFce(fce);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para ser no controller espercifico do FCE, que será chamado via
	 * dialog sem parametro.
	 *
	 * @param fce
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @param caminhoArquivo
	 * @return Relatorio
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	public StreamedContent getImprimirRelatorio(Fce fce, DocumentoObrigatorio documentoObrigatorio) throws Exception {
		if (Util.isNull(fce)) {
			carregarFceDoTecnico(documentoObrigatorio);
			if (Util.isNull(this.fce)) {
				carregarFceDoRequerente(documentoObrigatorio);
			}
			return fceServiceFacade.getImprimirRelatorio(this.fce);
		}
		else {
			return fceServiceFacade.getImprimirRelatorio(fce);
		}
	}

	public void limparFce() {
		fce = null;
		desabilitarTudo = false;
	}

	public void duplicarFceAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica) {
		requerimento = fce.getIdeRequerimento();
		processoReenquadramento = fce.getIdeProcessoReenquadramento();
		inicializarFceAnaliseTecnica(fce, analiseTecnica);
		prepararDuplicacao();
		duplicarFce();
	}

	/**
	 * Método que carrega o {@link Funcionario} através da {@link PessoaFisica}.
	 *
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/01/2016
	 */
	private Funcionario carregarFuncionarioByPessoaFisica(PessoaFisica pessoaFisica)  {
		return fceServiceFacade.obterTecnico(pessoaFisica);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 09/12/2015
	 */
	protected void inicializarFceAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica) {
		init();
		iniciarFceTecnico(fce.getIdeDocumentoObrigatorio(), analiseTecnica);
	}

	public void abrirFce(Fce fce) {
		this.fce = fce;
		this.requerimento = fce.getIdeRequerimento();
		if(isFceTecnico()) {
			carregarFceTecnico();
		}
		else {
			init();
		}
		abrirDialog();
	}

	/**
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 001</b></li>
	 * <p>
	 * <i>Inclusão realizada com sucesso!</i>
	 * </p>
	 * 
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public void exibirMensagem001() {
		MensagemLacFceUtil.exibirMensagem001();
	}

	/**
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 002</b></li>
	 * <p>
	 * <i>Alteração realizada com sucesso!</i>
	 * </p>
	 *
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public void exibirMensagem002() {
		MensagemLacFceUtil.exibirMensagem002();
	}


	/**
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 003</b></li>
	 * <p>
	 * <i>[MSG-003] [nome do campo] é de preenchimento obrigatório.</i>
	 * </p>
	 *
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author AlexandreQueiroz
	 * @since 27/06/2016
	 */
	public void exibirMensagem003(String nomeDoCampo) {
		MensagemLacFceUtil.exibirMensagem003(nomeDoCampo);
	}


	/**
	 * 
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 004</b></li>
	 * <p>
	 * <i>Deseja realmente excluir o registro? </i>
	 * </p>
	 * 
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author alexandre.queiroz
	 * @since 27/06/2016
	 */
	public void exibirMensagem004() {
		MensagemLacFceUtil.exibirMensagem004();
	}

	/**
	 * 
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 005</b></li>
	 * <p>
	 * <i>Exclusão realizada com sucesso.</i>
	 * </p>
	 * 
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}. 
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public void exibirMensagem005() {
		MensagemLacFceUtil.exibirMensagem005();
	}

	/**
	 * 
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>MSG - 016</b></li>
	 * <p>
	 * <i>atualizado com sucesso!.</i>
	 * </p>
	 * 
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author alexandre.queiroz
	 * @since 27/06/2016
	 */
	public void exibirMensagem016(String nomeDoCampo) {
		MensagemLacFceUtil.exibirMensagem016(nomeDoCampo);
	}


	/**
	 * 
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 * 
	 * <li><b>INF - 001</b></li>
	 * <p>
	 * <i>Favor entrar em contato com o INEMA através do e-mail
	 * atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada
	 * seja realizado.</i>
	 * </p>
	 * 
	 * 
	 * refatorar, modificar os filhos para utilizarem {@link MensagemLacFceUtil}.
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public void exibirInformacao001() {
		MensagemLacFceUtil.exibirInformacao001();
	}

	// Get && Set
	public Fce getFce() {
		return fce;
	}

	public boolean isDesabilitarTudo() {
		return desabilitarTudo;
	}
	
	public void setDesabilitarTudo(boolean desabilitarTudo) {
		this.desabilitarTudo = desabilitarTudo;
	}

	public void setDesabilitarTudo() {
		this.desabilitarTudo = true;
	}
	
	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public boolean isFceSalvo() {
		return !Util.isNullOuVazio(fce);
	}

	public boolean isFceTecnico() {
		return !Util.isNull(fce) && fce.isFceTecnico();
	}

	public String msgImprimirRelatorio(String nomFce) {
		if(isFceTecnico()) {
			return Util.getString("geral_msg_imprimir_relatorio_dados_concedidos_fce");
		}
		return Util.getString("geral_msg_imprimir_relatorio_fce") + " " + nomFce + "?";
	}

	public String headerFce(String nomeFce){
		if(isFceTecnico()){
			return Util.getString("analise_tecnica_lbl_header_dados_concedidos") + nomeFce.substring(3);
		}
		return nomeFce;
	}
}