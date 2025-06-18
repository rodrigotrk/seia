package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MJunior
 */
@Entity
@Table(name = "vw_responsavel_documento_procuracao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwResponsavelDocumentoProcuracao.findAll", query = "SELECT v FROM VwResponsavelDocumentoProcuracao v"),
    @NamedQuery(name = "VwResponsavelDocumentoProcuracao.findByIdDto", query = "SELECT v FROM VwResponsavelDocumentoProcuracao v WHERE v.idDto = :idDto"),
    @NamedQuery(name = "VwResponsavelDocumentoProcuracao.findByIdeRequerimentoDto", query = "SELECT v FROM VwResponsavelDocumentoProcuracao v WHERE v.ideRequerimentoDto = :ideRequerimentoDto"),
    @NamedQuery(name = "VwResponsavelDocumentoProcuracao.findByTipoDocumento", query = "SELECT v FROM VwResponsavelDocumentoProcuracao v WHERE v.tipoDocumento = :tipoDocumento"),
    @NamedQuery(name = "VwResponsavelDocumentoProcuracao.findByIdeRepresentanteDocRepresentacao", query = "SELECT v FROM VwResponsavelDocumentoProcuracao v WHERE v.ideRepresentanteDocRepresentacao = :ideRepresentanteDocRepresentacao")})
public class VwResponsavelDocumentoProcuracao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_dto")
    private BigInteger idDto;
    @Column(name = "ide_requerimento_dto")
    private Integer ideRequerimentoDto;
    @Column(name = "tipo_documento", length = 2147483647)
    private String tipoDocumento;
    @Column(name = "ide_representante_doc_representacao")
    private Integer ideRepresentanteDocRepresentacao;

    public VwResponsavelDocumentoProcuracao() {
    }

    public BigInteger getIdDto() {
        return idDto;
    }

    public void setIdDto(BigInteger idDto) {
        this.idDto = idDto;
    }

    public Integer getIdeRequerimentoDto() {
        return ideRequerimentoDto;
    }

    public void setIdeRequerimentoDto(Integer ideRequerimentoDto) {
        this.ideRequerimentoDto = ideRequerimentoDto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getIdeRepresentanteDocRepresentacao() {
        return ideRepresentanteDocRepresentacao;
    }

    public void setIdeRepresentanteDocRepresentacao(Integer ideRepresentanteDocRepresentacao) {
        this.ideRepresentanteDocRepresentacao = ideRepresentanteDocRepresentacao;
    }
    
}
