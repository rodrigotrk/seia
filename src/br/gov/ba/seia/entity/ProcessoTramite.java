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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author carlos.sousa
 */
@Entity
@Table(name = "processo_tramite")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ProcessoTramite.findAll", query = "SELECT p FROM ProcessoTramite p"),
		@NamedQuery(name = "ProcessoTramite.deleteByIde", query = "DELETE FROM ProcessoTramite WHERE ideProcessoTramite = :ideProcessoTramite"),
		@NamedQuery(name = "ProcessoTramite.buscaProcessoTramiteByIde", query = "SELECT pr FROM ProcessoTramite pr WHERE pr.ideRequerimento.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "ProcessoTramite.findByIdeProcessoTramite", query = "SELECT p FROM ProcessoTramite p WHERE p.ideProcessoTramite = :ideProcessoTramite"),
		@NamedQuery(name = "ProcessoTramite.findByNumProcessoTramite", query = "SELECT p FROM ProcessoTramite p WHERE p.numProcessoTramite = :numProcessoTramite"),
		@NamedQuery(name = "ProcessoTramite.findByRequerimento", query = "SELECT p FROM ProcessoTramite p left join p.ideRequerimento r WHERE r.ideRequerimento = :ideRequerimento") })
public class ProcessoTramite implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "PROCESSO_TRAMITE_IDEPROCESSOTRAMITE_GENERATOR", sequenceName = "PROCESSO_TRAMITE_IDE_PROCESSO_TRAMITE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_TRAMITE_IDEPROCESSOTRAMITE_GENERATOR")
	@Column(name = "ide_processo_tramite")
	private Integer ideProcessoTramite;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "num_processo_tramite", nullable = false, length = 40)
	private String numProcessoTramite;
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_sistema", referencedColumnName = "ide_sistema")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Sistema ideSistema;

	@Basic(optional = false)
	@Size(min = 1, max = 100)
	@Column(name = "dsc_tipo_processo_tramite", nullable = true, length = 100)
	private String dscTipoProcessoTramite;

	public ProcessoTramite() {
	}

	public ProcessoTramite(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public ProcessoTramite(Integer ideProcessoTramite) {
		this.ideProcessoTramite = ideProcessoTramite;
	}

	public ProcessoTramite(String numProcessoTramite, Requerimento ideRequerimento) {
		this.numProcessoTramite = numProcessoTramite;
		this.ideRequerimento = ideRequerimento;
	}

	public ProcessoTramite(Integer ideProcessoTramite, String numProcessoTramite) {
		this.ideProcessoTramite = ideProcessoTramite;
		this.numProcessoTramite = numProcessoTramite;
	}

	public Integer getIdeProcessoTramite() {
		return ideProcessoTramite;
	}

	public void setIdeProcessoTramite(Integer ideProcessoTramite) {
		this.ideProcessoTramite = ideProcessoTramite;
	}

	public String getNumProcessoTramite() {
		return numProcessoTramite;
	}

	public void setNumProcessoTramite(String numProcessoTramite) {
		this.numProcessoTramite = numProcessoTramite;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Sistema getIdeSistema() {
		return ideSistema;
	}

	public void setIdeSistema(Sistema ideSistema) {
		this.ideSistema = ideSistema;
	}

	public String getDscTipoProcessoTramite() {
		return dscTipoProcessoTramite;
	}

	public void setDscTipoProcessoTramite(String dscTipoProcessoTramite) {
		this.dscTipoProcessoTramite = dscTipoProcessoTramite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numProcessoTramite == null) ? 0 : numProcessoTramite.hashCode());
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
		ProcessoTramite other = (ProcessoTramite) obj;
		if (numProcessoTramite == null) {
			if (other.numProcessoTramite != null)
				return false;
		} else if (!numProcessoTramite.equals(other.numProcessoTramite))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.ProcessoTramite[ ideProcessoTramite=" + ideProcessoTramite + " ]";
	}

}
