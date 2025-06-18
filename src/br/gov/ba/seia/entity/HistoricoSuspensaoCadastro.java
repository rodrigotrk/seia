package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "historico_suspensao_cadastro")
public class HistoricoSuspensaoCadastro extends AbstractEntity implements  Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
   	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_suspensao_cadastro_seq")
   	@SequenceGenerator(name = "historico_suspensao_cadastro_seq", sequenceName = "historico_suspensao_cadastro_seq", allocationSize = 1)
   	@Column(name = "ide_suspensao_cadastro", nullable = false)
   	private Integer ideSuspensaoCadastro;
    
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;
		
    @Size(max = 50)
	@Column(name = "num_notificacao_auto")
	private String numNotificacaoAuto;
    
    @Size(max = 450)
   	@Column(name = "dsc_observacao")
   	private String dscObservacao;
    
    @Column(name = "dtc_retirada_suspensao")
   	@Temporal(TemporalType.TIMESTAMP)
   	private Date dtcRetiradaSuspensao;
    
    @Column(name = "dtc_suspensao_cadastro")
   	@Temporal(TemporalType.TIMESTAMP)
   	private Date dtcSuspensaoCadastro;
    
    @OneToMany(mappedBy="ideSuspensaoCadastro")
	private Collection<HistoricoMotivoSuspensao> histMotivoSuspensaoCollection;
        
    @JoinColumn(name="ide_pessoa_fisica", referencedColumnName="ide_pessoa_fisica", nullable=false)
	@OneToOne(optional=false)
	private PessoaFisica idePessoaFisica;
    
    public HistoricoSuspensaoCadastro() {}
    
    @Override
	public HistoricoSuspensaoCadastro clone() throws CloneNotSupportedException {
		return (HistoricoSuspensaoCadastro) super.clone();
	}
    
    public HistoricoSuspensaoCadastro(Integer ideSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
	}

	public Integer getIdeSuspensaoCadastro() {
		return ideSuspensaoCadastro;
	}

	public void setIdeSuspensaoCadastro(Integer ideSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public String getNumNotificacaoAuto() {
		return numNotificacaoAuto;
	}

	public void setNumNotificacaoAuto(String numNotificacaoAuto) {
		this.numNotificacaoAuto = numNotificacaoAuto;
	}

	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	public Date getDtcSuspensaoCadastro() {
		return dtcSuspensaoCadastro;
	}

	public void setDtcSuspensaoCadastro(Date dtcSuspensaoCadastro) {
		this.dtcSuspensaoCadastro = dtcSuspensaoCadastro;
	}

	public Date getDtcRetiradaSuspensao() {
		return dtcRetiradaSuspensao;
	}

	public void setDtcRetiradaSuspensao(Date dtcRetiradaSuspensao) {
		this.dtcRetiradaSuspensao = dtcRetiradaSuspensao;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Collection<HistoricoMotivoSuspensao> getHistMotivoSuspensaoCollection() {
		return histMotivoSuspensaoCollection;
	}

	public void setHistMotivoSuspensaoCollection(Collection<HistoricoMotivoSuspensao> histMotivoSuspensaoCollection) {
		this.histMotivoSuspensaoCollection = histMotivoSuspensaoCollection;
	}
}