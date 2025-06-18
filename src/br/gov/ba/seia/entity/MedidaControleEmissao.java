package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the medida_controle_emissao database table.
 * 
 */
@Entity
@Table(name="medida_controle_emissao")
@NamedQuery(name="MedidaControleEmissao.findAll", query="SELECT m FROM MedidaControleEmissao m")
public class MedidaControleEmissao implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MEDIDA_CONTROLE_EMISSAO_IDEMEDIDACONTROLEEMISSAO_GENERATOR", sequenceName="MEDIDA_CONTROLE_EMISSAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEDIDA_CONTROLE_EMISSAO_IDEMEDIDACONTROLEEMISSAO_GENERATOR")
	@Column(name="ide_medida_controle_emissao")
	private Integer ideMedidaControleEmissao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_medida_controle_emissao")
	private String nomMedidaControleEmissao;

	//bi-directional many-to-one association to CaracterizacaoAmbientalMedidaControleEmissao
	@OneToMany(mappedBy="medidaControleEmissao")
	private List<CaracterizacaoAmbientalMedidaControleEmissao> caracterizacaoAmbientalMedidaControleEmissaos;

	public MedidaControleEmissao() {
	}

	public Integer getIdeMedidaControleEmissao() {
		return this.ideMedidaControleEmissao;
	}

	public void setIdeMedidaControleEmissao(Integer ideMedidaControleEmissao) {
		this.ideMedidaControleEmissao = ideMedidaControleEmissao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomMedidaControleEmissao() {
		return this.nomMedidaControleEmissao;
	}

	public void setNomMedidaControleEmissao(String nomMedidaControleEmissao) {
		this.nomMedidaControleEmissao = nomMedidaControleEmissao;
	}

	public List<CaracterizacaoAmbientalMedidaControleEmissao> getCaracterizacaoAmbientalMedidaControleEmissaos() {
		return this.caracterizacaoAmbientalMedidaControleEmissaos;
	}

	public void setCaracterizacaoAmbientalMedidaControleEmissaos(List<CaracterizacaoAmbientalMedidaControleEmissao> caracterizacaoAmbientalMedidaControleEmissaos) {
		this.caracterizacaoAmbientalMedidaControleEmissaos = caracterizacaoAmbientalMedidaControleEmissaos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideMedidaControleEmissao == null) ? 0
						: ideMedidaControleEmissao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedidaControleEmissao other = (MedidaControleEmissao) obj;
		if (ideMedidaControleEmissao == null) {
			if (other.ideMedidaControleEmissao != null)
				return false;
		} else if (!ideMedidaControleEmissao
				.equals(other.ideMedidaControleEmissao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideMedidaControleEmissao);
	}

	
}