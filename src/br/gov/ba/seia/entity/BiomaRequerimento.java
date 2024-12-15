package br.gov.ba.seia.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "bioma_requerimento")
public class BiomaRequerimento extends AbstractEntity implements Cloneable {
    
	private static final long serialVersionUID = -7859268346418477199L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bioma_requerimento_ide_bioma_requerimento_seq")
	@SequenceGenerator(name = "bioma_requerimento_ide_bioma_requerimento_seq", sequenceName = "bioma_requerimento_ide_bioma_requerimento_seq", allocationSize = 1)
	@Column(name = "ide_bioma_requerimento", nullable = false)
    private Integer ideBiomaRequerimento;
    
    @Column(name = "val_area", nullable = false, length = 50)
    private Double valArea;
    
    @JoinColumn(name = "ide_bioma", referencedColumnName = "ide_bioma", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Bioma ideBioma;
    
    @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Requerimento ideRequerimento;
    
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @OneToOne(mappedBy = "ideBiomaRequerimento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental;
   
    public BiomaRequerimento() {
    	
    }
    
    public BiomaRequerimento(Integer ideBiomaRequerimento) {
    	this.ideBiomaRequerimento = ideBiomaRequerimento;
    }
    
    public BiomaRequerimento(Bioma ideBioma, Double valArea) {
    	this.ideBioma = ideBioma;
    	this.valArea = valArea;
    }

	public Integer getIdeBiomaRequerimento() {
		return ideBiomaRequerimento;
	}

	public void setIdeBiomaRequerimento(Integer ideBiomaRequerimento) {
		this.ideBiomaRequerimento = ideBiomaRequerimento;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public Bioma getIdeBioma() {
		return ideBioma;
	}

	public void setIdeBioma(Bioma ideBioma) {
		this.ideBioma = ideBioma;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public BiomaEnquadramentoAtoAmbiental getBiomaEnquadramentoAtoAmbiental() {
		return biomaEnquadramentoAtoAmbiental;
	}
	
	public void setBiomaEnquadramentoAtoAmbiental(
			BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental) {
		this.biomaEnquadramentoAtoAmbiental = biomaEnquadramentoAtoAmbiental;
	}
	
	@Override
	public BiomaRequerimento clone() throws CloneNotSupportedException {
		return (BiomaRequerimento) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((biomaEnquadramentoAtoAmbiental == null) ? 0 : biomaEnquadramentoAtoAmbiental.hashCode());
		result = prime * result + ((ideBioma == null) ? 0 : ideBioma.hashCode());
		result = prime * result + ((ideBiomaRequerimento == null) ? 0 : ideBiomaRequerimento.hashCode());
		result = prime * result + ((ideLocalizacaoGeografica == null) ? 0 : ideLocalizacaoGeografica.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		result = prime * result + ((valArea == null) ? 0 : valArea.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(Util.isNull(obj))
			return false;
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (obj!=null && getClass() != obj.getClass())
			return false;
		if(obj instanceof BiomaRequerimento) {
			BiomaRequerimento other = (BiomaRequerimento) obj;
			if (biomaEnquadramentoAtoAmbiental == null) {
				if (other.biomaEnquadramentoAtoAmbiental != null)
					return false;
			} else if (!biomaEnquadramentoAtoAmbiental.equals(other.biomaEnquadramentoAtoAmbiental))
				return false;
			if (ideBioma == null) {
				if (other.ideBioma != null)
					return false;
			} else if (!ideBioma.equals(other.ideBioma))
				return false;
			if (ideBiomaRequerimento == null) {
				if (other.ideBiomaRequerimento != null)
					return false;
			} else if (!ideBiomaRequerimento.equals(other.ideBiomaRequerimento))
				return false;
			if (ideLocalizacaoGeografica == null) {
				if (other.ideLocalizacaoGeografica != null)
					return false;
			} else if (!ideLocalizacaoGeografica.equals(other.ideLocalizacaoGeografica))
				return false;
			if (ideRequerimento == null) {
				if (other.ideRequerimento != null)
					return false;
			} else if (!ideRequerimento.equals(other.ideRequerimento))
				return false;
			if (valArea == null) {
				if (other.valArea != null)
					return false;
			} else if (!valArea.equals(other.valArea))
				return false;
			return true;
		}
		return false;
	}
	
}