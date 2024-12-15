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
 * The persistent class for the agro_classificacao_defensivo database table.
 * 
 */
@Entity
@Table(name="agro_classificacao_defensivo")
@NamedQueries({
	@NamedQuery(name = "AgroClassificacaoDefensivo.removerByIdeFceAgro", query ="DELETE FROM AgroClassificacaoDefensivo a WHERE a.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril") })
public class AgroClassificacaoDefensivo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgroClassificacaoDefensivoPK agroClassificacaoDefensivoPK;

	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	//bi-directional many-to-one association to ClassificacaoToxicologica
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_classificacao_defensivo_agricola", referencedColumnName = "ide_classificacao_defensivo_agricola", nullable = false, insertable = false, updatable = false)
	private ClassificacaoDefensivoAgricola ideClassificacaoDefensivoAgricola;

	public AgroClassificacaoDefensivo() {
	}

	public AgroClassificacaoDefensivo (AgroClassificacaoDefensivoPK agroClassificacaoDefensivoPK){
		this.agroClassificacaoDefensivoPK=agroClassificacaoDefensivoPK;
	}

	public AgroClassificacaoDefensivoPK getAgroClassificacaoDefensivoPK() {
		return agroClassificacaoDefensivoPK;
	}

	public void setAgroClassificacaoDefensivoPK(AgroClassificacaoDefensivoPK agroClassificacaoDefensivoPK) {
		this.agroClassificacaoDefensivoPK = agroClassificacaoDefensivoPK;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public ClassificacaoDefensivoAgricola getIdeClassificacaoDefensivoAgricola() {
		return ideClassificacaoDefensivoAgricola;
	}

	public void setIdeClassificacaoDefensivoAgricola(ClassificacaoDefensivoAgricola ideClassificacaoDefensivoAgricola) {
		this.ideClassificacaoDefensivoAgricola = ideClassificacaoDefensivoAgricola;
	}



}