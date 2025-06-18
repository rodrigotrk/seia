package br.gov.ba.seia.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoDocumentoImovelRuralEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "documento_imovel_rural_posse")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DocumentoImovelRuralPosse.findAll", query = "SELECT d FROM DocumentoImovelRuralPosse d"),
	@NamedQuery(name = "DocumentoImovelRuralPosse.findByIdeDocumentoImovelRuralPosse", query = "SELECT d FROM DocumentoImovelRuralPosse d WHERE d.ideDocumentoImovelRuralPosse = :ideDocumentoImovelRuralPosse"),
	@NamedQuery(name = "DocumentoImovelRuralPosse.remove", query = "DELETE FROM DocumentoImovelRuralPosse t WHERE t.ideDocumentoImovelRuralPosse = :ideDocumentoImovelRuralPosse") })
public class DocumentoImovelRuralPosse extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "DOCUMENTO_IMOVEL_RURAL_POSSE_IDE_DOCUMENTO_IMOVEL_RURAL_POSSE_GENERATOR", sequenceName = "DOCUMENTO_IMOVEL_RURAL_POSSE_IDE_DOCUMENTO_IMOVEL_RURAL_POSSE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_IMOVEL_RURAL_POSSE_IDE_DOCUMENTO_IMOVEL_RURAL_POSSE_GENERATOR")
	@Column(name = "ide_documento_imovel_rural_posse", unique = true, nullable = false)
	private Integer ideDocumentoImovelRuralPosse;
	
	@Column(name = "dtc_documento", nullable = true)
	private Date dtcDocumento;
	
	@Column(name = "dsc_emissor_documento", length = 100, nullable = true)
	private String dscEmissorDocumento;
	
	@Column(name = "nom_vendedor", length = 100, nullable = true)
	private String nomVendedor;
	
	@Column(name = "num_cpf_vendedor", length = 11, nullable = true)
	private String numCpfVendedor;
	
	@Column(name = "nom_declarante", length = 100, nullable = true)
	private String nomDeclarante;
	
	@Column(name = "num_cpf_cnpj_declarante", length = 14, nullable = true)
	private String numCpfCnpjDeclarante;
	
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private ImovelRural ideImovelRural;
	
	@JoinColumn(name = "ide_documento_imovel_rural", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentoImovelRural ideDocumentoImovelRural;
	
	@JoinColumn(name = "ide_tipo_documento_imovel_rural", referencedColumnName = "ide_tipo_documento_imovel_rural", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private TipoDocumentoImovelRural ideTipoDocumentoImovelRural;
	
	@JoinColumn(name = "ide_endereco_declarante", referencedColumnName = "ide_endereco",nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Endereco ideEnderecoDeclarante;
    
    @Transient
    private boolean isPjDeclarante;
    
	public DocumentoImovelRuralPosse() {}
	
	public DocumentoImovelRuralPosse(TipoDocumentoImovelRuralEnum tipoDocumentoImovelRuralEnum) {
		this.ideDocumentoImovelRuralPosse = tipoDocumentoImovelRuralEnum.getId();
	}
	
	public DocumentoImovelRuralPosse(Integer ideDocumentoImovelRuralPosse) {
		this.ideDocumentoImovelRuralPosse = ideDocumentoImovelRuralPosse;
	}

	public boolean isPjDeclarante() {
		if(!Util.isNullOuVazio(numCpfCnpjDeclarante) && numCpfCnpjDeclarante.length() > 11) return true;
		else return isPjDeclarante;
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public Integer getIdeDocumentoImovelRuralPosse() {
		return ideDocumentoImovelRuralPosse;
	}

	public void setIdeDocumentoImovelRuralPosse(Integer ideDocumentoImovelRuralPosse) {
		this.ideDocumentoImovelRuralPosse = ideDocumentoImovelRuralPosse;
	}
	
	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public TipoDocumentoImovelRural getIdeTipoDocumentoImovelRural() {
		return ideTipoDocumentoImovelRural;
	}

	public void setIdeTipoDocumentoImovelRural(
			TipoDocumentoImovelRural ideTipoDocumentoImovelRural) {
		this.ideTipoDocumentoImovelRural = ideTipoDocumentoImovelRural;
	}

	public DocumentoImovelRural getIdeDocumentoImovelRural() {
		return ideDocumentoImovelRural;
	}

	public void setIdeDocumentoImovelRural(DocumentoImovelRural ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}

	public Date getDtcDocumento() {
		return dtcDocumento;
	}

	public void setDtcDocumento(Date dtcDocumento) {
		this.dtcDocumento = dtcDocumento;
	}

	public String getDscEmissorDocumento() {
		return dscEmissorDocumento;
	}

	public void setDscEmissorDocumento(String dscEmissorDocumento) {
		this.dscEmissorDocumento = dscEmissorDocumento;
	}

	public String getNomVendedor() {
		return nomVendedor;
	}

	public void setNomVendedor(String nomVendedor) {
		this.nomVendedor = nomVendedor;
	}

	public String getNumCpfVendedor() {
		return numCpfVendedor;
	}

	public void setNumCpfVendedor(String numCpfVendedor) {
		this.numCpfVendedor = numCpfVendedor;
	}

	public String getNomDeclarante() {
		return nomDeclarante;
	}

	public void setNomDeclarante(String nomDeclarante) {
		this.nomDeclarante = nomDeclarante;
	}

	public String getNumCpfCnpjDeclarante() {
		return numCpfCnpjDeclarante;
	}

	public void setNumCpfCnpjDeclarante(String numCpfCnpjDeclarante) {
		this.numCpfCnpjDeclarante = numCpfCnpjDeclarante;
	}

	public Endereco getIdeEnderecoDeclarante() {
		return ideEnderecoDeclarante;
	}

	public void setIdeEnderecoDeclarante(Endereco ideEnderecoDeclarante) {
		this.ideEnderecoDeclarante = ideEnderecoDeclarante;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public void setPjDeclarante(boolean isPjDeclarante) {
		this.isPjDeclarante = isPjDeclarante;
	}
}
