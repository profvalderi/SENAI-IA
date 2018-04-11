/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao;

import br.senai.ia.recomendacao.agentes.AgenteLinhaDeProducao;
import br.senai.ia.recomendacao.agentes.AgenteRecomendador;
import br.senai.ia.recomendacao.modelo.Estoque;
import br.senai.ia.recomendacao.modelo.Peca;
import br.senai.ia.recomendacao.utils.ConfigAgents;
import java.io.File;
import java.util.ArrayList;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;

/**
 *
 * @author Guilherme
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /**
         * Configuração do ambiente
         */
        
        // set up do ambiente de estoque
        ArrayList<Peca> pecas = new ArrayList<Peca>(),pecasProducao;
        pecas.add(new Peca(1, "Parafuso", 150, new String[]{"A","C"}));
        pecas.add(new Peca(2, "Polca", 150, new String[]{"B","C"}));
        pecas.add(new Peca(3, "Rotor", 150, new String[]{"A","B"}));
        pecas.add(new Peca(4, "Engrenagem pequena", 150, new String[]{"A","B"}));
        pecas.add(new Peca(5, "Engrenagem grande", 150, new String[]{"A","B"}));
        pecas.add(new Peca(6, "Motor elétrico 60w", 150, new String[]{"A","D"}));
        pecas.add(new Peca(7, "Bateria 12v 30A", 150, new String[]{"B","D"}));
        
        // set up das variáveis dos agentes
        ConfigAgents config = new ConfigAgents();
        config.setDiasEntregaClassificacao(7);              // quantidades de dias ideal para o agente recomendar encomendar um lote de peças
        config.setProbabilidadeMinimaClassificacao(0.80);   // 80% de precisão para recomendar a encomenda ao usar classificação
        config.setLimiarSuperiorInferiorRegressao(0.5);     // limite de tolerência de erro na regressão para recomendar classificação
        
        // Arquivo de treino para os Classificadores, baixar ele: https://github.com/profvalderi/SENAI-IA/blob/master/bases%20de%20dados/dadosExemploApp.arff
        File arquivoTreino = new File("C:\\Users\\Public\\Documents\\dadosExemploApp.arff"); 
        
        // agente recomendador
        AgenteRecomendador recomendador = new AgenteRecomendador(arquivoTreino,config,new MultilayerPerceptron(), new  LinearRegression());
        //AgenteRecomendador recomendador = new AgenteRecomendador(arquivoTreino,config,new NaiveBayes(), new  SMOreg());
        //AgenteRecomendador recomendador = new AgenteRecomendador(arquivoTreino,config,new MultilayerPerceptron(), new  SMOreg());
        
        // prepara agente -- constroi os modelos para inferência
        recomendador.preparaAgente();

        // Variável para controle do estoque
        Estoque estoque = new Estoque(recomendador, pecas);
        

        // peças do agente de linha de produção
        pecasProducao = new ArrayList<>();
        pecasProducao.add(new Peca(1, "Parafuso", 1));
        pecasProducao.add(new Peca(2, "Polca", 1));
        pecasProducao.add(new Peca(3, "Rotor", 1));
        pecasProducao.add(new Peca(4, "Engrenagem pequena", 1));
        pecasProducao.add(new Peca(5, "Engrenagem grande", 1));
        pecasProducao.add(new Peca(6, "Motor elétrico 60w", 1));
        pecasProducao.add(new Peca(7, "Bateria 12v 30A", 1));
        // agente linha de produção
        AgenteLinhaDeProducao agenteLP1 = new AgenteLinhaDeProducao(pecasProducao, estoque);
        /**
         * OBS. importante: Seria possível mais agentes de linha de produção, 
         * esse é um dos motivos para programar usando Threads. Contudo, se esse 
         * é o caso seria necessário remonitorar a produção para poder utilizar
         * aprendizagem de máquina para automatização ou estudar usar outra 
         * estratégia (Exemplo, separar o estoque por agente; ou estimar o 
         * impácto no estoque).
        */
        
        /**
         * Executar agentes
         */
        Thread t1 = new Thread(recomendador);
        t1.start();
        Thread t2 = new Thread(agenteLP1);
        t2.start();
        t1.join();
        t2.join();
    }
    
}
