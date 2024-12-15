package br.gov.ba.seia.util.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author antonio.junior
 */
public class SecurityVoter implements AccessDecisionVoter<Object> {
	
	private static final Logger LOG = Logger.getLogger(SecurityVoter.class.getName());

	private static final String ACESSO_NEGADO = "/acessonegado.xhtml";
	private static final String ERRO = "/erro/erro.xhtml";
	private static final String NAVEGADOR = "/navegador.xhtml";
	private static final String VALIDAR_CERTIFICADO = "/validarCertificado.xhtml";
	private static final String LAI = "/lai.xhtml";
	private static final String LOGIN = "/login.xhtml";
	private static final String INDEX = "/index.jsp";
	private static final String HOME = "/home.xhtml";
	private static final String ALTERAR_SENHA = "/alterarSenha.xhtml";
	private static final String ALTERAR_SENHA_EXPIRADA = "/alterarSenhaExpirada.xhtml";
	private static final String ARQUIVO_TXT = "/827DBF18EEC04FCF0BB4CD6BDF0B9B5C.txt";
	private static final String CADASTRO = "/paginas/controle-acesso/usuarioExterno.xhtml";
	private static final String CADASTRO_PESSOA_FISICA = "/paginas/manter-pessoafisica/cadastro.xhtml?cadastroIncompleto=true";
	private static final String STATIC_RESOURCES = "/resources";
	private static final String DYNAMIC_RESOURCES = "/javax.faces.resource";
	private static final String WS = "/sse";
	private static final String USUARIO_INATIVADO = "/usuarioInativado.xhtml";
	private static final String DOWNLOAD_PORTARIA = "/portaria/download?token=01b8b96dcc3a00951f5ec76830b147fb";
	private static final String DOWNLOAD_AVISO_CAR_LOTE = "/aviso-car-lote/";
	private static final String PROTOTIPO_ANALISE_TECNICA = "/paginas/manter-processo/analise-tecnica/analiseTecnica.xhtml";
	private static final String PROTOTIPO_APROVAR_ANALISE_TECNICA = "/paginas/manter-processo/analise-tecnica/aprovarAnaliseTecnica.xhtml";
	private static final String DOWNLOAD = "/download/";
	private static final String SINAFLOR = "/paginas/manter-sinaflor/consulta.xhtml";
	
	private final SecurityService service = SecurityService.getInstance();
	
	@SuppressWarnings("rawtypes")
	@Override
	public int vote(Authentication auth, Object pFilterInvocation, Collection arg2) {
		FilterInvocation pPaginaRequisitada = (FilterInvocation) pFilterInvocation;
		String pagina = pPaginaRequisitada.getRequestUrl();
		
		try {
			
			/* se o usuario esta logado a pagina de login nao e exibida,
			 * o usuario e redirecionado para a tela configurada ou tela inicial
			 */
			if(isLoginOrIndex(pagina) && auth.getPrincipal() instanceof SecurityUser) {
				SecurityUser securityUser = (SecurityUser) auth.getPrincipal();
				if(Util.isNullOuVazio(securityUser.getItens())){
					securityUser.setItens(service.getUserItens(securityUser));
				}
				
				if(Util.isNullOuVazio(securityUser.getUrls())){
					securityUser.setUrls(service.getUserUrls(securityUser));
				} 
				
				if (!service.verificarCadastroCompletoUsuario(securityUser.getUsuario())) {
//					ContextoUtil.getContexto().setPessoaFisica(securityUser.getUsuario().getPessoaFisica());
					securityUser.setCadastroCompleto(false);
					pPaginaRequisitada.getResponse().sendRedirect(pPaginaRequisitada.getRequest().getContextPath() + CADASTRO_PESSOA_FISICA);
				} else {
					securityUser.setCadastroCompleto(true);
					
					if(verificaSenhaExpirada(securityUser)) {
						String url = ALTERAR_SENHA_EXPIRADA;
						pPaginaRequisitada.getResponse().sendRedirect(pPaginaRequisitada.getRequest().getContextPath() + url);
						
					} else if(verificaUsuarioInativado(securityUser)) {
						String url = USUARIO_INATIVADO;
						pPaginaRequisitada.getResponse().sendRedirect(pPaginaRequisitada.getRequest().getContextPath() + url);
						
					} else {
						
						String url = ((SecurityUser) auth.getPrincipal()).getUrlInicial();
						if (url == null || url.isEmpty()) {
							url = HOME;
						}
						
						pPaginaRequisitada.getResponse().sendRedirect(pPaginaRequisitada.getRequest().getContextPath() + url);
					}
				}
				
				return ACCESS_GRANTED;
			}
			
			if (isFreePages(pagina) || isResource(pagina ) ||isWebService(pagina)) {
				return ACCESS_GRANTED;
			}
			
			if (auth.getPrincipal() instanceof SecurityUser) {
				if(isHome(pagina) || isAlterarSenha(pagina) || isAlterarSenhaExpirada(pagina) || isUsuarioInativado(pagina)) {
					return ACCESS_GRANTED;
				}
				
				SecurityUser user = (SecurityUser) auth.getPrincipal();
				
				if (canAccessPage(pagina, user)) {
					return ACCESS_GRANTED;
				} else {
					// Se chegou aqui o usuario nao esta com acesso a pagina
					// Se for uma requicao AJAX, exibe uma mensagem de erro
					if(isAjax(pPaginaRequisitada.getRequest())) {
						return ACCESS_GRANTED;
					}
					
					// Se for requisicao normal redireciona para pagina de erro.
					try {
						pPaginaRequisitada.getResponse().sendRedirect(pPaginaRequisitada.getRequest().getContextPath() + ACESSO_NEGADO);
						LOG.severe("Acesso negado - Perfil: " + user.getIdePerfil() + " URL: " + pagina);
						return ACCESS_DENIED;
					} catch (IOException e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
						throw Util.capturarException(e,Util.SEIA_EXCEPTION);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
		LOG.severe("Acesso negado - Usuário não logado - URL: " + pagina);
		// Nega o acesso por padrao
		return ACCESS_DENIED;
	}
	
	public boolean verificaSenhaExpirada(SecurityUser securityUser) throws Exception {
		
		Usuario user = securityUser.getUsuario();
		
		if(user != null && user.getDtcUltimaSenha() != null
				&& user.getIdePerfil() != null 
				&& user.getIdePerfil().getIdePerfil() != PerfilEnum.USUARIO_EXTERNO.getId()) {
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, -(service.obterValorPorParametro(ParametroEnum.MAX_DIAS_EXPIRACAO_SENHA)));
			
			if ((user.getDtcUltimaSenha().compareTo(calendar.getTime()) <= 0)) { 
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean verificaUsuarioInativado(SecurityUser securityUser) {
		
		Usuario user = securityUser.getUsuario();
		
		if(user != null && user.getIndExcluido()) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isAlterarSenha(String pagina) {
		return ALTERAR_SENHA.equals(pagina);
	}
	
	private boolean isAlterarSenhaExpirada(String pagina) {
		return ALTERAR_SENHA_EXPIRADA.equals(pagina);
	}
	
	private boolean isUsuarioInativado(String pagina) {
		return USUARIO_INATIVADO.equals(pagina);
	}

	private boolean isAjax(HttpServletRequest request) {
		   return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		}
	
	private boolean canAccessPage(String pPagina, SecurityUser pUser) throws Exception {
		return service.canAccessUrl(pUser, pPagina);
	}
	
	private boolean isHome(String pPagina) {
		return HOME.equals(pPagina);
	}

	private boolean isLoginOrIndex(String pPagina) {
		return  pPagina != null && (pPagina.startsWith(LOGIN) || INDEX.equals(pPagina));
	}
	
	private boolean isFreePages(String pPagina) {
		return  pPagina != null && (pPagina.startsWith(LOGIN)
				|| INDEX.equals(pPagina)|| VALIDAR_CERTIFICADO.equals(pPagina) || ERRO.equals(pPagina) || ACESSO_NEGADO.equals(pPagina) 
				|| CADASTRO.equals(pPagina)	|| NAVEGADOR.equals(pPagina)) || LAI.equals(pPagina) || pPagina.contains("favicon.ico")|| ARQUIVO_TXT.equals(pPagina) 
				|| ALTERAR_SENHA_EXPIRADA.equals(pPagina) || USUARIO_INATIVADO.equals(pPagina) || PROTOTIPO_ANALISE_TECNICA.equals(pPagina) 
				|| PROTOTIPO_APROVAR_ANALISE_TECNICA.equals(pPagina)
				|| DOWNLOAD_PORTARIA.equals(pPagina)
				|| DOWNLOAD_AVISO_CAR_LOTE.equals(pPagina)
				|| DOWNLOAD.equals(pPagina)
				|| SINAFLOR.equals(pPagina)
		;
	}
	
	private boolean isWebService(String pPagina) {
	
		return pPagina.startsWith(WS);
	}
	
	private boolean isResource(String pPagina) {
		
		return pPagina.startsWith(STATIC_RESOURCES) || pPagina.startsWith(DYNAMIC_RESOURCES) ;
	}

	@Override
	public boolean supports(ConfigAttribute arg0) {
		return true;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class arg0) {
		return true;
	}
}