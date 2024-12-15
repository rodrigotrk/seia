package br.gov.ba.ws.entity;

import java.util.Date;

import br.gov.ba.seia.util.Util;

public class ProcessoWS {

	private Integer ideProcesso;

	private String numProcesso;
	private Date dtcFormacao;
	private String nomMunicipio;
	private String nomPorte;
	private String nomRazaoSocial;
	private String dscStatusFluxo;
	private String dscStatusFluxoExterno;
	private String desEmail;
	private String sistema;
	private RequerimentoWS requerimento;
	
	
	
	
	public ProcessoWS(Integer ideProcesso, 
						String numProcesso, 
						Date dtcFormacao,
						String nomPorte, 
						String nomPessoa, 
						String nomRazaoSocial, 
						String dscStatusFluxo,
						String dscStatusFluxoExterno,
						String nomEmpreendimento, 
						String nomMunicipio, 
						String desEmail,
						String numRequerimento,
						Date dtcCriacao,
						Integer ideRequerimento,
						Integer ideEmpreedimento) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		this.nomPorte = nomPorte;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.dscStatusFluxo = dscStatusFluxo;
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
		this.nomMunicipio = nomMunicipio;
		this.desEmail = desEmail;
		this.requerimento = new RequerimentoWS(numRequerimento, nomEmpreendimento,dtcCriacao,ideRequerimento,ideEmpreedimento);
	}
	
	private void verificarNomeRequerente(String nomPessoa,String nomRazaoSocial) {
		if(!Util.isNullOuVazio(nomPessoa)){
			this.nomRazaoSocial = nomPessoa;
		}else{
			this.nomRazaoSocial = nomRazaoSocial;
		}
	}

	public ProcessoWS(Integer ideProcesso,
			String numProcesso, Date dtcFormacao,
			String nomPorte, String nomPessoa, 
			String nomRazaoSocial, String dscStatusFluxo,
			String dscStatusFluxoExterno, Integer ideRequerimento) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		this.nomPorte = nomPorte;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.dscStatusFluxo = dscStatusFluxo;
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
		this.requerimento = new RequerimentoWS(ideRequerimento);
	}
	
	public ProcessoWS(Integer ideProcesso,
			String numProcesso, Date dtcFormacao,
			String nomPorte, String nomPessoa, 
			String nomRazaoSocial, String dscStatusFluxo,
			String dscStatusFluxoExterno) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		this.nomPorte = nomPorte;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.dscStatusFluxo = dscStatusFluxo;
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
	}
	
	
	public ProcessoWS(Integer ideProcesso,
			String numProcesso, Date dtcFormacao,
			String nomPessoa, 
			String nomRazaoSocial, String dscStatusFluxo, Integer ideRequerimento
			) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.dscStatusFluxo = dscStatusFluxo;
		this.requerimento = new RequerimentoWS(ideRequerimento);
	}

	public ProcessoWS(Integer ideProcesso,
			String numProcesso, 
			Date dtcFormacao,
			String nomPessoa, 
			String nomRazaoSocial, 
			String dscStatusFluxo, 
			Integer ideRequerimento,
			Date dtcTramitacao
			) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.dscStatusFluxo = dscStatusFluxo;
		this.requerimento = new RequerimentoWS(ideRequerimento);
	}
	
	public ProcessoWS(String numProcesso, Date dtcFormacao, String sistema, String dscStatusFluxo) {
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		this.sistema = sistema;
		this.dscStatusFluxo = dscStatusFluxo;
	}
	
	/*
	 * Usado por:
	 * 	ServicoProcesso/consultaPublica
	 * em:
	 * 	ProcessoService.consultarPublicaWS(...)
	 */
	public ProcessoWS(Integer ideProcesso, 
			String numProcesso,
			Date dtcFormacao, 
			String nomPorte,
			String nomPessoa, 
			String nomRazaoSocial, 
			String dscStatusFluxo,
			String dscStatusFluxoExterno,
			Integer ideRequerimento,
			Date dtcTramitacao) {
		this.ideProcesso = ideProcesso;
		this.numProcesso = numProcesso;
		this.dtcFormacao = dtcFormacao;
		verificarNomeRequerente(nomPessoa,nomRazaoSocial);
		this.nomPorte = nomPorte;
		this.nomRazaoSocial = nomRazaoSocial;
		this.dscStatusFluxo = dscStatusFluxo;
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
		this.requerimento = new RequerimentoWS(ideRequerimento);
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
	public Date getDtcFormacao() {
		return dtcFormacao;
	}
	public void setDtcFormacao(Date dtcFormacao) {
		this.dtcFormacao = dtcFormacao;
	}
	public String getNomMunicipio() {
		if(!Util.isNullOuVazio(nomMunicipio)){
			return nomMunicipio;
		}
		return "-";
	}
	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}
	public String getNomPorte() {
		if(!Util.isNullOuVazio(nomPorte)){
			return nomPorte;
		}
		return "-";
	}
	public void setNomPorte(String nomPorte) {
		this.nomPorte = nomPorte;
	}
	public String getNomRazaoSocial() {
		return nomRazaoSocial;
	}
	public void setNomRazaoSocial(String nomRazaoSocial) {
		this.nomRazaoSocial = nomRazaoSocial;
	}
	public String getDscStatusFluxo() {
		return dscStatusFluxo;
	}
	public void setDscStatusFluxo(String dscStatusFluxo) {
		this.dscStatusFluxo = dscStatusFluxo;
	}
	public String getDscStatusFluxoExterno() {
		if(!Util.isNullOuVazio(dscStatusFluxoExterno)){
			return dscStatusFluxoExterno;
		}
		return dscStatusFluxo;
	}
	public void setDscStatusFluxoExterno(String dscStatusFluxoExterno) {
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
	}
	public RequerimentoWS getRequerimento() {
		return requerimento;
	}
	public void setRequerimento(RequerimentoWS requerimento) {
		this.requerimento = requerimento;
	}

	public String getDesEmail() {
		if(!Util.isNullOuVazio(desEmail)){
			return desEmail;
		}
		return "-";
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}

	public Long getDiasFormadoProcesso() {
		Long qtdDiasFormado = new Date().getTime() - dtcFormacao.getTime();
		return (qtdDiasFormado / 86400000L);
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
}
