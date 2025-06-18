package br.gov.ba.seia.entity;

import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the fce_aquicultura_licenca_tipo_atividade database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca_tipo_atividade")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividade.findAll", query="SELECT f FROM FceAquiculturaLicencaTipoAtividade f"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividade.removeByIdeFceAquiculturaLicencaAndTipoAquicultura", query = "DELETE FROM FceAquiculturaLicencaTipoAtividade f WHERE f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca = :ideFceAquiculturaLicenca AND f.ideTipoAquicultura.ideTipoAquicultura = :ideTipoAquicultura"),
	@NamedQuery(name="FceAquiculturaLicencaTipoAtividade.removeByIdeTipoAquiculturaAndEspecieAquiculturaTipoAtividadeAndFceAquiculturaLicenca", query = "DELETE FROM FceAquiculturaLicencaTipoAtividade f WHERE f.ideTipoAquicultura.ideTipoAquicultura = :ideTipoAquicultura AND f.ideEspecieAquiculturaTipoAtividade.ideEspecieAquiculturaTipoAtividade = :ideEspecieAquiculturaTipoAtividade AND f.ideFceAquiculturaLicenca.ideFceAquiculturaLicenca =:ideFceAquiculturaLicenca")
})
public class FceAquiculturaLicencaTipoAtividade extends AbstractEntity implements Comparable<FceAquiculturaLicencaTipoAtividade>{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_IDEFCEAQUICULTURALICENCATIPOATIVIDADE_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_TIPO_ATIVIDADE_IDEFCEAQUICULTURALICENCATIPOATIVIDADE_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca_tipo_atividade")
	private Integer ideFceAquiculturaLicencaTipoAtividade;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_especie_aquicultura_tipo_atividade", referencedColumnName="ide_especie_aquicultura_tipo_atividade", nullable = false)
	private EspecieAquiculturaTipoAtividade ideEspecieAquiculturaTipoAtividade;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_aquicultura", referencedColumnName="ide_tipo_aquicultura", nullable = false)
	private TipoAquicultura ideTipoAquicultura;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura_licenca", referencedColumnName="ide_fce_aquicultura_licenca", nullable = false)
	private FceAquiculturaLicenca ideFceAquiculturaLicenca;

	@Transient
	private boolean producaoFormaJovem;
	@Transient
	private CaracterizacaoCultivoDTO aquiculturaProducaoDTOFormaJovem;

	@Transient
	private boolean producaoEngorda;
	@Transient
	private CaracterizacaoCultivoDTO aquiculturaProducaoDTOEngorda;
	
	public FceAquiculturaLicencaTipoAtividade() {

	}

	public FceAquiculturaLicencaTipoAtividade(EspecieAquiculturaTipoAtividade especieAquiculturaTipoAtividade, FceAquiculturaLicenca fceAquiculturaLicenca) {
		this.ideEspecieAquiculturaTipoAtividade = especieAquiculturaTipoAtividade;
		this.ideFceAquiculturaLicenca = fceAquiculturaLicenca;
	}

	public Integer getIdeFceAquiculturaLicencaTipoAtividade() {
		return this.ideFceAquiculturaLicencaTipoAtividade;
	}

	public void setIdeFceAquiculturaLicencaTipoAtividade(Integer ideFceAquiculturaLicencaTipoAtividade) {
		this.ideFceAquiculturaLicencaTipoAtividade = ideFceAquiculturaLicencaTipoAtividade;
	}

	public TipoAquicultura getIdeTipoAquicultura() {
		return ideTipoAquicultura;
	}

	public void setIdeTipoAquicultura(TipoAquicultura ideTipoAquicultura) {
		this.ideTipoAquicultura = ideTipoAquicultura;
	}

	public String getNomTipoAtividade(){
		if(!Util.isNullOuVazio(this.ideEspecieAquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade())){
			return this.ideEspecieAquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade().getNomAquiculturaTipoAtividade();
		}
		return " ";
	}

	public String getNomEspecie(){
		return this.ideEspecieAquiculturaTipoAtividade.getIdeEspecie().getNomeToLabel();
	}

	public EspecieAquiculturaTipoAtividade getIdeEspecieAquiculturaTipoAtividade() {
		return ideEspecieAquiculturaTipoAtividade;
	}

	public void setIdeEspecieAquiculturaTipoAtividade(EspecieAquiculturaTipoAtividade ideEspecieAquiculturaTipoAtividade) {
		this.ideEspecieAquiculturaTipoAtividade = ideEspecieAquiculturaTipoAtividade;
	}

	@Override
	public int compareTo(FceAquiculturaLicencaTipoAtividade o) {
		return getNomTipoAtividade().compareTo(o.getNomTipoAtividade());
	}

	public FceAquiculturaLicenca getIdeFceAquiculturaLicenca() {
		return ideFceAquiculturaLicenca;
	}

	public void setIdeFceAquiculturaLicenca(FceAquiculturaLicenca ideFceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
	}

	public CaracterizacaoCultivoDTO getAquiculturaProducaoDTOFormaJovem() {
		return aquiculturaProducaoDTOFormaJovem;
	}

	public void setAquiculturaProducaoDTOFormaJovem(CaracterizacaoCultivoDTO aquiculturaProducaoDTOFormaJovem) {
		this.aquiculturaProducaoDTOFormaJovem = aquiculturaProducaoDTOFormaJovem;
	}

	public CaracterizacaoCultivoDTO getAquiculturaProducaoDTOEngorda() {
		return aquiculturaProducaoDTOEngorda;
	}

	public void setAquiculturaProducaoDTOEngorda(CaracterizacaoCultivoDTO aquiculturaProducaoDTOEngorda) {
		this.aquiculturaProducaoDTOEngorda = aquiculturaProducaoDTOEngorda;
	}

	public boolean isProducaoFormaJovem() {
		return producaoFormaJovem;
	}

	public void setProducaoFormaJovem(boolean producaoFormaJovem) {
		this.producaoFormaJovem = producaoFormaJovem;
	}

	public boolean isProducaoEngorda() {
		return producaoEngorda;
	}

	public void setProducaoEngorda(boolean producaoEngorda) {
		this.producaoEngorda = producaoEngorda;
	}

	/*
	 * Flags
	 */
	public boolean isTipoProducaoChecked(){
		return producaoEngorda || producaoFormaJovem;
	}

	public boolean isTipoInstalacaoRacewaysInFormaJovem(){
		return aquiculturaProducaoDTOFormaJovem.getFceAquiculturaLicencaTipoAtividadeTipoProducao().isTipoInstalacaoRaceways();	
	}

	public boolean isTipoInstalacaoOutros(CaracterizacaoCultivoDTO caracterizacaoCultivoDTO){
		return isTipoInstalacaoOutros(caracterizacaoCultivoDTO.getListaTipoInstalacao());
	}
	
	public boolean isOrganismosOutros(){
		for(Organismo organismo : aquiculturaProducaoDTOFormaJovem.getListaOrganismo()){
			if(organismo.isOutros() && organismo.isRowSelect()){
				return true;
			}
		}
		return false;
	}

	public boolean isTipoInstalacaoRacewaysInEngorda(){
		return aquiculturaProducaoDTOEngorda.getFceAquiculturaLicencaTipoAtividadeTipoProducao().isTipoInstalacaoRaceways();
	}

	public boolean isOutrasEspecies(){
		return !Util.isNull(ideEspecieAquiculturaTipoAtividade)
				&& !Util.isNull(ideEspecieAquiculturaTipoAtividade.getIdeEspecie()) && ideEspecieAquiculturaTipoAtividade.getIdeEspecie().isOutros();
	}

	private boolean isTipoInstalacaoOutros(List<TipoInstalacao> listaTipoInstalacao) {
		for(TipoInstalacao tipoInstalacao : listaTipoInstalacao){
			if(tipoInstalacao.isOutros() && tipoInstalacao.isRowSelect()){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEspecieAquiculturaTipoAtividade == null) ? 0
						: ideEspecieAquiculturaTipoAtividade.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaLicenca == null) ? 0
						: ideFceAquiculturaLicenca.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaLicencaTipoAtividade == null) ? 0
						: ideFceAquiculturaLicencaTipoAtividade.hashCode());
		result = prime
				* result
				+ ((ideTipoAquicultura == null) ? 0 : ideTipoAquicultura
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
		FceAquiculturaLicencaTipoAtividade other = (FceAquiculturaLicencaTipoAtividade) obj;
		if (ideEspecieAquiculturaTipoAtividade == null) {
			if (other.ideEspecieAquiculturaTipoAtividade != null)
				return false;
		} else if (!ideEspecieAquiculturaTipoAtividade
				.equals(other.ideEspecieAquiculturaTipoAtividade))
			return false;
		if (ideFceAquiculturaLicenca == null) {
			if (other.ideFceAquiculturaLicenca != null)
				return false;
		} else if (!ideFceAquiculturaLicenca
				.equals(other.ideFceAquiculturaLicenca))
			return false;
		if (ideFceAquiculturaLicencaTipoAtividade == null) {
			if (other.ideFceAquiculturaLicencaTipoAtividade != null)
				return false;
		} else if (!ideFceAquiculturaLicencaTipoAtividade
				.equals(other.ideFceAquiculturaLicencaTipoAtividade))
			return false;
		if (ideTipoAquicultura == null) {
			if (other.ideTipoAquicultura != null)
				return false;
		} else if (!ideTipoAquicultura.equals(other.ideTipoAquicultura))
			return false;
		return true;
	}
}