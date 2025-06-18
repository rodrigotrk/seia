package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "vw_tipologia_cnae")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwTipologiaCnae.findAll", query = "SELECT v FROM VwTipologiaCnae v"),
    @NamedQuery(name = "VwTipologiaCnae.findById", query = "SELECT v FROM VwTipologiaCnae v WHERE v.id = :id"),
    @NamedQuery(name = "VwTipologiaCnae.findByIdeTipologia", query = "SELECT v FROM VwTipologiaCnae v WHERE v.ideTipologia = :ideTipologia"),
    @NamedQuery(name = "VwTipologiaCnae.findByCodTipologia", query = "SELECT v FROM VwTipologiaCnae v WHERE v.codTipologia = :codTipologia"),
    @NamedQuery(name = "VwTipologiaCnae.findByDesTipologia", query = "SELECT v FROM VwTipologiaCnae v WHERE v.desTipologia = :desTipologia"),
    @NamedQuery(name = "VwTipologiaCnae.findByCnae", query = "SELECT v FROM VwTipologiaCnae v WHERE v.cnae = :cnae"),
    @NamedQuery(name = "VwTipologiaCnae.findByArea", query = "SELECT v FROM VwTipologiaCnae v WHERE v.area = :area")})
public class VwTipologiaCnae implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "ide_tipologia")
    private Integer ideTipologia;
    @Size(max = 20)
    @Column(name = "cod_tipologia")
    private String codTipologia;
    @Size(max = 250)
    @Column(name = "des_tipologia")
    private String desTipologia;
    @Column(name="ide_nivel_tipologia")
    private Integer ideNivelTipologia;
    @Column(name="nom_nivel_tecnologia")
    private String nomNivelTecnologia;
    @Size(max = 2147483647)
    @Column(name = "cnae")
    private String cnae;
    @Size(max = 2147483647)
    @Column(name = "area")
    private String area;
    
    
    public VwTipologiaCnae() {
    	
    }
   
    
    public VwTipologiaCnae(String codTipologia) {
		this.codTipologia = codTipologia;
	}
    
    
	

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdeTipologia() {
        return ideTipologia;
    }

    public void setIdeTipologia(Integer ideTipologia) {
        this.ideTipologia = ideTipologia;
    }

    public String getCodTipologia() {
        return codTipologia;
    }

    public void setCodTipologia(String codTipologia) {
        this.codTipologia = codTipologia;
    }

    public String getDesTipologia() {
        return desTipologia;
    }

    public void setDesTipologia(String desTipologia) {
        this.desTipologia = desTipologia;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


	public Integer getIdeNivelTipologia() {
		return ideNivelTipologia;
	}


	public void setIdeNivelTipologia(Integer ideNivelTipologia) {
		this.ideNivelTipologia = ideNivelTipologia;
	}


	public String getNomNivelTecnologia() {
		return nomNivelTecnologia;
	}


	public void setNomNivelTecnologia(String nomNivelTecnologia) {
		this.nomNivelTecnologia = nomNivelTecnologia;
	}
    
}
