/**
 *
 */
package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhParcelasCobranca;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.TipoArquivoEnum;
import br.gov.ba.seia.facade.CerhCobrancaFacade;
import br.gov.ba.seia.facade.ConsultaCerhServiceFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JasperUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.RecursosUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.cerh.CobrancaCerhUtil;
import br.gov.ba.seia.util.security.SecurityUser;
import br.gov.ba.ws.enumerator.OpcaoPagamentoEnum;
import br.gov.ba.ws.enumerator.PapelSolicitanteEnum;

/**
 * @author lesantos
 *
 */
@Named("cerhCobrancaController")
@ViewScoped
public class CerhCobrancaController {

	private int etapa = 1;

	private Pessoa requerente;

	private PapelSolicitanteEnum papelSolicitante;

	private List<PapelSolicitanteEnum> solicitanteEnums;

//	O Ano base da cobrança deve estar disponível para seleção apenas para usuários internos,
//	trazendo por padrão o ano anterior já selecionado, e tendo como opções o ano de início da cobrança até o ano anterior ao exercício atual.
//	Caso o usuário autenticado seja um usuário externo, o campo deve exibir o ano anterior apenas como leitura.
	private List<String> anos;

	private String anoBase;

	private List<Empreendimento> empreendimentos;

	private Empreendimento empreendimento;

	private List<OpcaoPagamentoEnum> opcaoPagamentoEnums;

	private OpcaoPagamentoEnum opcaoPagamento;

	private boolean receberCorreio;

	private MetodoUtil metodoExterno;

	@EJB
	private LocalizacaoGeograficaServiceFacade geograficaServiceFacade;

	@EJB
	private CerhCobrancaFacade cerhCobrancaFacade;

	@EJB
	private ConsultaCerhServiceFacade consultaCerhServiceFacade;

	private List<CerhCobranca> cerhCobrancas;

	private CobrancaCerhUtil util;

	public CerhCobrancaController() {

	}

	@PostConstruct
	public void init() {
		this.solicitanteEnums = Arrays.asList(PapelSolicitanteEnum.values());
		this.opcaoPagamentoEnums = Arrays.asList(OpcaoPagamentoEnum.values());
		this.anos = new ArrayList<String>();
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR) -1;
		for(int x = anoAtual ; x > Calendar.getInstance().get(Calendar.YEAR) -2; x--){
			anos.add("" + x);
		}
		this.anoBase = "" + anoAtual;
		//
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		PessoaFisica p =  this.cerhCobrancaFacade.buscarPessoaFisica(usuario.getIdePessoaFisica());
		usuario.setPessoaFisica(p);
		//usuário Externo
		if(isUsuarioExterno()){
			this.empreendimentos = cerhCobrancaFacade.listarEmpreendimentos(usuario.getPessoaFisica().getPessoa());
			this.requerente = usuario.getPessoaFisica().getPessoa();
			this.etapa = 1;
		}else{
			//Usuário Interno
			this.etapa = 2;
		}
		setMetodoExterno(new MetodoUtil(this, "selecionarRequerente", Pessoa.class));
		this.cerhCobrancas = new ArrayList<CerhCobranca>();
		this.util= new CobrancaCerhUtil(cerhCobrancaFacade);
	}


	public boolean isUsuarioExterno(){
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		if(usuario != null && usuario.getIdePerfil().getIdePerfil().equals(2)){
			return true;
		}
		return false;
	}

	public void avancar(){
		try {
			if(etapa == 1 && !papelSolicitante.equals(PapelSolicitanteEnum.PROPRIO_REQUERENTE)){
				Html.exibir("dialogselecionarRequerente");
				return;
			}else if (etapa == 2) {
				if (this.isReceberCorreio()) {
					Html.exibir("dialogConfirmCorreio");
					return;
				}
				continuar();
				return;
			} else if (etapa == 3) {
				if (!isUsuarioExterno()) {
					Html.exibir("dialogConfirmGerarDae");
					return;
				}
			} else if (etapa == 4) {
				Html.exibir("dialogConfirmGerarDae");
				return;
			}
			etapa++;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao tentar emitir o(s) DAE(s) em questão. Nenhum DAE será gerado. Favor entrar em contato com o administrador do sistema.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public String getRpgas(){
		List<String> rpgaList = new ArrayList<String>();
		for(CerhCobranca cobranca : this.cerhCobrancas){
			for(CerhTipoUso tipoUso : cobranca.getCerh().getCerhTipoUsoCollection()){
				for (CerhLocalizacaoGeografica loc : tipoUso.getCerhLocalizacaoGeograficaCollection()) {
					String rpga = geograficaServiceFacade.getRPGA(loc.getIdeLocalizacaoGeografica()).replace(" e ", ", ");
					for(String r : rpga.split(", ")){
						if(!r.equals("--") && !rpgaList.contains(r)){
							rpgaList.add(r);
						}
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rpgaList.size(); i++) {
			sb.append(rpgaList.get(i));
			if(i < rpgaList.size() -1){
				sb.append(", ");
			}
		}
		return sb.toString();
	}


	public void voltar(){
		if(etapa == 5 && !isUsuarioExterno()){
			etapa--;
		}
		etapa --;
	}

	public String getMessage(String campo){
		return campo + " é de preenchimento obrigatório.";
	}

	public void continuar() {
		try{
			if(etapa == 2){
				this.cerhCobrancas = new ArrayList<CerhCobranca>();
				//Calcular Cobrança
				if(!Util.isNullOuVazio(this.empreendimento)){
					addGrupoCobranca(this.empreendimento);
				}else{
					for(Empreendimento empreendimento : this.empreendimentos){
						addGrupoCobranca(empreendimento);
					}
				}
			}else if((etapa == 3 && !isUsuarioExterno()) || (etapa == 4 && isUsuarioExterno())){
				//Gerar Daes
				//salvar DAES
				salvarCobranca();
			}
			etapa++;
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void addGrupoCobranca(Empreendimento empreendimento) throws Exception{
		Cerh cerh = this.cerhCobrancaFacade.buscarCerh(empreendimento);
		if(cerh != null){
			this.cerhCobrancas.addAll(util.montarCobrancas(cerh, isReceberCorreio(), opcaoPagamento, Integer.parseInt(anoBase)));
		}
	}


	public void selecionarRequerente(Pessoa requerente) {
		if(this.isUsuarioExterno()){
			boolean valido = false;
			Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
			if(this.papelSolicitante.equals(PapelSolicitanteEnum.REPRESENTANTE_LEGAL_PESSOA_JURIDICA)){
				valido = this.cerhCobrancaFacade.isRepresentanateLegal(usuario.getPessoaFisica(), requerente.getPessoaJuridica());
			}else if(this.papelSolicitante.equals(PapelSolicitanteEnum.PROCURADOR_PESSOA_FISICA)){
				valido = this.cerhCobrancaFacade.isProcurador(usuario.getPessoaFisica(), requerente);
			}else if(this.papelSolicitante.equals(PapelSolicitanteEnum.PROCURADOR_PESSOA_JURIDICA)){
				valido = this.cerhCobrancaFacade.isProcurador(usuario.getPessoaFisica(), requerente);
			}
			if(valido){
				etapa++;
			}else {
				JsfUtil.addWarnMessage("Requerente inválido");
				return;
			}
		}
		setRequerente(requerente);
		//busca os em empreendimentos
		setEmpreendimentos(cerhCobrancaFacade.listarEmpreendimentos(requerente));
		Html.atualizar("gerarDaeForm");
	}

	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public List<Dae> getDaes(boolean correios) throws Exception{
		List<CerhCobranca> cobranca = new ArrayList<CerhCobranca>();
		if(!Util.isNullOuVazio(this.empreendimento)){
			Cerh cerh = cerhCobrancaFacade.buscarCerh(empreendimento);
			cobranca.addAll(util.montarCobrancas(cerh, correios, opcaoPagamento, Integer.parseInt(anoBase)));
		}else{
			for(Empreendimento empreendimento : this.empreendimentos){
				Cerh cerh = cerhCobrancaFacade.buscarCerh(empreendimento);
				cobranca.addAll(util.montarCobrancas(cerh, correios, opcaoPagamento, Integer.parseInt(anoBase)));
			}
		}
		List<Dae> daes = new ArrayList<Dae>();
		for(CerhCobranca c : cobranca){
			for(CerhParcelasCobranca p : c.getCerhParcelasCobrancasCollection()){
				daes.addAll(p.getCerhDaesCollection());
			}
		}
		return daes;
	}



	public List<Dae> getDaes(){
		List<Dae> daes = new ArrayList<Dae>();
		for(CerhCobranca cobranca : this.cerhCobrancas){
			for(CerhParcelasCobranca parcela : cobranca.getCerhParcelasCobrancasCollection()){
				daes.addAll(parcela.getCerhDaesCollection());
			}
		}
		return daes;
	}

	public StreamedContent getImprimirDae(Dae dae) throws Exception{
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("PASTA", "cerh");
		parametros.put("LOGO_SEFAZ", RecursosUtil.retornaCaminho(TipoArquivoEnum.IMAGEM, Uri.LOGO_SEFAZ));
		parametros.put("NOME_RELATORIO", "cerh_dae.jasper");
		parametros.put("ide_dae", dae.getIdeDae());
		//IMAGEM Da sefaz
		JasperUtil jasper = new JasperUtil();
		StreamedContent stream = jasper.gerar(null, parametros, null);
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		this.cerhCobrancaFacade.atualizarDae(dae, usuario);
		return stream;
	}

	public void salvarCobranca() throws Exception{
//		Caso um usuário interno esteja gerando um lote de DAEs para um requerente, considerando todos os empreendimentos,
//		e um dos empreendimentos já tenha os DAEs emitidos anteriormente, este deve ser ignorado e o sistema deve prosseguir
//		com a emissão dos DAEs dos demais empreendimentos.
		if(!this.isUsuarioExterno() && this.empreendimento == null){
			List<CerhCobranca> cobrancasRemover = new ArrayList<CerhCobranca>();
			//empreendimento = null significa que o usuário selecionou todos os empreendimentos
			for(CerhCobranca c : this.getCerhCobrancas()){
				//Verificar se já foi emitida a cobrança para a RPGA e Tipo de Uso do CERH
				boolean cobrancaEmitida = cerhCobrancaFacade.isCobrancaJaEmitida(c);
				if(!cobrancaEmitida){
					util.gerarDaesSefaz(c);
					cerhCobrancaFacade.salvar(c);
				}else {
					cobrancasRemover.add(c);
				}
			}
			this.cerhCobrancas.removeAll(cobrancasRemover);
			if(this.cerhCobrancas.isEmpty()){

				throw new Exception("Os DAES deste empreendimento já foram gerados para o ano indicado.");//ALE -0003
			}
		}else{
			for(CerhCobranca c : this.getCerhCobrancas()){
//				Caso o usuário tente emitir a 1ª via de DAE para um mesmo cenário (empreendimento e Ano de cobrança) mais de uma vez,
//				o sistema não deve gerar os DAEs e deve informar ao usuário que os DAEs já foram gerados. [ALE-003].
				boolean cobrancaEmitida = cerhCobrancaFacade.isCobrancaJaEmitida(c);
				if(!cobrancaEmitida){
					util.gerarDaesSefaz(c);
					cerhCobrancaFacade.salvar(c);
				}else {
					throw new Exception("Os DAES deste empreendimento já foram gerados para o ano indicado.");//ALE -0003
				}
			}
		}
	}


	/**
	 * @return the anos
	 */
	public List<String> getAnos() {
		return this.anos;
	}

	/**
	 * @param anos the anos to set
	 */
	public void setAnos(List<String> anos) {
		this.anos = anos;
	}

	/**
	 * @return the anoBase
	 */
	public String getAnoBase() {
		return anoBase;
	}

	/**
	 * @param anoBase the anoBase to set
	 */
	public void setAnoBase(String anoBase) {
		this.anoBase = anoBase;
	}

	/**
	 * @return the empreendimento
	 */
	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	/**
	 * @param empreendimento the empreendimento to set
	 */
	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	/**
	 * @return the opcaoPagamentoEnums
	 */
	public List<OpcaoPagamentoEnum> getOpcaoPagamentoEnums() {
		return opcaoPagamentoEnums;
	}

	/**
	 * @param opcaoPagamentoEnums the opcaoPagamentoEnums to set
	 */
	public void setOpcaoPagamentoEnums(List<OpcaoPagamentoEnum> opcaoPagamentoEnums) {
		this.opcaoPagamentoEnums = opcaoPagamentoEnums;
	}

	/**
	 * @return the opcaoPagamento
	 */
	public OpcaoPagamentoEnum getOpcaoPagamento() {
		return opcaoPagamento;
	}

	/**
	 * @param opcaoPagamento the opcaoPagamento to set
	 */
	public void setOpcaoPagamento(OpcaoPagamentoEnum opcaoPagamento) {
		this.opcaoPagamento = opcaoPagamento;
	}

	/**
	 * @return the receberCorreio
	 */
	public boolean isReceberCorreio() {
		return receberCorreio;
	}

	/**
	 * @param receberCorreio the receberCorreio to set
	 */
	public void setReceberCorreio(boolean receberCorreio) {
		this.receberCorreio = receberCorreio;
	}

	/**
	 * @return the empreendimentos
	 */
	public List<Empreendimento> getEmpreendimentos() {
		return empreendimentos;
	}

	/**
	 * @param empreendimentos the empreendimentos to set
	 */
	public void setEmpreendimentos(List<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}

	/**
	 * @return the etapa
	 */
	public int getEtapa() {
		return etapa;
	}

	/**
	 * @param etapa the etapa to set
	 */
	public void setEtapa(int etapa) {
		this.etapa = etapa;
	}

	/**
	 * @return the papelSolicitante
	 */
	public PapelSolicitanteEnum getPapelSolicitante() {
		return papelSolicitante;
	}

	/**
	 * @param papelSolicitante the papelSolicitante to set
	 */
	public void setPapelSolicitante(PapelSolicitanteEnum papelSolicitante) {
		this.papelSolicitante = papelSolicitante;
	}

	/**
	 * @return the solicitanteEnums
	 */
	public List<PapelSolicitanteEnum> getSolicitanteEnums() {
		return solicitanteEnums;
	}

	/**
	 * @param solicitanteEnums the solicitanteEnums to set
	 */
	public void setSolicitanteEnums(List<PapelSolicitanteEnum> solicitanteEnums) {
		this.solicitanteEnums = solicitanteEnums;
	}

	/**
	 * @return the requerente
	 */
	public Pessoa getRequerente() {
		return requerente;
	}

	/**
	 * @param requerente the requerente to set
	 */
	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	/**
	 * @param metodoExterno the metodoExterno to set
	 */
	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}

	/**
	 * @return the cerhCobrancas
	 */
	public List<CerhCobranca> getCerhCobrancas() {
		return cerhCobrancas;
	}

	/**
	 * @param cerhCobrancas the cerhCobrancas to set
	 */
	public void setCerhCobrancas(List<CerhCobranca> cerhCobrancas) {
		this.cerhCobrancas = cerhCobrancas;
	}

}
