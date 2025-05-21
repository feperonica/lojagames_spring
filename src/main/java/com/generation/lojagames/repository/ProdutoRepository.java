package com.generation.lojagames.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.lojagames.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
    List<Produto> findAllByNomeContainingIgnoreCase(String nome);
    
 // Listar produtos com preço maior que o valor informado
    List<Produto> findAllByPrecoGreaterThan(BigDecimal preco);

    // Listar produtos com preço menor que o valor informado
    List<Produto> findAllByPrecoLessThan(BigDecimal preco);
}
