package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaTipoReceptor;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TipoReceptor;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoReceptorEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("lancamentoEfluentesController")
@ViewScoped
public class LancamentoEfluentesController  extends BaseDialogOutorgaController{
	
	private PerguntaRequerimento perguntaNR_A4_DCHID_P3;
	private boolean isExbirGridLocGeografica = false;
		
	@Override
	public void load() {
		try {
			
			this.limpar();
			this.carregarPerguntas();
			
			Outorga outorgaCaptacaoSuperficial = this.outorgaService.buscarOutorgaByModalidadeAndRequerimento(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES,super.getRequerimento());
			if(!Util.isNullOuVazio(outorgaCaptacaoSuperficial)){
				this.outorga = outorgaCaptacaoSuperficial;
				this.outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(this.outorga);
			}
			
			super.removerImoveisCadastrados(this.abaOutorgaController.getLancamentosEfluentes());
			
			this.editMode = false;
			
			this.carregarImoveisParaOutorga();
			
			this.novoRequerimentoController.verificaImovelNaLista();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public <T> void editar(T objeto) {
		try {
			
			this.outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica)objeto;
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();
			
			this.carregarPerguntas();
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			
			this.carregarRespostas(outorgaLocalizacaoGeografica);
			
			this.carregarFinalidadesUsoAgua();
			
			this.editMode = true;
			
			this.carregarImoveisParaOutorga();
			
			this.novoRequerimentoController.verificaImovelNaLista();
			if(this.novoRequerimentoController.isImovelRural())
				this.setImoveis(this.novoRequerimentoController.listarImovel());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void visualizar(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		try {
			
			this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
			this.outorga = this.outorgaLocalizacaoGeografica.getIdeOutorga();
			
			this.carregarPerguntas();
			this.carregarOutorgaLocalizacaoGeograficaImovel();
			
			this.carregarRespostas(outorgaLocalizacaoGeografica);
			
			this.carregarFinalidadesUsoAgua();
			
			this.editMode = false;
			
			this.carregarImoveisParaOutorga();
			
			this.novoRequerimentoController.verificaImovelNaLista();
			if(this.novoRequerimentoController.isImovelRural())
				this.setImoveis(this.novoRequerimentoController.listarImovel());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarPerguntas()  {
		super.listaPerguntasRequerimento = new ArrayList<PerguntaRequerimento>();
		
		this.perguntaNR_A4_DCHID_P3 = super.carregarPerguntaByCod("NR_A4_DCHID_P3");
		
		super.listaPerguntasRequerimento.add(this.perguntaNR_A4_DCHID_P3);
		
	}

	@Override
	public void salvar() {
		try {
			if(this.validar()){
				this.salvarOutorga(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES);

				this.salvarOutorgaTipoReceptor();
				
				this.outorgaLocalizacaoGeograficaService.salvarAtualizar(this.outorgaLocalizacaoGeografica);
				
				this.salvarPerguntasRequerimento();
				
				this.salvarOutorgaLocalizacaoGeograficaFinalidade(this.outorgaLocalizacaoGeografica);
				
				this.salvarOutorgaLocalizacaoGeograficaImovel(this.outorgaLocalizacaoGeografica);
				
				if(this.editMode)
					JsfUtil.addSuccessMessage("Lançamento de efluentes atualizado com sucesso.");
				else
					JsfUtil.addSuccessMessage("Lançamento de efluentes salvo com sucesso.");
				
				this.abaOutorgaController.adicionarOuAtualizarLancamentoDeEfluentes(this.outorgaLocalizacaoGeografica);
				
				RequestContext.getCurrentInstance().execute("dialogLancamentoEfluentes.hide()");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarOutorgaTipoReceptor()  {
		OutorgaTipoReceptor outorgaTipoReceptor = new OutorgaTipoReceptor(outorga,new TipoReceptor(TipoReceptorEnum.MANANCIAL_CORPO_HIDRICO.getId()));
		this.outorgaService.salvarAtualizarOutorgaTipoReceptor(outorgaTipoReceptor);
	}
	
	@Override
	boolean validar() {
		boolean valido = true;
		try {		
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaImovel())){
				JsfUtil.addWarnMessage("Favor selecionar o imovel.");
				valido = false;
			}
			if (Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica()) || !super.isTheGeomPersistido(this.outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
				JsfUtil.addWarnMessage("Favor incluir os pontos.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getNumVazao()) || (this.outorgaLocalizacaoGeografica.getNumVazao().compareTo(BigDecimal.ZERO) == 0)){
				JsfUtil.addWarnMessage("Preencha o campo 1.1.");
				valido = false;
			}
			if(Util.isNullOuVazio(this.perguntaNR_A4_DCHID_P3.getIndResposta())){
				JsfUtil.addWarnMessage("Selecione uma opção na questão 1.2.");
				valido = false;
			}			
			if(Util.isNullOuVazio(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua())){
				JsfUtil.addWarnMessage("Por favor, selecione uma ou mais finalidade para o campo 1.3.");
				valido = false;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return valido;
	}
	

	public PerguntaRequerimento getPerguntaNR_A4_DCHID_P3() {
		return perguntaNR_A4_DCHID_P3;
	}

	public void setPerguntaNR_A4_DCHID_P3(PerguntaRequerimento perguntaNR_A4_DCHID_P3) {
		this.perguntaNR_A4_DCHID_P3 = perguntaNR_A4_DCHID_P3;
	}

	public boolean getExbirGridLocGeografica() {
		return isExbirGridLocGeografica;
	}

	public void setExbirGridLocGeografica(boolean isExbirGridLocGeografica) {
		this.isExbirGridLocGeografica = isExbirGridLocGeografica;
	}
	
	

}
