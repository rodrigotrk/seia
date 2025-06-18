package br.gov.ba.seia.entity;

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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.TramitacaoInterface;
/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tramitacao_requerimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TramitacaoRequerimento.findAll", query = "SELECT t FROM TramitacaoRequerimento t"),
    @NamedQuery(name = "TramitacaoRequerimento.findByRequerimento", query = "SELECT t FROM TramitacaoRequerimento t inner join t.ideRequerimento r where r.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "TramitacaoRequerimento.findByIdeTramitacaoRequerimento", query = "SELECT t FROM TramitacaoRequerimento t WHERE t.ideTramitacaoRequerimento = :ideTramitacaoRequerimento"),
    @NamedQuery(name = "TramitacaoRequerimento.findByDtcMovimentacao", query = "SELECT t FROM TramitacaoRequerimento t WHERE t.dtcMovimentacao = :dtcMovimentacao")})
@NamedNativeQuery(name = "TramitacaoRequerimento.ListarByIdesRequerimentos",query="select tr.ide_requerimento,s.nom_status_requerimento,s.ide_status_requerimento from tramitacao_requerimento  tr left join status_requerimento s on s.ide_status_requerimento = tr.ide_status_requerimento where tr.dtc_movimentacao in ( select max(dtc_movimentacao) from tramitacao_requerimento group by ide_requerimento ) and tr.ide_requerimento in (:ides)",resultClass=TramitacaoRequerimento.class)
public class TramitacaoRequerimento extends AbstractEntity implements TramitacaoInterface {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq", sequenceName = "TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAMITACAO_REQUERIMENTO_IDE_TRAMITACAO_REQUERIMENTO_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tramitacao_requerimento", nullable = false)
    private Integer ideTramitacaoRequerimento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_movimentacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcMovimentacao;
    
    @JoinColumn(name = "ide_status_requerimento", referencedColumnName = "ide_status_requerimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private StatusRequerimento ideStatusRequerimento;
    
    @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Requerimento ideRequerimento;
    
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idePessoa;

    public TramitacaoRequerimento() {}

    public TramitacaoRequerimento(Integer ideTramitacaoRequerimento) {
        this.ideTramitacaoRequerimento = ideTramitacaoRequerimento;
    }

    public TramitacaoRequerimento(Integer ideTramitacaoRequerimento, Date dtcMovimentacao) {
        this.ideTramitacaoRequerimento = ideTramitacaoRequerimento;
        this.dtcMovimentacao = dtcMovimentacao;
    }

    public Integer getIdeTramitacaoRequerimento() {
        return ideTramitacaoRequerimento;
    }

    public void setIdeTramitacaoRequerimento(Integer ideTramitacaoRequerimento) {
        this.ideTramitacaoRequerimento = ideTramitacaoRequerimento;
    }

    public Date getDtcMovimentacao() {
        return dtcMovimentacao;
    }

    public void setDtcMovimentacao(Date dtcMovimentacao) {
        this.dtcMovimentacao = dtcMovimentacao;
    }

    public StatusRequerimento getIdeStatusRequerimento() {
        return ideStatusRequerimento;
    }

    public void setIdeStatusRequerimento(StatusRequerimento ideStatusRequerimento) {
        this.ideStatusRequerimento = ideStatusRequerimento;
    }

    public Requerimento getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(Requerimento ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public Pessoa getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Pessoa idePessoa) {
        this.idePessoa = idePessoa;
    }
}
