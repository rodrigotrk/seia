package br.gov.ba.seia.facade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.RFPController;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EspecieFloresta;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.NaturezaEspecieFloresta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoResponsavelTecnico;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SituacaoAtualFlorestaProducao;
import br.gov.ba.seia.enumerator.AbaRegistroFlorestaPlantadaEnum;
import br.gov.ba.seia.service.AtoDeclaratorioService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EspecieFlorestaService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.RegistroFlorestaProducaoImovelEspecieProducaoService;
import br.gov.ba.seia.service.RegistroFlorestaProducaoImovelPlantioService;
import br.gov.ba.seia.service.RegistroFlorestaProducaoImovelService;
import br.gov.ba.seia.service.RegistroFlorestaProducaoResponsavelTecnicoService;
import br.gov.ba.seia.service.RegistroFlorestaProducaoService;
import br.gov.ba.seia.service.SituacaoAtualFlorestaProducaoService;
import br.gov.ba.seia.service.NaturezaEspecieFlorestaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.rfp.Dialog;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RFPFacade extends AtoDeclaratorioFacade implements Serializable{
	private static final long serialVersionUID = 1L;

	@EJB
	private EspecieFlorestaService especieFlorestaService;
	
	@EJB
	private NaturezaEspecieFlorestaService naturezaEspecieFlorestaService;
	
	@EJB
	private SituacaoAtualFlorestaProducaoService situacaoAtualFlorestaProducaoService;
	
	@EJB
	private RegistroFlorestaProducaoResponsavelTecnicoService registroFlorestaProducaoResponsavelTecnicoService;
	
	@EJB
	private RegistroFlorestaProducaoService registroFlorestaProducaoService;
	
	@EJB
	private RegistroFlorestaProducaoImovelService registroFlorestaProducaoImovelService;
	
	@EJB
	private RegistroFlorestaProducaoImovelEspecieProducaoService registroFlorestaProducaoImovelEspecieProducaoService;
	
	@EJB
	private RegistroFlorestaProducaoImovelPlantioService registroFlorestaProducaoImovelPlantioService;
	
	@EJB
	private AtoDeclaratorioService atoDeclaratorioService;
	
	@EJB
	private ImovelService imovelService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			registroFlorestaProducaoImovelService.excluirIdeRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFloresta> listarEspecieFloresta(NaturezaEspecieFloresta naturezaEspecieFloresta) {
		try {
			return especieFlorestaService.listarEspecieFloresta(naturezaEspecieFloresta);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFloresta> listarEspecieFloresta() {
		try {
			return new ArrayList<EspecieFloresta>();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NaturezaEspecieFloresta> listarNaturezaEspecieFloresta() {
		try {
			return naturezaEspecieFlorestaService.listarNaturezaEspecieFloresta();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducaoResponsavelTecnico registroFlorestaProducaoResponsavelTecnico){
		try {
			registroFlorestaProducaoResponsavelTecnicoService.excluirRegistroFlorestaProducaoResponsavelTecnico(registroFlorestaProducaoResponsavelTecnico);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void excluirRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		try {
			registroFlorestaProducaoService.excluirRegistroFlorestaProducao(ideRegistroFlorestaProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoService.excluirRegistroFlorestaProducaoImovelEspecieProducao(ideRegistroFlorestaProducaoImovelEspecieProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio ideRegistroFlorestaProducaoImovelPlantio) {
		try {
			registroFlorestaProducaoImovelPlantioService.excluirRegistroFlorestaProducaoImovelPlantio(ideRegistroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoAtualFlorestaProducao> listarSituacaoAtualFlorestaProducao(){
		try {
			return situacaoAtualFlorestaProducaoService.listarSituacaoAtualFlorestaProducao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoAtualFlorestaProducao buscarSituacaoAtualFlorestaProducao(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			return situacaoAtualFlorestaProducaoService.buscarSituacaoAtualFlorestaProducao(registroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelEspecieProducao(List<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoList) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoService.excluirRegistroFlorestaProducaoImovelEspecieProducao(registroFlorestaProducaoImovelEspecieProducaoList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelPlantio(List<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioList) {
		try {
			registroFlorestaProducaoImovelPlantioService.excluirRegistroFlorestaProducaoImovelPlantio(registroFlorestaProducaoImovelPlantioList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			registroFlorestaProducaoImovelPlantioService.salvarRegistroFlorestaProducaoImovelPlantio(registroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarNomeEmpreendimento(Requerimento requerimento) {
		try {
			return empreendimentoService.carregarNomeEmpreendimento(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RegistroFlorestaProducao carregarRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		
		try {
		
			if(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio()==null){
				ideRegistroFlorestaProducao.setIdeAtoDeclaratorio(new AtoDeclaratorio());
			} 		
			
			if(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().getIdeRequerimento()==null){
				ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().setIdeRequerimento(new Requerimento());
			}
			
			RegistroFlorestaProducao rfp = registroFlorestaProducaoService.carregarRegistroFlorestaProducao(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().getIdeRequerimento());

			if(Util.isNullOuVazio(rfp)){
				return ideRegistroFlorestaProducao;
			}
			
			rfp.setRegistroFlorestaProducaoResponsavelTecnicoList(registroFlorestaProducaoResponsavelTecnicoService.listarRegistroFlorestaProducaoResponsavelTecnico(rfp));

			List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovelList = registroFlorestaProducaoImovelService.listarRegistroFlorestaProducaoImovel(rfp);

			for (RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel : registroFlorestaProducaoImovelList) {
			
				Imovel imovel = imovelService.carregarImovel(registroFlorestaProducaoImovel);
				registroFlorestaProducaoImovel.setIdeImovel(carregarNomeImovel(imovel, ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().getIdeRequerimento()));
				
				registroFlorestaProducaoImovel.setRegistroFlorestaProducaoImovelEspecieProducaoList(
					registroFlorestaProducaoImovelEspecieProducaoService.listarRegistroFlorestaProducaoImovelEspecieProducao(registroFlorestaProducaoImovel)
				);
				
				registroFlorestaProducaoImovel.setRegistroFlorestaProducaoImovelPlantioList(
					registroFlorestaProducaoImovelPlantioService.listarRegistroFlorestaProducaoImovel(registroFlorestaProducaoImovel)
				);
				
				for (RegistroFlorestaProducaoImovelPlantio plantio : registroFlorestaProducaoImovel.getRegistroFlorestaProducaoImovelPlantioList()) {
					locGeoService.tratarPonto(plantio.getIdeLocalizacaoGeografica());
				}
			}
			
			for (RegistroFlorestaProducaoImovel rfpi : registroFlorestaProducaoImovelList) {
				BigDecimal areaTotalPlantio = BigDecimal.ZERO;
				for (RegistroFlorestaProducaoImovelPlantio plantio : rfpi.getRegistroFlorestaProducaoImovelPlantioList()){
					if(plantio.getValAreaPlantio()!=null){
						areaTotalPlantio = areaTotalPlantio.add(plantio.getValAreaPlantio());
					}
				}
				rfpi.setAreaTotalPlantio(areaTotalPlantio);
			}
			
			rfp.setRegistroFlorestaProducaoImovelList(registroFlorestaProducaoImovelList);
			
			return rfp;
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao, AbaRegistroFlorestaPlantadaEnum  abaAtiva) {
		try {
			if(abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE){
				if(validarAbaAceite(ideRegistroFlorestaProducao,abaAtiva)){
					salvarAbaAceite(ideRegistroFlorestaProducao);
				}
			}else if(abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_REGISTRO_FLORESTA_PLANTADA){
				if(validarAbaAceite(ideRegistroFlorestaProducao,abaAtiva) && validarAbaRegistroFlorestaPlantada(ideRegistroFlorestaProducao,abaAtiva)){
					salvarAbaRegistroFlorestaPlantada(ideRegistroFlorestaProducao);
				}
			}

			else if(abaAtiva == AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE){
				if(validarAbaAceite(ideRegistroFlorestaProducao,abaAtiva) && validarAbaRegistroFlorestaPlantada(ideRegistroFlorestaProducao,abaAtiva) && validarAbaResponsabilidade(ideRegistroFlorestaProducao,abaAtiva)){
					salvarAbaResponsabilidade(ideRegistroFlorestaProducao);
					Html.esconder(Dialog.dialogRfp);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void salvarAtoDeclaratorio(RegistroFlorestaProducaoImovelPlantio rf) {
	}
	
	private boolean validarAbaAceite(RegistroFlorestaProducao ideRegistroFlorestaProducao, AbaRegistroFlorestaPlantadaEnum  abaAtiva){
		if((ideRegistroFlorestaProducao==null) ||  
			Util.isNullOuVazio(ideRegistroFlorestaProducao.getIndCienteTermoCompromisso()) || 
			!ideRegistroFlorestaProducao.getIndCienteTermoCompromisso()){
			
			MensagemUtil.msg0003("Ciente");
			
			return false;
		}
		
		return true;
	}
	
	private boolean validarAbaRegistroFlorestaPlantada(RegistroFlorestaProducao ideRegistroFlorestaProducao, AbaRegistroFlorestaPlantadaEnum  abaAtiva){
		
		if(Util.isNullOuVazio(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList())){
			MensagemUtil.msg0003("Dados da floresta de produção");
			return false;
		}

		else{
			BigDecimal valTotalAreas = BigDecimal.ZERO;
			for(RegistroFlorestaProducaoImovel imovel: ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList()){
				if(imovel.getRegistroFlorestaProducaoImovelPlantioList()!=null){
					for (RegistroFlorestaProducaoImovelPlantio plantio : imovel.getRegistroFlorestaProducaoImovelPlantioList()) {
						if(plantio.getValAreaPlantio()!=null){
							valTotalAreas = valTotalAreas.add(plantio.getValAreaPlantio());
						}
					}
				}
			}

			if(valTotalAreas.equals(BigDecimal.ZERO)){
				JsfUtil.addErrorMessage("A soma das áreas do plantio não pode ser zero.");
				return false;
			}
		}
			
		if(Util.isNullOuVazio(ideRegistroFlorestaProducao.getDtPrevistaUltimoCorte())){
			MensagemUtil.msg0003("Data prevista para o último corte");
			return false;
		}
		
		else if(Util.isNullOuVazio(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoResponsavelTecnicoList())){
			MensagemUtil.msg0003("Dados do resposável técnico pelo registro da floresta de procução plantada");
			return false;
		}
		
		return true;
	}
	
	private boolean validarAbaResponsabilidade(RegistroFlorestaProducao ideRegistroFlorestaProducao, AbaRegistroFlorestaPlantadaEnum  abaAtiva){
		if(ideRegistroFlorestaProducao.getIndAceiteResponsabilidade()==null || !ideRegistroFlorestaProducao.getIndAceiteResponsabilidade()){
			MensagemUtil.msg0003("Aceito");
			return false;
		}
		
		return true;	
	}
	
	
	private void salvarAbaAceite(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		
		if(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().getDtcCriacao()==null){
			ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().setDtcCriacao(new Date());
		}

		salvarAtoDeclaratorio(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio());
		
		ideRegistroFlorestaProducao.setIdeAtoDeclaratorio(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio());
		registroFlorestaProducaoService.salvar(ideRegistroFlorestaProducao);
	}

	
	private void salvarAbaRegistroFlorestaPlantada(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		
		if(!Util.isNullOuVazio(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoResponsavelTecnicoList())){
			for(RegistroFlorestaProducaoResponsavelTecnico tecnicos: ideRegistroFlorestaProducao.getRegistroFlorestaProducaoResponsavelTecnicoList()){
				tecnicos.setIdeRegistroFlorestaProducao(ideRegistroFlorestaProducao);
				pessoaFisicaService.salvarOuAtualizar(tecnicos.getIdePessoaFisica());
			}
			registroFlorestaProducaoResponsavelTecnicoService.salvar(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoResponsavelTecnicoList());
		}
		
		if(!Util.isNullOuVazio(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList())){
			for (RegistroFlorestaProducaoImovel imovelProducao : ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList()) {
				imovelProducao.setIdeRegistroFlorestaProducao(ideRegistroFlorestaProducao);
				imovelProducao.setIndArrendado(imovelProducao.getIdeImovel().getIndArrendado());
			}
			registroFlorestaProducaoImovelService.salvarRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList());
		}
		
		for (RegistroFlorestaProducaoImovel imovel : ideRegistroFlorestaProducao.getRegistroFlorestaProducaoImovelList()) {
			if(imovel.getRegistroFlorestaProducaoImovelPlantioList()!=null){
				for (RegistroFlorestaProducaoImovelPlantio imovelPlantio  : imovel.getRegistroFlorestaProducaoImovelPlantioList()) {
					imovelPlantio.setIdeRegistroFlorestaProducaoImovel(imovel);
				
				}
				registroFlorestaProducaoImovelPlantioService.salvarRegistroFlorestaProducaoImovelPlantio(imovel.getRegistroFlorestaProducaoImovelPlantioList());
			}
				
			if(imovel.getRegistroFlorestaProducaoImovelEspecieProducaoList()!=null){
				for(RegistroFlorestaProducaoImovelEspecieProducao especieProducao : imovel.getRegistroFlorestaProducaoImovelEspecieProducaoList() ){
					especieProducao.setIdeRegistroFlorestaProducaoImovel(imovel);
				}
				registroFlorestaProducaoImovelEspecieProducaoService.salvarRegistroFlorestaProducaoImovelEspecieProducao(imovel.getRegistroFlorestaProducaoImovelEspecieProducaoList());
			}	
		}
		
		salvarAtoDeclaratorio(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio());
		ideRegistroFlorestaProducao.setIndAceiteResponsabilidade(false);
		registroFlorestaProducaoService.salvar(ideRegistroFlorestaProducao);
	}
	
	private void salvarAbaResponsabilidade(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		
		ideRegistroFlorestaProducao.getIdeAtoDeclaratorio().setIndConcluido(true);
		finalizarAtoDeclaratorio(ideRegistroFlorestaProducao.getIdeAtoDeclaratorio());
		registroFlorestaProducaoService.salvar(ideRegistroFlorestaProducao);
		Html.exibir("confirmDialogImprimirRelatorio");
	}


	public Imovel carregarNomeImovel(Imovel imovel, Requerimento requerimento){
		if(imovel.getImovelRural()!=null){
			imovel.setNomImovel(imovel.getImovelRural().getDesDenominacao());
		}
		else if(imovel.getImovelUrbano()!=null){
			Empreendimento empreendimento = carregarNomeEmpreendimento(requerimento);
			if(empreendimento!=null){
				imovel.setNomImovel(empreendimento.getNomEmpreendimento());
			}
		}
		return imovel;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducao(RFPController rfp) {
		if(rfp.getIdeRegistroFlorestaProducaoImovel()!=null){
			rfp.getFacade().excluirRegistroFlorestaProducaoImovelEspecieProducao(rfp.getRegistroFlorestaProducaoImovelEspecieProducaoList());
			rfp.getFacade().excluirRegistroFlorestaProducaoImovelPlantio(rfp.getRegistroFlorestaProducaoImovelPlantioList());
			rfp.getFacade().excluirIdeRegistroFlorestaProducaoImovel(rfp.getIdeRegistroFlorestaProducaoImovel());
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaPorCpf(PessoaFisica pessoaFisica) {
		try {
			return pessoaFisicaService.buscarPessoaFisicaByCPF(pessoaFisica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
