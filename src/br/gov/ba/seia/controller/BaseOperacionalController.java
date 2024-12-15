package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.BaseOperacional;
import br.gov.ba.seia.entity.BaseOperacionalControleAmbiental;
import br.gov.ba.seia.entity.BaseOperacionalControleAmbientalPK;
import br.gov.ba.seia.entity.BaseOperacionalServico;
import br.gov.ba.seia.entity.BaseOperacionalServicoPK;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.TipoControleAmbiental;
import br.gov.ba.seia.entity.TipoServicoBase;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.TipoControleAmbientalEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BaseOperacionalService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("baseOperacionalController")
@ViewScoped
public class BaseOperacionalController {

	@EJB
	private BaseOperacionalService baseOperacionalService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@Inject
	private LacTransporteController lacTransporteController;

	private BaseOperacional baseOperacional;

	private List<TipoServicoBase> listaServicosBase;
	private List<TipoServicoBase> listaServicosBaseSelecionados;
	private List<TipoControleAmbiental> listaSisControlBase;
	private List<TipoControleAmbiental> listaSisControlBaseSelecionados;
	private List<BaseOperacionalControleAmbiental> listaEfluentes;
	private Double barraOleosa;
	private Double oleoLubrificante;
	private short exibirResiduosSolidos;
	private boolean exibirEfluentes;
	private EmpreendimentoRequerimento empreendimentoRequerimento;

	@PostConstruct
	public void init() {

		Exception erro = null;
		try {
			carregarServicosBaseOperac();
			carregarSistemasControle();
			carregarAbaBaseOperacional();
			if (!Util.isNullOuVazio(lacTransporteController.getRequerimento())) {
				empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(lacTransporteController.getRequerimento());
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
		} finally {
			if (erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * @INFO Carrega todos os dados prenchidos anteriormente na aba.
	 */
	public void carregarAbaBaseOperacional() {

		Exception erro = null;
		try {

			if (Util.isNull(lacTransporteController.getRequerimento())) {
				return;
			}

			int adcSistemTrat = 0;
			listaServicosBaseSelecionados = new ArrayList<TipoServicoBase>();
			listaSisControlBaseSelecionados = new ArrayList<TipoControleAmbiental>();
			listaEfluentes = new ArrayList<BaseOperacionalControleAmbiental>();
			baseOperacional = baseOperacionalService.buscarBaseOperacionalByIdeLacTransporte(lacTransporteController.getLacTransporte());
			if (Util.isNull(baseOperacional)) {
				baseOperacional = new BaseOperacional();
			} else {
				List<BaseOperacionalServico> tempServico = baseOperacionalService.listarBaseOperacionalServicoByIdBaseOperacional(baseOperacional);
				List<BaseOperacionalControleAmbiental> tempSistema = baseOperacionalService.listarBaseOperacionalControleAmbientalByIdBaseOperacional(baseOperacional);
				if (!Util.isNull(listaServicosBaseSelecionados)) {
					listaServicosBaseSelecionados.clear();
					listaSisControlBaseSelecionados.clear();
					listaEfluentes.clear();
				}
				for (BaseOperacionalServico baseOpSer : tempServico) {
					listaServicosBaseSelecionados.add(baseOpSer.getIdeTipoServicoBase());
				}
				for (BaseOperacionalControleAmbiental baseOpCA : tempSistema) {
					if (baseOpCA.getIdeTipoControleAmbiental().getIdeTipoControleAmbiental() == TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId()) {
						baseOpCA.setDscEfluente("SAO");
						listaEfluentes.add(0, baseOpCA);
						barraOleosa = baseOpCA.getNumBorraOleosa();
					}
					if (baseOpCA.getIdeTipoControleAmbiental().getIdeTipoControleAmbiental() == TipoControleAmbientalEnum.SISTEMA_TRATAMENTO_EFLUENTE.getId()
							|| baseOpCA.getIdeTipoControleAmbiental().getIdeTipoControleAmbiental() == TipoControleAmbientalEnum.ENVIA_EFLUENTE_TRATAMENTO_EXTERNO.getId()) {
						adcSistemTrat++;
						if (adcSistemTrat == 2) {// Logo foi preenchida 'Sistema de Tramento...' na Grid e a mesma será carregada
							baseOpCA.setDscEfluente("Sistema de Tratamento");
							listaEfluentes.add(baseOpCA);
						}
					}
					if (baseOpCA.getIdeTipoControleAmbiental().getIdeTipoControleAmbiental() == TipoControleAmbientalEnum.COLETA_ARMAZENAMENTO_OLEO_LUBRIFICANTE.getId()) {
						oleoLubrificante = baseOpCA.getNumOleoLubrificante();
					}
					listaSisControlBaseSelecionados.add(baseOpCA.getIdeTipoControleAmbiental());
				}
				selectSisControlBase();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		} finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * @INFO Carrega os 'Sistemas de Controle Ambiental'
	 */
	public void carregarServicosBaseOperac() {
		Exception erro = null;
		try {
			listaServicosBase = baseOperacionalService.listarTipoBaseServico();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * @INFO Carrega os 'Serviços (a serem) realizados'
	 */
	public void carregarSistemasControle() {
		Exception erro = null;
		try {
			listaSisControlBase = baseOperacionalService.listarTipoControleAmbiental();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * @INFO Disponibiliza para o usuário a os efluentes para preenchimento de seus atributos.
	 */
	public void carregarEfluentes() {

		List<TipoControleAmbiental> temp = listaSisControlBaseSelecionados;
		if (Util.isNull(listaEfluentes)) {
			listaEfluentes = new ArrayList<BaseOperacionalControleAmbiental>();
		}
		if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId()))
				&& temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_TRATAMENTO_EFLUENTE.getId()))
				&& temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.ENVIA_EFLUENTE_TRATAMENTO_EXTERNO.getId()))) {
			containEfluente(new BaseOperacionalControleAmbiental("SAO"), 0);
			containEfluente(new BaseOperacionalControleAmbiental("Sistema de Tratamento"), null);
			exibirEfluentes = true;
		} else if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId()))) {// Contém SAO na lista - Vide selectSisControlBase()
			containEfluente(new BaseOperacionalControleAmbiental("SAO"), 0);
			listaEfluentes.remove(new BaseOperacionalControleAmbiental("Sistema de Tratamento"));
			exibirEfluentes = true;
		} else if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_TRATAMENTO_EFLUENTE.getId()))
				&& temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.ENVIA_EFLUENTE_TRATAMENTO_EXTERNO.getId()))) {
			containEfluente(new BaseOperacionalControleAmbiental("Sistema de Tratamento"), null);
			listaEfluentes.remove(new BaseOperacionalControleAmbiental("SAO"));
			exibirEfluentes = true;
		} else {
			listaEfluentes.remove(new BaseOperacionalControleAmbiental("SAO"));
			listaEfluentes.remove(new BaseOperacionalControleAmbiental("Sistema de Tratamento"));
			exibirEfluentes = false;
		}
	}

	/**
	 * @param efluente
	 * @param type indica se é 0 'SAO' ou 1 'Sistema de Tratamento'
	 * @INFO Verifica se a lista contém o efluente passado, se sim não adiciona o mesmo.
	 */
	public void containEfluente(BaseOperacionalControleAmbiental efluente, Integer type) {

		if (!listaEfluentes.contains(efluente)) {
			if (type == null) {
				listaEfluentes.add(efluente);
			} else {
				listaEfluentes.add(type, efluente);
			}

		}
	}

	/**
	 * @INFO Caso o 'exibirResiduosSolidos' seja 1, permite a inserção de 'Barra Oleosa' e exibe SAO em Efluente, caso seja 2, permite a inserção de 'Óleo lubrificante', caso seja 3 exibe os dois, por
	 *       fim caso seja 4 exibe 'Sistema de tratamento' em 'Efluente'.
	 */
	public void selectSisControlBase() {

		List<TipoControleAmbiental> temp = listaSisControlBaseSelecionados;
		
		if (!Util.isNullOuVazio(empreendimentoRequerimento) 
				&& !Util.isNullOuVazio(empreendimentoRequerimento.getIdeFaseEmpreendimento())
				&& !Util.isNullOuVazio(empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento()) 
				&& empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.OPERACAO.getId()) {
			
			if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId()))
					&& temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.COLETA_ARMAZENAMENTO_OLEO_LUBRIFICANTE.getId()))) {
				exibirResiduosSolidos = 3;
			} else if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId()))) {
				exibirResiduosSolidos = 1;
			} else if (temp.contains(new TipoControleAmbiental(TipoControleAmbientalEnum.COLETA_ARMAZENAMENTO_OLEO_LUBRIFICANTE.getId()))) {
				exibirResiduosSolidos = 2;
			} else {
				exibirResiduosSolidos = 0;
			}
			
			carregarEfluentes();
		}
	}

	public void salvarBaseOperacional() {
		Exception erro = null;
		try {
			if (lacTransporteController.salvarLacTransporte()) {
				if (validarBaseOperacional()) {
					baseOperacional.setIdeLacTransporte(lacTransporteController.getLacTransporte());
					baseOperacionalService.salvarBaseOperacional(baseOperacional);
					salvarBaseOperacionalServico();
					salvarBaseOperacionalControleAmbiental();
					JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
					RequestContext.getCurrentInstance().execute("lac_transporte.hide()");

					lacTransporteController.verificarExibicaoRelatorio();
					limparAbaBaseOperacional();
				}
			} else {
				lacTransporteController.voltarAba();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao salvar Base Operacional");
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void salvarBaseOperacionalControleAmbiental() throws Exception {

		baseOperacionalService.excluirBaseOperacionalControleAmbientalByIdeBaseOperac(baseOperacional);
		int adcSistemTrat = 0;
		BaseOperacionalControleAmbiental baseOperacionalControleAmbiental = new BaseOperacionalControleAmbiental();
		for (TipoControleAmbiental sistema : listaSisControlBaseSelecionados) {
			if (sistema.equals(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_SEPARADOR_AGUA_OLEO.getId())) && !Util.isNullOuVazio(empreendimentoRequerimento)
					&& empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.OPERACAO.getId()) {
				listaEfluentes.get(0).setNumBorraOleosa(barraOleosa);// 0 (ZERO) é a posição de SAO na lista de Efluentes, está posição é garantida, Vide containEfluente() e carregarEfluentes()
				listaEfluentes.get(0).setId(new BaseOperacionalControleAmbientalPK(baseOperacional.getIdeBaseOperacional(), sistema.getIdeTipoControleAmbiental()));
				listaEfluentes.get(0).setIdeBaseOperacional(baseOperacional);
				listaEfluentes.get(0).setIdeTipoControleAmbiental(sistema);
				baseOperacionalService.salvarBaseOperacionalControleAmbiental(listaEfluentes.get(0));
				System.gc();// indica ao 'garbage' do java que remova objetos criados não utilizados da memória, como 'new TipoControleAmbiental' e 'new BaseOperacionalControleAmbientalPK'
				continue;
			}
			if ((sistema.equals(new TipoControleAmbiental(TipoControleAmbientalEnum.SISTEMA_TRATAMENTO_EFLUENTE.getId())) || sistema.equals(new TipoControleAmbiental(
					TipoControleAmbientalEnum.ENVIA_EFLUENTE_TRATAMENTO_EXTERNO.getId())))
					&& !Util.isNullOuVazio(empreendimentoRequerimento)
					&& empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.OPERACAO.getId()) {
				adcSistemTrat++;
				if (adcSistemTrat == 2) {// Se ele selecionou os dois então adiciona este efluente. Obs. Os dados serão salvos apenas no segundo Sistema (ENVIA_EFLUENTE_TRATAMENTO_EXTERNO)
					int ind;
					if (listaEfluentes.size() == 1) {
						ind = 0;// Sistema de Tratamento está na posição 0 (ZERO) pois SAO não foi selecionado
					} else {
						ind = 1;// 1 (HUM) é a posição de 'Sistema de Tratamento' na lista de Efluentes, está posição é garantida, Vide containEfluente() e carregarEfluentes()
					}
					listaEfluentes.get(ind).setId(new BaseOperacionalControleAmbientalPK(baseOperacional.getIdeBaseOperacional(), sistema.getIdeTipoControleAmbiental()));
					listaEfluentes.get(ind).setIdeBaseOperacional(baseOperacional);
					listaEfluentes.get(ind).setIdeTipoControleAmbiental(sistema);
					listaEfluentes.get(ind).setNumBorraOleosa(null);
					baseOperacionalService.salvarBaseOperacionalControleAmbiental(listaEfluentes.get(ind));
					System.gc();// sugere ao 'garbage' do java que remova objetos criados não utilizados da memória, como 'new TipoControleAmbiental' e 'new BaseOperacionalControleAmbientalPK'
					continue;
				}
			}
			baseOperacionalControleAmbiental.setId(new BaseOperacionalControleAmbientalPK(baseOperacional.getIdeBaseOperacional(), sistema.getIdeTipoControleAmbiental()));
			baseOperacionalControleAmbiental.setIdeBaseOperacional(baseOperacional);
			baseOperacionalControleAmbiental.setIdeTipoControleAmbiental(sistema);
			if (sistema.equals(new TipoControleAmbiental(TipoControleAmbientalEnum.COLETA_ARMAZENAMENTO_OLEO_LUBRIFICANTE.getId()))) {
				baseOperacionalControleAmbiental.setNumOleoLubrificante(oleoLubrificante);
				baseOperacionalService.salvarBaseOperacionalControleAmbiental(baseOperacionalControleAmbiental);
				baseOperacionalControleAmbiental.setNumOleoLubrificante(null);// Set null pois os outros sistemas não têm Óleo lubrificante e baseOperacionalControleAmbiental não é novamente instanciada
				System.gc();
				continue;
			}
			baseOperacionalService.salvarBaseOperacionalControleAmbiental(baseOperacionalControleAmbiental);
			System.gc();
		}
	}

	public void salvarBaseOperacionalServico() throws Exception {

		BaseOperacionalServico baseOperacionalServico;
		baseOperacionalService.excluirBaseOperacionalServicoByIdeBaseOperac(baseOperacional);
		baseOperacionalServico = new BaseOperacionalServico();
		for (TipoServicoBase servico : listaServicosBaseSelecionados) {
			baseOperacionalServico.setId(new BaseOperacionalServicoPK(baseOperacional.getIdeBaseOperacional(), servico.getIdeTipoServicoBase()));
			baseOperacionalServico.setIdeBaseOperacional(baseOperacional);
			baseOperacionalServico.setIdeTipoServicoBase(servico);
			baseOperacionalService.salvarBaseOperacionalServico(baseOperacionalServico);
			System.gc();// indica ao 'garbage' do java que remova objetos criados não utilizados da memória, como 'new BaseOperacionalServicoPK'
		}

	}

	/**
	 * @return true se válido
	 * @INFO Valida todos os campos da aba Base Operacional conforme especificado nas Regras de Negócio SEIA - LAC's e FCE'S atualizadas até a presente data(15/07/2013).
	 */
	public boolean validarBaseOperacional() {

		// Vide selectSisControlBase() para saber o signficado dos valores 1,2,3 em alguma condições abaixo executadas (IF)
		boolean valido = true;
		if (!Util.isNullOuVazio(empreendimentoRequerimento) && empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.OPERACAO.getId()
				&& Util.isNull(baseOperacional.getDtcOperacao())) {
			valido = false;
			campoObrigatorio("Data do inicio da Operação");
		} else if ((!Util.isNullOuVazio(empreendimentoRequerimento) && empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.LOCALIZACAO.getId()
				|| (!Util.isNullOuVazio(empreendimentoRequerimento) && empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.IMPLANTACAO.getId()))
				&& Util.isNull(baseOperacional.getDtcOperacao())) {
			valido = false;
			campoObrigatorio("Data prevista para inicio da Operação");
		}
		if (Util.isNullOuVazio(listaServicosBaseSelecionados)) {
			valido = false;
			JsfUtil.addErrorMessage("É obrigatória a seleção de ao menos um serviço realizado na Base Operacional.");
		}
		if (Util.isNull(baseOperacional.getNumAreaTotal()) || baseOperacional.getNumAreaTotal() == 0) {
			valido = false;
			campoObrigatorio("Área Total(m²)");
		}
		if (Util.isNull(baseOperacional.getNumAreaConstruida()) || baseOperacional.getNumAreaConstruida() == 0) {
			valido = false;
			campoObrigatorio("Área Construida(m²)");
		} else if (!Util.isNull(baseOperacional.getNumAreaTotal()) && (baseOperacional.getNumAreaTotal().compareTo(baseOperacional.getNumAreaConstruida()) == -1)) {
			valido = false;
			JsfUtil.addErrorMessage("Área Construída não pode ser maior que Área Total.");
		}
		if (Util.isNullOuVazio(listaSisControlBaseSelecionados)) {
			valido = false;
			JsfUtil.addErrorMessage("É obrigatória a seleção de ao menos um Sistema de Controle Ambiental.");
		} else if (exibirResiduosSolidos == 3) {
			if (Util.isNull(barraOleosa) || barraOleosa == 0) {
				valido = false;
				campoObrigatorio("Borra oleosa gerada anualmente (t/ano)");
			}
			if (Util.isNull(oleoLubrificante) || oleoLubrificante == 0) {
				valido = false;
				campoObrigatorio("Óleo lubrificante usado gerado anualmente (t/ano)");
			}
		} else if (exibirResiduosSolidos == 1 && (Util.isNull(barraOleosa) || barraOleosa == 0)) {
			valido = false;
			campoObrigatorio("Borra oleosa gerada anualmente (t/ano)");
		} else if (exibirResiduosSolidos == 2 && (Util.isNull(oleoLubrificante) || oleoLubrificante == 0)) {
			valido = false;
			campoObrigatorio("Óleo lubrificante usado gerado anualmente (t/ano)");
		}
		if (!Util.isNull(listaEfluentes) && !Util.isNullOuVazio(empreendimentoRequerimento)
				&& empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento().equals( FaseEmpreendimentoEnum.OPERACAO.getId())) {
			if (listaEfluentes.indexOf(new BaseOperacionalControleAmbiental("SAO")) != -1) {
				int ind = listaEfluentes.indexOf(new BaseOperacionalControleAmbiental("SAO"));
				if (Util.isNull(listaEfluentes.get(ind).getNumVazaoMedia()) || listaEfluentes.get(ind).getNumVazaoMedia() == 0) {
					valido = false;
					campoObrigatorio("Vazão Média (m³) de SAO");
				}
				if (Util.isNull(listaEfluentes.get(ind).getNumDboMedia()) || listaEfluentes.get(ind).getNumDboMedia() == 0) {
					valido = false;
					campoObrigatorio("DBO média do SAO (mg/L) de SAO");
				}
				if (Util.isNull(listaEfluentes.get(ind).getNumOgMedio()) || listaEfluentes.get(ind).getNumOgMedio() == 0) {
					valido = false;
					campoObrigatorio("OeG médio do SAO (mg/L) de SAO");
				}
			}
			if (listaEfluentes.indexOf(new BaseOperacionalControleAmbiental("Sistema de Tratamento")) != -1) {
				int ind = listaEfluentes.indexOf(new BaseOperacionalControleAmbiental("Sistema de Tratamento"));
				if (Util.isNull(listaEfluentes.get(ind).getNumVazaoMedia()) || listaEfluentes.get(ind).getNumVazaoMedia() == 0) {
					valido = false;
					campoObrigatorio("Vazão Média (m³) de Sistema de tratamento");
				}
				if (Util.isNull(listaEfluentes.get(ind).getNumDboMedia()) || listaEfluentes.get(ind).getNumDboMedia() == 0) {
					valido = false;
					campoObrigatorio("DBO média do SAO (mg/L) de Sistema de tratamento");
				}
				if (Util.isNull(listaEfluentes.get(ind).getNumOgMedio()) || listaEfluentes.get(ind).getNumOgMedio() == 0) {
					valido = false;
					campoObrigatorio("OeG médio do SAO (mg/L) de Sistema de tratamento");
				}
			}
		}
		return valido;
	}

	/**
	 * @INFO Anula todos os campos da aba Base Operacional
	 */
	public void limparAbaBaseOperacional() {

		if (!Util.isNull(listaSisControlBaseSelecionados)) {
			listaSisControlBaseSelecionados.clear();
		}
		if (!Util.isNull(listaServicosBaseSelecionados)) {
			listaServicosBaseSelecionados.clear();
		}
		barraOleosa = null;
		oleoLubrificante = null;
		exibirEfluentes = false;
		exibirResiduosSolidos = 0;
		for (BaseOperacionalControleAmbiental efluente : listaEfluentes) {
			efluente.setNumDboMedia(null);
			efluente.setNumOgMedio(null);
			efluente.setNumVazaoMedia(null);
		}
		baseOperacional = new BaseOperacional();
	}

	/**
	 * @param str
	 * @return String concatenada
	 * @INFO Adiciona ao campo passado a informação de que o mesmo é obrigatório, conforme especificado nas Regras de Negócio SEIA - LAC's e FCE'S atualizadas até a presente data(29/07/2013).
	 */
	public static void campoObrigatorio(String str) {

		JsfUtil.addErrorMessage(str += " é de preenchimento obrigatório.");
	}

	public BaseOperacionalService getBaseOperacionalService() {

		return baseOperacionalService;
	}

	public void setBaseOperacionalService(BaseOperacionalService baseOperacionalService) {

		this.baseOperacionalService = baseOperacionalService;
	}

	public List<TipoServicoBase> getListaServicosBase() {

		return listaServicosBase;
	}

	public void setListaServicosBase(List<TipoServicoBase> listaServicosBase) {

		this.listaServicosBase = listaServicosBase;
	}

	public List<TipoServicoBase> getListaServicosBaseSelecionados() {

		return listaServicosBaseSelecionados;
	}

	public void setListaServicosBaseSelecionados(List<TipoServicoBase> listaServicosBaseSelecionados) {

		this.listaServicosBaseSelecionados = listaServicosBaseSelecionados;
	}

	public List<TipoControleAmbiental> getListaSisControlBase() {

		return listaSisControlBase;
	}

	public void setListaSisControlBase(List<TipoControleAmbiental> listaSisControlBase) {

		this.listaSisControlBase = listaSisControlBase;
	}

	public List<TipoControleAmbiental> getListaSisControlBaseSelecionados() {

		return listaSisControlBaseSelecionados;
	}

	public void setListaSisControlBaseSelecionados(List<TipoControleAmbiental> listaSisControlBaseSelecionados) {

		this.listaSisControlBaseSelecionados = listaSisControlBaseSelecionados;
	}

	public List<BaseOperacionalControleAmbiental> getListaEfluentes() {

		return listaEfluentes;
	}

	public void setListaEfluentes(List<BaseOperacionalControleAmbiental> listaEfluentes) {

		this.listaEfluentes = listaEfluentes;
	}

	public short getExibirResiduosSolidos() {

		return exibirResiduosSolidos;
	}

	public void setExibirResiduosSolidos(short exibirResiduosSolidos) {

		this.exibirResiduosSolidos = exibirResiduosSolidos;
	}

	public boolean isExibirEfluentes() {

		return exibirEfluentes;
	}

	public void setExibirEfluentes(boolean exibirEfluentes) {

		this.exibirEfluentes = exibirEfluentes;
	}

	public BaseOperacional getBaseOperacional() {

		return baseOperacional;
	}

	public void setBaseOperacional(BaseOperacional baseOperacional) {

		this.baseOperacional = baseOperacional;
	}

	public Double getBarraOleosa() {

		return barraOleosa;
	}

	public void setBarraOleosa(Double barraOleosa) {

		this.barraOleosa = barraOleosa;
	}

	public Double getOleoLubrificante() {

		return oleoLubrificante;
	}

	public void setOleoLubrificante(Double oleoLubrificante) {

		this.oleoLubrificante = oleoLubrificante;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {

		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {

		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}
}