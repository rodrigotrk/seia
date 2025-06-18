package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;

@Entity
@Table(name = "finalidade_reenquadramento")
public class FinalidadeReenquadramento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FINALIDADE_REENQUADRAMENTO_IDEFINALIDADEREENQUADRAMENTO_GENERATOR", sequenceName = "FINALIDADE_REENQUADRAMENTO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FINALIDADE_REENQUADRAMENTO_IDEFINALIDADEREENQUADRAMENTO_GENERATOR")
	@Column(name = "ide_finalidade_reenquadramento")
	private Integer ideFinalidadeReenquadramento;

	@Column(name = "dtc_criacao")
	private Timestamp dtcCriacao;

	@Column(name = "dtc_exclusao")
	private Timestamp dtcExclusao;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@Column(name = "nom_motivo_reenquadramento")
	private String nomMotivoReenquadramento;

	@OneToMany(mappedBy = "ideFinalidadeReenquadramento", fetch = FetchType.LAZY)
	private Collection<FinalidadeReenquadramentoProcesso> finalidadeReequadramentoProcessoCollection;

	public FinalidadeReenquadramento() {
	}
	
	public FinalidadeReenquadramento(FinalidadeReenquadramentoEnum finalidadeReenquadramentoEnum) {
		this.ideFinalidadeReenquadramento = finalidadeReenquadramentoEnum.getId();
	}

	public Integer getIdeFinalidadeReenquadramento() {
		return ideFinalidadeReenquadramento;
	}

	public void setIdeFinalidadeReenquadramento(Integer ideFinalidadeReenquadramento) {
		this.ideFinalidadeReenquadramento = ideFinalidadeReenquadramento;
	}

	public Timestamp getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Timestamp dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Timestamp getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Timestamp dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomMotivoReenquadramento() {
		return nomMotivoReenquadramento;
	}

	public void setNomMotivoReenquadramento(String nomMotivoReenquadramento) {
		this.nomMotivoReenquadramento = nomMotivoReenquadramento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFinalidadeReenquadramento == null) ? 0 : ideFinalidadeReenquadramento.hashCode());
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
		FinalidadeReenquadramento other = (FinalidadeReenquadramento) obj;
		if (ideFinalidadeReenquadramento == null) {
			if (other.ideFinalidadeReenquadramento != null)
				return false;
		} else if (!ideFinalidadeReenquadramento.equals(other.ideFinalidadeReenquadramento))
			return false;
		return true;
	}

	public Collection<FinalidadeReenquadramentoProcesso> getFinalidadeReequadramentoProcessoCollection() {
		return finalidadeReequadramentoProcessoCollection;
	}

	public void setFinalidadeReequadramentoProcessoCollection(
			Collection<FinalidadeReenquadramentoProcesso> finalidadeReequadramentoProcessoCollection) {
		this.finalidadeReequadramentoProcessoCollection = finalidadeReequadramentoProcessoCollection;
	}

}