package br.gov.ba.seia.entity;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "outros_passivos_ambientais", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"ide_outros_passivos_ambientais"})})
@NamedQueries({
	@NamedQuery(name = "OutrosPassivosAmbientais.findByAll", query = "SELECT o FROM OutrosPassivosAmbientais o"),
	@NamedQuery(name = "OutrosPassivosAmbientais.findByIdeOutrosPassivosAmbientais", query = "SELECT o FROM OutrosPassivosAmbientais o WHERE o.ideOutrosPassivosAmbientais = :ideOutrosPassivosAmbientais"),
	@NamedQuery(name = "OutrosPassivosAmbientais.findByIdeImovelRural", query = "SELECT o FROM OutrosPassivosAmbientais o WHERE o.ideImovelRural = :ideImovelRural")})
public class OutrosPassivosAmbientais {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OUTROS_PASSIVOS_AMBIENTAIS_SEQ") 
    @SequenceGenerator(name="OUTROS_PASSIVOS_AMBIENTAIS_SEQ", sequenceName="OUTROS_PASSIVOS_AMBIENTAIS_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_outros_passivos_ambientais", nullable = false)
	private Integer ideOutrosPassivosAmbientais;
	
    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private ImovelRural ideImovelRural;
    
    @JoinColumn(name = "ide_documento_prad", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentoImovelRural ideDocumentoPrad;

    @OneToOne(mappedBy = "ideOutrosPassivosAmbientais")
	private CronogramaRecuperacao cronogramaRecuperacao;
    
    @Column(name = "ind_excluido")
    private boolean indExcluido = false;
    
	public OutrosPassivosAmbientais(){
    	
    }
    
    public OutrosPassivosAmbientais(Integer ideOutrosPassivosAmbientais){
    	this.ideOutrosPassivosAmbientais = ideOutrosPassivosAmbientais;
    }
    
	public Integer getIdeOutrosPassivosAmbientais() {
		return ideOutrosPassivosAmbientais;
	}

	public void setIdeOutrosPassivosAmbientais(Integer ideOutrosPassivosAmbientais) {
		this.ideOutrosPassivosAmbientais = ideOutrosPassivosAmbientais;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public DocumentoImovelRural getIdeDocumentoPrad() {
		return ideDocumentoPrad;
	}

	public void setIdeDocumentoPrad(DocumentoImovelRural ideDocumentoPrad) {
		this.ideDocumentoPrad = ideDocumentoPrad;
	}

	public CronogramaRecuperacao getCronogramaRecuperacao() {
		return cronogramaRecuperacao;
	}

	public void setCronogramaRecuperacao(CronogramaRecuperacao cronogramaRecuperacao) {
		this.cronogramaRecuperacao = cronogramaRecuperacao;
	}
	
    public boolean isIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOutrosPassivosAmbientais != null ? ideOutrosPassivosAmbientais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof OutrosPassivosAmbientais)) {
            return false;
        }
        OutrosPassivosAmbientais other = (OutrosPassivosAmbientais) object;
        if ((this.ideOutrosPassivosAmbientais == null && other.ideOutrosPassivosAmbientais != null) || (this.ideOutrosPassivosAmbientais != null && !this.ideOutrosPassivosAmbientais.equals(other.ideOutrosPassivosAmbientais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideOutrosPassivosAmbientais);
    }
}
