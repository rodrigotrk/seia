package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "posto_combustivel_tanque_produto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PostoCombustivelTanqueProduto.findAll", query = "SELECT p FROM PostoCombustivelTanqueProduto p"),
		@NamedQuery(name = "PostoCombustivelTanqueProduto.removerByIde", query = "delete from PostoCombustivelTanqueProduto p where p.idePostoTanqueProduto = :ideTanqueProduto"),
		@NamedQuery(name = "PostoCombustivelTanqueProduto.findByIdePostoTanqueProduto", query = "SELECT p FROM PostoCombustivelTanqueProduto p WHERE p.idePostoTanqueProduto = :idePostoTanqueProduto") })
public class PostoCombustivelTanqueProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tanque_produto_seq")
	@SequenceGenerator(name = "tanque_produto_seq", sequenceName = "posto_combustivel_tanque_produto_seq", allocationSize = 1)
	@Column(name = "ide_posto_tanque_produto")
	private Integer idePostoTanqueProduto;

	@JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto")
	@ManyToOne(optional = false)
	private Produto ideProduto;

	@Column(name = "val_capacidade")
	private BigDecimal valCapacidade;

	@JoinColumn(name = "ide_tanque", referencedColumnName = "ide_tanque")
	@ManyToOne(optional = false)
	private PostoCombustivelTanque postoCombustivelTanque;

	public PostoCombustivelTanqueProduto() {
	}

	public PostoCombustivelTanqueProduto(Integer idePostoTanqueProduto) {
		this.idePostoTanqueProduto = idePostoTanqueProduto;
	}

	public Integer getIdePostoTanqueProduto() {
		return idePostoTanqueProduto;
	}

	public void setIdePostoTanqueProduto(Integer idePostoTanqueProduto) {
		this.idePostoTanqueProduto = idePostoTanqueProduto;
	}

	public Produto getIdeProduto() {
		return ideProduto;
	}

	public void setIdeProduto(Produto ideProduto) {
		this.ideProduto = ideProduto;
	}

	public BigDecimal getValCapacidade() {
		return valCapacidade;
	}

	public String getValCapacidadeFormatado() {
		Locale ptBr = new Locale("pt", "BR");
		NumberFormat formater = NumberFormat.getInstance(ptBr);
		return Util.isNull(valCapacidade) ? null  : formater.format(valCapacidade);
	}

	
	public void setValCapacidade(BigDecimal valCapacidade) {
		this.valCapacidade = valCapacidade;
	}

	public PostoCombustivelTanque getPostoCombustivelTanque() {
		return postoCombustivelTanque;
	}

	public void setPostoCombustivelTanque(PostoCombustivelTanque postoCombustivelTanque) {
		this.postoCombustivelTanque = postoCombustivelTanque;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idePostoTanqueProduto != null ? idePostoTanqueProduto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof PostoCombustivelTanqueProduto)) {
			return false;
		}
		PostoCombustivelTanqueProduto other = (PostoCombustivelTanqueProduto) object;
		if ((this.idePostoTanqueProduto == null && other.idePostoTanqueProduto != null)
				|| (this.idePostoTanqueProduto != null && !this.idePostoTanqueProduto.equals(other.idePostoTanqueProduto))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.PostoCombustivelTanqueProduto[ idePostoTanqueProduto=" + idePostoTanqueProduto + " ]";
	}

}
