package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("pautaTecnicoController")
@ViewScoped
public class PautaTecnicoController extends PautaController {
	
	@EJB
	private MunicipioService municipioService;

	private List<Municipio> listaMunicipios;
	private Municipio  municipioSelecionado;
	
	@PostConstruct
	public void init() {
		carregarMunicipios();
		operacaoProcessoEnum = OperacaoProcessoEnum.PAUTA_TECNICO;
		consultarProcesso();
		super.listarCategoria();
	}
	
	private void carregarMunicipios() {
		try {
			this.listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	@Override
	public void consultarProcesso() {
		try {
			if (validate()) {
				consultar();
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregarNotificacaoPrazo(VwConsultaProcesso vwp) throws Exception {
		carregarNotificacao(vwp,TipoNotificacaoEnum.NOTIFICACAO_PRAZO);
	}
	
	public void carregarNotificacaoComunicacao(VwConsultaProcesso vwp) throws Exception {
		carregarNotificacao(vwp,TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO);
	}

	private void carregarNotificacao(VwConsultaProcesso vwp, TipoNotificacaoEnum tipoNotificacaoEnum) throws Exception {
		
		NotificacaoFinalController notificacaoFinalController = null;
		NotificacaoParcialController notificacaoParcialController = null;
		
		notificacaoFinalController = (NotificacaoFinalController) SessaoUtil.recuperarManagedBean("#{notificacaoFinalController}", NotificacaoFinalController.class);
		notificacaoParcialController = (NotificacaoParcialController) SessaoUtil.recuperarManagedBean("#{notificacaoParcialController}", NotificacaoParcialController.class);
		
		notificacaoFinalController.limparTudo();
		if(vwp.isIndLiderEquipe()) {
			notificacaoParcialController.limpaTudo();
			notificacaoFinalController.load(vwp,tipoNotificacaoEnum,false);
		}
		else{
			notificacaoParcialController.load(vwp,tipoNotificacaoEnum);
		}
	}

	public void limparTela(){
		super.limpar();
	}
	
	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> params = super.getParametros();
		
		List<Integer> statusValidos = new ArrayList<Integer>();
		statusValidos.add(StatusFluxoEnum.ANALISE_TECNICA.getStatus());
	
		params.put("statusFluxo", statusValidos);
		
		return params;
	}
	
	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}
}