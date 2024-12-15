package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the base_operacional database table.
 * 
 */
@Entity
@Table(name="base_operacional")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "BaseOperacional.excluirByIdeLacTransporte", query = "DELETE FROM BaseOperacional b WHERE b.ideLacTransporte.ideLacTransporte = :ideLacTransporte")
})
public class BaseOperacional implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_operacional_ide_base_operacional_generator")
	@SequenceGenerator(name = "base_operacional_ide_base_operacional_generator", sequenceName = "base_operacional_ide_base_operacional_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_base_operacional", nullable = false)
	private Integer ideBaseOperacional;

	@JoinColumn(name = "ide_lac_transporte", referencedColumnName = "ide_lac_transporte", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private LacTransporte ideLacTransporte;

	@Column(name="num_area_construida",precision = 10, scale = 2, nullable = true)
	private Double numAreaConstruida;

	@Column(name="num_area_total",precision = 10, scale = 2, nullable = true)
	private Double numAreaTotal;
	
	@Basic(optional = false)
	@Column(name = "dtc_operacao",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcOperacao;

    public BaseOperacional() {
    }

	public Integer getIdeBaseOperacional() {
		return this.ideBaseOperacional;
	}

	public void setIdeBaseOperacional(Integer ideBaseOperacional) {
		this.ideBaseOperacional = ideBaseOperacional;
	}

	public LacTransporte getIdeLacTransporte() {
		return ideLacTransporte;
	}

	public void setIdeLacTransporte(LacTransporte ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public Date getDtcOperacao() {
		return dtcOperacao;
	}

	public void setDtcOperacao(Date dtcOperacao) {
		this.dtcOperacao = dtcOperacao;
	}

	public Double getNumAreaConstruida() {
		return numAreaConstruida;
	}

	public void setNumAreaConstruida(Double numAreaConstruida) {
		this.numAreaConstruida = numAreaConstruida;
	}

	public Double getNumAreaTotal() {
		return numAreaTotal;
	}

	public void setNumAreaTotal(Double numAreaTotal) {
		this.numAreaTotal = numAreaTotal;
	}
	
	
}