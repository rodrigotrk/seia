package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author alex.santos
 */
@Entity
@Table(name = "objetivo_limpeza_area", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_objetivo_limpeza_area"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetivoLimpezaArea.findAll", query = "SELECT o FROM ObjetivoLimpezaArea o order by o.nomObjetivoLimpezaArea"),
    @NamedQuery(name = "ObjetivoLimpezaArea.findByIdeObjetivoLimpezaArea", query = "SELECT o FROM ObjetivoLimpezaArea o WHERE o.ideObjetivoLimpezaArea = :ideObjetivoLimpezaArea"),
    @NamedQuery(name = "ObjetivoLimpezaArea.findByNomObjetivoLimpezaArea", query = "SELECT o FROM ObjetivoLimpezaArea o WHERE o.nomObjetivoLimpezaArea = :nomObjetivoLimpezaArea")})
public class ObjetivoLimpezaArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="OBJETIVO_LIMPEZA_AREA_IDEOBJETIVOLIMPEZAAREA_GENERATOR", sequenceName="OBJETIVO_LIMPEZA_AREA_IDE_OBJETIVO_LIMPEZA_AREA_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OBJETIVO_LIMPEZA_AREA_IDEOBJETIVOLIMPEZAAREA_GENERATOR")
    @Column(name="ide_objetivo_limpeza_area", unique=true, nullable=false)
    private Integer ideObjetivoLimpezaArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_objetivo_limpeza_area", nullable = false, length = 60)
    private String nomObjetivoLimpezaArea;   
    
    @Transient
    private Boolean salvoObjetivoLimpezaArea;

    public ObjetivoLimpezaArea() {
    }

    public ObjetivoLimpezaArea(Integer ideObjetivoLimpezaArea) {
        this.ideObjetivoLimpezaArea = ideObjetivoLimpezaArea;
    }

    public ObjetivoLimpezaArea(Integer ideObjetivoLimpezaArea, String nomObjetivoLimpezaArea) {
        this.ideObjetivoLimpezaArea = ideObjetivoLimpezaArea;
        this.nomObjetivoLimpezaArea = nomObjetivoLimpezaArea;
    }

    public Integer getIdeObjetivoLimpezaArea() {
        return ideObjetivoLimpezaArea;
    }

    public void setIdeObjetivoLimpezaArea(Integer ideObjetivoLimpezaArea) {
        this.ideObjetivoLimpezaArea = ideObjetivoLimpezaArea;
    }

    public String getNomObjetivoLimpezaArea() {
        return nomObjetivoLimpezaArea;
    }

    public void setNomObjetivoLimpezaArea(String nomObjetivoLimpezaArea) {
        this.nomObjetivoLimpezaArea = nomObjetivoLimpezaArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideObjetivoLimpezaArea != null ? ideObjetivoLimpezaArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ObjetivoLimpezaArea)) {
            return false;
        }
        ObjetivoLimpezaArea other = (ObjetivoLimpezaArea) object;
        if ((this.ideObjetivoLimpezaArea == null && other.ideObjetivoLimpezaArea != null) || (this.ideObjetivoLimpezaArea != null && !this.ideObjetivoLimpezaArea.equals(other.ideObjetivoLimpezaArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+ideObjetivoLimpezaArea;
    }

	public Boolean getResposta() {
		return true;
	}
	
	public Boolean getSalvoObjetivoLimpezaArea(){
		if(Util.isNullOuVazio(ideObjetivoLimpezaArea)){
			salvoObjetivoLimpezaArea = false;
			return salvoObjetivoLimpezaArea;
		}else{
			List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea;
			try {
				listObjReqLimpArea = Util.clonaListaObjReqLimpArea(ContextoUtil.getContexto().getListaObjetReqLimpArea());
				if(!Util.isNullOuVazio(listObjReqLimpArea)){
					for (ObjetivoRequerimentoLimpezaArea objRqLimArea : listObjReqLimpArea) {
						if(objRqLimArea.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea() == ideObjetivoLimpezaArea){
							salvoObjetivoLimpezaArea = true;
							return salvoObjetivoLimpezaArea;
						}
					}
				}
			} catch (CloneNotSupportedException e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return false;
	}

	public void setSalvoObjetivoLimpezaArea(Boolean salvoObjetivoLimpezaArea) {
		this.salvoObjetivoLimpezaArea = salvoObjetivoLimpezaArea;
	}
    
}
