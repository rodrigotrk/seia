package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.EspecieHibrido;
import br.gov.ba.seia.entity.FceAgroTipoConservacao;
import br.gov.ba.seia.entity.Mes;
import br.gov.ba.seia.entity.Silvicultura;
import br.gov.ba.seia.entity.TipoConservacaoSoloAgua;
import br.gov.ba.seia.entity.TipoDadoSilvicultura;
import br.gov.ba.seia.enumerator.TipoConservacaoSoloAguaEnum;
import br.gov.ba.seia.enumerator.TipoDadoSilviculturaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EspecieHibridoService;
import br.gov.ba.seia.service.MesService;
import br.gov.ba.seia.service.SilviculturaService;
import br.gov.ba.seia.service.TipoConservacaoSoloAguaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("silviculturaController")
@ViewScoped
public class SilviculturaController {
	
	@EJB
	private EspecieHibridoService especieHibridoService;
	@EJB
	private TipoConservacaoSoloAguaService tipoConservacaoSoloAguaService;
	@EJB
	private SilviculturaService silviculturaService;
	@EJB
	private MesService mesService;
	
	@Inject
	private AgrossilvopastorilController agrossilvopastorilController;
	
	private String nomEspecieHibridos;
	private List<TipoConservacaoSoloAgua> listaTipoConservSoloAgua;
	private List<TipoConservacaoSoloAgua> listaTipoConservSoloAguaSelecionados;
	private List<EspecieHibrido> listaEspecieHibrido;
	private List<EspecieHibrido> listaEspecieHibridoAll;
	private EspecieHibrido especieHibrido;
	private List<Silvicultura> listaSilvicultura;
	private List<Silvicultura> listaSilviculturaFlorestaProducao;
	private List<Silvicultura> listaSilviculturaEspecieReflores;
	private List<Silvicultura> listaSilviculturaIMA;
	private List<String> listaAno;
	private List<Mes> listaMes;
	private final Integer QTD_ANO = 51;
	private Integer anoAtual;
	
	public void init() {
		carregarPraticasConservSolo();
		carregarAno();
		carregarMes();
		carregarEspeciesHibridos();
		if(agrossilvopastorilController.isFceSalvo()){
			carregarAbaSilvicultura();
		}
		if(!Util.isNull(agrossilvopastorilController.getFceAgrossilvopastoril())){
			carregarGrids();
		}
	}
	
	public void carregarAno() {
		listaAno = new ArrayList<String>();
		anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		for(int somarAno = 0; somarAno<QTD_ANO ;somarAno++){
			listaAno.add(new Integer(anoAtual+somarAno).toString());
		}
	}
	
	public void carregarMes() {
		Exception erro = null;
		try {
			listaMes = mesService.listarMes();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public boolean salvarAbaSilvicultura(Boolean isSalvarFinal){
		if(validarAbaSilvicultura()){
			Exception erro = null;
			try {
				insertTipoDadoEmEspeciesHibridos();
				silviculturaService.removerFceAgroTipoConservacaoByIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				silviculturaService.removerSilviculturaByIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				listaSilviculturaFlorestaProducao = removerTipoProjetoOuFlorestaNaoSelecionado(listaSilviculturaFlorestaProducao);
				listaSilviculturaEspecieReflores = removerTipoProjetoOuFlorestaNaoSelecionado(listaSilviculturaEspecieReflores);
				listaSilviculturaIMA = removerTipoProjetoOuFlorestaNaoSelecionado(listaSilviculturaIMA);
				silviculturaService.salvarAtualizarListaSilvicultura(setFalseInProjetoOrFloresta(listaSilviculturaFlorestaProducao),agrossilvopastorilController.getFceAgrossilvopastoril());
				silviculturaService.salvarAtualizarListaSilvicultura(setFalseInProjetoOrFloresta(listaSilviculturaEspecieReflores),agrossilvopastorilController.getFceAgrossilvopastoril());
				silviculturaService.salvarAtualizarListaSilvicultura(setFalseInProjetoOrFloresta(listaSilvicultura),agrossilvopastorilController.getFceAgrossilvopastoril());
				silviculturaService.salvarAtualizarListaSilvicultura(setFalseInProjetoOrFloresta(listaSilviculturaIMA),agrossilvopastorilController.getFceAgrossilvopastoril());
				silviculturaService.salvarTipoConservacaoSoloAgua(listaTipoConservSoloAguaSelecionados, agrossilvopastorilController.getFceAgrossilvopastoril());
				if(!isSalvarFinal){
					AgrossilvopastorilController.msgInclusao();
					agrossilvopastorilController.avancarAba();
				}
				carregarGrids();
				return true;
			} catch (Exception e) {
				erro =e;
				AgrossilvopastorilController.msgErroInclusao();
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				return false;
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return false;
	}
	
	/**
	 * @INFO Carrega os dados salvos anteriormente
	 */
	public void carregarAbaSilvicultura(){
		Exception erro = null;
		try {
			List<FceAgroTipoConservacao> listaTemp = silviculturaService.buscarTipoConservacaoSoloAguaByIdAgrossilvo(agrossilvopastorilController.getFceAgrossilvopastoril());
			listaTipoConservSoloAguaSelecionados = new ArrayList<TipoConservacaoSoloAgua>();
			for (FceAgroTipoConservacao fceAgroTipoConservacao : listaTemp) {
				listaTipoConservSoloAguaSelecionados.add(fceAgroTipoConservacao.getIdeTipoConservacaoSoloAgua());
			}
			listaSilvicultura = silviculturaService.buscarSilviculturaByIdAgrossilvoAndTipoDado(agrossilvopastorilController.getFceAgrossilvopastoril(), 
					TipoDadoSilviculturaEnum.ESPECIES_HIBRIDOS);
			for (Silvicultura silvicultura : listaSilvicultura) {
				silvicultura.setDesabilitado(true);
				listaEspecieHibrido.remove(silvicultura.getIdeEspecieHibrido());
				listaEspecieHibridoAll.remove(silvicultura.getIdeEspecieHibrido());
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	/**
	 * @INFO Identifica e adiciona o valor referente ao 'tipoProjetoOuFloresta' se é Projeto ou Floresta Implantada
	 * @Veja Silvicultura
	 */
	public void setProjetoOuFlorestaImplantada(){
		if(Util.isNull(listaSilviculturaFlorestaProducao)){
			listaSilviculturaFlorestaProducao = new ArrayList<Silvicultura>();
		}
		if(Util.isNull(listaSilviculturaEspecieReflores)){
			listaSilviculturaEspecieReflores = new ArrayList<Silvicultura>();
		}
		if(Util.isNull(listaSilviculturaIMA)){
			listaSilviculturaIMA = new ArrayList<Silvicultura>();
		}
		for (Silvicultura silvicultura : listaSilviculturaFlorestaProducao) {
			if(silvicultura.getIndProjeto()){
				silvicultura.setTipoProjetoOuFloresta("Projeto");
			}else
				silvicultura.setTipoProjetoOuFloresta("Floresta Implantada");
		}
		for (Silvicultura silvicultura : listaSilviculturaEspecieReflores) {
			if(silvicultura.getIndProjeto())
				silvicultura.setTipoProjetoOuFloresta("Projeto");
			else
				silvicultura.setTipoProjetoOuFloresta("Floresta Implantada");
		}
		for (Silvicultura silvicultura : listaSilviculturaIMA) {
			if(silvicultura.getIndProjeto())
				silvicultura.setTipoProjetoOuFloresta("Projeto");
			else
				silvicultura.setTipoProjetoOuFloresta("Floresta Implantada");
		}
	}
	
	/**
	 * @param list
	 * @return List<Silvicultura>
	 * @INFO Seta false em projeto ou floresta que nos tipo dados 1,2 e 4 este itens podem não ser renderizados permanecendo 
	 * assim nullos e impossibilitando a adição no banco de dados.
	 * @Veja TipoDadoSilviculturaEnum
	 */
	public List<Silvicultura> setFalseInProjetoOrFloresta(List<Silvicultura> listaSilvicultura){
		for (Silvicultura silvicultura : listaSilvicultura) {
			if(Util.isNull(silvicultura.getIndFlorestaImplantada()))
				silvicultura.setIndFlorestaImplantada(false);
			if(Util.isNull(silvicultura.getIndProjeto()))
				silvicultura.setIndProjeto(false);
		}
		return listaSilvicultura;
	}
	/**
	 * @param listaSilvicultura
	 * @return List<Silvicultura>
	 * @INFO Se o item (projeto ou floresta) não foi selecionado remove o mesmo da lista.
	 */
	public List<Silvicultura> removerTipoProjetoOuFlorestaNaoSelecionado(List<Silvicultura> listaSilvicultura){
		List<Silvicultura> listaTemp = new ArrayList<Silvicultura>();
		listaTemp.addAll(listaSilvicultura);
		for (int x = 0; x < listaTemp.size();x++) {
			if(listaTemp.get(x).getTipoProjetoOuFloresta().equals("Projeto") && !listaTemp.get(x).getIndProjeto()){
				listaSilvicultura.remove(x);
			}else if(listaTemp.get(x).getTipoProjetoOuFloresta().equals("Floresta Implantada") && !listaTemp.get(x).getIndFlorestaImplantada()){
				listaSilvicultura.remove(x);
			}
		}
		return listaSilvicultura;
	}
	/**
	 * @INFO Insere o tipo dado referente a Espécie Hibridos
	 */
	public void insertTipoDadoEmEspeciesHibridos(){
		for (Silvicultura silvicultura : this.listaSilvicultura) {
			silvicultura.setIdeTipoDadoSilvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.ESPECIES_HIBRIDOS.getId()));
		}
	}
	
	public boolean validarAbaSilvicultura(){
		boolean valido = true;
		if(!validaCheckBox(listaSilviculturaFlorestaProducao)){
			AgrossilvopastorilController.campoObrigatorio("A seleção dos dados para floresta de produção");
			valido = false;
		}
		if(!validaCheckBox(listaSilviculturaEspecieReflores)){
			AgrossilvopastorilController.campoObrigatorio("A seleção de espécies e variedades propostas/implantadas no reflorestamento");
			valido = false;
		}
		if(Util.isNullOuVazio(listaSilvicultura)){
			AgrossilvopastorilController.campoObrigatorio("Selecione ao menos 1 (hum) tipo de Espécie ou Hibrido");
			valido = false;
		}
		if(!validaCheckBox(listaSilviculturaIMA)){
			AgrossilvopastorilController.campoObrigatorio("A seleção de Incrimento Médio Anual Estimado - IMA");
			valido = false;
		}
		if (listaTipoConservSoloAguaSelecionados.size() == 0) {
			JsfUtil.addErrorMessage("Selecione ao menos 1 (hum) tipo de praticas existentes e planejadas, de manejo e conservação de solo e águas");
			valido = false;
		}
		//Se Floresta estiver marcada, a data de plantio deve ser nulla
		for (Silvicultura silvicultura : listaSilvicultura) {
			if(!Util.isNull(silvicultura.getIndFlorestaImplantada()) && silvicultura.getIndFlorestaImplantada()){
				silvicultura.setDtcAnoPrevisaoPlantio(null);
			}
		}
		if(!validarGrids(listaSilviculturaFlorestaProducao, "dados para floresta de produção",false,true) |
			!validarGrids(listaSilviculturaEspecieReflores, "espécies e variedades propostas/implantadas no reflorestamento",false,false) |
			!validarGrids(listaSilvicultura, "Espécie(s) ou Hibrido(s)",true,false) | 
			!validarGrids(listaSilviculturaIMA, "incrimento Médio Anual Estimado - IMA (m³/ha/ano)",false,true)){
			valido = false;
		}
		return valido;
	}
	
	/**
	 * @param lista
	 * @return
	 * @INFO Valida os checkbox de Projeto e Floresta se ao menos hum foi preenchido em cada Tipo de dados
	 * @Veja TipoDadoSilviculturaEnum
	 */
	public boolean validaCheckBox(List<Silvicultura> lista){
		if((Util.isNull(lista.get(0).getIndFlorestaImplantada()) || !lista.get(0).getIndFlorestaImplantada()) && (Util.isNull(lista.get(0).getIndProjeto()) || !lista.get(0).getIndProjeto())){
			if((Util.isNull(lista.get(1).getIndFlorestaImplantada()) || !lista.get(1).getIndFlorestaImplantada()) && (Util.isNull(lista.get(1).getIndProjeto()) || !lista.get(1).getIndProjeto())){
				return false;
			}
		}
		return true;
	}
	/**
	 * @param listaSilvicultura
	 * @param grid
	 * @param especieHibrido Caso seja grid de Espécie ou Hibrido
	 * @param florestaProducaoOuIMA => Caso seja a grid de Floresta de Produção ou IMA
	 * @return
	 */
	public boolean validarGrids(List<Silvicultura>  listaSilvicultura,String grid, boolean especieHibrido, boolean florestaProducaoOuIMA){
		boolean valido = true;
		String nomTipoDados;
		if(Util.isNull(listaSilvicultura)){
			listaSilvicultura = new ArrayList<Silvicultura>();
		}
		for (Silvicultura silvicultura : listaSilvicultura) {
			if((!Util.isNull(silvicultura.getIndProjeto()) && silvicultura.getIndProjeto()) || (!Util.isNull(silvicultura.getIndFlorestaImplantada()) && silvicultura.getIndFlorestaImplantada()) || especieHibrido){
				
				nomTipoDados = especieHibrido ? silvicultura.getIdeEspecieHibrido().getDscEspecieHibrido() : silvicultura.getTipoProjetoOuFloresta();
				//Somente se for Especies ou Hibridos
				if(especieHibrido && (Util.isNull(silvicultura.getIndFlorestaImplantada()) || !silvicultura.getIndFlorestaImplantada()) && (Util.isNull(silvicultura.getIndProjeto()) || !silvicultura.getIndProjeto())){
					AgrossilvopastorilController.campoObrigatorio("A seleção de projeto e/ou floresta de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if((Util.isNullOuVazio(silvicultura.getDtcAnoPrevisaoPlantio()) || silvicultura.getDtcAnoPrevisaoPlantio() < anoAtual) && !especieHibrido){
					AgrossilvopastorilController.campoObrigatorio("Ano de previsão de plantio de "+nomTipoDados+" em "+grid);
					valido = false;
				}else if(especieHibrido && (Util.isNull(silvicultura.getIndFlorestaImplantada()) || !silvicultura.getIndFlorestaImplantada()) && (Util.isNullOuVazio(silvicultura.getDtcAnoPrevisaoPlantio()) || silvicultura.getDtcAnoPrevisaoPlantio() < anoAtual)){
					AgrossilvopastorilController.campoObrigatorio("Ano de previsão de plantio de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if(Util.isNullOuVazio(silvicultura.getDtcAnoPrevisaoCorte()) || silvicultura.getDtcAnoPrevisaoCorte() < anoAtual){
					AgrossilvopastorilController.campoObrigatorio("Ano de previsão de corte de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if(florestaProducaoOuIMA && (Util.isNull(silvicultura.getDtcMesPrevisaoPlantio()) || Util.isNull(silvicultura.getDtcMesPrevisaoPlantio().getIdeMes()))){
					AgrossilvopastorilController.campoObrigatorio("Mês de previsão de plantio de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if(florestaProducaoOuIMA && (Util.isNull(silvicultura.getDtcMesPrevisaoCorte())|| Util.isNull(silvicultura.getDtcMesPrevisaoCorte().getIdeMes()))){
					AgrossilvopastorilController.campoObrigatorio("Mês de previsão de corte de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if(Util.isNullOuVazio(silvicultura.getNumAreaTotal())){
					AgrossilvopastorilController.campoObrigatorio("Área total de "+nomTipoDados+" em "+grid);
					valido = false;
				}
				if(Util.isNullOuVazio(silvicultura.getNumVolumeFinal())){
					AgrossilvopastorilController.campoObrigatorio("Estimativa do volume de produção de "+nomTipoDados+" em "+grid);
					valido = false;
				}
			}
		}
		
		return valido;
	}
	public void carregarGrids(){
		Exception erro = null;
		try {//Carrega os dados inseridos(ou não) pelo usuario anteriormente
			listaSilviculturaFlorestaProducao = silviculturaService.buscarSilviculturaByIdAgrossilvoAndTipoDado(agrossilvopastorilController.getFceAgrossilvopastoril(), 
					TipoDadoSilviculturaEnum.DADOS_FLORESTA_PRODUCAO);
			listaSilviculturaEspecieReflores = silviculturaService.buscarSilviculturaByIdAgrossilvoAndTipoDado(agrossilvopastorilController.getFceAgrossilvopastoril(), 
					TipoDadoSilviculturaEnum.ESPECIES_VARIED_PROPOSTAS_REFLOREST);
			listaSilviculturaIMA = silviculturaService.buscarSilviculturaByIdAgrossilvoAndTipoDado(agrossilvopastorilController.getFceAgrossilvopastoril(), 
				TipoDadoSilviculturaEnum.INCREMENTO_MEDIO_ANUAL);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		setProjetoOuFlorestaImplantada();
		//Se o usuario não inseriu nada, instância a lista
		if(Util.isNull(listaSilviculturaFlorestaProducao)){
			listaSilviculturaFlorestaProducao = new ArrayList<Silvicultura>();
		}
		//Se houver apenas um objeto na lista, ou seja o usuario selecionou apenas Projeto ou Floresta então adiciona o que está faltando
		if(listaSilviculturaFlorestaProducao.size()==1){
			if(listaSilviculturaFlorestaProducao.get(0).getTipoProjetoOuFloresta().equals("Projeto")){
				listaSilviculturaFlorestaProducao.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.DADOS_FLORESTA_PRODUCAO.getId()),"Floresta Implantada"));
			}else if(listaSilviculturaFlorestaProducao.get(0).getTipoProjetoOuFloresta().equals("Floresta Implantada")){
				listaSilviculturaFlorestaProducao.add(0,new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.DADOS_FLORESTA_PRODUCAO.getId()),"Projeto"));
			}
		//Se a lista for uma nova instância	
		}else if(listaSilviculturaFlorestaProducao.isEmpty()){
			listaSilviculturaFlorestaProducao.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.DADOS_FLORESTA_PRODUCAO.getId()),"Projeto"));
			listaSilviculturaFlorestaProducao.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.DADOS_FLORESTA_PRODUCAO.getId()),"Floresta Implantada"));
		}
		
		if(Util.isNull(listaSilviculturaEspecieReflores)){
			listaSilviculturaEspecieReflores = new ArrayList<Silvicultura>();
		}
		if(listaSilviculturaEspecieReflores.size()==1){
			if(listaSilviculturaEspecieReflores.get(0).getTipoProjetoOuFloresta().equals("Projeto")){
				listaSilviculturaEspecieReflores.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.ESPECIES_VARIED_PROPOSTAS_REFLOREST.getId()),"Floresta Implantada"));
			}else if(listaSilviculturaEspecieReflores.get(0).getTipoProjetoOuFloresta().equals("Floresta Implantada")){
				listaSilviculturaEspecieReflores.add(0,new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.ESPECIES_VARIED_PROPOSTAS_REFLOREST.getId()),"Projeto"));
			}
		}else if(listaSilviculturaEspecieReflores.isEmpty()){
			listaSilviculturaEspecieReflores.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.ESPECIES_VARIED_PROPOSTAS_REFLOREST.getId()),"Projeto"));
			listaSilviculturaEspecieReflores.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.ESPECIES_VARIED_PROPOSTAS_REFLOREST.getId()),"Floresta Implantada"));
		}
		
		if(Util.isNull(listaSilviculturaIMA)){
			listaSilviculturaIMA = new ArrayList<Silvicultura>();
		}
		if(listaSilviculturaIMA.size()==1){
			if(listaSilviculturaIMA.get(0).getTipoProjetoOuFloresta().equals("Projeto")){
				listaSilviculturaIMA.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.INCREMENTO_MEDIO_ANUAL.getId()),"Floresta Implantada"));
			}else if(listaSilviculturaIMA.get(0).getTipoProjetoOuFloresta().equals("Floresta Implantada")){
				listaSilviculturaIMA.add(0,new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.INCREMENTO_MEDIO_ANUAL.getId()),"Projeto"));
			}
		}else if(listaSilviculturaIMA.isEmpty()){
			listaSilviculturaIMA.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.INCREMENTO_MEDIO_ANUAL.getId()),"Projeto"));
			listaSilviculturaIMA.add(new Silvicultura(new TipoDadoSilvicultura(TipoDadoSilviculturaEnum.INCREMENTO_MEDIO_ANUAL.getId()),"Floresta Implantada"));
		}
	}
	public void carregarEspeciesHibridos(){
		Exception erro = null;
		try {
			listaEspecieHibridoAll = new ArrayList<EspecieHibrido>();
			listaEspecieHibrido = especieHibridoService.buscarTodosEspecieHibrido();
			listaEspecieHibridoAll.addAll(listaEspecieHibrido);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregarPraticasConservSolo(){
		Exception erro = null;
		try {
			listaTipoConservSoloAgua = tipoConservacaoSoloAguaService.buscarTodosTipoConservacaoSoloAgua();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void adicionarEspecie(){
		if(Util.isNull(listaSilvicultura)){
			listaSilvicultura = new ArrayList<Silvicultura>();
		}
		listaSilvicultura.add(new Silvicultura(especieHibrido));
		listaEspecieHibrido.remove(especieHibrido);
		listaEspecieHibridoAll.remove(especieHibrido);
	}
	
	public void excluirTipoCultura(){
		listaSilvicultura.remove(new Silvicultura(especieHibrido));
		listaEspecieHibrido.add(especieHibrido);
		listaEspecieHibridoAll.add(especieHibrido);
		Collections.sort(listaEspecieHibrido);
		Collections.sort(listaEspecieHibridoAll);
		AgrossilvopastorilController.msgExclusao();
	}
	
	public void pesquisarEspecieHibrido(){
		listaEspecieHibrido = new ArrayList<EspecieHibrido>();
		boolean achou = false;
		for (EspecieHibrido especieHibrido : listaEspecieHibridoAll) {
			if(especieHibrido.getDscEspecieHibrido().toLowerCase().indexOf(nomEspecieHibridos.toLowerCase()) != -1 || especieHibrido.getNomVulgar().toLowerCase().indexOf(nomEspecieHibridos.toLowerCase()) != -1){
				listaEspecieHibrido.add(especieHibrido);
				achou = true;
			}
		}
		if(!achou && nomEspecieHibridos.equals(""))	
			listaEspecieHibrido.addAll(listaEspecieHibridoAll);
	}
	
	@SuppressWarnings("unchecked")
	public void changeCheckBoxSoloAguas(ValueChangeEvent ev){
		List<TipoConservacaoSoloAgua> praticasPlanejadasNew = new ArrayList<TipoConservacaoSoloAgua>();
		praticasPlanejadasNew.addAll((List<TipoConservacaoSoloAgua>) ev.getNewValue());
		if(!Util.isNull(ev.getOldValue())){
			praticasPlanejadasNew.removeAll((List<TipoConservacaoSoloAgua>) ev.getOldValue());
		}
		if(praticasPlanejadasNew.contains(new TipoConservacaoSoloAgua(TipoConservacaoSoloAguaEnum.OUTROS.getId()))){
			RequestContext.getCurrentInstance().execute("infoOutros.show()");
		}
	}
	
	public void validaGridEspecie(Silvicultura especieSelecionada){
		boolean valido = true;
		if((Util.isNullOuVazio(especieSelecionada.getIndFlorestaImplantada()) || !especieSelecionada.getIndFlorestaImplantada()) && Util.isNullOuVazio(especieSelecionada.getDtcAnoPrevisaoPlantio())){
			AgrossilvopastorilController.campoObrigatorio("Ano de previsão de plantio");
			valido = false;
		}
		if(Util.isNullOuVazio(especieSelecionada.getDtcAnoPrevisaoCorte())){
			AgrossilvopastorilController.campoObrigatorio("Ano de previsão de corte");
			valido = false;
		}
		if(Util.isNullOuVazio(especieSelecionada.getNumAreaTotal())){
			AgrossilvopastorilController.campoObrigatorio("Área total (ha)");
			valido = false;
		}
		if(Util.isNullOuVazio(especieSelecionada.getNumVolumeFinal())){
			AgrossilvopastorilController.campoObrigatorio("Estimativa do volumede produção final (m³)");
			valido = false;
		}
		especieSelecionada.setDesabilitado(valido);
	}
	
	public boolean isExibirEspecieHibrido() {
		return !Util.isNullOuVazio(listaSilvicultura);
	}

	public List<Mes> getListaMes() {
		return listaMes;
	}

	public List<TipoConservacaoSoloAgua> getListaTipoConservSoloAgua() {
		return listaTipoConservSoloAgua;
	}

	public void setListaTipoConservSoloAgua(List<TipoConservacaoSoloAgua> listaTipoConservSoloAgua) {
		this.listaTipoConservSoloAgua = listaTipoConservSoloAgua;
	}

	public List<TipoConservacaoSoloAgua> getListaTipoConservSoloAguaSelecionados() {
		return listaTipoConservSoloAguaSelecionados;
	}

	public void setListaTipoConservSoloAguaSelecionados(List<TipoConservacaoSoloAgua> listaTipoConservSoloAguaSelecionados) {
		this.listaTipoConservSoloAguaSelecionados = listaTipoConservSoloAguaSelecionados;
	}
	
	public List<Silvicultura> getListaSilviculturaFlorestaProducao() {
		return listaSilviculturaFlorestaProducao;
	}

	public void setListaSilviculturaFlorestaProducao(List<Silvicultura> listaSilviculturaFlorestaProducao) {
		this.listaSilviculturaFlorestaProducao = listaSilviculturaFlorestaProducao;
	}

	public List<Silvicultura> getListaSilviculturaEspecieReflores() {
		return listaSilviculturaEspecieReflores;
	}

	public void setListaSilviculturaEspecieReflores(List<Silvicultura> listaSilviculturaEspecieReflores) {
		this.listaSilviculturaEspecieReflores = listaSilviculturaEspecieReflores;
	}

	public List<Silvicultura> getListaSilviculturaIMA() {
		return listaSilviculturaIMA;
	}

	public void setListaSilviculturaIMA(List<Silvicultura> listaSilviculturaIMA) {
		this.listaSilviculturaIMA = listaSilviculturaIMA;
	}

	public List<String> getListaAno() {
		return listaAno;
	}

	public String getNomEspecieHibridos() {
		return nomEspecieHibridos;
	}

	public void setNomEspecieHibridos(String nomEspecieHibridos) {
		this.nomEspecieHibridos = nomEspecieHibridos;
	}

	public List<EspecieHibrido> getListaEspecieHibrido() {
		return listaEspecieHibrido;
	}

	public void setListaEspecieHibrido(List<EspecieHibrido> listaEspecieHibrido) {
		this.listaEspecieHibrido = listaEspecieHibrido;
	}

	public List<Silvicultura> getListaSilvicultura() {
		return listaSilvicultura;
	}

	public void setListaSilvicultura(List<Silvicultura> listaSilvicultura) {
		this.listaSilvicultura = listaSilvicultura;
	}

	public EspecieHibrido getEspecieHibrido() {
		return especieHibrido;
	}

	public void setEspecieHibrido(EspecieHibrido especieHibrido) {
		this.especieHibrido = especieHibrido;
	}

	public Integer getAnoAtual() {
		return anoAtual;
	}

	public void setAnoAtual(Integer anoAtual) {
		this.anoAtual = anoAtual;
	}


}
