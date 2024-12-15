package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="fce_ses")
public class FceSes {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_seq")
	@SequenceGenerator(name = "fce_ses_seq", sequenceName = "fce_ses_ide_fce_ses_seq", allocationSize = 1)
	@Column(name="ide_fce_ses")
	private Integer ideFceSes;
	
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;
	
	@Column(name="val_horizonte_projeto")
	private Integer valorHorizonteProjeto;
	
	@Column(name="val_populacao_atendida")
	private Integer valorPopulacaoAtendida;
	
	@Column(name="val_vazao_med_projeto")
	private Double valorVazaoMediaProjeto;
	
	@Column(name="val_extensao_total_rede")
    private Double valorExtensaoTotalRede;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora", referencedColumnName="ide_faixa_diametro_adutora")
	private FaixaDiametroAdutora ideFaixaDiametroAdutora;
	
	public FceSes(Fce fce) {
		this.ideFce = fce;
	}
	
	public FceSes() {
	
	}

	public Integer getIdeFceSes() {
		return ideFceSes;
	}

	public void setIdeFceSes(Integer ideFceSes) {
		this.ideFceSes = ideFceSes;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Integer getValorHorizonteProjeto() {
		return valorHorizonteProjeto;
	}

	public void setValorHorizonteProjeto(Integer valorHorizonteProjeto) {
		this.valorHorizonteProjeto = valorHorizonteProjeto;
	}

	public Integer getValorPopulacaoAtendida() {
		return valorPopulacaoAtendida;
	}

	public void setValorPopulacaoAtendida(Integer valorPopulacaoAtendida) {
		this.valorPopulacaoAtendida = valorPopulacaoAtendida;
	}

	public Double getValorVazaoMediaProjeto() {
		return valorVazaoMediaProjeto;
	}

	public void setValorVazaoMediaProjeto(Double valorVazaoMediaProjeto) {
		this.valorVazaoMediaProjeto = valorVazaoMediaProjeto;
	}

	public Double getValorExtensaoTotalRede() {
		return valorExtensaoTotalRede;
	}

	public void setValorExtensaoTotalRede(Double valorExtensaoTotalRede) {
		this.valorExtensaoTotalRede = valorExtensaoTotalRede;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutora() {
		return ideFaixaDiametroAdutora;
	}

	public void setIdeFaixaDiametroAdutora(
			FaixaDiametroAdutora ideFaixaDiametroAdutora) {
		this.ideFaixaDiametroAdutora = ideFaixaDiametroAdutora;
	}
    
}
