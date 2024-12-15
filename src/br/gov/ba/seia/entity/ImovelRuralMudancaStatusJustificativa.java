package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "imovel_rural_mudanca_status_justificativa", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_status_justificativa" }) })
@XmlRootElement
public class ImovelRuralMudancaStatusJustificativa {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_JUSTIFICATIVA_seq")
	@SequenceGenerator(name = "STATUS_JUSTIFICATIVA_seq", sequenceName = "MUDANCA_STATUS_JUSTIFICATIVA_IDE_STATUS_JUSTIFICATIVA_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_status_justificativa", nullable = false)
	private Integer ideImovelRuralMudancaStatusJustificativa;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private ImovelRural ideImovelRural;

	@JoinColumn(name = "ide_documento_solicitacao", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentoImovelRural ideDocumentoSolicitacao;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ide_usuario")
	private Usuario ideUsuario;

	@Column(name = "observacao", length = 2000)
	private String observacao;

	@Column(name = "dtc_justificativa", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcJustificativa;
	
	@Column(name = "ind_alterar_proprietario", nullable = true)
	private boolean indAlterarProprietario;
	
	@JoinColumn(name = "ide_proprietario", referencedColumnName = "ide_pessoa", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa ideProprietario;

	public ImovelRuralMudancaStatusJustificativa(ImovelRural imovelRural) {
		setIdeImovelRural(imovelRural);
	}

	public ImovelRuralMudancaStatusJustificativa() {
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public DocumentoImovelRural getIdeDocumentoSolicitacao() {
		return ideDocumentoSolicitacao;
	}

	public void setIdeDocumentoSolicitacao(DocumentoImovelRural ideDocumentoSolicitacao) {
		this.ideDocumentoSolicitacao = ideDocumentoSolicitacao;
	}

	public Usuario getIdeUsuario() {
		return ideUsuario;
	}

	public void setIdeUsuario(Usuario ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDtcJustificativa() {
		return dtcJustificativa;
	}

	public void setDtcJustificativa(Date dtcJustificativa) {
		this.dtcJustificativa = dtcJustificativa;
	}
	
	 public boolean isIndAlterarProprietario() {
		return indAlterarProprietario;
	}

	public void setIndAlterarProprietario(boolean indAlterarProprietario) {
		this.indAlterarProprietario = indAlterarProprietario;
	}

	public Pessoa getIdeProprietario() {
		return ideProprietario;
	}

	public void setIdeProprietario(Pessoa ideProprietario) {
		this.ideProprietario = ideProprietario;
	}

	@Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (getIdeImovelRuralMudancaStatusJustificativa() != null ? getIdeImovelRuralMudancaStatusJustificativa().hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        
	        if (!(object instanceof ImovelRuralMudancaStatusJustificativa)) {
	            return false;
	        }
	        ImovelRuralMudancaStatusJustificativa other = (ImovelRuralMudancaStatusJustificativa) object;
	        if ((this.getIdeImovelRuralMudancaStatusJustificativa() == null && other.getIdeImovelRuralMudancaStatusJustificativa() != null) || (this.getIdeImovelRuralMudancaStatusJustificativa() != null && !this.getIdeImovelRuralMudancaStatusJustificativa().equals(other.getIdeImovelRuralMudancaStatusJustificativa()))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	    	return String.valueOf(getIdeImovelRuralMudancaStatusJustificativa());
	    }

		public Integer getIdeImovelRuralMudancaStatusJustificativa() {
			return ideImovelRuralMudancaStatusJustificativa;
		}

		public void setIdeImovelRuralMudancaStatusJustificativa(Integer ideImovelRuralMudancaStatusJustificativa) {
			this.ideImovelRuralMudancaStatusJustificativa = ideImovelRuralMudancaStatusJustificativa;
		}

}
