package br.com.coti.api_clientes.entities;

import java.util.List;

@getter
@setter
public class Cliente {

    private Integer id;
    private String nome;
    private String cpf;
    private List<Endereco> enderecos;

}
