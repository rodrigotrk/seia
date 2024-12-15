package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pessoa;

public interface ImovelRuralDAOIf {
     public List<ImovelRural> listarImovelRuralByProprietario(Pessoa proprietario, int first, int pageSize) ;
     
     public List<ImovelRural> listarImoveisPorProprietarioCompensacao(Pessoa proprietario) ;
     
     public ImovelRural carregarImovelPorId(Integer ideImovelRural) ;

	public Integer countImoveisCadastroIncompleto(ImovelRuralDTO dadosParaBusca);
}
