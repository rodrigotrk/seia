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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh")
public class Cerh extends AbstractEntityHist implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_seq")
	@SequenceGenerator(name = "cerh_seq", sequenceName = "cerh_seq", allocationSize = 1)
	@Column(name="ide_cerh")
	private Integer ideCerh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_cadastro")
	private Date dtcCadastro;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;
	
	@Column(name="ind_historico" , nullable = false)
	private boolean indHistorico;

	@Column(name="num_cadastro")
	private String numCadastro;
	
	@Historico
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_contrato_convenio")
	private ContratoConvenio ideContratoConvenio;
	
	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_empreendimento")
	private Empreendimento ideEmpreendimento;
	
	@Historico
	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="ide_pessoa_fisica_cadastro")
	private PessoaFisica idePessoaFisicaCadastro;

	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="ide_pessoa_requerente")
	private Pessoa idePessoaRequerente;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh_pai")
	private Cerh ideCerhPai;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_certificado", referencedColumnName = "ide_certificado")
	private Certificado ideCertificado;
	
	@ManyToOne
	@JoinColumn(name = "ide_cerh_status", referencedColumnName = "ide_cerh_status")
	private CerhStatus ideCerhStatus;

	@OneToOne(mappedBy="ideCerh", cascade=CascadeType.ALL)
	private CerhCadastroCancelado ideCerhCadastroCancelado;

	@Historico(subTable=true)
	@OneToMany(mappedBy="ideCerh", cascade=CascadeType.ALL)
	private Collection<CerhProcesso> cerhProcessoCollection;

	@Historico
	@OneToMany(mappedBy="ideCerh", cascade=CascadeType.ALL)
	private Collection<CerhRespostaDadosGerais> cerhRespostaDadosGeraisCollection;

	@Historico
	@OneToMany(mappedBy="ideCerh")
	private Collection<CerhTipoUso> cerhTipoUsoCollection;

	@OneToMany(mappedBy="cerh")
	private Collection<CerhCobranca> cerhCobrancaCollection;
	
	public Cerh() {
	}
	
	public Cerh(Integer ideCerh) {
		this.ideCerh = ideCerh;
	}
	
	public boolean isCadastroCompleto() {
		return new CerhStatus(CerhStatusEnum.CADASTRO_COMPLETO).equals(ideCerhStatus);
	}
	
	public Cerh getCerhHistorico() {
		Cerh retorno = Util.isNull(ideCerhPai) ? this : ideCerhPai;
		return retorno;
	}

	public Integer getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Integer ideCerh) {
		this.ideCerh = ideCerh;
	}

	public Date getDtcCadastro() {
		return dtcCadastro;
	}

	public void setDtcCadastro(Date dtcCadastro) {
		this.dtcCadastro = dtcCadastro;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNumCadastro() {
		return numCadastro;
	}
	
	public String getNumCadastroTabela() {
		String numCadastro = Util.isNull(ideCerhPai) ? this.numCadastro : this.ideCerhPai.getNumCadastro();
		String numTabela;
		
		if(!Util.isNullOuVazio(numCadastro)){
			numTabela = ajustarNumero(numCadastro);
		} else {
			numTabela = "-";
		}
		
		return numTabela;
	}

	private String ajustarNumero(String strNumero) {
		String retorno;
		int index = strNumero.indexOf('/');
		retorno = strNumero.substring(0, index);
		retorno = retorno + ' ' + strNumero.substring(index);
		return retorno;
	}

	public void setNumCadastro(String numCadastro) {
		this.numCadastro = numCadastro;
	}

	public ContratoConvenio getIdeContratoConvenio() {
		return ideContratoConvenio;
	}

	public void setIdeContratoConvenio(ContratoConvenio ideContratoConvenio) {
		this.ideContratoConvenio = ideContratoConvenio;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public PessoaFisica getIdePessoaFisicaCadastro() {
		return idePessoaFisicaCadastro;
	}

	public void setIdePessoaFisicaCadastro(PessoaFisica idePessoaFisicaCadastro) {
		this.idePessoaFisicaCadastro = idePessoaFisicaCadastro;
	}

	public Pessoa getIdePessoaRequerente() {
		return idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Pessoa idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}
                                    
	public CerhCadastroCancelado getIdeCerhCadastroCancelado() {
		return ideCerhCadastroCancelado;
	}

	public void setIdeCerhCadastroCancelado(CerhCadastroCancelado ideCerhCadastroCancelado) {
		this.ideCerhCadastroCancelado = ideCerhCadastroCancelado;
	}

	public Collection<CerhProcesso> getCerhProcessoCollection() {
		return cerhProcessoCollection;
	}

	public void setCerhProcessoCollection(Collection<CerhProcesso> cerhProcessoCollection) {
		this.cerhProcessoCollection = cerhProcessoCollection;
	}

	public Collection<CerhRespostaDadosGerais> getCerhRespostaDadosGeraisCollection() {
		return cerhRespostaDadosGeraisCollection;
	}

	public void setCerhRespostaDadosGeraisCollection(Collection<CerhRespostaDadosGerais> cerhRespostaDadosGeraisCollection) {
		this.cerhRespostaDadosGeraisCollection = cerhRespostaDadosGeraisCollection;
	}

	public Collection<CerhTipoUso> getCerhTipoUsoCollection() {
		return cerhTipoUsoCollection;
	}

	public void setCerhTipoUsoCollection(Collection<CerhTipoUso> cerhTipoUsoCollection) {
		this.cerhTipoUsoCollection = cerhTipoUsoCollection;
	}

	public Certificado getIdeCertificado() {
		return this.ideCertificado;
	}
	
	public void setIdeCertificado(Certificado certificado) {
		this.ideCertificado = certificado;
	}
	
	public Cerh getIdeCerhPai() {
		return ideCerhPai;
	}

	public void setIdeCerhPai(Cerh ideCerhPai) {
		this.ideCerhPai = ideCerhPai;
	}
	
	public void setIdeCerhStatus(CerhStatus ideCerhStatus) {
		this.ideCerhStatus = ideCerhStatus;
	}
	
	public CerhStatus getIdeCerhStatus() {
		return ideCerhStatus;
	}
	
	public boolean getIndHistorico() {
		return indHistorico;
	}

	public void setIndHistorico(boolean indHistorico) {
		this.indHistorico = indHistorico;
	}

	public Collection<CerhCobranca> getCerhCobrancaCollection() {
		return cerhCobrancaCollection;
	}

	public void setCerhCobrancaCollection(Collection<CerhCobranca> cerhCobrancaCollection) {
		this.cerhCobrancaCollection = cerhCobrancaCollection;
	}
	
	public Cerh clone() throws CloneNotSupportedException {
		Cerh clone = (Cerh) super.clone();
		clone.setCerhProcessoCollection(Util.deepCloneList(cerhProcessoCollection));
		clone.setCerhRespostaDadosGeraisCollection(Util.deepCloneList(cerhRespostaDadosGeraisCollection));
		clone.setCerhTipoUsoCollection(Util.deepCloneList(cerhTipoUsoCollection));
		return clone;
	}
	
}