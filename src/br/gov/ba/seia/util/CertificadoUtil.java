package br.gov.ba.seia.util;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;

public class CertificadoUtil {
	
	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	
	@EJB
	private static CertificadoService certificadoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	public static Certificado gerarCertificadoWithoutNumero(Requerimento requerimento,TipoCertificadoEnum tipoCertificado) {
		Certificado certificado = new Certificado();

		Orgao orgao = requerimento.getIdeOrgao();
		certificado.setIdeOrgao(orgao);

		certificado.setRequerimento(requerimento);
		
		certificado.setTipoCertificado(new TipoCertificado(tipoCertificado.getId()));

		certificado.setDtcEmissaoCertificado(new Date());

		return certificado;
	}
	
	public Certificado gerarCertificado(AtoAmbiental atoAmbiental, Requerimento requerimento)  {
		
		Certificado certificado = new Certificado();			
		certificado.setIdeAtoAmbiental(atoAmbiental);
		
		Orgao orgao =  new Orgao(1,1);
		certificado.setIdeOrgao(orgao);
		
		certificado.setRequerimento(requerimento);
		
		Date dtcEmissao = gerarDtcEmissao(requerimento);			
		certificado.setDtcEmissaoCertificado(dtcEmissao);
		
		return certificado;
	}
	
	private Date gerarDtcEmissao(Requerimento requerimento) {
		try {
			ControleTramitacao ctStatusFluxoProcessoConcluido = controleTramitacaoDAOImpl.buscarStatusFluxoProcessoConcluido(requerimento.getIdeRequerimento());
			
			if(Util.isNull(ctStatusFluxoProcessoConcluido)) {
				throw new Exception("Não foi possível gerar a data de emissão do certificado.");
			}
			
			Date dtcEmissao = ctStatusFluxoProcessoConcluido.getDtcTramitacao();
			return dtcEmissao;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public static Certificado gerarCertificadoByTipo(Requerimento requerimento,TipoCertificadoEnum tipoCertificado) {
		Certificado certificado = new Certificado();

		certificado.setTipoCertificado(new TipoCertificado(tipoCertificado.getId()));

		Orgao orgao = new Orgao(1,1);
		certificado.setIdeOrgao(orgao);

		certificado.setRequerimento(requerimento);

		certificado.setDtcEmissaoCertificado(new Date());

		return certificado;
	}

	public static String obterProximoNumeroCertificado(Certificado certificado, String ultimoNumero, Boolean isDeclaracao) {

		StringBuilder numeroCertificado = new StringBuilder(40);
		adicionarAno(numeroCertificado);

		String codigoOrgao = certificado.getIdeOrgao().getCodOrgao().toString();
		adicionarCodigoOrgao(codigoOrgao, numeroCertificado);

		//#9330

		String sequencia = StringUtils.EMPTY;
		try {
			if(isDeclaracao){
				sequencia = certificadoService.obterProximaSequencePorTipoCertificado(certificado.getTipoCertificado(), true);			
			}else{
				sequencia = certificadoService.obterProximaSequencePorTipoCertificado(certificado.getTipoCertificado(), false);
			}
			if(Util.isNullOuVazio(sequencia) || Integer.valueOf(sequencia) == 0){
				sequencia = gerarProximaSequenciaValida(ultimoNumero);
			}
		} catch (Exception e) {
			sequencia = gerarProximaSequenciaValida(ultimoNumero);
			Log4jUtil.log(CertificadoUtil.class.getName(),Level.ERROR, e);
		}
		adicionarSequencia(numeroCertificado, sequencia);

		if(!Util.isNullOuVazio(certificado.getIdeAtoAmbiental())) {
			String siglaAtoAmbiental = certificado.getIdeAtoAmbiental().getSglAtoAmbiental();
			adicionarSiglaAto(numeroCertificado, siglaAtoAmbiental);
		} 
		else {
			TipoCertificadoEnum tipoCertificado = TipoCertificadoEnum.getEnum(certificado.getTipoCertificado().getIdeTipoCertificado());
			adicionarSiglaCefir(numeroCertificado, tipoCertificado.getSigla());
		}
		
		return numeroCertificado.toString();
	}

	private static void adicionarAno(StringBuilder numeroCertificado) {
		Integer anoCorrente = Calendar.getInstance().get(Calendar.YEAR);

		numeroCertificado.append(anoCorrente);
		numeroCertificado.append('.');
	}

	private static void adicionarCodigoOrgao(String codigoOrgao, StringBuilder numeroCertificado) {
		codigoOrgao = Util.lpad(codigoOrgao, '0', 3);
		numeroCertificado.append(codigoOrgao);
		numeroCertificado.append('.');
	}

	private static String gerarProximaSequenciaValida(String ultimoNumero) {
		String numeroValido = null;
		if (!Util.isNullOuVazio(ultimoNumero)) {
			String numero = ultimoNumero;
			if (numero.length() >= 16) {
				numero = extraiSequencia(numero);
				int sequencia = Integer.parseInt(numero) + 1;
				numeroValido = "" + sequencia;
			}

		}
		if (Util.isNullOuVazio(numeroValido)) {
			numeroValido = "1";
		}
		return numeroValido;
	}

	private static String extraiSequencia(String numero) {
		int posicaoPonto = numero.lastIndexOf(".") + 1;
		int posicaoBarra = numero.indexOf("/");
		numero = numero.substring(posicaoPonto, posicaoBarra);
		return numero;
	}

	private static void adicionarSequencia(StringBuilder numeroCertificado, String sequencia) {
		sequencia = Util.lpad(sequencia, '0', 6);
		numeroCertificado.append(sequencia);
	}

	private static void adicionarSiglaAto(StringBuilder numeroCertificado, String siglaAtoAmbiental) {
		numeroCertificado.append("/");
		numeroCertificado.append(siglaAtoAmbiental);
	}

	private static void adicionarSiglaCefir(StringBuilder numeroCertificado, String sigla) {
		numeroCertificado.append("/");
		numeroCertificado.append(sigla);
	}

	public static String obterProximoNumeroCertificadoInexigibilidade(Certificado certificado, String ultimoNumero) {

		StringBuilder numeroCertificado = new StringBuilder(40);

		adicionarAno(numeroCertificado);

		String codigoOrgao = certificado.getIdeOrgao().getCodOrgao().toString();
		adicionarCodigoOrgao(codigoOrgao, numeroCertificado);

		String sequencia = gerarProximaSequenciaValida(ultimoNumero);
		adicionarSequencia(numeroCertificado, sequencia);

		numeroCertificado.append("/INEMA/INEXIG"); 

		return numeroCertificado.toString();
	}
	
	private Date gerarDtcEmissaoCertificado(Requerimento requerimento) {
		try {
			TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento());
			
			if(Util.isNull(tramitacaoRequerimento)) {
				throw new Exception("Não foi possível gerar a data de emissão do certificado.");
			}
			
			if(!Util.isNull(tramitacaoRequerimento) && 
					Util.isNullOuVazio(tramitacaoRequerimento.getDtcMovimentacao())) {
				throw new Exception("Não foi possível gerar a data de emissão do certificado.");
			}
			
			Date dtcEmissao = tramitacaoRequerimento.getDtcMovimentacao();
			
			return dtcEmissao;
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Certificado gerarCertificadoInexigibilidade(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		
		Certificado certificado = new Certificado();			
		certificado.setIdeAtoAmbiental(atoAmbiental);
		
		Orgao orgao =  new Orgao(1,1);
		certificado.setIdeOrgao(orgao);
		certificado.setRequerimento(requerimento);
		
		Date dtcEmissao = gerarDtcEmissaoCertificado(requerimento);
		
		certificado.setDtcEmissaoCertificado(dtcEmissao);
		
		return certificado;
	}
	
	public DeclaracaoParcialDae gerarDeclaracaoParcialDae(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		
		DeclaracaoParcialDae declaracaoParcialDae = new DeclaracaoParcialDae();			
		declaracaoParcialDae.setIdeAtoAmbiental(atoAmbiental);

		declaracaoParcialDae.setTipoCertificado(new TipoCertificado(TipoCertificadoEnum.CRF.getId()));
		
		Orgao orgao =  new Orgao(1,1);
		declaracaoParcialDae.setIdeOrgao(orgao);
		declaracaoParcialDae.setRequerimento(requerimento);
		
		Date dtcEmissao = gerarDtcEmissaoCertificado(requerimento);
		
		declaracaoParcialDae.setDtcEmissaoDeclaracaoParcialDae(dtcEmissao);
		
		return declaracaoParcialDae;
	}

	public static String obterProximoNumeroCertificado(DeclaracaoParcialDae certificado, String ultimoNumero, Boolean isDeclaracao) {

		StringBuilder numeroCertificado = new StringBuilder(40);
		adicionarAno(numeroCertificado);

		String codigoOrgao = certificado.getIdeOrgao().getCodOrgao().toString();
		adicionarCodigoOrgao(codigoOrgao, numeroCertificado);

		//#9330

		String sequencia = StringUtils.EMPTY;
		try {
			if(isDeclaracao){
				sequencia = certificadoService.obterProximaSequencePorTipoCertificado(certificado.getTipoCertificado(), true);
			}else{
				sequencia = certificadoService.obterProximaSequencePorTipoCertificado(certificado.getTipoCertificado(), false);
			}
			if(Util.isNullOuVazio(sequencia) || Integer.valueOf(sequencia) == 0){
				sequencia = gerarProximaSequenciaValida(ultimoNumero);
			}
		} catch (Exception e) {
			sequencia = gerarProximaSequenciaValida(ultimoNumero);
			Log4jUtil.log(CertificadoUtil.class.getName(),Level.ERROR, e);
		}
		adicionarSequencia(numeroCertificado, sequencia);

		if(!Util.isNullOuVazio(certificado.getIdeAtoAmbiental())) {
			String siglaAtoAmbiental = certificado.getIdeAtoAmbiental().getSglAtoAmbiental();
			adicionarSiglaAto(numeroCertificado, siglaAtoAmbiental);
		} 
		else {
			TipoCertificadoEnum tipoCertificado = TipoCertificadoEnum.getEnum(certificado.getTipoCertificado().getIdeTipoCertificado());
			adicionarSiglaCefir(numeroCertificado, tipoCertificado.getSigla());
		}
		
		return numeroCertificado.toString();
	}
}
