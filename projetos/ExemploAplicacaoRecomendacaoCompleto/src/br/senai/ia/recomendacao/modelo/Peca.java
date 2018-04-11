/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senai.ia.recomendacao.modelo;

/**
 *
 * @author Guilherme
 */
public class Peca {
    
    private int id;
    private String nome;
    private int quantidadeEmEstoque;
    private String[] fornecedores;

    public Peca() {
    }

    public Peca(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public Peca(int id, String nome, int quantidadeEmEstoque) {
        this(id,nome);
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public Peca(int id, String nome, int quantidadeEmEstoque, String[] fornecedores) {
        this(id,nome,quantidadeEmEstoque);
        this.fornecedores = fornecedores;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public void setFornecedores(String[] fornecedores) {
        this.fornecedores = fornecedores;
    }

    public String[] getFornecedores() {
        return fornecedores;
    }
}
