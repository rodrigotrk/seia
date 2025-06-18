package br.gov.ba.seia.service.rules.intf;

import br.gov.ba.seia.enumerator.CrudOperationEnum;
/**
 * Interface com serviços iniciais de persistencia
 * @author 
 *
 * @param <T>
 */
public interface RuleIntf<T> {

	public boolean prePersist(T aBean, CrudOperationEnum crudOperation);
	public boolean postPersist(T aBean, CrudOperationEnum crudOperation);
	public boolean preFind();
}
