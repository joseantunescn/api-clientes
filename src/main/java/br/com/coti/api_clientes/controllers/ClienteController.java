package br.com.coti.api_clientes.controllers;

import br.com.coti.api_clientes.services.ClienteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    //pération to create a client
    @PostMapping("criar")
    public String criarCliente(@RequestParam String nome, @RequestParam String cpf) {
        try {
            var clienteService = new ClienteService();
            clienteService.cadastrarCliente(nome, cpf);
            return "Cliente" + nome + "criado com sucesso!";
        } catch (Exception e) {
            return "Erro ao criar cliente: " + e.getMessage();
        }
    }


}
