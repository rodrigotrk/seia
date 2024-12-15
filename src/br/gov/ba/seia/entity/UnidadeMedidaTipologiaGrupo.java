package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "unidade_medida_tipologia_grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeMedidaTipologiaGrupo.findAll", query = "SELECT u FROM UnidadeMedidaTipologiaGrupo u"),
    @NamedQuery(name = "UnidadeMedidaTipologiaGrupo.findByIdeUnidadeMedidaTipologiaGrupo", query = "SELECT u FROM UnidadeMedidaTipologiaGrupo u WHERE u.ideUnidadeMedidaTipologiaGrupo = :ideUnidadeMedidaTipologiaGrupo"),
    @NamedQuery(name = "UnidadeMedidaTipologiaGrupo.findByEmpreenfimento", query = "SELECT u FROM UnidadeMedidaTipologiaGrupo u left join u.ideTipologiaGrupo tg left join tg.empreendimentoTipologiaCollection et WHERE et.empreendimento.ideEmpreendimento = :ideEmpreendimento")})
public class UnidadeMedidaTipologiaGrupo extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @SequenceGenerator(name="ide_unidade_medida_tipologia_grupo_seq", sequenceName="ide_unidade_medida_tipologia_grupo_seq",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_unidade_medida_tipologia_grupo_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_unidade_medida_tipologia_grupo", nullable = false)
    private Integer ideUnidadeMedidaTipologiaGrupo;
    
	@JoinColumn(name = "ide_unidade_medida", referencedColumnName = "ide_unidade_medida", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private UnidadeMedida ideUnidadeMedida;
    
	@JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo ideTipologiaGrupo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideUnidadeMedidaTipologiaGrupo", fetch = FetchType.LAZY)
	private Collection<RequerimentoTipologia> requerimentoTipologiaCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideUnidadeMedidaTipologiaGrupo", fetch = FetchType.LAZY)
	private Collection<ParametroReferencia> parametroReferenciaCollection;

    public UnidadeMedidaTipologiaGrupo() {
    }
    
    
    public UnidadeMedidaTipologiaGrupo(TipologiaGrupo ideTipologiaGrupo, UnidadeMedida ideUnidadeMedida) {
    	this.ideTipologiaGrupo = ideTipologiaGrupo;
    	this.ideUnidadeMedida = ideUnidadeMedida;
    }
    
    
    

    public UnidadeMedidaTipologiaGrupo(Integer ideUnidadeMedidaTipologiaGrupo) {
        this.ideUnidadeMedidaTipologiaGrupo = ideUnidadeMedidaTipologiaGrupo;
    }

    public Integer getIdeUnidadeMedidaTipologiaGrupo() {
        return ideUnidadeMedidaTipologiaGrupo;
    }

    public void setIdeUnidadeMedidaTipologiaGrupo(Integer ideUnidadeMedidaTipologiaGrupo) {
        this.ideUnidadeMedidaTipologiaGrupo = ideUnidadeMedidaTipologiaGrupo;
    }

    @XmlTransient
    public Collection<RequerimentoTipologia> getRequerimentoTipologiaCollection() {
        return requerimentoTipologiaCollection;
    }

    public void setRequerimentoTipologiaCollection(Collection<RequerimentoTipologia> requerimentoTipologiaCollection) {
        this.requerimentoTipologiaCollection = requerimentoTipologiaCollection;
    }

    @XmlTransient
    public Collection<ParametroReferencia> getParametroReferenciaCollection() {
        return parametroReferenciaCollection;
    }

    public void setParametroReferenciaCollection(Collection<ParametroReferencia> parametroReferenciaCollection) {
        this.parametroReferenciaCollection = parametroReferenciaCollection;
    }

    public UnidadeMedida getIdeUnidadeMedida() {
        return ideUnidadeMedida;
    }

    public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
        this.ideUnidadeMedida = ideUnidadeMedida;
    }

    public TipologiaGrupo getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(TipologiaGrupo ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }
}
