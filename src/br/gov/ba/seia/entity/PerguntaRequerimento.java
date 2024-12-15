package br.gov.ba.seia.entity;

import java.io.Serializable;
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

/**
 * 
 * @author micael.coutinho
 */
@Entity
@Table(name = "pergunta_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PerguntaRequerimento.findAll", query = "SELECT p FROM PerguntaRequerimento p"),
		@NamedQuery(name = "PerguntaRequerimento.findByIdePerguntaRequerimento", query = "SELECT p FROM PerguntaRequerimento p WHERE p.idePerguntaRequerimento = :idePerguntaRequerimento"),
		@NamedQuery(name = "PerguntaRequerimento.findByIndResposta", query = "SELECT p FROM PerguntaRequerimento p WHERE p.indResposta = :indResposta"),
		@NamedQuery(name = "PerguntaRequerimento.findByDtcResposta", query = "SELECT p FROM PerguntaRequerimento p WHERE p.dtcResposta = :dtcResposta"),
		@NamedQuery(name = "PerguntaRequerimento.findByIdeRequerimento", query = "SELECT p FROM PerguntaRequerimento p WHERE p.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "PerguntaRequerimento.excluirByIdLocalizacaoGeografica", query = "DELETE FROM PerguntaRequerimento p WHERE p.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
		@NamedQuery(name = "PerguntaRequerimento.excluirByIdeOutorgaLocGeo", query = "DELETE FROM PerguntaRequerimento p WHERE p.ideOutorgaLocalizacaoGeografica = :ideOutorgaLocalizacaoGeografica"),
		@NamedQuery(name = "PerguntaRequerimento.excluirByIdLicenca", query = "DELETE FROM PerguntaRequerimento p WHERE p.ideLicenca = :ideLicenca") })

public class PerguntaRequerimento implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pergunta_requerimento_seq")
	@SequenceGenerator(name = "pergunta_requerimento_seq", sequenceName = "pergunta_requerimento_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_pergunta_requerimento")
	private Integer idePerguntaRequerimento;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_resposta")
	private Boolean indResposta;

	@Column(name = "dtc_resposta")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcResposta;

	@Basic(optional = false)
	@Column(name = "ind_excluido")
	private Boolean indExcluido;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	@NotNull
	@JoinColumn(name = "ide_pergunta", referencedColumnName = "ide_pergunta")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Pergunta idePergunta;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Imovel ideImovel;

	@JoinColumn(name = "ide_licenca", referencedColumnName = "ide_licenca", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Licenca ideLicenca;
	
	@JoinColumn(name = "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	public PerguntaRequerimento() {
	}

	public PerguntaRequerimento(Integer idePerguntaRequerimento) {
		this.idePerguntaRequerimento = idePerguntaRequerimento;
	}

	public PerguntaRequerimento(Integer idePerguntaRequerimento, boolean indResposta) {
		this.idePerguntaRequerimento = idePerguntaRequerimento;
		this.indResposta = indResposta;
	}

	public PerguntaRequerimento(Requerimento ideRequerimento, Pergunta idePergunta) {
		super();
		this.ideRequerimento = ideRequerimento;
		this.idePergunta = idePergunta;
	}

	public PerguntaRequerimento(Pergunta idePergunta) {
		this.idePergunta = idePergunta;
	}

	public Integer getIdePerguntaRequerimento() {
		return idePerguntaRequerimento;
	}

	public void setIdePerguntaRequerimento(Integer idePerguntaRequerimento) {
		this.idePerguntaRequerimento = idePerguntaRequerimento;
	}

	public Boolean getIndResposta() {
		return indResposta;
	}

	public void setIndResposta(Boolean indResposta) {
		this.indResposta = indResposta;
	}

	public Date getDtcResposta() {
		return dtcResposta;
	}

	public void setDtcResposta(Date dtcResposta) {
		this.dtcResposta = dtcResposta;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Pergunta getIdePergunta() {
		return idePergunta;
	}

	public void setIdePergunta(Pergunta idePergunta) {
		this.idePergunta = idePergunta;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	@Override
	public String toString() {
		return "entity.PerguntaRequerimento[ idePerguntaRequerimento=" + idePerguntaRequerimento + " ]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtcResposta == null) ? 0 : dtcResposta.hashCode());
		result = prime * result + ((ideLocalizacaoGeografica == null) ? 0 : ideLocalizacaoGeografica.hashCode());
		result = prime * result + ((idePergunta == null) ? 0 : idePergunta.hashCode());
		result = prime * result + ((idePerguntaRequerimento == null) ? 0 : idePerguntaRequerimento.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		result = prime * result + ((indResposta == null) ? 0 : indResposta.hashCode());
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
		PerguntaRequerimento other = (PerguntaRequerimento) obj;
		if (dtcResposta == null) {
			if (other.dtcResposta != null)
				return false;
		} else if (!dtcResposta.equals(other.dtcResposta))
			return false;
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null)
				return false;
		} else if (!ideLocalizacaoGeografica.equals(other.ideLocalizacaoGeografica))
			return false;
		if (idePergunta == null) {
			if (other.idePergunta != null)
				return false;
		} else if (!idePergunta.equals(other.idePergunta))
			return false;
		if (idePerguntaRequerimento == null) {
			if (other.idePerguntaRequerimento != null)
				return false;
		} else if (!idePerguntaRequerimento.equals(other.idePerguntaRequerimento))
			return false;
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null)
				return false;
		} else if (!ideRequerimento.equals(other.ideRequerimento))
			return false;
		if (indResposta == null) {
			if (other.indResposta != null)
				return false;
		} else if (!indResposta.equals(other.indResposta))
			return false;
		return true;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@Override
	public PerguntaRequerimento clone() throws CloneNotSupportedException {
		return (PerguntaRequerimento) super.clone();
	}

	public Licenca getIdeLicenca() {
		return ideLicenca;
	}

	public void setIdeLicenca(Licenca ideLicenca) {
		this.ideLicenca = ideLicenca;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

}
