package br.gov.ba.seia.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

import br.gov.ba.seia.enumerator.StatusReservaLegalEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "reserva_legal", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_reserva_legal" }) })
@NamedQueries({ @NamedQuery(name = "ReservaLegal.findByAll", query = "SELECT r FROM ReservaLegal r"),
		@NamedQuery(name = "ReservaLegal.findByIdeReservaLegal", query = "SELECT r FROM ReservaLegal r WHERE r.ideReservaLegal = :ideReservaLegal"),
		@NamedQuery(name = "ReservaLegal.findByValArea", query = "SELECT r FROM ReservaLegal r WHERE r.valArea = :valArea"),
		@NamedQuery(name = "ReservaLegal.findByIndProcessoTramite", query = "SELECT r FROM ReservaLegal r WHERE r.indProcessoTramite = :indProcessoTramite"),
		@NamedQuery(name = "ReservaLegal.findByNumProcesso", query = "SELECT r FROM ReservaLegal r WHERE r.numProcesso = :numProcesso"),
		@NamedQuery(name = "ReservaLegal.findByNumCertificado", query = "SELECT r FROM ReservaLegal r WHERE r.numCertificado = :numCertificado"),
		@NamedQuery(name = "ReservaLegal.findByIdeImovelRural", query = "SELECT r FROM ReservaLegal r WHERE r.ideImovelRural = :ideImovelRural"),
		@NamedQuery(name = "ReservaLegal.findByIdeLocalizacaoGeografica", query = "SELECT r FROM ReservaLegal r WHERE r.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
		@NamedQuery(name = "ReservaLegal.findByIdeTipoArl", query = "SELECT r FROM ReservaLegal r WHERE r.ideTipoArl = :ideTipoArl"),
		@NamedQuery(name = "ReservaLegal.findByIdeTipoEstadoConservacao", query = "SELECT r FROM ReservaLegal r WHERE r.ideTipoEstadoConservacao = :ideTipoEstadoConservacao"),
		@NamedQuery(name = "ReservaLegal.findByIdeTipoOrigemCertificado", query = "SELECT r FROM ReservaLegal r WHERE r.ideTipoOrigemCertificado = :ideTipoOrigemCertificado") })
public class ReservaLegal implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private static final int AVERBADA = 2;
	private static final int APROVADA = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVA_LEGAL_IDE_RESERVA_LEGAL_SEQ")
	@SequenceGenerator(name = "RESERVA_LEGAL_IDE_RESERVA_LEGAL_SEQ", sequenceName = "RESERVA_LEGAL_IDE_RESERVA_LEGAL_SEQ", allocationSize = 1)
	@Column(name = "ide_reserva_legal", nullable = false)
	private Integer ideReservaLegal;

	@Basic(optional = false)
	@Column(name = "val_area", nullable = false, precision = 14, scale = 2)
	private Double valArea;

	@Basic(optional = false)
	@Column(name = "ind_processo_tramite", nullable = false)
	private Boolean indProcessoTramite;

	@Column(name = "num_processo", length = 30)
	private String numProcesso;

	@Column(name = "num_certificado", length = 30)
	private String numCertificado;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_tipo_arl", referencedColumnName = "ide_tipo_arl", nullable = false)
	@OneToOne(optional = false)
	private TipoArl ideTipoArl;

	@JoinColumn(name = "ide_tipo_estado_conservacao", referencedColumnName = "ide_tipo_estado_conservacao")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private TipoEstadoConservacao ideTipoEstadoConservacao;

	@JoinColumn(name = "ide_tipo_origem_certificado", referencedColumnName = "ide_tipo_origem_certificado")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private TipoOrigemCertificado ideTipoOrigemCertificado;

	@JoinColumn(name = "ide_status", referencedColumnName = "ide_status_reserva_legal", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private StatusReservaLegal ideStatus;

	@Column(name = "observacao", length = 2000)
	private String observacao;

	@Size(max = 500)
	@Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;

	@JoinColumn(name = "ide_usuario_aprovacao", referencedColumnName = "ide_pessoa_fisica")
	@OneToOne(optional = true)
	private Usuario ideUsuarioAprovacao;

	@JoinColumn(name = "ide_forma_realocacao_rl", referencedColumnName = "ide_forma_realocacao_rl", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private FormaRealocacaoRl ideFormaRealocacaoRl;

	@Column(name = "dtc_aprovacao", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcAprovacao;

	@Column(name = "ind_sobreposicao_app", nullable = true)
	private Boolean indSobreposicaoApp;

	@Column(name = "ind_sobreposicao_ap", nullable = true)
	private Boolean indSobreposicaoAp;

	@Basic(optional = false)
	@Column(name = "ind_menor_vinte_porcento")
	private Boolean indMenorVintePorcento;

	@Column(name = "ind_averbada", nullable = true)
	private Boolean indAverbada;

	@JoinColumn(name = "ide_documento_aprovacao", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentoImovelRural ideDocumentoAprovacao;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideReservaLegal")
	private ReservaLegalAverbada ideReservaLegalAverbada;

	@OneToOne(mappedBy = "ideReservaLegal",cascade = CascadeType.ALL)
	private CronogramaRecuperacao cronogramaRecuperacao;

	@Column(name = "num_sicar_compensacao", length = 100)
	private String numSicarCompensacao;

	@Basic(optional = false)
	@Column(name = "dtc_aprovacao_declarada", nullable = true)
	private Date dtcAprovacaoDeclarada;

	@JoinColumn(name = "ide_dado_origem", referencedColumnName = "ide_dado_origem", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private DadoOrigem ideDadoOrigem;

	@JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = true)
	@OneToOne(fetch = FetchType.EAGER)
	private Notificacao ideNotificacao;

	@JoinColumn(name = "ide_processo_ato", referencedColumnName = "ide_processo_ato", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name = "ide_reserva_legal_pai", referencedColumnName = "ide_reserva_legal", nullable = true)
	@OneToOne(fetch = FetchType.EAGER)
	private ReservaLegal ideReservaLegalPai;


	@Column(name = "ind_excluido")
    private String indExcluido;

	@Transient
	private Boolean indProcessoConcluidoCerberus;

	@Transient
	private Boolean indExcluida;

	@Transient
	private boolean indConcedido;

	@Transient
	private GeoJsonSicar geoJsonSicar;

	@Transient
	private boolean aceiteCondicoesRecuperacaoRl;

	@Transient
	private List<ImovelRural> listImoveisCredito;

	@Transient
	private boolean alterarStatusRl;

	@Basic(optional = false)
	@Column(name = "ind_deseja_cad_prad")
	private Boolean indDesejaCadPrad;

	@Column(name = "dtc_resp_deseja_cad_prad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcRespDesejaCadPrad;

	public ReservaLegal() {

	}

	public ReservaLegal(Integer ideReservaLegal) {
		this.ideReservaLegal = ideReservaLegal;
	}

	public ReservaLegal(Integer ideReservaLegal, Double valArea, boolean indProcessoTramite, String numProcesso,
			String numCertificado) {
		this.ideReservaLegal = ideReservaLegal;
		this.valArea = valArea;
		this.setIndProcessoTramite(indProcessoTramite);
		this.setNumProcesso(numProcesso);
		this.numCertificado = numCertificado;
	}

	public void validarReservaConcedida() {
		if (isIndConcedido()) {
			ideStatus = new StatusReservaLegal(StatusReservaLegalEnum.APROVADA.getId());
		} else {
			ideStatus = null;
		}
	}

	public Integer getIdeReservaLegal() {
		return ideReservaLegal;
	}

	public void setIdeReservaLegal(Integer ideReservaLegal) {
		this.ideReservaLegal = ideReservaLegal;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public Boolean getIndProcessoTramite() {
		return indProcessoTramite;
	}

	public void setIndProcessoTramite(Boolean indProcessoTramite) {
		this.indProcessoTramite = indProcessoTramite;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public String getNumCertificado() {
		return numCertificado;
	}

	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public TipoArl getIdeTipoArl() {
		return ideTipoArl;
	}

	public void setIdeTipoArl(TipoArl ideTipoArl) {
		this.ideTipoArl = ideTipoArl;
	}

	public TipoEstadoConservacao getIdeTipoEstadoConservacao() {
		return ideTipoEstadoConservacao;
	}

	public void setIdeTipoEstadoConservacao(TipoEstadoConservacao ideTipoEstadoConservacao) {
		this.ideTipoEstadoConservacao = ideTipoEstadoConservacao;
	}

	public TipoOrigemCertificado getIdeTipoOrigemCertificado() {
		return ideTipoOrigemCertificado;
	}

	public void setIdeTipoOrigemCertificado(TipoOrigemCertificado ideTipoOrigemCertificado) {
		this.ideTipoOrigemCertificado = ideTipoOrigemCertificado;
	}

	public StatusReservaLegal getIdeStatus() {
		return ideStatus;
	}

	public void setIdeStatus(StatusReservaLegal ideStatus) {
		this.ideStatus = ideStatus;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Usuario getIdeUsuarioAprovacao() {
		return ideUsuarioAprovacao;
	}

	public void setIdeUsuarioAprovacao(Usuario ideUsuarioAprovacao) {
		this.ideUsuarioAprovacao = ideUsuarioAprovacao;
	}

	public Date getDtcAprovacao() {
		return dtcAprovacao;
	}

	public void setDtcAprovacao(Date dtcAprovacao) {
		this.dtcAprovacao = dtcAprovacao;
	}

	public Boolean getIndSobreposicaoApp() {
		return indSobreposicaoApp;
	}

	public void setIndSobreposicaoApp(Boolean indSobreposicaoApp) {
		this.indSobreposicaoApp = indSobreposicaoApp;
	}

	public Boolean getIndSobreposicaoAp() {
		return indSobreposicaoAp;
	}

	public void setIndSobreposicaoAp(Boolean indSobreposicaoAp) {
		this.indSobreposicaoAp = indSobreposicaoAp;
	}

	public Boolean getIndMenorVintePorcento() {
		return indMenorVintePorcento;
	}

	public void setIndMenorVintePorcento(Boolean indMenorVintePorcento) {
		this.indMenorVintePorcento = indMenorVintePorcento;
	}

	public String getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(String indExcluido) {
		this.indExcluido = indExcluido;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideReservaLegal != null ? ideReservaLegal.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof ReservaLegal)) {
			return false;
		}
		ReservaLegal other = (ReservaLegal) object;
		if ((this.ideReservaLegal == null && other.ideReservaLegal != null)
				|| (this.ideReservaLegal != null && !this.ideReservaLegal.equals(other.ideReservaLegal))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideReservaLegal);
	}

	public Boolean getIndAverbada() {
		return indAverbada;
	}

	public void setIndAverbada(Boolean indAverbada) {
		this.indAverbada = indAverbada;
	}

	public DocumentoImovelRural getIdeDocumentoAprovacao() {
		return ideDocumentoAprovacao;
	}

	public void setIdeDocumentoAprovacao(DocumentoImovelRural ideDocumentoAprovacao) {
		this.ideDocumentoAprovacao = ideDocumentoAprovacao;
	}

	public Boolean getIndProcessoConcluidoCerberus() {
		return indProcessoConcluidoCerberus;
	}

	public void setIndProcessoConcluidoCerberus(Boolean indProcessoConcluidoCerberus) {
		this.indProcessoConcluidoCerberus = indProcessoConcluidoCerberus;
	}

	public ReservaLegalAverbada getIdeReservaLegalAverbada() {
		return ideReservaLegalAverbada;
	}

	public void setIdeReservaLegalAverbada(ReservaLegalAverbada ideReservaLegalAverbada) {
		this.ideReservaLegalAverbada = ideReservaLegalAverbada;
	}

	public CronogramaRecuperacao getCronogramaRecuperacao() {
		return cronogramaRecuperacao;
	}

	public void setCronogramaRecuperacao(CronogramaRecuperacao cronogramaRecuperacao) {
		this.cronogramaRecuperacao = cronogramaRecuperacao;
	}

	public String getNumSicarCompensacao() {
		return numSicarCompensacao;
	}

	public void setNumSicarCompensacao(String numSicarCompensacao) {
		if (numSicarCompensacao != null) {
			numSicarCompensacao = numSicarCompensacao.toUpperCase();
		}
		this.numSicarCompensacao = numSicarCompensacao;
	}

	public Date getDtcAprovacaoDeclarada() {
		return dtcAprovacaoDeclarada;
	}

	public void setDtcAprovacaoDeclarada(Date dtcAprovacaoDeclarada) {
		this.dtcAprovacaoDeclarada = dtcAprovacaoDeclarada;
	}

	public Boolean getIndExcluida() {
		return indExcluida;
	}

	public void setIndExcluida(Boolean indExcluida) {
		this.indExcluida = indExcluida;
	}

	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public boolean getAceiteCondicoesRecuperacaoRl() {
		return aceiteCondicoesRecuperacaoRl;
	}

	public void setAceiteCondicoesRecuperacaoRl(boolean aceiteCondicoesRecuperacaoRl) {
		this.aceiteCondicoesRecuperacaoRl = aceiteCondicoesRecuperacaoRl;
	}

	public List<ImovelRural> getListImoveisCredito() {
		return listImoveisCredito;
	}

	public void setListImoveisCredito(List<ImovelRural> listImoveisCredito) {
		this.listImoveisCredito = listImoveisCredito;
	}

	public boolean possuiLocalizacao() {
		return !Util.isNull(this.ideLocalizacaoGeografica);
	}

	public boolean isAlterarStatusRl() {
		return alterarStatusRl;
	}

	public void setAlterarStatusRl(boolean alterarStatusRl) {
		this.alterarStatusRl = alterarStatusRl;
	}

	public boolean isIndConcedido() {
		return indConcedido;
	}

	public void setIndConcedido(boolean indConcedido) {
		this.indConcedido = indConcedido;
	}

	public DadoOrigem getIdeDadoOrigem() {
		return ideDadoOrigem;
	}

	public void setIdeDadoOrigem(DadoOrigem ideDadoOrigem) {
		this.ideDadoOrigem = ideDadoOrigem;
	}

	public Notificacao getIdeNotificacao() {
		return ideNotificacao;
	}

	public void setIdeNotificacao(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}

	@Override
	public ReservaLegal clone() throws CloneNotSupportedException {
		return (ReservaLegal) super.clone();
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public ReservaLegal getIdeReservaLegalPai() {
		return ideReservaLegalPai;
	}

	public void setIdeReservaLegalPai(ReservaLegal ideReservaLegalPai) {
		this.ideReservaLegalPai = ideReservaLegalPai;
	}

	public FormaRealocacaoRl getIdeFormaRealocacaoRl() {
		return ideFormaRealocacaoRl;
	}

	public void setIdeFormaRealocacaoRl(FormaRealocacaoRl ideFormaRealocacaoRl) {
		this.ideFormaRealocacaoRl = ideFormaRealocacaoRl;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}

	public Date getDtcRespDesejaCadPrad() {
		return dtcRespDesejaCadPrad;
	}

	public void setDtcRespDesejaCadPrad(Date dtcRespDesejaCadPrad) {
		this.dtcRespDesejaCadPrad = dtcRespDesejaCadPrad;
	}

	public Boolean getIndDesejaCadPrad() {
		return indDesejaCadPrad;
	}

	public void setIndDesejaCadPrad(Boolean indDesejaCadPrad) {
		this.indDesejaCadPrad = indDesejaCadPrad;
	}
}
