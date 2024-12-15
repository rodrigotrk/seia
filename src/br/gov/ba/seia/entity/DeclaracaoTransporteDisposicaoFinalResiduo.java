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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "declaracao_transporte_disposicao_final_residuo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteDisposicaoFinalResiduo.findAll", query = "SELECT d FROM DeclaracaoTransporteDisposicaoFinalResiduo d"),
})
public class DeclaracaoTransporteDisposicaoFinalResiduo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3733235906839319236L;
	
	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_DISPOSICAO_FINAL_RESIDUO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_disposicao_final_residuo", nullable = false)
    private Integer ideDeclaracaoTransporteDisposicaoFinalResiduo;
	
	@JoinColumn(name = "IDE_DISPOSICAO_FINAL_RESIDUO", referencedColumnName = "IDE_DISPOSICAO_FINAL_RESIDUO")
	@OneToOne(fetch = FetchType.LAZY)
	private DisposicaoFinalResiduo disposicaoFinalResiduo;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@OneToOne(fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;

	public Integer getIdeDeclaracaoTransporteDisposicaoFinalResiduo() {
		return ideDeclaracaoTransporteDisposicaoFinalResiduo;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setIdeDeclaracaoTransporteDisposicaoFinalResiduo(
			Integer ideDeclaracaoTransporteDisposicaoFinalResiduo) {
		this.ideDeclaracaoTransporteDisposicaoFinalResiduo = ideDeclaracaoTransporteDisposicaoFinalResiduo;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public DisposicaoFinalResiduo getDisposicaoFinalResiduo() {
		return disposicaoFinalResiduo;
	}

	public void setDisposicaoFinalResiduo(
			DisposicaoFinalResiduo disposicaoFinalResiduo) {
		this.disposicaoFinalResiduo = disposicaoFinalResiduo;
	}

}
