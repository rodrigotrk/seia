package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RelatorioDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.enumerator.TipoRelatorioEnum;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.FormaterUtil;
import br.gov.ba.seia.util.JasperUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

public class ImpressoraCerhService {
	
	@Inject
	private JasperUtil relatorio;
	
	public StreamedContent resumoQuantitativoCerh(Map<String, Object> parametros, TipoRelatorioEnum tipoRelatorioEnum) {
		parametros.put("CERH",true);
		parametros.put("PASTA","cerh");
		
		if(tipoRelatorioEnum.equals(TipoRelatorioEnum.XLS)) {
			parametros.put("NOME_RELATORIO", "resumo_quantitativo_detalhe_xls.jasper");
		} else {
			parametros.put("NOME_RELATORIO", "resumo_quantitativo.jasper");
		}
		
		List<String> argumentos = new ArrayList<String>();
		String peridoInicio = "";
		String peridoFinal = "";
		
		try {
			for (Entry<String, Object> entry : parametros.entrySet()) {

				if(entry.getKey().equals("numCadastro")){
					argumentos.add(" (c.num_cadastro ilike '%"+ String.valueOf(entry.getValue()).replace(" ", "").replace("'", "\\'") +"%'"
							+ " OR cp.num_cadastro ilike '%"+ String.valueOf(entry.getValue()).replace(" ", "").replace("'", "\\'") +"%') ");
				}

				else if(entry.getKey().equals("contratoConvenio")){
					argumentos.add(" ( c.ide_contrato_convenio= "+ ((ContratoConvenio) entry.getValue()).getIdeContratoConvenio() +") ");
				}

				else if(entry.getKey().equals("requerente")){
					argumentos.add(" (p.ide_pessoa= "+ entry.getValue()+") ");
				}

				else if(entry.getKey().equals("nomEmpreendimento")){
					argumentos.add(" (e.nom_empreendimento ilike '%"+ String.valueOf(entry.getValue()).replace("'", "\\'") +"%') ");
				}

				else if(entry.getKey().equals("municipio")){
					argumentos.add(" (m.ide_municipio = "+ ((Municipio) entry.getValue()).getIdeMunicipio() +") ");
				}

				else if(entry.getKey().equals("periodoInicio")){
					peridoInicio = FormaterUtil.formatarData(((Date) entry.getValue()),FormaterUtil.formatoBanco);
				}

				else if(entry.getKey().equals("periodoFim")){
					peridoFinal = FormaterUtil.formatarData(DataUtil.finaldoDia((Date) entry.getValue()).getTime(),FormaterUtil.formatoBanco);
				}

				else if(entry.getKey().equals("rpgaSelecionado")){
					argumentos.add(" c.ide_cerh in (select c_.ide_cerh from cerh c_ " +
											" inner join cerh_tipo_uso ctu_ on ctu_.ide_cerh = c_.ide_cerh "+
											" inner join cerh_localizacao_geografica clg_ on clg_.ide_cerh_tipo_uso = ctu_.ide_cerh_tipo_uso "+
											" inner join dado_geografico dg_ on dg_.ide_localizacao_geografica = clg_.ide_localizacao_geografica "+
										" where "  +
											" (St_intersects(dg_.the_geom,(SELECT the_geom FROM geo_rpga WHERE gid = "+ entry.getValue() +"))) "+
											" and c_.ide_cerh = c.ide_cerh) ");
				}

				else if(entry.getKey().equals("cerhTipoStatus")){
					argumentos.add(" (c.ide_cerh_status = "+ entry.getValue()+") ");
				}
		 	}

			if(!"".endsWith(peridoInicio) && !"".endsWith(peridoFinal)){
				
				argumentos.add(" ((c.ide_cerh_pai IS NULL AND c.dtc_cadastro BETWEEN '"+ peridoInicio +"' AND '"+ peridoFinal +"') "
						+ " OR cp.dtc_cadastro BETWEEN '"+ peridoInicio +"' AND '"+ peridoFinal +"') ");
			}
			
			StringBuilder where = new StringBuilder();
			
			if(!argumentos.isEmpty()){
				argumentos.add(" (ee.ide_tipo_endereco = 4) ");
				argumentos.add(" (c.ind_excluido = false or c.ind_excluido is null) ");
				argumentos.add(" (c.ind_historico = false) ");
				where.append(" WHERE ");
				
				for (int x=0; x<argumentos.size();x++) {
					where.append(argumentos.get(x));
					
					if(x<argumentos.size()-1){
						where.append(" AND ");
					}
				}
				
				parametros.put("WHERE", where.toString());
			}

			return relatorio.gerar(null, parametros, tipoRelatorioEnum);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro(Util.getString("msg_generica_erro_imprimir_resumo") + " do CERH.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent resumoCerh(Cerh cerh) {
		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("IDE_CONSULTA", cerh.getIdeCerh());
				parametros.put("PASTA","cerh");

			RelatorioDTO resumoCerh = new RelatorioDTO();
				resumoCerh.setTitulo("Relatório do Cadastro Estadual de Usuários de Recursos Hídricos <br/> CERH");
				resumoCerh.setIdeEmpreendimento(cerh.getIdeEmpreendimento().getIdeEmpreendimento());
				resumoCerh.setIdeRequerente(cerh.getIdePessoaRequerente().getIdePessoa());
				resumoCerh.setTituloData("Data do Cadastro");
				resumoCerh.setData(FormaterUtil.getDataFormatoPtBr(cerh.getDtcCadastro()));
				resumoCerh.setTituloNumero("Número do Cadastro");
				resumoCerh.setNumero(cerh.getNumCadastro());
		
			return relatorio.gerar(resumoCerh, parametros, null);
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do CERH.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	public StreamedContent imprimirCertificado(Cerh cerh) {
		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("ide_cerh", cerh.getIdeCerh());			
				parametros.put("CERH",true);
				parametros.put("NOME_RELATORIO","certificado.jasper");

			return new RelatorioUtil(parametros).gerar();

		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_imprimir_certificado") + " do CERH.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}


}

