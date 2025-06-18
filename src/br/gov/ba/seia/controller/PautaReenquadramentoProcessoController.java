package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.StatusReenquadramentoService;
import br.gov.ba.seia.service.facade.PautaReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;

@Named("pautaReenquadramentoProcessoController")
@ViewScoped
public class PautaReenquadramentoProcessoController extends FiltroConsultaProcesso {
	
	@EJB
	private PautaReenquadramentoProcessoServiceFacade pautaReenquadramentoProcessoServiceFacade;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private StatusReenquadramentoService statusReenquadramentoService;
	private StatusReenquadramento statusReenquadramento;
	
	private Collection<StatusReenquadramento> listaStatusReenquadramento;
	
	private LazyDataModel<ProcessoReenquadramentoDTO> listaProcessoReenquadramentoDTO;
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	private String dsJustificativaNaoReenquadrar;
	private String statusARetornar;

	
	@PostConstruct
	private void load() {
		try{
			limpar();
			listarCategoria();
			listarStatusReenquadramento();
			
			if (!Util.isNullOuVazio(SessaoUtil.recuperarObjetoSessao("numProcesso"))){
				setNumProcesso((String)SessaoUtil.recuperarObjetoSessao("numProcesso"));
				SessaoUtil.removerObjetoSessao("numProcesso");
				consultar();
				Html.executarJS("atualizarPauta()");
			}
		}
		catch(Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("ERRO-0004"));
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isUsuarioExterno() {
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())){
			return ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
		}
		return false;
	}
	
	public boolean isAtendente() {
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())) {
			return PerfilEnum.ATENDENTE.getId().equals(ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil());
		}
		return false;
	}

	public boolean isRequerente(ProcessoReenquadramentoDTO pr) {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		if(!Util.isNullOuVazio(pr.getListaPessoaRequerimento())) {
			for(Pessoa p : pr.getListaPessoaRequerimento()) {
				if(usuarioLogado.getIdePessoaFisica().equals(p.getIdePessoa())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isRenderedEditarReenquadramento(ProcessoReenquadramentoDTO pr) {
		
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())
				&& (!Util.isNullOuVazio(pr.getRequerente().getPessoaFisica())
				|| !Util.isNullOuVazio(pr.getRequerente().getPessoaJuridica()))) {
			
			boolean isRequerente = isRequerente(pr);
			boolean isPerfilEdicao = (isRequerente || isAtendente() || isUsuarioExterno());
			boolean isStatusReenquadramentoEdicao = (pr.getStatusAtual().getIdeStatusReenquadramento().equals(StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO.getId())
					|| pr.getStatusAtual().getIdeStatusReenquadramento().equals(StatusReenquadramentoEnum.PENDENCIA_DE_REENQUADRAMENTO.getId()));
			if (isPerfilEdicao && isStatusReenquadramentoEdicao) {
				return true;
			}
		}

		return false;
	}
	
	public void visualizarProcesso(ProcessoReenquadramento processoReenquadramento) {
		try{
			VwConsultaProcesso vwProcesso = pautaReenquadramentoProcessoServiceFacade.buscarVwConsultaProcesso(processoReenquadramento.getIdeProcesso());
			DetalharProcessoController detalharProcessoController = (DetalharProcessoController) SessaoUtil.recuperarManagedBean("#{detalharProcessoController}", DetalharProcessoController.class);
			detalharProcessoController.setVwProcesso(vwProcesso);
			detalharProcessoController.setProcessoReenquadramento(processoReenquadramento);
			detalharProcessoController.visualizarProcesso();
			HistoricoTramitacaoProcessoController historicoTramitacaoProcessoController = (HistoricoTramitacaoProcessoController) SessaoUtil.recuperarManagedBean("#{historicoTramitacaoProcessoController}", HistoricoTramitacaoProcessoController.class);
			historicoTramitacaoProcessoController.setVwProcesso(vwProcesso);
			Html.atualizar("formDetalharProcesso");
			Html.exibir("dialogdetalharProcesso");
		}
		catch(Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void listarStatusReenquadramento() throws Exception {
		listaStatusReenquadramento = pautaReenquadramentoProcessoServiceFacade.listarStatusReenquadramento();
	}
	
	public void limparTela() {
		super.limpar();
		statusReenquadramento = null;
		listaProcessoReenquadramentoDTO = null;
	}

	public boolean isRequerimentoUnico(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		if (!Util.isNull(processoReenquadramentoDTO) && !Util.isNull(processoReenquadramentoDTO.getRequerimento())){
			Requerimento requerimento = requerimentoService.buscarEntidadeCarregadaPorId(processoReenquadramentoDTO.getRequerimento());
			
			if(!Util.isNull(requerimento.getRequerimentoUnico())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> params = super.getParametros();
		if (ContextoUtil.getContexto().isUsuarioExterno()) {
			List<Integer> listaPessoas = pautaReenquadramentoProcessoServiceFacade.listarPessoasQuePodemSerConsultadasPeloUsuario();
			params.put("listaPessoas", listaPessoas);
		}
		
		if(!Util.isNull(statusReenquadramento)) {
			params.put("statusReenquadramento", statusReenquadramento.getIdeStatusReenquadramento());
		}
		
		if (!Util.isNullOuVazio(getNumProcesso())){
			params.put("numProcesso", getNumProcesso());
		}
		
		return params;
	}
	
	public void consultar() {
		
		SessaoUtil.removerObjetoSessao("isEnquadramento");
		
		listaProcessoReenquadramentoDTO = new LazyDataModel<ProcessoReenquadramentoDTO>() {
			
			private static final long serialVersionUID = -549249300009769836L;
			
			@Override
			public List<ProcessoReenquadramentoDTO> load(int first, int pageSize, String sortField, SortOrder sortOrderder, Map<String, String> fields) {
				try {
					return (List<ProcessoReenquadramentoDTO>) pautaReenquadramentoProcessoServiceFacade.listarProcessoReenquadramentoDTO(getParametros(), first, pageSize);
				} 
				catch (Exception e) {
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		
		listaProcessoReenquadramentoDTO.setRowCount(count());
		resetPageDatatable();
	}

	private void resetPageDatatable() {
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmPautaReenquadramentoProcesso:dataTableProcessosReenquadramento");
		if(!Util.isNull(dataTable)) {
			dataTable.reset();
		}
	}
	
	private Integer count() {
		try {
			return pautaReenquadramentoProcessoServiceFacade.countProcessoReenquadramentoDTO(getParametros());
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent gerarResumoRequerimento(Requerimento requerimento) {
		try {
			return pautaReenquadramentoProcessoServiceFacade.gerarResumoRequerimento(requerimento.getIdeRequerimento());
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent gerarResumoRequerimentoInicial(Requerimento requerimento) {
		try {
			Requerimento req = requerimentoService.buscarEntidadeCarregadaPorId(requerimento);
			
			if (!Util.isNullOuVazio(req)){
				return new RelatorioUtil().downloadArquivoRelatorio(req.getDesCaminhoResumoOriginal());
			}
			return null;
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void updatePeriodo() {
		limparDatas();
		if(getOldProcessos()!=null && getOldProcessos()) {
			 DateTime dateTime = new DateTime()
					 .withHourOfDay(23)
					 .withMinuteOfHour(59);
			 setPeriodoFim(dateTime.minusDays(183).toDate());
		}
	}
	

	
	/**
	 * MÉTODO RESPONSÁVEL POR EXIBIR O BOTÃO DE VOLTAR STATUS SOB AS CONDIÇÕES DE
	 * SER O USUÁRIO ISABEL.CRISTINA E ESTÁ NUMA LISTA ESPECÍFICA QUE PERMITE A TROCA DE STATUS
	 * OS STATUS SÃO: VALIDAÇÃO PRÉVIA, VALIDADO, BOLETO EM PROCESSAMENTO, BOLETO DE PAGAMENTO LIBERADO ou AGUARDANDO ENVIO DA DOCUMENTAÇÃO 
	 * @author diegoraian
	 * @param processoReenquadramento
	 * @return
	 */
	public Boolean exibirVoltarStatus(ProcessoReenquadramentoDTO processoReenquadramento) {
		if ("isabel.cristina".equalsIgnoreCase(SecurityService.getUser().getDscLogin())) {
			if (!Util.isNull(processoReenquadramento) && !Util.isNull(processoReenquadramento.getStatusAtual())){
				for (Long status :StatusReenquadramentoEnum.getListAlterarStatus()){					
					if (status.equals(processoReenquadramento.getStatusAtual().getId())){
						return Boolean.TRUE;
					}	
				}											
			}
		}
		return Boolean.FALSE;
	}
	
	@Override
	public void selecionarRequerente(Pessoa requerente) {
		super.selecionarRequerente(requerente);
		listaProcessoReenquadramentoDTO = null;
	}
	public Collection<StatusReenquadramento> getListaStatusReenquadramento() {
		return listaStatusReenquadramento;
	}

	public void setListaStatusReenquadramento(
			Collection<StatusReenquadramento> listaStatusReenquadramento) {
		this.listaStatusReenquadramento = listaStatusReenquadramento;
	}

	public StatusReenquadramento getStatusReenquadramento() {
		return statusReenquadramento;
	}

	public void setStatusReenquadramento(StatusReenquadramento statusReenquadramento) {
		this.statusReenquadramento = statusReenquadramento;
	}

	public LazyDataModel<ProcessoReenquadramentoDTO> getListaProcessoReenquadramentoDTO() {
		return listaProcessoReenquadramentoDTO;
	}

	public void setListaProcessoReenquadramentoDTO(
			LazyDataModel<ProcessoReenquadramentoDTO> listaProcessoReenquadramentoDTO) {
		this.listaProcessoReenquadramentoDTO = listaProcessoReenquadramentoDTO;
	}

	public String getStatusARetornar() {
		return statusARetornar;
	}

	public void setStatusARetornar(String statusARetornar) {
		this.statusARetornar = statusARetornar;
	}
	

	
	



	
	public boolean renderedNaoReenquadrar(ProcessoReenquadramentoDTO prc) {
		if( (ContextoUtil.getContexto().getUsuarioLogado().isPerfilCoordenador()
				&& ContextoUtil.getContexto().getUsuarioLogado()
				.getPessoaFisica().getFuncionario().getIdeArea().getIdeArea().equals(AreaEnum.ATEND.getId()))
				&&  !prc.getStatusAtual().getIdeStatusReenquadramento().equals(StatusReenquadramentoEnum.REENQUADRADO.getId())) {
			return true;
		}
		return false;
	}
	
	public void iniciarNaoReenquadrar(ProcessoReenquadramentoDTO prc) {
		processoReenquadramentoDTO = prc;
		dsJustificativaNaoReenquadrar = pautaReenquadramentoProcessoServiceFacade.getTextoNaoReenquadrar(prc);
	}
	
	public void naoReenquadrarProcesso() {
		
		pautaReenquadramentoProcessoServiceFacade.finalizarNaoReenquadramento(processoReenquadramentoDTO,dsJustificativaNaoReenquadrar);
	}

	public String getDsJustificativaNaoReenquadrar() {
		return dsJustificativaNaoReenquadrar;
	}

	public void setDsJustificativaNaoReenquadrar(String dsJustificativaNaoReenquadrar) {
		this.dsJustificativaNaoReenquadrar = dsJustificativaNaoReenquadrar;
	}
	
	
	
}