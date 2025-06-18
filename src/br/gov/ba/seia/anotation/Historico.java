package br.gov.ba.seia.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Historico {
	
	
	
	/**
	 * Propiedade que guarda o nome ao qual a alteração do historico será apresentado.
	 **/
	String name() default "";

	/**
	 * Quando a propiedade que sera apresentada não possui o nome que deve aparecer ela, deverá apontar para o objeto que contem o valor.
	 **/
	String nameMethod() default "";

	/**
	 * 
	 **/
	String nameKey() default "";
	
	/**
	 * Propiedade que guarda o nome ao qual a alteração do historico será apresentado.
	 **/
	boolean key() default false;
	
	
	/**
	 * 
	 **/
	boolean subTable() default false;

	
	String subTableName() default "";

}
