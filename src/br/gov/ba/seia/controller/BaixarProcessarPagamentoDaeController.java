package br.gov.ba.seia.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.ArquivoBaixaDae;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.PagamentoDae;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.SefazCodigoReceitaEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.ProcessoRequerimentoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ArquivoBaixaDaeService;
import br.gov.ba.seia.service.CumprimentoReposicaoFlorestalService;
import br.gov.ba.seia.service.HistSituacaoDaeService;
import br.gov.ba.seia.service.MemoriaCalculoDaeParcelaService;
import br.gov.ba.seia.service.PagamentoDaeService;
import br.gov.ba.seia.service.SefazCodigoReceitaService;
import br.gov.ba.seia.service.SituacaoDaeService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.XLSUtil;

@ViewScoped
@Named("baixarProcesarPagamentoDae")
public class BaixarProcessarPagamentoDaeController {

	private String arquivo;
	private String desCaminhoArquivo;
	String caminho;
	private List<Object[]> valoresPorLinhas;
	private PagamentoDae pagamentoDae = null;
	private List<PagamentoDae> listPagamentosDae = null;
	private List<ArquivoBaixaDae> listArquivoBaixaDae = null;
	private List<Dae> listDaePorPeriodo = null;
	private ArquivoBaixaDae arquivoBaixaDae = null;
	private HistSituacaoDae histSituacaoDae = null; 
	
	@EJB
	private SituacaoDaeService situacaoDaeService;
	
	@EJB
	private HistSituacaoDaeService histSituacaoDaeService;
	
	@EJB
	private PagamentoDaeService pagamentoDaeService;
	
	@EJB
	private ArquivoBaixaDaeService arquivoBaixaService;
	
	@EJB
	private SefazCodigoReceitaService sefazCodigoReceitaService;
	
	@EJB
	private CumprimentoReposicaoFlorestalService florestalService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private MemoriaCalculoDaeParcelaService memoriaCalculoDaeParcelaService;
	
	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;
	
	@EJB
	private AreaService areaService;
	
	@Inject
	private EmailUtil sendEmail;
	
	private Integer codigoReceita;
	
	@PostConstruct
	private void inicializar(){
		pagamentoDae = new PagamentoDae();
		arquivoBaixaDae = new ArquivoBaixaDae();
		histSituacaoDae = new HistSituacaoDae();
		listPagamentosDae = new ArrayList<PagamentoDae>();
		codigoReceita = (Integer) SessaoUtil.recuperarObjetoSessao("codigoSefaz");
		carregarArquivosDae();
	}
	
	private void carregarArquivosDae() {
		try {
			listArquivoBaixaDae = arquivoBaixaService.carregarArquivosBaixaDae(codigoReceita);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/*
	 * baixar arquivo e fazer as validação devidas
	 * */
	public void prepararUpload(FileUploadEvent event) throws Exception {
		try {
			caminho = DiretorioArquivoEnum.SEFAZ.toString() + File.separator + "Planilhas_Sefaz";
			desCaminhoArquivo = FileUploadUtil.Enviar(event, caminho);

			darBaixaArquivo(); 
			processarPagamento(arquivoBaixaDae,SefazCodigoReceitaEnum.getEnum(codigoReceita));
		
		//Sequência de exceptions para os Roolbacks funcionar atrelado ao tipo de exceção invocada
		}catch (SeiaValidacaoRuntimeException e) {
			MensagemUtil.erro("Algum DAE no arquivo não foi encontrado, falha no processamento.");
			arquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Dae não encontrado");
			arquivoBaixaService.atualizarArquivoBaixa(arquivoBaixaDae);
			deletarArquivo(desCaminhoArquivo);
			carregarArquivosDae();
		}catch (SeiaException e) {
			MensagemUtil.erro(Util.getString("ERRO-0009"));
			arquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Erro na formatação do arquivo");
			arquivoBaixaService.atualizarArquivoBaixa(arquivoBaixaDae);
			deletarArquivo(desCaminhoArquivo);
			carregarArquivosDae();
		}catch (RuntimeException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro(e.getMessage());
			arquivoBaixaDae.setDescricaoProcessamento(e.getMessage());
			arquivoBaixaService.atualizarArquivoBaixa(arquivoBaixaDae);
			carregarArquivosDae();
			deletarArquivo(desCaminhoArquivo);
		}catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " o documento.");
			deletarArquivo(desCaminhoArquivo);
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/*
	 *Dar baixa no arquivo, persistindo na tabela arquivo_baixa_dae 
	 */
	private void darBaixaArquivo() {
		
		try {
			
			SefazCodigoReceita sefazCodigo = sefazCodigoReceitaService.buscarSefazCodigo(codigoReceita);
			Date data = new Date(System.currentTimeMillis());
			Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
			arquivoBaixaDae = new ArquivoBaixaDae();
			
			arquivoBaixaDae.setIdeUsuario(usuario);
			arquivoBaixaDae.setNom_arquivo(FileUploadUtil.getFileName(desCaminhoArquivo));
			arquivoBaixaDae.setDataProcessamento(data);
			arquivoBaixaDae.setIndProcessado(Boolean.FALSE);
			arquivoBaixaDae.setSefazCodigoReceita(sefazCodigo);
			
			
			arquivoBaixaService.salvarArquivoBaixa(arquivoBaixaDae);
		
	} catch (Exception e) {
		Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	}
	
}
	/*
	 *Fazer o processo de pagamento do DAE
	 */	
	private void processarPagamento(ArquivoBaixaDae arquivoBaixaDae, SefazCodigoReceitaEnum codigoSefazParam) throws Exception {
		
		try {
 			listPagamentosDae = new ArrayList<PagamentoDae>();
			Integer cont = 0;
			
			//Pegar todos os valores da tabela xls da primeira página [0]
			valoresPorLinhas = XLSUtil.getTodosValoresPorLinha(desCaminhoArquivo, 0);
			
			Integer qtdLinhas = valoresPorLinhas.size();
			
			//verificar se a primeira e última linha da tabela tem 7 colunas
			if(valoresPorLinhas.get(0).length == 7 && valoresPorLinhas.get(qtdLinhas-2).length == 7){
				
				for(Object[] linha : valoresPorLinhas ){
					
					Integer codigoSefaz = null;
					
					if(linha.length == 7 && linha[4] != null){
						//pegar o código do dae contido na planilha
						codigoSefaz = Integer.valueOf(linha[4].toString().substring(0,4));
					}
					
					//comparar se o códigoSefaz mandado pela funcionalidade (CERH ou FLorestal) é o mesmo contido na linha atual da planilha
					if(codigoSefazParam.getId().equals(codigoSefaz)){
						
						if(linha[1] != null && linha[1] != ""){
							arquivoBaixaDae.setDataInicioPeriodoPagamento(Util.formataData(linha[1].toString()));
							break;
						}
						
					}
				}
				
				for(int i= qtdLinhas-2; i >= 0 ;i--){
					
					Integer codigoSefaz = null;
					
					if(valoresPorLinhas.get(i).length == 7 && valoresPorLinhas.get(i)[4] != null){
						
						codigoSefaz = Integer.valueOf(valoresPorLinhas.get(i)[4].toString().substring(0,4));
					}
					
					if(codigoSefazParam.getId().equals(codigoSefaz) && valoresPorLinhas.get(i)[1] != null && valoresPorLinhas.get(i)[1] != ""){
							arquivoBaixaDae.setDataFimPeriodoPagamento(Util.formataData(valoresPorLinhas.get(i)[1].toString()));
							break;
						
					}	
				}
				
				if(Util.isNullOuVazio(arquivoBaixaDae.getDataInicioPeriodoPagamento()) 
						|| Util.isNullOuVazio(arquivoBaixaDae.getDataFimPeriodoPagamento())){
					
					throw new Exception("Não foi encontrado nenhum Dae referente ao código de receita desta funcionalidade");
					
				}
				
			}
			
			
			
			/**
			 * Verificar se o periodo de pagamento já foi persistido anteriormente
			 */
			if(!periodoPagamentoExistente(arquivoBaixaDae)){
			
				for (Object[] linha : valoresPorLinhas) {
					if(linha.length == 7){
						
						Dae dae = new Dae();
						Integer codigoSefaz= 0;
						
						if(linha.length == 7 && linha[4] != null){
							codigoSefaz = Integer.valueOf(linha[4].toString().substring(0,4));
						}
						
						if(codigoSefazParam.getId().equals(codigoSefaz)){
						
							dae = pagamentoDaeService.obterDae(linha[0].toString());
							
							if(!Util.isNullOuVazio(dae)){
								pagamentoDae = new PagamentoDae();
								pagamentoDae.setIdeDae(dae);
								pagamentoDae.setIdeArquivoBaixaDae(arquivoBaixaDae);
								pagamentoDae.setDataPagamento(Util.formataData(linha[1].toString()));
								pagamentoDae.setValorPagamento(Double.parseDouble(linha[6].toString()));
								listPagamentosDae.add(pagamentoDae);
							}else{
								throw new SeiaValidacaoRuntimeException("Dae não encontrado na tabela");
							}
						}	
					}else if(linha.length != 7 && cont != (qtdLinhas-1)){
						throw new SeiaException("Erro na formatação do arquivo de pagamento DAE");
					}
						
					cont++;
				} 
				
				//Pegar DAES que estão no mesmo período da planilha baixada
				obterDaesDoPeriodo(arquivoBaixaDae);
								
				for (PagamentoDae pagamentoDae : listPagamentosDae) {
					histSituacaoDae = new HistSituacaoDae();
					histSituacaoDae.setDtAlteracao(arquivoBaixaDae.getDataProcessamento());
					histSituacaoDae.setIdeDae(pagamentoDae.getIdeDae());
					histSituacaoDae.setIdeSituacaoDae(situacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.PAGO.getId()));
					histSituacaoDae.setIdeUsuario(arquivoBaixaDae.getIdeUsuario()); 
					
					//Persistir Pagamento DAE
					pagamentoDaeService.salvarPagamentoDae(pagamentoDae);
					
					//Persistir atualização no histórico de situação do DAE
					histSituacaoDaeService.salvarHistSituacaoDae(histSituacaoDae);
					
					/*
					 * Após persistir o Dae como pago, revomer ele da lista dos DAEs
					 * Se o Dae continuar na lista, é porque naquele período ele não foi pago
					 * Os DAEs que continuarem na lista, serão posteriomente persistido como "vencido" no método verificarDaeVencido()
					 * */
					for(Dae item: listDaePorPeriodo){
						if(pagamentoDae.getIdeDae().getDscNossoNumero().equals(item.getDscNossoNumero())){
							listDaePorPeriodo.remove(item);
							break;
						}
					}
					
				}
				
				deletarArquivo(desCaminhoArquivo);
				
				//Se Código de receita for florestal,será feito a possível tramitação do requerimento
				if(codigoSefazParam.getId().equals(SefazCodigoReceitaEnum.REPO_FLORESTAL.getId())){
					
					CumprimentoReposicaoFlorestal florestal = new CumprimentoReposicaoFlorestal();
					for(PagamentoDae pagDae : listPagamentosDae){
						
						//verificar se o dae pago é a última parcela
						if(verificarUltimaParcela(pagDae.getIdeDae())){
							florestal =	florestalService.obterCumprimentoReposicaoFlorestalPorDAE(pagDae.getIdeDae());
							//tramitar requerimento
							florestal.getRequerimento().setIdeArea(areaService.carregarGet(AreaEnum.COASP.getId()));
							processoRequerimentoServiceFacade.gerarProcessoCumprimentoReposicaoFlorestal(florestal.getRequerimento(), florestal.getRequerimento().getIdeArea(), tramitacaoRequerimentoService);
							florestal = new CumprimentoReposicaoFlorestal();
						}
						
					}
				}
				
				Integer qtdLinhasProcessadas = 0;
				
				for(Object[] linha : valoresPorLinhas ){
					
					if(linha.length == 7 && linha[4] != null){
						
						if(codigoSefazParam.getId().equals(Integer.valueOf(linha[4].toString().substring(0,4)))){
							qtdLinhasProcessadas++;
						}
					}
				}
				
				arquivoBaixaDae.setDescricaoProcessamento("Arquivo processado com sucesso. Foram processados " + (qtdLinhasProcessadas) + " pagamentos");
				arquivoBaixaDae.setIndProcessado(Boolean.TRUE);
				//Atualizar informando quantoos DAEs foram pagaos
				arquivoBaixaService.atualizarArquivoBaixa(arquivoBaixaDae);
				
				notificarDaesPagos();
				
				verificarDaeVencido(arquivoBaixaDae);

				carregarArquivosDae();
				MensagemUtil.sucesso("Arquivo processado com sucesso. Foram processados " + (qtdLinhasProcessadas) + " pagamentos");
			}else{
				MensagemUtil.erro("Arquivo não processado. Período já processado anteriormente.");
				arquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Período já processado anteriormente.");
				arquivoBaixaService.atualizarArquivoBaixa(arquivoBaixaDae);
				deletarArquivo(desCaminhoArquivo);
				carregarArquivosDae();
			}
		} catch (IOException e) {
			deletarArquivo(desCaminhoArquivo);
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void obterDaesDoPeriodo(ArquivoBaixaDae arquivoBaixaDae) {
		listDaePorPeriodo = pagamentoDaeService.listarTodosDaesDoPeriodo(arquivoBaixaDae.getDataFimPeriodoPagamento());
		List<Dae> daesIndisponiveis = new ArrayList<Dae>();
		
		for(Dae dae : listDaePorPeriodo){
			HistSituacaoDae hist = histSituacaoDaeService.obterUltimoHistSituacaoDae(dae);
			
			if (!Util.isNullOuVazio(hist)
					&& !hist.getIdeSituacaoDae().getIdeSitucaoDae().equals(SituacaoDaeEnum.EM_ABERTO.getId())) {
				daesIndisponiveis.add(dae);
			}
		}
		for(Dae dae : daesIndisponiveis){
			listDaePorPeriodo.remove(dae);
		}
	}

	private void notificarDaesPagos() {
		try {
			if(!Util.isNullOuVazio(listPagamentosDae)){
				List<Dae> daes = new ArrayList<Dae>();

				Date data =  new Date();
				Locale local = new Locale("pt","BR");
				SimpleDateFormat anoReferencia = new SimpleDateFormat("yyyy",local);
				SimpleDateFormat meseReferencia = new SimpleDateFormat("MMMM",local);	
				
				for(PagamentoDae  pag : listPagamentosDae){
					daes.add(pag.getIdeDae());
				}
				List<Pessoa> pessoas = arquivoBaixaService.listarPessoasVinculasAosDaes(daes);
				for(Pessoa pessoa : pessoas){
					final String lAssunto = "[SEIA] - Pagamento do DAE confirmado";
					final StringBuilder lMsg = new StringBuilder();
					lMsg.append("Prezado(a) ");
					if(!Util.isNullOuVazio(pessoa.getPessoaFisica())){
						lMsg.append(pessoa.getPessoaFisica().getNomPessoa());
					}else{
						lMsg.append(pessoa.getPessoaJuridica().getNomeFantasia());
					}
					lMsg.append(", \nO pagamento do DAE no mês atual (");
					lMsg.append(meseReferencia.format(data));
					lMsg.append("/");
					lMsg.append(anoReferencia.format(data));
					lMsg.append(") para reposição florestal ");
					lMsg.append("já foi confirmado.");
					lMsg.append("\nFavor acessar o SEIA para gerar o certificado ou boleto da próxima parcela. \n");
					lMsg.append("Atte.,\n");
					lMsg.append("Central de Atendimento/INEMA.\n");
					
					sendEmail.enviarEmail(pessoa.getDesEmail(), lAssunto, lMsg.toString());
				}
				
			}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	//verificar se o dae pago é a última parcela
	public Boolean verificarUltimaParcela(Dae dae){
		try {
			MemoriaCalculoDaeParcela parcelaDae = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcelaPorDae(dae);
			if(parcelaDae.getNumParcelaReferencia().equals(parcelaDae.getIdeMemoriaCalculoDae().getQtdParcelas())){
				return true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return false;
	}
	
	//Verificar se os Daes desse períodos já foram pagos
	private Boolean periodoPagamentoExistente(ArquivoBaixaDae cerhArquivoBaixaDae) {
		
		if(!Util.isNullOuVazio(listArquivoBaixaDae)){
			
			String dataInicial = "";
			String dataFinal = "";
			
			if(!Util.isNullOuVazio(cerhArquivoBaixaDae.getDataInicioPeriodoPagamento() )){
				 dataInicial = Util.formatData(cerhArquivoBaixaDae.getDataInicioPeriodoPagamento());
			}
			
			if(!Util.isNullOuVazio(cerhArquivoBaixaDae.getDataInicioPeriodoPagamento() )){
				dataFinal = Util.formatData(cerhArquivoBaixaDae.getDataFimPeriodoPagamento());
			}
			String dataInicialInLista = "";
			String dataFinalInLista = "";
			
			for (ArquivoBaixaDae item : listArquivoBaixaDae) {
				if(!Util.isNullOuVazio(item.getDataInicioPeriodoPagamento()) && !Util.isNullOuVazio(item.getDataFimPeriodoPagamento())){
					
					dataInicialInLista = Util.formatData(item.getDataInicioPeriodoPagamento());
					dataFinalInLista = Util.formatData(item.getDataFimPeriodoPagamento());
					
					//Além de verificar se o período consta na base, é preciso saber se a operação foi processada com sucesso (isIndProcessado)
					if(dataInicialInLista.equals(dataInicial) && dataFinalInLista.equals(dataFinal) && item.isIndProcessado() && item.getSefazCodigoReceita().getNumCodigoCeceita().equals(codigoReceita)){
						return Boolean.TRUE;
					}

				}
			}
		}
		return Boolean.FALSE;
	}
	
	//Itens que estão na lista abaixo, serão persistidos no histórico com Situação "Vencido"
	private void verificarDaeVencido(ArquivoBaixaDae arquivoBaixaDae) throws Exception {
		
		if(!Util.isNullOuVazio(listDaePorPeriodo)){
			for(Dae item: listDaePorPeriodo){
				histSituacaoDae = new HistSituacaoDae();
				
				histSituacaoDae.setDtAlteracao(arquivoBaixaDae.getDataProcessamento());
				histSituacaoDae.setIdeDae(item);
				histSituacaoDae.setIdeSituacaoDae(situacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.VENCIDO.getId()));
				histSituacaoDae.setIdeUsuario(arquivoBaixaDae.getIdeUsuario());
				
				histSituacaoDaeService.salvarHistSituacaoDae(histSituacaoDae);
			}
			notificarDaeVencido();
		}
	}

	private void notificarDaeVencido() {
		
		try {				
				Date data =  new Date();
				Locale local = new Locale("pt","BR");
				SimpleDateFormat anoReferencia = new SimpleDateFormat("yyyy",local);
				SimpleDateFormat meseReferencia = new SimpleDateFormat("MMMM",local);	
				
				List<Pessoa> pessoas = arquivoBaixaService.listarPessoasVinculasAosDaes(listDaePorPeriodo);
				
				for(Pessoa pessoa : pessoas){
					final String lAssunto = "[SEIA] - Boleto do DAE vencido";
					final StringBuilder lMsg = new StringBuilder();
					lMsg.append("Prezado(a) ");
					if(!Util.isNullOuVazio(pessoa.getPessoaFisica())){
						lMsg.append(pessoa.getPessoaFisica().getNomPessoa());
					}else{
						lMsg.append(pessoa.getPessoaJuridica().getNomeFantasia());
					}
					lMsg.append(", <br>O pagamento do DAE no mês atual (");
					lMsg.append(meseReferencia.format(data));
					lMsg.append("/");
					lMsg.append(anoReferencia.format(data));
					lMsg.append(") para reposição florestal ");
					lMsg.append("<b>não</b> consta em nossos sistemas.");
					lMsg.append("<br>Favor acessar o SEIA para regularizar sua dívida. <br>");
					lMsg.append("Atte.,<br>");
					lMsg.append("Central de Atendimento/INEMA.<br>");
					
					sendEmail.enviarEmailHtml(pessoa.getDesEmail(), lAssunto, lMsg.toString());
				}
				
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}

	//remover arquivo do diretório
	public void deletarArquivo(String diretorio) {
		File file = new File(diretorio);
		if (file.exists() && !file.delete()) {
			file.deleteOnExit();
		}
		
	}
	
	public String getArquivo() {
		if(Util.isNull(arquivo) && !Util.isNullOuVazio(desCaminhoArquivo)){
			arquivo = desCaminhoArquivo;
		}
		return arquivo;
	}

	public String getDesCaminhoArquivo() {
		return desCaminhoArquivo;
	}

	public void setDesCaminhoArquivo(String desCaminhoArquivo) {
		this.desCaminhoArquivo = desCaminhoArquivo;
	}

	public List<ArquivoBaixaDae> getListArquivoBaixaDae() {
		return listArquivoBaixaDae;
	}

	public void setListArquivoBaixaDae(List<ArquivoBaixaDae> listArquivoBaixaDae) {
		this.listArquivoBaixaDae = listArquivoBaixaDae;
	}

	public Integer getCodigoReceita() {
		return codigoReceita;
	}

	public void setCodigoReceita(Integer codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

}
