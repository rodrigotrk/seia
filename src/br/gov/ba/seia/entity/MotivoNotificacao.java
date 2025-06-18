package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedNativeQuery;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import flexjson.JSON;

/**
 *
 * @author rubem.filho
 */
@Entity
@Table(name = "motivo_notificacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_motivo_notificacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivoNotificacao.findAll", query = "SELECT m FROM MotivoNotificacao m"),
    @NamedQuery(name = "MotivoNotificacao.findByIdeMotivoNotificacao", query = "SELECT m FROM MotivoNotificacao m WHERE m.ideMotivoNotificacao = :ideMotivoNotificacao"),
    @NamedQuery(name = "MotivoNotificacao.findByNomMotivoNotificacao", query = "SELECT m FROM MotivoNotificacao m WHERE m.nomMotivoNotificacao = :nomMotivoNotificacao")})
@NamedNativeQuery(name = "MotivoNotificacao.findByIdeNotificacao",query="select mn.* from notificacao_motivo_notificacao nmn ,motivo_notificacao mn where mn.ide_motivo_notificacao = nmn.ide_motivo_notificacao  and nmn.ide_notificacao  = :ideNotificacao ",resultClass=MotivoNotificacao.class)
public class MotivoNotificacao extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Column(name = "ide_motivo_notificacao", nullable = false)
    private Integer ideMotivoNotificacao;
    
	@Column(name = "nom_motivo_notificacao", length = 30)
    private String nomMotivoNotificacao;
    
	@Column(name="ind_notificacao_prazo")
    private boolean indNotPrazo;
    
	@Column(name="ind_notificacao_comunicacao")
    private boolean indNotComunicacao;
    
	@Column(name="ind_notificacao_homologacao")
    private boolean indNotHomologacao;
	
	@Column(name="ind_envio_shape")
	private Boolean indEnvioShape;
    
	@OneToMany(mappedBy="ideMotivoNotificacao")
    private Collection<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollection;
	
	@OneToMany(mappedBy="ideArquivoProcesso")
	private Collection<ArquivoProcesso> arquivoProcessoCollection;
    
    @Transient
    private boolean checked;

    public MotivoNotificacao() {
    }

    public MotivoNotificacao(Integer ideMotivoNotificacao) {
        this.ideMotivoNotificacao = ideMotivoNotificacao;
    }

    public MotivoNotificacao(Integer ideMotivoNotificacao, String nomMotivoNotificacao) {
        this.ideMotivoNotificacao = ideMotivoNotificacao;
        this.nomMotivoNotificacao = nomMotivoNotificacao;
    }

    @JSON(include = false)
    public Integer getIdeMotivoNotificacao() {
        return ideMotivoNotificacao;
    }

    public void setIdeMotivoNotificacao(Integer ideMotivoNotificacao) {
        this.ideMotivoNotificacao = ideMotivoNotificacao;
    }

    public String getNomMotivoNotificacao() {
        return nomMotivoNotificacao;
    }

    public void setNomMotivoNotificacao(String nomMotivoNotificacao) {
        this.nomMotivoNotificacao = nomMotivoNotificacao;
    }

    @JSON(include = false)
	public boolean isIndNotPrazo() {
		return indNotPrazo;
	}

	public void setIndNotPrazo(boolean indNotPrazo) {
		this.indNotPrazo = indNotPrazo;
	}

	@JSON(include = false)
	public boolean isIndNotComunicacao() {
		return indNotComunicacao;
	}

	public void setIndNotComunicacao(boolean indNotComunicacao) {
		this.indNotComunicacao = indNotComunicacao;
	}

	@JSON(include = false)
	public boolean isIndNotHomologacao() {
		return indNotHomologacao;
	}

	public void setIndNotHomologacao(boolean indNotHomologacao) {
		this.indNotHomologacao = indNotHomologacao;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Boolean getIndEnvioShape() {
		return indEnvioShape;
	}

	public void setIndEnvioShape(Boolean indEnvioShape) {
		this.indEnvioShape = indEnvioShape;
	}

	public Collection<NotificacaoMotivoNotificacao> getNotificacaoMotivoNotificacaoCollection() {
		return notificacaoMotivoNotificacaoCollection;
	}

	public void setNotificacaoMotivoNotificacaoCollection(
			Collection<NotificacaoMotivoNotificacao> notificacaoMotivoNotificacaoCollection) {
		this.notificacaoMotivoNotificacaoCollection = notificacaoMotivoNotificacaoCollection;
	}

	public Collection<ArquivoProcesso> getArquivoProcessoCollection() {
		return arquivoProcessoCollection;
	}

	public void setArquivoProcessoCollection(
			Collection<ArquivoProcesso> arquivoProcessoCollection) {
		this.arquivoProcessoCollection = arquivoProcessoCollection;
	}
}