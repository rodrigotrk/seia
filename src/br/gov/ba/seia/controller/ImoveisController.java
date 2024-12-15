package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoImovel;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.ReservaLegalTramite;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ReservaLegalAverbadaService;
import br.gov.ba.seia.service.ReservaLegalTramiteService;
import br.gov.ba.seia.service.TipoArlService;
import br.gov.ba.seia.service.TipoVinculoImovelService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("imoveisController")
@ViewScoped
public class ImoveisController extends SeiaControllerAb {

	private UIForm formularioASerLimpo;
	private String itr;
	private Collection<ImovelRural> listaImoveis;
	private Collection<Imovel> listaImoveisTabela;
	private Collection<TipoVinculoImovel> listaTipoVinculos;
	private Collection<TipoArl> listaTipoArl;
	private DataModel<Pessoa> listaPessoas;
	private Collection<PessoaImovel> listaPessoaImoveis;
	private TipoArl tipoArl;
	private ImovelRural imovelRural;
	private ImovelRural imovelSelecionado;
	private Empreendimento empreendimento;
	private Pessoa requerente;
	private Endereco endereco;
	private TipoVinculoImovel tipoVinculoImovel;
	private PessoaImovel pessoaiMovel;
	private EmpreendimentoImovel empreendimentoImovel;
	private String areaHa;
	private String propriedade;
	private String folhas;
	private String livro;
	private String numReg;
	private String cartorio;
	private String comarca;
	private String CCIRINCRA;
	private String denominacao;
	private String numProcessoPara;
	private String inema_numero_processo;
	private String lblNumeroMatricula;
	private Boolean novaDenominacao;
	private Boolean adicionaProprietario;
	private Boolean editMode;
	private Boolean editModeProprietario;
	private Boolean visualizaImoveisTabela;
	private Boolean visualizaListaPessoasImoveis;
	private Boolean temPassivoAmbiental;
	private Boolean aderiuPARA;
	private Boolean temItr;
	private Boolean aderePARA;
	private Boolean temAverbado;
	private Boolean temProcesso;
	private Imovel imovelExcluir;
	private Double areaAverbada;
	private Boolean temImovelSelecionado = false;
	private Boolean mostraListaVertices;
	private Boolean checkTemAverbado;
	//LOCALIZACAO GEOGRAFICA
	private LocalizacaoGeografica localizacaoGeograficaSelecionada;
	private String grausLatitudeLoc;
	private String minutosLatitudeLoc;
	private String segundosLatitudeLoc;
	private String grausLongitudeLoc;
	private String minutosLongitudeLoc;
	private String segundosLongitudeLoc;
	private String fracaoGrauLatitudeLoc;
	private String fracaoGrauLongitudeLoc;
	private DadoGeografico verticeLoc;
	private DadoGeografico verticeExclusaoLoc;
	private String grausLatitude;
	private String minutosLatitude;
	private String segundosLatitude;
	private String grausLongitude;
	private String minutosLongitude;
	private String segundosLongitude;
	private String fracaoGrauLatitude;
	private String fracaoGrauLongitude;
	private SistemaCoordenada datum;
	//FIM LOCALIZAÇAO
	private int OUTRO = -1;
	@EJB
	private TipoVinculoImovelService tipoVinculoImovelService;
	@EJB
	private TipoArlService tipoArlService;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	@EJB
	private DatumService serviceDatum;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private ReservaLegalAverbadaService reservaLegalAverbadaService;
	@EJB
	private ReservaLegalTramiteService reservaLegalTramiteService;
	// Dados do Proprietário
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	private Boolean telaCpf;
	private Boolean telaCnpj;
	private Boolean flagTableParticipacaoAcionaria;
	private Boolean enableButtonAddParticipacaoAcionaria;
	private PessoaJuridica pessoaJuridica;
	private Pessoa pessoaPersist;
	private PessoaFisica pessoaFisicaPersist;
	private PessoaJuridica pessoaJuridicaPersist;
	private Boolean enableBotaoPesquisa;
	private Boolean enableFormPessoaFisica;
	private Boolean enableFormPessoaJuridica;
	private Boolean disableBtnAssociarImovel;

	private String labelBtnSalvar;
	private Boolean visualizaBtnAssociar; 
	
	@PostConstruct
	public void init() {
		temItr = false;
		lblNumeroMatricula = "Número de Matrícula";
		propriedade = "1";
		carregarTipoVinculos();
		carregarTipoArls();
		editMode = false;
		editModeProprietario = false;
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridica = new PessoaJuridica();
		pessoaJuridicaPersist = new PessoaJuridica();
		imovelRural = new ImovelRural();
		novaDenominacao = true;
		denominacao = "";
		adicionaProprietario = false;
		tipoVinculoImovel = new TipoVinculoImovel();
		listaImoveisTabela = new ArrayList<Imovel>();
		listaPessoaImoveis = new ArrayList<PessoaImovel>();
		this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
		carregarImoveisEmpreendimento();
		enableBotaoPesquisa = true;
		temPassivoAmbiental = null;
		aderiuPARA = null;
		inema_numero_processo = "";
		temImovelSelecionado = false;
		this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		this.verticeLoc = new DadoGeografico();
		this.datum = new SistemaCoordenada();
		this.verticeExclusaoLoc = new DadoGeografico();
		labelBtnSalvar=ResourceBundle.getBundle("/Bundle").getString("btn_salvar");
		visualizaBtnAssociar = Boolean.FALSE;
	}

	private void carregarTipoVinculos() {
		try {
			listaTipoVinculos = tipoVinculoImovelService.listarTipoVinculoImoveis();
			listaTipoVinculos.remove(4);
			listaTipoVinculos.remove(3);
			listaTipoVinculos.remove(2);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void carregarTipoArls() {
		try {
			listaTipoArl = tipoArlService.listarTipoArls();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void consultarItr() {
		try {
			editMode = Boolean.FALSE;
			listaImoveis = imovelRuralService.listarImoveisPorItrAndEmpreendimento(itr, this.empreendimento.getIdeEmpreendimento());
			ImovelRural i = new ImovelRural();
			this.imovelRural = new ImovelRural();
			this.resetarDadosImovelSelecionado();
			i.setDesDenominacao("Outro");
			i.setIdeImovelRural(OUTRO);
			listaImoveis.add(i);
			if (!listaImoveis.isEmpty()) {
				temItr = true;
				novaDenominacao = false;
			} else {
				temItr = false;
				novaDenominacao = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void resetarDadosImovelSelecionado() {
		imovelSelecionado = null;
		temImovelSelecionado = false;
	}

	public void mudaTipoPropriedade(ValueChangeEvent event) {
		if (event.getNewValue().toString().equals("1")) {
			lblNumeroMatricula = "Número de Matrícula";
		} else {
			lblNumeroMatricula = "Número de registro do documento de posse em cartório";
		}
	}

	public void mudaDenominacao(ValueChangeEvent event) throws Exception {
		if (!Util.isNullOuVazio(event.getNewValue())) {
			ImovelRural iMovel = (ImovelRural) event.getNewValue();
			if (Util.isNullOuVazio(iMovel) || Util.isNullOuVazio(iMovel.getIdeImovelRural())) {
				imovelSelecionado = new ImovelRural();
				init();
				disableBtnAssociarImovel = true;
			}else if(iMovel.getIdeImovelRural().equals(-1)){ 
				imovelSelecionado = new ImovelRural();
				init();
				novaDenominacao = true;
				editMode = true;
				temImovelSelecionado = true;
				disableBtnAssociarImovel = false;
			}
			else{
				carregarEconfiguraTela(iMovel);
				carregarPessoasImoveis();
				montarObjetoTela();
				novaDenominacao = true;
				editMode = false;
				temImovelSelecionado = false;
				disableBtnAssociarImovel = false;
			}
		}
	}

	private void carregarEconfiguraTela(ImovelRural iMovel) {
		if (!Util.isNullOuVazio(iMovel) && iMovel.getIdeImovelRural() == OUTRO) {
			novaDenominacao = true;
			imovelRural = new ImovelRural();
			editMode = true;
			listaPessoaImoveis.clear();
			denominacao = "";
			limparTela();
			this.areaHa = "";
			this.tipoArl = null;
			this.areaAverbada = null;
		} else {
			novaDenominacao = false;
		}
		try {
			if (iMovel.getIdeImovelRural() != OUTRO) {
				iMovel = imovelRuralService.carregar(iMovel.getIdeImovelRural());
				this.imovelRural = iMovel;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void mudaPassivoAmbiental(ValueChangeEvent event) {
		Boolean vlo = (Boolean) event.getNewValue();
		if (vlo) {
			temPassivoAmbiental = true;
		} else {
			temPassivoAmbiental = false;
		}
	}

	public void mudaAderiuPARA(ValueChangeEvent event) {
		Boolean vlo = (Boolean) event.getNewValue();
		if (vlo) {
			aderiuPARA = true;
		} else {
			aderiuPARA = false;
		}
	}

	public void mudaProcesso(ValueChangeEvent event) {
	}
	
//	public Boolean libera//

	public void salvarReqImovel() {
		if (Util.isNullOuVazio(this.imovelRural) || Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())) {
			if (montarImovelRural()) {
				this.adicionaProprietario = true;
				carregarImoveisEmpreendimento();
			}
			if (tipoVinculoImovel.getIdeTipoVinculoImovel() == 1) {//se for proprietário atualize a tabela;
				carregarPessoasImoveis();
				this.denominacao = this.imovelRural.getDesDenominacao();
			}
			ContextoUtil.getContexto().setBloquearLocGeoImovelDepoisDeImovelSalvo(Boolean.FALSE);
			ContextoUtil.getContexto().setBloquearEnderecoImovelDepoisDeImovelSalvo(Boolean.FALSE);
			editMode = true;
		} 
	}
	
	public void salvar() {
		if (Util.isNullOuVazio(this.imovelRural) || Util.isNullOuVazio(this.imovelRural.getIdeImovelRural())) {
			if(!this.validarImovelRural()){
				return;
			}
			if (montarImovelRural()) {
				this.adicionaProprietario = true;
				carregarImoveisEmpreendimento();
			}
			if (tipoVinculoImovel.getIdeTipoVinculoImovel() == 1) {//se for proprietário atualize a tabela;
				carregarPessoasImoveis();
				this.denominacao = this.imovelRural.getDesDenominacao();
			}
			ContextoUtil.getContexto().setBloquearLocGeoImovelDepoisDeImovelSalvo(Boolean.FALSE);
			ContextoUtil.getContexto().setBloquearEnderecoImovelDepoisDeImovelSalvo(Boolean.FALSE);
			editMode = true;
		} else {
			if (montarImovelRuralEditar()) {
				this.adicionaProprietario = true;
				carregarImoveisEmpreendimento();
			}
			limparTela();
			this.itr = null;
			this.denominacao = null;
			resetarDadosImovelSelecionado();
			this.imovelRural.setValArea(null);
			this.imovelRural.setNumFolha(null);
			this.imovelRural.setDesLivro(null);
			this.imovelRural.setNumMatricula(null);
			this.imovelRural.setDesCartorio(null);
			this.imovelRural.setDesComarca(null);
			this.imovelRural.setNumCcir(null);
			this.temProcesso = null;
			this.inema_numero_processo = null;
			this.temImovelSelecionado = true;
			//seta valor para poder bloquear o painel com as informações de localização geográfica
			ContextoUtil.getContexto().setBloquearLocGeoImovelDepoisDeImovelSalvo(Boolean.TRUE);
			//setando valores para não exibir tabela de proprietarios e desabilitar botão para adição dos mesmo, já que não há imovel selecionado.
			this.visualizaListaPessoasImoveis = false;
			this.adicionaProprietario = false;
			//seta valor para poder bloquear o painel com as informações de endereço do imóvel
			ContextoUtil.getContexto().setBloquearEnderecoImovelDepoisDeImovelSalvo(Boolean.TRUE);
			if(!Util.isNullOuVazio(listaPessoaImoveis) && !listaPessoaImoveis.isEmpty())
				this.listaPessoaImoveis = new ArrayList<PessoaImovel>();
		}
	}
	
//	public Boolean getIrtIsNull(){
//		if(this.)
//	}

	private boolean validarImovelRural() {
		
		boolean valido = true;
		
		if(Util.isNull(this.temPassivoAmbiental)){
			JsfUtil.addErrorMessage("O campo Tem passivo ambiental? é de preenchimento obrigatório");
			valido =false;
		}
		
		else if((temPassivoAmbiental && Util.isNull(this.aderiuPARA))){
			JsfUtil.addErrorMessage("O campo já aderiu ao PARA? é de preenchimento obrigatório");
			valido =false;
		}
		
		else if((!temPassivoAmbiental && Util.isNull(this.aderePARA)) || (temPassivoAmbiental && !this.aderiuPARA && Util.isNull(this.aderePARA))){
			JsfUtil.addErrorMessage("O campo Deseja aderir ao PARA? é de preenchimento obrigatório");
			valido =false;
		}
		
		if(Util.isNull(this.temAverbado)){
			JsfUtil.addErrorMessage("O campo Reserva Legal é de preenchimento obrigatório");
			valido =false;
		}
		
		return valido;
	}

	private Boolean montarImovelRuralEditar() {
		//		ImovelRuralDTO imovelRuralDto = new ImovelRuralDTO();
		this.imovelRural.setNumItr(this.itr);
		this.imovelRural.setDesDenominacao(this.denominacao);
		this.imovelRural.setIndAderirPara(this.aderePARA);
		this.imovelRural.setIndParaAderido(this.aderiuPARA);
		this.imovelRural.setIndPassivoAmbiental(this.temPassivoAmbiental);
		this.imovelRural.setNumProcessoPara(numProcessoPara);
		this.imovelRural.setIndReservaLegal(temAverbado);
		if(temAverbado){
			ReservaLegalAverbada reservaLegalAverb = new ReservaLegalAverbada();
			try {
//				List<ReservaLegalAverbada> listReservaLegalAverb = reservaLegalAverbadaService.listarReservaLegalAverbadaByImovelRural(imovelRural);
//				if(!listReservaLegalAverb.isEmpty()){
//					reservaLegalAverb = listReservaLegalAverb.get(0);
//				}
//				else{
//					reservaLegalAverb.setIdeImovel(this.imovelRural);
//				reservaLegalAverb.setValAreaReserva(areaAverbada);
//				reservaLegalAverb.setIdeTipoArl(tipoArl);
//				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				System.out.println(e);
			}
		}
		
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada()) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao())) {
			this.imovelRural.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		
		try {
			List<ReservaLegalTramite> listaReservaLegalTramite = reservaLegalTramiteService.filtrarByImovel(imovelRural);
			ReservaLegalTramite reservaLegalTramite = new ReservaLegalTramite();
			
			if (!Util.isNull(listaReservaLegalTramite) && !listaReservaLegalTramite.isEmpty() && temAverbado) {
				reservaLegalTramite = (ReservaLegalTramite) listaReservaLegalTramite.get(0);
				reservaLegalTramite.setIndTramite(temProcesso);
				
				
				if(!Util.isNullOuVazio(temProcesso) && temProcesso)
					reservaLegalTramite.setNumProcesso(inema_numero_processo);
				else
					reservaLegalTramite.setNumProcesso(null);
				reservaLegalTramite.setIdeImovel(imovelRural);
			}else{
				reservaLegalTramite.setIndTramite(temProcesso);
				reservaLegalTramite.setNumProcesso(null);
			}
			
			boolean imovelExistente = false;
			//			imovelRuralDto.setEmpreendimento(this.empreendimento);
			//			imovelRuralDto.setImovelRural(this.imovelRural);
			if (!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getImovelCollection())) {
				for (Imovel imov : empreendimento.getImovelCollection()) {
					if (imov.getIdeImovel().equals(this.imovelRural.getIdeImovelRural()))
						imovelExistente = true;
				}
			}
			if (!imovelExistente)
				imovelRuralService.associarImovel(this.imovelRural, empreendimento);
			imovelRuralService.atualizar(this.imovelRural);
			//Implementação para persistir o relacionamento de pessoa com imóvel e o tipo de vínculo. - Antes não tinha nada. -Autor desse código MICAEL
			PessoaImovel pessoaIm = new PessoaImovel();
			pessoaIm.setIdePessoa(this.requerente);
			pessoaIm.setIdeTipoVinculoImovel(this.tipoVinculoImovel);
			pessoaIm.setIdeImovel(this.imovelRural.getImovel());
			if(Util.isNullOuVazio(pessoaIm.getIdePessoaImovel()))
				imovelRuralService.salvarImovelPessoa(pessoaIm);
			else
				imovelRuralService.atualizarPessoaImovel(pessoaIm);
			
			habilitarTela(this.imovelRural);
			JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
			novaDenominacao = false;
			editMode = false;
			return true;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Salvar Imóvel");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}
	}

	private Boolean montarImovelRural() {
		try {
			ReservaLegalAverbada reserva = null;
			ReservaLegalTramite reservaTramite = null;
			ImovelRuralDTO imovelRuralDto = new ImovelRuralDTO();
			Imovel i = new Imovel();
			this.imovelRural.setImovel(i);
			this.imovelRural.getImovel().setIdeTipoImovel(new TipoImovel(1));//Fixo Imovel Rural
			//this.imovelRural.getImovel().setIdeEndereco(new Endereco(1));
			this.imovelRural.getImovel().setDtcCriacao(new Date());
			this.imovelRural.getImovel().setIndExcluido(false);
			this.imovelRural.setNumItr(this.itr);
			this.imovelRural.setDesDenominacao(this.denominacao);
			this.imovelRural.setIndAderirPara(this.aderePARA);
			this.imovelRural.setIndParaAderido(this.aderiuPARA);
			this.imovelRural.setIndPassivoAmbiental(this.temPassivoAmbiental);
			this.imovelRural.setNumProcessoPara(numProcessoPara);
			PessoaImovel pessoaIm = new PessoaImovel();
			pessoaIm.setIdePessoa(this.requerente);
			pessoaIm.setIdeTipoVinculoImovel(this.tipoVinculoImovel);
			pessoaIm.setIdeImovel(this.imovelRural.getImovel());
			//AVERBADO
			if (temAverbado) {
				reserva = new ReservaLegalAverbada();
//				reserva.setIdeTipoArl(tipoArl);
//				reserva.setIdeImovel(this.imovelRural);
//				reserva.setValAreaReserva(areaAverbada);
				this.imovelRural.setIndReservaLegal(true);
//				imovelRuralDto.setReservaAverbada(reserva);
			}
			//Processo INEMA
			reservaTramite = new ReservaLegalTramite();
			reservaTramite.setIdeImovel(this.imovelRural);
			reservaTramite.setIndTramite(Util.isNull(temProcesso) ? false : temProcesso);
			reservaTramite.setNumProcesso(inema_numero_processo);
			imovelRuralDto.setEmpreendimento(this.empreendimento);
			imovelRuralDto.setImovelRural(this.imovelRural);
			imovelRuralDto.setPessoaImovel(pessoaIm);
			imovelRuralDto.setReservaTramite(reservaTramite);
			if (imovelRuralDto.getEmpreendimento().getImovelCollection() == null) {
				imovelRuralDto.getEmpreendimento().setImovelCollection(new ArrayList<Imovel>());
			}
			imovelRuralDto.getEmpreendimento().getImovelCollection().add(imovelRural.getImovel());
			imovelRuralServiceFacade.salvarImovelRural(imovelRuralDto);
			addAtributoSessao("IMOVEL_RURAL", this.imovelRural);
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			consultarItr();
			this.imovelRural = imovelRuralDto.getImovelRural();
			habilitarTela(imovelRuralDto.getImovelRural());
			return true;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Cadastrar Imóvel");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}
	}

	public void habilitarTela(ImovelRural imovelRural) {
		imovelSelecionado = imovelRural;
		temImovelSelecionado = true;
	}
	
	public Boolean empreendimentoNaoDonoDoImovel(Imovel imovel){
		try {
			if(imovelRuralServiceFacade.isPrimeiroEmpreendimentoAssociado(imovel, empreendimento))
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, e.toString());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return Boolean.FALSE;
		}
	}

	public void editar() {
		editMode = true;
		novaDenominacao = true;
		adicionaProprietario = true;
		montarObjetoTela();
		carregarPessoasImoveis();
	}

	private void montarObjetoTela() {
		if (Util.isNullOuVazio(this.imovelRural)
				|| (Util.isNullOuVazio(this.imovelRural.getNumItr()) && Util.isNullOuVazio(this.imovelRural.getIdeImovelRural()) && Util.isNullOuVazio(this.imovelRural.getNumRegistro()) && Util.isNullOuVazio(this.imovelRural.getNumProcessoPara()))) {
			return;
		} else if (!Util.isNullOuVazio(this.imovelRural.getIdeImovelRural()) && this.imovelRural.getIdeImovelRural() < 1)
			return;
		this.itr = this.imovelRural.getNumItr();
		aderiuPARA = this.imovelRural.getIndParaAderido();
		aderePARA = this.imovelRural.getIndAderirPara();
		temPassivoAmbiental = this.imovelRural.getIndPassivoAmbiental();
		this.numProcessoPara = this.imovelRural.getNumProcessoPara();
		this.denominacao = this.imovelRural.getDesDenominacao();
		this.temPassivoAmbiental = this.imovelRural.getIndPassivoAmbiental();
		this.temAverbado = this.imovelRural.getIndReservaLegal(); 
		List<ReservaLegalTramite> listaReservaLegalTramite = new ArrayList<ReservaLegalTramite>();
		try {
			listaReservaLegalTramite = reservaLegalTramiteService.filtrarByImovel(imovelRural);
		} catch (Exception e1) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
		}
		if (!Util.isNull(listaReservaLegalTramite) && !listaReservaLegalTramite.isEmpty()) {
			ReservaLegalTramite reservaLegalTramite = listaReservaLegalTramite.get(0);
			inema_numero_processo = reservaLegalTramite.getNumProcesso();
			temProcesso = reservaLegalTramite.getIndTramite();
		}
		if (Util.isNullOuVazio(this.imovelRural.getImovel()))
			this.endereco = null;
		else
			this.endereco = this.imovelRural.getImovel().getIdeEndereco();
		addAtributoSessao("IMOVEL_RURAL", this.imovelRural);
		if (!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica())) {
			try {
				this.localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(this.imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
					this.datum = localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
		//this.datum = this.imovelRural.getIdeLocalizacaoGeografica().getIdeDatum();
		List<PessoaImovel> listaPessoaImovel = new ArrayList<PessoaImovel>();
		try {
			listaPessoaImovel = imovelRuralService.filtrarPessoasProprietariasOuNaoPorImovel(this.imovelRural.getImovel());
			for (PessoaImovel pessoaImovel : listaPessoaImovel) {
				if(pessoaImovel.getIdePessoa().getIdePessoa() == this.requerente.getIdePessoa())
					this.tipoVinculoImovel = pessoaImovel.getIdeTipoVinculoImovel();
			}
		} catch (Exception e) {
			System.out.println("Requerente ou pessoa imovel são nulos.");
			this.tipoVinculoImovel = listaPessoaImovel.get(0).getIdeTipoVinculoImovel();
		} 
		if(Util.isNullOuVazio(this.tipoVinculoImovel.getIdeTipoVinculoImovel())){
			this.tipoVinculoImovel = new TipoVinculoImovel(1, "Proprietário");
		}
	}

	private void limparTela() {
		aderiuPARA = null;
		aderePARA = null;
		temPassivoAmbiental = null;
		numProcessoPara = "";
		denominacao = "";
		temAverbado = null;
		endereco = new Endereco();
		areaHa = "";
	}

	public void excluirPessoaImovel() {
		try {
			imovelRuralService.removerPessoaImovel(this.pessoaiMovel);
			carregarPessoasImoveis();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void excluirImovel() {
		try {
			imovelRuralServiceFacade.excluirImovel(imovelExcluir);
			carregarImoveisEmpreendimento();
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Excluir Imóvel");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void desasociarImovelEmpreedimento() {
		try {
			imovelRuralServiceFacade.desassociarImovel(imovelExcluir, empreendimento.getIdeEmpreendimento());
			carregarImoveisEmpreendimento();
			limparTela();
			this.itr = null;
			this.denominacao = null;
			resetarDadosImovelSelecionado();
			this.imovelRural.setValArea(null);
			this.imovelRural.setNumFolha(null);
			this.imovelRural.setDesLivro(null);
			this.imovelRural.setNumMatricula(null);
			this.imovelRural.setDesCartorio(null);
			this.imovelRural.setDesComarca(null);
			this.imovelRural.setNumCcir(null);
			this.temProcesso = null;
			this.inema_numero_processo = null;
			this.temImovelSelecionado = true;
			//seta valor para poder bloquear o painel com as informações de localização geográfica
			ContextoUtil.getContexto().setBloquearLocGeoImovelDepoisDeImovelSalvo(Boolean.TRUE);
			//setando valores para não exibir tabela de proprietarios e desabilitar botão para adição dos mesmo, já que não há imovel selecionado.
			this.visualizaListaPessoasImoveis = false;
			this.adicionaProprietario = false;
			//seta valor para poder bloquear o painel com as informações de endereço do imóvel
			ContextoUtil.getContexto().setBloquearEnderecoImovelDepoisDeImovelSalvo(Boolean.TRUE);
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao desassociar Imóvel de um empreendimento");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void carregarImoveisEmpreendimento() {
		listaImoveisTabela.clear();
		try {
			if (!Util.isNullOuVazio(this.empreendimento)) {
				listaImoveisTabela = imovelRuralService.carregarImoveisByEmpreendimento(this.empreendimento);
				
				for (Imovel imovel : listaImoveisTabela) {
					imovel.setTemRequerimentoAssociado(this.imovelRuralService.imovelComRequerimentoAberto(imovel.getIdeImovel()));
				}
				
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void salvarLocalizacaoGeografica() {
		this.imovelRural.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		try {
			imovelRuralService.atualizar(this.imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	// ************** PROPRIETARIO ***********************
	public String salvarProprietario() {
		if (telaCpf) {
			if (!validatePessoaFisica()) {
				return null;
			}
		} else {
			if (!validatePessoaJuridica()) {
				return null;
			}
		}
		if (!Util.isNullOuVazio(pessoaPersist) && !Util.isNullOuVazio(pessoaPersist.getIdePessoa()) && !editModeProprietario) {
			if (existePessoaNaListaProprietarios() && !editModeProprietario) {
				JsfUtil.addErrorMessage("Proprietário já está cadastrado.");
			    return null;
			}
			salvarPessoaImovel();
			limparObjetos();
			carregarPessoasImoveis();
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			return null;
		}
		if (telaCpf) {
			salvarPessoaFisica();
			if (!editModeProprietario)
				salvarPessoaImovel();
		} else {
			salvarPessoaJuridica();
			if (!editModeProprietario)
				salvarPessoaImovel();
		}
		JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
		limparObjetos();
		carregarPessoasImoveis();
		return null;
	}

	private Boolean validatePessoaFisica() {
		Boolean retorno = null;
		Boolean teveErro = false;
		// pesquisa de novo, caso o usuário tente modificar o CPF e salvar 
		PessoaFisica pessoaPesquisa = null;
		try {
			pessoaPesquisa = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaFisicaPersist);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			pessoaPesquisa = null;
		}
		if (Util.isNull(pessoaPesquisa) && !editModeProprietario) {
			if (!Util.isNull(pessoaFisicaPersist.getNumCpf()) && !Util.isEmptyString(pessoaFisicaPersist.getNumCpf())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo CPF é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
				teveErro = true;
			}
			if (!Util.isNull(pessoaFisicaPersist.getNomPessoa()) && !Util.isEmptyString(pessoaFisicaPersist.getNomPessoa())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Nome é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
				teveErro = true;
			}
			if (!Util.isNull(pessoaFisicaPersist.getDtcNascimento())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Data de Nascimento é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
				teveErro = true;
			}
			if (!Util.isNull(pessoaPersist.getDesEmail()) && !Util.isEmptyString(pessoaPersist.getDesEmail())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo E-mail é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
				teveErro = true;
			}
			if (!Util.isNull(pessoaFisicaPersist.getIdePais())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo País é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
				teveErro = true;
			}
		} 
		else {
			retorno = Boolean.TRUE;
			teveErro = false;
		}
		return retorno && !teveErro;
	}

	private Boolean validatePessoaJuridica() {
		Boolean retorno = null;
		// pesquisa de novo, caso o usuário tente modificar o CNPJ e salvar
		PessoaJuridica pessoaJuridicaPesquisa = null;
		try {
			pessoaJuridicaPesquisa = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridicaPersist);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			pessoaJuridicaPesquisa = null;
		}
		if (Util.isNull(pessoaJuridicaPesquisa)) {
			if (!Util.isNull(pessoaJuridicaPersist.getNumCnpj()) && !Util.isEmptyString(pessoaJuridicaPersist.getNumCnpj())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo CNPJ é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
			if (!Util.isNull(pessoaJuridicaPersist.getNomRazaoSocial()) && !Util.isEmptyString(pessoaJuridicaPersist.getNomRazaoSocial())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Razão Social é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
			if (!Util.isNull(pessoaJuridicaPersist.getNomeFantasia()) && !Util.isEmptyString(pessoaJuridicaPersist.getNomeFantasia())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Nome Fantasia é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
			if (!Util.isNull(pessoaJuridicaPersist.getIdeNaturezaJuridica())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Natureza Jurídica é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
			if (!Util.isNull(pessoaJuridicaPersist.getDtcAbertura())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo Data de Abertura é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
			if (!Util.isNull(pessoaPersist) && !Util.isNull(pessoaPersist.getDesEmail())) {
				retorno = Boolean.TRUE;
			} else {
				JsfUtil.addErrorMessage("O campo e-mail é de preenchimento obrigatório.");
				retorno = Boolean.FALSE;
			}
		} else {
			if (!editMode) {
				retorno = Boolean.FALSE;
				JsfUtil.addErrorMessage("CNPJ já está cadastrado para outro usuário!");
			} else {
				retorno = Boolean.TRUE;
			}
		}
		return retorno;
	}

	private void salvarPessoaImovel() {
		PessoaImovel pessoaIm = new PessoaImovel();
		pessoaIm.setIdeImovel(this.imovelRural.getImovel());
		pessoaIm.setIdePessoa(pessoaPersist);
		pessoaIm.setIdeTipoVinculoImovel(new TipoVinculoImovel(1)); //proprietário.
		try {
			imovelRuralService.salvarImovelPessoa(pessoaIm);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	private void salvarPessoaFisica() {
		try {
			if (Util.isNull(pessoaPersist.getIdePessoa())) {
				pessoaPersist.setDtcCriacao(new Date());				
			}
			pessoaPersist.setIndExcluido(Boolean.FALSE);
			pessoaFisicaPersist.setPessoa(pessoaPersist);
			pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaFisicaPersist);
			// salvarParticipacaoAcionaria(TipoAcionistaEnum.PESSOA_FISICA);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	private void salvarPessoaJuridica() {
		try {
			if (Util.isNull(pessoaPersist.getIdePessoa())) {
				pessoaPersist.setDtcCriacao(new Date());				
			}
			pessoaPersist.setIndExcluido(Boolean.FALSE);
			pessoaJuridicaPersist.setPessoa(pessoaPersist);
			pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridicaPersist);
			// salvarParticipacaoAcionaria(TipoAcionistaEnum.PESSOA_JURIDICA);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void alterarLayout(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			this.telaCnpj = false;
			this.telaCpf = true;
		} else {
			this.telaCpf = false;
			this.telaCnpj = true;
		}
	}

	public String limparObjetosAction() {
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		return null;
	}

	public void limparObjetos() {
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		setEnableBotaoPesquisa(true);
		enableFormPessoaFisica = false;
		enableFormPessoaJuridica = false;
		limparComponentesFormulario(formularioASerLimpo);
	}

	public void pesquisarPessoa() {
		if (telaCpf) {
			pesquisarPessoaFisica();
		} else {
			pesquisarPessoaJuridica();
		}
	}

	public void pesquisarPessoaFisica() {
		try {
			String cpf = pessoaFisicaPersist.getNumCpf();
			pessoaFisicaPersist = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaFisicaPersist);
			if (Util.isNullOuVazio(pessoaFisicaPersist)) {
				pessoaFisicaPersist = new PessoaFisica();
				pessoaFisicaPersist.setNumCpf(cpf);
				pessoaPersist = new Pessoa();
				enableFormPessoaFisica = Boolean.TRUE;
			} else {
				pessoaPersist = pessoaFisicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public void pesquisarPessoaJuridica() {
		try {
			String cnpj = pessoaJuridicaPersist.getNumCnpj();
			pessoaJuridicaPersist = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridicaPersist);
			if (Util.isNullOuVazio(pessoaJuridicaPersist)) {
				pessoaJuridicaPersist = new PessoaJuridica();
				pessoaJuridicaPersist.setNumCnpj(cnpj);
				pessoaPersist = new Pessoa();
				enableFormPessoaJuridica = Boolean.TRUE;
			} else {
				pessoaPersist = pessoaJuridicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public Boolean existePessoaNaListaProprietarios() {
		try {
			carregarPessoasImoveis();
			if (telaCpf) {
				return existePessoaFisicaNaLista(pessoaFisicaPersist, listaPessoaImoveis);
			}
			if (!telaCpf) {
				return existePessoaJuridicaNaLista(pessoaJuridicaPersist, listaPessoaImoveis);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		return false;
	}

	private Boolean existePessoaFisicaNaLista(PessoaFisica pessoaFisica, Collection<PessoaImovel> lista) {
		for (PessoaImovel p : lista) {
			if (!Util.isNullOuVazio(p.getIdePessoa().getPessoaFisica()) && p.getIdePessoa().getPessoaFisica().getNumCpf().equals(pessoaFisicaPersist.getNumCpf())) {
				return true;
			}
		}
		return false;
	}

	private Boolean existePessoaJuridicaNaLista(PessoaJuridica pessoaJuridica, Collection<PessoaImovel> lista) {
		for (PessoaImovel p : lista) {
			if (!Util.isNullOuVazio(p.getIdePessoa().getPessoaJuridica()) && p.getIdePessoa().getPessoaJuridica().getNumCnpj().equals(pessoaJuridicaPersist.getNumCnpj())) {
				return true;
			}
		}
		return false;
	}
	
	public void mudaValorTemAverbado(){
		if(temAverbado){
			tipoArl = null;
			areaAverbada = null;
			temProcesso = null;
			inema_numero_processo = null;
		}else{
			tipoArl = null;
			areaAverbada = null;
			temProcesso = null;
			inema_numero_processo = null;
		}
	}

	public String prepararParaEdicao() {
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		setEnableBotaoPesquisa(true);
		limparComponentesFormulario(formularioASerLimpo);
		enableFormPessoaFisica = true;
		if (!Util.isNullOuVazio(pessoaPersist.getPessoaFisica())) {
			pessoaFisicaPersist = pessoaPersist.getPessoaFisica();
			selecionarRadioPessoaFisica();
		} else {
			pessoaJuridicaPersist = pessoaPersist.getPessoaJuridica();
			selecionarRadioPessoaJuridica();
		}
		return null;
	}

	private void selecionarRadioPessoaFisica() {
		telaCnpj = Boolean.FALSE;
		telaCpf = Boolean.TRUE;
	}

	private void selecionarRadioPessoaJuridica() {
		telaCnpj = Boolean.TRUE;
		telaCpf = Boolean.FALSE;
	}

	// ************** FIM PROPRIETARIO ***********************
	// ************** INICIO LOCALIZAÇAO ***********************	
	public void calculaFracaoGrauLatitudeLoc(ActionEvent event) {
		try {
			this.fracaoGrauLatitudeLoc = PostgisUtil.calculaFracaoGrauLatitude(grausLatitudeLoc, minutosLatitudeLoc, segundosLatitudeLoc).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void calculaFracaoGrauLongitudeLoc(ActionEvent event) {
		try {
			this.fracaoGrauLongitudeLoc = PostgisUtil.calculaFracaoGrauLongitude(grausLongitudeLoc, minutosLongitudeLoc, segundosLongitudeLoc).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void calculaFracaoGrauLatitude(ActionEvent event) {
		try {
			this.fracaoGrauLatitude = PostgisUtil.calculaFracaoGrauLatitude(grausLatitude, minutosLatitude, segundosLatitude).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void calculaFracaoGrauLongitude(ActionEvent event) {
		try {
			this.fracaoGrauLongitude = PostgisUtil.calculaFracaoGrauLongitude(grausLongitude, minutosLongitude, segundosLongitude).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void definirDatum(AjaxBehaviorEvent event) {
		try {
			SistemaCoordenada datum = serviceDatum.carregar(this.datum.getIdeSistemaCoordenada());
			this.localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(datum);
			this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
			this.localizacaoGeograficaSelecionada.setDtcExclusao(null);
			this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(null);
			this.localizacaoGeograficaSelecionada.setIndExcluido(Boolean.FALSE);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void incluirVertice() {
		if (!Util.isNullOuVazio(this.fracaoGrauLatitudeLoc) && !Util.isNullOuVazio(this.fracaoGrauLongitudeLoc)) {
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(this.fracaoGrauLatitudeLoc), Double.parseDouble(this.fracaoGrauLongitudeLoc));
				this.verticeLoc.setCoordGeoNumerica(ponto.toString());
				this.verticeLoc.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				localizacaoGeograficaSelecionada.getDadoGeograficoCollection().add(verticeLoc);
				salvarLocalizacaoGeografica();
				RequestContext.getCurrentInstance().execute("dlgIncluirVertice.hide()");
				limparFormLocGeografica();
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
		}
	}

	public void excluirVertice() {
		try {
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusaoLoc);
			this.limparFormLocGeografica();
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void limparFormLocGeografica() {
		this.grausLatitudeLoc = "";
		this.minutosLatitudeLoc = "";
		this.segundosLatitudeLoc = "";
		this.grausLongitudeLoc = "";
		this.minutosLongitudeLoc = "";
		this.segundosLongitudeLoc = "";
		this.fracaoGrauLatitudeLoc = "";
		this.fracaoGrauLongitudeLoc = "";
		this.verticeLoc = new DadoGeografico();
	}

	// ************** FIM LOCALIZAÇAO ***********************
	public String getAreaHa() {
		return areaHa;
	}

	public void setAreaHa(String areaHa) {
		this.areaHa = areaHa;
	}

	public String getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}

	public String getFolhas() {
		return folhas;
	}

	public void setFolhas(String folhas) {
		this.folhas = folhas;
	}

	public String getLivro() {
		return livro;
	}

	public void setLivro(String livro) {
		this.livro = livro;
	}

	public String getNumReg() {
		return numReg;
	}

	public void setNumReg(String numReg) {
		this.numReg = numReg;
	}

	public String getCartorio() {
		return cartorio;
	}

	public void setCartorio(String cartorio) {
		this.cartorio = cartorio;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getCCIRINCRA() {
		return CCIRINCRA;
	}

	public void setCCIRINCRA(String cCIRINCRA) {
		CCIRINCRA = cCIRINCRA;
	}

	public String getItr() {
		return itr;
	}

	public void setItr(String itr) {
		this.itr = itr;
	}

	public Collection<ImovelRural> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(Collection<ImovelRural> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}

	public Boolean isTemItr() {
		return temItr;
	}

	public void setTemItr(Boolean temItr) {
		this.temItr = temItr;
	}

	public Collection<TipoVinculoImovel> getListaTipoVinculos() {
		return listaTipoVinculos;
	}

	public void setListaTipoVinculos(Collection<TipoVinculoImovel> listaTipoVinculos) {
		this.listaTipoVinculos = listaTipoVinculos;
	}

	public String getLblNumeroMatricula() {
		return lblNumeroMatricula;
	}

	public void setLblNumeroMatricula(String lblNumeroMatricula) {
		this.lblNumeroMatricula = lblNumeroMatricula;
	}

	public Boolean getTemAverbado() {
		return temAverbado;
	}

	public void setTemAverbado(Boolean temAverbado) {
		this.temAverbado = temAverbado;
	}

	public Boolean isTemProcesso() {
		return temProcesso;
	}

	public void setTemProcesso(Boolean temProcesso) {
		this.temProcesso = temProcesso;
	}

	public Double getAreaAverbada() {
		return areaAverbada;
	}

	public void setAreaAverbada(Double areaAverbada) {
		this.areaAverbada = areaAverbada;
	}

	public Collection<TipoArl> getListaTipoArl() {
		return listaTipoArl;
	}

	public void setListaTipoArl(Collection<TipoArl> listaTipoArl) {
		this.listaTipoArl = listaTipoArl;
	}

	public TipoArl getTipoArl() {
		return tipoArl;
	}

	public void setTipoArl(TipoArl tipoArl) {
		this.tipoArl = tipoArl;
	}

	public String getInema_numero_processo() {
		return inema_numero_processo;
	}

	public void setInema_numero_processo(String inema_numero_processo) {
		this.inema_numero_processo = inema_numero_processo;
	}

	public Boolean isAderePARA() {
		return aderePARA;
	}

	public void setAderePARA(Boolean aderePARA) {
		this.aderePARA = aderePARA;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
		carregarEconfiguraTela(imovelRural);
		editar();
	}

	// DADOS DO PROPRIETáRIO
	public Boolean getTelaCpf() {
		return telaCpf;
	}

	public void setTelaCpf(Boolean telaCpf) {
		this.telaCpf = telaCpf;
	}

	public Boolean getTelaCnpj() {
		return telaCnpj;
	}

	public void setTelaCnpj(Boolean telaCnpj) {
		this.telaCnpj = telaCnpj;
	}

	public Boolean getFlagTableParticipacaoAcionaria() {
		return flagTableParticipacaoAcionaria;
	}

	public void setFlagTableParticipacaoAcionaria(Boolean flagTableParticipacaoAcionaria) {
		this.flagTableParticipacaoAcionaria = flagTableParticipacaoAcionaria;
	}

	public Boolean getEnableButtonAddParticipacaoAcionaria() {
		return enableButtonAddParticipacaoAcionaria;
	}

	public void setEnableButtonAddParticipacaoAcionaria(Boolean enableButtonAddParticipacaoAcionaria) {
		this.enableButtonAddParticipacaoAcionaria = enableButtonAddParticipacaoAcionaria;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public Pessoa getPessoaPersist() {
		return pessoaPersist;
	}

	public void setPessoaPersist(Pessoa pessoaPersist) {
		this.pessoaPersist = pessoaPersist;
		prepararParaEdicao();
	}

	public PessoaFisica getPessoaFisicaPersist() {
		return pessoaFisicaPersist;
	}

	public void setPessoaFisicaPersist(PessoaFisica pessoaFisicaPersist) {
		this.pessoaFisicaPersist = pessoaFisicaPersist;
	}

	public PessoaJuridica getPessoaJuridicaPersist() {
		return pessoaJuridicaPersist;
	}

	public void setPessoaJuridicaPersist(PessoaJuridica pessoaJuridicaPersist) {
		this.pessoaJuridicaPersist = pessoaJuridicaPersist;
	}

	public DataModel<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public void setListaPessoas(DataModel<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	public String getDenominacao() {
		return denominacao;
	}

	public void setDenominacao(String denominacao) {
		this.denominacao = denominacao;
	}

	public Boolean isNovaDenominacao() {
		return novaDenominacao;
	}

	public void setNovaDenominacao(Boolean novaDenominacao) {
		this.novaDenominacao = novaDenominacao;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public ImovelRural getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(ImovelRural imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
		/*
		 * if(Util.isNullOuVazio(this.imovelSelecionado)) { init(); return; } if( Util.isNullOuVazio(imovelSelecionado) || imovelSelecionado.getIdeImovelRural() != OUTRO){ try { imovelRural =
		 * imovelRuralService.Carregar(this.imovelSelecionado.getIdeImovelRural()); } catch (Exception e) { Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log } }
		 */
	}

	public void carregarPessoasImoveis() {
		try {
			listaPessoaImoveis = imovelRuralService.filtrarPROPRIETARIOImovel(this.imovelRural.getImovel());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
	}

	public Boolean isAdicionaProprietario() {
		return adicionaProprietario;
	}

	public void setAdicionaProprietario(Boolean adicionaProprietario) {
		this.adicionaProprietario = adicionaProprietario;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	/*public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
		if (endereco.getIdeEndereco() > 0 && this.imovelRural.getIdeImovelRural() != null && this.imovelRural.getIdeImovelRural() > 0) {
			//Salva relacionamento de Imovel com Endereco
			this.imovelRural.getImovel().setIdeEndereco(endereco);
			try {
				imovelRuralService.atualizar(this.imovelRural);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
	}*/

	public TipoVinculoImovel getTipoVinculoImovel() {
		return tipoVinculoImovel;
	}

	public void setTipoVinculoImovel(TipoVinculoImovel tipoVinculoImovel) {
		this.tipoVinculoImovel = tipoVinculoImovel;
	}

	public Collection<PessoaImovel> getListaPessoaImoveis() {
		return listaPessoaImoveis;
	}

	public void setListaPessoaImoveis(Collection<PessoaImovel> listaPessoaImoveis) {
		this.listaPessoaImoveis = listaPessoaImoveis;
	}

	public PessoaImovel getPessoaiMovel() {
		return pessoaiMovel;
	}

	public void setPessoaiMovel(PessoaImovel pessoaiMovel) {
		this.pessoaiMovel = pessoaiMovel;
	}

	public Collection<Imovel> getListaImoveisTabela() {
		return listaImoveisTabela;
	}

	public void setListaImoveisTabela(Collection<Imovel> listaImoveisTabela) {
		this.listaImoveisTabela = listaImoveisTabela;
	}

	public Boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Imovel getImovelExcluir() {
		return imovelExcluir;
	}

	public void setImovelExcluir(Imovel imovelExcluir) {
		this.imovelExcluir = imovelExcluir;
	}

	public EmpreendimentoImovel getEmpreendimentoImovel() {
		return empreendimentoImovel;
	}

	public void setEmpreendimentoImovel(EmpreendimentoImovel empreendimentoImovel) {
		this.empreendimentoImovel = empreendimentoImovel;
	}

	public boolean isVisualizaImoveisTabela() {
		if (!Util.isNullOuVazio(listaImoveisTabela) || !listaImoveisTabela.isEmpty()) {
			visualizaImoveisTabela = true;
		} else {
			visualizaImoveisTabela = false;
		}
		return visualizaImoveisTabela;
	}

	public void setVisualizaImoveisTabela(Boolean visualizaImoveisTabela) {
		this.visualizaImoveisTabela = visualizaImoveisTabela;
	}

	public boolean isVisualizaListaPessoasImoveis() {
		if (!listaPessoaImoveis.isEmpty()) {
			visualizaListaPessoasImoveis = true;
		} else {
			visualizaListaPessoasImoveis = false;
		}
		return visualizaListaPessoasImoveis;
	}

	public Boolean getEditModeProprietario() {
		return editModeProprietario;
	}

	public void setEditModeProprietario(Boolean editModeProprietario) {
		this.editModeProprietario = editModeProprietario;
	}

	public Boolean getTemPassivoAmbiental() {
		return temPassivoAmbiental;
	}

	public void setTemPassivoAmbiental(Boolean temPassivoAmbiental) {
		this.temPassivoAmbiental = temPassivoAmbiental;
	}

	public Boolean getAderiuPARA() {
		return aderiuPARA;
	}

	public void setAderiuPARA(Boolean aderiuPARA) {
		this.aderiuPARA = aderiuPARA;
	}

	public Boolean getEnableBotaoPesquisa() {
		return enableBotaoPesquisa;
	}

	public void setEnableBotaoPesquisa(Boolean enableBotaoPesquisa) {
		this.enableBotaoPesquisa = enableBotaoPesquisa;
	}

	public Boolean getEnableFormPessoaFisica() {
		return enableFormPessoaFisica;
	}

	public void setEnableFormPessoaFisica(Boolean enableFormPessoaFisica) {
		this.enableFormPessoaFisica = enableFormPessoaFisica;
	}

	public Boolean getEnableFormPessoaJuridica() {
		return enableFormPessoaJuridica;
	}

	public void setEnableFormPessoaJuridica(Boolean enableFormPessoaJuridica) {
		this.enableFormPessoaJuridica = enableFormPessoaJuridica;
	}

	public Boolean getTemProcesso() {
		return temProcesso;
	}

	public Boolean getTemItr() {
		return temItr;
	}

	public Boolean getAderePARA() {
		return aderePARA;
	}

	public Boolean getNovaDenominacao() {
		return novaDenominacao;
	}

	public Boolean getAdicionaProprietario() {
		return adicionaProprietario;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public String getNumProcessoPara() {
		return numProcessoPara;
	}

	public void setNumProcessoPara(String numProcessoPara) {
		this.numProcessoPara = numProcessoPara;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return localizacaoGeograficaSelecionada;
	}

	public void setLocalizacaoGeograficaSelecionada(LocalizacaoGeografica localizacaoGeograficaSelecionada) {
		this.localizacaoGeograficaSelecionada = localizacaoGeograficaSelecionada;
	}

	public String getGrausLatitudeLoc() {
		return grausLatitudeLoc;
	}

	public void setGrausLatitudeLoc(String grausLatitudeLoc) {
		this.grausLatitudeLoc = grausLatitudeLoc;
	}

	public String getMinutosLatitudeLoc() {
		return minutosLatitudeLoc;
	}

	public void setMinutosLatitudeLoc(String minutosLatitudeLoc) {
		this.minutosLatitudeLoc = minutosLatitudeLoc;
	}

	public String getSegundosLatitudeLoc() {
		return segundosLatitudeLoc;
	}

	public void setSegundosLatitudeLoc(String segundosLatitudeLoc) {
		this.segundosLatitudeLoc = segundosLatitudeLoc;
	}

	public String getGrausLongitudeLoc() {
		return grausLongitudeLoc;
	}

	public void setGrausLongitudeLoc(String grausLongitudeLoc) {
		this.grausLongitudeLoc = grausLongitudeLoc;
	}

	public String getMinutosLongitudeLoc() {
		return minutosLongitudeLoc;
	}

	public void setMinutosLongitudeLoc(String minutosLongitudeLoc) {
		this.minutosLongitudeLoc = minutosLongitudeLoc;
	}

	public String getSegundosLongitudeLoc() {
		return segundosLongitudeLoc;
	}

	public void setSegundosLongitudeLoc(String segundosLongitudeLoc) {
		this.segundosLongitudeLoc = segundosLongitudeLoc;
	}

	public String getFracaoGrauLatitudeLoc() {
		return fracaoGrauLatitudeLoc;
	}

	public void setFracaoGrauLatitudeLoc(String fracaoGrauLatitudeLoc) {
		this.fracaoGrauLatitudeLoc = fracaoGrauLatitudeLoc;
	}

	public String getFracaoGrauLongitudeLoc() {
		return fracaoGrauLongitudeLoc;
	}

	public void setFracaoGrauLongitudeLoc(String fracaoGrauLongitudeLoc) {
		this.fracaoGrauLongitudeLoc = fracaoGrauLongitudeLoc;
	}

	public DadoGeografico getVerticeLoc() {
		return verticeLoc;
	}

	public void setVerticeLoc(DadoGeografico verticeLoc) {
		this.verticeLoc = verticeLoc;
	}

	public DadoGeografico getVerticeExclusaoLoc() {
		return verticeExclusaoLoc;
	}

	public void setVerticeExclusaoLoc(DadoGeografico verticeExclusaoLoc) {
		this.verticeExclusaoLoc = verticeExclusaoLoc;
	}

	public SistemaCoordenada getDatum() {
		return datum;
	}

	public void setDatum(SistemaCoordenada datum) {
		this.datum = datum;
	}

	public DatumService getServiceDatum() {
		return serviceDatum;
	}

	public void setServiceDatum(DatumService serviceDatum) {
		this.serviceDatum = serviceDatum;
	}

	public Boolean getTemImovelSelecionado() {
		//o Util.isnUllOuVazio estava deixando passar o objeto null, então fiz esse tratamento aí.
		if (!Util.isNullOuVazio(this.imovelSelecionado) && temImovelSelecionado && !Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeImovelRural()) && this.imovelRural.getIdeImovelRural() > 0) {
			temImovelSelecionado = true;
		} else {
			temImovelSelecionado = false;//é pra ser false aqui
		}
		return temImovelSelecionado;
	}

	public void setTemImovelSelecionado(Boolean temImovelSelecionado) {
		this.temImovelSelecionado = temImovelSelecionado;
	}

	public Boolean getMostraListaVertices() {
		if (Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
			return false;
		}
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) && !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
			mostraListaVertices = true;
		} else {
			mostraListaVertices = false;
		}
		return mostraListaVertices;
	}

	public void setMostraListaVertices(Boolean mostraListaVertices) {
		this.mostraListaVertices = mostraListaVertices;
	}

	public String getFracaoGrauLatitude() {
		return fracaoGrauLatitude;
	}

	public void setFracaoGrauLatitude(String fracaoGrauLatitude) {
		this.fracaoGrauLatitude = fracaoGrauLatitude;
	}

	public String getFracaoGrauLongitude() {
		return fracaoGrauLongitude;
	}

	public void setFracaoGrauLongitude(String fracaoGrauLongitude) {
		this.fracaoGrauLongitude = fracaoGrauLongitude;
	}

	public Boolean getCheckTemAverbado() {
		if (temAverbado == null) {
			this.checkTemAverbado = Boolean.FALSE;
			return checkTemAverbado;
		} else {
			checkTemAverbado = temAverbado;
		}
		return checkTemAverbado;
	}

	public void setCheckTemAverbado(Boolean checkTemAverbado) {
		this.checkTemAverbado = checkTemAverbado;
	}

	public String getLabelBtnSalvar() {
		return labelBtnSalvar;
	}
	
	public Boolean getDisableBtnAssociarImovel() {
		return disableBtnAssociarImovel;
	}

	public void setDisableBtnAssociarImovel(Boolean disableBtnAssociarImovel) {
		this.disableBtnAssociarImovel = disableBtnAssociarImovel;
	}

	public void setLabelBtnSalvar(String labelBtnSalvar) {
		this.labelBtnSalvar = labelBtnSalvar;
	}
	
	public Boolean getVisualizaBtnAssociar() {
		return visualizaBtnAssociar;
	}

	public void setVisualizaBtnAssociar(Boolean visualizaBtnAssociar) {
		this.visualizaBtnAssociar = visualizaBtnAssociar;
	}
	
	
	public void alterarLabelBtnSalvar(){
		if(!Util.isNullOuVazio(imovelSelecionado)){
			if(imovelSelecionado.getIdeImovelRural()!=-1){
				labelBtnSalvar=ResourceBundle.getBundle("/Bundle").getString("btn_associar_imovel_empreendimento");
				visualizaBtnAssociar= Boolean.TRUE;
			}else{
				labelBtnSalvar=ResourceBundle.getBundle("/Bundle").getString("btn_salvar");
				visualizaBtnAssociar = Boolean.FALSE;
			}
		}
	}
	
	
	public String getEnderecoCompleto() { 
		if(Util.isNullOuVazio(imovelRural) || Util.isNullOuVazio(imovelRural.getImovel()) ||  Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco()) ) {
			return "";
		}
		else {
		  return	imovelSelecionado.getImovel().getIdeEndereco().getEnderecoCompleto();
		}
	}

	
	
	
	
	
	
	
	
}