package br.com.coti.api_clientes.entities;

@getter
@setter
public class Endereco {

    private Integer id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private Cliente cliente;

}
