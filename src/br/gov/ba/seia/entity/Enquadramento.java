package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.util.FileUploadUtil;

/**
 * 
 * @author MJunior
 */
@Entity
@Table(name = "enquadramento", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_requerimento_unico" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Enquadramento.findAll", query = "SELECT e FROM Enquadramento e"),
		@NamedQuery(name = "Enquadramento.findByIdeEnquadramento", query = "SELECT e FROM Enquadramento e WHERE e.ideEnquadramento = :ideEnquadramento"),
		@NamedQuery(name = "Enquadramento.findByIndEnquadramentoAprovado", query = "SELECT e FROM Enquadramento e WHERE e.indEnquadramentoAprovado = :indEnquadramentoAprovado"),
		@NamedQuery(name = "Enquadramento.findByDscJustificativa", query = "SELECT e FROM Enquadramento e WHERE e.dscJustificativa = :dscJustificativa"),
		@NamedQuery(name = "Enquadramento.findByIndPassivelEiarima", query = "SELECT e FROM Enquadramento e WHERE e.indPassivelEiarima = :indPassivelEiarima"),
		@NamedQuery(name = "Enquadramento.findByIdeRequerimento", query = "SELECT e FROM Enquadramento e WHERE e.ideRequerimento.ideRequerimento = :ideRequerimento OR e.ideRequerimentoUnico.ideRequerimentoUnico = :ideRequerimento"),
		@NamedQuery(name = "Enquadramento.findByRequerimentoUnico", query = "SELECT e FROM Enquadramento e join e.ideRequerimentoUnico ru WHERE e.indPassivelEiarima = :indPassivelEiarima") })
public class Enquadramento implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_enquadramento")
	private Integer ideEnquadramento;
	@Column(name = "ind_enquadramento_aprovado")
	private Boolean indEnquadramentoAprovado;
	@Column(name = "dsc_justificativa")
	private String dscJustificativa;
	@Column(name = "ind_passivel_eiarima")
	private Boolean indPassivelEiarima;

	@Column(name = "dsc_caminho_arquivo_rima")
	private String dscCaminhoArquivoRima;
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(optional = false)
	private Requerimento ideRequerimento;

	@OneToMany(mappedBy = "enquadramento")
	private Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection;
	
	@OneToMany(mappedBy = "enquadramento")
	private Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoCollection;
	
	@JoinColumn(name = "ide_requerimento_unico", referencedColumnName = "ide_requerimento_unico")
	@ManyToOne
	private RequerimentoUnico ideRequerimentoUnico;
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa idePessoa;
	@ManyToMany(mappedBy = "enquadramentoCollection", fetch = FetchType.LAZY)
	private Collection<AtoAmbiental> atoAmbientalCollection;
	@ManyToMany(mappedBy = "enquadramentoCollection", fetch = FetchType.LAZY)
	private Collection<DocumentoObrigatorio> documentoObrigatorioCollection;
	
	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento")
	@ManyToOne(fetch = FetchType.EAGER)
	private ProcessoReenquadramento ideProcessoReenquadramento;

	@Transient
	private boolean indInexigibilidadeIsenta;
	
	@Transient
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;

	@Transient
	private Collection<EnquadramentoDocumentoAto> reenquadramentoDocumentoAtoCollection;
	
	//#9326
	@Column(name = "desc_caminho_rel_requerimento_reenq")
	private String descCaminhoRelRequerimentoReenq;
	
	public Enquadramento() {
	}

	public Enquadramento(Integer ideEnquadramento) {
		this.ideEnquadramento = ideEnquadramento;
	}

	public Integer getIdeEnquadramento() {
		return ideEnquadramento;
	}

	public void setIdeEnquadramento(Integer ideEnquadramento) {
		this.ideEnquadramento = ideEnquadramento;
	}

	public Boolean getIndEnquadramentoAprovado() {
		return indEnquadramentoAprovado;
	}

	public void setIndEnquadramentoAprovado(Boolean indEnquadramentoAprovado) {
		this.indEnquadramentoAprovado = indEnquadramentoAprovado;
	}

	public String getDscJustificativa() {
		return dscJustificativa;
	}

	public void setDscJustificativa(String dscJustificativa) {
		this.dscJustificativa = dscJustificativa;
	}

	public Boolean getIndPassivelEiarima() {
		return indPassivelEiarima;
	}

	public void setIndPassivelEiarima(Boolean indPassivelEiarima) {
		this.indPassivelEiarima = indPassivelEiarima;
	}

	public String getDscCaminhoArquivoRima() {
		return dscCaminhoArquivoRima;
	}

	public void setDscCaminhoArquivoRima(String dscCaminhoArquivoRima) {
		this.dscCaminhoArquivoRima = dscCaminhoArquivoRima;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	@XmlTransient
	public Collection<EnquadramentoAtoAmbiental> getEnquadramentoAtoAmbientalCollection() {
		return enquadramentoAtoAmbientalCollection;
	}

	public void setEnquadramentoAtoAmbientalCollection(
			Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection) {
		this.enquadramentoAtoAmbientalCollection = enquadramentoAtoAmbientalCollection;
	}

	public RequerimentoUnico getIdeRequerimentoUnico() {
		return ideRequerimentoUnico;
	}

	public void setIdeRequerimentoUnico(RequerimentoUnico ideRequerimentoUnico) {
		this.ideRequerimentoUnico = ideRequerimentoUnico;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public Collection<AtoAmbiental> getAtoAmbientalCollection() {
		return atoAmbientalCollection;
	}

	public void setAtoAmbientalCollection(Collection<AtoAmbiental> atoAmbientalCollection) {
		this.atoAmbientalCollection = atoAmbientalCollection;
	}

	public Collection<DocumentoObrigatorio> getDocumentoObrigatorioCollection() {
		return documentoObrigatorioCollection;
	}

	public void setDocumentoObrigatorioCollection(Collection<DocumentoObrigatorio> documentoObrigatorioCollection) {
		this.documentoObrigatorioCollection = documentoObrigatorioCollection;
	}

	public Collection<EnquadramentoDocumentoAto> getEnquadramentoDocumentoAtoCollection() {
		return enquadramentoDocumentoAtoCollection;
	}

	public void setEnquadramentoDocumentoAtoCollection(
			Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoCollection) {
		this.enquadramentoDocumentoAtoCollection = enquadramentoDocumentoAtoCollection;
	}

	public String getNomeArquivoRima(){
		return FileUploadUtil.getFileName(dscCaminhoArquivoRima);
	}
	

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideEnquadramento != null ? ideEnquadramento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof Enquadramento)) {
			return false;
		}
		Enquadramento other = (Enquadramento) object;
		if ((this.ideEnquadramento == null && other.ideEnquadramento != null)
				|| (this.ideEnquadramento != null && !this.ideEnquadramento.equals(other.ideEnquadramento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.Enquadramento[ ideEnquadramento=" + ideEnquadramento + " ]";
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public Collection<EnquadramentoDocumentoAto> getReenquadramentoDocumentoAtoCollection() {
		return reenquadramentoDocumentoAtoCollection;
	}

	public void setReenquadramentoDocumentoAtoCollection(
			Collection<EnquadramentoDocumentoAto> reenquadramentoDocumentoAtoCollection) {
		this.reenquadramentoDocumentoAtoCollection = reenquadramentoDocumentoAtoCollection;
	}

	public String getDescCaminhoRelRequerimentoReenq() {
		return descCaminhoRelRequerimentoReenq;
	}

	public void setDescCaminhoRelRequerimentoReenq(String descCaminhoRelRequerimentoReenq) {
		this.descCaminhoRelRequerimentoReenq = descCaminhoRelRequerimentoReenq;
	}
	
	public boolean getIndInexigibilidadeIsenta() {
		return indInexigibilidadeIsenta;
	}

	public void setIndInexigibilidadeIsenta(boolean indInexigibilidadeIsenta) {
		this.indInexigibilidadeIsenta = indInexigibilidadeIsenta;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

}
