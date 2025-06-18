package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "objetivo_requerimento_limpeza_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.findAll", query = "SELECT o FROM ObjetivoRequerimentoLimpezaArea o"),
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.updateLocGeo", query = "UPDATE ObjetivoRequerimentoLimpezaArea o SET o.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica where o.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeograficaAdeletar"),
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.deleteByIde", query = "DELETE FROM ObjetivoRequerimentoLimpezaArea o WHERE o.ideObjetivoRequerimentoLimpezaArea = :ideObjetivoRequerimentoLimpezaArea"),
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.deleteByIdeLoc", query = "DELETE FROM ObjetivoRequerimentoLimpezaArea o WHERE o.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.findByIdeRequerimento", query = "SELECT o FROM ObjetivoRequerimentoLimpezaArea o WHERE o.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "ObjetivoRequerimentoLimpezaArea.findByIdeObjetivoRequerimentoLimpezaArea", query = "SELECT o FROM ObjetivoRequerimentoLimpezaArea o WHERE o.ideObjetivoRequerimentoLimpezaArea = :ideObjetivoRequerimentoLimpezaArea")})
public class ObjetivoRequerimentoLimpezaArea implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="objetivo_requerimento_limpeza_area_seq")    
    @SequenceGenerator(name="objetivo_requerimento_limpeza_area_seq", sequenceName="objetivo_requerimento_limpeza_area_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_objetivo_requerimento_limpeza_area")
    private Integer ideObjetivoRequerimentoLimpezaArea;
    @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false,updatable=false)
    @ManyToOne(optional = false)
    @NotNull
    private Requerimento ideRequerimento;
    @JoinColumn(name = "ide_requerimento_imovel", referencedColumnName = "ide_requerimento_imovel", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private RequerimentoImovel ideRequerimentoImovel;
    @JoinColumn(name = "ide_objetivo_limpeza_area", referencedColumnName = "ide_objetivo_limpeza_area", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private ObjetivoLimpezaArea ideObjetivoLimpezaArea;
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @Transient
    private Boolean salvoObjetivoReqLimpezaArea;

    public ObjetivoRequerimentoLimpezaArea() {
    }

    public ObjetivoRequerimentoLimpezaArea(Integer ideObjetivoRequerimentoLimpezaArea) {
        this.ideObjetivoRequerimentoLimpezaArea = ideObjetivoRequerimentoLimpezaArea;
    }

    public Integer getIdeObjetivoRequerimentoLimpezaArea() {
        return ideObjetivoRequerimentoLimpezaArea;
    }

    public void setIdeObjetivoRequerimentoLimpezaArea(Integer ideObjetivoRequerimentoLimpezaArea) {
        this.ideObjetivoRequerimentoLimpezaArea = ideObjetivoRequerimentoLimpezaArea;
    }

    public Requerimento getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(Requerimento ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public RequerimentoImovel getIdeRequerimentoImovel() {
        return ideRequerimentoImovel;
    }

    public void setIdeRequerimentoImovel(RequerimentoImovel ideRequerimentoImovel) {
        this.ideRequerimentoImovel = ideRequerimentoImovel;
    }

    public ObjetivoLimpezaArea getIdeObjetivoLimpezaArea() {
        return ideObjetivoLimpezaArea;
    }

    public void setIdeObjetivoLimpezaArea(ObjetivoLimpezaArea ideObjetivoLimpezaArea) {
        this.ideObjetivoLimpezaArea = ideObjetivoLimpezaArea;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }
    
    public Boolean getSalvoObjetivoReqLimpezaArea(){
		if(Util.isNullOuVazio(ideObjetivoLimpezaArea)){
			salvoObjetivoReqLimpezaArea = false;
			return salvoObjetivoReqLimpezaArea;
		}else{
			List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea;
			try {
				listObjReqLimpArea = Util.clonaListaObjReqLimpArea(ContextoUtil.getContexto().getListaObjetReqLimpArea());
				if(!Util.isNullOuVazio(listObjReqLimpArea)){
					for (ObjetivoRequerimentoLimpezaArea objRqLimArea : listObjReqLimpArea) {
						if(objRqLimArea.getIdeObjetivoRequerimentoLimpezaArea() == ideObjetivoRequerimentoLimpezaArea){
							salvoObjetivoReqLimpezaArea = true;
							return salvoObjetivoReqLimpezaArea;
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

	public void setSalvoObjetivoReqLimpezaArea(Boolean salvoObjetivoLimpezaArea) {
		this.salvoObjetivoReqLimpezaArea = salvoObjetivoLimpezaArea;
	}



	@Override
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }

    @Override
    public String toString() {
        return ""+ideObjetivoRequerimentoLimpezaArea+"";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideObjetivoRequerimentoLimpezaArea == null) ? 0
						: ideObjetivoRequerimentoLimpezaArea.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjetivoRequerimentoLimpezaArea other = (ObjetivoRequerimentoLimpezaArea) obj;
		if (ideObjetivoRequerimentoLimpezaArea == null) {
			if (other.ideObjetivoRequerimentoLimpezaArea != null)
				return false;
		} else if (!ideObjetivoRequerimentoLimpezaArea
				.equals(other.ideObjetivoRequerimentoLimpezaArea))
			return false;
		return true;
	}
    
}
