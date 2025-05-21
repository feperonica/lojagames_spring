package com.generation.lojagames.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 2, max = 100, message = "O atributo nome dever ter no minimo 5 e no maximo 100 caracteres.")
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull(message = "O preço é obrigatório!")
    @Positive(message = "O preço deve ser positivo!")
    private BigDecimal preco;

    @NotNull(message = "O estoque é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo descrição dever ter no minimo 10 e no maximo 1000 caracteres.")
    private Integer estoque;
    
    @Size(max = 5000)
    private String imagem;
    
	@UpdateTimestamp
	private LocalDateTime data;

    @ManyToOne
    @JsonIgnoreProperties("produtos")
    private Categoria categoria;
    
    // Getters e Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}