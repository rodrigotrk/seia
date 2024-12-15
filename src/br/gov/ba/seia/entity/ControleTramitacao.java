package br.gov.ba.seia.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "controle_tramitacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_controle_tramitacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControleTramitacao.findAll", query = "SELECT c FROM ControleTramitacao c"),
    @NamedQuery(name = "ControleTramitacao.findByIdeControleTramitacao", query = "SELECT c FROM ControleTramitacao c WHERE c.ideControleTramitacao = :ideControleTramitacao"),
    @NamedQuery(name = "ControleTramitacao.findByDtcTramitacao", query = "SELECT c FROM ControleTramitacao c WHERE c.dtcTramitacao = :dtcTramitacao"),
    @NamedQuery(name = "ControleTramitacao.findByIndFimDaFila", query = "SELECT c FROM ControleTramitacao c WHERE c.indFimDaFila = :indFimDaFila"),
    @NamedQuery(name = "ControleTramitacao.findByDscComentarioExterno", query = "SELECT c FROM ControleTramitacao c WHERE c.dscComentarioExterno = :dscComentarioExterno"),
    @NamedQuery(name = "ControleTramitacao.findByDscComentarioInterno", query = "SELECT c FROM ControleTramitacao c WHERE c.dscComentarioInterno = :dscComentarioInterno"),
    @NamedQuery(name = "ControleTramitacao.findByIndResponsavel", query = "SELECT c FROM ControleTramitacao c WHERE c.indResponsavel = :indResponsavel"),
    @NamedQuery(name = "ControleTramitacao.findByIdeProcessoIndFimDaFila", query = "SELECT c FROM ControleTramitacao c WHERE c.ideProcesso.ideProcesso = :ideProcesso AND c.indFimDaFila = :indFimDaFila"),
    @NamedQuery(name = "ControleTramitacao.findByIdeProcessoIdeStatusIndFimDaFila", query = "SELECT c FROM ControleTramitacao c WHERE c.ideProcesso.ideProcesso = :ideProcesso AND c.ideStatusFluxo.ideStatusFluxo in (:ideStatusFluxo) AND c.indFimDaFila = :indFimDaFila")})
public class ControleTramitacao extends AbstractEntity implements Cloneable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @SequenceGenerator(name="CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq", sequenceName="CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONTROLE_TRAMITACAO_IDE_CONTROLE_TRAMITACAO_seq")
    @Column(name = "ide_controle_tramitacao", nullable = false)
    private Integer ideControleTramitacao;
    
    @Column(name = "dtc_tramitacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) 
    private Date dtcTramitacao;
    
    @Column(name = "ind_fim_da_fila", nullable = false)
    private Boolean indFimDaFila;
    
    @Column(name = "dsc_comentario_externo", length = 500)
    private String dscComentarioExterno;
    
    @Column(name = "dsc_comentario_interno", length = 500)
    private String dscComentarioInterno;
    
    @Column(name = "ind_responsavel", nullable = false)
    private boolean indResponsavel;
    
	@JoinColumn(name = "ide_status_fluxo", referencedColumnName = "ide_status_fluxo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private StatusFluxo ideStatusFluxo;
    
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Processo ideProcesso;
    
	@JoinColumn(name = "ide_pauta", referencedColumnName = "ide_pauta", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Pauta idePauta;   
    
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Area ideArea;
    
	@JoinColumn(name="ide_pessoa_fisica",referencedColumnName = "ide_pessoa_fisica")
    @ManyToOne(fetch=FetchType.LAZY)
    private PessoaFisica idePessoaFisica;
    
	@Column(name = "ind_area_secundaria")
	private Boolean indAreaSecundaria;
    
	@Transient
    private boolean controleTramitacaoComPessoaFisica;

    public ControleTramitacao() {
    }
    public ControleTramitacao(Integer ideControleTramitacao) {
        this.ideControleTramitacao = ideControleTramitacao;
    }
    public ControleTramitacao(Processo ideProcesso) {
        this.ideProcesso = ideProcesso;
    }
    public ControleTramitacao(Pauta idePauta) {
        this.idePauta = idePauta;
    }
    public ControleTramitacao(Processo ideProcesso, Pauta idePauta, Boolean indFimDaFila) {
        this.ideProcesso = ideProcesso;
        this.idePauta = idePauta;
        this.indFimDaFila = indFimDaFila;
    }
    public ControleTramitacao(Processo ideProcesso, Boolean indFimDaFila) {
        this.ideProcesso = ideProcesso;
        this.indFimDaFila = indFimDaFila;
    }
    public ControleTramitacao(Processo ideProcesso, Boolean indFimDaFila, boolean controleTramitacaoComPessoaFisica) {
        this.ideProcesso = ideProcesso;
        this.indFimDaFila = indFimDaFila;
        this.controleTramitacaoComPessoaFisica = controleTramitacaoComPessoaFisica;
    }
    public ControleTramitacao(Area ideArea, Boolean indFimDaFila) {
        this.ideArea = ideArea;
        this.indFimDaFila = indFimDaFila;
    }
    public ControleTramitacao(Pauta idePauta, Boolean indFimDaFila) {
        this.idePauta = idePauta;
        this.indFimDaFila = indFimDaFila;
    }
    public ControleTramitacao(Processo ideProcesso, StatusFluxo ideStatusFluxo, Area ideArea, Date dtcTramitacao, Boolean indFimDaFila, Pauta idePauta, boolean indResponsavel) {
        this.ideProcesso = ideProcesso;
        this.ideStatusFluxo = ideStatusFluxo;
        this.ideArea = ideArea;
        this.dtcTramitacao = dtcTramitacao;
        this.indFimDaFila = indFimDaFila;
        this.idePauta = idePauta;
        this.indResponsavel = indResponsavel;
    }
    public ControleTramitacao(Processo ideProcesso, StatusFluxo ideStatusFluxo, Area ideArea, Date dtcTramitacao, Boolean indFimDaFila, Pauta idePauta, boolean indResponsavel,String dscComentarioInterno, PessoaFisica idePessoaFisica) {
        this.ideProcesso = ideProcesso;
        this.ideStatusFluxo = ideStatusFluxo;
        this.ideArea = ideArea;
        this.dtcTramitacao = dtcTramitacao;
        this.indFimDaFila = indFimDaFila;
        this.idePauta = idePauta;
        this.indResponsavel = indResponsavel;
        this.dscComentarioInterno = dscComentarioInterno;
        this.idePessoaFisica = idePessoaFisica;
    }
    public ControleTramitacao(Integer ideControleTramitacao, Date dtcTramitacao, Boolean indFimDaFila, boolean indResponsavel) {
        this.ideControleTramitacao = ideControleTramitacao;
        this.dtcTramitacao = dtcTramitacao;
        this.indFimDaFila = indFimDaFila;
        this.indResponsavel = indResponsavel;
    }

    public Integer getIdeControleTramitacao() {
        return ideControleTramitacao;
    }

    public void setIdeControleTramitacao(Integer ideControleTramitacao) {
        this.ideControleTramitacao = ideControleTramitacao;
    }

    public Date getDtcTramitacao() {
        return dtcTramitacao;
    }
    public String getDtcTramitacaoFormatado(){
    	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	return formatter.format(dtcTramitacao);
    }

    public void setDtcTramitacao(Date dtcTramitacao) {
        this.dtcTramitacao = dtcTramitacao;
    }

    public Boolean getIndFimDaFila() {
        return indFimDaFila;
    }

    public void setIndFimDaFila(Boolean indFimDaFila) {
        this.indFimDaFila = indFimDaFila;
    }

    public String getDscComentarioExterno() {
        return dscComentarioExterno;
    }

    public void setDscComentarioExterno(String dscComentarioExterno) {
        this.dscComentarioExterno = dscComentarioExterno;
    }

    public String getDscComentarioInterno() {
        return dscComentarioInterno;
    }

    public void setDscComentarioInterno(String dscComentarioInterno) {
        this.dscComentarioInterno = dscComentarioInterno;
    }

    public boolean getIndResponsavel() {
        return indResponsavel;
    }

    public void setIndResponsavel(boolean indResponsavel) {
        this.indResponsavel = indResponsavel;
    }

    public StatusFluxo getIdeStatusFluxo() {
        return ideStatusFluxo;
    }

    public void setIdeStatusFluxo(StatusFluxo ideStatusFluxo) {
        this.ideStatusFluxo = ideStatusFluxo;
    }

    public Processo getIdeProcesso() {
        return ideProcesso;
    }

    public void setIdeProcesso(Processo ideProcesso) {
        this.ideProcesso = ideProcesso;
    }

    public Pauta getIdePauta() {
        return idePauta;
    }

    public void setIdePauta(Pauta idePauta) {
        this.idePauta = idePauta;
    }


    public Area getIdeArea() {
        return ideArea;
    }

    public void setIdeArea(Area ideArea) {
        this.ideArea = ideArea;
    }

    public boolean isControleTramitacaoComPessoaFisica() {
		return controleTramitacaoComPessoaFisica;
	}

	public void setControleTramitacaoComPessoaFisica(boolean controleTramitacaoComPessoaFisica) {
		this.controleTramitacaoComPessoaFisica = controleTramitacaoComPessoaFisica;
	}


	
    public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}
	
	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	public Boolean getIndAreaSecundaria() {
		return indAreaSecundaria;
	}
	
	public void setIndAreaSecundaria(Boolean indAreaSecundaria) {
		this.indAreaSecundaria = indAreaSecundaria;
	}
	
	public ControleTramitacao clone() throws CloneNotSupportedException {
		return (ControleTramitacao) super.clone();
	}
}