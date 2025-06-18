package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.duarte
 */
@Entity
@Table(name = "importacao_cda_cefir_processo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportacaoCdaCefirProcesso.findAll", query = "SELECT i FROM ImportacaoCdaCefirProcesso i"),
    @NamedQuery(name = "ImportacaoCdaCefirProcesso.findByNumProcessoImportacaoCdaCefir", query = "SELECT i FROM ImportacaoCdaCefirProcesso i WHERE i.numProcessoImportacaoCdaCefir = :numProcessoImportacaoCdaCefir")})  

public class ImportacaoCdaCefirProcesso implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "num_processo_importacao_cda_cefir", nullable = false)
	private Integer numProcessoImportacaoCdaCefir;
			
	public ImportacaoCdaCefirProcesso() {
		
	}
	
	public ImportacaoCdaCefirProcesso(Integer numProcessoImportacaoCdaCefir) {
		this.numProcessoImportacaoCdaCefir = numProcessoImportacaoCdaCefir;
	}

	public Integer getNumProcessoImportacaoCdaCefir() {
		return numProcessoImportacaoCdaCefir;
	}

	public void setNumProcessoImportacaoCdaCefir(Integer numProcessoImportacaoCdaCefir) {
		this.numProcessoImportacaoCdaCefir = numProcessoImportacaoCdaCefir;
	}
	
	@Override
    public boolean equals(Object object) {
        if (!(object instanceof ImportacaoCdaCefir)) {
            return false;
        }
        ImportacaoCdaCefirProcesso other = (ImportacaoCdaCefirProcesso) object;
        if ((this.numProcessoImportacaoCdaCefir == null && other.numProcessoImportacaoCdaCefir != null) || (this.numProcessoImportacaoCdaCefir != null && !this.numProcessoImportacaoCdaCefir.equals(other.numProcessoImportacaoCdaCefir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ImportacaoCdaCefirProcessos[ numProcessoImportacaoCdaCefir=" + numProcessoImportacaoCdaCefir + " ]";
    }

}
