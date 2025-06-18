package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "pergunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pergunta.findAll", query = "SELECT p FROM Pergunta p"),
    @NamedQuery(name = "Pergunta.findByIdePergunta", query = "SELECT p FROM Pergunta p WHERE p.idePergunta = :idePergunta"),
    @NamedQuery(name = "Pergunta.findByIndAgrupador", query = "SELECT p FROM Pergunta p WHERE p.indAgrupador = :indAgrupador"),
    @NamedQuery(name = "Pergunta.findByIndLocalizacaoGeografica", query = "SELECT p FROM Pergunta p WHERE p.indLocalizacaoGeografica = :indLocalizacaoGeografica"),
    @NamedQuery(name = "Pergunta.findByIndLocGeoByIndAtivo", query = "SELECT p FROM Pergunta p WHERE p.indLocalizacaoGeografica <> :indLocalizacaoGeografica and p.indAtivo = :indAtivo order by p.idePergunta"),
    @NamedQuery(name = "Pergunta.findByIndAtivo", query = "SELECT p FROM Pergunta p WHERE p.indAtivo = :indAtivo"),
    @NamedQuery(name = "Pergunta.findByDtcCriacao", query = "SELECT p FROM Pergunta p WHERE p.dtcCriacao = :dtcCriacao")})
public class Pergunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pergunta_seq")    
    @SequenceGenerator(name="pergunta_seq", sequenceName="pergunta_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pergunta")
    private Integer idePergunta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_agrupador")
    private boolean indAgrupador;
    @Basic(optional = false)
    @Column(name = "cod_pergunta")
    private String codPergunta;
    @NotNull
    @Column(name = "dsc_pergunta")
    private String dscPergunta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_classificacao_secao_pergunta")
    private int indLocalizacaoGeografica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_ativo")
    private boolean indAtivo;
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @JoinColumn(name = "ide_pergunta_pai", referencedColumnName = "ide_pergunta")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pergunta idePerguntaPai;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePergunta")
    private Collection<PerguntaRequerimento> perguntaRequerimentoCollection;
    
    @Transient
    private List<RequerimentoImovel> listRequerimentoImovel;
    
    @Transient
    private Boolean resposta;
    
    @Transient
    private List<LocalizacaoGeografica> listLocalizacaoGeograficaDaPergunta;
    
    @Transient
    private List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea;

    public Pergunta() {
    }

    public Pergunta(Integer idePergunta) {
        this.idePergunta = idePergunta;
    }

    public Pergunta(Integer idePergunta, boolean indAgrupador, int indLocalizacaoGeografica, boolean indAtivo) {
        this.idePergunta = idePergunta;
        this.indAgrupador = indAgrupador;
        this.indLocalizacaoGeografica = indLocalizacaoGeografica;
        this.indAtivo = indAtivo;
    }

    public Integer getIdePergunta() {
        return idePergunta;
    }

    public void setIdePergunta(Integer idePergunta) {
        this.idePergunta = idePergunta;
    }

    public boolean getIndAgrupador() {
        return indAgrupador;
    }

    public void setIndAgrupador(boolean indAgrupador) {
        this.indAgrupador = indAgrupador;
    }

    public String getCodPergunta() {
		return codPergunta;
	}
    
    public String getNumeracaoPergunta() {
		String numeracao = null;
		try {
			numeracao = this.dscPergunta.substring(0, this.dscPergunta.indexOf(" "));
		} catch (Exception e) {
			numeracao = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return numeracao;
	}

	public void setCodPergunta(String codPergunta) {
		this.codPergunta = codPergunta;
	}

	public int getIndLocalizacaoGeografica() {
        return indLocalizacaoGeografica;
    }

    public void setIndLocalizacaoGeografica(int indLocalizacaoGeografica) {
        this.indLocalizacaoGeografica = indLocalizacaoGeografica;
    }

    public boolean getIndAtivo() {
        return indAtivo;
    }

    public void setIndAtivo(boolean indAtivo) {
        this.indAtivo = indAtivo;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    @XmlTransient
    public Collection<PerguntaRequerimento> getPerguntaRequerimentoCollection() {
        return perguntaRequerimentoCollection;
    }

    public void setPerguntaRequerimentoCollection(Collection<PerguntaRequerimento> perguntaRequerimentoCollection) {
        this.perguntaRequerimentoCollection = perguntaRequerimentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePergunta != null ? idePergunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pergunta)) {
            return false;
        }
        Pergunta other = (Pergunta) object;
        if ((this.idePergunta == null && other.idePergunta != null) || (this.idePergunta != null && !this.idePergunta.equals(other.idePergunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Pergunta[ idePergunta=" + idePergunta + " ]";
    }

	public List<RequerimentoImovel> getListRequerimentoImovel() {
		return listRequerimentoImovel;
	}

	public void setListRequerimentoImovel(List<RequerimentoImovel> listRequerimentoImovel) {
		this.listRequerimentoImovel = listRequerimentoImovel;
	}
	
	public void definePerguntaDosRequerimentosDeImovel(){
		for (RequerimentoImovel reqImovel : listRequerimentoImovel) {
			reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
			reqImovel.getPerguntaRequerimento().setIdePergunta(this);
		}
	}
	
	public void defineTodosRequerimentosDeImovelComoDeReqUnico(){
		for (RequerimentoImovel reqImovel : listRequerimentoImovel) {
			reqImovel.setRequerimentoUnico(true);
		}
	}

	public String getDscPergunta() {
		return dscPergunta;
	}

	public void setDscPergunta(String dscPergunta) {
		this.dscPergunta = dscPergunta;
	}

	public Pergunta getIdePerguntaPai() {
		return idePerguntaPai;
	}

	public void setIdePerguntaPai(Pergunta idePerguntaPai) {
		this.idePerguntaPai = idePerguntaPai;
	}
	
	/**
	 * É preciso ter a propriedade listRequerimentoImovel carregada 
	 */
	public void organizaLocalizacoesDoReqImovel() {
		if(!Util.isNullOuVazio(listRequerimentoImovel)){
			if(Util.isNullOuVazio(listLocalizacaoGeograficaDaPergunta))
				listLocalizacaoGeograficaDaPergunta = new ArrayList<LocalizacaoGeografica>();
			for (RequerimentoImovel reqImovel : listRequerimentoImovel) {
				if(!listLocalizacaoGeograficaDaPergunta.contains(reqImovel.getIdeLocalizacaoGeografica())){
					reqImovel.getIdeLocalizacaoGeografica().setVlrArea(reqImovel.getVlrArea());
					listLocalizacaoGeograficaDaPergunta.add(reqImovel.getIdeLocalizacaoGeografica());
				}
			}
		}
	}

	public RequerimentoImovel getReqImovelDaLocalizacaoGeografica(LocalizacaoGeografica l) throws CloneNotSupportedException {
		if(!Util.isNullOuVazio(listRequerimentoImovel)){
			for (RequerimentoImovel rImo : this.listRequerimentoImovel) {
				if(!Util.isNullOuVazio(rImo.getIdeLocalizacaoGeografica()) && !Util.isNullOuVazio(l) && rImo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().intValue() == l.getIdeLocalizacaoGeografica().intValue())
					return (RequerimentoImovel) rImo.clone();
			}
		}
		return null;
	}
	
	public PerguntaRequerimento getPerguntaRequerimento(LocalizacaoGeografica l) {
		if(!Util.isNullOuVazio(perguntaRequerimentoCollection)){
			for (PerguntaRequerimento pReq : this.perguntaRequerimentoCollection) {
				if(!Util.isNullOuVazio(pReq.getIdeLocalizacaoGeografica()) 
						&& pReq.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(l.getIdeLocalizacaoGeografica()));
					
				return pReq;
			}
		}
		
		return null;
	}
	
	public void removeLocalizacaoByObjetivo(ObjetivoLimpezaArea objetivoLimpezaArea, LocalizacaoGeografica localizacaoGeograficaASerExcluida) {
		if(!Util.isNullOuVazio(listObjReqLimpArea)){
			try {
				for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : Util.clonaListaObjReqLimpArea(listObjReqLimpArea)) {
					if(objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea().equals(objetivoLimpezaArea) && 
							localizacaoGeograficaASerExcluida.equals(objetivoRequerimentoLimpezaArea.getIdeLocalizacaoGeografica())){
						listObjReqLimpArea.remove(objetivoRequerimentoLimpezaArea);
					}
				}
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public LocalizacaoGeografica getLocalizacaoByObjetivo(ObjetivoLimpezaArea objetivoLimpezaArea,RequerimentoImovel requerimentoImovel) {
		if(!Util.isNullOuVazio(listObjReqLimpArea)){
			for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : listObjReqLimpArea) {
				if(objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea().equals(objetivoLimpezaArea)
						&& objetivoRequerimentoLimpezaArea.getIdeRequerimentoImovel().equals(requerimentoImovel)){
					return objetivoRequerimentoLimpezaArea.getIdeLocalizacaoGeografica();
				}
			}
		}
		return null;
	}

	
	public Boolean getLocalizacaoGeograficaDaPerguntaIsNotNull() {
		if(Util.isNullOuVazio(listLocalizacaoGeograficaDaPergunta))
			return false;
		else
			return true;
	}

	public Boolean getResposta() {
		return Util.isNull(resposta)? resposta = false: resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
		
		if(!Util.isNullOuVazio(perguntaRequerimentoCollection)) {
			for (PerguntaRequerimento pergReq : perguntaRequerimentoCollection) {
				pergReq.setIndResposta(resposta);
			}
		}
	}

	public List<ObjetivoRequerimentoLimpezaArea> getListObjReqLimpArea() {
		return Util.isNull(listObjReqLimpArea) ? listObjReqLimpArea= new ArrayList<ObjetivoRequerimentoLimpezaArea>(): listObjReqLimpArea;
	}
	
	public Collection<ObjetivoRequerimentoLimpezaArea> getListObjReqLimpAreaLikeCollection() {
		return listObjReqLimpArea;
	}

	public Boolean getListObjReqLimpAreaIsNotNull() {
		return !Util.isNullOuVazio(listObjReqLimpArea);
	}
	
	public void setListObjReqLimpArea(
			List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea) {
		this.listObjReqLimpArea = listObjReqLimpArea;
	}

	public List<LocalizacaoGeografica> getListLocalizacaoGeograficaDaPergunta() {
		return listLocalizacaoGeograficaDaPergunta;
	}

	public void setListLocalizacaoGeograficaDaPergunta(
			List<LocalizacaoGeografica> listLocalizacaoGeograficaDaPergunta) {
		this.listLocalizacaoGeograficaDaPergunta = listLocalizacaoGeograficaDaPergunta;
	}
}
