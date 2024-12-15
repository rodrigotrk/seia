package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="portaria")
public class Portaria implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="portaria_seq")    
	@SequenceGenerator(name="portaria_seq", sequenceName="portaria_seq", allocationSize=1)
	@Column(name="ide_portaria", unique=true, nullable=false)
	private Integer idePortaria;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_portaria", nullable=false)
	private Date dtcPortaria;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_publicacao_portaria", nullable=false)
	private Date dtcPublicacaoPortaria;

	@Length(max =50, message = "O campo 'N° Portaria' permite somente 50 carcteres.")
	@Column(name="num_portaria")
	private String numPortaria;
	
	@Column(name="texto_portaria")
	private String textoPortaria;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Funcionario idePessoaFisica;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;

	@JoinColumn(name = "ide_tipo_portaria", referencedColumnName = "ide_tipo_portaria", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private TipoPortaria ideTipoPortaria;

	@Transient
	private boolean textoObrigatorio;
	
	public Portaria() {}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idePortaria == null) ? 0 : idePortaria.hashCode());
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
		Portaria other = (Portaria) obj;
		if (idePortaria == null) {
			if (other.idePortaria != null)
				return false;
		} else if (!idePortaria.equals(other.idePortaria))
			return false;
		return true;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/

	public Integer getIdePortaria() {
		return idePortaria;
	}

	public void setIdePortaria(Integer idePortaria) {
		this.idePortaria = idePortaria;
	}

	public Date getDtcPortaria() {
		return dtcPortaria;
	}

	public void setDtcPortaria(Date dtcPortaria) {
		this.dtcPortaria = dtcPortaria;
	}

	public Date getDtcPublicacaoPortaria() {
		return dtcPublicacaoPortaria;
	}

	public void setDtcPublicacaoPortaria(Date dtcPublicacaoPortaria) {
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
	}

	public String getNumPortaria() {
		return numPortaria;
	}

	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
	}

	public String getTextoPortaria() {
		return textoPortaria;
	}

	public void setTextoPortaria(String textoPortaria) {
		this.textoPortaria = textoPortaria;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public TipoPortaria getIdeTipoPortaria() {
		return ideTipoPortaria;
	}

	public void setIdeTipoPortaria(TipoPortaria ideTipoPortaria) {
		this.ideTipoPortaria = ideTipoPortaria;
	}

	public boolean isTextoObrigatorio() {
		return textoObrigatorio;
	}

	public void setTextoObrigatorio(boolean textoObrigatorio) {
		this.textoObrigatorio = textoObrigatorio;
	}
}
