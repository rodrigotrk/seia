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
 * The persistent class for the fce_asv_classi_vegetacao database table.
 * 
 */
@Entity
@Table(name="fce_asv_classi_vegetacao")
@NamedQueries({	
	@NamedQuery(name = "FceAsvClassiVegetacao.removerByIdeFceAsv", query ="DELETE FROM FceAsvClassiVegetacao f WHERE f.ideFceAsv = :ideFceAsv") }) 

public class FceAsvClassiVegetacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAsvClassiVegetacaoPK fceAsvClassiVegetacaoPK;
	
	@NotNull
	@JoinColumn(name = "ide_fce_asv", referencedColumnName = "ide_fce_asv", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAsv ideFceAsv;

	@NotNull
	@JoinColumn(name = "ide_classificacao_vegetacao", referencedColumnName = "ide_classificacao_vegetacao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ClassificacaoVegetacao ideClassificacaoVegetacao;
	
	public FceAsvClassiVegetacao (){
	}
	
    public FceAsvClassiVegetacao(FceAsvClassiVegetacaoPK fceAsvClassiVegetacaoPK){
    	this.fceAsvClassiVegetacaoPK = fceAsvClassiVegetacaoPK;
    }

	public FceAsv getIdeFceAsv() {
		return ideFceAsv;
	}

	public void setIdeFceAsv(FceAsv ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public ClassificacaoVegetacao getIdeClassificacaoVegetacao() {
		return ideClassificacaoVegetacao;
	}

	public void setIdeClassificacaoVegetacao(
			ClassificacaoVegetacao ideClassificacaoVegetacao) {
		this.ideClassificacaoVegetacao = ideClassificacaoVegetacao;
	}

	public FceAsvClassiVegetacaoPK getFceAsvClassiVegetacaoPK() {
		return fceAsvClassiVegetacaoPK;
	}

	public void setFceAsvClassiVegetacaoPK(
			FceAsvClassiVegetacaoPK fceAsvClassiVegetacaoPK) {
		this.fceAsvClassiVegetacaoPK = fceAsvClassiVegetacaoPK;
	}

}