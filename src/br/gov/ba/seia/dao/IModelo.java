package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Pessoa;

interface IPessoa extends IDAO<Pessoa>{
	List<Pessoa> buscaTodosModelosNovos();	
}
