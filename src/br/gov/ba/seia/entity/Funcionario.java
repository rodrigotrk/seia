package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.gov.ba.seia.interfaces.BaseEntity;
import flexjson.JSON;

/**
 * @author MJunior
 */
@Entity
@Table(name = "funcionario")



@XmlRootElement
@NamedQueries({ 
	@NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f"), 
	@NamedQuery(name = "Funcionario.findByIdePessoaFisica", query = "SELECT f FROM Funcionario f WHERE f.idePessoaFisica = :idePessoaFisica"),
	@NamedQuery(name = "Funcionario.findByMatricula", query = "SELECT f FROM Funcionario f WHERE f.matricula = :matricula"),
	@NamedQuery(name = "Funcionario.findByPerfil", query = "SELECT f FROM Funcionario f WHERE f.pessoaFisica.usuario.idePerfil.idePerfil = :idePerfil and f.pessoaFisica.usuario.indTipoUsuario = :indTipoUsuario")})
public class Funcionario implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fk_funcionario_pessoa_fisica")
	@SequenceGenerator(name = "fk_funcionario_pessoa_fisica", sequenceName = "fk_funcionario_pessoa_fisica", allocationSize = 1)
	@GenericGenerator(name = "fk_funcionario_pessoa_fisica", strategy = "foreign", parameters = @Parameter(name = "property", value = "pessoaFisica"))
	@Column(name = "ide_pessoa_fisica", nullable = false)
	private Integer idePessoaFisica;
	
	@Column(name = "matricula", length = 50)
	private String matricula;
    
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;	
	
	@OneToMany(mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<Area> areaCollection;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false, insertable = false, updatable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private PessoaFisica pessoaFisica;
	
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Area ideArea;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<Pauta> pautaCollection;
	
	@OneToMany(mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<IntegranteEquipe> integranteEquipeCollection;
	
	@OneToMany(mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<Portaria> portariaCollection;
	
	@OneToMany(mappedBy = "idePessoaStatusReservaAgua", fetch = FetchType.LAZY)
	private Collection<ReservaAgua> reservaAguaCollection;
	
	@OneToMany(mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<ControleProcessoAto> controleProcessoAtoCollection;
	
	@OneToMany(mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<Fce> fceCollection;

	public Funcionario() {
	}

	public Funcionario(Area ideArea) {
		this.ideArea = ideArea;
	}

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }
	public Funcionario(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public Funcionario(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	 @JSON(include = false)
	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	 @JSON(include = false)
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@XmlTransient
	public Collection<Area> getAreaCollection() {
		return areaCollection;
	}

	public void setAreaCollection(Collection<Area> areaCollection) {
		this.areaCollection = areaCollection;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	 @JSON(include = false)
	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}

	@XmlTransient
	public Collection<Pauta> getPautaCollection() {
		return pautaCollection;
	}

	public void setPautaCollection(Collection<Pauta> pautaCollection) {
		this.pautaCollection = pautaCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idePessoaFisica != null ? idePessoaFisica.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		if (!(object instanceof Funcionario)) {
			return false;
		}
		Funcionario other = (Funcionario) object;
		if ((this.idePessoaFisica == null && other.idePessoaFisica != null) || (this.idePessoaFisica != null && !this.idePessoaFisica.equals(other.idePessoaFisica))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(idePessoaFisica);
	}

	public Collection<IntegranteEquipe> getIntegranteEquipeCollection() {
		return integranteEquipeCollection;
	}

	public void setIntegranteEquipeCollection(
			Collection<IntegranteEquipe> integranteEquipeCollection) {
		this.integranteEquipeCollection = integranteEquipeCollection;
	}

	public Collection<Portaria> getPortariaCollection() {
		return portariaCollection;
	}

	public void setPortariaCollection(Collection<Portaria> portariaCollection) {
		this.portariaCollection = portariaCollection;
	}

	public Collection<ReservaAgua> getReservaAguaCollection() {
		return reservaAguaCollection;
	}

	public void setReservaAguaCollection(Collection<ReservaAgua> reservaAguaCollection) {
		this.reservaAguaCollection = reservaAguaCollection;
	}

	public Collection<ControleProcessoAto> getControleProcessoAtoCollection() {
		return controleProcessoAtoCollection;
	}

	public void setControleProcessoAtoCollection(
			Collection<ControleProcessoAto> controleProcessoAtoCollection) {
		this.controleProcessoAtoCollection = controleProcessoAtoCollection;
	}

	public Collection<Fce> getFceCollection() {
		return fceCollection;
	}

	public void setFceCollection(Collection<Fce> fceCollection) {
		this.fceCollection = fceCollection;
	}

	@Override
	public Long getId() {
		return idePessoaFisica.longValue();
	}
}
