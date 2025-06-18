package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh_barragem_caracterizacao")
public class CerhBarragemCaracterizacao extends AbstractEntityHist implements CerhCaracterizacaoInterface, CerhCaracterizacaoFinalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_barragem_caracterizacao_seq")
	@SequenceGenerator(name = "cerh_barragem_caracterizacao_seq", sequenceName = "cerh_barragem_caracterizacao_seq", allocationSize = 1)
	@Column(name="ide_cerh_barragem_caracterizacao")
	private Integer ideCerhBarragemCaracterizacao;
					
	@Historico(name="Nome do corpo hidríco")
	@Column(name="nom_corpo_hidrico")
	private String nomCorpoHidrico;
	
	@Historico(name="Observação")
	@Size(max=500,message = "O campo observação somente suporta 500 caracteres." )
	@Column(name="dsc_observacao")
	private String dscObservacao;

	@Historico(name="Altura maxima barragem (m)")
	@Column(name="val_altura_maxima_barragem")
	private BigDecimal valAlturaMaximaBarragem;

	@Historico(name="Vazão a ser liberada para jusante (m³/dia)")
	@Column(name="val_vazao_liberada_jusante")
	private BigDecimal valVazaoLiberadaJusante;

	@Historico(name="Vazão regularizada com 90% de garantia (m³/s)")
	@Column(name="val_vazao_regularizada", precision = 12, scale = 4)
	private BigDecimal valVazaoRegularizada;

	@Historico(name="Volume maximo reservatório (m³)")
	@Column(name="val_volume_maximo_reservatorio")
	private BigDecimal valVolumeMaximoReservatorio;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_barragem")
	private TipoBarragem ideTipoBarragem;
	
	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_barragem")
	private Barragem ideBarragem;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_situacao_tipo_uso")
	private CerhSituacaoTipoUso ideCerhSituacaoTipoUso;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_corpo_hidrico")
	private TipoCorpoHidrico ideTipoCorpoHidrico;
	
	@Historico
	@OneToMany(mappedBy="ideCerhBarragemCaracterizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhBarragemCaracterizacaoFinalidade> cerhBarragemCaracterizacaoFinalidadeCollection;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica;
	
	@Transient
	private BarragemDTO barragemDTO;
	
	public CerhBarragemCaracterizacao() {
	}

	public CerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public Integer getIdeCerhBarragemCaracterizacao() {
		return ideCerhBarragemCaracterizacao;
	}

	public void setIdeCerhBarragemCaracterizacao(Integer ideCerhBarragemCaracterizacao) {
		this.ideCerhBarragemCaracterizacao = ideCerhBarragemCaracterizacao;
	}

	public String getNomCorpoHidrico() {
		return nomCorpoHidrico;
	}

	public void setNomCorpoHidrico(String nomCorpoHidrico) {
		this.nomCorpoHidrico = nomCorpoHidrico;
	}

	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	public BigDecimal getValAlturaMaximaBarragem() {
		return valAlturaMaximaBarragem;
	}

	public void setValAlturaMaximaBarragem(BigDecimal valAlturaMaximaBarragem) {
		this.valAlturaMaximaBarragem = valAlturaMaximaBarragem;
	}

	public BigDecimal getValVazaoLiberadaJusante() {
		return valVazaoLiberadaJusante;
	}

	public void setValVazaoLiberadaJusante(BigDecimal valVazaoLiberadaJusante) {
		this.valVazaoLiberadaJusante = valVazaoLiberadaJusante;
	}

	public BigDecimal getValVazaoRegularizada() {
		return valVazaoRegularizada;
	}

	public void setValVazaoRegularizada(BigDecimal valVazaoRegularizada) {
		this.valVazaoRegularizada = valVazaoRegularizada;
	}

	public BigDecimal getValVolumeMaximoReservatorio() {
		return valVolumeMaximoReservatorio;
	}

	public void setValVolumeMaximoReservatorio(BigDecimal valVolumeMaximoReservatorio) {
		this.valVolumeMaximoReservatorio = valVolumeMaximoReservatorio;
	}

	public TipoBarragem getIdeTipoBarragem() {
		return ideTipoBarragem;
	}

	public void setIdeTipoBarragem(TipoBarragem ideTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
	}

	public Barragem getIdeBarragem() {
		return ideBarragem;
	}

	public void setIdeBarragem(Barragem ideBarragem) {
		this.ideBarragem = ideBarragem;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public CerhSituacaoTipoUso getIdeCerhSituacaoTipoUso() {
		return ideCerhSituacaoTipoUso;
	}

	public void setIdeCerhSituacaoTipoUso(CerhSituacaoTipoUso ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}

	public Collection<CerhBarragemCaracterizacaoFinalidade> getCerhBarragemCaracterizacaoFinalidadeCollection() {
		return cerhBarragemCaracterizacaoFinalidadeCollection;
	}

	public void setCerhBarragemCaracterizacaoFinalidadeCollection(
			Collection<CerhBarragemCaracterizacaoFinalidade> cerhBarragemCaracterizacaoFinalidadeCollection) {
		this.cerhBarragemCaracterizacaoFinalidadeCollection = cerhBarragemCaracterizacaoFinalidadeCollection;
	}

	@Override
	public void setIde(Integer ideCerhBarragemCaracterizacao) {
		this.ideCerhBarragemCaracterizacao=ideCerhBarragemCaracterizacao;
	}
	
	@Override
	public Collection<CerhFinalidadeUsoAguaInterface> getFinalidadeCollection() {
		Collection<CerhFinalidadeUsoAguaInterface> coll = null;
		if(!Util.isNullOuVazio(this.cerhBarragemCaracterizacaoFinalidadeCollection)){
			coll = new ArrayList<CerhFinalidadeUsoAguaInterface>();
			for (CerhBarragemCaracterizacaoFinalidade finalidade : this.cerhBarragemCaracterizacaoFinalidadeCollection) {
				CerhFinalidadeUsoAguaInterface inter = finalidade;
				coll.add(inter);
			}
		}
		return coll;
	}

	public TipoCorpoHidrico getIdeTipoCorpoHidrico() {
		return ideTipoCorpoHidrico;
	}

	public void setIdeTipoCorpoHidrico(TipoCorpoHidrico ideTipoCorpoHidrico) {
		this.ideTipoCorpoHidrico = ideTipoCorpoHidrico;
	}

	public BarragemDTO getBarragemDTO() {
		return barragemDTO;
	}

	public void setBarragemDTO(BarragemDTO barragemDTO) {
		this.barragemDTO = barragemDTO;
	}

}