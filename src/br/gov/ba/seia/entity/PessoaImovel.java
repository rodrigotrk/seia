package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pessoa_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaImovel.findAll", query = "SELECT p FROM PessoaImovel p"),
    @NamedQuery(name = "PessoaImovel.findByIdePessoaImovel", query = "SELECT p FROM PessoaImovel p WHERE p.idePessoaImovel = :idePessoaImovel"),
    @NamedQuery(name = "PessoaImovel.findByDtcCriacao", query = "SELECT p FROM PessoaImovel p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "PessoaImovel.findByIndExcluido", query = "SELECT p FROM PessoaImovel p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "PessoaImovel.findByImovel", query = "SELECT p FROM PessoaImovel p WHERE p.ideImovel = :ideImovel and p.indExcluido = :indExcluido"),
    @NamedQuery(name = "PessoaImovel.findByDtcExclusao", query = "SELECT p FROM PessoaImovel p WHERE p.dtcExclusao = :dtcExclusao")})
public class PessoaImovel implements Serializable {
    private static final long serialVersionUID = 1L;    
    @Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq")    
    @SequenceGenerator(name="PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq", sequenceName="PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pessoa_imovel", nullable = false)
    private Integer idePessoaImovel;
    
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
    
    @JoinColumn(name = "ide_tipo_vinculo_imovel", referencedColumnName = "ide_tipo_vinculo_imovel", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoVinculoImovel ideTipoVinculoImovel;
    
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idePessoa;
    
    @JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Imovel ideImovel;
    
    @JoinColumn(name = "ide_tipo_vinculo_pct", referencedColumnName = "ide_tipo_vinculo_pct", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private TipoVinculoPCT ideTipoVinculoPCT;
    
    @Column(name="dsc_tipo_vinculo_pct_outros", nullable = true)
    private String dscTipoVinculoPCTOutros;
    
    public PessoaImovel() {
    }

    public PessoaImovel(Integer idePessoaImovel) {
        this.idePessoaImovel = idePessoaImovel;
    }

    public PessoaImovel(Integer idePessoaImovel, Date dtcCriacao, boolean indExcluido) {
        this.idePessoaImovel = idePessoaImovel;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdePessoaImovel() {
        return idePessoaImovel;
    }

    public void setIdePessoaImovel(Integer idePessoaImovel) {
        this.idePessoaImovel = idePessoaImovel;
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

    public TipoVinculoImovel getIdeTipoVinculoImovel() {
        return ideTipoVinculoImovel;
    }

    public void setIdeTipoVinculoImovel(TipoVinculoImovel ideTipoVinculoImovel) {
        this.ideTipoVinculoImovel = ideTipoVinculoImovel;
    }

    public String getDscTipoVinculoPCTOutros() {
		return dscTipoVinculoPCTOutros;
	}

	public void setDscTipoVinculoPCTOutros(String dscTipoVinculoPCTOutros) {
		this.dscTipoVinculoPCTOutros = dscTipoVinculoPCTOutros;
	}

	public Pessoa getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Pessoa idePessoa) {
        this.idePessoa = idePessoa;
    }

    public Imovel getIdeImovel() {
        return ideImovel;
    }

    public void setIdeImovel(Imovel ideImovel) {
        this.ideImovel = ideImovel;
    }

    public TipoVinculoPCT getIdeTipoVinculoPCT() {
		return ideTipoVinculoPCT;
	}

	public void setIdeTipoVinculoPCT(TipoVinculoPCT ideTipoVinculoPCT) {
		this.ideTipoVinculoPCT = ideTipoVinculoPCT;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idePessoaImovel != null ? idePessoaImovel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaImovel)) {
            return false;
        }
        PessoaImovel other = (PessoaImovel) object;
        if ((this.idePessoaImovel == null && other.idePessoaImovel != null) || (this.idePessoaImovel != null && !this.idePessoaImovel.equals(other.idePessoaImovel))) {
            return false;
        }
        return true;
    }
    
    public Boolean atributosPessoaEImovelIguais(PessoaImovel p){
    	if(this.idePessoa.getIdePessoa().equals(p.getIdePessoa().getIdePessoa()) && this.ideImovel.getIdeImovel().equals(p.getIdeImovel().getIdeImovel()))
    		return true;
    	else{
    		System.out.println("OU PESSOAS NãO SAO IGUAIS-> "+this.idePessoa.getIdePessoa()+" = "+ p.getIdePessoa().getIdePessoa());
    		System.out.println("OU IMOVEL NãO SAO IGUAIS-> "+this.ideImovel.getIdeImovel()+" = "+ p.getIdeImovel().getIdeImovel());
    		return false;
    	}
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaImovel[ idePessoaImovel=" + idePessoaImovel + " ]";
    }
    
}
