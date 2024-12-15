package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pessoa_endereco_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaEnderecoHistorico.findAll", query = "SELECT p FROM PessoaEnderecoHistorico p"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByIdePessoaEnderecoHistorico", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.idePessoaEnderecoHistorico = :idePessoaEnderecoHistorico"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNomTipoEndereco", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.nomTipoEndereco = :nomTipoEndereco"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNomEstado", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.nomEstado = :nomEstado"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNomMunicipio", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.nomMunicipio = :nomMunicipio"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNomBairro", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.nomBairro = :nomBairro"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNumEndereco", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.numEndereco = :numEndereco"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByNumCep", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.numCep = :numCep"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByDesLogradouro", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.desLogradouro = :desLogradouro"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByDtcHistorico", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.dtcHistorico = :dtcHistorico"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByDesComplemento", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.desComplemento = :desComplemento"),
    @NamedQuery(name = "PessoaEnderecoHistorico.findByDesPontoReferencia", query = "SELECT p FROM PessoaEnderecoHistorico p WHERE p.desPontoReferencia = :desPontoReferencia")})
public class PessoaEnderecoHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pessoa_endereco_historico", nullable = false)
    private Integer idePessoaEnderecoHistorico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_endereco", nullable = false, length = 20)
    private String nomTipoEndereco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_estado", nullable = false, length = 30)
    private String nomEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_municipio", nullable = false, length = 60)
    private String nomMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_bairro", nullable = false, length = 50)
    private String nomBairro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "num_endereco", nullable = false, length = 5)
    private String numEndereco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_cep", nullable = false)
    private int numCep;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "des_logradouro", nullable = false, length = 200)
    private String desLogradouro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_historico", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcHistorico;
    @Size(max = 100)
    @Column(name = "des_complemento", length = 100)
    private String desComplemento;
    @Size(max = 100)
    @Column(name = "des_ponto_referencia", length = 100)
    private String desPontoReferencia;
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa_juridica", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoa;
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica idePessoa1;

    public PessoaEnderecoHistorico() {
    }

    public PessoaEnderecoHistorico(Integer idePessoaEnderecoHistorico) {
        this.idePessoaEnderecoHistorico = idePessoaEnderecoHistorico;
    }

    public PessoaEnderecoHistorico(Integer idePessoaEnderecoHistorico, String nomTipoEndereco, String nomEstado, String nomMunicipio, String nomBairro, String numEndereco, int numCep, String desLogradouro, Date dtcHistorico) {
        this.idePessoaEnderecoHistorico = idePessoaEnderecoHistorico;
        this.nomTipoEndereco = nomTipoEndereco;
        this.nomEstado = nomEstado;
        this.nomMunicipio = nomMunicipio;
        this.nomBairro = nomBairro;
        this.numEndereco = numEndereco;
        this.numCep = numCep;
        this.desLogradouro = desLogradouro;
        this.dtcHistorico = dtcHistorico;
    }

    public Integer getIdePessoaEnderecoHistorico() {
        return idePessoaEnderecoHistorico;
    }

    public void setIdePessoaEnderecoHistorico(Integer idePessoaEnderecoHistorico) {
        this.idePessoaEnderecoHistorico = idePessoaEnderecoHistorico;
    }

    public String getNomTipoEndereco() {
        return nomTipoEndereco;
    }

    public void setNomTipoEndereco(String nomTipoEndereco) {
        this.nomTipoEndereco = nomTipoEndereco;
    }

    public String getNomEstado() {
        return nomEstado;
    }

    public void setNomEstado(String nomEstado) {
        this.nomEstado = nomEstado;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }

    public void setNomMunicipio(String nomMunicipio) {
        this.nomMunicipio = nomMunicipio;
    }

    public String getNomBairro() {
        return nomBairro;
    }

    public void setNomBairro(String nomBairro) {
        this.nomBairro = nomBairro;
    }

    public String getNumEndereco() {
        return numEndereco;
    }

    public void setNumEndereco(String numEndereco) {
        this.numEndereco = numEndereco;
    }

    public int getNumCep() {
        return numCep;
    }

    public void setNumCep(int numCep) {
        this.numCep = numCep;
    }

    public String getDesLogradouro() {
        return desLogradouro;
    }

    public void setDesLogradouro(String desLogradouro) {
        this.desLogradouro = desLogradouro;
    }

    public Date getDtcHistorico() {
        return dtcHistorico;
    }

    public void setDtcHistorico(Date dtcHistorico) {
        this.dtcHistorico = dtcHistorico;
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

    public PessoaJuridica getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(PessoaJuridica idePessoa) {
        this.idePessoa = idePessoa;
    }

    public PessoaFisica getIdePessoa1() {
        return idePessoa1;
    }

    public void setIdePessoa1(PessoaFisica idePessoa1) {
        this.idePessoa1 = idePessoa1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePessoaEnderecoHistorico != null ? idePessoaEnderecoHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaEnderecoHistorico)) {
            return false;
        }
        PessoaEnderecoHistorico other = (PessoaEnderecoHistorico) object;
        if ((this.idePessoaEnderecoHistorico == null && other.idePessoaEnderecoHistorico != null) || (this.idePessoaEnderecoHistorico != null && !this.idePessoaEnderecoHistorico.equals(other.idePessoaEnderecoHistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaEnderecoHistorico[ idePessoaEnderecoHistorico=" + idePessoaEnderecoHistorico + " ]";
    }
    
}
