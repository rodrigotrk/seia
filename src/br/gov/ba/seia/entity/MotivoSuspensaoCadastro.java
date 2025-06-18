package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * 
 * @author danilo.santos
 */
@Entity
@Table(name = "motivo_suspensao")
public class MotivoSuspensaoCadastro extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
   	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motivosuspensao_pk")
   	@SequenceGenerator(name = "motivosuspensao_pk", sequenceName = "motivosuspensao_pk", allocationSize = 1)
   	@Column(name = "ide_motivo_suspensao", nullable = false)
   	private Integer ideMotivoSuspensaoCadastro;
    
	@Column(name = "des_motivo")
	private String desMotivo;
	
	@OneToMany(mappedBy="ideMotivoSuspensaoCadastro")
	private Collection<HistoricoMotivoSuspensao> histMotivoSuspensaoCollection;
    
    public MotivoSuspensaoCadastro() {
    }
    
    public MotivoSuspensaoCadastro(String desMotivo) {
    	this.desMotivo = desMotivo;
    }
    
    public MotivoSuspensaoCadastro(Integer ideMotivoSuspensaoCadastro) {
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
	}
	
	public Integer getIdeMotivoSuspensaoCadastro() {
		return ideMotivoSuspensaoCadastro;
	}

	public void setIdeMotivoSuspensaoCadastro(Integer ideMotivoSuspensaoCadastro) {
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
	}

	public String getDesMotivo() {
		return desMotivo;
	}

	public void setDesMotivo(String desMotivo) {
		this.desMotivo = desMotivo;
	}
	
	public Collection<HistoricoMotivoSuspensao> getHistMotivoSuspensaoCollection() {
		return histMotivoSuspensaoCollection;
	}

	public void setHistMotivoSuspensaoCollection(Collection<HistoricoMotivoSuspensao> histMotivoSuspensaoCollection) {
		this.histMotivoSuspensaoCollection = histMotivoSuspensaoCollection;
	}
}