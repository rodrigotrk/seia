package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "imovel_rural_uso_agua_intervencao")
@XmlRootElement
public class ImovelRuralUsoAguaIntervencao implements Serializable {

	private static final int TRAVESSIA_DUTO = 1;
	private static final int CONSTRUCAO_PONTE = 2;
	private static final int CONSTRUCAO_CAIS = 5;
	private static final int DRENAGEM_PLUVIAL_LANCAMENTO = 6;
	private static final int DESASSOREAMENTO_LIMPEZA = 8;
	private static final int CONSTRUCAO_TRAVESSIA = 11;
	private static final int CONSTRUCAO_PIER = 15;
	
	private static final long serialVersionUID = 5520609592516988326L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imovel_rural_uso_agua_intervencao_ide_uso_agua_intervencao_seq")
	@SequenceGenerator(name = "imovel_rural_uso_agua_intervencao_ide_uso_agua_intervencao_seq", sequenceName="imovel_rural_uso_agua_intervencao_ide_uso_agua_intervencao_seq", allocationSize=1)
	@Basic(optional = false)
	@Column(name = "ide_imovel_rural_uso_agua_intervencao", nullable=false)
	private Integer ideImovelRuralUsoAguaIntervencao;
	
	@JoinColumn(name = "ide_imovel_rural_uso_agua", referencedColumnName = "ide_imovel_rural_uso_agua", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ImovelRuralUsoAgua ideImovelRuralUsoAgua;
	
	@JoinColumn(name = "ide_tipo_intervencao", referencedColumnName = "ide_tipo_intervencao", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
	private TipoIntervencao ideTipoIntervencao;
	
	@Basic(optional = false)
    @Column(name = "barragem_volume")
	private Double volumeBarragem;

	@Basic(optional = false)
	@Column(name = "barragem_area")
	private Double areaBarragem;

	@Basic(optional = false)
	@Column(name = "barragem_tipo")
	@Size(max = 1)
	private String tipoBarragem;

	@Basic(optional = false)
	@Column(name = "barragem_uso")
	@Size(max = 1)
	private String usoBarragem;

	@Basic(optional = false)
	@Column(name = "travessia_tipo")
	@Size(max = 1)
	private String tipoTravessia;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	public Integer getIdeImovelRuralUsoAguaIntervencao() {
		return ideImovelRuralUsoAguaIntervencao;
	}

	public void setIdeImovelRuralUsoAguaIntervencao(
			Integer ideImovelRuralUsoAguaIntervencao) {
		this.ideImovelRuralUsoAguaIntervencao = ideImovelRuralUsoAguaIntervencao;
	}

	public ImovelRuralUsoAgua getIdeImovelRuralUsoAgua() {
		return ideImovelRuralUsoAgua;
	}

	public void setIdeImovelRuralUsoAgua(ImovelRuralUsoAgua ideImovelRuralUsoAgua) {
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
	}

	public Double getVolumeBarragem() {
		return volumeBarragem;
	}

	public void setVolumeBarragem(Double volumeBarragem) {
		this.volumeBarragem = volumeBarragem;
	}

	public Double getAreaBarragem() {
		return areaBarragem;
	}

	public void setAreaBarragem(Double areaBarragem) {
		this.areaBarragem = areaBarragem;
	}

	public String getTipoBarragem() {
		return tipoBarragem;
	}

	public void setTipoBarragem(String tipoBarragem) {
		this.tipoBarragem = tipoBarragem;
	}

	public String getUsoBarragem() {
		return usoBarragem;
	}

	public void setUsoBarragem(String usoBarragem) {
		this.usoBarragem = usoBarragem;
	}



	public String getTipoTravessia() {
		return tipoTravessia;
	}

	public void setTipoTravessia(String tipoTravessia) {
		this.tipoTravessia = tipoTravessia;
	}

	public TipoIntervencao getIdeTipoIntervencao() {
		return ideTipoIntervencao;
	}

	public void setIdeTipoIntervencao(TipoIntervencao ideTipoIntervencao) {
		this.ideTipoIntervencao = ideTipoIntervencao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideImovelRuralUsoAguaIntervencao != null ? ideImovelRuralUsoAguaIntervencao.hashCode() : 0);
        return hash;
    }
	
	@Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralUsoAguaIntervencao)) {
            return false;
        }
        ImovelRuralUsoAguaIntervencao other = (ImovelRuralUsoAguaIntervencao) object;
        if ((this.ideImovelRuralUsoAguaIntervencao == null && other.ideImovelRuralUsoAguaIntervencao != null) || (this.ideImovelRuralUsoAguaIntervencao != null && !this.ideImovelRuralUsoAguaIntervencao.equals(other.ideImovelRuralUsoAguaIntervencao))) {
            return false;
        }
        return true;
    }

	public boolean isTravessiaDuto() {
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == TRAVESSIA_DUTO;
	}
	
	public boolean isConstrucaoPonte() {
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == CONSTRUCAO_PONTE;
	}
	 
	public boolean isConstrucaoCais() {
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == CONSTRUCAO_CAIS;
	}
	
	public boolean isDrenagemPluvialLancamento(){
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == DRENAGEM_PLUVIAL_LANCAMENTO;
	}
	
	public boolean isDesassoreamentoLimpeza(){
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == DESASSOREAMENTO_LIMPEZA;
	}
	
	public boolean isConstrucaoTravessia(){
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == CONSTRUCAO_TRAVESSIA;
	}
	
	public boolean isConstrucaoPier(){
		return getIdeTipoIntervencao().getIdeTipoIntervencao() == CONSTRUCAO_PIER;
	}
}
