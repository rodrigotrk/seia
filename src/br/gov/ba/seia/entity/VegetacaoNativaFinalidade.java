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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "vegetacao_nativa_finalidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VegetacaoNativaFinalidade.findAll", query = "SELECT v FROM VegetacaoNativaFinalidade v"),
    @NamedQuery(name = "VegetacaoNativaFinalidade.findByIdeVegetacaoNativaFinalidade", query = "SELECT v FROM VegetacaoNativaFinalidade v WHERE v.ideVegetacaoNativaFinalidade = :ideVegetacaoNativaFinalidade")})
public class VegetacaoNativaFinalidade implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VEGETACAO_NATIVA_FINALIDADE_IDE_VEGETACAO_NATIVA_FINALIDADE_SEQ") 
    @SequenceGenerator(name="VEGETACAO_NATIVA_FINALIDADE_IDE_VEGETACAO_NATIVA_FINALIDADE_SEQ", sequenceName="VEGETACAO_NATIVA_FINALIDADE_IDE_VEGETACAO_NATIVA_FINALIDADE_SEQ", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_vegetacao_nativa_finalidade", nullable = false)
	private Integer ideVegetacaoNativaFinalidade;
    
	@JoinColumn(name = "ide_vegetacao_nativa", referencedColumnName = "ide_vegetacao_nativa")
    @ManyToOne(optional = false)
    private VegetacaoNativa ideVegetacaoNativa;
	
	@JoinColumn(name = "ide_tipo_finalidade_vegetacao_nativa", referencedColumnName = "ide_tipo_finalidade_vegetacao_nativa")
	@OneToOne(optional = false)
	private TipoFinalidadeVegetacaoNativa ideTipoFinalidadeVegetacaoNativa;
	
    public VegetacaoNativaFinalidade() {
    }

    public VegetacaoNativaFinalidade(Integer ideVegetacaoNativaFinalidade) {
        this.ideVegetacaoNativaFinalidade = ideVegetacaoNativaFinalidade;
    }
    
    public VegetacaoNativaFinalidade(TipoFinalidadeVegetacaoNativa ideTipoFinalidadeVegetacaoNativa) {
        this.ideTipoFinalidadeVegetacaoNativa = ideTipoFinalidadeVegetacaoNativa;
    }

    

    public Integer getIdeVegetacaoNativaFinalidade() {
		return ideVegetacaoNativaFinalidade;
	}

	public void setIdeVegetacaoNativaFinalidade(Integer ideVegetacaoNativaFinalidade) {
		this.ideVegetacaoNativaFinalidade = ideVegetacaoNativaFinalidade;
	}

	public VegetacaoNativa getIdeVegetacaoNativa() {
		return ideVegetacaoNativa;
	}

	public void setIdeVegetacaoNativa(VegetacaoNativa ideVegetacaoNativa) {
		this.ideVegetacaoNativa = ideVegetacaoNativa;
	}

	public TipoFinalidadeVegetacaoNativa getIdeTipoFinalidadeVegetacaoNativa() {
		return ideTipoFinalidadeVegetacaoNativa;
	}

	public void setIdeTipoFinalidadeVegetacaoNativa(
			TipoFinalidadeVegetacaoNativa ideTipoFinalidadeVegetacaoNativa) {
		this.ideTipoFinalidadeVegetacaoNativa = ideTipoFinalidadeVegetacaoNativa;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVegetacaoNativaFinalidade != null ? ideVegetacaoNativaFinalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof VegetacaoNativaFinalidade)) {
            return false;
        }
        VegetacaoNativaFinalidade other = (VegetacaoNativaFinalidade) object;
        if ((this.ideVegetacaoNativaFinalidade == null && other.ideVegetacaoNativaFinalidade != null) || (this.ideVegetacaoNativaFinalidade != null && !this.ideVegetacaoNativaFinalidade.equals(other.ideVegetacaoNativaFinalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VegetacaoNativaFinalidade[ ideVegetacaoNativaFinalidade=" + ideVegetacaoNativaFinalidade + " ]";
    }
    
}
