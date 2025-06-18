package br.gov.ba.seia.util;

import br.gov.ba.seia.entity.AtividadeInexigivel;

public class AtividadeInexigivelUtil {

	public static boolean permiteLocalRealizacao(AtividadeInexigivel atividadeInexigivel) {
		boolean permite = false;
		
		permite = atividadeInexigivel.getPermiteLocalRealizacao();
		
		return permite;
	}
	
}
