/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao.agentes;

import br.senai.ia.recomendacao.modelo.Peca;
import br.senai.ia.recomendacao.utils.ConfigAgents;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Guilherme
 */
public class AgenteRecomendador implements Runnable {
       
    // fila para ser usada de buffer
    private Queue<Peca> buffer;   
    
    // Variáveis para usar classes do Weka 
    private Classifier classificador = null;
    private Classifier preditorPorRegressao = null;
    private DenseInstance instanciaParaClassificacao;
    private Instances instances;
    
    // arquivo contendo dados para treino
    private File arquivoTreino;
    
    // arquivo com configurações para classificação e tomada de decisão dos agentes
    private final ConfigAgents config;
    
    public AgenteRecomendador(File arquivoTreino, ConfigAgents config) throws FileNotFoundException {
        this.buffer = new LinkedList();
        this.arquivoTreino = arquivoTreino;
        this.config = config;
    }

    public AgenteRecomendador( File arquivoTreino, ConfigAgents config, Classifier classificador, Classifier pretitorPorRegressao) throws FileNotFoundException {
        this(arquivoTreino, config);
        this.classificador = classificador;
        this.preditorPorRegressao = pretitorPorRegressao;
    }    
    
    
    /**
     * Notifica o agente recomendador informando qual peça foi retirada
     * @param peca 
     */
    public synchronized void informarPecaRetirada(Peca peca) {
        buffer.add(peca);
        notifyAll();
    }
    
    /**
     * Retira uma Peça do Buffer para tratar a notificação
     * @return
     * @throws InterruptedException 
     */
    public synchronized Peca atenderRequisicao() throws InterruptedException{
        Peca peca = null;
        // repita até existir algo no buffer
        {
            wait();
            peca = buffer.poll();
        } while(peca == null);
        // retorna a peça
        return peca;
    }
    
    /**
     * Função para notificar a recomendação para o usuário. Em um programa mais 
     * robusto é possível usar uma API de email, mensagem ou mesmo troca de 
     * mensagens com outra aplicação.
     * @param peca
     * @param dias
     * @param fornecedor 
     */
    public void recomendarEncomendaDePecaAoUsuario(Peca peca, int dias,String fornecedor){
        System.out.println("--------------------------------");
        System.out.println("Foi recomendado encomendar a peça "+peca.getNome());
        System.out.println("Ela deve ser encomendada para chegar em "+dias+" dias pelo fornecedor: "+fornecedor);
        System.out.println("--------------------------------");
    }
    
    
    /**
     * Função necessária para preparar o Agente
     * @throws Exception 
     */
    public void preparaAgente() throws Exception{
        
        // converter arquivo para geração do modelo em instâncias
        ConverterUtils.DataSource ds = new ConverterUtils.DataSource(new FileInputStream(arquivoTreino));
        //new ConverterUtils.DataSource("C:\\Users\\Guilherme\\Dropbox\\SENAC\\Inteligência Artificial - SENAI - 2018\\dadosExemploTudo-semStatus.arff");
        instances = ds.getDataSet();

        /// Instancia de classificador para regressão
        // instancia o preditor padrão, se não setado
        if(this.preditorPorRegressao == null)this.preditorPorRegressao = new LinearRegression();
        // Definir atributo classe --- a ser predito: dias
        this.instances.setClassIndex(3);
        //  constroi o modelo para predição por regressão
        this.preditorPorRegressao.buildClassifier(instances);

        /// Instancia de classificador para classificação
        // instancia o classificador padrão, se não setado
        if(this.classificador == null)this.classificador = new MultilayerPerceptron();
        // Definir atributo classe --- a ser predito: status
        this.instances.setClassIndex(4);
        //  constroi o modelo de classificação
        this.classificador.buildClassifier(instances);

        // instancia variável para ser usada na classificação e na regressão
        this.instanciaParaClassificacao = new DenseInstance(5);
        // relaciona a instância com o dataset de instâncias
        this.instanciaParaClassificacao.setDataset(instances); 

    }
    

    /**
     * Função de loop do agente
     */
    @Override
    public void run() {
        try {
            // loop infinito do agente
            while(true){
                Peca peca = atenderRequisicao();
                System.out.println("\n++++++++++++++++++++++++++");
                // faz regressão
                regressao(peca);
                // faz classificação
                classificacao(peca);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(AgenteRecomendador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AgenteRecomendador.class.getName()).log(Level.SEVERE, null, ex);
        }           
    }
    
    /**
     * Processo de regressão do agente
     * @param peca
     * @throws Exception 
     */
    private void regressao(Peca peca) throws Exception {
        System.out.println("Regressão");
        /* atributos são numerados de 0 à n, pela ordem no arquivo
            @attribute peca         - índice 0
            @attribute estoque      - índice 1
            @attribute fornecedor   - índice 2
            @attribute dias         - índice 3  -- atributo a ser predito
            @attribute status       - índice 4 
         */

        // cada fornecedor possui um valor diferente
        for(String fornecedor : peca.getFornecedores()){
            // ajusta os parâmetros para regressão
            instanciaParaClassificacao.setValue(0, peca.getNome());                 // Peça
            instanciaParaClassificacao.setValue(1, peca.getQuantidadeEmEstoque());  // Quantidade em estoque
            instanciaParaClassificacao.setValue(2, fornecedor);                     // Fornecedor 
            //instanciaParaClassificacao.setValue(3, 3);        // Dias para entrega -- a ser descoberto
            instanciaParaClassificacao.setValue(4, ConfigAgents.STATUS_PRODUCAO_SATISFATORIA); // status da produção
            
            // realiza a predição por regressão
            double valorPredito = this.preditorPorRegressao.classifyInstance(instanciaParaClassificacao);
            
            //System.out.println("Predições:");
            System.out.println("Peça: "+peca.getNome()
                    +" Quantidade: "+peca.getQuantidadeEmEstoque()
                    +" Fornecedor: "+fornecedor
                    +" Dias: " + valorPredito);
            
            /// Tomada de decisão do agente, recomendar ou não?
            if(valorPredito < (config.getDiasEntregaClassificacao() + config.getLimiarSuperiorInferiorRegressao()) // 7.5
                    && (config.getDiasEntregaClassificacao() - valorPredito) > config.getLimiarSuperiorInferiorRegressao()){ // 6.5
                recomendarEncomendaDePecaAoUsuario(peca, (int) Math.round(valorPredito), fornecedor);
            }
            
        }
    }
    
    private void classificacao(Peca peca) throws Exception {
        System.out.println("\nClassificassão");
        /* atributos são numerados de 0 à n, pela ordem no arquivo
            @attribute peca         - índice 0
            @attribute estoque      - índice 1
            @attribute fornecedor   - índice 2
            @attribute dias         - índice 3  
            @attribute status       - índice 4 -- atributo a ser predito (classe)
         */
        // cada fornecedor possui um valor difernete
        for(String fornecedor : peca.getFornecedores()){
            // ajusta os parâmetros para regressão
            instanciaParaClassificacao.setValue(0, peca.getNome());                 // Peça
            instanciaParaClassificacao.setValue(1, peca.getQuantidadeEmEstoque());  // Quantidade em estoque
            instanciaParaClassificacao.setValue(2, fornecedor);                     // Fornecedor 
            instanciaParaClassificacao.setValue(3, config.getDiasEntregaClassificacao()); // Dias para entrega 
            //instanciaParaClassificacao.setValue(4, STATUS_PRODUCAO_SATISFATORIA); // status da produção -- a ser descoberto
                        
            // obtém as brobabilidades
            double probabilidades[] = classificador.distributionForInstance(instanciaParaClassificacao);
            System.out.println("Probabilidades Fornecedor: "+fornecedor+" Peça: "+peca.getNome());
            System.out.println("  Superlotado:  " + probabilidades[0]);
            System.out.println("  Atrazado:     " + probabilidades[1]);
            System.out.println("  Satisfatório: " + probabilidades[2]);
            
            /// Tomada de decisão do agente, recomendar ou não?
            if(probabilidades[2] > config.getProbabilidadeMinimaClassificacao()){ // recomenda se for maior que 80%
                recomendarEncomendaDePecaAoUsuario(peca, config.getDiasEntregaClassificacao(), fornecedor);
            }
            
        }
    }
    
}
