/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemploregressaosenai;

import java.text.SimpleDateFormat;
import java.util.Date;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Guilherme
 */
public class ExemploRegressaoSENAI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // variável usada para formatar datas
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // fonte dos dados para treino do classificador
        ConverterUtils.DataSource ds = 
                new ConverterUtils.DataSource("C:\\Program Files\\Weka-3-8\\data\\airline.arff");
        // Instancias contidas no arquivo
        Instances instances = ds.getDataSet();
        
        // verificar se os dados foram carregados com sucesso
        //System.out.println("Testar carregamento do arquivo:\n"+instances.toString());
        
         /* atributos das instâncias são numerados de 0 à n, pela ordem no arquivo
            @attribute passenger_numbers  - índice 0
            @attribute date               - índice 1
         */
         
        // Instancia Regressão Linear com configuração padrão
        LinearRegression lr = new LinearRegression();
        // OBS: Caso queira mudar os parâmetros deve fazer isso na variável lr
        
        // Atribui o lr como classificador - Mesma variável usada para ambas as tarefas
        Classifier classificador = lr;
        
        /* OBS.: como as classes mudam para cada exemplo é necessário 
                 reconstruir novamente o modelo */
        
        /*** Exemplo 1: Obter data pela quantidade de pessoas ***/
        
        // Definir atributo classe - aquele que será classificado
        instances.setClassIndex(1);
        // Comando para construir modelo para regressão (treinamento)
        classificador.buildClassifier(instances);
        
        // Instância para regressão
        DenseInstance instanciaTeste = new DenseInstance(2); // n
        instanciaTeste.setDataset(instances); // relaciona com o conjunto de instâncias do classificador
        instanciaTeste.setValue(0, 900);    // passenger_numbers
        //instanciaTeste.setValue(1, "")    // date
        
        double valorPredito = classificador.classifyInstance(instanciaTeste);
        System.out.println("Predição:");
        System.out.println("Data: " + valorPredito); // valor em timestamp
        System.out.println("Data: " + dateFormat.format(new Date((long) valorPredito))); // convertida
        
        
        /*** Exemplo 2: Obter quantidade de pessoas pela data ***/
        
        // Definir atributo classe - aquele que será classificado
        instances.setClassIndex(0);
        // Comando para construir modelo para regressão (treinamento)
        classificador.buildClassifier(instances);
        
        // Instância para regressão
        instanciaTeste = new DenseInstance(2); // n
        instanciaTeste.setDataset(instances); // relaciona com o conjunto de instâncias do classificador
        //instanciaTeste.setValue(0, 900);    // passenger_numbers
        instanciaTeste.setValue(1, dateFormat.parse("1971-07-19").getTime());       // date
        
        valorPredito = classificador.classifyInstance(instanciaTeste);
        System.out.println("Predição:");
        System.out.println("Quantidade: " + valorPredito);
    }
    
}
