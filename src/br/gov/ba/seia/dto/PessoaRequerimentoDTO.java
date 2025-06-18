package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.RequerimentoPessoa;

public class PessoaRequerimentoDTO {

       private RequerimentoPessoa atendente = new RequerimentoPessoa();
       private RequerimentoPessoa requerente = new RequerimentoPessoa();
       private RequerimentoPessoa solicitante = new RequerimentoPessoa();

       public RequerimentoPessoa getAtendente() {
               return atendente;
       }

       public void setAtendente(RequerimentoPessoa atendente) {
               this.atendente = atendente;
       }

       public RequerimentoPessoa getRequerente() {
               return requerente;
       }

       public void setRequerente(RequerimentoPessoa requerente) {
               this.requerente = requerente;
       }

       public RequerimentoPessoa getSolicitante() {
               return solicitante;
       }

       public void setSolicitante(RequerimentoPessoa solicitante) {
               this.solicitante = solicitante;
       }

}