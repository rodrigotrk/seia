package br.gov.ba.seia.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.ArquivoBaixaDae;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.PagamentoDae;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CerhArquivoBaixaDaeService;
import br.gov.ba.seia.service.CerhHistSituacaoDaeService;
import br.gov.ba.seia.service.CerhPagamentoDaeService;
import br.gov.ba.seia.service.CerhSituacaoDaeService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.XLSUtil;

@ViewScoped
@Named("cerhBaixarProcesarPagamentoDae")
public class CerhBaixarProcesarPagamentoDaeController {

	private String arquivo;
	private String desCaminhoArquivo;
	String caminho;
	private List<Object[]> valoresPorLinhas;
	private PagamentoDae cerhPagamentoDae = null;
	private List<PagamentoDae> listCerhPagamentosDae = null;
	private List<ArquivoBaixaDae> listArquivoBaixaDae = null;
	private List<Dae> listCerhDaePorPeriodo = null;
	private ArquivoBaixaDae cerhArquivoBaixaDae = null;
	private HistSituacaoDae cerhHistSituacaoDae = null; 
	private Boolean nomeArquivoDuplicado;
	
	@EJB
	private CerhSituacaoDaeService cerhSituacaoDaeService;
	
	@EJB
	private CerhHistSituacaoDaeService cerhHistSituacaoDaeService;
	
	@EJB
	private CerhPagamentoDaeService cerhPagamentoDaeService;
	
	@EJB
	private CerhArquivoBaixaDaeService cerhArquivoBaixaService;
	
	@PostConstruct
	private void inicializar(){
		cerhPagamentoDae = new PagamentoDae();
		cerhArquivoBaixaDae = new ArquivoBaixaDae();
		cerhHistSituacaoDae = new HistSituacaoDae();
		listCerhPagamentosDae = new ArrayList<PagamentoDae>();
		
		carregarArquivosDae();
	}
	
	private void carregarArquivosDae() {
		try {
			listArquivoBaixaDae = cerhArquivoBaixaService.carregarArquivosBaixaDae();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/*
	 * baixar arquivo e fazer as validação devidas
	 * */
	public void prepararUpload(FileUploadEvent event) throws Exception {
		try {
			caminho = DiretorioArquivoEnum.CERH.toString() + File.separator + "Planilhas_Sefaz";
			desCaminhoArquivo = FileUploadUtil.Enviar(event, caminho);

			darBaixaArquivo(); 
			processarPagamento(cerhArquivoBaixaDae);
		
		//Sequência de exceptions para os Roolbacks funcionar atrelado ao tipo de exceção invocada
		}catch (SeiaValidacaoRuntimeException e) {
			MensagemUtil.erro("Algum DAE no arquivo não foi encontrado, falha no processamento.");
			cerhArquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Dae não encontrado");
			cerhArquivoBaixaService.atualizarArquivoBaixa(cerhArquivoBaixaDae);
			deletarArquivo(desCaminhoArquivo);
			carregarArquivosDae();
		}catch (SeiaException e) {
			MensagemUtil.erro(Util.getString("ERRO-0009"));
			cerhArquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Erro na formatação do arquivo");
			cerhArquivoBaixaService.atualizarArquivoBaixa(cerhArquivoBaixaDae);
			deletarArquivo(desCaminhoArquivo);
			carregarArquivosDae();
		}catch (RuntimeException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro("Arquivo de mesmo nome já processado anteriormente");
			cerhArquivoBaixaDae.setDescricaoProcessamento("Arquivo de mesmo nome já processado anteriormente");
			cerhArquivoBaixaService.atualizarArquivoBaixa(cerhArquivoBaixaDae);
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
	 *Dar baixa no arquivo, persistindo na tabela cerh_arquivo_baixa_dae 
	 */
	private void darBaixaArquivo() {
		
		try {
			nomeArquivoDuplicado = Boolean.FALSE;
			Date data = new Date(System.currentTimeMillis());
			Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
			cerhArquivoBaixaDae = new ArquivoBaixaDae();
			
			cerhArquivoBaixaDae.setIdeUsuario(usuario);
			cerhArquivoBaixaDae.setNom_arquivo(FileUploadUtil.getFileName(desCaminhoArquivo));
			cerhArquivoBaixaDae.setDataProcessamento(data);
			cerhArquivoBaixaDae.setIndProcessado(Boolean.FALSE);
			
			//Verificar se já existe arquivo persistido com o mesmo no do arquivo que está sendo baixado
			if(!Util.isNullOuVazio(listArquivoBaixaDae)){
				for (ArquivoBaixaDae item : listArquivoBaixaDae) {
					if(item.getNomArquivo().equals(cerhArquivoBaixaDae.getNomArquivo()) && item.isIndProcessado()){
						nomeArquivoDuplicado = Boolean.TRUE;
						break;
					}
				}
			}	
			
			cerhArquivoBaixaService.salvarArquivoBaixa(cerhArquivoBaixaDae);
		
	} catch (Exception e) {
		Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	}
	
}
	/*
	 *Fazer o processo de pagamento do DAE
	 */	
	private void processarPagamento(ArquivoBaixaDae cerhArquivoBaixaDae) throws Exception {
		
		try {
			listCerhPagamentosDae = new ArrayList<PagamentoDae>();
			Integer cont = 0;
			
			//Pegar todos os valores da tabela xls da primeira página [0]
			valoresPorLinhas = XLSUtil.getTodosValoresPorLinha(desCaminhoArquivo, 0);
			Integer qtdLinhas = valoresPorLinhas.size();
			
			//verificar se a primeira e última linha da tabela tem 8 colunas
			if(valoresPorLinhas.get(0).length == 8 && valoresPorLinhas.get(qtdLinhas-2).length == 8){
				
				if(valoresPorLinhas.get(0)[1] != null && valoresPorLinhas.get(0)[1] != ""){
					cerhArquivoBaixaDae.setDataInicioPeriodoPagamento(Util.formataData(valoresPorLinhas.get(0)[1].toString()));
				}
				if(valoresPorLinhas.get(qtdLinhas-2)[1] != null && valoresPorLinhas.get(qtdLinhas-2)[1] != ""){
					cerhArquivoBaixaDae.setDataFimPeriodoPagamento(Util.formataData(valoresPorLinhas.get(qtdLinhas-2)[1].toString()));
				}
			}
			
			//Verificar se o arquivo está com nome duplicado
			if(nomeArquivoDuplicado){
				throw new RuntimeException("Nome do arquivo já existente");
			}
			
			//Verificar se o periodo de pagamento já foi persistido anteriormente
			if(!periodoPagamentoExistente(cerhArquivoBaixaDae)){
			
				for (Object[] linha : valoresPorLinhas) {
					if(linha.length == 8){
						
						Dae dae = cerhPagamentoDaeService.obterCerhCnae(linha[0].toString());
						
						if(!Util.isNullOuVazio(dae)){
							cerhPagamentoDae = new PagamentoDae();
							cerhPagamentoDae.setIdeDae(dae);
							cerhPagamentoDae.setIdeArquivoBaixaDae(cerhArquivoBaixaDae);
							cerhPagamentoDae.setDataPagamento(Util.formataData(linha[1].toString()));
							cerhPagamentoDae.setValorPagamento(Double.parseDouble(linha[7].toString()));
							listCerhPagamentosDae.add(cerhPagamentoDae);
						}else{
							throw new SeiaValidacaoRuntimeException("Cnae não encontrado na tabela");
						}
					}else if(linha.length != 8 && cont != (qtdLinhas-1)){
						throw new SeiaException("Erro na formatação do arquivo de pagamento DAE");
					}
					cont++;
				} 
				
				//Pegar DAES que estão no mesmo perído da planilha baixada
				listCerhDaePorPeriodo = cerhPagamentoDaeService.listarTodosCerhCnae(cerhArquivoBaixaDae.getDataInicioPeriodoPagamento(), cerhArquivoBaixaDae.getDataFimPeriodoPagamento());
				
				for (PagamentoDae pagamentoDae : listCerhPagamentosDae) {
					cerhHistSituacaoDae = new HistSituacaoDae();
					cerhHistSituacaoDae.setDtAlteracao(cerhArquivoBaixaDae.getDataProcessamento());
					cerhHistSituacaoDae.setIdeDae(pagamentoDae.getIdeDae());
					cerhHistSituacaoDae.setIdeSituacaoDae(cerhSituacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.PAGO.getId()));
					cerhHistSituacaoDae.setIdeUsuario(cerhArquivoBaixaDae.getIdeUsuario()); 
					
					//Persistir Pagamento DAE
					cerhPagamentoDaeService.salvarCerhPagamentoDae(pagamentoDae);
					
					//Persistir atualização no histórico de situação do DAE
					cerhHistSituacaoDaeService.salvarHistSituacaoDae(cerhHistSituacaoDae);
					
					/*
					 * Após persistir o Dae como pago, revomer ele da lista dos DAEs
					 * Se o Dae continuar na lista, é porque naquele período ele não foi pago
					 * Os DAEs que continuarem na lista, serão posteriomente persistido como "vencido" no método verificarDaeVencido()
					 * */
					for(Dae item: listCerhDaePorPeriodo){
						if(pagamentoDae.getIdeDae().getDscNossoNumero().equals(item.getDscNossoNumero())){
							listCerhDaePorPeriodo.remove(item);
							break;
						}
					}
					
				}
				
				deletarArquivo(desCaminhoArquivo);
				
				cerhArquivoBaixaDae.setDescricaoProcessamento("Arquivo processado com sucesso. Foram processados " + (qtdLinhas-1) + " pagamentos");
				cerhArquivoBaixaDae.setIndProcessado(Boolean.TRUE);
				//Atualizar informando quantoos DAEs foram pagaos
				cerhArquivoBaixaService.atualizarArquivoBaixa(cerhArquivoBaixaDae);
				
				verificarDaeVencido(cerhArquivoBaixaDae);

				carregarArquivosDae();
				MensagemUtil.sucesso("Arquivo processado com sucesso. Foram processados " + (qtdLinhas-1) + " pagamentos");
			}else{
				MensagemUtil.erro("Arquivo não processado. Período já processado anteriormente.");
				cerhArquivoBaixaDae.setDescricaoProcessamento("Arquivo não processado. Período já processado anteriormente.");
				cerhArquivoBaixaService.atualizarArquivoBaixa(cerhArquivoBaixaDae);
				deletarArquivo(desCaminhoArquivo);
				carregarArquivosDae();
			}
		} catch (IOException e) {
			deletarArquivo(desCaminhoArquivo);
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}


	//Verificar se os Daes desse períodos já foram pagos
	private Boolean periodoPagamentoExistente(ArquivoBaixaDae cerhArquivoBaixaDae)  {
		
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
					if(dataInicialInLista.equals(dataInicial) && dataFinalInLista.equals(dataFinal) && item.isIndProcessado()){
						return Boolean.TRUE;
					}

				}
			}
		}
		return Boolean.FALSE;
	}
	
	//Itens que estão na lista abaixo, serão persistidos no histórico com Situação "Vencido"
	private void verificarDaeVencido(ArquivoBaixaDae cerhArquivoBaixaDae)  {
		
		for(Dae item: listCerhDaePorPeriodo){
			cerhHistSituacaoDae = new HistSituacaoDae();
			
			cerhHistSituacaoDae.setDtAlteracao(cerhArquivoBaixaDae.getDataProcessamento());
			cerhHistSituacaoDae.setIdeDae(item);
			cerhHistSituacaoDae.setIdeSituacaoDae(cerhSituacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.VENCIDO.getId()));
			cerhHistSituacaoDae.setIdeUsuario(cerhArquivoBaixaDae.getIdeUsuario()); 
			
			cerhHistSituacaoDaeService.salvarHistSituacaoDae(cerhHistSituacaoDae);
		}
		
	}

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
	
}
