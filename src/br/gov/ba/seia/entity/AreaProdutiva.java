package br.gov.ba.seia.entity;

import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "area_produtiva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProdutiva.findAll", query = "SELECT a FROM AreaProdutiva a"),
    @NamedQuery(name = "AreaProdutiva.findByIdeAreaProdutiva", query = "SELECT a FROM AreaProdutiva a WHERE a.ideAreaProdutiva = :ideAreaProdutiva"),
    @NamedQuery(name = "AreaProdutiva.findByValArea", query = "SELECT a FROM AreaProdutiva a WHERE a.valArea = :valArea"),
    @NamedQuery(name = "AreaProdutiva.findByLicenciada", query = "SELECT a FROM AreaProdutiva a WHERE a.licenciada = :licenciada")})
public class AreaProdutiva extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_PRODUTIVA_IDE_AREA_PRODUTIVA_SEQ") 
    @SequenceGenerator(name="AREA_PRODUTIVA_IDE_AREA_PRODUTIVA_SEQ", sequenceName="AREA_PRODUTIVA_IDE_AREA_PRODUTIVA_SEQ", allocationSize=1)
	@Column(name = "ide_area_produtiva", nullable = false)
    private Integer ideAreaProdutiva;

    @Basic(optional = false)
    @NotNull
    @Column(name = "val_area", precision = 14, scale = 2)
    private Double valArea;
    
    @Basic(optional = false)
    @Column(name = "licenciada")
    private Boolean licenciada;
    
    @Column(name = "num_processo")
    private String numProcesso;
    
    @Size(max = 500)
    @Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;
    
    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ImovelRural ideImovelRural;
    
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private LocalizacaoGeografica ideLocalizacaoGeografica;
    
    @JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Tipologia ideTipologia;

    @JoinColumn(name = "ide_tipologia_subgrupo", referencedColumnName = "ide_tipologia")
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private Tipologia ideTipologiaSubgrupo;
    
    @OneToMany(mappedBy = "ideAreaProdutiva")
    @Cascade(CascadeType.ALL)
    private Collection<AreaProdutivaTipologiaAtividade> areaProdutivaTipologiaAtividadeCollection;
    
    @JoinColumn(name = "ide_documento_autorizacao_manejo", referencedColumnName = "ide_documento_imovel_rural")
    @OneToOne(fetch = FetchType.LAZY)
    private DocumentoImovelRural ideDocumentoAutorizacaoManejo;
    
    @Column(name = "num_unidade_producao")
    private Integer numUnidadeProducao;


    @Basic(optional = true)
    @Column(name = "num_processo_amc", nullable = true)
    private String numProcessoAmc;
    
    @Transient
    private boolean indExcluido;
    
    @Transient
    private String codigoPersistirShape;
    
    @Transient
    private GeoJsonSicar geoJsonSicar;
    
    public AreaProdutiva() {
    }
    
    public AreaProdutiva(Integer ideAreaProdutiva) {
        this.ideAreaProdutiva = ideAreaProdutiva;
    }

    public AreaProdutiva(Integer ideAreaProdutiva, Double valArea, boolean licenciada) {
        this.ideAreaProdutiva = ideAreaProdutiva;
        this.valArea = valArea;
        this.licenciada = licenciada;
    }

    public Integer getIdeAreaProdutiva() {
        return ideAreaProdutiva;
    }

    public void setIdeAreaProdutiva(Integer ideAreaProdutiva) {
        this.ideAreaProdutiva = ideAreaProdutiva;
    }

    public Double getValArea() {
        return valArea;
    }

    public void setValArea(Double valArea) {
        this.valArea = valArea;
    }

    public Boolean getLicenciada() {
        return licenciada;
    }

    public void setLicenciada(Boolean licenciada) {
        this.licenciada = licenciada;
    }

    public Tipologia getIdeTipologia() {
        return ideTipologia;
    }

    public void setIdeTipologia(Tipologia ideTipologia) {
        this.ideTipologia = ideTipologia;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }

    public ImovelRural getIdeImovelRural() {
        return ideImovelRural;
    }

    public void setIdeImovelRural(ImovelRural ideImovelRural) {
        this.ideImovelRural = ideImovelRural;
    }    

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

    public Collection<AreaProdutivaTipologiaAtividade> getAreaProdutivaTipologiaAtividadeCollection() {
		return areaProdutivaTipologiaAtividadeCollection;
	}

	public void setAreaProdutivaTipologiaAtividadeCollection(
			Collection<AreaProdutivaTipologiaAtividade> areaProdutivaTipologiaAtividadeCollection) {
		this.areaProdutivaTipologiaAtividadeCollection = areaProdutivaTipologiaAtividadeCollection;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	public String getCodigoPersistirShape() {
		return codigoPersistirShape;
	}

	public void setCodigoPersistirShape(String codigoPersistirShape) {
		this.codigoPersistirShape = codigoPersistirShape;
	}
    
	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public Tipologia getIdeTipologiaSubgrupo() {
		return ideTipologiaSubgrupo;
	}

	public void setIdeTipologiaSubgrupo(Tipologia ideTipologiaSubgrupo) {
		this.ideTipologiaSubgrupo = ideTipologiaSubgrupo;
	}

	public boolean possuiTipologiaCadastrada() {
		return !Util.isNull(this.getIdeTipologia()) && !Util.isNull(this.getIdeTipologia().getIdeTipologia());
	}

	public boolean possuiTipologiaSubGrupoCadastrada() {
		return !Util.isNull(this.getIdeTipologiaSubgrupo()) && !Util.isNull(this.getIdeTipologiaSubgrupo().getIdeTipologia());
	}

	public boolean possuiAreaCadastrada() {
		return !Util.isNull(this.getValArea());
	}
	
	public boolean possuiTipologiaAtividadeCadastrada() {
		return !Util.isNullOuVazio(areaProdutivaTipologiaAtividadeCollection);
	}

	public boolean podeCadastrarTipologiaSomenteUmaVez(Tipologia tipologia) {
		Integer ideTipologia = tipologia.getIdeTipologia();
		return ideTipologia.equals(TipologiaCefirEnum.ALGICULTURA.getId()) 
				|| ideTipologia.equals(TipologiaCefirEnum.MALACOCULTURA.getId()) 
				|| ideTipologia.equals(TipologiaCefirEnum.RANICULTURA.getId())
				|| ideTipologia.equals(TipologiaCefirEnum.MANEJO_FLORESTAL_SUSTENTAVEL.getId())
				|| ideTipologia.equals(TipologiaCefirEnum.MANEJO_CABRUCA.getId())
				|| ideTipologia.equals(TipologiaCefirEnum.AGRICULTURA_DE_SEQUEIRO.getId())
				|| ideTipologia.equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())
				|| ideTipologia.equals(TipologiaCefirEnum.PECUARIA.getId());
	}
	
	
	public boolean isManejoSustentavel() {
		return possuiTipologiaCadastrada() && this.getIdeTipologia().getIdeTipologia().equals(TipologiaCefirEnum.MANEJO_FLORESTAL_SUSTENTAVEL.getId());
	}

	public DocumentoImovelRural getIdeDocumentoAutorizacaoManejo() {
		return ideDocumentoAutorizacaoManejo;
	}

	public void setIdeDocumentoAutorizacaoManejo(DocumentoImovelRural ideDocumentoAutorizacaoManejo) {
		this.ideDocumentoAutorizacaoManejo = ideDocumentoAutorizacaoManejo;
	}

	public Integer getNumUnidadeProducao() {
		return numUnidadeProducao;
	}

	public void setNumUnidadeProducao(Integer numUnidadeProducao) {
		this.numUnidadeProducao = numUnidadeProducao;
	}

	public String getNumProcessoAmc() {
		return numProcessoAmc;
	}

	public void setNumProcessoAmc(String numProcessoAmc) {
		this.numProcessoAmc = numProcessoAmc;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}
	
	@Override
	public AreaProdutiva clone() throws CloneNotSupportedException {
		return (AreaProdutiva) super.clone();
	}
}
