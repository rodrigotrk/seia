package br.gov.ba.seia.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.dto.ColunasExibirRelatorioQuantitativoImoveisRuraisDTO;
import br.gov.ba.seia.dto.FiltroImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.ImovelRuralRelatorioDTO;
import br.gov.ba.seia.util.Util;

public class ImovelRuralRelatorioDAOImpl implements  ImovelRuralRelatorioDAOIf{
	
	SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ImovelRuralRelatorioDTO> listarImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, Map<String, Object> params, boolean consultaSimples) throws Exception {
		List<Object[]> listaObj = null;
		List<ImovelRuralRelatorioDTO> listaImoveis = new ArrayList<ImovelRuralRelatorioDTO>();
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(extrairConsultaTo(filtroImovelRuralRelatorioDTO,consultaSimples).toString());
		if(!consultaSimples){
			Integer first = (Integer) params.get("first");
			Integer pageSize = (Integer) params.get("pageSize");
		
			lQuery.setFirstResult(first);
			lQuery.setMaxResults(pageSize);
		}
		
		listaObj = lQuery.getResultList();
		
		for (Object[] resultElement : listaObj) {	
			listaImoveis.add(new ImovelRuralRelatorioDTO(resultElement,consultaSimples));
		}
		return listaImoveis;
	}
	
	@Override
	public StringBuilder extrairConsultaTo(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, boolean indRelatorioPDF) {
		
		StringBuilder sql = montarConsultaImovelRuralRelatorio(filtroImovelRuralRelatorioDTO, indRelatorioPDF);
		
		if ( (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) 
				|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado()))
						|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado()))
								|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado()))
										|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado()))) {
			sql = montarConsultaImovelRuralRelatorioGEO(sql, filtroImovelRuralRelatorioDTO); 
		}
		return sql;
	}
	
	@Override
	public StringBuilder extrairConsultaTo(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = montarConsultaImovelRuralRelatorio(filtroImovelRuralRelatorioDTO);
		
		if ( (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) 
				|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado()))
						|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado()))
								|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado()))
										|| (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado()))) {
			sql = montarConsultaImovelRuralRelatorioGEOSoImovel(sql, filtroImovelRuralRelatorioDTO); 
		}
		
		return sql;
	}
	
	@Override
	public StringBuilder montarConsultaImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, boolean indRelatorioPDF) {
		
		final int TIPO_CADASTRO_IMOVEL_RURAL_CDA = 3;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT ON (ir.ide_imovel_rural) "); 
		sql.append(" ir.ide_imovel_rural as ideImovelRural");	
		sql.append(",   ir.val_area as valArea");
		sql.append(",   ir.ide_localizacao_geografica ");
		
		if(!indRelatorioPDF){
			sql.append(",   m.nom_municipio as nomMunicipio, ");       
			sql.append("   (CASE ");
			sql.append("WHEN ir.status_cadastro = 0 and (ir.ind_suspensao = false or ir.ind_suspensao is null) THEN 'Registro incompleto' ");
			sql.append("WHEN ir.status_cadastro = 1 and (ir.ind_suspensao = false or ir.ind_suspensao is null) THEN 'Registrado' ");
			sql.append("WHEN ir.status_cadastro = 2 and (ir.ind_suspensao = false or ir.ind_suspensao is null) THEN 'Cadastro incompleto' ");
			sql.append("WHEN ir.status_cadastro = 3 and (ir.ind_suspensao = false or ir.ind_suspensao is null) THEN 'Cadastrado' ");
			sql.append("WHEN ir.ind_suspensao = true THEN 'Pendente' ");
			sql.append("ELSE 'Status Não Encontrado!' ");
			sql.append("   END) as statusCadastro, ");
			sql.append("   ir.dtc_finalizacao as dtcFinalizacao, ");		
			sql.append("   rl.dtc_aprovacao as dtcAprovacao, ");	
			sql.append("   td.dsc_tipo_documento_imovel_rural as dscTipoDocumentoImovelRural, ");
			sql.append("   ir.des_denominacao as desDenominacao, ");
			sql.append("   ir.qtd_modulo_fiscal as qtdModuloFiscal, ");
			sql.append("   sr.dsc_status as dscStatus, ");
			sql.append("   nom_contrato_convenio_cefir || ' - ' || cc.num_contrato_convenio_cefir as numContratoConvenioCefir, ");
			sql.append("   (CASE WHEN pf.nom_pessoa is not null THEN pf.nom_pessoa ");
			sql.append("    ELSE pj.nom_razao_social ");
			sql.append("   END) as nomRequerente, ");
			sql.append("   (CASE WHEN pf.nom_pessoa is not null THEN pf.num_cpf ");
			sql.append("    ELSE pj.num_cnpj ");
			sql.append("   END) as numCpfCnpjRequerente, ");
			sql.append("   ir.sts_certificado as stsCertificado, ");
			sql.append("   ir.dtc_primeira_finalizacao as dtcPrimeiraFinalizacao, ");
			sql.append("   s.url_recibo_inscricao as urlReciboInscricao, ");
			sql.append("   (CASE "); 
			sql.append("   		WHEN ir.status_cadastro = 1 AND ir.ide_tipo_cadastro_imovel_rural = 4 AND ir.sts_certificado =false THEN true ");
			sql.append("   		ELSE false ");
			sql.append("   	   END) AS ind_avisoBndes, ");
			sql.append("   dtc_primeira_sincronia_com_sucesso AS dtcSincronizacao ,");
			sql.append(" pfr.nom_pessoa AS nomeResponsavelTecnico, ");
     		sql.append(" pfc.nom_pessoa as nomeCadastrante, "); 
    		sql.append(" pfc.num_cpf as cpfCadastrante ");
			

		}
		
		sql.append(" FROM imovel_rural ir ");
		sql.append("INNER JOIN imovel i ON (ir.ide_imovel_rural = i.ide_imovel) ");
		sql.append("LEFT OUTER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel) ");
		sql.append("LEFT OUTER JOIN endereco en ON (i.ide_endereco = en.ide_endereco) ");
		sql.append("LEFT OUTER JOIN logradouro l ON (en.ide_logradouro = l.ide_logradouro) ");
		sql.append("LEFT OUTER JOIN municipio m ON (l.ide_municipio = m.ide_municipio) ");
		sql.append("LEFT OUTER JOIN documento_imovel_rural_posse di ON (ir.ide_imovel_rural = di.ide_imovel_rural) ");
		sql.append("LEFT OUTER JOIN tipo_documento_imovel_rural td ON (di.ide_tipo_documento_imovel_rural = td.ide_tipo_documento_imovel_rural) ");
		sql.append("LEFT OUTER JOIN reserva_legal rl ON (ir.ide_imovel_rural = rl.ide_imovel_rural) ");
		sql.append("LEFT OUTER JOIN status_reserva_legal sr ON (rl.ide_status = sr.ide_status_reserva_legal) ");
		sql.append("LEFT OUTER JOIN contrato_convenio_cefir cc ON (ir.ide_contrato_convenio = cc.ide_contrato_convenio_cefir) ");
		sql.append("LEFT OUTER JOIN gestor_financeiro_cefir gf ON (cc.ide_gestor_financeiro_cefir = gf.ide_gestor_financeiro_cefir) ");
		sql.append("LEFT JOIN pessoa_fisica pf ON (ir.ide_requerente_cadastro = pf.ide_pessoa_fisica) ");
		sql.append("LEFT JOIN pessoa_juridica pj ON (ir.ide_requerente_cadastro = pj.ide_pessoa_juridica) ");
		sql.append("LEFT JOIN requerimento_imovel ri ON (i.ide_imovel = ri.ide_imovel) ");
		sql.append("LEFT JOIN requerimento r ON (ri.ide_requerimento = r.ide_requerimento) ");
		sql.append("LEFT JOIN certificado c ON (c.ide_requerimento = r.ide_requerimento) ");
		sql.append("LEFT JOIN responsavel_imovel_rural resp ON (ir.ide_imovel_rural = resp.ide_imovel_rural) ");
		sql.append("LEFT JOIN pessoa_fisica pfr ON (pfr.ide_pessoa_fisica = resp.ide_pessoa_fisica) ");
        sql.append("LEFT JOIN pessoa_fisica pfc ON pfc.ide_pessoa_fisica=ir.ide_requerente_cadastro ");
		
		
		if(filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			sql.append("INNER JOIN imovel_rural_sicar s ON (s.ide_imovel_rural = ir.ide_imovel_rural) ");
		}else{
			sql.append("LEFT JOIN imovel_rural_sicar s ON (s.ide_imovel_rural = ir.ide_imovel_rural) ");
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) && filtroImovelRuralRelatorioDTO.getTipoArl() > 0) {
			sql.append("LEFT JOIN tipo_arl trl ON (trl.ide_tipo_arl = rl.ide_tipo_arl) ");			
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getResponsavelTecnico())) {
			sql.append("LEFT JOIN responsavel_imovel_rural rir ON (ir.ide_imovel_rural = rir.ide_imovel_rural) ");
		}
		
		sql.append("WHERE i.ind_excluido = false ");
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getResponsavelTecnico())) {
			sql.append("  AND rir.ind_excluido IS FALSE ");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProprietario())){
			sql.append("  AND (pi.ide_tipo_vinculo_imovel in (1, 2) AND pi.ide_pessoa = " + filtroImovelRuralRelatorioDTO.getIdeProprietario() + ")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProcurador())){
			sql.append("  AND ir.ide_imovel_rural in (SELECT pi2.ide_imovel FROM pessoa_imovel pi2 WHERE pi2.ide_tipo_vinculo_imovel = 5 AND pi2.ide_pessoa = " + filtroImovelRuralRelatorioDTO.getIdeProcurador() + ")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNomImovelRural())){
			sql.append("AND remover_acentuacao_uppercase(ir.des_denominacao) ILIKE '%'||remover_acentuacao_uppercase('" + filtroImovelRuralRelatorioDTO.getNomImovelRural().replace("'", "''") + "')||'%' ");
		}
								
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListMunicipio())){
			sql.append("  AND m.ide_municipio IN ("+ convertListToString(filtroImovelRuralRelatorioDTO.getListMunicipio()) +")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListStatusCadastro())){
			sql.append("  AND ir.status_cadastro IN ("+ convertListToString(filtroImovelRuralRelatorioDTO.getListStatusCadastro()) +")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getStatusPendente())){
			if (filtroImovelRuralRelatorioDTO.getStatusPendente()) {
				sql.append("  AND ir.ind_suspensao = " + filtroImovelRuralRelatorioDTO.getStatusPendente());
			}else {				
				sql.append("  AND (ir.ind_suspensao = " + filtroImovelRuralRelatorioDTO.getStatusPendente() + " OR ir.ind_suspensao is null) ");
			}
		}
															     
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
			
			if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
					&& Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND (ir.dtc_primeira_finalizacao BETWEEN '" + dataInicio +" 00:00:00' AND now()) AND status_cadastro <> 0");
			
			} else if (Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {				
			
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND ir.dtc_primeira_finalizacao BETWEEN '2012-01-01 00:00:00' AND '" + dataFim +" 23:59:59' AND status_cadastro <> 0");
			
			} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {				
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND ir.dtc_primeira_finalizacao BETWEEN '" + dataInicio +" 00:00:00' AND '" + dataFim +" 23:59:59' AND status_cadastro <> 0");			
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
			
			if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
					&& Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND (s.dtc_primeira_sincronia_com_sucesso BETWEEN '" + dataInicio +" 00:00:00' AND now()) ");
			
			} else if (Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {				
			
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND s.dtc_primeira_sincronia_com_sucesso BETWEEN '2012-01-01 00:00:00' AND '" + dataFim +" 23:59:59' ");
			
			} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {				
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND s.dtc_primeira_sincronia_com_sucesso BETWEEN '" + dataInicio +" 00:00:00' AND '" + dataFim +" 23:59:59' ");			
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())) {
			
			if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())
					&& Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())) {
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal()) : sdfymd.format(new Date());
				sql.append("  AND (rl.dtc_aprovacao BETWEEN '" + dataInicio +" 00:00:00' AND now()) ");
			
			} else if (Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())) {				
			
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal()) : sdfymd.format(new Date());
				sql.append("  rl.dtc_aprovacao BETWEEN '2012-01-01 00:00:00' AND '" + dataFim +" 23:59:59' ");
			
			} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())) {				
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal()) : sdfymd.format(new Date());
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal()) : sdfymd.format(new Date());
				sql.append("  AND rl.dtc_aprovacao BETWEEN '" + dataInicio +" 00:00:00' AND '" + dataFim +" 23:59:59' ");			
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDocumentoFinal())) {
			if(filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(2) || filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(3)) {
				if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNumCertificado())) {
					sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false AND c.num_certificado ilike '%" + filtroImovelRuralRelatorioDTO.getNumCertificado() + "%') ");
				} else {
					if (filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(2)){
						sql.append(" AND ir.sts_certificado = true ");
					} else {
						sql.append(" AND ir.sts_certificado = false ");
					}
					sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false) ");
				}
				
				if(Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListStatusCadastro())) {
					sql.append("  AND ir.status_cadastro = 3 ");
				}
			} else if(filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(4)) {
				sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false) ");
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())){
			sql.append("  AND gf.ide_gestor_financeiro_cefir in (" + convertListToString(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro()) + ") ");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())){
			sql.append("  AND cc.ide_contrato_convenio_cefir in (" + convertListToString(filtroImovelRuralRelatorioDTO.getListContratoConvenio()) + ") ");
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais() > 0){
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(1)) {
				sql.append("  AND ir.qtd_modulo_fiscal <=  4 ");	
			} else if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(2)) {
				sql.append("  AND ir.qtd_modulo_fiscal >  4 ");
			}			
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) && filtroImovelRuralRelatorioDTO.getTipoArl() > 0) {
			sql.append("  AND trl.ide_tipo_arl = "+filtroImovelRuralRelatorioDTO.getTipoArl()+" ");			
		}

		if(filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = "+TIPO_CADASTRO_IMOVEL_RURAL_CDA+" ");
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(1)) {
			sql.append("  AND ir.ind_deseja_completar_informacoes = true ");
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(2)) {
			sql.append("  AND ir.ind_deseja_completar_informacoes = false ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()) {
			sql.append("  AND (ir.ide_imovel_rural IN(SELECT irrl.ide_imovel_rural FROM imovel_rural irrl ");
			sql.append("       INNER JOIN imovel irl ON (irrl.ide_imovel_rural = irl.ide_imovel AND irl.ind_excluido = false) ");
			sql.append("       INNER JOIN reserva_legal rl ON (irrl.ide_imovel_rural = rl.ide_imovel_rural AND rl.ide_tipo_estado_conservacao <> 1) ");
			sql.append("       INNER JOIN cronograma_recuperacao crrl ON (rl.ide_reserva_legal = crrl.ide_reserva_legal)) ");
			sql.append("  OR ir.ide_imovel_rural IN (SELECT irapp.ide_imovel_rural FROM imovel_rural irapp ");
			sql.append("     INNER JOIN imovel iapp ON (irapp.ide_imovel_rural = iapp.ide_imovel AND iapp.ind_excluido = false) ");
			sql.append("     INNER JOIN app app ON (irapp.ide_imovel_rural = app.ide_imovel_rural AND app.ide_tipo_estado_conservacao <> 1) ");
			sql.append("     INNER JOIN cronograma_recuperacao crapp ON (app.ide_app = crapp.ide_app))) ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {
			sql.append("  AND ir.ind_outros_passivos = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isRlAprovada()) {
			sql.append("  AND rl.ide_status = 1 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isLimiteBloqueado()) {
			sql.append("  AND ir.ind_bloqueio_limite = true ");
		}

		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()) {
			sql.append("  AND rl.ind_sobreposicao_app = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {
			sql.append("  AND ind_menor_vinte_porcento = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			sql.append("  AND s.num_sicar IS NOT NULL ");			
		}
		
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 4 ");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 3 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 1 ");
		} else if (filtroImovelRuralRelatorioDTO.isModuloAssentamento()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		} else if (filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 5 ");
		}

		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getResponsavelTecnico())) {
			sql.append(" AND rir.ide_pessoa_fisica = " + filtroImovelRuralRelatorioDTO.getResponsavelTecnico().getIdePessoa() + " ");
		}
		
		sql.append(" ORDER BY ir.ide_imovel_rural DESC ");
	 System.out.println(sql);
		return sql;
	}
	
	@Override
	public StringBuilder montarConsultaImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		
		final int TIPO_CADASTRO_IMOVEL_RURAL_CDA = 3;
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT ON(ir.ide_imovel_rural) "); 
		sql.append(" ir.ide_imovel_rural as ideImovelRural");	
		sql.append(" FROM imovel_rural ir ");
		sql.append("INNER JOIN imovel i ON (ir.ide_imovel_rural = i.ide_imovel) ");
		sql.append("LEFT OUTER JOIN pessoa_imovel pi ON (i.ide_imovel = pi.ide_imovel) ");
		sql.append("LEFT OUTER JOIN endereco en ON (i.ide_endereco = en.ide_endereco) ");
		sql.append("LEFT OUTER JOIN logradouro l ON (en.ide_logradouro = l.ide_logradouro) ");
		sql.append("LEFT OUTER JOIN municipio m ON (l.ide_municipio = m.ide_municipio) ");
		sql.append("LEFT OUTER JOIN documento_imovel_rural_posse di ON (ir.ide_imovel_rural = di.ide_imovel_rural) ");
		sql.append("LEFT OUTER JOIN tipo_documento_imovel_rural td ON (di.ide_tipo_documento_imovel_rural = td.ide_tipo_documento_imovel_rural) ");
		sql.append("LEFT OUTER JOIN reserva_legal rl ON (ir.ide_imovel_rural = rl.ide_imovel_rural) ");
		sql.append("LEFT OUTER JOIN status_reserva_legal sr ON (rl.ide_status = sr.ide_status_reserva_legal) ");
		sql.append("LEFT OUTER JOIN contrato_convenio_cefir cc ON (ir.ide_contrato_convenio = cc.ide_contrato_convenio_cefir) ");
		sql.append("LEFT OUTER JOIN gestor_financeiro_cefir gf ON (cc.ide_gestor_financeiro_cefir = gf.ide_gestor_financeiro_cefir) ");
		sql.append("LEFT JOIN pessoa_fisica pf ON (ir.ide_requerente_cadastro = pf.ide_pessoa_fisica) ");
		sql.append("LEFT JOIN pessoa_juridica pj ON (ir.ide_requerente_cadastro = pj.ide_pessoa_juridica) ");
		sql.append("LEFT JOIN requerimento_imovel ri ON (i.ide_imovel = ri.ide_imovel) ");
		sql.append("LEFT JOIN requerimento r ON (ri.ide_requerimento = r.ide_requerimento) ");
		sql.append("LEFT JOIN certificado c ON (c.ide_requerimento = r.ide_requerimento) ");
		sql.append("LEFT JOIN responsavel_imovel_rural resp ON (ir.ide_imovel_rural = resp.ide_imovel_rural) ");
		sql.append("LEFT JOIN pessoa_fisica pfr ON (pfr.ide_pessoa_fisica = resp.ide_pessoa_fisica) ");
		
		if(filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			sql.append("INNER JOIN imovel_rural_sicar s ON (s.ide_imovel_rural = ir.ide_imovel_rural) ");
		}else{
			sql.append("LEFT JOIN imovel_rural_sicar s ON (s.ide_imovel_rural = ir.ide_imovel_rural) ");
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) && filtroImovelRuralRelatorioDTO.getTipoArl() > 0) {
			sql.append("LEFT JOIN tipo_arl trl ON (trl.ide_tipo_arl = rl.ide_tipo_arl) ");			
		}
		
		sql.append("WHERE i.ind_excluido = false ");
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProprietario())){
			sql.append("  AND (pi.ide_tipo_vinculo_imovel in (1, 2) AND pi.ide_pessoa = " + filtroImovelRuralRelatorioDTO.getIdeProprietario() + ")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProcurador())){
			sql.append("  AND ir.ide_imovel_rural in (SELECT pi2.ide_imovel FROM pessoa_imovel pi2 WHERE pi2.ide_tipo_vinculo_imovel = 5 AND pi2.ide_pessoa = " + filtroImovelRuralRelatorioDTO.getIdeProcurador() + ")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNomImovelRural())){
			sql.append("AND remover_acentuacao_uppercase(ir.des_denominacao) ILIKE '%'||remover_acentuacao_uppercase('" + filtroImovelRuralRelatorioDTO.getNomImovelRural().replace("'", "''") + "')||'%' ");
		}
								
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListMunicipio())){
			sql.append("  AND m.ide_municipio IN ("+ convertListToString(filtroImovelRuralRelatorioDTO.getListMunicipio()) +")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListStatusCadastro())){
			sql.append("  AND ir.status_cadastro IN ("+ convertListToString(filtroImovelRuralRelatorioDTO.getListStatusCadastro()) +")");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getStatusPendente())){
			if (filtroImovelRuralRelatorioDTO.getStatusPendente()) {
				sql.append("  AND ir.ind_suspensao = " + filtroImovelRuralRelatorioDTO.getStatusPendente());
			}else {				
				sql.append("  AND (ir.ind_suspensao = " + filtroImovelRuralRelatorioDTO.getStatusPendente() + " OR ir.ind_suspensao is null) ");
			}
		}
															     
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
			
			if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
					&& Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND (ir.dtc_primeira_finalizacao BETWEEN '" + dataInicio +" 00:00:00' AND now()) AND status_cadastro <> 0");
			
			} else if (Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {				
			
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND ir.dtc_primeira_finalizacao BETWEEN '2012-01-01 00:00:00' AND '" + dataFim +" 23:59:59' AND status_cadastro <> 0");
			
			} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {				
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao()) : sdfymd.format(new Date());
				sql.append("  AND ir.dtc_primeira_finalizacao BETWEEN '" + dataInicio +" 00:00:00' AND '" + dataFim +" 23:59:59' AND status_cadastro <> 0");			
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
			
			if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
					&& Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND (s.dtc_primeira_sincronia_com_sucesso BETWEEN '" + dataInicio +" 00:00:00' AND now()) ");
			
			} else if (Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {				
			
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND s.dtc_primeira_sincronia_com_sucesso BETWEEN '2012-01-01 00:00:00' AND '" + dataFim +" 23:59:59' ");
			
			} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
						&& !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {				
				
				String dataInicio = filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				String dataFim = filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() != null ? sdfymd.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao()) : sdfymd.format(new Date());
				sql.append("  AND s.dtc_primeira_sincronia_com_sucesso BETWEEN '" + dataInicio +" 00:00:00' AND '" + dataFim +" 23:59:59' ");			
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDocumentoFinal())) {
			if(filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(2) || filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(3)) {
				if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNumCertificado())) {
					sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false AND c.num_certificado ilike '%" + filtroImovelRuralRelatorioDTO.getNumCertificado() + "%') ");
				} else {
					if (filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(2)){
						sql.append(" AND ir.sts_certificado = true ");
					} else {
						sql.append(" AND ir.sts_certificado = false ");
					}
					sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false) ");
				}
				
				if(Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListStatusCadastro())) {
					sql.append("  AND ir.status_cadastro = 3 ");
				}
			} else if(filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(4)) {
				sql.append("  AND (c.num_token is not null AND c.ide_tipo_certificado = " + filtroImovelRuralRelatorioDTO.getDocumentoFinal() + " AND ri.ind_excluido=false) ");
			}
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())){
			sql.append("  AND gf.ide_gestor_financeiro_cefir in (" + convertListToString(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro()) + ") ");
		}
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())){
			sql.append("  AND cc.ide_contrato_convenio_cefir in (" + convertListToString(filtroImovelRuralRelatorioDTO.getListContratoConvenio()) + ") ");
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais() > 0){
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(1)) {
				sql.append("  AND ir.qtd_modulo_fiscal <=  4 ");	
			} else if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(2)) {
				sql.append("  AND ir.qtd_modulo_fiscal >  4 ");
			}			
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) && filtroImovelRuralRelatorioDTO.getTipoArl() > 0) {
			sql.append("  AND trl.ide_tipo_arl = "+filtroImovelRuralRelatorioDTO.getTipoArl()+" ");			
		}

		if(filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = "+TIPO_CADASTRO_IMOVEL_RURAL_CDA+" ");
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(1)) {
			sql.append("  AND ir.ind_deseja_completar_informacoes = true ");
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(2)) {
			sql.append("  AND ir.ind_deseja_completar_informacoes = false ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()) {
			sql.append("  AND (ir.ide_imovel_rural IN(SELECT irrl.ide_imovel_rural FROM imovel_rural irrl ");
			sql.append("       INNER JOIN imovel irl ON (irrl.ide_imovel_rural = irl.ide_imovel AND irl.ind_excluido = false) ");
			sql.append("       INNER JOIN reserva_legal rl ON (irrl.ide_imovel_rural = rl.ide_imovel_rural AND rl.ide_tipo_estado_conservacao <> 1) ");
			sql.append("       INNER JOIN cronograma_recuperacao crrl ON (rl.ide_reserva_legal = crrl.ide_reserva_legal)) ");
			sql.append("  OR ir.ide_imovel_rural IN (SELECT irapp.ide_imovel_rural FROM imovel_rural irapp ");
			sql.append("     INNER JOIN imovel iapp ON (irapp.ide_imovel_rural = iapp.ide_imovel AND iapp.ind_excluido = false) ");
			sql.append("     INNER JOIN app app ON (irapp.ide_imovel_rural = app.ide_imovel_rural AND app.ide_tipo_estado_conservacao <> 1) ");
			sql.append("     INNER JOIN cronograma_recuperacao crapp ON (app.ide_app = crapp.ide_app))) ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {
			sql.append("  AND ir.ind_outros_passivos = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isRlAprovada()) {
			sql.append("  AND rl.ide_status = 1 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isLimiteBloqueado()) {
			sql.append("  AND ir.ind_bloqueio_limite = true ");
		}

		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()) {
			sql.append("  AND rl.ind_sobreposicao_app = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {
			sql.append("  AND ind_menor_vinte_porcento = true ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			sql.append("  AND s.num_sicar IS NOT NULL ");			
		}
		
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 4 ");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 3 ");
		}
		
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 1 ");
		} else if (filtroImovelRuralRelatorioDTO.isModuloAssentamento()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 2 ");
		} else if (filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			sql.append("  AND ir.ide_tipo_cadastro_imovel_rural = 5 ");
		}
		
		sql.append(" ORDER BY ir.ide_imovel_rural DESC ");
		


		return sql;
	}
	
	@Override
	public StringBuilder montarConsultaImovelRuralRelatorioGEO(StringBuilder sql, FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		
		StringBuilder sqlGEO = new StringBuilder();
		sqlGEO.append("SELECT t.* ");
		sqlGEO.append("FROM ( ");
		sqlGEO.append(sql);		
		sqlGEO.append(") t ");
		sqlGEO.append("INNER JOIN localizacao_tabela_espacial lte ON lte.ide_localizacao_geografica = t.ide_localizacao_geografica ");
		sqlGEO.append("INNER JOIN tabela_espacial te ON te.ide_tabela_espacial = lte.ide_tabela_espacial ");
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Bioma' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'RPGA' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdRPGASelecionado().getGid() +" ");			
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Unidade de Conservação' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Território Identidade' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Bacia' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado().getGid() +" ");
		}
		return sqlGEO;
	}
	
	@Override
	public StringBuilder montarConsultaImovelRuralRelatorioGEOSoImovel(StringBuilder sql, FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		
		StringBuilder sqlGEO = new StringBuilder();
		sqlGEO.append("SELECT t.* ");
		sqlGEO.append("FROM ( ");
		sqlGEO.append(sql);		
		sqlGEO.append(") t ");		
		sqlGEO.append("INNER JOIN imovel_rural imo on t.ideImovelRural = imo.ide_imovel_rural ");
		sqlGEO.append("INNER JOIN localizacao_geografica loc on loc.ide_localizacao_geografica = imo.ide_localizacao_geografica ");
		sqlGEO.append("INNER JOIN localizacao_tabela_espacial lte ON lte.ide_localizacao_geografica = loc.ide_localizacao_geografica ");
		sqlGEO.append("INNER JOIN tabela_espacial te ON te.ide_tabela_espacial = lte.ide_tabela_espacial ");		
		
		if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Bioma' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'RPGA' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdRPGASelecionado().getGid() +" ");			
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Unidade de Conservação' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Território Identidade' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado().getGid() +" ");
		} else if (!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado())) {
			sqlGEO.append("WHERE te.dsc_tabela_espacial = 'Bacia' AND lte.gid_tabela_espacial = " + filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado().getGid() +" ");
		}
		return sqlGEO;
	}
	
	private String convertListToString(List<Integer> litInteger) {
		String listaConvertida = "";
		for (Integer integer : litInteger) {
			if(listaConvertida.isEmpty())
				listaConvertida = integer.toString();
			else
				listaConvertida += ", "+ integer.toString(); 
		}
		return listaConvertida;
	}
	
	public String montarConsultaImovelRuralRelatorioQuantitativo(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, ColunasExibirRelatorioQuantitativoImoveisRuraisDTO colunasExibirRelatorioQuantitativoImoveisRuraisDTO, String listaIdImovel) {
		
		StringBuilder sql = new StringBuilder();
		final boolean selecionadoImovel = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isImoveisRurais(); 
		final boolean selecionadoReservaLegal = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isReservaLegal();
		final boolean selecionadoAreaProdutiva = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isAreaProdutiva();
		final boolean selecionadoVegetacaoNativa = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isVegetacaoNativa();
		final boolean selecionadoApp = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isApp();
		final boolean selecionadoOutrosPassivos = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isOutrosPassivos();
		final boolean selecionadoRppn = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isRppn();
		final boolean selecionadoUsoAgua = colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isUsoAgua();
		
		if(selecionadoImovel){
			sql.append(addConsultaImovelRural(filtroImovelRuralRelatorioDTO));
		}
		
		if(selecionadoReservaLegal){
			addUnion(sql);
			sql.append(addReservaLegal(filtroImovelRuralRelatorioDTO));
		}
		
		if(selecionadoApp){
			addUnion(sql);
			sql.append(addConsultaApp(filtroImovelRuralRelatorioDTO));
		}
		
		if(selecionadoAreaProdutiva){
			addUnion(sql);
			sql.append(addConsultaAreaProdutiva(filtroImovelRuralRelatorioDTO));
		}
		
		if(selecionadoVegetacaoNativa){
			addUnion(sql);
			sql.append(addConsultaVegetacaoNativa(filtroImovelRuralRelatorioDTO));
		}

		if(selecionadoRppn){
			addUnion(sql);
			sql.append(addConsultaRppn(filtroImovelRuralRelatorioDTO));
		}	
		
		if(selecionadoUsoAgua){
			addUnion(sql);
			sql.append(addConsultaUsoAgua(filtroImovelRuralRelatorioDTO));
		}
		
		if(selecionadoOutrosPassivos){
			addUnion(sql);
			sql.append(addConsultaOutrosPassivos(filtroImovelRuralRelatorioDTO));
		}
		
		
		return sql.toString();
 	}
	
	private void addUnion(StringBuilder sql) {
		if(sql.length()>0){
			sql.append(" UNION ALL ");
		}
	}
	
	public String addConsultaImovelRural (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Imóveis rurais'::text AS tipo,"
				+ " COUNT(distinct ir.ide_imovel_rural) AS total,"
				+ " SUM(ir.val_area) AS area"
				+ "	FROM imovel_rural ir"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	public String addReservaLegal (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Reserva legal'::text AS tipo,"
			    + " COUNT(rl.ide_reserva_legal) AS total,"
			    + " SUM(rl.val_area) AS area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN reserva_legal rl ON (ir.ide_imovel_rural = rl.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");		
		return sql.toString();
	}

	public String addConsultaApp (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Área de preservação permanente'::text AS tipo,"
			    + " COUNT(a.ide_app) AS total,"
			    + " SUM(a.val_area) as area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN app a ON (a.ide_imovel_rural = ir.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	public String addConsultaAreaProdutiva (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Atividade desenvolvida'::text AS tipo,"
			    + " COUNT(ap.ide_area_produtiva) AS total,"
			    + " SUM(ap.val_area) as area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN area_produtiva ap ON (ir.ide_imovel_rural = ap.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	public String addConsultaVegetacaoNativa (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Vegetação nativa'::text AS tipo,"
			    + " COUNT(vn.ide_vegetacao_nativa) AS total,"
			    + " SUM(vn.val_area) AS area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN vegetacao_nativa vn ON (ir.ide_imovel_rural = vn.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	public String addConsultaUsoAgua (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Uso da água'::text AS tipo,"
			    + " COUNT(irua.ide_imovel_rural_uso_agua) AS total,"
			    + " NULL AS area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN imovel_rural_uso_agua irua ON (ir.ide_imovel_rural = irua.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");

		return sql.toString();
	}
	
	public String addConsultaOutrosPassivos (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'Outros passivos'::text AS tipo,"
			    + " COUNT(opa.ide_outros_passivos_ambientais) AS total,"
			    + " NULL AS area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN outros_passivos_ambientais opa ON (ir.ide_imovel_rural = opa.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	public String addConsultaRppn (FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'RPPN'::text AS tipo,"
			    + " COUNT(rppn.ide_imovel_rural_rppn) AS total,"
			    + " SUM(rppn.val_area) AS area"
				+ "	FROM imovel_rural ir"
				+ "	INNER JOIN imovel_rural_rppn rppn ON (ir.ide_imovel_rural = rppn.ide_imovel_rural)"
				+ "	WHERE ir.ide_imovel_rural IN ("
				+ extrairConsultaTo(filtroImovelRuralRelatorioDTO)
				+") ");
		return sql.toString();
	}
	
	/**
	 * Método responsável por verificar se algum dos campos "Bioma, Bacia, RPGA, Unidade de Conservação, Território de Identidade" foi selecionado 
	 */
	private boolean isAlgumFiltroGeo(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		return !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado());
	}
	
	public String montarConsultaImovelRuralRelatorioQuantitativoObs(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, 
						ColunasExibirRelatorioQuantitativoImoveisRuraisDTO colunasExibirRelatorioQuantitativoImoveisRuraisDTO, 
						String listaIdImovel, int qtdImoveis) {
		
		StringBuilder sql = new StringBuilder();
		Object observacao = "";
		
		if(colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isImoveisRurais() && isAlgumFiltroGeo(filtroImovelRuralRelatorioDTO)) {			
			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado().getGid())) {
				sql.append("SELECT "
						+ " CASE WHEN COUNT(distinct ir.ide_imovel_rural) = 0 THEN '' "
						+ " WHEN COUNT(distinct ir.ide_imovel_rural) = 1 THEN 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' está inserido em mais de um Bioma' "
						+ " ELSE 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' estão inseridos em mais de um Bioma' END AS observacao"
						+ "	FROM imovel_rural ir"
						+ "	WHERE ir.ide_imovel_rural IN (" + extrairConsultaTo(filtroImovelRuralRelatorioDTO) + " ) ");				
			} else if (!Util.isNull(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado().getGid())) {
				sql.append("SELECT "
						+ " CASE WHEN COUNT(distinct ir.ide_imovel_rural) = 0 THEN '' "
						+ " WHEN COUNT(distinct ir.ide_imovel_rural) = 1 THEN 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' está inserido em mais de um RPGA' "
						+ " ELSE 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' estão inseridos em mais de um RPGA' END AS observacao"
						+ "	FROM imovel_rural ir"
						+ "	WHERE ir.ide_imovel_rural IN (" + extrairConsultaTo(filtroImovelRuralRelatorioDTO) + " ) ");				
			} else if (!Util.isNull(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado().getGid())) {
				sql.append("SELECT "
						+ " CASE WHEN COUNT(distinct ir.ide_imovel_rural) = 0 THEN '' "
						+ " WHEN COUNT(distinct ir.ide_imovel_rural) = 1 THEN 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' está inserido em mais de uma Unidade de Conservação' "
						+ " ELSE 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' estão inseridos em mais de uma Unidade de Conservação' END AS observacao"
						+ "	FROM imovel_rural ir"
						+ "	WHERE ir.ide_imovel_rural IN (" + extrairConsultaTo(filtroImovelRuralRelatorioDTO) + " ) ");
			} else if (!Util.isNull(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado().getGid())) {
				sql.append("SELECT "
						+ " CASE WHEN COUNT(distinct ir.ide_imovel_rural) = 0 THEN '' "
						+ " WHEN COUNT(distinct ir.ide_imovel_rural) = 1 THEN 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' está inserido em mais de um Território de Identidade' "
						+ " ELSE 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' estão inseridos em mais de um Território de Identidade' END AS observacao"
						+ "	FROM imovel_rural ir"
						+ "	WHERE ir.ide_imovel_rural IN (" + extrairConsultaTo(filtroImovelRuralRelatorioDTO) + " ) ");
			} else if (!Util.isNull(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado().getGid())) {
				sql.append("SELECT "
						+ " CASE WHEN COUNT(distinct ir.ide_imovel_rural) = 0 THEN '' "
						+ " WHEN COUNT(distinct ir.ide_imovel_rural) = 1 THEN 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' está inserido em mais de uma Bacia' "
						+ " ELSE 'Observação: De "+qtdImoveis+" imóvel(is), ' || COUNT(distinct ir.ide_imovel_rural) || ' estão inseridos em mais de uma Bacia' END AS observacao"
						+ "	FROM imovel_rural ir"
						+ "	WHERE ir.ide_imovel_rural IN (" + extrairConsultaTo(filtroImovelRuralRelatorioDTO) + " ) ");
			}
						
			EntityManager lEntityManager =  DAOFactory.getEntityManager();
			lEntityManager.joinTransaction();
			Query lQuery = lEntityManager.createNativeQuery(sql.toString());
									
			observacao  = lQuery.getResultList();
			observacao = observacao.toString().replace("[", "").replace("]","");
		}		
		return observacao.toString();		
	}	
}
