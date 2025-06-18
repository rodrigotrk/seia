package br.gov.ba.seia.entity.generic;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Hieraquia
 * 
 * [AbstractEntity]
 * -> [AbstractEntityHist]
 * 
 * Objetos que são salvos para versões de comparação, podem implementar essa classe que é filha de AbstractEntity.
 * Possui apenas uma unico atribulto, que serve para referenciar o objeto que deu origem a ele.  
 * 
 * Exemplo:
 * 
 * AbstractEntity
 * 
 * Classe A extends AbstractEntity{
 *  Id = 1;
 * }
 *
 * Classe B extends AbstractEntity{
 * 	Id = 2;
 * 	IdObjetoPai = 1;
 * }
 * 
 **/
@MappedSuperclass
public class AbstractEntityHist extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Column(name="ide_objeto_pai")
	private Integer ideObjetoPai;

	public AbstractEntityHist() {
	}
	
	public AbstractEntityHist(Integer ideObjetoPai) {
		this.ideObjetoPai = ideObjetoPai;
	}

	public Integer getIdeObjetoPai() {
		return ideObjetoPai;
	}

	public void setIdeObjetoPai(Integer ideObjetoPai) {
		this.ideObjetoPai = ideObjetoPai;
	}
}