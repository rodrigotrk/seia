package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "unidade_hidrografica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeHidrografica.findAll", query = "SELECT u FROM UnidadeHidrografica u"),
    @NamedQuery(name = "UnidadeHidrografica.findByIdeUnidadeHidrografica", query = "SELECT u FROM UnidadeHidrografica u WHERE u.ideUnidadeHidrografica = :ideUnidadeHidrografica"),
    @NamedQuery(name = "UnidadeHidrografica.findBySglUnidadeHidrografica", query = "SELECT u FROM UnidadeHidrografica u WHERE u.sglUnidadeHidrografica = :sglUnidadeHidrografica"),
    @NamedQuery(name = "UnidadeHidrografica.findByNomUnidadeGeografica", query = "SELECT u FROM UnidadeHidrografica u WHERE u.nomUnidadeGeografica = :nomUnidadeGeografica"),
    @NamedQuery(name = "UnidadeHidrografica.findByCodUnidadeHidrografica", query = "SELECT u FROM UnidadeHidrografica u WHERE u.codUnidadeHidrografica = :codUnidadeHidrografica"),
    @NamedQuery(name = "UnidadeHidrografica.findByDtcCriacao", query = "SELECT u FROM UnidadeHidrografica u WHERE u.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "UnidadeHidrografica.findByIndExcluido", query = "SELECT u FROM UnidadeHidrografica u WHERE u.indExcluido = :indExcluido"),
    @NamedQuery(name = "UnidadeHidrografica.findByDtcExclusao", query = "SELECT u FROM UnidadeHidrografica u WHERE u.dtcExclusao = :dtcExclusao")})
public class UnidadeHidrografica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_unidade_hidrografica", nullable = false)
    private Integer ideUnidadeHidrografica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "sgl_unidade_hidrografica", nullable = false, length = 3)
    private String sglUnidadeHidrografica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_unidade_geografica", nullable = false, length = 60)
    private String nomUnidadeGeografica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_unidade_hidrografica", nullable = false)
    private int codUnidadeHidrografica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @JoinTable(name = "rpga_unidade_hidrografica", joinColumns = {
        @JoinColumn(name = "ide_unidade_hidrografica", referencedColumnName = "ide_unidade_hidrografica", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ide_unid_hidrografica_rpga", referencedColumnName = "ide_unidade_hidrografica", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<UnidadeHidrografica> unidadeHidrograficaCollection;
    @ManyToMany(mappedBy = "unidadeHidrograficaCollection", fetch = FetchType.LAZY)
    private Collection<UnidadeHidrografica> unidadeHidrograficaCollection1;
    @OneToMany(mappedBy = "ideUnidadeSuperior", fetch = FetchType.LAZY)
    private Collection<UnidadeHidrografica> unidadeHidrograficaCollection2;
    @JoinColumn(name = "ide_unidade_superior", referencedColumnName = "ide_unidade_hidrografica")
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadeHidrografica ideUnidadeSuperior;
    @JoinColumn(name = "ide_tipo_unidade_hidrografica", referencedColumnName = "ide_tipo_unidade_hidrografica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoUnidadeHidrografica ideTipoUnidadeHidrografica;
    @JoinColumn(name = "ide_tipo_gestao", referencedColumnName = "ide_tipo_gestao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoGestao ideTipoGestao;
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LocalizacaoGeografica ideLocalizacaoGeografica;

    public UnidadeHidrografica() {
    }

    public UnidadeHidrografica(Integer ideUnidadeHidrografica) {
        this.ideUnidadeHidrografica = ideUnidadeHidrografica;
    }

    public UnidadeHidrografica(Integer ideUnidadeHidrografica, String sglUnidadeHidrografica, String nomUnidadeGeografica, int codUnidadeHidrografica, Date dtcCriacao, boolean indExcluido) {
        this.ideUnidadeHidrografica = ideUnidadeHidrografica;
        this.sglUnidadeHidrografica = sglUnidadeHidrografica;
        this.nomUnidadeGeografica = nomUnidadeGeografica;
        this.codUnidadeHidrografica = codUnidadeHidrografica;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeUnidadeHidrografica() {
        return ideUnidadeHidrografica;
    }

    public void setIdeUnidadeHidrografica(Integer ideUnidadeHidrografica) {
        this.ideUnidadeHidrografica = ideUnidadeHidrografica;
    }

    public String getSglUnidadeHidrografica() {
        return sglUnidadeHidrografica;
    }

    public void setSglUnidadeHidrografica(String sglUnidadeHidrografica) {
        this.sglUnidadeHidrografica = sglUnidadeHidrografica;
    }

    public String getNomUnidadeGeografica() {
        return nomUnidadeGeografica;
    }

    public void setNomUnidadeGeografica(String nomUnidadeGeografica) {
        this.nomUnidadeGeografica = nomUnidadeGeografica;
    }

    public int getCodUnidadeHidrografica() {
        return codUnidadeHidrografica;
    }

    public void setCodUnidadeHidrografica(int codUnidadeHidrografica) {
        this.codUnidadeHidrografica = codUnidadeHidrografica;
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

    @XmlTransient
    public Collection<UnidadeHidrografica> getUnidadeHidrograficaCollection() {
        return unidadeHidrograficaCollection;
    }

    public void setUnidadeHidrograficaCollection(Collection<UnidadeHidrografica> unidadeHidrograficaCollection) {
        this.unidadeHidrograficaCollection = unidadeHidrograficaCollection;
    }

    @XmlTransient
    public Collection<UnidadeHidrografica> getUnidadeHidrograficaCollection1() {
        return unidadeHidrograficaCollection1;
    }

    public void setUnidadeHidrograficaCollection1(Collection<UnidadeHidrografica> unidadeHidrograficaCollection1) {
        this.unidadeHidrograficaCollection1 = unidadeHidrograficaCollection1;
    }

    @XmlTransient
    public Collection<UnidadeHidrografica> getUnidadeHidrograficaCollection2() {
        return unidadeHidrograficaCollection2;
    }

    public void setUnidadeHidrograficaCollection2(Collection<UnidadeHidrografica> unidadeHidrograficaCollection2) {
        this.unidadeHidrograficaCollection2 = unidadeHidrograficaCollection2;
    }

    public UnidadeHidrografica getIdeUnidadeSuperior() {
        return ideUnidadeSuperior;
    }

    public void setIdeUnidadeSuperior(UnidadeHidrografica ideUnidadeSuperior) {
        this.ideUnidadeSuperior = ideUnidadeSuperior;
    }

    public TipoUnidadeHidrografica getIdeTipoUnidadeHidrografica() {
        return ideTipoUnidadeHidrografica;
    }

    public void setIdeTipoUnidadeHidrografica(TipoUnidadeHidrografica ideTipoUnidadeHidrografica) {
        this.ideTipoUnidadeHidrografica = ideTipoUnidadeHidrografica;
    }

    public TipoGestao getIdeTipoGestao() {
        return ideTipoGestao;
    }

    public void setIdeTipoGestao(TipoGestao ideTipoGestao) {
        this.ideTipoGestao = ideTipoGestao;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUnidadeHidrografica != null ? ideUnidadeHidrografica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof UnidadeHidrografica)) {
            return false;
        }
        UnidadeHidrografica other = (UnidadeHidrografica) object;
        if ((this.ideUnidadeHidrografica == null && other.ideUnidadeHidrografica != null) || (this.ideUnidadeHidrografica != null && !this.ideUnidadeHidrografica.equals(other.ideUnidadeHidrografica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.UnidadeHidrografica[ ideUnidadeHidrografica=" + ideUnidadeHidrografica + " ]";
    }
    
}
