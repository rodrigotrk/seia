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
@Table(name = "imovel_rural_desbloqueio", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_imovel_rural_desbloqueio" }) })
@XmlRootElement
public class ImovelRuralDesbloqueio {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESBLOQUEIO_IMOVEL_RURAL_seq")
	@SequenceGenerator(name = "DESBLOQUEIO_IMOVEL_RURAL_seq", sequenceName = "DESBLOQUEIO_IMOVEL_RURAL_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_desbloqueio", nullable = false)
	private Integer ideImovelRuralDesbloqueio;

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

	public ImovelRuralDesbloqueio(ImovelRural imovelRural) {
		setIdeImovelRural(imovelRural);
	}

	public ImovelRuralDesbloqueio() {
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

	@Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (getIdeImovelRuralDesbloqueio() != null ? getIdeImovelRuralDesbloqueio().hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        
	        if (!(object instanceof ImovelRuralDesbloqueio)) {
	            return false;
	        }
	        ImovelRuralDesbloqueio other = (ImovelRuralDesbloqueio) object;
	        if ((this.getIdeImovelRuralDesbloqueio() == null && other.getIdeImovelRuralDesbloqueio() != null) || (this.getIdeImovelRuralDesbloqueio() != null && !this.getIdeImovelRuralDesbloqueio().equals(other.getIdeImovelRuralDesbloqueio()))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	    	return String.valueOf(getIdeImovelRuralDesbloqueio());
	    }

		public Integer getIdeImovelRuralDesbloqueio() {
			return ideImovelRuralDesbloqueio;
		}

		public void setIdeImovelRuralDesbloqueio(Integer ideImovelRuralDesbloqueio) {
			this.ideImovelRuralDesbloqueio = ideImovelRuralDesbloqueio;
		}

}
