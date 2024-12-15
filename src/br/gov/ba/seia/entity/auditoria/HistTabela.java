package br.gov.ba.seia.entity.auditoria;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Classe do historico da tabela.
 * @author 
 *
 */
@Entity
@Table(name = "HIST_TABELA")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "HistTabela.findAll", query = "SELECT t FROM HistTabela t"),
    @NamedQuery(name = "HistTabela.findByName", query = "SELECT t FROM HistTabela t WHERE t.nomeTabela = :nomeTabela")})
public class HistTabela implements Serializable{
	private static final long serialVersionUID = 2361099096310462166L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tabela_ide_tabela_seq")
	@SequenceGenerator(name = "tabela_ide_tabela_seq", sequenceName = "tabela_ide_tabela_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_tabela")
	private Long ideTabela;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "desc_tabela")
	private String nomeTabela;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTabela")
	private Collection<HistCampo> campoCollection;
	
	public HistTabela() {
	}
	
	public HistTabela(Long ideTabela) {
		this.ideTabela = ideTabela;
	}

	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public Long getIdeTabela() {
		return ideTabela;
	}

	public void setIdeTabela(Long ideTabela) {
		this.ideTabela = ideTabela;
	}

	public Collection<HistCampo> getCampoCollection() {
		return campoCollection;
	}

	public void setCampoCollection(Collection<HistCampo> campoCollection) {
		this.campoCollection = campoCollection;
	}
	
	@Override
	public String toString() {
		return ideTabela+"";
	}
	
	@Override
	public boolean equals(Object obj) {
		        if (!(obj instanceof HistTabela)) {
		            return false;
		        }
		        HistTabela other = (HistTabela) obj;
		        if ((this.ideTabela == null && other.ideTabela != null) || (this.ideTabela != null && !this.ideTabela.equals(other.ideTabela))) {
		            return false;
		        }
		        return true;
	}
}
