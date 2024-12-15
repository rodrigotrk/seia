package br.gov.ba.seia.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.historico.suspensao.FiltroHistoricoSuspensao;
import br.gov.ba.seia.util.Util;



public class HistoricoSuspensaoCadastroDAOImpl implements HistoricoSuspensaoCadastroDAOIf {

	@Inject
	IDAO<HistoricoSuspensaoCadastro> histSuspensaoCadastroDAO;

	public IDAO<HistoricoSuspensaoCadastro> getHistSuspensaoCadastroDAO() {
		return histSuspensaoCadastroDAO;
	}

	public void setHistSuspensaoCadastroDAO(IDAO<HistoricoSuspensaoCadastro> histSuspensaoCadastroDAO) {
		this.histSuspensaoCadastroDAO = histSuspensaoCadastroDAO;
	}
	
	public HistoricoSuspensaoCadastro carregarTudo(final HistoricoSuspensaoCadastro suspensaoCadastro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoricoSuspensaoCadastro.class);
		criteria.add(Restrictions.eq("ideSuspensaoCadastro", suspensaoCadastro.getIdeSuspensaoCadastro()));
		return histSuspensaoCadastroDAO.buscarPorCriteria(criteria);
	}
	
	public List<FiltroHistoricoSuspensao> filtrarHistoricoSuspensaoCadastro(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first, int pageSize, Integer ...ides)  {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select hsc.dtc_suspensao_cadastro,pf.nom_pessoa, ms.des_motivo, hsc.num_notificacao_auto, hsc.dsc_observacao, pf.ide_pessoa_fisica, hsc.ide_suspensao_cadastro");
		sql.append(" from historico_suspensao_cadastro hsc");
		sql.append(" Inner join pessoa_fisica pf on hsc.ide_pessoa_fisica = pf.ide_pessoa_fisica");
		sql.append(" Inner join historico_motivo_suspensao hms on hsc.ide_suspensao_cadastro = hms.ide_suspensao_cadastro");
		sql.append(" Inner join motivo_suspensao ms on hms.ide_motivo_suspensao = ms.ide_motivo_suspensao");
		sql.append(" where 1=1");
		
		if(!Util.isNullOuVazio(dataInicio)) {
			sql.append(" and cast(hsc.dtc_suspensao_cadastro as date) >= cast('" + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + "' as date) ");
		}
		if(!Util.isNullOuVazio(dataFim)) {
			sql.append(" and cast(hsc.dtc_suspensao_cadastro as date) <= cast('" + new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + "' as date) ");
		}
		
		if(!Util.isNullOuVazio(ideImovelRural) && !Util.isNullOuVazio(ideImovelRural.getIdeImovelRural())) {
			sql.append(" and  hsc.ide_imovel_rural = "+ ideImovelRural.getIdeImovelRural());
		}
		
		sql.append(" order by hsc.dtc_suspensao_cadastro desc, hsc.num_notificacao_auto, pf.nom_pessoa, hsc.dsc_observacao, ms.ide_motivo_suspensao, pf.ide_pessoa_fisica, hsc.ide_suspensao_cadastro ");
		
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();

		lEntityManager.joinTransaction();
			
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());
		

		lQuery.setFirstResult(first);
		if(pageSize > 0) {
			lQuery.setMaxResults(pageSize);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = lQuery.getResultList();
		List<FiltroHistoricoSuspensao> listFiltro = new ArrayList<FiltroHistoricoSuspensao>();
		for (Object object[] : resultList) {
			FiltroHistoricoSuspensao filtro = new FiltroHistoricoSuspensao();
			
			if(!Util.isNullOuVazio(object[0])) {
				filtro.setDataSuspensao((Date) object[0]);
			}
			if(!Util.isNullOuVazio(object[1])) {
				filtro.setPessoa(new PessoaFisica(Integer.decode(object[5].toString()), object[1].toString()));
			}
			
			if(!Util.isNullOuVazio(object[2])) {
				filtro.setMotivo(object[2].toString());
			}
			
			if(!Util.isNullOuVazio(object[3])) {
				filtro.setNotificacaoOuAuto(object[3].toString());
			}
			
			if(!Util.isNullOuVazio(object[4])) {
				filtro.setObservacao(object[4].toString());
			}
			
			if(!Util.isNullOuVazio(object[6])) {
				filtro.setIdeSuspensaoCadastro(new Integer(object[6].toString()));
			}
			
			listFiltro.add(filtro);
		}
		return listFiltro;
	}
	
	public List<FiltroHistoricoSuspensao> visualizarSuspensaoCadastro(ImovelRural ideImovelRural)  {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select hsc.dtc_suspensao_cadastro,pf.nom_pessoa, ms.des_motivo, hsc.num_notificacao_auto, hsc.dsc_observacao, pf.ide_pessoa_fisica");
		sql.append(" from historico_suspensao_cadastro hsc");
		sql.append(" Inner join pessoa_fisica pf on hsc.ide_pessoa_fisica = pf.ide_pessoa_fisica");
		sql.append(" Inner join historico_motivo_suspensao hms on hsc.ide_suspensao_cadastro = hms.ide_suspensao_cadastro");
		sql.append(" Inner join motivo_suspensao ms on hms.ide_motivo_suspensao = ms.ide_motivo_suspensao");
		sql.append(" where hsc.dtc_retirada_suspensao is null");
		sql.append(" and  hsc.ide_imovel_rural = "+ ideImovelRural.getIdeImovelRural());
		sql.append(" order by hsc.dtc_suspensao_cadastro desc, hsc.num_notificacao_auto, pf.nom_pessoa, hsc.dsc_observacao, ms.ide_motivo_suspensao, pf.ide_pessoa_fisica ");
		
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();

		lEntityManager.joinTransaction();
			
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = lQuery.getResultList();
		List<FiltroHistoricoSuspensao> listFiltro = new ArrayList<FiltroHistoricoSuspensao>();
		List<MotivoSuspensaoCadastro> listMotivo = new ArrayList<MotivoSuspensaoCadastro>();

		for (Object object[] : resultList) {
			FiltroHistoricoSuspensao filtro = new FiltroHistoricoSuspensao();
			
			if(!Util.isNullOuVazio(object[0])) {
				filtro.setDataSuspensao((Date) object[0]);
			}
			if(!Util.isNullOuVazio(object[1])) {
				filtro.setPessoa(new PessoaFisica(Integer.decode(object[5].toString()), object[1].toString()));
			}
			if(!Util.isNullOuVazio(object[2])) {
				filtro.setMotivo(object[2].toString());
				MotivoSuspensaoCadastro motivo = new MotivoSuspensaoCadastro();
				motivo.setDesMotivo(object[2].toString());
				listMotivo.add(motivo);
			}
			
			if(!Util.isNullOuVazio(object[3])) {
				filtro.setNotificacaoOuAuto(object[3].toString());
			}
			
			if(!Util.isNullOuVazio(object[4])) {
				filtro.setObservacao(object[4].toString());
			}
			filtro.setListaMotivos(listMotivo);
			listFiltro.add(filtro);
		}
		return listFiltro;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistoricoSuspensaoCadastro obterHistoricoSuspensaoCadastroPorIdeImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoricoSuspensaoCadastro.class);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.add(Restrictions.isNull("dtcRetiradaSuspensao"));
		return histSuspensaoCadastroDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HistoricoSuspensaoCadastro> obterListaHistoricoSuspensaoCadastroPorIdeImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoricoSuspensaoCadastro.class);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		return histSuspensaoCadastroDAO.listarPorCriteria(criteria);
	}
	
	
	
}
