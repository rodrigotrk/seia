package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "parametro_calculo")
@NamedQueries({@NamedQuery(name = "ParametroCalculo.findAll", query = "SELECT p FROM ParametroCalculo p")})
public class ParametroCalculo implements Serializable, BaseEntity, Comparable<ParametroCalculo>, Cloneable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_parametro_calculo")
    private Integer ideParametroCalculo;
    
	@Column(name = "valor_taxa")
    private BigDecimal valorTaxa;
    
	@Column(name = "area_minima")
    private BigDecimal areaMinima;
    
	@Column(name = "area_maxima")
    private BigDecimal areaMaxima;
    
	@Column(name = "num_ufir")
    private BigDecimal numUfir;
    
	@Basic(optional = false)
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @Column(name = "ind_boleto")
    private boolean indBoleto;
    
	@Basic(optional = false)
    @Column(name = "ind_ativo")
    private boolean indAtivo;
	
	@Column(name = "fator_multiplicador")
	private BigDecimal fatorMultiplicador;
	
	@Column(name = "vazao_minima")
    private BigDecimal vazaoMinima;
    
	@Column(name = "vazao_maxima")
    private BigDecimal vazaoMaxima;
	
	@JoinColumn(name = "ide_classe", referencedColumnName = "ide_classe")
	@ManyToOne
	private Classe ideClasse;
	
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tipologia ideTipologia;
    
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
    @ManyToOne
    private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
    
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
    @ManyToOne
    private AtoAmbiental ideAtoAmbiental;
	
	@JoinColumn(name = "ide_tipo_criadouro_fauna", referencedColumnName = "ide_tipo_criadouro_fauna")
    @ManyToOne
	private TipoCriadouroFauna ideTipoCriadouroFauna;
	
	@JoinColumn(name = "ide_bioma", referencedColumnName = "ide_bioma")
	@ManyToOne
	private Bioma ideBioma;

    public ParametroCalculo() {}

    public ParametroCalculo(Integer ideParametroCalculo) {
        this.ideParametroCalculo = ideParametroCalculo;
    }

    public ParametroCalculo(Integer ideParametroCalculo, Date dtcCriacao, boolean indBoleto, boolean indAtivo) {
        this.ideParametroCalculo = ideParametroCalculo;
        this.dtcCriacao = dtcCriacao;
        this.indBoleto = indBoleto;
        this.indAtivo = indAtivo;
    }

    public Integer getIdeParametroCalculo() {
        return ideParametroCalculo;
    }

    public void setIdeParametroCalculo(Integer ideParametroCalculo) {
        this.ideParametroCalculo = ideParametroCalculo;
    }

    public BigDecimal getValorTaxa() {
		return valorTaxa;
	}

	public void setValorTaxa(BigDecimal valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public BigDecimal getAreaMinima() {
        return areaMinima;
    }

    public void setAreaMinima(BigDecimal areaMinima) {
        this.areaMinima = areaMinima;
    }

    public BigDecimal getAreaMaxima() {
        return areaMaxima;
    }

    public void setAreaMaxima(BigDecimal areaMaxima) {
        this.areaMaxima = areaMaxima;
    }


    public Classe getIdeClasse() {
		return ideClasse;
	}

	public void setIdeClasse(Classe ideClasse) {
		this.ideClasse = ideClasse;
	}

	public BigDecimal getNumUfir() {
        return numUfir;
    }

    public void setNumUfir(BigDecimal numUfir) {
        this.numUfir = numUfir;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndBoleto() {
        return indBoleto;
    }

    public void setIndBoleto(boolean indBoleto) {
        this.indBoleto = indBoleto;
    }

    public boolean getIndAtivo() {
        return indAtivo;
    }

    public void setIndAtivo(boolean indAtivo) {
        this.indAtivo = indAtivo;
    }

    public Tipologia getIdeTipologia() {
        return ideTipologia;
    }

    public void setIdeTipologia(Tipologia ideTipologia) {
        this.ideTipologia = ideTipologia;
    }

    public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
        return ideTipoFinalidadeUsoAgua;
    }

    public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
        this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
    }

    public AtoAmbiental getIdeAtoAmbiental() {
        return ideAtoAmbiental;
    }

    public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
        this.ideAtoAmbiental = ideAtoAmbiental;
    }

    public boolean isDAE(){
    	return !indBoleto;
    }
    
    public BigDecimal getFatorMultiplicador() {
		return fatorMultiplicador;
	}

	public void setFatorMultiplicador(BigDecimal fatorMultiplicador) {
		this.fatorMultiplicador = fatorMultiplicador;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParametroCalculo != null ? ideParametroCalculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParametroCalculo)) {
            return false;
        }
        ParametroCalculo other = (ParametroCalculo) object;
        if ((this.ideParametroCalculo == null && other.ideParametroCalculo != null) || (this.ideParametroCalculo != null && !this.ideParametroCalculo.equals(other.ideParametroCalculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ParametroCalculo[ ideParametroCalculo=" + ideParametroCalculo + " ]";
    }

	public boolean isExigeCalculo() {
		return !Util.isNull(areaMaxima) || !Util.isNull(areaMinima) || !Util.isNull(fatorMultiplicador) ;
	}

	@Override
	public Long getId() {
		return new Long(ideParametroCalculo);
	}
	
	public String getDscAtoTipologiaFinalidade() {
		
		String dsc = ideAtoAmbiental.getNomAtoAmbiental();
		
		if(!Util.isNullOuVazio(ideTipologia)) {
			dsc += (" - " + ideTipologia.getDesTipologia());
		}
		
		if (!Util.isNullOuVazio(ideTipoFinalidadeUsoAgua)) {
			dsc += (" - " + ideTipoFinalidadeUsoAgua.getNomTipoFinalidadeUsoAgua());
		}
		
		return dsc;
	}

	@Override
	public int compareTo(ParametroCalculo parametroCalculo) {
		return this.getDscAtoTipologiaFinalidade().compareTo(parametroCalculo.getDscAtoTipologiaFinalidade());
	}
	
	@Override
	public ParametroCalculo clone() throws CloneNotSupportedException {
		return (ParametroCalculo) super.clone();
	}

	public BigDecimal getVazaoMinima() {
		return vazaoMinima;
	}

	public void setVazaoMinima(BigDecimal vazaoMinima) {
		this.vazaoMinima = vazaoMinima;
	}

	public BigDecimal getVazaoMaxima() {
		return vazaoMaxima;
	}

	public void setVazaoMaxima(BigDecimal vazaoMaxima) {
		this.vazaoMaxima = vazaoMaxima;
	}

	public Bioma getIdeBioma() {
		return ideBioma;
	}

	public void setIdeBioma(Bioma ideBioma) {
		this.ideBioma = ideBioma;
	}
}