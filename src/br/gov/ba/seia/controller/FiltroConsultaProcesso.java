package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Level;
import org.joda.time.DateTime;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
/**
 * Classe para centralizar os campos comuns de busca por Processo.
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 02/12/2015
 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
 */
public class FiltroConsultaProcesso {

	@EJB
	private TipoAtoService tipoAtoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	@EJB
	private EmpreendimentoService empreendimentoService;

	// Nº do Processo:
	private String numProcesso;
	
	// Nome do Requerente:
	private String nomeRequerente;
	
	// CPF ou CNPJ do Requerente:
	private String cpfOrCnpjRequerente;
	
	// Nome do Empreendimento:
	private String nomeEmpreendimento;
	
	// Processos com mais de 180 dias:
	private Boolean oldProcessos;
	
	// Período de Formação:
	private Date periodoInicio;
	private Date periodoFim;
	
	private Date currentDate = new Date();
	
	// Categoria:
	private Collection<TipoAto> listaTipoAto;
	private TipoAto tipoAto;
	
	// Ato:
	private Collection<AtoAmbiental> listaAtoAmbiental;
	private AtoAmbiental atoAmbiental;

	// Tipologia:
	private Collection<Tipologia> listaTipologia;
	private Tipologia tipologia;
	
	// Finalidade:
	private Collection<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua;	
	private TipoFinalidadeUsoAgua finalidadeUsoAgua;
	
	//Requerente
	private Pessoa requerente;
	
	//Empreendimento
	private Empreendimento empreendimento;
	private List<SelectItem> collEmpreendimento;
	/**
	 * Método para listar todas as Categorias
	 * @see {@link TipoAto}
	 */
	protected void listarCategoria() {
		try {
			listaTipoAto = tipoAtoService.listarTiposAto();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Categoria.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregarEmpreendimento() {
		try {

			Collection<Empreendimento> collectionsEmpreendimento = inicializaCollections();

			if (!Util.isNull(requerente)) {
				collectionsEmpreendimento = empreendimentoService.listarEmpreendimento(requerente);
			}

			if (!Util.isNullOuVazio(collectionsEmpreendimento)) {
				for (Empreendimento empreendimento : collectionsEmpreendimento) {
					SelectItem item = new SelectItem(empreendimento.getIdeEmpreendimento(),empreendimento.getNomEmpreendimento());
					this.collEmpreendimento.add(item);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de empreendimentos - " + e.getMessage());
		}
	}
	
	private Collection<Empreendimento> inicializaCollections() {

		collEmpreendimento = new ArrayList<SelectItem>();
		Collection<Empreendimento> collectionsEmpreendimento = null;
		return collectionsEmpreendimento;
	}
	
	public void selecionarRequerente(Pessoa requerente) {
		this.requerente = requerente;
		if (!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getPessoaFisica())) {
			nomeRequerente = requerente.getPessoaFisica().getNomPessoa();
			cpfOrCnpjRequerente = requerente.getPessoaFisica().getNumCpf();
		} else if (!Util.isNullOuVazio(requerente) && !Util.isNullOuVazio(requerente.getPessoaJuridica())) {
			nomeRequerente = requerente.getPessoaJuridica().getNomRazaoSocial();
			cpfOrCnpjRequerente = requerente.getPessoaJuridica().getNumCnpj();
		}
		
		carregarEmpreendimento();
	
		Html.atualizar("frmPautaReenquadramentoProcesso");
	}

	/**
	 * Método que preenche uma lista de {@link AtoAmbiental} de acordo com o {@link TipoAto} selecionado e limpa os combos dependentes daquele antigo ato, quando houver.
	 */
	public void changeCategoria(ValueChangeEvent event) {
		try {
			TipoAto tipoAtoSelecionado = (TipoAto) event.getNewValue();
			if (!Util.isNull(tipoAtoSelecionado) && tipoAtoSelecionado.getIdeTipoAto() != 0 && tipoAtoSelecionado.getIdeTipoAto() != -1) {
				listaAtoAmbiental = atoAmbientalService.obterAtoAmbientalPorTipoAto(tipoAtoSelecionado.getIdeTipoAto());
			} 
			else {
				limparComboAtoAmbiental();
			}
			limparComboTipologia();
			limparComboFinalidade();
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Atos Ambientais.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	/**
	 * Método que preenche uma lista de {@link Tipologia} de acordo com o {@link AtoAmbiental} selecionado e limpa os combos dependentes daquele antiga tipologia, quando houver.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	public void changeAtoAmbiental(ValueChangeEvent changeEvent) {
		try {
			AtoAmbiental atoAmbiental = (AtoAmbiental) changeEvent.getNewValue();
			if(!Util.isNull(atoAmbiental)){
				listaTipologia = tipologiaService.listarByAto(atoAmbiental);
			} 
			else {
				limparComboTipologia();
			}
			limparComboFinalidade();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipologias.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que preenche uma lista de Finalidades de acordo com a {@link Tipologia} selecionada.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see {@link TipoFinalidadeUsoAgua}
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	public void changeTipologia(ValueChangeEvent changeEvent) {
		try {
			Tipologia tipologia = (Tipologia) changeEvent.getNewValue();
			if(!Util.isNull(tipologia)){
				listaFinalidadeUsoAgua = tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologiaAndAto(new EnquadramentoAtoAmbiental(tipologia, atoAmbiental));
			} else {
				limparComboFinalidade();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Finalidades.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para limpar as instâncias dessa classe.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	protected void limpar() {
		numProcesso = null;
		nomeEmpreendimento=null;
		nomeRequerente = null;
		requerente = null;
		cpfOrCnpjRequerente = null;
		oldProcessos = false;
		empreendimento = new Empreendimento();
		collEmpreendimento = new ArrayList();
		limparDatas();
		limparComboCategoria();
		limparComboAtoAmbiental();
		limparComboFinalidade();
		limparComboTipologia();
	}
	
	public void limparDatas(){
		periodoInicio = null;
		periodoFim = null;
	}
	
	/**
	 * Método para limpar o combo de seleção de Categoria: [{@link TipoAto}].
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	private void limparComboCategoria(){
//		if(!Util.isNull(listaTipoAto)){
//			listaTipoAto.clear();
//		}
		tipoAto = null;
	}
	
	/**
	 * Método para limpar o combo de seleção de {@link AtoAmbiental}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	private void limparComboAtoAmbiental(){
		if(!Util.isNull(listaAtoAmbiental)){
			listaAtoAmbiental.clear();
		}
		atoAmbiental = null;
	}
	
	/**
	 * Método para limpar o combo de seleção de {@link Tipologia}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	private void limparComboTipologia(){
		if(!Util.isNull(listaTipologia)){
			listaTipologia.clear();
		}
		tipologia = null;
	}
	
	/**
	 * Método para limpar o combo de seleção de Finalidade [{@link TipoFinalidadeUsoAgua}].
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 02/12/2015
	 * @see <a href="http://10.105.12.26/redmine/issues/7348">#7348</a>
	 */
	private void limparComboFinalidade(){
		if(!Util.isNull(listaFinalidadeUsoAgua)){
			listaFinalidadeUsoAgua.clear();
		}
		finalidadeUsoAgua = null;
	}
	
	/*
	 * FLAG's
	 */
	protected boolean isNumProcessoPreenchido(){
		return !Util.isNullOuVazio(numProcesso);
	}
	
	protected boolean isPeriodoInicialPreenchido(){
		return !Util.isNullOuVazio(periodoInicio);
	}
	
	protected boolean isPeriodoFinalPreenchido(){
		return !Util.isNullOuVazio(periodoFim);
	}
	
	protected boolean isCategoriaSelected(){
		return !Util.isNull(tipoAto);
	}
	
	public boolean isComboAtoBlocked(){
		return !isCategoriaSelected() || Util.isNullOuVazio(listaAtoAmbiental);
	}
	
	protected boolean isAtoSelected(){
		return !Util.isNull(atoAmbiental);
	}
	
	public boolean isComboTipologiaBlocked(){
		return !isAtoSelected() || Util.isNullOuVazio(listaTipologia);
	}
	
	protected boolean isTipologiaSelected(){
		return !Util.isNull(tipologia);
	}
	
	protected boolean isNomeRequerentePreenchido(){
		return !Util.isNullOuVazio(nomeRequerente);
	}
	
	protected boolean isNomeEmpreendimentoPreenchido(){
		return !Util.isNullOuVazio(nomeEmpreendimento);
	}

	protected boolean isCpfOrCnpjRequerentePreenchido(){
		return !Util.isNullOuVazio(cpfOrCnpjRequerente);
	}
	
	public boolean isComboFinalidadeBlocked(){
		return !isTipologiaSelected() || Util.isNullOuVazio(listaFinalidadeUsoAgua);
	}
	
	protected boolean isFinalidadeSelected(){
		return !Util.isNull(finalidadeUsoAgua);
	}
	
	
	protected boolean isEmpreendimentoPreenchido(){
		return !Util.isNullOuVazio(empreendimento) 
				&& empreendimento.getId()!=0;
	}
	
	protected boolean isRequerentePreenchido(){
		
		return !Util.isNullOuVazio(requerente) 
				&& (!Util.isNullOuVazio(requerente.getPessoaFisica()) ||
						!Util.isNullOuVazio(requerente.getPessoaJuridica()));
	}
	
	
	protected Map<String, Object> getParametros() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(isNumProcessoPreenchido()) {
			params.put("numProcesso", numProcesso);
		}
		if(isPeriodoInicialPreenchido()) {
			params.put("periodoInicio", periodoInicio);
		}
		if(isPeriodoFinalPreenchido()) {
			params.put("periodoFim", periodoFim);
		}
		if(isCategoriaSelected()) {
			params.put("tipoAto", tipoAto.getIdeTipoAto());
		}
		if(isAtoSelected()) {
			params.put("atoAmbiental", atoAmbiental.getIdeAtoAmbiental());
		}
		if(isTipologiaSelected()) {
			params.put("tipologia", tipologia.getIdeTipologia());
		}
		if(isFinalidadeSelected()) {
			params.put("finalidade", finalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua());
		}
		if(isRequerentePreenchido()) {
			params.put("nomeRequerente", nomeRequerente);
		}
		if(isEmpreendimentoPreenchido()) {
			params.put("empreendimento", empreendimento.getId());
		}
		if(isCpfOrCnpjRequerentePreenchido()) {
			params.put("cpfOrCnpjRequerente", cpfOrCnpjRequerente);
		}
		if(!Util.isNullOuVazio(oldProcessos)) {
			params.put("oldProcessos", oldProcessos);
		}
		
		
		return params;
	}
	
	/*
	 * Getter && Setters
	 */
	public String getNumProcesso() {
		if(!Util.isNullOuVazio(numProcesso)){
			return numProcesso.trim();
		}
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}
	
	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		if (!Util.isNullOuVazio(periodoFim)) {
			DateTime dt = new DateTime(periodoFim);
			this.periodoFim = dt.plusHours(23).plusMinutes(59).plusSeconds(59).toDate();
		}
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}
	
	public Collection<TipoAto> getTiposAto() {
		return listaTipoAto;
	}

	public Collection<AtoAmbiental> getAtos() {
		return listaAtoAmbiental;
	}

	public Collection<TipoAto> getListaTipoAto() {
		return listaTipoAto;
	}

	public void setListaTipoAto(Collection<TipoAto> listaTipoAto) {
		this.listaTipoAto = listaTipoAto;
	}

	public TipoAto getTipoAto() {
		return tipoAto;
	}

	public void setTipoAto(TipoAto tipoAto) {
		this.tipoAto = tipoAto;
	}

	public Collection<AtoAmbiental> getListaAtoAmbiental() {
		return listaAtoAmbiental;
	}

	public void setListaAtoAmbiental(Collection<AtoAmbiental> listaAtoAmbiental) {
		this.listaAtoAmbiental = listaAtoAmbiental;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Collection<Tipologia> getListaTipologia() {
		return listaTipologia;
	}

	public void setListaTipologia(Collection<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	public TipoFinalidadeUsoAgua getFinalidadeUsoAgua() {
		return finalidadeUsoAgua;
	}

	public void setFinalidadeUsoAgua(TipoFinalidadeUsoAgua finalidadeUsoAgua) {
		this.finalidadeUsoAgua = finalidadeUsoAgua;
	}

	public Collection<TipoFinalidadeUsoAgua> getListaFinalidadeUsoAgua() {
		return listaFinalidadeUsoAgua;
	}

	public void setListaFinalidadeUsoAgua(Collection<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua) {
		this.listaFinalidadeUsoAgua = listaFinalidadeUsoAgua;
	}

	public Boolean getOldProcessos() {
		return this.oldProcessos;
	}
	
	
	public void setOldProcessos(Boolean oldProcessos) {
		this.oldProcessos = oldProcessos;
	}

	public String getNomeEmpreendimento() {
		return nomeEmpreendimento;
	}

	public void setNomeEmpreendimento(String nomeEmpreendimento) {
		this.nomeEmpreendimento = nomeEmpreendimento;
	}

	public String getCpfOrCnpjRequerente() {
		return cpfOrCnpjRequerente;
	}

	public void setCpfOrCnpjRequerente(String cpfOrCnpjRequerente) {
		this.cpfOrCnpjRequerente = cpfOrCnpjRequerente;
	}

	public String getNomeRequerente() {
		return nomeRequerente;
	}

	public void setNomeRequerente(String nomeRequerente) {
		this.nomeRequerente = nomeRequerente;
	}
	
	public Date getCurrentDate() {
	    return this.currentDate;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<SelectItem> getCollEmpreendimento() {
		return collEmpreendimento;
	}

	public void setCollEmpreendimento(List<SelectItem> collEmpreendimento) {
		this.collEmpreendimento = collEmpreendimento;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}
}
