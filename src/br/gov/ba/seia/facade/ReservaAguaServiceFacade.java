package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceReservaAgua;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.MotivoReservaAgua;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.entity.StatusReservaAgua;
import br.gov.ba.seia.enumerator.StatusReservaAguaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.FceReservaAguaService;
import br.gov.ba.seia.service.MotivoReservaAguaService;
import br.gov.ba.seia.service.ReservaAguaService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaAguaServiceFacade {

	@EJB
	private ReservaAguaService reservaAguaService;
	
	@EJB
	private FceReservaAguaService fceReservaAguaService;
	
	@EJB
	private AreaService areaService;
	
	@EJB
	private MotivoReservaAguaService motivoReservaAguaService;
	
	private static final StatusReservaAgua RESERVADO = new StatusReservaAgua(StatusReservaAguaEnum.RESERVADO);
	private static final StatusReservaAgua CANCELADO = new StatusReservaAgua(StatusReservaAguaEnum.CANCELADO);

	/**
	 * Método que vai persistir a entidade {@link ReservaAgua} no banco de dados para RESERVAR a Água.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @param funcionario
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reservarAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, Funcionario funcionario) throws Exception{
		persistir(RESERVADO, fceOutorgaLocalizacaoGeografica, funcionario);
	}
	
	/**
	 * Método que vai persistir a entidade {@link ReservaAgua} no banco de dados CANCELAR a Reserva de Água.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @param funcionario
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelarReservaAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, Funcionario funcionario) throws Exception{
		persistir(CANCELADO, fceOutorgaLocalizacaoGeografica, funcionario);
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @param funcionario
	 * @param justificativa 
	 * @param motivoReservaAgua 
	 * @throws Exception
	 * @since 24/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void cancelarReservaAguaByCoordenador(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, Funcionario funcionario, MotivoReservaAgua motivoReservaAgua, String justificativa) {
		FceReservaAgua fceReservaAgua = criarFceReservaAgua(fceOutorgaLocalizacaoGeografica.getIdeFce());
		salvarFceReservaAgua(fceReservaAgua);
		ReservaAgua reservaAgua = new ReservaAgua(fceReservaAgua, CANCELADO, fceOutorgaLocalizacaoGeografica, funcionario);
		reservaAgua.setIdeMotivoReservaAgua(motivoReservaAgua);
		reservaAgua.setDscStatusReservaAgua(justificativa);
		salvarReservaAgua(reservaAgua);
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @param funcionario
	 * @throws Exception
	 * @since 22/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	private void persistir(StatusReservaAgua status, FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, Funcionario funcionario) {
		FceReservaAgua fceReservaAgua = criarFceReservaAgua(fceOutorgaLocalizacaoGeografica.getIdeFce());
		salvarFceReservaAgua(fceReservaAgua);
		ReservaAgua reservaAgua = new ReservaAgua(fceReservaAgua, status, fceOutorgaLocalizacaoGeografica, funcionario);
		salvarReservaAgua(reservaAgua);
		fceOutorgaLocalizacaoGeografica.getListaReservaAgua().add(reservaAgua);
	}

	private FceReservaAgua criarFceReservaAgua(Fce fce) {
		return new FceReservaAgua(fce);
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param reservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarReservaAgua(ReservaAgua reservaAgua)  {
		reservaAguaService.salvarReservaAgua(reservaAgua);
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceReservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceReservaAgua(FceReservaAgua fceReservaAgua)  {
		fceReservaAguaService.salvarFceReservaAgua(fceReservaAgua);
	}
	
	/**
	 * Método responsável por remover a {@link ReservaAgua} de acordo com o {@link ReservaAgua} passada no parâmetro.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param reservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerReservaAgua(ReservaAgua reservaAgua) throws Exception{
		excluir(reservaAgua);
	}
	
	/**
	 * Método responsável por remover a {@link ReservaAgua} de acordo com o {@link FceOutorgaLocalizacaoGeografica} passado no parâmetro.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerReservaAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception{
		excluir(buscarReservarAgua(fceOutorgaLocalizacaoGeografica));
	}

	/**
	 * Método responsável por excluir o {@link FceReservaAgua} e a {@link ReservaAgua}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param reservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluir(ReservaAgua reservaAgua) throws Exception {
		excluirFceReservaAgua(reservaAgua);
		excluirReservaAgua(reservaAgua);
	}

	/**
	 * Método para exluir a {@link ReservaAgua}
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param reservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirReservaAgua(ReservaAgua reservaAgua)  {
		reservaAguaService.excluirReservaAgua(reservaAgua);
	}

	/**
	 * Método para exluir o {@link FceReservaAgua}
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param reservaAgua
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirFceReservaAgua(ReservaAgua reservaAgua) {
		fceReservaAguaService.excluirFceReservaAgua(reservaAgua);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelarReservarAgua(Integer ideProcesso, Funcionario coordenadorNout, MotivoReservaAgua motivoReservaAgua, String justificativa) throws Exception{
		List<ReservaAgua> listaReservaAgua = reservaAguaService.listarReservaAguaBy(ideProcesso);
		for(ReservaAgua aguaReservada : listaReservaAgua){
			cancelarReservaAguaByCoordenador(aguaReservada.getIdeFceOutorgaLocalizacaoGeografica(), coordenadorNout, motivoReservaAgua, justificativa);
		}
	}

	/**
	 * Método que busca a {@link ReservaAgua} de acordo com o {@link FceOutorgaLocalizacaoGeografica}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceOutorgaLocalizacaoGeografica
	 * @return
	 * @throws Exception
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaAgua buscarReservarAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		return reservaAguaService.buscarUltimaReservaAgua(fceOutorgaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ReservaAgua> listarReservarAgua(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		return reservaAguaService.listarReservaAgua(fceOutorgaLocalizacaoGeografica);
	}
	
	
	public List<MotivoReservaAgua> listarMotivoReservaAguaByStatusCancelado(){
		return motivoReservaAguaService.listarMotivoReservaAgua(StatusReservaAguaEnum.CANCELADO);
	}
	
	public List<MotivoReservaAgua> listarMotivoReservaAguaByStatusReservado() {
		return motivoReservaAguaService.listarMotivoReservaAgua(StatusReservaAguaEnum.RESERVADO);
	}
}