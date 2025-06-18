package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.primefaces.event.RowEditEvent;

import br.gov.ba.seia.entity.EmpreendimentoImovel;
import br.gov.ba.seia.entity.ImovelRural;

@Named("declaracaoLimpezaArea")
@ViewScoped
public class DeclaracaoLimpezaAreaController {

	private DataModel<ImovelRural> dataImovel;
	private DataModel<EmpreendimentoImovel> dataEmpreendimentoImovel;

	@PostConstruct
	public void init() {
		List<ImovelRural> listImovelRural = new ArrayList<ImovelRural>();

		ImovelRural imovel = new ImovelRural();
		imovel.setDesDenominacao("Fazenda Santa Maria");
		imovel.setDesComarca("Rodovia BR-324 KM 10 Salvador BA");
		listImovelRural.add(imovel);

		ImovelRural imovel2 = new ImovelRural();
		imovel2.setDesDenominacao("Fazenda Estrela");
		imovel2.setDesComarca("Rodovia BR-101 KM 10 Ilheus BA");
		listImovelRural.add(imovel2);

		ImovelRural imovel3 = new ImovelRural();
		imovel3.setDesDenominacao("Aiô Silver");
		imovel3.setDesComarca("Rodovia BR-171 KM 43 Ilheus BA");
		listImovelRural.add(imovel3);

		dataImovel = new ListDataModel<ImovelRural>(listImovelRural);

		List<EmpreendimentoImovel> listEmpreendimentoImovel = new ArrayList<EmpreendimentoImovel>();
		
		EmpreendimentoImovel empreendimentoImovel = new EmpreendimentoImovel();
		empreendimentoImovel.setDenominacao("Denominação do imóvel 1");
		empreendimentoImovel.setAreaIntervencao("500");
		empreendimentoImovel.setLatitude("10° 20' 33''");
		empreendimentoImovel.setLongitude("10° 20' 33''");
		empreendimentoImovel.setPontoReferencia("Lorem Ipsum");
		empreendimentoImovel.setData(new Date());
		listEmpreendimentoImovel.add(empreendimentoImovel);

		EmpreendimentoImovel empreendimentoImovel2 = new EmpreendimentoImovel();
		empreendimentoImovel2.setDenominacao("Denominação do imóvel 2");
		empreendimentoImovel2.setAreaIntervencao("500");
		empreendimentoImovel2.setLatitude("10° 20' 33''");
		empreendimentoImovel2.setLongitude("10° 20' 33''");
		empreendimentoImovel2.setPontoReferencia("Lorem Ipsum");
		empreendimentoImovel2.setData(new Date());
		listEmpreendimentoImovel.add(empreendimentoImovel2);

		EmpreendimentoImovel empreendimentoImovel3 = new EmpreendimentoImovel();
		empreendimentoImovel3.setDenominacao("Denominação do imóvel 3");
		empreendimentoImovel3.setAreaIntervencao("500");
		empreendimentoImovel3.setLatitude("10° 20' 33''");
		empreendimentoImovel3.setLongitude("10° 20' 33''");
		empreendimentoImovel3.setPontoReferencia("Lorem Ipsum");
		empreendimentoImovel3.setData(new Date());
		listEmpreendimentoImovel.add(empreendimentoImovel3);

		this.dataEmpreendimentoImovel = new ListDataModel<EmpreendimentoImovel>(listEmpreendimentoImovel);
	}
	
	public void onEditRow(RowEditEvent event){
		EmpreendimentoImovel empreendimentoImovel = (EmpreendimentoImovel) event.getObject();

	}

	public DataModel<ImovelRural> getDataImovel() {
		return dataImovel;
	}

	public void setDataImovel(DataModel<ImovelRural> dataImovel) {
		this.dataImovel = dataImovel;
	}

	public DataModel<EmpreendimentoImovel> getDataEmpreendimentoImovel() {
		return dataEmpreendimentoImovel;
	}

	public void setDataEmpreendimentoImovel(
			DataModel<EmpreendimentoImovel> dataEmpreendimentoImovel) {
		this.dataEmpreendimentoImovel = dataEmpreendimentoImovel;
	}
}