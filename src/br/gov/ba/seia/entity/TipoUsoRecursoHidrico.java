package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;

@Entity
@Table(name="tipo_uso_recurso_hidrico")
public class TipoUsoRecursoHidrico extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_uso_recurso_hidrico")
	private Integer ideTipoUsoRecursoHidrico;

	@Column(name="dsc_tipo_uso_recurso_hidrico")
	@Historico(name="Tipo Uso de Recurso Hídrico", key=true)
	private String dscTipoUsoRecursoHidrico;

	@OneToMany(mappedBy="ideTipoUsoRecursoHidrico")
	private Collection<CerhTipoUso> cerhTipoUsoCollection;

	@ManyToOne
	@JoinColumn(name="ide_sefaz_codigo_receita")
	private SefazCodigoReceita cerhCodigoReceita;
	
	@Transient
	private boolean contemNaLista;

	public TipoUsoRecursoHidrico() {
	}
	
	public TipoUsoRecursoHidrico(Integer ideTipoUsoRecursoHidrico) {
		this.ideTipoUsoRecursoHidrico = ideTipoUsoRecursoHidrico;
	}

	public TipoUsoRecursoHidrico(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		this.ideTipoUsoRecursoHidrico = tipoUsoRecursoHidricoEnum.getId();
	}

	public Integer getIdeTipoUsoRecursoHidrico() {
		return ideTipoUsoRecursoHidrico;
	}

	public void setIdeTipoUsoRecursoHidrico(Integer ideTipoUsoRecursoHidrico) {
		this.ideTipoUsoRecursoHidrico = ideTipoUsoRecursoHidrico;
	}

	public String getDscTipoUsoRecursoHidrico() {
		return dscTipoUsoRecursoHidrico;
	}

	public void setDscTipoUsoRecursoHidrico(String dscTipoUsoRecursoHidrico) {
		this.dscTipoUsoRecursoHidrico = dscTipoUsoRecursoHidrico;
	}

	public Collection<CerhTipoUso> getCerhTipoUsoCollection() {
		return cerhTipoUsoCollection;
	}

	public void setCerhTipoUsoCollection(Collection<CerhTipoUso> cerhTipoUsoCollection) {
		this.cerhTipoUsoCollection = cerhTipoUsoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoUsoRecursoHidrico == null) ? 0
						: ideTipoUsoRecursoHidrico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoUsoRecursoHidrico other = (TipoUsoRecursoHidrico) obj;
		if (ideTipoUsoRecursoHidrico == null) {
			if (other.ideTipoUsoRecursoHidrico != null)
				return false;
		} else if (!ideTipoUsoRecursoHidrico
				.equals(other.ideTipoUsoRecursoHidrico))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideTipoUsoRecursoHidrico);
	}

	@Override
	public Integer getId() {
		return ideTipoUsoRecursoHidrico;
	}

	/**
	 * @return the cerhCodigoReceita
	 */
	public SefazCodigoReceita getCerhCodigoReceita() {
		return cerhCodigoReceita;
	}

	/**
	 * @param cerhCodigoReceita the cerhCodigoReceita to set
	 */
	public void setCerhCodigoReceita(SefazCodigoReceita cerhCodigoReceita) {
		this.cerhCodigoReceita = cerhCodigoReceita;
	}

	public boolean isContemNaLista() {
		return contemNaLista;
	}

	public void setContemNaLista(boolean contemNaLista) {
		this.contemNaLista = contemNaLista;
	}

	
}