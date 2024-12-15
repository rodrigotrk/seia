package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Acao;

public interface AcaoDAOIf {

	public List<Acao> getAcoes(Acao pAcao);
}