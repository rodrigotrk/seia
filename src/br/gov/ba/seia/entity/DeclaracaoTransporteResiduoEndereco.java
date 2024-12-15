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
@Table(name = "declaracao_transporte_residuo_endereco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteResiduoEndereco.findAll", query = "SELECT d FROM DeclaracaoTransporteResiduoEndereco d"),
})
public class DeclaracaoTransporteResiduoEndereco implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 729357231027799425L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_RESIDUO_ENDERECO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_residuo_endereco", nullable = false)
    private Integer ideDeclaracaoTransporteResiduo;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco", nullable=true)
	@ManyToOne(optional = false)
	private Endereco endereco;
	
	@JoinColumn(name = "ide_tipo_endereco", referencedColumnName = "ide_tipo_endereco", nullable=true)
	@ManyToOne(optional = false)
	private TipoEndereco tipoEndereco;
	
	@Column(name="ind_possui_licenca_autorizacao", nullable=true)
	private Boolean indPossuiLicencaAutorizacao;
	
	@Column(name="num_processo_licenca_autorizacao", nullable=true)
	private String numProcessoLicencaAutorizacao;

	public Integer getIdeDeclaracaoTransporteResiduo() {
		return ideDeclaracaoTransporteResiduo;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	public Boolean getIndPossuiLicencaAutorizacao() {
		return indPossuiLicencaAutorizacao;
	}

	public String getNumProcessoLicencaAutorizacao() {
		return numProcessoLicencaAutorizacao;
	}

	public void setIdeDeclaracaoTransporteResiduo(
			Integer ideDeclaracaoTransporteResiduo) {
		this.ideDeclaracaoTransporteResiduo = ideDeclaracaoTransporteResiduo;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public void setIndPossuiLicencaAutorizacao(Boolean indPossuiLicencaAutorizacao) {
		this.indPossuiLicencaAutorizacao = indPossuiLicencaAutorizacao;
	}

	public void setNumProcessoLicencaAutorizacao(
			String numProcessoLicencaAutorizacao) {
		this.numProcessoLicencaAutorizacao = numProcessoLicencaAutorizacao;
	}
	
	public String getPossuiLicencaAutorizacao() {
		String resposta = "";
		
		if(getIndPossuiLicencaAutorizacao() != null && getIndPossuiLicencaAutorizacao()) {
			resposta = "Sim";
		}else{
			resposta = "Não";
		}
		
		return resposta;
	}
}
