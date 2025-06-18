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
@Table(name = "declaracao_transporte_roteiro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteRoteiro.findAll", query = "SELECT d FROM DeclaracaoTransporteRoteiro d"),
})
public class DeclaracaoTransporteRoteiro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7241776037421134988L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_ROTEIRO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_ROTEIRO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_ROTEIRO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_residuo", nullable = false)
    private Integer ideDeclaracaoTransporteResiduo;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@ManyToOne(optional = false)
	private DeclaracaoTransporte declaracaoTransporte;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(optional = false)
	private LocalizacaoGeografica localizacaoGeografica;

	public Integer getIdeDeclaracaoTransporteResiduo() {
		return ideDeclaracaoTransporteResiduo;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setIdeDeclaracaoTransporteResiduo(
			Integer ideDeclaracaoTransporteResiduo) {
		this.ideDeclaracaoTransporteResiduo = ideDeclaracaoTransporteResiduo;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}
}
