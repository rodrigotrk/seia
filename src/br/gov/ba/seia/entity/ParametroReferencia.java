package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "parametro_referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroReferencia.findAll", query = "SELECT p FROM ParametroReferencia p"),
    @NamedQuery(name = "ParametroReferencia.findByIdeParametroReferencia", query = "SELECT p FROM ParametroReferencia p WHERE p.ideParametroReferencia = :ideParametroReferencia"),
    @NamedQuery(name = "ParametroReferencia.findByValReferenciaMinimo", query = "SELECT p FROM ParametroReferencia p WHERE p.valReferenciaMinimo = :valReferenciaMinimo"),
    @NamedQuery(name = "ParametroReferencia.findByValReferenciaMaximo", query = "SELECT p FROM ParametroReferencia p WHERE p.valReferenciaMaximo = :valReferenciaMaximo")})
public class ParametroReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAMETRO_REFERENCIA_IDE_PARAMETRO_REFERENCIA_seq")
    @SequenceGenerator(name="PARAMETRO_REFERENCIA_IDE_PARAMETRO_REFERENCIA_seq", sequenceName="PARAMETRO_REFERENCIA_IDE_PARAMETRO_REFERENCIA_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_parametro_referencia", nullable = false)
    private Integer ideParametroReferencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "val_referencia_minimo", precision = 14, scale = 3)
    private BigDecimal valReferenciaMinimo;
    @Column(name = "val_referencia_maximo", precision = 14, scale = 3)
    private BigDecimal valReferenciaMaximo;
    @JoinColumn(name = "ide_unidade_medida_tipologia_grupo", referencedColumnName = "ide_unidade_medida_tipologia_grupo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo;
    @JoinColumn(name = "ide_tipologia_tipo_ato", referencedColumnName = "ide_tipologia_tipo_ato", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaTipoAto ideTipologiaTipoAto;
    
    public ParametroReferencia() {
    }

    public ParametroReferencia(Integer ideParametroReferencia) {
        this.ideParametroReferencia = ideParametroReferencia;
    }

    public Integer getIdeParametroReferencia() {
        return ideParametroReferencia;
    }

    public void setIdeParametroReferencia(Integer ideParametroReferencia) {
        this.ideParametroReferencia = ideParametroReferencia;
    }

    public BigDecimal getValReferenciaMinimo() {
        return valReferenciaMinimo;
    }

    public void setValReferenciaMinimo(BigDecimal valReferenciaMinimo) {
        this.valReferenciaMinimo = valReferenciaMinimo;
    }

    public BigDecimal getValReferenciaMaximo() {
        return valReferenciaMaximo;
    }

    public void setValReferenciaMaximo(BigDecimal valReferenciaMaximo) {
        this.valReferenciaMaximo = valReferenciaMaximo;
    }

    public UnidadeMedidaTipologiaGrupo getIdeUnidadeMedidaTipologiaGrupo() {
        return ideUnidadeMedidaTipologiaGrupo;
    }

    public void setIdeUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo) {
        this.ideUnidadeMedidaTipologiaGrupo = ideUnidadeMedidaTipologiaGrupo;
    }

    public TipologiaTipoAto getIdeTipologiaTipoAto() {
        return ideTipologiaTipoAto;
    }

    public void setIdeTipologiaTipoAto(TipologiaTipoAto ideTipologiaTipoAto) {
        this.ideTipologiaTipoAto = ideTipologiaTipoAto;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParametroReferencia != null ? ideParametroReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ParametroReferencia)) {
            return false;
        }
        ParametroReferencia other = (ParametroReferencia) object;
        if ((this.ideParametroReferencia == null && other.ideParametroReferencia != null) || (this.ideParametroReferencia != null && !this.ideParametroReferencia.equals(other.ideParametroReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ParametroReferencia[ ideParametroReferencia=" + ideParametroReferencia + " ]";
    }
    
}
