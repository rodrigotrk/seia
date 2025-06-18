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
@Table(name="objetivo_queima_controlada")
public class ObjetivoQueimaControlada implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objetivo_qc_ide_objetivo_queima_controlada_seq")
	@SequenceGenerator(name = "objetivo_qc_ide_objetivo_queima_controlada_seq", sequenceName = "objetivo_qc_ide_objetivo_queima_controlada_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_objetivo_queima_controlada")
	private Integer ideObjetivoQueimaControlada;

	@Column(name="des_objetivo_queima_controlada", length = 200)
	private String desObjetivoQueimaControlada;

	@Column(name="dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;
	
	@Column(name="ind_possui_area_prevista")
	private Boolean indPossuiAreaPrevista;

	@OneToMany(mappedBy="ideObjetivoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;

	public ObjetivoQueimaControlada() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObjetivoQueimaControlada == null) ? 0 : ideObjetivoQueimaControlada.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ObjetivoQueimaControlada other = (ObjetivoQueimaControlada) obj;
		if (ideObjetivoQueimaControlada == null) {
			if (other.ideObjetivoQueimaControlada != null) return false;
		} else if (!ideObjetivoQueimaControlada.equals(other.ideObjetivoQueimaControlada)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjetivoQueimaControlada [ideObjetivoQueimaControlada=" + ideObjetivoQueimaControlada + "]";
	}

	@Override
	public Long getId() {
		return ideObjetivoQueimaControlada.longValue();
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeObjetivoQueimaControlada() {
		return ideObjetivoQueimaControlada;
	}

	public void setIdeObjetivoQueimaControlada(Integer ideObjetivoQueimaControlada) {
		this.ideObjetivoQueimaControlada = ideObjetivoQueimaControlada;
	}

	public String getDesObjetivoQueimaControlada() {
		return desObjetivoQueimaControlada;
	}

	public void setDesObjetivoQueimaControlada(String desObjetivoQueimaControlada) {
		this.desObjetivoQueimaControlada = desObjetivoQueimaControlada;
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

	public List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection() {
		return declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;
	}

	public void setDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection(
			List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection) {
		this.declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection = declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;
	}

	public Boolean getIndPossuiAreaPrevista() {
		return indPossuiAreaPrevista;
	}

	public void setIndPossuiAreaPrevista(Boolean indPossuiAreaPrevista) {
		this.indPossuiAreaPrevista = indPossuiAreaPrevista;
	}
}