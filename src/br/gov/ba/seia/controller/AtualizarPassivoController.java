package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ControleProcessoAtoService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("atualizarPassivoController")
@ViewScoped
public class AtualizarPassivoController extends SeiaControllerAb {
	
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private ControleProcessoAtoService controleProcessoAtoService;
	
	private VwConsultaProcesso vwProcesso;
	private DualListModel<ProcessoAto> dualListProcessoAto = new DualListModel<ProcessoAto>(new ArrayList<ProcessoAto>(), new ArrayList<ProcessoAto>());

	@PostConstruct
	public void init() {
		carregarProcessoAto();
		
	}

	public void carregarProcessoAto(){
		try {
			
			if(vwProcesso!=null && vwProcesso.getIdeProcesso()!=null){
				
				List<ProcessoAto> processoAtoList = new ArrayList<ProcessoAto>();
				
					processoAtoList.addAll(processoAtoService.listarAtosPorProcessoComOuSemStatus(vwProcesso.getIdeProcesso(), false, null));
					processoAtoList.addAll(processoAtoService.listarAtosPorProcessoComOuSemStatus(vwProcesso.getIdeProcesso(), true, StatusProcessoAtoEnum.EM_ANALISE));
				
				if(Util.isNullOuVazio(processoAtoList)){
					JsfUtil.addWarnMessage("Não existe passivos a serem atualizados.");
				}
				
				dualListProcessoAto = new DualListModel<ProcessoAto>(processoAtoList, new ArrayList<ProcessoAto>());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvar() {
		
		try {
			ControleProcessoAto controleProcessoAto;

			if(dualListProcessoAto.getTarget().isEmpty() && dualListProcessoAto.getSource().isEmpty()){
				JsfUtil.addWarnMessage("Adicione os atos ambientais antes de salvar.");
			}else{
				for (ProcessoAto processoAto: dualListProcessoAto.getTarget()) {
					controleProcessoAto = new ControleProcessoAto();
					controleProcessoAto.setIdeProcessoAto(processoAto);
					controleProcessoAto.setIndAprovado(true);
					controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO.getId()));
					controleProcessoAto.setDtcControleProcessoAto(new Date());
					controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
					controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
				}
				for (ProcessoAto processoAto: dualListProcessoAto.getSource()) {
					controleProcessoAto = new ControleProcessoAto();
					controleProcessoAto.setIdeProcessoAto(processoAto);
					controleProcessoAto.setIndAprovado(true);
					controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO.getId()));
					controleProcessoAto.setDtcControleProcessoAto(new Date());
					controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
					controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
				}
				
				
			  RequestContext.getCurrentInstance().execute("dialogAtualizarPassivo.hide();");
		      RequestContext.getCurrentInstance().execute("confirmacaoAtualizarPassivo.hide();");
				
			JsfUtil.addSuccessMessage("Passivo dos atos ambientais atualizados com sucesso!");
			}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
		
	
	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}
	
	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}
	
	public DualListModel<ProcessoAto> getDualListAtos() {
		return dualListProcessoAto;
	}

	public void setDualListAtos(DualListModel<ProcessoAto> dualListProcessoAto) {
		this.dualListProcessoAto = dualListProcessoAto;
	}
}