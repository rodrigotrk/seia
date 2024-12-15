package br.gov.ba.seia.controller;


public class IdentificarPessoaFisicaDocumentoPapelSolicitacaoController {
	
	/*
	
	private List<SelectItem> tipoIdentificacaoList;
	private List<SelectItem> orgaoExpedidorList;
	private List<SelectItem> estadoList;
	private DocumentoIdentificacao documentoIdentificacao;
	private SolicitacaoDTO solicitacao;
	
	public IdentificarPessoaFisicaDocumentoPapelSolicitacaoController() {
	}

	public void prepararSolicitacao(SolicitacaoDTO solicitacao) {
		this.solicitacao = solicitacao;
		documentoIdentificacao = new DocumentoIdentificacao();
	}
	
	public void salvar(){
		if(isValido()){
			try {
				if(solicitacao.getRequerente().getDocumentoIdentificacaoCollection() == null){
					solicitacao.getRequerente().setDocumentoIdentificacaoCollection(new ArrayList<DocumentoIdentificacao>());
				}
				
				documentoIdentificacao.setIdePessoa(solicitacao.getRequerente());
				
				if(!solicitacao.getRequerente().getDocumentoIdentificacaoCollection().contains(documentoIdentificacao)){
					solicitacao.getRequerente().getDocumentoIdentificacaoCollection().add(documentoIdentificacao);
				}
				
				documentoIdentificacao.setDtcCriacao(new Date());
				getFacade().salvarDocumentoIdentificacao(documentoIdentificacao);
				fecharDialog();
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public void excluir(){
		try {
			getFacade().excluirDocumentoIdentificacao(documentoIdentificacao);
			fecharDialog();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void limpar(){
		documentoIdentificacao = new DocumentoIdentificacao();
		Html.getCurrency()
			.update("pessoaFisicaDocumento")
			.show("dialogPessoaFisicaIncluirDocumento");
	}
	
	public void fecharDialog(){
		Html.getCurrency()
			.update("tab_pesssoa:formPessoaFisicaExibirDocumento")
			.hide("dialogPessoaFisicaIncluirDocumento");
	}
	
	public void enviarArquivo(FileUploadEvent event){
		documentoIdentificacao
			.setArquivoEnviado(event.getFile());

		documentoIdentificacao
			.setDscCaminhoArquivo(event.getFile().getFileName());
		
		Html.getCurrency()
			.update(":pessoaFisicaDocumento:pnlEnviarDocumento")
			.update(":pessoaFisicaDocumento:pnlExibirDocumento")
			;
	}

	public void prepararDialog() {
		documentoIdentificacao = new DocumentoIdentificacao();
		documentoIdentificacao.setAcaoTelaEnum(TelaAcaoEnum.INCLUIR);
		
		carregarListas();
	}
	
	public void prepararDocumento(ActionEvent event){
		documentoIdentificacao = (DocumentoIdentificacao) event.getComponent().getAttributes().get("documento");
		documentoIdentificacao.setAcaoTelaEnum(TelaAcaoEnum.getEnum((String) event.getComponent().getAttributes().get("acao")));
		
		carregarListas();
		
		Html.getCurrency()
			.update("pessoaFisicaDocumento")
			.show("dialogPessoaFisicaIncluirDocumento");
	}
	
	
	public void carregarListas(){
		try {
			if(tipoIdentificacaoList == null){
				tipoIdentificacaoList = new ArrayList<SelectItem>();
				for (TipoIdentificacao tipoIdentificacao : getFacade().listarTipoIdentificacao()) {
					tipoIdentificacaoList.add(new SelectItem(tipoIdentificacao, tipoIdentificacao.getNomTipoIdentificacao()));  
				}
			}
			if(orgaoExpedidorList == null){
				orgaoExpedidorList = new ArrayList<SelectItem>();
				for (OrgaoExpedidor orgaoExpedidor : getFacade().listarOrgaoExpedidor()) {
					orgaoExpedidorList.add(new SelectItem(orgaoExpedidor, orgaoExpedidor.getDscOrgaoExpedidor()));  
				}
			}
			if(estadoList == null){
				estadoList = new ArrayList<SelectItem>();

				for (Estado estado : getFacade().listarEstado()) {
					if(estado.getId() == EstadoEnum.BAHIA.getId()){
						estadoList.add(0,new SelectItem(estado, estado.getDesSigla()));
					}
					else{
						estadoList.add(new SelectItem(estado, estado.getDesSigla()));  
					}
				}
			}
			
			Html.getCurrency()
				.update("pessoaFisicaDocumento")
				.show("dialogPessoaFisicaIncluirDocumento");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private boolean isValido(){
		boolean isValido = true;
		if(documentoIdentificacao.getIdeTipoIdentificacao()==null){
			MensagemUtil.msg0003("Tipo de documento");
			isValido = false;
		}
		
		if(documentoIdentificacao.getNumDocumento()==null){
			MensagemUtil.msg0003("Número de documento");
			isValido = false;
		}
		
		if(documentoIdentificacao.getIdeOrgaoExpedidor()==null){
			MensagemUtil.msg0003("Orgão emissor");
			isValido = false;
		}
		
		if(documentoIdentificacao.getIdeEstado()==null){
			MensagemUtil.msg0003("U.F.");
			isValido = false;
		}
		
		if(documentoIdentificacao.getDtcEmissao()==null){
			MensagemUtil.msg0003("Data de emissão");
			isValido = false;
		}
		
		if(documentoIdentificacao.getDscCaminhoArquivo()==null){
			MensagemUtil.msg0003("Documento");
			isValido = false;
		}
		
		return isValido;
	}
	
	public StreamedContent getBaixarArquivo() {
		
		try {
			if(documentoIdentificacao.getDscCaminhoArquivo() != null){
				return getFacade().getArquivo(documentoIdentificacao.getDscCaminhoArquivo());
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			MensagemUtil.erro("Arquivo não encontrado");
		}

		return null;
	}


	/**
	public List<SelectItem> getValuesTipoIdentificacao(){
       return tipoIdentificacaoList;
    }
	
	public List<SelectItem> getValuesOrgaoEmissor(){
      return orgaoExpedidorList;
   }
	
	public List<SelectItem> getValuesEstado(){
      return estadoList;
   }

	public DocumentoIdentificacao getDocumentoIdentificacao() {
		return documentoIdentificacao;
	}

	public void setDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		this.documentoIdentificacao = documentoIdentificacao;
	}
	 * */
}
	