package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisica;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPauta;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.FuncionalidadeControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;


public abstract class PautaCompartilhadaController extends PautaController {
	
	@EJB
	private AreaService areaService;
	@EJB
	private PautaService pautaService;
	
	protected Area area;
	protected Pauta pauta;
	protected Boolean funcionarioResponsavel;
	
	@PostConstruct
	protected void init() {
		try {
			pauta = (Pauta) SessaoUtil.recuperarObjetoSessao("pauta");
			funcionarioResponsavel = (pauta == null);
			carregarArea();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarArea() throws Exception {
		if(funcionarioResponsavel) {
			area = areaService.buscarAreaPorPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
		}
		else{
			area = new TipoPauta(TipoPautaEnum.PAUTA_AREA.getTipo()).equals(pauta.getIdeTipoPauta()) ? pauta.getIdeArea() : pauta.getIdePessoaFisica().getIdeArea();
		}
	}
	
	@Override
	protected void consultar() throws Exception {
		
		final Pauta pautaAlvo = retornarPautaAlvo();
		
		dataModelProcessos = new LazyDataModel<VwConsultaProcesso>() {
			
			private static final long serialVersionUID = -549249300009769836L;
			
			@Override
			public List<VwConsultaProcesso> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				try {
					return (List<VwConsultaProcesso>) vwProcessoService.listarPorCriteriaDemanda(getParametros(), operacaoProcessoEnum, pautaAlvo.getIdePauta(), first, pageSize);
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		};
		
		dataModelProcessos.setRowCount(getRowCountProcesso(pautaAlvo));
	}

	private Pauta retornarPautaAlvo() throws Exception {
		
		Pauta pautaAlvo = null;
		
		if(funcionarioResponsavel) {
			switch (operacaoProcessoEnum) {
			case DISTRIBUIR:
				pautaAlvo = pautaService.obtemPautaArea(area.getIdeArea());
				break;
			default:
				Integer idePessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
				pautaAlvo = pautaService.obtemPautaPorIdeFuncionario(idePessoaFisica);
				break;
			}
		}
		else{
			pautaAlvo = pauta;
		}
		
		return pautaAlvo;
	}
	
	public boolean temAcessoConcedidoPeloGestor(Integer acao){
		
		Funcionalidade funcionalidade = operacaoProcessoEnum.equals(OperacaoProcessoEnum.DISTRIBUIR) 
			? new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA.getId())
			: new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DO_GESTOR.getId())
		;
		
		for (FuncionalidadeAcaoPessoaFisicaPauta fapfp : pauta.getFuncionalidadeAcaoPessoaFisicaPautaCollection()) {
			FuncionalidadeAcaoPessoaFisica fapf = fapfp.getIdeFuncionalidadeAcaoPessoaFisica();
			if(funcionalidade.equals(fapf.getIdeFuncionalidade()) && new Acao(acao).equals(fapf.getIdeAcao())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean temAcessoConcedidoPeloGestor() {
		return !Util.isNull(pauta) && !Util.isNullOuVazio(pauta.getFuncionalidadeAcaoPessoaFisicaPautaCollection());		
	}

	
	public abstract void consultarProcesso();

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Boolean getFuncionarioResponsavel() {
		return funcionarioResponsavel;
	}

	public void setFuncionarioResponsavel(Boolean funcionarioResponsavel) {
		this.funcionarioResponsavel = funcionarioResponsavel;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}
}
