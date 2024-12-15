package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.enumerator.StatusFinanceiro;
import br.gov.ba.seia.util.Util;

public class RelatorioFinanceiroDAOImpl {
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obterRelatorioFinanceiro(String numDocumento, String nomeRequerente, String dataVencimentoInicial, String dataVencimentoFinal, String dataPagamentoInicial, String dataPagamentoFinal, List<StatusFinanceiro> listStatusFinanceiroEnum) {
		
		removerBoletoPago(listStatusFinanceiroEnum, dataPagamentoInicial, dataPagamentoFinal);
		
		StringBuilder lSql = gerarSql(numDocumento, nomeRequerente, dataVencimentoInicial, dataVencimentoFinal, listStatusFinanceiroEnum, dataPagamentoInicial, dataPagamentoFinal);
		
//		lSql = adicionarUnion(lSql, dataPagamentoInicial, dataPagamentoFinal, listStatusFinanceiroEnum, numDocumento, nomeRequerente, dataVencimentoInicial, dataVencimentoFinal);
		
		lSql.append(" order by 4 ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
								
		return lQuery.getResultList();
	}
	
	private StringBuilder gerarSql(String numDocumento, String nomeRequerente, String dataVencimentoInicial, String dataVencimentoFinal, List<StatusFinanceiro> listStatusFinanceiroEnum, String dataPagamentoInicial, String dataPagamentoFinal){
		StringBuilder lSql = new StringBuilder();
		lSql.append(" SELECT   coalesce(pf.nom_pessoa,pj.nom_razao_social) as pessoa2,"
		        + "        coalesce(pf.nom_pessoa,pj.nom_razao_social) as pessoa,"
		        + "       coalesce(pf.num_cpf,pj.num_cnpj) as documento, "
		        + "      num_boleto,"
		        + "      tbp.nom_tipo_boleto_pagamento,"
                + "        r.num_requerimento,"
		        + "        array_to_string( array( SELECT aa.nom_ato_ambiental FROM ato_ambiental aa "
		        + "           INNER JOIN enquadramento_ato_ambiental eaa ON eaa.ide_ato_ambiental = aa.ide_ato_ambiental "
		        + "           INNER JOIN enquadramento e ON e.ide_enquadramento = eaa.ide_enquadramento "
		        + "           WHERE e.ide_requerimento_unico =  db.ide_requerimento or e.ide_requerimento = db.ide_requerimento), ', ') as atos, "
                + "        val_boleto,"
                + "        val_boleto_outorga,"
                + "        val_boleto+val_boleto_outorga total,"
                + "        dtc_vencimento,"
                + "       CASE WHEN tr.ide_status_requerimento=6 THEN 'Aguardando Pagamento' "
              + "          WHEN tr.ide_status_requerimento=7 THEN 'Aguardando Validação' "
              + "          WHEN tr.ide_status_requerimento=8 THEN 'Boleto Pago' "
              + "          WHEN tr.ide_status_requerimento=19 THEN 'Boleto Pago' "
              + "          WHEN tr.ide_status_requerimento=9 THEN 'Cancelado' "
              + "          ELSE ''"
              + "          END as status,"
              + "        dtc_pagamento"
		        + " FROM vw_daes_boletos db"
		        + " INNER JOIN requerimento_pessoa rp ON rp.ide_requerimento = db.ide_requerimento and rp.ide_tipo_pessoa_requerimento = 1 "
		        + "         INNER JOIN tramitacao_requerimento tr ON tr.ide_tramitacao_requerimento = db.ide_max_tramitacao_requerimento "
		        + "         LEFT JOIN pessoa_fisica pf ON pf.ide_pessoa_fisica = rp.ide_pessoa "
		        + "         LEFT JOIN pessoa_juridica pj ON pj.ide_pessoa_juridica = rp.ide_pessoa "
		        + "         LEFT JOIN tipo_boleto_pagamento tbp ON tbp.ide_tipo_boleto_pagamento = db.ide_tipo_boleto_pagamento"
		        + "         LEFT JOIN requerimento r ON r.ide_requerimento =db.ide_requerimento "
		        + "         LEFT JOIN status_requerimento sr ON tr.ide_status_requerimento =sr.ide_status_requerimento "
		        + "         LEFT JOIN processo p ON p.ide_processo =db.ide_processo "
		        + "         WHERE ide_boleto_pagamento_requerimento is not null and dt_cancelamento is null ");
			if(!Util.isNullOuVazio(dataVencimentoInicial)) {
			lSql.append(" and dtc_vencimento >= '" + dataVencimentoInicial + "'");
		}
		
		if(!Util.isNullOuVazio(dataVencimentoFinal)) {
			dataVencimentoFinal += " 23:59:00";
			lSql.append(" and dtc_vencimento <= '" + dataVencimentoFinal + "'");
		}
		
		if(!Util.isNullOuVazio(nomeRequerente)) {
			lSql.append(" and coalesce(pf.nom_pessoa,pj.nom_razao_social) like '%" + nomeRequerente.trim() + "%'");
		}

		if(!Util.isNullOuVazio(numDocumento)) {
			lSql.append(" and num_boleto  like '%" + numDocumento.trim()+ "%'");
		}
		
		if(!Util.isNullOuVazio(dataPagamentoInicial) || !Util.isNullOuVazio(dataPagamentoFinal)) {
		    String sqlDtcPagamento = "";
	        
	        if(!Util.isNullOuVazio(dataPagamentoInicial)) {
	            sqlDtcPagamento += " dtc_pagamento >= '".concat(dataPagamentoInicial).concat("' ");
	        }
	        
	        if(!Util.isNullOuVazio(dataPagamentoFinal)) {
	            dataPagamentoFinal += " 23:59:00";
	            sqlDtcPagamento += (sqlDtcPagamento == "")?"":" and";
	            sqlDtcPagamento += " dtc_pagamento <= '".concat(dataPagamentoFinal).concat("' ");
	        }
	        
	        if ("".equals(sqlDtcPagamento)){
	            sqlDtcPagamento += " AND dtc_pagamento IS NOT NULL";
	            
	        }
	        lSql.append(" and ("+sqlDtcPagamento+")");
		}
		
		if(!Util.isNullOuVazio(listStatusFinanceiroEnum)) {
			if(listStatusFinanceiroEnum.get(0).getId().equals(8)){
				listStatusFinanceiroEnum.add(StatusFinanceiro.DECLARACAO_EMITIDA);
			}
			lSql.append(" and tr.ide_status_requerimento  in ( '" + listStatusFinanceiroEnum.get(0).getId()+"'");
			for (int i = 1; i < listStatusFinanceiroEnum.size(); i++) {
				lSql.append(", '" + listStatusFinanceiroEnum.get(i).getId()+"'");
			} 
			lSql.append(" )");
		}
		return lSql;
	}
	
	private void removerBoletoPago(List<StatusFinanceiro> listStatusFinanceiroEnum, String dataPagamentoInicial, String dataPagamentoFinal){
		if (!Util.isNullOuVazio(dataPagamentoInicial) || !Util.isNullOuVazio(dataPagamentoFinal)){
			for (Iterator<StatusFinanceiro> it = listStatusFinanceiroEnum.iterator(); it.hasNext(); ) {
				
				StatusFinanceiro statusFinanceiro =  it.next();
				
				if (StatusFinanceiro.BOLETO_PAGO.equals(statusFinanceiro)){
					it.remove();
				}
			}
		}
	}

}
