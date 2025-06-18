package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "devedor_reposicao_florestal")
@XmlRootElement
public class DevedorReposicaoFlorestal implements Serializable {

	private static final long serialVersionUID = -3328183101816482610L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEVEDOR_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "DEVEDOR_REPOSICAO_FLORESTAL_SEQ", sequenceName = "DEVEDOR_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_devedor_reposicao_florestal")
	private Integer ideDevedorReposicaoFlorestal;
	
	@JoinColumn(name = "ide_cumprimento_reposicao_florestal", referencedColumnName = "ide_cumprimento_reposicao_florestal", nullable = false)
	@ManyToOne(optional = false)
	private CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal;
	
	@JoinColumn(name = "ide_orgao_emissor_auto", referencedColumnName = "ide_orgao_emissor_auto", nullable = false)
	@ManyToOne(optional = false)
	private OrgaoEmissorAuto ideOrgaoEmissorAuto;
	
	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false)
	@ManyToOne(optional = false)
	private Municipio ideMunicipio;
	
	@Column(name = "num_auto_infracao", length = 50, nullable = false)
	private String numAutoInfracao;
	
	@JoinColumn(name = "ide_bioma", referencedColumnName = "ide_bioma", nullable = false)
	@ManyToOne(optional = false)
	private Bioma ideBioma;
	
	@Column(name = "vlr_area_suprimida", nullable = false, precision = 12, scale = 4)
	private BigDecimal vlrAreaSuprimida;
	
	@Column(name="val_volume_referencia", precision = 12, scale = 2)
	private BigDecimal valVolumeReferencia;
	
	@Column(name="dsc_caminho_parecer_tecnico")
	private String dscCaminhoParecerTecnico;
	
	@Column(name="nome_arquivo")
	private String nomeArquivo;
	
	@Column(name="dtc_gravado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcGravado;

	public Integer getIdeDevedorReposicaoFlorestal() {
		return ideDevedorReposicaoFlorestal;
	}

	public void setIdeDevedorReposicaoFlorestal(Integer ideDevedorReposicaoFlorestal) {
		this.ideDevedorReposicaoFlorestal = ideDevedorReposicaoFlorestal;
	}

	public CumprimentoReposicaoFlorestal getIdeCumprimentoReposicaoFlorestal() {
		return ideCumprimentoReposicaoFlorestal;
	}

	public void setIdeCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}

	public OrgaoEmissorAuto getIdeOrgaoEmissorAuto() {
		return ideOrgaoEmissorAuto;
	}

	public void setIdeOrgaoEmissorAuto(OrgaoEmissorAuto ideOrgaoEmissorAuto) {
		this.ideOrgaoEmissorAuto = ideOrgaoEmissorAuto;
	}

	public Municipio getIdeMunicipio() {
		return ideMunicipio;
	}

	public void setIdeMunicipio(Municipio ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}

	public String getNumAutoInfracao() {
		return numAutoInfracao;
	}

	public void setNumAutoInfracao(String numAutoInfracao) {
		this.numAutoInfracao = numAutoInfracao;
	}

	public Bioma getIdeBioma() {
		return ideBioma;
	}

	public void setIdeBioma(Bioma ideBioma) {
		this.ideBioma = ideBioma;
	}

	public BigDecimal getVlrAreaSuprimida() {
		return vlrAreaSuprimida;
	}

	public void setVlrAreaSuprimida(BigDecimal vlrAreaSuprimida) {
		this.vlrAreaSuprimida = vlrAreaSuprimida;
	}

	public BigDecimal getValVolumeReferencia() {
		return valVolumeReferencia;
	}

	public void setValVolumeReferencia(BigDecimal valVolumeReferencia) {
		this.valVolumeReferencia = valVolumeReferencia;
	}

	public String getDscCaminhoParecerTecnico() {
		return dscCaminhoParecerTecnico;
	}

	public void setDscCaminhoParecerTecnico(String dscCaminhoParecerTecnico) {
		this.dscCaminhoParecerTecnico = dscCaminhoParecerTecnico;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Date getDtcGravado() {
		return dtcGravado;
	}

	public void setDtcGravado(Date dtcGravado) {
		this.dtcGravado = dtcGravado;
	}
	
	@JSON(include = false)
	public String getTamanhoDoc() {
		File arquivo = null;
		if (!Util.isNull(this.dscCaminhoParecerTecnico)) {
			arquivo = new File(this.dscCaminhoParecerTecnico);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}
}
