package br.gov.ba.seia.entity;

import javax.persistence.PostLoad;
import org.hibernate.Hibernate;

/**
 * Correção do problema reportado no ticket #36411
 * Descrição: Ajuste realizado para resolver o erro na visualização da aba de Dados Específicos do cadastro de Imóvel Rural "Fazenda Beija Flor".
 * Detalhes do erro incluíam campos em branco nas abas "Dados Básicos", "Documentação" e "Questionário" que deveriam exibir informações cadastradas.
 * O problema foi causado por uma falha na consulta de dados e foi corrigido para garantir que todas as informações sejam corretamente exibidas conforme esperado.
 */
public class ImovelRuralListener {
    @PostLoad
    public void loadResponsaveis(ImovelRural imovelRural) {
        Hibernate.initialize(imovelRural.getResponsavelImovelRuralCollection());
    }
}
