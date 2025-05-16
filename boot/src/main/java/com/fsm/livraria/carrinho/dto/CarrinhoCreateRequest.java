package com.fsm.livraria.carrinho.dto;

import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.carrinho.entities.Carrinho;
import com.fsm.livraria.carrinho.entities.CarrinhoItem;
import com.fsm.livraria.livro.entities.Livro;
import com.fsm.livraria.livro.repositories.LivroRepository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Serdeable
public class CarrinhoCreateRequest {

    @NotNull(message = "O total não pode ser nulo")
    @Min(value = 1, message = "O total não pode ser menor que 1")
    private BigDecimal total;

    @NotNull(message = "O carrinho não pode ser nulo")
    @Size(min = 1, message = "O carrinho deve ter pelo menos um item")
    private List<CarrinhoItemCreateRequest> itens = new ArrayList<>();

    public CarrinhoCreateRequest() {
    }

    public CarrinhoCreateRequest(BigDecimal bigDecimal, List<CarrinhoItemCreateRequest> items) {
        this.total = bigDecimal;
        this.itens.addAll(items);
    }

    public Carrinho toEntity(LivroRepository livroRepository) {

        //validar se os livros existem
        List<UUID> livroIds = itens.stream().map(CarrinhoItemCreateRequest::getLivroId).map(UUID::fromString).toList();

        List<Livro> livros = livroRepository.findByUuidInList(livroIds);

        //verificar se todos os livros foram encontrados
        if (livros.size() != livroIds.size()) {
            throw new ServiceError("%s livros não encontrados", livroIds.size() - livros.size());
        }

        //criar uma mapa aonde a chave é o id do livro e o valor é a quantidade
        Map<String, Integer> livrosPrecos = itens.stream().collect(Collectors.toMap(
                CarrinhoItemCreateRequest::getLivroId,
                CarrinhoItemCreateRequest::getQuantidade
        ));


        //fazer o calculo do total
        BigDecimal total = livros.stream()
                .map(livro -> livro.getPreco().multiply(BigDecimal.valueOf(livrosPrecos.get(livro.getUuid().toString()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //verificar se o total é igual ao total do carrinho
        //se o total for diferente do total do carrinho, lançar uma exceção
        if (total.compareTo(this.total) != 0) {
            throw new ServiceError("O total do carrinho não é igual ao total dos livros");
        }
        //criar o carrinho
        Carrinho carrinho = new Carrinho(total);
        //criar os itens do carrinho
        Map<String, Livro> livroMap = livros.stream()
                .collect(Collectors.toMap(livro -> livro.getUuid().toString(), livro -> livro));

        List<CarrinhoItem> items = this.itens.stream().map(
                item -> new CarrinhoItem(
                        livroMap.get(item.getLivroId()),
                        carrinho,
                        item.getQuantidade())).toList();

        carrinho.getItens().addAll(items);

        return carrinho;
    }


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CarrinhoItemCreateRequest> getItens() {
        return itens;
    }

    public void setItens(List<CarrinhoItemCreateRequest> itens) {
        this.itens = itens;
    }
}
