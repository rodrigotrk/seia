package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author carlos.duarte
 */
@Entity
@Table(name = "contrato_convenio_cefir")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContratoConvenioCefir.findAll", query = "SELECT c FROM ContratoConvenioCefir c"),
    @NamedQuery(name = "ContratoConvenioCefir.findByIdeContratoConvenioCefir", query = "SELECT c FROM ContratoConvenioCefir c WHERE c.ideContratoConvenioCefir = :ideContratoConvenioCefir"),
    @NamedQuery(name = "ContratoConvenioCefir.findByNomContratoConvenioCefir", query = "SELECT c FROM ContratoConvenioCefir c WHERE c.nomContratoConvenioCefir = :nomContratoConvenioCefir"),
    @NamedQuery(name = "ContratoConvenioCefir.findByNumContratoConvenioCefir", query = "SELECT c FROM ContratoConvenioCefir c WHERE c.numContratoConvenioCefir = :numContratoConvenioCefir"),
    @NamedQuery(name = "ContratoConvenioCefir.findByIdeGestorFinanceiroCefir", query = "SELECT c FROM ContratoConvenioCefir c WHERE c.ideGestorFinanceiroCefir = :ideGestorFinanceiroCefir")})
public class ContratoConvenioCefir implements Serializable {
    
	private static final long serialVersionUID = -5604350058110898219L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTRATO_CONVENIO_CEFIR_SEQ") 
    @SequenceGenerator(name="CONTRATO_CONVENIO_CEFIR_SEQ", sequenceName="CONTRATO_CONVENIO_CEFIR_SEQ", allocationSize=1)
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_contrato_convenio_cefir", nullable = false)
    private Integer ideContratoConvenioCefir;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "nom_contrato_convenio_cefir")
    private String nomContratoConvenioCefir;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "num_contrato_convenio_cefir")
    private String numContratoConvenioCefir;
    
    @JoinColumn(name = "ide_gestor_financeiro_cefir", referencedColumnName = "ide_gestor_financeiro_cefir", nullable = false)
	@ManyToOne(optional = false)
	private GestorFinanceiroCefir ideGestorFinanceiroCefir;
    
    @Column(name = "ind_projeto_bndes")
    private boolean indProjetoBndes;
    
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;
    
    @OneToMany(mappedBy = "ideContratoConvenioCefir")
    private Collection<ContratoConvenioCefirMunicipio> contratoConvenioCefirMunicipioCollection;
    
    @Transient
	private int qtdImovel;
    
    public ContratoConvenioCefir() {
    }

    public ContratoConvenioCefir(Integer ideContratoConvenioCefir) {
        this.ideContratoConvenioCefir = ideContratoConvenioCefir;
    }

    public ContratoConvenioCefir(Integer ideContratoConvenioCefir, String nomContratoConvenioCefir) {
        this.ideContratoConvenioCefir = ideContratoConvenioCefir;
        this.nomContratoConvenioCefir = nomContratoConvenioCefir;
    }
    
    public ContratoConvenioCefir(Integer ideContratoConvenioCefir, String nomContratoConvenioCefir, String numContratoConvenioCefir) {
        this.ideContratoConvenioCefir = ideContratoConvenioCefir;
        this.nomContratoConvenioCefir = nomContratoConvenioCefir;
        this.numContratoConvenioCefir = numContratoConvenioCefir;
    }


	public Integer getIdeContratoConvenioCefir() {
		return ideContratoConvenioCefir;
	}

	public void setIdeContratoConvenioCefir(Integer ideContratoConvenioCefir) {
		this.ideContratoConvenioCefir = ideContratoConvenioCefir;
	}

	public String getNomContratoConvenioCefir() {
		return nomContratoConvenioCefir;
	}

	public void setNomContratoConvenioCefir(String nomContratoConvenioCefir) {
		this.nomContratoConvenioCefir = nomContratoConvenioCefir;
	}

	public String getNumContratoConvenioCefir() {
		return numContratoConvenioCefir;
	}

	public void setNumContratoConvenioCefir(String numContratoConvenioCefir) {
		this.numContratoConvenioCefir = numContratoConvenioCefir;
	}

	public GestorFinanceiroCefir getIdeGestorFinanceiroCefir() {
		return ideGestorFinanceiroCefir;
	}

	public void setIdeGestorFinanceiroCefir(
			GestorFinanceiroCefir ideGestorFinanceiroCefir) {
		this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
	}

	public boolean isIndProjetoBndes() {
		return indProjetoBndes;
	}

	public void setIndProjetoBndes(boolean indProjetoBndes) {
		this.indProjetoBndes = indProjetoBndes;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public Collection<ContratoConvenioCefirMunicipio> getContratoConvenioCefirMunicipioCollection() {
		return contratoConvenioCefirMunicipioCollection;
	}

	public void setContratoConvenioCefirMunicipioCollection(
			Collection<ContratoConvenioCefirMunicipio> contratoConvenioCefirMunicipioCollection) {
		this.contratoConvenioCefirMunicipioCollection = contratoConvenioCefirMunicipioCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideContratoConvenioCefir != null ? ideContratoConvenioCefir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContratoConvenioCefir)) {
            return false;
        }
        ContratoConvenioCefir other = (ContratoConvenioCefir) object;
        if ((this.ideContratoConvenioCefir.equals(other.ideContratoConvenioCefir)) || (this.numContratoConvenioCefir.equals(other.numContratoConvenioCefir))) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "" + ideContratoConvenioCefir;
    }

	public int getQtdImovel() {
		return qtdImovel;
	}

	public void setQtdImovel(int qtdImovel) {
		this.qtdImovel = qtdImovel;
	}
}
