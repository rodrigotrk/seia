package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "solicitacao_administrativo_ato_ambiental")
@NamedQueries({ 
	@NamedQuery(name = "SolicitacaoAdministrativoAtoAmbiental.removedByIdeSolicitacao", 
				query = "DELETE FROM SolicitacaoAdministrativoAtoAmbiental s WHERE s.ideSolicitacaoAdministrativo = :ideSolicitacaoAdministrativo") })

public class SolicitacaoAdministrativoAtoAmbiental implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="solicitacao_adminstrativo_ato_ambiental_seq", sequenceName="solicitacao_adminstrativo_ato_ambiental_seq",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="solicitacao_adminstrativo_ato_ambiental_seq")
    @NotNull
    @Column(name = "ide_solicitacao_administrativo_ato_ambiental",  unique=true, nullable = false)
    private Integer ideSolicitacaoAtoAdminstrativoAtoAmbiental;
	
	@NotNull
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbiental;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideTipologia;

	@NotNull
	@JoinColumn(name = "ide_solicitacao_administrativo", referencedColumnName = "ide_solicitacao_administrativo", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SolicitacaoAdministrativo ideSolicitacaoAdministrativo;

	public SolicitacaoAdministrativoAtoAmbiental() {
	}
	
	public SolicitacaoAdministrativoAtoAmbiental(AtoAmbiental ideAtoAmbiental, Tipologia ideTipologia, SolicitacaoAdministrativo ideSolicitacaoAdministrativo) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideTipologia = ideTipologia;
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}
	
	public SolicitacaoAdministrativoAtoAmbiental(AtoAmbiental ideAtoAmbiental, SolicitacaoAdministrativo ideSolicitacaoAdministrativo) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}
	public AtoAmbiental getIdeAtoAmbiental() {
		return this.ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public SolicitacaoAdministrativo getIdeSolicitacaoAdministrativo() {
		return this.ideSolicitacaoAdministrativo;
	}

	public void setIdeSolicitacaoAdministrativo(SolicitacaoAdministrativo ideSolicitacaoAdministrativo) {
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public Integer getIdeSolicitacaoAtoAdminstrativoAtoAmbiental() {
		return ideSolicitacaoAtoAdminstrativoAtoAmbiental;
	}

	public void setIdeSolicitacaoAtoAdminstrativoAtoAmbiental(Integer ideSolicitacaoAtoAdminstrativoAtoAmbiental) {
		this.ideSolicitacaoAtoAdminstrativoAtoAmbiental = ideSolicitacaoAtoAdminstrativoAtoAmbiental;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
		result = prime
				* result
				+ ((ideSolicitacaoAdministrativo == null) ? 0
						: ideSolicitacaoAdministrativo.hashCode());
		result = prime * result
				+ ((ideTipologia == null) ? 0 : ideTipologia.hashCode());
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
		SolicitacaoAdministrativoAtoAmbiental other = (SolicitacaoAdministrativoAtoAmbiental) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (ideSolicitacaoAdministrativo == null) {
			if (other.ideSolicitacaoAdministrativo != null)
				return false;
		} else if (!ideSolicitacaoAdministrativo
				.equals(other.ideSolicitacaoAdministrativo))
			return false;
		if (ideTipologia == null) {
			if (other.ideTipologia != null)
				return false;
		} else if (!ideTipologia.equals(other.ideTipologia))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[ideSolicitacaoAtoAdminstrativoAtoAmbiental=" + ideSolicitacaoAtoAdminstrativoAtoAmbiental
				+ ", ideAtoAmbiental=" 	+ ideAtoAmbiental
				+ ", ideTipologia="		+ ideTipologia
				+ ", ideSolicitacaoAdministrativo=" + ideSolicitacaoAdministrativo + "]";
	}

	
	
}