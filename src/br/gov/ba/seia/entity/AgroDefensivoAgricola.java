package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the agro_defensivo_agricola database table.
 * 
 */
@Entity
@Table(name="agro_defensivo_agricola")
@NamedQueries({
	@NamedQuery(name = "AgroDefensivoAgricola.removerByIdeFceAgro", query ="DELETE FROM AgroDefensivoAgricola a WHERE a.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril") })
public class AgroDefensivoAgricola implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgroDefensivoAgricolaPK agroDefensivoAgricolaPK;


	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	@NotNull
	@JoinColumn(name="ide_tipo_defensivo_agricola", referencedColumnName = "ide_tipo_defensivo_agricola", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoDefensivoAgricola ideTipoDefensivoAgricola;

	public AgroDefensivoAgricola() {
	}

	public AgroDefensivoAgricola (AgroDefensivoAgricolaPK agroDefensivoAgricolaPK){
		this.agroDefensivoAgricolaPK=agroDefensivoAgricolaPK;
	}


	public AgroDefensivoAgricolaPK getAgroDefensivoAgricolaPK() {
		return agroDefensivoAgricolaPK;
	}


	public void setAgroDefensivoAgricolaPK(AgroDefensivoAgricolaPK agroDefensivoAgricolaPK) {
		this.agroDefensivoAgricolaPK = agroDefensivoAgricolaPK;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public TipoDefensivoAgricola getIdeTipoDefensivoAgricola() {
		return ideTipoDefensivoAgricola;
	}

	public void setIdeTipoDefensivoAgricola(TipoDefensivoAgricola ideTipoDefensivoAgricola) {
		this.ideTipoDefensivoAgricola = ideTipoDefensivoAgricola;
	}

}