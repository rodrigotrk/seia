package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.LazyInitializationException;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "endereco")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Endereco.findAll", query = "SELECT e FROM Endereco e"),
		@NamedQuery(name = "Endereco.findByIdeEndereco", query = "SELECT e FROM Endereco e WHERE e.ideEndereco = :ideEndereco"),
		@NamedQuery(name = "Endereco.findByNumEndereco", query = "SELECT e FROM Endereco e WHERE e.numEndereco = :numEndereco"),
		@NamedQuery(name = "Endereco.findByDtcCriacao", query = "SELECT e FROM Endereco e WHERE e.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "Endereco.findByIndExcluido", query = "SELECT e FROM Endereco e WHERE e.indExcluido = :indExcluido"),
		@NamedQuery(name = "Endereco.findByDtcExclusao", query = "SELECT e FROM Endereco e WHERE e.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "Endereco.findByDesComplemento", query = "SELECT e FROM Endereco e WHERE e.desComplemento = :desComplemento"),
		@NamedQuery(name = "Endereco.findByDesPontoReferencia", query = "SELECT e FROM Endereco e WHERE e.desPontoReferencia = :desPontoReferencia") })
public class Endereco extends AbstractEntity implements Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENDERECO_IDE_ENDERECO_seq")
	@SequenceGenerator(name = "ENDERECO_IDE_ENDERECO_seq", sequenceName = "ENDERECO_IDE_ENDERECO_seq", allocationSize = 1)
	@Column(name = "ide_endereco", nullable = false)
	private Integer ideEndereco;

	@Column(name = "num_endereco", length = 5)
	private String numEndereco;

	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtcCriacao;

	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.DATE)
	private Date dtcExclusao;

	@Column(name = "des_complemento", length = 250)
	private String desComplemento;

	@Column(name = "des_ponto_referencia", length = 250)
	private String desPontoReferencia;

	@JoinColumn(name = "ide_logradouro", referencedColumnName = "ide_logradouro")
	@ManyToOne(fetch = FetchType.LAZY)
	private Logradouro ideLogradouro;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEndereco", fetch = FetchType.LAZY)
	private Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEndereco", fetch = FetchType.LAZY)
	private Collection<EnderecoPessoa> enderecoPessoaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEndereco")
	private Collection<Imovel> imovelCollection;

	@OneToMany(mappedBy = "ideEnderecoContato", fetch = FetchType.LAZY)
	private Collection<Requerimento> requerimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEnderecoDeclarante", fetch = FetchType.LAZY)
	private Collection<DocumentoImovelRuralPosse> documentoImovelRuralPosseCollection;

	public Endereco() {}

	public Endereco(Integer ideEndereco) {
		this.ideEndereco = ideEndereco;
	}

	public Endereco(Logradouro ideLogradouro) {
		this.ideLogradouro = ideLogradouro;
	}

	public Endereco(Integer ideEndereco, Date dtcCriacao, boolean indExcluido) {
		this.ideEndereco = ideEndereco;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}

	@Transient
	public String getEnderecoFormatado() {
		StringBuilder sb = new StringBuilder();

		if (this.getIdeLogradouro() != null && this.getIdeLogradouro().getIdeTipoLogradouro() != null) {
			sb.append(this.getIdeLogradouro().getIdeTipoLogradouro().getNomTipoLogradouro());
		}

		sb.append(this.getIdeLogradouro().getNomLogradouro() + ", ");

		if (this.getNumEndereco() != null) {
			sb.append("Nº " + this.getNumEndereco() + ", ");
		}
		sb.append(this.getIdeLogradouro().getIdeBairro().getNomBairro());

		if (!Util.isNullOuVazio( this.getDesComplemento())) {
			sb.append(", "+this.getDesComplemento());
		}

		return sb.toString();
	}

	@Transient
	public String getEnderecoBasicoFormatado() {
		StringBuilder sb = new StringBuilder();

		if (this.getIdeLogradouro() != null && this.getIdeLogradouro().getIdeTipoLogradouro() != null) {
			sb.append(this.getIdeLogradouro().getIdeTipoLogradouro().getNomTipoLogradouro() + " ");
		}

		sb.append(this.getIdeLogradouro().getNomLogradouro() + ", ");

		if (this.getNumEndereco() != null) {
			sb.append("Nº " + this.getNumEndereco());
		}

		if (!Util.isNullOuVazio( this.getDesComplemento())) {
			sb.append(", "+this.getDesComplemento());
		}

		return sb.toString();
	}

	@JSON(include=false)
	@Transient
	public String getEnderecoCompleto() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getIdeLogradouro().getNomLogradouro() + ", ");
		if (this.getNumEndereco() != null) {
			sb.append("Nº " + this.getNumEndereco() + ", ");
		}
		sb.append(this.getIdeLogradouro().getIdeBairro().getNomBairro() + ", ");
		if (this.getDesComplemento() != null && !this.getDesComplemento().isEmpty()) {
			sb.append(this.getDesComplemento() + ", ");
		}

		try{
			sb.append(this.getIdeLogradouro().getIdeBairro().getIdeMunicipio().getNomMunicipio() + ", ");
			sb.append(this.getIdeLogradouro().getIdeBairro().getIdeMunicipio().getIdeEstado().getDesSigla() + ". ");
		}catch(LazyInitializationException e){
		}

		if (this.getDesPontoReferencia() != null) {
			sb.append(this.getDesPontoReferencia() + ". ");
		}
		sb.append("CEP " + this.getIdeLogradouro().getNumCepFormatado() + ".");
		return sb.toString();
	}

	@JSON(include=false)
	@Transient
	public String getEnderecoCompletoSemPais() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getIdeLogradouro().getNomLogradouro() + ", ");
		if (this.getNumEndereco() != null && !this.getNumEndereco().isEmpty()) {
			sb.append("nº " + this.getNumEndereco() + ", ");
		}
		sb.append(this.getIdeLogradouro().getIdeBairro().getNomBairro() + ", ");
		if (this.getDesComplemento() != null && !this.getDesComplemento().isEmpty()) {
			sb.append(this.getDesComplemento() + ", ");
		}

		try{
			sb.append(this.getIdeLogradouro().getIdeBairro().getIdeMunicipio().getNomMunicipio() + ", ");
			sb.append(this.getIdeLogradouro().getIdeBairro().getIdeMunicipio().getIdeEstado().getDesSigla());
		}catch(LazyInitializationException e){
		}

		return sb.toString();
	}

	public Integer getIdeEndereco() {
		return ideEndereco;
	}

	public void setIdeEndereco(Integer ideEndereco) {
		this.ideEndereco = ideEndereco;
	}

	public String getNumEndereco() {
		return numEndereco;
	}

	public void setNumEndereco(String numEndereco) {
		this.numEndereco = numEndereco;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public String getDesComplemento() {
		return desComplemento;
	}

	public void setDesComplemento(String desComplemento) {
		this.desComplemento = desComplemento;
	}

	public String getDesPontoReferencia() {
		return desPontoReferencia;
	}

	public void setDesPontoReferencia(String desPontoReferencia) {
		this.desPontoReferencia = desPontoReferencia;
	}

	@XmlTransient
	public Collection<EnderecoEmpreendimento> getEnderecoEmpreendimentoCollection() {
		return enderecoEmpreendimentoCollection;
	}

	public void setEnderecoEmpreendimentoCollection(Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection) {
		this.enderecoEmpreendimentoCollection = enderecoEmpreendimentoCollection;
	}

	@XmlTransient
	public Collection<EnderecoPessoa> getEnderecoPessoaCollection() {
		return enderecoPessoaCollection;
	}

	public void setEnderecoPessoaCollection(Collection<EnderecoPessoa> enderecoPessoaCollection) {
		this.enderecoPessoaCollection = enderecoPessoaCollection;
	}

	public Logradouro getIdeLogradouro() {
		return ideLogradouro;
	}

	public void setIdeLogradouro(Logradouro ideLogradouro) {
		this.ideLogradouro = ideLogradouro;
	}

	@XmlTransient
	public Collection<Imovel> getImovelCollection() {
		return imovelCollection;
	}

	public void setImovelCollection(Collection<Imovel> imovelCollection) {
		this.imovelCollection = imovelCollection;
	}

	@XmlTransient
	public Collection<Requerimento> getRequerimentoCollection() {
		return requerimentoCollection;
	}

	public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
		this.requerimentoCollection = requerimentoCollection;
	}

	@JSON(include=false)
	public Collection<DocumentoImovelRuralPosse> getDocumentoImovelRuralPosseCollection() {
		return documentoImovelRuralPosseCollection;
	}

	public void setDocumentoImovelRuralPosseCollection(
			Collection<DocumentoImovelRuralPosse> documentoImovelRuralPosseCollection) {
		this.documentoImovelRuralPosseCollection = documentoImovelRuralPosseCollection;
	}

	@Override
	public Endereco clone() throws CloneNotSupportedException {
		return (Endereco) super.clone();
	}
}