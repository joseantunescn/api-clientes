package br.com.coti.api_clientes.repositories;

import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.factories.ConnectionFactory;

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

        try(var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("SELECT COUNT(*) AS QTD FROM CLIENTES WHERE cpf = ?");
            statement.setString(1, cpf);
            var resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        }
    }
}
