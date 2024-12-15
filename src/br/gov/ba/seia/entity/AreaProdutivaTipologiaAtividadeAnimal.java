package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: AreaProdutivaTipologiaAtividadeAnimal
 *
 */
@Entity
@Table(name = "area_produtiva_tipologia_atividade_animal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAnimal.findAll", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAnimal a"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAnimal.findByIdeAreaProdutivaTipologiaAtividadeAnimal", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAnimal a WHERE a.ideAreaProdutivaTipologiaAtividadeAnimal = :ideAreaProdutivaTipologiaAtividadeAnimal"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAnimal.findByAreaProdutivaTipologiaAtividade", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAnimal a WHERE a.ideAreaProdutivaTipologiaAtividade = :ideAreaProdutivaTipologiaAtividade"),
	@NamedQuery(name = "AreaProdutivaTipologiaAtividadeAnimal.findByManejo", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAnimal a WHERE a.ideManejo = :ideManejo")})

public class AreaProdutivaTipologiaAtividadeAnimal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_ANIMAL_SEQ") 
    @SequenceGenerator(name="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_ANIMAL_SEQ", sequenceName="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_ANIMAL_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_area_produtiva_tipologia_atividade_animal", nullable = false)
	private Integer ideAreaProdutivaTipologiaAtividadeAnimal;	

	@JoinColumn(name = "ide_area_produtiva_tipologia_atividade", referencedColumnName = "ide_area_produtiva_tipologia_atividade")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade;
	
	@JoinColumn(name = "ide_manejo", referencedColumnName = "ide_manejo")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Manejo ideManejo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "qtd_cabeca")
    private Integer qtdCabeca;

    @Basic(optional = false)
    @Column(name = "tipo_uso_agua")
    private Integer tipoUsoAgua;
    
    @Basic(optional = false)
    @Column(name = "ind_manejo_cria")
    private boolean indManejoCria;
    
    @Basic(optional = false)
    @Column(name = "ind_manejo_engorda")
    private boolean indManejoEngorda;
    
    @Basic(optional = false)
    @Column(name = "ind_manejo_recria")
    private boolean indManejoRecria;
    
    @Basic(optional = false)
    @Column(name = "ind_manejo_reproducao")
    private boolean indManejoReproducao;
		
	public AreaProdutivaTipologiaAtividadeAnimal() {
	}	
	
	public AreaProdutivaTipologiaAtividadeAnimal(
			Integer ideAreaProdutivaTipologiaAtividadeAnimal,
			AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade,
			Manejo ideManejo,
			Integer qtdCabeca,
			Integer tipoUsoAgua,
			boolean indManejoCria,
			boolean indManejoEngorda,
			boolean indManejoRecria,
			boolean indManejoReproducao) {
		this.ideAreaProdutivaTipologiaAtividadeAnimal = ideAreaProdutivaTipologiaAtividadeAnimal;
		this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
		this.ideManejo = ideManejo;
		this.qtdCabeca = qtdCabeca;
		this.tipoUsoAgua = tipoUsoAgua;
		this.indManejoCria = indManejoCria;
		this.indManejoEngorda = indManejoEngorda;
		this.indManejoRecria = indManejoRecria;
		this.indManejoReproducao = indManejoReproducao;
	}

	public Integer getIdeAreaProdutivaTipologiaAtividadeAnimal() {
		return ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	public void setIdeAreaProdutivaTipologiaAtividadeAnimal(
			Integer ideAreaProdutivaTipologiaAtividadeAnimal) {
		this.ideAreaProdutivaTipologiaAtividadeAnimal = ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	public AreaProdutivaTipologiaAtividade getIdeAreaProdutivaTipologiaAtividade() {
		return ideAreaProdutivaTipologiaAtividade;
	}

	public void setIdeAreaProdutivaTipologiaAtividade(
			AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade) {
		this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
	}

	public Manejo getIdeManejo() {
		return ideManejo;
	}

	public void setIdeManejo(Manejo ideManejo) {
		this.ideManejo = ideManejo;
	}

	public Integer getQtdCabeca() {
		return qtdCabeca;
	}

	public void setQtdCabeca(Integer qtdCabeca) {
		this.qtdCabeca = qtdCabeca;
	}

	public Integer getTipoUsoAgua() {
		return tipoUsoAgua;
	}

	public void setTipoUsoAgua(Integer tipoUsoAgua) {
		this.tipoUsoAgua = tipoUsoAgua;
	}

	public boolean isIndManejoCria() {
		return indManejoCria;
	}

	public void setIndManejoCria(boolean indManejoCria) {
		this.indManejoCria = indManejoCria;
	}

	public boolean isIndManejoEngorda() {
		return indManejoEngorda;
	}

	public void setIndManejoEngorda(boolean indManejoEngorda) {
		this.indManejoEngorda = indManejoEngorda;
	}

	public boolean isIndManejoRecria() {
		return indManejoRecria;
	}

	public void setIndManejoRecria(boolean indManejoRecria) {
		this.indManejoRecria = indManejoRecria;
	}

	public boolean isIndManejoReproducao() {
		return indManejoReproducao;
	}

	public void setIndManejoReproducao(boolean indManejoReproducao) {
		this.indManejoReproducao = indManejoReproducao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAreaProdutivaTipologiaAtividadeAnimal == null) ? 0
						: ideAreaProdutivaTipologiaAtividadeAnimal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AreaProdutivaTipologiaAtividadeAnimal))
			return false;
		AreaProdutivaTipologiaAtividadeAnimal other = (AreaProdutivaTipologiaAtividadeAnimal) obj;
		if (ideAreaProdutivaTipologiaAtividadeAnimal == null) {
			if (other.ideAreaProdutivaTipologiaAtividadeAnimal != null)
				return false;
		} else if (!ideAreaProdutivaTipologiaAtividadeAnimal
				.equals(other.ideAreaProdutivaTipologiaAtividadeAnimal))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "" + ideAreaProdutivaTipologiaAtividadeAnimal;
	}

}
