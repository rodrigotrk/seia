package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class RequerimentoUnicoDAOImpl implements RequerimentoUnicoDAOIf {

	@Inject
	IDAO<RequerimentoUnico> requerimentoUnicoDao;

	@Inject
	IDAO<TramitacaoRequerimento> tramitacaoRequerimentoDao;

	@Inject
	IDAO<ControleTramitacao> controleTramitacaoDao;

	@Inject
	IDAO<AtoAmbiental> atoAmbientalDao;

	@Inject
	IDAO<DadoGeografico> dadoGeograficoDAO;

	@Inject
	IDAO<Pessoa> pessoaDAO;


	@SuppressWarnings("unchecked")
	@Override
	public List<RequerimentoUnicoDTO> listaRequerimentoUnicoComParametro(RequerimentoUnico pRequerimentoUnico, Empreendimento empreendimentoRequerimento,
			Date pDataInicio, Date pDataFinal, StatusRequerimento statusRequerimento, Pessoa requerente, TipoPessoaRequerimento tipoPessoaRequerimento,
			List<Integer> idesPessoa)  {

		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico, pessoa, emp, status,lac ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		lSql.append("INNER JOIN req.requerimentoPessoaCollection req_pess ");
		lSql.append("INNER JOIN req_pess.ideTipoPessoaRequerimento tp_pess WITH tp_pess.ideTipoPessoaRequerimento = :ideTipoPessoaRequerimento ");
		lSql.append("INNER JOIN req_pess.pessoa pessoa ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("WHERE tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran) ");

		// Filtros
		if (!Util.isNullOuVazio(pRequerimentoUnico) && !Util.isNullOuVazio(pRequerimentoUnico.getRequerimento().getNumRequerimento())) {
			lSql.append(" AND req.numRequerimento LIKE :numRequerimento ");
		}
		if (!Util.isNull(pDataInicio) && !Util.isNull(pDataFinal)) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim ");
		}
		if (!Util.isNullOuVazio(empreendimentoRequerimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento ");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND pessoa.idePessoa = :idePessoa ");
		}
		if (!Util.isNullOuVazio(idesPessoa)) {
			lSql.append(" AND pessoa.idePessoa in (:idesPessoa) ");
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}

		lSql.append(" ORDER BY req.dtcCriacao");

		// Cria consulta hql
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		// seta parametros

		lQuery.setParameter("ideTipoPessoaRequerimento", tipoPessoaRequerimento.getIdeTipoPessoaRequerimento());

		if (!Util.isNullOuVazio(pRequerimentoUnico) && !Util.isNullOuVazio(pRequerimentoUnico.getRequerimento().getNumRequerimento())) {
			lQuery.setParameter("numRequerimento", "%" + pRequerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!Util.isNull(pDataInicio) && !Util.isNull(pDataFinal)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(pDataFinal);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			pDataFinal = gc.getTime();
			lQuery.setParameter("dataInicio", pDataInicio);
			lQuery.setParameter("dataFim", pDataFinal);
		}
		if (!Util.isNullOuVazio(empreendimentoRequerimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimentoRequerimento.getIdeEmpreendimento());
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("idePessoa", requerente.getIdePessoa());
		}
		if (!Util.isNullOuVazio(idesPessoa)) {
			lQuery.setParameter("idesPessoa", idesPessoa);
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}

		final List<RequerimentoUnicoDTO> lRetorno = new ArrayList<RequerimentoUnicoDTO>();

		// Obtem o resultado da consulta
		List<Object[]> result = lQuery.getResultList();

		// Converte retorno no DTO. -- [0]req_unico, [1]pessoa, [2]emp,
		// [3]status
		for (Object[] resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement[0];
			Pessoa lPessoa = (Pessoa) resultElement[1];
			Empreendimento lEmpreendimento = (Empreendimento) resultElement[2];
			StatusRequerimento lStatusRequerimento = (StatusRequerimento) resultElement[3];
			Lac lac = obterLac(resultElement);

			if (!Util.isNullOuVazio(lRequerimentoUnico.getIdeLocalizacaoGeografica())) {
				lRequerimentoUnico.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(buscarDadoGeograficoPorLocalizacaoGeografica(lRequerimentoUnico.getIdeLocalizacaoGeografica()));
			}
			lRetorno.add(new RequerimentoUnicoDTO(lRequerimentoUnico, lPessoa, lEmpreendimento, lStatusRequerimento, lac));
		}

		return lRetorno;
	}

	@Override
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorRequerente(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico, pessoa, emp, status,lac ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		lSql.append("INNER JOIN emp.idePessoa pessoa ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("WHERE req.indExcluido = :indExcluido ");

		lSql.append("AND tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lSql.append(" AND pessoa.idePessoa = :idePessoa");
		}

		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND pessoa.idePessoa = :ideRequerente");
		}
		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lSql.append(" AND req.numRequerimento LIKE :numRequerimento");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim");
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lQuery.setParameter("idePessoa", usuario.getIdePessoaFisica());
		}

		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lQuery.setParameter("numRequerimento", "%" + requerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFim);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			dataFim = gc.getTime();
			lQuery.setParameter("dataInicio", dataInicio);
			lQuery.setParameter("dataFim", dataFim);
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("ideRequerente", requerente.getIdePessoa());
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}

		final List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object[] resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement[0];
			Pessoa lPessoa = (Pessoa) resultElement[1];
			Empreendimento lEmpreendimento = (Empreendimento) resultElement[2];
			StatusRequerimento lStatusRequerimento = (StatusRequerimento) resultElement[3];
			Lac lac = obterLac(resultElement);

			if (!Util.isNullOuVazio(lRequerimentoUnico.getIdeLocalizacaoGeografica())) {
				lRequerimentoUnico.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(buscarDadoGeograficoPorLocalizacaoGeografica(lRequerimentoUnico.getIdeLocalizacaoGeografica()));
			}
			RequerimentoUnicoDTO requerimentoDTO = new RequerimentoUnicoDTO(lRequerimentoUnico, lPessoa, lEmpreendimento, lStatusRequerimento, lac);
			if (lRequerimentoUnico.getIndDla() != null && lRequerimentoUnico.getIndDla().equals(true)) {
				requerimentoDTO.setDla(true);
			} else {
				requerimentoDTO.setDla(false);
			}

			collRequerimentoUnicoDTO.add(requerimentoDTO);
		}

		return collRequerimentoUnicoDTO;
	}

	@Override
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorRepresentanteLegal(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico, pessoa, emp, status,lac ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		
		lSql.append("INNER JOIN emp.idePessoa pessoa ");
		lSql.append("INNER JOIN pessoa.pessoaJuridica pj ");
		lSql.append("INNER JOIN pj.representanteLegalCollection rl ");
		lSql.append("INNER JOIN rl.idePessoaFisica pf ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("WHERE tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		lSql.append("AND req.indExcluido = :indExcluido");

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lSql.append(" AND pf.idePessoaFisica = :idePessoa");
		}

		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" OR pessoa.idePessoa = :ideRequerente ");
		}
		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lSql.append(" AND req.numRequerimento LIKE :numRequerimento ");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim ");
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento ");
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lQuery.setParameter("idePessoa", usuario.getIdePessoaFisica());
		}

		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lQuery.setParameter("numRequerimento", "%" + requerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFim);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			dataFim = gc.getTime();
			lQuery.setParameter("dataInicio", dataInicio);
			lQuery.setParameter("dataFim", dataFim);
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("ideRequerente", requerente.getIdePessoa());
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}

		final List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object[] resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement[0];
			Pessoa lPessoa = (Pessoa) resultElement[1];
			Empreendimento lEmpreendimento = (Empreendimento) resultElement[2];
			StatusRequerimento lStatusRequerimento = (StatusRequerimento) resultElement[3];
			Lac lac = obterLac(resultElement);

			if (!Util.isNullOuVazio(lRequerimentoUnico.getIdeLocalizacaoGeografica())) {
				lRequerimentoUnico.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(buscarDadoGeograficoPorLocalizacaoGeografica(lRequerimentoUnico.getIdeLocalizacaoGeografica()));
			}

			RequerimentoUnicoDTO requerimentoDTO = new RequerimentoUnicoDTO(lRequerimentoUnico, lPessoa, lEmpreendimento, lStatusRequerimento, lac);
			if (lRequerimentoUnico.getIndDla() != null && lRequerimentoUnico.getIndDla().equals(true)) {
				requerimentoDTO.setDla(true);
			} else {
				requerimentoDTO.setDla(false);
			}

			collRequerimentoUnicoDTO.add(requerimentoDTO);
		}

		return collRequerimentoUnicoDTO;
	}

	@Override
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorProcuradorPessoaFisica(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico, pessoa, emp, status,lac ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		lSql.append("INNER JOIN emp.idePessoa pessoa ");
		lSql.append("INNER JOIN emp.procuradorPfEmpreendimentoCollection ppfe ");
		lSql.append("INNER JOIN ppfe.ideProcuradorPessoaFisica ppf ");
		lSql.append("INNER JOIN ppf.idePessoaFisica pf ");
		lSql.append("INNER JOIN ppf.ideProcurador pro ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("WHERE tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		lSql.append("AND req.indExcluido = :indExcluido");

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lSql.append(" AND pro.idePessoaFisica = :idePessoa");
		}

		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND pessoa.idePessoa = :ideRequerente ");
		}
		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lSql.append(" AND req.numRequerimento LIKE :numRequerimento ");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim ");
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento ");
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lQuery.setParameter("idePessoa", usuario.getIdePessoaFisica());
		}

		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lQuery.setParameter("numRequerimento", "%" + requerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFim);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			dataFim = gc.getTime();
			lQuery.setParameter("dataInicio", dataInicio);
			lQuery.setParameter("dataFim", dataFim);
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("ideRequerente", requerente.getIdePessoa());
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}

		final List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object[] resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement[0];
			Pessoa lPessoa = (Pessoa) resultElement[1];
			Empreendimento lEmpreendimento = (Empreendimento) resultElement[2];
			StatusRequerimento lStatusRequerimento = (StatusRequerimento) resultElement[3];
			Lac lac = obterLac(resultElement);

			if (!Util.isNullOuVazio(lRequerimentoUnico.getIdeLocalizacaoGeografica())) {
				lRequerimentoUnico.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(buscarDadoGeograficoPorLocalizacaoGeografica(lRequerimentoUnico.getIdeLocalizacaoGeografica()));
			}

			RequerimentoUnicoDTO requerimentoDTO = new RequerimentoUnicoDTO(lRequerimentoUnico, lPessoa, lEmpreendimento, lStatusRequerimento, lac);
			if (lRequerimentoUnico.getIndDla() != null && lRequerimentoUnico.getIndDla().equals(true)) {
				requerimentoDTO.setDla(true);
			} else {
				requerimentoDTO.setDla(false);
			}

			collRequerimentoUnicoDTO.add(requerimentoDTO);
		}

		return collRequerimentoUnicoDTO;
	}

	@Override
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorProcuradorPessoaJuridica(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico, pessoa, emp, status,lac ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		
		lSql.append("INNER JOIN emp.idePessoa pessoa ");
		lSql.append("INNER JOIN emp.procuradorRepEmpreendimentoCollection ppje ");
		lSql.append("INNER JOIN ppje.ideProcuradorRepresentante ppj ");
		lSql.append("INNER JOIN ppj.ideProcurador pro ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("WHERE tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		lSql.append("AND req.indExcluido = :indExcluido");

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lSql.append(" AND pro.idePessoaFisica = :idePessoa");
		}

		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND pessoa.idePessoa = :ideRequerente ");
		}
		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lSql.append(" AND req.numRequerimento LIKE :numRequerimento ");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim ");
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento ");
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (usuario != null && usuario.getIdePessoaFisica() != null) {
			lQuery.setParameter("idePessoa", usuario.getIdePessoaFisica());
		}

		if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento())) {
			lQuery.setParameter("numRequerimento", "%" + requerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFim);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			dataFim = gc.getTime();
			lQuery.setParameter("dataInicio", dataInicio);
			lQuery.setParameter("dataFim", dataFim);
		}
		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("ideRequerente", requerente.getIdePessoa());
		}
		if (!Util.isNullOuVazio(statusRequerimento)) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}

		final List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object[] resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement[0];
			Pessoa lPessoa = (Pessoa) resultElement[1];
			Empreendimento lEmpreendimento = (Empreendimento) resultElement[2];
			StatusRequerimento lStatusRequerimento = (StatusRequerimento) resultElement[3];
			Lac lac = obterLac(resultElement);

			if (!Util.isNullOuVazio(lRequerimentoUnico.getIdeLocalizacaoGeografica())) {
				lRequerimentoUnico.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(buscarDadoGeograficoPorLocalizacaoGeografica(lRequerimentoUnico.getIdeLocalizacaoGeografica()));
			}

			RequerimentoUnicoDTO requerimentoDTO = new RequerimentoUnicoDTO(lRequerimentoUnico, lPessoa, lEmpreendimento, lStatusRequerimento, lac);
			if (lRequerimentoUnico.getIndDla() != null && lRequerimentoUnico.getIndDla().equals(true)) {
				requerimentoDTO.setDla(true);
			} else {
				requerimentoDTO.setDla(false);
			}

			collRequerimentoUnicoDTO.add(requerimentoDTO);
		}

		return collRequerimentoUnicoDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RequerimentoUnicoDTO> listarRequerimentoUnico(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario, Integer first, Integer pageSize,Map<String, Object> params)
					 {

		List<Object[]> lista = getResultListConsultaRequerimento(first, pageSize, requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, false, null, true,params).getResultList();
		List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();


		for (Object[] resultElement : lista) {
			collRequerimentoUnicoDTO.add(new RequerimentoUnicoDTO(resultElement));
		}

		List<AtoAmbiental> atosAmbientais = getListaAtosEnquadrados(collRequerimentoUnicoDTO);
		for (RequerimentoUnicoDTO requerimentoUnicoDTO : collRequerimentoUnicoDTO) {
			preencheAtosAmbientais(atosAmbientais, requerimentoUnicoDTO.getRequerimentoUnico());
		}

		return collRequerimentoUnicoDTO;

	}

	private void preencheAtosAmbientais(List<AtoAmbiental> atosAmbientais, RequerimentoUnico req) {
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			if(req.equals(atoAmbiental.getEnquadramentoRequerimento().getIdeRequerimentoUnico())){
				req.getAtosAmbientais().add(atoAmbiental);
			}
		}
	}

	private List<AtoAmbiental> getListaAtosEnquadrados(List<RequerimentoUnicoDTO> lista)  {

		List<Integer> ides = getIdesDTo(lista);

		if (ides.isEmpty()) {
			return new ArrayList<AtoAmbiental>();
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoCollection","enq")
				.createAlias("enq.ideRequerimentoUnico", "req")
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
						.add(Projections.property("indDeclaratorio"),"indDeclaratorio")
						.add(Projections.property("sglAtoAmbiental"),"sglAtoAmbiental")
						.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
						.add(Projections.property("req.ideRequerimentoUnico"),"enquadramentoRequerimento.ideRequerimentoUnico.ideRequerimentoUnico"));

		criteria.add(Restrictions.in("req.ideRequerimentoUnico", ides));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		return this.atoAmbientalDao.listarPorCriteria(criteria);
	}


	private List<Integer> getIdesDTo(List<RequerimentoUnicoDTO> lista) {
		List<Integer> ides = new ArrayList<Integer>();
		for (RequerimentoUnicoDTO req : lista) {
			ides.add(req.getRequerimentoUnico().getIdeRequerimentoUnico());
		}
		return ides;
	}


	@Override
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoExterno(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim,
			Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario, List<Integer> listaPessoas,
			Integer first, Integer pageSize,Map<String, Object> params)  {

		if (!Util.isNullOuVazio(requerente)) {
			listaPessoas.add(requerente.getIdePessoa()); /** sempre adiciona o requerente para lista de pessoas aptas.*/
		}

		@SuppressWarnings("unchecked")
		List<Object[]> lista = getResultListConsultaRequerimento(first, pageSize, requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, false, listaPessoas, true,params).getResultList();
		List<RequerimentoUnicoDTO> collRequerimentoUnicoDTO = new ArrayList<RequerimentoUnicoDTO>();

		for (Object[] resultElement : lista) {
			collRequerimentoUnicoDTO.add(new RequerimentoUnicoDTO(resultElement));
		}

		List<AtoAmbiental> atosAmbientais = getListaAtosEnquadrados(collRequerimentoUnicoDTO);
		for (RequerimentoUnicoDTO requerimentoUnicoDTO : collRequerimentoUnicoDTO) {
			preencheAtosAmbientais(atosAmbientais, requerimentoUnicoDTO.getRequerimentoUnico());
		}

		return collRequerimentoUnicoDTO;

	}

	@Override
	public Integer countListarRequerimentoUnicoExterno(int first,int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario, List<Integer> listaPessoas,Map<String, Object> params)  {

		if (!Util.isNullOuVazio(requerente)) {
			listaPessoas.add(requerente.getIdePessoa());
		}

		Long qtdRequerimentos = (Long) getResultListConsultaRequerimento(first,pageSize,requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios,true,listaPessoas,false,params).getSingleResult();
		return  qtdRequerimentos.intValue();

	}

	@Override
	public Integer countListarRequerimentoUnico(int first, int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento, Municipio municipio,List<Municipio> municipios, Usuario usuario,Map<String, Object> params)  {

		Long qtdRequerimentos = (Long) getResultListConsultaRequerimento(first,pageSize,requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios,true,null,false,params).getSingleResult();
		return  qtdRequerimentos.intValue();
	}

	@SuppressWarnings("all")
	private List<Integer> getRequerimentoIdes(int first, int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento, Municipio municipio,ArrayList<Municipio> municipios,List<Integer> listaPessoas,boolean paginando,Map<String, Object> params){
		List<Integer> resultList = getResultListConsultaRequerimento(first,pageSize,requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios,false,listaPessoas,paginando,params).getResultList();

		return  resultList;
	}

	@SuppressWarnings("unchecked")
	private Query getResultListConsultaRequerimento(int first, int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, boolean isCount,List<Integer> listaPessoas,boolean paginando,Map<String, Object> params) {

		boolean noRequerente = Util.isNullOuVazio(requerente);
		boolean noNumRequerimento = !Util.isNullOuVazio(requerimentoUnico) && Util.isNullOuVazio(requerimentoUnico.getRequerimento().getNumRequerimento());
		boolean noData = Util.isNull(dataInicio) && Util.isNull(dataFim);
		boolean noEmpreendimento = Util.isNullOuVazio(empreendimento);
		boolean noStatus = Util.isNullOuVazio(statusRequerimento);
		boolean noTipoAto = Util.isNullOuVazio(params.get("TIPOATO"));
		boolean noMunicipio = Util.isNullOuVazio(municipio);
		boolean noMunicipios = Util.isNullOuVazio(municipios);
		boolean noPorte = Util.isNullOuVazio(requerimentoUnico.getIdePorte());



		StringBuilder lSql = new StringBuilder();

		boolean semFiltro = noRequerente && noNumRequerimento && noData && noEmpreendimento && noStatus && noTipoAto && noMunicipio && noMunicipios && noPorte;
		if(isCount && semFiltro && Util.isNullOuVazio(listaPessoas)){
			return getCountQuery();
		}else if(isCount){
			lSql.append("SELECT count(distinct req_unico.ideRequerimentoUnico)");
		}else{
			lSql.append("SELECT distinct req_unico.ideRequerimentoUnico,req_unico.indDla,req.dtcCriacao, ");
			lSql.append("req.numRequerimento,req.ideRequerimento,orgao.ideOrgao,orgao.codOrgao,pessoa.idePessoa,pj.nomRazaoSocial,pf.nomPessoa,");
			lSql.append("emp.ideEmpreendimento,emp.nomEmpreendimento,emp.indConversaoTcraLac,status,lac.ideLac,pTra.idePessoa,pTraF.nomPessoa,pTraJ.nomRazaoSocial," +
					"lac.ideDocumentoObrigatorio.ideDocumentoObrigatorio,porte.idePorte,porte.nomPorte,coalesce(pj.numCnpj,pf.numCpf) as documento, ");
			lSql.append(" lm.ideMunicipio as ide_lm,lm.nomMunicipio as nom_lm,lm.indEstadoEmergencia as emergencia_lm ,bm.ideMunicipio as ide_bm,bm.nomMunicipio as nom_bm,bm.indEstadoEmergencia as emergencia_mb ,req.indEstadoEmergencia as emergencia_req, fce.ideFce, fce.ideDocumentoObrigatorio.ideDocumentoObrigatorio ");

		}

		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.ideOrgao orgao ");
		lSql.append("INNER JOIN req.requerimentoPessoaCollection reqPessoa ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.idePessoa pTra ");
		lSql.append("left JOIN pTra.pessoaFisica pTraF ");
		lSql.append("left JOIN pTra.pessoaJuridica pTraJ ");
		lSql.append("left JOIN req_unico.idePorte porte ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		lSql.append("INNER JOIN emp.enderecoEmpreendimentoCollection endem ");
		lSql.append("INNER JOIN endem.ideEndereco ende ");
		lSql.append("INNER JOIN ende.ideLogradouro log ");
		lSql.append("LEFT JOIN log.ideMunicipio lm ");
		lSql.append("LEFT JOIN log.ideBairro br ");
		lSql.append("LEFT JOIN br.ideMunicipio bm ");
		lSql.append("INNER JOIN reqPessoa.pessoa pessoa ");
		lSql.append("left JOIN pessoa.pessoaFisica pf ");
		lSql.append("left JOIN pessoa.pessoaJuridica pj ");
		lSql.append("left JOIN req.lac lac ");
		lSql.append("left JOIN req.fce fce ");
		if(!noTipoAto){
			lSql.append("INNER JOIN req_unico.enquadramento enquadramento ");
			lSql.append("INNER JOIN enquadramento.atoAmbientalCollection atoAmbientalCollection ");
		}
		lSql.append("WHERE req.numRequerimento is not null and tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran) ");
		lSql.append(" AND req.indExcluido = :indExcluido");
		lSql.append(" AND reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");


		if(!noPorte){
			lSql.append(" AND req_unico.idePorte = :idePorte");
		}
		if (!noRequerente) {
			lSql.append(" AND reqPessoa.pessoa.idePessoa = :ideRequerente");
		}
		if (!noNumRequerimento) {
			lSql.append(" AND lower(req.numRequerimento) LIKE lower(:numRequerimento)");
		}
		if (!noData) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dataInicio AND :dataFim");
		}
		if (!noEmpreendimento) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}
		if (!noStatus) {
			lSql.append(" AND status.ideStatusRequerimento = :ideStatusRequerimento");
		}
		if(!noMunicipios){
			lSql.append(" AND ((lm in (:listaMunicipios) OR bm in (:listaMunicipios)) )" );
		}else if (!noMunicipio) {
			lSql.append(" AND (lm = :ideMunicipio OR bm = :ideMunicipio )" );
		}

		lSql.append(" AND endem.ideTipoEndereco = 4 ");

		if(listaPessoas != null){
			lSql.append( " AND reqPessoa.pessoa.idePessoa in (:listaPessoas)");
		}

		if(!noTipoAto){
			lSql.append(" AND atoAmbientalCollection.ideAtoAmbiental in (:listaAtos)");
		}

		if(!isCount && paginando){
			lSql.append(" order by req.ideRequerimento desc ");
		}



		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		if(!isCount && paginando){
			lQuery.setFirstResult(first);
			lQuery.setMaxResults(pageSize);
		}

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (!noPorte) {
			lQuery.setParameter("idePorte",  requerimentoUnico.getIdePorte());
		}

		if (!noNumRequerimento) {
			lQuery.setParameter("numRequerimento", "%" + requerimentoUnico.getRequerimento().getNumRequerimento() + "%");
		}
		if (!noData) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataFim);
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
			dataFim = gc.getTime();
			lQuery.setParameter("dataInicio", dataInicio);
			lQuery.setParameter("dataFim", dataFim);
		}
		if (!noEmpreendimento) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}
		if (!noRequerente) {
			lQuery.setParameter("ideRequerente", requerente.getIdePessoa());
		}
		if (!noStatus) {
			lQuery.setParameter("ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento());
		}
		if(!noMunicipios) {
			lQuery.setParameter("listaMunicipios", municipios);
		}else if (!noMunicipio) {
			lQuery.setParameter("ideMunicipio", municipio);
		}

		if(listaPessoas != null){
			lQuery.setParameter("listaPessoas", listaPessoas);
		}

		if(!noTipoAto){

			if(!Util.isNullOuVazio(params.get("LISTAATOS"))){
				List<Integer>lista = new ArrayList<Integer>();
				for (AtoAmbiental ato : (List<AtoAmbiental>) params.get("LISTAATOS")) {
					lista.add(ato.getIdeAtoAmbiental());
				}
				lQuery.setParameter("listaAtos", lista);
			}
			else{
				lQuery.setParameter("listaAtos", ((AtoAmbiental)params.get("ATO")).getIdeAtoAmbiental());
			}

		}

		return lQuery;
	}

	private Query getCountQuery() {

		StringBuilder lSql = new StringBuilder();

		lSql.append("SELECT  count(req_unico.ideRequerimentoUnico)");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.requerimentoPessoaCollection reqPessoa ");
		lSql.append(" WHERE req.indExcluido = :indExcluido");
		lSql.append(" AND reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		return lQuery;
	}

	@Override
	public Boolean listarRequerimentoUnicoAbertos(Empreendimento empreendimento)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req_unico ");
		lSql.append("FROM RequerimentoUnico req_unico ");
		lSql.append("INNER JOIN req_unico.requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		
		lSql.append("WHERE req.indExcluido = :indExcluido and req.numRequerimento is not null ");

		lSql.append("AND tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}

		lSql.append(" AND status.ideStatusRequerimento not in (8, 9)");
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}

		final List<RequerimentoUnico> collRequerimentoUnico = new ArrayList<RequerimentoUnico>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object resultElement : result) {
			RequerimentoUnico lRequerimentoUnico = (RequerimentoUnico) resultElement;
			collRequerimentoUnico.add(lRequerimentoUnico);
		}

		return !collRequerimentoUnico.isEmpty();
	}

	/**
	 *  Método para listar todos os requerimentos associados a um empreendimento
	 *  @param EmpreendimentoEntrada object
	 *  @return um <code>Boolean</code> especificando a existência ou não de requerimentos associados ao empreendimento
	 */
	@Override
	public Boolean listarRequerimentosAssociadosEmpreendimento(Empreendimento empreendimento)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req ");
		lSql.append("FROM Requerimento req ");
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tra ");
		lSql.append("WHERE req.numRequerimento is not null ");


		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}

		lSql.append(" AND tra.ideTramitacaoRequerimento = (select max(ideTramitacaoRequerimento) from TramitacaoRequerimento where ideRequerimento = req.ideRequerimento ) and tra.ideStatusRequerimento <> 9");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());


		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}

		final List<Requerimento> collRequerimento = new ArrayList<Requerimento>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object resultElement : result) {
			Requerimento lRequerimentoUnico = (Requerimento) resultElement;
			collRequerimento.add(lRequerimentoUnico);
		}

		return !collRequerimento.isEmpty();
	}

	private Lac obterLac(Object[] resultElement) {
		Lac lac = null;
		if (resultElement.length > 4) {
			lac = (Lac) resultElement[4];
		}
		return lac;
	}



	@Override
	public RequerimentoUnico recuperarUltimoRequerimentoUnicoProcessado(Empreendimento empreendimento)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("select ru.* from requerimento_unico ru " +
				"inner join requerimento r on (ru.ide_requerimento_unico = r.ide_requerimento) " +
				"inner join empreendimento_requerimento er on (er.ide_requerimento = r.ide_requerimento) " +
				"inner join (select * from tramitacao_requerimento trs where ide_status_requerimento in (8)) as tr " +
				"on (r.ide_requerimento = tr.ide_requerimento) " +
				"where er.ide_empreendimento = :ideEmpreendimento and r.ind_excluido = :indExcluido order by tr.dtc_movimentacao desc limit 1");
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString(), RequerimentoUnico.class);

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}

		RequerimentoUnico lRequerimentoUnico = null;
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object resultElement : result) {
			lRequerimentoUnico = (RequerimentoUnico) resultElement;
		}

		return lRequerimentoUnico;
	}

	@Override
	public RequerimentoUnico recuperarRequerimentoUnicoProcessoFormadoAnterior(Empreendimento empreendimento)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("select ru.* from requerimento_unico ru " +
				"inner join requerimento r on (ru.ide_requerimento_unico = r.ide_requerimento) " +
				"inner join empreendimento_requerimento er on (er.ide_requerimento = r.ide_requerimento) " +
				"inner join (select * from tramitacao_requerimento trs where ide_status_requerimento in (8)) as tr " +
				"on (r.ide_requerimento = tr.ide_requerimento) " +
				"where er.ide_empreendimento = :ideEmpreendimento and r.ind_excluido = :indExcluido order by tr.dtc_movimentacao desc limit 1");

		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createNativeQuery(lSql.toString(), RequerimentoUnico.class);

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}

		RequerimentoUnico lRequerimentoUnico = null;
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object resultElement : result) {
			lRequerimentoUnico = (RequerimentoUnico) resultElement;
		}

		return lRequerimentoUnico;
	}

	public Collection<DadoGeografico> buscarDadoGeograficoPorLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		return dadoGeograficoDAO.buscarPorNamedQuery("DadoGeografico.findByIdeLocalizacaoGeografica", parametros);
	}


}