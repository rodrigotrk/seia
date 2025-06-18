package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "endereco_empreendimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnderecoEmpreendimento.findAll", query = "SELECT e FROM EnderecoEmpreendimento e"),
    @NamedQuery(name = "EnderecoEmpreendimento.findByIdeEmpreendimento", query = "SELECT e FROM EnderecoEmpreendimento e WHERE e.ideEmpreendimento.ideEmpreendimento = :ideEmpreendimento"),
    @NamedQuery(name = "EnderecoEmpreendimento.findByIdeEmpreendimentoIdeTipoEnderecoTipo", query = "SELECT e FROM EnderecoEmpreendimento e WHERE e.ideEmpreendimento.ideEmpreendimento = :ideEmpreendimento and e.ideTipoEndereco.ideTipoEndereco = :ideTipoEndereco"),
    @NamedQuery(name = "EnderecoEmpreendimento.findByIdeEnderecoEmpreendimento", query = "SELECT e FROM EnderecoEmpreendimento e WHERE e.ideEnderecoEmpreendimento = :ideEnderecoEmpreendimento")})
public class EnderecoEmpreendimento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ENDERECO_EMPREENDIMENTO_IDE_ENDERECO_EMPREENDIMENTO_seq")    
    @SequenceGenerator(name="ENDERECO_EMPREENDIMENTO_IDE_ENDERECO_EMPREENDIMENTO_seq", sequenceName="ENDERECO_EMPREENDIMENTO_IDE_ENDERECO_EMPREENDIMENTO_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_endereco_empreendimento", nullable = false)
    private Integer ideEnderecoEmpreendimento;
    
    @JoinColumn(name = "ide_tipo_endereco", referencedColumnName = "ide_tipo_endereco", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoEndereco ideTipoEndereco;
    
    @JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Endereco ideEndereco;
    
    @JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empreendimento ideEmpreendimento;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEnderecoEmpreendimento", fetch = FetchType.LAZY)
	private Collection<EnderecoEmpreendimentoMunicipio> enderecoEmpreendimentoMunicipioCollection;

    public EnderecoEmpreendimento() {
    }
    public EnderecoEmpreendimento(Integer ideEnderecoEmpreendimento) {
        this.ideEnderecoEmpreendimento = ideEnderecoEmpreendimento;
    }
    public EnderecoEmpreendimento(Empreendimento ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }
    public EnderecoEmpreendimento(Endereco ideEndereco, Empreendimento ideEmpreendimento) {
    	this.ideEndereco = ideEndereco;
        this.ideEmpreendimento = ideEmpreendimento;
    }
    public EnderecoEmpreendimento(Empreendimento ideEmpreendimento, TipoEndereco pTipoEndereco) {
        this.ideEmpreendimento = ideEmpreendimento;
        this.ideTipoEndereco = pTipoEndereco;
    }
    public EnderecoEmpreendimento(TipoEndereco pTipoEndereco, Endereco pEndereco, Empreendimento pEmpreendimento) {
    	this.ideTipoEndereco = pTipoEndereco;
    	this.ideEndereco = pEndereco;
    	this.ideEmpreendimento = pEmpreendimento;
    }

    public Integer getIdeEnderecoEmpreendimento() {
        return ideEnderecoEmpreendimento;
    }

    public void setIdeEnderecoEmpreendimento(Integer ideEnderecoEmpreendimento) {
        this.ideEnderecoEmpreendimento = ideEnderecoEmpreendimento;
    }

    public TipoEndereco getIdeTipoEndereco() {
        return ideTipoEndereco;
    }

    public void setIdeTipoEndereco(TipoEndereco ideTipoEndereco) {
        this.ideTipoEndereco = ideTipoEndereco;
    }

    public Endereco getIdeEndereco() {
        return ideEndereco;
    }

    public void setIdeEndereco(Endereco ideEndereco) {
        this.ideEndereco = ideEndereco;
    }

    public Empreendimento getIdeEmpreendimento() {
        return ideEmpreendimento;
    }

    public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEnderecoEmpreendimento != null ? ideEnderecoEmpreendimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EnderecoEmpreendimento)) {
            return false;
        }
        EnderecoEmpreendimento other = (EnderecoEmpreendimento) object;
        if ((this.ideEnderecoEmpreendimento == null && other.ideEnderecoEmpreendimento != null) || (this.ideEnderecoEmpreendimento != null && !this.ideEnderecoEmpreendimento.equals(other.ideEnderecoEmpreendimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideEnderecoEmpreendimento);
    }
}
