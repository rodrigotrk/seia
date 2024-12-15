package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "especie_supressao_autorizacao")
public class EspecieSupressaoAutorizacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 6201710025306420494L;

	@Id
	@SequenceGenerator(name = "ESPECIE_SUPRESSAO_AUTORIZACAO_GENERATOR", sequenceName = "especie_supressao_autorizacao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESPECIE_SUPRESSAO_AUTORIZACAO_GENERATOR")
	@Column(name = "ide_especie_supressao_autorizacao")
	private Integer ideEspecieSupressaoAutorizacao;

	@JoinColumn(name = "ide_especie_supressao", referencedColumnName = "ide_especie_supressao", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieSupressao ideEspecieSupressao;
	
	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NomePopularEspecie ideNomePopularEspecie;
	
	@JoinColumn(name = "ide_fce_asv", referencedColumnName = "ide_fce_asv", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAsv fceAsv;
	
	@Column(name = "volume_total_fora_app", nullable = true)
	private Double volumeTotalForaApp;

	@Column(name = "volume_total_em_app", nullable = true)
	private Double volumeTotalEmApp;
	
	@JoinColumn(name = "ide_processo_ato_concedido", referencedColumnName = "ide_processo_ato_concedido", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAtoConcedido ideProcessoAtoConcedido;
	
	@OneToMany(mappedBy="ideEspecieSupressaoAutorizacao", fetch=FetchType.LAZY)
	private Collection<EspecieSupressaoAutDestinoSocioEconomico> especieSupressaoAutDestinoSocioEconomicoCollection;
	
	@JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Produto ideProduto;
	
	@JoinColumn(name = "ide_processo_ato", referencedColumnName = "ide_processo_ato", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAto ideProcessoAto;
	
	@Transient
	private Double volumeTotalForaEstereo;
	
	@Transient
	private Double volumeTotalForaMDC;
	
	@Transient
	private Double volumeTotalEmEstereo;
	
	@Transient
	private Double volumeTotalEmMDC;
	
	@Transient
	private Boolean edicao;
	
	@Transient
	private List<DestinoSocioeconomico> listaDestinoSocioeconomicoSelecionado;
	
	public Integer getIdeEspecieSupressaoAutorizacao() {
		return ideEspecieSupressaoAutorizacao;
	}

	public void setIdeEspecieSupressaoAutorizacao(
			Integer ideEspecieSupressaoAutorizacao) {
		this.ideEspecieSupressaoAutorizacao = ideEspecieSupressaoAutorizacao;
	}

	public EspecieSupressao getIdeEspecieSupressao() {
		return ideEspecieSupressao;
	}

	public void setIdeEspecieSupressao(EspecieSupressao ideEspecieSupressao) {
		this.ideEspecieSupressao = ideEspecieSupressao;
	}

	public NomePopularEspecie getIdeNomePopularEspecie() {
		return ideNomePopularEspecie;
	}

	public void setIdeNomePopularEspecie(NomePopularEspecie ideNomePopularEspecie) {
		this.ideNomePopularEspecie = ideNomePopularEspecie;
	}

	public Double getVolumeTotalForaApp() {
		return volumeTotalForaApp;
	}

	public void setVolumeTotalForaApp(Double volumeTotalForaApp) {
		this.volumeTotalForaApp = volumeTotalForaApp;
	}

	public Double getVolumeTotalEmApp() {
		return volumeTotalEmApp;
	}

	public void setVolumeTotalEmApp(Double volumeTotalEmApp) {
		this.volumeTotalEmApp = volumeTotalEmApp;
	}

	public Collection<EspecieSupressaoAutDestinoSocioEconomico> getEspecieSupressaoAutDestinoSocioEconomicoCollection() {
		return especieSupressaoAutDestinoSocioEconomicoCollection;
	}

	public void setEspecieSupressaoAutDestinoSocioEconomicoCollection(
			Collection<EspecieSupressaoAutDestinoSocioEconomico> especieSupressaoAutDestinoSocioEconomicoCollection) {
		this.especieSupressaoAutDestinoSocioEconomicoCollection = especieSupressaoAutDestinoSocioEconomicoCollection;
	}

	public Double getVolumeTotalForaEstereo() {
		return volumeTotalForaEstereo;
	}

	public void setVolumeTotalForaEstereo(Double volumeTotalForaEstereo) {
		this.volumeTotalForaEstereo = volumeTotalForaEstereo;
	}

	public Double getVolumeTotalForaMDC() {
		return volumeTotalForaMDC;
	}

	public void setVolumeTotalForaMDC(Double volumeTotalForaMDC) {
		this.volumeTotalForaMDC = volumeTotalForaMDC;
	}

	public Double getVolumeTotalEmEstereo() {
		return volumeTotalEmEstereo;
	}

	public void setVolumeTotalEmEstereo(Double volumeTotalEmEstereo) {
		this.volumeTotalEmEstereo = volumeTotalEmEstereo;
	}

	public Double getVolumeTotalEmMDC() {
		return volumeTotalEmMDC;
	}

	public void setVolumeTotalEmMDC(Double volumeTotalEmMDC) {
		this.volumeTotalEmMDC = volumeTotalEmMDC;
	}

	public FceAsv getFceAsv() {
		return fceAsv;
	}

	public void setFceAsv(FceAsv fceAsv) {
		this.fceAsv = fceAsv;
	}

	public List<DestinoSocioeconomico> getListaDestinoSocioeconomicoSelecionado() {
		return listaDestinoSocioeconomicoSelecionado;
	}

	public void setListaDestinoSocioeconomicoSelecionado(
			List<DestinoSocioeconomico> listaDestinoSocioeconomicoSelecionado) {
		this.listaDestinoSocioeconomicoSelecionado = listaDestinoSocioeconomicoSelecionado;
	}

	public ProcessoAtoConcedido getIdeProcessoAtoConcedido() {
		return ideProcessoAtoConcedido;
	}

	public void setIdeProcessoAtoConcedido(
			ProcessoAtoConcedido ideProcessoAtoConcedido) {
		this.ideProcessoAtoConcedido = ideProcessoAtoConcedido;
	}

	public Boolean getEdicao() {
		if (edicao == null) {
			if (ideEspecieSupressaoAutorizacao != null){
				edicao = false;
			} else {
				edicao = true;
			}
		}
		return edicao;
	}

	public void setEdicao(Boolean edicao) {
		this.edicao = edicao;
	}

	@Override
	public Long getId() {
		return new Long(this.ideEspecieSupressaoAutorizacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEspecieSupressao == null) ? 0 : ideEspecieSupressao
						.hashCode());
		result = prime
				* result
				+ ((ideEspecieSupressaoAutorizacao == null) ? 0
						: ideEspecieSupressaoAutorizacao.hashCode());
		result = prime * result
				+ ((ideProduto == null) ? 0 : ideProduto.hashCode());
		return result;
	}

	public Produto getIdeProduto() {
		return ideProduto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EspecieSupressaoAutorizacao other = (EspecieSupressaoAutorizacao) obj;
		if (ideEspecieSupressao == null) {
			if (other.ideEspecieSupressao != null)
				return false;
		} else if (!ideEspecieSupressao.equals(other.ideEspecieSupressao))
			return false;
		if (ideEspecieSupressaoAutorizacao == null) {
			if (other.ideEspecieSupressaoAutorizacao != null)
				return false;
		} else if (!ideEspecieSupressaoAutorizacao
				.equals(other.ideEspecieSupressaoAutorizacao))
			return false;
		if (ideProduto == null) {
			if (other.ideProduto != null)
				return false;
		} else if (!ideProduto.equals(other.ideProduto))
			return false;
		return true;
	}

	public void setIdeProduto(Produto ideProduto) {
		this.ideProduto = ideProduto;
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}
	
	
}
