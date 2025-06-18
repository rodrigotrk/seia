package br.gov.ba.seia.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "endereco_pessoa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnderecoPessoa.findAll", query = "SELECT e FROM EnderecoPessoa e"),
    @NamedQuery(name = "EnderecoPessoa.findByIdeEnderecoPessoa", query = "SELECT e FROM EnderecoPessoa e WHERE e.ideEnderecoPessoa = :ideEnderecoPessoa"),
    @NamedQuery(name = "EnderecoPessoa.findByIdePessoa", query = "SELECT e FROM EnderecoPessoa e WHERE e.idePessoa.idePessoa = :idePessoa")
})
public class EnderecoPessoa extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ENDERECO_PESSOA_IDE_ENDERECO_PESSOA_seq")    
    @SequenceGenerator(name="ENDERECO_PESSOA_IDE_ENDERECO_PESSOA_seq", sequenceName="ENDERECO_PESSOA_IDE_ENDERECO_PESSOA_seq", allocationSize=1)
    @Column(name = "ide_endereco_pessoa", nullable = false)
    private Integer ideEnderecoPessoa;
    
    @JoinColumn(name = "ide_tipo_endereco", referencedColumnName = "ide_tipo_endereco", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoEndereco ideTipoEndereco;
    
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa idePessoa;
    
    @JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Endereco ideEndereco;

    public EnderecoPessoa() {
    }

    public EnderecoPessoa(Pessoa idePessoa, Endereco ideEndereco) {
		this.idePessoa = idePessoa;
		this.ideEndereco = ideEndereco;
		this.ideTipoEndereco = new TipoEndereco(TipoEnderecoEnum.RESIDENCIAL.getId());
	}

	public EnderecoPessoa(Integer ideEnderecoPessoa) {
        this.ideEnderecoPessoa = ideEnderecoPessoa;
    }

    public Integer getIdeEnderecoPessoa() {
        return ideEnderecoPessoa;
    }

    public void setIdeEnderecoPessoa(Integer ideEnderecoPessoa) {
        this.ideEnderecoPessoa = ideEnderecoPessoa;
    }

    public TipoEndereco getIdeTipoEndereco() {
        return ideTipoEndereco;
    }

    public void setIdeTipoEndereco(TipoEndereco ideTipoEndereco) {
        this.ideTipoEndereco = ideTipoEndereco;
    }

    public Pessoa getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Pessoa idePessoa) {
        this.idePessoa = idePessoa;
    }

    public Endereco getIdeEndereco() {
        return ideEndereco;
    }

    public void setIdeEndereco(Endereco ideEndereco) {
        this.ideEndereco = ideEndereco;
    }
}
