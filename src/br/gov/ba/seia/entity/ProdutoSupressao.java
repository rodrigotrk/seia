package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the produto_supressao database table.
 * 
 */
@Entity
@Table(name="produto_supressao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProdutoSupressao.deleteByIdeFceAsv", query = "DELETE FROM ProdutoSupressao WHERE fceAsv = :fceAsv")})
public class ProdutoSupressao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_asv_produto_supressao_seq")
	@SequenceGenerator(name = "fce_asv_produto_supressao_seq", sequenceName = "fce_asv_produto_supressao_seq", allocationSize = 1)
	@Column(name="ide_produto_supressao")
	private Integer ideProdutoSupressao;

	@Column(name="num_em_app")
	private Double numEmApp;

	@Column(name="num_fora_app")
	private Double numForaApp;

	@Column(name="num_volume_total")
	private Double numVolumeTotal;

	//bi-directional many-to-one association to FceAsv
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_asv")
	private FceAsv fceAsv;

	@Transient
	private List<DestinoSocioeconomicoProduto> destinoSocioeconomicoProdutosSelecionados;

	@Transient
	private List<DestinoSocioeconomicoProduto> destinoSocioeconomicoProdutosBD;

	@Transient
	private boolean desabilitaQtd;

	@Transient
	private boolean edicao;

	@Transient
	private Produto produto;

	public ProdutoSupressao() {
		setDestinoSocioeconomicoProdutosBD(new ArrayList<DestinoSocioeconomicoProduto>());
	}

	public ProdutoSupressao(Integer ideProdutoSupressao) {
		this.ideProdutoSupressao = ideProdutoSupressao;
	}

	public Integer getIdeProdutoSupressao() {
		return this.ideProdutoSupressao;
	}

	public void setIdeProdutoSupressao(Integer ideProdutoSupressao) {
		this.ideProdutoSupressao = ideProdutoSupressao;
	}

	public FceAsv getFceAsv() {
		return this.fceAsv;
	}

	public void setFceAsv(FceAsv fceAsv) {
		this.fceAsv = fceAsv;
	}


	public Double getNumEmApp() {
		return numEmApp;
	}

	public void setNumEmApp(Double numEmApp) {
		this.numEmApp = numEmApp;
	}

	public Double getNumForaApp() {
		return numForaApp;
	}

	public void setNumForaApp(Double numForaApp) {
		this.numForaApp = numForaApp;
	}

	public Double getNumVolumeTotal() {
		return numVolumeTotal;
	}

	public void setNumVolumeTotal(Double numVolumeTotal) {
		this.numVolumeTotal = numVolumeTotal;
	}


	public boolean isDesabilitaQtd() {
		return desabilitaQtd;
	}

	public void setDesabilitaQtd(boolean desabilitaQtd) {
		this.desabilitaQtd = desabilitaQtd;
		if(desabilitaQtd){
			setEdicao(false);
		}
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<DestinoSocioeconomicoProduto> getDestinoSocioeconomicoProdutosSelecionados() {
		return destinoSocioeconomicoProdutosSelecionados;
	}

	public void setDestinoSocioeconomicoProdutosSelecionados(
			List<DestinoSocioeconomicoProduto> destinoSocioeconomicoProdutosSelecionados) {
		this.destinoSocioeconomicoProdutosSelecionados = destinoSocioeconomicoProdutosSelecionados;
	}

	public List<DestinoSocioeconomicoProduto> getDestinoSocioeconomicoProdutosBD() {
		return destinoSocioeconomicoProdutosBD;
	}

	public void setDestinoSocioeconomicoProdutosBD(List<DestinoSocioeconomicoProduto> destinoSocioeconomicoProdutosBD) {
		this.destinoSocioeconomicoProdutosBD = destinoSocioeconomicoProdutosBD;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

}