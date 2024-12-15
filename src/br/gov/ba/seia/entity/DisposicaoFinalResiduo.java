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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "DISPOSICAO_FINAL_RESIDUO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DisposicaoFinalResiduo.findAll", query = "SELECT d FROM DisposicaoFinalResiduo d"),
})
public class DisposicaoFinalResiduo implements Serializable {
    
	private static final long serialVersionUID = 5431163083204459587L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ")    
    @SequenceGenerator(name="DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ", sequenceName="DISPOSICAO_FINAL_RESIDUO_IDE_DISPOSICAO_FINAL_RESIDUO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDE_DISPOSICAO_FINAL_RESIDUO", nullable = false)
    private Integer ideDisposicaoFinalResiduo;
    
	@Basic(optional = false)
    @Column(name = "DTC_CRIACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @Column(name = "IND_EXCLUIDO", nullable = false)
    private boolean indExcluido;
    
	@Column(name = "DES_DISPOSICAO_FINAL_RESIDUO", length = 80)
    private String desDisposicaoFinalResiduo;
    
	@Column(name = "DTC_EXCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	@Transient
	private boolean isChecked;
	
	
	public Integer getIdeDisposicaoFinalResiduo() {
		return ideDisposicaoFinalResiduo;
	}

	public void setIdeDisposicaoFinalResiduo(Integer ideDisposicaoFinalResiduo) {
		this.ideDisposicaoFinalResiduo = ideDisposicaoFinalResiduo;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getDesDisposicaoFinalResiduo() {
		return desDisposicaoFinalResiduo;
	}

	public void setDesDisposicaoFinalResiduo(String desDisposicaoFinalResiduo) {
		this.desDisposicaoFinalResiduo = desDisposicaoFinalResiduo;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
   
	public boolean isOutros() {
		return (desDisposicaoFinalResiduo.contains("Outras"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((desDisposicaoFinalResiduo == null) ? 0
						: desDisposicaoFinalResiduo.hashCode());
		result = prime
				* result
				+ ((ideDisposicaoFinalResiduo == null) ? 0
						: ideDisposicaoFinalResiduo.hashCode());
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
		DisposicaoFinalResiduo other = (DisposicaoFinalResiduo) obj;
		if (desDisposicaoFinalResiduo == null) {
			if (other.desDisposicaoFinalResiduo != null)
				return false;
		} else if (!desDisposicaoFinalResiduo
				.equals(other.desDisposicaoFinalResiduo))
			return false;
		if (ideDisposicaoFinalResiduo == null) {
			if (other.ideDisposicaoFinalResiduo != null)
				return false;
		} else if (!ideDisposicaoFinalResiduo
				.equals(other.ideDisposicaoFinalResiduo))
			return false;
		return true;
	}

}