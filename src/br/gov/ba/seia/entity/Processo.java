package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "processo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_processo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Processo.findAll", query = "SELECT p FROM Processo p"),
    @NamedQuery(name = "Processo.findByIdeProcesso", query = "SELECT p FROM Processo p WHERE p.ideProcesso = :ideProcesso"),
    @NamedQuery(name = "Processo.findByIdeRequerimento", query = "SELECT p FROM Processo p WHERE p.ideRequerimento.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "Processo.findByNumProcesso", query = "SELECT p FROM Processo p WHERE p.numProcesso = :numProcesso"),
    @NamedQuery(name = "Processo.findByIndExcluido", query = "SELECT p FROM Processo p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "Processo.findByDtcFormacao", query = "SELECT p FROM Processo p WHERE p.dtcFormacao = :dtcFormacao"),
    @NamedQuery(name = "Processo.findByOrgaoAno", query = "SELECT p FROM Processo p WHERE TO_CHAR(p.dtcFormacao, 'YYYY') = :anoFormacao AND p.ideOrgao.codOrgao = :codOrgao ORDER BY p.numProcesso DESC "),
    @NamedQuery(name = "Processo.findLastByOrgaoAno", query = "SELECT p FROM Processo p WHERE TO_CHAR(p.dtcFormacao, 'YYYY') = :anoFormacao AND p.ideOrgao.codOrgao = :codOrgao and p.ideProcesso = (SELECT max(p.ideProcesso) FROM Processo p WHERE TO_CHAR(p.dtcFormacao, 'YYYY') = :anoFormacao AND p.ideOrgao.codOrgao = :codOrgao) ORDER BY p.numProcesso DESC"),
    @NamedQuery(name = "Processo.findLastByOrgaoAnoGrupo", query = "SELECT p FROM Processo p WHERE TO_CHAR(p.dtcFormacao, 'YYYY') = :anoFormacao AND p.ideOrgao.codOrgao = :codOrgao and p.ideProcesso = (SELECT max(p.ideProcesso) FROM Processo p WHERE TO_CHAR(p.dtcFormacao, 'YYYY') = :anoFormacao AND p.ideOrgao.codOrgao = :codOrgao and p.numProcesso like :grupo) and p.numProcesso like :grupo  ORDER BY p.numProcesso DESC"),
    @NamedQuery(name = "Processo.findByDtcExcluido", query = "SELECT p FROM Processo p WHERE p.dtcExcluido = :dtcExcluido")})
public class Processo extends AbstractEntity implements Cloneable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="PROCESSO_IDEPROCESSO_GENERATOR", sequenceName="PROCESSO_IDE_PROCESSO_SEQ",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROCESSO_IDEPROCESSO_GENERATOR")
    @Column(name = "ide_processo",  unique=true, nullable = false)
    private Integer ideProcesso;
    
    @Column(name = "num_processo", nullable = false, length = 50)
    private String numProcesso;
    
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Column(name = "dtc_excluido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExcluido;
    
    @Column(name = "dtc_formacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcFormacao;
    
    @Column(name = "texto_portaria")
    private String textoPortaria;
    
    @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
    @ManyToOne(fetch = FetchType.LAZY)
    private Requerimento ideRequerimento;
    
    @JoinColumn(name = "ide_processo_pai", referencedColumnName = "ide_processo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Processo ideProcessoPai;
    
    @JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Orgao ideOrgao;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<ArquivoProcesso> arquivoProcessoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "processo", fetch = FetchType.LAZY)
    private Collection<ProcessoAto> processoAtoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<ControleTramitacao> controleTramitacaoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<ControleTramitacao> controleTramitacaoFormacaoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<Notificacao> notificacaoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<Cronograma> cronogramaCollection;
    
    @OneToMany(mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<AnaliseTecnica> analiseTecnicaCollection;
    
    @OneToMany(mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<Equipe> equipeCollection;
    
    @OneToMany(mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<Portaria> portariaCollection;
    
    @OneToMany(mappedBy = "ideProcessoTla", fetch = FetchType.LAZY)
    private Collection<TransferenciaAmbiental> transferenciaAmbientalCollection;
    
    @OneToMany(mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<ProcessoReenquadramento> processoReenquadramentoCollection;
    
    @JoinTable(name = "processo_vinculado", joinColumns = {
    		@JoinColumn(name = "ide_processo_originador", referencedColumnName = "ide_processo", nullable = false)}, inverseJoinColumns = {
    		@JoinColumn(name = "ide_processo_vinculado", referencedColumnName = "ide_processo", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Processo> processoCollection;
 
    @OneToMany(mappedBy = "ideProcesso", fetch = FetchType.LAZY)
    private Collection<ProcessoSinaflor> processoSinaflorCollection;

    @Transient
    private Collection<AtoAmbiental> atosAmbientais;
    
    public Processo() {}

    public Processo(Integer ideProcesso, Integer ideRequerimento) {
		super();
		this.ideProcesso = ideProcesso;
		this.ideRequerimento = new Requerimento(ideRequerimento);
	}

	public Processo(Integer ideProcesso) {
        this.ideProcesso = ideProcesso;
    }
    public Processo(Integer ideProcesso, String numProcesso) {
        this.ideProcesso = ideProcesso;
        this.numProcesso = numProcesso;
    }
    public Processo(String numProcesso) {
    	this.numProcesso = numProcesso;
    }
    public Processo(Integer ideProcesso, String numProcesso, Date dtcFormacao) {
        this.ideProcesso = ideProcesso;
        this.numProcesso = numProcesso;
        this.dtcFormacao = dtcFormacao;
    }
    public Processo(Integer ideProcesso, String numProcesso, boolean indExcluido, Date dtcFormacao) {
        this.ideProcesso = ideProcesso;
        this.numProcesso = numProcesso;
        this.indExcluido = indExcluido;
        this.dtcFormacao = dtcFormacao;
    }
    
    /**
     * Método que retorna o número do processo formatado para quebrar linha na grid. 
     * Caso o requerimento nao possua numero, retorna um traco.
     *
     * @author micael.coutinho
     * @return {@link String} já formatada com espaço
     */
    @JSON(include = false)
	public String getNumProcessoTabela() {
		String numTabela;
		
		if(!Util.isNullOuVazio(numProcesso)){
			int index = numProcesso.indexOf('/');
			numTabela = numProcesso.substring(0, index);
			numTabela = numTabela + ' ' + numProcesso.substring(index);
		} else {
			numTabela = "-";
		}
		
		return numTabela;
	}
	
	/**
	 * Método que retorna o número do processo formatado para quebrar linha na grid. 
	 * Caso o requerimento nao possua numero, retorna null
	 *
	 * @author micael.coutinho
	 * @return {@link String} já formatada com espaço
	 */
    @JSON(include = false)
	public String getNumProcessoOrNull() {
		String numTabela;
		
		if(!Util.isNullOuVazio(numProcesso)){
			int index = numProcesso.indexOf('/');
			numTabela = numProcesso.substring(0, index);
			numTabela = numTabela + ' ' + numProcesso.substring(index);
			
			return numTabela;
		} else {
			return null;
		}
	}

    public Integer getIdeProcesso() {
        return ideProcesso;
    }

    public void setIdeProcesso(Integer ideProcesso) {
        this.ideProcesso = ideProcesso;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
    }

    @JSON(include = false)
    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcFormacao() {
        return dtcFormacao;
    }

    public void setDtcFormacao(Date dtcFormacao) {
        this.dtcFormacao = dtcFormacao;
    }

    @JSON(include = false)
    public Date getDtcExcluido() {
        return dtcExcluido;
    }

    public void setDtcExcluido(Date dtcExcluido) {
        this.dtcExcluido = dtcExcluido;
    }

    @XmlTransient
    public Collection<Processo> getProcessoCollection() {
        return processoCollection;
    }

    public void setProcessoCollection(Collection<Processo> processoCollection) {
        this.processoCollection = processoCollection;
    }

 
    @XmlTransient
    public Collection<ArquivoProcesso> getArquivoProcessoCollection() {
        return arquivoProcessoCollection;
    }

    public void setArquivoProcessoCollection(Collection<ArquivoProcesso> arquivoProcessoCollection) {
        this.arquivoProcessoCollection = arquivoProcessoCollection;
    }

    @JSON(include = false)
    public Requerimento getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(Requerimento ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    @JSON(include = false)
    public Processo getIdeProcessoPai() {
        return ideProcessoPai;
    }

    public void setIdeProcessoPai(Processo ideProcessoPai) {
        this.ideProcessoPai = ideProcessoPai;
    }

    @JSON(include = false)
    public Orgao getIdeOrgao() {
        return ideOrgao;
    }

    public void setIdeOrgao(Orgao ideOrgao) {
        this.ideOrgao = ideOrgao;
    }

    @XmlTransient
    public Collection<ProcessoAto> getProcessoAtoCollection() {
        return processoAtoCollection;
    }

    public void setProcessoAtoCollection(Collection<ProcessoAto> processoAtoCollection) {
        this.processoAtoCollection = processoAtoCollection;
    }

    @XmlTransient
    public Collection<ControleTramitacao> getControleTramitacaoCollection() {
        return controleTramitacaoCollection;
    }

    public void setControleTramitacaoCollection(Collection<ControleTramitacao> controleTramitacaoCollection) {
        this.controleTramitacaoCollection = controleTramitacaoCollection;
    }

    @XmlTransient
    public Collection<Notificacao> getNotificacaoCollection() {
        return notificacaoCollection;
    }

    public void setNotificacaoCollection(Collection<Notificacao> notificacaoCollection) {
        this.notificacaoCollection = notificacaoCollection;
    }

    @XmlTransient
    public Collection<Cronograma> getCronogramaCollection() {
        return cronogramaCollection;
    }

    public void setCronogramaCollection(Collection<Cronograma> cronogramaCollection) {
        this.cronogramaCollection = cronogramaCollection;
    }

    @JSON(include = false)
    public Collection<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	@Transient
	@JSON(include = false)
    public Processo getClone() {

    	try {

    		return (Processo) this.clone();
		}
    	catch (CloneNotSupportedException e) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}

    	return null;
    }
	
    @Override
    public String toString() {
    	
    	if(ideProcesso != null){
    		return ideProcesso.toString();
    		
    	}else if(numProcesso != null){
    		return numProcesso;
    	}
    	else{
    		return "";
    	}
    	
    }

    @JSON(include = false)
	public String getTextoPortaria() {
		return textoPortaria;
	}

	public void setTextoPortaria(String textoPortaria) {
		this.textoPortaria = textoPortaria;
	}

	public Collection<AnaliseTecnica> getAnaliseTecnicaCollection() {
		return analiseTecnicaCollection;
	}

	public void setAnaliseTecnicaCollection(Collection<AnaliseTecnica> analiseTecnicaCollection) {
		this.analiseTecnicaCollection = analiseTecnicaCollection;
	}

	public Collection<Equipe> getEquipeCollection() {
		return equipeCollection;
	}

	public void setEquipeCollection(Collection<Equipe> equipeCollection) {
		this.equipeCollection = equipeCollection;
	}

	public Collection<Portaria> getPortariaCollection() {
		return portariaCollection;
	}

	public void setPortariaCollection(Collection<Portaria> portariaCollection) {
		this.portariaCollection = portariaCollection;
	}

	public Collection<TransferenciaAmbiental> getTransferenciaAmbientalCollection() {
		return transferenciaAmbientalCollection;
	}

	public void setTransferenciaAmbientalCollection(
			Collection<TransferenciaAmbiental> transferenciaAmbientalCollection) {
		this.transferenciaAmbientalCollection = transferenciaAmbientalCollection;
	}

	public Collection<ControleTramitacao> getControleTramitacaoFormacaoCollection() {
		return controleTramitacaoFormacaoCollection;
	}

	public void setControleTramitacaoFormacaoCollection(
			Collection<ControleTramitacao> controleTramitacaoFormacaoCollection) {
		this.controleTramitacaoFormacaoCollection = controleTramitacaoFormacaoCollection;
	}

	public Collection<ProcessoReenquadramento> getProcessoreenquadramentoCollection() {
		return processoReenquadramentoCollection;
	}

	public void setProcessoreenquadramentoCollection(
			Collection<ProcessoReenquadramento> processoReenquadramentoCollection) {
		this.processoReenquadramentoCollection = processoReenquadramentoCollection;
	}

	public Collection<ProcessoSinaflor> getProcessoSinaflorCollection() {
		return processoSinaflorCollection;
	}

	public void setProcessoSinaflorCollection(Collection<ProcessoSinaflor> processoSinaflorCollection) {
		this.processoSinaflorCollection = processoSinaflorCollection;
	}
    
}
