package br.gov.ba.seia.middleware.seia.model;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.util.Util;
/**
 * Classe modelo para leitura de  arquivo de retorno
 * @author 
 *
 */
public class LeituraArquivoRetornoDTO {

	private Pessoa usuarioLogado;
	private List<BoletoPagamentoRequerimento> listaBoleto;
	private List<Requerimento> listaRequerimento;
	private List<Processo> listaProcesso;
	private List<ProcessoReenquadramento> listaProcessoReenquadramento;
	private List<Requerimento> listaRequerimentoGerarProcesso;
	private List<ProcessoReenquadramento> listaProcessoReenquadramentoTramitar;
	private String caminhoArquivoRetorno;

	public LeituraArquivoRetornoDTO() {
		
	}

	public LeituraArquivoRetornoDTO(Pessoa usuarioLogado, String caminhoArquivoRetorno) {
		this.usuarioLogado = usuarioLogado;
		this.caminhoArquivoRetorno = caminhoArquivoRetorno;
	}

	public void addListaBoleto(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaBoleto == null) {
			listaBoleto = new ArrayList<BoletoPagamentoRequerimento>();
		}
		listaBoleto.add(Util.objectTransform(obj, BoletoPagamentoRequerimento.class));
	}
	
	public void addListaRequerimento(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaRequerimento == null) {
			listaRequerimento = new ArrayList<Requerimento>();
		}
		listaRequerimento.add(Util.objectTransform(obj, Requerimento.class));
	}
	
	public void addListaProcesso(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaProcesso == null) {
			listaProcesso = new ArrayList<Processo>();
		}
		listaProcesso.add(Util.objectTransform(obj, Processo.class));
	}

	public void addListaRequerimentoGerarProcesso(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaRequerimentoGerarProcesso == null) {
			listaRequerimentoGerarProcesso = new ArrayList<Requerimento>();
		}
		listaRequerimentoGerarProcesso.add(Util.objectTransform(obj, Requerimento.class));
		
	}
	
	public void addListaProcessoReenquadramento(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaProcessoReenquadramento == null) {
			listaProcessoReenquadramento = new ArrayList<ProcessoReenquadramento>();
		}
		listaProcessoReenquadramento.add(Util.objectTransform(obj, ProcessoReenquadramento.class));

	}
	
	public void addListaProcessoReenquadramentoTramitar(Object obj) throws InstantiationException, IllegalAccessException {
		if (listaProcessoReenquadramentoTramitar == null) {
			listaProcessoReenquadramentoTramitar = new ArrayList<ProcessoReenquadramento>();
		}
		listaProcessoReenquadramentoTramitar.add(Util.objectTransform(obj, ProcessoReenquadramento.class));
		
	}

	public List<BoletoPagamentoRequerimento> getListaBoleto() {
		return listaBoleto;
	}

	public void setListaBoleto(List<BoletoPagamentoRequerimento> listaBoleto) {
		this.listaBoleto = listaBoleto;
	}

	public List<Requerimento> getListaRequerimento() {
		return listaRequerimento;
	}

	public void setListaRequerimento(List<Requerimento> listaRequerimento) {
		this.listaRequerimento = listaRequerimento;
	}

	public List<Processo> getListaProcesso() {
		return listaProcesso;
	}

	public void setListaProcesso(List<Processo> listaProcesso) {
		this.listaProcesso = listaProcesso;
	}

	public List<ProcessoReenquadramento> getListaProcessoReenquadramento() {
		return listaProcessoReenquadramento;
	}

	public void setListaProcessoReenquadramento(List<ProcessoReenquadramento> listaProcessoReenquadramento) {
		this.listaProcessoReenquadramento = listaProcessoReenquadramento;
	}

	public List<Requerimento> getListaRequerimentoGerarProcesso() {
		return listaRequerimentoGerarProcesso;
	}

	public void setListaRequerimentoGerarProcesso(List<Requerimento> listaRequerimentoGerarProcesso) {
		this.listaRequerimentoGerarProcesso = listaRequerimentoGerarProcesso;
	}

	public List<ProcessoReenquadramento> getListaProcessoReenquadramentoTramitar() {
		return listaProcessoReenquadramentoTramitar;
	}

	public void setListaProcessoReenquadramentoTramitar(
			List<ProcessoReenquadramento> listaProcessoReenquadramentoTramitar) {
		this.listaProcessoReenquadramentoTramitar = listaProcessoReenquadramentoTramitar;
	}

	public String getCaminhoArquivoRetorno() {
		return caminhoArquivoRetorno;
	}

	public void setCaminhoArquivoRetorno(String caminhoArquivoRetorno) {
		this.caminhoArquivoRetorno = caminhoArquivoRetorno;
	}

	public Pessoa getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Pessoa usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

}