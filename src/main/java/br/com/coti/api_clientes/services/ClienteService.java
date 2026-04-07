package br.com.coti.api_clientes.services;

import br.com.coti.api_clientes.dtos.ClienteRequest;
import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //name and cpf are mandatory, so we need to validate their existence them before inserting into the database

    public void cadastrarCliente(ClienteRequest request) throws Exception {
        if (request.nome() == null || request.nome().trim().length() < 6) {
            throw new IllegalArgumentException("O nome deve conter no mínimo 6 caracteres.");
        }

        if (request.cpf() == null) {
            throw new IllegalArgumentException("O CPF é obrigatório!");
        }

        if (request.enderecos() == null || request.enderecos().length == 0) {
            throw new IllegalArgumentException("O cliente deve conter pelo menos um endereço.");
        }

        //check if cpf is not already registered in the database
        if(clienteRepository.cpfExists(request.cpf())) {
            throw new IllegalArgumentException("O CPF já está cadastrado no sistema.");
        }

        var cliente = new Cliente();

        cliente.setEnderecos( new ArrayList<>());
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());

        for (var item : request.enderecos()) {
            var endereco = new br.com.coti.api_clientes.entities.Endereco();
            endereco.setLogradouro(item.logradouro());
            endereco.setNumero(item.numero());
            endereco.setComplemento(item.complemento());
            endereco.setBairro(item.bairro());
            endereco.setCidade(item.cidade());
            endereco.setUf(item.uf());
            endereco.setCep(item.cep());

            cliente.getEnderecos().add(endereco);
        }

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

        var lista = clienteRepository.listar(nome);
        return lista;

    }




}
