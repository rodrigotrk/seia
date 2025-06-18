package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_intervencao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_intervencao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoIntervencao.findAll", query = "SELECT t FROM TipoIntervencao t order by t.nomTipoIntervencao"),
    @NamedQuery(name = "TipoIntervencao.findByIdeTipoIntervencao", query = "SELECT t FROM TipoIntervencao t WHERE t.ideTipoIntervencao = :ideTipoIntervencao"),
    @NamedQuery(name = "TipoIntervencao.findByIndAtivo", query = "SELECT t FROM TipoIntervencao t WHERE t.indAtivo = :indAtivo"),
    @NamedQuery(name = "TipoIntervencao.findByNomTipoIntervencao", query = "SELECT t FROM TipoIntervencao t WHERE t.nomTipoIntervencao = :nomTipoIntervencao"),
    @NamedQuery(name = "TipoIntervencao.findTipoIntervencaoByRequerimentoUnico", query = "SELECT t FROM TipoIntervencao t join t.requerimentoUnicoCollection ru WHERE ru.ideRequerimentoUnico = :ideRequerimentoUnico")})
@NamedNativeQueries({
	@NamedNativeQuery(name = "TipoIntervencao.findByIdeOutorga", query = "SELECT ti.nom_tipo_intervencao, ti.ide_tipo_intervencao,ti.ind_ativo FROM tipo_intervencao ti INNER JOIN outorga_tipo_intervencao oti ON oti.ide_tipo_intervencao = ti.ide_tipo_intervencao INNER JOIN outorga o ON o.ide_outorga = oti.ide_outorga WHERE o.ide_outorga = :ideOutorga",resultClass=TipoIntervencao.class),
	@NamedNativeQuery(name = "TipoIntervencao.excluirByIdeTipoIntervencao", query="DELETE FROM outorga_tipo_intervencao WHERE ide_tipo_intervencao = :ideTipoIntervencao AND ide_outorga = :ideOutorga",resultClass=TipoIntervencao.class)})
public class TipoIntervencao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ide_tipo_intervencao", nullable = false)
    private Integer ideTipoIntervencao;
    
    @Historico
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nom_tipo_intervencao", nullable = false, length = 80)
    private String nomTipoIntervencao;
    
	@Basic(optional = true)
    @NotNull
    @Column(name = "ind_ativo")
    private Boolean indAtivo;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
    
    @ManyToMany(mappedBy = "tipoIntervencaoCollection", fetch = FetchType.LAZY)    
    private Collection<RequerimentoUnico> requerimentoUnicoCollection;
    
    @OneToOne
    @JoinColumn(name = "ide_tipo_intervencao_pai", referencedColumnName = "ide_tipo_intervencao")
    private TipoIntervencao ideTipoIntervencaoPai;

    public TipoIntervencao() {
    }

    public TipoIntervencao(Integer ideTipoIntervencao) {
        this.ideTipoIntervencao = ideTipoIntervencao;
    }

    public TipoIntervencao(Integer ideTipoIntervencao, String nomTipoIntervencao) {
        this.ideTipoIntervencao = ideTipoIntervencao;
        this.nomTipoIntervencao = nomTipoIntervencao;
    }

    public Integer getIdeTipoIntervencao() {
        return ideTipoIntervencao;
    }

    public void setIdeTipoIntervencao(Integer ideTipoIntervencao) {
        this.ideTipoIntervencao = ideTipoIntervencao;
    }

    public String getNomTipoIntervencao() {
        return nomTipoIntervencao;
    }

    public void setNomTipoIntervencao(String nomTipoIntervencao) {
        this.nomTipoIntervencao = nomTipoIntervencao;
    }

    public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
		return requerimentoUnicoCollection;
	}

	public void setRequerimentoUnicoCollection(
			Collection<RequerimentoUnico> requerimentoUnicoCollection) {
		this.requerimentoUnicoCollection = requerimentoUnicoCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoIntervencao != null ? ideTipoIntervencao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoIntervencao)) {
            return false;
        }
        TipoIntervencao other = (TipoIntervencao) object;
        if ((this.ideTipoIntervencao == null && other.ideTipoIntervencao != null) || (this.ideTipoIntervencao != null && !this.ideTipoIntervencao.equals(other.ideTipoIntervencao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(ideTipoIntervencao);
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoIntervencao);
	}

	public Boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
    
	public boolean isAquicultura(){
		return TipoIntervencaoEnum.AQUICULTURA.getId().equals(this.ideTipoIntervencao);
	}
	
	public boolean isExplotacao(){
		return TipoIntervencaoEnum.EXPLOTACAO.getId().equals(this.ideTipoIntervencao);
	}
	
	public boolean isBarragem(){
		return TipoIntervencaoEnum.CONSTRUCAO_BARRAGEM.getId().equals(this.ideTipoIntervencao);
	}
	
	public boolean isTravessia(){
		return TipoIntervencaoEnum.TRAVESSIA.getId().equals(this.ideTipoIntervencao);
	}

	public boolean isDesassoreamento() {
		return TipoIntervencaoEnum.DESASSOREAMENTO.getId().equals(this.ideTipoIntervencao);
	}

	public boolean isConstrucaoCais() {
		return TipoIntervencaoEnum.CONSTRUCAO_CAIS.getId().equals(this.ideTipoIntervencao);
	}
	
	public boolean isTravessiaDuto(){
		return TipoIntervencaoEnum.TRAVESSIA_DUTO.getId().equals(this.ideTipoIntervencao);
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	/**
	 * @return the ideTipoIntervencaoPai
	 */
	public TipoIntervencao getIdeTipoIntervencaoPai() {
		return ideTipoIntervencaoPai;
	}

	/**
	 * @param ideTipoIntervencaoPai the ideTipoIntervencaoPai to set
	 */
	public void setIdeTipoIntervencaoPai(TipoIntervencao ideTipoIntervencaoPai) {
		this.ideTipoIntervencaoPai = ideTipoIntervencaoPai;
	}
	
}
