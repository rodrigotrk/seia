package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "declaracao_transporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporte.findAll", query = "SELECT d FROM DeclaracaoTransporte d")
})
public class DeclaracaoTransporte implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 9027365187595141319L;
	
	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ", sequenceName="DECLARACAO_TRANSPORTE_IDE_DECLARACAO_TRANSPORTE_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte", nullable = false)
    private Integer ideDeclaracaoTransporte;

	@Column(name = "ind_aceite_responsabilidade", nullable = true)
	private Boolean indAceiteResponsabilidade;
	
	@JoinColumn(name = "ide_ato_declaratorio", referencedColumnName = "ide_ato_declaratorio")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private AtoDeclaratorio atoDeclaratorio;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "declaracaoTransporte", fetch = FetchType.LAZY, orphanRemoval=true)
	private List<DeclaracaoTransporteDisposicaoFinalResiduo> listaDeclaracaoTransporteDisposicaoFinalResiduo;
	
	@OneToOne(mappedBy = "declaracaoTransporte", fetch = FetchType.LAZY)
	private DeclaracaoTransporteGeradorResiduo declaracaoTransporteGeradorResiduo;
	
	@OneToOne(mappedBy = "declaracaoTransporte", fetch = FetchType.LAZY)
	private DeclaracaoTransporteDestinatarioResiduo declaracaoTransporteDestinatarioResiduo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "declaracaoTransporte", fetch = FetchType.LAZY)
	private List<DeclaracaoTransporteResiduo> listaResiduosCadastrados;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "declaracaoTransporte", fetch = FetchType.LAZY)
	private List<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadora;
	
	@Transient
	private List<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeracaoResiduo;
	
	@Transient
	private DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEnderecoDestinacao;
	
	@Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;


	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	@Override
	public Long getId() {
		return new Long(this.ideDeclaracaoTransporte);
	}

	public Integer getIdeDeclaracaoTransporte() {
		return ideDeclaracaoTransporte;
	}

	public Boolean getIndAceiteResponsabilidade() {
		return indAceiteResponsabilidade;
	}

	public void setIdeDeclaracaoTransporte(Integer ideDeclaracaoTransporte) {
		this.ideDeclaracaoTransporte = ideDeclaracaoTransporte;
	}

	public void setIndAceiteResponsabilidade(Boolean indAceiteResponsabilidade) {
		this.indAceiteResponsabilidade = indAceiteResponsabilidade;
	}

	public AtoDeclaratorio getAtoDeclaratorio() {
		return atoDeclaratorio;
	}

	public void setAtoDeclaratorio(AtoDeclaratorio atoDeclaratorio) {
		this.atoDeclaratorio = atoDeclaratorio;
	}

	public DeclaracaoTransporteGeradorResiduo getDeclaracaoTransporteGeradorResiduo() {
		return declaracaoTransporteGeradorResiduo;
	}

	public void setDeclaracaoTransporteGeradorResiduo(
			DeclaracaoTransporteGeradorResiduo declaracaoTransporteGeradorResiduo) {
		this.declaracaoTransporteGeradorResiduo = declaracaoTransporteGeradorResiduo;
	}

	public List<DeclaracaoTransporteResiduoEndereco> getListaEnderecoGeracaoResiduo() {
		return listaEnderecoGeracaoResiduo;
	}

	public DeclaracaoTransporteResiduoEndereco getDeclaracaoTransporteResiduoEnderecoDestinacao() {
		return declaracaoTransporteResiduoEnderecoDestinacao;
	}

	public void setListaEnderecoGeracaoResiduo(
			List<DeclaracaoTransporteResiduoEndereco> listaEnderecoGeracaoResiduo) {
		this.listaEnderecoGeracaoResiduo = listaEnderecoGeracaoResiduo;
	}

	public void setDeclaracaoTransporteResiduoEnderecoDestinacao(
			DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEnderecoDestinacao) {
		this.declaracaoTransporteResiduoEnderecoDestinacao = declaracaoTransporteResiduoEnderecoDestinacao;
	}

	public DeclaracaoTransporteDestinatarioResiduo getDeclaracaoTransporteDestinatarioResiduo() {
		return declaracaoTransporteDestinatarioResiduo;
	}

	public void setDeclaracaoTransporteDestinatarioResiduo(
			DeclaracaoTransporteDestinatarioResiduo declaracaoTransporteDestinatarioResiduo) {
		this.declaracaoTransporteDestinatarioResiduo = declaracaoTransporteDestinatarioResiduo;
	}

	public List<DeclaracaoTransporteEntidadeTransportadora> getListaEntidadeTransportadora() {
		return listaEntidadeTransportadora;
	}

	public void setListaEntidadeTransportadora(
			List<DeclaracaoTransporteEntidadeTransportadora> listaEntidadeTransportadora) {
		this.listaEntidadeTransportadora = listaEntidadeTransportadora;
	}

	public List<DeclaracaoTransporteResiduo> getListaResiduosCadastrados() {
		return listaResiduosCadastrados;
	}

	public void setListaResiduosCadastrados(
			List<DeclaracaoTransporteResiduo> listaResiduosCadastrados) {
		this.listaResiduosCadastrados = listaResiduosCadastrados;
	}

	public List<DeclaracaoTransporteDisposicaoFinalResiduo> getListaDeclaracaoTransporteDisposicaoFinalResiduo() {
		return listaDeclaracaoTransporteDisposicaoFinalResiduo;
	}

	public void setListaDeclaracaoTransporteDisposicaoFinalResiduo(
			List<DeclaracaoTransporteDisposicaoFinalResiduo> listaDeclaracaoTransporteDisposicaoFinalResiduo) {
		this.listaDeclaracaoTransporteDisposicaoFinalResiduo = listaDeclaracaoTransporteDisposicaoFinalResiduo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDeclaracaoTransporte == null) ? 0
						: ideDeclaracaoTransporte.hashCode());
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
		DeclaracaoTransporte other = (DeclaracaoTransporte) obj;
		if (ideDeclaracaoTransporte == null) {
			if (other.ideDeclaracaoTransporte != null)
				return false;
		} else if (!ideDeclaracaoTransporte
				.equals(other.ideDeclaracaoTransporte))
			return false;
		return true;
	}

}
