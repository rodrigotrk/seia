package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.Organismo;
import br.gov.ba.seia.entity.SistemaCultivo;
import br.gov.ba.seia.entity.TipoInstalacao;
import br.gov.ba.seia.enumerator.TipoProducaoEnum;


public class CaracterizacaoCultivoDTO {

	private FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao;
	private List<SistemaCultivo> listaSistemaCultivo;
	private List<TipoInstalacao> listaTipoInstalacao;
	private List<Organismo> listaOrganismo;

	public CaracterizacaoCultivoDTO() {

	}

	public CaracterizacaoCultivoDTO(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade, TipoProducaoEnum tipoProducaoEnum) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = new FceAquiculturaLicencaTipoAtividadeTipoProducao(fceAquiculturaLicencaTipoAtividade, tipoProducaoEnum);
	}

	/* Getters && Setters */
	public List<SistemaCultivo> getListaSistemaCultivo() {
		return listaSistemaCultivo;
	}
	public void setListaSistemaCultivo(List<SistemaCultivo> listaSistemaCultivo) {
		this.listaSistemaCultivo = listaSistemaCultivo;
	}
	public List<TipoInstalacao> getListaTipoInstalacao() {
		return listaTipoInstalacao;
	}
	public void setListaTipoInstalacao(List<TipoInstalacao> listaTipoInstalacao) {
		this.listaTipoInstalacao = listaTipoInstalacao;
	}
	public List<Organismo> getListaOrganismo() {
		return listaOrganismo;
	}
	public void setListaOrganismo(List<Organismo> listaOrganismo) {
		this.listaOrganismo = listaOrganismo;
	}
	
	public FceAquiculturaLicencaTipoAtividadeTipoProducao getFceAquiculturaLicencaTipoAtividadeTipoProducao() {
		return fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}

	public void setFceAquiculturaLicencaTipoAtividadeTipoProducao(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) {
		this.fceAquiculturaLicencaTipoAtividadeTipoProducao = fceAquiculturaLicencaTipoAtividadeTipoProducao;
	}
}