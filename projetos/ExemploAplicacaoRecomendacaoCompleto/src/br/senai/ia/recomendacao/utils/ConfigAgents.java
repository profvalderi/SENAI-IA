/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao.utils;

/**
 *
 * @author Guilherme
 */
public class ConfigAgents {
        
    // constantes da classe para usar nos parâmetros = {Superlotado,Atrazado,Satisfatório}
    public final static String STATUS_PRODUCAO_SATISFATORIA = "Satisfatório";
    public final static String STATUS_PRODUCAO_SUPERLOTADA = "Superlotado";
    public final static String STATUS_PRODUCAO_ATRAZADA = "Atrazado";
    
    // constante para tempo entre peças em ms (agente linha de produção)
    public final static int TEMPO_MONGAGEM = 1000;
    
    // variável para indicar a quantidade de dias para o processo de classificação
    private int diasEntregaClassificacao;
    /**
     * Variáveis para tomada de decisão dos agentes
     */
    private double limiarSuperiorInferiorRegressao;  //threshold regressão
    private double probabilidadeMinimaClassificacao; //threshold probabilidade para recomendação classificação

    public ConfigAgents(int diasEntregaClassificacao, double limiarRegressao, double probabilidadeMinimaClassificacao) {
        this.diasEntregaClassificacao = diasEntregaClassificacao;
        this.limiarSuperiorInferiorRegressao = limiarRegressao;
        this.probabilidadeMinimaClassificacao = probabilidadeMinimaClassificacao;
    }

    public ConfigAgents() {
    }

    public int getDiasEntregaClassificacao() {
        return diasEntregaClassificacao;
    }

    public void setDiasEntregaClassificacao(int diasEntregaClassificacao) {
        this.diasEntregaClassificacao = diasEntregaClassificacao;
    }

    public double getLimiarSuperiorInferiorRegressao() {
        return limiarSuperiorInferiorRegressao;
    }

    public void setLimiarSuperiorInferiorRegressao(double limiarSuperiorInferiorRegressao) {
        this.limiarSuperiorInferiorRegressao = limiarSuperiorInferiorRegressao;
    }

    public double getProbabilidadeMinimaClassificacao() {
        return probabilidadeMinimaClassificacao;
    }

    public void setProbabilidadeMinimaClassificacao(double probabilidadeMinimaClassificacao) {
        this.probabilidadeMinimaClassificacao = probabilidadeMinimaClassificacao;
    }
    
}
