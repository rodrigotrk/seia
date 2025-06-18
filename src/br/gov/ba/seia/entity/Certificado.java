package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.util.Util;

@Table(name = "certificado")
@Entity
public class Certificado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "certificado_ide_generator", sequenceName = "certificado_ide_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificado_ide_generator")
	@Column(name = "ide_certificado")
	private Integer ideCertificado;

	@Column(name = "dtc_emissao_certificado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEmissaoCertificado;
	
	@Column(name = "num_certificado")
	private String numCertificado;
	
	@Column(name = "num_token")
	private String numToken;

	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbiental;

	@JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orgao ideOrgao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento requerimento;

	@JoinColumn(name = "ide_tipo_certificado", referencedColumnName = "ide_tipo_certificado", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoCertificado tipoCertificado;

	@OneToOne(mappedBy = "ideCertificado", fetch = FetchType.LAZY)
	private CadastroAtividadeNaoSujeitaLic cadastro;
	
	@OneToOne(mappedBy = "ideCertificado", fetch = FetchType.LAZY)
	private Cerh ideCerh;
	
    @Column(name = "ind_excluido")
    private boolean indExcluido;
	
	public boolean isIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Certificado() {}
	
	public boolean isCefir(){
		return !Util.isNull(this.tipoCertificado) && !Util.isNull(this.tipoCertificado.getIdeTipoCertificado())  
				&& TipoCertificadoEnum.CEFIR.getId().equals(this.tipoCertificado.getIdeTipoCertificado());
	}
	
	public boolean isTermoCompromisso(){
		return !Util.isNull(this.tipoCertificado) && !Util.isNull(this.tipoCertificado.getIdeTipoCertificado())  
				&& TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId().equals(this.tipoCertificado.getIdeTipoCertificado());
	}
	
	public boolean isAvisoBndes(){
		return !Util.isNull(this.tipoCertificado) && !Util.isNull(this.tipoCertificado.getIdeTipoCertificado())  
				&& TipoCertificadoEnum.AVISO_BNDES.getId().equals(this.tipoCertificado.getIdeTipoCertificado());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
		result = prime * result + ((requerimento == null) ? 0 : requerimento.hashCode());
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
		Certificado other = (Certificado) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (requerimento == null) {
			if (other.requerimento != null)
				return false;
		} else if (!requerimento.equals(other.requerimento))
			return false;
		return true;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeCertificado() {
		return this.ideCertificado;
	}

	public void setIdeCertificado(Integer ideCertificado) {
		this.ideCertificado = ideCertificado;
	}

	public Date getDtcEmissaoCertificado() {
		return dtcEmissaoCertificado;
	}

	public void setDtcEmissaoCertificado(Date dtcEmissaoCertificado) {
		this.dtcEmissaoCertificado = dtcEmissaoCertificado;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public Orgao getIdeOrgao() {
		return ideOrgao;
	}

	public void setIdeOrgao(Orgao ideOrgao) {
		this.ideOrgao = ideOrgao;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public String getNumCertificado() {
		return this.numCertificado;
	}

	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}

	public String getNumToken() {
		return numToken;
	}

	public void setNumToken(String numToken) {
		this.numToken = numToken;
	}

	public TipoCertificado getTipoCertificado() {
		return tipoCertificado;
	}

	public void setTipoCertificado(TipoCertificado tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}
	
	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public Cerh getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Cerh ideCerh) {
		this.ideCerh = ideCerh;
	}
}