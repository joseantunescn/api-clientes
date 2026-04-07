package br.com.coti.api_clientes.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ConnectionFactory {

    @Value("${datasource.host}")
    private String host;

    @Value("${datasource.username}")
    private String user;

    @Value("${datasource.password}")
    private String password;

        public Connection getConnection() throws Exception {

            return DriverManager.getConnection(host, user, password);
        }

}
