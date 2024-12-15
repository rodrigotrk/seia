package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CerhAbaDTO;
import br.gov.ba.seia.dto.CerhAbaDadoGeraisDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.CerhDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhPerguntaDadosGerais;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhProcessoSuspensao;
import br.gov.ba.seia.entity.CerhRespostaDadosGerais;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhDadosGeraisServiceFacade;
import br.gov.ba.seia.service.CerhBarragemCaracterizacaoService;
import br.gov.ba.seia.service.CerhCaptacaoCaracterizacaoService;
import br.gov.ba.seia.service.CerhIntervencaoCaracterizacaoService;
import br.gov.ba.seia.service.CerhLancamentoEfluenteCaracterizacaoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("cerhAbaDadosGeraisController")
public class CerhAbaDadosGeraisController extends CerhAbaController {

	private final Integer[] tipoIntervencao = new Integer[] {
			TipoUsoRecursoHidricoEnum.BARRAGEM.getId(),
			TipoUsoRecursoHidricoEnum.INTERVENCAO.getId()
	};

	private final Integer[] tipoCaptacao = new Integer[] {
			TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId(), 
			TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId()
	};

	private final Integer[] tipoLancamento = new Integer[] {
			TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId()
	};

	@EJB
	private CerhDadosGeraisServiceFacade cerhDadosGeraisServiceFacade;
	
	@EJB
	private CerhCaptacaoCaracterizacaoService cerhCaptacaoCaracterizacaoService;
	
	@EJB
	private CerhIntervencaoCaracterizacaoService cerhIntervencaoCaracterizacaoService;
	
	@EJB
	private CerhLancamentoEfluenteCaracterizacaoService cerhLancamentoEfluenteCaracterizacaoService;
	
	@EJB
	private CerhBarragemCaracterizacaoService cerhBarragemCaracterizacaoService;
	
	@Inject
	private CerhIncluirProcessoController dlgIncluirProcesso;
	
	@Inject
	private CerhController cerhController;

	private CerhAbaDadoGeraisDTO dto;

	private MetodoUtil metodoSelecionarEmpreendimento;
	private MetodoUtil metodoIncluirEmpreendimento;
	private MetodoUtil metodoSalvarAba;

	public CerhAbaDadosGeraisController() {}

	public void load(CerhDTO cerhDTO) throws Exception {
		inicializarCerh(cerhDTO);
		super.cerhDTO = cerhDTO;
		
		metodoSelecionarEmpreendimento = new MetodoUtil(this, "selecionarEmpreendimento", Empreendimento.class);
		metodoIncluirEmpreendimento = new MetodoUtil(this, "incluirEmpreendimento");
		metodoSalvarAba = new MetodoUtil(this, "salvarAba");
		dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado());
		dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado());
	}

	protected void inicializarCerh(CerhDTO cerhDTO) throws Exception {
		if(cerhDTO.isValido()) {
			
			super.cerhDTO = cerhDTO;
			dto = new CerhAbaDadoGeraisDTO(cerhDTO.getTelaAcaoEnum());
			super.cerhDTO.setAbaDadoGerais(dto);
			
			if(cerhDTO.isCadastrar()) {
				inicializarNovoCerh();
			}
			else {
				inicializarCerh();
			} 
		}
	}

	private void inicializarNovoCerh() throws Exception {
		dto.getCerh().setIndHistorico(false);
		carregarListaContratoConvenio();
		listarTipoUsoRecursoHidrico();
		carregarPerguntas();
		carregarRequerente();
		carregarPessoaFisicaCadastro();
		carregarEmpreendimento();
		dto.setConvenio(cerhDadosGeraisServiceFacade.isProcuradorConveniado() || cerhDadosGeraisServiceFacade.isUsuarioLogadoComContratoConvenio());
		carregarRespostas();
	}

	private void carregarEmpreendimento() throws Exception {
		if(!Util.isNull(SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO"))) {
			carregarEmpreendimentoCERH((Empreendimento) SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO"));
			SessaoUtil.removerObjetoSessao("EMPREENDIMENTO_SESSAO");
			SessaoUtil.removerObjetoSessao("URL_EMPREENDIMENTO_ORIGEM");
		} 
		else {
			if(Util.isNullOuVazio(dto.getCerh().getIdeEmpreendimento())) {
				dto.getCerh().setIdeEmpreendimento(new Empreendimento());
			}
		}
	}

	private void carregarPessoaFisicaCadastro() {
		dto.getCerh().setIdePessoaFisicaCadastro(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getIdePessoa()));
	}

	private void carregarRequerente() throws Exception {
		Pessoa requerente = cerhDTO.getRequerente();
		requerente = cerhDadosGeraisServiceFacade.carregarDadosRequerenteCerh(requerente);
		dto.getCerh().setIdePessoaRequerente(requerente);
	}

	private void inicializarCerh() throws Exception {
		carregarListaContratoConvenio();
		listarTipoUsoRecursoHidrico();
		carregarPerguntas();
		
		dto.setCerh(cerhDadosGeraisServiceFacade.carregarCerhPor(cerhDTO.getIdeCerh()));
		carregarRespostas();
		carregarListaCerhTipoUso();
		dto.setConvenio(cerhDadosGeraisServiceFacade.isProcuradorConveniado() || cerhDadosGeraisServiceFacade.isUsuarioLogadoComContratoConvenio());
	}
	

	private void sincronizarListaTipoUsoRecursoHidrico() {

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionado = sincronizarComListaAmazenada(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado());
		dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionado(listaTipoUsoRecursoHidricoIntervencaoSelecionado);

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionado = sincronizarComListaAmazenada(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado());
		dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionado(listaTipoUsoRecursoHidricoCaptacaoSelecionado);

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoLancamentoSelecionado = sincronizarComListaAmazenada(dto.getListaTipoUsoRecursoHidricoLancamentoSelecionado());
		dto.setListaTipoUsoRecursoHidricoLancamentoSelecionado(listaTipoUsoRecursoHidricoLancamentoSelecionado);
	}

	private Collection<TipoUsoRecursoHidrico> sincronizarComListaAmazenada(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoSelecionado) {
		if(!Util.isNullOuVazio(dto.getCerh().getCerhTipoUsoCollection()) && !Util.isNullOuVazio(listaTipoUsoRecursoHidricoSelecionado)) {
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoArmazenado=new ArrayList<TipoUsoRecursoHidrico>();
			for (CerhTipoUso ctu : dto.getCerh().getCerhTipoUsoCollection()) {
				listaTipoUsoRecursoHidricoArmazenado.add(ctu.getIdeTipoUsoRecursoHidrico());
			}

			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoNova=new ArrayList<TipoUsoRecursoHidrico>();
			for (TipoUsoRecursoHidrico turhSelecionado : listaTipoUsoRecursoHidricoSelecionado) {
				if(listaTipoUsoRecursoHidricoArmazenado.contains(turhSelecionado)) {
					for (TipoUsoRecursoHidrico turhArmazenado : listaTipoUsoRecursoHidricoArmazenado) {
						if(turhArmazenado.equals(turhSelecionado)) {
							listaTipoUsoRecursoHidricoNova.add(turhArmazenado);
							break;
						}
					}
				}
				else{
					listaTipoUsoRecursoHidricoNova.add(turhSelecionado);
				}
			}
			return listaTipoUsoRecursoHidricoNova;
		}

		return listaTipoUsoRecursoHidricoSelecionado;
	}

	public void onChangeResposta1() {
		if(!dto.getResposta1().getIndResposta()) {
			dto.getCerh().setIdeContratoConvenio(null);
		}
	}

	public void onChangeResposta2() {
		if(!dto.getResposta2().getIndResposta()){
			dto.getCerh().setCerhProcessoCollection(null);
			dto.getResposta3().setIndResposta(null);
		} 
		else {
			if(Util.isNull(dto.getResposta4().getIndResposta()) && Util.isNull(dto.getResposta5().getIndResposta()) && Util.isNull(dto.getResposta6().getIndResposta())){
				dto.getResposta3().setIndResposta(null);
			} 
			else {
				dto.getResposta3().setIndResposta(
						dto.getResposta4().getIndResposta()
						|| dto.getResposta5().getIndResposta()
						|| dto.getResposta6().getIndResposta());
			}
		}
	}

	public void onChangeResposta3() {
		if(!dto.getResposta3().getIndResposta()) {
			dto.getResposta4().setIndResposta(false);
			dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionado(null);
			
			dto.getResposta5().setIndResposta(false);
			dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionado(null);

			dto.getResposta6().setIndResposta(false);
			dto.setListaTipoUsoRecursoHidricoLancamentoSelecionado(null);
		} 
		else {
			dto.getResposta4().setIndResposta(null);
			dto.getResposta5().setIndResposta(null);
			dto.getResposta6().setIndResposta(null);
		}
	}

	public void onChangeResposta4() {
		if(!dto.getResposta4().getIndResposta()) {
			dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionado(null);
		}
	}

	public void onChangeResposta5() {
		if(!dto.getResposta5().getIndResposta()) {
			dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionado(null);
		}
	}

	public void onChangeResposta6() {
		if(dto.getResposta6().getIndResposta()) {
			Collection<TipoUsoRecursoHidrico> tipoUsoRecursoHidricoLancamentoSelecionado = new ArrayList<TipoUsoRecursoHidrico>();
			tipoUsoRecursoHidricoLancamentoSelecionado.add(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE));
			dto.setListaTipoUsoRecursoHidricoLancamentoSelecionado(tipoUsoRecursoHidricoLancamentoSelecionado);
		}
		else{
			dto.setListaTipoUsoRecursoHidricoLancamentoSelecionado(null);
		}
	}

	private void carregarPerguntas() throws Exception {
		dto.setListaPerguntas(cerhDadosGeraisServiceFacade.listar());
		int index = 0;
		for(CerhPerguntaDadosGerais cpdg : dto.getListaPerguntas()) {
			index++;
			MetodoUtil setPergunta = new MetodoUtil(dto,"setPergunta"+index, CerhPerguntaDadosGerais.class);
			setPergunta.executeMethod(cpdg);
		}
	}

	private void carregarRespostas() throws Exception {
		int index = 0;
		for(CerhPerguntaDadosGerais cpdg : dto.getListaPerguntas()) {
			index++;
			MetodoUtil setRergunta = new MetodoUtil(dto,"setResposta"+index, CerhRespostaDadosGerais.class);
			if(Util.isNullOuVazio(dto.getCerh().getCerhRespostaDadosGeraisCollection())) {
				CerhRespostaDadosGerais respostaVazia = new CerhRespostaDadosGerais();
				respostaVazia.setIdeCerh(dto.getCerh());
				respostaVazia.setIdeCerhPerguntaDadosGerais(cpdg);
				setRergunta.executeMethod(respostaVazia);
			}
			else {
				for(CerhRespostaDadosGerais crdg : dto.getCerh().getCerhRespostaDadosGeraisCollection()) {
					if(cpdg.equals(crdg.getIdeCerhPerguntaDadosGerais())) {
						setRergunta.executeMethod(crdg);
						break;
					}
				}
			}
		}
	}

	private void carregarListaContratoConvenio()  {
		dto.setListaContratoConvenio(cerhDadosGeraisServiceFacade.listarContratoConvenio());
	}

	private void listarTipoUsoRecursoHidrico()  {
		Collection<TipoUsoRecursoHidrico> listarTipoUsoRecursoHidrico = cerhDadosGeraisServiceFacade.listarTipoUsoRecursoHidrico();

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencao = new ArrayList<TipoUsoRecursoHidrico>();
		classificarPorTipo(listarTipoUsoRecursoHidrico, tipoIntervencao, listaTipoUsoRecursoHidricoIntervencao);
		dto.setListaTipoUsoRecursoHidricoIntervencao(listaTipoUsoRecursoHidricoIntervencao);

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacao = new ArrayList<TipoUsoRecursoHidrico>();
		classificarPorTipo(listarTipoUsoRecursoHidrico, tipoCaptacao, listaTipoUsoRecursoHidricoCaptacao);
		dto.setListaTipoUsoRecursoHidricoCaptacao(listaTipoUsoRecursoHidricoCaptacao);

	}

	private void carregarListaCerhTipoUso() {
		Collection<TipoUsoRecursoHidrico> listarTipoUsoRecursoHidricoAmazendado = null;
		if(!Util.isNullOuVazio(dto.getCerh().getCerhTipoUsoCollection())) {

			listarTipoUsoRecursoHidricoAmazendado = new ArrayList<TipoUsoRecursoHidrico>();

			for(CerhTipoUso ctu : dto.getCerh().getCerhTipoUsoCollection()) {
				listarTipoUsoRecursoHidricoAmazendado.add(ctu.getIdeTipoUsoRecursoHidrico());
			}
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionado = new ArrayList<TipoUsoRecursoHidrico>();
			classificarPorTipo(listarTipoUsoRecursoHidricoAmazendado, tipoIntervencao, listaTipoUsoRecursoHidricoIntervencaoSelecionado);
			dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionado(listaTipoUsoRecursoHidricoIntervencaoSelecionado);

			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionado = new ArrayList<TipoUsoRecursoHidrico>();
			classificarPorTipo(listarTipoUsoRecursoHidricoAmazendado, tipoCaptacao, listaTipoUsoRecursoHidricoCaptacaoSelecionado);
			dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionado(listaTipoUsoRecursoHidricoCaptacaoSelecionado);

			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoLancamentoSelecionado = new ArrayList<TipoUsoRecursoHidrico>();
			classificarPorTipo(listarTipoUsoRecursoHidricoAmazendado, tipoLancamento, listaTipoUsoRecursoHidricoLancamentoSelecionado);
			dto.setListaTipoUsoRecursoHidricoLancamentoSelecionado(listaTipoUsoRecursoHidricoLancamentoSelecionado);

		}
		
		dto.getResposta6().setIndResposta(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoLancamentoSelecionado()));
	}

	private void classificarPorTipo(Collection<TipoUsoRecursoHidrico> listarTipoUsoRecursoHidrico, Integer[] itens, Collection<TipoUsoRecursoHidrico> listaDestino) {
		for(TipoUsoRecursoHidrico turh : listarTipoUsoRecursoHidrico) {
			if(Arrays.asList(itens).contains(turh.getIdeTipoUsoRecursoHidrico())) {
				listaDestino.add(turh);
			}
		}
	}

	public void selecionarEmpreendimento(Empreendimento empreendimento) {
		try {
			carregarEmpreendimentoCERH(empreendimento);
			Html.atualizar("tabViewCerh:frmDadosGerais:pnlCaracterizacaoEmpreendimento");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarEmpreendimentoCERH(Empreendimento empreendimento) throws Exception {

		if(cerhDadosGeraisServiceFacade.isExisteCerhParaEmpreendimento(empreendimento)) {
			MensagemUtil.erro("Já existe um CERH para o empreendimento selecionado.");
		}
		else {
			if(!empreendimento.equals(dto.getCerh().getIdeEmpreendimento())) {
				dto.getCerh().setCerhProcessoCollection(new ArrayList<CerhProcesso>());
			}
			cerhDadosGeraisServiceFacade.carregarEnderecoEmpreendimento(empreendimento);
			dto.getCerh().setIdeEmpreendimento(empreendimento);
		}
	}

	public void incluirEmpreendimento() {
		ContextoUtil.getContexto().setPessoa(dto.getCerh().getIdePessoaRequerente());
		SessaoUtil.adicionarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM",PaginaEnum.CADASTRO_CERH.getUrl());
	}

	@Override
	public void validarAba() {
		boolean retorno = true;
		String msg = "";

		if(Util.isNull(dto.getCerh().getIdeEmpreendimento())) {
			retorno = false;
		}

		if(dto.isRenderedPnlContratoConvenio()) {
			if(Util.isNull(dto.getResposta1().getIndResposta())){
				retorno = false;
			}
			else if(dto.getResposta1().getIndResposta() && Util.isNull(dto.getCerh().getIdeContratoConvenio())) {
				retorno = false;
			}
		}

		if(Util.isNull(dto.getResposta2().getIndResposta())) {
			retorno = false;
		}
		else if(dto.getResposta2().getIndResposta()) {
			if(!isExisteProcessoCerh()){
				msg = "Informe pelo menos um processo.";
				retorno = false;
			}
			if(Util.isNull(dto.getResposta3().getIndResposta())) {
				retorno = false;
			}
		}
		
		Boolean isResposta3 = false;
		if(!Util.isNullOuVazio(dto.getResposta3().getIndResposta())) {
			isResposta3 = dto.getResposta3().getIndResposta();
		}
		
		if((dto.getResposta4()==null || (dto.getResposta4()!=null && dto.getResposta4().getIndResposta()==null)) && 
		  (!dto.getResposta2().getIndResposta() || isResposta3)){
			retorno = false;
		}
		
		
		else if(!Util.isNull(dto.getResposta4()) && !Util.isNull(dto.getResposta4().getIndResposta()) && dto.getResposta4().getIndResposta() && Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado())) {
			retorno = false; 
		}

		if(Util.isNull(dto.getResposta5().getIndResposta())){
			retorno = false;
		}
		else if(dto.getResposta5().getIndResposta() && Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado())) {
			retorno = false;
		}

		if(Util.isNull(dto.getResposta6().getIndResposta())){
			retorno = false;
		}
		else if(dto.getResposta6().getIndResposta() && Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoLancamentoSelecionado())) {
			retorno = false;
		}

		if(!possuiProcessoOutorga() && usoDeRecursoHidricoNaoInformado()) {
			msg = "Não foi informado nenhum uso de recurso hídrico.";
			retorno = false;
		} else if (empreendimentoFazOutrosUsos() && usoDeRecursoHidricoNaoInformado()) {
			msg = "Informe ao menos 1 dos usos além dos contemplados no(s) processo(s) informado(s).";
			retorno = false;
		}

		if(!retorno) {
			List<String> msgsValidacao = new ArrayList<String>(); 
			if(msg.isEmpty()) {
				msgsValidacao.add("Os campos obrigatórios devem ser preenchidos.");
			}
			else {
				msgsValidacao.add(msg);
			}
			throw new SeiaValidacaoRuntimeException(msgsValidacao);
		}
	}

	private boolean usoDeRecursoHidricoNaoInformado() {
		return (Util.isNull(dto.getResposta4().getIndResposta()) || !dto.getResposta4().getIndResposta())
				&& (Util.isNull(dto.getResposta5().getIndResposta()) || !dto.getResposta5().getIndResposta())
				&& (Util.isNull(dto.getResposta6().getIndResposta()) || !dto.getResposta6().getIndResposta());
	}

	private boolean empreendimentoFazOutrosUsos() {
		return !Util.isNull(dto.getResposta3().getIndResposta()) && dto.getResposta3().getIndResposta();
	}

	private boolean possuiProcessoOutorga() {
		return !Util.isNull(dto.getResposta2().getIndResposta()) && dto.getResposta2().getIndResposta();
	}

	@Override
	public void salvarAba() {
		try {
			montarObjetos();
			cerhDadosGeraisServiceFacade.verificarSeExisteCaracterizacaoParaRemocao(dto);
			cerhDadosGeraisServiceFacade.salvar(dto.getCerh());
			salvarTipoUso(dto.getCerh());
			
			super.cerhDTO.setPendente(null);
			MensagemUtil.sucesso("Salvo com sucesso.");
		} 
		catch (SeiaValidacaoRuntimeException e) {
			if(e.getMessage()!=null){
				throw new SeiaValidacaoRuntimeException(e.getMessage());
			}
			else if(e.getMessages()!=null){
				throw new SeiaValidacaoRuntimeException(e.getMessages());
			}
		}
		catch (Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void salvarTipoUso(Cerh cerh) {
		try {
			if(dto.isRenderedPnlDesejaCadastrarOutosUsos()) {
				salvarTipoUsoIntevencao(cerh);
				salvarTipoUsoCaptacao(cerh);
				salvarTipoUsoEfluente(cerh);
				
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void salvarTipoUsoEfluente(Cerh cerh) throws Exception {
		boolean temEfluente = false;
		List<CerhTipoUso> listaCerhTipoUso = new ArrayList<CerhTipoUso>();
		if(dto.getResposta6().getIndResposta()) {
			CerhTipoUso cerhTipoUso = new CerhTipoUso();
			cerhTipoUso.setIdeCerh(cerh);
			cerhTipoUso.setIdeTipoUsoRecursoHidrico(new TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE));
			cerhTipoUso.setIdeCerhRespostaDadosGerais(dto.getResposta6());
			if(!Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
				for(CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
					if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) && ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())) {
					gerenciarTipoUso(listaCerhTipoUso, cerhTipoUso);
								temEfluente = true;
					
					}
				}
			}else {
				gerenciarTipoUso(listaCerhTipoUso, cerhTipoUso);
			}
			if(!temEfluente) {
				cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
				if(Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
					cerh.setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
				}
				cerh.getCerhTipoUsoCollection().add(cerhTipoUso);
			}
			adicionarTipoUso(cerh, listaCerhTipoUso);
		}else {
			for(CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
				if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())) {
					if(!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
						for(CerhLocalizacaoGeografica cerhLoc : ctu.getCerhLocalizacaoGeograficaCollection()) {
							cerhLancamentoEfluenteCaracterizacaoService.carregarParaHistorico(cerhLoc);
							if(!Util.isNullOuVazio(cerhLoc.getIdeCerhLancamentoEfluenteCaracterizacao())) {
								cerhController.getAbaLancamentoEfluentes().excluirCaracterizacao(cerhLoc.getIdeCerhLancamentoEfluenteCaracterizacao());
							}
						}
					}
					cerhDadosGeraisServiceFacade.removerSemProcesso(ctu);
				}
			}
		}
	}

	private void salvarTipoUsoCaptacao(Cerh cerh) throws Exception {
		List<CerhTipoUso> listaCerhTipoUso = new ArrayList<CerhTipoUso>();
		CerhTipoUso tipoUsoAnterior = new CerhTipoUso();
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado())) {
			
			atualizarListaCerhTipoUsoCaptacao(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado(), cerh.getCerhTipoUsoCollection());
			
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado()) {
				CerhTipoUso cerhTipoUso = new CerhTipoUso();
				cerhTipoUso.setIdeCerh(cerh);
				cerhTipoUso.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
				cerhTipoUso.setIdeCerhRespostaDadosGerais(dto.getResposta5());
				
				if(!Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
					for(CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
						if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) 
								&& ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico())
								&& ctu.getIdeCerhTipoUso() == null) {
							cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
							listaCerhTipoUso.add(cerhTipoUso);
						}else if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) && (Util.isNullOuVazio(tipoUsoAnterior) || !tipoUsoAnterior.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico()))
								&& ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico())) {
							gerenciarTipoUso(listaCerhTipoUso, cerhTipoUso);
						}else if (!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) && ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico()) ){
							cerhTipoUso.setCerhLocalizacaoGeograficaCollection(ctu.getCerhLocalizacaoGeograficaCollection());
						}
						tipoUsoAnterior = cerhTipoUso;
					}
				}else {
					cerhDadosGeraisServiceFacade.removerSemProcesso(cerhTipoUso);
					cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
					cerh.setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
					cerh.getCerhTipoUsoCollection().add(cerhTipoUso);
					listaCerhTipoUso.add(cerhTipoUso);
					tipoUsoAnterior = cerhTipoUso;
				}
				
				if(!cerh.getCerhTipoUsoCollection().contains(cerhTipoUso) && Util.isNullOuVazio(cerhTipoUso.getIdeCerhTipoUso()) && Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
					cerhDadosGeraisServiceFacade.removerSemProcesso(cerhTipoUso);
					cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
					listaCerhTipoUso.add(cerhTipoUso);
				}
			}
			adicionarTipoUso(cerh, listaCerhTipoUso);
		}else {
			atualizarListaCerhTipoUsoCaptacao(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado(), cerh.getCerhTipoUsoCollection());
		}
	}

	private void atualizarListaCerhTipoUsoCaptacao(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionado, Collection<CerhTipoUso> listaCtuParam) {
		List<TipoUsoRecursoHidrico> turhRemovidos = new ArrayList<TipoUsoRecursoHidrico>();
		
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial())) {
			for(TipoUsoRecursoHidrico turh : dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial()) {
				if(Util.isNullOuVazio(listaTipoUsoRecursoHidricoCaptacaoSelecionado) || !listaTipoUsoRecursoHidricoCaptacaoSelecionado.contains(turh)) {
					for(CerhTipoUso ctu :turh.getCerhTipoUsoCollection()) {
						listaCtuParam.remove(ctu);
						if(!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
							for(CerhLocalizacaoGeografica cerhLoc : ctu.getCerhLocalizacaoGeograficaCollection()) {
								cerhCaptacaoCaracterizacaoService.carregarCaptacaoCaracterizacao(cerhLoc);
								/**
								 * Acessando classe subterrânea, mas deletando apartir da interface servido para Superficial e subterrânea
								 */
								if(!Util.isNullOuVazio(cerhLoc.getIdeCerhCaptacaoCaracterizacao())) {
									cerhController.getAbaCaptacaoSubterranea().excluirCaracterizacao(cerhLoc.getIdeCerhCaptacaoCaracterizacao());
								}
							}
						}
						cerhDadosGeraisServiceFacade.removerSemProcesso(ctu);
						turhRemovidos.add(turh);
					}
				}
			}
			
			dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial().removeAll(turhRemovidos);
		}
	}
	
	private void atualizarListaCerhTipoUsoIntervencao(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionado, Collection<CerhTipoUso> listaCtuParam) {
		List<TipoUsoRecursoHidrico> turhRemovidos = new ArrayList<TipoUsoRecursoHidrico>();
		
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial())) {
			for(TipoUsoRecursoHidrico turh : dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial()) {
				if(Util.isNullOuVazio(listaTipoUsoRecursoHidricoIntervencaoSelecionado) || !listaTipoUsoRecursoHidricoIntervencaoSelecionado.contains(turh)) {
					for(CerhTipoUso ctu :turh.getCerhTipoUsoCollection()) {
						listaCtuParam.remove(ctu);
						if(!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection())) {
							for(CerhLocalizacaoGeografica cerhLoc : ctu.getCerhLocalizacaoGeograficaCollection()) {
								if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId())) {
									cerhBarragemCaracterizacaoService.carregarParaHistorico(cerhLoc);	
									if(!Util.isNullOuVazio(cerhLoc.getIdeCerhBarragemCaracterizacao())) {
										cerhController.getAbaBarragem().excluirCaracterizacao(cerhLoc.getIdeCerhBarragemCaracterizacao());
									}
								}else if(ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())) {
									cerhIntervencaoCaracterizacaoService.carregarParaHistorico(cerhLoc);
									if(!Util.isNullOuVazio(cerhLoc.getIdeCerhIntervencaoCaracterizacao())) {
										cerhController.getAbaIntervencao().excluirCaracterizacao(cerhLoc.getIdeCerhIntervencaoCaracterizacao());
									}
								}
							}
						}
						cerhDadosGeraisServiceFacade.removerSemProcesso(ctu);
						turhRemovidos.add(turh);
					}
				}
			}
			
			dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial().removeAll(turhRemovidos);
		}
	}

	private void salvarTipoUsoIntevencao(Cerh cerh) throws Exception {
		List<CerhTipoUso> listaCerhTipoUso = new ArrayList<CerhTipoUso>();
		CerhTipoUso tipoUsoAnterior = new CerhTipoUso();
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado())) {
			
			atualizarListaCerhTipoUsoIntervencao(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado(), cerh.getCerhTipoUsoCollection());
			
			for (TipoUsoRecursoHidrico tipoUsoRecursoHidrico : dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado()) {
				CerhTipoUso cerhTipoUso = new CerhTipoUso();
				cerhTipoUso.setIdeCerh(cerh);
				cerhTipoUso.setIdeTipoUsoRecursoHidrico(tipoUsoRecursoHidrico);
				cerhTipoUso.setIdeCerhRespostaDadosGerais(dto.getResposta4());
		
				if(!Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
					for(CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
						if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) 
								&& ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico())
								&& ctu.getIdeCerhTipoUso() == null) {
									cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
									listaCerhTipoUso.add(cerhTipoUso);
							
						}else if(Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) && (Util.isNullOuVazio(tipoUsoAnterior) || !tipoUsoAnterior.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico()))
								&& ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico())) {
							gerenciarTipoUso(listaCerhTipoUso, cerhTipoUso);
						}else if (!Util.isNullOuVazio(ctu.getCerhLocalizacaoGeograficaCollection()) && ctu.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico()) ){
							cerhTipoUso.setCerhLocalizacaoGeograficaCollection(ctu.getCerhLocalizacaoGeograficaCollection());
						}
						tipoUsoAnterior = cerhTipoUso;
					}
				}else {
					cerhDadosGeraisServiceFacade.removerSemProcesso(cerhTipoUso);
					cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
					cerh.setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
					cerh.getCerhTipoUsoCollection().add(cerhTipoUso);
					tipoUsoAnterior = cerhTipoUso;
				}
				
				if(!cerh.getCerhTipoUsoCollection().contains(cerhTipoUso) && Util.isNullOuVazio(cerhTipoUso.getIdeCerhTipoUso()) && Util.isNullOuVazio(cerhTipoUso.getCerhLocalizacaoGeograficaCollection())) {
					cerhDadosGeraisServiceFacade.removerSemProcesso(cerhTipoUso);
					cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
					listaCerhTipoUso.add(cerhTipoUso);
				}
			
			}			
			adicionarTipoUso(cerh, listaCerhTipoUso);
		}else {
			atualizarListaCerhTipoUsoIntervencao(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado(), cerh.getCerhTipoUsoCollection());
		}

	}

	private void adicionarTipoUso(Cerh cerh, List<CerhTipoUso> listaCerhTipoUso) {
		
		if(!Util.isNullOuVazio(listaCerhTipoUso)) {
			Iterator<CerhTipoUso> ctu = cerh.getCerhTipoUsoCollection().iterator();
			CerhTipoUso cerhTipoUso;
			while(ctu.hasNext()){
				for(CerhTipoUso aux : listaCerhTipoUso) {
					if(ctu.hasNext()) {
						cerhTipoUso = (CerhTipoUso) ctu.next();
						if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(aux.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico())) {
							ctu.remove();
						}
					}
				}
			}
			
			cerh.getCerhTipoUsoCollection().addAll(listaCerhTipoUso);
			
			for(CerhTipoUso index : listaCerhTipoUso) {
				if(index.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId()) || index.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())) {
					if(Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial())) {
						dto.setListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial(new ArrayList<TipoUsoRecursoHidrico>());
					}
					if(!dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial().contains(index.getIdeTipoUsoRecursoHidrico())) {
						dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial().add(index.getIdeTipoUsoRecursoHidrico());
					}
				} else if(index.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId()) || index.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.INTERVENCAO.getId())) {
					if(Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial())) {
						dto.setListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial(new ArrayList<TipoUsoRecursoHidrico>());
					}
					dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial().add(index.getIdeTipoUsoRecursoHidrico());
				} 
			}
		}
		
		
	}

	private void gerenciarTipoUso(List<CerhTipoUso> listaCerhTipoUso, CerhTipoUso cerhTipoUso) {
		cerhDadosGeraisServiceFacade.removerSemProcesso(cerhTipoUso);
		cerhDadosGeraisServiceFacade.salvarCerhTipoUso(cerhTipoUso);
		listaCerhTipoUso.add(cerhTipoUso);
	}

	private void gerarCerhTipoUso(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoDestino, Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoOrigem, CerhRespostaDadosGerais resposta, Cerh cerh) {
		for (TipoUsoRecursoHidrico turh : listaTipoUsoRecursoHidricoOrigem) {
			if(Util.isNullOuVazio(turh.getCerhTipoUsoCollection())) {
				CerhTipoUso cerhTipoUso = null;
				turh.setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
				try {
					cerhTipoUso = cerhDadosGeraisServiceFacade.carregarCerhTipoUso(cerh, turh);
					if(Util.isNull(cerhTipoUso)){
						cerhTipoUso = new CerhTipoUso();
						cerhTipoUso.setIdeCerh(cerh);
						cerhTipoUso.setIdeTipoUsoRecursoHidrico(turh);
						cerhTipoUso.setIdeCerhRespostaDadosGerais(resposta);							
					}
					turh.getCerhTipoUsoCollection().add(cerhTipoUso);
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
		listaTipoUsoRecursoHidricoDestino.addAll(listaTipoUsoRecursoHidricoOrigem);
	}

	private void montarObjetos() {

		if(Util.isNull(dto.getCerh().getDtcCadastro())){
			dto.getCerh().setDtcCadastro(new Date());
		}

		if(Util.isNull(dto.getCerh().getIdeCerhStatus())){
			dto.getCerh().setIdeCerhStatus(new CerhStatus(CerhStatusEnum.CADASTRO_INCOMPLETO));
		}

		Collection<CerhRespostaDadosGerais> listaRespostas = new ArrayList<CerhRespostaDadosGerais>();
		listaRespostas.add(dto.getResposta1());
		listaRespostas.add(dto.getResposta2());
		listaRespostas.add(dto.getResposta3());
		listaRespostas.add(dto.getResposta4());
		listaRespostas.add(dto.getResposta5());
		listaRespostas.add(dto.getResposta6());

		dto.getCerh().setCerhRespostaDadosGeraisCollection(listaRespostas);

		sincronizarListaTipoUsoRecursoHidrico();

		Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidrico = new ArrayList<TipoUsoRecursoHidrico>();
		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado()))
			gerarCerhTipoUso(listaTipoUsoRecursoHidrico, dto.getListaTipoUsoRecursoHidricoIntervencaoSelecionado(), dto.getResposta4(), dto.getCerh());

		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado()))
			gerarCerhTipoUso(listaTipoUsoRecursoHidrico, dto.getListaTipoUsoRecursoHidricoCaptacaoSelecionado(), dto.getResposta5(), dto.getCerh());

		if(!Util.isNullOuVazio(dto.getListaTipoUsoRecursoHidricoLancamentoSelecionado()))
			gerarCerhTipoUso(listaTipoUsoRecursoHidrico, dto.getListaTipoUsoRecursoHidricoLancamentoSelecionado(), dto.getResposta6(), dto.getCerh());

		/*
		 * if(Util.isNullOuVazio(dto.getCerh().getCerhTipoUsoCollection())) {
		 * dto.getCerh().setCerhTipoUsoCollection(new ArrayList<CerhTipoUso>());
		 * for(TipoUsoRecursoHidrico turh : listaTipoUsoRecursoHidrico) {
		 * dto.getCerh().getCerhTipoUsoCollection().addAll(turh.getCerhTipoUsoCollection
		 * ()); } }
		 */

		if(isExisteProcessoCerh()) {
			for (CerhProcesso cp : dto.getCerh().getCerhProcessoCollection()) {
				if(!Util.isNullOuVazio(cp.getCerhProcessoSuspensaoCollection())) {
					for (CerhProcessoSuspensao cps : cp.getCerhProcessoSuspensaoCollection()) {
						if(cps.getIdeCerhProcessoSuspensao()<0) {
							cps.setIdeCerhProcessoSuspensao(null);
						}
					}
				}
			}
		}
	}

	/*
	 * RN - 235
	 * 
	 * O processo inserido no cadastro não poderá ser excluído se tiver dados concedidos, ou se estiver vinculado a alguma coordenada geográfica de algum tipo de uso cadastrado no CERH (o ícone de exclusão deverá ficar desabilitado). 
	 * Neste caso, o usuário deverá desassociar o processo de todas as coordenadas que ele estiver associado e retornar para a aba de Dados Gerais para excluir o processo. 
	 * 
	 */
	public boolean isPodeExcluirProcesso(CerhProcesso cerhProcesso) {
		if(super.cerhDTO.isVisualizar() || cerhProcesso.getIndDadosConcedidos()) {
			return false;
		} 
		else {
			if(!Util.isNullOuVazio(super.cerhDTO.getAbas())) {
				for (CerhAbaDTO cerhAbaDTO : super.cerhDTO.getAbas()) {
					if(!Util.isNullOuVazio(cerhAbaDTO.getListaCaracterizacaoDTO())){
						for (CerhCaracterizacaoDTO caracterizacaoDTO : cerhAbaDTO.getListaCaracterizacaoDTO()) {
							if(!Util.isNull(caracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhProcesso())
									&& caracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhProcesso().equals(cerhProcesso)){
								return false;
							}
						}
					}
				}
			}

		}
		return true;
	}

	public boolean isExisteProcessoCerh() {
		return !Util.isNullOuVazio(dto.getCerh().getCerhProcessoCollection());
	}
	
	public String getDesEmail(){
		String email = dto.getCerh().getIdePessoaRequerente().getDesEmail();
		if(Util.isNullOuVazio(email)){
			email = dto.getCerh().getIdePessoaFisicaCadastro().getPessoa().getDesEmail();
		}
		return email;
	}

	public CerhAbaDadoGeraisDTO getDto() {
		return dto;
	}

	public void setDto(CerhAbaDadoGeraisDTO dto) {
		this.dto = dto;
	}

	public MetodoUtil getMetodoSelecionarEmpreendimento() {
		return metodoSelecionarEmpreendimento;
	}

	public void setMetodoSelecionarEmpreendimento(
			MetodoUtil metodoSelecionarEmpreendimento) {
		this.metodoSelecionarEmpreendimento = metodoSelecionarEmpreendimento;
	}

	public CerhIncluirProcessoController getDlgIncluirProcesso() {
		return dlgIncluirProcesso;
	}

	public void setDlgIncluirProcesso(CerhIncluirProcessoController dlgIncluirProcesso) {
		this.dlgIncluirProcesso = dlgIncluirProcesso;
	}

	public MetodoUtil getMetodoIncluirEmpreendimento() {
		return metodoIncluirEmpreendimento;
	}

	public void setMetodoIncluirEmpreendimento(
			MetodoUtil metodoIncluirEmpreendimento) {
		this.metodoIncluirEmpreendimento = metodoIncluirEmpreendimento;
	}

	public MetodoUtil getMetodoSalvarAba() {
		return metodoSalvarAba;
	}

	public void setMetodoSalvarAba(MetodoUtil metodoSalvarAba) {
		this.metodoSalvarAba = metodoSalvarAba;
	}

	@Override
	public void armazenarHistorico() throws Exception {
		
		Cerh cerh = cerhDTO.getAbaDadoGerais().getCerh();
		cerh.setIndHistorico(true);
		
		Cerh novoCerh = cerh.clone();
		novoCerh.setIdeObjetoPai(novoCerh.getId());
		novoCerh.setIdeCerh(null);
		novoCerh.setNumCadastro(null);
		novoCerh.setIndHistorico(false);
		novoCerh.setDtcCadastro(new Date());
		novoCerh.setIdePessoaFisicaCadastro(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		novoCerh.setIdeCerhStatus(new CerhStatus(CerhStatusEnum.CADASTRO_INCOMPLETO));
		
		Cerh ideCerhPai = Util.isNull(cerh.getIdeCerhPai()) ? new Cerh(cerh.getIdeCerh()) : new Cerh(cerh.getIdeCerhPai().getIdeCerh());
		novoCerh.setIdeCerhPai(ideCerhPai);
		
		if(novoCerh.getCerhProcessoCollection()!=null) {
			novoCerh.setCerhProcessoCollection(Util.deepCloneList(novoCerh.getCerhProcessoCollection()));
			
			for(CerhProcesso cp : novoCerh.getCerhProcessoCollection()) {
				CerhProcesso cerhProcessoPai = new CerhProcesso(cp.getIdeCerhProcesso());
				cp.setIdeObjetoPai(cp.getId());
				cp.setIdeCerhProcesso(null);
				cp.setIdeCerh(novoCerh);
				cp.setCerhProcessoPai(cerhProcessoPai);
				cp.setCerhLocalizacaoGeograficaCollection(null);
				
				cp.setCerhTipoUsoCollection(Util.deepCloneList(cp.getCerhTipoUsoCollection()));
				for(CerhTipoUso ctu : cp.getCerhTipoUsoCollection()) {
					ctu.setIdeObjetoPai(ctu.getId());
					ctu.setIdeCerhTipoUso(null);
					ctu.setIdeCerhProcesso(cp);
					ctu.setIdeCerh(novoCerh);
					
					/* AS propias abas devem salvar isso!*/	
					ctu.setCerhLocalizacaoGeograficaCollection(null);
				}
				
				for(CerhProcessoSuspensao cps : cp.getCerhProcessoSuspensaoCollection()) {
					cps.setIdeObjetoPai(cps.getId());
					cps.setIdeCerhProcessoSuspensao(null);
					cps.setIdeCerhProcesso(cp);
				}
				
			}
		}
		
		if(!Util.isNullOuVazio(novoCerh.getCerhRespostaDadosGeraisCollection())) {
			for(CerhRespostaDadosGerais crdg : novoCerh.getCerhRespostaDadosGeraisCollection()) {
				crdg.setIdeObjetoPai(crdg.getId());
				crdg.setIdeCerhRespostaDadosGerais(null);
				crdg.setIdeCerh(novoCerh);
			}
		}
		
		if(!Util.isNullOuVazio(novoCerh.getCerhTipoUsoCollection())) {
			for(CerhTipoUso ctu : novoCerh.getCerhTipoUsoCollection()) {
				CerhTipoUso cerhTipoUsoPai = ctu.clone();
				ctu.setIdeObjetoPai(ctu.getId());
				ctu.setIdeCerhTipoUso(null);
				ctu.setIdeCerh(novoCerh);
				ctu.setCerhTipoUsoPai(cerhTipoUsoPai);
			
			}
		}
		
		
		cerhDadosGeraisServiceFacade.salvarComoHistorico(cerh);
		cerhDadosGeraisServiceFacade.salvar(novoCerh);
		
		for(CerhTipoUso tipoUso : novoCerh.getCerhTipoUsoCollection()) {
			cerhDadosGeraisServiceFacade.salvarCerhTipUso(tipoUso);
		
		}
		
		
		cerhDTO.setIdeCerh(novoCerh.getIdeCerh());
		cerhDTO.getAbaDadoGerais().setNovoCerh(novoCerh);
	}

}