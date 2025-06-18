package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;



public class MotivoSuspensaoCadastroDAOImpl implements MotivoSuspensaoCadastroDAOIf {

	@Inject
	IDAO<MotivoSuspensaoCadastro> motivoSuspensaoCadastroDAO;

	public IDAO<MotivoSuspensaoCadastro> getMotivoSuspensaoCadastroDAO() {
		return motivoSuspensaoCadastroDAO;
	}

	public void setMotivoSuspensaoCadastroDAO(IDAO<MotivoSuspensaoCadastro> motivoSuspensaoCadastroDAO) {
		this.motivoSuspensaoCadastroDAO = motivoSuspensaoCadastroDAO;
	}


	

}
