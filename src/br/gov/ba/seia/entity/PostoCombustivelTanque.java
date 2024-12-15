package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "posto_combustivel_tanque")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PostoCombustivelTanque.findAll", query = "SELECT p FROM PostoCombustivelTanque p"),
		@NamedQuery(name = "PostoCombustivelTanque.findByIdeTanque", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.ideTanque = :ideTanque"),
		@NamedQuery(name = "PostoCombustivelTanque.findByNomeTanque", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.nomeTanque = :nomeTanque"),
		@NamedQuery(name = "PostoCombustivelTanque.findByIndInstalado", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.indInstalado = :indInstalado"),
		@NamedQuery(name = "PostoCombustivelTanque.findByIndTipoDescarga", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.indTipoDescargaLocal = :indTipoDescargaLocal"),
		@NamedQuery(name = "PostoCombustivelTanque.findByDscEspTecnica", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.dscEspTecnica = :dscEspTecnica"),
		@NamedQuery(name = "PostoCombustivelTanque.findByDtcInstalacao", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.dtcInstalacao = :dtcInstalacao"),
		@NamedQuery(name = "PostoCombustivelTanque.findByDtcUltimaInspecao", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.dtcUltimaInspecao = :dtcUltimaInspecao"),
		@NamedQuery(name = "PostoCombustivelTanque.findByDtcEstanqueidade", query = "SELECT p FROM PostoCombustivelTanque p WHERE p.dtcEstanqueidade = :dtcEstanqueidade"),
		@NamedQuery(name = "PostoCombustivelTanque.removerByIde", query = "delete from PostoCombustivelTanque l where l.ideTanque = :ideTanque") })
public class PostoCombustivelTanque implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tanque_seq")
	@SequenceGenerator(name = "tanque_seq", sequenceName = "posto_combustivel_tanque_ide_tanque_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_tanque")
	private Integer ideTanque;
	@Basic(optional = false)
	@Column(name = "nome_tanque")
	private String nomeTanque;
	@Basic(optional = false)
	@Column(name = "ind_instalado")
	private Boolean indInstalado;
	@Basic(optional = false)
	@Column(name = "ind_tipo_descarga_local")
	private Boolean indTipoDescargaLocal;

	@Size(max = 200)
	@Column(name = "dsc_esp_tecnica")
	private String dscEspTecnica;

	@Basic(optional = false)
	@Column(name = "dtc_instalacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcInstalacao;
	@Column(name = "dtc_ultima_inspecao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcUltimaInspecao;
	@Column(name = "dtc_estanqueidade")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEstanqueidade;
	@JoinColumn(name = "ide_tipo_tanque_posto", referencedColumnName = "ide_tipo_tanque_posto")
	@ManyToOne(optional = false)
	private TipoTanquePosto ideTipoTanquePosto;
	@JoinColumn(name = "ide_tipo_parede_tanque", referencedColumnName = "ide_tipo_parede_tanque")
	@ManyToOne(optional = false)
	private TipoParedeTanque ideTipoParedeTanque;
	@JoinColumn(name = "ide_tipo_estrutura_tanque", referencedColumnName = "ide_tipo_estrutura_tanque")
	@ManyToOne(optional = false)
	private TipoEstruturaTanque ideTipoEstruturaTanque;

	@JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel")
	@ManyToOne(optional = false)
	private LacPostoCombustivel idePostoCombustivel;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postoCombustivelTanque")
	Collection<PostoCombustivelTanqueProduto> produtoCollection;

	public PostoCombustivelTanque() {
	}

	public PostoCombustivelTanque(Integer ideTanque) {
		this.ideTanque = ideTanque;
	}

	public PostoCombustivelTanque(Integer ideTanque, String nomeTanque, Boolean indInstalado, Boolean indTipoDescargaLocal, Date dtcInstalacao) {
		this.ideTanque = ideTanque;
		this.nomeTanque = nomeTanque;
		this.indInstalado = indInstalado;
		this.indTipoDescargaLocal = indTipoDescargaLocal;
		this.dtcInstalacao = dtcInstalacao;
	}

	public Integer getIdeTanque() {
		return ideTanque;
	}

	public void setIdeTanque(Integer ideTanque) {
		this.ideTanque = ideTanque;
	}

	public String getNomeTanque() {
		return nomeTanque;
	}

	public void setNomeTanque(String nomeTanque) {
		this.nomeTanque = nomeTanque;
	}

	public Boolean getIndInstalado() {
		return indInstalado;
	}

	public void setIndInstalado(Boolean indInstalado) {
		this.indInstalado = indInstalado;
	}

	public Boolean getIndTipoDescargaLocal() {
		return indTipoDescargaLocal;
	}

	public void setIndTipoDescargaLocal(Boolean indTipoDescargaLocal) {
		this.indTipoDescargaLocal = indTipoDescargaLocal;
	}

	public String getDscEspTecnica() {
		return dscEspTecnica;
	}

	public void setDscEspTecnica(String dscEspTecnica) {
		this.dscEspTecnica = dscEspTecnica;
	}

	public Date getDtcInstalacao() {
		return dtcInstalacao;
	}

	public void setDtcInstalacao(Date dtcInstalacao) {
		this.dtcInstalacao = dtcInstalacao;
	}

	public Date getDtcUltimaInspecao() {
		return dtcUltimaInspecao;
	}

	public void setDtcUltimaInspecao(Date dtcUltimaInspecao) {
		this.dtcUltimaInspecao = dtcUltimaInspecao;
	}

	public Date getDtcEstanqueidade() {
		return dtcEstanqueidade;
	}

	public void setDtcEstanqueidade(Date dtcEstanqueidade) {
		this.dtcEstanqueidade = dtcEstanqueidade;
	}

	public TipoTanquePosto getIdeTipoTanquePosto() {
		return ideTipoTanquePosto;
	}

	public void setIdeTipoTanquePosto(TipoTanquePosto ideTipoTanquePosto) {
		this.ideTipoTanquePosto = ideTipoTanquePosto;
	}

	public TipoParedeTanque getIdeTipoParedeTanque() {
		return ideTipoParedeTanque;
	}

	public void setIdeTipoParedeTanque(TipoParedeTanque ideTipoParedeTanque) {
		this.ideTipoParedeTanque = ideTipoParedeTanque;
	}

	public TipoEstruturaTanque getIdeTipoEstruturaTanque() {
		return ideTipoEstruturaTanque;
	}

	public void setIdeTipoEstruturaTanque(TipoEstruturaTanque ideTipoEstruturaTanque) {
		this.ideTipoEstruturaTanque = ideTipoEstruturaTanque;
	}

	public LacPostoCombustivel getIdePostoCombustivel() {
		return idePostoCombustivel;
	}

	public void setIdePostoCombustivel(LacPostoCombustivel idePostoCombustivel) {
		this.idePostoCombustivel = idePostoCombustivel;
	}

	public Collection<PostoCombustivelTanqueProduto> getProdutoCollection() {
		return Util.isNull(produtoCollection) ? produtoCollection = new ArrayList<PostoCombustivelTanqueProduto>() : produtoCollection;
	}

	public void setProdutoCollection(Collection<PostoCombustivelTanqueProduto> produtoCollection) {
		this.produtoCollection = produtoCollection;
	}
	
	public String getValCapacidadeFormatado(){
		Locale ptBr = new Locale("pt", "BR"); 
		Double total = 0D;
		for (PostoCombustivelTanqueProduto lee : this.getProdutoCollection()) {
			total += lee.getValCapacidade().doubleValue();
		} 
		
		NumberFormat instance = NumberFormat.getInstance(ptBr);
		instance.setMinimumFractionDigits(2);
		return instance.format(total);	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscEspTecnica == null) ? 0 : dscEspTecnica.hashCode());
		result = prime * result + ((dtcEstanqueidade == null) ? 0 : dtcEstanqueidade.hashCode());
		result = prime * result + ((dtcInstalacao == null) ? 0 : dtcInstalacao.hashCode());
		result = prime * result + ((dtcUltimaInspecao == null) ? 0 : dtcUltimaInspecao.hashCode());
		result = prime * result + ((idePostoCombustivel == null) ? 0 : idePostoCombustivel.hashCode());
		result = prime * result + ((ideTanque == null) ? 0 : ideTanque.hashCode());
		result = prime * result + ((ideTipoEstruturaTanque == null) ? 0 : ideTipoEstruturaTanque.hashCode());
		result = prime * result + ((ideTipoParedeTanque == null) ? 0 : ideTipoParedeTanque.hashCode());
		result = prime * result + ((ideTipoTanquePosto == null) ? 0 : ideTipoTanquePosto.hashCode());
		result = prime * result + ((indInstalado == null) ? 0 : indInstalado.hashCode());
		result = prime * result + ((indTipoDescargaLocal == null) ? 0 : indTipoDescargaLocal.hashCode());
		result = prime * result + ((nomeTanque == null) ? 0 : nomeTanque.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostoCombustivelTanque other = (PostoCombustivelTanque) obj;
		if (dscEspTecnica == null) {
			if (other.dscEspTecnica != null)
				return false;
		} else if (!dscEspTecnica.equals(other.dscEspTecnica))
			return false;
		if (dtcEstanqueidade == null) {
			if (other.dtcEstanqueidade != null)
				return false;
		} else if (!dtcEstanqueidade.equals(other.dtcEstanqueidade))
			return false;
		if (dtcInstalacao == null) {
			if (other.dtcInstalacao != null)
				return false;
		} else if (!dtcInstalacao.equals(other.dtcInstalacao))
			return false;
		if (dtcUltimaInspecao == null) {
			if (other.dtcUltimaInspecao != null)
				return false;
		} else if (!dtcUltimaInspecao.equals(other.dtcUltimaInspecao))
			return false;
		if (idePostoCombustivel == null) {
			if (other.idePostoCombustivel != null)
				return false;
		} else if (!idePostoCombustivel.equals(other.idePostoCombustivel))
			return false;
		if (ideTanque == null) {
			if (other.ideTanque != null)
				return false;
		} else if (!ideTanque.equals(other.ideTanque))
			return false;
		if (ideTipoEstruturaTanque == null) {
			if (other.ideTipoEstruturaTanque != null)
				return false;
		} else if (!ideTipoEstruturaTanque.equals(other.ideTipoEstruturaTanque))
			return false;
		if (ideTipoParedeTanque == null) {
			if (other.ideTipoParedeTanque != null)
				return false;
		} else if (!ideTipoParedeTanque.equals(other.ideTipoParedeTanque))
			return false;
		if (ideTipoTanquePosto == null) {
			if (other.ideTipoTanquePosto != null)
				return false;
		} else if (!ideTipoTanquePosto.equals(other.ideTipoTanquePosto))
			return false;
		if (indInstalado == null) {
			if (other.indInstalado != null)
				return false;
		} else if (!indInstalado.equals(other.indInstalado))
			return false;
		if (indTipoDescargaLocal == null) {
			if (other.indTipoDescargaLocal != null)
				return false;
		} else if (!indTipoDescargaLocal.equals(other.indTipoDescargaLocal))
			return false;
		if (nomeTanque == null) {
			if (other.nomeTanque != null)
				return false;
		} else if (!nomeTanque.equals(other.nomeTanque))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.PostoCombustivelTanque[ ideTanque=" + ideTanque + " ]";
	}

}
