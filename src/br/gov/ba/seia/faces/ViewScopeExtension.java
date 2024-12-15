package br.gov.ba.seia.faces;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
/**
 * Classe que trata do view scope do SEIA
 * @author 
 *
 */
public class ViewScopeExtension implements Extension {
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        event.addContext(new ViewContext());
    }
}