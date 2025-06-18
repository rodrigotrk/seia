package br.gov.ba.seia.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.enumerator.PapelSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.MetodoUtil;

@ViewScoped
@Named("identificarPapelSolicitacaoController")
public class IdentificarPapelSolicitacaoController extends IdentificarPapelSolicitacaoBaseController{
	//localhost:8080/paginas/identificar-papel-solicitacao/identificar-papel-solicitacao.xhtml
	
	@PostConstruct
	public void init(){
		super.inicializarVariaveis();
		super.inicializarSolicitacao();
	}
	
	public void salvar(){}
	
	public void validar(){}

	public void carregar(){}
	

	public void selecionarPapelSolicitacao(){
		super.selecionarPapelSolicitacao();
	}
	
	public void receberSolicitante(Pessoa pessoa){
		super.solicitacao.setSolicitante(pessoa);
		Html.atualizar("identificarPapelSolicitacao");
	}
	
	public List<PapelSolicitacaoEnum> getPapelSolicitacaoEnum(){
		return Arrays.asList(PapelSolicitacaoEnum.values());
	} 
	
	public boolean getPodeSelecionarRequerente(){
		return true; 
	}
	
	

	public void limpar(){
		super.limpar();
	}

	public void buscarRequerente(){
		super.buscarRequerente();
	}

	
	
	
	
	/*Listas*/
	public List<TipoIdentificacao> getTipoIdentificacaoList(){
		return getFacade().listarTipoIdentificacao();
	}
	
	public List<OrgaoExpedidor> getOrgaoExpedidorList(){
		return getFacade().listarOrgaoExpedidor();
	}
	
	public List<Estado> getEstadoList(){
		return getFacade().listarEstado();
	}
	
	
	
	
	public SolicitacaoDTO getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SolicitacaoDTO solicitacao) {
		this.solicitacao = solicitacao;
	}

	public MetodoUtil getMetodoSelecionarSolicitante() {
		return metodoSelecionarSolicitante;
	}

	public void setMetodoSelecionarSolicitante(MetodoUtil metodoSelecionarSolicitante) {
		this.metodoSelecionarSolicitante = metodoSelecionarSolicitante;
	}
	
}
	