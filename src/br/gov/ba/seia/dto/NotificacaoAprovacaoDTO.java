package br.gov.ba.seia.dto;

import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.FinalidadeReenquadramentoProcesso;
import br.gov.ba.seia.entity.JustificativaRejeicao;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.MotivoEdicaoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.ReenquadramentoPotencialPoluicao;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoTipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;
import br.gov.ba.seia.util.Util;

public class NotificacaoAprovacaoDTO {

	private Legislacao legislacao;
	private VwConsultaProcesso vwProcesso;
	private Endereco enderecoEmpreendimento;
	private Notificacao notificacao;
	private List<JustificativaRejeicao> listaJustificativas;
	private List<MotivoEdicaoNotificacao> lMotivoEdicaoNotificacao;
	private List<SelectItem> lSituacaoNotificacao;
	private Integer situacaoNotificacao;
	private Area areaGestor;
	private Pauta pautaGestor;

	private ReenquadramentoProcesso reenquadramentoProcesso;
	private Collection<ReenquadramentoProcessoAto> listaAlteracaoReenquadramentoProcessoAto;
	private Collection<ReenquadramentoProcessoAto> listaInclusaoReenquadramentoProcessoAto;
	private Collection<ReenquadramentoPotencialPoluicao> listaReenquadramentoPotencialPoluicao;
	private Collection<ReenquadramentoTipologia> listaReenquadramentoTipologia;

	public boolean isRenderedPnlReequadramentoProcesso() {
		return isExisteReenquadramento();
	}

	public boolean isRenderedPnlAlterarAtoAmbiental() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.ALTERACAO_ATOS_AUTORIZATIVOS);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}

	public boolean isRenderedPnlIncluirNovoAtoAmbiental() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}
	
	public boolean isRenderedPnlAlterarPotencialPoluidor() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}
	
	public boolean isRenderedPnlAlterarClasseEmpreendimento() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}
	
	public boolean isRenderedPnlAlterarTipologia() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.ALTERACAO_TIPOLOGIA);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}
	
	public boolean isRenderedPnlCorrigirPorte() {
		FinalidadeReenquadramentoProcesso frp = new FinalidadeReenquadramentoProcesso(reenquadramentoProcesso, FinalidadeReenquadramentoEnum.CORRECAO_PORTE_EMPREENDIMENTO);
		return isExisteReenquadramento() && reenquadramentoProcesso.getFinalidadeReequadramentoProcessoCollection().contains(frp);
	}

	private boolean isExisteReenquadramento() {
		return Util.isNull(reenquadramentoProcesso) == false;
	}

	public Legislacao getLegislacao() {
		return legislacao;
	}

	public void setLegislacao(Legislacao legislacao) {
		this.legislacao = legislacao;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Endereco getEnderecoEmpreendimento() {
		return enderecoEmpreendimento;
	}

	public void setEnderecoEmpreendimento(Endereco enderecoEmpreendimento) {
		this.enderecoEmpreendimento = enderecoEmpreendimento;
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public List<JustificativaRejeicao> getListaJustificativas() {
		return listaJustificativas;
	}

	public void setListaJustificativas(List<JustificativaRejeicao> listaJustificativas) {
		this.listaJustificativas = listaJustificativas;
	}

	public List<MotivoEdicaoNotificacao> getlMotivoEdicaoNotificacao() {
		return lMotivoEdicaoNotificacao;
	}

	public void setlMotivoEdicaoNotificacao(List<MotivoEdicaoNotificacao> lMotivoEdicaoNotificacao) {
		this.lMotivoEdicaoNotificacao = lMotivoEdicaoNotificacao;
	}

	public List<SelectItem> getlSituacaoNotificacao() {
		return lSituacaoNotificacao;
	}

	public void setlSituacaoNotificacao(List<SelectItem> lSituacaoNotificacao) {
		this.lSituacaoNotificacao = lSituacaoNotificacao;
	}

	public Integer getSituacaoNotificacao() {
		return situacaoNotificacao;
	}

	public void setSituacaoNotificacao(Integer situacaoNotificacao) {
		this.situacaoNotificacao = situacaoNotificacao;
	}

	public Area getAreaGestor() {
		return areaGestor;
	}

	public void setAreaGestor(Area areaGestor) {
		this.areaGestor = areaGestor;
	}

	public Pauta getPautaGestor() {
		return pautaGestor;
	}

	public void setPautaGestor(Pauta pautaGestor) {
		this.pautaGestor = pautaGestor;
	}

	public ReenquadramentoProcesso getReenquadramentoProcesso() {
		return reenquadramentoProcesso;
	}

	public void setReenquadramentoProcesso(ReenquadramentoProcesso reenquadramentoProcesso) {
		this.reenquadramentoProcesso = reenquadramentoProcesso;
	}

	public Collection<ReenquadramentoProcessoAto> getListaAlteracaoReenquadramentoProcessoAto() {
		return listaAlteracaoReenquadramentoProcessoAto;
	}

	public void setListaAlteracaoReenquadramentoProcessoAto(
			Collection<ReenquadramentoProcessoAto> listaAlteracaoReenquadramentoProcessoAto) {
		this.listaAlteracaoReenquadramentoProcessoAto = listaAlteracaoReenquadramentoProcessoAto;
	}

	public Collection<ReenquadramentoProcessoAto> getListaInclusaoReenquadramentoProcessoAto() {
		return listaInclusaoReenquadramentoProcessoAto;
	}

	public void setListaInclusaoReenquadramentoProcessoAto(
			Collection<ReenquadramentoProcessoAto> listaInclusaoReenquadramentoProcessoAto) {
		this.listaInclusaoReenquadramentoProcessoAto = listaInclusaoReenquadramentoProcessoAto;
	}

	public Collection<ReenquadramentoPotencialPoluicao> getListaReenquadramentoPotencialPoluicao() {
		return listaReenquadramentoPotencialPoluicao;
	}

	public void setListaReenquadramentoPotencialPoluicao(
			Collection<ReenquadramentoPotencialPoluicao> listaReenquadramentoPotencialPoluicao) {
		this.listaReenquadramentoPotencialPoluicao = listaReenquadramentoPotencialPoluicao;
	}

	public Collection<ReenquadramentoTipologia> getListaReenquadramentoTipologia() {
		return listaReenquadramentoTipologia;
	}

	public void setListaReenquadramentoTipologia(Collection<ReenquadramentoTipologia> listaReenquadramentoTipologia) {
		this.listaReenquadramentoTipologia = listaReenquadramentoTipologia;
	}

}