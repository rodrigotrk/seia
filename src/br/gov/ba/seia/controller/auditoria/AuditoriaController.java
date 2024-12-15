package br.gov.ba.seia.controller.auditoria;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.gov.ba.seia.entity.auditoria.HistCampo;
import br.gov.ba.seia.entity.auditoria.HistTabela;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
/**
 * Classe controller da classe auditoria.
 * @author 
 *
 */
@Named("auditoriaController")
@ViewScoped
public class AuditoriaController {
	
	@Inject
	private AuditoriaFacade auditoriaFacade;
	
	private HistTabela tabelaFiltro;
	
	private HistCampo campoTabelaFiltro;
	
	/**
	 * 
	 * @return obtenção do historico
	 * @throws Exception
	 */
	public List<HistValor> getListHistorico() throws Exception {
		return auditoriaFacade.obterHistorico(tabelaFiltro, campoTabelaFiltro);
	}

	public HistCampo getCampoTabelaFiltro() {
		return campoTabelaFiltro;
	}

	public void setCampoTabelaFiltro(HistCampo campoTabelaFiltro) {
		this.campoTabelaFiltro = campoTabelaFiltro;
	}

	public HistTabela getTabelaFiltro() {
		return tabelaFiltro;
	}

	public void setTabelaFiltro(HistTabela tabelaFiltro) {
		this.tabelaFiltro = tabelaFiltro;
	}
	/**
	 * 
	 * @return tabelas
	 */
	public List<HistTabela> getListTabela()  {
		return auditoriaFacade.obterTabelas();
	}
	/**
	 * 
	 * @return campos  da tabela
	 */
	public List<HistCampo> getListCampoTabela() {
		return auditoriaFacade.obterCamposTabela(tabelaFiltro);
	}
	
}
