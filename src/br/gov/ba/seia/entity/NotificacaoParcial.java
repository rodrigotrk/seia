package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "notificacao_parcial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacaoParcial.findAll", query = "SELECT n FROM NotificacaoParcial n"),
    @NamedQuery(name = "NotificacaoParcial.findByDscTextoParcial", query = "SELECT n FROM NotificacaoParcial n WHERE n.dscTextoParcial = :dscTextoParcial"),
    @NamedQuery(name = "NotificacaoParcial.findByIndEmissao", query = "SELECT n FROM NotificacaoParcial n WHERE n.indEmissao = :indEmissao"),
    @NamedQuery(name = "NotificacaoParcial.findByDtcCriacao", query = "SELECT n FROM NotificacaoParcial n WHERE n.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "NotificacaoParcial.findByPautaProcesso", query = "SELECT n FROM NotificacaoParcial n WHERE n.idePauta = :idePauta AND n.ideProcesso = :ideProcesso AND n.indEmissao = :indEmissao"),
	@NamedQuery(name = "NotificacaoParcial.findByPautaProcessoTecnico", query = "SELECT n FROM NotificacaoParcial n WHERE n.ideProcesso = :ideProcesso AND n.indEmissao = :indEmissao"),
	@NamedQuery(name = "NotificacaoParcial.findByIdeNotificacaoParcial", query = "SELECT n FROM NotificacaoParcial n WHERE n.ideNotificacaoParcial = :ideNotificacaoParcial")})
public class NotificacaoParcial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="NOTIFICACAO_PARCIAL_IDE_NOTIFICACAO_PARCIAL_seq", sequenceName="NOTIFICACAO_PARCIAL_IDE_NOTIFICACAO_PARCIAL_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICACAO_PARCIAL_IDE_NOTIFICACAO_PARCIAL_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_notificacao_parcial", nullable = false)
    private Integer ideNotificacaoParcial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dsc_texto_parcial", nullable = false, length = 250)
    private String dscTextoParcial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_emissao", nullable = false)
    private boolean indEmissao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Processo ideProcesso;
    @JoinColumn(name = "ide_pauta", referencedColumnName = "ide_pauta", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pauta idePauta;
    @Transient
    private String indEmissaoString;
    @Column(name="tipo")
    private int tipo;
    @JoinColumn(name="ide_notificacao",referencedColumnName = "ide_notificacao", nullable = true)
    @OneToOne(optional = true,fetch = FetchType.LAZY)
    private Notificacao ideNotificacao;

    public NotificacaoParcial() {
    }

    public NotificacaoParcial(Integer ideNotificacaoParcial) {
        this.ideNotificacaoParcial = ideNotificacaoParcial;
    }

    public NotificacaoParcial(Integer ideNotificacaoParcial, String dscTextoParcial, boolean indEmissao, Date dtcCriacao) {
        this.ideNotificacaoParcial = ideNotificacaoParcial;
        this.dscTextoParcial = dscTextoParcial;
        this.indEmissao = indEmissao;
        this.dtcCriacao = dtcCriacao;
    }

    public String getDscTextoParcial() {
        return dscTextoParcial;
    }

    public void setDscTextoParcial(String dscTextoParcial) {
        this.dscTextoParcial = dscTextoParcial;
    }

    public boolean getIndEmissao() {
        return indEmissao;
    }

    public void setIndEmissao(boolean indEmissao) {
        this.indEmissao = indEmissao;
    }

    public Integer getIdeNotificacaoParcial() {
        return ideNotificacaoParcial;
    }

    public void setIdeNotificacaoParcial(Integer ideNotificacaoParcial) {
        this.ideNotificacaoParcial = ideNotificacaoParcial;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNotificacaoParcial != null ? ideNotificacaoParcial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NotificacaoParcial)) {
            return false;
        }
        NotificacaoParcial other = (NotificacaoParcial) object;
        if ((this.ideNotificacaoParcial == null && other.ideNotificacaoParcial != null) || (this.ideNotificacaoParcial != null && !this.ideNotificacaoParcial.equals(other.ideNotificacaoParcial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.NotificacaoParcial[ ideNotificacaoParcial=" + ideNotificacaoParcial + " ]";
    }

	public String getIndEmissaoString() {
		if (indEmissao) {
			indEmissaoString = "Sim";
		} else {
			indEmissaoString = "Não";
		}
		return indEmissaoString;
	}

	public void setIndEmissaoString(String indEmissaoString) {
		this.indEmissaoString = indEmissaoString;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Notificacao getIdeNotificacao() {
		return ideNotificacao;
	}

	public void setIdeNotificacao(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}
    
}
