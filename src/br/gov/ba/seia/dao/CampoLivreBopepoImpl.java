package br.gov.ba.seia.dao;

import org.jrimum.bopepo.campolivre.CampoLivre;
import org.jrimum.utilix.text.Field;
import org.jrimum.utilix.text.Filler;

/**
 * @author monique.dantas
 */
public class CampoLivreBopepoImpl implements CampoLivre {

	private static final long serialVersionUID = 1L;
	private String campoLivreFormatado = null;

	public CampoLivreBopepoImpl(Integer codigoContaCedente, String nossoNumero, String codigoCarteira) {
		campoLivreFormatado = new Field<Integer>(0, 6, Filler.ZERO_LEFT).write()
				+ new Field<String>(nossoNumero, 17, Filler.ZERO_LEFT).write()
				+ new Field<String>(codigoCarteira, 2, Filler.ZERO_LEFT).write();
	}

	@Override
	public String write() {
		return campoLivreFormatado;
	}

	@Override
	public void read(String g) {
		
		
	}

	

}
