package br.gov.ba.seia.enumerator;


public enum ClasseCorpoHidricoEnum {

	CLASSE_ESPECIAL(1, "Classe Especial", 0d),
	CLASSE_1(2, "Classe 1", 3d),
	CLASSE_2(3, "Classe 2", 5d),
	CLASSE_3(4, "Classe 3", 10d),
	CLASSE_4(5, "Classe 4", 120d);
	
	
	private int id;
	
	private String nome;
	
	private double limitClasse;

	private ClasseCorpoHidricoEnum(int id, String nome, double limitClasse) {
		this.id = id;
		this.nome = nome;
		this.limitClasse = limitClasse;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the limitClasse
	 */
	public double getLimitClasse() {
		return limitClasse;
	}
	

}
