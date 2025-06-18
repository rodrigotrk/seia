package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.TipoSecao;

public interface TipoSecaoDAOIf {

	public List<TipoSecao> getTiposSecao(TipoSecao pTipoSecao);
}