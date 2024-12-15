package br.gov.ba.seia.entity;

import javax.persistence.Basic;
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

@Entity
@Table(name="fce_saa")
public class FceSaa {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_seq")
	@SequenceGenerator(name = "fce_saa_seq", sequenceName = "fce_saa_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_saa", nullable = false)
	private Integer ideFceSaa;
	
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;
	
	@Column(name="val_horizonte_projeto")
	private Integer valorHorizonteProjeto;
	
	@Column(name="val_populacao_atendida")
	private Integer valorPopulacaoAtendida;
	
	@Column(name="val_consumo_per_capta")
	private Double valorConsumoPerCapta;
	
	@Column(name="val_vazao_media_projeto")
	private Double valorVazaoMedidaProjeto;
	
	@Column(name="val_ext_total_adutora_bruta")
	private Double valorExtTotalAdutoraBruta;

	@Column(name="val_ext_total_adutora_tratada")
	private Double valorExtTotalAdutoraTratada;
	
	@Column(name="val_reserva_ext_total_rede")
    private Double valorReservaExtTotalRede;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora_bruta", referencedColumnName="ide_faixa_diametro_adutora")
	private FaixaDiametroAdutora ideFaixaDiametroAdutoraBruta;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora_tratada", referencedColumnName="ide_faixa_diametro_adutora")
    private FaixaDiametroAdutora ideFaixaDiametroAdutoraTratada;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_faixa_diametro_adutora_reserva", referencedColumnName="ide_faixa_diametro_adutora")
    private FaixaDiametroAdutora ideFaixaDiametroAdutoraReserva;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_tipo_material_utilizado_bruta", referencedColumnName="ide_tipo_material_utilizado")
    private TipoMaterialUtilizado ideTipoMaterialUtilizadoBruta;
    
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ide_tipo_material_utilizado_tratada", referencedColumnName="ide_tipo_material_utilizado")
    private TipoMaterialUtilizado ideTipoMaterialUtilizadoTratada;
    
    public FceSaa() {

    }
    
    public FceSaa(Fce fce){
    	this.ideFce = fce;
    }

	public Integer getIdeFceSaa() {
		return ideFceSaa;
	}

	public void setIdeFceSaa(Integer ideFceSaa) {
		this.ideFceSaa = ideFceSaa;
	}

	public Double getValorConsumoPerCapta() {
		return valorConsumoPerCapta;
	}

	public void setValorConsumoPerCapta(Double valorConsumoPerCapta) {
		this.valorConsumoPerCapta = valorConsumoPerCapta;
	}

	public Double getValorExtTotalAdutoraBruta() {
		return valorExtTotalAdutoraBruta;
	}

	public void setValorExtTotalAdutoraBruta(Double valorExtTotalAdutoraBruta) {
		this.valorExtTotalAdutoraBruta = valorExtTotalAdutoraBruta;
	}

	public Double getValorExtTotalAdutoraTratada() {
		return valorExtTotalAdutoraTratada;
	}

	public void setValorExtTotalAdutoraTratada(Double valorExtTotalAdutoraTratada) {
		this.valorExtTotalAdutoraTratada = valorExtTotalAdutoraTratada;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutoraBruta() {
		return ideFaixaDiametroAdutoraBruta;
	}

	public void setIdeFaixaDiametroAdutoraBruta(
			FaixaDiametroAdutora ideFaixaDiametroAdutoraBruta) {
		this.ideFaixaDiametroAdutoraBruta = ideFaixaDiametroAdutoraBruta;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutoraTratada() {
		return ideFaixaDiametroAdutoraTratada;
	}

	public void setIdeFaixaDiametroAdutoraTratada(
			FaixaDiametroAdutora ideFaixaDiametroAdutoraTratada) {
		this.ideFaixaDiametroAdutoraTratada = ideFaixaDiametroAdutoraTratada;
	}

	public FaixaDiametroAdutora getIdeFaixaDiametroAdutoraReserva() {
		return ideFaixaDiametroAdutoraReserva;
	}

	public void setIdeFaixaDiametroAdutoraReserva(
			FaixaDiametroAdutora ideFaixaDiametroAdutoraReserva) {
		this.ideFaixaDiametroAdutoraReserva = ideFaixaDiametroAdutoraReserva;
	}

	public TipoMaterialUtilizado getIdeTipoMaterialUtilizadoBruta() {
		return ideTipoMaterialUtilizadoBruta;
	}

	public void setIdeTipoMaterialUtilizadoBruta(
			TipoMaterialUtilizado ideTipoMaterialUtilizadoBruta) {
		this.ideTipoMaterialUtilizadoBruta = ideTipoMaterialUtilizadoBruta;
	}

	public TipoMaterialUtilizado getIdeTipoMaterialUtilizadoTratada() {
		return ideTipoMaterialUtilizadoTratada;
	}

	public void setIdeTipoMaterialUtilizadoTratada(
			TipoMaterialUtilizado ideTipoMaterialUtilizadoTratada) {
		this.ideTipoMaterialUtilizadoTratada = ideTipoMaterialUtilizadoTratada;
	}

	public Integer getValorHorizonteProjeto() {
		return valorHorizonteProjeto;
	}

	public void setValorHorizonteProjeto(Integer valorHorizonteProjeto) {
		this.valorHorizonteProjeto = valorHorizonteProjeto;
	}

	public Integer getValorPopulacaoAtendida() {
		return valorPopulacaoAtendida;
	}

	public void setValorPopulacaoAtendida(Integer valorPopulacaoAtendida) {
		this.valorPopulacaoAtendida = valorPopulacaoAtendida;
	}

	public Double getValorVazaoMedidaProjeto() {
		return valorVazaoMedidaProjeto;
	}

	public void setValorVazaoMedidaProjeto(Double valorVazaoMedidaProjeto) {
		this.valorVazaoMedidaProjeto = valorVazaoMedidaProjeto;
	}

	public Double getValorReservaExtTotalRede() {
		return valorReservaExtTotalRede;
	}

	public void setValorReservaExtTotalRede(Double valorReservaExtTotalRede) {
		this.valorReservaExtTotalRede = valorReservaExtTotalRede;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}
    
}
