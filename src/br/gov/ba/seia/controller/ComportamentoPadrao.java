package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.Organismo;
import br.gov.ba.seia.entity.SistemaCultivo;
import br.gov.ba.seia.entity.TipoInstalacao;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.interfaces.AtividadeAbaComportamentoInterface;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


public class ComportamentoPadrao implements AtividadeAbaComportamentoInterface{

	@Override
	public void adicionarEspecie(AquiculturaAtividadeDTO dto, FceAquiculturaLicenca fceAquiculturaLicenca) {
		if(validarEspecie(dto)){
			if(Util.isNullOuVazio(dto.getListaCaracterizacaoCultivo())){
				dto.setListaCaracterizacaoCultivo(new ArrayList<FceAquiculturaLicencaTipoAtividade>());
			}
			for(EspecieAquiculturaTipoAtividade especie : dto.getListaEspecieSelecionada()){
				FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo = new FceAquiculturaLicencaTipoAtividade(especie, fceAquiculturaLicenca);
				if(!dto.getListaCaracterizacaoCultivo().contains(caracterizacaoCultivo)){
					dto.getListaCaracterizacaoCultivo().add(caracterizacaoCultivo);
					dto.getListaEspecie().remove(especie);
				}
				if(especie.isOutrasEspecies()){
					JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
				}
			}
			dto.getListaEspecieSelecionada().clear();
		}
	}

	@Override
	public void adicionarPoligonal(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> poligonais, AquiculturaTipoAtividade aquiculturaTipoAtividade, FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum aquiculturaEnum) {
		poligonais.add(new FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(fceAquiculturaLicenca, aquiculturaTipoAtividade, aquiculturaEnum));
	}

	private boolean validarEspecie(AquiculturaAtividadeDTO dto){
		if(Util.isNullOuVazio(dto.getListaEspecieSelecionada())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma espécie." );
			return false;
		}
		return true;
	}
	
	private boolean validarAreaAndProducao(CaracterizacaoCultivoDTO dto, String unidadeMedida) {
		boolean valido = true;
		if(Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumAreaCultivo())){
			JsfUtil.addErrorMessage("A Área do cultivo (ha) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumProducao())){
			JsfUtil.addErrorMessage("A Produção ("+unidadeMedida+"/ano) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	@Override
	public boolean validarCaracterizacaoCultivoEngorda(CaracterizacaoCultivoDTO dto, TipoAquiculturaEnum tipoAquiculturaEnum) {
		boolean valido = true;
		if(!validarSistemaCultivoAndTipoInstalacao(dto)){
			valido = false;
		}
		if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.VIVEIRO_ESCAVADO) && dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().isTipoInstalacaoRaceways() 
				|| tipoAquiculturaEnum.equals(TipoAquiculturaEnum.TANQUE_REDE)){
			if(Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
				JsfUtil.addErrorMessage("O Volume do cultivo (m³) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}
		}
		if(!validarAreaAndProducao(dto, "tonelada")){
			valido = false;
		}
		return valido;
	}

	@Override
	public boolean validarCaracterizacaoCultivoFormasJovens(CaracterizacaoCultivoDTO dto, TipoAquiculturaEnum tipoAquiculturaEnum) {
		boolean valido = true;
		if(!validarSistemaCultivoAndTipoInstalacao(dto)){
			valido = false;
		}
		if(!validarOrganismo(dto)){
			valido = false;
		}
		if((tipoAquiculturaEnum.equals(TipoAquiculturaEnum.VIVEIRO_ESCAVADO) && dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().isTipoInstalacaoRaceways())
				|| tipoAquiculturaEnum.equals(TipoAquiculturaEnum.TANQUE_REDE)){
			if(Util.isNullOuVazio(dto.getFceAquiculturaLicencaTipoAtividadeTipoProducao().getNumVolumeCultivo())){
				JsfUtil.addErrorMessage("O Volume do cultivo (m³) " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}
		}
		if(!validarAreaAndProducao(dto, "milheiro")){
			valido = false;
		}
		return valido;
	}

	private boolean validarOrganismo(CaracterizacaoCultivoDTO dto) {
		for(Organismo organismo : dto.getListaOrganismo()){
			if(organismo.isRowSelect()){
				return true;
			}
		}
		JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Organismo.");
		return false;
	}

	private boolean validarSistemaCultivoAndTipoInstalacao(CaracterizacaoCultivoDTO dto){
		boolean valido = true;
		if(!validarSistemaCultivo(dto)){
			valido = false;
		}
		if(!validarTipoInstalacao(dto)){
			valido = false;
		}
		return valido;
	}
	
	private boolean validarSistemaCultivo(CaracterizacaoCultivoDTO dto) {
		for(SistemaCultivo sistemaCultivo : dto.getListaSistemaCultivo()){
			if(sistemaCultivo.isRowSelect()){
				return true;
			}
		}
		JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Sistema de Cultivo.");
		return false;
	}
	
	private boolean validarTipoInstalacao(CaracterizacaoCultivoDTO dto) {
		boolean noInstalacao = true;
		for(TipoInstalacao tipoInstalacao : dto.getListaTipoInstalacao()){
			if(tipoInstalacao.isRowSelect()){
				noInstalacao = false;
				if(Util.isNullOuVazio(tipoInstalacao.getNumEstrutura())){
					JsfUtil.addErrorMessage("O Nº de Estruturas " + Util.getString("msg_generica_preenchimento_obrigatorio"));
					return false;
				}
			}
		}
		if(noInstalacao){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um")+" um Tipo de Instalação.");
			return false;
		}
		return true;
	}
}