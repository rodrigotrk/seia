package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TipoRelatorioEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.ConsultaCerhServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("consultarCerhController")
@ViewScoped
public class ConsultarCerhController {

	@EJB
	private ConsultaCerhServiceFacade consultaCerhServiceFacade;

	private MetodoUtil metodoExterno;
	private Pessoa requerenteSelecionado;
	private String numCadastro;
	private Municipio municipioSelecionado;
	private String nomEmpreendimento;
	private ContratoConvenio contratoConvenioSelecionado;
	private Date periodoInicio;
	private Date periodoFim;
	private CerhStatus cerhTipoStatusSelecionado;
	private Collection<ContratoConvenio> listaContratoConvenio;
	private Collection<CerhStatus> listaCerhTipoStatus;
	private Collection<Municipio> listaMunicipio;
	private TipoRelatorioEnum tipoRelatorioEnum;
	
	private LazyDataModel<Cerh> listaCerh;

	private Collection<GeoBahia> listaRpga;
	private GeoBahia rpgaSelecionado;

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void load() {
		try {
			inicializarVariaveis();
			listaCerh = (LazyDataModel<Cerh>) SessaoUtil.recuperarObjetoSessao("listaCerh");
			listaContratoConvenio = consultaCerhServiceFacade.listarContratoConvenio();
			listaMunicipio = consultaCerhServiceFacade.listarMunicipioBahia();
			listaCerhTipoStatus = consultaCerhServiceFacade.listarCerhTipoStatus();
			listaRpga = consultaCerhServiceFacade.listarRPGA();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public MetodoUtil getMetodoConsultar(){
		return new MetodoUtil(this,"consultar");
	}
	
	/**
	 * Método para gerar uma requisição AJAX.
	 *
	 * @author eduardo.fernandes
	 */
	public void poll() {
		Html.executarJS("consultaPoll.stop();");

		String msg = ContextoUtil.getContexto().getUpdateMessage();
		Boolean sucesso = ContextoUtil.getContexto().getSucessMessage();
		if(!Util.isNullOuVazio(msg)){
			if(sucesso){
				MensagemUtil.sucesso(msg);
			}
			else {
				MensagemUtil.alerta(msg);
			}
			limparConsultaAnterior();
			Html.atualizar("frmConsultaCerh:pnlDataTable");
		}

		ContextoUtil.getContexto().setUpdateMessage(null);
		ContextoUtil.getContexto().setSucessMessage(null);
	}

	public String novo() {
		ContextoUtil.getContexto().setTipoRequerimento(null);
		ContextoUtil.getContexto().setTelaParaRedirecionar(PaginaEnum.CADASTRO_CERH);
		return "/paginas/identificar-papel/identificar-papel.xhtml";
	}

	public void limpar() {
		try {
			inicializarVariaveis();
			limparConsultaAnterior();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void inicializarVariaveis() {
		setMetodoExterno(new MetodoUtil(this, "selecionarRequerente", Pessoa.class));
		requerenteSelecionado=new Pessoa();
		numCadastro=null;
		municipioSelecionado=null;
		nomEmpreendimento=null;
		contratoConvenioSelecionado=null;
		cerhTipoStatusSelecionado=null;
		periodoInicio=null;
		periodoFim=null;
		rpgaSelecionado=null;

	}

	public void selecionarRequerente(Pessoa requerente) {
		setRequerenteSelecionado(requerente);
		Html.atualizar("frmConsultaCerh:nomeRazaoSocial","frmConsultaCerh:btnResumo");
	}

	public String getDataHoje() {
		return DataUtil.getDataHoje();
	}

	public void consultar() {
		try {
			final Map<String, Object> parametros = getParametros();

			limparConsultaAnterior();

			validar(parametros);

			listaCerh = new LazyDataModel<Cerh>() {
				private static final long serialVersionUID = 4518130802802551379L;
				@Override
				public List<Cerh> load(int first, int pageSize, String arg2, SortOrder arg3, Map<String, String> arg4) {
					try {
						return consultaCerhServiceFacade.listarCerh(first, pageSize,parametros);
					}
					catch (Exception e) {
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
				}
			};

			listaCerh.setRowCount(consultaCerhServiceFacade.listarCerhCount(parametros));
			SessaoUtil.adicionarObjetoSessao("listaCerh", listaCerh);
		}
		catch(SeiaValidacaoRuntimeException e) {
			MensagemUtil.alerta(e.getMessage());
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void limparConsultaAnterior() {
		SessaoUtil.removerObjetoSessao("listaCerh");
		listaCerh = null;
	}

	private void validar(final Map<String, Object> parametros) {
		if(parametros.isEmpty()) {
			throw new SeiaValidacaoRuntimeException("Nenhum filtro foi informado. Por favor, informe um filtro.");
		}
		if(!Util.validaPeriodo(periodoInicio, periodoFim)) {
			throw new SeiaValidacaoRuntimeException("O período informado é inválido.");
		}
	}

	public boolean podeEditar(Cerh cerh) {
		return !cerh.getIdeCerhStatus().equals(new CerhStatus(CerhStatusEnum.CANCELADO));
	}

	public boolean podeCancelar(Cerh cerh) {
		return !ContextoUtil.getContexto().isUsuarioExterno() && podeEditar(cerh);
	}

	public boolean podeImprimirCertificado(Cerh cerh) {
		return cerh.getIdeCerhStatus().equals(new CerhStatus(CerhStatusEnum.CADASTRO_COMPLETO));
	}

	public void getImprimirResumoQuantitativoCerh(String tipoRelatorio){
		try {
			tipoRelatorioEnum = TipoRelatorioEnum.valueOf(tipoRelatorio);
			StreamedContent cerhRelatorio = consultaCerhServiceFacade.resumoQuantitativoCerh(getParametros(), tipoRelatorioEnum);
			
			SessaoUtil.adicionarObjetoSessao("arquivo", cerhRelatorio.getStream());
			SessaoUtil.adicionarObjetoSessao("nomeArquivo", cerhRelatorio.getName());

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String url =  request.getScheme() + "://" + request.getServerName();
			
			if (request.getServerPort()!=80){
				url += ":" + request.getServerPort();
			}
			
			Html.executarJS("window.open('"+url+"/download/')");
			JsfUtil.addSuccessMessage("Arquivo gerado com sucesso.");
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public StreamedContent getImprimirResumoCerh(Cerh cerh){

		if(cerh.getNumCadastro()==null){
			try {
				Cerh cerhOld = consultaCerhServiceFacade.getNumeroAtualCerh(cerh);
				cerh.setNumCadastro(cerhOld.getNumCadastro());

			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}

		return consultaCerhServiceFacade.resumoCerh(cerh);
	}


	public StreamedContent getImprimirCertificado(Cerh cerh) {
		try {

			if(!existeCertificado(cerh)){
				gerarCertificado(cerh);
			}

			return consultaCerhServiceFacade.imprimirCertificado(cerh);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	private void gerarCertificado(Cerh cerh) throws Exception {
		consultaCerhServiceFacade.salvarCertificado(cerh);
	}

	private boolean existeCertificado(Cerh cerh){
		return !Util.isNullOuVazio(cerh.getIdeCertificado());
	}

	public void selecionarTipoRelatorio(TipoRelatorioEnum tipoRelatorioEnum){
		this.tipoRelatorioEnum = tipoRelatorioEnum;
	}

	private Map<String, Object> getParametros() {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!Util.isNullOuVazio(requerenteSelecionado) && !Util.isNull(requerenteSelecionado.getNomeRazao())) {
			params.put("requerente", requerenteSelecionado);
			params.put("nomRequerente", requerenteSelecionado.getNomeRazao());
		}

		if(!Util.isNullOuVazio(numCadastro)) {
			params.put("numCadastro", numCadastro);
		}

		if(!Util.isNull(municipioSelecionado)) {
			params.put("municipio", municipioSelecionado);
			if(!Util.isNull(municipioSelecionado.getNomMunicipio())) {
				params.put("nomMunicipio", municipioSelecionado.getNomMunicipio());
			}
		}

		if(!Util.isNullOuVazio(nomEmpreendimento)) {
			params.put("nomEmpreendimento", nomEmpreendimento);
		}

		if(!Util.isNull(contratoConvenioSelecionado)) {
			params.put("contratoConvenio", contratoConvenioSelecionado);
			if(!Util.isNull(contratoConvenioSelecionado.getNomContratoConvenio())) {
				params.put("nomContratoConvenio", contratoConvenioSelecionado.getNomContratoConvenio());
			}
		}

		if(!Util.isNull(periodoInicio)) {
			params.put("periodoInicio", periodoInicio);
		}

		if(!Util.isNull(periodoFim)) {
			params.put("periodoFim", periodoFim);
		}

		if(!Util.isNull(cerhTipoStatusSelecionado)) {
			params.put("cerhTipoStatus", cerhTipoStatusSelecionado);
			if(!Util.isNull(cerhTipoStatusSelecionado.getDscStatus())) {
				params.put("nomCerhTipoStatus", cerhTipoStatusSelecionado.getDscStatus());
			}
		}

		if(!Util.isNull(rpgaSelecionado)) {
			params.put("rpgaSelecionado", rpgaSelecionado);

			if(!Util.isNull(rpgaSelecionado.getNome())) {
				params.put("nomRpga", rpgaSelecionado.getNome());
			}

		}

		return params;
	}


	public boolean isExisteParametro(){

		if(!Util.isNullOuVazio(requerenteSelecionado)) {
			return true;
		}

		else if(!Util.isNullOuVazio(numCadastro)) {
			return true;
		}

		else if(!Util.isNullOuVazio(contratoConvenioSelecionado)) {
			return true;
		}

		else if(!Util.isNullOuVazio(cerhTipoStatusSelecionado)) {
			return true;
		}

		else if(!Util.isNullOuVazio(periodoInicio) && !Util.isNullOuVazio(periodoFim)) {
			return true;
		}

		else if(!Util.isNullOuVazio(rpgaSelecionado)) {
			return true;
		}

		else if(!Util.isNullOuVazio(municipioSelecionado)) {
			return true;
		}

		else if(!Util.isNullOuVazio(nomEmpreendimento)) {
			return true;
		}

		return false;
	}

	public void atualizarBtnResumoQuantitativo(){
		Html.atualizar("frmConsultaCerh:btnResumo");
	}

	/**
	 * Gets e sets
	 * */

	public Pessoa getRequerenteSelecionado() {
		return requerenteSelecionado;
	}

	public void setRequerenteSelecionado(Pessoa requerenteSelecionado) {
		this.requerenteSelecionado = requerenteSelecionado;
	}

	public String getNumCadastro() {
		return numCadastro;
	}

	public void setNumCadastro(String numCadastro) {
		this.numCadastro = numCadastro;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public String getNomEmpreendimento() {
		return nomEmpreendimento;
	}

	public void setNomEmpreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento;
	}

	public ContratoConvenio getContratoConvenioSelecionado() {
		return contratoConvenioSelecionado;
	}

	public void setContratoConvenioSelecionado(ContratoConvenio contratoConvenioSelecionado) {
		this.contratoConvenioSelecionado = contratoConvenioSelecionado;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public LazyDataModel<Cerh> getListaCerh() {
		return listaCerh;
	}

	public void setListaCerh(LazyDataModel<Cerh> listaCerh) {
		this.listaCerh = listaCerh;
	}

	public CerhStatus getCerhTipoStatusSelecionado() {
		return cerhTipoStatusSelecionado;
	}

	public void setCerhTipoStatusSelecionado(CerhStatus cerhTipoStatusSelecionado) {
		this.cerhTipoStatusSelecionado = cerhTipoStatusSelecionado;
	}

	public Collection<CerhStatus> getListaCerhTipoStatus() {
		return listaCerhTipoStatus;
	}

	public void setListaCerhTipoStatus(Collection<CerhStatus> listaCerhTipoStatus) {
		this.listaCerhTipoStatus = listaCerhTipoStatus;
	}

	public Collection<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(Collection<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public Collection<ContratoConvenio> getListaContratoConvenio() {
		return listaContratoConvenio;
	}

	public void setListaContratoConvenio(Collection<ContratoConvenio> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}

	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}

	public GeoBahia getRpgaSelecionado() {
		return rpgaSelecionado;
	}

	public void setRpgaSelecionado(GeoBahia rpgaSelecionado) {
		this.rpgaSelecionado = rpgaSelecionado;
	}

	public Collection<GeoBahia> getListaRpga() {
		return this.listaRpga;
	}
	
	public TipoRelatorioEnum getTipoRelatorioEnum() {
		return tipoRelatorioEnum;
	}	
	
	public void setTipoRelatorioEnum(String tipoRelatorioEnum) {
		this.tipoRelatorioEnum = TipoRelatorioEnum.valueOf(tipoRelatorioEnum);
	}

}