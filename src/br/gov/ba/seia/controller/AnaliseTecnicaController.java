package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalBloqueio;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ReservaLegalService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

@Named("analiseTecnicaController")
@ViewScoped
public class AnaliseTecnicaController extends SeiaControllerAb {

	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private AuditoriaFacade auditoriaFacade;
	@EJB
	private ReservaLegalService reservaLegalService;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;

	private UIForm formularioASerLimpo;
	private VwConsultaProcesso vwProcesso;
	private ArrayList<Imovel> listaImoveis = null;
	private String caminhoDesenharGeoBahia = URLEnum.CAMINHO_GEOBAHIA.toString();
	private Imovel imovelSelecionado = new Imovel();
	private Boolean indAprovacao;
	private Boolean chamadaTelaPautaTecnico = false;
	private String lblPergunta = null;
	private String lblTitulo = null;
	private String perguntaConfirmacao = null;
		
	private int activeIndex;
	
	// Melhoria #7112
	// Bloqueio da Reserva Legal
	private ReservaLegalBloqueio ultimaReservaLegalBloqueio;
	private Area areaDoTecnicoQueBloqueou;
	private Funcionario coordenadorResponsavelPelaArea;
	private Usuario substitutoDoCoordenador;

	public void limparTela() {
		limparComponentesFormulario(formularioASerLimpo);
		ultimaReservaLegalBloqueio = null;
		areaDoTecnicoQueBloqueou = null;
		coordenadorResponsavelPelaArea = null;
		substitutoDoCoordenador = null;
	}
	
	public String analisarProcesso() {
		this.activeIndex = 0;
		limparTela();
		montarListaImoveis();
		return null;
	}
	
	public boolean exibirAnaliseTecnica(Integer ideProcesso){
		
		try{
			Empreendimento emp = empreendimentoService.buscarEmpreendimentoPorProcesso(ideProcesso);
			return imovelRuralService.verificarReservaLegal(emp.getIdeEmpreendimento());			
		}
		catch(Exception e){
			return false;
		}
		
	}
	
	private void montarListaImoveis(){
		Exception erro = null;
		try{
			listaImoveis = new ArrayList<Imovel>();
			listaImoveis = imovelRuralServiceFacade.listarPorRequerimento(vwProcesso.getIdeRequerimento());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
		}
		}
		
	}
	
	public String carregarReservaLegal() {
		try{
			indAprovacao = null;
			imovelSelecionado.getImovelRural().setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelSelecionado.getImovelRural().getReservaLegal()));
			if (!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal())){
				imovelSelecionado.getImovelRural().setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelSelecionado.getImovelRural().getReservaLegal()));
				prepararReservaLegalBloqueio();
				if(isSendoAnalisado()) {
					JsfUtil.addWarnMessage(Util.getString("cefir_lbl_reserva_legal_sendo_analisada") + " " + ultimaReservaLegalBloqueio.getUsuario().getPessoaFisica().getNomPessoa());
				}
				carregarImoveisCredito();
			}
			if (!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal().getIdeStatus()) && !Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal().getIdeStatus().getIdeStatusReservaLegal())){
				if(imovelSelecionado.getImovelRural().getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(1)) {
					indAprovacao = true;
				}		
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		return null;
	}

	/**
	 * Carrega os imóveis que um imóvel selecionado compensa sua reserva
	 */
	private void carregarImoveisCredito() {
		List<Object[]> listImoveisCredito = validacaoGeoSeiaService.obterImoveisCredito(imovelSelecionado.getImovelRural(), true);
		List<ImovelRural> listImovelRural = new ArrayList<ImovelRural>();
		if(!Util.isNullOuVazio(listImoveisCredito)) {
			for (Object[] objects : listImoveisCredito) {
				ImovelRural imovelRuralTemp = new ImovelRural();
				imovelRuralTemp.setIdeImovelRural(Integer.valueOf(objects[0].toString()));
				imovelRuralTemp.setDesDenominacao(objects[1].toString());
				imovelRuralTemp.setValArea(Double.valueOf(objects[4].toString()));
				listImovelRural.add(imovelRuralTemp);
			}
			imovelSelecionado.getImovelRural().getReservaLegal().setListImoveisCredito(listImovelRural);
		}
	}
		
	public String retornaStatusImovel(ImovelRural imovelRural) {
		try {

			
			if(imovelRural.getIndSuspensao() != null && imovelRural.getIndSuspensao()){
				return "Pendente";
			}else{
				switch (imovelRural.getStatusCadastro()) {
					case 0:
						return "Registro incompleto";
					case 1:
						return "Registrado";
					case 2:
						return "Cadastro incompleto";
					case 3:
						return "Cadastrado";
					default:
						return "Status Não Encontrado!";
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return "Status Não Encontrado!";
	}
	
	public String retornaNomeDaPessoa(Pessoa p) {
		if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getPessoaFisica()) && !Util.isNullOuVazio(p.getPessoaFisica().getNomPessoa())){
			return p.getPessoaFisica().getNomPessoa().toString();
		}else if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getPessoaJuridica()) && !Util.isNullOuVazio(p.getPessoaJuridica().getNomRazaoSocial())){
			return p.getPessoaJuridica().getNomRazaoSocial().toString();
		} else {
			return "Esse imóvel só tem Justo Possuidor";
	}
	}
	
	public boolean existeTheGeomByIdeLocGeo(Integer pIdeLocGeo) throws Exception{
		
		return imovelRuralServiceFacade.verificaSeExisteTheGeomValido(pIdeLocGeo);
	}
	
	public void salvarAprovacao(){
		try{
			if(Util.isNull(indAprovacao)) {
				JsfUtil.addErrorMessage("O campo Deseja aprovar a localização da reserva legal é de preenchimento obrigatório.");
				return;
			}
			
			boolean isValidacao = imovelSelecionado.getImovelRural().getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(8) || imovelSelecionado.getImovelRural().getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9);
			if(!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal())){				
				ReservaLegal objAntigo = reservaLegalService.carregarTudo(imovelSelecionado.getImovelRural().getReservaLegal());
				imovelSelecionado.getImovelRural().getReservaLegal().setDtcAprovacao(new Date());				
				imovelSelecionado.getImovelRural().getReservaLegal().setIdeUsuarioAprovacao(new Usuario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
				if(indAprovacao){
					imovelSelecionado.getImovelRural().getReservaLegal().setIdeStatus(new StatusReservaLegal(1));
					imovelRuralServiceFacade.atualizarReservaLegal(imovelSelecionado.getImovelRural().getReservaLegal());
					auditoriaFacade.atualizar(objAntigo, imovelSelecionado.getImovelRural().getReservaLegal());
					if(!chamadaTelaPautaTecnico){
						RequestContext.getCurrentInstance().execute("fecharModal('#statusDialog'); abrirModal('#statusDialog'); document.getElementById('filtroImoveis:btnConsulta').click();");
					}else{
						montarListaImoveis();
					}
					RequestContext.getCurrentInstance().execute("confirmacaoAprovacaoRl.hide();");
					RequestContext.getCurrentInstance().execute("dialogAprovarRL.hide();");					
					JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S006"));
					imovelSelecionado = null;
				}else{
					if(!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal().getObservacao())){
						if(isValidacao) {
							imovelSelecionado.getImovelRural().getReservaLegal().setIdeStatus(new StatusReservaLegal(9));
						} else {
							imovelSelecionado.getImovelRural().getReservaLegal().setIdeStatus(new StatusReservaLegal(5));
						}
						imovelRuralServiceFacade.atualizarReservaLegal(imovelSelecionado.getImovelRural().getReservaLegal());
						auditoriaFacade.atualizar(objAntigo, imovelSelecionado.getImovelRural().getReservaLegal());
						if(!chamadaTelaPautaTecnico){
							RequestContext.getCurrentInstance().execute("fecharModal('#statusDialog'); abrirModal('#statusDialog'); document.getElementById('filtroImoveis:btnConsulta').click();");
						}else{
							montarListaImoveis();
						}
						RequestContext.getCurrentInstance().execute("confirmacaoAprovacaoRl.hide();");						
						RequestContext.getCurrentInstance().execute("dialogAprovarRL.hide();");
						JsfUtil.addSuccessMessage("Status da reserva legal alterado com sucesso!");
						imovelSelecionado = null;
					}else{
						RequestContext.getCurrentInstance().execute("confirmacaoAprovacaoRl.hide();");
						JsfUtil.addErrorMessage("O preenchimento do campo Observações é obrigatório!");
					}
				}
			}
		} catch (Exception e) {			
			JsfUtil.addErrorMessage(e.getMessage());
		}		
	}
	
	public StreamedContent getFileDownload(String caminhoArquivo) {		
		if(!Util.isNullOuVazio(caminhoArquivo)) {
			try {
				return imovelRuralServiceFacade.getContentFile(caminhoArquivo);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");				
			}
		}			
		return null;
	}

	/**
	 * Método criado para buscar a {@link ReservaLegalBloqueio} e {@link Usuario} (Coordenador ou Substituto) responsável pela {@link Area} de quem bloqueou.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 23/07/2015
	 * @see #7112
	 */
	private void prepararReservaLegalBloqueio(){
		carregarReservaLegalBloqueio();
		if(existeReservaLegalBloqueio()){
			carregarCoordenadorDaArea();
			carregarSubstitutoDoCoordenador();
	}
	}

	/**
	 * Método que carrega a {@link ReservaLegalBloqueio} daquela {@link ReservaLegal}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 23/07/2015
	 * @see #7112
	 */
	private void carregarReservaLegalBloqueio(){
		try {
			ultimaReservaLegalBloqueio = imovelRuralServiceFacade.obterUltimaReservaLegalBloqueioBy(imovelSelecionado.getImovelRural().getReservaLegal());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Bloqueio da Reserva Legal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que vai criar a bloqueio na {@link ReservaLegal}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 20/07/2015
	 * @see #7112
	 */
	public void bloquearReservaLegal(){
		salvarReservaLegalBloqueio(true);
		JsfUtil.addWarnMessage(Util.getString("cefir_msg_A015"));
	}

	/**
	 * Método que vai desbloquear a {@link ReservaLegal}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 20/07/2015
	 * @see #7112
	 */
	public void liberarReservaLegal(){
		salvarReservaLegalBloqueio(false);
		JsfUtil.addWarnMessage(Util.getString("cefir_msg_A016"));
	}

	/**
	 * Método para persistir a {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 20/07/2015
	 * @see #7112
	 */
	private void salvarReservaLegalBloqueio(Boolean indBloqueado){
		try {
			ReservaLegalBloqueio novaReservaLegalBloqueio = criarNovaReservaLegalBloqueio(indBloqueado);
			if(!Util.isNull(ultimaReservaLegalBloqueio) && !Util.isNull(ultimaReservaLegalBloqueio.getIdeReservaLegalBloqueio())){
				novaReservaLegalBloqueio.setIdeReservaLegalBloqueio(ultimaReservaLegalBloqueio.getIdeReservaLegalBloqueio());
			}
			ReservaLegalBloqueio ultimaReservaLegalBloqueioTemp = imovelRuralServiceFacade.salvarReservaLegalBloqueio(ultimaReservaLegalBloqueio, novaReservaLegalBloqueio);
			if(Util.isNull(ultimaReservaLegalBloqueio)){
				auditoriaFacade.salvar(novaReservaLegalBloqueio);
			}
			else {
				auditoriaFacade.atualizar(ultimaReservaLegalBloqueio, novaReservaLegalBloqueio);
			}
			ultimaReservaLegalBloqueio = ultimaReservaLegalBloqueioTemp;
			RequestContext.getCurrentInstance().execute("dialogAprovarRL.hide()");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o bloqueio/desbloqueio da Reserva Legal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param indBloqueado
	 * @return
	 * @since 21/07/2015
	 */
	private ReservaLegalBloqueio criarNovaReservaLegalBloqueio(Boolean indBloqueado) {
		return new ReservaLegalBloqueio(imovelSelecionado.getImovelRural().getReservaLegal(), indBloqueado);
	}

	/**
	 * Método criado para impedir que outros usuários aprovem a {@link ReservaLegal} quando a mesma estiver sendo analisada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 20/07/2015
	 * @see #7112
	 */
	public boolean isSendoAnalisado(){
		return isExisteBloqueio() && !isUsuarioResponsavel();
	}

	/**
	 * Método que verifica se existe {@link ReservaLegalBloqueio} e se ela está ou não bloqueada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return boolean
	 * @since 20/07/2015
	 * @see #7112
	 */
	public boolean isExisteBloqueio(){
		return existeReservaLegalBloqueio() && ultimaReservaLegalBloqueio.getIndBloqueado();
	}

	/**
	 * Método que verifica se há {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 21/07/2015
	 * @see #7112
	 */
	private boolean existeReservaLegalBloqueio() {
		return !Util.isNull(ultimaReservaLegalBloqueio);
	}

	public boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}

	/**
	 * Método que verifica se o {@link Usuario} logado tem acesso aquela {@link Acao} de "Bloquear\Desbloquear a Reserva Legal".
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 24/07/2015
	 * @see #7112
	 */
	public boolean isTemAcesso(){
		SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
		// Seção // Funcionalidade // Ação
		return security.temAcesso("3.33.59") || security.temAcesso("5.19.59") || security.temAcesso("5.18.59");
	}

	/**
	 * Método que verifica se o {@link Usuario} logado pode alterar o {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 20/07/2015
	 * @see #7112
	 */
	public boolean isUsuarioResponsavel(){
		return isUsuarioQueRealizouBloqueio() || isCoordenadorDaAreaDoUsuario() || isSubstitutoDoCoordenador();
	}

	/**
	 * Usuário responsável pela análise da reserva legal.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 23/07/2015
	 * @see #7112
	 */
	private boolean isUsuarioQueRealizouBloqueio() {
		return ContextoUtil.getContexto().getUsuarioLogado().equals(ultimaReservaLegalBloqueio.getUsuario());
	}

	/**
	 * Método que verifica se o {@link Usuario} logado é Coordenador e é o Coordenador da Área do {@link Usuario} que criou o {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 20/07/2015
	 * @see #7112
	 */
	private boolean isCoordenadorDaAreaDoUsuario(){
		return isExisteCoordenadorParaArea() && ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica().equals(coordenadorResponsavelPelaArea.getIdePessoaFisica());
	}

	/**
	 * Método que verifica se há um Coordenador para {@link Area} do {@link Usuario} responsável pela {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 23/07/2015
	 * @see #7112
	 */
	private boolean isExisteCoordenadorParaArea() {
		return !Util.isNull(coordenadorResponsavelPelaArea);
	}

	/**
	 * Método que verifica se o {@link Usuario} logado é o substituto do Coordenador responsável pela {@link Area} que criou a {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 23/07/2015
	 * @see #7112
	 */
	private boolean isSubstitutoDoCoordenador(){
		return !Util.isNull(substitutoDoCoordenador) && ContextoUtil.getContexto().getUsuarioLogado().equals(substitutoDoCoordenador);
	}

	/**
	 * Método que carrega o {@link Usuario} que substitui o coordenador responsável pela {@link Area} que criou a {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 23/07/2015
	 * @see #7112
	 */
	private void carregarSubstitutoDoCoordenador(){
		try {
			if(isExisteCoordenadorParaArea()){
			substitutoDoCoordenador = imovelRuralServiceFacade.obterSubstitutoDoCoordenador(coordenadorResponsavelPelaArea.getIdePessoaFisica());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Substituto do Coordenador da Área.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para obter o {@link Funcionario} (Coordenador) da {@link Area} resposável pela {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param usuario
	 * @return
	 * @since 20/07/2015
	 * @see #7112
	 */
	private void carregarCoordenadorDaArea(){
		try {
			coordenadorResponsavelPelaArea = imovelRuralServiceFacade.obterCoordenadorByArea(obterAreaFuncionarioResponsavelPeloBloqueio());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Coordenador da Área.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para obter a {@link Area} do resposável pela {@link ReservaLegalBloqueio}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param usuario
	 * @return
	 * @since 20/07/2015
	 * @see #7112
	 */
	private Area obterAreaFuncionarioResponsavelPeloBloqueio(){
		try {
			if(Util.isNull(areaDoTecnicoQueBloqueou)){
				areaDoTecnicoQueBloqueou = imovelRuralServiceFacade.obterAreaByUsuario(ultimaReservaLegalBloqueio.getUsuario());
			}
			return areaDoTecnicoQueBloqueou;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da Área do Técnico.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public boolean habilitarLinkStatusPendenteImovel(ImovelRural imovelRural){
		return "Pendente".equalsIgnoreCase(retornaStatusImovel(imovelRural));
	}

	public VwConsultaProcesso getVwProcesso() {
		if (Util.isNullOuVazio(vwProcesso)) {
			vwProcesso = new VwConsultaProcesso();
		}
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
	
	public String formatarNumero(BigDecimal pValor){
		DecimalFormat df = Util.getDecimalFormatPtBr(); 
		return df.format(pValor);
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	
	public ArrayList<Imovel> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(ArrayList<Imovel> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}
	
	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}
	
	public Imovel getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(Imovel imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}
	
	public Boolean getIndAprovacao() {
		return indAprovacao;
	}

	public void setIndAprovacao(Boolean indAprovacao) {
		this.indAprovacao = indAprovacao;
	}
	
	public Boolean getChamadaTelaPautaTecnico() {
		return chamadaTelaPautaTecnico;
	}
	
	public void setChamadaTelaPautaTecnico(Boolean chamadaTelaPautaTecnico) {
		this.chamadaTelaPautaTecnico = chamadaTelaPautaTecnico;
	}
	
	public String getLblPergunta() {
		return lblPergunta;
	}

	public void setLblPergunta(String lblPergunta) {
		this.lblPergunta = lblPergunta;
	}
	
	public String getLblTitulo() {
		return lblTitulo;
	}

	public void setLblTitulo(String lblTitulo) {
		this.lblTitulo = lblTitulo;
	}
	
	public String getPerguntaConfirmacao() {
		return perguntaConfirmacao;
	}

	public void setPerguntaConfirmacao(String perguntaConfirmacao) {
		this.perguntaConfirmacao = perguntaConfirmacao;
	}

	public List<DocumentoImovelRural> getlistaArquivoReservaAprovada() {
		List<DocumentoImovelRural> listarquivo = new ArrayList<DocumentoImovelRural>();
		if (!Util.isNullOuVazio(imovelSelecionado) && !Util.isNullOuVazio(imovelSelecionado.getImovelRural()) && !Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal()) && !Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal().getIdeDocumentoAprovacao())) {
			listarquivo.add(imovelSelecionado.getImovelRural().getReservaLegal().getIdeDocumentoAprovacao());
		}
		return listarquivo;			
	}
	
	public boolean habilitarBotoesStatusPendenteImovel(ImovelRural imovelRural){
		return !"Pendente".equalsIgnoreCase(retornaStatusImovel(imovelRural));
	}
}