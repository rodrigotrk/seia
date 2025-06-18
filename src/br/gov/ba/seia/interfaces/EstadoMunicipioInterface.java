package br.gov.ba.seia.interfaces;

import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;


/**
 * @author eduardo.fernandes 
 * @since 01/02/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
 *
 */
public interface EstadoMunicipioInterface {

	public Municipio getMunicipio();
	
	public void setMunicipio(Municipio municipio);
	
	public Estado getEstado();
	
	public void setEstado(Estado estado);
	
}
