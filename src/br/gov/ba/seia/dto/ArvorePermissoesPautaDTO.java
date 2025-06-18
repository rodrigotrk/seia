package br.gov.ba.seia.dto;

import java.io.Serializable;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.Acao;
/**
 * Classe de integração
 * @author 
 *
 */
public class ArvorePermissoesPautaDTO implements Serializable {  
	private static final long serialVersionUID = 1L;

	private TreeNode root;    
    private TreeNode[] selectedNodes;  
    
    /**
     * Construtor
     * @param pauta
     */
    public ArvorePermissoesPautaDTO(String pauta) {    	
        root = new DefaultTreeNode(pauta, null);  
    }
    
    public void addNewNode(Acao acao){
    	new DefaultTreeNode(acao,root);  	
    }
    
    public void addNewNode(Acao acao, boolean selecao){
    	TreeNode node = new DefaultTreeNode(acao,root);
    	node.setSelected(selecao);
    }
    
    
    public void addNewNodeSetParent(Acao acao, int indexParent){
    	new DefaultTreeNode(acao,root.getChildren().get(indexParent));  	
    }
    
    public void addNewNodeSetParent(Acao acao, int indexParent, boolean selecao){
    	TreeNode node = new DefaultTreeNode(acao,root.getChildren().get(indexParent));
    	node.setSelected(selecao);
    }
    
    public void addNewNodeSetParent(String descricao, int indexParent){
    	new DefaultTreeNode(descricao,root.getChildren().get(indexParent));  	
    }
  
    public TreeNode[] getSelectedNodes() {  
        return selectedNodes;  
    }  
  
    public void setSelectedNodes(TreeNode[] selectedNodes) {  
        this.selectedNodes = selectedNodes;  
    }

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	
}