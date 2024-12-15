package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "reserva_legal_averbada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReservaLegalAverbada.findAll", query = "SELECT r FROM ReservaLegalAverbada r"),
    @NamedQuery(name = "ReservaLegalAverbada.findByIdeReservaLegalAverbada", query = "SELECT r FROM ReservaLegalAverbada r WHERE r.ideReservaLegalAverbada = :ideReservaLegalAverbada")})
public class ReservaLegalAverbada implements Serializable {
	private static final long serialVersionUID = -2293375622823621374L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESERVA_LEGAL_AVERBADA_IDE_RESERVA_LEGAL_AVERBADA_seq")    
    @SequenceGenerator(name="RESERVA_LEGAL_AVERBADA_IDE_RESERVA_LEGAL_AVERBADA_seq", sequenceName="RESERVA_LEGAL_AVERBADA_IDE_RESERVA_LEGAL_AVERBADA_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reserva_legal_averbada", nullable = false)
    private Integer ideReservaLegalAverbada;
    
    @JoinColumn(name = "ide_reserva_legal", referencedColumnName = "ide_reserva_legal", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ReservaLegal ideReservaLegal;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_averbacao", nullable = false, length = 30)
    private String numAverbacao;

    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 30)
    @Column(name = "num_matricula", nullable = false, length = 30)
    private String numMatricula;
    
    @JoinColumn(name = "ide_documento_averbacao", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL, optional=true)
	private DocumentoImovelRural ideDocumentoAverbacao;
    
    @Basic(optional = false)
	@Column(name = "dtc_averbacao", nullable = true)
	private Date dtcAverbacao;

    
    public DocumentoImovelRural getIdeDocumentoAverbacao() {
		return ideDocumentoAverbacao;
	}

	public void setIdeDocumentoAverbacao(DocumentoImovelRural ideDocumentoAverbacao) {
		this.ideDocumentoAverbacao = ideDocumentoAverbacao;
	}

	public ReservaLegalAverbada() {
    }

    public ReservaLegalAverbada(Integer ideReservaLegalAverbada) {
        this.ideReservaLegalAverbada = ideReservaLegalAverbada;
    }

    public Integer getIdeReservaLegalAverbada() {
        return ideReservaLegalAverbada;
    }

    public void setIdeReservaLegalAverbada(Integer ideReservaLegalAverbada) {
        this.ideReservaLegalAverbada = ideReservaLegalAverbada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReservaLegalAverbada != null ? ideReservaLegalAverbada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaLegalAverbada)) {
            return false;
        }
        ReservaLegalAverbada other = (ReservaLegalAverbada) object;
        if ((this.ideReservaLegalAverbada == null && other.ideReservaLegalAverbada != null) || (this.ideReservaLegalAverbada != null && !this.ideReservaLegalAverbada.equals(other.ideReservaLegalAverbada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ReservaLegalAverbada[ ideReservaLegalAverbada=" + ideReservaLegalAverbada + " ]";
    }

	public ReservaLegal getIdeReservaLegal() {
		return ideReservaLegal;
	}

	public void setIdeReservaLegal(ReservaLegal ideReservaLegal) {
		this.ideReservaLegal = ideReservaLegal;
	}

	public String getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(String numMatricula) {
		this.numMatricula = numMatricula;
	}

	public String getNumAverbacao() {
		return numAverbacao;
	}

	public void setNumAverbacao(String numAverbacao) {
		this.numAverbacao = numAverbacao;
	}

	public Date getDtcAverbacao() {
		return dtcAverbacao;
	}

	public void setDtcAverbacao(Date dtcAverbacao) {
		this.dtcAverbacao = dtcAverbacao;
	}
    
}
