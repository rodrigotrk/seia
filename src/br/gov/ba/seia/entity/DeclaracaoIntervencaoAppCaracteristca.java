package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;

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

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;


/**
 * @author eduardo.fernandes
 * @since 09/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="declaracao_intervencao_app_caracteristca")
@NamedQueries({	
	@NamedQuery(name="DeclaracaoIntervencaoAppCaracteristca.findAll", query="SELECT d FROM DeclaracaoIntervencaoAppCaracteristca d"),
	@NamedQuery(name="DeclaracaoIntervencaoAppCaracteristca.removerByDeclaracaoIntervencaoApp", query="DELETE FROM DeclaracaoIntervencaoAppCaracteristca d WHERE d.declaracaoIntervencaoApp.ideDeclaracaoIntervencaoApp = :ideDeclaracaoIntervencaoApp")
})
public class DeclaracaoIntervencaoAppCaracteristca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="declaracao_intervencao_app_caracteristca_seq", sequenceName="declaracao_intervencao_app_caracteristca_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="declaracao_intervencao_app_caracteristca_seq")
	@Column(name="ide_declaracao_intervencao_app_caracteristica")
	private Integer ideDeclaracaoIntervencaoAppCaracteristica;

	@Column(name="des_caminho_arquivo_decreto")
	private String desCaminhoArquivoDecreto;

	//bi-directional many-to-one association to CaracteristicaAtividadeIntervencaoApp
	@ManyToOne
	@JoinColumn(name="ide_caracteristica_atividade_intervencao_app")
	private CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp;

	//bi-directional many-to-one association to DeclaracaoIntervencaoApp
	@ManyToOne
	@JoinColumn(name="ide_declaracao_intervencao_app")
	private DeclaracaoIntervencaoApp declaracaoIntervencaoApp;

	@Basic(optional = false)
	@Column(name = "ind_documento_validado")
	private Boolean indDocumentoValidado=Boolean.FALSE;
	
	public DeclaracaoIntervencaoAppCaracteristca() {
	}

	public DeclaracaoIntervencaoAppCaracteristca(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		this.declaracaoIntervencaoApp = declaracaoIntervencaoApp;
	}

	/**
	 * @param diap
	 * @param caracteristica
	 * @param atividade
	 */
	public DeclaracaoIntervencaoAppCaracteristca(DeclaracaoIntervencaoApp diap, CaracteristicaIntervencaoApp caracteristica, AtividadeIntervencaoApp atividade) {
		this.caracteristicaAtividadeIntervencaoApp = new CaracteristicaAtividadeIntervencaoApp(caracteristica, atividade);
		this.declaracaoIntervencaoApp = diap;
	}

	public Integer getIdeDeclaracaoIntervencaoAppCaracteristica() {
		return this.ideDeclaracaoIntervencaoAppCaracteristica;
	}

	public void setIdeDeclaracaoIntervencaoAppCaracteristica(Integer ideDeclaracaoIntervencaoAppCaracteristica) {
		this.ideDeclaracaoIntervencaoAppCaracteristica = ideDeclaracaoIntervencaoAppCaracteristica;
	}

	public String getDesCaminhoArquivoDecreto() {
		return this.desCaminhoArquivoDecreto;
	}

	public void setDesCaminhoArquivoDecreto(String desCaminhoArquivoDecreto) {
		this.desCaminhoArquivoDecreto = desCaminhoArquivoDecreto;
	}

	public CaracteristicaAtividadeIntervencaoApp getCaracteristicaAtividadeIntervencaoApp() {
		return this.caracteristicaAtividadeIntervencaoApp;
	}

	public void setCaracteristicaAtividadeIntervencaoApp(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) {
		this.caracteristicaAtividadeIntervencaoApp = caracteristicaAtividadeIntervencaoApp;
	}

	public DeclaracaoIntervencaoApp getDeclaracaoIntervencaoApp() {
		return this.declaracaoIntervencaoApp;
	}

	public void setDeclaracaoIntervencaoApp(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		this.declaracaoIntervencaoApp = declaracaoIntervencaoApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((caracteristicaAtividadeIntervencaoApp == null) ? 0
						: caracteristicaAtividadeIntervencaoApp.hashCode());
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
		DeclaracaoIntervencaoAppCaracteristca other = (DeclaracaoIntervencaoAppCaracteristca) obj;
		if (caracteristicaAtividadeIntervencaoApp == null) {
			if (other.caracteristicaAtividadeIntervencaoApp != null)
				return false;
		} else if (!caracteristicaAtividadeIntervencaoApp
				.equals(other.caracteristicaAtividadeIntervencaoApp))
			return false;
		return true;
	}

	@JSON(include = false)
	public String getFileNameDoc() {
		if (!Util.isNullOuVazio(this.desCaminhoArquivoDecreto)) {
			return FileUploadUtil.getFileName(this.desCaminhoArquivoDecreto);
		}
		return "";
	}

	@JSON(include = false)
	public String getTamanhoDoc() {
		File arquivo = null;
		if (!Util.isNull(this.desCaminhoArquivoDecreto)) {
			arquivo = new File(this.desCaminhoArquivoDecreto);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}

	public Boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(Boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}
}