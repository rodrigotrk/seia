package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "erb_equipamento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_erb_equipamento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ErbEquipamento.findAll", query = "SELECT e FROM ErbEquipamento e"),
    @NamedQuery(name = "ErbEquipamento.findByIdeErbEquipamento", query = "SELECT e FROM ErbEquipamento e WHERE e.ideErbEquipamento = :ideErbEquipamento"),
    @NamedQuery(name = "ErbEquipamento.findByVlrPotenciaErp", query = "SELECT e FROM ErbEquipamento e WHERE e.vlrPotenciaErp = :vlrPotenciaErp"),
    @NamedQuery(name = "ErbEquipamento.findByVlrPotenciaLobuloPrincipal", query = "SELECT e FROM ErbEquipamento e WHERE e.vlrPotenciaLobuloPrincipal = :vlrPotenciaLobuloPrincipal"),
    @NamedQuery(name = "ErbEquipamento.findByVlrNivelRadiacao", query = "SELECT e FROM ErbEquipamento e WHERE e.vlrNivelRadiacao = :vlrNivelRadiacao")})
public class ErbEquipamento implements Serializable {
    private static final long serialVersionUID = 1L;
  
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="erb_equipamento_ide_erb_equipamento_seq")    
    @SequenceGenerator(name="erb_equipamento_ide_erb_equipamento_seq", sequenceName="erb_equipamento_ide_erb_equipamento_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_erb_equipamento", nullable = false)
    private Integer ideErbEquipamento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlr_potencia_erp", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlrPotenciaErp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlr_potencia_lobulo_principal", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlrPotenciaLobuloPrincipal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlr_nivel_radiacao", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlrNivelRadiacao;

	@Transient
	private Collection<LacErbEquipamento> lacErbEquipamentos;
    
    public ErbEquipamento() {
    }

    public ErbEquipamento(Integer ideErbEquipamento) {
        this.ideErbEquipamento = ideErbEquipamento;
    }

    public ErbEquipamento(Integer ideErbEquipamento, BigDecimal vlrPotenciaErp, BigDecimal vlrPotenciaLobuloPrincipal, BigDecimal vlrNivelRadiacao) {
        this.ideErbEquipamento = ideErbEquipamento;
        this.vlrPotenciaErp = vlrPotenciaErp;
        this.vlrPotenciaLobuloPrincipal = vlrPotenciaLobuloPrincipal;
        this.vlrNivelRadiacao = vlrNivelRadiacao;
    }

    public ErbEquipamento(Integer ideErbEquipamento, BigDecimal vlrPotenciaErp, BigDecimal vlrPotenciaLobuloPrincipal, BigDecimal vlrNivelRadiacao,
			 Collection<LacErbEquipamento> lacErbEquipamentos) {
		super();
		this.ideErbEquipamento = ideErbEquipamento;
		this.vlrPotenciaErp = vlrPotenciaErp;
		this.vlrPotenciaLobuloPrincipal = vlrPotenciaLobuloPrincipal;
		this.vlrNivelRadiacao = vlrNivelRadiacao;
		this.lacErbEquipamentos = lacErbEquipamentos;
	}

	public Integer getIdeErbEquipamento() {
        return ideErbEquipamento;
    }

    public void setIdeErbEquipamento(Integer ideErbEquipamento) {
        this.ideErbEquipamento = ideErbEquipamento;
    }

    public BigDecimal getVlrPotenciaErp() {
        return vlrPotenciaErp;
    }

    public void setVlrPotenciaErp(BigDecimal vlrPotenciaErp) {
        this.vlrPotenciaErp = vlrPotenciaErp;
    }

    public BigDecimal getVlrPotenciaLobuloPrincipal() {
        return vlrPotenciaLobuloPrincipal;
    }

    public void setVlrPotenciaLobuloPrincipal(BigDecimal vlrPotenciaLobuloPrincipal) {
        this.vlrPotenciaLobuloPrincipal = vlrPotenciaLobuloPrincipal;
    }

    public BigDecimal getVlrNivelRadiacao() {
        return vlrNivelRadiacao;
    }

    public void setVlrNivelRadiacao(BigDecimal vlrNivelRadiacao) {
        this.vlrNivelRadiacao = vlrNivelRadiacao;
    }


    public Collection<LacErbEquipamento> getLacErbEquipamentos() {
		return Util.isNull(lacErbEquipamentos) ? lacErbEquipamentos = new ArrayList<LacErbEquipamento>() : lacErbEquipamentos;
	}

	public void setLacErbEquipamentos(Collection<LacErbEquipamento> lacErbEquipamentos) {
		this.lacErbEquipamentos = lacErbEquipamentos;
	}

	public String getCanais(){
		String formatado = "";
		int index = 0;
		for (LacErbEquipamento lee : this.getLacErbEquipamentos()) {
			if(index == 0){
				formatado +=  lee.getTipoCanalErb().getDscTipoCanalErb();
			}else{
				formatado +=  ","+lee.getTipoCanalErb().getDscTipoCanalErb();				
			}
			index++;
		} 
		return formatado;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideErbEquipamento != null ? ideErbEquipamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ErbEquipamento)) {
            return false;
        }
        ErbEquipamento other = (ErbEquipamento) object;
        if ((this.ideErbEquipamento == null && other.ideErbEquipamento != null) || (this.ideErbEquipamento != null && !this.ideErbEquipamento.equals(other.ideErbEquipamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ErbEquipamento[ ideErbEquipamento=" + ideErbEquipamento + " ]";
    }
    
    @Override
    public ErbEquipamento clone() {
    	return new ErbEquipamento(ideErbEquipamento, vlrPotenciaErp, vlrPotenciaLobuloPrincipal, vlrNivelRadiacao, lacErbEquipamentos);
    }
    
}
