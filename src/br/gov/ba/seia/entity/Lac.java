package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;


/**
 * @author luis
 * 
 */
@Entity
@Table(name = "lac")
@Inheritance(strategy = InheritanceType.JOINED)
public class Lac implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lac_seq")
	@SequenceGenerator(name = "lac_seq", sequenceName = "lac_ide_lac_seq", allocationSize = 1)
	@Column(name = "ide_lac")
	protected Integer ideLac;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	protected Requerimento ideRequerimento;

	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	protected DocumentoObrigatorio ideDocumentoObrigatorio;

	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dtcCriacao;

	@Transient
	protected Certificado certificado;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lac")
	protected Collection<ArquivoLac> arquivoLacCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lac", fetch = FetchType.LAZY)
	protected Collection<LacLegislacao> lacLegislacaoCollection;

	public Lac() {
	}

	public Lac(Integer ideLac) {
		super();
		this.ideLac = ideLac;
	}

	/**
	 * @param requerimento
	 */
	public Lac(Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorioEnum) {
		this.ideRequerimento = requerimento;
		this.dtcCriacao = new Date();
		this.ideDocumentoObrigatorio = new DocumentoObrigatorio(documentoObrigatorioEnum.getId());	
	}
	
	public Lac(Integer ideLac, Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorioEnum) {
		this.ideLac = ideLac;
		this.ideRequerimento = requerimento;
		this.dtcCriacao = new Date();
		this.ideDocumentoObrigatorio = new DocumentoObrigatorio(documentoObrigatorioEnum.getId());	
	}

	public Integer getIdeLac() {
		return ideLac;
	}

	public void setIdeLac(Integer ideLac) {
		this.ideLac = ideLac;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Collection<LacLegislacao> getLacLegislacaoCollection() {
		return lacLegislacaoCollection;
	}

	public void setLacLegislacaoCollection(Collection<LacLegislacao> lacLegislacaoCollection) {
		this.lacLegislacaoCollection = lacLegislacaoCollection;
	}

	public Collection<ArquivoLac> getArquivoLacCollection() {
		return arquivoLacCollection;
	}

	public void setArquivoLacCollection(Collection<ArquivoLac> arquivoLacCollection) {
		this.arquivoLacCollection = arquivoLacCollection;
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}
	
	public boolean isPosto() {
		return new DocumentoObrigatorio(DocumentoObrigatorioEnum.POSTO.getId()).equals(ideDocumentoObrigatorio);		
	}
	
	public boolean isErb() {
		return new DocumentoObrigatorio(DocumentoObrigatorioEnum.ERB.getId()).equals(ideDocumentoObrigatorio);				
	}
	
	public boolean isTransportadora() {
		return new DocumentoObrigatorio(DocumentoObrigatorioEnum.TRANSPORTADORA.getId()).equals(ideDocumentoObrigatorio);				
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideLac == null) ? 0 : ideLac.hashCode());
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
		Lac other = (Lac) obj;
		if (ideLac == null) {
			if (other.ideLac != null)
				return false;
		} else if (!ideLac.equals(other.ideLac))
			return false;
		return true;
	}

}
