package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.controller.BoletoAutomatizadoController;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.BoletoDaeHistorico;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducao;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoBoletoPagamento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaFormarProcessoException;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.BiomaRequerimentoService;
import br.gov.ba.seia.service.BoletoDaeHistoricoService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoHistoricoService;
import br.gov.ba.seia.service.BoletoService;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.ParametroCalculoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private BoletoService boletoService;
	@EJB
	private BoletoDaeRequerimentoService boletoDaeService;
	@EJB
	private BoletoPagamentoHistoricoService boletoPagamentoHistoricoService;
	@EJB
	private BoletoDaeHistoricoService boletoDaeHistoricoService;
	@EJB
	private BiomaRequerimentoService biomaRequerimentoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@EJB
	private EmailService emailService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private FlorestalService florestalService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private AreaService areaServive;
	@EJB
	private ParametroCalculoService parametroCalculoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	
	public void salvarBoletos(BoletoAutomatizadoController ctrl) throws Exception {

		
		for(DetalhamentoBoleto detalhamento : ctrl.getDetalhamentosBoleto()) {
			if(detalhamento.isExisteArea()) {
				if(!Util.isNull(detalhamento.getAreaVistoriada())) {
					if(detalhamento.getAreaVistoriada().compareTo(BigDecimal.ZERO)==-1 
					|| detalhamento.getAreaVistoriada().compareTo(BigDecimal.ZERO)==0) {
						JsfUtil.addWarnMessage("A área a ser vistoriada que foi informada é menor ou igual a zero.");
						return;				
					}
					
				}
				else{
					JsfUtil.addWarnMessage("Informe a área a ser vistoriada.");
					return;				
					
				}
			}
		}
		
		if (isRequerimentoFoiTramitado(ctrl.getRequerimento(), ctrl.getTramitacaoRequerimento())) {
			JsfUtil.addWarnMessage(Util.getString("requerimento_msg_ja_tramitado"));
			return;				
		}
		
		if (!boletosValidos(ctrl)) {
			return;
		}

		if(ctrl.getBoleto()!= null && ctrl.getBoleto().getIndBoletoGeradoManualmente() != null && ctrl.getBoleto().getIndBoletoGeradoManualmente()){
			salvarBoletoManualmente(ctrl); //#9241
			tramitarRequerimento(ctrl.getRequerimento(), StatusRequerimentoEnum.PAGAMENTO_LIBERADO, ctrl.getTramitacaoRequerimento());
			gerarComunicacaoRequerimento(ctrl);
			JsfUtil.addSuccessMessage("Boleto gerado com sucesso!");
		} else if((ctrl.getExisteBoleto() && ctrl.getBoleto().getIndIsento() && ctrl.getExisteDae()) //existe boleto, é isento e tem dae
			|| (!ctrl.getExisteBoleto() && ctrl.getExisteDae())) { //não existe boleto mas tem dae
			
			if(ctrl.getExisteBoleto()){
				salvarBoleto(ctrl);
			}
			
			if(deveSalvarDAE(ctrl) && !ctrl.getCertificado().isIndIsento()) {
				salvarCertificado(ctrl);
				salvarVistoria(ctrl);
			} else {
				salvarCertificado(ctrl);
			}
			
			tramitarRequerimento(ctrl.getRequerimento(), StatusRequerimentoEnum.PAGAMENTO_LIBERADO, ctrl.getTramitacaoRequerimento());
			gerarComunicacaoRequerimento(ctrl);
			JsfUtil.addSuccessMessage("Boleto gerado com sucesso!");
			
		} else if((ctrl.getExisteBoleto() && !ctrl.getBoleto().getIndIsento())){ //existe boleto e não é isento
				
				if(ctrl.getExisteBoleto()){
					salvarBoleto(ctrl);
				}
				
				if(deveSalvarDAE(ctrl)) {
					salvarCertificado(ctrl);
					salvarVistoria(ctrl);
				}
				
				tramitarRequerimento(ctrl.getRequerimento(), StatusRequerimentoEnum.BOLETO_EM_PROCESSAMENTO, ctrl.getTramitacaoRequerimento());
				JsfUtil.addSuccessMessage("Boleto gerado com sucesso!");
				
		} else if(ctrl.getExisteBoleto() && ctrl.getBoleto().getIndIsento() && !ctrl.getExisteDae()) { //existe boleto, é isento, não tem dae
			if(ctrl.getRequerimento().getIdeArea() == null){
				JsfUtil.addErrorMessage("Não foi possível gerar Boleto para esse requerimento.");
				JsfUtil.addErrorMessage("Não foi possível gerar Processo para esse Requerimento, pois não foi informado previamente a Área para onde o processo será encaminhado.");
				return;
			}
			salvarBoleto(ctrl);
			tramitarRequerimento(ctrl.getRequerimento(), StatusRequerimentoEnum.PAGAMENTO_LIBERADO, ctrl.getTramitacaoRequerimento());
			gerarProcesso(ctrl);
			gerarComunicacaoRequerimento(ctrl);
			
		}
		RequestContext.getCurrentInstance().execute("dialogBoleto.hide()");
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarRequerimento(Requerimento requerimento, StatusRequerimentoEnum statusRequerimentoEnum, TramitacaoRequerimento tramitacao) {
		tramitacaoRequerimentoService.tramitarSemFlush(requerimento, statusRequerimentoEnum, getOperador());
		if(isRequerimentoFoiTramitado(requerimento, tramitacao)) {
			throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado"));
		}
	}
	
	
	private boolean boletosValidos(BoletoAutomatizadoController ctrl) {
		
		boolean valido = true;
		
		if(ctrl.getDetalhamentosBoleto().isEmpty()) {
			JsfUtil.addWarnMessage("Nenhum parâmetro foi atribuído.");
			valido = false;
		}
		
		if ((ctrl.getExisteBoleto() && ctrl.getBoleto().getIndIsento() && Util.isNull(ctrl.getBoleto().getIdeMotivoIsencaoBoleto())) ||
				(ctrl.getExisteDae() && ctrl.getCertificado().isIndIsento() && Util.isNullOuVazio(ctrl.getCertificado().getIdeMotivoIsencaoBoleto()))) {
			JsfUtil.addErrorMessage("O campo Motivo Isenção é de preenchimento obrigatório.");
			valido = false;
		}

		BigDecimal valorTotalDAECertificado = new BigDecimal(ctrl.calcularCertificado());

		if (ctrl.getExisteDae() && valorTotalDAECertificado.doubleValue() == 0) {
			JsfUtil.addErrorMessage("Favor calcular o valor do DAE ");
			valido = false;
		}
		
	    if(isTotaInvalido(ctrl)){
	    	valido =false;
	    }
		
	    boolean isSomenteSisfauna= true;
	    BigDecimal valorTotalBoleto = new BigDecimal(0);
	    
	    /* Verifica todos os detalhamentos para descobrir se algum ato é de SISFAUNA */
	    for (DetalhamentoBoleto detalhamentoBoleto : ctrl.getDetalhamentosBoleto()) {
	    	
	    	System.out.println("andre");
			System.out.println("=========================================================================");
			System.out.println("isSomenteSisfauna"+isSomenteSisfauna);
			System.out.println("=========================================================================");
			
	    	if(!detalhamentoBoleto.getIdeAtoAmbiental().isSisfauna()){
	    		
	    		
	    		isSomenteSisfauna = false;
	    		
	    	} else if(!Util.isNullOuVazio(detalhamentoBoleto.getValor())) {
	    		valorTotalBoleto =  valorTotalBoleto.add(detalhamentoBoleto.getValor());
	    	}
	    }

		for (DetalhamentoBoleto detalhamentoBoleto : ctrl.getDetalhamentosBoleto()) {
			
			/* Quando o boleto possuir SOMENTE atos de Sisfauna o valor da Soma do boleto NÃO PODE ser zero */
			if(isSomenteSisfauna){
				
				if(valorTotalBoleto.compareTo(new BigDecimal(0)) == 0 && !ctrl.getBoleto().getIndIsento()){
					JsfUtil.addErrorMessage("O valor total do boleto não pode ser zero.");
					valido = false;
				}
			
			/* Quando o boleto possuir atos além de Sisfauna, nenhum ato excerto sisfauna pode ter valor igual a zero */
			} else if(!isSomenteSisfauna){
				
				if (!detalhamentoBoleto.getIdeAtoAmbiental().isSisfauna() && !detalhamentoBoleto.isDae() && Util.isNullOuVazio(detalhamentoBoleto.getValor()) && !ctrl.getBoleto().getIndIsento() && !detalhamentoBoleto.getIdeAtoAmbiental().isAPE()) {
					
					JsfUtil.addErrorMessage("Favor calcular o valor do Ato Ambiental: " + detalhamentoBoleto.getIdeAtoAmbiental().getNomAtoAmbiental());
					valido = false;
				}
			}
		}		

		return valido;
	}

	private boolean isTotaInvalido(BoletoAutomatizadoController ctrl) {
		if(ctrl.isPerfuracaoPocoEnquadramentoAto() || ctrl.isTLAEnquadramentoAto()) {
			
			BigDecimal valorTotalDosOutrosDetalhamentos = new BigDecimal(0);
			BigDecimal valorTotalDetalhamentos = new BigDecimal(0);
			
			for (DetalhamentoBoleto detalhamento : ctrl.getDetalhamentosBoleto()) {				
				if(!Util.isNull(detalhamento.getValor())) {					
					valorTotalDetalhamentos = valorTotalDetalhamentos.add(detalhamento.getValor());
					if(!detalhamento.getIdeAtoAmbiental().isPerfuracaoDePoco() && !detalhamento.getIdeAtoAmbiental().isTLA()){
						valorTotalDosOutrosDetalhamentos = valorTotalDosOutrosDetalhamentos.add(detalhamento.getValor()); 
					}
				}
			}			

			ParametroCalculo pc = null;
			try {
				pc = parametroCalculoService.obterParametroCalculoPorAtoSemClasse(98).get(0);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			BigDecimal valorPorAto = new BigDecimal(0);
			if(!Util.isNullOuVazio(pc)){
				valorPorAto = pc.getValorTaxa();
			}
			
			if(valorPorAto.compareTo(ctrl.getBoleto().getValBoleto()) == 1 && !ctrl.getBoleto().getIndIsento()){
				JsfUtil.addErrorMessage("O valor Total do Boleto não pode ser menor que R$ " + new DecimalFormat("#,###.00").format(valorPorAto));
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoFoiTramitado(Requerimento requerimento, TramitacaoRequerimento tramitacao) {
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(requerimento.getIdeRequerimento(), tramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarProcesso(BoletoAutomatizadoController ctrl) throws Exception {
		Area area = ctrl.getRequerimento().getIdeArea();
		Pessoa operador = getOperador();
		tramitacaoRequerimentoService.tramitar(ctrl.getRequerimento(), StatusRequerimentoEnum.FORMADO, operador);
		processoService.gerarProcesso(ctrl.getRequerimento(), operador, area.getIdeArea(), false);
		RequestContext.getCurrentInstance().execute("dialogBoleto.hide()");
		JsfUtil.addSuccessMessage("Processo formado com sucesso!");
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcesso(Requerimento requerimento) throws Exception {
		
		validarPossibilidadeGerarProcesso(requerimento);
		
		Pessoa operador = getOperador();
		if(requerimento.getNumRequerimento().toLowerCase().contains("inexig")){
			this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.DECLARACAO_EMITIDA,operador);
		}else{
			tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.FORMADO, operador);
			processoService.gerarProcesso(requerimento, operador, requerimento.getIdeArea().getIdeArea(), false);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcessoLoteBoleto(Requerimento requerimento) throws Exception {
		
		validarPossibilidadeGerarProcesso(requerimento);
		
		Pessoa operador = getOperador();
		if(requerimento.getNumRequerimento().toLowerCase().contains("inexig")){
			this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.DECLARACAO_EMITIDA,operador);
		}else{
			tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.FORMADO, operador);
			processoService.gerarProcessoLoteBoleto(requerimento, operador, requerimento.getIdeArea().getIdeArea(), false);
		}
	}

	public void validarPossibilidadeGerarProcesso(Requerimento requerimento) throws Exception, SeiaFormarProcessoException {
		
		if(requerimento.getNumRequerimento().toLowerCase().contains("inexig") && Util.isNullOuVazio(requerimento.getIdeArea())){
			requerimento.setIdeArea(areaServive.carregarGet(AreaEnum.ATEND.getId()));
		}
				
		if (Util.isNullOuVazio(requerimento.getIdeArea())){
			throw new SeiaFormarProcessoException("Não foi informado a área onde o processo do requerimento "
					.concat(requerimento.getNumRequerimento())
					.concat(" será formado. Favor entrar em contato com o Atendimento."));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarBoleto(BoletoAutomatizadoController ctrl) throws Exception {
		ctrl.gerarBoleto();
		ctrl.getBoleto().setIdeTipoBoletoPagamento(new TipoBoletoPagamento(1));//ide_tipo_boleto_pagamento = 1 é o tipo 'Requerimento'
		boletoService.salvarBoleto(ctrl.getBoleto());
		if(!ctrl.getBoleto().getIndIsento()) {
			boletoPagamentoHistoricoService.salvar(new BoletoPagamentoHistorico(ctrl.getBoleto(), StatusBoletoEnum.EMITIDO.getId(), new Date(), getOperador()));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarVistoria(BoletoAutomatizadoController ctrl) throws Exception {
		
		Double valorTotalVistoriaDAE = ctrl.getValorTotalVistoriaDAE();
		
		if (valorTotalVistoriaDAE > 0) {
			ctrl.gerarDAEVistoria(valorTotalVistoriaDAE);
			boletoDaeService.salvarOuAtualizar(ctrl.getVistoria());
			biomaRequerimentoService.salvarListaBiomaRequerimento(ctrl.getListaBiomaRequerimento());
			boletoDaeHistoricoService.salvar(new BoletoDaeHistorico(ctrl.getVistoria(), StatusBoletoEnum.EMITIDO.getId(), new Date(), getOperador()));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCertificado(BoletoAutomatizadoController ctrl) throws Exception {
		Double valorTotalDAECertificado = ctrl.calcularCertificado().doubleValue();
		if (valorTotalDAECertificado > 0) {
			ctrl.gerarDAECertificado(valorTotalDAECertificado);
			boletoDaeService.salvarOuAtualizar(ctrl.getCertificado());
			boletoDaeHistoricoService.salvar(new BoletoDaeHistorico(ctrl.getCertificado(), StatusBoletoEnum.EMITIDO.getId(), new Date(), getOperador()));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	private void gerarComunicacaoRequerimento(BoletoAutomatizadoController ctrl) throws Exception {
		
		Requerimento requerimento = ctrl.getRequerimento();
		BoletoPagamentoRequerimento boleto = ctrl.getBoleto();
		BoletoDaeRequerimento vistoria = ctrl.getVistoria();
		BoletoDaeRequerimento certificado = ctrl.getCertificado();
		
		final String lAssunto = "[SEIA] - Boleto/DAE do Requerimento de nº " + requerimento.getNumRequerimento();
		
		final StringBuilder mensagem = new StringBuilder();
		
		mensagem.append("Prezado(a),\n");
		
		if (ctrl.getExisteBoleto())	{
			if(boleto.getIndIsento()) {
				mensagem.append("\nO Requerimento de nº '" + requerimento.getNumRequerimento() + "' ");
				mensagem.append("foi insento do valor referente as taxas das solicitações realizadas no SEIA.\n");
			}
			else {
				mensagem.append("\nO boleto de pagamento do Requerimento de nº '" + requerimento.getNumRequerimento() + "' já está disponível.\n");
				mensagem.append("Favor acessar o SEIA para efetuar o download do boleto.\n");
			}
		}

		if (ctrl.getExisteDae()) {
			DecimalFormat df = Util.getDecimalFormatPtBr();
			String textoPadrao  = "A geração de DAE é feita a partir do site da SEFAZ (www.sefaz.ba.gov.br) no seguinte caminho: \"Inspetoria eletrônica - Pagamentos - Cálculo e geração DAE - Taxas. \n"
							    + "Utilize os dados abaixo para geração do DAE : \n";
			
			if (!Util.isNullOuVazio(ctrl.getCertificado())) {
				mensagem.append("\nO DAE (Documento de Arrecadação Estadual) de Certificado do Requerimento de nº '" + requerimento.getNumRequerimento() +"' já está disponível.\n");
				mensagem.append(textoPadrao);
				mensagem.append("Código: 2214 \n");
				mensagem.append("Valor: R$ "+df.format(certificado.getVlrTotalCertificado())+"\n");
			}
			
			if (!Util.isNullOuVazio(ctrl.getVistoria())) {
				mensagem.append("\nO DAE (Documento de Arrecadação Estadual) de Vistoria do Requerimento de nº '" + requerimento.getNumRequerimento() + "' já está disponível.\n");
				mensagem.append(textoPadrao);
				mensagem.append("Código: 2345 \n");
				mensagem.append("Valor: R$ "+df.format(vistoria.getVlrTotalVistoria())+"\n");
			}
			
			mensagem.append("\nApós o pagamento, favor acessar o SEIA para efetuar o envio do comprovante de pagamento do DAE.\n");
		}
		
		mensagem.append("\nAtte.,\n");
		mensagem.append("Central de Atendimento/INEMA.\n");
		
		ComunicacaoRequerimento comunicacaoRequerimento = gerarComunicacao(requerimento, lAssunto, mensagem);
		this.comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		
		emailService.enviarEmailsAoRequerente(requerimento, lAssunto, mensagem.toString());
	}
	
	//Metodo para gerar comunicado ao requerente quando o boleto for importado no BB cobranca
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void gerarComunicacaoRequerimento(Requerimento requerimento, BoletoPagamentoRequerimento boleto, BoletoDaeRequerimento vistoria, BoletoDaeRequerimento certificado) {
		
		final String lAssunto = "[SEIA] - Boleto/DAE do Requerimento de nº " + requerimento.getNumRequerimento();
		final StringBuilder mensagem = new StringBuilder();
		
		mensagem.append("Prezado(a),\n");
		
		if (boleto != null)	{
			if(boleto.getIndIsento()) {
				mensagem.append("\nO Requerimento de nº '" + requerimento.getNumRequerimento() + "' ");
				mensagem.append("foi insento do valor referente as taxas das solicitações realizadas no SEIA.\n");
			}
			else {
				mensagem.append("\nO boleto de pagamento do Requerimento de nº '" + requerimento.getNumRequerimento() + "' já está disponível.\n");
				mensagem.append("Favor acessar o SEIA para efetuar o download do boleto.\n");
			}
		}

		if (vistoria != null || certificado != null) {
			DecimalFormat df = Util.getDecimalFormatPtBr();
			String textoPadrao  = "A geração de DAE é feita a partir do site da SEFAZ (www.sefaz.ba.gov.br) no seguinte caminho: \"Inspetoria eletrônica - Pagamentos - Cálculo e geração DAE - Taxas. \n"
							    + "Utilize os dados abaixo para geração do DAE : \n";
			
			if (!Util.isNullOuVazio(certificado)) {
				mensagem.append("\nO DAE (Documento de Arrecadação Estadual) de Certificado do Requerimento de nº '" + requerimento.getNumRequerimento() +"' já está disponível.\n");
				mensagem.append(textoPadrao);
				mensagem.append("Código: 2214 \n");
				mensagem.append("Valor: R$ "+df.format(certificado.getVlrTotalCertificado())+"\n");
			}
			
			if (!Util.isNullOuVazio(vistoria)) {
				mensagem.append("\nO DAE (Documento de Arrecadação Estadual) de Vistoria do Requerimento de nº '" + requerimento.getNumRequerimento() + "' já está disponível.\n");
				mensagem.append(textoPadrao);
				mensagem.append("Código: 2345 \n");
				mensagem.append("Valor: R$ "+df.format(vistoria.getVlrTotalVistoria())+"\n");
			}
			
			mensagem.append("\nApós o pagamento, favor acessar o SEIA para efetuar o envio do comprovante de pagamento do DAE.\n");
		}
		
		mensagem.append("\nAtte.,\n");
		mensagem.append("Central de Atendimento/INEMA.\n");
		
		ComunicacaoRequerimento comunicacaoRequerimento = gerarComunicacao(requerimento, lAssunto, mensagem);
		this.comunicacaoRequerimentoService.salvar(comunicacaoRequerimento);
		
		emailService.enviarEmailsAoRequerente(requerimento, lAssunto, mensagem.toString());
	}
	
	private ComunicacaoRequerimento gerarComunicacao(Requerimento requerimento, final String lAssunto, final StringBuilder mensagem) {
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(mensagem.toString());
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimento.setAssunto(lAssunto);
		return comunicacaoRequerimento;
	}
	
	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}
	
	public PerguntaRequerimento buscarPerguntaRequerimentoPorRequerimentoECodPergunta(Requerimento req, String codPergunta) {
		try {
			return perguntaRequerimentoService.buscarPerguntaRequerimentoPorRequerimentoECodPergunta(req, codPergunta);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<PerguntaRequerimento> listarPerguntaRequerimentoPorRequerimentoECodPergunta(Requerimento req, String codPergunta) {
		try {
			return perguntaRequerimentoService.listarPerguntaRequerimentoPorRequerimentoECodPergunta(req, codPergunta);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<Florestal> listarFlorestalPorRequerimento(Requerimento req) {
		try {
			return florestalService.carregarListaFlorestal(req);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<FlorestalCaracteristicaFlorestaProducao> listarFlorestalCaracteristicaFlorestaProducaoPorFlorestal(Florestal f) {
		try {
			return florestalService.obterListaFlorestaProducaoSelecionado(f);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean deveSalvarDAE(BoletoAutomatizadoController ctrl) {
		
		if(ctrl.getDetalhamentosBoleto().size() > 1) return true;
		
		for (DetalhamentoBoleto db : ctrl.getDetalhamentosBoleto()) {
			if(db.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.DQC.getId())) {
				return false;
			}
		}
		
		return true;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED) //#9241
	private void salvarBoletoManualmente(BoletoAutomatizadoController ctrl) throws Exception {
		ctrl.gerarBoleto();
		ctrl.getBoleto().setIdeTipoBoletoPagamento(new TipoBoletoPagamento(1));//ide_tipo_boleto_pagamento = 1 é o tipo 'Requerimento'
		boletoService.salvarBoletoManualmente(ctrl.getBoleto());
		boletoPagamentoHistoricoService.salvar(new BoletoPagamentoHistorico(ctrl.getBoleto(), StatusBoletoEnum.EMITIDO.getId(), new Date(), getOperador()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRequerimentoInexigibilidade(Requerimento requerimento) throws Exception {
		boolean isInexigibilidade = false;
		
		List<EnquadramentoAtoAmbiental> lista = enquadramentoService.buscarEnquadramentoAtoAmbientalByRequerimento(requerimento.getIdeRequerimento());
		
		if(!Util.isNullOuVazio(lista)) {
			for(EnquadramentoAtoAmbiental ato : lista) {
				if(ato.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.INEXIGIBILIDADE.getId())) {
					isInexigibilidade = true;
				}
			}
		}
			
		return isInexigibilidade;
	}
}