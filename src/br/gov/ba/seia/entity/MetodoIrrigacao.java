package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Entity implementation class for Entity: MetodoIrrigacao
 *
 */
@Entity
@Table(name = "metodo_irrigacao", uniqueConstraints = {@UniqueConstraint(columnNames = {"ide_metodo_irrigacao"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "MetodoIrrigacao.findAll", query = "SELECT m FROM MetodoIrrigacao m"),
	@NamedQuery(name = "MetodoIrrigacaofindByIdeMetodoIrrigacao", query = "SELECT m FROM MetodoIrrigacao m WHERE m.ideMetodoIrrigacao = :ideMetodoIrrigacao"),
	@NamedQuery(name = "MetodoIrrigacao.findByDscMetodoIrrigacao", query = "SELECT m FROM MetodoIrrigacao m WHERE m.dscMetodoIrrigacao = :dscMetodoIrrigacao")
})
public class MetodoIrrigacao extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_metodo_irrigacao", nullable = false)
	private Integer ideMetodoIrrigacao;

	@Historico(name="Método de Irrigação")
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "dsc_metodo_irrigacao", nullable = false, length = 100)
	private String dscMetodoIrrigacao;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideMetodoIrrigacao")
	private Collection<AreaProdutivaTipologiaAtividadeAgricultura> AreaProdutivaTipologiaAtividadeAgriculturaCollection;


	public MetodoIrrigacao() {
	}

	public MetodoIrrigacao(Integer ideMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}

	public MetodoIrrigacao(Integer ideMetodoIrrigacao, String dscMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
		this.dscMetodoIrrigacao = dscMetodoIrrigacao;
	}

	public MetodoIrrigacao(String dscMetodoIrrigacao) {
		this.dscMetodoIrrigacao = dscMetodoIrrigacao;
	}

	public Integer getIdeMetodoIrrigacao() {
		return this.ideMetodoIrrigacao;
	}

	public void setIdeMetodoIrrigacao(Integer ideMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}

	public String getDscMetodoIrrigacao() {
		return this.dscMetodoIrrigacao;
	}

	public void setDscMetodoIrrigacao(String dscMetodoIrrigacao) {
		this.dscMetodoIrrigacao = dscMetodoIrrigacao;
	}

	public Collection<AreaProdutivaTipologiaAtividadeAgricultura> getAreaProdutivaTipologiaAtividadeAgriculturaCollection() {
		return AreaProdutivaTipologiaAtividadeAgriculturaCollection;
	}

	public void setAreaProdutivaTipologiaAtividadeAgriculturaCollection(
			Collection<AreaProdutivaTipologiaAtividadeAgricultura> areaProdutivaTipologiaAtividadeAgriculturaCollection) {
		AreaProdutivaTipologiaAtividadeAgriculturaCollection = areaProdutivaTipologiaAtividadeAgriculturaCollection;
	}

	// Controlador de Tela
	public boolean isOutros(){
		return dscMetodoIrrigacao.compareTo("Outros") == 0;
	}
}
