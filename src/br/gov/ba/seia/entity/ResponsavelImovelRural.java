package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "responsavel_imovel_rural")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResponsavelImovelRural.findAll", query = "SELECT r FROM ResponsavelImovelRural r"),
    @NamedQuery(name = "ResponsavelImovelRural.findByIdeResponsavelImovelRural", query = "SELECT r FROM ResponsavelImovelRural r WHERE r.ideResponsavelImovelRural = :ideResponsavelImovelRural"),
    @NamedQuery(name = "ResponsavelImovelRural.findByDtcCriacao", query = "SELECT r FROM ResponsavelImovelRural r WHERE r.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ResponsavelImovelRural.findByIndExcluido", query = "SELECT r FROM ResponsavelImovelRural r WHERE r.indExcluido = :indExcluido"),
    @NamedQuery(name = "ResponsavelImovelRural.findByDtcExclusao", query = "SELECT r FROM ResponsavelImovelRural r WHERE r.dtcExclusao = :dtcExclusao")})
public class ResponsavelImovelRural extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPONSAVEL_IMOVEL_RURAL_IDE_RESPONSAVEL_IMOVEL_RURAL_seq")    
    @SequenceGenerator(name="RESPONSAVEL_IMOVEL_RURAL_IDE_RESPONSAVEL_IMOVEL_RURAL_seq", sequenceName="RESPONSAVEL_IMOVEL_RURAL_IDE_RESPONSAVEL_IMOVEL_RURAL_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_responsavel_imovel_rural", nullable = false)
    private Integer ideResponsavelImovelRural;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.DATE)
    private Date dtcCriacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido")
    private boolean indExcluido;
    
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica")
    @ManyToOne(optional = false)
    private PessoaFisica idePessoaFisica;
    
    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
    @ManyToOne(optional = false)
    private ImovelRural ideImovelRural;
    
    @OneToOne(optional = true)
    @JoinColumn(name = "ide_documento_responsavel", referencedColumnName = "ide_documento_imovel_rural")
    private DocumentoImovelRural ideDocumentoResponsavel;

    public ResponsavelImovelRural() {
    }

    public ResponsavelImovelRural(Integer ideResponsavelImovelRural) {
        this.ideResponsavelImovelRural = ideResponsavelImovelRural;
    }

    public ResponsavelImovelRural(Integer ideResponsavelImovelRural, Date dtcCriacao, boolean indExcluido) {
        this.ideResponsavelImovelRural = ideResponsavelImovelRural;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeResponsavelImovelRural() {
        return ideResponsavelImovelRural;
    }

    public void setIdeResponsavelImovelRural(Integer ideResponsavelImovelRural) {
        this.ideResponsavelImovelRural = ideResponsavelImovelRural;
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

    public PessoaFisica getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    public ImovelRural getIdeImovelRural() {
        return ideImovelRural;
    }

    public void setIdeImovelRural(ImovelRural ideImovelRural) {
        this.ideImovelRural = ideImovelRural;
    }
    
	public DocumentoImovelRural getIdeDocumentoResponsavel() {
		return ideDocumentoResponsavel;
	}

	public void setIdeDocumentoResponsavel(DocumentoImovelRural ideDocumentoResponsavel) {
		this.ideDocumentoResponsavel = ideDocumentoResponsavel;
	}
}
