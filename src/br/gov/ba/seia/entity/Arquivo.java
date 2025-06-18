package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "arquivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arquivo.findAll", query = "SELECT a FROM Arquivo a"),
    @NamedQuery(name = "Arquivo.findByIdeArquivo", query = "SELECT a FROM Arquivo a WHERE a.ideArquivo = :ideArquivo"),
    @NamedQuery(name = "Arquivo.findByDscCaminhoArquivo", query = "SELECT a FROM Arquivo a WHERE a.dscCaminhoArquivo = :dscCaminhoArquivo"),
    @NamedQuery(name = "Arquivo.findByDscArquivo", query = "SELECT a FROM Arquivo a WHERE a.dscArquivo = :dscArquivo")})
public class Arquivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ARQUIVO_IDE_ARQUIVO_seq")    
    @SequenceGenerator(name="ARQUIVO_IDE_ARQUIVO_seq", sequenceName="ARQUIVO_IDE_ARQUIVO_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_arquivo")
    private Integer ideArquivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dsc_caminho_arquivo")
    private String dscCaminhoArquivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "dsc_arquivo")
    private String dscArquivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arquivo")
    private Collection<ArquivoLac> arquivoLacCollection;

    public Arquivo() {
    }

    public Arquivo(Integer ideArquivo) {
        this.ideArquivo = ideArquivo;
    }

    public Arquivo(Integer ideArquivo, String dscCaminhoArquivo, String dscArquivo) {
        this.ideArquivo = ideArquivo;
        this.dscCaminhoArquivo = dscCaminhoArquivo;
        this.dscArquivo = dscArquivo;
    }

    public Integer getIdeArquivo() {
        return ideArquivo;
    }

    public void setIdeArquivo(Integer ideArquivo) {
        this.ideArquivo = ideArquivo;
    }

    public String getDscCaminhoArquivo() {
        return dscCaminhoArquivo;
    }

    public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
        this.dscCaminhoArquivo = dscCaminhoArquivo;
    }

    public String getDscArquivo() {
        return dscArquivo;
    }

    public void setDscArquivo(String dscArquivo) {
        this.dscArquivo = dscArquivo;
    }

    @XmlTransient
    public Collection<ArquivoLac> getArquivoLacCollection() {
        return arquivoLacCollection;
    }

    public void setArquivoLacCollection(Collection<ArquivoLac> arquivoLacCollection) {
        this.arquivoLacCollection = arquivoLacCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideArquivo != null ? ideArquivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Arquivo)) {
            return false;
        }
        Arquivo other = (Arquivo) object;
        if ((this.ideArquivo == null && other.ideArquivo != null) || (this.ideArquivo != null && !this.ideArquivo.equals(other.ideArquivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Arquivo[ ideArquivo=" + ideArquivo + " ]";
    }

}
