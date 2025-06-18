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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


@Entity
@Table(name = "cadastro_atividade_nao_sujeita_lic_resp_empreendimento")
public class CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 207993023897063965L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cadastro_atividade_nao_sujeita_lic_resp_empreendimento_seq")    
    @SequenceGenerator(name="cadastro_atividade_nao_sujeita_lic_resp_empreendimento_seq", sequenceName="cadastro_atividade_nao_sujeita_lic_resp_empreendimento_seq", allocationSize=1)
    @Column(name = "ide_cadastro_atividade_nao_sujeita_lic_resp_empreendimento", nullable = false)
	private Integer ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
	
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic;
	
	@JoinColumn(name = "ide_responsavel_empreendimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ResponsavelEmpreendimento ideResponsavelEmpreendimento;
	
	public CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento() {
		// Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento)){
			return false;
		}
		CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento other = (CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento)obj;
		
		return new EqualsBuilder()
			.append(this.ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento, other.getIdeCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento())
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento)
				.toHashCode(); 
	}
	
	public CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento(CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic,
			ResponsavelEmpreendimento ideResponsavelEmpreendimento) {
		super();
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
		this.ideResponsavelEmpreendimento = ideResponsavelEmpreendimento;
	}


	public boolean equalsResponsavelEmpreendimento(ResponsavelEmpreendimento responsavelEmpreendimento){
		return this.ideResponsavelEmpreendimento.equals(responsavelEmpreendimento);
	}


	public Integer getIdeCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento() {
		return ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
	}
	public void setIdeCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento(
			Integer ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento) {
		this.ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento = ideCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
	}
	public CadastroAtividadeNaoSujeitaLic getIdeCadastroAtividadeNaoSujeitaLic() {
		return ideCadastroAtividadeNaoSujeitaLic;
	}
	public void setIdeCadastroAtividadeNaoSujeitaLic(
			CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic) {
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
	}
	public ResponsavelEmpreendimento getIdeResponsavelEmpreendimento() {
		return ideResponsavelEmpreendimento;
	}
	public void setIdeResponsavelEmpreendimento(
			ResponsavelEmpreendimento ideResponsavelEmpreendimento) {
		this.ideResponsavelEmpreendimento = ideResponsavelEmpreendimento;
	}
	
}
