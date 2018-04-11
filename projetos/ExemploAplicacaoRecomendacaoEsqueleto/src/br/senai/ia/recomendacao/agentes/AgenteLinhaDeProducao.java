/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao.agentes;

import br.senai.ia.recomendacao.modelo.Estoque;
import br.senai.ia.recomendacao.modelo.Peca;
import br.senai.ia.recomendacao.utils.ConfigAgents;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class AgenteLinhaDeProducao implements Runnable{
    
    /**
     * Lista de peças necessárias para montar um produto
     */
    private final ArrayList<Peca> pecasNecessarias;
    /**
     * Variável para controle do estoque
     */
    private final Estoque estoque;

    public AgenteLinhaDeProducao(ArrayList<Peca> pecasNecessarias, Estoque estoque) {
        this.pecasNecessarias = pecasNecessarias;
        this.estoque = estoque;
    }

    /**
     * Loop do agente para montagem de um produto
     */
    @Override
    public void run() {
        while(true){
            // para cada peça necessária para um agente montar um produto faça
            for(Peca peca: pecasNecessarias){
                System.out.println("\nRetirar Peça: "+peca.getNome());
                // retira peça do estoque
                int pecasEmEstoque = estoque.retirarPecas(peca, peca.getQuantidadeEmEstoque());
                // se não há peças em estoque atraza a produção e ela para
                if(pecasEmEstoque <= 0) {
                    System.exit(0);
                }
                if(ConfigAgents.TEMPO_MONGAGEM > 0) { // desativa simulação se valor 0 ou negativo
                    try {
                        // simulação do agente montando o produto com a peça
                        sleep(ConfigAgents.TEMPO_MONGAGEM);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AgenteLinhaDeProducao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
