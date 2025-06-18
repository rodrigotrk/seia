package br.gov.ba.seia.util;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Modalidade;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigito;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;
import br.gov.ba.seia.entity.*;
import br.gov.ba.seia.util.boleto.CustomBancoDoBrasil;
import br.gov.ba.seia.util.boleto.CustomGeradorDeDigito;

public class BoletoUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static GeradorDeBoleto geradorDeBoleto(BoletoPagamentoRequerimento boletoPagamento, Pessoa requerente, DadoBancario dadoBancario) throws Exception {
        Banco banco = new CustomBancoDoBrasil();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(boletoPagamento.getDtcEmissao());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(boletoPagamento.getDtcVencimento());

        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(boletoPagamento.getDtcEmissao());

        Datas datas = Datas.novasDatas()
                .comProcessamento(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
                .comDocumento(calendar3.get(Calendar.DAY_OF_MONTH), calendar3.get(Calendar.MONTH) + 1, calendar3.get(Calendar.YEAR))
                .comVencimento(calendar2.get(Calendar.DAY_OF_MONTH), calendar2.get(Calendar.MONTH) + 1, calendar2.get(Calendar.YEAR));

        Beneficiario beneficiario = gerarBeneficiario(dadoBancario, boletoPagamento);

        Pagador pagador = gerarPagador(requerente);

        Boleto boleto = Boleto.novoBoleto()
                .comBanco(banco)
                .comDatas(datas)
                .comBeneficiario(beneficiario)
                .comPagador(pagador)
                .comValorBoleto(boletoPagamento.getValTotalBoleto().setScale(2, RoundingMode.HALF_EVEN))
                .comNumeroDoDocumento(Util.zeroLTrim(boletoPagamento.getNumBoleto().substring(boletoPagamento.getNumBoleto().length() - 10)))
                .comInstrucoes(dadoBancario.getDscInstrucao1(), dadoBancario.getDscInstrucao2(), dadoBancario.getDscInstrucao3(), dadoBancario.getDscInstrucao4(), dadoBancario.getDscInstrucao5())
                .comEspecieDocumento("DM")
                .comLocaisDePagamento(dadoBancario.getLocalPagamento());

        return new GeradorDeBoleto(boleto);
    }

    private static Pagador gerarPagador(Pessoa requerente) {
        Pagador pagador;
        if (requerente.isPF()) {
            PessoaFisica pf = requerente.getPessoaFisica();
            pagador = Pagador.novoPagador()
                    .comNome(pf.getNomPessoa())
                    .comDocumento(NumeroDocumentoFormatterUtil.formatarCpf(pf.getNumCpf()));
        } else {
            PessoaJuridica pj = requerente.getPessoaJuridica();
            pagador = Pagador.novoPagador()
                    .comNome(pj.getNomRazaoSocial())
                    .comDocumento(NumeroDocumentoFormatterUtil.formatarCnpj(pj.getNumCnpj()));
        }

        br.com.caelum.stella.boleto.Endereco enderecoPagador = br.com.caelum.stella.boleto.Endereco.novoEndereco();

        if (!Util.isNull(requerente.getEndereco())) {
            Logradouro logradouro = requerente.getEndereco().getIdeLogradouro();
            if (!Util.isNull(logradouro)) {
                Municipio municipio = logradouro.getMunicipio();
                Estado estado = municipio.getIdeEstado();
                enderecoPagador = enderecoPagador
                        .comLogradouro(logradouro.getIdeTipoLogradouro().getNomTipoLogradouro() + " " + logradouro.getNomLogradouro())
                        .comBairro(logradouro.getIdeBairro().getNomBairro())
                        .comCep(logradouro.getNumCepFormatado())
                        .comCidade(municipio.getNomMunicipio())
                        .comUf(estado.getDesSigla());
            }
        }

        pagador.comEndereco(enderecoPagador);
        return pagador;
    }

    private static Beneficiario gerarBeneficiario(DadoBancario dadoBancario, BoletoPagamentoRequerimento boletoPagamento) {
        GeradorDeDigito digitoVerificador = new CustomGeradorDeDigito();
        int digitoVerificadorGerado = digitoVerificador.geraDigitoMod11(boletoPagamento.getNumBoleto());

        return Beneficiario.novoBeneficiario()
                .comNomeBeneficiario(dadoBancario.getNomCedente())
                .comAgencia(dadoBancario.getNumAgencia().toString()).comDigitoAgencia(dadoBancario.getNumDigitoAgencia())
                .comCodigoBeneficiario(dadoBancario.getNumConta().toString())
                .comDigitoCodigoBeneficiario(dadoBancario.getNumDigitoConta())
                .comNumeroConvenio(dadoBancario.getConvenio())
                .comModalidade(Modalidade.COM_REGISTRO)
                .comCarteira(dadoBancario.getNumCarteira())
                .comNossoNumero(boletoPagamento.getNumBoleto())
                .comDigitoNossoNumero(String.valueOf(digitoVerificadorGerado))
                .comDocumento(dadoBancario.getNumCnpj());
    }
}
