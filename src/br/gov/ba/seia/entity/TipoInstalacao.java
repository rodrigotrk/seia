package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.TipoInstalacaoEnum;
import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


/**
 * Entidade que representa a tabela <i>tipo_instalacao</i>, utilizada na <b>Abas Viveiro Escavado e Tanque Rede</b> do FCE - Licenciamento para Aquicultura.
 * <ul>
 * 		<li>01-'Baias'</li>
 * 		<li>02-'Raceways'</li>
 * 		<li>03-'Tanques de alvenaria'</li>
 * 		<li>04-'Tanques de fibra ou PVC'</li>
 * 		<li>05-'Outros'</li>
 * 		<li>06-'Tanques-rede'</li>
 * 		<li>07-'Raceway ou Similar'</li>
 * 		<li>08-'Viveiros Escavados'</li>
 * 		<li>09-'Baias semi-inundadas (anfigranjas)'</li>
 * 		<li>10-'Baias inundadas'</li>
 * 		<li>11-'Sistema vertical'</li>
 * 		<li>12-'Long-line'</li>
 * 		<li>13-'Multi long-line/balsas'</li>
 * 		<li>14-'Tanques em terra'</li>
 * 		<li>15-'Varal Fixo'</li>
 * 		<li>16-'Balsa'</li>
 * 		<li>17-'Mesa'</li>
 * </ul>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 28/05/2015
 * @see #6934
 */
@Entity
@Table(name="tipo_instalacao")
@NamedQuery(name="TipoInstalacao.findAll", query="SELECT t FROM TipoInstalacao t")
public class TipoInstalacao implements Serializable, BaseEntity, Comparable<TipoInstalacao> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_INSTALACAO_IDETIPOINSTALACAO_GENERATOR", sequenceName="TIPO_INSTALACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_INSTALACAO_IDETIPOINSTALACAO_GENERATOR")
	@Column(name="ide_tipo_instalacao", nullable = false)
	private Integer ideTipoInstalacao;

	@Column(name="ind_ativo", nullable = false)
	private Boolean indAtivo;

	@Column(name="nom_tipo_instalacao", nullable = false)
	private String nomTipoInstalacao;

	@Transient
	private TipoProducaoEnum tipoProducaoEnum;

	@Transient
	private boolean rowSelect = false;

	@Transient
	private Integer numEstrutura;

	public TipoInstalacao() {
	}

	public TipoInstalacao(TipoInstalacaoEnum tipoInstalacaoEnum) {
		this.ideTipoInstalacao = tipoInstalacaoEnum.getId();
		this.nomTipoInstalacao = tipoInstalacaoEnum.getNomTipoInstalacao();
	}

	public Integer getIdeTipoInstalacao() {
		return this.ideTipoInstalacao;
	}

	public void setIdeTipoInstalacao(Integer ideTipoInstalacao) {
		this.ideTipoInstalacao = ideTipoInstalacao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoInstalacao() {
		return this.nomTipoInstalacao;
	}

	public void setNomTipoInstalacao(String nomTipoInstalacao) {
		this.nomTipoInstalacao = nomTipoInstalacao;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoInstalacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoInstalacao == null) ? 0 : ideTipoInstalacao
						.hashCode());
		result = prime
				* result
				+ ((nomTipoInstalacao == null) ? 0 : nomTipoInstalacao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TipoInstalacao other = (TipoInstalacao) obj;
		if (ideTipoInstalacao == null) {
			if (other.ideTipoInstalacao != null) {
				return false;
			}
		} else if (!ideTipoInstalacao.equals(other.ideTipoInstalacao)) {
			return false;
		}
		if (nomTipoInstalacao == null) {
			if (other.nomTipoInstalacao != null) {
				return false;
			}
		} else if (!nomTipoInstalacao.equals(other.nomTipoInstalacao)) {
			return false;
		}
		return true;
	}

	public TipoProducaoEnum getTipoProducaoEnum() {
		return tipoProducaoEnum;
	}

	public void setTipoProducaoEnum(TipoProducaoEnum tipoProducaoEnum) {
		this.tipoProducaoEnum = tipoProducaoEnum;
	}

	public boolean isRaceway(){
		return this.nomTipoInstalacao.compareTo("Raceway ou Similar") == 0;
	}

	public boolean isOutros(){
		return this.nomTipoInstalacao.compareTo("Outros") == 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TipoInstalacao o) {
		return this.ideTipoInstalacao.compareTo(o.getIdeTipoInstalacao());
	}

	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
		if(!this.rowSelect){
			this.numEstrutura = null;
		} else {
			if(isOutros()){
				JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
			}
		}
	}

	public Integer getNumEstrutura() {
		return numEstrutura;
	}

	public void setNumEstrutura(Integer numEstrutura) {
		this.numEstrutura = numEstrutura;
	}
}