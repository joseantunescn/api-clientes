package br.com.coti.api_clientes.repositories;

import br.com.coti.api_clientes.entities.Cliente;
import br.com.coti.api_clientes.entities.Endereco;
import br.com.coti.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    //method to insert a new client in the database
    public void inserir(Cliente cliente) throws Exception {



        try (var connection = connectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            var statement = connection.prepareStatement("INSERT INTO clientes (nome, cpf) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }
            if (cliente.getEnderecos() != null) {
                for (var endereco : cliente.getEnderecos()) {
                    statement = connection.prepareStatement("INSERT INTO enderecos (logradouro, numero, complemento, bairro, cidade, uf, cep, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    statement.setString(1, endereco.getLogradouro());
                    statement.setString(2, endereco.getNumero());
                    statement.setString(3, endereco.getComplemento());
                    statement.setString(4, endereco.getBairro());
                    statement.setString(5, endereco.getCidade());
                    statement.setString(6, endereco.getUf());
                    statement.setString(7, endereco.getCep());
                    statement.setInt(8, cliente.getId());
                    statement.execute();

                }
            }
            connection.commit();
        }
        }

        //method to check if cpf is already registered in the database
        public boolean cpfExists (String cpf) throws Exception {

            try (var connection = connectionFactory.getConnection()) {
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
        public List<Cliente> listar (String nome) throws Exception {
            try (var connection = connectionFactory.getConnection()) {

                var sql = """
                        SELECT
                        	c.ID AS IDCLIENTE,
                        	c.NOME,
                        	c.CPF,
                        	e.ID AS IDENDERECO,
                        	e.LOGRADOURO,
                        	e.NUMERO,
                        	e.COMPLEMENTO,
                        	e.BAIRRO,
                        	e.CIDADE,
                        	e.UF,
                        	e.CEP
                        FROM CLIENTES c
                        LEFT JOIN ENDERECOS e
                        ON c.ID = e.CLIENTE_ID
                        WHERE c.NOME ILIKE ? AND c.STATUS = 1
                        ORDER BY c.NOME;
                        """;

                var statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + nome + "%");
                var result = statement.executeQuery();

                var lista = new ArrayList<Cliente>();

                var map = new java.util.HashMap<Integer, Cliente>();

                while(result.next()) { //Percorrendo cada registro obtido na consulta

                    //capturando o id do cliente no banco de dados
                    var clienteId = result.getInt("IDCLIENTE");

                    Cliente cliente; //Objeto Cliente

                    if(map.containsKey(clienteId)) { //verificando se o cliente já foi lido
                        cliente = map.get(clienteId); //pegando o cliente que já foi lido
                    }
                    else {
                        cliente = new Cliente(); //Criando um cliente novo

                        cliente.setId(result.getInt("IDCLIENTE"));
                        cliente.setNome(result.getString("NOME"));
                        cliente.setCpf(result.getString("CPF"));
                        cliente.setEnderecos(new ArrayList<>());

                        map.put(clienteId, cliente);

                        lista.add(cliente); //adicionar o cliente dentro da lista
                    }

                    //Se houver endereços, adiciona na lista
                    var enderecoId = result.getObject("IDENDERECO");
                    if(enderecoId != null) { //verifiando se possui endereço

                        var endereco = new Endereco();

                        endereco.setId(result.getInt("IDENDERECO"));
                        endereco.setLogradouro(result.getString("LOGRADOURO"));
                        endereco.setNumero(result.getString("NUMERO"));
                        endereco.setComplemento(result.getString("COMPLEMENTO"));
                        endereco.setBairro(result.getString("BAIRRO"));
                        endereco.setCidade(result.getString("CIDADE"));
                        endereco.setUf(result.getString("UF"));
                        endereco.setCep(result.getString("CEP"));

                        cliente.getEnderecos().add(endereco);
                    }
                }

                return lista;
            }
        }

        //method to virtually delete a client from the database
        public boolean excluir(Integer id) throws Exception {

            try (var connection = connectionFactory.getConnection()) {

                var sql = """
                        UPDATE CLIENTES SET 
                                STATUS = 0,
                                DATAHORAEXCLUSAO = CURRENT_TIMESTAMP
                            WHERE ID = ?
                            AND STATUS = 1
                    """;

                var statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                var result = statement.executeUpdate();

                return result > 0;
            }
        }



}

