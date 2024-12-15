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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@XmlRootElement
@Table(name="FINALIDADE_GERACAO_ENERGIA")
public class FinalidadeGeracaoEnergia implements Serializable, BaseEntity{
	

	private static final long serialVersionUID = -135464552436758006L;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finalidade_geracao_energia_seq")
	@SequenceGenerator(name = "finalidade_geracao_energia_seq", sequenceName = "finalidade_geracao_energia_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FINALIDADE_GERACAO_ENERGIA")
	private Integer ideFinalidadeGeracaoEnergia;

	@Basic(optional = false)
	@NotNull
    @Column(name = "DES_FINALIDADE_GERACAO_ENERGIA")
	private String desFinalidadeGeracaoEnergia;
	
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

	public Integer getIdeFinalidadeGeracaoEnergia() {
		return ideFinalidadeGeracaoEnergia;
	}

	public void setIdeFinalidadeGeracaoEnergia(Integer ideFinalidadeGeracaoEnergia) {
		this.ideFinalidadeGeracaoEnergia = ideFinalidadeGeracaoEnergia;
	}

	public String getDesFinalidadeGeracaoEnergia() {
		return desFinalidadeGeracaoEnergia;
	}

	public void setDesFinalidadeGeracaoEnergia(String desFinalidadeGeracaoEnergia) {
		this.desFinalidadeGeracaoEnergia = desFinalidadeGeracaoEnergia;
	}

	public boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
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

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return Long.valueOf(this.ideFinalidadeGeracaoEnergia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFinalidadeGeracaoEnergia == null) ? 0
						: ideFinalidadeGeracaoEnergia.hashCode());
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
		FinalidadeGeracaoEnergia other = (FinalidadeGeracaoEnergia) obj;
		if (ideFinalidadeGeracaoEnergia == null) {
			if (other.ideFinalidadeGeracaoEnergia != null)
				return false;
		} else if (!ideFinalidadeGeracaoEnergia
				.equals(other.ideFinalidadeGeracaoEnergia))
			return false;
		return true;
	}
		
}