package br.com.coti.api_clientes.dtos;

public record ClienteRequest(
        String nome, //nome do cliente
        String cpf, //cpf do cliente
        EnderecoRequest[] enderecos //array de endereços
) {
}
