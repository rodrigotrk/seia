package br.gov.ba.seia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.enumerator.SefazCodigoReceitaEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.UsuarioAutorizacaoGeobahiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("menuSeiaController")
@ViewScoped
public class MenuSeiaController {

	@EJB
    private UsuarioAutorizacaoGeobahiaService usuarioAutorizacaoGeobahiaService;
	
	private List<Pauta> listaPautaAreaComAcessoConcedido;
	private List<Pauta> listaPautaGestorComAcessoConcedido;
	
	@PostConstruct
	public void init(){
	    listaPautaAreaComAcessoConcedido = ContextoUtil.getContexto().getListaPautaAreaComAcessoConcedido();
	    listaPautaGestorComAcessoConcedido = ContextoUtil.getContexto().getListaPautaGestorComAcessoConcedido();
	    if (Util.isNullOuVazio(listaPautaAreaComAcessoConcedido)==false) {
	        Pauta primeiraPauta = listaPautaGestorComAcessoConcedido.get(0);
	        listaPautaGestorComAcessoConcedido.clear();
	        listaPautaGestorComAcessoConcedido.add(primeiraPauta);
	    }
	}
	
	public String abrirPautaArea(Pauta pauta) {
		SessaoUtil.adicionarObjetoSessao("pauta", pauta);
		return "/paginas/manter-processo/pautaArea.xhtml";
	}
	
	public String abrirPautaGestor(Pauta pauta) {
		SessaoUtil.adicionarObjetoSessao("pauta", pauta);
		return "/paginas/manter-processo/pautaGestor.xhtml";
		
	}

	public String getUrlPerfilTecnico() {
        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("acao", "view");
        parametros.put("amp;token", usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahiaCefir());
        try {
            return URLEnum.CAMINHO_GEOBAHIA.getUrl(parametros);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
	
	public String getUrlSeiaMonitoramento() {
		return URLEnum.SEIA_MONITORAMENTO.toString();
	}
	
	public String getUrlValidarCertificado() {
		return URLEnum.VALIDAR_CERTIFICADO.toString();
	}
	
	public String abrirTelaBaixarDae(SefazCodigoReceitaEnum codigoSefaz) {
		SessaoUtil.adicionarObjetoSessao("codigoSefaz", codigoSefaz.getId());
		return "/paginas/manter-cerh/baixar-dae/baixar-dae.xhtml?faces-redirect=true";
		
	}

	public List<Pauta> getListaPautaAreaComAcessoConcedido() {
		return listaPautaAreaComAcessoConcedido;
	}

	public List<Pauta> getListaPautaGestorComAcessoConcedido() {
		return listaPautaGestorComAcessoConcedido;
	}

	
}
