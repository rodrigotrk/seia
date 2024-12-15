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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "especie_florestal_autorizacao")
public class EspecieFlorestalAutorizacao extends AbstractEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especie_florestal_autorizacao_seq")
	@SequenceGenerator(name = "especie_florestal_autorizacao_seq", sequenceName = "especie_florestal_autorizacao_seq", allocationSize = 1)
	@Column(name = "ide_especie_florestal_autorizacao")
	private Integer ideEspecieFlorestalAutorizacao;
	
	@Column(name = "volume_total_fora_app", nullable = true)
	private Double volumeTotalForaApp;
	
	@Column(name = "volume_total_em_app", nullable = true)
	private Double volumeTotalEmApp;
	
	@JoinColumn(name = "ide_fce_florestal", referencedColumnName = "ide_fce_florestal", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceFlorestal fceFlorestal;
	
	@JoinColumn(name = "ide_processo_ato", referencedColumnName = "ide_processo_ato", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAto ideProcessoAto;
	
	@JoinColumn(name = "ide_processo_ato_concedido", referencedColumnName = "ide_processo_ato_concedido", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAtoConcedido ideProcessoAtoConcedido;
	
	@JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Produto ideProduto;
	
	@JoinColumn(name = "ide_especie_florestal", referencedColumnName = "ide_especie_florestal", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieFlorestal ideEspecieFlorestal;
	
	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NomePopularEspecie ideNomePopularEspecie;
	
	@OneToMany(mappedBy = "ideEspecieFlorestalAutorizacao", fetch = FetchType.LAZY)
	private Collection<EspecieFlorestalAutDestinoSocioEconomico> especieFlorestalAutDestinoSocioEconomicoCollection;
	
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
	
	public EspecieFlorestalAutorizacao() {}

	public Boolean getEdicao() {
		if (edicao == null) {
			if (ideEspecieFlorestalAutorizacao != null) {
				edicao = false;
			} else {
				edicao = true;
			}
		}
		return edicao;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeEspecieFlorestalAutorizacao() {
		return ideEspecieFlorestalAutorizacao;
	}
	
	public void setIdeEspecieFlorestalAutorizacao(Integer ideEspecieFlorestalAutorizacao) {
		this.ideEspecieFlorestalAutorizacao = ideEspecieFlorestalAutorizacao;
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
	
	public FceFlorestal getFceFlorestal() {
		return fceFlorestal;
	}
	
	public void setFceFlorestal(FceFlorestal fceFlorestal) {
		this.fceFlorestal = fceFlorestal;
	}
	
	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}
	
	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}
	
	public ProcessoAtoConcedido getIdeProcessoAtoConcedido() {
		return ideProcessoAtoConcedido;
	}
	
	public void setIdeProcessoAtoConcedido(ProcessoAtoConcedido ideProcessoAtoConcedido) {
		this.ideProcessoAtoConcedido = ideProcessoAtoConcedido;
	}
	
	public Produto getIdeProduto() {
		return ideProduto;
	}
	
	public void setIdeProduto(Produto ideProduto) {
		this.ideProduto = ideProduto;
	}
	
	public EspecieFlorestal getIdeEspecieFlorestal() {
		return ideEspecieFlorestal;
	}
	
	public void setIdeEspecieFlorestal(EspecieFlorestal ideEspecieFlorestal) {
		this.ideEspecieFlorestal = ideEspecieFlorestal;
	}
	
	public NomePopularEspecie getIdeNomePopularEspecie() {
		return ideNomePopularEspecie;
	}
	
	public void setIdeNomePopularEspecie(NomePopularEspecie ideNomePopularEspecie) {
		this.ideNomePopularEspecie = ideNomePopularEspecie;
	}
	
	public Collection<EspecieFlorestalAutDestinoSocioEconomico> getEspecieFlorestalAutDestinoSocioEconomicoCollection() {
		return especieFlorestalAutDestinoSocioEconomicoCollection;
	}
	
	public void setEspecieFlorestalAutDestinoSocioEconomicoCollection(
			Collection<EspecieFlorestalAutDestinoSocioEconomico> especieFlorestalAutDestinoSocioEconomicoCollection) {
		this.especieFlorestalAutDestinoSocioEconomicoCollection = especieFlorestalAutDestinoSocioEconomicoCollection;
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
	
	public List<DestinoSocioeconomico> getListaDestinoSocioeconomicoSelecionado() {
		return listaDestinoSocioeconomicoSelecionado;
	}
	
	public void setListaDestinoSocioeconomicoSelecionado(List<DestinoSocioeconomico> listaDestinoSocioeconomicoSelecionado) {
		this.listaDestinoSocioeconomicoSelecionado = listaDestinoSocioeconomicoSelecionado;
	}
	
	public void setEdicao(Boolean edicao) {
		this.edicao = edicao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((ideEspecieFlorestalAutorizacao == null) ? 0
						: ideEspecieFlorestalAutorizacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EspecieFlorestalAutorizacao other = (EspecieFlorestalAutorizacao) obj;
		if (ideEspecieFlorestalAutorizacao == null) {
			if (other.ideEspecieFlorestalAutorizacao != null)
				return false;
		} else if (!ideEspecieFlorestalAutorizacao
				.equals(other.ideEspecieFlorestalAutorizacao))
			return false;
		return true;
	}

	@Override
	public EspecieFlorestalAutorizacao clone() throws CloneNotSupportedException  {
		return (EspecieFlorestalAutorizacao) super.clone();
	}
}
