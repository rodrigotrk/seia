package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaComDocumentoAdicionalController;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceBarragLicencLocaGeo;
import br.gov.ba.seia.entity.FceBarragem;
import br.gov.ba.seia.entity.FceBarragemLicenciamento;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MaterialUtilizado;
import br.gov.ba.seia.entity.ObrasInfraComplementares;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.UsoReservatorio;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;
import br.gov.ba.seia.facade.FceBarragemFacade;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceBarragLicencLocaGeoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.controller.EnquadramentoController;

@Named("fceBarragemController")
@ViewScoped
public class FceBarragemController extends BaseFceOutorgaComDocumentoAdicionalController implements FceNavegacaoInterface{

	private static final DocumentoObrigatorio DOCUMENTO_OBRIGATORIO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_CONSTRUCAO_BARRAGEM.getId());
	
	private int activeAba;
	
	private FceBarragem fceBarragem;
	
	@EJB
	private FceBarragemFacade facade;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private FceBarragLicencLocaGeoService fceBarragLicencLocaGeoService;
	
	private boolean finalizar;
	
	protected MetodoUtil metodoAddLocalizacaoLicenciamento;
	
	private List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeografica;
	
	private List<OutorgaConcedida> listaOutorgaConcedida;
	
	private FceBarragemLicenciamento fceBarragemLicenciamento;
	
	private FceBarragLicencLocaGeo fceBarragLicencLocaGeo;
	
	private FceBarragLicencLocaGeo fceBarragLicencLocaGeoExclusao;
	
	
	//DADOS GERAIS
	private MetodoUtil metodoRetornoInundacao;
	
	private LocalizacaoGeografica localizacaoGeograficaInundacao;
	
	//LICENCIMENTO
	private MetodoUtil metodoRetornoAppReservatorio;
	
	private LocalizacaoGeografica localizacaoGeograficaAppReservatorio;
	
	private LocalizacaoGeografica localizacaoGeograficaSuprimida;
	
	private MetodoUtil metodoRetornoPontoInicial;
	
	private MetodoUtil metodoRetornoPontoFinal;
	
	private MetodoUtil metodoRetornoSuprimida;
	
	//REEQUADRAMENTO
	private ProcessoReenquadramento processoReenquadramento;
	
	@Inject
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	
	/**
	//[RN 00310]- Disponibilizar FCE de barragem para preenchimento.			
	 * @throws  
	 * 
	 */
	public void avancarAba() {
		if(this.validarAba()){
			finalizar = false;
			
			switch (this.activeAba) {
			case 0:
				salvarFcebarragem();
				
				if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento()) && !this.getDisableAbaBarragemLicenciamento()){
					this.getFceBarragem().setFceBarragemLicenciamento(new FceBarragemLicenciamento());
					this.getFceBarragem().getFceBarragemLicenciamento().setFceBarragem(this.fceBarragem);
				}
				if(this.getDisableAbaIntervencaoBarragem()){
					activeAba++;
				}
				break;
			case 1:
				//salvarFcebarragem();
				salvarFceIntervencaoBarragem();
				break;
			}
			if(this.activeAba < 2){
				activeAba ++;
			}
		}
	}
	/**
	//[MSG-001] - Inclusão realizada com sucesso!
	//[MSG-002]	- Alteração realizada com sucesso!
	 * @throws Exception 
	 * 
	 */
	public void salvarFcebarragem() {
		try {
			Integer ideFceBarragem = fceBarragem.getIdeFceBarragem();
			
			fceBarragemLicenciamento = fceBarragem.getFceBarragemLicenciamento();
			fceBarragem.setFceBarragemLicenciamento(null);
			facade.salvarFceBarragem(fceBarragem);
			fceBarragem.setFceBarragemLicenciamento(fceBarragemLicenciamento);
			
			if (!finalizar){
				if(Util.isNullOuVazio(ideFceBarragem)){
					super.exibirMensagem001();
				}
				else {
					super.exibirMensagem002();
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void limparPonto() {
		fceBarragLicencLocaGeoExclusao = null;
		fceBarragLicencLocaGeo = new FceBarragLicencLocaGeo(true);
		fceBarragLicencLocaGeo.setIdeLocalGeoInicioEixo(new LocalizacaoGeografica());
		fceBarragLicencLocaGeo.setIdeLocalGeoFimEixo(new LocalizacaoGeografica());
		localizacaoGeograficaInundacao = new LocalizacaoGeografica();
		localizacaoGeograficaAppReservatorio = new LocalizacaoGeografica();
	}
	
	public void atualizarIntervencaoBarragem(FceIntervencaoBarragem fceIntervencaoBarragem){
		boolean removido = false;
		for (Iterator<FceIntervencaoBarragem> iter = fceBarragem.getFceIntervencaoBarragems().iterator(); iter.hasNext(); ) {
			FceIntervencaoBarragem fceIntervencaoBarragemIt = iter.next();
		    if (!Util.isNullOuVazio(fceIntervencaoBarragemIt.getIdeOutorgaLocalizacaoGeografica())){
				if (fceIntervencaoBarragemIt.getIdeOutorgaLocalizacaoGeografica().equals(fceIntervencaoBarragem.getIdeOutorgaLocalizacaoGeografica())){
					iter.remove();
					removido = true;
				}
		    }
		}
		
		if (removido){
			fceBarragem.getFceIntervencaoBarragems().add(fceIntervencaoBarragem);
			Html.atualizar("dtIntervencaoBarragem");
		}
	}
	
	public void salvarFceIntervencaoBarragem() {
		try {
			
			if (!Util.isNullOuVazio(fceBarragem.getFceIntervencaoBarragems())){
				facade.salvarFceBarragemIntervencaoLista(fceBarragem.getFceIntervencaoBarragems());
				
				if (!finalizar){
					if(isFceSalvo()){
						super.exibirMensagem001();
					}
					else {
						super.exibirMensagem002();
					}
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public int getSomenteShape() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	
	/**
	 * Validação de todos os campos na tela de Dados Gerais para o FCE de Barragem
	 * @return
	 */
	private boolean validadadosGerais(){
		if(Util.isNullOuVazio(this.getFceBarragem().getUsosReservatorio())){
			JsfUtil.addWarnMessage("É obrigatório selecionar pelo menos um Uso do Reservatório");
			return false;
		}	
		if(Util.isNullOuVazio(this.getFceBarragem().getVazaoMaxima())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Vazão máxima'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getVazaoRegularizada())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Vazão regularizada'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getDescargaFundo())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Descarga de fundo'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getAlturaMaxima())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Altura máxima'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getVolumeMaximoAcumuldao())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Volume máximo acumulado'");
			return false;
		}	
		if(Util.isNullOuVazio(this.getFceBarragem().getLocalizacaoGeografica())){
			JsfUtil.addWarnMessage("É obrigatório informar a poligonal da 'Área máxima de inundação'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getCotaMaxima())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Cota máxima'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getCotaMinima())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Cota mínima'");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getCotaMediaDiarias())){
			JsfUtil.addWarnMessage("É obrigatório informar o campo 'Cota média diárias'");
			return false;
		}
		return true;
	}
	
	
	/**
	 * [MSG-003] 	[nome do campo] é de preenchimento obrigatório.
		[MSG-0011] 	[nome do campo] deve ser maior que zero.
	 * 
	 * @return
	 */
	private boolean validadadosLicenciamento(){
		if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo())){
			MensagemUtil.msg0003("Dados Geográficos");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getMaterialUtilizados())){
			MensagemUtil.msg0003("Material(ais) utilizado(s)");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getObrasInfraComplementares())){
			MensagemUtil.msg0003("Obras de infraestrutura e complementares");
			return false;
		}		
		if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getPoligonalApp())){
			MensagemUtil.msg0003("Poligonal App");
			return false;
		}
		if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getPoligonalAreaSuprimida()) && isLp()){
			MensagemUtil.msg0003("Poligonal da área a ser suprimida");
			return false;
		}
		if(!Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo())){
			for(FceBarragLicencLocaGeo licencLocaGeo : this.getFceBarragem().getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo()){
				if(Util.isNullOuVazio(licencLocaGeo.getNomeRio())){
					MensagemUtil.msg0003("nome do rio");
					return false;
				}
				
				if (!licencLocaGeo.getConfirmar()){
					JsfUtil.addErrorMessage("É necessário a confirmação do nome do rio.");
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validadadosIntervencao() {
		if(Util.isNullOuVazio(this.getFceBarragem().getFceIntervencaoBarragems())){
			JsfUtil.addErrorMessage("É necessário informar os campos da barragem de regularização.");
			return false;
		}
		return true;
	}
	
	public void voltarAba() {
		if(activeAba == 0){
			Html.esconder("dialogFcebarragem");
		}else if(activeAba == 2){
			if(this.getDisableAbaIntervencaoBarragem()){
				activeAba --;
				activeAba --;
			}else {
				activeAba --;
			}
		}else {
			activeAba --;
		}
	}
	
	@Override
	public void init() {
		try {
			this.setActiveAba(0);
			finalizar = false;
			verificarEdicao();
			
			metodoAddLocalizacaoLicenciamento = new MetodoUtil(this, "addLocalizacaoLicenciamento");
			metodoRetornoInundacao = new MetodoUtil(this, "retornoInundacao");
			metodoRetornoAppReservatorio = new MetodoUtil(this, "retornoAppReservatorio");
			metodoRetornoPontoInicial =  new MetodoUtil(this, "retornoPontoInicial");
			metodoRetornoPontoFinal =  new MetodoUtil(this, "retornoPontoFinal");
			metodoRetornoSuprimida = new MetodoUtil(this, "retornoSuprimida");			
		
			limparPonto();
			
			if(!isFceSalvo()){
				super.iniciarFce(DOCUMENTO_OBRIGATORIO);
				salvarFce();
			}
			carregarAba();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public LocalizacaoGeografica getNovaLocalizacao() {
		return new LocalizacaoGeografica();
	}
	
	public void retornoInundacao() {
		if (!Util.isNullOuVazio(localizacaoGeograficaInundacao)){
			boolean valido = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaInundacao.getIdeLocalizacaoGeografica());
			if (valido){
				getFceBarragem().setLocalizacaoGeografica(localizacaoGeograficaInundacao);
				Html.atualizar("formFcebarragem:_tabAbasFcebarragem:pgAreaMaxima");
			} else {
				getFceBarragem().setLocalizacaoGeografica(null);
			}
		}
	}
	
	public void retornoAppReservatorio()  {
		if (!Util.isNullOuVazio(localizacaoGeograficaAppReservatorio)){
			boolean valido = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaAppReservatorio.getIdeLocalizacaoGeografica());
			if (valido){
				getFceBarragem().getFceBarragemLicenciamento().setPoligonalApp(localizacaoGeograficaAppReservatorio);
				Html.atualizar("formFcebarragem");
			} else {
				getFceBarragem().getFceBarragemLicenciamento().setPoligonalApp(null);
			}
		}
	}
	
	public void retornoSuprimida()  {
		if (!Util.isNullOuVazio(localizacaoGeograficaSuprimida)){
			boolean valido = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSuprimida.getIdeLocalizacaoGeografica());
			if (valido){
				getFceBarragem().getFceBarragemLicenciamento().setPoligonalAreaSuprimida(localizacaoGeograficaSuprimida);
				Html.atualizar("formFcebarragem");
			} else {
				getFceBarragem().getFceBarragemLicenciamento().setPoligonalAreaSuprimida(null);
			}
		}
	}
	
	public void removerPoligonalApp(){
		getFceBarragem().getFceBarragemLicenciamento().setPoligonalApp(null);
		setLocalizacaoGeograficaAppReservatorio(new LocalizacaoGeografica());
	}
	
	public void retornoPontoInicial()  {
		if (!Util.isNullOuVazio(getFceBarragLicencLocaGeo().getIdeLocalGeoInicioEixo())){
			if (!Util.isNullOuVazio(getFceBarragLicencLocaGeo().getIdeLocalGeoInicioEixo().getPontoLatitude())){
				Html.atualizar("formPonto");
			} else {
				getFceBarragLicencLocaGeo().setIdeLocalGeoInicioEixo(new LocalizacaoGeografica());
			}
		}
	}
	
	public void retornoPontoFinal()  {
		if (!Util.isNullOuVazio(getFceBarragLicencLocaGeo().getIdeLocalGeoFimEixo())){
			if (!Util.isNullOuVazio(getFceBarragLicencLocaGeo().getIdeLocalGeoFimEixo().getPontoLatitude())){
				Html.atualizar("formPonto");
			} else {
				getFceBarragLicencLocaGeo().setIdeLocalGeoFimEixo(new LocalizacaoGeografica());
			}
		}
	}
	
	public void addUsoReservatorio(UsoReservatorio uso){
		if(this.getFceBarragem().getUsosReservatorio().contains(uso)){
			uso.setSelecionado(false);
			this.getFceBarragem().getUsosReservatorio().remove(uso);
		} else {
			uso.setSelecionado(true);
			this.getFceBarragem().getUsosReservatorio().add(uso);
			if(uso.getUsoReservatorio().equals("Outros")){
				JsfUtil.addWarnMessage(Util.getString("fce_barragem_msg_outros"));
			}
		}
	}
	
	public void addMateriaisUtilizados(MaterialUtilizado item){
		if(this.getFceBarragem().getFceBarragemLicenciamento().getMaterialUtilizados().contains(item)){
			item.setSelecionado(false);
			this.getFceBarragem().getFceBarragemLicenciamento().getMaterialUtilizados().remove(item);
		} else {
			this.getFceBarragem().getFceBarragemLicenciamento().getMaterialUtilizados().add(item);
			item.setSelecionado(true);
			if(item.getMaterialUtilizado().equals("Outros")){
				JsfUtil.addWarnMessage(Util.getString("fce_barragem_msg_outros"));
			}
		}
	}
	//addObrasInfraComplementares
	public void addObrasInfraComplementares(ObrasInfraComplementares item){
		if(this.getFceBarragem().getFceBarragemLicenciamento().getObrasInfraComplementares().contains(item)){
			item.setSelecionado(false);
			this.getFceBarragem().getFceBarragemLicenciamento().getObrasInfraComplementares().remove(item);
		} else {
			this.getFceBarragem().getFceBarragemLicenciamento().getObrasInfraComplementares().add(item);
			item.setSelecionado(true);
			if(item.getNome().equals("Outros")){
				JsfUtil.addWarnMessage(Util.getString("fce_barragem_msg_outros"));
			}
		}
	}
	
	public List<UsoReservatorio> getBuildUsoReservatorios() throws Exception{
		List<UsoReservatorio> usosReservatorios =  facade.listarUsoReservatorios();
		if(!Util.isNullOuVazio(fceBarragem) && !Util.isNullOuVazio(fceBarragem.getUsosReservatorio())){
			for (UsoReservatorio usoReservatorio : usosReservatorios) {
				for (UsoReservatorio usoReservatorioBarragem : fceBarragem.getUsosReservatorio()){
					if(usoReservatorio.equals(usoReservatorioBarragem)){
						usoReservatorio.setSelecionado(true);
						break;
					}
				}
			}
		}
		return usosReservatorios;
	}
	
	public boolean verificarSeOpcaoOutros() throws Exception{
		return facade.verificarSeOpcaoOutros(fceBarragem.getUsosReservatorio(), fceBarragem.getFceBarragemLicenciamento());
	}
	
	public List<MaterialUtilizado> getBuildMateriaisUtilizados() throws Exception {
		List<MaterialUtilizado> data =  facade.listarMateriaislUtilizados();
		if(!Util.isNullOuVazio(fceBarragem) && !Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento())){
			for (MaterialUtilizado item: data) {
				for (MaterialUtilizado itemFce : fceBarragem.getFceBarragemLicenciamento().getMaterialUtilizados()){
					if(item.equals(itemFce)){
						item.setSelecionado(true);
						break;
					}
				}
			}
		}
		return data;
	}
	
	public List<ObrasInfraComplementares> getBuildObrasInfraComplementares() throws Exception {
		List<ObrasInfraComplementares> data =  facade.listarObrasInfraComplementares();
		if(!Util.isNullOuVazio(fceBarragem) && !Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento())){
			for (ObrasInfraComplementares item : data) {
				for (ObrasInfraComplementares itemFce : fceBarragem.getFceBarragemLicenciamento().getObrasInfraComplementares()){
					if(item.equals(itemFce)){
						item.setSelecionado(true);
						break;
					}
				}
			}
		}
		return data;
	}
	
	public void addLocalizacaoLicenciamento(){
		if (!Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento())){
			
			if (Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo())){
				fceBarragem.getFceBarragemLicenciamento().setBarragemLicenciamentoLocalizacaoGeo(new ArrayList<FceBarragLicencLocaGeo>());
			}
			
			this.fceBarragLicencLocaGeo.setFceBarragemLicenciamento(this.fceBarragem.getFceBarragemLicenciamento());
			
			if (!fceBarragem.getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo().contains(this.fceBarragLicencLocaGeo)){
				if (!Util.isNullOuVazio(fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo().getLatitudeInicial()) || !Util.isNullOuVazio(fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo().getLatitudeInicial())){
					fceBarragem.getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo().add(this.fceBarragLicencLocaGeo);
				} else {
					limparEixo();
				}
			} else {
				limparEixo();
			}
			
			Html.atualizar("formPonto");
		}	
	}
	
	private void limparEixo() {
		if (Util.isNullOuVazio(fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo().getLatitudeInicial())){
			fceBarragLicencLocaGeo.setIdeLocalGeoInicioEixo(new LocalizacaoGeografica());
		}
		
		if (Util.isNullOuVazio(fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo().getLatitudeInicial())){
			fceBarragLicencLocaGeo.setIdeLocalGeoFimEixo(new LocalizacaoGeografica());
		}
	}
	
	public void desabilitarNomeRio(FceBarragLicencLocaGeo fceBarragLicencLocaGeo, boolean confirmar) {
		if (Util.isNullOuVazio(fceBarragLicencLocaGeo.getNomeRio())){
			MensagemUtil.msg0003("Nome do Rio");
			return;
		}
		
		fceBarragLicencLocaGeo.setConfirmar(confirmar);
	}
	
	public FceIntervencaoBarragem obterIntervencaoBarragem(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		return facade.obterIntervencaoBarragem(this.fce, this.fceBarragem, outorgaLocalizacaoGeografica);
	}
	
	public DadoGeografico getDadoGeograficoInCollection(Collection<DadoGeografico> dados, Integer index) {
		if(!Util.isNullOuVazio(dados)){
			if(index != null){
				return new ArrayList<DadoGeografico>(dados).get(index);
			}else{
				return new ArrayList<DadoGeografico>(dados).get(dados.size() -1);
			}
		}
		return null;
	}
	
	public String getRPGA(LocalizacaoGeografica localizacaoGeografica){
		return localizacaoGeograficaServiceFacade.getRPGA(localizacaoGeografica);
	}
	
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		return localizacaoGeograficaServiceFacade.getBacia(localizacaoGeografica);
	}
	
	public void removeItemList() throws Exception {
		facade.removerFceBarragLicencLocaGeo(fceBarragLicencLocaGeoExclusao);
		this.fceBarragem.getFceBarragemLicenciamento().getBarragemLicenciamentoLocalizacaoGeo().remove(fceBarragLicencLocaGeoExclusao);
	}
	
	public void removerItemLocalizacaoGeografica(){
		setLocalizacaoGeograficaInundacao(new LocalizacaoGeografica());
		this.fceBarragem.setLocalizacaoGeografica(null);
	}
	
	public Double getAreaShape(LocalizacaoGeografica localizacaoGeografica){
		try {
			return facade.getAreaDoShape(localizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return null;
	}
	
	public String getBioma(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return facade.getBioma(localizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return null;
	}
	
	public boolean verificarTipoBarragem(TipoBarragem tipoBarragem){
		return TipoBarragemEnum.REGULARIZACAO.getId().toString().equals(tipoBarragem.getIdeTipoBarragem().toString());
	}
	
	@Override
	public void verificarEdicao() {
		super.carregarFceDoRequerente(DOCUMENTO_OBRIGATORIO);
	}

	@Override
	public void carregarAba() {
		
		try {
			this.fceBarragem = facade.buscarFceBarragem(this.getFce());
			processoReenquadramento = processoReenquadramentoDAOImpl.processoReenquadramentoPorRequerimento(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if(Util.isNullOuVazio(this.fceBarragem)){
			fceBarragem = new FceBarragem();
			fceBarragem.setIdeFce(this.getFce());
		}
	}

	@Override
	public void finalizar() {
		
		if (validarAba()) {
			try {
				facade.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Barragem.");
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if (!verificarSeOpcaoOutros()){
			super.concluirFce();
		} else {
			super.getFce().setIndConcluido(false);
			fceServiceFacade.salvarFce(super.getFce());
			JsfUtil.addWarnMessage(Util.getString("fce_barragem_msg_outros"));
		}
		
		finalizar = true;
		
		if (!Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento())){
			facade.salvarFceBarragemLicenciamento(fceBarragem.getFceBarragemLicenciamento());
			facade.salvarFceBarragLicencLocaGeo(fceBarragem.getFceBarragemLicenciamento(), false);
		}
		
		if (!Util.isNullOuVazio(fceBarragem.getFceIntervencaoBarragems())){
			salvarFceIntervencaoBarragem();
		}
		
		salvarFcebarragem();
		
		if(!isFceSalvo()){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
		
		if (isFceTecnico()){

			 Html.executarJS("atualizarDadoConcedido();");
		}
		
		//imprimir relatório
		
		Html.exibir("confirmationSalvarFCEBarragem");
	}

	public void validarPonto()  {
		if (Util.isNullOuVazio(this.fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo())){
			MensagemUtil.msg0003("Ponto Inicial");
			return;
		}
		
		if (Util.isNullOuVazio(this.fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo())){
			MensagemUtil.msg0003("Ponto Final");
			return;
		}
		
		Html.atualizar("formFcebarragem:_tabAbasFcebarragem:pgDadosGeograficos");
		Html.executarJS("dialogIncluirPonto.hide()");
	}
	
	@Override
	public void limpar() {
		super.limparFce();		
	}

	@Override
	public boolean validarAba() {
		switch (this.activeAba) {
		case 0:
			return validadadosGerais();
		case 1:
			return validadadosIntervencao();
		case 2:
			return validadadosLicenciamento();
		default:
			return true;
		}
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formFcebarragem");
		RequestContext.getCurrentInstance().execute("dialogFcebarragem.show()");
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			fceBarragem.setIdeFce(super.fce);
			fceBarragem.setIdeFceBarragem(null);
			List<UsoReservatorio> usoReservatorioLista = new ArrayList<UsoReservatorio>();
			
			//adiciona o uso reservatorio
			for (UsoReservatorio usoReservatorio : this.fceBarragem.getUsosReservatorio()) {
				usoReservatorioLista.add(usoReservatorio);
			}
			this.fceBarragem.setUsosReservatorio(usoReservatorioLista);
			
			if (!Util.isNullOuVazio(this.fceBarragem.getFceBarragemLicenciamento())){
				FceBarragemLicenciamento fceBarragemLicenciamentoClone = this.fceBarragem.getFceBarragemLicenciamento().clone();
				fceBarragemLicenciamentoClone.setIdeFceBarragemLicenciamento(null);
				
				//adiciona o material utilizado
				fceBarragemLicenciamentoClone.setMaterialUtilizados(new ArrayList<MaterialUtilizado>());
				List<MaterialUtilizado> listaMaterialUtilizado = new ArrayList<MaterialUtilizado>();
				for (MaterialUtilizado materialUtilizado : this.fceBarragem.getFceBarragemLicenciamento().getMaterialUtilizados()) {
					fceBarragemLicenciamentoClone.getMaterialUtilizados().add(materialUtilizado);
				}
				this.fceBarragem.getFceBarragemLicenciamento().setMaterialUtilizados(listaMaterialUtilizado);
				
				//adiciona o obras infra complementares
				fceBarragemLicenciamentoClone.setObrasInfraComplementares(new ArrayList<ObrasInfraComplementares>());
				List<ObrasInfraComplementares> listaObrasInfraComplementares = new ArrayList<ObrasInfraComplementares>();
				for (ObrasInfraComplementares obrasInfraComplementares : this.fceBarragem.getFceBarragemLicenciamento().getObrasInfraComplementares()) {
					fceBarragemLicenciamentoClone.getObrasInfraComplementares().add(obrasInfraComplementares);
				}
				this.fceBarragem.getFceBarragemLicenciamento().setObrasInfraComplementares(listaObrasInfraComplementares);
				
				fceBarragem.setFceBarragemLicenciamento(fceBarragemLicenciamentoClone);
			}
			
			for (FceIntervencaoBarragem fceIntervencaoBarragemClone : this.fceBarragem.getFceIntervencaoBarragems()){
				fceIntervencaoBarragemClone.setIdeFceIntervencaoBarragem(null);
				fceIntervencaoBarragemClone.setIdeFce(super.fce);
				fceIntervencaoBarragemClone.setFceBarragem(fceBarragem);
				
				OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = fceIntervencaoBarragemClone.getIdeOutorgaLocalizacaoGeografica();
				if (!Util.isNullOuVazio(outorgaLocalizacaoGeografica)){
					outorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(null);
				}
				fceIntervencaoBarragemClone.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
				
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Barragem - Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	@Override
	protected void duplicarFce() {
		try {
			facade.duplicarFce(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Intervenção de Barragem.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}	
	}

	@Override
	protected void carregarFceTecnico() {
		super.carregarFceDoTecnico(DOCUMENTO_OBRIGATORIO);
		listarOutorgaLocalizacaoGeografica = facade.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaTecnico(super.getRequerimento(), ModalidadeOutorgaEnum.INTERVENCAO, DadoOrigemEnum.TECNICO);
		carregarAba();
	}

	public boolean isLp(){
		try {
			return facade.verificarLP(getRequerimento(), false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return false;
	}
	
	public List<LocalizacaoGeografica> getLocalizacaoGeograficaList(LocalizacaoGeografica localizacaoGeografica){
		List<LocalizacaoGeografica> data = new ArrayList<LocalizacaoGeografica>();
		if(!Util.isNullOuVazio(localizacaoGeografica)){
			data.add(localizacaoGeografica);
		}
		return data;
	}
	
	/**
	 * @return the activeAba
	 */
	public int getActiveAba() {
		return activeAba;
	}

	/**
	 * @param activeAba the activeAba to set
	 */
	public void setActiveAba(int activeAba) {
		this.activeAba = activeAba;
	}

	/**
	 * @return the fceBarragem
	 */
	public FceBarragem getFceBarragem() {
		return fceBarragem;
	}

	/**
	 * @param fceBarragem the fceBarragem to set
	 */
	public void setFceBarragem(FceBarragem fceBarragem) {
		this.fceBarragem = fceBarragem;
	}
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("FcebarragemAbaDadosGerais".equals(event.getTab().getId())) {
			this.activeAba = 0;
		} else if ("FcebarragemAbaIntervencaoBarragem".equals(event.getTab().getId())) {
			this.activeAba = 1;
		} else if ("FcebarragemLicenciamentoBarragem".equals(event.getTab().getId())) {
			if(Util.isNullOuVazio(this.getFceBarragem().getFceBarragemLicenciamento()) && !this.getDisableAbaBarragemLicenciamento()){
				this.getFceBarragem().setFceBarragemLicenciamento(new FceBarragemLicenciamento());
				this.getFceBarragem().getFceBarragemLicenciamento().setFceBarragem(this.fceBarragem);
			}
			this.activeAba = 2;
		}
	}

	
	public boolean getDisableAbaIntervencaoBarragem(){
		DadoOrigemEnum dadoOrigemEnum = null;
		if (!Util.isNullOuVazio(super.getRequerimento()) && !Util.isNullOuVazio(fceBarragem.getIdeFceBarragem())){
			if (!isFceTecnico()){
				if (Util.isNullOuVazio(fceBarragem.getFceIntervencaoBarragems())){
					listarOutorgaLocalizacaoGeografica = facade.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(super.getRequerimento());
				} else {
					if (!Util.isNullOuVazio(processoReenquadramento)){
						dadoOrigemEnum = DadoOrigemEnum.REENQUADRAMENTO;
					} else {
						dadoOrigemEnum = DadoOrigemEnum.REQUERIMENTO;
					}

					listarOutorgaLocalizacaoGeografica = facade.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaTecnico(super.getRequerimento(), ModalidadeOutorgaEnum.INTERVENCAO, dadoOrigemEnum);
				}
			}
			return Util.isNullOuVazio(listarOutorgaLocalizacaoGeografica);
		}
		return true;
	}
	
	public boolean getDisableAbaBarragemLicenciamento(){
		if (!Util.isNullOuVazio(fceBarragem.getIdeFceBarragem())){
			return facade.disableAbaFceBarragemLicenciamento(super.getRequerimento());
		}
		return true;
	}
	
	public StreamedContent getImprimirRelatorio() throws Exception{
		if(isFceTecnico()){
			carregarFceDoTecnico(DOCUMENTO_OBRIGATORIO);
		} else {
			carregarFceDoRequerente(DOCUMENTO_OBRIGATORIO);
		}
		return getImprimirRelatorio(fce, DOCUMENTO_OBRIGATORIO);
	}
	
	public MetodoUtil getMetodoAddLocalizacaoLicenciamento() {
		return metodoAddLocalizacaoLicenciamento;
	}
	public void setMetodoAddLocalizacaoLicenciamento(
			MetodoUtil metodoAddLocalizacaoLicenciamento) {
		this.metodoAddLocalizacaoLicenciamento = metodoAddLocalizacaoLicenciamento;
	}
	public List<OutorgaLocalizacaoGeografica> getListarOutorgaLocalizacaoGeografica() {
		return listarOutorgaLocalizacaoGeografica;
	}
	
	public void setListarOutorgaLocalizacaoGeografica(
			List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeografica) {
		this.listarOutorgaLocalizacaoGeografica = listarOutorgaLocalizacaoGeografica;
	}
	public List<OutorgaConcedida> getListaOutorgaConcedida() {
		return listaOutorgaConcedida;
	}
	public void setListaOutorgaConcedida(
			List<OutorgaConcedida> listaOutorgaConcedida) {
		this.listaOutorgaConcedida = listaOutorgaConcedida;
	}
	
	public FceBarragLicencLocaGeo getFceBarragLicencLocaGeo() {
		return fceBarragLicencLocaGeo;
	}
	
	public void setFceBarragLicencLocaGeo(
			FceBarragLicencLocaGeo fceBarragLicencLocaGeo) {
		this.fceBarragLicencLocaGeo = fceBarragLicencLocaGeo;
	}
	
	public FceBarragLicencLocaGeo getFceBarragLicencLocaGeoExclusao() {
		return fceBarragLicencLocaGeoExclusao;
	}
	
	public void setFceBarragLicencLocaGeoExclusao(
			FceBarragLicencLocaGeo fceBarragLicencLocaGeoExclusao) {
		this.fceBarragLicencLocaGeoExclusao = fceBarragLicencLocaGeoExclusao;
	}
	public MetodoUtil getMetodoRetornoInundacao() {
		return metodoRetornoInundacao;
	}
	public void setMetodoRetornoInundacao(MetodoUtil metodoRetornoInundacao) {
		this.metodoRetornoInundacao = metodoRetornoInundacao;
	}
	public LocalizacaoGeografica getLocalizacaoGeograficaInundacao() {
		return localizacaoGeograficaInundacao;
	}
	public void setLocalizacaoGeograficaInundacao(
			LocalizacaoGeografica localizacaoGeograficaInundacao) {
		this.localizacaoGeograficaInundacao = localizacaoGeograficaInundacao;
	}
	public MetodoUtil getMetodoRetornoAppReservatorio() {
		return metodoRetornoAppReservatorio;
	}
	public void setMetodoRetornoAppReservatorio(
			MetodoUtil metodoRetornoAppReservatorio) {
		this.metodoRetornoAppReservatorio = metodoRetornoAppReservatorio;
	}
	public LocalizacaoGeografica getLocalizacaoGeograficaAppReservatorio() {
		return localizacaoGeograficaAppReservatorio;
	}
	public void setLocalizacaoGeograficaAppReservatorio(
			LocalizacaoGeografica localizacaoGeograficaAppReservatorio) {
		this.localizacaoGeograficaAppReservatorio = localizacaoGeograficaAppReservatorio;
	}
	public MetodoUtil getMetodoRetornoPontoInicial() {
		return metodoRetornoPontoInicial;
	}
	public void setMetodoRetornoPontoInicial(MetodoUtil metodoRetornoPontoInicial) {
		this.metodoRetornoPontoInicial = metodoRetornoPontoInicial;
	}
	public MetodoUtil getMetodoRetornoPontoFinal() {
		return metodoRetornoPontoFinal;
	}
	public void setMetodoRetornoPontoFinal(MetodoUtil metodoRetornoPontoFinal) {
		this.metodoRetornoPontoFinal = metodoRetornoPontoFinal;
	}
	public MetodoUtil getMetodoRetornoSuprimida() {
		return metodoRetornoSuprimida;
	}
	public void setMetodoRetornoSuprimida(MetodoUtil metodoRetornoSuprimida) {
		this.metodoRetornoSuprimida = metodoRetornoSuprimida;
	}
	public LocalizacaoGeografica getLocalizacaoGeograficaSuprimida() {
		return localizacaoGeograficaSuprimida;
	}
	public void setLocalizacaoGeograficaSuprimida(
			LocalizacaoGeografica localizacaoGeograficaSuprimida) {
		this.localizacaoGeograficaSuprimida = localizacaoGeograficaSuprimida;
	}
	
}