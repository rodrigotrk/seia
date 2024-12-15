package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.TransferenciaAmbiental;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransferenciaAmbientalDAO {

	@Inject
	IDAO<TransferenciaAmbiental> transferenciaAmbientaIDAO;
	@Inject 
	IDAO<Pessoa> pessoaDAO;
	@Inject
	IDAO<Processo> processoDAO;
	@Inject
	IDAO<ProcessoAto> processoAtoDAO;
	@Inject
	IDAO<ControleProcessoAto> controleProcessoAtoDAO;
	@Inject
	IDAO<StatusProcessoAto> statusProcessoAtoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TransferenciaAmbiental transferenciaAmbiental) {
		transferenciaAmbientaIDAO.salvar(transferenciaAmbiental);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<TransferenciaAmbiental> transferenciaAmbiental) {
		transferenciaAmbientaIDAO.salvarEmLote(transferenciaAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TransferenciaAmbiental getTransferenciaAmbientalByStatusProcessoCedente(int ideStatusProcessoAto, int ideProcesso)  {
		 DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
			.createAlias("ideProcessoAto", "ideProcessoAto")
			.createAlias("controleProcessoAtoCollection", "controleProcessoAtoCollection")
			.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
			
			.add(Restrictions.eq("ideStatusProcessoAto.ideStatusProcessoAto", ideStatusProcessoAto))
			.add(Restrictions.eq("ideProcessoTla.ideProcesso.ideProcesso", ideProcesso));

		return transferenciaAmbientaIDAO.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> getProcessoAtoCedente(Processo ideProcesso) {
 	  DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
		.createAlias("ideProcessoAto", "ideProcessoAto")
		.add(Restrictions.eq("ideProcessoAto.indExcluido", false))
		.add(Restrictions.eq("ideProcessoTla", ideProcesso))
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideProcessoAto.ideProcessoAto"),"ideProcessoAto")
			.add(Projections.property("ideProcessoAto.processo"),"processo")
			//.add(Projections.property("ideProcessoAto.atoAmbiental"),"atoAmbiental")
			//.add(Projections.property("ideProcessoAto.tipologia"),"tipologia")
			
		).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class)) ; 		 
	 	
	 	return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa getPessoaCedente(int ideProcesso)  {
		 DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
			.createAlias("ideEmpreendimentoOrigem","ideEmpreendimentoOrigem" ,JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimentoOrigem.idePessoa","idePessoa",JoinType.INNER_JOIN)
			.createAlias("idePessoa.pessoaFisica","pessoaFisica" ,JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoa.pessoaJuridica","pessoaJuridica",JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideProcessoTla.ideProcesso", ideProcesso))
				   
			   .setProjection(Projections.projectionList()				
				   .add(Projections.property("idePessoa.idePessoa"),"idePessoa")
				   
				   .add(Projections.property("pessoaFisica.idePessoaFisica"),"pessoaFisica.idePessoaFisica")
				   .add(Projections.property("pessoaFisica.nomPessoa"),"pessoaFisica.nomPessoa")
				   	
   				   .add(Projections.property("pessoaJuridica.idePessoaJuridica"),"pessoaJuridica.idePessoaJuridica")
   				   .add(Projections.property("pessoaJuridica.nomRazaoSocial"),"pessoaJuridica.nomRazaoSocial")
				   
				   
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class)) ; 
		 
		 if(!Util.isNullOuVazio(pessoaDAO.listarPorCriteria(detachedCriteria)) && !Util.isNullOuVazio(pessoaDAO.listarPorCriteria(detachedCriteria))){
			 return pessoaDAO.listarPorCriteria(detachedCriteria).get(0);			 
		 }else{
			 return null;
		 }
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa getPessoaReceptor(int ideProcesso)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
					.createAlias("ideEmpreendimentoDestino","ideEmpreendimentoDestino" ,JoinType.INNER_JOIN)
					.createAlias("ideEmpreendimentoDestino.idePessoa","idePessoa",JoinType.INNER_JOIN)
					.createAlias("idePessoa.pessoaFisica","pessoaFisica" ,JoinType.LEFT_OUTER_JOIN)
					.createAlias("idePessoa.pessoaJuridica","pessoaJuridica",JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideProcessoTla.ideProcesso", ideProcesso))
						   
					   .setProjection(Projections.projectionList()				
						   .add(Projections.property("idePessoa.idePessoa"),"idePessoa")
						   
						   .add(Projections.property("pessoaFisica.idePessoaFisica"),"pessoaFisica.idePessoaFisica")
						   .add(Projections.property("pessoaFisica.nomPessoa"),"pessoaFisica.nomPessoa")
						   	
		   				   .add(Projections.property("pessoaJuridica.idePessoaJuridica"),"pessoaJuridica.idePessoaJuridica")
		   				   .add(Projections.property("pessoaJuridica.nomRazaoSocial"),"pessoaJuridica.nomRazaoSocial")
						   
						   
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class)) ; 
		 
		 if(!Util.isNullOuVazio(pessoaDAO.listarPorCriteria(detachedCriteria)) && !Util.isNullOuVazio(pessoaDAO.listarPorCriteria(detachedCriteria))){
			 return pessoaDAO.listarPorCriteria(detachedCriteria).get(0);			 
		 }else{
			 return null;
		 }
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoAto getProcessoAtoTransferido(int ideProcesso)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
				.createAlias("ideProcessoAto", "ideProcessoAto")
				.createAlias("ideProcessoAto.atoAmbiental", "atoAmbiental")
				.createAlias("ideProcessoAto.tipologia", "tipologia")

				.add(Restrictions.eq("ideProcessoAto.indExcluido", false))
				.add(Restrictions.eq("ideProcessoTla.ideProcesso", ideProcesso))
				
				.setProjection(Projections.projectionList()				
						.add(Projections.property("atoAmbiental.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
						.add(Projections.property("atoAmbiental.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
						
						.add(Projections.property("tipologia.ideTipologia"),"tipologia.ideTipologia")
						.add(Projections.property("tipologia.desTipologia"),"tipologia.desTipologia")
						
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class)) ; 
		
	return processoAtoDAO.buscarPorCriteria(detachedCriteria);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer getIdeStatusProcessoAtoMaisRecente(int ideProcessoAto) {
		
		Object  ideControleProcessoAto;

		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
			
			.createAlias("processoAtoCollection", "processoAto")
			.createAlias("processoAto.controleProcessoAtoCollection", "controleProcessoAto")
			.createAlias("controleProcessoAto.ideStatusProcessoAto", "ideStatusProcessoAto")
			.add(Restrictions.eq("processoAto.ideProcessoAto", ideProcessoAto))
			.add(Restrictions.eq("controleProcessoAto.indAprovado", true))
					
			.setProjection(Projections.projectionList()
				.add(Projections.max("controleProcessoAto.ideControleProcessoAto")));
	
	
		ideControleProcessoAto = controleProcessoAtoDAO.buscarPorCriteria(detachedCriteria);
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ControleProcessoAto.class)
			.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
			.add(Restrictions.eq("ideControleProcessoAto", ideControleProcessoAto))
						
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
			.add(Projections.property("ideStatusProcessoAto.ideStatusProcessoAto"),"ideStatusProcessoAto.ideStatusProcessoAto"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class));
	
		ControleProcessoAto retorno = controleProcessoAtoDAO.buscarPorCriteria(subCriteria);
		
		if(!Util.isNullOuVazio(retorno) &&
		   !Util.isNullOuVazio(retorno.getIdeStatusProcessoAto()) &&
		   !Util.isNullOuVazio(retorno.getIdeStatusProcessoAto().getIdeStatusProcessoAto())){
			return retorno.getIdeStatusProcessoAto().getIdeStatusProcessoAto();
		}
		else{
			return 0;
		}		
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> getListProcessoAtoRecebidos(Processo processo)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("transferenciaAmbientalCollection", "transferenciaAmbiental", JoinType.INNER_JOIN)
			.createAlias("transferenciaAmbiental.ideProcessoAto","processoAto", JoinType.INNER_JOIN)
			.createAlias("processoAto.atoAmbiental","atoAmbiental", JoinType.INNER_JOIN)
			.createAlias("processoAto.tipologia","tipologia", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("processoAto.indExcluido", false));
		
			if(processo.getIdeProcesso()!=null){
				detachedCriteria.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()));
			}else{
				detachedCriteria.add(Restrictions.eq("numProcesso", processo.getNumProcesso()));
			}
			
					
			detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("processoAto.ideProcessoAto"),"ideProcessoAto")
				.add(Projections.property("processoAto.atoAmbiental"),"atoAmbiental")
				.add(Projections.property("ideProcesso"),"processo.ideProcesso")
				.add(Projections.property("numProcesso"),"processo.numProcesso")
				.add(Projections.property("tipologia.ideTipologia"),"tipologia.ideTipologia")
				.add(Projections.property("tipologia.desTipologia"),"tipologia.desTipologia"))
			
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
	
	return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo getNumProcessoByIdeProcesso(Integer ideProcessoAto)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("processoAtoCollection", "processoAto")
			.add(Restrictions.eq("processoAto.ideProcessoAto", ideProcessoAto))
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("numProcesso"))
				.add(Projections.property("numProcesso"),"numProcesso"))	
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class));
		
	return processoDAO.buscarPorCriteria(detachedCriteria);		
	}

	
	public List<TransferenciaAmbiental> isProcessoAtoTransferenciaAmbiental(ProcessoAto pa)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
				.createAlias("ideProcessoTla", "ideProcessoTla")
				.add(Restrictions.eq("ideProcessoAto.ideProcessoAto", pa.getIdeProcessoAto()));
		return transferenciaAmbientaIDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto carregarStatusProcessoAto(Integer ideStatusProcessoAto){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class, "t")
			.createAlias("t.ideProcessoTla", "ideProcesso")
			.createAlias("ideProcesso.processoAtoCollection", "ideProcessoAto")
			.createAlias("ideProcessoAto.controleProcessoAtoCollection", "ideControleProcessoAto")
			.createAlias("ideControleProcessoAto.ideStatusProcessoAto", "ideTipo")
			.add(Restrictions.eq("t.ideProcessoAto.ideProcessoAto", ideStatusProcessoAto))
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideTipo.ideStatusProcessoAto"), "ideStatusProcessoAto")	
				.add(Projections.groupProperty("ideTipo.nomStatusProcessoAto"), "nomStatusProcessoAto"))
				//.add(Projections.property("ideTipo.ideStatusProcessoAto"),"ideStatusProcessoAto")
				//.add(Projections.property("ideTipo.nomStatusProcessoAto"),"nomStatusProcessoAto"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusProcessoAto.class));
		
	return statusProcessoAtoDAO.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto carregarStatusProcessoAto(StatusProcessoAto statusProcessoAto) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StatusProcessoAto.class)
			.add(Restrictions.eq("ideStatusProcessoAto", statusProcessoAto.getIdeStatusProcessoAto()));
	return statusProcessoAtoDAO.buscarPorCriteria(detachedCriteria);	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto getIdeStatusProcessoAtoByStatus(Integer ideControleProcessoAto)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ControleProcessoAto.class)
			.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
			.add(Restrictions.eq("ideControleProcessoAto", ideControleProcessoAto))
		
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideStatusProcessoAto.ideStatusProcessoAto"),"ideStatusProcessoAto"))		
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusProcessoAto.class));
	
		return statusProcessoAtoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcessoPossuiSolicitacaoAmbiental(Integer ideProcesso,Integer ideTipoSolicitacao)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("ideRequerimento", "ideRequerimento")
			.createAlias("ideRequerimento.solicitacaoAdministrativoCollection", "solicitacaoAdministrativo")
				
			.add(Restrictions.eq("solicitacaoAdministrativo.ideTipoSolicitacao.ideTipoSolicitacao", ideTipoSolicitacao))
			.add(Restrictions.eq("ideProcesso", ideProcesso));
			
		return !Util.isNullOuVazio(processoDAO.listarPorCriteria(detachedCriteria));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Processo> getHistNumProcesso(ProcessoAto pa)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
			.createAlias("ideProcessoTla", "ideProcesso")
			
			.add(Restrictions.eq("ideProcessoAto", pa))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcesso.numProcesso"),"numProcesso"))		
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class));
		
		return processoDAO.listarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTransferenciaComOrigem(Processo processo)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)				
			.createAlias("ideProcessoTla", "ideProcesso")
			.add(Restrictions.eq("ideProcesso.ideProcesso",processo.getIdeProcesso()))
			.add(Restrictions.isNull("ideProcessoAto"));
				
		return Util.isNullOuVazio(transferenciaAmbientaIDAO.listarPorCriteria(detachedCriteria));
	}
}













