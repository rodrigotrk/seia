package br.gov.ba.seia.faces;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.enterprise.context.NormalScope;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * Classe que trata do view scope do SEIA
 * @author 
 *
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
@NormalScope
@Inherited
public @interface ViewScoped { }