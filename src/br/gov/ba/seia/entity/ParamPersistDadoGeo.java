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

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "param_persist_dado_geo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParamPersistDadoGeo.findAll", query = "SELECT p FROM ParamPersistDadoGeo p"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByIdeParamPersistDadoGeo", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.ideParamPersistDadoGeo = :ideParamPersistDadoGeo"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByIdeDadoGeografico", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.ideDadoGeografico = :ideDadoGeografico"),
    @NamedQuery(name = "ParamPersistDadoGeo.findBySrid", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.srid = :srid"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByPastaDosArquivos", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.pastaDosArquivos = :pastaDosArquivos"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByLatitude", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.latitude = :latitude"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByLongitude", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.longitude = :longitude"),
    @NamedQuery(name = "ParamPersistDadoGeo.findByDtcPersistido", query = "SELECT p FROM ParamPersistDadoGeo p WHERE p.dtcPersistido = :dtcPersistido"),
	@NamedQuery(name = "ParamPersistDadoGeo.excluirPorLocGeoOUDadoGeo", query = "DELETE FROM ParamPersistDadoGeo p WHERE (p.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica) or (p.ideDadoGeografico = :ideDadoGeografico)")})
public class ParamPersistDadoGeo implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="param_persist_dado_geo_ide_param_persist_dado_geo_seq")    
    @SequenceGenerator(name="param_persist_dado_geo_ide_param_persist_dado_geo_seq", sequenceName="param_persist_dado_geo_ide_param_persist_dado_geo_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_param_persist_dado_geo")
    private Integer ideParamPersistDadoGeo;
    @Column(name = "ide_dado_geografico")
    private Integer ideDadoGeografico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "srid")
    private String srid;
    @Size(max = 60)
    @Column(name = "pasta_dos_arquivos")
    private String pastaDosArquivos;
    @Size(max = 10)
    @Column(name = "latitude")
    private String latitude;
    @Size(max = 10)
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "dtc_persistido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcPersistido;
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
    @ManyToOne
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @Column(name = "ide_imovel_rural")
    private Integer ideImovelRural;

    public ParamPersistDadoGeo() {
    }

    public ParamPersistDadoGeo(Integer ideParamPersistDadoGeo) {
        this.ideParamPersistDadoGeo = ideParamPersistDadoGeo;
    }

    public ParamPersistDadoGeo(Integer ideParamPersistDadoGeo, String srid) {
        this.ideParamPersistDadoGeo = ideParamPersistDadoGeo;
        this.srid = srid;
    }

    public Integer getIdeParamPersistDadoGeo() {
        return ideParamPersistDadoGeo;
    }

    public void setIdeParamPersistDadoGeo(Integer ideParamPersistDadoGeo) {
        this.ideParamPersistDadoGeo = ideParamPersistDadoGeo;
    }

    public Integer getIdeDadoGeografico() {
        return ideDadoGeografico;
    }

    public void setIdeDadoGeografico(Integer ideDadoGeografico) {
        this.ideDadoGeografico = ideDadoGeografico;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public String getPastaDosArquivos() {
        return pastaDosArquivos;
    }

    public void setPastaDosArquivos(String pastaDosArquivos) {
        this.pastaDosArquivos = pastaDosArquivos;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getDtcPersistido() {
        return dtcPersistido;
    }

    public void setDtcPersistido(Date dtcPersistido) {
        this.dtcPersistido = dtcPersistido;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }

    public Integer getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(Integer ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParamPersistDadoGeo != null ? ideParamPersistDadoGeo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ParamPersistDadoGeo)) {
            return false;
        }
        ParamPersistDadoGeo other = (ParamPersistDadoGeo) object;
        if ((this.ideParamPersistDadoGeo == null && other.ideParamPersistDadoGeo != null) || (this.ideParamPersistDadoGeo != null && !this.ideParamPersistDadoGeo.equals(other.ideParamPersistDadoGeo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ParamPersistDadoGeo[ ideParamPersistDadoGeo=" + ideParamPersistDadoGeo + " ]";
    }
    
    @Override
    public ParamPersistDadoGeo clone() throws CloneNotSupportedException {
    	return (ParamPersistDadoGeo) super.clone();
    }
    
}
