/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao.modelo;

import br.senai.ia.recomendacao.agentes.AgenteRecomendador;
import br.senai.ia.recomendacao.utils.ConfigAgents;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author Guilherme
 */
public class Estoque {
    
    /**
     * Agente recomendador para informar o usuário
     */
    private final AgenteRecomendador recomendador;
    /**
     * Controle das peças em estoque (Idealmente se usa banco de dados)
     */
    private final ArrayList<Peca> pecas;
    
    public Estoque(File arquivoTreino, ConfigAgents config) throws FileNotFoundException{
        this.pecas = new ArrayList<>();
        this.recomendador = new AgenteRecomendador(arquivoTreino, config);
    }
    
    public Estoque(File arquivoTreino, ConfigAgents config,ArrayList<Peca> pecas) throws FileNotFoundException{
        this.pecas = pecas;
        this.recomendador = new AgenteRecomendador(arquivoTreino, config);
    }

    public Estoque(AgenteRecomendador recomendador, ArrayList<Peca> pecas) {
        this.recomendador = recomendador;
        this.pecas = pecas;
    }    
    
    /**
     * Função para peça em estoque - feita pelo usuário
     * @param peca 
     */
    public synchronized void reporPecas(Peca peca){
        // encontra a peça
        for(int i = 0; i < pecas.size(); i++){
            if(pecas.get(i).getId() == peca.getId()){
                // incrementa a quantidade
                int novaQuantidade = pecas.get(i).getQuantidadeEmEstoque() + peca.getQuantidadeEmEstoque();
                pecas.get(i).setQuantidadeEmEstoque(novaQuantidade);
                break;
            }
        }
    }
    
    /**
     * Função para retirar peça de estoque
     * @param peca
     * @param quantidadeNecessaria
     * @return 
     */
    public synchronized int retirarPecas(Peca peca, int quantidadeNecessaria){
        // encontra a peça
        for(int i = 0; i < this.pecas.size(); i++){
            if(this.pecas.get(i).getId() == peca.getId()){
                // calcula a quantidade
                int novaQuantidade = pecas.get(i).getQuantidadeEmEstoque() - quantidadeNecessaria;
                this.pecas.get(i).setQuantidadeEmEstoque(novaQuantidade);
                // informa o agente recomendador sobre a retirada da peça
                this.recomendador.informarPecaRetirada(this.pecas.get(i));
                // retorna a informação de quantas peças ainda há em estoque
                return pecas.get(i).getQuantidadeEmEstoque();
            }
        }
        return 0;
    }
    
}
