package br.gov.ba.seia.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "cronograma_etapa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CronogramaEtapa.findAll", query = "SELECT c FROM CronogramaEtapa c"),
    @NamedQuery(name = "CronogramaEtapa.findByIdeCronogramaEtapa", query = "SELECT c FROM CronogramaEtapa c WHERE c.ideCronogramaEtapa = :ideCronogramaEtapa"),
    @NamedQuery(name = "CronogramaEtapa.findByMesInicio", query = "SELECT c FROM CronogramaEtapa c WHERE c.mesInicio = :mesInicio"),
    @NamedQuery(name = "CronogramaEtapa.findByAnoInicio", query = "SELECT c FROM CronogramaEtapa c WHERE c.anoInicio = :anoInicio"),
    @NamedQuery(name = "CronogramaEtapa.findByMesFim", query = "SELECT c FROM CronogramaEtapa c WHERE c.mesFim = :mesFim"),
    @NamedQuery(name = "CronogramaEtapa.findByAnoFim", query = "SELECT c FROM CronogramaEtapa c WHERE c.anoFim = :anoFim"),
    @NamedQuery(name = "CronogramaEtapa.findByDscOutroTipoRecuperacao", query = "SELECT c FROM CronogramaEtapa c WHERE c.dscOutroTipoRecuperacao = :dscOutroTipoRecuperacao")})
public class CronogramaEtapa implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRONOGRAMA_ETAPA_IDE_CRONOGRAMA_ETAPA_SEQ") 
    @SequenceGenerator(name="CRONOGRAMA_ETAPA_IDE_CRONOGRAMA_ETAPA_SEQ", sequenceName="CRONOGRAMA_ETAPA_IDE_CRONOGRAMA_ETAPA_SEQ", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cronograma_etapa", nullable = false)
	private Integer ideCronogramaEtapa;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "mes_inicio")
    private String mesInicio;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ano_inicio")
    private String anoInicio;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "mes_fim")
    private String mesFim;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ano_fim")
    private String anoFim;
    
	@Size(max = 50)
    @Column(name = "dsc_outro_tipo_recuperacao")
    private String dscOutroTipoRecuperacao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "percentual")
    private Integer percentual;
	
	@JoinColumn(name = "ide_tipo_recuperacao", referencedColumnName = "ide_tipo_recuperacao")
    @OneToOne(optional = false)
    private TipoRecuperacao ideTipoRecuperacao;
    
	@JoinColumn(name = "ide_cronograma_recuperacao", referencedColumnName = "ide_cronograma_recuperacao")
    @ManyToOne(optional = false)
    private CronogramaRecuperacao ideCronogramaRecuperacao;

    public CronogramaEtapa() {
    }

    public CronogramaEtapa(Integer ideCronogramaEtapa) {
        this.ideCronogramaEtapa = ideCronogramaEtapa;
    }

    public CronogramaEtapa(Integer ideCronogramaEtapa, String mesInicio, String anoInicio, String mesFim, String anoFim, Integer percentual) {
        this.ideCronogramaEtapa = ideCronogramaEtapa;
        this.mesInicio = mesInicio;
        this.anoInicio = anoInicio;
        this.mesFim = mesFim;
        this.anoFim = anoFim;
        this.percentual = percentual;
    }

    public Integer getIdeCronogramaEtapa() {
        return ideCronogramaEtapa;
    }

    public void setIdeCronogramaEtapa(Integer ideCronogramaEtapa) {
        this.ideCronogramaEtapa = ideCronogramaEtapa;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(String anoInicio) {
        this.anoInicio = anoInicio;
    }

    public String getMesFim() {
        return mesFim;
    }

    public void setMesFim(String mesFim) {
        this.mesFim = mesFim;
    }

    public String getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(String anoFim) {
        this.anoFim = anoFim;
    }

    public Integer getPercentual() {
		return percentual;
	}

	public void setPercentual(Integer percentual) {
		this.percentual = percentual;
	}

	public String getDscOutroTipoRecuperacao() {
        return dscOutroTipoRecuperacao;
    }

    public void setDscOutroTipoRecuperacao(String dscOutroTipoRecuperacao) {
        this.dscOutroTipoRecuperacao = dscOutroTipoRecuperacao;
    }

    public TipoRecuperacao getIdeTipoRecuperacao() {
        return ideTipoRecuperacao;
    }

    public void setIdeTipoRecuperacao(TipoRecuperacao ideTipoRecuperacao) {
        this.ideTipoRecuperacao = ideTipoRecuperacao;
    }

    public CronogramaRecuperacao getIdeCronogramaRecuperacao() {
        return ideCronogramaRecuperacao;
    }

    public void setIdeCronogramaRecuperacao(CronogramaRecuperacao ideCronogramaRecuperacao) {
        this.ideCronogramaRecuperacao = ideCronogramaRecuperacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCronogramaEtapa != null ? ideCronogramaEtapa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof CronogramaEtapa)) {
            return false;
        }
        CronogramaEtapa other = (CronogramaEtapa) object;
        if ((this.ideCronogramaEtapa == null && other.ideCronogramaEtapa != null) || (this.ideCronogramaEtapa != null && !this.ideCronogramaEtapa.equals(other.ideCronogramaEtapa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CronogramaEtapa[ ideCronogramaEtapa=" + ideCronogramaEtapa + " ]";
    }
    
}
