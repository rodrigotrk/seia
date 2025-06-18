package br.gov.ba.seia.util.boleto;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigito;
import br.com.caelum.stella.boleto.exception.CriacaoBoletoException;

/**
 * Builder para criar o código de barras de um boleto bancário conforme as
 * normas da carta circular 2926 do Banco Central do Brasil.
 */
class CodigoDeBarrasBuilder {

    private StringBuilder codigoDeBarras;
    private Banco banco;

    /**
     * Inicializa o builder do código de barras com os primeiros 18 dígitos baseados
     * nos dados do boleto.
     *
     * @param boleto para o qual será gerado o código de barras.
     */
    CodigoDeBarrasBuilder(Boleto boleto) {
        this.banco = boleto.getBanco();
        this.codigoDeBarras = new StringBuilder(44);
        this.codigoDeBarras.append(banco.getNumeroFormatado());

        int codigoEspecieMoeda = boleto.getCodigoEspecieMoeda();
        this.codigoDeBarras.append(codigoEspecieMoeda);

        this.codigoDeBarras.append(boleto.getFatorVencimento());

        this.codigoDeBarras.append(boleto.getValorFormatado());
    }

    /**
     * Adiciona o campo livre ao código de barras e calcula o dígito verificador
     * geral.
     *
     * @param campoLivre preparado pelo banco de acordo com suas regras.
     * @return o código de barras completo, incluindo o dígito verificador geral.
     * @throws CriacaoBoletoException se o código gerado não tiver exatamente 44
     *                                dígitos.
     */
    public String comCampoLivre(StringBuilder campoLivre) {
        this.codigoDeBarras.append(campoLivre);

        String trecho = this.codigoDeBarras.toString();
        int digito = recalcularDigitoVerificadorCodigoDeBarras(trecho);

        this.codigoDeBarras.insert(4, digito);
        validaTamanhoDoCodigoDeBarrasCompletoGerado();

        return this.codigoDeBarras.toString();
    }

    /**
     * Valida o tamanho do código de barras gerado. Lança uma exceção se o tamanho
     * não for 44 dígitos.
     */
    private void validaTamanhoDoCodigoDeBarrasCompletoGerado() {
        int tamanho = this.codigoDeBarras.length();

        if (tamanho != 44) {
            throw new CriacaoBoletoException(
                    "Erro na geração do código de barras. Número de dígitos diferente de 44. Verifique se todos os dados foram preenchidos corretamente.");
        }
    }

    /**
     * Recalcula o dígito verificador geral usando o algoritmo Modulo 11.
     *
     * @param trecho os primeiros 43 caracteres do código de barras.
     * @return o dígito verificador calculado.
     */
    private int recalcularDigitoVerificadorCodigoDeBarras(String trecho) {
        GeradorDeDigito geradorDeDigito = new CustomGeradorDeDigito();
        return geradorDeDigito.geraDigitoMod11(trecho);
    }
}
