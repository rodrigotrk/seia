package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "telefone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Telefone.findAll", query = "SELECT t FROM Telefone t"),
    @NamedQuery(name = "Telefone.findByIdeTelefone", query = "SELECT t FROM Telefone t WHERE t.ideTelefone = :ideTelefone"),
    @NamedQuery(name = "Telefone.findByNumTelefone", query = "SELECT t FROM Telefone t WHERE t.numTelefone = :numTelefone"),
    @NamedQuery(name = "Telefone.findByDtcCriacao", query = "SELECT t FROM Telefone t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Telefone.findByDtcExclusao", query = "SELECT t FROM Telefone t WHERE t.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Telefone.findByIndExcluido", query = "SELECT t FROM Telefone t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "Telefone.findByIdePessoa", query = "SELECT t FROM Telefone t join t.pessoaCollection as p WHERE t.indExcluido = :indExcluido and p.idePessoa = :idePessoa"),    
    @NamedQuery(name = "Telefone.remove", query = "UPDATE Telefone t SET t.indExcluido = true, t.dtcExclusao = :dtcExclusao WHERE t.ideTelefone = :ideTelefone")
    })
public class Telefone extends AbstractEntity {
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="telefone_ide_telefone_seq")    
    @SequenceGenerator(name="telefone_ide_telefone_seq", sequenceName="telefone_ide_telefone_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_telefone", nullable = false)
    private Integer ideTelefone;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "num_telefone", nullable = false, length = 20)
    private String numTelefone;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
	@Column(name = "ind_excluido")
    private boolean indExcluido;
    
	@JoinTable(name = "telefone_pessoa", joinColumns = {
        @JoinColumn(name = "ide_telefone", referencedColumnName = "ide_telefone", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)})
    @ManyToMany(fetch=FetchType.LAZY) 
    private Collection<Pessoa> pessoaCollection;  
    
	@JoinColumn(name = "ide_tipo_telefone", referencedColumnName = "ide_tipo_telefone", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoTelefone ideTipoTelefone;

    @Transient
    private boolean visualizacao;
    
    public Telefone() {
    }

    public Telefone(Integer ideTelefone) {
        this.ideTelefone = ideTelefone;
    }

    public Telefone(Integer ideTelefone, String numTelefone, Date dtcCriacao) {
        this.ideTelefone = ideTelefone;
        this.numTelefone = numTelefone;
        this.dtcCriacao = dtcCriacao;
    }

	public Telefone(br.gov.ba.seia.entity.Pessoa pessoa) {
		this.pessoaCollection = new ArrayList<Pessoa>();
		this.pessoaCollection.add(pessoa);
	}

	public Integer getIdeTelefone() {
        return ideTelefone;
    }

    public void setIdeTelefone(Integer ideTelefone) {
        this.ideTelefone = ideTelefone;
    }

    public String getNumTelefone() {
        return numTelefone;
    }

    public void setNumTelefone(String numTelefone) {
        this.numTelefone = numTelefone;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    @XmlTransient
    public Collection<Pessoa> getPessoaCollection() {
        return pessoaCollection;
    }

    public void setPessoaCollection(Collection<Pessoa> pessoaCollection) {
        this.pessoaCollection = pessoaCollection;
    }
    
    public TipoTelefone getIdeTipoTelefone() {
        return ideTipoTelefone;
    }

    public void setIdeTipoTelefone(TipoTelefone ideTipoTelefone) {
        this.ideTipoTelefone = ideTipoTelefone;
    }

    private boolean isNumTelefoneEquals(Telefone other) {
    	if (!Util.isNull(other.numTelefone) && !Util.isNull(this.numTelefone) && other.numTelefone.equals(this.numTelefone)) {
    		return true;
    	}
    	return false;
    }
    
    @SuppressWarnings("unused")
	private boolean isIdeTelefoneEquals(Telefone other) {
    	if (!Util.isNull(other.ideTelefone) && !Util.isNull(this.ideTelefone) && other.ideTelefone.equals(this.ideTelefone)) {
    		return true;
    	}
    	return false;
    }
    
    private boolean isTipoTelefoneEquals(Telefone other) {
    	if (!Util.isNull(other.ideTipoTelefone) && !Util.isNull(this.ideTipoTelefone) && other.ideTipoTelefone.equals(this.ideTipoTelefone)) {
    		return true;
    	}
    	return false;
    }


	public boolean isVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

}
