package br.gov.ba.seia.entity.auditoria;

import java.util.Date;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Util;
/**
 * Classe Filtro da auditoria.
 * @author 
 *
 */
public class FiltroAuditoria {
	
	private Date dataModificacao;
	
	private Usuario usuarioAlteracao;	
	
	private String nomeUsuario;
	
	private HistCampo campo;

	private String campoAlterado;
	
	private HistValor valorAntigo;
	
	private String valorAntigoString;
	
	private HistValor valorNovo;
	
	private String valorNovoString;
	
	private Date dataInicio;
	
	private Date dataFim;
	


	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public HistCampo getCampo() {
		return campo;
	}

	public void setCampo(HistCampo campo) {
		this.campo = campo;
	}

	public HistValor getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(HistValor valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public HistValor getValorNovo() {
		return valorNovo;
	}

	public void setValorNovo(HistValor valorNovo) {
		this.valorNovo = valorNovo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getCampoAlterado() {
		return campoAlterado;
	}

	public void setCampoAlterado(String campoAlterado) {
		this.campoAlterado = campoAlterado;
	}

	public String getValorAntigoString() {
		return valorAntigoString;
	}

	public void setValorAntigoString(String valorAntigoString) {
		this.valorAntigoString = valorAntigoString;
	}

	public String getValorNovoString() {
		return valorNovoString;
	}

	public void setValorNovoString(String valorNovoString) {
		this.valorNovoString = valorNovoString;
	}
	
}
