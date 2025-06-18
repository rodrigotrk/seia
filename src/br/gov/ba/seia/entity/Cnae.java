package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "cnae", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cod_cnae", "des_cnae"})})
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Cnae.findAll", query = "SELECT c FROM Cnae c"),
    @NamedQuery(name = "Cnae.findByIdeCnae", query = "SELECT c FROM Cnae c WHERE c.ideCnae = :ideCnae"),
    @NamedQuery(name = "Cnae.findByCodCnae", query = "SELECT c FROM Cnae c WHERE c.codCnae = :codCnae"),
    @NamedQuery(name = "Cnae.findByDesCnae", query = "SELECT c FROM Cnae c WHERE c.desCnae = :desCnae"),
    @NamedQuery(name = "Cnae.findByDtcCriacao", query = "SELECT c FROM Cnae c WHERE c.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Cnae.findByIndExcluido", query = "SELECT c FROM Cnae c WHERE c.indExcluido = :indExcluido"),
    @NamedQuery(name = "Cnae.findByDtcExclusao", query = "SELECT c FROM Cnae c WHERE c.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Cnae.findByPai", query = "SELECT c FROM Cnae c WHERE c.ideCnaePai.ideCnae = :ideCnaePai"),
    @NamedQuery(name = "Cnae.findNivel", query = "SELECT c FROM Cnae c WHERE c.ideNivelCnae.ideNivelCnae = :ideNivelCnae")})
public class Cnae implements Serializable ,Cloneable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ide_cnae", nullable = false)
    private Integer ideCnae;
    @Basic(optional = false)
    @Column(name = "cod_cnae", nullable = false, length = 20)
    private String codCnae;
    @Size(min = 1, max = 250)
    @Column(name = "des_cnae", nullable = false, length = 250)
    private String desCnae;
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "ind_excluido", nullable = false)
    private Boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @JoinTable(name = "tipologia_cnae", joinColumns = {
        @JoinColumn(name = "ide_cnae", referencedColumnName = "ide_cnae", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false)})
    
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Tipologia> tipologiaCollection;
    @JoinColumn(name = "ide_nivel_cnae", referencedColumnName = "ide_nivel_cnae", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NivelCnae ideNivelCnae;
    @OneToMany(mappedBy = "ideCnaePai", fetch = FetchType.LAZY)
    private Collection<Cnae> cnaeCollection;
    @JoinColumn(name = "ide_cnae_pai", referencedColumnName = "ide_cnae")
    @ManyToOne(fetch = FetchType.EAGER)
    private Cnae ideCnaePai;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="ideCnae", fetch = FetchType.LAZY)   
    private Collection<PessoaJuridicaCnae> pessoaJuridicaCnaeCollection;

    public Cnae() {
    }

    public Cnae(Integer ideCnae) {
        this.ideCnae = ideCnae;
    }

    public Cnae(Integer ideCnae, String codCnae, String desCnae, Date dtcCriacao, boolean indExcluido) {
        this.ideCnae = ideCnae;
        this.codCnae = codCnae;
        this.desCnae = desCnae;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeCnae() {
        return ideCnae;
    }

    public void setIdeCnae(Integer ideCnae) {
        this.ideCnae = ideCnae;
    }

    public String getCodCnae() {
        return codCnae;
    }

    public void setCodCnae(String codCnae) {
        this.codCnae = codCnae;
    }

    public String getDesCnae() {
        return desCnae;
    }

    public void setDesCnae(String desCnae) {
        this.desCnae = desCnae;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(Boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @XmlTransient
    public Collection<Tipologia> getTipologiaCollection() {
        return tipologiaCollection;
    }

    public void setTipologiaCollection(Collection<Tipologia> tipologiaCollection) {
        this.tipologiaCollection = tipologiaCollection;
    }

    public NivelCnae getIdeNivelCnae() {
        return ideNivelCnae;
    }

    public void setIdeNivelCnae(NivelCnae ideNivelCnae) {
        this.ideNivelCnae = ideNivelCnae;
    }

    @XmlTransient
    public Collection<Cnae> getCnaeCollection() {
        return cnaeCollection;
    }

    public void setCnaeCollection(Collection<Cnae> cnaeCollection) {
        this.cnaeCollection = cnaeCollection;
    }

    public Cnae getIdeCnaePai() {
        return ideCnaePai;
    }

    public void setIdeCnaePai(Cnae ideCnaePai) {
        this.ideCnaePai = ideCnaePai;
    }

    @XmlTransient
    public Collection<PessoaJuridicaCnae> getPessoaJuridicaCnaeCollection() {
        return pessoaJuridicaCnaeCollection;
    }

    public void setPessoaJuridicaCnaeCollection(Collection<PessoaJuridicaCnae> pessoaJuridicaCnaeCollection) {
        this.pessoaJuridicaCnaeCollection = pessoaJuridicaCnaeCollection;
    }

	public String getCnaeFormatado(){
		return this.getCodCnae() + " - "+ this.getDesCnae();
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCnae != null ? ideCnae.hashCode() : 0);
        return hash;
    }

    @Override
    public Cnae clone() throws CloneNotSupportedException {
    	return (Cnae) super.clone();
    }
    
    @Override
    public boolean equals(Object object) {
		
		
        if (!(object instanceof Cnae)) {
            return false;
        }
        Cnae other = (Cnae) object;
        if ((this.ideCnae == null && other.ideCnae != null) || (this.ideCnae != null && !this.ideCnae.equals(other.ideCnae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
		return String.valueOf(ideCnae);
    }
    
}
