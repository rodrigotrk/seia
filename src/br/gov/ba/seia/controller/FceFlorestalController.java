package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.factory.FceFlorestalControllerFactory;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Named("fceFlorestalController")
@ViewScoped
public class FceFlorestalController {
	
	@EJB
	private FceFlorestalControllerFactory fceFlorestalFactory;
	
	private FceFlorestalAbstractController impl;
	
	public void init(ActionEvent evt) {
		try {
			Requerimento requerimento = (Requerimento) evt.getComponent().getAttributes().get("requerimento");
			DocumentoObrigatorio documentoObrigatorio = (DocumentoObrigatorio) evt.getComponent().getAttributes().get("documentoObrigatorio");
			load(requerimento, documentoObrigatorio);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void init(Fce fce) {
		try {
			load(fce.getIdeRequerimento(), fce.getIdeDocumentoObrigatorio());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void init(DadoConcedidoFceImpl dadoConcedidoImpl) {
		try {
			load(dadoConcedidoImpl);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void load(DadoConcedidoFceImpl dadoConcedidoFceImpl) throws Exception {
        impl = fceFlorestalFactory.getInstance(dadoConcedidoFceImpl);
        impl.init();
    }
	
	private void load(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio) throws Exception {
        impl = fceFlorestalFactory.getInstance(requerimento, documentoObrigatorio);
        impl.init();
    }
    
	public FceFlorestalAbstractController getImpl() {
		return impl;
	}

	public void setImpl(FceFlorestalAbstractController impl) {
		this.impl = impl;
	}
	
}