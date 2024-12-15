package br.gov.ba.seia.entity;

import java.util.Collection;
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
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * 
 * @author antoniony.lima
 *
 */
@Entity
@Table(name = "COMUNICACAO")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Comunicacao.findAll", query = "SELECT A FROM Comunicacao A") })
public class Comunicacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMUNICACAO_IDE_GENERATOR")
	@SequenceGenerator(name = "COMUNICACAO_IDE_GENERATOR", sequenceName = "comunicacao_seq", allocationSize = 1)
	@Column(name = "IDE_COMUNICACAO")
	private Integer ideComunicacao;

	@Basic(optional = false)
	@Column(name = "dsc_titulo", length = 20)
	private String dscTitulo;

	@Basic(optional = false)
	@Column(name = "ide_comunicacao_status", length = 40)
	private Integer ideComunicacaoStatus;

	@Basic(optional = false)
	@Column(name = "ind_ativa", nullable = false)
	private boolean indAtiva;

	@Basic(optional = false)
	@Column(name = "dtc_periodo_inicio", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtcPeriodoInicio;

	@Column(name = "dtc_periodo_fim")
	@Temporal(TemporalType.DATE)
	private Date dtcPeriodoFim;

	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	@Basic(optional = false)
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false, insertable = true, updatable = true)
	@ManyToOne(targetEntity = Usuario.class)
	private Usuario idePessoaFisica;

	@Basic(optional = false)
	@JoinColumn(name = "ide_comunicacao_status", referencedColumnName = "ide_comunicacao_status", nullable = false, insertable = false, updatable = false)
	@ManyToOne(targetEntity = ComunicacaoStatus.class)
	private ComunicacaoStatus comunicacaoStatus;

	@Basic(optional = false)
	@Column(name = "txt_conteudo")
	private String txtConteudo;

	@Basic(optional = false)
	@Column(name = "tp_comunicacao", length = 1)
	private String tpComunicacao;

	@MapsId("comunicacaoPerfilPK.ideComunicacao")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comunicacaoPerfilPK.ideComunicacao")
	private Collection<ComunicacaoPerfil> comunicacaoPerfilCollection;

	@Transient
	private String tpComunicacaoDesc;
	
	@Transient
	private String situacao;


	public Integer getIdeComunicacao() {
		return ideComunicacao;
	}

	public void setIdeComunicacao(Integer ideComunicacao) {
		this.ideComunicacao = ideComunicacao;
	}

	public String getDscTitulo() {
		return dscTitulo;
	}

	public void setDscTitulo(String dscTitulo) {
		this.dscTitulo = dscTitulo;
	}

	public Integer getIdeComunicacaoStatus() {
		return ideComunicacaoStatus;
	}

	public void setIdeComunicacaoStatus(Integer ideComunicacaoStatus) {
		this.ideComunicacaoStatus = ideComunicacaoStatus;
	}

	public boolean isIndAtiva() {
		return indAtiva;
	}

	public void setIndAtiva(boolean indAtiva) {
		this.indAtiva = indAtiva;
	}

	public Date getDtcPeriodoInicio() {
		return dtcPeriodoInicio;
	}

	public void setDtcPeriodoInicio(Date dtcPeriodoInicio) {
		this.dtcPeriodoInicio = dtcPeriodoInicio;
	}

	public Date getDtcPeriodoFim() {
		return dtcPeriodoFim;
	}

	public void setDtcPeriodoFim(Date dtcPeriodoFim) {
		this.dtcPeriodoFim = dtcPeriodoFim;
	}

	public Usuario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Usuario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public String getTxtConteudo() {
		return txtConteudo;
	}

	public void setTxtConteudo(String txtConteudo) {
		this.txtConteudo = txtConteudo;
	}

	public String getTpComunicacao() {
		
		return tpComunicacao;
	}

	public void setTpComunicacao(String tpComunicacao) {
		this.tpComunicacao = tpComunicacao;
	}

	public Collection<ComunicacaoPerfil> getComunicacaoPerfilCollection() {
		return comunicacaoPerfilCollection;
	}

	public void setComunicacaoPerfilCollection(Collection<ComunicacaoPerfil> comunicacaoPerfilCollection) {
		this.comunicacaoPerfilCollection = comunicacaoPerfilCollection;
	}

	public ComunicacaoStatus getComunicacaoStatus() {
		return comunicacaoStatus;
	}

	public void setComunicacaoStatus(ComunicacaoStatus comunicacaoStatus) {
		this.comunicacaoStatus = comunicacaoStatus;
	}
	
	public String getTpComunicacaoDesc() {
		tpComunicacaoDesc = "Notificação";
		if (tpComunicacao.equals("T")) {
			tpComunicacaoDesc = "Temporária";
		}
		return tpComunicacaoDesc;
	}
	
	public String getSituacao() {
		situacao =  "Inativa";
		if(indAtiva) {
			situacao =  "Ativa";
		}

		return situacao;
	}

	public void setTpComunicacaoDesc(String tpComunicacaoDesc) {
		this.tpComunicacaoDesc = tpComunicacaoDesc;
	}


}
