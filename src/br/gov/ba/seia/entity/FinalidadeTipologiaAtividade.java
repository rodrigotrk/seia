package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the finalidade_tipologia_atividade database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="finalidade_tipologia_atividade")
public class FinalidadeTipologiaAtividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finalidade_tipologia_atividade_ide_finalidade_tipologia_atividade_generator")
	@SequenceGenerator(name = "finalidade_tipologia_atividade_ide_finalidade_tipologia_atividade_generator", sequenceName = "finalidade_tipologia_atividade_ide_finalidade_tipologia_atividade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_finalidade_tipologia_atividade")
	private Integer ideFinalidadeTipologiaAtividade;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipologia_atividade", referencedColumnName = "ide_tipologia_atividade", nullable = true)
	private TipologiaAtividade ideTipologiaAtividade;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_finalidade", referencedColumnName = "ide_finalidade", nullable = true)
	private Finalidade ideFinalidade;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public FinalidadeTipologiaAtividade() {
    }

	public Integer getIdeFinalidadeTipologiaAtividade() {
		return this.ideFinalidadeTipologiaAtividade;
	}

	public void setIdeFinalidadeTipologiaAtividade(Integer ideFinalidadeTipologiaAtividade) {
		this.ideFinalidadeTipologiaAtividade = ideFinalidadeTipologiaAtividade;
	}

	public TipologiaAtividade getIdeTipologiaAtividade() {
		return ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Finalidade getIdeFinalidade() {
		return ideFinalidade;
	}

	public void setIdeFinalidade(Finalidade ideFinalidade) {
		this.ideFinalidade = ideFinalidade;
	}
}