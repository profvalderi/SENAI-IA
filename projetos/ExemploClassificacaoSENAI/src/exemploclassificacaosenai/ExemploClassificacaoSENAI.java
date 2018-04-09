/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemploclassificacaosenai;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Guilherme
 */
public class ExemploClassificacaoSENAI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception  {
        // fonte dos dados para treino do classificador
        ConverterUtils.DataSource ds = 
                new ConverterUtils.DataSource("C:\\Program Files\\Weka-3-8\\data\\iris.2D.arff");
        // Instancias contidas no arquivo
        Instances instances = ds.getDataSet();
        
        // verificar se os dados foram carregados com sucesso
        //System.out.println("Testar carregamento do arquivo:\n"+instances.toString());
        
         /* atributos das instâncias são numerados de 0 à n, pela ordem no arquivo
            @attribute petallength  - índice 0
            @attribute petalwidth   - índice 1
            @attribute class        - índice 2
         */
        
        // Definir atributo classe - aquele que será classificado
        instances.setClassIndex(2);
        
        // Instanciar Classificador MultiLayer Perceptron com as variáveis padrão
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        // caso queira alterar as configurações padrão deve alterar pela variável MLP
        
        // Atribui o mlp como classificador        
        Classifier classificador = mlp;
        // Comando para construir modelo para classificação (treinamento)
        classificador.buildClassifier(instances);
       
        //***** Exemplo de uso do classificador ******//
        
        // Instância 1 para classificação
        DenseInstance novo = new DenseInstance(3); // n
        novo.setDataset(instances); // relaciona com o conjunto de instâncias do classificador
        novo.setValue(0, 3.7);      // petallength
        novo.setValue(1, 0.7);      // petalwidth
        //novo.setValue(2, "?");    // class -- Não necessário. Será a saída.

        // O classificador gera as probabilidades de todas as opções de saída da classe
        double probabilidades[] = classificador.distributionForInstance(novo);
        System.out.println("Probabilidades:");
        System.out.println("Iris-setosa: " + probabilidades[0]);
        System.out.println("Iris-versicolor: " + probabilidades[1]);
        System.out.println("Iris-virginica: " + probabilidades[2]);
        
        // Instância 2 para classificação
        novo = new DenseInstance(3); // n
        novo.setDataset(instances); // relaciona com o conjunto de instâncias do classificador
        novo.setValue(0, 3.7);      // petallength
        novo.setValue(1, 2.7);      // petalwidth -- Valor não contido nas instâncias
        //novo.setValue(2, "?");    // class -- Não necessário. Será a saída.

        // O classificador gera as probabilidades de todas as opções de saída da classe
        probabilidades = classificador.distributionForInstance(novo);
        System.out.println("Probabilidades:");
        System.out.println("Iris-setosa: " + probabilidades[0]);
        System.out.println("Iris-versicolor: " + probabilidades[1]);
        System.out.println("Iris-virginica: " + probabilidades[2]);
         
    }
    
}
