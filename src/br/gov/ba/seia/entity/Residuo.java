package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;
/**
 * 		
 * @author eduardo.fernandes
 *
 */
@Entity
@Table(name = "residuo")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Residuo.findAll", query = "SELECT r FROM Residuo r"),
		@NamedQuery(name = "Residuo.findByIdeResiduo", query = "SELECT r FROM Residuo r WHERE r.ideResiduo = :ideResiduo"),
		@NamedQuery(name = "Residuo.findByCodResiduo", query = "SELECT r FROM Residuo r WHERE r.codResiduo = :codResiduo and r.indAtivo = true"),
		@NamedQuery(name = "Residuo.findByNomeResiduo", query = "SELECT r FROM Residuo r WHERE r.nomResiduo = :nomResiduo and r.indAtivo = true"),
		@NamedQuery(name = "Residuo.findByComposicaoResiduo", query = "SELECT r FROM Residuo r WHERE r.dscComposicaoResiduo = :dscComposicaoResiduo and r.indAtivo = true"),
		@NamedQuery(name = "Residuo.findByIndAtivo", query = "SELECT r FROM Residuo r WHERE r.indAtivo = :indAtivo") })
public class Residuo implements Serializable, BaseEntity,Comparable<Residuo> {
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="residuo_ide_residuo_seq")    
    @SequenceGenerator(name="residuo_ide_residuo_seq", sequenceName="residuo_ide_residuo_seq", allocationSize=1)
	@NotNull
	@Column(name = "ide_residuo")
	private Integer ideResiduo;
	@Basic(optional = false)
	@Column(name = "cod_residuo", length = 20)
	private String codResiduo;
	@Basic(optional = false)
	@Column(name = "nom_residuo", length = 1000)
	private String nomResiduo;
	@Basic(optional = false)
	@Column(name = "dsc_composicao_residuo", length = 1000)
	private String dscComposicaoResiduo;
	@Basic(optional = false)
	@Column(name = "dsc_periculosidade", length = 100)
	private String dscPericulosidade;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_ativo")
    private boolean indAtivo;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
	@Transient
	private Double qtdTransporteAnual;
	@Transient
	private boolean desabilitaQtd;
	@Transient
	private boolean outro;
	
	public Residuo() {
	}
	
	public Residuo(String string) {
		this.nomResiduo = string;
	}
	
	public Residuo(Integer ideResiduo) {
		this.ideResiduo = ideResiduo;
	}

	public Integer getIdeResiduo() {
		return ideResiduo;
	}
	public void setIdeResiduo(Integer ideResiduo) {
		this.ideResiduo = ideResiduo;
	}
	public String getCodResiduo() {
		return codResiduo;
	}
	public void setCodResiduo(String codResiduo) {
		this.codResiduo = codResiduo;
	}
	public String getNomResiduo() {
		return nomResiduo;
	}
	public void setNomResiduo(String nomResiduo) {
		this.nomResiduo = nomResiduo;
	}
	public String getDscComposicaoResiduo() {
		return dscComposicaoResiduo;
	}
	public void setDscComposicaoResiduo(String dscComposicaoResiduo) {
		this.dscComposicaoResiduo = dscComposicaoResiduo;
	}
	public String getDscPericulosidade() {
		return dscPericulosidade;
	}
	public void setDscPericulosidade(String dscPericulosidade) {
		this.dscPericulosidade = dscPericulosidade;
	}
	public boolean isIndAtivo() {
		return indAtivo;
	}
	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideResiduo == null) ? 0 : ideResiduo.hashCode());
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
		Residuo other = (Residuo) obj;
		if (ideResiduo == null) {
			if (other.ideResiduo != null)
				return false;
		} else if (!ideResiduo.equals(other.ideResiduo))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Residuo o) {
		if(Util.isNullOuVazio(o.getCodResiduo())){
			return 0;
		} else {
			return this.getCodResiduo().compareTo(o.getCodResiduo());
		}
	}

	@Override
	public String toString() {
		return "Residuo [ideResiduo=" + ideResiduo + "]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideResiduo);
	}

	public boolean isDesabilitaQtd() {
		return desabilitaQtd;
	}

	public void setDesabilitaQtd(boolean desabilitaQtd) {
		if(desabilitaQtd && Util.isNullOuVazio(getQtdTransporteAnual())){
			JsfUtil.addWarnMessage("A quantidade média anual do resíduo transportado deve ser maior que zero.");
		} else {
			this.desabilitaQtd = desabilitaQtd;
		}
	}

	public boolean isOutro() {
		return outro;
	}

	public void setOutro(boolean outro) {
		this.outro = outro;
	}

	public Double getQtdTransporteAnual() {
		return qtdTransporteAnual;
	}

	public void setQtdTransporteAnual(Double qtdTransporteAnual) {
		this.qtdTransporteAnual = qtdTransporteAnual;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

}
