package br.gov.ba.seia.util.security;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AcaoEnum;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.FuncionalidadeEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.SecaoEnum;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("security")
@RequestScoped
public class SecurityController {
	
	@EJB
	private ParametroService parametroService;
	@EJB
	private FuncionarioService funcionarioService;
	
	private final SecurityService securityService = SecurityService.getInstance();
	
	private SecurityUser user;
	
	private boolean cadastroCompleto;
	
	private boolean alteracaoSenhaExpirada;
	
	private boolean usuarioInativado;
	
	@PostConstruct
	public void init() {
		
		try {
			if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				String msg = (String) SessaoUtil.recuperarRemoverObjetoSessao("msgCadastroConcluido");			
				
				if(!Util.isNullOuVazio(msg)){
					
					JsfUtil.addSuccessMessage(msg);
				}
				
				if (principal instanceof UserDetails) {
					user = (SecurityUser) principal;
					
					if (user != null && user.getUsuario() != null && user.getUsuario().getIndExcluido()) {
						usuarioInativado = true;
						return;
					} else {
						
						alteracaoSenhaExpirada = verificaSenhaExpirada();
						
						if (Util.isNullOuVazio(user.getItens())) {
							user.setItens(securityService.getUserItens(user));
						}
						
						if (Util.isNullOuVazio(user.getUrls())) {
							user.setUrls(securityService.getUserUrls(user));
						}
						
						ContextoUtil.getContexto().verificarComunicacoes();
						
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Verifica se o usuário logado tem acesso ao item referenciado.
	 * @param key - item referenciado
	 * @return
	 */
	public boolean temAcesso(final String key) {
		try {
			return (securityService.canAccessUsingCache(user, key));
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	/**
	 * Verifica se o usuário logado tem acesso ao item referenciado.
	 * @param key - item referenciado
	 * @return
	 */
	public boolean temAcesso(final String key, AreaEnum areaEnum) {
		try {
			boolean permissao = securityService.canAccessUsingCache(user, key);
			if(permissao) {
				Funcionario funcionario = funcionarioService.buscarFuncionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica(), areaEnum);
				return !Util.isNull(funcionario);
			}
			return permissao;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean temAcesso(SecaoEnum secao, FuncionalidadeEnum funcionalidade, AcaoEnum acao) {
        try {
            
            String key = "";
            
            if(secao != null){
                key = secao.getId().toString();

                if(funcionalidade != null){
                    key += "." + funcionalidade.getId();
                    
                    if(acao != null){
                        key += "." + acao.getId();
                    }
                }
            } else {
                return false;
            }
            
            return (securityService.canAccessUsingCache(user, key));
        } catch (Exception e) {
            Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
        }
    }
	
	public boolean temAcesso(SecaoEnum secao, FuncionalidadeEnum funcionalidade, AcaoEnum acao, AreaEnum areaEnum) {
		try {
			boolean permissao = temAcesso(secao,funcionalidade,acao);
			
			if(permissao) {
				Funcionario funcionario = funcionarioService.buscarFuncionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica(), areaEnum);
				return !Util.isNull(funcionario);
			}
			return permissao;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	
	/**
	 * Verifica se o usuário logado tem o perfil passado como parametro.
	 * @param perfil - id do perfil do usuario
	 * @return
	 */
	public boolean temAcessoPorPerfil(String perfil) {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		if(Util.isNullOuVazio(usuarioLogado)){
			return false;
		}		
		return (usuarioLogado.getIdePerfil().getIdePerfil().intValue()==Integer.parseInt(perfil));
	}
	
	public boolean verificaSenhaExpirada() throws Exception {
		
		Usuario user = ContextoUtil.getContexto().getUsuarioLogado();
		
		if(user != null && user.getDtcUltimaSenha() != null
				&& user.getIdePerfil() != null 
				&& user.getIdePerfil().getIdePerfil() != PerfilEnum.USUARIO_EXTERNO.getId()) {
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, -(securityService.obterValorPorParametro(ParametroEnum.MAX_DIAS_EXPIRACAO_SENHA)));
			
			if ((user.getDtcUltimaSenha().compareTo(calendar.getTime()) <= 0)) { 
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean getCadastroCompleto() {
		Exception erro =null;
		try {
			if (!Util.isNull(user) && !cadastroCompleto) {
				cadastroCompleto = securityService.verificarCadastroCompletoUsuario(user.getUsuario());
				if (!cadastroCompleto) {
					ContextoUtil.getContexto().setPessoaFisica(user.getUsuario().getPessoaFisica());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return false;
	}
	
	public void setCadastroCompleto(boolean cadastroCompleto) {
		this.cadastroCompleto = cadastroCompleto;
	}

	public boolean isAlteracaoSenhaExpirada() {
		return alteracaoSenhaExpirada;
	}

	public void setAlteracaoSenhaExpirada(boolean alteracaoSenhaExpirada) {
		this.alteracaoSenhaExpirada = alteracaoSenhaExpirada;
	}

	public boolean isUsuarioInativado() {
		return usuarioInativado;
	}
	
	public void setUsuarioInativado(boolean usuarioInativado) {
		this.usuarioInativado = usuarioInativado;
	}
}