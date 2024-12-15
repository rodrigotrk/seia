package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.TipoTravessia;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("intervencaoController")
@ViewScoped
public class IntervencaoController extends BaseDialogOutorgaController {

	private PerguntaRequerimento perguntaNR_A4_DINTERV_P4;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P41;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P42;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P421;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P4211;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P42112;
	private PerguntaRequerimento perguntaNR_A4_DINTERV_P612;

	private Collection<TipoIntervencao> tiposIntervencao;
	
	private Collection<TipoIntervencao> tiposIntervencaoFilhos;
	
	private Collection<TipoBarragem> tiposBarragem;
	
	private Collection<TipoTravessia> tiposTravessia;
	
	private Collection<TipoTravessia> subTiposTravessia;
	
	private TipoTravessia tipoTravessiaPai;
	
	private TipoTravessia tipoTravessiaFilho;
	
	private Boolean dispensa;
	
	private Boolean isEtapa2;
	
	private TipoIntervencao tpIntervPai;
	
	@Override
	public void load() {
		try {

			this.limpar();
			this.carregarPerguntas();
			this.tiposIntervencaoFilhos = null;
			tipoTravessiaPai = new TipoTravessia(-1);
			tipoTravessiaFilho = new TipoTravessia(-1);
			
			Outorga outorgaCaptacaoSuperficial = null;
			
			if(isEtapa2 != null && isEtapa2 && !Util.isNullOuVazio(outorga))
				outorgaCaptacaoSuperficial = outorga;
			
			if(Util.isNullOuVazio(outorgaCaptacaoSuperficial))
				outorgaCaptacaoSuperficial = this.outorgaService.buscarOutorgaByModalidadeAndRequerimentoAndTipoSolicitacao(ModalidadeOutorgaEnum.INTERVENCAO, super.getRequerimento(), new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId()));
			
			if (!Util.isNullOuVazio(outorgaCaptacaoSuperficial)) {
				this.outorga = outorgaCaptacaoSuperficial;
				this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(this.outorga);
			}
			
			this.carregarListas();

			this.editMode = false;
			
			super.removerImoveisCadastrados(this.abaOutorgaController.getIntervencoes());
			
			this.carregarImoveisParaOutorga();
			this.novoRequerimentoController.verificaImovelNaLista();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	@Override
	public <T> void editar(T objeto) {
		carregarDialog(objeto);
	}
	
	public <T> void visualizar(T objeto) {
		carregarDialog(objeto);
		this.editMode = false;
	}

	private <T> void carregarDialog(T objeto) {
		try {

			this.outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objeto;
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();

			this.carregarPerguntas();
			this.carregarOutorgaLocalizacaoGeograficaImovel();

			this.carregarRespostas();

			this.carregarListas();
			
			this.carregarFinalidadesUsoAgua();
			
			this.editMode = true;
			this.carregarImoveisParaOutorga();
			
			this.carregarImoveisParaOutorga();
			this.novoRequerimentoController.verificaImovelNaLista();
			
			if(!Util.isLazyInitExcepOuNull(this.outorgaLocalizacaoGeografica.getTipoIntervencao()) 
					&& !Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencaoPai())){
				
				this.valueChangeTipoIntervencaoPai(new ValueChangeEvent(new HtmlSelectOneRadio(), null, new TipoIntervencao(this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencaoPai().getIdeTipoIntervencao())));
				this.valueChangeTipoIntervencao(new ValueChangeEvent(new HtmlSelectOneRadio(), null, new TipoIntervencao(this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencao())));
			}else{
				this.valueChangeTipoIntervencaoPai(new ValueChangeEvent(new HtmlSelectOneRadio(), null, this.outorgaLocalizacaoGeografica.getTipoIntervencao()));
			}
			
			if(this.novoRequerimentoController.isImovelRural())
				this.setImoveis(this.novoRequerimentoController.listarImovel());
			
			if(!Util.isNull(outorgaLocalizacaoGeografica.getIdeTipoTravessia()) 
					&& !Util.isNull(outorgaLocalizacaoGeografica.getIdeTipoTravessia().getIdeTipoTravessiaPai())) {
				
				tipoTravessiaPai = new TipoTravessia(outorgaLocalizacaoGeografica.getIdeTipoTravessia().getIdeTipoTravessiaPai());
				tipoTravessiaFilho = new TipoTravessia(outorgaLocalizacaoGeografica.getIdeTipoTravessia().getIdeTipoTravessia());

				this.valueChangeTipoTravessia(new ValueChangeEvent(new HtmlSelectOneRadio(), null, tipoTravessiaPai.getIdeTipoTravessia()));
			} else if(!Util.isNull(outorgaLocalizacaoGeografica.getIdeTipoTravessia())) {
				tipoTravessiaPai = new TipoTravessia(outorgaLocalizacaoGeografica.getIdeTipoTravessia().getIdeTipoTravessia());
				tipoTravessiaFilho = null;
			} else {
				tipoTravessiaPai = null;
				tipoTravessiaFilho = null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListas() throws Exception {
		this.tiposIntervencao = this.outorgaService.carregaListaTipoIntervencaoByIndAtivo();
		
		if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencaoPai()))
			this.tiposIntervencaoFilhos = this.outorgaService.carregaListaTipoIntervencaoByIndAtivo();
		
		this.tiposTravessia = this.outorgaService.carregaListaTipoTravessia();
		this.tiposBarragem = this.outorgaService.carregaListaTipoBarragem();
	}

	@Override
	void limpar() {
		super.limpar();
		this.limparSubTipoTravessia();
	}
	
	public void limparSubTipoTravessia(){
		this.subTiposTravessia = new ArrayList<TipoTravessia>();
	}

	@Override
	protected void carregarPerguntas() {
		super.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();

		this.perguntaNR_A4_DINTERV_P4 = super.carregarPerguntaByCod("NR_A4_DINTERV_P4");
		this.perguntaNR_A4_DINTERV_P41 = super.carregarPerguntaByCod("NR_A4_DINTERV_P41");
		this.perguntaNR_A4_DINTERV_P42 = super.carregarPerguntaByCod("NR_A4_DINTERV_P42");
		this.perguntaNR_A4_DINTERV_P421 = super.carregarPerguntaByCod("NR_A4_DINTERV_P421");
		this.perguntaNR_A4_DINTERV_P4211 = super.carregarPerguntaByCod("NR_A4_DINTERV_P4211");
		this.perguntaNR_A4_DINTERV_P42112 = super.carregarPerguntaByCod("NR_A4_DINTERV_P42112");
		this.perguntaNR_A4_DINTERV_P612 = super.carregarPerguntaByCod("NR_A4_DINTERV_P612");

		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P4);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P41);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P42);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P421);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P4211);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P42112);
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DINTERV_P612);

	}

	public void valueChangeTipoIntervencao(ValueChangeEvent event){
		try {
			this.carregarPerguntas();
//			O limpar() abaixo instanciava uma nova Outorga e perdíamos aquela carregada no load(), por isso eram salvas duas Outorgas de mesma Modalidade para aquele Requerimento.   
//			this.limpar();
			this.limparSubTipoTravessia();
			for (PerguntaRequerimento perguntaRequerimento : super.listaPerguntasRequerimento) {
				perguntaRequerimento.setIndResposta(null);
			}
			this.carregarImoveisParaOutorga();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void valueChangeTipoIntervencaoPai(ValueChangeEvent event){
		try {
			TipoIntervencao tpInterv = ((TipoIntervencao) event.getNewValue());
			if(tpInterv.getIdeTipoIntervencao().equals(TipoIntervencaoEnum.OUTRAS_INTERVENCOES_Q_INTERFIRAM_QUANTIDADE_QUALIDADE_AGUAS.getId()) ){
				outorgaLocalizacaoGeografica.setTipoIntervencaoPai(tpInterv);
				if(!this.editMode)
					outorgaLocalizacaoGeografica.setTipoIntervencao(null);
				this.tiposIntervencaoFilhos = this.outorgaService.carregaListaTipoIntervencaoByIndAtivoByPaiExiste(outorgaLocalizacaoGeografica.getTipoIntervencaoPai().getIdeTipoIntervencao());
			}else{
				outorgaLocalizacaoGeografica.setTipoIntervencao(tpInterv);
				
				if(this.editMode) {
					outorgaLocalizacaoGeografica.setTipoIntervencaoPai(tpInterv);
				} else {
					outorgaLocalizacaoGeografica.setTipoIntervencaoPai(null);

					this.carregarPerguntas();
					for (PerguntaRequerimento perguntaRequerimento : super.listaPerguntasRequerimento) {
						perguntaRequerimento.setIndResposta(null);
					}
				}
				
				this.tiposIntervencaoFilhos = null;
				this.limparSubTipoTravessia();
				
				this.carregarImoveisParaOutorga();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void valueChangeTipoTravessia(ValueChangeEvent event){
		try {
			TipoTravessia tipoTravessia = new TipoTravessia((Integer) event.getNewValue());
			this.subTiposTravessia = this.outorgaService.carregaListaSubTipoTravessia(tipoTravessia.getIdeTipoTravessia());
			
			if(Util.isNullOuVazio(subTiposTravessia)) {
				tipoTravessiaFilho = null;
			} else if(Util.isNullOuVazio(tipoTravessiaFilho)) {
				tipoTravessiaFilho = new TipoTravessia();
				tipoTravessiaFilho.setIdeTipoTravessia(-1);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void valueChangeOutorgaDispensa(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(resposta){
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
		}else{
			this.outorgaLocalizacaoGeografica.setDtcEmissaoOficio(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
		}
	}
	
	public void valueChangePerguntaNR_A4_P12(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.perguntaNR_A4_DINTERV_P421.setIndResposta(null);
			this.perguntaNR_A4_DINTERV_P4211.setIndResposta(null);
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DINTERV_P42112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
		}
	}
	
	public void valueChangePerguntaNR_A4_DCAPSUP_P121(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DINTERV_P42112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumAreaInundadaReservatorio(null);
		}
	}
	
	public void valueChangePerguntaNR_A4_DCAPSUP_P1211(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.outorgaLocalizacaoGeografica.setIdeTipoBarragem(null);
			this.perguntaNR_A4_DINTERV_P42112.setIndResposta(null);
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
		}
	}
	
	public void valueChangePerguntaNR_A4_DCAPSUP_P12112(ValueChangeEvent event){
		Boolean resposta = (Boolean) event.getNewValue();
		if(!resposta){
			this.dispensa = null;
			this.outorgaLocalizacaoGeografica.setNumPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setDtcPublicacaoPortariaBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumOficio(null);
			this.outorgaLocalizacaoGeografica.setNumAreaIrrigada(null);
			this.outorgaLocalizacaoGeografica.setNumAreaIrrigadaCaptacao(null);
			this.outorgaLocalizacaoGeografica.setNumProcessoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumVolumeAcumulacaoBarragem(null);
			this.outorgaLocalizacaoGeografica.setNumDescargaFundo(null);
			this.outorgaLocalizacaoGeografica.setNumAreaInundadaReservatorio(null);
		}
	}
	
	@Override
	public void salvar() {
		try {
			boolean valid;
			
			if(Util.isNull(isEtapa2)) isEtapa2 = false;
			
			if(isEtapa2)
				valid = validarNaEtapa2();
			else
				valid = validar();
			
			if(valid){
				if(isEtapa2)
					this.setOutorga(this.renovarAlterarOutorgaController.getOutorga());
				
				this.salvarOutorga(ModalidadeOutorgaEnum.INTERVENCAO);
				
				this.outorgaLocalizacaoGeografica.setIdeOutorga(super.getOutorga());
				
				atribuiValoresTipoTravessia();

				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(this.outorgaLocalizacaoGeografica);
				
				this.salvarPerguntasRequerimento();
				
				this.salvarOutorgaLocalizacaoGeograficaFinalidade(this.outorgaLocalizacaoGeografica);
				
				this.salvarOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeografica);
				
				if(this.editMode)
					JsfUtil.addSuccessMessage("Intervenção em corpo hidrico atualizada com sucesso.");
				else
					JsfUtil.addSuccessMessage("Intervenção em corpo hidrico salva com sucesso.");
				
				if(isEtapa2){
					this.renovarAlterarOutorgaController.adicionarOutorgaLocalizacaoGeografica(this.outorgaLocalizacaoGeografica);
					this.renovarAlterarOutorgaController.setOutorga(this.getOutorga());
				}else
					this.abaOutorgaController.adicionarOuAtualizarIntervencao(this.outorgaLocalizacaoGeografica);
				
				RequestContext.getCurrentInstance().execute("dialogIntervencaoCorpoHidrico.hide()");
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar salvar a intervenção!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void atribuiValoresTipoTravessia() {
	
		if(!Util.isNull(tipoTravessiaPai) 
				&& !Util.isNull(tipoTravessiaFilho) 
				&& !tipoTravessiaPai.getIdeTipoTravessia().equals(-1) 
				&& !tipoTravessiaFilho.getIdeTipoTravessia().equals(-1)) {
			
			//CASO EXISTA PAI E FILHO
			outorgaLocalizacaoGeografica.setIdeTipoTravessia(new TipoTravessia());
			outorgaLocalizacaoGeografica.getIdeTipoTravessia().setIdeTipoTravessiaPai(tipoTravessiaPai.getIdeTipoTravessia());
			outorgaLocalizacaoGeografica.getIdeTipoTravessia().setIdeTipoTravessia(tipoTravessiaFilho.getIdeTipoTravessia());
			
		} else if(!Util.isNull(tipoTravessiaPai) 
				&& !tipoTravessiaPai.getIdeTipoTravessia().equals(-1)) {
			
			//CASO EXISTA SOMENTE PAI
			outorgaLocalizacaoGeografica.setIdeTipoTravessia(new TipoTravessia());
			outorgaLocalizacaoGeografica.getIdeTipoTravessia().setIdeTipoTravessia(tipoTravessiaPai.getIdeTipoTravessia());
			outorgaLocalizacaoGeografica.getIdeTipoTravessia().setIdeTipoTravessiaPai(null);
		}
	}
	
	boolean validarNaEtapa2() throws Exception {
		boolean valido = true;
		try {
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
				JsfUtil.addWarnMessage("Por favor, selecione um Imóvel.");	
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencao())){
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNomIntervencao())){
				JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
				valido = false;
			}else if(!Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNomIntervencao(), 50)){
				JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
				valido = false;
			}
			
			if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())
					|| (!super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica()) 
							&& !this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao()
								.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId()))) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 3.");
				valido = false;
			}
			if (!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencao())
					&& !this.outorgaLocalizacaoGeografica.getTipoIntervencao().isBarragem()
					&& !this.outorgaLocalizacaoGeografica.getTipoIntervencao().isAquicultura()
					&& (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeograficaFinal())
					|| !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeograficaFinal()))) {
				JsfUtil.addWarnMessage("Por favor, inclua o ponto final.");
				valido = false;
			}
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro na validação do dialog intervenção: "+e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return valido;
	}

	@Override
	boolean validar() {
		boolean valido = true;
		try {
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
				JsfUtil.addWarnMessage("Por favor, selecione um Imóvel.");	
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencao())){
				JsfUtil.addWarnMessage("Por favor, preencha o campo 1.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNomIntervencao())){
				JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
				valido = false;
			}else if(!Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNomIntervencao(), 50)){
				JsfUtil.addWarnMessage("O campo 2. só aceita 50 caracteres");
				valido = false;
			}

			if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica()) || !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Por favor, inclua o ponto/shape.");
				valido = false;
			}

			if(!Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getTipoIntervencao())){

				if (this.outorgaLocalizacaoGeografica.getTipoIntervencao().isBarragem() && this.outorgaLocalizacaoGeografica.getTipoIntervencao().isAquicultura()
						&& (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeograficaFinal()) || !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeograficaFinal()))) {
					JsfUtil.addWarnMessage("Por favor, inclua o ponto final.");
					valido = false;
				}

				if (this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencao().equals(TipoIntervencaoEnum.PONTE.getId())) {
					if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P4.getIndResposta())){
						JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO' na pergunta 4.");
						valido = false;
					}
				}
				else if (this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencao().equals(TipoIntervencaoEnum.CANALIZACAO_DESASSOREAMENTO_DRAGAGEM.getId()) || this.outorgaLocalizacaoGeografica.getTipoIntervencao().isBarragem()) {
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVolumeMaximoAcumulado())) {
						this.outorgaLocalizacaoGeografica.setNumVolumeMaximoAcumulado(null);
						JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
						valido = false;
					}
					if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio())) {
						this.outorgaLocalizacaoGeografica.setNumAreaInundadaReservatorio(null);
						JsfUtil.addWarnMessage("Por favor, preencha o campo 6.");
						valido = false;
					}
					if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeTipoBarragem())){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 7.");
						valido = false;
					}
				} else if(this.outorgaLocalizacaoGeografica.getTipoIntervencao().isConstrucaoCais()){
					if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeTipoTravessia())){
						JsfUtil.addWarnMessage("Por favor, preencha o campo 8.");
						valido = false;	
					} else {
						if(this.outorgaLocalizacaoGeografica.getIdeTipoTravessia().getIdeTipoTravessia().equals(1)){
							JsfUtil.addWarnMessage("Por favor, preencha o campo 8.1.");
							valido = false;
						}
					}
				}
				else if (this.outorgaLocalizacaoGeografica.getTipoIntervencao().getIdeTipoIntervencao().equals(TipoIntervencaoEnum.TRAVESSIA.getId())) {
					
					if((Util.isNull(tipoTravessiaPai))
							|| (!Util.isNull(tipoTravessiaPai.getIdeTipoTravessia()) && tipoTravessiaPai.getIdeTipoTravessia().equals(-1))) {
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 5.");
						valido = false;
						
					} else if(tipoTravessiaPai.getIdeTipoTravessia().equals(1)
							&& ((Util.isNull(tipoTravessiaFilho)) || (!Util.isNull(tipoTravessiaFilho.getIdeTipoTravessia()) && tipoTravessiaFilho.getIdeTipoTravessia().equals(-1)))) {
						
						JsfUtil.addWarnMessage("Por favor, preencha o campo 5.1.");
						valido = false;
					}
				}
			}
			
			if(this.outorgaLocalizacaoGeografica.getTipoIntervencao().isAquicultura()){
				if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P41.getIndResposta())){
					JsfUtil.addWarnMessage("Você precisa responder a questão 3.1");
					valido = false;
				}
				if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P42.getIndResposta())){
					JsfUtil.addWarnMessage("Você precisa responder a questão 3.2");
					valido = false;
				} else if(this.perguntaNR_A4_DINTERV_P42.getIndResposta() ){
					if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P421.getIndResposta())){
						JsfUtil.addWarnMessage("Você precisa responder a questão 3.2.1");
						valido = false;
					} else if(this.perguntaNR_A4_DINTERV_P421.getIndResposta() ){
						if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P4211.getIndResposta())){
							JsfUtil.addWarnMessage("Você precisa responder a questão 3.2.1.1");
							valido = false;
						} else if(this.perguntaNR_A4_DINTERV_P4211.getIndResposta() ){
							if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeTipoBarragem())){
								JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.1");
								valido = false;
							}						
							if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P42112.getIndResposta())){
								JsfUtil.addWarnMessage("Você precisa responder a questão 3.2.1.1.2");
								valido = false;
							} else if(this.perguntaNR_A4_DINTERV_P42112.getIndResposta() ){
								if(Util.isNullOuVazio(this.dispensa)){
									JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1");
									valido = false;
								} else {
									if((!this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumPortariaBarragem()) || this.outorgaLocalizacaoGeografica.getNumPortariaBarragem().trim().equals("0") || 
											!Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumPortariaBarragem(), 50))){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.1");
										valido = false;
									}
									if((!this.dispensa) && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcPublicacaoPortariaBarragem())){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.2");
										valido = false;
									}
									if((this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumOficio())  || this.outorgaLocalizacaoGeografica.getNumOficio().trim().equals("0")
											|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumOficio(), 50))){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.1");
										valido = false;
									}
									if((this.dispensa) && Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getDtcEmissaoOficio())){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.2");
										valido = false;
									}
									if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumProcessoBarragem()) ||  this.outorgaLocalizacaoGeografica.getNumProcessoBarragem().trim().equals("0")
											|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumProcessoBarragem(), 50))){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.3.");
										valido = false;
									}else{
										
									}
									if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem()) || this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem().compareTo(BigDecimal.ZERO) == 0)
											|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumVolumeAcumulacaoBarragem().toString(), 11)){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.4");
										valido = false;
									}
									if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio()) || this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio().compareTo(BigDecimal.ZERO)==0)
											|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumAreaInundadaReservatorio().toString(), 11)){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.5");
										valido = false;
									}
									if(!Util.isNullOuVazio(this.dispensa) && (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumDescargaFundo()) || this.outorgaLocalizacaoGeografica.getNumDescargaFundo().compareTo(BigDecimal.ZERO)==0)
											|| !Util.validaTamanhoString(this.outorgaLocalizacaoGeografica.getNumDescargaFundo().toString(), 11)){
										JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.1.2.1.6");
										valido = false;
									}
																		
									if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua())){
										JsfUtil.addWarnMessage("Por favor, selecione uma ou mais finalidade para o campo 6.1.1.2.1.7.");
										valido = false;
									}
									
								}
							}
						}
						
						if(Util.isNullOuVazio(this.perguntaNR_A4_DINTERV_P612.getIndResposta())){
							JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.2");
							valido = false;
						} else if(this.perguntaNR_A4_DINTERV_P612.getIndResposta() ){
							if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumPortariaLicencaBarragem())){
								JsfUtil.addWarnMessage("Você precisa responder a questão 6.1.2.1");
								valido = false;
							}	
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro na validação do dialog intervenção: "+e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return valido;
	}

	public Collection<TipoIntervencao> getTiposIntervencao() {
		return tiposIntervencao;
	}

	public void setTiposIntervencao(Collection<TipoIntervencao> tiposIntervencao) {
		this.tiposIntervencao = tiposIntervencao;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P4() {
		return perguntaNR_A4_DINTERV_P4;
	}

	public void setPerguntaNR_A4_DINTERV_P4(PerguntaRequerimento perguntaNR_A4_DINTERV_P4) {
		this.perguntaNR_A4_DINTERV_P4 = perguntaNR_A4_DINTERV_P4;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P41() {
		return perguntaNR_A4_DINTERV_P41;
	}

	public void setPerguntaNR_A4_DINTERV_P41(PerguntaRequerimento perguntaNR_A4_DINTERV_P41) {
		this.perguntaNR_A4_DINTERV_P41 = perguntaNR_A4_DINTERV_P41;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P42() {
		return perguntaNR_A4_DINTERV_P42;
	}

	public void setPerguntaNR_A4_DINTERV_P42(PerguntaRequerimento perguntaNR_A4_DINTERV_P42) {
		this.perguntaNR_A4_DINTERV_P42 = perguntaNR_A4_DINTERV_P42;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P421() {
		return perguntaNR_A4_DINTERV_P421;
	}

	public void setPerguntaNR_A4_DINTERV_P421(PerguntaRequerimento perguntaNR_A4_DINTERV_P421) {
		this.perguntaNR_A4_DINTERV_P421 = perguntaNR_A4_DINTERV_P421;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P4211() {
		return perguntaNR_A4_DINTERV_P4211;
	}

	public void setPerguntaNR_A4_DINTERV_P4211(PerguntaRequerimento perguntaNR_A4_DINTERV_P4211) {
		this.perguntaNR_A4_DINTERV_P4211 = perguntaNR_A4_DINTERV_P4211;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P42112() {
		return perguntaNR_A4_DINTERV_P42112;
	}

	public void setPerguntaNR_A4_DINTERV_P42112(PerguntaRequerimento perguntaNR_A4_DINTERV_P42112) {
		this.perguntaNR_A4_DINTERV_P42112 = perguntaNR_A4_DINTERV_P42112;
	}

	public PerguntaRequerimento getPerguntaNR_A4_DINTERV_P612() {
		return perguntaNR_A4_DINTERV_P612;
	}

	public void setPerguntaNR_A4_DINTERV_P612(PerguntaRequerimento perguntaNR_A4_DINTERV_P612) {
		this.perguntaNR_A4_DINTERV_P612 = perguntaNR_A4_DINTERV_P612;
	}

	public AbaOutorgaController getAbaOutorgaController() {
		return abaOutorgaController;
	}

	public void setAbaOutorgaController(AbaOutorgaController abaOutorgaController) {
		this.abaOutorgaController = abaOutorgaController;
	}

	public Collection<TipoBarragem> getTiposBarragem() {
		return tiposBarragem;
	}

	public void setTiposBarragem(Collection<TipoBarragem> tiposBarragem) {
		this.tiposBarragem = tiposBarragem;
	}

	public Collection<TipoTravessia> getTiposTravessia() {
		return tiposTravessia;
	}

	public void setTiposTravessia(Collection<TipoTravessia> tiposTravessia) {
		this.tiposTravessia = tiposTravessia;
	}

	public Boolean getDispensa() {
		return dispensa;
	}

	public void setDispensa(Boolean dispensa) {
		this.dispensa = dispensa;
	}

	public Collection<TipoTravessia> getSubTiposTravessia() {
		return subTiposTravessia;
	}

	public void setSubTiposTravessia(Collection<TipoTravessia> subTiposTravessia) {
		this.subTiposTravessia = subTiposTravessia;
	}

	/**
	 * @return the tpIntervPai
	 */
	public TipoIntervencao getTpIntervPai() {
		return tpIntervPai;
	}

	/**
	 * @param tpIntervPai the tpIntervPai to set
	 */
	public void setTpIntervPai(TipoIntervencao tpIntervPai) {
		this.tpIntervPai = tpIntervPai;
	}

	/**
	 * @return the tiposIntervencaoFilhos
	 */
	public Collection<TipoIntervencao> getTiposIntervencaoFilhos() {
		return tiposIntervencaoFilhos;
	}

	/**
	 * @param tiposIntervencaoFilhos the tiposIntervencaoFilhos to set
	 */
	public void setTiposIntervencaoFilhos(Collection<TipoIntervencao> tiposIntervencaoFilhos) {
		this.tiposIntervencaoFilhos = tiposIntervencaoFilhos;
	}

	/**
	 * @return the isEtapa2
	 */
	public Boolean getIsEtapa2() {
		return isEtapa2;
	}

	/**
	 * @param isEtapa2 the isEtapa2 to set
	 */
	public void setIsEtapa2(Boolean isEtapa2) {
		this.isEtapa2 = isEtapa2;
	}

	
	/**
	 * @return the tipoTravessiaPai
	 */
	public TipoTravessia getTipoTravessiaPai() {
	
		return tipoTravessiaPai;
	}

	
	/**
	 * @param tipoTravessiaPai the tipoTravessiaPai to set
	 */
	public void setTipoTravessiaPai(TipoTravessia tipoTravessiaPai) {
	
		this.tipoTravessiaPai = tipoTravessiaPai;
	}

	
	/**
	 * @return the tipoTravessiaFilho
	 */
	public TipoTravessia getTipoTravessiaFilho() {
	
		return tipoTravessiaFilho;
	}

	
	/**
	 * @param tipoTravessiaFilho the tipoTravessiaFilho to set
	 */
	public void setTipoTravessiaFilho(TipoTravessia tipoTravessiaFilho) {
	
		this.tipoTravessiaFilho = tipoTravessiaFilho;
	}
	
}
