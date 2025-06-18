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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "area_produtiva_tipologia_atividade_piscicultura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadePiscicultura.findAll", query = "SELECT a FROM AreaProdutivaTipologiaAtividadePiscicultura a"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadePiscicultura.findByIdeAreaProdutivaTipologiaAtividadePiscicultura", query = "SELECT a FROM AreaProdutivaTipologiaAtividadePiscicultura a WHERE a.ideAreaProdutivaTipologiaAtividadePiscicultura = :ideAreaProdutivaTipologiaAtividadePiscicultura"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadePiscicultura.findByAreaProdutivaTipologiaAtividade", query = "SELECT a FROM AreaProdutivaTipologiaAtividadePiscicultura a WHERE a.ideAreaProdutivaTipologiaAtividade = :ideAreaProdutivaTipologiaAtividade"),
	@NamedQuery(name = "AreaProdutivaTipologiaAtividadePiscicultura.findByVolume", query = "SELECT a FROM AreaProdutivaTipologiaAtividadePiscicultura a WHERE a.volume = :volume")})
public class AreaProdutivaTipologiaAtividadePiscicultura implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_PISCICULTURA_SEQ") 
    @SequenceGenerator(name="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_PISCICULTURA_SEQ", sequenceName="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_PISCICULTURA_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_area_produtiva_tipologia_atividade_piscicultura", nullable = false)
	private Integer ideAreaProdutivaTipologiaAtividadePiscicultura;

	@JoinColumn(name = "ide_area_produtiva_tipologia_atividade", referencedColumnName = "ide_area_produtiva_tipologia_atividade")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "volume", nullable = false, precision = 14, scale = 2)
    private Double volume;

	public AreaProdutivaTipologiaAtividadePiscicultura() {
	}

	public AreaProdutivaTipologiaAtividadePiscicultura(
			Integer ideAreaProdutivaTipologiaAtividadePiscicultura,
			AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade,
			Double volume) {
		this.setIdeAreaProdutivaTipologiaAtividadePiscicultura(ideAreaProdutivaTipologiaAtividadePiscicultura);
		this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
		this.setVolume(volume);
	}

	public Integer getIdeAreaProdutivaTipologiaAtividadePiscicultura() {
		return ideAreaProdutivaTipologiaAtividadePiscicultura;
	}

	public void setIdeAreaProdutivaTipologiaAtividadePiscicultura(
			Integer ideAreaProdutivaTipologiaAtividadePiscicultura) {
		this.ideAreaProdutivaTipologiaAtividadePiscicultura = ideAreaProdutivaTipologiaAtividadePiscicultura;
	}

	public AreaProdutivaTipologiaAtividade getIdeAreaProdutivaTipologiaAtividade() {
		return this.ideAreaProdutivaTipologiaAtividade;
	}

	public void setIdeAreaProdutivaTipologiaAtividade(AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade) {
		this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAreaProdutivaTipologiaAtividadePiscicultura != null ? ideAreaProdutivaTipologiaAtividadePiscicultura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof AreaProdutivaTipologiaAtividadePiscicultura)) {
            return false;
        }
        AreaProdutivaTipologiaAtividadePiscicultura other = (AreaProdutivaTipologiaAtividadePiscicultura) object;
        if ((this.ideAreaProdutivaTipologiaAtividadePiscicultura == null && other.ideAreaProdutivaTipologiaAtividadePiscicultura != null) || (this.ideAreaProdutivaTipologiaAtividadePiscicultura != null && !this.ideAreaProdutivaTipologiaAtividadePiscicultura.equals(other.ideAreaProdutivaTipologiaAtividadePiscicultura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideAreaProdutivaTipologiaAtividadePiscicultura;
    }
   
}
