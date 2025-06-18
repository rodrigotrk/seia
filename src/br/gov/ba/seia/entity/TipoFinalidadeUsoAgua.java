package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;

@Entity
@Table(name = "tipo_finalidade_uso_agua", uniqueConstraints = { @UniqueConstraint(columnNames = { "nom_tipo_finalidade_uso_agua" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoFinalidadeUsoAgua.findAll", query = "SELECT t FROM TipoFinalidadeUsoAgua t ORDER BY t.nomTipoFinalidadeUsoAgua"),
		@NamedQuery(name = "TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByRequerimentoUnico", query = "SELECT t FROM TipoFinalidadeUsoAgua t left join t.requerimentoUnicoCollection ru WHERE ru.ideRequerimentoUnico = :ideRequerimentoUnico AND t.indRequerimento = :indRequerimento"),
		@NamedQuery(name = "TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByTipologiaAndAto", query = "SELECT tfua FROM AtoAmbientalTipologiaFinalidade atf INNER JOIN atf.ideTipoFinalidadeUsoAgua tfua INNER JOIN atf.ideAtoAmbientalTipologia at  WHERE at.ideTipologia = :ideTipologia and at.ideAtoAmbiental = :ideAtoAmbiental AND tfua.indRequerimento = :indRequerimento"),
		@NamedQuery(name = "TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByTipologia", query = "SELECT DISTINCT atf.ideTipoFinalidadeUsoAgua FROM AtoAmbientalTipologiaFinalidade atf INNER JOIN atf.ideTipoFinalidadeUsoAgua tfua INNER JOIN atf.ideAtoAmbientalTipologia at INNER JOIN  at.ideTipologia t WHERE t.ideTipologia = :ideTipologia AND tfua.indRequerimento = :indRequerimento") })
public class TipoFinalidadeUsoAgua extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_tipo_finalidade_uso_agua", nullable = false)
	private Integer ideTipoFinalidadeUsoAgua;
	
	@Historico(name="Finalidade/Origem")
	@Basic(optional = false)
	@Column(name = "nom_tipo_finalidade_uso_agua", nullable = false, length = 30)
	private String nomTipoFinalidadeUsoAgua;
	
	@ManyToMany(mappedBy = "tipoFinalidadeUsoAguaCollection", fetch = FetchType.LAZY)
	private Collection<RequerimentoUnico> requerimentoUnicoCollection;
	
	@OneToMany(mappedBy = "ideTipoFinalidadeUsoAgua" ,fetch = FetchType.LAZY)
	private Collection<TipoIntervencao> tipoIntervencaoCollection;
	
	@OneToMany(mappedBy = "ideTipoFinalidadeUsoAgua" ,fetch = FetchType.LAZY)
	private Collection<FceOutorgaLocalizacaoGeograficaFinalidade> fceOutorgaLocalizacaoGeograficaFinalidadeCollection; 
	
	@OneToMany(mappedBy = "ideTipoFinalidadeUsoAgua" ,fetch = FetchType.LAZY)
	private Collection<AtoAmbientalTipologiaFinalidade> atoAmbientalTipologiaFinalidadeCollection; 
	
	@Column(name = "ind_ativo")
	private Boolean indAtivo;
	
	@Column(name = "ind_requerimento")
	private Boolean indRequerimento;
	
	@Column(name = "ind_cerh")
	private Boolean indCerh;
	
	@Transient
	private boolean indCaptacao;

	@Transient
	private boolean checked;
	
	
	public TipoFinalidadeUsoAgua() {
	}

	public TipoFinalidadeUsoAgua(Integer ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}
	
	public TipoFinalidadeUsoAgua(Integer ideTipoFinalidadeUsoAgua, boolean indCaptacao) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
		this.indCaptacao = indCaptacao;
	}

	public TipoFinalidadeUsoAgua(Integer ideTipoFinalidadeUsoAgua, String nomTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
		this.nomTipoFinalidadeUsoAgua = nomTipoFinalidadeUsoAgua;
	}

	public Integer getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(Integer ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public String getNomTipoFinalidadeUsoAgua() {
		return nomTipoFinalidadeUsoAgua;
	}

	public void setNomTipoFinalidadeUsoAgua(String nomTipoFinalidadeUsoAgua) {
		this.nomTipoFinalidadeUsoAgua = nomTipoFinalidadeUsoAgua;
	}

	public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
		return requerimentoUnicoCollection;
	}

	public void setRequerimentoUnicoCollection(Collection<RequerimentoUnico> requerimentoUnicoCollection) {
		this.requerimentoUnicoCollection = requerimentoUnicoCollection;
	}

	public boolean getIndCaptacao() {
		return indCaptacao;
	}

	public void setIndCaptacao(boolean indCaptacao) {
		this.indCaptacao = indCaptacao;
	}

	public Collection<TipoIntervencao> getTipoIntervencaoCollection() {
		return tipoIntervencaoCollection;
	}

	public void setTipoIntervencaoCollection(Collection<TipoIntervencao> tipoIntervencaoCollection) {
		this.tipoIntervencaoCollection = tipoIntervencaoCollection;
	}
	
	public Collection<FceOutorgaLocalizacaoGeograficaFinalidade> getFceOutorgaLocalizacaoGeograficaFinalidadeCollection() {
		return fceOutorgaLocalizacaoGeograficaFinalidadeCollection;
	}

	public void setFceOutorgaLocalizacaoGeograficaFinalidadeCollection(Collection<FceOutorgaLocalizacaoGeograficaFinalidade> fceOutorgaLocalizacaoGeograficaFinalidadeCollection) {
		this.fceOutorgaLocalizacaoGeograficaFinalidadeCollection = fceOutorgaLocalizacaoGeograficaFinalidadeCollection;
	}

	public Collection<AtoAmbientalTipologiaFinalidade> getAtoAmbientalTipologiaFinalidadeCollection() {
		return atoAmbientalTipologiaFinalidadeCollection;
	}

	public void setAtoAmbientalTipologiaFinalidadeCollection(Collection<AtoAmbientalTipologiaFinalidade> atoAmbientalTipologiaFinalidadeCollection) {
		this.atoAmbientalTipologiaFinalidadeCollection = atoAmbientalTipologiaFinalidadeCollection;
	}

	public Boolean getIndRequerimento() {
		return indRequerimento;
	}

	public void setIndRequerimento(Boolean indRequerimento) {
		this.indRequerimento = indRequerimento;
	}

	public Boolean getIndCerh() {
		return indCerh;
	}

	public void setIndCerh(Boolean indCerh) {
		this.indCerh = indCerh;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public boolean isFinalidade(TipoFinalidadeUsoAguaEnum tipoEnum){
		return tipoEnum.getId().equals(ideTipoFinalidadeUsoAgua);
	}
	
	public boolean isFinalidadeOutrosUsos(){
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.OUTROS.getId()) ||
			   this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.OUTRA.getId());
	}	

	public boolean isFinalidadeMineracao() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.MINERACAO.getId());
	}

	public boolean isFinalidadeAbastecimentoIndustrial() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId());
	}

	public boolean isFinalidadeIrrigacao() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId());
	}

	public boolean isFinalidadeAbastecimentoPublico() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_PUBLICO.getId()); 
	}

	public boolean isFinalidadeMineracaoAreia() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA.getId());
	}

	public boolean isFinalidadeTermoeletrica() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId());
	}

	public boolean isFinalidadeTransposicao() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO.getId());
	}
	
	public boolean isFinalidadeAproveitamentoHidreletrico() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.APROVEITAMENTO_HIDRELETRICO.getId());
	}
	
	public boolean isFinalidadeEsgotamentoSanitario() {
		return this.ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId()); 
	}

}
