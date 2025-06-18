package br.gov.ba.seia.util.auditoria;

import java.util.HashMap;
import java.util.Map;
/**
 * Classe map para auditoria
 * @author 
 *
 */
public final class AuditoriaMaps {
	public final static Map<Integer, String> tipoVinculoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoInsercaoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> sistemaCoordenadaLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoReservaLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> estadoDeConservacaoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoRecuperacaoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoOrigemCertificadoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoDeProducaoLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoAreaPreservacaoPermanenteLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> statusImovelLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> tipoUsoAguaLabel = new HashMap<Integer, String>();
	public final static Map<Integer, String> statusReservaLegalLabel = new HashMap<Integer, String>();

	static {
			tipoVinculoLabel.put(1, "Proprietário");
			tipoVinculoLabel.put(2, "Justo Possuidor");
			tipoVinculoLabel.put(3, "Conveniado");
			tipoVinculoLabel.put(4, "Atendente");
			tipoVinculoLabel.put(5, "Procurador");
			
			tipoInsercaoLabel.put(1,"Ponto");
			tipoInsercaoLabel.put(2,"Shapefile");
			tipoInsercaoLabel.put(3,"Desenho");
	
			sistemaCoordenadaLabel.put(1, "Geográfica SAD69");
			sistemaCoordenadaLabel.put(2, "UTM 23 SAD69");
			sistemaCoordenadaLabel.put(3, "UTM 24 SAD69");
			sistemaCoordenadaLabel.put(4, "Geográfica SIRGAS 2000");
			sistemaCoordenadaLabel.put(5, "UTM 23 SIRGAS 2000");
			sistemaCoordenadaLabel.put(6, "UTM 24 SIRGAS 2000");
	
			tipoReservaLabel.put(1, "Em condomínio");
			tipoReservaLabel.put(2, "No próprio imóvel");
			tipoReservaLabel.put(3, "Em compensação entre imóveis de mesmo proprietário");
			tipoReservaLabel.put(4, "Em compensação por Servidão ambiental");
	
			estadoDeConservacaoLabel.put(1, "Preservada");
			estadoDeConservacaoLabel.put(2, "Parcialmente Degradada");
			estadoDeConservacaoLabel.put(3, "Degradada");
			
			tipoRecuperacaoLabel.put(1, "Correções topográficas");
			tipoRecuperacaoLabel.put(2, "Correções do solo");
			tipoRecuperacaoLabel.put(3, "Isolamento da Área");
			tipoRecuperacaoLabel.put(4, "Retirada dos fatores de degradação");
			tipoRecuperacaoLabel.put(5, "Preparação e espaçamento das covas");
			tipoRecuperacaoLabel.put(6, "Plantio por categoria regenerativa(distribuição das espécies)");
			tipoRecuperacaoLabel.put(7, "Manutenção, contemplando: controle de pragas, adubação ou irrigação, quando couber");
			tipoRecuperacaoLabel.put(8, "Implementação de equipamentos atrativos para a avifauna");
			tipoRecuperacaoLabel.put(9, "Outro");
		
			tipoOrigemCertificadoLabel.put(1, "Federal");
			tipoOrigemCertificadoLabel.put(2, "Estadual");
			tipoOrigemCertificadoLabel.put(3, "Municipal");

			tipoAreaPreservacaoPermanenteLabel.put(1, "Faixa marginal de curso d’água natural");
			tipoAreaPreservacaoPermanenteLabel.put(2, "Entorno dos lagos e lagoas naturais");
			tipoAreaPreservacaoPermanenteLabel.put(3, "Entorno de reservatório d’água artificial");
			tipoAreaPreservacaoPermanenteLabel.put(4, "Entorno de nascentes e de olhos d’água perenes");
			tipoAreaPreservacaoPermanenteLabel.put(5, "Encosta com declividade superior a 45º");
			tipoAreaPreservacaoPermanenteLabel.put(6, "Restinga fixadora de duna ou estabilizadora de mangue");
			tipoAreaPreservacaoPermanenteLabel.put(7, "Manguezal");
			tipoAreaPreservacaoPermanenteLabel.put(8, "Borda de tabuleiro ou chapada");
			tipoAreaPreservacaoPermanenteLabel.put(9, "Topo de morro, montes, montanhas e serras");
			tipoAreaPreservacaoPermanenteLabel.put(10, "Área em altitude superior a 1800m");
			tipoAreaPreservacaoPermanenteLabel.put(11, "Veredas");
			tipoAreaPreservacaoPermanenteLabel.put(12, "Áreas declaradas de interesse social");
			
			tipoDeProducaoLabel.put(4, "Agricultura");
			tipoDeProducaoLabel.put(7, "Criação de Animais");
			tipoDeProducaoLabel.put(22, "Silvicultura");
			
			statusImovelLabel.put(0, "Registro incompleto");
			statusImovelLabel.put(1, "Registrado");
			statusImovelLabel.put(2, "Cadastro incompleto");
			statusImovelLabel.put(3, "Cadastrado");
			
			tipoUsoAguaLabel.put(1, "Subterrâneo");
			tipoUsoAguaLabel.put(2, "Superficial");
			tipoUsoAguaLabel.put(3, "Lançamento");
			tipoUsoAguaLabel.put(4, "Intervenção");

			statusReservaLegalLabel.put(1, "Aprovada");
			statusReservaLegalLabel.put(2, "Averbada");
			statusReservaLegalLabel.put(3, "Cadastrada");
			statusReservaLegalLabel.put(4, "Aguardando validação de documento");
			statusReservaLegalLabel.put(5, "Com pendência");
			statusReservaLegalLabel.put(6, "Com pendência na documentação");
			statusReservaLegalLabel.put(7, "Não cadastrada");
			statusReservaLegalLabel.put(8, "Aprovada aguardando validação");
			statusReservaLegalLabel.put(9, "Documento não validado");
			statusReservaLegalLabel.put(10, "Aprovada aguardando conclusão do processo");
			statusReservaLegalLabel.put(11, "Realocada");

	}
}
