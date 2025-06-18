package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MJunior
 */
@Entity
@Table(name = "vw_documento_identificacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwDocumentoIdentificacao.findAll", query = "SELECT v FROM VwDocumentoIdentificacao v"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdDto", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.idDto = :idDto"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdeDocumentoIdentificacao", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdeTipoIdentificacao", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.ideTipoIdentificacao = :ideTipoIdentificacao"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdePessoa", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.idePessoa = :idePessoa"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdeEstado", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.ideEstado = :ideEstado"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByNumDocumento", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.numDocumento = :numDocumento"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByNumSerie", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.numSerie = :numSerie"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByDtcCriacao", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIndExcluido", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.indExcluido = :indExcluido"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByDtcEmissao", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.dtcEmissao = :dtcEmissao"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByDtcValidade", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.dtcValidade = :dtcValidade"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByDtcExclusao", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByOrgExpedidorOutros", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.orgExpedidorOutros = :orgExpedidorOutros"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdeOrgaoExpedidor", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.ideOrgaoExpedidor = :ideOrgaoExpedidor"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByDscCaminhoArquivo", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.dscCaminhoArquivo = :dscCaminhoArquivo"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByIdeRequerimento", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "VwDocumentoIdentificacao.findByTipoDocumento", query = "SELECT v FROM VwDocumentoIdentificacao v WHERE v.tipoDocumento = :tipoDocumento")})
public class VwDocumentoIdentificacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_dto")
    private Integer idDto;
    @Column(name = "ide_documento_identificacao")
    private Integer ideDocumentoIdentificacao;
    @Column(name = "ide_tipo_identificacao")
    private Integer ideTipoIdentificacao;
    @Column(name = "ide_pessoa")
    private Integer idePessoa;
    @Column(name = "ide_estado")
    private Integer ideEstado;
    @Column(name = "num_documento", length = 50)
    private String numDocumento;
    @Column(name = "num_serie", length = 15)
    private String numSerie;
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "ind_excluido")
    private Boolean indExcluido;
    @Column(name = "dtc_emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcEmissao;
    @Column(name = "dtc_validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidade;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @Column(name = "org_expedidor_outros", length = 150)
    private String orgExpedidorOutros;
    @Column(name = "ide_orgao_expedidor")
    private Integer ideOrgaoExpedidor;
    @Column(name = "dsc_caminho_arquivo", length = 1000)
    private String dscCaminhoArquivo;
    @Column(name = "ide_requerimento")
    private Integer ideRequerimento;
    @Column(name = "tipo_documento", length = 2147483647)
    private String tipoDocumento;

    public VwDocumentoIdentificacao() {
    }

    public Integer getIdDto() {
        return idDto;
    }

    public void setIdDto(Integer idDto) {
        this.idDto = idDto;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public Integer getIdeTipoIdentificacao() {
        return ideTipoIdentificacao;
    }

    public void setIdeTipoIdentificacao(Integer ideTipoIdentificacao) {
        this.ideTipoIdentificacao = ideTipoIdentificacao;
    }

    public Integer getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Integer idePessoa) {
        this.idePessoa = idePessoa;
    }

    public Integer getIdeEstado() {
        return ideEstado;
    }

    public void setIdeEstado(Integer ideEstado) {
        this.ideEstado = ideEstado;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(Boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcEmissao() {
        return dtcEmissao;
    }

    public void setDtcEmissao(Date dtcEmissao) {
        this.dtcEmissao = dtcEmissao;
    }

    public Date getDtcValidade() {
        return dtcValidade;
    }

    public void setDtcValidade(Date dtcValidade) {
        this.dtcValidade = dtcValidade;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public String getOrgExpedidorOutros() {
        return orgExpedidorOutros;
    }

    public void setOrgExpedidorOutros(String orgExpedidorOutros) {
        this.orgExpedidorOutros = orgExpedidorOutros;
    }

    public Integer getIdeOrgaoExpedidor() {
        return ideOrgaoExpedidor;
    }

    public void setIdeOrgaoExpedidor(Integer ideOrgaoExpedidor) {
        this.ideOrgaoExpedidor = ideOrgaoExpedidor;
    }

    public String getDscCaminhoArquivo() {
        return dscCaminhoArquivo;
    }

    public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
        this.dscCaminhoArquivo = dscCaminhoArquivo;
    }

    public Integer getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(Integer ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
}
