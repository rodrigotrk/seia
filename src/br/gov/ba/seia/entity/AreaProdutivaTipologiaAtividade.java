package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "area_produtiva_tipologia_atividade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProdutivaTipologiaAtividade.findAll", query = "SELECT a FROM AreaProdutivaTipologiaAtividade a"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividade.findByIdeAreaProdutivaTipologiaAtividade", query = "SELECT a FROM AreaProdutivaTipologiaAtividade a WHERE a.ideAreaProdutivaTipologiaAtividade = :ideAreaProdutivaTipologiaAtividade"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividade.findByIndExcluido", query = "SELECT a FROM AreaProdutivaTipologiaAtividade a WHERE a.indExcluido = :indExcluido"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividade.findByDtcCriacao", query = "SELECT a FROM AreaProdutivaTipologiaAtividade a WHERE a.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividade.findByDtcExclusao", query = "SELECT a FROM AreaProdutivaTipologiaAtividade a WHERE a.dtcExclusao = :dtcExclusao")})
public class AreaProdutivaTipologiaAtividade implements Serializable, Cloneable {
    
	private static final long serialVersionUID = -8665383072936331201L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_IDE_AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_SEQ") 
	@SequenceGenerator(name="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_IDE_AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_SEQ", sequenceName="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_IDE_AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_area_produtiva_tipologia_atividade", nullable = false)
	private Integer ideAreaProdutivaTipologiaAtividade;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido")
    private boolean indExcluido;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIME)
    private Date dtcCriacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIME)
    private Date dtcExclusao;
    
	@JoinColumn(name = "ide_tipologia_atividade", referencedColumnName = "ide_tipologia_atividade")
    @ManyToOne(optional = false)
    private TipologiaAtividade ideTipologiaAtividade;
    
	@JoinColumn(name = "ide_area_produtiva", referencedColumnName = "ide_area_produtiva")
    @ManyToOne(optional = false)
    private AreaProdutiva ideAreaProdutiva;
	
    @OneToOne(mappedBy = "ideAreaProdutivaTipologiaAtividade")
    @Cascade(CascadeType.DELETE)
    private AreaProdutivaTipologiaAtividadeAgricultura ideAreaProdutivaTipologiaAtividadeAgricultura;

    @OneToOne(mappedBy = "ideAreaProdutivaTipologiaAtividade")
    @Cascade(CascadeType.DELETE)
    private AreaProdutivaTipologiaAtividadeAnimal ideAreaProdutivaTipologiaAtividadeAnimal;

    @OneToOne(mappedBy = "ideAreaProdutivaTipologiaAtividade")
    @Cascade(CascadeType.DELETE)
    private AreaProdutivaTipologiaAtividadePiscicultura ideAreaProdutivaTipologiaAtividadePiscicultura;

    public AreaProdutivaTipologiaAtividade() {
    }

    public AreaProdutivaTipologiaAtividade(Integer ideAreaProdutivaTipologiaAtividade) {
        this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
    }

    public AreaProdutivaTipologiaAtividade(Integer ideAreaProdutivaTipologiaAtividade, boolean indExcluido, Date dtcCriacao) {
        this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeAreaProdutivaTipologiaAtividade() {
        return ideAreaProdutivaTipologiaAtividade;
    }

    public void setIdeAreaProdutivaTipologiaAtividade(Integer ideAreaProdutivaTipologiaAtividade) {
        this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
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

    public TipologiaAtividade getIdeTipologiaAtividade() {
        return ideTipologiaAtividade;
    }

    public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
        this.ideTipologiaAtividade = ideTipologiaAtividade;
    }

    public AreaProdutiva getIdeAreaProdutiva() {
        return ideAreaProdutiva;
    }

    public void setIdeAreaProdutiva(AreaProdutiva ideAreaProdutiva) {
        this.ideAreaProdutiva = ideAreaProdutiva;
    }

	public AreaProdutivaTipologiaAtividadeAgricultura getIdeAreaProdutivaTipologiaAtividadeAgricultura() {
		return ideAreaProdutivaTipologiaAtividadeAgricultura;
	}

	public void setIdeAreaProdutivaTipologiaAtividadeAgricultura(
			AreaProdutivaTipologiaAtividadeAgricultura ideAreaProdutivaTipologiaAtividadeAgricultura) {
		this.ideAreaProdutivaTipologiaAtividadeAgricultura = ideAreaProdutivaTipologiaAtividadeAgricultura;
	}

	public AreaProdutivaTipologiaAtividadeAnimal getIdeAreaProdutivaTipologiaAtividadeAnimal() {
		return ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	public void setIdeAreaProdutivaTipologiaAtividadeAnimal(
			AreaProdutivaTipologiaAtividadeAnimal ideAreaProdutivaTipologiaAtividadeAnimal) {
		this.ideAreaProdutivaTipologiaAtividadeAnimal = ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	public AreaProdutivaTipologiaAtividadePiscicultura getIdeAreaProdutivaTipologiaAtividadePiscicultura() {
		return ideAreaProdutivaTipologiaAtividadePiscicultura;
	}

	public void setIdeAreaProdutivaTipologiaAtividadePiscicultura(
			AreaProdutivaTipologiaAtividadePiscicultura ideAreaProdutivaTipologiaAtividadePiscicultura) {
		this.ideAreaProdutivaTipologiaAtividadePiscicultura = ideAreaProdutivaTipologiaAtividadePiscicultura;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAreaProdutivaTipologiaAtividade != null ? ideAreaProdutivaTipologiaAtividade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof AreaProdutivaTipologiaAtividade)) {
            return false;
        }
        AreaProdutivaTipologiaAtividade other = (AreaProdutivaTipologiaAtividade) object;
        if ((this.ideAreaProdutivaTipologiaAtividade == null && other.ideAreaProdutivaTipologiaAtividade != null) || (this.ideAreaProdutivaTipologiaAtividade != null && !this.ideAreaProdutivaTipologiaAtividade.equals(other.ideAreaProdutivaTipologiaAtividade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideAreaProdutivaTipologiaAtividade;
    }
    
    public AreaProdutivaTipologiaAtividade clone() throws CloneNotSupportedException {
		return (AreaProdutivaTipologiaAtividade) super.clone();
	}
}
