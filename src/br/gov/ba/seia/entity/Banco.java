package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "banco")
@NamedQueries({ @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b") })
public class Banco implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_banco")
	private Integer ideBanco;
	@Basic(optional = false)
	@Column(name = "cod_banco")
	private String codBanco;
	@Basic(optional = false)
	@Column(name = "nom_banco")
	private String nomBanco;
	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;

	public Banco() {
	}

	public Banco(Integer ideBanco) {
		this.ideBanco = ideBanco;
	}

	public Banco(Integer ideBanco, String codBanco, String nomBanco, boolean indAtivo) {
		this.ideBanco = ideBanco;
		this.codBanco = codBanco;
		this.nomBanco = nomBanco;
		this.indAtivo = indAtivo;
	}

	public Integer getIdeBanco() {
		return ideBanco;
	}

	public void setIdeBanco(Integer ideBanco) {
		this.ideBanco = ideBanco;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getNomBanco() {
		return nomBanco;
	}

	public void setNomBanco(String nomBanco) {
		this.nomBanco = nomBanco;
	}

	public boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideBanco != null ? ideBanco.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof Banco)) {
			return false;
		}
		Banco other = (Banco) object;
		if ((this.ideBanco == null && other.ideBanco != null)
				|| (this.ideBanco != null && !this.ideBanco.equals(other.ideBanco))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.Banco[ ideBanco=" + ideBanco + " ]";
	}

}
