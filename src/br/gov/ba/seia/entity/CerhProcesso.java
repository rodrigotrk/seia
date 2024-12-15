package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh_processo")
public class CerhProcesso extends AbstractEntityHist implements Cloneable, Comparable<CerhProcesso> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cerh_processo_seq")
    @SequenceGenerator(name="cerh_processo_seq", sequenceName="cerh_processo_seq", allocationSize=1)
	@Column(name="ide_cerh_processo")
	private Integer ideCerhProcesso;

	@Historico(name = "Data de autorização")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_autorizacao")
	private Date dtInicioAutorizacao;

	@Column(name="ind_dados_concedidos")
	private Boolean indDadosConcedidos;

	@Historico(name = "Possui carta de inexibilidade")
	@Column(name="ind_possui_carta_inexigibilidade")
	private Boolean indPossuiCartaInexigibilidade;

	@Historico(name = "Número do documento da portaria")
	@Column(name="num_portaria_documento")
	private String numPortariaDocumento;

	@Historico(name = "Prazo de validade")
	@Column(name="num_prazo_validade")
	private Integer numPrazoValidade;

	@Historico(name="Número do processo", key=true)
	@Column(name="num_processo")
	private String numProcesso;

	@Column(name="ind_outorga_referente_ponto_cadastrado_cerh")
	private Boolean indOutorgaReferentePontoCadastradoCerh;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_sistema")
	private Sistema ideSistema;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh")
	private Cerh ideCerh;

	@Historico
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh_situacao_regularizacao")
	private CerhSituacaoRegularizacao ideCerhSituacaoRegularizacao;

	@Historico
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh_tipo_ato_dispensa")
	private CerhTipoAtoDispensa ideCerhTipoAtoDispensa;

	@Historico
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh_tipo_autorizacao_outorgado")
	private CerhTipoAutorizacaoOutorgado ideCerhTipoAutorizacaoOutorgado;

	@OneToMany(mappedBy="ideCerhProcesso", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CerhTipoUso> cerhTipoUsoCollection;

	@OneToMany(mappedBy="ideCerhProcesso", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CerhLocalizacaoGeografica> cerhLocalizacaoGeograficaCollection;
	
	@Historico
	@OneToMany(mappedBy="ideCerhProcesso", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CerhProcessoSuspensao> cerhProcessoSuspensaoCollection;
	
	@Transient
	private CerhProcesso cerhProcessoPai;

	public CerhProcesso() {
	}
	
	public CerhProcesso(Integer ideCerhProcesso) {
		this.ideCerhProcesso = ideCerhProcesso;
	}

	public CerhProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public Integer getIdeCerhProcesso() {
		return ideCerhProcesso;
	}

	public void setIdeCerhProcesso(Integer ideCerhProcesso) {
		this.ideCerhProcesso = ideCerhProcesso;
	}

	public Date getDtInicioAutorizacao() {
		return dtInicioAutorizacao;
	}

	public void setDtInicioAutorizacao(Date dtInicioAutorizacao) {
		this.dtInicioAutorizacao = dtInicioAutorizacao;
	}

	public Boolean getIndDadosConcedidos() {
		return indDadosConcedidos;
	}

	public void setIndDadosConcedidos(Boolean indDadosConcedidos) {
		this.indDadosConcedidos = indDadosConcedidos;
	}

	public Boolean getIndPossuiCartaInexigibilidade() {
		return indPossuiCartaInexigibilidade;
	}

	public void setIndPossuiCartaInexigibilidade(Boolean indPossuiCartaInexigibilidade) {
		this.indPossuiCartaInexigibilidade = indPossuiCartaInexigibilidade;
	}

	public String getNumPortariaDocumento() {
		return numPortariaDocumento;
	}

	public void setNumPortariaDocumento(String numPortariaDocumento) {
		this.numPortariaDocumento = numPortariaDocumento;
	}

	public Integer getNumPrazoValidade() {
		return numPrazoValidade;
	}

	public void setNumPrazoValidade(Integer numPrazoValidade) {
		this.numPrazoValidade = numPrazoValidade;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public Boolean getIndOutorgaReferentePontoCadastradoCerh() {
		return indOutorgaReferentePontoCadastradoCerh;
	}

	public void setIndOutorgaReferentePontoCadastradoCerh(
			Boolean indOutorgaReferentePontoCadastradoCerh) {
		this.indOutorgaReferentePontoCadastradoCerh = indOutorgaReferentePontoCadastradoCerh;
	}

	public Sistema getIdeSistema() {
		return ideSistema;
	}

	public void setIdeSistema(Sistema ideSistema) {
		this.ideSistema = ideSistema;
	}

	public Cerh getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Cerh ideCerh) {
		this.ideCerh = ideCerh;
	}

	public CerhSituacaoRegularizacao getIdeCerhSituacaoRegularizacao() {
		return ideCerhSituacaoRegularizacao;
	}

	public void setIdeCerhSituacaoRegularizacao(CerhSituacaoRegularizacao ideCerhSituacaoRegularizacao) {
		this.ideCerhSituacaoRegularizacao = ideCerhSituacaoRegularizacao;
	}

	public CerhTipoAtoDispensa getIdeCerhTipoAtoDispensa() {
		return ideCerhTipoAtoDispensa;
	}

	public void setIdeCerhTipoAtoDispensa(CerhTipoAtoDispensa ideCerhTipoAtoDispensa) {
		this.ideCerhTipoAtoDispensa = ideCerhTipoAtoDispensa;
	}

	public CerhTipoAutorizacaoOutorgado getIdeCerhTipoAutorizacaoOutorgado() {
		return ideCerhTipoAutorizacaoOutorgado;
	}

	public void setIdeCerhTipoAutorizacaoOutorgado(	CerhTipoAutorizacaoOutorgado ideCerhTipoAutorizacaoOutorgado) {
		this.ideCerhTipoAutorizacaoOutorgado = ideCerhTipoAutorizacaoOutorgado;
	}

	public Collection<CerhTipoUso> getCerhTipoUsoCollection() {
		return cerhTipoUsoCollection;
	}

	public void setCerhTipoUsoCollection(Collection<CerhTipoUso> cerhTipoUsoCollection) {
		this.cerhTipoUsoCollection = cerhTipoUsoCollection;
	}

	public Collection<CerhLocalizacaoGeografica> getCerhLocalizacaoGeograficaCollection() {
		return cerhLocalizacaoGeograficaCollection;
	}

	public void setCerhLocalizacaoGeograficaCollection(Collection<CerhLocalizacaoGeografica> cerhLocalizacaoGeograficaCollection) {
		this.cerhLocalizacaoGeograficaCollection = cerhLocalizacaoGeograficaCollection;
	}

	public Collection<CerhProcessoSuspensao> getCerhProcessoSuspensaoCollection() {
		return cerhProcessoSuspensaoCollection;
	}

	public void setCerhProcessoSuspensaoCollection(	Collection<CerhProcessoSuspensao> cerhProcessoSuspensaoCollection) {
		this.cerhProcessoSuspensaoCollection = cerhProcessoSuspensaoCollection;
	}
	
	public CerhProcesso getCerhProcessoPai() {
		return cerhProcessoPai;
	}

	public void setCerhProcessoPai(CerhProcesso cerhProcessoPai) {
		this.cerhProcessoPai = cerhProcessoPai;
	}
	
	@Override
	public CerhProcesso clone() throws CloneNotSupportedException {
		CerhProcesso clone = (CerhProcesso) super.clone();		
		return clone;
	}

	@Override
	public int compareTo(CerhProcesso o) {
		
		if(Util.isNull(ideCerhProcesso)) {
			return -1;
		}
		
		return ideCerhProcesso.compareTo(o.getIdeCerhProcesso());
	}

	

}