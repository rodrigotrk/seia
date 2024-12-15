package br.gov.ba.seia.dto;

import java.util.Date;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.util.Util;

public class RequerimentoUnicoDTO implements Comparable<RequerimentoUnicoDTO>{

	private RequerimentoUnico requerimentoUnico;
	private Pessoa pessoa;
	private Empreendimento empreendimento;
	private StatusRequerimento statusRequerimento;
	private Lac lac;
	private Fce fce;
	private Pessoa pessoaTramitacao;



	private boolean visualizar = false;
	private boolean efetuandoEnquadramento = false;
	private String urlVoltar = "/paginas/novo-requerimento/consulta.xhtml";
	private boolean dla = false;

	public RequerimentoUnicoDTO(RequerimentoUnico requerimentoUnico, Pessoa pessoa, Empreendimento empreendimento, StatusRequerimento statusRequerimento, Lac lac) {
		this.requerimentoUnico = requerimentoUnico;
		this.pessoa = pessoa;
		this.empreendimento = empreendimento;
		this.statusRequerimento = statusRequerimento;
		this.lac = lac;
	}

	public RequerimentoUnicoDTO(Object[] resultElement) {
		this.requerimentoUnico =  new RequerimentoUnico();

		this.requerimentoUnico.setIdeRequerimentoUnico((Integer) resultElement[0]);
		this.requerimentoUnico.setIndDla((Boolean) resultElement[1]);

		this.requerimentoUnico.setRequerimento(new Requerimento((Integer) resultElement[4],(String) resultElement[3],(Date) resultElement[2]));
		this.requerimentoUnico.getRequerimento().setIdeOrgao(new Orgao((Integer) resultElement[5],(Integer) resultElement[6]));

		this.empreendimento = new Empreendimento((Integer)resultElement[10],(String)resultElement[11],(Boolean)resultElement[12]);

		String nomPessoaFisica = (String)resultElement[9];
		String nomPessoaJuridica = (String)resultElement[8];
		Integer idePessoa = (Integer)resultElement[7];
		if(!Util.isNullOuVazio(nomPessoaFisica)) {
			this.pessoa = new Pessoa(idePessoa, new PessoaFisica(idePessoa,nomPessoaFisica,(String) resultElement[21]));
		} else {
			this.pessoa = new Pessoa(idePessoa, new PessoaJuridica(idePessoa,nomPessoaJuridica,(String) resultElement[21]));
		}

		this.statusRequerimento = (StatusRequerimento) resultElement[13] ;

		Integer idePessoaTramitacao = (Integer)resultElement[15];
		String nomPessoaFisicaTramitacao = (String)resultElement[16];
		String nomPessoaJuridicaTramitacao = (String)resultElement[17];

		if(!Util.isNullOuVazio(nomPessoaFisicaTramitacao)) {
			this.pessoaTramitacao = new Pessoa(idePessoaTramitacao, new PessoaFisica(idePessoa,nomPessoaFisicaTramitacao));
		} else {
			this.pessoaTramitacao = new Pessoa(idePessoaTramitacao, new PessoaJuridica(idePessoa,nomPessoaJuridicaTramitacao));
		}

		this.lac = new Lac((Integer) resultElement[14]) ;
		this.lac.setIdeDocumentoObrigatorio(new DocumentoObrigatorio((Integer) resultElement[18])) ;

		this.requerimentoUnico.setIdePorte(new Porte((Integer) resultElement[19],(String) resultElement[20]));

		Municipio municipio = null;
		if(!Util.isNull(resultElement[22])){
			municipio = new Municipio((Integer)resultElement[22],(String)resultElement[23],(Boolean)resultElement[24]);
		}else{
			municipio = new Municipio((Integer)resultElement[25],(String)resultElement[26],(Boolean)resultElement[27]);
		}

		this.empreendimento.setMunicipio(municipio);

		this.requerimentoUnico.getRequerimento().setIndEstadoEmergencia((Boolean)resultElement[28]);

		this.fce = new Fce(((Integer) resultElement[29]));
		this.fce.setIdeDocumentoObrigatorio(new DocumentoObrigatorio((Integer) resultElement[30]));
	}

	public RequerimentoUnicoDTO(RequerimentoUnico requerimentoUnico, Pessoa pessoa, Empreendimento empreendimento, StatusRequerimento statusRequerimento, Lac lac,Orgao orgao) {
		this.requerimentoUnico = requerimentoUnico;
		this.pessoa = pessoa;
		this.empreendimento = empreendimento;
		this.statusRequerimento = statusRequerimento;
		this.lac = lac;
		if(!Util.isNull(this.requerimentoUnico.getRequerimento())){
			this.requerimentoUnico.getRequerimento().setIdeOrgao(orgao);
		}
	}

	public RequerimentoUnicoDTO(RequerimentoUnico requerimentoUnico, Pessoa pessoa, Empreendimento empreendimento, StatusRequerimento statusRequerimento, Lac lac,Pessoa pessoaTramitacao) {
		this.requerimentoUnico = requerimentoUnico;
		this.pessoa = pessoa;
		this.empreendimento = empreendimento;
		this.statusRequerimento = statusRequerimento;
		this.lac = lac;
		this.pessoaTramitacao = pessoaTramitacao;
	}

	public RequerimentoUnicoDTO() {

	}

	public RequerimentoUnicoDTO(Integer ideRequerimento) {
		this.requerimentoUnico = new RequerimentoUnico(ideRequerimento);
	}

	public RequerimentoUnico getRequerimentoUnico() {
		return requerimentoUnico;
	}

	public void setRequerimentoUnico(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public StatusRequerimento getStatusRequerimento() {
		return statusRequerimento;
	}

	public void setStatusRequerimento(StatusRequerimento statusRequerimento) {
		this.statusRequerimento = statusRequerimento;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean pVisualizar) {
		visualizar = pVisualizar;
	}

	public boolean isEfetuandoEnquadramento() {
		return efetuandoEnquadramento;
	}

	public void setEfetuandoEnquadramento(boolean efetuandoEnquadramento) {
		this.efetuandoEnquadramento = efetuandoEnquadramento;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	public String getUrlVoltar() {
		return urlVoltar;
	}

	public void setUrlVoltar(String urlVoltar) {
		this.urlVoltar = urlVoltar;
	}

	public boolean isDla() {
		return dla;
	}

	public void setDla(boolean dla) {
		this.dla = dla;
	}

	public Pessoa getPessoaTramitacao() {
		return pessoaTramitacao;
	}

	public void setPessoaTramitacao(Pessoa pessoaTramitacao) {
		this.pessoaTramitacao = pessoaTramitacao;
	}

	@Override
	public int compareTo(RequerimentoUnicoDTO arg0) {
		if(this.requerimentoUnico.getIdeRequerimentoUnico() != null && arg0.getRequerimentoUnico().getIdeRequerimentoUnico() != null){
			if(arg0.getRequerimentoUnico().getIdeRequerimentoUnico() < this.requerimentoUnico.getIdeRequerimentoUnico()){
				return -1;
			}else if(arg0.getRequerimentoUnico().getIdeRequerimentoUnico() > this.requerimentoUnico.getIdeRequerimentoUnico()){
				return 1;
			}else{
				return 0;
			}
		}else{
			return 0;
		}

	}

	public Fce getFce() {
		return fce;
	}

	public void setFce(Fce fce) {
		this.fce = fce;
	}
}