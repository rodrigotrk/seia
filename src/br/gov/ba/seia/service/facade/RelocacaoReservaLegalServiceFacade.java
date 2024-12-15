package br.gov.ba.seia.service.facade;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.LocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.dao.ReservaLegalDAOImpl;
import br.gov.ba.seia.dto.RelocacaoReservaLegalDTO;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.FormaRealocacaoRl;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.StatusReservaLegalEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.service.FormaRealocacaoRlService;
import br.gov.ba.seia.service.TipoArlService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelocacaoReservaLegalServiceFacade {

	@EJB
	private LocalizacaoGeograficaDAOImpl localizacaoGeograficaDAOImpl;
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	@EJB
	private ImovelDAOImpl imovelListagemDAOImpl;
	@EJB
	private ReservaLegalDAOImpl reservaLegalDAOImpl;
	@EJB
	private TipoArlService tipoArlService;
	@EJB
	private FormaRealocacaoRlService formaRealocacaoRlService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<FormaRealocacaoRl> listarTodosFormaRealocacaoRls()  {
		return formaRealocacaoRlService.listarTodosFormaRealocacaoRls();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoArl> listarTipoArl()  {
		return tipoArlService.listarTipoArls();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvar(Collection<RelocacaoReservaLegalDTO> lista)  {
		for(RelocacaoReservaLegalDTO rrl : lista) {
			Integer ideReservaLegal = Util.isNull(rrl.getReservaLegalPoligonalConcedida()) ? null : rrl.getReservaLegalPoligonalConcedida().getIdeReservaLegal();  
			rrl.getReservaLegalPoligonalSelecionada().setIdeReservaLegal(ideReservaLegal);
			reservaLegalDAOImpl.salvarOuAtualizar(rrl.getReservaLegalPoligonalSelecionada());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RelocacaoReservaLegalDTO> carregar(ProcessoAto processoAto) throws Exception  {
		
		Processo processo = processoAto.getProcesso();
		
		Collection<Imovel> listarImovel = imovelListagemDAOImpl.listarImoveisPor(processo.getIdeProcesso());
		
		Collection<RelocacaoReservaLegalDTO> listaRelocacaoReservaLegalDTO = new ArrayList<RelocacaoReservaLegalDTO>();
		for(Imovel imovel : listarImovel) {
			Collection<LocalizacaoGeografica> listaLocalizacaoReservaRequerimento = localizacaoGeograficaDAOImpl.listarLocalizacaoReservaRequerimento(processo.getIdeProcesso());
			Collection<LocalizacaoGeografica> listaLocalizacaoReservaNotificacao = localizacaoGeograficaDAOImpl.listarLocalizacaoReservaNotificacao(processo.getIdeProcesso(),imovel.getIdeImovel());
			RelocacaoReservaLegalDTO rrl = montarRelocacaoReservaLegalDTO(imovel, processoAto, listaLocalizacaoReservaRequerimento,listaLocalizacaoReservaNotificacao);
			listaRelocacaoReservaLegalDTO.add(rrl);
		}
		
		return listaRelocacaoReservaLegalDTO;
	}

	private RelocacaoReservaLegalDTO montarRelocacaoReservaLegalDTO(Imovel imovel, ProcessoAto processoAto,
		Collection<LocalizacaoGeografica> listaLocalizacaoReservaRequerimento, 
		Collection<LocalizacaoGeografica> listaLocalizacaoReservaNotificacao) throws Exception  {
		
		RelocacaoReservaLegalDTO rrl = new RelocacaoReservaLegalDTO();
		
		ReservaLegal reservaLegalConcedida = reservaLegalDAOImpl.buscarReservaLegalConcedida(imovel.getIdeImovel(), processoAto.getIdeProcessoAto());
		
		rrl.setImovelRural(imovel.getImovelRural());
		if(!Util.isNull(reservaLegalConcedida)) {
			rrl.setReservaLegalPoligonalConcedida(reservaLegalConcedida);
			rrl.setReservaLegalPoligonalSelecionada(reservaLegalConcedida.clone());
		}
		
		Index indexReservaLegal = new Index(); 
		rrl.setListaPoligonalRequerimento(montarReservaLegalPoligonal(listaLocalizacaoReservaRequerimento, processoAto, imovel.getImovelRural(),indexReservaLegal));
		rrl.setListaPoligonalNotificacao(montarReservaLegalPoligonal(listaLocalizacaoReservaNotificacao, processoAto, imovel.getImovelRural(),indexReservaLegal));
		
		if(!Util.isNull(rrl.getReservaLegalPoligonalConcedida())) {
			if(Util.isNull(rrl.getReservaLegalPoligonalConcedida().getIdeNotificacao())) {
				rrl.getListaPoligonalRequerimento().clear();
				rrl.getListaPoligonalRequerimento().add(rrl.getReservaLegalPoligonalConcedida().clone());
			}
			else{
				Collection<ReservaLegal> listaPoligonalNotificacao = new ArrayList<ReservaLegal>(rrl.getListaPoligonalNotificacao());
				for(ReservaLegal rl : listaPoligonalNotificacao) {
					if(rl.getIdeNotificacao().equals(rrl.getReservaLegalPoligonalConcedida().getIdeNotificacao())) {
						rrl.getListaPoligonalNotificacao().remove(rl);
						rrl.getListaPoligonalNotificacao().add(rrl.getReservaLegalPoligonalConcedida().clone());
					}
				}
			}
		}
		
		return rrl;
	}

	private Collection<ReservaLegal> montarReservaLegalPoligonal(Collection<LocalizacaoGeografica> listaLocalizacaoReserva, ProcessoAto processoAto, ImovelRural imovelRural, Index indexReservaLegal) throws Exception  {
		Collection<ReservaLegal> listaReservaLegalPoligonal = new ArrayList<ReservaLegal>();
		for(LocalizacaoGeografica lg : listaLocalizacaoReserva) {
			indexReservaLegal.subt();
			ReservaLegal rl = new ReservaLegal();
			rl.setIdeReservaLegal(indexReservaLegal.getValue());
			rl.setIdeImovelRural(null);
			rl.setIdeReservaLegalPai(reservaLegalDAOImpl.buscarReservaLegalPor(imovelRural));
			rl.setIdeLocalizacaoGeografica(lg);
			rl.setIdeProcessoAto(processoAto);
			rl.setIdeTipoEstadoConservacao(new TipoEstadoConservacao(TipoEstadoConservacaoEnum.PRESERVADA.getId()));
			rl.setIdeDadoOrigem(new DadoOrigem(DadoOrigemEnum.TECNICO.getId()));
			rl.setIdeStatus(new StatusReservaLegal(StatusReservaLegalEnum.CADASTRADA.getId()));
			rl.setValArea(localizacaoGeograficaServiceFacade.retornarAreaShape(lg));
			if(!Util.isNull(lg.getNotificacao())) {
				rl.setIdeNotificacao(lg.getNotificacao());
			}
			listaReservaLegalPoligonal.add(rl);
		}
		return listaReservaLegalPoligonal;
	}

}

class Index {
	
	private Integer value;
	
	public Index() {
		this.value = 0;
	}

	public Integer getValue() {
		return value;
	}

	public void subt() {
		this.value = value-1;
	}
}