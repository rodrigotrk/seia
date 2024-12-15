package br.gov.ba.seia.entity;

import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "logradouro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logradouro.findAll", query = "SELECT l FROM Logradouro l"),
    @NamedQuery(name = "Logradouro.findByIdeLogradouro", query = "SELECT l FROM Logradouro l WHERE l.ideLogradouro = :ideLogradouro"),
    @NamedQuery(name = "Logradouro.findByNomLogradouro", query = "SELECT l FROM Logradouro l WHERE l.nomLogradouro = :nomLogradouro"),
    @NamedQuery(name = "Logradouro.findByNumCep", query = "SELECT l FROM Logradouro l WHERE l.numCep = :numCep"),
    @NamedQuery(name = "Logradouro.findByIndOrigemCorreio", query = "SELECT l FROM Logradouro l WHERE l.indOrigemCorreio = :indOrigemCorreio"),
    @NamedQuery(name = "Logradouro.findByIdeMunicipio", query = "SELECT l FROM Logradouro l WHERE l.ideMunicipio = :ideMunicipio"),
    @NamedQuery(name = "Logradouro.findByIdeBairro", query = "SELECT l FROM Logradouro l WHERE l.ideBairro.ideBairro = :ideBairro and l.indOrigemCorreio = :indOrigemCorreio and l.numCep = :numCep"),
    @NamedQuery(name = "Logradouro.findByNumCepAndCorreios", query = "SELECT l FROM Logradouro l WHERE l.numCep = :numCep and l.indOrigemCorreio = :indOrigemCorreio"),
  })
public class Logradouro extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	private static final Integer OUTRO = -1;
	private static final Integer NAO_SELECIONADO = 0;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOGRADOURO_IDE_LOGRADOURO_seq")
    @SequenceGenerator(name="LOGRADOURO_IDE_LOGRADOURO_seq", sequenceName="LOGRADOURO_IDE_LOGRADOURO_seq", allocationSize=1)
    @Column(name = "ide_logradouro", nullable = false)
    private Integer ideLogradouro;

	@Basic(optional = false)
    @Column(name = "nom_logradouro", nullable = false, length = 200)
    private String nomLogradouro;

	@Basic(optional = false)
    @Column(name = "num_cep", nullable = false, length = 10)
    private Integer numCep;
	
	@Column(name = "ind_origem_api", nullable = false)
    private Boolean indOrigemApi;

    @Column(name = "ind_origem_correio", nullable = false)
    private Boolean indOrigemCorreio;

	@JoinColumn(name = "ide_tipo_logradouro", referencedColumnName = "ide_tipo_logradouro", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoLogradouro ideTipoLogradouro;

	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Municipio ideMunicipio;

	@JoinColumn(name = "ide_bairro", referencedColumnName = "ide_bairro")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bairro ideBairro;

	@OneToMany(mappedBy = "ideLogradouro", fetch = FetchType.LAZY)
    private Collection<Endereco> enderecoCollection;
	
    @Column(name = "nom_origem_api", nullable = false, length = 2)
    private String nomOrigemApi;

    public Logradouro() {}


    public Logradouro(Integer ideLogradouro, String nomLogradouro) {
        this.ideLogradouro = ideLogradouro;
        this.nomLogradouro = nomLogradouro;
    }

    public Logradouro(Integer ideLogradouro) {
        this.ideLogradouro = ideLogradouro;
    }

    public Logradouro(Municipio ideMunicipio) {
        this.ideMunicipio = ideMunicipio;
    }

    public Logradouro(Integer ideLogradouro, String nomLogradouro, Integer numCep, boolean indOrigemCorreio, Municipio ideMunicipio) {
        this.ideLogradouro = ideLogradouro;
        this.nomLogradouro = nomLogradouro;
        this.numCep = numCep;
        this.indOrigemCorreio = indOrigemCorreio;
        this.ideMunicipio = ideMunicipio;
    }

    public Logradouro(Integer ideLogradouro, String nomLogradouro, boolean indOrigemCorreio) {
    	this.ideLogradouro = ideLogradouro;
    	this.nomLogradouro = nomLogradouro;
    	this.indOrigemCorreio = indOrigemCorreio;
    }

    public Integer getIdeLogradouro() {
        return ideLogradouro;
    }

    public void setIdeLogradouro(Integer ideLogradouro) {
        this.ideLogradouro = ideLogradouro;
    }

    public String getNomLogradouro() {
        if(Util.isNullOuVazio(nomLogradouro)){
        	return "";
        }

    	return nomLogradouro;


    }

    public void setNomLogradouro(String nomLogradouro) {
        this.nomLogradouro = nomLogradouro;
    }

	public Integer getNumCep() {
		return numCep;
	}

	public String getNumCepString() {
		String cep = "";
		if (!Util.isNullOuVazio(numCep)) {
			if (!Util.isNull(numCep) && numCep.toString().length() < 8) {
				cep = Util.lpad(numCep.toString(), '0', 8);
			} else {
				cep = numCep.toString();
			}
		}
		return cep;
	}

    public String getNumCepFormatado(){
    	StringBuilder str = new StringBuilder();
    	String cep = this.getNumCepString();
    	str.append(cep.substring(0,2)).append(".").append(cep.substring(2,5)).append("-").append(cep.substring(5,8));
    	return str.toString();
    }

    public void setNumCep(Integer numCep) {
    	this.numCep = numCep;
    }

    public Boolean getIndOrigemCorreio() {
        return indOrigemCorreio;
    }

    public void setIndOrigemCorreio(Boolean indOrigemCorreio) {
        this.indOrigemCorreio = indOrigemCorreio;
    }

    public TipoLogradouro getIdeTipoLogradouro() {
        return ideTipoLogradouro;
    }

    public void setIdeTipoLogradouro(TipoLogradouro ideTipoLogradouro) {
        this.ideTipoLogradouro = ideTipoLogradouro;
    }

    public Municipio getIdeMunicipio() {
        return ideMunicipio;
    }

    public Municipio getMunicipio() {
    	return Util.isNull(ideMunicipio) ? getIdeBairro().getIdeMunicipio() : ideMunicipio;
    }

    public void setIdeMunicipio(Municipio ideMunicipio) {
        this.ideMunicipio = ideMunicipio;
    }

    public Bairro getIdeBairro() {
        return ideBairro;
    }

    public void setIdeBairro(Bairro ideBairro) {
        this.ideBairro = ideBairro;
    }

    public Boolean getIndOrigemApi() {
		return indOrigemApi;
	}


	public void setIndOrigemApi(Boolean indOrigemApi) {
		this.indOrigemApi = indOrigemApi;
	}


	@XmlTransient
    public Collection<Endereco> getEnderecoCollection() {
        return enderecoCollection;
    }

    public void setEnderecoCollection(Collection<Endereco> enderecoCollection) {
        this.enderecoCollection = enderecoCollection;
    }

	public boolean isOutroLogradouro() {
		return !Util.isNull(this.ideLogradouro) && this.ideLogradouro.equals(OUTRO);
	}

	public boolean isLagradouroNaoSelecionado(){
		return !Util.isNull(this.ideLogradouro) && this.ideLogradouro.equals(NAO_SELECIONADO);
	}

	public boolean isHouveMudanca(Logradouro other) {
        if(this.nomLogradouro != null && !this.nomLogradouro.equals(other.nomLogradouro)) {
            return true;
        }

        return false;
    }
	
    public void setNomOrigemApi(String nomOrigemApi) {
        this.nomOrigemApi = nomOrigemApi;
    }

    public String getNomOrigemApi() {
        if(Util.isNullOuVazio(nomOrigemApi)){
        	return "";
        }
    	return nomOrigemApi;
    }

}
