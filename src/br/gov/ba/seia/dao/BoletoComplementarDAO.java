package br.gov.ba.seia.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.dto.BoletoComplementarDTO;
import br.gov.ba.seia.dto.BoletoComplementarFilter;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.util.Util;

/**
 * Classe de persistencia do boleto complementar
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 18/11/2013
 */
public class BoletoComplementarDAO {
	
	/**
	 * Consulta que retorna os boletos cadastradas com seus respectivos historicos de datas, status e valores.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param filter - Filtro da tela de consulta de boleto complementar
	 * @param first - Usado pela paginacao para definir a pagina atual
	 * @param pageSize - Usado pela paginacao para definir a quantida maxima de registros por pagina
	 * @param usuarioExterno - Modifica o filtro de requerente na query quando o usuario e externo
	 * @return List de array de {@link Object} que deve ser convertida em uma List<{@link BoletoComplementarDTO}>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consultarBoletoComplementar(BoletoComplementarFilter filter, int first, int pageSize, Boolean usuarioExterno) {

		Integer idStatus = null;
		Date dtInicial = null;
		Date dtFinal = null;
		int cont = 0;
		
		StringBuilder lSql = new StringBuilder();
		
		lSql.append("select * from ( ");
		
		if(filter.isStatusSelecionado()) {
			
			if(filter.isEmProcessamento()) {
				idStatus = StatusBoletoEnum.EM_PROCESSAMENTO.getId();
				
				if(!Util.isNullOuVazio(filter.getDtEmProcessamentoInicial())) {
					dtInicial = filter.getDtEmProcessamentoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtEmProcessamentoFinal())) {
					dtFinal = filter.getDtEmProcessamentoFinal();
				}
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
		
			if(filter.isEmitido()) {
				idStatus = StatusBoletoEnum.EMITIDO.getId();

				if(!Util.isNullOuVazio(filter.getDtEmitidoInicial())) {
					dtInicial = filter.getDtEmitidoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtEmitidoFinal())) {
					dtFinal = filter.getDtEmitidoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isCancelamento()) {
				idStatus = StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId();

				if(!Util.isNullOuVazio(filter.getDtCancelamentoInicial())) {
					dtInicial = filter.getDtCancelamentoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtCancelamentoFinal())) {
					dtFinal = filter.getDtCancelamentoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isComprovante()) {
				idStatus = StatusBoletoEnum.COMPROVANTE.getId();

				if(!Util.isNullOuVazio(filter.getDtComprovanteInicial())) {
					dtInicial = filter.getDtComprovanteInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtComprovanteFinal())) {
					dtFinal = filter.getDtComprovanteFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isPago()) {
				idStatus = StatusBoletoEnum.PAGO.getId();

				if(!Util.isNullOuVazio(filter.getDtPagoInicial())) {
					dtInicial = filter.getDtPagoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtPagoFinal())) {
					dtFinal = filter.getDtPagoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isCancelado()) {
				idStatus = StatusBoletoEnum.CANCELADO.getId();

				if(!Util.isNullOuVazio(filter.getDtCanceladoInicial())) {
					dtInicial = filter.getDtCanceladoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtCanceladoFinal())) {
					dtFinal = filter.getDtCanceladoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isVencido()) {
				idStatus = StatusBoletoEnum.VENCIDO.getId();

				if(!Util.isNullOuVazio(filter.getDtVencidoInicial())) {
					dtInicial = filter.getDtVencidoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtVencidoFinal())) {
					dtFinal = filter.getDtVencidoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isPendenciaValidacaoComprovante()) {
				idStatus = StatusBoletoEnum.PENDENCIA_VALIDACAO.getId();
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, null, null, usuarioExterno));
				
				cont++;
			}
		} else {
			lSql.append(queryConsultarBoletoComplementar(filter, null, null, null, usuarioExterno));
		}		
		
		lSql.append(") as tbInterna order by DT_GERACAO DESC ");
		
		lSql.append("OFFSET "+ first + " LIMIT " + pageSize + ";");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		return lQuery.getResultList();
	}
	
	/**
	 * Consulta que informa a quantidade total de registros retornados pela query do metodo {@link BoletoComplementarDAO#consultarBoletoComplementar(BoletoComplementarFilter, int, int)}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param filter - Filtro da tela de consulta de boleto complementar
	 * @param usuarioExterno - Modifica o filtro de requerente na query quando o usuario e externo
	 * @return Quantidade total de boletos para aquela consulta 
	 */
	public Integer consultarBoletoComplementarCount(BoletoComplementarFilter filter, Boolean usuarioExterno) {

		Integer idStatus = null;
		Date dtInicial = null;
		Date dtFinal = null;
		int cont = 0;
		
		StringBuilder lSql = new StringBuilder();

		lSql.append("select COUNT(*) FROM ( ");
		
		if(filter.isStatusSelecionado()) {
			
			if(filter.isEmProcessamento()) {
				idStatus = StatusBoletoEnum.EM_PROCESSAMENTO.getId();

				if(!Util.isNullOuVazio(filter.getDtEmProcessamentoInicial())) {
					dtInicial = filter.getDtEmProcessamentoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtEmProcessamentoFinal())) {
					dtFinal = filter.getDtEmProcessamentoFinal();
				}
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isEmitido()) {
				idStatus = StatusBoletoEnum.EMITIDO.getId();

				if(!Util.isNullOuVazio(filter.getDtEmitidoInicial())) {
					dtInicial = filter.getDtEmitidoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtEmitidoFinal())) {
					dtFinal = filter.getDtEmitidoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isCancelamento()) {
				idStatus = StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getId();

				if(!Util.isNullOuVazio(filter.getDtCancelamentoInicial())) {
					dtInicial = filter.getDtCancelamentoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtCancelamentoFinal())) {
					dtFinal = filter.getDtCancelamentoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isComprovante()) {
				idStatus = StatusBoletoEnum.COMPROVANTE.getId();

				if(!Util.isNullOuVazio(filter.getDtComprovanteInicial())) {
					dtInicial = filter.getDtComprovanteInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtComprovanteFinal())) {
					dtFinal = filter.getDtComprovanteFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isPago()) {
				idStatus = StatusBoletoEnum.PAGO.getId();

				if(!Util.isNullOuVazio(filter.getDtPagoInicial())) {
					dtInicial = filter.getDtPagoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtPagoFinal())) {
					dtFinal = filter.getDtPagoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isCancelado()) {
				idStatus = StatusBoletoEnum.CANCELADO.getId();

				if(!Util.isNullOuVazio(filter.getDtCanceladoInicial())) {
					dtInicial = filter.getDtCanceladoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtCanceladoFinal())) {
					dtFinal = filter.getDtCanceladoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			
			if(filter.isVencido()) {
				idStatus = StatusBoletoEnum.VENCIDO.getId();

				if(!Util.isNullOuVazio(filter.getDtVencidoInicial())) {
					dtInicial = filter.getDtVencidoInicial();
				}
				
				if(!Util.isNullOuVazio(filter.getDtVencidoFinal())) {
					dtFinal = filter.getDtVencidoFinal();
				}
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, dtInicial, dtFinal, usuarioExterno));
				
				cont++;
			}
			if(filter.isPendenciaValidacaoComprovante()) {
				idStatus = StatusBoletoEnum.PENDENCIA_VALIDACAO.getId();
				
				if(cont > 0) lSql.append(" UNION ALL ");
				
				lSql.append(queryConsultarBoletoComplementar(filter, idStatus, null, null, usuarioExterno));
				
				cont++;
			}
		} else {
			
			lSql.append(queryConsultarBoletoComplementar(filter, null, null, null, usuarioExterno));
		}
		
		lSql.append(") as tabInterna ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		return ((BigInteger) lQuery.getSingleResult()).intValue();
	}
	
	/**
	 * Query principal da consulta de boleto.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 *
	 * @param filter - Filtro da tela de consulta de boleto complementar.
	 * @param idStatus - Filtro com o id do status selecionado na tela.
	 * @param dtInicial - Filtro com a data inicial relacionada ao status escolhido.
	 * @param dtFinal - Filtro com a data final relacionada ao status escolhido.
	 * @param usuarioExterno - Modifica o filtro de requerente na query quando o usuario e externo
	 * @return StringBuilder com o principal da query montada.
	 */
	public StringBuilder queryConsultarBoletoComplementar(BoletoComplementarFilter filter, Integer idStatus, Date dtInicial, Date dtFinal, Boolean usuarioExterno) {
		
		StringBuilder lSql = new StringBuilder();
		
		//BLOCO REQUERIMENTO
		
		lSql.append("select distinct ");
		lSql.append("bpr.ide_boleto_pagamento_requerimento as ID_BOLETO, ");
		
		lSql.append("(CASE "
				+ "WHEN pf.nom_pessoa IS NOT NULL THEN pf.nom_pessoa "
				+ "ELSE pj.nom_razao_social END) as NOME_REQUERENTE, ");
		
		lSql.append("bpr.num_boleto as NUM_BOLETO, ");
		
		lSql.append("(select bph1.dtc_tramitacao "
				+ "from boleto_pagamento_historico bph1 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph1.ide_boleto_pagamento and bph1.ide_status_boleto_pagamento = 1 "
				+ "order by bph1.ide_boleto_pagamento_historico limit 1) as DT_GERACAO, ");
		
		lSql.append("bpr.dtc_vencimento as DT_VENCIMENTO, ");
		
		lSql.append("(select sta.nom_status_boleto_pagamento "
				+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
				+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento "
				+ "and bph2.ide_boleto_pagamento_historico = (select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) as NOME_STATUS, ");
		
		lSql.append("(select max(bph4.dtc_tramitacao) "
				+ "from boleto_pagamento_historico bph4 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph4.ide_boleto_pagamento and ((bph4.ide_status_boleto_pagamento = 3) OR (bph4.ide_status_boleto_pagamento = 5))) as DT_PAGAMENTO_CANCELAMENTO, ");
		
		
		lSql.append("(select max(bph5.dtc_tramitacao) "
				+ "from boleto_pagamento_historico bph5 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph5.ide_boleto_pagamento and bph5.ide_status_boleto_pagamento = 4) as DT_VALIDACAO, ");

		lSql.append("bpr.val_boleto as VALOR, ");
		
		lSql.append("bpr.ide_requerimento as ID_REQUERIMENTO, ");
		
		lSql.append("req.num_requerimento as NUM_REQUERIMENTO, ");
		
		lSql.append("bpr.ide_processo as ID_PROCESSO, ");
		
		lSql.append("proc.num_processo as NUM_PROCESSO, ");
		
		lSql.append("(select nom_tipo_boleto_pagamento from tipo_boleto_pagamento where ide_tipo_boleto_pagamento = BPR.ide_tipo_boleto_pagamento) as NOM_TIPO_BOLETO, ");
		
		lSql.append("0.0 AS valorTotalCertificado, ");
		lSql.append("0.0 AS valorTotalVistoria, ");
		
		lSql.append("bpr.ide_processo_reenquadramento AS ideProcessoReenquadramento, ");
		lSql.append("bpr.ind_boleto_registrado as indBoletoRegistrado ");
		
		lSql.append("from boleto_pagamento_requerimento BPR "); //BOLETO PAGAMENTO REQUERIMENTO
		lSql.append("inner join requerimento REQ on bpr.ide_requerimento = req.ide_requerimento "); //REQUERIMENTO
		lSql.append("left join processo PROC on bpr.ide_requerimento = proc.ide_requerimento "); //PROCESSO
		lSql.append("inner join requerimento_pessoa REQ_PES on req.ide_requerimento = REQ_PES.ide_requerimento  "); //REQUERIMENTO_PESSOA
		lSql.append("inner join pessoa PES on REQ_PES.ide_pessoa = PES.ide_pessoa "); //PESSOA
		lSql.append("left join pessoa_fisica PF on PES.ide_pessoa = PF.ide_pessoa_fisica "); //PESSOA FISICA
		lSql.append("left join pessoa_juridica PJ on PES.ide_pessoa = PJ.ide_pessoa_juridica "); //PESSOA JURIDICA
		lSql.append("inner join boleto_pagamento_historico BPH0 on bpr.ide_boleto_pagamento_requerimento = bph0.ide_boleto_pagamento "); //HISTORICO
		lSql.append("inner join status_boleto_pagamento STA0 on bph0.ide_status_boleto_pagamento = sta0.ide_status_boleto_pagamento "); //STATUS
		
		lSql.append("where REQ_PES.ide_tipo_pessoa_requerimento = 1 and BPR.ide_tipo_boleto_pagamento != 1 ");
		
		if(!Util.isNullOuVazio(filter)) {
			
			if(usuarioExterno) {
				
				if(!Util.isNullOuVazio(filter.getListPF()) && !Util.isNullOuVazio(filter.getListPJ())) {
					
					//ID dos Requerentes - Pessoa Fisica
					lSql.append("and (");
						lSql.append("pf.ide_pessoa_fisica in ( ");
							lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
							
							for (int i = 1; i < filter.getListPF().size(); i++) {
								
								lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
							}
						lSql.append(") ");
					
						//ID dos Requerentes - Pessoa Juridica
						lSql.append("OR pj.ide_pessoa_juridica in (");
							lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
							
							for (int i = 1; i < filter.getListPJ().size(); i++) {
								
								lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
							}
						lSql.append(") ");
					lSql.append(") ");
					
				} else {
					
					//ID dos Requerentes - Pessoa Fisica
					if(!Util.isNullOuVazio(filter.getListPF())) {
						
						lSql.append("and pf.ide_pessoa_fisica in ("); 
						
						lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
						
						for (int i = 1; i < filter.getListPF().size(); i++) {
							
							lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
						}
						
						lSql.append(") ");
					}
					
					//ID dos Requerentes - Pessoa Juridica
					if(!Util.isNullOuVazio(filter.getListPJ())) {
						
						lSql.append("and pj.ide_pessoa_juridica in (");
						
						lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
						
						for (int i = 1; i < filter.getListPJ().size(); i++) {
							
							lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
						}
						
						lSql.append(") ");
					}
				}
				
			} else {
				
				if(!Util.isNullOuVazio(filter.getRequerente())) {
					
					//ID do Requerente - Pessoa Fisica
					if(!Util.isNullOuVazio(filter.getRequerente().getPessoaFisica())){
						
						lSql.append("and pf.ide_pessoa_fisica = " + filter.getRequerente().getPessoaFisica().getIdePessoaFisica() + " ");
					}
					
					//ID do Requerente - Pessoa Juridica
					if(!Util.isNullOuVazio(filter.getRequerente().getPessoaJuridica())) {
						
						lSql.append("and pj.ide_pessoa_juridica = " + filter.getRequerente().getPessoaJuridica().getIdePessoaJuridica() + " ");
					}
				}
			}
			
			//id do boleto
			if(!Util.isNullOuVazio(filter.getIdeProcessoReenquadramento())) {
				
				lSql.append("and BPR.ide_processo_reenquadramento = " + filter.getIdeProcessoReenquadramento() + " ");
			}
			
			//Numero do Requerimento
			if(!Util.isNullOuVazio(filter.getNumRequerimento())) {
				
				lSql.append("and req.num_requerimento like '%" + filter.getNumRequerimento() + "%' ");
			}
			
			//Numero do Processo
			if(!Util.isNullOuVazio(filter.getNumProcesso())) {
				
				lSql.append("and proc.num_processo like '%" + filter.getNumProcesso() + "%' ");
			}
			
			//Numero do Boleto
			if(!Util.isNullOuVazio(filter.getNumBoleto())) {
				
				lSql.append("and bpr.num_boleto like '%" + filter.getNumBoleto() + "%' ");
			}
			
			//Filtro do Status e Data
			if(filter.isStatusSelecionado()) {
				if(!Util.isNullOuVazio(idStatus)) {
					
					lSql.append(" and (select sta.ide_status_boleto_pagamento "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) = " + idStatus + " ");
				}
				
				if(!Util.isNullOuVazio(dtInicial)) {
					
					lSql.append(" and (select bph2.dtc_tramitacao "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) >= '" + dtInicial + "' ");
				}
				
				if(!Util.isNullOuVazio(dtFinal)) {
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime(dtFinal);
					calendar.add(GregorianCalendar.HOUR, 23);
					calendar.add(GregorianCalendar.MINUTE, 55);
					dtFinal = calendar.getTime();
					
					lSql.append(" and (select bph2.dtc_tramitacao "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) <= '" + dtFinal + "' ");
				}
			}
		}
		
		
		
		
		
		lSql.append(" UNION ALL ");
		
		
		
		
		
		// BLOCO PROCESSO
		
		lSql.append("select distinct ");
		lSql.append("bpr.ide_boleto_pagamento_requerimento as ID_BOLETO, ");
		
		lSql.append("(CASE "
				+ "WHEN pf.nom_pessoa IS NOT NULL THEN pf.nom_pessoa "
				+ "ELSE pj.nom_razao_social END) as NOME_REQUERENTE, ");
		
		lSql.append("bpr.num_boleto as NUM_BOLETO, ");
		
		lSql.append("(select bph1.dtc_tramitacao "
				+ "from boleto_pagamento_historico bph1 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph1.ide_boleto_pagamento and bph1.ide_status_boleto_pagamento = 1 "
				+ "order by  bph1.ide_boleto_pagamento_historico limit 1) as DT_GERACAO, ");
		
		lSql.append("bpr.dtc_vencimento as DT_VENCIMENTO, ");
		
		lSql.append("(select sta.nom_status_boleto_pagamento "
				+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
				+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento "
				+ "and bph2.ide_boleto_pagamento_historico = (select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) as NOME_STATUS, ");
		
		lSql.append("(select max(bph4.dtc_tramitacao) "
				+ "from boleto_pagamento_historico bph4 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph4.ide_boleto_pagamento and ((bph4.ide_status_boleto_pagamento = 3) OR (bph4.ide_status_boleto_pagamento = 5))) as DT_PAGAMENTO_CANCELAMENTO, ");
		
		
		lSql.append("(select max(bph5.dtc_tramitacao) "
				+ "from boleto_pagamento_historico bph5 "
				+ "where bpr.ide_boleto_pagamento_requerimento = bph5.ide_boleto_pagamento and bph5.ide_status_boleto_pagamento = 4) as DT_VALIDACAO, ");

		lSql.append("bpr.val_boleto as VALOR, ");

		lSql.append("bpr.ide_requerimento as ID_REQUERIMENTO, ");
		
		lSql.append("req.num_requerimento as NUM_REQUERIMENTO, ");

		lSql.append("bpr.ide_processo as ID_PROCESSO, ");
		
		lSql.append("proc.num_processo as NUM_PROCESSO, ");
		
		lSql.append("(select nom_tipo_boleto_pagamento from tipo_boleto_pagamento where ide_tipo_boleto_pagamento = BPR.ide_tipo_boleto_pagamento) as NOM_TIPO_BOLETO, ");
		
		lSql.append("0.0 AS valorTotalCertificado, ");
		lSql.append("0.0 AS valorTotalVistoria, ");
		
		lSql.append("bpr.ide_processo_reenquadramento AS ideProcessoReenquadramento, ");
		lSql.append("bpr.ind_boleto_registrado as indBoletoRegistrado ");
		
		lSql.append("from boleto_pagamento_requerimento BPR "); //BOLETO PAGAMENTO REQUERIMENTO
		lSql.append("left join processo PROC on bpr.ide_processo = proc.ide_processo "); //PROCESSO
		lSql.append("left join requerimento REQ on proc.ide_requerimento = req.ide_requerimento "); //REQUERIMENTO
		lSql.append("left join requerimento_pessoa REQ_PES on proc.ide_requerimento = REQ_PES.ide_requerimento  "); //REQUERIMENTO_PESSOA
		lSql.append("left join pessoa PES on REQ_PES.ide_pessoa = PES.ide_pessoa "); //PESSOA
		lSql.append("left join pessoa_fisica PF on PES.ide_pessoa = PF.ide_pessoa_fisica "); //PESSOA FISICA
		lSql.append("left join pessoa_juridica PJ on PES.ide_pessoa = PJ.ide_pessoa_juridica "); //PESSOA JURIDICA
		lSql.append("inner join boleto_pagamento_historico BPH0 on bpr.ide_boleto_pagamento_requerimento = bph0.ide_boleto_pagamento "); //HISTORICO
		lSql.append("inner join status_boleto_pagamento STA0 on bph0.ide_status_boleto_pagamento = sta0.ide_status_boleto_pagamento "); //STATUS
		
		lSql.append("where REQ_PES.ide_tipo_pessoa_requerimento = 1 and BPR.ide_tipo_boleto_pagamento != 1 ");
		
		if (!Util.isNullOuVazio(filter)) {

			if(usuarioExterno) {
				
				if(!Util.isNullOuVazio(filter.getListPF()) && !Util.isNullOuVazio(filter.getListPJ())) {
					
					//ID dos Requerentes - Pessoa Fisica
					lSql.append("and (");
						lSql.append("pf.ide_pessoa_fisica in ( ");
							lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
							
							for (int i = 1; i < filter.getListPF().size(); i++) {
								
								lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
							}
						lSql.append(") ");
					
						//ID dos Requerentes - Pessoa Juridica
						lSql.append("OR pj.ide_pessoa_juridica in (");
							lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
							
							for (int i = 1; i < filter.getListPJ().size(); i++) {
								
								lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
							}
						lSql.append(") ");
					lSql.append(") ");
					
				} else {
					
					//ID dos Requerentes - Pessoa Fisica
					if(!Util.isNullOuVazio(filter.getListPF())) {
						
						lSql.append("and pf.ide_pessoa_fisica in ("); 
						
						lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
						
						for (int i = 1; i < filter.getListPF().size(); i++) {
							
							lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
						}
						
						lSql.append(") ");
					}
					
					//ID dos Requerentes - Pessoa Juridica
					if(!Util.isNullOuVazio(filter.getListPJ())) {
						
						lSql.append("and pj.ide_pessoa_juridica in (");
						
						lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
						
						for (int i = 1; i < filter.getListPJ().size(); i++) {
							
							lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
						}
						
						lSql.append(") ");
					}
				}
				
			} else {

				if (!Util.isNullOuVazio(filter.getRequerente())) {

					// ID do Requerente - Pessoa Fisica
					if (!Util.isNullOuVazio(filter.getRequerente().getPessoaFisica())) {

						lSql.append("and pf.ide_pessoa_fisica = " + filter.getRequerente().getPessoaFisica().getIdePessoaFisica() + " ");
					}

					// ID do Requerente - Pessoa Juridica
					if (!Util.isNullOuVazio(filter.getRequerente().getPessoaJuridica())) {

						lSql.append("and pj.ide_pessoa_juridica = " + filter.getRequerente().getPessoaJuridica().getIdePessoaJuridica() + " ");
					}
				}
			}
			
			//id do boleto
			if(!Util.isNullOuVazio(filter.getIdeProcessoReenquadramento())) {
				
				lSql.append("and BPR.ide_processo_reenquadramento = " + filter.getIdeProcessoReenquadramento() + " ");
			}
			
			//Numero do Requerimento
			if(!Util.isNullOuVazio(filter.getNumRequerimento())) {
				
				lSql.append("and req.num_requerimento like '%" + filter.getNumRequerimento() + "%' ");
			}
			
			//Numero do Processo
			if(!Util.isNullOuVazio(filter.getNumProcesso())) {
				
				lSql.append("and proc.num_processo like '%" + filter.getNumProcesso() + "%' ");
			}
			
			//Numero do Boleto
			if(!Util.isNullOuVazio(filter.getNumBoleto())) {
				
				lSql.append("and bpr.num_boleto like '%" + filter.getNumBoleto() + "%' ");
			}
			
			//Filtro do Status e Data
			if(filter.isStatusSelecionado()) {
				if(!Util.isNullOuVazio(idStatus)) {
					
					lSql.append(" and (select sta.ide_status_boleto_pagamento "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) = " + idStatus + " ");
				}
				
				if(!Util.isNullOuVazio(dtInicial)) {
					
					lSql.append(" and (select bph2.dtc_tramitacao "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) >= '" + dtInicial + "' ");
				}
				
				if(!Util.isNullOuVazio(dtFinal)) {
					
					lSql.append(" and (select bph2.dtc_tramitacao "
							+ "from status_boleto_pagamento sta, boleto_pagamento_historico bph2 "
							+ "where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento and bph2.ide_boleto_pagamento_historico = "
							+ "(select MAX(ide_boleto_pagamento_historico) from boleto_pagamento_historico bph3 where bph3.ide_boleto_pagamento = bpr.ide_boleto_pagamento_requerimento)) <= '" + dtFinal + "' ");
				}
			}
		}
		
		
		lSql.append(" UNION ALL ");
		
		
		lSql.append("SELECT distinct this_.ide_boleto_dae_requerimento AS id, ");
		
		lSql.append("coalesce(pf5_.nom_pessoa, pj6_.nom_razao_social) AS requerente, ");
		
		lSql.append("NULL AS numeroBoleto, ");

		lSql.append("(SELECT bph1.dtc_tramitacao ");
		lSql.append("FROM boleto_dae_historico bph1 ");
		lSql.append("WHERE this_.ide_boleto_dae_requerimento = bph1.ide_boleto_dae_requerimento ");
		lSql.append("AND bph1.ide_status_boleto_pagamento = 1 ");
		lSql.append("ORDER BY bph1.ide_boleto_dae_requerimento ");
		lSql.append("LIMIT 1) AS dtGeracao, ");
		
		lSql.append("cast(null as date) AS dtVencimento, ");
		
		lSql.append("(SELECT sta.nom_status_boleto_pagamento ");
		lSql.append("FROM status_boleto_pagamento sta, ");
		lSql.append("boleto_dae_historico bph2 ");
		lSql.append(" WHERE sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento ");
		lSql.append("AND bph2.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento ");
		lSql.append("AND bph2.ide_boleto_dae_historico = ");
		lSql.append("(SELECT MAX(ide_boleto_dae_historico) ");
		lSql.append("FROM boleto_dae_historico bph3 ");
		lSql.append("WHERE bph3.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento)) AS status, ");

		lSql.append("(SELECT max(bph4.dtc_tramitacao) ");
		lSql.append("FROM boleto_dae_historico bph4 ");
		lSql.append("WHERE this_.ide_boleto_dae_requerimento = bph4.ide_boleto_dae_requerimento ");
		lSql.append("AND ((bph4.ide_status_boleto_pagamento = 3) ");
		lSql.append("OR (bph4.ide_status_boleto_pagamento = 5))) AS dtPagamentoCancelamento, ");

		lSql.append("(SELECT max(bph5.dtc_tramitacao) ");
		lSql.append("FROM boleto_dae_historico bph5 ");
		lSql.append("WHERE this_.ide_boleto_dae_requerimento = bph5.ide_boleto_dae_requerimento ");
		lSql.append("AND bph5.ide_status_boleto_pagamento = 4) AS dtValidacao, ");
		
		lSql.append("0.0 AS valor, ");
		
		lSql.append("req2_.ide_requerimento AS ID_REQUERIMENTO, ");
		
		lSql.append("req2_.num_requerimento AS NUM_REQUERIMENTO, ");
		
		lSql.append("proc1_.ide_processo AS ID_PROCESSO, ");
		
		lSql.append("proc1_.num_processo AS NUM_PROCESSO, ");

		lSql.append("(SELECT nom_tipo_boleto_pagamento ");
		lSql.append("FROM tipo_boleto_pagamento ");
		lSql.append("WHERE ide_tipo_boleto_pagamento = this_.ide_tipo_boleto_pagamento) AS tipoBoleto, ");
		
		lSql.append("this_.vlr_total_certificado AS valorTotalCertificado, ");
		
		lSql.append("this_.vlr_total_vistoria AS valorTotalVistoria, ");
	
		lSql.append("this_.ide_processo_reenquadramento AS ideProcessoReenquadramento, ");
		lSql.append("false as indBoletoRegistrado ");
		
		lSql.append("FROM boleto_dae_requerimento this_ ");
		lSql.append("INNER JOIN boleto_dae_historico bph0x7_ ON this_.ide_boleto_dae_requerimento=bph0x7_.ide_boleto_dae_requerimento ");
		lSql.append("INNER JOIN status_boleto_pagamento sta0x8_ ON bph0x7_.ide_status_boleto_pagamento=sta0x8_.ide_status_boleto_pagamento ");
		lSql.append("INNER JOIN processo proc1_ ON this_.ide_processo=proc1_.ide_processo ");
		lSql.append("LEFT OUTER JOIN requerimento req2_ ON proc1_.ide_requerimento=req2_.ide_requerimento ");
		lSql.append("LEFT OUTER JOIN requerimento_pessoa req_pes3_ ON req2_.ide_requerimento=req_pes3_.ide_requerimento ");
		lSql.append("LEFT OUTER JOIN pessoa pes4_ ON req_pes3_.ide_pessoa=pes4_.ide_pessoa ");
		lSql.append("LEFT OUTER JOIN pessoa_fisica pf5_ ON pes4_.ide_pessoa=pf5_.ide_pessoa_fisica ");
		lSql.append("LEFT OUTER JOIN pessoa_juridica pj6_ ON pes4_.ide_pessoa=pj6_.ide_pessoa_juridica ");
		lSql.append("WHERE req_pes3_.ide_tipo_pessoa_requerimento = 1 AND this_.ide_tipo_boleto_pagamento = 7 ");
		
		if (!Util.isNullOuVazio(filter)) {

			if(usuarioExterno) {
				
				if(!Util.isNullOuVazio(filter.getListPF()) && !Util.isNullOuVazio(filter.getListPJ())) {
					
					//ID dos Requerentes - Pessoa Fisica
					lSql.append("and (");
						lSql.append("pf5_.ide_pessoa_fisica in ( ");
							lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
							
							for (int i = 1; i < filter.getListPF().size(); i++) {
								
								lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
							}
						lSql.append(") ");
					
						//ID dos Requerentes - Pessoa Juridica
						lSql.append("OR pj6_.ide_pessoa_juridica in (");
							lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
							
							for (int i = 1; i < filter.getListPJ().size(); i++) {
								
								lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
							}
						lSql.append(") ");
					lSql.append(") ");
					
				} else {
					
					//ID dos Requerentes - Pessoa Fisica
					if(!Util.isNullOuVazio(filter.getListPF())) {
						
						lSql.append("and pf5_.ide_pessoa_fisica in ("); 
						
						lSql.append(filter.getListPF().get(0).getIdePessoaFisica());
						
						for (int i = 1; i < filter.getListPF().size(); i++) {
							
							lSql.append(", " + filter.getListPF().get(i).getIdePessoaFisica());
						}
						
						lSql.append(") ");
					}
					
					//ID dos Requerentes - Pessoa Juridica
					if(!Util.isNullOuVazio(filter.getListPJ())) {
						
						lSql.append("and pj6_.ide_pessoa_juridica in (");
						
						lSql.append(filter.getListPJ().get(0).getIdePessoaJuridica());
						
						for (int i = 1; i < filter.getListPJ().size(); i++) {
							
							lSql.append(", " + filter.getListPJ().get(i).getIdePessoaJuridica());
						}
						
						lSql.append(") ");
					}
				}
				
			} else {

				if (!Util.isNullOuVazio(filter.getRequerente())) {

					// ID do Requerente - Pessoa Fisica
					if (!Util.isNullOuVazio(filter.getRequerente().getPessoaFisica())) {

						lSql.append("and pf5_.ide_pessoa_fisica = " + filter.getRequerente().getPessoaFisica().getIdePessoaFisica() + " ");
					}

					// ID do Requerente - Pessoa Juridica
					if (!Util.isNullOuVazio(filter.getRequerente().getPessoaJuridica())) {

						lSql.append("and pj6_.ide_pessoa_juridica = " + filter.getRequerente().getPessoaJuridica().getIdePessoaJuridica() + " ");
					}
				}
			}
			
			//id do boleto
			if(!Util.isNullOuVazio(filter.getIdeProcessoReenquadramento())) {
				
				lSql.append("and this_.ide_processo_reenquadramento = " + filter.getIdeProcessoReenquadramento() + " ");
			}
			
			//Numero do Requerimento
			if(!Util.isNullOuVazio(filter.getNumRequerimento())) {
				
				lSql.append("and req2_.num_requerimento like '%" + filter.getNumRequerimento() + "%' ");
			}
			
			//Numero do Processo
			if(!Util.isNullOuVazio(filter.getNumProcesso())) {
				
				lSql.append("and proc1_.num_processo like '%" + filter.getNumProcesso() + "%' ");
			}
		
			//Filtro do Status e Data
			if(filter.isStatusSelecionado()) {
				if(!Util.isNullOuVazio(idStatus)) {
					lSql.append(" and (select sta.ide_status_boleto_pagamento "
							.concat("from status_boleto_pagamento sta, boleto_dae_historico bph2 ")
							.concat("where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento and bph2.ide_boleto_dae_historico = ")
							.concat("(select MAX(ide_boleto_dae_historico) from boleto_dae_historico bph3 where bph3.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento)) = ")
							.concat(idStatus.toString())
							.concat(" "));
				
				}
				
				if(!Util.isNullOuVazio(dtInicial)) {
					lSql.append("and (select bph2.dtc_tramitacao "
							.concat("from status_boleto_pagamento sta, boleto_dae_historico bph2 ")
							.concat("where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento and bph2.ide_boleto_dae_historico = ")
							.concat("(select MAX(ide_boleto_dae_historico) from boleto_dae_historico bph3 where bph3.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento)) >= '")
							.concat(dtInicial.toString())
							.concat("' "));
				
				}
				
				if(!Util.isNullOuVazio(dtFinal)) {
					lSql.append("and (select bph2.dtc_tramitacao "
							.concat("from status_boleto_pagamento sta, boleto_dae_historico bph2 ")
							.concat("where sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento and bph2.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento and bph2.ide_boleto_dae_historico = ")
							.concat("(select MAX(ide_boleto_dae_historico) from boleto_dae_historico bph3 where bph3.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento)) <= '")
							.concat(dtFinal.toString())
							.concat("' "));
				}
			}
		}
		
		return lSql;
	}
}