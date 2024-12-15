package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "vw_tipologia_tipo_ato_unidade_medida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findAll", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findById", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.id = :id"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByIdeTipologiaTipoAto", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.ideTipologiaTipoAto = :ideTipologiaTipoAto"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByIdAto", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.idAto = :idAto"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByNomeAto", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.nomeAto = :nomeAto"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByIdeUnidadeMedida", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.ideUnidadeMedida = :ideUnidadeMedida"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByNomUnidadadeMedida", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.nomUnidadadeMedida = :nomUnidadadeMedida"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByIdeTipologia", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.ideTipologia = :ideTipologia"),
    @NamedQuery(name = "VwTipologiaTipoAtoUnidadeMedida.findByDescTipo", query = "SELECT v FROM VwTipologiaTipoAtoUnidadeMedida v WHERE v.descTipo = :descTipo")})
public class VwTipologiaTipoAtoUnidadeMedida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")		
    private BigInteger id;
    @Column(name = "ide_tipologia_tipo_ato")
    private Integer ideTipologiaTipoAto;
    @Column(name = "ide_tipologia_grupo")
    private Integer idetipologiagrupo;
    @Column(name = "id_ato")
    private Integer idAto;
    @Column(name = "ide_tipologia")
    private Integer ideTipologia;
    @Size(max = 2147483647)
    @Column(name = "nome_ato", length = 2147483647)
    private String nomeAto;
    @Column(name = "ide_unidade_medida_tipologia_grupo")
    private Integer ideUnidadeMedidaTipologiaGrupo;
    @Column(name = "ide_unidade_medida")
    private Integer ideUnidadeMedida;
    @Size(max = 150)
    @Column(name = "nom_unidadade_medida", length = 150)
    private String nomUnidadadeMedida;
    @Size(max = 2147483647)
    @Column(name = "desc_tipo", length = 2147483647)
    private String descTipo;
    
    @Column(name = "valor_minimo" )
    private String valorMinimo;
    @Column(name = "valor_Maximo")
    private String valorMaximo;
    

    public VwTipologiaTipoAtoUnidadeMedida() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getIdeTipologiaTipoAto() {
        return ideTipologiaTipoAto;
    }

    public void setIdeTipologiaTipoAto(Integer ideTipologiaTipoAto) {
        this.ideTipologiaTipoAto = ideTipologiaTipoAto;
    }

    public Integer getIdAto() {
        return idAto;
    }

    public void setIdAto(Integer idAto) {
        this.idAto = idAto;
    }

    public String getNomeAto() {
        return nomeAto;
    }

    public void setNomeAto(String nomeAto) {
        this.nomeAto = nomeAto;
    }

    public Integer getIdeUnidadeMedida() {
        return ideUnidadeMedida;
    }

    public void setIdeUnidadeMedida(Integer ideUnidadeMedida) {
        this.ideUnidadeMedida = ideUnidadeMedida;
    }

    public String getNomUnidadadeMedida() {
        return nomUnidadadeMedida;
    }

    public void setNomUnidadadeMedida(String nomUnidadadeMedida) {
        this.nomUnidadadeMedida = nomUnidadadeMedida;
    }

    public String getDescTipo() {
        return descTipo;
    }

    public void setDescTipo(String descTipo) {
        this.descTipo = descTipo;
    }

	public Integer getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Integer ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public String getValorMinimo() {
		return valorMinimo;
	}
	
	
	
	
	public void setValorMinimo(String valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	
	public String getValorMaximo() {
		return valorMaximo;
	}
	
	public void setValorMaximo(String valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	@Transient
	public String getValorMinimoFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		if(!Util.isNullOuVazio(valorMinimo))
			return df.format(Double.parseDouble(valorMinimo));
		else
			return valorMinimo;
	}
	
	@Transient
	public void setValorMinimoFormatado(String valorMinimo) {
		this.valorMinimo = valorMinimo.replace(',','.');
	}

	@Transient
	public String getValorMaximoFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		if(!Util.isNullOuVazio(valorMaximo))
			return df.format(Double.parseDouble(valorMaximo));
		else
			return valorMaximo;
	}
	
	@Transient
	public void setValorMaximoFormatado(String valorMaximo) {
		this.valorMaximo = valorMaximo.replace(',','.');
	}
	

	
	public Integer getIdeUnidadeMedidaTipologiaGrupo() {
		return ideUnidadeMedidaTipologiaGrupo;
	}

	public void setIdeUnidadeMedidaTipologiaGrupo(Integer ideUnidadeMedidaTipologiaGrupo) {
		this.ideUnidadeMedidaTipologiaGrupo = ideUnidadeMedidaTipologiaGrupo;
	}

	public Integer getIdetipologiagrupo() {
		return idetipologiagrupo;
	}

	public void setIdetipologiagrupo(Integer idetipologiagrupo) {
		this.idetipologiagrupo = idetipologiagrupo;
	}
    
}
