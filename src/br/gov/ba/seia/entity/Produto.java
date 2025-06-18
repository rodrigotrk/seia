package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "produto")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
	@NamedQuery(name = "Produto.findByideProduto", query = "SELECT p FROM Produto p WHERE p.ideProduto = :ideProduto"),
	@NamedQuery(name = "Produto.findByDscProduto", query = "SELECT p FROM Produto p WHERE p.dscProduto = :dscProduto"),
	@NamedQuery(name = "Produto.findByIdeTipoProduto", query = "SELECT p FROM Produto p WHERE p.ideTipoProduto = :ideTipoProduto order by p.dscProduto asc"),
	@NamedQuery(name = "Produto.findByNumOnu", query = "SELECT p FROM Produto p WHERE p.numOnu = :numOnu")})
public class Produto implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "ide_produto")
	private Integer ideProduto;
	
	@Basic(optional = false)
	@Column(name = "dsc_produto")
	private String dscProduto;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProduto")
	private Collection<BombaCombustivel> bombaCombustivelCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "produto")
	private Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutosComercializadosCollection;
	
	// Lac Transporte
	@Column(name = "num_onu", length = 50)
	private String numOnu;
	
	@Column(name = "dsc_classe_risco", length = 50)
	private String dscClasseRisco;
	
	@JoinColumn(name = "ide_tipo_produto", referencedColumnName = "ide_tipo_produto", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoProduto ideTipoProduto;

	@Transient
	private Double qtdTransporteAnual;
	@Transient
	private boolean desabilitaQtd;
	@Transient
	private boolean outro;

	public Produto() {
	}

	public Produto(Integer ideProduto) {
		this.ideProduto = ideProduto;
	}

	public Produto(String string) {
		this.dscProduto = string;
	}

	public Produto(Integer ideProduto, String dscProduto) {
		this.ideProduto = ideProduto;
		this.dscProduto = dscProduto;
	}

	public Integer getIdeProduto() {
		return ideProduto;
	}

	public void setIdeProduto(Integer ideProduto) {
		this.ideProduto = ideProduto;
	}

	public String getDscProduto() {
		return dscProduto;
	}

	public void setDscProduto(String dscProduto) {
		this.dscProduto = dscProduto;
	}

	@XmlTransient
	public Collection<BombaCombustivel> getBombaCombustivelCollection() {
		return bombaCombustivelCollection;
	}

	public void setBombaCombustivelCollection(Collection<BombaCombustivel> bombaCombustivelCollection) {
		this.bombaCombustivelCollection = bombaCombustivelCollection;
	}


	@XmlTransient
	public Collection<PostoCombustivelProdutoComercializado> getPostoCombustivelProdutosComercializadosCollection() {
		return postoCombustivelProdutosComercializadosCollection;
	}

	public void setPostoCombustivelProdutosComercializadosCollection(Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutosComercializadosCollection) {
		this.postoCombustivelProdutosComercializadosCollection = postoCombustivelProdutosComercializadosCollection;
	}

	public String getNumOnu() {
		return numOnu;
	}

	public void setNumOnu(String numOnu) {
		this.numOnu = numOnu;
	}

	public String getDscClasseRisco() {
		return dscClasseRisco;
	}

	public void setDscClasseRisco(String dscClasseRisco) {
		this.dscClasseRisco = dscClasseRisco;
	}

	public TipoProduto getIdeTipoProduto() {
		return ideTipoProduto;
	}

	public void setIdeTipoProduto(TipoProduto ideTipoProduto) {
		this.ideTipoProduto = ideTipoProduto;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideProduto != null ? ideProduto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof Produto)) {
			return false;
		}
		Produto other = (Produto) object;
		if ((this.ideProduto == null && other.ideProduto != null) || (this.ideProduto != null && !this.ideProduto.equals(other.ideProduto))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Produtos[ ideProduto=" + ideProduto + " ]";
	}

	public boolean isDesabilitaQtd() {
		return desabilitaQtd;
	}

	public void setDesabilitaQtd(boolean desabilitaQtd) {
		if(desabilitaQtd && Util.isNullOuVazio(getQtdTransporteAnual())){
			JsfUtil.addWarnMessage("A quantidade média anual do produto transportado deve ser maior que zero.");
		} else {
			this.desabilitaQtd = desabilitaQtd;
		}
	}

	public boolean isOutro() {
		return outro;
	}

	public void setOutro(boolean outro) {
		this.outro = outro;
	}

	public Double getQtdTransporteAnual() {
		return qtdTransporteAnual;
	}

	public void setQtdTransporteAnual(Double qtdTransporteAnual) {
		this.qtdTransporteAnual = qtdTransporteAnual;
	}

	@Override
	public Long getId() {
		return new Long(ideProduto);
	}
}