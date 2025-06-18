package br.gov.ba.seia.enumerator;

public enum TipoServicoBaseEnum {
	
	ABASTECIMENTO_VEICULOS(1),
	TROCA_OLEO(2),
	OFICINA_MANUTENCAO(3),
	LAVAGEM_EXTERNA_VEICULO(4),
	LAVAGEM_INTERNA_TANQUE_VEICULO(5),
	PINTURA_VEICULO(6);
	
	private int id;
	
	private TipoServicoBaseEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}


}
