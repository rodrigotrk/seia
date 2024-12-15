package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "arquivo_lac")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ArquivoLac.findAll", query = "SELECT a FROM ArquivoLac a"),
		@NamedQuery(name = "ArquivoLac.findByIdeArquivo", query = "SELECT a FROM ArquivoLac a WHERE a.arquivoLacPK.ideArquivo = :ideArquivo"),
		@NamedQuery(name = "ArquivoLac.findByIdeLac", query = "SELECT a FROM ArquivoLac a WHERE a.arquivoLacPK.ideLac = :ideLac"),
		@NamedQuery(name = "ArquivoLac.findByIndAtivo", query = "SELECT a FROM ArquivoLac a WHERE a.indAtivo = :indAtivo") })
public class ArquivoLac implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	protected ArquivoLacPK arquivoLacPK;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_ativo")
	private boolean indAtivo;
	
	@JoinColumn(name = "ide_lac", referencedColumnName = "ide_lac", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Lac lac;
	
	@JoinColumn(name = "ide_arquivo", referencedColumnName = "ide_arquivo", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Arquivo arquivo;

	public ArquivoLac() {}

	public ArquivoLac(ArquivoLacPK arquivoLacPK) {
		this.arquivoLacPK = arquivoLacPK;
	}

	public ArquivoLac(ArquivoLacPK arquivoLacPK, boolean indAtivo) {
		this.arquivoLacPK = arquivoLacPK;
		this.indAtivo = indAtivo;
	}

	public ArquivoLac(int ideArquivo, int ideLac) {
		this.arquivoLacPK = new ArquivoLacPK(ideArquivo, ideLac);
	}

	public ArquivoLacPK getArquivoLacPK() {
		return arquivoLacPK;
	}

	public void setArquivoLacPK(ArquivoLacPK arquivoLacPK) {
		this.arquivoLacPK = arquivoLacPK;
	}

	public boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (arquivoLacPK != null ? arquivoLacPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof ArquivoLac)) {
			return false;
		}
		ArquivoLac other = (ArquivoLac) object;
		if ((this.arquivoLacPK == null && other.arquivoLacPK != null) || (this.arquivoLacPK != null && !this.arquivoLacPK.equals(other.arquivoLacPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ArquivoLac[ arquivoLacPK=" + arquivoLacPK + " ]";
	}

}
