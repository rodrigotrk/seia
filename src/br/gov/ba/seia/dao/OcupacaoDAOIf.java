package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Ocupacao;

public interface OcupacaoDAOIf {

	public List<Ocupacao> getOcupacoes(Ocupacao pOcupacao);
}