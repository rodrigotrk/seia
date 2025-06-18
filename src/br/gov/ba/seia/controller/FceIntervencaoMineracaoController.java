package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.collection.internal.PersistentBag;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.FceIntervencaoCaracteristicaExtracao;
import br.gov.ba.seia.entity.FceIntervencaoMineracao;
import br.gov.ba.seia.entity.FceIntervencaoTipoCaractExtracao;
import br.gov.ba.seia.entity.LancamentoEfluenteLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoCaracteristicaExtracao;
import br.gov.ba.seia.entity.TipoMineralExtraido;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoCaracteristicaExtracaoEnum;
import br.gov.ba.seia.facade.FceIntervencaoMineracaoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@Named("fceIntervencaoMineracaoController")
@ViewScoped
public class FceIntervencaoMineracaoController extends FceController{

	private static final DocumentoObrigatorio DOC = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_INTERVENCAO_MINERACAO.getId(), "FORMULÁRIO DE CARACTERIZAÇÃO DO EMPREENDIMENTO - INTERVENÇÃO EM RECURSOS HÍDRICOS PARA FINS DE MINERAÇÃO");
	
	private FceIntervencaoMineracao fceIntervencaoMineracao; 
	
	private FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao;
	
	private LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica;
	
	@EJB
	private FceIntervencaoMineracaoFacade fceIntervencaoMineracaoFacade;
	
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	private List<FceIntervencaoTipoCaractExtracao> fceIntervencaoTipoCaracticasExtracao;
	
	private List<TipoMineralExtraido> tiposMineraislExtraidos;
	
	private MetodoUtil metodoRetornoCoordenada;

	@Override
	public void init() {
		try {
			this.fceIntervencaoMineracao = null;
			
			this.carregarFceDoRequerente(DOC);
			
			if(!this.isFceSalvo()){
				this.iniciarFce(DOC);
			}else {
				this.fceIntervencaoMineracao = this.fceIntervencaoMineracaoFacade.getFceIntervencaoMineracao(fce);
			}
			
			if(this.fceIntervencaoMineracao == null){
				this.fceIntervencaoMineracao = new FceIntervencaoMineracao();
				this.fceIntervencaoMineracao.setFce(this.fce);
				
				List<OutorgaLocalizacaoGeografica> outorgas = fceIntervencaoMineracaoFacade.getOutorgas(this.requerimento);
				
				if(!outorgas.isEmpty()){
					this.fceIntervencaoMineracao.setFceIntervencaoCaracteristicaExtracoes(this.montarFceIntervencaoCaracteristicaExtracao(outorgas));
				}
			} else {
				ajustarIntervencaoCaracteristicaExtracao();
				
				if(Util.isNullOuVazio(fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica())) {
					fceIntervencaoMineracao.setDscTratamentoEfluente(null);
				} else {
					for (LancamentoEfluenteLocalizacaoGeografica lfl : fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica()) {
						lfl.setEditar(false);
					}
				}
			}
			
			this.tiposMineraislExtraidos = fceIntervencaoMineracaoFacade.listarTipoMineralExtraido();
			
			metodoRetornoCoordenada = new MetodoUtil(this, "retornarCoordenada");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public void retornarCoordenada() {
		if (!Util.isNullOuVazio(fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica())){
			for (Iterator<?> it = fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica().iterator(); it.hasNext(); ) {
				LancamentoEfluenteLocalizacaoGeografica obj = (LancamentoEfluenteLocalizacaoGeografica) it.next();
				
				if (Util.isNullOuVazio(obj.getIdeLocalizacaoGeografica()) || (Util.isNullOuVazio(obj.getIdeLocalizacaoGeografica().getPontoLatitude()) && Util.isNullOuVazio(obj.getIdeLocalizacaoGeografica().getLatitudeInicial()))
						|| Util.isNullOuVazio(obj.getIdeLocalizacaoGeografica().getDadoGeograficoCollection())){
					it.remove();
				}
			}
			
			Html.atualizar("formFceInterMineracao:dataTableIntervMine");
		}
	}
	
	private void ajustarIntervencaoCaracteristicaExtracao() throws Exception {
		List<OutorgaLocalizacaoGeografica> outorgas = fceIntervencaoMineracaoFacade.getOutorgas(this.requerimento);
		if(!outorgas.isEmpty()){
			List<FceIntervencaoCaracteristicaExtracao> listaFceIntervencaoCaracteristicaExtracao = this.montarFceIntervencaoCaracteristicaExtracao(outorgas);
			
			for (FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracaoObj : listaFceIntervencaoCaracteristicaExtracao) {
				
				Boolean jaSalvo = false;
				
				for (FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracaoSalvo : this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()) {
					if (fceIntervencaoCaracteristicaExtracaoSalvo.getNomCoordenada().equals(fceIntervencaoCaracteristicaExtracaoObj.getNomCoordenada())){
						jaSalvo = true;
					}
				}
				
				if (!jaSalvo){
					this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes().add(fceIntervencaoCaracteristicaExtracaoObj);
				}
			}
		}
	}

	public List<FceIntervencaoTipoCaractExtracao> getMontarFceIntervencaoTipoCaractExtracao() throws Exception{
		List<TipoCaracteristicaExtracao> tipos = this.fceIntervencaoMineracaoFacade.listarCaracteristicaExtracoes();
		fceIntervencaoTipoCaracticasExtracao = new ArrayList<FceIntervencaoTipoCaractExtracao>();
		for(TipoCaracteristicaExtracao t : tipos){
			FceIntervencaoTipoCaractExtracao caractExtracao = new FceIntervencaoTipoCaractExtracao(t, this.fceIntervencaoCaracteristicaExtracao);
			if(!this.fceIntervencaoCaracteristicaExtracao.getFceIntervencaoTipoCaracticasExtracao().contains(caractExtracao)){
				fceIntervencaoTipoCaracticasExtracao.add(caractExtracao);
			}
		}
		fceIntervencaoTipoCaracticasExtracao.addAll(this.fceIntervencaoCaracteristicaExtracao.getFceIntervencaoTipoCaracticasExtracao());
		Collections.sort(fceIntervencaoTipoCaracticasExtracao);
		return fceIntervencaoTipoCaracticasExtracao;
	}
	
	private List<FceIntervencaoCaracteristicaExtracao> montarFceIntervencaoCaracteristicaExtracao(List<OutorgaLocalizacaoGeografica> outorgas){
		List<FceIntervencaoCaracteristicaExtracao> caracteristicaExtracaos = new ArrayList<FceIntervencaoCaracteristicaExtracao>();
		for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : outorgas){
			FceIntervencaoCaracteristicaExtracao intervencaoCaracteristicaExtracao = new FceIntervencaoCaracteristicaExtracao();
			intervencaoCaracteristicaExtracao.setIdeFceIntervencaoMineracao(this.fceIntervencaoMineracao);
			intervencaoCaracteristicaExtracao.setIdeLocalizacaoGeograficaIni(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			intervencaoCaracteristicaExtracao.setIdeLocalizacaoGeograficaFim(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeograficaFinal());
			intervencaoCaracteristicaExtracao.setNomCoordenada(outorgaLocalizacaoGeografica.getNomIntervencao());
			intervencaoCaracteristicaExtracao.setEditar(true);
			caracteristicaExtracaos.add(intervencaoCaracteristicaExtracao);
		}
		return caracteristicaExtracaos;
	}
	
	/**
	 * RN 0020 - Mensagem de aviso
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void selectOutros(ValueChangeEvent event){
		String op = "";
		if(event.getNewValue() instanceof ArrayList){
			List<FceIntervencaoTipoCaractExtracao> listNew = ((ArrayList<FceIntervencaoTipoCaractExtracao>)event.getNewValue());
			if (!Util.isNullOuVazio(listNew)){
				String opNew = (listNew.get(listNew.size() -1)).getIdeTipoCaracteristicaExtracao().getNomCaracteristicaExtracao(); //ultima opção escolhida
				
				List<FceIntervencaoTipoCaractExtracao> listOld = null;
				
				if(event.getOldValue() instanceof PersistentBag){
					PersistentBag persistentBag = (PersistentBag)event.getOldValue();
					
					listOld = (List<FceIntervencaoTipoCaractExtracao>) persistentBag.getStoredSnapshot();
				} else {
					listOld = ((ArrayList<FceIntervencaoTipoCaractExtracao>)event.getOldValue());
				}
				
				String opOld = "";
				if (!Util.isNullOuVazio(listOld)){
					opOld = (listOld.get(listOld.size() -1)).getIdeTipoCaracteristicaExtracao().getNomCaracteristicaExtracao(); //ultima opção escolhida
				}
				
				if (!opNew.equals(opOld) && opNew.equals("Outros")){
					op = opNew;
				}
			}
		}else if(event.getNewValue() instanceof TipoMineralExtraido){
			op = ((TipoMineralExtraido)event.getNewValue()).getNomTipoMineralExtraido(); 
		}
		
		if(op.equals("Outros")){
			Html.exibir("infoFceOutros");
		}
	}
	
	@Override
	public void verificarEdicao() {
		
	}

	@Override
	public void carregarAba() {
		
	}

	@Override
	public void finalizar() {
		try {
			this.fceIntervencaoMineracaoFacade.finalizar(this);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public StreamedContent getImprimirRelatorio() throws Exception {
		return super.getImprimirRelatorio(this.fce, DOC);
	}
	
	@Override
	public void limpar() {
		
	}

	@Override
	public boolean validarAba() {
		
		return false;
	}

	@Override
	public void abrirDialog() {
		Html.exibir("mineracaoIntervencao");
		Html.atualizar("formFceInterMineracao");
	}

	@Override
	public void prepararParaFinalizar() throws Exception {
		boolean finalizado = true;
		for(FceIntervencaoCaracteristicaExtracao c : this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()){
			for(FceIntervencaoTipoCaractExtracao t : c.getFceIntervencaoTipoCaracticasExtracao()){
				if(t.getIdeTipoCaracteristicaExtracao().getNomCaracteristicaExtracao().equals("Outros")){
					JsfUtil.addWarnMessage(Util.getString("fce_interv_min_finalizar_outros_carac"));
					finalizado = false;
					break;
				}
			}
			if(c.getIdeTipoMineralExtraido().getNomTipoMineralExtraido().equals("Outros")){
				JsfUtil.addWarnMessage(Util.getString("fce_interv_min_finalizar_outros_tipo_min"));
				finalizado = false;
			}
		}
		if(finalizado){
			super.concluirFce();
		}else {
			this.fce.setIndConcluido(finalizado);
			this.salvarFce();
		}
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			this.fceIntervencaoMineracao.setIdeFceIntervencaoMineracao(null);
			this.fceIntervencaoMineracao.setFce(getFce());
			//Caracteristica
			for(FceIntervencaoCaracteristicaExtracao c : this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()){
				c.setIdeFceIntervencaoMineracao(this.fceIntervencaoMineracao);
				c.setIdeFceIntervencaoCaracteristicaExtracao(null);
				c.setIdeLocalizacaoGeograficaIni(fceIntervencaoMineracaoFacade.duplicarLocalizacaoGeografica(c.getIdeLocalizacaoGeograficaIni()));
				c.setIdeLocalizacaoGeograficaFim(fceIntervencaoMineracaoFacade.duplicarLocalizacaoGeografica(c.getIdeLocalizacaoGeograficaFim()));
				for(FceIntervencaoTipoCaractExtracao t : c.getFceIntervencaoTipoCaracticasExtracao()){
					t.setIdeFceIntervencaoTipoCaractExtracao(null);
					t.setIdeFceIntervencaoCaracteristicaExtracao(c);
				}
			}
			//Lanc. Loc
			for(LancamentoEfluenteLocalizacaoGeografica c : this.fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica()){
				c.setIdeLancamentoEfluenteLocalizacaoGeografica(null);
				c.setIdeFceIntervencaoMineracao(this.fceIntervencaoMineracao);
				c.setIdeLocalizacaoGeografica(this.fceIntervencaoMineracaoFacade.duplicarLocalizacaoGeografica(c.getIdeLocalizacaoGeografica()));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			this.salvarFce();
			this.fceIntervencaoMineracaoFacade.salvarFceIntervencaoMineracao(this.fceIntervencaoMineracao);
			this.salvarLancamentoEfluenteLocalizacaoGeografica();
			this.salvarCaracteristicasExtracao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarFceDoTecnico(fce.getIdeDocumentoObrigatorio());
			this.fceIntervencaoMineracao = this.fceIntervencaoMineracaoFacade.getFceIntervencaoMineracao(fce);
			this.tiposMineraislExtraidos = fceIntervencaoMineracaoFacade.listarTipoMineralExtraido();
			
			EmpreendimentoRequerimento er = empreendimentoRequerimentoService.buscarEmpreendimentoRequerimento(requerimento);
			requerimento.setUltimoEmpreendimento(er.getIdeEmpreendimento());
			
			metodoRetornoCoordenada = new MetodoUtil(this, "retornarCoordenada");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public void salvar(){
		try {
			if(this.validarFce()){
				String mensagem = Util.getString("MSG-001");
				if(fceIntervencaoMineracao.getIdeFceIntervencaoMineracao() != null){
					mensagem = Util.getString("MSG-002");
				}
				if(!isFceSalvo()){
					salvarFce();
				}
				this.fceIntervencaoMineracaoFacade.salvarFceIntervencaoMineracao(this.fceIntervencaoMineracao);
				this.salvarLancamentoEfluenteLocalizacaoGeografica();
				JsfUtil.addSuccessMessage(mensagem);
				
				this.finalizar();
				
				
				if (!verificarOutros()){
					Html.exibir("dRelInterMineracao");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	private boolean verificarOutros() {
		boolean retorno = false;
		
		for (FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracaoObj : fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()) {
			for (FceIntervencaoTipoCaractExtracao fceIntervencaoTipoCaractExtracao : fceIntervencaoCaracteristicaExtracaoObj.getFceIntervencaoTipoCaracticasExtracao()){
				if (TipoCaracteristicaExtracaoEnum.OUTROS.getId().equals(fceIntervencaoTipoCaractExtracao.getIdeTipoCaracteristicaExtracao().getIdeTipoCaracteristicaExtracao())){
					retorno = true;
				}
			}
			
			if (Util.isNullOuVazio(fceIntervencaoCaracteristicaExtracaoObj.getIdeTipoMineralExtraido())){
				retorno = true;
			} else if ("Outros".equals(fceIntervencaoCaracteristicaExtracaoObj.getIdeTipoMineralExtraido().getNomTipoMineralExtraido())){
				retorno = true;
			}
		}
		
		return retorno;
	}

	private boolean validarCaracterizacao(){
		boolean r = true;
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getNomManancial())){
			MensagemUtil.msg0003("Manancial");
			r = false;
		}
		
		if (Util.isNullOuVazio(fceIntervencaoCaracteristicaExtracao.getFceIntervencaoTipoCaracticasExtracao())){
			MensagemUtil.msg0003("Caracteristica da Extração");
			r = false;
		}
		
		if (Util.isNullOuVazio(fceIntervencaoCaracteristicaExtracao.getIdeTipoMineralExtraido())){
			MensagemUtil.msg0003("Mineral Extraído");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValPeriodoMineracao()) || this.fceIntervencaoCaracteristicaExtracao.getValPeriodoMineracao() == 0){
			MensagemUtil.msg0003("Período da Atividade de Mineração");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValVazaoMineralExtraidoPolpa()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValVazaoMineralExtraidoPolpa())){
			MensagemUtil.msg0003("Volume de mineração extraído na polpa");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValVazaoAguaPolpa()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValVazaoAguaPolpa())){
			MensagemUtil.msg0003("Vazão de água na polpa");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValVazaoPolpa()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValVazaoPolpa())){
			MensagemUtil.msg0003("Vazão de polpa");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValTeorUmidade()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValTeorUmidade())){
			MensagemUtil.msg0003("Teor de umidade");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValAreaLavra()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValAreaLavra())){
			MensagemUtil.msg0003("Área de lavra");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValProducaoMaximaMensal()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValProducaoMaximaMensal())){
			MensagemUtil.msg0003("Produção máxima mensal");
			r = false;
		}
		
		if(Util.isNullOuVazio(this.fceIntervencaoCaracteristicaExtracao.getValQtdeDiasProducaoMes()) || BigDecimal.ZERO.equals(this.fceIntervencaoCaracteristicaExtracao.getValQtdeDiasProducaoMes())){
			MensagemUtil.msg0003("Quantidade de dias de produção/mês");
			r = false;
		}
		
		return r && !validarPeriodoAtividadeMineracao() && !validarQuantidadeDiasProducaoMes();
	}

	private boolean validarFce(){
		boolean r = true;
		for(FceIntervencaoCaracteristicaExtracao c : this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()){
			if(Util.isNullOuVazio(c.getNomManancial())){
				MensagemUtil.msg0003("Manancial");
				r = false;
			}
			
			if (Util.isNullOuVazio(c.getIdeFceIntervencaoCaracteristicaExtracao())){
				MensagemUtil.msg0003("Característica da Extração");
				r = false;
			}
		}
		return r;
	}
	
	public String getPonto(LocalizacaoGeografica localizacaoGeografica){
		if(localizacaoGeografica.getLatitudeInicial() == null){
			localizacaoGeografica.setLatitudeInicial(this.fceIntervencaoMineracaoFacade.getLatitude(localizacaoGeografica));
		}
		if(localizacaoGeografica.getLongitudeInicial() == null){
			localizacaoGeografica.setLongitudeInicial(this.fceIntervencaoMineracaoFacade.getLongitude(localizacaoGeografica));
		}
		return localizacaoGeografica.getLatitudeInicial() + " " + localizacaoGeografica.getLongitudeInicial();
	}
	
	public String getBacia(FceIntervencaoCaracteristicaExtracao caracteristicaExtracao){
		if(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().getBaciaHidrografica() == null){
			caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().setBaciaHidrografica(this.fceIntervencaoMineracaoFacade.getBaciaHidrografica(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni()));
		}
		if(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getBaciaHidrografica() == null){
			caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().setBaciaHidrografica(this.fceIntervencaoMineracaoFacade.getBaciaHidrografica(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim()));
		}
		
		StringBuilder st = new StringBuilder();
		st.append(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().getBaciaHidrografica());
		if(!st.toString().contains(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getBaciaHidrografica())){
			st.append(", " + caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getBaciaHidrografica());
		}
		return st.toString();
	}
	
	public String getRPGA(FceIntervencaoCaracteristicaExtracao caracteristicaExtracao){
		if(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().getRpga() == null){
			caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().setRpga(this.fceIntervencaoMineracaoFacade.getRpga(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni()));
		}
		if(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getRpga() == null){
			caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().setRpga(this.fceIntervencaoMineracaoFacade.getRpga(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim()));
		}
		
		StringBuilder st = new StringBuilder();
		st.append(caracteristicaExtracao.getIdeLocalizacaoGeograficaIni().getRpga());
		if(!st.toString().contains(caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getRpga())){
			st.append(", " + caracteristicaExtracao.getIdeLocalizacaoGeograficaFim().getRpga());
		}
		return st.toString();
	}
	
	public String getMunicipiosCaracteritica() throws Exception{
		StringBuilder sb = new StringBuilder();
		Municipio m1 = this.fceIntervencaoMineracaoFacade.getMunicipio(this.fceIntervencaoCaracteristicaExtracao.getIdeLocalizacaoGeograficaIni());
		Municipio m2 = this.fceIntervencaoMineracaoFacade.getMunicipio(this.fceIntervencaoCaracteristicaExtracao.getIdeLocalizacaoGeograficaFim());
		sb.append(m1.getNomMunicipio());
		if(!m1.equals(m2)){
			sb.append(", " + m2.getNomMunicipio());
		}
		return sb.toString();
	}
	
	public LancamentoEfluenteLocalizacaoGeografica getNovoLancamentoEfluenteLocalizacaoGeografica(){
		LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeograficaObj =  new LancamentoEfluenteLocalizacaoGeografica(this.fceIntervencaoMineracao);
		this.fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica().add(lancamentoEfluenteLocalizacaoGeograficaObj);
		return lancamentoEfluenteLocalizacaoGeograficaObj;
	}
	
	public String getUrlToVisualizar(LocalizacaoGeografica localizacaoGeografica){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(localizacaoGeografica);
	}
	
	public void excluirLocalizacaoLancEfluente(){
		try {
			if(lancamentoEfluenteLocalizacaoGeografica.getIdeLancamentoEfluenteLocalizacaoGeografica() != null){
				this.fceIntervencaoMineracaoFacade.excluir(lancamentoEfluenteLocalizacaoGeografica);
				this.fceIntervencaoMineracaoFacade.excluir(lancamentoEfluenteLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			}else {
				this.fceIntervencaoMineracaoFacade.excluir(lancamentoEfluenteLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			}
			this.fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica().remove(lancamentoEfluenteLocalizacaoGeografica);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	public void salvarCaracteristicaExtracao(){
		try {
			if(this.validarCaracterizacao()){
				// Validar
				if(fceIntervencaoCaracteristicaExtracao.getIdeFceIntervencaoMineracao().getIdeFceIntervencaoMineracao() == null){
					if(fceIntervencaoCaracteristicaExtracao.getIdeFceIntervencaoMineracao().getFce().getIdeFce() == null){
						this.salvarFce();
					}
					this.fceIntervencaoMineracaoFacade.salvarFceIntervencaoMineracao(fceIntervencaoCaracteristicaExtracao.getIdeFceIntervencaoMineracao());
					this.salvarLancamentoEfluenteLocalizacaoGeografica();
				}
				String mensagem = Util.getString("MSG-001");
				if(fceIntervencaoCaracteristicaExtracao.getIdeFceIntervencaoCaracteristicaExtracao() != null){
					mensagem = Util.getString("MSG-002");
				}
				this.fceIntervencaoMineracaoFacade.salvarFceIntervencaoCaracteristicaExtracao(fceIntervencaoCaracteristicaExtracao);
				Html.esconder("caracteristicaExtracao");
				JsfUtil.addSuccessMessage(mensagem);				
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	private void salvarCaracteristicasExtracao(){
		try {
			for(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao : this.fceIntervencaoMineracao.getFceIntervencaoCaracteristicaExtracoes()){
				this.fceIntervencaoMineracaoFacade.salvarFceIntervencaoCaracteristicaExtracao(fceIntervencaoCaracteristicaExtracao);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	private void salvarLancamentoEfluenteLocalizacaoGeografica() throws Exception{
		for(LancamentoEfluenteLocalizacaoGeografica lfl : this.fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica()){
			if(Util.isNullOuVazio(lfl.getValVazaoLancamentoManancial()) || lfl.isEditar()) {
				throw new Exception(Util.getString("MSG-003", "Vazão de lançamento do manancial"));
			} else {
				this.fceIntervencaoMineracaoFacade.salvarOuAtualizarLancamentoEfluenteLocalizacaoGeografica(lfl);
			}
		}
	}
	
	/**
	 * RN 00425  FCE Intervenção Mineração – Razão da água  por mineral extraído
	O campo “Razão água/mineral extraído” é um campo somente leitura e deve exibir o resultado da divisão entre os campos “Vazão de água na polpa” e “Volume de mineração extraído na polpa”. O seu conteúdo deve ser atualizado sempre que a página for carregada ou caso ocorra alteração em um dos campos da divisão.
	Obs. O sistema não deve fazer o cálculo caso um dos dois campos da divisão tenha seu valor nulo ou igual a 0 (zero).
	 */
	public BigDecimal getCalculoRazaoDaAguaMineralExtraido(){
		if(this.fceIntervencaoCaracteristicaExtracao.getValVazaoAguaPolpa() != null && this.fceIntervencaoCaracteristicaExtracao.getValVazaoAguaPolpa().doubleValue() != 0.0d
				&& this.fceIntervencaoCaracteristicaExtracao.getValVazaoMineralExtraidoPolpa() != null && this.fceIntervencaoCaracteristicaExtracao.getValVazaoMineralExtraidoPolpa().doubleValue() != 0.0d){
			return this.fceIntervencaoCaracteristicaExtracao.getValVazaoAguaPolpa().divide(this.fceIntervencaoCaracteristicaExtracao.getValVazaoMineralExtraidoPolpa(), RoundingMode.HALF_UP).setScale(2, RoundingMode.CEILING);			
		}else{
			return null;			
		}
	}
	
	public boolean validarPeriodoAtividadeMineracao(){
		if(this.fceIntervencaoCaracteristicaExtracao.getValPeriodoMineracao().doubleValue() > 24){
			JsfUtil.addWarnMessage("O tempo de atividade de mineração não pode ser maior que 24h/dia.");//[INF-0094]

			return true;
		}
		return false;
	}

	public boolean validarQuantidadeDiasProducaoMes(){
		if(this.fceIntervencaoCaracteristicaExtracao.getValQtdeDiasProducaoMes().doubleValue() > 31){
			JsfUtil.addWarnMessage("A quantidade de dias de produção/mês não pode ser maior que 31.");//[INF-0095]   
			
			return true;
		}
		return false;
	}
	
	public void validarManancial(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao, boolean edicao) {
		if(Util.isNullOuVazio(fceIntervencaoCaracteristicaExtracao.getNomManancial())){
			MensagemUtil.msg0003("Manancial");
		} else {
			fceIntervencaoCaracteristicaExtracao.setEditar(edicao);
		}
	}
	
	public void validarVazaoLancamentoManancial(LancamentoEfluenteLocalizacaoGeografica lfl, boolean edicao) {
		if(Util.isNullOuVazio(lfl.getValVazaoLancamentoManancial())){
			MensagemUtil.msg0003("Vazão de lançamento do manancial");
		} else {
			lfl.setEditar(edicao);
		}
		
		Html.atualizar("formFceInterMineracao:dataTableIntervMine");
	}
	
	public FceIntervencaoMineracao getFceIntervencaoMineracao() {
		return fceIntervencaoMineracao;
	}

	public void setFceIntervencaoMineracao(
			FceIntervencaoMineracao fceIntervencaoMineracao) {
		this.fceIntervencaoMineracao = fceIntervencaoMineracao;
	}

	public LancamentoEfluenteLocalizacaoGeografica getLancamentoEfluenteLocalizacaoGeografica() {
		return lancamentoEfluenteLocalizacaoGeografica;
	}

	public void setLancamentoEfluenteLocalizacaoGeografica(
			LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica) {
		this.lancamentoEfluenteLocalizacaoGeografica = lancamentoEfluenteLocalizacaoGeografica;
	}

	/**
	 * @return the fceIntervencaoCaracteristicaExtracao
	 */
	public FceIntervencaoCaracteristicaExtracao getFceIntervencaoCaracteristicaExtracao() {
		return fceIntervencaoCaracteristicaExtracao;
	}

	/**
	 * @param fceIntervencaoCaracteristicaExtracao the fceIntervencaoCaracteristicaExtracao to set
	 */
	public void setFceIntervencaoCaracteristicaExtracao(
			FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) {
		this.fceIntervencaoCaracteristicaExtracao = fceIntervencaoCaracteristicaExtracao;
	}

	/**
	 * @return the fceIntervencaoMineracaoFacade
	 */
	public FceIntervencaoMineracaoFacade getFceIntervencaoMineracaoFacade() {
		return fceIntervencaoMineracaoFacade;
	}

	/**
	 * @param fceIntervencaoMineracaoFacade the fceIntervencaoMineracaoFacade to set
	 */
	public void setFceIntervencaoMineracaoFacade(
			FceIntervencaoMineracaoFacade fceIntervencaoMineracaoFacade) {
		this.fceIntervencaoMineracaoFacade = fceIntervencaoMineracaoFacade;
	}

	/**
	 * @return the fceIntervencaoTipoCaracticasExtracao
	 */
	public List<FceIntervencaoTipoCaractExtracao> getFceIntervencaoTipoCaracticasExtracao() {
		return fceIntervencaoTipoCaracticasExtracao;
	}

	/**
	 * @param fceIntervencaoTipoCaracticasExtracao the fceIntervencaoTipoCaracticasExtracao to set
	 */
	public void setFceIntervencaoTipoCaracticasExtracao(
			List<FceIntervencaoTipoCaractExtracao> fceIntervencaoTipoCaracticasExtracao) {
		this.fceIntervencaoTipoCaracticasExtracao = fceIntervencaoTipoCaracticasExtracao;
	}

	/**
	 * @return the tiposMineraislExtraidos
	 */
	public List<TipoMineralExtraido> getTiposMineraislExtraidos() {
		return tiposMineraislExtraidos;
	}

	/**
	 * @param tiposMineraislExtraidos the tiposMineraislExtraidos to set
	 */
	public void setTiposMineraislExtraidos(
			List<TipoMineralExtraido> tiposMineraislExtraidos) {
		this.tiposMineraislExtraidos = tiposMineraislExtraidos;
	}

	public MetodoUtil getMetodoRetornoCoordenada() {
		return metodoRetornoCoordenada;
	}

	public void setMetodoRetornoCoordenada(MetodoUtil metodoRetornoCoordenada) {
		this.metodoRetornoCoordenada = metodoRetornoCoordenada;
	}
	

}
