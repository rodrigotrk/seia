package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "imovel_rural_sicar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImovelRuralSicar.findAll", query = "SELECT i FROM ImovelRuralSicar i"),
    @NamedQuery(name = "ImovelRuralSicar.findByIdeImovelRuralSicar", query = "SELECT i FROM ImovelRuralSicar i WHERE i.ideImovelRuralSicar = :ideImovelRuralSicar")})  
public class ImovelRuralSicar implements Serializable, Cloneable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMOVEL_RURAL_SICAR_IDE_IMOVEL_RURAL_SICAR_SEQ") 
    @SequenceGenerator(name="IMOVEL_RURAL_SICAR_IDE_IMOVEL_RURAL_SICAR_SEQ", sequenceName="IMOVEL_RURAL_SICAR_IDE_IMOVEL_RURAL_SICAR_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_sicar", nullable = false)
	private Integer ideImovelRuralSicar;
		
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
    @ManyToOne(optional = false)
    private ImovelRural ideImovelRural;
	
	@Size(max = 100)
	@Column(name = "num_sicar", nullable = true)
	private String numSicar;
	
	@Size(max = 100)
	@Column(name = "num_protocolo", nullable = true)
	private String numProtocolo;
	
	@Column(name = "json", nullable = false)
	private String json;
	
	@Column(name = "ind_sincronia", nullable = false)
	private boolean indSicronia;
	
	@Column(name = "dtc_ini_sincronia", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcIniSicronia;
	
	@Column(name = "dtc_fim_sincronia", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFimSicronia;
	
	@Size(max = 100)
	@Column(name = "token", nullable = true)
	private String token;
	
	@Size(max = 250)
	@Column(name = "url_recibo_inscricao", nullable = true)
	private String urlReciboInscricao;
	
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Size(max = 5)
	@Column(name = "cod_retorno_sincronia", nullable = true)
	private String codRetornoSincronia;
	
	@Column(name = "msg_retorno_sincronia", nullable = true)
	private String msgRetornoSincronia;
	
	@Transient
	private String numCertificado;
	
	public ImovelRuralSicar() {}
	
	public ImovelRuralSicar(String numSicar) {
		this.numSicar = numSicar;
	}
	
	public ImovelRuralSicar(Integer ideImovelRuralSicar) {
		this.ideImovelRuralSicar = ideImovelRuralSicar;
	}
	
	@Override
	public ImovelRuralSicar clone() throws CloneNotSupportedException {
		return (ImovelRuralSicar) super.clone();
	}
	
	@Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralSicar)) {
            return false;
        }
        ImovelRuralSicar other = (ImovelRuralSicar) object;
        if ((this.ideImovelRuralSicar == null && other.ideImovelRuralSicar != null) || (this.ideImovelRuralSicar != null && !this.ideImovelRuralSicar.equals(other.ideImovelRuralSicar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ImovelRuralSicar[ ideImovelRuralSicar=" + ideImovelRuralSicar + " ]";
    }

	public Integer getIdeImovelRuralSicar() {
		return ideImovelRuralSicar;
	}

	public void setIdeImovelRuralSicar(Integer ideImovelRuralSicar) {
		this.ideImovelRuralSicar = ideImovelRuralSicar;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public String getNumProtocolo() {
		return numProtocolo;
	}

	public void setNumProtocolo(String numProtocolo) {
		this.numProtocolo = numProtocolo;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public boolean isIndSicronia() {
		return indSicronia;
	}

	public void setIndSicronia(boolean indSicronia) {
		this.indSicronia = indSicronia;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcIniSicronia() {
		return dtcIniSicronia;
	}

	public void setDtcIniSicronia(Date dtcIniSicronia) {
		this.dtcIniSicronia = dtcIniSicronia;
	}

	public Date getDtcFimSicronia() {
		return dtcFimSicronia;
	}

	public void setDtcFimSicronia(Date dtcFimSicronia) {
		this.dtcFimSicronia = dtcFimSicronia;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrlReciboInscricao() {
		return urlReciboInscricao;
	}

	public void setUrlReciboInscricao(String urlReciboInscricao) {
		this.urlReciboInscricao = urlReciboInscricao;
	}

	public String getCodRetornoSincronia() {
		return codRetornoSincronia;
	}

	public void setCodRetornoSincronia(String codRetornoSincronia) {
		this.codRetornoSincronia = codRetornoSincronia;
	}

	public String getMsgRetornoSincronia() {
		return msgRetornoSincronia;
	}

	public void setMsgRetornoSincronia(String msgRetornoSincronia) {
		this.msgRetornoSincronia = msgRetornoSincronia;
	}

	public String getNumCertificado() {
		return numCertificado;
	}

	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}
}
