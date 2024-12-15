package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.Transient;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TorreAnenometrica.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 19 de set de 2017
 * Objetivo: 	
 */
@Entity
@Table(name="torre_anemometrica")
public class TorreAnemometrica {
	/**
	 * Propriedade: ideTorreAnenometrica
	 * @type: Long
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "torre_anemometrica_ide_torre_anemometrica_seq")
	@SequenceGenerator(name = "torre_anemometrica_ide_torre_anemometrica_seq", sequenceName = "torre_anemometrica_ide_torre_anemometrica_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_torre_anemometrica", nullable = false)
	private Long ideTorreAnemometrica;

	/**
	 * Propriedade: nomTorreAnenometrica
	 * @type: String
	 */
	@Column(name="nom_idenf_torre_anemometrica", nullable = false)
	private String nomTorreAnemometrica; 
	
	/**
	 * Propriedade: valProjecaoMonitoramento
	 * @type: Long
	 */
	@Column(name="val_projecao_monitoramento", nullable = false)
	private Long valProjecaoMonitoramento; 
	
	/**
	 * Propriedade: indConservacaoAmortecimento
	 * @type: Boolean
	 */
	@Column(name="ind_conservacao_amortecimento", nullable = false)
	private Boolean indConservacaoAmortecimento;
	
	/**
	 * Propriedade: areaSupressaoVegetal
	 * @type: Double
	 */
	@Column(name="area_supressao_vegetal", nullable = false)
	private BigDecimal areaSupressaoVegetal;
	
	/**
	 * Propriedade: valAlturaTorre
	 * @type: Double
	 */
	@Column(name="val_altura_torre", nullable = false)
	private Double valAlturaTorre;
	
	/**
	 * Propriedade: indExcluido
	 * @type: Boolean
	 */
	@Column(name="ind_excluido", nullable = false)
	private Boolean indExcluido;
	
	/**
	 * Propriedade: nomUnidadeConservadora
	 * @type: String
	 */
	@Column(name="nom_unidade_conservadora", nullable = false)
	private String nomUnidadeConservadora;

	/**
	 * Propriedade: indAceiteResponsabilidade
	 * @type: Boolean
	 */
	@Column(name="ind_aceite_responsabilidade", nullable = false)
	private Boolean indAceiteResponsabilidade;
	
	/**
	 * Propriedade: ideLocalizacaoGeografica
	 * @type: LocalizacaoGeografica
	 */
	@JoinColumn(name="ide_localizacao_geografica", referencedColumnName="ide_localizacao_geografica", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	/**
	 * Propriedade: ideCadastroAtividadeNaoSugeitLic
	 * @type: CadastroAtividadeNaoSujeitaLic
	 */
	@JoinColumn(name="ide_cadastro_atividade_nao_sujeita_lic", referencedColumnName="ide_cadastro_atividade_nao_sujeita_lic", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSugeitaLic;	
	
	/**
	 * Propriedade: ideNaturezaTorre
	 * @type: Collection<TipoNaturezaTorre>
	 */
	@JoinColumn(name="ide_tipo_natureza_torre", referencedColumnName="ide_tipo_natureza_torre", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoNaturezaTorre ideTipoNaturezaTorre;
	
	/**
	 * Propriedade: ideTipoUnidadeConservacaoTorre
	 * @type: TipoUnidadeConservacaoTorre
	 */
	@JoinColumn(name="ide_tipo_unidade_conservacao_torre", referencedColumnName="ide_tipo_unidade_conservacao_torre", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoUnidadeConservacaoTorre ideTipoUnidadeConservacaoTorre; 

	/**
	 * Propriedade: ideLocalizacaoAtividadeTorre
	 * @type: LocalizacaoAtividadeTorre
	 */
	@JoinColumn( name="ide_localizacao_atividade_torre" , referencedColumnName="ide_localizacao_atividade_torre", nullable=false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoAtividadeTorre ideLocalizacaoAtividadeTorre;
	
	@OneToMany(mappedBy = "ideTorreAnemometrica")
	private List<TorreAnemometricaLocalizacaoAtividadeTorre> torreAnemometricaLocalizacaoAtividadeTorresList;

	@Column(name = "ind_processo_inema", nullable = false)
	private Boolean indProcessoInema;
	
	@Column(name = "num_processo_inema", length = 50)
    private String numProcessoInema;
	
	@Column(name="ind_aceite_instrucoes_cadastro", nullable = false)
	private Boolean indAceiteInstrucoesCadastro;
	
	@Column(name = "ind_possui_cefir", nullable = false)
	private Boolean indPossuiCefir;

	@Column(name = "ind_atividade")
	private Boolean indAtividade;

	@Transient
	private List<LocalizacaoAtividadeTorre> listaLocalizacaoAtividadeTorres;
	
	@Transient
	private List<TorreAnemometricaLocalizacaoAtividadeTorre> torreAnemometricaLocalizacaoAtividadeTorreListAuxiliar;
	
	/**
	 * 
	 */
	public TorreAnemometrica() {
		this.ideCadastroAtividadeNaoSugeitaLic = new CadastroAtividadeNaoSujeitaLic();
		this.indExcluido = Boolean.FALSE;
		this.indAceiteResponsabilidade = Boolean.FALSE;
		this.indAceiteInstrucoesCadastro = Boolean.TRUE;
	}
	/**
	 * Getter do campo ideTorreAnemometrica
	 *	
	 * @return the ideTorreAnemometrica
	 */
	public Long getIdeTorreAnemometrica() {
		return ideTorreAnemometrica;
	}

	/**
	 * Setter do campo  ideTorreAnemometrica
	 * @param ideTorreAnemometrica the ideTorreAnemometrica to set
	 */
	public void setIdeTorreAnemometrica(Long ideTorreAnemometrica) {
		this.ideTorreAnemometrica = ideTorreAnemometrica;
	}

	/**
	 * Getter do campo nomTorreAnemometrica
	 *	
	 * @return the nomTorreAnemometrica
	 */
	public String getNomTorreAnemometrica() {
		return nomTorreAnemometrica;
	}

	/**
	 * Setter do campo  nomTorreAnemometrica
	 * @param nomTorreAnemometrica the nomTorreAnemometrica to set
	 */
	public void setNomTorreAnemometrica(String nomTorreAnemometrica) {
		this.nomTorreAnemometrica = nomTorreAnemometrica;
	}

	/**
	 * Getter do campo valProjecaoMonitoramento
	 *	
	 * @return the valProjecaoMonitoramento
	 */
	public Long getValProjecaoMonitoramento() {
		return valProjecaoMonitoramento;
	}

	/**
	 * Setter do campo  valProjecaoMonitoramento
	 * @param valProjecaoMonitoramento the valProjecaoMonitoramento to set
	 */
	public void setValProjecaoMonitoramento(Long valProjecaoMonitoramento) {
		this.valProjecaoMonitoramento = valProjecaoMonitoramento;
	}

	/**
	 * Getter do campo indConservacaoAmortecimento
	 *	
	 * @return the indConservacaoAmortecimento
	 */
	public Boolean getIndConservacaoAmortecimento() {
		return indConservacaoAmortecimento;
	}

	/**
	 * Setter do campo  indConservacaoAmortecimento
	 * @param indConservacaoAmortecimento the indConservacaoAmortecimento to set
	 */
	public void setIndConservacaoAmortecimento(Boolean indConservacaoAmortecimento) {
		this.indConservacaoAmortecimento = indConservacaoAmortecimento;
	}

	/**
	 * Getter do campo areaSupressaoVegetal
	 *	
	 * @return the areaSupressaoVegetal
	 */
	public BigDecimal getAreaSupressaoVegetal() {
		return areaSupressaoVegetal;
	}

	/**
	 * Setter do campo  areaSupressaoVegetal
	 * @param areaSupressaoVegetal the areaSupressaoVegetal to set
	 */
	public void setAreaSupressaoVegetal(BigDecimal areaSupressaoVegetal) {
		this.areaSupressaoVegetal = areaSupressaoVegetal;
	}

	
	/**
	 * Getter do campo valAlturaTorre
	 *	
	 * @return the valAlturaTorre
	 */
	public Double getValAlturaTorre() {
		return valAlturaTorre;
	}

	/**
	 * Setter do campo  valAlturaTorre
	 * @param valAlturaTorre the valAlturaTorre to set
	 */
	public void setValAlturaTorre(Double valAlturaTorre) {
		this.valAlturaTorre = valAlturaTorre;
	}

	/**
	 * Getter do campo indExcluido
	 *	
	 * @return the indExcluido
	 */
	public Boolean getIndExcluido() {
		return indExcluido;
	}

	/**
	 * Setter do campo  indExcluido
	 * @param indExcluido the indExcluido to set
	 */
	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	/**
	 * Getter do campo nomUnidadeConservadora
	 *	
	 * @return the nomUnidadeConservadora
	 */
	public String getNomUnidadeConservadora() {
		return nomUnidadeConservadora;
	}

	/**
	 * Setter do campo  nomUnidadeConservadora
	 * @param nomUnidadeConservadora the nomUnidadeConservadora to set
	 */
	public void setNomUnidadeConservadora(String nomUnidadeConservadora) {
		this.nomUnidadeConservadora = nomUnidadeConservadora;
	}

	/**
	 * Getter do campo indAceiteResponsabilidade
	 *	
	 * @return the indAceiteResponsabilidade
	 */
	public Boolean getIndAceiteResponsabilidade() {
		return indAceiteResponsabilidade;
	}

	/**
	 * Setter do campo  indAceiteResponsabilidade
	 * @param indAceiteResponsabilidade the indAceiteResponsabilidade to set
	 */
	public void setIndAceiteResponsabilidade(Boolean indAceiteResponsabilidade) {
		this.indAceiteResponsabilidade = indAceiteResponsabilidade;
	}

	/**
	 * Getter do campo ideLocalizacaoGeografica
	 *	
	 * @return the ideLocalizacaoGeografica
	 */
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	/**
	 * Setter do campo  ideLocalizacaoGeografica
	 * @param ideLocalizacaoGeografica the ideLocalizacaoGeografica to set
	 */
	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	/**
	 * Getter do campo ideCadastroAtividadeNaoSugeitaLic
	 *	
	 * @return the ideCadastroAtividadeNaoSugeitaLic
	 */
	public CadastroAtividadeNaoSujeitaLic getIdeCadastroAtividadeNaoSugeitaLic() {
		return ideCadastroAtividadeNaoSugeitaLic;
	}

	/**
	 * Setter do campo  ideCadastroAtividadeNaoSugeitaLic
	 * @param ideCadastroAtividadeNaoSugeitaLic the ideCadastroAtividadeNaoSugeitaLic to set
	 */
	public void setIdeCadastroAtividadeNaoSugeitaLic(CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSugeitaLic) {
		this.ideCadastroAtividadeNaoSugeitaLic = ideCadastroAtividadeNaoSugeitaLic;
	}

	/**
	 * Getter do campo ideTipoNaturezaTorre
	 *	
	 * @return the ideTipoNaturezaTorre
	 */
	public TipoNaturezaTorre getIdeTipoNaturezaTorre() {
		return ideTipoNaturezaTorre;
	}

	/**
	 * Setter do campo  ideTipoNaturezaTorre
	 * @param ideTipoNaturezaTorre the ideTipoNaturezaTorre to set
	 */
	public void setIdeTipoNaturezaTorre(TipoNaturezaTorre ideTipoNaturezaTorre) {
		this.ideTipoNaturezaTorre = ideTipoNaturezaTorre;
	}
	/**
	 * Getter do campo ideTipoUnidadeConservacaoTorre
	 *	
	 * @return the ideTipoUnidadeConservacaoTorre
	 */
	public TipoUnidadeConservacaoTorre getIdeTipoUnidadeConservacaoTorre() {
		return ideTipoUnidadeConservacaoTorre;
	}
	/**
	 * Setter do campo  ideTipoUnidadeConservacaoTorre
	 * @param ideTipoUnidadeConservacaoTorre the ideTipoUnidadeConservacaoTorre to set
	 */
	public void setIdeTipoUnidadeConservacaoTorre(TipoUnidadeConservacaoTorre ideTipoUnidadeConservacaoTorre) {
		this.ideTipoUnidadeConservacaoTorre = ideTipoUnidadeConservacaoTorre;
	}
	/**
	 * Getter do campo ideLocalizacaoAtividadeTorre
	 *	
	 * @return the ideLocalizacaoAtividadeTorre
	 */
	public LocalizacaoAtividadeTorre getIdeLocalizacaoAtividadeTorre() {
		return ideLocalizacaoAtividadeTorre;
	}
	/**
	 * Setter do campo  ideLocalizacaoAtividadeTorre
	 * @param ideLocalizacaoAtividadeTorre the ideLocalizacaoAtividadeTorre to set
	 */
	public void setIdeLocalizacaoAtividadeTorre(LocalizacaoAtividadeTorre ideLocalizacaoAtividadeTorre) {
		this.ideLocalizacaoAtividadeTorre = ideLocalizacaoAtividadeTorre;
	}
	public Boolean getIndProcessoInema() {
		return indProcessoInema;
	}
	public void setIndProcessoInema(Boolean indProcessoInema) {
		this.indProcessoInema = indProcessoInema;
	}
	public String getNumProcessoInema() {
		return numProcessoInema;
	}
	public void setNumProcessoInema(String numProcessoInema) {
		this.numProcessoInema = numProcessoInema;
	}
	public Boolean getIndAceiteInstrucoesCadastro() {
		return indAceiteInstrucoesCadastro;
	}
	public void setIndAceiteInstrucoesCadastro(Boolean indAceiteInstrucoesCadastro) {
		this.indAceiteInstrucoesCadastro = indAceiteInstrucoesCadastro;
	}
	public Boolean getIndPossuiCefir() {
		return indPossuiCefir;
	}
	public void setIndPossuiCefir(Boolean indPossuiCefir) {
		this.indPossuiCefir = indPossuiCefir;
	}
	
	@Transient
	public int getTamanhoIdentificacao(){
		return nomTorreAnemometrica.length();
	}
	public List<LocalizacaoAtividadeTorre> getListaLocalizacaoAtividadeTorres() {
		return listaLocalizacaoAtividadeTorres;
	}
	public void setListaLocalizacaoAtividadeTorres(
			List<LocalizacaoAtividadeTorre> listaLocalizacaoAtividadeTorres) {
		this.listaLocalizacaoAtividadeTorres = listaLocalizacaoAtividadeTorres;
	}
	public List<TorreAnemometricaLocalizacaoAtividadeTorre> getTorreAnemometricaLocalizacaoAtividadeTorreListAuxiliar() {
		return torreAnemometricaLocalizacaoAtividadeTorreListAuxiliar;
	}
	public void setTorreAnemometricaLocalizacaoAtividadeTorreListAuxiliar(
			List<TorreAnemometricaLocalizacaoAtividadeTorre> torreAnemometricaLocalizacaoAtividadeTorreListAuxiliar) {
		this.torreAnemometricaLocalizacaoAtividadeTorreListAuxiliar = torreAnemometricaLocalizacaoAtividadeTorreListAuxiliar;
	}
	public Boolean getIndAtividade() {
		return indAtividade;
	}
	public void setIndAtividade(Boolean indAtividade) {
		this.indAtividade = indAtividade;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTorreAnemometrica == null) ? 0 : ideTorreAnemometrica
						.hashCode());
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
		TorreAnemometrica other = (TorreAnemometrica) obj;
		if (ideTorreAnemometrica == null) {
			if (other.ideTorreAnemometrica != null)
				return false;
		} else if (!ideTorreAnemometrica.equals(other.ideTorreAnemometrica))
			return false;
		return true;
	}
	public List<TorreAnemometricaLocalizacaoAtividadeTorre> getTorreAnemometricaLocalizacaoAtividadeTorresList() {
		return torreAnemometricaLocalizacaoAtividadeTorresList;
	}
	public void setTorreAnemometricaLocalizacaoAtividadeTorresList(
			List<TorreAnemometricaLocalizacaoAtividadeTorre> torreAnemometricaLocalizacaoAtividadeTorresList) {
		this.torreAnemometricaLocalizacaoAtividadeTorresList = torreAnemometricaLocalizacaoAtividadeTorresList;
	}
	
	
}