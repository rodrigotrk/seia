package br.gov.ba.seia.entity;

import java.util.Collection;

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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "bairro", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_bairro", "ide_municipio"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bairro.findAll", query = "SELECT b FROM Bairro b"),
    @NamedQuery(name = "Bairro.findByIdeBairro", query = "SELECT b FROM Bairro b WHERE b.ideBairro = :ideBairro and b.indOrigemCorreio = :indOrigemCorreio"),
    @NamedQuery(name = "Bairro.findByNomBairro", query = "SELECT b FROM Bairro b WHERE b.nomBairro = :nomBairro"),
    @NamedQuery(name = "Bairro.findByIndOrigemCorreio", query = "SELECT b FROM Bairro b WHERE b.indOrigemCorreio = :indOrigemCorreio"),
  })
public class Bairro extends AbstractEntity implements Cloneable {

	private static final long serialVersionUID = 1L;
	private static final Integer OUTRO = -1;
	private static final Integer NAO_SELECIONADO = 0;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BAIRRO_IDE_BAIRRO_seq")
    @SequenceGenerator(name="BAIRRO_IDE_BAIRRO_seq", sequenceName="BAIRRO_IDE_BAIRRO_seq", allocationSize=1)
    @Column(name = "ide_bairro", nullable = false)
    private Integer ideBairro;

    @Column(name = "nom_bairro", nullable = false, length = 50)
    private String nomBairro;

    @Column(name = "ind_origem_correio", nullable = false)
    private boolean indOrigemCorreio;

    @Column(name = "ind_origem_api", nullable = false)
    private boolean indOrigemApi;

	public boolean isIndOrigemApi() {
		return indOrigemApi;
	}

	public void setIndOrigemApi(boolean indOrigemApi) {
		this.indOrigemApi = indOrigemApi;
	}

	@OneToMany(mappedBy = "ideBairro", fetch = FetchType.LAZY)
    private Collection<Logradouro> logradouroCollection;

	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Municipio ideMunicipio;

    public Bairro() {}

    public Bairro(Integer ideBairro) {
        this.ideBairro = ideBairro;
    }

    public Bairro(Integer ideBairro, String nomBairro, boolean indOrigemCorreio) {
        this.ideBairro = ideBairro;
        this.nomBairro = nomBairro;
        this.indOrigemCorreio = indOrigemCorreio;
    }

    public Integer getIdeBairro() {
        return ideBairro;
    }

    public void setIdeBairro(Integer ideBairro) {
        this.ideBairro = ideBairro;
    }

    public String getNomBairro() {
        return nomBairro;
    }

    public void setNomBairro(String nomBairro) {
        this.nomBairro = nomBairro;
    }

    public boolean getIndOrigemCorreio() {
        return indOrigemCorreio;
    }

    public void setIndOrigemCorreio(boolean indOrigemCorreio) {
        this.indOrigemCorreio = indOrigemCorreio;
    }

    @XmlTransient
    public Collection<Logradouro> getLogradouroCollection() {
        return logradouroCollection;
    }

    public void setLogradouroCollection(Collection<Logradouro> logradouroCollection) {
        this.logradouroCollection = logradouroCollection;
    }

    public Municipio getIdeMunicipio() {
        return ideMunicipio;
    }

    public void setIdeMunicipio(Municipio ideMunicipio) {
        this.ideMunicipio = ideMunicipio;
    }

	public boolean isBairroNaoSelecionado(){
		return !Util.isNull(this.ideBairro) && this.ideBairro.equals(NAO_SELECIONADO);
	}

	public boolean isOutroBairro(){
		return !Util.isNull(this.ideBairro) && this.ideBairro.equals(OUTRO);
	}

	public boolean isHouveMudanca(Bairro other) {
        if (this.nomBairro != null && !this.nomBairro.equals(other.nomBairro)) {
            return true;
        }

        return false;
    }
}
