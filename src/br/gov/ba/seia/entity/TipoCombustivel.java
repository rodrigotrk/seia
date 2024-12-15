package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@XmlRootElement
@Table(name="TIPO_COMBUSTIVEL")
public class TipoCombustivel implements Serializable, BaseEntity {
	

	private static final long serialVersionUID = 3013715484039431793L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_combustivel_seq")
	@SequenceGenerator(name = "tipo_combustivel_seq", sequenceName = "tipo_combustivel_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_TIPO_COMBUSTIVEL")
	private Integer ideTipoCombustivel;

	@Basic(optional = false)
    @Size(min = 1, max = 18)
    @Column(name = "DES_TIPO_COMBUSTIVEL")
	private String desTipoCombustivel;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "IND_ATIVO", nullable = false)
    private Boolean indAtivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DTC_CRIACAO")
	private Date dtcCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DTC_EXCLUSAO")
	private Date dtcExclusao;

	
	public Integer getIdeTipoCombustivel() {
		return ideTipoCombustivel;
	}

	public void setIdeTipoCombustivel(Integer ideTipoCombustivel) {
		this.ideTipoCombustivel = ideTipoCombustivel;
	}

	public String getDesTipoCombustivel() {
		return desTipoCombustivel;
	}

	public void setDesTipoCombustivel(String desTipoCombustivel) {
		this.desTipoCombustivel = desTipoCombustivel;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	@Override
	public Long getId() {
		return Long.valueOf(this.getIdeTipoCombustivel());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoCombustivel == null) ? 0 : ideTipoCombustivel
						.hashCode());
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
		TipoCombustivel other = (TipoCombustivel) obj;
		if (ideTipoCombustivel == null) {
			if (other.ideTipoCombustivel != null)
				return false;
		} else if (!ideTipoCombustivel.equals(other.ideTipoCombustivel))
			return false;
		return true;
	}
	
	
}