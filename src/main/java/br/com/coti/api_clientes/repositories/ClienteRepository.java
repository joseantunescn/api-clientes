package br.com.coti.api_clientes.repositories;

import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.factories.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    //method to insert a new client in the database
    public void inserir(Cliente cliente) throws Exception {

        try(var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("INSERT INTO clientes (nome, cpf) VALUES (?, ?)");
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();
        }
    }

    //method to check if cpf is already registered in the database
    public boolean cpfExists(String cpf) throws Exception {

        try (var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("SELECT COUNT(*) AS QTD FROM CLIENTES WHERE cpf = ?");
            statement.setString(1, cpf);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        }
    }

        //method to return a client list thru a name search
        public List<Cliente> listar(String nome) throws Exception {
            try(var connection = ConnectionFactory.getConnection()) {
                var statement = connection.prepareStatement("SELECT * FROM clientes WHERE nome ILIKE ? ORDER BY nome");
                statement.setString(1, "%" + nome + "%");
                var result = statement.executeQuery();

                var lista = new ArrayList<Cliente>();

                while(result.next()) {
                    var cliente = new Cliente();
                    cliente.setId(result.getInt("id"));
                    cliente.setNome(result.getString("nome"));
                    cliente.setCpf(result.getString("cpf"));
                    lista.add(cliente);

                }
                return lista;
            }
        }

    }

