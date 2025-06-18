package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "cronograma_recuperacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CronogramaRecuperacao.findAll", query = "SELECT c FROM CronogramaRecuperacao c"),
    @NamedQuery(name = "CronogramaRecuperacao.findByIdeCronogramaRecuperacao", query = "SELECT c FROM CronogramaRecuperacao c WHERE c.ideCronogramaRecuperacao = :ideCronogramaRecuperacao"),
    @NamedQuery(name = "CronogramaRecuperacao.findByIdeDocumentoObrigatorio", query = "SELECT c FROM CronogramaRecuperacao c WHERE c.ideDocumentoObrigatorio.ideDocumentoImovelRural = :ideDocumentoObrigatorio"),
    @NamedQuery(name = "CronogramaRecuperacao.findByStsAceite", query = "SELECT c FROM CronogramaRecuperacao c WHERE c.stsAceite = :stsAceite"),
    @NamedQuery(name = "CronogramaRecuperacao.findByDtcCriacao", query = "SELECT c FROM CronogramaRecuperacao c WHERE c.dtcCriacao = :dtcCriacao")})
public class CronogramaRecuperacao extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRONOGRAMA_RECUPERACAO_IDE_CRONOGRAMA_RECUPERACAO_SEQ") 
    @SequenceGenerator(name="CRONOGRAMA_RECUPERACAO_IDE_CRONOGRAMA_RECUPERACAO_SEQ", sequenceName="CRONOGRAMA_RECUPERACAO_IDE_CRONOGRAMA_RECUPERACAO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cronograma_recuperacao", nullable = false)
    private Integer ideCronogramaRecuperacao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "sts_aceite")
    private boolean stsAceite;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao = new Date();
    
    @JoinColumn(name = "ide_app", referencedColumnName = "ide_app")
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private App ideApp;
    
    @JoinColumn(name = "ide_reserva_legal", referencedColumnName = "ide_reserva_legal")
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ReservaLegal ideReservaLegal;
    
    @JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_imovel_rural")
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private DocumentoImovelRural ideDocumentoObrigatorio;
    
    @OneToMany(mappedBy = "ideCronogramaRecuperacao", cascade = CascadeType.ALL)
    private Collection<CronogramaEtapa> cronogramaEtapaCollection;
    
    @Basic(optional = false)
	@Column(name = "ind_prad_importada")
    private Boolean indPradImportada;
    
    @JoinColumn(name = "ide_outros_passivos_ambientais", referencedColumnName = "ide_outros_passivos_ambientais")
    @ManyToOne(optional = true)
    private OutrosPassivosAmbientais ideOutrosPassivosAmbientais;
    
    @JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private LocalizacaoGeografica localizacaoGeografica;

    public CronogramaRecuperacao() {
    }

    public CronogramaRecuperacao(Integer ideCronogramaRecuperacao) {
        this.ideCronogramaRecuperacao = ideCronogramaRecuperacao;
    }

    public CronogramaRecuperacao(Integer ideCronogramaRecuperacao, boolean stsAceite, Date dtcCriacao) {
        this.ideCronogramaRecuperacao = ideCronogramaRecuperacao;
        this.stsAceite = stsAceite;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeCronogramaRecuperacao() {
        return ideCronogramaRecuperacao;
    }

    public void setIdeCronogramaRecuperacao(Integer ideCronogramaRecuperacao) {
        this.ideCronogramaRecuperacao = ideCronogramaRecuperacao;
    }

    public boolean getStsAceite() {
        return stsAceite;
    }

    public void setStsAceite(boolean stsAceite) {
        this.stsAceite = stsAceite;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public ReservaLegal getIdeReservaLegal() {
        return ideReservaLegal;
    }

    public void setIdeReservaLegal(ReservaLegal ideReservaLegal) {
        this.ideReservaLegal = ideReservaLegal;
    }

    public App getIdeApp() {
        return ideApp;
    }

    public void setIdeApp(App ideApp) {
        this.ideApp = ideApp;
    }

    @XmlTransient
    public Collection<CronogramaEtapa> getCronogramaEtapaCollection() {
        return cronogramaEtapaCollection;
    }

    public void setCronogramaEtapaCollection(Collection<CronogramaEtapa> cronogramaEtapaCollection) {
        this.cronogramaEtapaCollection = cronogramaEtapaCollection;
    }

    public DocumentoImovelRural getIdeDocumentoObrigatorio() {
    	return ideDocumentoObrigatorio;
    }

    public void setIdeDocumentoObrigatorio(DocumentoImovelRural ideDocumentoObrigatorio) {
    	this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
    }

    public Boolean getIndPradImportada() {
    	return indPradImportada;
    }

    public OutrosPassivosAmbientais getIdeOutrosPassivosAmbientais() {
    	return ideOutrosPassivosAmbientais;
    }

    public void setIdeOutrosPassivosAmbientais(OutrosPassivosAmbientais ideOutrosPassivosAmbientais) {
    	this.ideOutrosPassivosAmbientais = ideOutrosPassivosAmbientais;
    }

    public void setIndPradImportada(Boolean indPradImportada) {
    	this.indPradImportada = indPradImportada;
    }
	
    public LocalizacaoGeografica getLocalizacaoGeografica() {
    	return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
    	this.localizacaoGeografica = localizacaoGeografica;
    }
}
