package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;
import br.gov.ba.ws.entity.UsuarioDispositivo;
import flexjson.JSON;

@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsc_login"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdePessoaFisica", query = "SELECT u FROM Usuario u WHERE u.idePessoaFisica = :idePessoaFisica"),
    @NamedQuery(name = "Usuario.findByDscLogin", query = "SELECT u FROM Usuario u WHERE u.dscLogin = :dscLogin"),
    @NamedQuery(name = "Usuario.findByDscSenha", query = "SELECT u FROM Usuario u WHERE u.dscSenha = :dscSenha"),
    @NamedQuery(name = "Usuario.findByIndExcluido", query = "SELECT u FROM Usuario u WHERE u.indExcluido = :indExcluido"),
    @NamedQuery(name = "Usuario.findByDtcCriacao", query = "SELECT u FROM Usuario u WHERE u.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Usuario.findByDtcExclusao", query = "SELECT u FROM Usuario u WHERE u.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Usuario.findByIndTipoUsuario", query = "SELECT u FROM Usuario u WHERE u.indTipoUsuario = :indTipoUsuario"),
    @NamedQuery(name = "Usuario.updateSenha", query = "UPDATE Usuario u SET u.dscSenha = :dscSenha, u.dtcUltimaSenha = :dtcUltimaSenha, u.idePessoaFisicaUsuario = :idePessoaFisicaUsuario, u.caminhoRequisicao = :caminhoRequisicao, u.enderecoIp = :enderecoIp  WHERE u.idePessoaFisica = :idePessoaFisica AND u.dscSenha = :senhaAntiga")})
public class Usuario implements Serializable, br.gov.ba.seia.interfaces.Auditoria {
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fk_usuario_pessoa_fisica")    
    @SequenceGenerator(name="fk_usuario_pessoa_fisica", sequenceName="fk_usuario_pessoa_fisica", allocationSize=1)
    @GenericGenerator(name="fk_usuario_pessoa_fisica", strategy="foreign", parameters=@Parameter(name="property", value="pessoaFisica"))
    @Column(name = "ide_pessoa_fisica", nullable = false)
    private Integer idePessoaFisica;
    
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 25)
    @Column(name = "dsc_login", nullable = false, length = 25)
    private String dscLogin;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 6, max = 32, message = "O campo senha exige no mí­nimo 8 caracteres.")
    @Column(name = "dsc_senha", nullable = false, length = 32)
    private String dscSenha;
    
	@Transient
    @Basic(optional = false)
    @Size(min = 8, max = 32, message = "O campo Confirmação da Senha exige no mínimo 8 caracteres.")
    private String dscConfirmacaoSenha;
    
	@Transient
    @Basic(optional = false)
    @Size(min = 6 , max = 32, message = "O campo Senha Antiga exige no mí­nimo 8 caracteres.")
    private String senhaAntiga;    
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_validacao", nullable = false)
    private boolean indValidacao;
    
	@Basic(optional = false)
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_tipo_usuario", nullable = false)
    private boolean indTipoUsuario; //true = usuario interno
	
    @Column(name = "dtc_ultimo_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcUltimoLogin;
    
    @Column(name = "dtc_ultima_senha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcUltimaSenha;
    
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica pessoaFisica;
    
	@JoinColumn(name = "ide_perfil", referencedColumnName = "ide_perfil", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Perfil idePerfil;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.LAZY)
    private Collection<UsuarioDispositivo> usuarioDispositivoCollection;
	
	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name="caminho_requisicao")
	private String caminhoRequisicao;
	
	@Column(name="endereco_ip")
	private String enderecoIp;

    public Usuario() {}
    
    public Usuario(Integer idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }
    
    public Usuario(Perfil idePerfil) {
        this.idePerfil = idePerfil;
    }
    
    public Usuario(Perfil idePerfil, boolean indTipoUsuario) {
        this.idePerfil = idePerfil;
        this.indTipoUsuario = indTipoUsuario;
    }
    
    public Usuario(boolean indTipoUsuario) {
    	this.indTipoUsuario = indTipoUsuario;
    }
    
    public Usuario(String dscLogin) {
    	this.dscLogin = dscLogin;
    }
    
    public Usuario(Integer idePessoaFisica, String dscLogin, String dscSenha, boolean indExcluido, Boolean indTipoUsuario, Date dtcCriacao) {
        this.idePessoaFisica = idePessoaFisica;
        this.dscLogin = dscLogin;
        this.dscSenha = dscSenha;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
        this.indTipoUsuario = indTipoUsuario;
    }

    public Integer getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(Integer idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    public String getDscLogin() {
        return dscLogin;
    }

    public void setDscLogin(String dscLogin) {
        this.dscLogin = dscLogin;
    }

    @JSON(include = false)
    public String getDscSenha() {
        return dscSenha;
    }

    public void setDscSenha(String dscSenha) {
    	if(!Util.isNullOuVazio(dscSenha)){
    		this.dscSenha = dscSenha;
    	}
    	else{
    		this.dscSenha = "";
    	}
    }

    @JSON(include = false)
    public String getDscConfirmacaoSenha() {
		return dscConfirmacaoSenha;
	}

	public void setDscConfirmacaoSenha(String dscConfirmacaoSenha) {
		this.dscConfirmacaoSenha = dscConfirmacaoSenha;
	}

	@JSON(include = false)
	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public boolean getIndValidacao() {
		return indValidacao;
	}

	public void setIndValidacao(boolean indValidacao) {
		this.indValidacao = indValidacao;
	}

	public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @JSON(include = false)
    public boolean getIndTipoUsuario() {
        return indTipoUsuario;
    }

    public void setIndTipoUsuario(boolean indTipoUsuario) {
        this.indTipoUsuario = indTipoUsuario;
    }

    @JSON(include = false)
    public PessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(PessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    @JSON(include = false)
    public Perfil getIdePerfil() {
        return idePerfil;
    }

    public void setIdePerfil(Perfil idePerfil) {
        this.idePerfil = idePerfil;
    }

    @Transient
    @JSON(include = false)
    public boolean isValidaSenha() {
    	Matcher m = Pattern.compile("[!@#$%&*?/]{1,}").matcher(getDscSenha()); 
		Matcher m2 = Pattern.compile("[A-Z]{1,}").matcher(getDscSenha());
		Matcher m3 = Pattern.compile("[0-9]{1,}").matcher(getDscSenha());
		
		if(!m.find() || !m2.find() || !m3.find()){
			MensagemUtil.erro("Sua senha deve conter pelo menos um caractere especial(!@#$%&*?/), uma letra maiúscula, um numeral e no mínimo de 8 caracteres.");
		}else if (getDscSenha().equals(getDscConfirmacaoSenha()) ){ 
    		return true;
    	}else{
    		MensagemUtil.erro("Favor informar senhas iguais!");
    	}
    	return false;
    }

    public boolean isUsuarioExterno() {
    	return !indTipoUsuario;    	
    }
    
    @JSON (include = false)
    public boolean isMP() {
    	return PerfilEnum.USUARIO_MP.getId().equals(this.getIdePerfil().getIdePerfil());    	
    }
    
    @JSON(include = false)
    public boolean isAtende() {
    	return PerfilEnum.ATENDENTE.getId().equals(this.getIdePerfil().getIdePerfil()) || PerfilEnum.ATENDENTE_CEFIR.getId().equals(this.getIdePerfil().getIdePerfil());    	    	
    }
    
    @JSON(include = false)
    public boolean isConveniado() {
    	return PerfilEnum.TECNICO_CONVENIADO.getId().equals(this.getIdePerfil().getIdePerfil());    	    	
    }
    
    @JSON(include = false)
    public boolean isUsuarioCTGA() {
    	return PerfilEnum.TEC_CTGA.getId().equals(this.getIdePerfil().getIdePerfil()) || PerfilEnum.COORD_CTGA.getId().equals(this.getIdePerfil().getIdePerfil());    	
    }
    
    @JSON (include = false)
    public boolean isPerfilCoordenador() {
    	return PerfilEnum.COORDENADOR.getId().equals(this.getIdePerfil().getIdePerfil());
    }
    
    @JSON (include = false)
    public boolean isPerfilDiretor() {
    	return PerfilEnum.DIRETOR.getId().equals(this.getIdePerfil().getIdePerfil());
    }
    
    public boolean isTecnico(){
		return PerfilEnum.TECNICO.getId().equals(this.getIdePerfil().getIdePerfil()); 
	}
    
    public boolean isTecnicoCTGA(){
    	return PerfilEnum.TEC_CTGA.getId().equals(this.getIdePerfil().getIdePerfil()); 
    }
    
    public boolean isTecnicoConveniado(){
    	return PerfilEnum.TECNICO_CONVENIADO.getId().equals(this.getIdePerfil().getIdePerfil()); 
    }
    
    public boolean isFinanceiro(){
		return PerfilEnum.FINANCEIRO.getId().equals(this.getIdePerfil().getIdePerfil());
	}
    
    public Collection<UsuarioDispositivo> getUsuarioDispositivoCollection() {
    	return usuarioDispositivoCollection;
    }
    public void setUsuarioDispositivoCollection(
    		Collection<UsuarioDispositivo> usuarioDispositivoCollection) {
    	this.usuarioDispositivoCollection = usuarioDispositivoCollection;
    }
    
	public Date getDtcUltimoLogin() {
		return dtcUltimoLogin;
	}
	
	public void setDtcUltimoLogin(Date dtcUltimoLogin) {
		this.dtcUltimoLogin = dtcUltimoLogin;
	}
	
	public Date getDtcUltimaSenha() {
		return dtcUltimaSenha;
	}
	
	public void setDtcUltimaSenha(Date dtcUltimaSenha) {
		this.dtcUltimaSenha = dtcUltimaSenha;
	}
	
	
	public Integer getContadorCaractere(){
		
		Integer cont = 25;

		if(getDscLogin() == null){
			return 25;
		}
		
		return cont - getDscLogin().length();
		
	}
	
	public boolean isValidaLogin(){
		
		boolean retorno = true ;
		
		if(Util.isNullOuVazio(getDscLogin())){
			
			retorno = false;
			MensagemUtil.erro("O campo Login é de preenchimento obrigatório.");
		}else if(!getDscLogin().matches("^[\\._a-z0-9]+$")){
			
			retorno = false;
			MensagemUtil.erro("Por favor insira somente caracteres validos!");
		}
		
		return retorno;	
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idePessoaFisica != null ? idePessoaFisica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idePessoaFisica == null && other.idePessoaFisica != null) || (this.idePessoaFisica != null && !this.idePessoaFisica.equals(other.idePessoaFisica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(idePessoaFisica);
    }

	@Override
	public String getEnderecoIp() {
		return enderecoIp;
	}

	@Override
	public void setEnderecoIp(String enderecoIp) {
		this.enderecoIp = enderecoIp;
		
	}

	@Override
	public String getCaminhoRequisicao() {
		return caminhoRequisicao;
	}

	@Override
	public void setCaminhoRequisicao(String caminhoRequisicao) {
		this.caminhoRequisicao = caminhoRequisicao;
		
	}

	@Override
	public Integer getIdePessoaFisicaUsuario() {
		return idePessoaFisicaUsuario;
	}

	@Override
	public void setIdePessoaFisicaUsuario(Integer idePessoaFisicaUsuario) {
		this.idePessoaFisicaUsuario = idePessoaFisicaUsuario;
		
	}
	
	@Override
	public void capturarCamposAuditoria() {
		
		if(Util.isNullOuVazio(AuditoriaUtil.obterUsuario())){
			setIdePessoaFisicaUsuario(0);
		}else{
			
			setIdePessoaFisicaUsuario(AuditoriaUtil.obterUsuario().getIdePessoaFisica());
		}
		setEnderecoIp(AuditoriaUtil.obterIp());
		setCaminhoRequisicao(AuditoriaUtil.obterCaminhoPaginaRequisicao());
	}	
}