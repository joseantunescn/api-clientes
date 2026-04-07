package br.com.coti.api_clientes.controllers;

import br.com.coti.api_clientes.dtos.ClienteRequest;
import br.com.coti.api_clientes.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //pération to create a client
    @PostMapping("criar")
    public ResponseEntity<String> criarCliente(@RequestBody ClienteRequest request) {
        try {
            clienteService.cadastrarCliente(request);
            return ResponseEntity.status(201).body("Cliente " + request.nome() + "criado com sucesso!");
        }

        catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar(@RequestParam String nome) {
        try {
            var lista = clienteService.pesquisarClientes(nome);

            return ResponseEntity.status(200).body(lista);
        }

        catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


}
