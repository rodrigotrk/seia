package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaElementoIntervencao;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovel;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaResponsavelTecnico;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaTecnicaUtilizada;
import br.gov.ba.seia.entity.ElementoIntervencaoQueimaControlada;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ObjetivoQueimaControlada;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TecnicaQueimaControlada;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.DeclaracaoQueimaControladaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.util.ExpressaoRegularUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Named("dqcController")
@ViewScoped
public class DeclaracaoQueimaControladaController {
	
	@EJB
	private DeclaracaoQueimaControladaFacade facade;
	
	private int tabAtual;
	private String numSicar;
	private Requerimento requerimento;
	private boolean existeOpcaoOutroSelecionada;
	
	private DeclaracaoQueimaControlada dqc;
	private DeclaracaoQueimaControladaImovel dqcImovel;
	private DeclaracaoQueimaControladaResponsavelTecnico dqcResponsavelTecnico;
	
	private List<DeclaracaoQueimaControladaImovel> listaDQCImovel;
	private List<DeclaracaoQueimaControladaTecnicaUtilizada> listaDQCTecnicaUtilizada;
	private List<DeclaracaoQueimaControladaElementoIntervencao> listaDQCElementoIntervencao;
	private List<DeclaracaoQueimaControladaResponsavelTecnico> listaDQCResponsavelTecnico;
	
	public DeclaracaoQueimaControladaController() {
		super();
	}

	public DeclaracaoQueimaControladaController(DeclaracaoQueimaControlada dqc) {
		super();
		this.dqc = dqc;
	}

	public void init() {
		try{
			tabAtual = 0;
			
			carregarDQC();
			carregarListasDqc();
		} catch (Exception e) {
			MensagemUtil.erro008();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarAtoDeclaratorioComBooleanosFalsos() {
		dqc.setIndAceiteResponsabilidade(false);
		facade.salvarAtoDeclaratorio(dqc.getIdeAtoDeclaratorio());
	}
	
	public StreamedContent getImprimirRelatorio(){
        try {
        	return new ImpressoraAtoDeclaratorio().imprimirRelatorio(dqc.getIdeAtoDeclaratorio());
        } catch (Exception e) {
            Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }
	
	public void carregarListasDqc() {
		carregarListaDQCImovel();
		carregarListaDQCTecnicaUtilizada();
		carregarListaDQCElementoIntervencao();
		carregarListaDQCResponsavelTecnico();
	}
	
	/************
	/*			*
	//XXX: DQC  *
	/* 			*
	/************/
	
	private void carregarDQC() {
		dqc = new DeclaracaoQueimaControlada();
		
		AtoDeclaratorio ad =  facade.buscarAtoDeclaratorioBy(requerimento, DocumentoObrigatorioEnum.FORMULARIO_DQC);
		
		if(!Util.isNullOuVazio(ad)) {
			dqc = facade.buscarDeclaracaoQueimaControladaPorAtoDeclaratorio(ad); 
		} else {
			dqc.setIdeAtoDeclaratorio(new AtoDeclaratorio(requerimento, DocumentoObrigatorioEnum.FORMULARIO_DQC));
		}
	}
	
	public boolean salvarDQC(){
		
		if(validaDQC()) {
			facade.salvarAtoDeclaratorio(dqc.getIdeAtoDeclaratorio());
			facade.salvarDeclaracaoQueimaControlada(dqc);
		} else {
			return false;
		}
		
		if(tabAtual > 0) { //Aba Dados da Queima (Etapa 2)
			
			if(validaDQCImovel()) {
				salvarDQCImovel();
			} else {
				return false;
			}
			
			if(!validaTodosDQCIOQC()) {
				return false;
			}
			
			if(validaDQCTecnicaUtilizada()) {
				salvarDQCTecnicaUtilizada();
			} else {
				return false; 
			}
			
			if(validaDQCElementoIntervencao()) {
				salvarDQCElementoIntervencao();
			} else {
				return false;
			}
			
			if(validaDQCResponsavelTecnico()) {
				salvarDQCResponsavelTecnico();
			} else {
				return false;
			}
		}
		
		if(tabAtual > 1) {
			if(!(dqc.getIndCienteTermoCompromisso() && dqc.getIndAceiteResponsabilidade())) {
				MensagemUtil.msg0003("Ciente");
				return false;
			}
		}
		
		MensagemUtil.msg0010();
		
		return true;
	}
	
	public boolean validaDQC() {
		if(dqc == null || dqc.getIndCienteTermoCompromisso() == null || !dqc.getIndCienteTermoCompromisso()) {
			MensagemUtil.msg0003("Ciente");
			return false;
		}
		
		return true;
	}
	
	
	public void finalizarDQC() {
		
		if(validaFinalizarDQC()) {
			facade.finalizarAtoDeclaratorio(dqc.getIdeAtoDeclaratorio());
			Html.esconder("dialogDQC");
			Html.exibir("confirmDialogRelatorio");
		}
	}
	
	public boolean validaFinalizarDQC() {
		
		if(salvarDQC()) {
			existeOpcaoOutroSelecionada = false;
			
			if(validaDQCTecnicaUtilizada()) {
				if(existeOpcaoOutroSelecionada) return false;
				
				if(validaDQCElementoIntervencao()) {
					if(existeOpcaoOutroSelecionada) return false;
					
					
					if(validaTodosDQCIOQC()) {
						if(existeOpcaoOutroSelecionada) return false;
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**************
	/*			  *
	//XXX: IMÓVEL *
	/* 			  *
	/**************/
	
	private void carregarListaDQCImovel() {
		listaDQCImovel = new ArrayList<DeclaracaoQueimaControladaImovel>();
		listaDQCImovel.addAll(facade.listarDeclaracaoQueimaControladaImovelPorDQC(dqc));
		
		for (PerguntaRequerimento pr : facade.listarPerguntaRequerimentoRespondidasParaDQC(requerimento.getIdeRequerimento())) {
			
			boolean imovelNovo = true;
			//Verifica se o imóvel já existe na lista
			for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
				if(pr.getIdeImovel().getIdeImovel().equals(dqcI.getIdeImovel().getIdeImovel())) {
					imovelNovo = false;
				}
			}
			
			if(imovelNovo) {
				DeclaracaoQueimaControladaImovel di = new DeclaracaoQueimaControladaImovel(dqc);
				di.setIdeImovel(pr.getIdeImovel());
				di.setIdeLocalizacaoGeografica(pr.getIdeLocalizacaoGeografica());
				di.setIndArrendado(false);
				
				listaDQCImovel.add(di);
			}
		}
		
		for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
			carregarListaDQCIObjetivoQueimaControlada(dqcI);
		}
	}
	
	public void prepararDialogIncluirImovel() {
		numSicar = new String();
		dqcImovel = new DeclaracaoQueimaControladaImovel(dqc);
	}
	
	public void buscarImovel() {
		dqcImovel.setIdeImovel(facade.buscarImovelPorNumeroCar(new ImovelRural(numSicar)));
		dqcImovel.setIndArrendado(true);
		
		if(dqcImovel.getIdeImovel() == null) {
			JsfUtil.addErrorMessage("Número CAR inválido.");
		}
	}
	
	public void adicionarDQCImovel() {
		
		if (validaAdicionarDQCImovel()) {
			
			salvarAtoDeclaratorioComBooleanosFalsos();
			salvarDQCImovel();
			
			listaDQCImovel.add(dqcImovel);
			prepararDialogIncluirImovel();
			
			Html.atualizar("tabViewDialogDQC:formAbaDadosQueima:panelDadosQueima");
			Html.esconder("dialogIncluirImovelCAR");
			
			MensagemUtil.msg0010();
		}
	}
	
	public boolean validaAdicionarDQCImovel() {
		
		if(!Util.isNullOuVazio(dqcImovel.getIdeImovel())
			&& !Util.isNullOuVazio(dqcImovel.getIdeImovel().getIdeEndereco())
			&& !Util.isNullOuVazio(dqcImovel.getIdeImovel().getIdeEndereco().getIdeLogradouro())
			&& !Util.isNullOuVazio(dqcImovel.getIdeImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio())){
			
			if(dqcImovel.getIdeImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getIndBloqueioDQC()){
				JsfUtil.addErrorMessage("A queima está suspensa neste município.");
				return false;
			}
		}
		
		for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
			
			if(!dqcImovel.isEditar() && dqcI.getIdeImovel().getIdeImovel().equals(dqcImovel.getIdeImovel().getIdeImovel())) {
				JsfUtil.addErrorMessage("O imóvel informado já foi cadastrado.");
				return false;
			}
			
		}
		
		return true;
	}
	
	public void excluirDqcImovel() {
		
		salvarAtoDeclaratorioComBooleanosFalsos();
		
		//Remove os objetivos da queima
		if(!Util.isNullOuVazio(dqcImovel.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection())) {
			
			for (DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc : dqcImovel.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection()) {
				if(!Util.isNullOuVazio(dqciOqc)) facade.excluirDQCIObjetivoQueimaControlada(dqciOqc);
			}
			
			dqcImovel.setDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection(null);
		}
		
		//Remove o dqcImovel
		if(!Util.isNullOuVazio(dqcImovel)) {
			facade.excluirDeclaracaoQueimaControladaImovel(dqcImovel);
		}
		
		//Remove a localização geográfica
		if(!Util.isNullOuVazio(dqcImovel.getIdeLocalizacaoGeografica())) {
			facade.excluirLocalizacaoGeografica(dqcImovel.getIdeLocalizacaoGeografica());
		}
		
		listaDQCImovel.remove(dqcImovel);
		dqcImovel = null;
		Html.atualizar("tabViewDialogDQC:formAbaDadosQueima:panelDadosQueima");
		Html.esconder("confirmarExclusaoDqcImovel");
		
		MensagemUtil.msg0005();
	}
	
	public void salvarDQCImovel() {
		
		for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
			facade.salvarDeclaracaoQueimaControladaImovel(dqcI);
		}
	}

	public boolean validaDQCImovel() {
		
		if(Util.isNullOuVazio(listaDQCImovel)) {
			MensagemUtil.msg0003("Imóveis");
			return false;
		}
		
		for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
			
			if(dqcI.getIndArrendado()) {
				if(Util.isNullOuVazio(dqcI.getIdeLocalizacaoGeografica()) 
						|| (Util.isNullOuVazio(dqcI.getIdeLocalizacaoGeografica().getDadoGeograficoCollection())) 
							&& Util.isNullOuVazio(dqcI.getIdeLocalizacaoGeografica().getParamPersistDadoGeoCollection())){
					
					MensagemUtil.msg0003("Localização geográfica do imóvel " + dqcI.getIdeImovel().getImovelRural().getDesDenominacao());
					return false;
				}
			}
			
			if(!validaDQCIObjetivoQueimaControlada(dqcI)) {
				return false;
			}
		}
		
		return true;
	}
	
	/****************
	/*			    *
	//XXX: OBJETIVO *
	/* 			    *
	/****************/
	
	public void carregarListaDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovel dqcI) {
		dqcI.setDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection(new ArrayList<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada>());
		
		List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> listaDQCIObjetivoQueimaControladaPorDQC = 
				facade.listarDQCIObjetivoQueimaControladaPorDQCI(dqcI);
		
		//Carrega todos os elementos possíveis
		for (ObjetivoQueimaControlada oqc : facade.listarTodosObjetivoQueimaControlada()) {
			dqcI.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection().add(
					new DeclaracaoQueimaControladaImovelObjetivoQueimaControlada(dqcI, oqc));
		}
		
		//Substitui os elementos possíveis pelos já selecionados
		for (DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqcGeral : dqcI.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection()) {
			
			for (DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqcSelecionada : listaDQCIObjetivoQueimaControladaPorDQC) {
				
				if(dqciOqcSelecionada.getIdeObjetivoQueimaControlada().getIdeObjetivoQueimaControlada().equals(
						dqciOqcGeral.getIdeObjetivoQueimaControlada().getIdeObjetivoQueimaControlada())) {
					
					dqciOqcGeral.setChecked(true);
					dqciOqcGeral.setValAreaPrevistaQueima(dqciOqcSelecionada.getValAreaPrevistaQueima());
					dqciOqcGeral.setIdeDeclaracaoQueimaControladaImovelObjetivoQueimaControlada(dqciOqcSelecionada.getIdeDeclaracaoQueimaControladaImovelObjetivoQueimaControlada());
					dqciOqcGeral.setIdeDeclaracaoQueimaControladaImovel(dqciOqcSelecionada.getIdeDeclaracaoQueimaControladaImovel());
					dqciOqcGeral.setIdeObjetivoQueimaControlada(dqciOqcSelecionada.getIdeObjetivoQueimaControlada());
				}
			}
		}
	}
	
	public void salvarDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovel dqci) {
		
		salvarAtoDeclaratorioComBooleanosFalsos();
		
		if (validaDQCIObjetivoQueimaControlada(dqci)) {
			for (DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc : dqci.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection()) {
				
				if (dqciOqc.isChecked()) {
					if(dqciOqc.getIdeDeclaracaoQueimaControladaImovel().getIdeDeclaracaoQueimaControladaImovel() == null) {
						facade.salvarDeclaracaoQueimaControladaImovel(dqciOqc.getIdeDeclaracaoQueimaControladaImovel());
					}
					
					facade.salvarDQCIObjetivoQueimaControlada(dqciOqc);
					
				} else if (!Util.isNullOuVazio(dqciOqc)) {
					facade.excluirDQCIObjetivoQueimaControlada(dqciOqc);
					dqciOqc.setIdeDeclaracaoQueimaControladaImovelObjetivoQueimaControlada(null);
					dqciOqc.setValAreaPrevistaQueima(null);
				}
			}
			
			Html.atualizar("tabViewDialogDQC");
			Html.esconder("dialogDQCImovel");
			MensagemUtil.msg0010();
		}
	}
	
	public boolean validaDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovel dqci) {
		
		boolean existeAlgumSelecionado = false;
		
		if(Util.isNullOuVazio(dqci.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection())) {
			return false;
		} else {
			for (DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc : dqci.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection()) {
				
				if(dqciOqc.isChecked()) {
					existeAlgumSelecionado = true;
	
					if(dqciOqc.getIdeObjetivoQueimaControlada().getDesObjetivoQueimaControlada().equals("Outro")) {
						existeOpcaoOutroSelecionada = true;
						MensagemUtil.info0035();
						
					}
					//Retirado código abaixo pelo ticket 35912
//					else if(exibeAreaPrevistaParaQueima(dqciOqc) && Util.isNullOuVazio(dqciOqc.getValAreaPrevistaQueima())) {
//						
//						MensagemUtil.msg0003("Área prevista para a queima");
//						return false;
//					}
				}
			}
			
			if(Util.isNullOuVazio(dqci.getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection()) || !existeAlgumSelecionado) {
				MensagemUtil.msg0003("Objetivo(s) da queima");
				return false;
			}
			
			return true;
			
		}
	}
	
	public boolean validaTodosDQCIOQC() {
		
		for (DeclaracaoQueimaControladaImovel dqcI : listaDQCImovel) {
			if(!validaDQCIObjetivoQueimaControlada(dqcI)) return false;
		}
		
		return true;
	}
	
	public boolean exibeAreaPrevistaParaQueima(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc) {
		
		if(dqciOqc != null
				&& dqciOqc.isChecked() 
				&& dqciOqc.getIdeObjetivoQueimaControlada().getIndPossuiAreaPrevista() 
				&& dqciOqc.getIdeDeclaracaoQueimaControladaImovel().getIndArrendado()) {
			
			return true;
		}
		
		return false;
	}

	public void verificaObjetivoSelecionadoOutros(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc) {
		if(dqciOqc.isChecked() && dqciOqc.getIdeObjetivoQueimaControlada().getDesObjetivoQueimaControlada().equalsIgnoreCase("Práticas Agrossilvopastoris")) {
			dqciOqc.setValAreaPrevistaQueima(null);
		}
		
		if(dqciOqc.isChecked() && dqciOqc.getIdeObjetivoQueimaControlada().getDesObjetivoQueimaControlada().equals("Outro")) {
			MensagemUtil.info0035();
		}
	}
	
	/***************
	/*			   *
	//XXX: TÉCNICA *
	/* 			   *
	/***************/
	
	public void carregarListaDQCTecnicaUtilizada() {
		listaDQCTecnicaUtilizada = new ArrayList<DeclaracaoQueimaControladaTecnicaUtilizada>();
		List<DeclaracaoQueimaControladaTecnicaUtilizada> listaDQCTecnicaUtilizadaPorDQC = facade.listarDQCTecnicaUtilizadaPorDQC(dqc);
		
		//Carrega todas as técnicas possíveis
		for (TecnicaQueimaControlada tqc : facade.listarTodosTecnicaQueimaControlada()) {
			listaDQCTecnicaUtilizada.add(new DeclaracaoQueimaControladaTecnicaUtilizada(dqc, tqc));
		}
		
		//Substitui as técnicas possíveis pelas já selecionadas
		for (DeclaracaoQueimaControladaTecnicaUtilizada dqcTqcGeral : listaDQCTecnicaUtilizada) {
			
			for (DeclaracaoQueimaControladaTecnicaUtilizada dqcTqcSelecionada : listaDQCTecnicaUtilizadaPorDQC) {
				
				if(dqcTqcSelecionada.getIdeTecnicaQueimaControlada().getIdeTecnicaQueimaControlada().equals(
						dqcTqcGeral.getIdeTecnicaQueimaControlada().getIdeTecnicaQueimaControlada())) {
					
					dqcTqcGeral.setChecked(true);
					dqcTqcGeral.setIdeDeclaracaoQueimaControladaTecnicaUtilizada(dqcTqcSelecionada.getIdeDeclaracaoQueimaControladaTecnicaUtilizada());
					dqcTqcGeral.setIdeDeclaracaoQueimaControlada(dqcTqcSelecionada.getIdeDeclaracaoQueimaControlada());
					dqcTqcGeral.setIdeTecnicaQueimaControlada(dqcTqcSelecionada.getIdeTecnicaQueimaControlada());
				}
			}
		}
	}
	
	public void salvarDQCTecnicaUtilizada() {
		
		for (DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc : listaDQCTecnicaUtilizada) {
			
			if(dqcTqc.isChecked()) {
				
				facade.salvarDQCTecnicaUtilizada(dqcTqc);
				
			} else if(!Util.isNullOuVazio(dqcTqc)) {
				facade.excluirDQCTecnicaUtilizada(dqcTqc);
				dqcTqc.setIdeDeclaracaoQueimaControladaTecnicaUtilizada(null);
			}
		}
	}

	public boolean validaDQCTecnicaUtilizada() {
		
		boolean existeAlgumSelecionado = false;
			
		for (DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc : listaDQCTecnicaUtilizada) {
			
			if(dqcTqc.isChecked()) {
				existeAlgumSelecionado = true;

				if(dqcTqc.getIdeTecnicaQueimaControlada().getNomTecnicaQueimaControlada().equals("Outro")) {
					existeOpcaoOutroSelecionada = true;
					MensagemUtil.info0035();
				}
			}
		}
		
		if(Util.isNullOuVazio(listaDQCTecnicaUtilizada) || !existeAlgumSelecionado) {
			MensagemUtil.msg0003("Técnica da queima");
			return false;
		}
		
		return true;
	}

	public void verificaTecnicaSelecionadoOutro(DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc) {
		if(dqcTqc.isChecked() && dqcTqc.getIdeTecnicaQueimaControlada().getNomTecnicaQueimaControlada().equals("Outro")) {
			MensagemUtil.info0035();
		}
	}
	
	/****************
	/*			    *
	//XXX: ELEMENTO *
	/* 			    *
	/****************/
	
	public void carregarListaDQCElementoIntervencao() {
		listaDQCElementoIntervencao = new ArrayList<DeclaracaoQueimaControladaElementoIntervencao>();
		List<DeclaracaoQueimaControladaElementoIntervencao> listaDQCElementoIntervencaoPorDQC = facade.listarDQCElementoIntervencaoPorDQC(dqc);
		
		//Carrega todos os elementos possíveis
		for (ElementoIntervencaoQueimaControlada eqc : facade.listarTodosElementoIntervencaoQueimaControlada()) {
			listaDQCElementoIntervencao.add(new DeclaracaoQueimaControladaElementoIntervencao(dqc, eqc));
		}
		
		//Substitui os elementos possíveis pelos já selecionados
		for (DeclaracaoQueimaControladaElementoIntervencao dqcEiGeral : listaDQCElementoIntervencao) {
			
			for (DeclaracaoQueimaControladaElementoIntervencao dqcEiSelecionada : listaDQCElementoIntervencaoPorDQC) {
				
				if(dqcEiSelecionada.getIdeElementoIntervencaoQueimaControlada().getIdeElementoIntervencaoQueimaControlada().equals(
						dqcEiGeral.getIdeElementoIntervencaoQueimaControlada().getIdeElementoIntervencaoQueimaControlada())) {
					
					dqcEiGeral.setChecked(true);
					dqcEiGeral.setValDistancia(dqcEiSelecionada.getValDistancia());
					dqcEiGeral.setIdeDeclaracaoQueimaControladaElementoIntervencao(dqcEiSelecionada.getIdeDeclaracaoQueimaControladaElementoIntervencao());
					dqcEiGeral.setIdeDeclaracaoQueimaControlada(dqcEiSelecionada.getIdeDeclaracaoQueimaControlada());
					dqcEiGeral.setIdeElementoIntervencaoQueimaControlada(dqcEiSelecionada.getIdeElementoIntervencaoQueimaControlada());
				}
			}
		}
	}
	
	public void salvarDQCElementoIntervencao() {
		
		for (DeclaracaoQueimaControladaElementoIntervencao dqcEi : listaDQCElementoIntervencao) {
			
			if(dqcEi.isChecked()) {
				
				facade.salvarDQCElementoIntervencao(dqcEi);

			} else if(!Util.isNullOuVazio(dqcEi)) {
				facade.excluirDQCElementoIntervencao(dqcEi);
				dqcEi.setIdeDeclaracaoQueimaControladaElementoIntervencao(null);
				dqcEi.setValDistancia(null);
			}
		}
	}
	
	public boolean validaDQCElementoIntervencao() {
		
		boolean existeAlgumSelecionado = false;
			
		for (DeclaracaoQueimaControladaElementoIntervencao dqcEi : listaDQCElementoIntervencao) {
			
			if(dqcEi.isChecked()) {
				existeAlgumSelecionado = true;

				if(dqcEi.getIdeElementoIntervencaoQueimaControlada().getNomElementoIntervencao().equals("Outro")) {
					existeOpcaoOutroSelecionada = true;
					MensagemUtil.info0035();
					
				} else if(Util.isNullOuVazio(dqcEi.getValDistancia())) {
					MensagemUtil.msg0003("Distância mínima");
					return false;
				}
			}
		}
		
		if(Util.isNullOuVazio(listaDQCElementoIntervencao) || !existeAlgumSelecionado) {
			MensagemUtil.msg0003("Elementos de intervenção");
			return false;
		}
		
		return true;
	}
	
	public void verificaElementoSelecionadoOutro(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		if(dqcEi.isChecked() && dqcEi.getIdeElementoIntervencaoQueimaControlada().getNomElementoIntervencao().equals("Outro")){
			MensagemUtil.info0035();
		}
	}
	
	public boolean renderizaDistanciaMinimaElemento(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		
		if(dqcEi == null || !dqcEi.isChecked()) {
			return false;
		}
		
		if(dqcEi.getIdeElementoIntervencaoQueimaControlada().getNomElementoIntervencao().equals("Outro")) {
			return false;
		}
		
		return true;
	}
	
	/****************************
	/*					 		*
	//XXX: RESPONSÁVEL TÉCNICO  *
	/* 					 		*
	/****************************/
	
	public void carregarListaDQCResponsavelTecnico() {
		listaDQCResponsavelTecnico = new ArrayList<DeclaracaoQueimaControladaResponsavelTecnico>();
		listaDQCResponsavelTecnico.addAll(facade.listarDQCResponsavelTecnicoPorDQC(dqc));
	}
	
	public void prepararDialogDQCResponsavelTecnico() {
		dqcResponsavelTecnico = new DeclaracaoQueimaControladaResponsavelTecnico(new PessoaFisica(), dqc);
		Html.exibir("dialogDQCResponsavelTecnico");
		Html.atualizar("formDQCResponsavelTecnico");
	}
	
	public void buscarPessoaFisica() {
		if(!Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getNumCpf())) {
			if(dqcResponsavelTecnico.getIdePessoaFisica().getNumCpf().matches(ExpressaoRegularUtil.getCpf())){
				
				PessoaFisica pessoaFisica = facade.buscarPessoaFisicaPorCPF(dqcResponsavelTecnico.getIdePessoaFisica());
				
				if(Util.isNullOuVazio(pessoaFisica)) {
					dqcResponsavelTecnico.getIdePessoaFisica().setIdePessoaFisica(null);
					JsfUtil.addErrorMessage("CPF não encontrado.");
					
				} else {
					pessoaFisica.setResponsavelTecnico(true);
					dqcResponsavelTecnico.setIdePessoaFisica(pessoaFisica);
				}
			} else {
				dqcResponsavelTecnico.getIdePessoaFisica().setIdePessoaFisica(null);
				JsfUtil.addErrorMessage("CPF inválido.");
			}
		} else {
			dqcResponsavelTecnico.getIdePessoaFisica().setIdePessoaFisica(null);
			MensagemUtil.msg0003("CPF");
		}
		
		Html.atualizar("formDQCResponsavelTecnico");
	}
	
	public void adicionarDQCResponsavelTecnico() {
		
		if(validaAdicionarDQCResponsavelTecnico()) {
			salvarAtoDeclaratorioComBooleanosFalsos();
		
			if(!dqcResponsavelTecnico.isEditar()) {
				listaDQCResponsavelTecnico.add(dqcResponsavelTecnico);
			}
			
			salvarDQCResponsavelTecnico();
			
			MensagemUtil.msg0001();
			
			Html.atualizar("tabViewDialogDQC:formAbaDadosQueima:panelDadosQueima");
			Html.esconder("dialogDQCResponsavelTecnico");
		}
	}
	
	public boolean validaAdicionarDQCResponsavelTecnico() {
		
		for (DeclaracaoQueimaControladaResponsavelTecnico dqcRt : listaDQCResponsavelTecnico) {
			if(!dqcResponsavelTecnico.isEditar() &&
					dqcRt.getIdePessoaFisica().getIdePessoaFisica().equals(dqcResponsavelTecnico.getIdePessoaFisica().getIdePessoaFisica())) {
				
				JsfUtil.addErrorMessage("Responsável técnico já inserido.");
				return false;
			}
		}
		
		if(dqcResponsavelTecnico == null) {
			MensagemUtil.erro008();
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica())) {
			MensagemUtil.erro008();
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getNumCpf())) {
			MensagemUtil.msg0003("CPF");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getNomPessoa())) {
			MensagemUtil.msg0003("Nome");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getDtcNascimento())) {
			MensagemUtil.msg0003("Data de nascimento");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getPessoa())) {
			MensagemUtil.erro008();
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getPessoa().getDesEmail())) {
			MensagemUtil.msg0003("Email");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getNomMae())) {
			MensagemUtil.msg0003("Nome da mãe");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getDesNaturalidade())) {
			MensagemUtil.msg0003("Naturalidade");
			return false;
			
		} else if(Util.isNullOuVazio(dqcResponsavelTecnico.getIdePessoaFisica().getIdePais())) {
			MensagemUtil.msg0003("País");
			return false;
			
		}
		
		return true;
	}
	
	public void excluirDQCResponsavelTecnico() {
		
		salvarAtoDeclaratorioComBooleanosFalsos();
		
		if(!Util.isNullOuVazio(dqcResponsavelTecnico)) {
			facade.excluirDQCResponsavelTecnico(dqcResponsavelTecnico);
		}
		
		listaDQCResponsavelTecnico.remove(dqcResponsavelTecnico);
		
		Html.atualizar("tabViewDialogDQC:formAbaDadosQueima:panelDadosQueima");
		Html.esconder("confirmDialogExcluirDQCResponsavelTecnico");
		MensagemUtil.msg0005();
	}
	
	public boolean validaDQCResponsavelTecnico() {
		if(Util.isNullOuVazio(listaDQCResponsavelTecnico)) {
			MensagemUtil.msg0003("Responsável técnico");
			return false;
		}
		
		return true;
	}
	
	public void salvarDQCResponsavelTecnico() {
		
		for (DeclaracaoQueimaControladaResponsavelTecnico dqcRt : listaDQCResponsavelTecnico) {
			facade.salvarPessoaFisica(dqcRt.getIdePessoaFisica());
			facade.salvarDQCResponsavelTecnico(dqcRt);
		}
	}
	
	/************************
	/*					 	*
	//XXX: CONTROLE DE ABAS *
	/* 					 	*
	/************************/
	
	public void incrementaIndexAba() {
		
		if(salvarDQC()) {
			tabAtual++;
		}
	}
	
	public void decrementaIndexAba() {
		tabAtual--;
	}
	
	public void onTabChange(TabChangeEvent event) {
		int activeTabIndex = 0;
		
		for (UIComponent comp : event.getTab().getParent().getChildren()) {
			if (comp.getId().equals(event.getTab().getId())) break;
			else activeTabIndex++;
		}
		
		tabAtual = activeTabIndex;
	}
	
	public Boolean getVisualizaBotaoAvancar() {
		return (tabAtual < 2);
	}
	
	public Boolean getVisualizaBotaoAnterior() {
		return (tabAtual > 0);
	}
	
	public Boolean getVisualizaBotaoConcluir() {
		return (tabAtual == 2);
	}
	
	public boolean isDesabilitaAbas() {
		
		if(!Util.isNullOuVazio(dqc) && !Util.isNullOuVazio(dqc.getIndCienteTermoCompromisso()) && dqc.getIndCienteTermoCompromisso()) {
			return false;
		}
		
		return true;
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public DeclaracaoQueimaControlada getDqc() {
		return dqc;
	}

	public void setDqc(DeclaracaoQueimaControlada dqc) {
		this.dqc = dqc;
	}

	public int getTabAtual() {
		return tabAtual;
	}

	public void setTabAtual(int tabAtual) {
		this.tabAtual = tabAtual;
	}

	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public DeclaracaoQueimaControladaFacade getFacade() {
		return facade;
	}

	public void setFacade(DeclaracaoQueimaControladaFacade facade) {
		this.facade = facade;
	}

	public List<DeclaracaoQueimaControladaImovel> getListaDQCImovel() {
		return listaDQCImovel;
	}

	public void setListaDQCImovel(List<DeclaracaoQueimaControladaImovel> listaDQCImovel) {
		this.listaDQCImovel = listaDQCImovel;
	}

	public DeclaracaoQueimaControladaImovel getDqcImovel() {
		return dqcImovel;
	}

	public void setDqcImovel(DeclaracaoQueimaControladaImovel dqcImovel) {
		this.dqcImovel = dqcImovel;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public List<DeclaracaoQueimaControladaTecnicaUtilizada> getListaDQCTecnicaUtilizada() {
		return listaDQCTecnicaUtilizada;
	}

	public void setListaDQCTecnicaUtilizada(List<DeclaracaoQueimaControladaTecnicaUtilizada> listaDQCTecnicaUtilizada) {
		this.listaDQCTecnicaUtilizada = listaDQCTecnicaUtilizada;
	}

	public List<DeclaracaoQueimaControladaElementoIntervencao> getListaDQCElementoIntervencao() {
		return listaDQCElementoIntervencao;
	}

	public void setListaDQCElementoIntervencao(List<DeclaracaoQueimaControladaElementoIntervencao> listaDQCElementoIntervencao) {
		this.listaDQCElementoIntervencao = listaDQCElementoIntervencao;
	}

	public DeclaracaoQueimaControladaResponsavelTecnico getDqcResponsavelTecnico() {
		return dqcResponsavelTecnico;
	}

	public void setDqcResponsavelTecnico(DeclaracaoQueimaControladaResponsavelTecnico dqcResponsavelTecnico) {
		this.dqcResponsavelTecnico = dqcResponsavelTecnico;
	}

	public List<DeclaracaoQueimaControladaResponsavelTecnico> getListaDQCResponsavelTecnico() {
		return listaDQCResponsavelTecnico;
	}

	public void setListaDQCResponsavelTecnico(List<DeclaracaoQueimaControladaResponsavelTecnico> listaDQCResponsavelTecnico) {
		this.listaDQCResponsavelTecnico = listaDQCResponsavelTecnico;
	}

	public boolean isExisteOpcaoOutroSelecionada() {
		return existeOpcaoOutroSelecionada;
	}

	public void setExisteOpcaoOutroSelecionada(boolean existeOpcaoOutroSelecionada) {
		this.existeOpcaoOutroSelecionada = existeOpcaoOutroSelecionada;
	}
}