package br.gov.ba.seia.entity.auditoria;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * Classe historico de alteração nos campos.
 * @author 
 *
 */
@Entity
@Table(name = "HIST_CAMPO")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "HistCampo.findAll", query = "SELECT ct FROM HistCampo ct"),
    @NamedQuery(name = "HistCampo.findByName", query = "SELECT ct FROM HistCampo ct WHERE lower(ct.nomeCampo) = lower(:nomeCampoTabela)"),
    @NamedQuery(name = "HistCampo.findByIdeTabela", query = "SELECT ct FROM HistCampo ct WHERE ct.ideTabela.ideTabela = :ideTabela ")})
public class HistCampo implements Serializable{
	private static final long serialVersionUID = -8532171144257944303L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campo_ide_campo_seq")
	@SequenceGenerator(name = "campo_ide_campo_seq", sequenceName = "campo_ide_campo_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_campo")
	private Long ideCampo;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "desc_campo")
	private String nomeCampo;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "ide_tabela", nullable = false)
	private HistTabela ideTabela;
	
    public HistCampo(Long ideCampo) {
    	this.ideCampo = ideCampo;
	}
    
    public HistCampo() {
	}
    
	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public Long getIdeCampo() {
		return ideCampo;
	}

	public void setIdeCampo(Long ideCampo) {
		this.ideCampo = ideCampo;
	}

	public HistTabela getIdeTabela() {
		return ideTabela;
	}

	public void setIdeTabela(HistTabela ideTabela) {
		this.ideTabela = ideTabela;
	}
	
	@Override
	public String toString() {
		return ideCampo+"";
	}
	
	@Override
	public boolean equals(Object obj) {
		        if (!(obj instanceof HistCampo)) {
		            return false;
		        }
		        HistCampo other = (HistCampo) obj;
		        if ((this.ideCampo == null && other.ideCampo != null) || (this.ideCampo!= null && !this.ideCampo.equals(other.ideCampo))) {
		            return false;
		        }
		        return true;
	}

}
