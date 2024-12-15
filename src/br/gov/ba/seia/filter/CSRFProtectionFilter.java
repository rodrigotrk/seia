package br.gov.ba.seia.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSRFProtectionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            // Inicialmente tente buscar diretamente o token sem prefixo
            String csrfToken = httpRequest.getParameter("csrfToken");

            // Se não encontrar, procure por parâmetros com prefixo
            if (csrfToken == null || csrfToken.isEmpty()) {
                Enumeration<String> parameterNames = httpRequest.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String paramName = parameterNames.nextElement();
                    if (paramName.endsWith(":csrfToken")) { // Verifica se o nome do parâmetro termina com ":csrfToken"
                        csrfToken = httpRequest.getParameter(paramName);
                        break;
                    }
                }
            }

            // Recupera o token da sessão
            String sessionToken = (String) httpRequest.getSession().getAttribute("csrfToken");


            // Validação do token
            if (csrfToken == null || !csrfToken.equals(sessionToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF Token inválido!");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
