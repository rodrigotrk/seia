package br.gov.ba.seia.entity;

import java.io.Serializable;

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

@Entity
@Table(name = "endereco_empreendimento_municipio")
public class EnderecoEmpreendimentoMunicipio implements Serializable {
    
	private static final long serialVersionUID = 618882167430423810L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="endereco_empreendimento_municipio_ide_endereco_empreendimento_municipio_seq")    
	@SequenceGenerator(name="endereco_empreendimento_municipio_ide_endereco_empreendimento_municipio_seq", 
		sequenceName="endereco_empreendimento_municipio_ide_endereco_empreendimento_municipio_seq", allocationSize=1)
	@Column(name="ide_endereco_empreendimento_municipio", unique=true, nullable=false)
    private Integer ideEnderecoEmpreendimentoMunicipio;
    
	@JoinColumn(name = "ide_endereco_empreendimento", referencedColumnName = "ide_endereco_empreendimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EnderecoEmpreendimento ideEnderecoEmpreendimento;
    
	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Municipio ideMunicipio;
   
    public EnderecoEmpreendimentoMunicipio() {
    	
    }

	public Integer getIdeEnderecoEmpreendimentoMunicipio() {
		return ideEnderecoEmpreendimentoMunicipio;
	}

	public void setIdeEnderecoEmpreendimentoMunicipio(
			Integer ideEnderecoEmpreendimentoMunicipio) {
		this.ideEnderecoEmpreendimentoMunicipio = ideEnderecoEmpreendimentoMunicipio;
	}

	public EnderecoEmpreendimento getIdeEnderecoEmpreendimento() {
		return ideEnderecoEmpreendimento;
	}

	public void setIdeEnderecoEmpreendimento(
			EnderecoEmpreendimento ideEnderecoEmpreendimento) {
		this.ideEnderecoEmpreendimento = ideEnderecoEmpreendimento;
	}

	public Municipio getIdeMunicipio() {
		return ideMunicipio;
	}

	public void setIdeMunicipio(Municipio ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEnderecoEmpreendimentoMunicipio == null) ? 0
						: ideEnderecoEmpreendimentoMunicipio.hashCode());
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
		EnderecoEmpreendimentoMunicipio other = (EnderecoEmpreendimentoMunicipio) obj;
		if (ideEnderecoEmpreendimentoMunicipio == null) {
			if (other.ideEnderecoEmpreendimentoMunicipio != null)
				return false;
		} else if (!ideEnderecoEmpreendimentoMunicipio
				.equals(other.ideEnderecoEmpreendimentoMunicipio))
			return false;
		return true;
	}
    
   
}