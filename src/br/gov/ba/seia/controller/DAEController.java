package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ParametroCalculoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("daeController")
@ViewScoped
public class DAEController {

	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	@Inject
	private BoletoAutomatizadoController boletoAutomatizadoController;

	private DetalhamentoBoleto detalhamentoBoleto;
	
	private DetalhamentoBoleto detalhamentoBoletoACalcular;
	
	private Collection<DetalhamentoBoleto> listaDetalhamentoBoleto;
	
	private Collection<BiomaRequerimento> listaBiomaRequerimento;

	public void load(ActionEvent evt) {
		try {
			inicializarVariaveis(evt);
			
			Collection<ParametroCalculo> parametros = this.carregarParametros(detalhamentoBoletoACalcular);

			for (ParametroCalculo parametroCalculo : parametros) {
				if (!parametroCalculo.isExigeCalculo()) {
					BigDecimal valorTaxa = parametroCalculo.getValorTaxa();
					this.detalhamentoBoletoACalcular.setValorCertificado(valorTaxa);
				}
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void zerarArea(AjaxBehaviorEvent e) {
		if(!detalhamentoBoletoACalcular.isExisteArea()) {
			detalhamentoBoletoACalcular.setAreaVistoriada(null);
		}
	}

	private void inicializarVariaveis(ActionEvent evt) throws CloneNotSupportedException {
		
		DetalhamentoBoleto detalhamentoBoletoSelecionado = ((DetalhamentoBoleto) evt.getComponent().getAttributes().get("detalhamentoBoleto"));
		
		detalhamentoBoleto = detalhamentoBoletoSelecionado.clone();
		detalhamentoBoletoACalcular = detalhamentoBoletoSelecionado.clone();
		listaDetalhamentoBoleto = (Collection<DetalhamentoBoleto>) evt.getComponent().getAttributes().get("listaDetalhamentoBoleto");
		listaBiomaRequerimento = (Collection<BiomaRequerimento>) evt.getComponent().getAttributes().get("listaBiomaRequerimento");
	}

	private Collection<ParametroCalculo> carregarParametros(DetalhamentoBoleto detalhamentoBoleto) {
		Collection<ParametroCalculo> parametros = this.parametroCalculoService.listarParametrosDAE(detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental());
		this.detalhamentoBoletoACalcular.setParametros(parametros);
		return parametros;
	}

	public void gerarValorDAE() {
		
		for(DetalhamentoBoleto db : listaDetalhamentoBoleto) {
			if(detalhamentoBoletoACalcular.equals(db)) {
				db.setExisteArea((detalhamentoBoletoACalcular.isExisteArea())); 
				db.setAreaVistoriada(detalhamentoBoletoACalcular.getAreaVistoriada()); 
			}
		}
		
		for(BiomaRequerimento br : listaBiomaRequerimento) {
			if(!Util.isNull(br.getBiomaEnquadramentoAtoAmbiental())) {
				AtoAmbiental atoAmbiental = br.getBiomaEnquadramentoAtoAmbiental().getIdeEnquadramentoAtoAmbiental().getAtoAmbiental();
				if(atoAmbiental.equals(detalhamentoBoletoACalcular.getIdeAtoAmbiental())) {
					if(!Util.isNull(detalhamentoBoletoACalcular.getAreaVistoriada())) {
						BigDecimal total = BigDecimal.valueOf(br.getValArea());
						
						if(!Util.isNull(detalhamentoBoleto.getAreaVistoriada())) {
							total = total.subtract(detalhamentoBoleto.getAreaVistoriada());
						}
						
						total = total.add(detalhamentoBoletoACalcular.getAreaVistoriada());							
						br.setValArea(total.doubleValue());
					}
					else{
						br.setValArea(0.0);
					}
				}
			}
		}
		
		boletoAutomatizadoController.atualizarAreaBiomaRequerimento(listaDetalhamentoBoleto, listaBiomaRequerimento);
	}
	
	public boolean isAtoSemVistoria() {
		if(!Util.isNull(detalhamentoBoletoACalcular) 
				&& !Util.isNull(detalhamentoBoletoACalcular.getIdeAtoAmbiental()) && !Util.isNull(detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()) 
				&& (detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId())
				|| detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId()))) {
			
				return true;
			}
		
		return false;
	}

	public DetalhamentoBoleto getDetalhamentoBoletoACalcular() {
		return detalhamentoBoletoACalcular;
	}

	public void setDetalhamentoBoletoACalcular(DetalhamentoBoleto detalhamentoBoletoACalcular) {
		this.detalhamentoBoletoACalcular = detalhamentoBoletoACalcular;
	}

	public Collection<BiomaRequerimento> getListaBiomaRequerimento() {
		return listaBiomaRequerimento;
	}

	public void setListaBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		this.listaBiomaRequerimento = listaBiomaRequerimento;
	}

}
