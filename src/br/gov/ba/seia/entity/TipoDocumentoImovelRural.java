package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoDocumentoImovelRuralEnum;

@Entity
@Table(name = "tipo_documento_imovel_rural", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsc_tipo_documento_imovel_rural"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoDocumentoImovelRural.findAll", query = "SELECT t FROM TipoDocumentoImovelRural t"),
	@NamedQuery(name = "TipoDocumentoImovelRural.findByIdeTipoDocumentoImovelRural", query = "SELECT t FROM TipoDocumentoImovelRural t WHERE t.ideTipoDocumentoImovelRural = :ideTipoDocumentoImovelRural"),
	@NamedQuery(name = "TipoDocumentoImovelRural.findByDscTipoDocumentoImovelRural", query = "SELECT t FROM TipoDocumentoImovelRural t WHERE t.dscTipoDocumentoImovelRural = :dscTipoDocumentoImovelRural"),
	@NamedQuery(name = "TipoDocumentoImovelRural.findByIdeTipoVinculoImovel", query = "SELECT t FROM TipoDocumentoImovelRural t WHERE t.ideTipoVinculoImovel.ideTipoVinculoImovel = :ideTipoVinculoImovel order by t.dscTipoDocumentoImovelRural"),
	@NamedQuery(name = "TipoDocumentoImovelRural.findByideTipoTerritorioPct", query = "SELECT t FROM TipoDocumentoImovelRural t WHERE t.ideTipoTerritorioPct.ideTipoTerritorioPct = :ideTipoTerritorioPct order by t.dscTipoDocumentoImovelRural")})
public class TipoDocumentoImovelRural extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_documento_imovel_rural", nullable = false)
    private Integer ideTipoDocumentoImovelRural;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "sgl_tipo_documento_imovel_rural", nullable = false, length = 5)
    private String sglTipoDocumentoImovelRural;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_documento_imovel_rural", nullable = false, length = 100)
    private String dscTipoDocumentoImovelRural;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_grupo_documento", nullable = false)
    private Integer numGrupoDocumento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @JoinColumn(name = "ide_tipo_vinculo_imovel", referencedColumnName = "ide_tipo_vinculo_imovel", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoVinculoImovel ideTipoVinculoImovel;
    
    @JoinColumn(name = "ide_tipo_territorio_pct", referencedColumnName = "ide_tipo_territorio_pct", nullable = true)
    @ManyToOne(optional = false)
    private TipoTerritorioPct ideTipoTerritorioPct;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoDocumentoImovelRural", fetch = FetchType.LAZY)
    private Collection<DocumentoImovelRuralPosse> documentoImovelRuralPosseCollection;

    public TipoDocumentoImovelRural() {}

    public TipoDocumentoImovelRural(Integer ideTipoDocumentoImovelRural) {
        this.ideTipoDocumentoImovelRural = ideTipoDocumentoImovelRural;
    }
    
    public TipoDocumentoImovelRural(TipoDocumentoImovelRuralEnum tipoDocumentoImovelRuralEnum) {
    	this.ideTipoDocumentoImovelRural = tipoDocumentoImovelRuralEnum.getId();
    }

    public TipoDocumentoImovelRural(Integer ideTipoDocumentoImovelRural, String dscTipoDocumentoImovelRural) {
        this.ideTipoDocumentoImovelRural = ideTipoDocumentoImovelRural;
        this.dscTipoDocumentoImovelRural = dscTipoDocumentoImovelRural;
    }
    
    /*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/
    
    public Integer getIdeTipoDocumentoImovelRural() {
        return ideTipoDocumentoImovelRural;
    }

    public void setIdeTipoDocumentoImovelRural(Integer ideTipoDocumentoImovelRural) {
        this.ideTipoDocumentoImovelRural = ideTipoDocumentoImovelRural;
    }

    public String getDscTipoDocumentoImovelRural() {
        return dscTipoDocumentoImovelRural;
    }

    public void setDscTipoDocumentoImovelRural(String dscTipoDocumentoImovelRural) {
        this.dscTipoDocumentoImovelRural = dscTipoDocumentoImovelRural;
    }

    public String getSglTipoDocumentoImovelRural() {
		return sglTipoDocumentoImovelRural;
	}

	public void setSglTipoDocumentoImovelRural(String sglTipoDocumentoImovelRural) {
		this.sglTipoDocumentoImovelRural = sglTipoDocumentoImovelRural;
	}

	public TipoVinculoImovel getIdeTipoVinculoImovel() {
		return ideTipoVinculoImovel;
	}

	public void setIdeTipoVinculoImovel(TipoVinculoImovel ideTipoVinculoImovel) {
		this.ideTipoVinculoImovel = ideTipoVinculoImovel;
	}

	public Integer getNumGrupoDocumento() {
		return numGrupoDocumento;
	}

	public void setNumGrupoDocumento(Integer numGrupoDocumento) {
		this.numGrupoDocumento = numGrupoDocumento;
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

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public TipoTerritorioPct getIdeTipoTerritorioPct() {
		return ideTipoTerritorioPct;
	}

	public void setIdeTipoTerritorioPct(TipoTerritorioPct ideTipoTerritorioPct) {
		this.ideTipoTerritorioPct = ideTipoTerritorioPct;
	}			

	@XmlTransient
    public Collection<DocumentoImovelRuralPosse> getDocumentoImovelRuralPosseCollection() {
        return documentoImovelRuralPosseCollection;
    }

    public void setDocumentoImovelRuralPosseCollection(Collection<DocumentoImovelRuralPosse> documentoImovelRuralPosseCollection) {
        this.documentoImovelRuralPosseCollection = documentoImovelRuralPosseCollection;
    }
}
