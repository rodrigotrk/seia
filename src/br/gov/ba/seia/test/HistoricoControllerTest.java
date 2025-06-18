package br.gov.ba.seia.test;

import org.junit.Before;
import org.junit.Test;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.generic.AbstractEntity;
/**
 * Classe controle do historico teste
 * @author 
 *
 */
public class HistoricoControllerTest {

	private Cerh atual;
	private Cerh anterior;
	
	@Before
	public void setUp(){
		atual = new Cerh();
		anterior = new Cerh();

		atual.setIdeContratoConvenio(new ContratoConvenio());
		atual.getIdeContratoConvenio().setNomContratoConvenio("Silva");
		
		anterior.setIdeContratoConvenio(new ContratoConvenio());
		anterior.getIdeContratoConvenio().setNomContratoConvenio("Zeno");
	
	}
	
	
	@Test
	public void comparar(AbstractEntity a1, AbstractEntity a2) {

		
		
		return;
	}

	
	
}
