package br.gov.ba.seia.util.boleto;

import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Modalidade;
import br.com.caelum.stella.boleto.bancos.BancoDoBrasil;

/**
 * Customização do banco BancoDoBrasil para geração de código de barras e formatação de carteiras.
 */
public class CustomBancoDoBrasil extends BancoDoBrasil {
    private static final long serialVersionUID = 1L;

    /**
     * Gera o código de barras para o boleto, incluindo validações específicas.
     *
     * @param boleto Objeto contendo os dados do boleto.
     * @return O código de barras gerado.
     */
    @Override
    public String geraCodigoDeBarrasPara(Boleto boleto) {
        Beneficiario beneficiario = boleto.getBeneficiario();
        String numeroConvenio = beneficiario.getNumeroConvenio();

        if (numeroConvenio == null || numeroConvenio.isEmpty()) {
            throw new IllegalArgumentException("O número do convênio não pode ser nulo ou vazio!");
        }

        int numeroPosicoesConvenio = numeroConvenio.length();
        Modalidade modalidade = beneficiario.getModalidade();

        StringBuilder campoLivre = new StringBuilder();

        if (numeroPosicoesConvenio == 7 && modalidade.equals(Modalidade.COM_REGISTRO)) {
            campoLivre.append("000000").append(beneficiario.getNossoNumero());
        } else if ((numeroPosicoesConvenio == 4 || numeroPosicoesConvenio == 6) && modalidade.equals(Modalidade.COM_REGISTRO)) {
            campoLivre.append(beneficiario.getNossoNumero())
                      .append(beneficiario.getAgenciaFormatada())
                      .append(beneficiario.getCodigoBeneficiario());
        } else if (numeroPosicoesConvenio == 6 && modalidade.equals(Modalidade.SEM_REGISTRO)) {
            campoLivre.append(beneficiario.getNossoNumero());
        } else {
            throw new IllegalArgumentException("Configuração do convênio ou modalidade inválida para geração do código de barras.");
        }

        String carteiraFormatada = beneficiario.getCarteira().substring(0, 2);
        campoLivre.append(carteiraFormatada);

        if (campoLivre.length() != 25) {
            throw new IllegalArgumentException(
                String.format("Tamanho do campo livre inválido. Deveria ter 25, mas tem %d caracteres.", campoLivre.length())
            );
        }

        return new CodigoDeBarrasBuilder(boleto).comCampoLivre(campoLivre);
    }

    /**
     * Formata a carteira do beneficiário, ajustando o formato com barras.
     *
     * @param beneficiario Objeto contendo os dados do beneficiário.
     * @return A carteira formatada.
     */
    @Override
    public String getCarteiraFormatado(Beneficiario beneficiario) {
        String carteira = beneficiario.getCarteira();
        if (carteira == null || !carteira.contains("/")) {
            return carteira;
        }

        String[] partes = carteira.split("/");
        String antesDaBarra = partes[0];
        String depoisDaBarra = partes[1];

        if (depoisDaBarra.startsWith("0")) {
            depoisDaBarra = depoisDaBarra.substring(1);
        }

        return antesDaBarra + "/" + depoisDaBarra;
    }
}
