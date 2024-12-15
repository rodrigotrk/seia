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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "declaracao_transporte_gerador_residuo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteGeradorResiduo.findAll", query = "SELECT d FROM DeclaracaoTransporteGeradorResiduo d"),
})
public class DeclaracaoTransporteGeradorResiduo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8570922910279970306L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_IDE_DECLARACAO_TRANSPORTE_GERADOR_RESIDUO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_gerador_residuo", nullable = false)
    private Integer ideDeclaracaoTransporteGeradorResiduo;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@OneToOne(fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable=false)
	@ManyToOne(optional = false)
	private Pessoa pessoa;

	public Integer getIdeDeclaracaoTransporteGeradorResiduo() {
		return ideDeclaracaoTransporteGeradorResiduo;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setIdeDeclaracaoTransporteGeradorResiduo(
			Integer ideDeclaracaoTransporteGeradorResiduo) {
		this.ideDeclaracaoTransporteGeradorResiduo = ideDeclaracaoTransporteGeradorResiduo;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}
