package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="indice_cobranca")
@XmlRootElement
public class IndiceCobranca implements Serializable {

	private static final long serialVersionUID = -819148968735957631L;
	
	@Id
	@SequenceGenerator(name="INDICE_COBRANCA_SEQ", sequenceName="INDICE_COBRANCA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INDICE_COBRANCA_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_indice_cobranca")
	private Integer ideIndiceCobranca;
	
	@Column(name = "dsc_indice_cobranca", nullable = false, length=100)
	private String dscIndiceCobranca;
	
	@Column(name = "sgl_indice_cobranca", nullable = false, length=10)
	private String sglIndiceCobranca;

	public Integer getIdeIndiceCobranca() {
		return ideIndiceCobranca;
	}

	public void setIdeIndiceCobranca(Integer ideIndiceCobranca) {
		this.ideIndiceCobranca = ideIndiceCobranca;
	}

	public String getDscIndiceCobranca() {
		return dscIndiceCobranca;
	}

	public void setDscIndiceCobranca(String dscIndiceCobranca) {
		this.dscIndiceCobranca = dscIndiceCobranca;
	}

	public String getSglIndiceCobranca() {
		return sglIndiceCobranca;
	}

	public void setSglIndiceCobranca(String sglIndiceCobranca) {
		this.sglIndiceCobranca = sglIndiceCobranca;
	}
	
}
