package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="tecnica_queima_controlada")
public class TecnicaQueimaControlada implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tecnica_qc_ide_tecnica_queima_controlada_seq")
	@SequenceGenerator(name = "tecnica_qc_ide_tecnica_queima_controlada_seq", sequenceName = "tecnica_qc_ide_tecnica_queima_controlada_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tecnica_queima_controlada")
	private Integer ideTecnicaQueimaControlada;

	@Column(name="dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	@Column(name="nom_tecnica_queima_controlada", length = 100)
	private String nomTecnicaQueimaControlada;

	@OneToMany(mappedBy="ideTecnicaQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaTecnicaUtilizada> declaracaoQueimaControladaTecnicaUtilizadaCollection;
	
	public TecnicaQueimaControlada() {
	}
	
	public TecnicaQueimaControlada(String nomTecnicaQueimaControlada) {
		this.nomTecnicaQueimaControlada = nomTecnicaQueimaControlada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTecnicaQueimaControlada == null) ? 0 : ideTecnicaQueimaControlada.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TecnicaQueimaControlada other = (TecnicaQueimaControlada) obj;
		if (ideTecnicaQueimaControlada == null) {
			if (other.ideTecnicaQueimaControlada != null) return false;
		} else if (!ideTecnicaQueimaControlada.equals(other.ideTecnicaQueimaControlada)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "TecnicaQueimaControlada [ideTecnicaQueimaControlada=" + ideTecnicaQueimaControlada + "]";
	}

	@Override
	public Long getId() {
		return ideTecnicaQueimaControlada.longValue();
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeTecnicaQueimaControlada() {
		return ideTecnicaQueimaControlada;
	}

	public void setIdeTecnicaQueimaControlada(Integer ideTecnicaQueimaControlada) {
		this.ideTecnicaQueimaControlada = ideTecnicaQueimaControlada;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomTecnicaQueimaControlada() {
		return nomTecnicaQueimaControlada;
	}

	public void setNomTecnicaQueimaControlada(String nomTecnicaQueimaControlada) {
		this.nomTecnicaQueimaControlada = nomTecnicaQueimaControlada;
	}

	public List<DeclaracaoQueimaControladaTecnicaUtilizada> getDeclaracaoQueimaControladaTecnicaUtilizadaCollection() {
		return declaracaoQueimaControladaTecnicaUtilizadaCollection;
	}

	public void setDeclaracaoQueimaControladaTecnicaUtilizadaCollection(
			List<DeclaracaoQueimaControladaTecnicaUtilizada> declaracaoQueimaControladaTecnicaUtilizadaCollection) {
		this.declaracaoQueimaControladaTecnicaUtilizadaCollection = declaracaoQueimaControladaTecnicaUtilizadaCollection;
	}
}