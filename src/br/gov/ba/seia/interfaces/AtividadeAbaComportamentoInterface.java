package br.gov.ba.seia.interfaces;

import java.util.List;

import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.dto.CaracterizacaoCultivoDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;

public interface AtividadeAbaComportamentoInterface {

	public void adicionarPoligonal(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> poligonais, AquiculturaTipoAtividade aquiculturaTipoAtividade, FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum aquiculturaEnum);

	public void adicionarEspecie(AquiculturaAtividadeDTO aquiculturaAtividade, FceAquiculturaLicenca fceAquiculturaLicenca);

	public boolean validarCaracterizacaoCultivoEngorda(CaracterizacaoCultivoDTO dto, TipoAquiculturaEnum tipoAquiculturaEnum);

	public boolean validarCaracterizacaoCultivoFormasJovens(CaracterizacaoCultivoDTO dto, TipoAquiculturaEnum tipoAquiculturaEnum);

}
