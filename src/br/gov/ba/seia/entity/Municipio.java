package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.MunicipioEnum;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "municipio", uniqueConstraints = {@UniqueConstraint(columnNames = { "ide_estado", "nom_municipio" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Municipio.findAll", query = "SELECT m FROM Municipio m where m.indExcluido=false"),
		@NamedQuery(name = "Municipio.findByIdeMunicipio", query = "SELECT m FROM Municipio m WHERE m.ideMunicipio = :ideMunicipio and m.indExcluido=false"),
		@NamedQuery(name = "Municipio.findByIndExcluido", query = "SELECT m FROM Municipio m WHERE m.indExcluido = :indExcluido"),
		@NamedQuery(name = "Municipio.findByIdeTipoMunicipio", query = "SELECT m FROM Municipio m WHERE m.ideTipoMunicipio = :ideTipoMunicipio"),
		@NamedQuery(name = "Municipio.findByIdeSituacaoMunicipio", query = "SELECT m FROM Municipio m WHERE m.ideSituacaoMunicipio = :ideSituacaoMunicipio"),
		@NamedQuery(name = "Municipio.findByNomMunicipio", query = "SELECT m FROM Municipio m WHERE m.nomMunicipio = :nomMunicipio"),
		@NamedQuery(name = "Municipio.findByUr", query = "SELECT a.municipio FROM AreaMunicipio a INNER JOIN a.municipio m WHERE  a.area = :area"),
		@NamedQuery(name = "Municipio.findByIdeEstado", query = "SELECT m FROM Municipio m WHERE m.ideEstado = :ideEstado and m.indExcluido=false order by m.nomMunicipio asc"),
		@NamedQuery(name = "Municipio.findByCoordGeobahiaMunicipio", query = "SELECT m FROM Municipio m WHERE m.coordGeobahiaMunicipio = :coordGeobahiaMunicipio"),		
		@NamedQuery(name = "Municipio.findByFceCanal", query = "SELECT fceCanal.municipios FROM FceCanal fceCanal WHERE fceCanal = :fceCanal")
})
public class Municipio extends AbstractEntity implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_municipio", nullable = false)
	private Integer ideMunicipio;
	
	@Column(name = "nom_municipio", nullable = false, length = 60)
	private String nomMunicipio;

	@Column(name = "ind_estado_emergencia")
	private Boolean indEstadoEmergencia;
	
	@Column(name = "num_cep", nullable = true, length = 9)
	private Integer numCep;
	
	@Column(name = "ind_bloqueio_dqc", nullable = true)
	private Boolean indBloqueioDQC;
	
	@Column(name = "cod_ibge_municipio", nullable = true, length = 7)
	private Double coordGeobahiaMunicipio;

	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@JoinColumn(name = "ide_tipo_municipio", referencedColumnName = "ide_tipo_municipio", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoMunicipio ideTipoMunicipio;
	
	@JoinColumn(name = "ide_situacao_municipio", referencedColumnName = "ide_situacao_municipio", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SituacaoMunicipio ideSituacaoMunicipio;
	
	@JoinColumn(name = "ide_estado", referencedColumnName = "ide_estado", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private Estado ideEstado;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideMunicipio", fetch = FetchType.LAZY)
	private Collection<Logradouro> logradouroCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideMunicipio", fetch = FetchType.LAZY)
	private Collection<Bairro> bairroCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio", fetch = FetchType.LAZY)
	private Collection<RelOrgaoMunicipio> relOrgaoMunicipioCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideMunicipioCartorio", fetch = FetchType.LAZY)
	private Collection<ImovelRural> imovelRuralCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideMunicipio", fetch = FetchType.LAZY)
	private Collection<EnderecoEmpreendimentoMunicipio> enderecoEmpreendimentoMunicipioCollection;
	
	@Transient
	private Boolean checked;
	
	@Transient
	private Boolean selectable;

	public Municipio() {}

	public Municipio(Integer ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}

	public Municipio(Integer ideMunicipio, String nomMunicipio) {
		this(ideMunicipio);
		this.nomMunicipio = nomMunicipio;
	}
	
	public Municipio(MunicipioEnum municipioEnum) {
		this(municipioEnum.getId(), municipioEnum.getNomMunicipio());
	}

	public Municipio(Integer ideMunicipio, String nomMunicipio, Boolean indEstadoEmergencia) {
		this(ideMunicipio, nomMunicipio);
		this.indEstadoEmergencia = indEstadoEmergencia;
	}
	
	public Municipio(Integer ideMunicipio, String nomMunicipio, Estado estado) {
		this(ideMunicipio, nomMunicipio);
		this.ideEstado = estado;		
	}
	        
	        
	public String getNumCepString() {
		String cep = "";
		if (!Util.isNullOuVazio(numCep) && numCep.toString().length() < 8) {
			cep = Util.lpad(numCep.toString(), '0', 8);
		}
		return cep;
	}
	
	public String getNomMunicipioSemAcento() {
		return Util.removerAcentos(nomMunicipio);
	}

	public Integer getIdeMunicipio() {
		return ideMunicipio;
	}

	public void setIdeMunicipio(Integer ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}

	public String getNomMunicipio() {
		return nomMunicipio;
	}

	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}

	@XmlTransient
	public Collection<Logradouro> getLogradouroCollection() {
		return logradouroCollection;
	}

	public void setLogradouroCollection(Collection<Logradouro> logradouroCollection) {
		this.logradouroCollection = logradouroCollection;
	}

	@XmlTransient
	public Collection<Bairro> getBairroCollection() {
		return bairroCollection;
	}

	public void setBairroCollection(Collection<Bairro> bairroCollection) {
		this.bairroCollection = bairroCollection;
	}

	@JSON(include = false)
	public TipoMunicipio getIdeTipoMunicipio() {
		return ideTipoMunicipio;
	}

	public void setIdeTipoMunicipio(TipoMunicipio ideTipoMunicipio) {
		this.ideTipoMunicipio = ideTipoMunicipio;
	}

	@JSON(include = false)
	public SituacaoMunicipio getIdeSituacaoMunicipio() {
		return ideSituacaoMunicipio;
	}

	public void setIdeSituacaoMunicipio(SituacaoMunicipio ideSituacaoMunicipio) {
		this.ideSituacaoMunicipio = ideSituacaoMunicipio;
	}

	@JSON(include = false)
	public Estado getIdeEstado() {
		return ideEstado;
	}

	public void setIdeEstado(Estado ideEstado) {
		this.ideEstado = ideEstado;
	}

	@JSON(include = false)
	public Integer getNumCep() {
		return numCep;
	}

	public void setNumCep(Integer numCep) {
		this.numCep = numCep;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	@XmlTransient
	public Collection<RelOrgaoMunicipio> getRelOrgaoMunicipioCollection() {
		return relOrgaoMunicipioCollection;
	}

	public void setRelOrgaoMunicipioCollection(Collection<RelOrgaoMunicipio> relOrgaoMunicipioCollection) {
		this.relOrgaoMunicipioCollection = relOrgaoMunicipioCollection;
	}

	@JSON(include = false)
	public Boolean getIndEstadoEmergencia() {
		return indEstadoEmergencia;
	}

	public void setIndEstadoEmergencia(Boolean indEstadoEmergencia) {
		this.indEstadoEmergencia = indEstadoEmergencia;
	}
	
	@JSON(include = false)
	public Double getCoordGeobahiaMunicipio() {
		return coordGeobahiaMunicipio;
	}
	
	public void setCoordGeobahiaMunicipio(Double coordGeobahiaMunicipio) {
		this.coordGeobahiaMunicipio = coordGeobahiaMunicipio;
	}
	
	public Collection<ImovelRural> getImovelRuralCollection() {
		return imovelRuralCollection;
	}
	
	public void setImovelRuralCollection(Collection<ImovelRural> imovelRuralCollection) {
		this.imovelRuralCollection = imovelRuralCollection;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Collection<EnderecoEmpreendimentoMunicipio> getEnderecoEmpreendimentoMunicipioCollection() {
		return enderecoEmpreendimentoMunicipioCollection;
	}

	public void setEnderecoEmpreendimentoMunicipioCollection(Collection<EnderecoEmpreendimentoMunicipio> enderecoEmpreendimentoMunicipioCollection) {
		this.enderecoEmpreendimentoMunicipioCollection = enderecoEmpreendimentoMunicipioCollection;
	}

	public Boolean getIndBloqueioDQC() {
		return indBloqueioDQC;
	}

	public void setIndBloqueioDQC(Boolean indBloqueioDQC) {
		this.indBloqueioDQC = indBloqueioDQC;
	}

	@Override
	public Municipio clone() throws CloneNotSupportedException{
		return (Municipio) super.clone();
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}
}