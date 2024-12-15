package br.gov.ba.seia.dto.identificarPapel;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.PapelSolicitacaoEnum;

/**
 * @author Alexandre Queiroz
 **/

public class SolicitacaoDTO  {

	private Pessoa solicitante;							/**	Usuario que faz a solicitação, podeendo ou não ser a pessoa que será beneficiada	*/
	private Pessoa requerente;							/** beneficado, quem recebe o beneficio 			*/
	private Usuario usuarioLogado;
	private PaginaEnum paginaEnum;						/**Qual pagina a sistema deve ir quando acabar?  	*/
	private PapelSolicitacaoEnum papelSolicitacaoEnum;  /**Qual o papel do usuario logado na soliictação?	*/
	private boolean isCadastroSimplificado;				/**Algumas funcionalidades podem fazer o cadastro simplificado, deixando campos antes obrigatorios como opcionais*/
	private boolean isCadastroCompleto;					/**Significa que o usuario preeencheu o minino para avancar;*/
	private boolean isUsuarioConvenio;

	public SolicitacaoDTO() {
		isCadastroSimplificado = false; 				/**O cadastro é por natureza completo, é preciso que o controller solicitante esplicite que o quer simplificado*/
		isCadastroCompleto = false;
		isUsuarioConvenio = false;
	}

	public void limparRequerente(){
		requerente = new Pessoa();
		requerente.setPessoaFisica(new PessoaFisica());
	}

	public PapelSolicitacaoEnum getPapelSolicitacaoEnum() {
		return papelSolicitacaoEnum;
	}

	public void setPapelSolicitacaoEnum(PapelSolicitacaoEnum papelSolicitacaoEnum) {
		this.papelSolicitacaoEnum = papelSolicitacaoEnum;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public PaginaEnum getPaginaEnum() {
		return paginaEnum;
	}

	public void setPaginaEnum(PaginaEnum paginaEnum) {
		this.paginaEnum = paginaEnum;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public boolean isUsuarioConvenio() {
		return isUsuarioConvenio;
	}

	public void setUsuarioConvenio(boolean isUsuarioConvenio) {
		this.isUsuarioConvenio = isUsuarioConvenio;
	}

	public boolean isCadastroSimplificado() {
		return isCadastroSimplificado;
	}

	public void setCadastroSimplificado(boolean isCadastroSimplificado) {
		this.isCadastroSimplificado = isCadastroSimplificado;
	}

	public boolean isCadastroCompleto() {
		return isCadastroCompleto;
	}

	public void setCadastroCompleto(boolean isCadastroCompleto) {
		this.isCadastroCompleto = isCadastroCompleto;
	}
}
