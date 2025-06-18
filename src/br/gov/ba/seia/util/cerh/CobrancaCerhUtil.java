package br.gov.ba.seia.util.cerh;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao;
import br.gov.ba.seia.dto.EmissaoDocumentoArrecadacaoReqDTO.DocumentoArrecadacao.Referencia;
import br.gov.ba.seia.dto.EmitirDocumentoArrecadacaoResponseDTO.RetEmissaoDocumentoArrecadacaoDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.entity.CerhClasseCorpoHidrico;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhDaeTipoUso;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhParcelasCobranca;
import br.gov.ba.seia.entity.CerhPondClasCorpoHidrico;
import br.gov.ba.seia.entity.CerhPondGestao;
import br.gov.ba.seia.entity.CerhPondVolConsumido;
import br.gov.ba.seia.entity.CerhPrecoPubUnitario;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ClasseCorpoHidricoEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.facade.CerhCobrancaFacade;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.FormaterUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityUser;
import br.gov.ba.ws.enumerator.OpcaoPagamentoEnum;
/**
 * Classe utilitária de cobrança cerh
 * @author 
 *
 */
public final class CobrancaCerhUtil {

	private CerhCobrancaFacade facade;

	private static final double TAXA_ATRASO = 0.02; //2%
	
	private static final double TAXA_SELIC_DEFAULT = 0.01; //1%
	
	private static final double TAR = 79.87;
	
	private static final double P = 0.75;
			
	private static final double VAZAO_ISENCAO_ABAST_HUMAN = 129.6d;

	private static final double VAZAO_ISENCAO = 43.2d;

	private static final int PRAZO_CORREIOS = 20;
	
	public CobrancaCerhUtil(CerhCobrancaFacade facade){
		this.facade = facade;
	}
	/**
	 * Metodo para montar cobraca CERH
	 * @param cerh
	 * @param correios
	 * @param opcaoPagamento
	 * @param anoBase
	 * @return cobrança
	 * @throws Exception
	 */
	public List<CerhCobranca> montarCobrancas(Cerh cerh, boolean correios, OpcaoPagamentoEnum opcaoPagamento, int anoBase) throws Exception{
		List<ValorTipoUso> valorTipoUsos = new ArrayList<CobrancaCerhUtil.ValorTipoUso>();
		//DAEs
		for(CerhTipoUso tipoUso : cerh.getCerhTipoUsoCollection()){
			for (CerhLocalizacaoGeografica loc : tipoUso.getCerhLocalizacaoGeograficaCollection()) {
				String[] rpgas = facade.getRpga(loc.getIdeLocalizacaoGeografica()).split(",");
				for(String rpga : rpgas){
					//recuperar o Valor de Cada tipo de Uso individualmente
					ValorTipoUso valorTipoUso = new ValorTipoUso(tipoUso.getIdeTipoUsoRecursoHidrico(), new BigDecimal(0), rpga, loc);
					//Calculo de Cobrança
					//A. Lançamento de Efluentes e Captação Superficial deverão fazer parte do mesmo grupo. 
					if(tipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE.getId())
							|| tipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL.getId())){
						if(!Util.isNullOuVazio(loc.getIdeCerhLancamentoEfluenteCaracterizacao())){
							valorTipoUso.setValorFinal(getTotalLancamentoEfluentes(loc.getIdeCerhLancamentoEfluenteCaracterizacao(), tipoUso.getIdeTipoUsoRecursoHidrico()));
						}else if(!Util.isNullOuVazio(loc.getIdeCerhCaptacaoCaracterizacao())){
							valorTipoUso.setValorFinal(getTotalCapSuperficialOuSubterrranea(tipoUso.getIdeTipoUsoRecursoHidrico(), loc.getIdeCerhCaptacaoCaracterizacao()));
						}
					}	
					//B. Captação Subterrânea  é um grupo separado		
					else if(tipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())){
						//Calcula o consumo
						valorTipoUso.setValorFinal(getTotalCapSuperficialOuSubterrranea(tipoUso.getIdeTipoUsoRecursoHidrico(), loc.getIdeCerhCaptacaoCaracterizacao()));
					}else if (tipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.BARRAGEM.getId())){
						valorTipoUso.setValorFinal(getTotalBarragem(loc.getIdeCerhBarragemCaracterizacao()));
					}else{
						valorTipoUso.setValorFinal(getTotalIntervencao(loc.getIdeCerhIntervencaoCaracterizacao()));
					}
					valorTipoUsos.add(valorTipoUso);
				}
			}
		}
		//Gera os grupos de Cobrança
		GrupoCobranca gCapSub = new GrupoCobranca();
		GrupoCobranca goutros = new GrupoCobranca();
		for(ValorTipoUso  v : valorTipoUsos){
			if(v.getTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA.getId())){
				//B. Captação Subterrânea  é um grupo separado	
				gCapSub.getValorTipoUsos().add(v);
			}else{
				//A. Lançamento de Efluentes e Captação Superficial deverão fazer parte do mesmo grupo.
				goutros.getValorTipoUsos().add(v);
			}
		}
		List<CerhCobranca> cobrancas = new ArrayList<CerhCobranca>();
		//Montar Cobranças Cobrança
		if(!gCapSub.getValorTipoUsos().isEmpty()){
			CerhCobranca cobranca = this.gerarGrupoCobranca(opcaoPagamento, correios, cerh, anoBase, gCapSub);
			cobrancas.add(cobranca);
		}
		if(!goutros.getValorTipoUsos().isEmpty()){
			CerhCobranca cobranca = this.gerarGrupoCobranca(opcaoPagamento, correios, cerh, anoBase, goutros);
			cobrancas.add(cobranca);	
		}
		return cobrancas;
	}
	/**
	 * Metod para gerar grupo de cobrança
	 * @param opcaoPagamento
	 * @param correios
	 * @param cerh
	 * @param anoBase
	 * @param grupo
	 * @return CERH cobrança
	 * @throws Exception
	 */
	private CerhCobranca gerarGrupoCobranca(OpcaoPagamentoEnum opcaoPagamento, boolean correios, Cerh cerh, Integer anoBase, GrupoCobranca grupo) throws Exception{
		CerhCobranca cerhCobranca = new CerhCobranca();
		cerhCobranca.setCerh(cerh);
		if(opcaoPagamento.equals(OpcaoPagamentoEnum.INTEGRAL)){
			cerhCobranca.setInd_parcelado(false);
		}else{
			cerhCobranca.setInd_parcelado(true);
		}
		cerhCobranca.setNumAnoCobranca(anoBase);
		cerhCobranca.setInd_correios(correios);
		gerarDaes(cerhCobranca, grupo, opcaoPagamento);
		return cerhCobranca;
	}
	/**
	 * Metodo para gerar DAES
	 * @param cerhCobranca
	 * @param grupo
	 * @param pagamentoEnumm
	 * @throws Exception
	 */
	private void gerarDaes(CerhCobranca cerhCobranca, GrupoCobranca grupo, OpcaoPagamentoEnum pagamentoEnumm) throws Exception{
		BigDecimal valor = new BigDecimal(0);
		for(ValorTipoUso v :grupo.getValorTipoUsos()){
			valor = valor.add(v.getValorFinal());
		}
		int parcelas = 1;
		if(pagamentoEnumm.equals(OpcaoPagamentoEnum.PARECELADO)){
			parcelas = getNumParcelas(valor);
		}
		
		for (int p = 1 ; p <= parcelas; p++) {
			CerhParcelasCobranca parcelaCobranca = new CerhParcelasCobranca();
			parcelaCobranca.setNumParcela(p);
			parcelaCobranca.setVlrOriginalParcela(valor.doubleValue() / parcelas);
			parcelaCobranca.setIdeCerhCobranca(cerhCobranca);
			List<CerhDaeTipoUso> daeTipoUsos = new ArrayList<CerhDaeTipoUso>(); 
			String rpga = null;
			for(ValorTipoUso v :grupo.getValorTipoUsos()){
				CerhDaeTipoUso cerhDaeTipoUso = new CerhDaeTipoUso(v.getTipoUsoRecursoHidrico(), v.getValorFinal().divide(new BigDecimal(parcelas)), v.getCerhLocalizacaoGeografica());
				daeTipoUsos.add(cerhDaeTipoUso);
				if(rpga == null){
					rpga = v.getRpga();
				}
			}
			Dae dae = getDae(cerhCobranca, parcelaCobranca, p, parcelas, daeTipoUsos, rpga);
			parcelaCobranca.getCerhDaesCollection().add(dae);
			cerhCobranca.getCerhParcelasCobrancasCollection().add(parcelaCobranca);
		}
		cerhCobranca.setNumMaxParcelas(cerhCobranca.getCerhParcelasCobrancasCollection().size());
	}
	
	/**
	 * 
	 * @param cerhCobranca
	 * @param parcelaCobranca
	 * @param parcela
	 * @param totalParcelas
	 * @param tipoUsos
	 * @param rpga
	 * @return DAE
	 * @throws Exception
	 */
	private Dae getDae(CerhCobranca cerhCobranca, CerhParcelasCobranca parcelaCobranca, int parcela, int totalParcelas, List<CerhDaeTipoUso> tipoUsos, String rpga) throws Exception{
		Dae dae = new Dae();
		dae.setNumParcelaReferencia(parcela);
		dae.setCerhParcelasCobranca(parcelaCobranca);
		dae.setRpga(rpga);
		dae.setDtEmissao(Calendar.getInstance().getTime());
		dae.setNumAnoReferencia(cerhCobranca.getNumAnoCobranca());
		dae.setNumMesReferencia(Calendar.getInstance().get(Calendar.MONTH));
		dae.setNumTotalParcelaReferencia(totalParcelas);
		SefazCodigoReceita codigoReceita = facade.getSefazCodigoReceita(tipoUsos.get(0).getTipoUsoRecursoHidrico());
		dae.setCerhCodigoReceita(codigoReceita);
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		setDataVencimentoParcela(dae);
		for(CerhDaeTipoUso t : tipoUsos){
			t.setDae(dae);
			dae.getCerhDaetipoUsos().add(t);
		}
		//Juros de Atrazo no Pagamento
		definirMultaAtrasoParcela(dae);
		//calcular Data de vencimento e Valor da multa após mesclar DAES
		definirParcelaCorreios(dae);
		
		HistSituacaoDae historico = new HistSituacaoDae();
		historico.setDtAlteracao(Calendar.getInstance().getTime());
		historico.setIdeDae(dae);
		historico.setIdeSituacaoDae(new SituacaoDae(SituacaoDaeEnum.AGUARDANDO_IMPRESSAO.getId()));
		historico.setIdeUsuario(usuario);
		dae.getHistSituacaoDae().add(historico);
		return dae;
	}
	
	
	/**
	 * Qcap – é a vazão máxima instantânea do período * a quantidade de dias do período
	 * Caso haja variação de vazão, cada alteração marca um período diferente.
	 * @param cerhCaptacaoCaracterizacao
	 * @return Qcap
	 */
	private double getQcap(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao){
		double Qcap = 0;
		for(CerhVazaoSazonalidadeInterface i : cerhCaptacaoCaracterizacao.getCerhCaptacaoVazaoSazonalidadeCollection()){
			CerhCaptacaoVazaoSazonalidade sansonalidade = (CerhCaptacaoVazaoSazonalidade) i; 
			double vazMaxima = cerhCaptacaoCaracterizacao.getValVazaoMaximaInstantanea().doubleValue();
			//verifica isenção do periodo
			if(!isIsento(cerhCaptacaoCaracterizacao, vazMaxima)){
				int dias = sansonalidade.getValDiaMes();
				//Qcap – é a vazão máxima instantânea do período * a quantidade de dias do período
				Qcap = Qcap + (vazMaxima * dias);
			}
		}
		return Qcap;
	}
	/**
	 * Qcap * KclasseCaptacao* PPU
		Qcap – é a vazão máxima instantânea do período * a quantidade de dias do período
		Caso haja variação de vazão, cada alteração marca um período diferente.
		Kclasse – Ponderador da Classe do Corpo Hídrico;
		Cada classe de corpo hídrico, identificado através do GeoBahia, terá um ponderador.
		Separado em “KClasseCaptacao” e “KClasseConsumo” pois são utilizados parâmetros diferentes para cada etapa da equação.
		PPU – Preço Público Unitário
	 * @param Qcap
	 * @param pondClasse
	 * @param precoPubUnitario
	 * @return valorCaptação
	 */
	private double getValorCaptacao(double Qcap, CerhPondClasCorpoHidrico pondClasse, CerhPrecoPubUnitario precoPubUnitario){
		return Qcap * pondClasse.getVlrReferencia() * precoPubUnitario.getVlrReferencia();
	}
	
	/**
	 * Qcap*K0 * KclasseConsumo * PPU
	 * @param Qcap
	 * @param pondVolConsumido
	 * @param pondClasse
	 * @param precoPubUnitario
	 * @return consumo
	 */
	private double getValorCaptacaoConsumoPercentual(double Qcap, CerhPondVolConsumido pondVolConsumido, CerhPondClasCorpoHidrico pondClasse, CerhPrecoPubUnitario precoPubUnitario){
		return Qcap * pondClasse.getVlrReferencia() * pondVolConsumido.getVlrReferencia() * precoPubUnitario.getVlrReferencia();
	}
	
	
	public BigDecimal getTotalCapSuperficialOuSubterrranea(TipoUsoRecursoHidrico tipoUsoRecursoHidrico, CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao) throws Exception{
		double QCap = getQcap(cerhCaptacaoCaracterizacao);
		CerhPondClasCorpoHidrico kClasse = facade.getCerhClasseCorpoHidrico(tipoUsoRecursoHidrico);
		CerhPrecoPubUnitario PPU = facade.getPPU(tipoUsoRecursoHidrico);
		CerhPondVolConsumido k0 = facade.getK0();
		CerhPondGestao kg = facade.getKg();
		//Qcap * KclasseCaptacao* PPU
		double consumo = getValorCaptacao(QCap, kClasse, PPU);
		//Qcap*K0 * KclasseConsumo * PPU
		double consumoPercentual = getValorCaptacaoConsumoPercentual(QCap, k0, kClasse, PPU);
		//(Qcap * KclasseCaptacao* PPU) + (Qcap*K0 * KclasseConsumo * PPU)
		return new BigDecimal((consumo + consumoPercentual) / kg.getVlrReferencia());
	}
	
	/**
	 * Qlanç – Vazão do efluente máxima Instantânea do período * a quantidade de dias do período
		Caso haja variação de vazão, cada alteração marca um período diferente
	 * @param caracterizacao
	 * @return
	 * @throws Exception 
	 */
	public BigDecimal getQLanc(CerhLancamentoEfluenteCaracterizacao caracterizacao, TipoUsoRecursoHidrico tipoUsoRecursoHidrico) throws Exception{
		BigDecimal QLanc = new BigDecimal(0);
		for(CerhLancamentoEfluenteSazonalidade s :caracterizacao.getCerhLancamentoEfluenteSazonalidadeCollection()){
			//verificar isenção do período
			BigDecimal periodo = s.getValVazao().multiply(new BigDecimal(s.getValDiaMes()));
			CerhPondClasCorpoHidrico KclasseEfluente = facade.getCerhClasseCorpoHidrico(tipoUsoRecursoHidrico);
			if(!isIsento(KclasseEfluente.getClasseCorpoHidrico(), periodo.doubleValue())){
				QLanc = QLanc.add(periodo);
			}
		}
		return QLanc;
	}
	/**
	 * Qlanç * [(1-K1)+(1-K2)]*PPU*KclasseEfluente  
	 * @return
	 * @throws Exception 
	 */
	public BigDecimal getTotalLancamentoEfluentes(CerhLancamentoEfluenteCaracterizacao caracterizacao, TipoUsoRecursoHidrico tipoUsoRecursoHidrico) throws Exception{
		CerhPondClasCorpoHidrico KclasseEfluente = facade.getCerhClasseCorpoHidrico(tipoUsoRecursoHidrico);
		BigDecimal QLanc = getQLanc(caracterizacao, tipoUsoRecursoHidrico);
		double k1 = caracterizacao.getValDboEficienciaTratamento().doubleValue() / 100;
		double k2 = caracterizacao.getValColiformesEficienciaTratamento().doubleValue() / 100;
		CerhPrecoPubUnitario PPU = facade.getPPU(tipoUsoRecursoHidrico); 
		CerhPondGestao kg = facade.getKg();
		//Qlanç * [(1-K1)+(1-K2)]*PPU*KclasseEfluente  
		return new BigDecimal((QLanc.doubleValue() * ((1-k1) + (1-k2)) + PPU.getVlrReferencia() * KclasseEfluente.getVlrReferencia()) / kg.getVlrReferencia());
	}
	
	
	/**
	 * EH*TAR*P
	 * @return novo big decimal
	 * @throws Exception 
	 */
	public BigDecimal getTotalIntervencao(CerhIntervencaoCaracterizacao caracterizacao){
		double EH = caracterizacao.getValProducaoAnualEfetivamenteVerificada() != null ? caracterizacao.getValProducaoAnualEfetivamenteVerificada().doubleValue() : 0d;
		CerhPondGestao kg = facade.getKg();
		//EH*TAR*P
		return new BigDecimal((EH*TAR*P) / kg.getVlrReferencia());
	}
	/**
	 * EH*TAR*P
	 * @return
	 * @throws Exception 
	 */
	public BigDecimal getTotalBarragem(CerhBarragemCaracterizacao caracterizacao) throws Exception{
		double EH = caracterizacao.getValVazaoRegularizada().doubleValue();
		CerhPondGestao kg = facade.getKg();
		//EH*TAR*P
		return new BigDecimal((EH*TAR*P) / kg.getVlrReferencia());
	}
	
	/**
	 * Metodo para definir multa por atraso de parcela
	 * @param dae
	 */
	public void definirMultaAtrasoParcela(Dae dae){
		//Data da Emissão
		Calendar dataGeracaoDae = (Calendar)Calendar.getInstance().clone();
		dataGeracaoDae.setTime(dae.getDtEmissao());
		//Data de vencimento
		Calendar dataVencimentoFixaParcela = (Calendar)Calendar.getInstance().clone();
		dataVencimentoFixaParcela.setTime(getVencimentoFixo(dae.getNumParcelaReferencia(), dae.getNumAnoReferencia()));
		//DAE Emitido após a data de vencimento Fixa
		if(dataGeracaoDae.after(dataVencimentoFixaParcela)){
			DateTime ini = new DateTime(dataVencimentoFixaParcela.getTime());
			DateTime fim = new DateTime(dataGeracaoDae.getTime());
			//meses de Atrazo da parcela
			int mesesAtraso = Months.monthsBetween(ini, fim).getMonths();
			for(CerhDaeTipoUso t : dae.getCerhDaetipoUsos()){
				double multa = 0;
				double multa_atraso = t.getValorOriginal().doubleValue() * TAXA_ATRASO; // taxa fixa de 2% pelo atraso
				double juros_atraso = 0;
				for(int x = 0; x < mesesAtraso; x++){
					// FIX
					//Obter Selic do mês aterior, 
					//caso não exista selic do mês anterior Cadastrada deve Cobrar 1% = TAXA_SELIC_DEFALT
					juros_atraso =  juros_atraso + (t.getValorOriginal().doubleValue() * TAXA_SELIC_DEFAULT); 				
				}
				multa = multa_atraso + juros_atraso;
				t.setValorAcrescimo(new BigDecimal(multa));
				
			}
		}
	}
	
	
	/**
	 * Retorna a Data que deve ser paga a Parcela
	 * @param parcela
	 * @return data
	 */
	private Date getVencimentoFixo(int parcela, int anoBase){
		Calendar c = (Calendar) Calendar.getInstance().clone();
		c.set(Calendar.MONTH, parcela + 1); //1° Parcela em março, 2º Abril....
		c.set(Calendar.DAY_OF_MONTH, 10);
		c.set(Calendar.YEAR, anoBase + 1);
		return c.getTime();
	}
	
	/**
	 * Define a data de vencimento da Parcela
	 * @param parcela
	 */
	public void setDataVencimentoParcela(Dae dae){
		Date data = null;
		Calendar vencimentoFixoParcela = (Calendar) Calendar.getInstance().clone();
		vencimentoFixoParcela.setTime(getVencimentoFixo(dae.getNumParcelaReferencia(), dae.getNumAnoReferencia()));
		Calendar dataEmissao = (Calendar) Calendar.getInstance().clone();
		dataEmissao.setTime(dae.getDtEmissao());
		if(dataEmissao.after(vencimentoFixoParcela)){
			Calendar c = (Calendar) Calendar.getInstance().clone();
			//gera a Cobrança Para o dia Seguinte
			c.add(Calendar.DAY_OF_MONTH, 1);
			data = c.getTime();
			dae.setAtraso(true);
		}else{
			data = getVencimentoFixo(dae.getNumParcelaReferencia(), dae.getNumAnoReferencia());
		}
		dae.setDtVencimento(getDiaUtil(data));
	}
	/**
	 * Metodo para definir parcela correio
	 * @param dae
	 */
	public void definirParcelaCorreios(Dae dae){
		if(dae.getCerhParcelasCobranca().getIdeCerhCobranca().isInd_correios()){
			//2017-10-02T15:49:03.783-03:00
			DateTime emissaoDt = new DateTime(Calendar.getInstance().getTime());
			//2017-03-10T15:49:48.016-03:00
			DateTime vencimentoDt = new DateTime(this.getVencimentoFixo(dae.getNumParcelaReferencia(), dae.getNumAnoReferencia()));
			int diasParaVencer = Days.daysBetween(emissaoDt, vencimentoDt).getDays();
			//verifica se existe tempo habil para pagamento 
			if(diasParaVencer < PRAZO_CORREIOS){
				Calendar v = (Calendar) Calendar.getInstance().clone();
				v.add(Calendar.DAY_OF_MONTH, PRAZO_CORREIOS);
				dae.setDtVencimento(getDiaUtil(v.getTime()));
				//calcula Multa ao enviar pelos Correios e não existe tempo habil para receber e pagar o DAE
				for(CerhDaeTipoUso t : dae.getCerhDaetipoUsos()){
					double multaCorreios = (t.getValorOriginal().doubleValue() * TAXA_ATRASO) + (t.getValorOriginal().doubleValue() * TAXA_SELIC_DEFAULT);
					t.setValorAcrescimo(t.getValorAcrescimo().add(new BigDecimal(multaCorreios)));
				}
			}
		}
	}
	
	
	public int getNumParcelas(BigDecimal valor){
		if(valor.doubleValue() <= 0){
			return 1;
		}
		int numParcela = valor.divide(new BigDecimal(50)).intValue();//50 valor minimo de Parcelas
		if(numParcela > 10){
			numParcela = 10;
		}
		return numParcela;
	}
	
	public BigDecimal getValorParcela(BigDecimal total, int parcelas){
		return total.divide(new BigDecimal(parcelas));
	}	
	
	/**
	 * Verificar se é dia util para pagamento, caso não for dia util ou for feriado deve passar para o primeiro dia util, 
	 * @param date
	 * @return date
	 */
	private Date getDiaUtil(Date date){
		Calendar data = (Calendar) Calendar.getInstance().clone();
		data.setTime(date);
		if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isFeriado(data)){
			data.add(Calendar.DAY_OF_MONTH, 1);
			return getDiaUtil(data.getTime());
		}
		return date;
	}
	

	
	/**
	 * Verificar se cai em um feriado
	 * @param date
	 * @return boolean
	 */
	private boolean isFeriado(Calendar date){
		return false;
	}
	
	
	/**
	 * Para usuários Declarados (que não possuam processo vinculado ou que possuam processo com situação “Em análise” ou “Indeferido” 
	 * Para pontos onde a finalidade seja exclusivamente “Abastecimento Humano”, o usuário será isento de cobrança quando o somatório das vazões (vazão máxima instantânea) for menor ou igual à 129,6m³/dia 
	 * @param cerh
	 * @return
	 */
	public boolean isIsento(CerhCaptacaoCaracterizacao caracterizacao, double vazaoMaximaInstantanea){
		return isIsentoAbastecimentoHumano(caracterizacao, vazaoMaximaInstantanea)
				|| isIsentoProcesso(caracterizacao);
	}
	/**
	 * Para usuários Declarados (que não possuam processo vinculado ou que possuam processo com situação “Em análise” ou “Indeferido” 
	 * @param caracterizacao
	 * @param vazaoMaximaInstantanea
	 * @return
	 */
	public boolean isIsentoProcesso(CerhCaptacaoCaracterizacao caracterizacao){
		return false;
	}
	
	/**
	 * Para pontos onde a finalidade seja exclusivamente “Abastecimento Humano”, o usuário será isento de cobrança quando o somatório das vazões (vazão máxima instantânea) for menor ou igual à 129,6m³/dia
	 * @param caracterizacao
	 * @param vazaoMaximaInstantanea
	 * @return isento
	 */
	public boolean isIsentoAbastecimentoHumano(CerhCaptacaoCaracterizacao caracterizacao, double vazaoMaximaInstantanea){
		if(caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().isEmpty()){
			if(caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().size() == 1){
				CerhBarragemCaracterizacaoFinalidade finalidade = (CerhBarragemCaracterizacaoFinalidade) caracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection().toArray()[0];
				if(finalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getId())
						&& vazaoMaximaInstantanea <= VAZAO_ISENCAO_ABAST_HUMAN){
					return true;
				}
			}else{
				if(vazaoMaximaInstantanea <= VAZAO_ISENCAO){
					return true;
				}
			}			
		}
		return false;
	}
	
	
	public boolean isIsento(CerhClasseCorpoHidrico cerhClasseCorpoHidrico, double dbo){
		return isIsentoConcentracaoDBO(cerhClasseCorpoHidrico, dbo);
	}
	
	public boolean isIsentoConcentracaoDBO(CerhClasseCorpoHidrico cerhClasseCorpoHidrico, double dbo){
		if(cerhClasseCorpoHidrico.getId().equals(ClasseCorpoHidricoEnum.CLASSE_1.getId())){
			return dbo <= ClasseCorpoHidricoEnum.CLASSE_1.getLimitClasse();
		}else
		if(cerhClasseCorpoHidrico.getId().equals(ClasseCorpoHidricoEnum.CLASSE_2.getId())){
			return dbo <= ClasseCorpoHidricoEnum.CLASSE_2.getLimitClasse();
		}else
		if(cerhClasseCorpoHidrico.getId().equals(ClasseCorpoHidricoEnum.CLASSE_3.getId())){
			return dbo <= ClasseCorpoHidricoEnum.CLASSE_3.getLimitClasse();
		}else
		if(cerhClasseCorpoHidrico.getId().equals(ClasseCorpoHidricoEnum.CLASSE_3.getId())){
			return dbo <= ClasseCorpoHidricoEnum.CLASSE_3.getLimitClasse();
		}else
		if(cerhClasseCorpoHidrico.getId().equals(ClasseCorpoHidricoEnum.CLASSE_4.getId())){
			return dbo <= ClasseCorpoHidricoEnum.CLASSE_4.getLimitClasse();
		}
		
		return false;
	}
	
	private class ValorTipoUso{
		private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;
		
		private BigDecimal valorFinal;

		private String rpga;

		private CerhLocalizacaoGeografica cerhLocalizacaoGeografica;

		public ValorTipoUso(TipoUsoRecursoHidrico tipoUsoRecursoHidrico,
				BigDecimal valorFinal,
				String rpga, CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
			this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
			this.valorFinal = valorFinal;
			this.rpga = rpga;
			this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
		}

		/**
		 * @return the tipoUsoRecursoHidrico
		 */
		public TipoUsoRecursoHidrico getTipoUsoRecursoHidrico() {
			return tipoUsoRecursoHidrico;
		}

		/**
		 * @return the valorFInal
		 */
		public BigDecimal getValorFinal() {
			return valorFinal;
		}

		/**
		 * @param valorFInal the valorFInal to set
		 */
		public void setValorFinal(BigDecimal valorFinal) {
			this.valorFinal = valorFinal;
		}

		/**
		 * @return the cerhLocalizacaoGeografica
		 */
		public CerhLocalizacaoGeografica getCerhLocalizacaoGeografica() {
			return cerhLocalizacaoGeografica;
		}

		/**
		 * @return the rpga
		 */
		public String getRpga() {
			return rpga;
		}

	}
	
	private class GrupoCobranca{
		
		private List<ValorTipoUso> valorTipoUsos;

		public GrupoCobranca() {
			valorTipoUsos = new ArrayList<CobrancaCerhUtil.ValorTipoUso>();
		}

		/**
		 * @return the valorTipoUsos
		 */
		public List<ValorTipoUso> getValorTipoUsos() {
			return valorTipoUsos;
		}

	}
	
	/**
	 * verificar atributos que devem ser enviados PARA o webservice
	 * @param dae
	 * @return DocumentoArrecadacao
	 * @throws Exception
	 */
	private DocumentoArrecadacao getDocumentoArrecadacao(Dae dae) {
		Endereco endereco = facade.buscarEndereco(dae.getCerhParcelasCobranca().getIdeCerhCobranca().getCerh().getIdeEmpreendimento());
		DocumentoArrecadacao documentoArrecadacao = new DocumentoArrecadacao();
		documentoArrecadacao.setSeq_documento_arrecadacao(SefazUtil.SEQ_DOC_ARREACADACAO);//2
		documentoArrecadacao.setCod_receita(dae.getCerhCodigoReceita().getNumCodigoCeceita());// 2214 
		documentoArrecadacao.setCod_identificacao_emitente(1);// 0 PJ e 1 PF
	//	documento_arrecadacao.setNum_identificacao_emitente(new NumIdentificacaoEmitente(cerhDae.getUsuario().getPessoaFisica().getNumCpf()));// new NumIdentificacaoEmitente("16512975000139")
		documentoArrecadacao.setCod_tipo_documento_origem(2);// 2
//		documento_arrecadacao.setNum_documento_origem((cerhDae.getNumDocumentoOrigem() != null ? cerhDae.getNumDocumentoOrigem() : "1")); //"10000222015"
		documentoArrecadacao.setDtc_vencimento(FormaterUtil.formatarData(dae.getDtVencimento(), "yyyy-MM-dd"));//"2017-10-01"
		documentoArrecadacao.setDtc_max_pagamento(FormaterUtil.formatarData(dae.getDtVencimento(), "yyyy-MM-dd"));//"2017-10-01" 
		documentoArrecadacao.setReferencia(new Referencia(1, dae.getNumMesReferencia(), dae.getNumAnoReferencia()));//1,10,2016 
		documentoArrecadacao.setVal_principal(dae.getValorDae().setScale(2, RoundingMode.CEILING).doubleValue());
		documentoArrecadacao.setVal_acrescimo(dae.getValorAcrescimo().setScale(2, RoundingMode.CEILING).doubleValue());
		//documento_arrecadacao.setNom_razao_social(cerhDae.getUsuario().getPessoaFisica().getNomPessoa());// ver PJ "PJ__Teste_Juan_Final"
		documentoArrecadacao.setDes_tipo_logradouro(endereco.getIdeLogradouro().getIdeTipoLogradouro().getIdeTipoLogradouro().toString());// "-"
		documentoArrecadacao.setDes_endereco(SefazUtil.removeCaracterInvalido(endereco.getEnderecoBasicoFormatado().toUpperCase()));//
		documentoArrecadacao.setDes_bairro(endereco.getIdeLogradouro().getIdeBairro().getNomBairro());//
		documentoArrecadacao.setNum_cep(endereco.getIdeLogradouro().getNumCepString());//"41600115"
		documentoArrecadacao.setCod_municipio_ibge(endereco.getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio().intValue() + "");//2927408
		documentoArrecadacao.setTipo_dae(SefazUtil.TIPO_DAE);//2
		documentoArrecadacao.setCod_unidade_orcamentaria_origem(10301);// 10301
		documentoArrecadacao.setCod_unidade_gestora_origem(1);//
		documentoArrecadacao.setCod_unidade_gestora_destino(1);//
		documentoArrecadacao.setCod_unidade_orcamentaria_destino(10301);// 10301
		documentoArrecadacao.setCod_tipo_retorno_documento(1);//
		documentoArrecadacao.setSts_retornar_barra(SefazUtil.RET_COD_BARRA);//1
		return documentoArrecadacao;
	}
	
	public void gerarDaesSefaz(CerhCobranca cobranca) throws Exception{
		for(CerhParcelasCobranca parcela : cobranca.getCerhParcelasCobrancasCollection()){
			for(Dae dae : parcela.getCerhDaesCollection()){
				gerarDaeSefaz(dae);
			}
		}
	}
	/**
	 * Metodo para gerar dae sefaz
	 * @param dae
	 * @throws Exception
	 */
	private void gerarDaeSefaz(Dae dae) throws Exception{
		RetEmissaoDocumentoArrecadacaoDTO retorno = SefazUtil.emitirDae(getDocumentoArrecadacao(dae));
		if(retorno.getEmissao_documento_arrecadacao() != null){
			dae.setDscNossoNumero(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_dae_emitido().toString());
			dae.setCodDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao());
			dae.setNumDocumentoOrigem(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getSeq_documento_arrecadacao().toString());
			dae.setCodReferencia(0);// onde obter ?
			dae.setUrlDae(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_endereco_doc_arrec());
			dae.setCodbarras(retorno.getEmissao_documento_arrecadacao().getDocumento_arrecadacao().getDes_codigo_barra());
		}else{
			throw new Exception(retorno.getxMotivo());
		}
	}
	
	/**
	 * 
	 * @param dae
	 * @return
	 * @throws Exception
	 */
	public Dae montar2Via(Dae dae) throws Exception{
		Dae dae2 = (Dae) Util.copiarEntity(dae);
		dae2.setDtEmissao(Calendar.getInstance().getTime());
		dae2.setCerhDaetipoUsos(new ArrayList<CerhDaeTipoUso>());
		for(CerhDaeTipoUso tU : dae.getCerhDaetipoUsos()){
			CerhDaeTipoUso cerhDaeTipoUso = (CerhDaeTipoUso) Util.copiarEntity(tU);
			cerhDaeTipoUso.setIdeCerhDaeTipoUso(null);
			dae2.getCerhDaetipoUsos().add(cerhDaeTipoUso);
		}
		definirMultaAtrasoParcela(dae2);
		dae2.setDaePai(dae);
		return dae2;
	}
	
	
}
