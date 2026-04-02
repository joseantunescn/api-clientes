package br.com.coti.api_clientes.services;

import br.com.coti.api_clientes.dtos.ClienteRequest;
import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.repositories.ClienteRepository;

import java.util.List;

public class ClienteService {

    //name and cpf are mandatory, so we need to validate their existence them before inserting into the database

    public void cadastrarCliente(ClienteRequest request) throws Exception {
        if (request.nome() == null || request.nome().trim().length() < 6) {
            throw new IllegalArgumentException("O nome deve conter no mínimo 6 caracteres.");
        }

        if (request.cpf() == null) {
            throw new IllegalArgumentException("O CPF é obrigatório!");
        }

        //check if cpf is not already registered in the database
        var clienteRepository = new ClienteRepository();
        if(clienteRepository.cpfExists(request.cpf())) {
            throw new IllegalArgumentException("O CPF já está cadastrado no sistema.");
        }

        var cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());

        try {
            clienteRepository.inserir(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage());
        }



    }
    //method to return a client list thru a name search
    public List<Cliente> pesquisarClientes(String nome) throws Exception {

        //client name must have at least 5 characters
        if(nome == null || nome.trim().length() < 5) {
            throw new IllegalArgumentException("O nome deve conter no mínimo 5 caracteres.");
        }
        var clienteRepository = new ClienteRepository();
        var lista = clienteRepository.listar(nome);
        return lista;

    }




}
