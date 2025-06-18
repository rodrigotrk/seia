package br.gov.ba.seia.dto;

import java.util.Collection;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhPerguntaDadosGerais;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhRespostaDadosGerais;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.Util;

public class CerhAbaDadoGeraisDTO {
	
	/** Contrato/Convenio */
	private CerhPerguntaDadosGerais pergunta1;
	/** Processo de outorga conluído ou em tramite no INEMA */
	private CerhPerguntaDadosGerais pergunta2;
	/** Deseja cadastrar outros usos */
	private CerhPerguntaDadosGerais pergunta3;
	/** Faz intervenção */
	private CerhPerguntaDadosGerais pergunta4;
	/** Faz captação */
	private CerhPerguntaDadosGerais pergunta5;
	/** Faz lançamento de efluentes */
	private CerhPerguntaDadosGerais pergunta6;

	/** Contrato/Convenio */
	private CerhRespostaDadosGerais resposta1;
	/** Processo de outorga conluído ou em tramite no INEMA */
	private CerhRespostaDadosGerais resposta2;
	/** Deseja cadastrar outros usos */
	private CerhRespostaDadosGerais resposta3;
	/** Faz intervenção */
	private CerhRespostaDadosGerais resposta4;
	/** Faz captação */
	private CerhRespostaDadosGerais resposta5;
	/** Faz lançamento de efluentes */
	private CerhRespostaDadosGerais resposta6;
	
	private boolean convenio;
	private TelaAcaoEnum telaAcaoEnum;
	private Collection<ContratoConvenio> listaContratoConvenio; 
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacao; 
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencao; 
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionado;
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial;
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionado; 
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial; 
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoLancamentoSelecionado; 
	private Collection<CerhPerguntaDadosGerais> listaPerguntas; 
	
	
	private Cerh cerh;
	private Cerh novoCerh;

	public CerhAbaDadoGeraisDTO(TelaAcaoEnum telaAcaoEnum) {
		this.cerh = new Cerh();
		this.telaAcaoEnum = telaAcaoEnum;
	}
	
	public boolean isDisabledEmpreendimento() {
		if(telaAcaoEnum.isVisualizar() || telaAcaoEnum.isEditar() || !Util.isNull(cerh.getIdeCerh())) {
			return true;
		}
		return false;
	}
	
	public boolean isDisabledRespostaCerhTipoUso() {
		if(telaAcaoEnum.isVisualizar() || (Boolean.TRUE.equals(resposta2.getIndResposta()) && Boolean.FALSE.equals(resposta3.getIndResposta()))) {
			return true;
		}
		return false;
	}
	
	public boolean isDisabledResposta3() {
		if(telaAcaoEnum.isVisualizar() || new Empreendimento().equals(getCerh().getIdeEmpreendimento())) {
			return false;
		}
		return true;
	}
	
	public boolean isRenderedSeEmpreendimentoPreenchindo() {
		if(new Empreendimento().equals(getCerh().getIdeEmpreendimento())) {
			return false;
		}
		return true;
	}
	
	public boolean isRenderedPnlContratoConvenio() {
		if(!Util.isNull(cerh.getIdeContratoConvenio()) || convenio) {
			return true;
		}
		return false;
	}
	
	public boolean isRenderedPnlSelecaoContratoConvenio() {
		if(Boolean.TRUE.equals(resposta1.getIndResposta())) {
			return true;
		}
		return false;
	}
	
	public boolean isRenderedPnlListaProcessos() {
		if(Boolean.TRUE.equals(resposta2.getIndResposta())) {
			return true;
		}
		return false;
	}
	
	public boolean isRenderedPnlDesejaCadastrarOutosUsos() {
		if(Boolean.TRUE.equals(resposta3.getIndResposta()) ||  Boolean.FALSE.equals(resposta2.getIndResposta())) {
			return true;
		}
		return false;
	}
	
	public boolean isRenderedPnlIntervencao() {
		if(Boolean.TRUE.equals(resposta4.getIndResposta())) {
			return true;
		}
		return false;
	}
	
	public boolean isRenderedPnlCaptacao() {
		if(Boolean.TRUE.equals(resposta5.getIndResposta())) {
			return true;
		}
		return false;
	}
	
	public boolean existeIntervencao(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		return isRenderedPnlIntervencao() 
				&& !Util.isNullOuVazio(getListaTipoUsoRecursoHidricoIntervencaoSelecionado())
				&& getListaTipoUsoRecursoHidricoIntervencaoSelecionado().contains(new TipoUsoRecursoHidrico(tipoUsoRecursoHidricoEnum));
	}
	
	public boolean existeCaptacao(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		return isRenderedPnlCaptacao() 
				&& !Util.isNullOuVazio(getListaTipoUsoRecursoHidricoCaptacaoSelecionado())
				&& getListaTipoUsoRecursoHidricoCaptacaoSelecionado().contains(new TipoUsoRecursoHidrico(tipoUsoRecursoHidricoEnum));
	}
	
	public boolean existeProcessoSeiaComTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		if(!Util.isNullOuVazio(cerh.getCerhProcessoCollection())) {
			for (CerhProcesso cerhProcesso : cerh.getCerhProcessoCollection()) {
				if(!Util.isNull(obterCerhTipoUsoDoProcesso(cerhProcesso, tipoUsoRecursoHidricoEnum))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean processoComTipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum,CerhProcesso cerhProcessoParam) {
		if(!Util.isNullOuVazio(cerhProcessoParam)) {
				if(!Util.isNull(obterCerhTipoUsoDoProcesso(cerhProcessoParam, tipoUsoRecursoHidricoEnum))) {
					return true;
				}
		}else if(!Util.isNullOuVazio(cerh.getCerhProcessoCollection())) {
			for (CerhProcesso cerhProcesso : cerh.getCerhProcessoCollection()) {
				if(!Util.isNull(obterCerhTipoUsoDoProcesso(cerhProcesso, tipoUsoRecursoHidricoEnum))) {
					return true;
				}
			}
		}
		return false;
	}

	public CerhTipoUso obterCerhTipoUso(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		for (CerhTipoUso cerhTipoUso : cerh.getCerhTipoUsoCollection()) {
			if(!Util.isNull(cerhTipoUso.getIdeCerhRespostaDadosGerais()) && cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidricoEnum.getId())) {
				return cerhTipoUso;
			}
		}
		return null;
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/04/2017
	 * @return
	 */
	public CerhTipoUso obterCerhTipoUsoDoProcesso(CerhProcesso cerhProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
			if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(tipoUsoRecursoHidricoEnum.getId())) {
				return cerhTipoUso;
			}
		}
		return null;
	}	
	
	
	public Cerh getCerh() {
		return cerh;
	}

	public void setCerh(Cerh cerh) {
		this.cerh = cerh;
	}
	
	/** Contrato/Convenio */
	public CerhPerguntaDadosGerais getPergunta1() {
		return pergunta1;
	}

	public void setPergunta1(CerhPerguntaDadosGerais pergunta1) {
		this.pergunta1 = pergunta1;
	}
	
	/** Processo de outorga conluído ou em tramite no INEMA */
	public CerhPerguntaDadosGerais getPergunta2() {
		return pergunta2;
	}

	public void setPergunta2(CerhPerguntaDadosGerais pergunta2) {
		this.pergunta2 = pergunta2;
	}
	
	/** Deseja cadastrar outros usos */
	public CerhPerguntaDadosGerais getPergunta3() {
		return pergunta3;
	}

	public void setPergunta3(CerhPerguntaDadosGerais pergunta3) {
		this.pergunta3 = pergunta3;
	}
	
	/** Faz intervenção */
	public CerhPerguntaDadosGerais getPergunta4() {
		return pergunta4;
	}

	public void setPergunta4(CerhPerguntaDadosGerais pergunta4) {
		this.pergunta4 = pergunta4;
	}
	
	/** Faz captação */
	public CerhPerguntaDadosGerais getPergunta5() {
		return pergunta5;
	}

	public void setPergunta5(CerhPerguntaDadosGerais pergunta5) {
		this.pergunta5 = pergunta5;
	}
	
	/** Faz lançamento de efluentes */
	public CerhPerguntaDadosGerais getPergunta6() {
		return pergunta6;
	}

	public void setPergunta6(CerhPerguntaDadosGerais pergunta6) {
		this.pergunta6 = pergunta6;
	}

	public CerhRespostaDadosGerais getResposta1() {
		return resposta1;
	}

	public void setResposta1(CerhRespostaDadosGerais resposta1) {
		this.resposta1 = resposta1;
	}

	public CerhRespostaDadosGerais getResposta2() {
		return resposta2;
	}

	public void setResposta2(CerhRespostaDadosGerais resposta2) {
		this.resposta2 = resposta2;
	}

	public CerhRespostaDadosGerais getResposta3() {
		return resposta3;
	}

	public void setResposta3(CerhRespostaDadosGerais resposta3) {
		this.resposta3 = resposta3;
	}

	public CerhRespostaDadosGerais getResposta4() {
		return resposta4;
	}

	public void setResposta4(CerhRespostaDadosGerais resposta4) {
		this.resposta4 = resposta4;
	}

	public CerhRespostaDadosGerais getResposta5() {
		return resposta5;
	}

	public void setResposta5(CerhRespostaDadosGerais resposta5) {
		this.resposta5 = resposta5;
	}

	public CerhRespostaDadosGerais getResposta6() {
		return resposta6;
	}

	public void setResposta6(CerhRespostaDadosGerais resposta6) {
		this.resposta6 = resposta6;
	}

	public Collection<CerhPerguntaDadosGerais> getListaPerguntas() {
		return listaPerguntas;
	}

	public void setListaPerguntas(Collection<CerhPerguntaDadosGerais> listaPerguntas) {
		this.listaPerguntas = listaPerguntas;
	}

	public Collection<ContratoConvenio> getListaContratoConvenio() {
		return listaContratoConvenio;
	}

	public void setListaContratoConvenio(Collection<ContratoConvenio> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoCaptacao() {
		return listaTipoUsoRecursoHidricoCaptacao;
	}

	public void setListaTipoUsoRecursoHidricoCaptacao(
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacao) {
		this.listaTipoUsoRecursoHidricoCaptacao = listaTipoUsoRecursoHidricoCaptacao;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoIntervencao() {
		return listaTipoUsoRecursoHidricoIntervencao;
	}

	public void setListaTipoUsoRecursoHidricoIntervencao(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencao) {
		this.listaTipoUsoRecursoHidricoIntervencao = listaTipoUsoRecursoHidricoIntervencao;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoCaptacaoSelecionado() {
		return listaTipoUsoRecursoHidricoCaptacaoSelecionado;
	}

	public void setListaTipoUsoRecursoHidricoCaptacaoSelecionado(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionado) {
		this.listaTipoUsoRecursoHidricoCaptacaoSelecionado = listaTipoUsoRecursoHidricoCaptacaoSelecionado;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoIntervencaoSelecionado() {
		return listaTipoUsoRecursoHidricoIntervencaoSelecionado;
	}

	public void setListaTipoUsoRecursoHidricoIntervencaoSelecionado(
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionado) {
		this.listaTipoUsoRecursoHidricoIntervencaoSelecionado = listaTipoUsoRecursoHidricoIntervencaoSelecionado;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoLancamentoSelecionado() {
		return listaTipoUsoRecursoHidricoLancamentoSelecionado;
	}

	public void setListaTipoUsoRecursoHidricoLancamentoSelecionado(
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoLancamentoSelecionado) {
		this.listaTipoUsoRecursoHidricoLancamentoSelecionado = listaTipoUsoRecursoHidricoLancamentoSelecionado;
	}

	public boolean isConvenio() {
		return convenio;
	}

	public void setConvenio(boolean convenio) {
		this.convenio = convenio;
	}

	public Cerh getNovoCerh() {
		return novoCerh;
	}

	public void setNovoCerh(Cerh novoCerh) {
		this.novoCerh = novoCerh;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial() {
		return listaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial;
	}

	public void setListaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial(
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial) {
		this.listaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial = listaTipoUsoRecursoHidricoCaptacaoSelecionadoInicial;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial() {
		return listaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial;
	}

	public void setListaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial(
			Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial) {
		this.listaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial = listaTipoUsoRecursoHidricoIntervencaoSelecionadoInicial;
	}
	
}
