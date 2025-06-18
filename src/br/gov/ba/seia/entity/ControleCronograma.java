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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import flexjson.JSON;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "controle_cronograma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControleCronograma.findAll", query = "SELECT c FROM ControleCronograma c"),
    @NamedQuery(name = "ControleCronograma.findByIdeControleCronograma", query = "SELECT c FROM ControleCronograma c WHERE c.ideControleCronograma = :ideControleCronograma"),
    @NamedQuery(name = "ControleCronograma.findByDscJustificativa", query = "SELECT c FROM ControleCronograma c WHERE c.dscJustificativa = :dscJustificativa"),
    @NamedQuery(name = "ControleCronograma.findByDtcItemPrevista", query = "SELECT c FROM ControleCronograma c WHERE c.dtcItemPrevista = :dtcItemPrevista"),
    @NamedQuery(name = "ControleCronograma.findByDtcItemRealizada", query = "SELECT c FROM ControleCronograma c WHERE c.dtcItemRealizada = :dtcItemRealizada"),
    @NamedQuery(name = "ControleCronograma.findByIndExcluido", query = "SELECT c FROM ControleCronograma c WHERE c.indExcluido = :indExcluido"),
    @NamedQuery(name = "ControleCronograma.findByDtcExclusao", query = "SELECT c FROM ControleCronograma c WHERE c.dtcExclusao = :dtcExclusao")})
public class ControleCronograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTROLE_CRONOGRAMA_IDE_CONTROLE_CRONOGRAMA_seq")    
    @SequenceGenerator(name="CONTROLE_CRONOGRAMA_IDE_CONTROLE_CRONOGRAMA_seq", sequenceName="CONTROLE_CRONOGRAMA_IDE_CONTROLE_CRONOGRAMA_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_controle_cronograma")
    private Integer ideControleCronograma;
    @Size(max = 2147483647)
    @Column(name = "dsc_justificativa")
    private String dscJustificativa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_item_prevista")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcItemPrevista;
    @Column(name = "dtc_item_realizada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcItemRealizada;
    @JoinColumn(name = "ide_item_cronograma", referencedColumnName = "ide_item_cronograma")
    @ManyToOne(optional = false)
    private ItemCronograma ideItemCronograma;
    @JoinColumn(name = "ide_cronograma", referencedColumnName = "ide_cronograma")
    @ManyToOne(optional = false)
    private Cronograma ideCronograma;
    @Column(name = "ind_excluido")
    private Boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

    public ControleCronograma() {
    }

    public ControleCronograma(Integer ideControleCronograma) {
        this.ideControleCronograma = ideControleCronograma;
    }
    
    public ControleCronograma(ItemCronograma itemCrono) {
        this.ideItemCronograma = itemCrono;
    }
    @JSON(include = false)
    public Integer getIdeControleCronograma() {
        return ideControleCronograma;
    }

    public void setIdeControleCronograma(Integer ideControleCronograma) {
        this.ideControleCronograma = ideControleCronograma;
    }

    public String getDscJustificativa() {
        return dscJustificativa;
    }

    public void setDscJustificativa(String dscJustificativa) {
        this.dscJustificativa = dscJustificativa;
    }

    public Date getDtcItemPrevista() {
        return dtcItemPrevista;
    }

    public void setDtcItemPrevista(Date dtcItemPrevista) {
        this.dtcItemPrevista = dtcItemPrevista;
    }

    public Date getDtcItemRealizada() {
        return dtcItemRealizada;
    }

    public void setDtcItemRealizada(Date dtcItemRealizada) {
        this.dtcItemRealizada = dtcItemRealizada;
    }

    public ItemCronograma getIdeItemCronograma() {
        return ideItemCronograma;
    }

    public void setIdeItemCronograma(ItemCronograma ideItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
    }

    @JSON(include = false)
    public Cronograma getIdeCronograma() {
        return ideCronograma;
    }

    public void setIdeCronograma(Cronograma ideCronograma) {
        this.ideCronograma = ideCronograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideControleCronograma != null ? ideControleCronograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ControleCronograma)) {
            return false;
        }
        ControleCronograma other = (ControleCronograma) object;
        if ((this.ideControleCronograma == null && other.ideControleCronograma != null) || (this.ideControleCronograma != null && !this.ideControleCronograma.equals(other.ideControleCronograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ControleCronograma[ ideControleCronograma=" + ideControleCronograma + " ]";
    }

	/**
	 * @return the dtcExclusao
	 */
    @JSON(include = false)
	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	/**
	 * @param dtcExclusao the dtcExclusao to set
	 */
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	/**
	 * @return the indExcluido
	 */
	@JSON(include = false)
	public boolean isIndExcluido() {
		return indExcluido;
	}

	/**
	 * @param indExcluido the indExcluido to set
	 */
	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
    
}
