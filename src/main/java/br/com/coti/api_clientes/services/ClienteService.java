package br.com.coti.api_clientes.services;

import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.repositories.ClienteRepository;

public class ClienteService {

    //name and cpf are mandatory, so we need to validate their existence them before inserting into the database

    public void cadastrarCliente(String nome, String cpf) throws Exception {
        if (nome == null || nome.trim().length() < 6) {
            throw new IllegalArgumentException("O nome deve conter no mínimo 6 caracteres.");
        }

        if (cpf == null) {
            throw new IllegalArgumentException("O CPF é obrigatório!");
        }

        //check if cpf is not already registered in the database
        var clienteRepository = new ClienteRepository();
        if(clienteRepository.cpfExists(cpf)) {
            throw new IllegalArgumentException("O CPF já está cadastrado no sistema.");
        }

        var cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);

        try {
            clienteRepository.inserir(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage());
        }



    }




}
