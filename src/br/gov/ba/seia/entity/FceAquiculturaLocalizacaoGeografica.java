package br.gov.ba.seia.entity;

import java.io.Serializable;

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


/**
 * Classe que representa a Poligonal de Cultivo na Aba <i>Captação - Viveiro Escavado</i> do <b>FCE - Outorga para Aquicultura</b>.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="fce_aquicultura_localizacao_geografica")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLocalizacaoGeografica.findAll", query="SELECT f FROM FceAquiculturaLocalizacaoGeografica f"),
	@NamedQuery(name = "FceAquiculturaLocalizacaoGeografica.removeByIdeFceAquicultura", query = "DELETE FROM FceAquiculturaLocalizacaoGeografica f WHERE f.ideFceAquicultura.ideFceAquicultura = :ideFceAquicultura")})
public class FceAquiculturaLocalizacaoGeografica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALOCALIZACAOGEOGRAFICA_GENERATOR", sequenceName="FCE_AQUICULTURA_LOCALIZACAO_GEOGRAFICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LOCALIZACAO_GEOGRAFICA_IDEFCEAQUICULTURALOCALIZACAOGEOGRAFICA_GENERATOR")
	@Column(name="ide_fce_aquicultura_localizacao_geografica")
	private Integer ideFceAquiculturaLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura", referencedColumnName="ide_fce_aquicultura")
	private FceAquicultura ideFceAquicultura;

	public FceAquiculturaLocalizacaoGeografica() {
	}

	public FceAquiculturaLocalizacaoGeografica(FceAquicultura ideFceAquicultura) {
		this.ideFceAquicultura = ideFceAquicultura;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeFceAquiculturaLocalizacaoGeografica() {
		return this.ideFceAquiculturaLocalizacaoGeografica;
	}

	public void setIdeFceAquiculturaLocalizacaoGeografica(Integer ideFceAquiculturaLocalizacaoGeografica) {
		this.ideFceAquiculturaLocalizacaoGeografica = ideFceAquiculturaLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return this.ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public FceAquicultura getIdeFceAquicultura() {
		return this.ideFceAquicultura;
	}

	public void setIdeFceAquicultura(FceAquicultura fceAquicultura) {
		this.ideFceAquicultura = fceAquicultura;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceAquiculturaLocalizacaoGeografica == null) ? 0
						: ideFceAquiculturaLocalizacaoGeografica.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceAquiculturaLocalizacaoGeografica other = (FceAquiculturaLocalizacaoGeografica) obj;
		if (ideFceAquiculturaLocalizacaoGeografica == null) {
			if (other.ideFceAquiculturaLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideFceAquiculturaLocalizacaoGeografica
				.equals(other.ideFceAquiculturaLocalizacaoGeografica)) {
			return false;
		}
		return true;
	}

}