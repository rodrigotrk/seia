
package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.Days;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "notificacao")
@NamedQuery(name = "Notificacao.findByIdeNotificacao", query = "SELECT n FROM Notificacao n WHERE n.ideNotificacao = :ideNotificacao")
@XmlRootElement
public class Notificacao extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @SequenceGenerator(name="NOTIFICACAO_IDE_NOTIFICACAO_seq", sequenceName="NOTIFICACAO_IDE_NOTIFICACAO_seq",allocationSize=1 )
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICACAO_IDE_NOTIFICACAO_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_notificacao", nullable = false)
    private Integer ideNotificacao;
    
	@Size(max = 2147483647)
    @Column(name = "dsc_notificacao", length = 2147483647)
    private String dscNotificacao;
    
	@Column(name = "dtc_validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidade;
    
	@Size(max = 100)
    @Column(name = "num_notificacao", length = 100)
    private String numNotificacao;
    
	@Column(name = "ind_aprovado")
    private Boolean indAprovado;
    
	@Column(name = "dtc_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcEnvio;
    
	@Column(name = "ind_retorno")
    private Boolean indRetorno;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
	
	@Column(name = "dtc_resposta")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcResposta;
    
	@Column(name = "dtc_reprovacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcReprovacao;
    
	@Column(name = "ind_cancelado")
    private Boolean indCancelado;
    
	@Column(name = "ind_rejeitado")
    private Boolean indRejeitado;
    
	@Column(name = "ind_enviado_aprovacao")
    private Boolean indEnviadoAprovacao;
    
	@Size(max = 200)
    @Column(name = "dsc_outro_justificativa")
    private String dscOutroJustificativa;
    
	@Column(name = "qtd_dias_validade")
    private Integer qtdDiasValidade;
	
	@Column(name="tipo")
	private int tipo;
    
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Processo ideProcesso;
    
	@JoinColumn(name = "ide_pauta_criacao", referencedColumnName = "ide_pauta", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pauta idePautaCriacao;
    
	@JoinColumn(name = "ide_pauta_aprovacao", referencedColumnName = "ide_pauta")
    @ManyToOne(fetch = FetchType.LAZY)
    private Pauta idePautaAprovacao;
    
    @JoinColumn(name = "ide_legislacao", referencedColumnName = "ide_legislacao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Legislacao ideLegislacao;
    
    @JoinTable(name = "notificacao_justificativa_rejeicao", joinColumns = {
            @JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao")}, inverseJoinColumns = {
            @JoinColumn(name = "ide_justificativa_rejeicao", referencedColumnName = "ide_justificativa_rejeicao")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<JustificativaRejeicao> justificativaRejeicaoCollection;
    
    @JoinTable(name = "notificacao_motivo_edicao_notificacao", joinColumns = {
            @JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "ide_motivo_edicao_notificacao", referencedColumnName = "ide_motivo_edicao_notificacao", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<MotivoEdicaoNotificacao> motivoEdicaoNotificacaoCollection;
    
    @OneToMany(mappedBy="ideNotificacao")
    private Collection<ArquivoProcesso> arquivoProcessoCollection;
    
    @OneToMany(mappedBy="ideNotificacao")
    private Collection<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollection;
  
    @OneToMany(mappedBy="ideNotificacao")
    private Collection<NotificacaoParcial> notificacaoParcialCollection;
    
	@OneToOne(mappedBy="ideNotificacao", optional=true)
    private ReenquadramentoProcesso ideReenquadramentoProcesso;
    
    @Transient
    private MotivoNotificacao motivoNotificacaoSelecionado;
    
    @Transient
    private List<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollectionSemShape;
    
    @Transient
    private Collection<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollectionComShape;

    public Notificacao() {
    }

	public Collection<NotificacaoMotivoNotificacao> getNotificacaoMotivoNotificacaoCollectionSemShape() {
		notificacaoMotivoNotificacaoCollectionSemShape = new ArrayList<NotificacaoMotivoNotificacao>();
		
		for (NotificacaoMotivoNotificacao nmn : notificacaoMotivoNotificacaoCollection) {
			if(!Util.isNullOuVazio(nmn.getIdeMotivoNotificacao()) && !nmn.getIdeMotivoNotificacao().getIndEnvioShape()) {
				notificacaoMotivoNotificacaoCollectionSemShape.add(nmn);
			}
		}
		
		return notificacaoMotivoNotificacaoCollectionSemShape;
	}

	public Collection<NotificacaoMotivoNotificacao> getNotificacaoMotivoNotificacaoCollectionComShape() {
		return notificacaoMotivoNotificacaoCollectionComShape;
	}
	
	public void setNotificacaoMotivoNotificacaoCollectionComShape(List<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollection) {
		this.notificacaoMotivoNotificacaoCollectionComShape = notificacaoMotivoNotificacaoCollection;
	}

	
    public Notificacao(Integer ideNotificacao) {
        this.ideNotificacao = ideNotificacao;
    }

    public Notificacao(Integer ideNotificacao, Date dtcCriacao) {
        this.ideNotificacao = ideNotificacao;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeNotificacao() {
        return ideNotificacao;
    }

    public void setIdeNotificacao(Integer ideNotificacao) {
        this.ideNotificacao = ideNotificacao;
    }

    @JSON(include = false)
    public String getDscNotificacao() {
        return dscNotificacao;
    }

    public void setDscNotificacao(String dscNotificacao) {
        this.dscNotificacao = dscNotificacao;
    }

    @JSON(include = false)
    public Date getDtcValidade() {
        return dtcValidade;
    }
    @JSON(include = false)
    public void setDtcValidade(Date dtcValidade) {
        this.dtcValidade = dtcValidade;
    }

    @JSON(include = false)
    public String getNumNotificacao() {
        return numNotificacao;
    }

    public void setNumNotificacao(String numNotificacao) {
        this.numNotificacao = numNotificacao;
    }
    @JSON(include = false)
    public Boolean getIndAprovado() {
        return indAprovado;
    }
    @JSON(include = false)
    public String getIndAprovadoFormatado() {
	    if(indAprovado == null){
	    	return "";
	    }
    	else if(indAprovado == false){
        	return "Não";
        }
        else if(indAprovado == true){
        	return "Sim";
        }
	    
    	return "";
    }

    public void setIndAprovado(Boolean indAprovado) {
        this.indAprovado = indAprovado;
    }
    
    public Date getDtcEnvio() {
        return dtcEnvio;
    }

    public void setDtcEnvio(Date dtcEnvio) {
        this.dtcEnvio = dtcEnvio;
    }
    @JSON(include = false)
    public Boolean getIndRetorno() {
        return indRetorno;
    }
    @JSON(include = false)
    public String getIndRetornoFormatado() {
    	if(indRetorno == null){
	    	return "";
	    }
    	else if(indRetorno == false){
        	return "Não";
        }
        else if(indRetorno == true){
        	return "Sim";
        }
    	
    	return "";
    }

    public void setIndRetorno(Boolean indRetorno) {
        this.indRetorno = indRetorno;
    }
    
    @JSON(include = false)
    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }
    @JSON(include = false)
    public Date getDtcReprovacao() {
        return dtcReprovacao;
    }

    public void setDtcReprovacao(Date dtcReprovacao) {
        this.dtcReprovacao = dtcReprovacao;
    }
    
   public Processo getIdeProcesso() {
        return ideProcesso;
    }

    public void setIdeProcesso(Processo ideProcesso) {
        this.ideProcesso = ideProcesso;
    }
    @JSON(include = false)
    public Pauta getIdePautaCriacao() {
        return idePautaCriacao;
    }

    public void setIdePautaCriacao(Pauta idePautaCriacao) {
        this.idePautaCriacao = idePautaCriacao;
    }

    @JSON(include = false)
    public Pauta getIdePautaAprovacao() {
        return idePautaAprovacao;
    }

    public void setIdePautaAprovacao(Pauta idePautaAprovacao) {
        this.idePautaAprovacao = idePautaAprovacao;
    }
    
    @JSON(include = false)
    public Legislacao getIdeLegislacao() {
        return ideLegislacao;
    }

    public void setIdeLegislacao(Legislacao ideLegislacao) {
        this.ideLegislacao = ideLegislacao;
    }
    @JSON(include = false)
    public Boolean getIndCancelado() {
		return indCancelado;
	}

	public void setIndCancelado(Boolean indCancelado) {
		this.indCancelado = indCancelado;
	}

	@JSON(include = false)
	public Boolean getIndRejeitado() {
		return indRejeitado;
	}

	public void setIndRejeitado(Boolean indRejeitado) {
		this.indRejeitado = indRejeitado;
	}

	@JSON(include = false)
	public String getDscOutroJustificativa() {
		return dscOutroJustificativa;
	}

	public void setDscOutroJustificativa(String dscOutroJustificativa) {
		this.dscOutroJustificativa = dscOutroJustificativa;
	}
	@JSON(include = false)
	public Integer getQtdDiasValidade() {
		return qtdDiasValidade;
	}

	public void setQtdDiasValidade(Integer qtdDiasValidade) {
		this.qtdDiasValidade = qtdDiasValidade;
	}
	
	@JSON(include = false)
	public Collection<JustificativaRejeicao> getJustificativaRejeicaoCollection() {
		return justificativaRejeicaoCollection;
	}

	public void setJustificativaRejeicaoCollection(
			Collection<JustificativaRejeicao> justificativaRejeicaoCollection) {
		this.justificativaRejeicaoCollection = justificativaRejeicaoCollection;
	}
	
	@JSON(include = false)
	public String retornarMotivosEdicaoNotificacao(){
		String texto="";
		for(MotivoEdicaoNotificacao m : motivoEdicaoNotificacaoCollection){
			if(texto.equals("")){
				texto+=m.getNomMotivoEdicaoNotificacao();
			}
			else{
				texto+=(", "+m.getNomMotivoEdicaoNotificacao());
			}
		}
		return texto;
	}
	
	public Integer getValidadeEmDias() {
		if (Util.isNullOuVazio(dtcValidade)){
			return 0;
		}
		 Date dataAtual = new Date();
		 
		 Date dataValidade = new Date(dtcValidade.getTime());
		 int days = Days.daysBetween(new DateTime(dataAtual).toDateMidnight(),new DateTime(dataValidade).toDateMidnight()).getDays();
		 return days < 0 ? 0 : days;
	}
	
	/*@JSON(include = false)
	public String getMotivosFormatado(){
		if(Util.isNullOuVazio(this.motivoNotificacaoCollection)){
			return "";
		}
		
		StringBuilder buffer = new StringBuilder();		
		for(MotivoNotificacao motivo: this.motivoNotificacaoCollection){
 			if(buffer.length()>0){
               buffer.append(",");			
			}
			buffer.append(motivo.getNomMotivoNotificacao()); 
		}
		
		return buffer.toString();
		
	}*/

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/*@JSON(include = false)
	public Boolean isExisteShape() {
		int length = MotivoNotificacaoEnum.SHAPE.getIds().length;
		
		for (int i = 0; i < length; i++) {
			if(motivoNotificacaoCollection.contains(new MotivoNotificacao(MotivoNotificacaoEnum.SHAPE.getIds()[i])))
				return true;
		}
		
		return false;
	}*/

	@JSON(include = false)
	public Boolean getIndEnviadoAprovacao() {
		return indEnviadoAprovacao;
	}

	public void setIndEnviadoAprovacao(Boolean indEnviadoAprovacao) {
		this.indEnviadoAprovacao = indEnviadoAprovacao;
	}

	@JSON(include = false)
	public boolean isNotificacaoPrazo() {
		return TipoNotificacaoEnum.NOTIFICACAO_PRAZO.getTipo() == tipo;
	}
	
	@JSON(include = false)
	public boolean isNotificacaoComunicacao() {
		return TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.getTipo() == tipo;
	}
	
	@JSON(include = false)
	public boolean isNotificacaoHomologacao() {
		return TipoNotificacaoEnum.NOTIFICACAO_HOMOLOGACAO.getTipo() == tipo;
	}

	@JSON(include = false)
	public Collection<MotivoEdicaoNotificacao> getMotivoEdicaoNotificacaoCollection() {
		return motivoEdicaoNotificacaoCollection;
	}

	public void setMotivoEdicaoNotificacaoCollection(
			Collection<MotivoEdicaoNotificacao> motivoEdicaoNotificacaoCollection) {
		this.motivoEdicaoNotificacaoCollection = motivoEdicaoNotificacaoCollection;
	}
	
	public String getNumNotificacaoOuStatus(){
		if(!Util.isNullOuVazio(numNotificacao)){
			return this.numNotificacao;
		}if(indCancelado){
			return "NOTIFICAÇÃO CANCELADA";
		}if(indRejeitado){
			return "NOTIFICAÇÃO REJEITADA";
		}if(!indAprovado){
			return "AGUARDANDO APROVAÇÃO ";
		}
		return "-";
	}

	public MotivoNotificacao getMotivoNotificacaoSelecionado() {
		return motivoNotificacaoSelecionado;
	}

	public void setMotivoNotificacaoSelecionado(
			MotivoNotificacao motivoNotificacaoSelecionado) {
		this.motivoNotificacaoSelecionado = motivoNotificacaoSelecionado;
	}

	public Collection<ArquivoProcesso> getArquivoProcessoCollection() {
		return arquivoProcessoCollection;
	}
	
	public void setArquivoProcessoCollection(Collection<ArquivoProcesso> arquivoProcessoCollection) {
		this.arquivoProcessoCollection = arquivoProcessoCollection;
	}

	public Date getDtcResposta() {
		return dtcResposta;
	}

	public void setDtcResposta(Date dtcResposta) {
		this.dtcResposta = dtcResposta;
	}

	public Collection<NotificacaoParcial> getNotificacaoParcialCollection() {
		return notificacaoParcialCollection;
	}

	public void setNotificacaoParcialCollection(
			Collection<NotificacaoParcial> notificacaoParcialCollection) {
		this.notificacaoParcialCollection = notificacaoParcialCollection;
	}

	public Collection<NotificacaoMotivoNotificacao> getNotificacaoMotivoNotificacaoCollection() {
		return notificacaoMotivoNotificacaoCollection;
	}

	public void setNotificacaoMotivoNotificacaoCollection(
			Collection<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollection) {
		this.notificacaoMotivoNotificacaoCollection = notificacaoMotivoNotificacaoCollection;
	}

	public ReenquadramentoProcesso getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}
}