package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.TipologiaGrupo;

public interface TipologiaGrupoDAOIf {

	public List<TipologiaGrupo> getTipologiasGrupos(TipologiaGrupo pTipologiaGrupo);
}