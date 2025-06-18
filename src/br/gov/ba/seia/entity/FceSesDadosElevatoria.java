package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
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

@Entity
@Table(name="fce_ses_dados_elevatoria")
public class FceSesDadosElevatoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_dados_elevatoria_seq")
	@SequenceGenerator(name = "fce_ses_dados_elevatoria_seq", sequenceName = "fce_ses_dados_elevatoria_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_dados_elevatoria", nullable = false)
	private Integer ideFceSesDadosElevatoria;
	
	@Column(name="des_identificacao_elevatoria")
	private String descricaoIdentificacao;
	
	@Column(name="val_vazao")
	private Double valorVazao;
	
	@Column(name="val_extensao_linha")
	private Double valorExtensaoLinha;
		
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_fce_ses", referencedColumnName="ide_fce_ses")
	private FceSes ideFceSes;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora", referencedColumnName="ide_faixa_diametro_adutora")
	private FaixaDiametroAdutora ideFaixaDiametroAdutora;

	@OneToMany(mappedBy="ideFceSesDadosElevatoria",fetch=FetchType.LAZY)
	private Collection<FceSesElevatoriaLocalizacaoGeografica> fceSesElevatoriaLocalizacaoGeograficaCollection;
	
	public Integer getIdeFceSesDadosElevatoria() {
		return ideFceSesDadosElevatoria;
	}

	public void setIdeFceSesDadosElevatoria(Integer ideFceSesDadosElevatoria) {
		this.ideFceSesDadosElevatoria = ideFceSesDadosElevatoria;
	}

	public String getDescricaoIdentificacao() {
		return descricaoIdentificacao;
	}

	public void setDescricaoIdentificacao(String descricaoIdentificacao) {
		this.descricaoIdentificacao = descricaoIdentificacao;
	}

	public Double getValorVazao() {
		return valorVazao;
	}

	public void setValorVazao(Double valorVazao) {
		this.valorVazao = valorVazao;
	}

	public Double getValorExtensaoLinha() {
		return valorExtensaoLinha;
	}

	public void setValorExtensaoLinha(Double valorExtensaoLinha) {
		this.valorExtensaoLinha = valorExtensaoLinha;
	}

	public FceSes getIdeFceSes() {
		return ideFceSes;
	}

	public void setIdeFceSes(FceSes ideFceSes) {
		this.ideFceSes = ideFceSes;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutora() {
		return ideFaixaDiametroAdutora;
	}

	public void setIdeFaixaDiametroAdutora(
			FaixaDiametroAdutora ideFaixaDiametroAdutora) {
		this.ideFaixaDiametroAdutora = ideFaixaDiametroAdutora;
	}

	public Collection<FceSesElevatoriaLocalizacaoGeografica> getFceSesElevatoriaLocalizacaoGeograficaCollection() {
		return fceSesElevatoriaLocalizacaoGeograficaCollection;
	}

	public void setFceSesElevatoriaLocalizacaoGeograficaCollection(
			Collection<FceSesElevatoriaLocalizacaoGeografica> fceSesElevatoriaLocalizacaoGeograficaCollection) {
		this.fceSesElevatoriaLocalizacaoGeograficaCollection = fceSesElevatoriaLocalizacaoGeograficaCollection;
	}
	
}
