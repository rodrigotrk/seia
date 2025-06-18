package br.gov.ba.seia.util.boleto;

import java.io.Serializable;

import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementação customizada do gerador de dígitos para validação de códigos de barras e campos de boletos.
 */
public class CustomGeradorDeDigito implements GeradorDeDigito, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomGeradorDeDigito.class);

    /**
     * Gera o dígito verificador utilizando o módulo 11.
     *
     * @param codigoDeBarras o código de barras para cálculo.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoMod11(String codigoDeBarras) {
        LOGGER.debug("Iniciando cálculo do dígito verificador Modulo 11 para o código de barras.");
        return geraDigitoMod(codigoDeBarras, 0, codigoDeBarras.length(), 11);
    }

    /**
     * Gera o dígito verificador utilizando o módulo especificado.
     *
     * @param codigoDeBarras o código de barras para cálculo.
     * @param inicio posição inicial para o cálculo.
     * @param fim posição final para o cálculo.
     * @param numMOD o número do módulo a ser utilizado.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoMod(String codigoDeBarras, int inicio, int fim, int numMOD) {
        LOGGER.debug("Calculando dígito Mod {} para o código de barras de {} a {}.", numMOD, inicio, fim);
        int soma = 0;
        int peso = 2;
        for (int i = fim - 1; i >= inicio; i--) {
            int digito = Character.getNumericValue(codigoDeBarras.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % numMOD;
        if (resto == 0 || resto == 1) {
            LOGGER.debug("Dígito ajustado para conformidade: 1.");
            return 1; // Ajuste para conformidade com algumas regras bancárias
        }
        return (numMOD - resto);
    }

    /**
     * Gera o dígito verificador utilizando o módulo 10.
     *
     * @param campo o campo para cálculo.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoMod10(String campo) {
        LOGGER.debug("Iniciando cálculo do dígito verificador Modulo 10 para o campo.");
        int soma = 0;
        int peso = 2;
        for (int i = campo.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(campo.charAt(i));
            int produto = digito * peso;
            soma += (produto > 9) ? (produto / 10 + produto % 10) : produto;
            peso = (peso == 2) ? 1 : 2;
        }
        int resto = soma % 10;
        return (resto == 0) ? 0 : (10 - resto);
    }

    /**
     * Gera o dígito verificador para o bloco 1 utilizando o módulo 10.
     *
     * @param bloco o bloco de dígitos.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoBloco1(String bloco) {
        LOGGER.debug("Gerando dígito para o bloco 1.");
        return geraDigitoMod10(bloco);
    }

    /**
     * Gera o dígito verificador para o bloco 2 utilizando o módulo 10.
     *
     * @param bloco o bloco de dígitos.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoBloco2(String bloco) {
        LOGGER.debug("Gerando dígito para o bloco 2.");
        return geraDigitoMod10(bloco);
    }

    /**
     * Gera o dígito verificador para o bloco 3 utilizando o módulo 10.
     *
     * @param bloco o bloco de dígitos.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoBloco3(String bloco) {
        LOGGER.debug("Gerando dígito para o bloco 3.");
        return geraDigitoMod10(bloco);
    }

    /**
     * Gera o dígito verificador utilizando o módulo 11 aceitando resto zero.
     *
     * @param codigoDeBarras o código de barras para cálculo.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoMod11AceitandoRestoZero(String codigoDeBarras) {
        LOGGER.debug("Iniciando cálculo do dígito Modulo 11 aceitando resto zero.");
        return geraDigitoModAceitandoRestoZero(codigoDeBarras, 0, codigoDeBarras.length(), 11);
    }

    /**
     * Gera o dígito verificador utilizando o módulo especificado aceitando resto zero.
     *
     * @param codigoDeBarras o código de barras para cálculo.
     * @param inicio posição inicial para o cálculo.
     * @param fim posição final para o cálculo.
     * @param numMOD o número do módulo a ser utilizado.
     * @return o dígito verificador calculado.
     */
    @Override
    public int geraDigitoModAceitandoRestoZero(String codigoDeBarras, int inicio, int fim, int numMOD) {
        LOGGER.debug("Calculando dígito Mod{} aceitando resto zero para o código de barras de {} a {}.", numMOD, inicio, fim);
        int soma = 0;
        int peso = 2;
        for (int i = fim - 1; i >= inicio; i--) {
            int digito = Character.getNumericValue(codigoDeBarras.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % numMOD;
        return (resto == 0) ? 0 : (numMOD - resto);
    }

}
