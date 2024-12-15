package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "pauta", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_pauta" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Pauta.findAll", query = "SELECT p FROM Pauta p"),
		@NamedQuery(name = "Pauta.findByIdePauta", query = "SELECT p FROM Pauta p WHERE p.idePauta = :idePauta"),
		@NamedQuery(name = "Pauta.findByIdeFuncionario", query = "SELECT p FROM Pauta p WHERE p.idePessoaFisica.idePessoaFisica = :ideFuncionario"),
		@NamedQuery(name = "Pauta.findByIdeFuncionarioPauta", query = "SELECT p FROM Pauta p WHERE p.idePessoaFisica.idePessoaFisica = :ideFuncionario AND p.ideTipoPauta.ideTipoPauta = :ideTipoPauta"),
		@NamedQuery(name = "Pauta.findByIdeAreaTipoPauta", query = "SELECT p FROM Pauta p WHERE p.ideArea.ideArea = :ideArea AND p.ideTipoPauta.ideTipoPauta = :ideTipoPauta "),
		@NamedQuery(name = "Pauta.findByIdeArea", query = "SELECT p FROM Pauta p WHERE p.ideArea = :ideArea"),
		@NamedQuery(name = "Pauta.findByIdePautaCoordenadorArea", query = "SELECT p FROM Pauta p INNER JOIN p.ideArea a WHERE  a.ideArea = :ideArea and a.idePessoaFisica = p.idePessoaFisica"),
		@NamedQuery(name = "Pauta.findByIdePessoaFisicaIdeTipoPauta", query = "SELECT p FROM Pauta p WHERE p.idePessoaFisica.idePessoaFisica = :idePessoaFisica AND p.ideTipoPauta.ideTipoPauta = :ideTipoPauta"),
		@NamedQuery(name = "Pauta.findByIdePautaControleTramitação", query = "SELECT p FROM Pauta p inner join fetch p.controleTramitacaoCollection ct  inner join fetch ct.ideProcesso pr WHERE p.idePauta = :idePauta") })
public class Pauta extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAUTA_IDE_PAUTA_seq")
	@SequenceGenerator(name = "PAUTA_IDE_PAUTA_seq", sequenceName = "PAUTA_IDE_PAUTA_seq", allocationSize = 1)
	@Column(name = "ide_pauta", nullable = false)
	private Integer idePauta;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePauta", fetch = FetchType.LAZY)
	private Collection<ControleTramitacao> controleTramitacaoCollection;
	
	@JoinColumn(name = "ide_tipo_pauta", referencedColumnName = "ide_tipo_pauta", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoPauta ideTipoPauta;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.EAGER)
	private Funcionario idePessoaFisica;
	
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area")
	@ManyToOne(fetch = FetchType.EAGER)
	private Area ideArea;
	
	@OneToMany(mappedBy = "idePauta", fetch = FetchType.LAZY)
	private Collection<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaCollection;
	
	public Pauta() {
	}
	
	public Pauta(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	public Pauta(Area ideArea) {
		this.ideArea = ideArea;
	}
	
	public Pauta(Funcionario idePessoaFisica, TipoPauta ideTipoPauta, Area ideArea) {
		this.idePessoaFisica = idePessoaFisica;
		this.ideTipoPauta = ideTipoPauta;
		this.ideArea = ideArea;
	}
	
	public Pauta(Integer idePauta) {
		this.idePauta = idePauta;
	}
	
	public Integer getIdePauta() {
		return idePauta;
	}
	
	public void setIdePauta(Integer idePauta) {
		this.idePauta = idePauta;
	}
	
	@XmlTransient
	public Collection<ControleTramitacao> getControleTramitacaoCollection() {
		return controleTramitacaoCollection;
	}
	
	public void setControleTramitacaoCollection(Collection<ControleTramitacao> controleTramitacaoCollection) {
		this.controleTramitacaoCollection = controleTramitacaoCollection;
	}
	
	public TipoPauta getIdeTipoPauta() {
		return ideTipoPauta;
	}
	
	public void setIdeTipoPauta(TipoPauta ideTipoPauta) {
		this.ideTipoPauta = ideTipoPauta;
	}
	
	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}
	
	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	public Area getIdeArea() {
		return ideArea;
	}
	
	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}
	
	public Collection<FuncionalidadeAcaoPessoaFisicaPauta> getFuncionalidadeAcaoPessoaFisicaPautaCollection() {
		return funcionalidadeAcaoPessoaFisicaPautaCollection;
	}
	
	public void setFuncionalidadeAcaoPessoaFisicaPautaCollection(Collection<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaCollection) {
		this.funcionalidadeAcaoPessoaFisicaPautaCollection = funcionalidadeAcaoPessoaFisicaPautaCollection;
	}
}