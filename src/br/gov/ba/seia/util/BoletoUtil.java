package br.gov.ba.seia.util;

import java.io.File;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;

import br.gov.ba.seia.dao.CampoLivreBopepoImpl;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.DadoBancario;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;

public class BoletoUtil {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	public static BoletoViewer gerarBoleto(BoletoPagamentoRequerimento boletoPagamento, Pessoa requerente, DadoBancario dadoBancario) throws Exception {

		Sacado sacado = gerarSacado(requerente);
		vincularEnderecoSacado(requerente, sacado);

		Cedente cedente = gerarCedente(dadoBancario);
		SacadorAvalista sacadorAvalista = gerarSacadorAvalista();
		
		ContaBancaria contaBancaria = gerarContaBancaria(dadoBancario);
		
		Titulo titulo = gerarTitulo(boletoPagamento, sacado, cedente, sacadorAvalista, contaBancaria);
		
		String carteiraI = StringUtils.substring(dadoBancario.getNumCarteira(), 0, 2);
		CampoLivreBopepoImpl campoLivre = new CampoLivreBopepoImpl(dadoBancario.getNumConta(), titulo.getNossoNumero(), carteiraI);
		
		Boleto boleto = gerarBoleto(boletoPagamento, dadoBancario, titulo, campoLivre);
		
		File boletoPDF = new File(new RelatorioUtil().retornaCaminhoRelatorio()+ "BoletoTemplateSemSacadorAvalista.pdf");
		
		return new BoletoViewer(boleto, boletoPDF);
	}

	private static Boleto gerarBoleto(BoletoPagamentoRequerimento boletoPagamento, DadoBancario dadoBancario,
			Titulo titulo, CampoLivreBopepoImpl campoLivre) {
		Boleto boleto = new Boleto(titulo, campoLivre);
		boleto.setInstrucao1(dadoBancario.getDscInstrucao1());
		boleto.setInstrucao2(dadoBancario.getDscInstrucao2());
		boleto.setInstrucao3(dadoBancario.getDscInstrucao3());
		boleto.setInstrucao4(dadoBancario.getDscInstrucao4());
		boleto.setInstrucao5(dadoBancario.getDscInstrucao5());
		boleto.setInstrucao6(dadoBancario.getDscInstrucao6());
		boleto.setLocalPagamento(dadoBancario.getLocalPagamento());
		boleto.setDataDeProcessamento(boletoPagamento.getDtcEmissao());
		boleto.addTextosExtras("txtFcCarteira", dadoBancario.getNumCarteira().replaceAll("0", ""));
		return boleto;
	}

	private static Sacado gerarSacado(Pessoa requerente) {
		Sacado sacado = null;

		if (requerente.isPF()) {
			PessoaFisica pf = requerente.getPessoaFisica();
			String cpf = NumeroDocumentoFormatterUtil.formatarCpf(pf.getNumCpf());
			sacado = new Sacado(pf.getNomPessoa(), cpf);
		} else {
			PessoaJuridica pj = requerente.getPessoaJuridica();
			sacado = new Sacado(pj.getNomRazaoSocial(), NumeroDocumentoFormatterUtil.formatarCnpj(pj.getNumCnpj()));
		}
		return sacado;
	}

	private static Cedente gerarCedente(DadoBancario dadoBancario) {
		Cedente cedente = new Cedente(dadoBancario.getNomCedente(), dadoBancario.getNumCnpj());
		return cedente;
	}

	private static SacadorAvalista gerarSacadorAvalista() {
		SacadorAvalista sacadorAvalista = new SacadorAvalista("");
		Collection<org.jrimum.domkee.comum.pessoa.endereco.Endereco> enderecos = new ArrayList<org.jrimum.domkee.comum.pessoa.endereco.Endereco>();
		enderecos.add(new org.jrimum.domkee.comum.pessoa.endereco.Endereco());
		sacadorAvalista.setEnderecos(enderecos);
		return sacadorAvalista;
	}

	private static ContaBancaria gerarContaBancaria(DadoBancario dadoBancario) {
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_DO_BRASIL.create());
		contaBancaria.setNumeroDaConta(new NumeroDaConta(dadoBancario.getNumConta(), dadoBancario.getNumDigitoConta()));
		
		Integer carteira = Integer.valueOf(StringUtils.substring(dadoBancario.getNumCarteira(), 0, 2));
		contaBancaria.setCarteira(new Carteira(carteira));
		contaBancaria.setAgencia(new Agencia(dadoBancario.getNumAgencia(), dadoBancario.getNumDigitoAgencia()));
		return contaBancaria;
	}

	private static Titulo gerarTitulo(BoletoPagamentoRequerimento boletoPagamento, Sacado sacado, Cedente cedente,
			SacadorAvalista sacadorAvalista, ContaBancaria contaBancaria) {
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
		titulo.setNumeroDoDocumento(Util.zeroLTrim(boletoPagamento.getNumBoleto().substring(boletoPagamento.getNumBoleto().length() - 10)));
		titulo.setNossoNumero(boletoPagamento.getNumBoleto());
		titulo.setDataDoDocumento(boletoPagamento.getDtcEmissao());
		titulo.setDataDoVencimento(boletoPagamento.getDtcVencimento());
		titulo.setValor(boletoPagamento.getValTotalBoleto().setScale(2, RoundingMode.HALF_EVEN));
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		titulo.setAceite(Titulo.Aceite.N);
		//titulo.setEnumMoeda(TipoDeMoeda.REAL);
		return titulo;
	}

	private static void vincularEnderecoSacado(Pessoa requerente, Sacado sacado) {
		if (!Util.isNull(requerente.getEndereco())) {
			Endereco endereco = requerente.getEndereco();
			org.jrimum.domkee.comum.pessoa.endereco.Endereco enderecoSac = new org.jrimum.domkee.comum.pessoa.endereco.Endereco();
			Logradouro logradouro = endereco.getIdeLogradouro();
			if(!Util.isNull(logradouro)) {
				Municipio municipio = logradouro.getMunicipio();
				Estado estado = municipio.getIdeEstado();
				enderecoSac.setUF(UnidadeFederativa.valueOf(estado.getDesSigla()));
				enderecoSac.setLocalidade(municipio.getNomMunicipio());
				enderecoSac.setCep(new CEP(logradouro.getNumCepFormatado()));
				enderecoSac.setBairro(logradouro.getIdeBairro().getNomBairro());
				enderecoSac.setLogradouro(logradouro.getIdeTipoLogradouro().getNomTipoLogradouro() + " "+ logradouro.getNomLogradouro());
				enderecoSac.setNumero(endereco.getNumEndereco());
			}
			sacado.addEndereco(enderecoSac);
		}
	}

}
