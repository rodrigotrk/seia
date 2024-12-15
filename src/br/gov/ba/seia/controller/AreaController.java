package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.component.UIForm;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.TipoArea;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.OrgaoService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.TipoAreaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("areaController")
@ViewScoped
public class AreaController extends SeiaControllerAb {

	private String nomeArea;
	private Area area;
	
	private TipoArea tipoArea = new TipoArea();
	
	private DataModel<Area> modelAreas;

	private UIForm formularioASerLimpo;

	@EJB
	private AreaService areaService;
	@EJB
	private TipoAreaService tipoAreaService;	
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private OrgaoService orgaoService;
	@EJB
	private PautaService pautaService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;

	public AreaController() {}

	public void pesquisarArea() {

		try {

			modelAreas = Util.castListToDataModel(areaService.filtrarListaAreas(!Util.isNullOuVazio(getNomeArea()) ? new Area(getNomeArea()) : new Area()));
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void novaArea() {

		setArea(new Area());

		limparTela();
	}

	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarArea() {

		try {
			
			area.setIdeTipoArea(tipoArea.getIdeTipoArea());
		
			if (Util.isNull(getArea().getIdeArea())) {
				areaService.salvarArea(getArea());				
			}
			else {				
				//verificando se a area já possuia coordenador, e removendo os vinculos
				Area areaStatusAnterior = areaService.carregarGet(getArea().getIdeArea());
				if(Util.isNullOuVazio(area.getIdeAreaPai())) {
					area.setIdeAreaPai(areaStatusAnterior.getIdeAreaPai());
				}
				if(areaStatusAnterior.getIdePessoaFisica()!=null){
					Funcionario fCoordenadorAnterior = funcionarioService.carregaFuncionario(areaStatusAnterior.getIdePessoaFisica().getIdePessoaFisica());				
					Pauta pCoordenadorAnterior = pautaService.obtemPautaPorIdeFuncionario(fCoordenadorAnterior.getIdePessoaFisica());										
					Area aAntigaDoAntigoCoordenador = areaService.carregar(fCoordenadorAnterior.getIdeArea());
					aAntigaDoAntigoCoordenador.setIdePessoaFisica(null);
					pCoordenadorAnterior.setIdeArea(null);					
					areaService.atualizar(aAntigaDoAntigoCoordenador);
					pautaService.atualizar(pCoordenadorAnterior);
				}								
				
				if(Util.isNullOuVazio(getArea().getIdePessoaFisica())) {
					areaService.atualizar(getArea());				
				}
				else {
					//carregando dados do novo coordenador
					Funcionario fNovoCoordenador = funcionarioService.carregaFuncionario(getArea().getIdePessoaFisica().getIdePessoaFisica());				
					Pauta pNovoCoordenador = pautaService.obtemPautaPorIdeFuncionario(fNovoCoordenador.getIdePessoaFisica());
					
					//verificando se o novo coordenador está vinculado a uma área, e removendo os vinculos 
					Area aAntigaDoNovoCoordenador = areaService.obterAreaFuncionarioCoordenadorPorIdeFuncionario(fNovoCoordenador.getIdePessoaFisica());
					if(aAntigaDoNovoCoordenador!=null){
						aAntigaDoNovoCoordenador.setIdePessoaFisica(null);
						areaService.atualizar(aAntigaDoNovoCoordenador);
					}
					
					fNovoCoordenador.setIdeArea(getArea());
					pNovoCoordenador.setIdeArea(getArea());							
					
					areaService.atualizar(getArea());				
					pautaService.atualizar(pNovoCoordenador);
					funcionarioService.atualizarFuncionario(fNovoCoordenador);
				}
			}

			this.setModelAreas(null);
			JsfUtil.addSuccessMessage("Operação efetuada com sucesso.");
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}


	public void excluirArea() {

		try {

			areaService.excluirArea(getArea());

			this.setModelAreas(null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	
	
    public Collection<Area> getColAreas() {
    	return areaService.filtrarListaAreas(new Area());
    }

    

    public Collection<TipoArea> getTipoAreas() {
    	
    	return  tipoAreaService.listarTipoAreas();
    	    	
    	
    }
    
    
    
    public Collection<Orgao> getColOrgaos() {

    	return orgaoService.filtrarListaOrgaos(new Orgao());
    }
    public Collection<Funcionario> getColFuncionarios() {

    	try {

    		return funcionarioService.listarPessoaFisicaCoordenadoresDiretores();
		}
    	catch (Exception e) {

    		JsfUtil.addErrorMessage(e.getMessage());
		}

    	return null;
    }

	public DataModel<Area> getModelAreas() {

		if (Util.isNull(modelAreas)) {

			try {

				modelAreas = Util.castListToDataModel(areaService.filtrarListaAreas(!Util.isNullOuVazio(getNomeArea()) ? new Area(getNomeArea()) : new Area()));
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelAreas;
	}
	public void setModelAreas(DataModel<Area> modelAreas) {
		this.modelAreas = modelAreas;
	}

	public Area getArea() {

		if (Util.isNull(area)) area = new Area();

		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	
	
	
	public TipoArea getTipoArea() {
		return tipoArea;
	}

	public void setTipoArea(TipoArea tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
	
	public void carregarResponsavelArea(){
		PessoaFisica pf;
		try {
			if(!Util.isNullOuVazio(area)	&& !Util.isNullOuVazio(area.getIdePessoaFisica())){
				pf = pessoaFisicaService.carregarPessoaFisicaGet(area.getIdePessoaFisica().getIdePessoaFisica());
				area.getIdePessoaFisica().setPessoaFisica(pf);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}		
	}
}