package br.gov.ba.seia.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.ba.seia.util.Util;

/**
 * Servlet Filter implementation class SeiaFilter
 */
@WebFilter({"/paginas/manter-pessoafisica/*", "/paginas/manter-pessoajuridica/*", "/paginas/identificar-papel/*"})
public class SeiaFilter implements Filter {

	public SeiaFilter() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		
		if(!Util.isNullOuVazio(servletRequest.getContentType()) && !servletRequest.getContentType().isEmpty()) {
			chain.doFilter(request, response);
		} else {
			servletResponse.sendRedirect(servletRequest.getContextPath().concat("/home.xhtml"));
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

}
