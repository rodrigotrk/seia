package br.gov.ba.seia.interfaces;

import org.primefaces.event.TabChangeEvent;

/**
 * Interface para navegação das telas dos FCE's
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/01/2015
 */
public interface FceNavegacaoInterface {

	public void controlarAbas(TabChangeEvent event);

	public void avancarAba();

	public void voltarAba();

}