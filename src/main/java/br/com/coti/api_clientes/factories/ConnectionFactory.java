package br.com.coti.api_clientes.factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

        public static Connection getConnection() throws Exception {

            var host = "jdb:postgresql://localhost:5432/bd-api-clientes";
            var user = "postgresql";
            var password = "coti";

            return DriverManager.getConnection(host, user, password);
        }

}
