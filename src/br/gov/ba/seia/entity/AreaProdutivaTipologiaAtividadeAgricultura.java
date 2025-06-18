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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: AreaProdutivaTipologiaAtividadeAgricultura
 *
 */
@Entity
@Table(name = "area_produtiva_tipologia_atividade_agricultura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAgricultura.findAll", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAgricultura a"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAgricultura.findByIdeAreaProdutivaTipologiaAtividadeAgricultura", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAgricultura a WHERE a.ideAreaProdutivaTipologiaAtividadeAgricultura = :ideAreaProdutivaTipologiaAtividadeAgricultura"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAgricultura.findByAreaProdutivaTipologiaAtividade", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAgricultura a WHERE a.ideAreaProdutivaTipologiaAtividade = :ideAreaProdutivaTipologiaAtividade"),
    @NamedQuery(name = "AreaProdutivaTipologiaAtividadeAgricultura.findByMeses", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAgricultura a WHERE a.meses = :meses"),
	@NamedQuery(name = "AreaProdutivaTipologiaAtividadeAgricultura.findByMetodoIrrigacao", query = "SELECT a FROM AreaProdutivaTipologiaAtividadeAgricultura a WHERE a.ideMetodoIrrigacao = :ideMetodoIrrigacao")})
public class AreaProdutivaTipologiaAtividadeAgricultura implements Serializable {

	private static final long serialVersionUID = 1L;
	   
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_AGRICULTURA_SEQ") 
    @SequenceGenerator(name="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_AGRICULTURA_SEQ", sequenceName="AREA_PRODUTIVA_TIPOLOGIA_ATIVIDADE_AGRICULTURA_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_area_produtiva_tipologia_atividade_agricultura", nullable = false)
	private Integer ideAreaProdutivaTipologiaAtividadeAgricultura;

	@JoinColumn(name = "ide_area_produtiva_tipologia_atividade", referencedColumnName = "ide_area_produtiva_tipologia_atividade")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "meses", nullable = false, length = 30)
	private String meses;

	@JoinColumn(name = "ide_metodo_irrigacao", referencedColumnName = "ide_metodo_irrigacao")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private MetodoIrrigacao ideMetodoIrrigacao;
	
	
	public AreaProdutivaTipologiaAtividadeAgricultura() {
	}   

	public AreaProdutivaTipologiaAtividadeAgricultura(
			Integer ideAreaProdutivaTipologiaAtividadeAgricultura,
			AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade,
			String meses,
			MetodoIrrigacao ideMetodoIrrigacao,
			Integer numUnidadeProducao,
			DocumentoImovelRural ideDocumentoAutorizacaoManejo) {
		
				this.ideAreaProdutivaTipologiaAtividadeAgricultura = ideAreaProdutivaTipologiaAtividadeAgricultura;
				this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
				this.meses = meses;
				this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}

	public Integer getIdeAreaProdutivaTipologiaAtividadeAgricultura() {
		return this.ideAreaProdutivaTipologiaAtividadeAgricultura;
	}

	public void setIdeAreaProdutivaTipologiaAtividadeAgricultura(Integer ideAreaProdutivaTipologiaAtividadeAgricultura) {
		this.ideAreaProdutivaTipologiaAtividadeAgricultura = ideAreaProdutivaTipologiaAtividadeAgricultura;
	}   
	public AreaProdutivaTipologiaAtividade getIdeAreaProdutivaTipologiaAtividade() {
		return this.ideAreaProdutivaTipologiaAtividade;
	}

	public void setIdeAreaProdutivaTipologiaAtividade(AreaProdutivaTipologiaAtividade ideAreaProdutivaTipologiaAtividade) {
		this.ideAreaProdutivaTipologiaAtividade = ideAreaProdutivaTipologiaAtividade;
	}
	
	public MetodoIrrigacao getIdeMetodoIrrigacao() {
		return ideMetodoIrrigacao;
	}
	
	public void setIdeMetodoIrrigacao(MetodoIrrigacao ideMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}
	
	public String getMeses() {
		return meses;
	}
	
	public void setMeses(String meses) {
		this.meses = meses;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAreaProdutivaTipologiaAtividadeAgricultura == null) ? 0
						: ideAreaProdutivaTipologiaAtividadeAgricultura
								.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AreaProdutivaTipologiaAtividadeAgricultura))
			return false;
		AreaProdutivaTipologiaAtividadeAgricultura other = (AreaProdutivaTipologiaAtividadeAgricultura) obj;
		if (ideAreaProdutivaTipologiaAtividadeAgricultura == null) {
			if (other.ideAreaProdutivaTipologiaAtividadeAgricultura != null)
				return false;
		} else if (!ideAreaProdutivaTipologiaAtividadeAgricultura
				.equals(other.ideAreaProdutivaTipologiaAtividadeAgricultura))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "" + ideAreaProdutivaTipologiaAtividadeAgricultura;
	}

}
