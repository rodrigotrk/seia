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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "declaracao_transporte_destinatario_residuo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteDestinatarioResiduo.findAll", query = "SELECT d FROM DeclaracaoTransporteDestinatarioResiduo d"),
})
public class DeclaracaoTransporteDestinatarioResiduo implements Serializable {
	
	private static final long serialVersionUID = -6317429687797077391L;
	
	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_DESTINATARIO_RESIDUO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_destinatario_residuo", nullable = false)
    private Integer ideDeclaracaoTransporteDestinatarioResiduo;
	
	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica")
	@ManyToOne(optional = false)
	private PessoaJuridica pessoaJuridica;

	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@ManyToOne(optional = false)
	private DeclaracaoTransporte declaracaoTransporte;
	

	public Integer getIdeDeclaracaoTransporteDestinatarioResiduo() {
		return ideDeclaracaoTransporteDestinatarioResiduo;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setIdeDeclaracaoTransporteDestinatarioResiduo(
			Integer ideDeclaracaoTransporteDestinatarioResiduo) {
		this.ideDeclaracaoTransporteDestinatarioResiduo = ideDeclaracaoTransporteDestinatarioResiduo;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}
	
}