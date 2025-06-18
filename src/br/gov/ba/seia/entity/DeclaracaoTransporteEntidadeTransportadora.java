package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "declaracao_transporte_entidade_transportadora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteEntidadeTransportadora.findAll", query = "SELECT d FROM DeclaracaoTransporteEntidadeTransportadora d"),
})
public class DeclaracaoTransporteEntidadeTransportadora implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1755455781755491298L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ", sequenceName="DECLARACAO_TRANSPORTE_ENTIDADE_TRANSPORTADORA_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_entidade_transportadora", nullable = false)
    private Integer ideDeclaracaoTransporteEntidadeTransportadora;
	
	@Column(name="num_processo_licenciamento")
	private String numProcessoLicenciamento;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa pessoa;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;

	public Integer getIdeDeclaracaoTransporteEntidadeTransportadora() {
		return ideDeclaracaoTransporteEntidadeTransportadora;
	}

	public String getNumProcessoLicenciamento() {
		return numProcessoLicenciamento;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setIdeDeclaracaoTransporteEntidadeTransportadora(
			Integer ideDeclaracaoTransporteEntidadeTransportadora) {
		this.ideDeclaracaoTransporteEntidadeTransportadora = ideDeclaracaoTransporteEntidadeTransportadora;
	}

	public void setNumProcessoLicenciamento(String numProcessoLicenciamento) {
		this.numProcessoLicenciamento = numProcessoLicenciamento;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}
