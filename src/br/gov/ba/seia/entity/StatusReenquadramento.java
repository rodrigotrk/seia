package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "status_reenquadramento")
public class StatusReenquadramento implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_REENQUADRAMENTO_IDESTATUSREENQUADRAMENTO_GENERATOR")
	@SequenceGenerator(name = "STATUS_REENQUADRAMENTO_IDESTATUSREENQUADRAMENTO_GENERATOR", sequenceName = "STATUS_REENQUADRAMENTO_SEQ", allocationSize=1)
	@Column(name = "ide_status_reenquadramento")
	private Long ideStatusReenquadramento;

	@Column(name = "dtc_criacao")
	private Timestamp dtcCriacao;

	@Column(name = "dtc_exclusao")
	private Timestamp dtcExclusao;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@Column(name = "nom_status_reenquadramento")
	private String nomStatusReenquadramento;

	@OneToMany(mappedBy = "ideStatusReenquadramento")
	private Collection<TramitacaoReenquadramentoProcesso> tramitacaoReenquadramentoProcessoCollection;

	public StatusReenquadramento() {
	}
	
	public StatusReenquadramento(StatusReenquadramentoEnum statusReenquadramentoEnum) {
		this.ideStatusReenquadramento = statusReenquadramentoEnum.getId();
	}
	
	public Long getIdeStatusReenquadramento() {
		return ideStatusReenquadramento;
	}

	public void setIdeStatusReenquadramento(Long ideStatusReenquadramento) {
		this.ideStatusReenquadramento = ideStatusReenquadramento;
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

	public String getNomStatusReenquadramento() {
		return nomStatusReenquadramento;
	}

	public void setNomStatusReenquadramento(String nomStatusReenquadramento) {
		this.nomStatusReenquadramento = nomStatusReenquadramento;
	}

	public Collection<TramitacaoReenquadramentoProcesso> getTramitacaoReenquadramentoProcessoCollection() {
		return tramitacaoReenquadramentoProcessoCollection;
	}

	public void setTramitacaoReenquadramentoProcessoCollection(
			Collection<TramitacaoReenquadramentoProcesso> tramitacaoReenquadramentoProcessoCollection) {
		this.tramitacaoReenquadramentoProcessoCollection = tramitacaoReenquadramentoProcessoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideStatusReenquadramento == null) ? 0 : ideStatusReenquadramento.hashCode());
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
		StatusReenquadramento other = (StatusReenquadramento) obj;
		if (ideStatusReenquadramento == null) {
			if (other.ideStatusReenquadramento != null)
				return false;
		} else if (!ideStatusReenquadramento.equals(other.ideStatusReenquadramento))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return ideStatusReenquadramento;
	}

}