package br.gov.ba.seia.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DateSelectEvent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.LicencaObjetoAlteracao;
import br.gov.ba.seia.entity.ObjetoAlteracao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.ObjetoAlteracaoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.TipoSolicitacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("renovarAlterarLicencaController")
@ViewScoped
public class RenovarAlterarLicencaController extends BaseDialogController {
	
	@Inject
	private AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoAlteracaoProrrogacaoController;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private TipoSolicitacaoService tipoSolicitacaoService;
	
	@EJB
	RequerimentoTipologiaService requerimentoTipologiaService;
	
	@EJB
	ProcessoService processoService;
	
	@EJB
	private ObjetoAlteracaoService objetoAlteracaoService;
	
	@EJB
	private CertificadoService certificadoService;

	private Licenca licenca;
	
	private PerguntaRequerimento perguntaNR_A2_DRENALT_P11;
	
	private PerguntaRequerimento perguntaNR_A2_DRENALT_P12;
	
	private Collection<TipoSolicitacao> tiposSolicitacao;

	private List<ObjetoAlteracao> objetosAlteracao;

	private String strInformativa;
	
	private Processo processo;
	
	private Certificado certificado;
	
	private String dataValidadeCertificado;
	
	private boolean disableTipoSolicitacao;
	
	private boolean renovacaoLicenca;
	
	private boolean desabilitarTudo;
	
	@Override
	public void load() {
		try {
			this.limpar();
			this.carregarPerguntas();
			this.carregarListas();
			super.editMode = false;
			if(abaRenovacaoAlteracaoProrrogacaoController.isAlteracaoLicenca()){
				selecionaTipoSolicitacao(TipoSolicitacaoEnum.ALTERACAO_LICENCA.getId());
				disableTipoSolicitacao = true;
			}else if(abaRenovacaoAlteracaoProrrogacaoController.isRenovacaoLicenca()){
				selecionaTipoSolicitacao(TipoSolicitacaoEnum.RENOVACAO_LICENCA.getId());
				disableTipoSolicitacao = true;
			}
			
			this.desabilitarTudo = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	/**
	 * Define valor para a propriedade tipoSolicitacao da variavel "licenca" atraves da lista tiposSolicitacao a qual é a lista que é exibida no selectOneRadio
	 * @author micael.coutinho
	 * @param idTipoSolicitacao
	 */
	public void selecionaTipoSolicitacao(Integer idTipoSolicitacao) {
		for (TipoSolicitacao tipoSolicit : tiposSolicitacao) {
			if(tipoSolicit.getIdeTipoSolicitacao().equals(idTipoSolicitacao)){
				licenca.setTipoSolicitacao(tipoSolicit);
			}
		}
	}

	@Override
	public <T> void editar(T licenca) {
		try {
			this.licenca = ((Licenca) licenca).clone();
			this.carregarPerguntas();
			this.carregarRespostas();
			this.carregarListas();
			this.carregaObjetosAlteracao();
			super.editMode = true;
			
			if (!Util.isNull(this.licenca) && !Util.isNull(this.licenca.getTipoSolicitacao()) && this.licenca.getTipoSolicitacao().isRenovacaoLicenca()) {
				this.carregarProcessoEVerificarRLAC();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public <T> void visualizar(T licenca) {
		try {
			
			this.desabilitarTudo = true;
			
			this.licenca = ((Licenca) licenca).clone();
			this.carregarPerguntas();
			this.carregarRespostas();
			this.carregarListas();
			this.carregaObjetosAlteracao();
			super.editMode = true;
			
			if (!Util.isNull(this.licenca) && !Util.isNull(this.licenca.getTipoSolicitacao()) && this.licenca.getTipoSolicitacao().isRenovacaoLicenca()) {
				this.carregarProcessoEVerificarRLAC();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	protected void carregarPerguntas(){
		
		try {
			super.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
			
			this.perguntaNR_A2_DRENALT_P11 = super.carregarPerguntaByCod("NR_A2_DRENALT_P11");
			this.perguntaNR_A2_DRENALT_P12 = super.carregarPerguntaByCod("NR_A2_DRENALT_P12");
			
			super.listaPerguntasRequerimento.add(this.perguntaNR_A2_DRENALT_P11);
			super.listaPerguntasRequerimento.add(this.perguntaNR_A2_DRENALT_P12);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarRespostas() {
		try {
			
			this.perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(this.listaPerguntasRequerimento, this.getRequerimento(),this.licenca);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListas() throws Exception {
		this.tiposSolicitacao = this.tipoSolicitacaoService.listarTipoSolicitacaoLicenca();
		this.objetosAlteracao = this.objetoAlteracaoService.listaTodosAtivos();
	}
	
	@Override
	public void limpar() {
		this.licenca = new Licenca(super.getRequerimento());
		this.objetosAlteracao = new ArrayList<ObjetoAlteracao>();
		super.editMode = false;
		disableTipoSolicitacao = false;
		processo = null;
		renovacaoLicenca = false;
		certificado = null;
		dataValidadeCertificado = null;
	}

	private void carregaObjetosAlteracao() {
		try {
			Collection<ObjetoAlteracao> objetosSelecionados = objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(this.licenca);
			
			for (ObjetoAlteracao objetoAlteracao : this.objetosAlteracao) {
				if(objetosSelecionados.contains(objetoAlteracao))
					objetoAlteracao.setRowSelect(true);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar a lista de objetos do banco");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}
	
	public void valueChangeTipoSolicitacao(ValueChangeEvent event) {
		this.perguntaNR_A2_DRENALT_P11.setIndResposta(null);
		this.perguntaNR_A2_DRENALT_P12.setIndResposta(null);
		
		licenca.setNumProcessoLicenca(null);
		licenca.setNumPortariaLicenca(null);
		licenca.setDataPublicacaoPortaria(null);
		licenca.setDataValidadeLicenca(null);
		
		processo = null;
		renovacaoLicenca = false;
		certificado = null;
		dataValidadeCertificado = null;
	}

	public void valueChangePerguntaNR_A2_DRENALT_P11(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		if (!resposta) {
			this.perguntaNR_A2_DRENALT_P12.setIndResposta(null);
		}
	}
	
	public void valueChangeDataPublicacao(DateSelectEvent event) {
		try {
			if (event.getDate().before(new Date())) {
				this.licenca.setDataPublicacaoPortaria(event.getDate());
			} else {
				JsfUtil.addErrorMessage("A data informada não pode ser maior que a atual.");
				this.licenca.setDataPublicacaoPortaria(null);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Data inválida!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void valueChangeDataValidade(DateSelectEvent event) {
		try {
			this.licenca.setDataValidadeLicenca(event.getDate());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Data inválida!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void salvar() {
		try {
			if (validar()) {
				Licenca licencaParaSalvar = licenca.clone();
				
				this.licencaService.salvarLicenca(licencaParaSalvar);
				
				for (PerguntaRequerimento pergunta : this.listaPerguntasRequerimento) {
					pergunta.setIdeLicenca(licencaParaSalvar);
				}
				
				super.perguntaRequerimentoService.salvaListPerguntaRequerimento((List<PerguntaRequerimento>) this.listaPerguntasRequerimento, super.getRequerimento());
				
				this.excluirObjetosAlteracao(licencaParaSalvar);
				
				if(!Util.isNullOuVazio(this.perguntaNR_A2_DRENALT_P12.getIndResposta()) && this.perguntaNR_A2_DRENALT_P12.getIndResposta()) {
					this.salvarObjetosAlteracao(licencaParaSalvar);
				}
				
				this.abaRenovacaoAlteracaoProrrogacaoController.adicionarOuAtualizarLicenca(licencaParaSalvar);
				
				if(abaRenovacaoAlteracaoProrrogacaoController.isAlteracaoLicenca()) {
					requerimentoTipologiaService.removerByRequerimento(super.getRequerimento().getIdeRequerimento());
				}
				
				if(this.editMode) {
					JsfUtil.addSuccessMessage("Licença atualizada com sucesso.");
				} else {
					JsfUtil.addSuccessMessage("Licença salva com sucesso.");
				}
				
				RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId:formAbaRenovacao");
				RequestContext.getCurrentInstance().execute("dialogIncluirLicenca.hide()");
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao salvar a licença:");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}
	
	@Override
	public boolean validar() {
		
		boolean valido = true;
		
		if (Util.isNullOuVazio(this.licenca.getTipoSolicitacao())) {
			JsfUtil.addWarnMessage("Por favor, selecione uma das opções no campo 1.");
			valido = false;
			
		} else if (this.licenca.getTipoSolicitacao().isAlteracaoLicenca()) {
			
			if (Util.isNullOuVazio(this.perguntaNR_A2_DRENALT_P11.getIndResposta())) {
				JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.1.");
				valido = false;
			} 

			if (Util.isNullOuVazio(this.perguntaNR_A2_DRENALT_P12.getIndResposta())) {
					JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 1.2.");
					valido = false;
			}
			
			boolean existeObjetoAlteracao = false;
			for (ObjetoAlteracao objetoAlteracao : this.objetosAlteracao) {
				if(objetoAlteracao.isRowSelect()){
					existeObjetoAlteracao = true;
				}
			}
			
			if(!Util.isNull(this.perguntaNR_A2_DRENALT_P11) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P11.getIndResposta()) 
					&& !this.perguntaNR_A2_DRENALT_P11.getIndResposta() 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12.getIndResposta()) 
					&& !this.perguntaNR_A2_DRENALT_P12.getIndResposta()) {
				
				strInformativa= "De acordo com o que determina o § 1º do Art. 116 do Decreto 14.024/2012, alterado pelo decreto 14.032/2012,"
							  + " tendo sido negativas as respostas aos itens 1.1 e 1.2, a solicitação não se configura como licença de alteração.";
				valido = false;
				RequestContext.getCurrentInstance().addPartialUpdateTarget("idConfirmDiagAlterarLicenca");
				RequestContext.getCurrentInstance().execute("confirmDiagAlterarLicenca.show()");
				
				return valido;
				
			} else if(!Util.isNull(this.perguntaNR_A2_DRENALT_P11)
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P11.getIndResposta()) 
					&& this.perguntaNR_A2_DRENALT_P11.getIndResposta() 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12.getIndResposta()) 
					&& !this.perguntaNR_A2_DRENALT_P12.getIndResposta()) {
				
				strInformativa= "De acordo com o que determina o § 1º do Art. 116 do Decreto 14.024/2012, alterado pelo decreto 14.032/2012, se há agravamento dos impactos ambientais,"
							  + " mas a alteração não está incluída no mesmo objeto da atividade/empreendimento licenciado, a solicitação deve ser de uma nova licença, e não de uma Licença de alteração.";
				valido = false;
				RequestContext.getCurrentInstance().addPartialUpdateTarget("idConfirmDiagAlterarLicenca");
				RequestContext.getCurrentInstance().execute("confirmDiagAlterarLicenca.show()");
				
				return valido;
			}
			
			if(!Util.isNull(this.perguntaNR_A2_DRENALT_P11) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P11.getIndResposta()) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12) 
					&& !Util.isNull(this.perguntaNR_A2_DRENALT_P12.getIndResposta()) 
					&& !existeObjetoAlteracao){
				
				JsfUtil.addWarnMessage("Por favor, selecione uma opção na pergunta 1.2.1.");
				valido = false;
			}
			
			if (Util.isNullOuVazio(this.licenca.getNumProcessoLicenca())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
				valido = false;
			} else {
				if (!Util.validaTamanhoString(this.licenca.getNumProcessoLicenca(), 50)) {
					valido = false;
					JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
				}
			}
			
			if (Util.isNullOuVazio(this.licenca.getNumPortariaLicenca())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
				valido = false;
			} else {
				if (!Util.validaTamanhoString(this.licenca.getNumPortariaLicenca(), 50)) {
					valido = false;
					JsfUtil.addWarnMessage("O campo 3. só aceita 50 caracteres");
				}
			}
			
			if (this.licenca.getDataPublicacaoPortaria() == null) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 4.");
				valido = false;
			}
			
			if (this.licenca.getDataValidadeLicenca() == null) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
				valido = false;
			} else if (this.licenca.getDataValidadeLicenca().before(this.licenca.getDataPublicacaoPortaria())) {
				JsfUtil.addErrorMessage("A data de validade não pode ser inferior a data de publicação.");
				valido = false;
			}
		} else if(!renovacaoLicenca) {
			if (Util.isNullOuVazio(this.licenca.getNumProcessoLicenca())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
				valido = false;
			} else {
				if (!Util.validaTamanhoString(this.licenca.getNumProcessoLicenca(), 50)) {
					valido = false;
					JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
				}
			}
			
			if (Util.isNullOuVazio(this.licenca.getNumPortariaLicenca())) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
				valido = false;
			} else {
				if (!Util.validaTamanhoString(this.licenca.getNumPortariaLicenca(), 50)) {
					valido = false;
					JsfUtil.addWarnMessage("O campo 3. só aceita 50 caracteres");
				}
			}
			
			if (this.licenca.getDataPublicacaoPortaria() == null) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 4.");
				valido = false;
			}
			
			if (this.licenca.getDataValidadeLicenca() == null) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
				valido = false;
			} else if (this.licenca.getDataValidadeLicenca().before(this.licenca.getDataPublicacaoPortaria())) {
				JsfUtil.addErrorMessage("A data de validade não pode ser inferior a data de publicação.");
				valido = false;
			}
		}
		
		return valido;
	}

	private void excluirObjetosAlteracao(Licenca licenca) {
		try {
			this.objetoAlteracaoService.excluirLicencaObjetoAlteracaoByLicenca(licenca);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao excluir Licenca Objeto Alteração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarObjetosAlteracao(Licenca licenca) {
		try {
			for (ObjetoAlteracao objetoAlteracao : this.objetosAlteracao) {
				if (objetoAlteracao.isRowSelect()) {
					LicencaObjetoAlteracao licencaObjAlteracao = new LicencaObjetoAlteracao(licenca, objetoAlteracao);
					this.objetoAlteracaoService.salvarLicencaObjetoAlteracao(licencaObjAlteracao);
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar Licença Objeto Alteração");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarProcessoEVerificarRLAC() {
		Collection<AtoAmbiental> atos;
		renovacaoLicenca = false;
		
		try {
			if(!Util.isNull(licenca) && !Util.isNullOuVazio(licenca.getNumProcessoLicenca())) {
				processo = processoService.buscarProcessoPorCriteria(licenca.getNumProcessoLicenca());
			
				if(!Util.isNull(processo) && !Util.isNull(processo.getIdeRequerimento())) {
					atos = processoService.obterListaAtosAmbientais(processo.getIdeProcesso());
					
					if(!Util.isNullOuVazio(atos)) {
						for (AtoAmbiental ato : atos) {
							if(ato.isLac()) {
								
								certificado = certificadoService.carregarByIdRequerimentoAndAtoLac(processo.getIdeRequerimento().getIdeRequerimento());
								
								if(!Util.isNull(certificado)) {
									GregorianCalendar calendar = new GregorianCalendar();
									calendar.setTime(certificado.getDtcEmissaoCertificado());
									
									SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
									dataValidadeCertificado = sdf.format(calendar.getTime());
									
									renovacaoLicenca = true;
									licenca.setNumPortariaLicenca(null);
									licenca.setDataPublicacaoPortaria(null);
									licenca.setDataValidadeLicenca(null);
								}
							}
						}
					}
				} else {
					processo = new Processo();
				}
			} else {
				processo = null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public List<ObjetoAlteracao> getObjetosAlteracao(){
		try {
			if(perguntaNR_A2_DRENALT_P11.getIndResposta() && !Util.isNull(this.objetosAlteracao) && this.objetosAlteracao.size()<=1){
				this.objetosAlteracao = this.objetoAlteracaoService.listaTodosAtivos();
				if(editMode)
					this.carregaObjetosAlteracao();
				
			}else if(!perguntaNR_A2_DRENALT_P11.getIndResposta() && perguntaNR_A2_DRENALT_P12.getIndResposta()){
				for (ObjetoAlteracao objAlteracao : objetosAlteracao) {
					if(objAlteracao.getIdeObjetoAlteracao().intValue() == 2){
						objetosAlteracao = new ArrayList<ObjetoAlteracao>();
						objAlteracao.setRowSelect(true);
						objetosAlteracao.add(objAlteracao);
					}
				}
			}
		} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return objetosAlteracao;
	}
	
	public List<ObjetoAlteracao> getObjetosAlteracaoSelecionados(){
		List<ObjetoAlteracao> lista = new ArrayList<ObjetoAlteracao>();
		if(Util.isNullOuVazio(objetosAlteracao)){
			this.carregaObjetosAlteracao();
		}
		for (ObjetoAlteracao objAlteracao : objetosAlteracao) {
			if(objAlteracao.isRowSelect()){
				lista.add(objAlteracao);
			}
		}
		return lista;
	}
	
	public Licenca getLicenca() {
		return licenca;
	}

	public void setLicenca(Licenca licenca) {
		this.licenca = licenca;
	}

	public PerguntaRequerimento getPerguntaNR_A2_DRENALT_P11() {
		return perguntaNR_A2_DRENALT_P11;
	}

	public void setPerguntaNR_A2_DRENALT_P11(PerguntaRequerimento perguntaNR_A2_DRENALT_P11) {
		this.perguntaNR_A2_DRENALT_P11 = perguntaNR_A2_DRENALT_P11;
	}

	public PerguntaRequerimento getPerguntaNR_A2_DRENALT_P12() {
		return perguntaNR_A2_DRENALT_P12;
	}

	public void setPerguntaNR_A2_DRENALT_P12(PerguntaRequerimento perguntaNR_A2_DRENALT_P12) {
		this.perguntaNR_A2_DRENALT_P12 = perguntaNR_A2_DRENALT_P12;
	}

	public Collection<TipoSolicitacao> getTiposSolicitacao() {
		return tiposSolicitacao;
	}

	public void setTiposSolicitacao(Collection<TipoSolicitacao> tiposSolicitacao) {
		this.tiposSolicitacao = tiposSolicitacao;
	}

	public void setObjetosAlteracao(List<ObjetoAlteracao> objetosAlteracao) {
		this.objetosAlteracao = objetosAlteracao;
	}

	public boolean isDisableTipoSolicitacao() {
		return disableTipoSolicitacao;
	}

	public void setDisableTipoSolicitacao(boolean disableTipoSolicitacao) {
		this.disableTipoSolicitacao = disableTipoSolicitacao;
	}

	public String getStrInformativa() {
		return strInformativa;
	}

	public void setStrInformativa(String strInformativa) {
		this.strInformativa = strInformativa;
	}
	
	public Date getDataAtual() {
		return new Date();
	}
	
	/**
	 * @return the processo
	 */
	public Processo getProcesso() {
		return processo;
	}
	
	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(Processo processo) {
	
		this.processo = processo;
	}

	/**
	 * @return the renovacaoLicenca
	 */
	public boolean isRenovacaoLicenca() {
	
		return renovacaoLicenca;
	}
	
	/**
	 * @param renovacaoLicenca the renovacaoLicenca to set
	 */
	public void setRenovacaoLicenca(boolean renovacaoLicenca) {
	
		this.renovacaoLicenca = renovacaoLicenca;
	}

	/**
	 * @return the certificado
	 */
	public Certificado getCertificado() {
	
		return certificado;
	}

	/**
	 * @param certificado the certificado to set
	 */
	public void setCertificado(Certificado certificado) {
	
		this.certificado = certificado;
	}
	
	/**
	 * @return the dataValidadeCertificado
	 */
	public String getDataValidadeCertificado() {
	
		return dataValidadeCertificado;
	}

	/**
	 * @param dataValidadeCertificado the dataValidadeCertificado to set
	 */
	public void setDataValidadeCertificado(String dataValidadeCertificado) {
	
		this.dataValidadeCertificado = dataValidadeCertificado;
	}

	
	public boolean isDesabilitarTudo() {
	
		return desabilitarTudo;
	}

	
	public void setDesabilitarTudo(boolean desabilitarTudo) {
	
		this.desabilitarTudo = desabilitarTudo;
	}
}
