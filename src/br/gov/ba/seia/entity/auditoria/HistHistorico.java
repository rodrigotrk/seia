package br.gov.ba.seia.entity.auditoria;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.Usuario;
/**
 * Classe historico do historico.
 * @author 
 *
 */
@Entity
@Table(name = "HIST_HISTORICO")
@XmlRootElement
public class HistHistorico implements Serializable{
	private static final long serialVersionUID = 3069878299105137734L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_ide_historico_seq")
	@SequenceGenerator(name = "historico_ide_historico_seq", sequenceName = "historico_ide_historico_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_historico")
	private Long ideHistorico;
	
	@Column(name = "data_historico", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 33)
    @Column(name = "acao_historico")
	private String acao;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ide_usuario")
	private Usuario ideUsuario;
	
	@Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "ip_historico")
	private String ipHistorico;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideHistorico")
	private Collection<HistValor> valorCampoCollection;

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Long getIdeHistorico() {
		return ideHistorico;
	}

	public void setIdeHistorico(Long ideHistorico) {
		this.ideHistorico = ideHistorico;
	}

	public Usuario getIdeUsuario() {
		return ideUsuario;
	}

	public void setIdeUsuario(Usuario ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	public String getIpHistorico() {
		return ipHistorico;
	}

	public void setIpHistorico(String ipHistorico) {
		this.ipHistorico = ipHistorico;
	}

	public Collection<HistValor> getValorCampoCollection() {
		return valorCampoCollection;
	}

	public void setValorCampoCollection(Collection<HistValor> valorCampoCollection) {
		this.valorCampoCollection = valorCampoCollection;
	}
}
