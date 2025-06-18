package br.gov.ba.seia.dto;

import java.util.Collection;
import java.util.Date;

import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhProcessoSuspensao;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;
import br.gov.ba.seia.entity.CerhTipoAtoDispensa;
import br.gov.ba.seia.entity.CerhTipoAutorizacaoOutorgado;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.CerhSituacaoRegularizacaoEnum;
import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.util.Util;


public class CerhIncluirProcessoDTO {

	private boolean novoRegistro;
	private boolean removerEdicaoCerhSituacaoRegularizacao;
	private boolean existeCerhTipoUsoSemProcesso;
	private boolean validarNumProcesso;
	private boolean processoSemTipologia;
	private boolean possuiCerhTipoUsoEditavel;
	private TelaAcaoEnum telaAcaoEnum;
	private Processo processo;
	private CerhProcesso cerhProcesso;
	private Date dtFimAutorizacao;
	private CerhTipoUso cerhTipoUsoSelecionado;
	private Collection<CerhTipoUso> listaCerhTipoUso;
	private CerhProcessoSuspensao cerhProcessoSuspensaoSelecionado;
	private Collection<CerhSituacaoRegularizacao> listaSituacaoRegularizacao;
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidrico;
	private Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoSelecionado;
	private Collection<CerhTipoAtoDispensa> listaCerhTipoAtoDispensa;
	private Collection<CerhTipoAutorizacaoOutorgado> listaCerhTipoAutorizacaoOutorgado;
	private Collection<Tipologia> listaTipologiaDadosConcedidos;
	private Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica;
	private boolean exibirLabelTipoUso;
	
	public CerhIncluirProcessoDTO(TelaAcaoEnum telaAcaoEnum) {
		this.telaAcaoEnum = telaAcaoEnum;
	}
	
	public boolean isVisualizar() {
		return telaAcaoEnum.isVisualizar();
	}
	
	public boolean isEditar() {
		return telaAcaoEnum.isEditar();
	}
	
	public boolean isDisabledNumProcesso() {
		return telaAcaoEnum.isVisualizar() || telaAcaoEnum.isEditar();
	}
	
	public boolean isDisabledCaptacao() {
		return telaAcaoEnum.isVisualizar();
	}
	
	public boolean isDisabledBotaoConsultar() {
		return telaAcaoEnum.isVisualizar() || telaAcaoEnum.isEditar();
	}
	
	public boolean isDisabledCerhSituacaoRegularizacao() {
		return telaAcaoEnum.isVisualizar() || removerEdicaoCerhSituacaoRegularizacao;
	}
	
	public boolean isRenderedPnlProcessoSEIA() {
		return Util.isNull(processo) == false;
	}
	
	public boolean isRenderedPergunta1() {
		return existeCerhTipoUsoSemProcesso;
	}
	
	public boolean isRenderedPnlListaCoordenada() {
		return existeCerhTipoUsoSemProcesso && Boolean.TRUE.equals(cerhProcesso.getIndOutorgaReferentePontoCadastradoCerh());
	}
	
	public boolean isRenderedPnlTipoUso() {
		return Util.isNull(processo) && isNumProcessoPreenchido();
	}
	
	public boolean isRenderedPnlSituacaoRegulacao() {
		return isNumProcessoPreenchido();
	}
	
	public boolean isRenderedPnlTipoUsoProcessoSemTipologia() {
		return processoSemTipologia;
	}
	
	private boolean isNumProcessoPreenchido() {
		return !Util.isNullOuVazio(cerhProcesso.getNumProcesso());
	}
	
	public boolean isRenderedPnlPortaria() {
		return isRenderedPnlCancelado();
	}
	
	public boolean isRenderedPnlCancelado() {
		return new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.CANCELADO).equals(cerhProcesso.getIdeCerhSituacaoRegularizacao());
	}
	
	public boolean isRenderedPnlOutorgado() {
		return new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.OUTORGADO).equals(cerhProcesso.getIdeCerhSituacaoRegularizacao());
	}
	
	public boolean isRenderedPnlDispensa() {
		return new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.DISPENSADO).equals(cerhProcesso.getIdeCerhSituacaoRegularizacao());
	}
	
	public boolean isRenderedPnlInexigibilidade() {
		return new CerhSituacaoRegularizacao(CerhSituacaoRegularizacaoEnum.INEXIGIVEL).equals(cerhProcesso.getIdeCerhSituacaoRegularizacao());
	}
	
	public boolean isRenderedPossuiCarta() {
		return !Util.isNull(getCerhProcesso()) && !Util.isNull(getCerhProcesso().getIndPossuiCartaInexigibilidade()) && getCerhProcesso().getIndPossuiCartaInexigibilidade();   
	}
	
	public Collection<CerhSituacaoRegularizacao> getListaSituacaoRegularizacao() {
		return listaSituacaoRegularizacao;
	}

	public void setListaSituacaoRegularizacao(Collection<CerhSituacaoRegularizacao> listaSituacaoRegularizacao) {
		this.listaSituacaoRegularizacao = listaSituacaoRegularizacao;
	}

	public Collection<Tipologia> getListaTipologiaDadosConcedidos() {
		return listaTipologiaDadosConcedidos;
	}

	public void setListaTipologiaDadosConcedidos(Collection<Tipologia> listaTipologiaDadosConcedidos) {
		this.listaTipologiaDadosConcedidos = listaTipologiaDadosConcedidos;
	}

	public CerhProcesso getCerhProcesso() {
		return cerhProcesso;
	}

	public void setCerhProcesso(CerhProcesso cerhProcesso) {
		this.cerhProcesso = cerhProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidrico() {
		return listaTipoUsoRecursoHidrico;
	}

	public void setListaTipoUsoRecursoHidrico(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidrico) {
		this.listaTipoUsoRecursoHidrico = listaTipoUsoRecursoHidrico;
	}

	public Collection<CerhTipoAutorizacaoOutorgado> getListaCerhTipoAutorizacaoOutorgado() {
		return listaCerhTipoAutorizacaoOutorgado;
	}

	public void setListaCerhTipoAutorizacaoOutorgado(Collection<CerhTipoAutorizacaoOutorgado> listaCerhTipoAutorizacaoOutorgado) {
		this.listaCerhTipoAutorizacaoOutorgado = listaCerhTipoAutorizacaoOutorgado;
	}

	public Collection<CerhTipoAtoDispensa> getListaCerhTipoAtoDispensa() {
		return listaCerhTipoAtoDispensa;
	}

	public void setListaCerhTipoAtoDispensa(Collection<CerhTipoAtoDispensa> listaCerhTipoAtoDispensa) {
		this.listaCerhTipoAtoDispensa = listaCerhTipoAtoDispensa;
	}

	public CerhTipoUso getCerhTipoUsoSelecionado() {
		return cerhTipoUsoSelecionado;
	}

	public void setCerhTipoUsoSelecionado(CerhTipoUso cerhTipoUsoSelecionado) {
		this.cerhTipoUsoSelecionado = cerhTipoUsoSelecionado;
	}

	public boolean isNovoRegistro() {
		return novoRegistro;
	}

	public void setNovoRegistro(boolean novoRegistro) {
		this.novoRegistro = novoRegistro;
	}

	public CerhProcessoSuspensao getCerhProcessoSuspensaoSelecionado() {
		return cerhProcessoSuspensaoSelecionado;
	}

	public void setCerhProcessoSuspensaoSelecionado(
			CerhProcessoSuspensao cerhProcessoSuspensaoSelecionado) {
		this.cerhProcessoSuspensaoSelecionado = cerhProcessoSuspensaoSelecionado;
	}

	public boolean isExisteCerhTipoUsoSemProcesso() {
		return existeCerhTipoUsoSemProcesso;
	}

	public void setExisteCerhTipoUsoSemProcesso(boolean existeCerhTipoUsoSemProcesso) {
		this.existeCerhTipoUsoSemProcesso = existeCerhTipoUsoSemProcesso;
	}

	public Collection<CerhLocalizacaoGeografica> getListaCerhLocalizacaoGeografica() {
		return listaCerhLocalizacaoGeografica;
	}

	public void setListaCerhLocalizacaoGeografica(
			Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica) {
		this.listaCerhLocalizacaoGeografica = listaCerhLocalizacaoGeografica;
	}

	public boolean isRemoverEdicaoCerhSituacaoRegularizacao() {
		return removerEdicaoCerhSituacaoRegularizacao;
	}

	public void setRemoverEdicaoCerhSituacaoRegularizacao(
			boolean removerEdicaoCerhSituacaoRegularizacao) {
		this.removerEdicaoCerhSituacaoRegularizacao = removerEdicaoCerhSituacaoRegularizacao;
	}

	public TelaAcaoEnum getTelaAcaoEnum() {
		return telaAcaoEnum;
	}

	public void setTelaAcaoEnum(TelaAcaoEnum telaAcaoEnum) {
		this.telaAcaoEnum = telaAcaoEnum;
	}

	public Date getDtFimAutorizacao() {
		return dtFimAutorizacao;
	}

	public void setDtFimAutorizacao(Date dtFimAutorizacao) {
		this.dtFimAutorizacao = dtFimAutorizacao;
	}

	public boolean isValidarNumProcesso() {
		return validarNumProcesso;
	}

	public void setValidarNumProcesso(boolean validarNumProcesso) {
		this.validarNumProcesso = validarNumProcesso;
	}

	public Collection<TipoUsoRecursoHidrico> getListaTipoUsoRecursoHidricoSelecionado() {
		return listaTipoUsoRecursoHidricoSelecionado;
	}

	public void setListaTipoUsoRecursoHidricoSelecionado(Collection<TipoUsoRecursoHidrico> listaTipoUsoRecursoHidricoSelecionado) {
		this.listaTipoUsoRecursoHidricoSelecionado = listaTipoUsoRecursoHidricoSelecionado;
	}

	public boolean isProcessoSemTipologia() {
		return processoSemTipologia;
	}

	public void setProcessoSemTipologia(boolean processoSemTipologia) {
		this.processoSemTipologia = processoSemTipologia;
	}

	public boolean isPossuiCerhTipoUsoEditavel() {
		return possuiCerhTipoUsoEditavel;
	}

	public void setPossuiCerhTipoUsoEditavel(boolean possuiCerhTipoUsoEditavel) {
		this.possuiCerhTipoUsoEditavel = possuiCerhTipoUsoEditavel;
	}

	public boolean isExibirLabelTipoUso() {
		return exibirLabelTipoUso;
	}

	public void setExibirLabelTipoUso(boolean exibirLabelTipoUso) {
		this.exibirLabelTipoUso = exibirLabelTipoUso;
	}

	public Collection<CerhTipoUso> getListaCerhTipoUso() {
		return listaCerhTipoUso;
	}

	public void setListaCerhTipoUso(Collection<CerhTipoUso> listaCerhTipoUso) {
		this.listaCerhTipoUso = listaCerhTipoUso;
	}
	
	
}
