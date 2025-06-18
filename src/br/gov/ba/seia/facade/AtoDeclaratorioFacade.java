package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.AtoDeclaratorioDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoDeclaratorioFacade {

	
	@EJB
	private ImovelService imovelService;
	
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaFacade;

	@EJB
	private AtoDeclaratorioDAOImpl atoDeclaratorioDAOImpl;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtoDeclaratorio(AtoDeclaratorio atoDeclaratorio) {
		try {
			atoDeclaratorio.setIndConcluido(false);
			atoDeclaratorioDAOImpl.salvar(atoDeclaratorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarAtoDeclaratorio(AtoDeclaratorio atoDeclaratorio) {
		try {
			atoDeclaratorio.setIndConcluido(true);
			atoDeclaratorioDAOImpl.salvar(atoDeclaratorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoDeclaratorio buscarAtoDeclaratorioBy(Requerimento requerimento, DocumentoObrigatorio docObrigatorio){
		try {
			return atoDeclaratorioDAOImpl.buscar(requerimento, docObrigatorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoDeclaratorio buscarAtoDeclaratorioBy(Requerimento requerimento, DocumentoObrigatorioEnum docObrigatorioEnum){
		try {
			return buscarAtoDeclaratorioBy(requerimento, new DocumentoObrigatorio(docObrigatorioEnum.getId()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoDeclaratorio> listarAtoDeclaratorioBy(Requerimento requerimento){
		try {
			return atoDeclaratorioDAOImpl.listar(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(ImovelRural imovelRural) {
		try {
			return imovelService.buscarImovelPorNumeroCar(imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarRequerimentoImovelPor(Requerimento requerimento){
		try {
			return imovelService.listarRequerimentoImovelPor(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisicaPorCPF(PessoaFisica pessoaFisica){
		try {
			return pessoaFisicaDAOImpl.buscarPorCPF(pessoaFisica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica localizacaoGeografica){
		if(!Util.isNullOuVazio(localizacaoGeografica)){
			return !Util.isNullOuVazio(retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return localizacaoGeograficaFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaLocalizacaoGeografica(List<LocalizacaoGeografica> lista) {
		try {
			if(!Util.isNullOuVazio(lista)){
				for(LocalizacaoGeografica localizacaoGeografica : lista){
					excluirLocalizacaoGeografica(localizacaoGeografica);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		try {
			localizacaoGeograficaFacade.excluirDadoGeografico(localizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoaFisica(PessoaFisica pf) {
		try {
			pessoaFisicaDAOImpl.salvarOuAtualizarPessoaFisica(pf);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarPonto(LocalizacaoGeografica localizacaoGeografica){
		if(!Util.isNullOuVazio(localizacaoGeografica)){
			localizacaoGeograficaFacade.tratarPonto(localizacaoGeografica);
		}
	}
	
}