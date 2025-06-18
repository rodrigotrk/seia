package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "imovel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Imovel.findAll", query = "SELECT i FROM Imovel i"),
		@NamedQuery(name = "Imovel.findByIdeImovel", query = "SELECT i FROM Imovel i WHERE i.ideImovel = :ideImovel"),
		@NamedQuery(name = "Imovel.findByDtcCriacao", query = "SELECT i FROM Imovel i WHERE i.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "Imovel.findByIndExcluido", query = "SELECT i FROM Imovel i WHERE i.indExcluido = :indExcluido"),
		@NamedQuery(name = "Imovel.findByDtcExclusao", query = "SELECT i FROM Imovel i WHERE i.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "Imovel.findByAusenteRequerimento", query = "SELECT i FROM Imovel i left join i.imovelRural ir left join i.empreendimentoCollection e WHERE e.ideEmpreendimento = :ideEmpreendimento and i.ideImovel not in(SELECT r.imovel.ideImovel FROM RequerimentoImovel r WHERE r.requerimento = :requerimento)"),
		@NamedQuery(name = "Imovel.findByEmpreendimento", query = "SELECT i FROM Imovel i inner join i.imovelRural ir left join i.empreendimentoCollection e WHERE e.ideEmpreendimento = :ideEmpreendimento"),
		@NamedQuery(name = "Imovel.findByEmpreendimentoComOuSemImovel", query = "SELECT i FROM Imovel i left join i.imovelRural ir left join i.empreendimentoCollection e WHERE e.ideEmpreendimento = :ideEmpreendimento"),
		@NamedQuery(name = "Imovel.findImovelByEmpreendimento", query = "SELECT i FROM Imovel i left join i.imovelRural ir left join i.imovelUrbano iu inner join i.empreendimentoCollection e WHERE e.ideEmpreendimento = :ideEmpreendimento"),
		@NamedQuery(name = "Imovel.findComImovelUrbanoByEmpreendimento", query = "SELECT i FROM Imovel i inner join i.empreendimentoCollection e WHERE e.ideEmpreendimento = :ideEmpreendimento") })
public class Imovel implements Serializable, BaseEntity, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMOVEL_IDE_IMOVEL_seq")
	@SequenceGenerator(name = "IMOVEL_IDE_IMOVEL_seq", sequenceName = "IMOVEL_IDE_IMOVEL_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel", nullable = false)
	private Integer ideImovel;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "imovel", fetch = FetchType.EAGER)
	private ImovelUrbano imovelUrbano;
	
	@OneToOne(cascade = CascadeType.MERGE, mappedBy = "imovel", fetch = FetchType.LAZY)
	private ImovelRural imovelRural;
	
	@JoinColumn(name = "ide_tipo_imovel", referencedColumnName = "ide_tipo_imovel", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoImovel ideTipoImovel;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private Endereco ideEndereco;
	
	@ManyToMany(mappedBy = "imovelCollection", fetch = FetchType.LAZY)
	private Collection<Empreendimento> empreendimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovel", fetch = FetchType.LAZY)
	private Collection<PessoaImovel> pessoaImovelCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "imovel", fetch = FetchType.LAZY)
	private Collection<RequerimentoImovel> requerimentoImovelCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovel", fetch = FetchType.LAZY)
	private Collection<ImovelEmpreendimento> imovelEmpreendimentoCollection;
	
	@OneToMany(mappedBy="ideImovel",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private Collection<MotivoNotificacaoImovel> motivoNotificacaoImovelCollection;
	
	@OneToMany(mappedBy="ideArquivoProcesso",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private Collection<ArquivoProcesso> arquivoProcessoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovel", fetch = FetchType.LAZY)
	private Collection<Florestal> florestalCollection;
	
	@JoinColumn(name = "ide_usuario_exclusao", referencedColumnName = "ide_pessoa_fisica")
	@OneToOne(fetch = FetchType.LAZY)
	private Usuario ideUsuarioExclusao;

	@Transient
	private boolean indArrendado;
	
	@Transient
	private Boolean temRequerimentoAssociado;
	
	@Transient
	private String nomImovel;
	
	@Transient
	private Pessoa proprietario;
	
	public Imovel() {}
	
	public Imovel(Object[] resultElement) {
		ideImovel = (Integer) resultElement[0];
		
		imovelRural = new ImovelRural();
		imovelRural.setIdeImovelRural((Integer) resultElement[0]);
		imovelRural.setDesDenominacao((String) resultElement[1]);
		imovelRural.setStatusCadastro((Integer) resultElement[2]);
		imovelRural.setStsCertificado((Boolean) resultElement[3]);
		imovelRural.setPrazoValidade((Date) resultElement[4]);
		
		ideEndereco = new Endereco();
		ideEndereco.setIdeLogradouro(new Logradouro());
		ideEndereco.getIdeLogradouro().setIdeMunicipio(new Municipio());
		ideEndereco.getIdeLogradouro().getIdeMunicipio().setIdeMunicipio((Integer) resultElement[5]);
		ideEndereco.getIdeLogradouro().getIdeMunicipio().setNomMunicipio((String) resultElement[6]);
		ideEndereco.getIdeLogradouro().getIdeMunicipio().setIdeEstado(new Estado((Integer) resultElement[7]));
		
		proprietario = new Pessoa((Integer) resultElement[8]);
		
		String nome = (String) resultElement[9];
		if (!Util.isNullOuVazio(nome)) proprietario.setPessoaFisica(new PessoaFisica((Integer) resultElement[8], nome));
		
		String razao = (String) resultElement[10];
		if (!Util.isNullOuVazio(razao)) {
			proprietario.setPessoaFisica(new PessoaFisica((Integer) resultElement[8], razao));
			proprietario.setPessoaJuridica(new PessoaJuridica((Integer) resultElement[8], razao));
		}
		
		imovelRural.setReservaLegal(new ReservaLegal((Integer) resultElement[11]));
		imovelRural.getReservaLegal().setIdeStatus(new StatusReservaLegal((Integer) resultElement[12], (String) resultElement[13]));
		imovelRural.setDtcFinalizacao((Date) resultElement[14]);
		imovelRural.setIdeLocalizacaoGeografica(new LocalizacaoGeografica((Integer) resultElement[15]));
		imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
		
		if (!Util.isNullOuVazio(resultElement[16])) imovelRural.getImovelRuralSicar().setUrlReciboInscricao((String) resultElement[16]);
		
		if (!Util.isNullOuVazio(resultElement[17])) imovelRural.getImovelRuralSicar().setIndSicronia((Boolean) resultElement[17]);
		
		if (!Util.isNullOuVazio(resultElement[18])) imovelRural.getImovelRuralSicar().setNumProtocolo((String) resultElement[18]);
		
		if (!Util.isNullOuVazio(resultElement[19])) imovelRural.getImovelRuralSicar().setCodRetornoSincronia((String) resultElement[19]);
		
		if (!Util.isNullOuVazio(resultElement[20])) imovelRural.getImovelRuralSicar().setNumSicar((String) resultElement[20]);
		
		if (!Util.isNullOuVazio(resultElement[21])) imovelRural.setIdeTipoCadastroImovelRural(new TipoCadastroImovelRural((Integer) resultElement[21]));

		imovelRural.setIdeRequerenteCadastro(new Pessoa((Integer) resultElement[22]));
		
		imovelRural.setIndSuspensao((Boolean) resultElement[23]);
		
		imovelRural.setIndBloqueioLimite((Boolean) resultElement[24]);
	}
	
	public Imovel(Integer ideImovel) {
		this.ideImovel = ideImovel;
	}
	
	public Imovel(ImovelUrbano imovelUrbano) {
		this.imovelUrbano = imovelUrbano;
	}
	
	public Imovel(ImovelUrbano imovelUrbano, TipoImovel ideTipoImovel, Date dtcCriacao, boolean indExcluido) {
		this.imovelUrbano = imovelUrbano;
		this.ideTipoImovel = ideTipoImovel;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}
	
	public Imovel(Integer ideImovel, Date dtcCriacao, boolean indExcluido) {
		this.ideImovel = ideImovel;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}
	
	public Integer getIdeImovel() {
		return ideImovel;
	}
	
	public void setIdeImovel(Integer ideImovel) {
		this.ideImovel = ideImovel;
	}
	
	public Date getDtcCriacao() {
		return dtcCriacao;
	}
	
	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	public boolean getIndExcluido() {
		return indExcluido;
	}
	
	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	public Date getDtcExclusao() {
		return dtcExclusao;
	}
	
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	public void setEmpreendimentoCollection(Collection<Empreendimento> empreendimentoCollection) {
		this.empreendimentoCollection = empreendimentoCollection;
	}
	
	public void setPessoaImovelCollection(Collection<PessoaImovel> pessoaImovelCollection) {
		this.pessoaImovelCollection = pessoaImovelCollection;
	}
	
	public ImovelUrbano getImovelUrbano() {
		return imovelUrbano;
	}
	
	public void setImovelUrbano(ImovelUrbano imovelUrbano) {
		this.imovelUrbano = imovelUrbano;
	}
	
	public ImovelRural getImovelRural() {
		return imovelRural;
	}
	
	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	
	public TipoImovel getIdeTipoImovel() {
		return ideTipoImovel;
	}
	
	public void setIdeTipoImovel(TipoImovel ideTipoImovel) {
		this.ideTipoImovel = ideTipoImovel;
	}
	
	public Endereco getIdeEndereco() {
		return ideEndereco;
	}
	
	public void setIdeEndereco(Endereco ideEndereco) {
		this.ideEndereco = ideEndereco;
	}
	
	public boolean getIndArrendado() {
		return indArrendado;
	}

	public void setIndArrendado(boolean indArrendado) {
		this.indArrendado = indArrendado;
	}

	public void setRequerimentoImovelCollection(Collection<RequerimentoImovel> requerimentoImovelCollection) {
		this.requerimentoImovelCollection = requerimentoImovelCollection;
	}
	
	public Pessoa getProprietario() {
		return proprietario;
	}
	
	public void setProprietario(Pessoa proprietario) {
		this.proprietario = proprietario;
	}
	
	public void setTemRequerimentoAssociado(Boolean temRequerimentoAssociado) {
		this.temRequerimentoAssociado = temRequerimentoAssociado;
	}
	
	public Boolean getTemRequerimentoAssociado() {
		if (this.getRequerimentoImovelCollection() == null || this.getRequerimentoImovelCollection().size() == 0) {
			temRequerimentoAssociado = false;
		} else {
			temRequerimentoAssociado = true;
		}
		
		return temRequerimentoAssociado;
	}

	public String getNomImovel() {
		return Util.isNullOuVazio(nomImovel) ? StringUtils.EMPTY : nomImovel;
	}

	public void setNomImovel(String nomImovel) {
		this.nomImovel = nomImovel;
	}

	public String getNomeImovelRural() {
		if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getDesDenominacao())) {
			return this.imovelRural.getDesDenominacao();
		} else {
			return "";
		}
	}
	
	public String getLocalidadeImovelRural() {
		if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.getIdeEndereco())
				&& !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro()) && !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro().getIdeBairro())
				&& !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro().getIdeBairro().getNomBairro())) {
			return this.getIdeEndereco().getIdeLogradouro().getIdeBairro().getNomBairro();
		} else {
			return "";
		}
	}
	
	public String getMunicipioImovelRural() {
		if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.getIdeEndereco())
				&& !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro()) && !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro().getIdeMunicipio())
				&& !Util.isNullOuVazio(this.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getNomMunicipio())) {
			return this.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getNomMunicipio();
		} else {
			return "";
		}
	}
	
	@Override
	public Long getId() {
		return new Long(this.ideImovel);
	}
	
	@Override
	public Imovel clone() throws CloneNotSupportedException {
		return (Imovel) super.clone();
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideImovel != null ? ideImovel.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Imovel)) {
			return false;
		}
		Imovel other = (Imovel) object;
		if ((this.ideImovel == null && other.ideImovel != null) || (this.ideImovel != null && !this.ideImovel.equals(other.ideImovel))) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideImovel);
	}
	
	@XmlTransient
	public Collection<Empreendimento> getEmpreendimentoCollection() {
		return empreendimentoCollection;
	}
	
	@XmlTransient
	public Collection<PessoaImovel> getPessoaImovelCollection() {
		return pessoaImovelCollection;
	}
	
	@XmlTransient
	public Collection<RequerimentoImovel> getRequerimentoImovelCollection() {
		return requerimentoImovelCollection;
	}

	public Collection<MotivoNotificacaoImovel> getMotivoNotificacaoImovelCollection() {
		return motivoNotificacaoImovelCollection;
	}

	public void setMotivoNotificacaoImovelCollection(
			Collection<MotivoNotificacaoImovel> motivoNotificacaoImovelCollection) {
		this.motivoNotificacaoImovelCollection = motivoNotificacaoImovelCollection;
	}

	public Collection<ArquivoProcesso> getArquivoProcessoCollection() {
		return arquivoProcessoCollection;
	}

	public void setArquivoProcessoCollection(
			Collection<ArquivoProcesso> arquivoProcessoCollection) {
		this.arquivoProcessoCollection = arquivoProcessoCollection;
	}

	public Collection<Florestal> getFlorestalCollection() {
		return florestalCollection;
	}

	public void setFlorestalCollection(Collection<Florestal> florestalCollection) {
		this.florestalCollection = florestalCollection;
	}

	public Usuario getIdeUsuarioExclusao() {
		return ideUsuarioExclusao;
	}

	public void setIdeUsuarioExclusao(Usuario ideUsuarioExclusao) {
		this.ideUsuarioExclusao = ideUsuarioExclusao;
	}
}