package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "processo_ato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcessoAto.findAll", query = "SELECT p FROM ProcessoAto p"),
    @NamedQuery(name = "ProcessoAto.findByIdeProcesso", query = "SELECT p FROM ProcessoAto p WHERE p.processo = :ideProcesso"),
    @NamedQuery(name = "ProcessoAto.findByIdeAtoAmbiental", query = "SELECT p FROM ProcessoAto p WHERE p.atoAmbiental = :ideAtoAmbiental"),
    @NamedQuery(name = "ProcessoAto.findByIndExcluido", query = "SELECT p FROM ProcessoAto p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcessoAto.findByDtcExclusao", query = "SELECT p FROM ProcessoAto p WHERE p.dtcExclusao = :dtcExclusao")})
public class ProcessoAto extends AbstractEntity implements Cloneable {
    
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "processo_ato_ide_processo_ato_GENERATOR", sequenceName = "processo_ato_ide_processo_ato_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processo_ato_ide_processo_ato_GENERATOR")
    @Column(name = "ide_processo_ato")
    private Integer ideProcessoAto;
    
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Processo processo;

	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private AtoAmbiental atoAmbiental;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
	@ManyToOne(fetch = FetchType.EAGER)
	private Tipologia tipologia;
	
	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento")
	@OneToOne(fetch = FetchType.EAGER)
	private ProcessoReenquadramento ideProcessoReenquadramento;
	
	@OneToOne(mappedBy="ideProcessoAto", fetch=FetchType.LAZY)
	private ProcessoAtoConcedido ideProcessoAtoConcedido;

	@OneToMany(mappedBy="ideProcessoAto", fetch=FetchType.LAZY)
	private Collection<ControleProcessoAto> controleProcessoAtoCollection;

	@OneToMany(mappedBy="ideProcessoAto", fetch=FetchType.LAZY)
	private Collection<TransferenciaAmbiental> transferenciaAmbientalCollection;

	@OneToMany(mappedBy="ideProcessoAto", fetch=FetchType.LAZY)
	private Collection<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeCollection;

	@OneToMany(mappedBy="ideProcessoAto", fetch=FetchType.LAZY)
	private Collection<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoCollection;
	
	@Transient
	private TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua;
	
	@Transient
	private StatusProcessoAto statusProcessoAto;
	
	@Transient
	private String status;

	@Transient
	private boolean isTlaStatusDeferido;

	@Transient
	private List<Processo> processoDeferido;

	@Transient
	private List<Processo> processoNaoDeferido;
	
	@Transient
	private String observacaoAtoProcesso;

	public ProcessoAto() {
	}

	public ProcessoAto(Processo processo) {
		this.processo = processo;
	}

	public ProcessoAto(int ideProcesso, int ideAtoAmbiental) {
		this.processo = new Processo(ideProcesso);
		this.atoAmbiental = new AtoAmbiental(ideAtoAmbiental);
	}

	public ProcessoAto(Processo ideProcesso, AtoAmbiental ideAtoAmbiental) {
		this.processo = ideProcesso;
		this.atoAmbiental = ideAtoAmbiental;
	}

	public String getNomAtoAmbientalTipologia(){
		if(!Util.isNullOuVazio(atoAmbiental) && !Util.isNullOuVazio(atoAmbiental.getNomAtoAmbiental())&&
				!Util.isNullOuVazio(tipologia)    && !Util.isNullOuVazio(tipologia.getDesTipologia())){
			return atoAmbiental.getNomAtoAmbiental() + " - " + tipologia.getDesTipologia();
		}
		else if(!Util.isNullOuVazio(atoAmbiental) && !Util.isNullOuVazio(atoAmbiental.getNomAtoAmbiental())){
			return atoAmbiental.getNomAtoAmbiental();
		}
		else{
			return "";
		}
	}


	@JSON(include = false)
	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@JSON(include = false)
	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	@JSON(include = false)
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public String getNomAtoAmbiental() {
		return atoAmbiental.getNomAtoAmbiental();
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atoAmbiental == null) ? 0 : atoAmbiental.hashCode());
		result = prime * result + ((processo == null) ? 0 : processo.hashCode());
		result = prime * result + ((tipologia == null) ? 0 : tipologia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProcessoAto other = (ProcessoAto) obj;
		if (atoAmbiental == null) {
			if (other.atoAmbiental != null) {
				return false;
			}
		} else if (!atoAmbiental.equals(other.atoAmbiental)) {
			return false;
		}
		if (processo == null) {
			if (other.processo != null) {
				return false;
			}
		} else if (!processo.equals(other.processo)) {
			return false;
		}
		if (tipologia == null) {
			if (other.tipologia != null) {
				return false;
			}
		} else if (!tipologia.equals(other.tipologia)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ProcessoAto [ideProcessoAto=" + ideProcessoAto + ", processo=" + processo + ", atoAmbiental="
				+ atoAmbiental + ", tipologia=" + tipologia + "]";
	}

	public StatusProcessoAto getStatusProcessoAto() {
		return statusProcessoAto;
	}
	
	public void setStatusProcessoAto(StatusProcessoAto statusProcessoAto) {
		this.statusProcessoAto = statusProcessoAto;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(Integer ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public Collection<ControleProcessoAto> getControleProcessoAtoCollection() {
		return controleProcessoAtoCollection;
	}

	public void setControleProcessoAtoCollection(Collection<ControleProcessoAto> controleProcessoAtoCollection) {
		this.controleProcessoAtoCollection = controleProcessoAtoCollection;
	}

	public Collection<TransferenciaAmbiental> getTransferenciaAmbientalCollection() {
		return transferenciaAmbientalCollection;
	}

	public void setTransferenciaAmbientalCollection(Collection<TransferenciaAmbiental> transferenciaAmbientalCollection) {
		this.transferenciaAmbientalCollection = transferenciaAmbientalCollection;
	}

	public boolean isTlaStatusDeferido() {
		return isTlaStatusDeferido;
	}

	public void setTlaStatusDeferido(boolean isTLAstatusDeferido) {
		this.isTlaStatusDeferido = isTLAstatusDeferido;
	}


	public Collection<ProcessoAtoIntegranteEquipe> getProcessoAtoIntegranteEquipeCollection() {
		return processoAtoIntegranteEquipeCollection;
	}

	public void setProcessoAtoIntegranteEquipeCollection(
			Collection<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeCollection) {
		this.processoAtoIntegranteEquipeCollection = processoAtoIntegranteEquipeCollection;
	}

	public String getDescricao() {
		return Util.isNull(tipologia) ? atoAmbiental.getNomAtoAmbiental() : atoAmbiental.getNomAtoAmbiental() + " - " + tipologia.getDesTipologia();
	}

	public String getListProcessoNaoDeferidos(){
		String retorno = "Existe transferência de licença ambiental (TLA) em trâmite para esse ato. ";

		if(processoNaoDeferido!=null){
			for (Processo processo : processoNaoDeferido){
				retorno += processo.getNumProcesso() + " \n";
			}
		}
		return retorno;
	}

	public List<Processo> getProcessoDeferido() {
		if(Util.isNullOuVazio(processoDeferido)){
			processoDeferido = new ArrayList<Processo>();
		}

		return processoDeferido;
	}

	public void setProcessoDeferido(List<Processo> processoDeferido) {
		this.processoDeferido = processoDeferido;
	}

	public List<Processo> getProcessoNaoDeferido() {
		if(Util.isNullOuVazio(processoNaoDeferido)){
			processoNaoDeferido = new ArrayList<Processo>();
		}
		return processoNaoDeferido;
	}

	public void setProcessoNaoDeferido(List<Processo> processoNaoDeferido) {
		this.processoNaoDeferido = processoNaoDeferido;
	}
	
	@Override
	public ProcessoAto clone() throws CloneNotSupportedException {
		return (ProcessoAto) super.clone();
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public Collection<ReenquadramentoProcessoAto> getReenquadramentoProcessoAtoCollection() {
		return reenquadramentoProcessoAtoCollection;
	}

	public void setReenquadramentoProcessoAtoCollection(
			Collection<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoCollection) {
		this.reenquadramentoProcessoAtoCollection = reenquadramentoProcessoAtoCollection;
	}

	public String getObservacaoAtoProcesso() {
		return observacaoAtoProcesso;
	}

	public void setObservacaoAtoProcesso(String observacaoAtoProcesso) {
		this.observacaoAtoProcesso = observacaoAtoProcesso;
	}

	public ProcessoAtoConcedido getIdeProcessoAtoConcedido() {
		return ideProcessoAtoConcedido;
	}

	public void setIdeProcessoAtoConcedido(ProcessoAtoConcedido ideProcessoAtoConcedido) {
		this.ideProcessoAtoConcedido = ideProcessoAtoConcedido;
	}

	public TipoFinalidadeUsoAgua getTipoFinalidadeUsoAgua() {
		return tipoFinalidadeUsoAgua;
	}

	public void setTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.tipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}
}
