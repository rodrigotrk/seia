package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author eduardo.fernandes
 * 
 */

@Entity
@Table(name = "licenca")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Licenca.findAll", query = "SELECT t FROM Licenca t"),
		@NamedQuery(name = "Licenca.findByIdeLicenca", query = "SELECT l FROM Licenca l WHERE l.ideLicenca = :ideLicenca and l.indExcluido = false"),
		@NamedQuery(name = "Licenca.findByNumProcessoLicenca", query = "SELECT l FROM Licenca l WHERE l.numProcessoLicenca = :numProcessoLicenca"),
		@NamedQuery(name = "Licenca.findByNumPortariaLicenca", query = "SELECT l FROM Licenca l WHERE l.numPortariaLicenca = :numPortariaLicenca"),
		@NamedQuery(name = "Licenca.findByDataPublicacaoPortaria", query = "SELECT l FROM Licenca l WHERE l.dataPublicacaoPortaria = :dataPublicacaoPortaria"),
		@NamedQuery(name = "Licenca.findByDataValidadeLicenca", query = "SELECT l FROM Licenca l WHERE l.dataValidadeLicenca = :dataValidadeLicenca"),
		@NamedQuery(name = "Licenca.findByIdeRequerimento", query = "SELECT l FROM Licenca l WHERE l.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "Licenca.findByIdeTipoSolicitacao", query = "SELECT l FROM Licenca l WHERE l.ideTipoSolicitacao = :ideTipoSolicitacao"),
		@NamedQuery(name = "Licenca.removeByIdeLicenca", query = "DELETE FROM Licenca l WHERE l.ideLicenca = :ideLicenca")})
public class Licenca implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "licenca_ide_licenca_generator", sequenceName = "licenca_ide_licenca_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "licenca_ide_licenca_generator")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_licenca")
	private Integer ideLicenca;

	@Column(name = "num_processo_licenca", length = 50)
	private String numProcessoLicenca;

	@Column(name = "num_portaria_licenca", length = 50)
	private String numPortariaLicenca;

	@Column(name = "dtc_publicacao_portaria")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPublicacaoPortaria;

	@Column(name = "dtc_validade_licenca")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataValidadeLicenca;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_tipo_solicitacao", referencedColumnName = "ide_tipo_solicitacao")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoSolicitacao ideTipoSolicitacao;

	@Column(name = "ind_excluido", nullable = false)
	private Boolean indExcluido;

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@Column(name = "num_processo_licenca_municipal", length = 50)
	private String numProcessoLicencaMunicipal;

	@Column(name = "dsc_atividade_processo_licenca_municipal", length = 50)
	private String dscAtividadeProcessoLicencaMunicipal;
	
	public Licenca() {

	}

	public Licenca(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public Licenca(Integer ideLicenca) {
		this.ideLicenca = ideLicenca;
	}

	public Integer getIdeLicenca() {
		return ideLicenca;
	}

	public void setIdeLicenca(Integer ideLicenca) {
		this.ideLicenca = ideLicenca;
	}

	public String getNumProcessoLicenca() {
		return numProcessoLicenca;
	}

	public void setNumProcessoLicenca(String numProcessoLicenca) {
		this.numProcessoLicenca = numProcessoLicenca;
	}

	public String getNumPortariaLicenca() {
		return numPortariaLicenca;
	}

	public void setNumPortariaLicenca(String numPortariaLicenca) {
		this.numPortariaLicenca = numPortariaLicenca;
	}

	public Date getDataPublicacaoPortaria() {
		return dataPublicacaoPortaria;
	}

	public void setDataPublicacaoPortaria(Date dataPublicacaoPortaria) {
		this.dataPublicacaoPortaria = dataPublicacaoPortaria;
	}

	public Date getDataValidadeLicenca() {
		return dataValidadeLicenca;
	}
	
	public String getDataValidadeLicencaFormatado() {
		if(!Util.isNullOuVazio(dataValidadeLicenca)) {
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dataValidadeLicenca);
		} else {
			return "-";
		}
	}

	public void setDataValidadeLicenca(Date dataValidadeLicenca) {
		this.dataValidadeLicenca = dataValidadeLicenca;
	}

	public TipoSolicitacao getTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
		this.ideTipoSolicitacao = tipoSolicitacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public TipoSolicitacao getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(TipoSolicitacao ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public String getNumProcessoLicencaMunicipal() {
		return numProcessoLicencaMunicipal;
	}

	public void setNumProcessoLicencaMunicipal(String numProcessoLicencaMunicipal) {
		this.numProcessoLicencaMunicipal = numProcessoLicencaMunicipal;
	}

	public String getDscAtividadeProcessoLicencaMunicipal() {
		return dscAtividadeProcessoLicencaMunicipal;
	}

	public void setDscAtividadeProcessoLicencaMunicipal(String dscAtividadeProcessoLicencaMunicipal) {
		this.dscAtividadeProcessoLicencaMunicipal = dscAtividadeProcessoLicencaMunicipal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideLicenca == null) ? 0 : ideLicenca.hashCode());
		result = prime * result + ((numProcessoLicenca == null) ? 0 : numProcessoLicenca.hashCode());
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
		Licenca other = (Licenca) obj;
		if (ideLicenca == null) {
			if (other.ideLicenca != null)
				return false;
		} else if (!ideLicenca.equals(other.ideLicenca))
			return false;
		if (numProcessoLicenca == null) {
			if (other.numProcessoLicenca != null)
				return false;
		} else if (!numProcessoLicenca.equals(other.numProcessoLicenca))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.Licenca[ ideLicenca=" + ideLicenca + " ]";
	}
	
	public Licenca clone() throws CloneNotSupportedException {
		return (Licenca) super.clone();
	}
}