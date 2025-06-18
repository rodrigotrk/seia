package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import flexjson.JSON;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "status_requerimento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_status_requerimento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusRequerimento.findAll", query = "SELECT s FROM StatusRequerimento s"),
    @NamedQuery(name = "StatusRequerimento.findByIdeStatusRequerimento", query = "SELECT s FROM StatusRequerimento s WHERE s.ideStatusRequerimento = :ideStatusRequerimento"),
    @NamedQuery(name = "StatusRequerimento.findByNomStatusRequerimento", query = "SELECT s FROM StatusRequerimento s WHERE s.nomStatusRequerimento = :nomStatusRequerimento")})
public class StatusRequerimento extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ide_status_requerimento", nullable = false)
    private Integer ideStatusRequerimento;
   
    @Size(min = 1, max = 20)
    @Column(name = "nom_status_requerimento", nullable = false, length = 20)
    private String nomStatusRequerimento;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideStatusRequerimento", fetch = FetchType.LAZY)
    private Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection;

    public StatusRequerimento() {
    }

    public StatusRequerimento(Integer ideStatusRequerimento) {
        this.ideStatusRequerimento = ideStatusRequerimento;
    }

    public StatusRequerimento(Integer ideStatusRequerimento, String nomStatusRequerimento) {
        this.ideStatusRequerimento = ideStatusRequerimento;
        this.nomStatusRequerimento = nomStatusRequerimento;
    }

    public Integer getIdeStatusRequerimento() {
        return ideStatusRequerimento;
    }

    public void setIdeStatusRequerimento(Integer ideStatusRequerimento) {
        this.ideStatusRequerimento = ideStatusRequerimento;
    }

    public String getNomStatusRequerimento() {
        return nomStatusRequerimento;
    }

    public void setNomStatusRequerimento(String nomStatusRequerimento) {
        this.nomStatusRequerimento = nomStatusRequerimento;
    }

    @XmlTransient
    public Collection<TramitacaoRequerimento> getTramitacaoRequerimentoCollection() {
        return tramitacaoRequerimentoCollection;
    }

    public void setTramitacaoRequerimentoCollection(Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection) {
        this.tramitacaoRequerimentoCollection = tramitacaoRequerimentoCollection;
    }

    @JSON(include = false)
	public boolean isEnquadrado() {
		return StatusRequerimentoEnum.ENQUADRADO.getStatus() == this.ideStatusRequerimento;
	}
	
    @JSON(include = false)
	public boolean isPendenciaEnvioDocumentacao() {
		return StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus() == this.ideStatusRequerimento;
	}
    
    @JSON(include = false)
	public boolean isPendenciaValidacao() {
		return StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus() == this.ideStatusRequerimento;
	}
    
    @JSON(include = false)
   	public boolean isValidado() {
   		return StatusRequerimentoEnum.VALIDADO.getStatus() == this.ideStatusRequerimento;
   	}
    
    @JSON(include = false)
   	public boolean isBoletoLiberado() {
   		return StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus() == this.ideStatusRequerimento;
   	}
    @JSON(include = false)
   	public boolean isProcessoFormado() {
   		return StatusRequerimentoEnum.FORMADO.getStatus() == this.ideStatusRequerimento;
   	}
    @JSON(include = false)
   	public boolean isCancelado() {
   		return StatusRequerimentoEnum.CANCELADO.getStatus() == this.ideStatusRequerimento;
   	}
    
    @JSON(include = false)
	public boolean isPendenciaValidacaoComprovante() {
		return StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus() == this.ideStatusRequerimento;
	}
    
    @JSON(include = false)
	public boolean isRequerimentoIncompleto() {
		return StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus() == this.ideStatusRequerimento;
	}
    
    @JSON(include = false)
    public boolean isAguardandoEnquadramento() {
    	return StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus() == this.ideStatusRequerimento;
    }
    
    @JSON(include = false)
    public boolean isDeclaracaoEmitida() {
    	return StatusRequerimentoEnum.DECLARACAO_EMITIDA.getStatus() == this.ideStatusRequerimento;
    }
}