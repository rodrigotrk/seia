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
 * The persistent class for the agro_classificacao_toxicologica database table.
 * 
 */
@Entity
@Table(name="agro_classificacao_toxicologica")
@NamedQueries({
	@NamedQuery(name = "AgroClassificacaoToxicologica.removerByIdeFceAgro", query ="DELETE FROM AgroClassificacaoToxicologica a WHERE a.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril") })

public class AgroClassificacaoToxicologica implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgroClassificacaoToxicologicaPK agroClassificacaoToxicologicaPK;

	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	//bi-directional many-to-one association to ClassificacaoToxicologica
	@NotNull
	@JoinColumn(name="ide_classificacao_toxicologica", referencedColumnName = "ide_classificacao_toxicologica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch=FetchType.LAZY)
	private ClassificacaoToxicologica ideClassificacaoToxicologica;

	public AgroClassificacaoToxicologica() {
	}

	public AgroClassificacaoToxicologica(AgroClassificacaoToxicologicaPK agroClassificacaoToxicologicaPK){
		this.agroClassificacaoToxicologicaPK=agroClassificacaoToxicologicaPK;
	}


	public AgroClassificacaoToxicologicaPK getAgroClassificacaoToxicologicaPK() {
		return agroClassificacaoToxicologicaPK;
	}


	public void setAgroClassificacaoToxicologicaPK(AgroClassificacaoToxicologicaPK agroClassificacaoToxicologicaPK) {
		this.agroClassificacaoToxicologicaPK = agroClassificacaoToxicologicaPK;
	}


	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}


	public void setIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}


	public ClassificacaoToxicologica getIdeClassificacaoToxicologica() {
		return ideClassificacaoToxicologica;
	}


	public void setIdeClassificacaoToxicologica(ClassificacaoToxicologica ideClassificacaoToxicologica) {
		this.ideClassificacaoToxicologica = ideClassificacaoToxicologica;
	}

}