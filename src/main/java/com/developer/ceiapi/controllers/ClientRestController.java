package com.developer.ceiapi.controllers;

import com.developer.ceiapi.models.entity.Client;
import com.developer.ceiapi.models.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT})
public class ClientRestController {

    // http://localhost:8081/api/clients

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index() {
        return clientService.findAll();
    }

    @PostMapping("/clients")
    public ResponseEntity<?> create(@RequestBody Client client, BindingResult result) {

        Client clientNew = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            clientNew = clientService.save(client);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar el insert");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente se creo correctamente");
        response.put("client", clientNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            clientService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al eliminar cliente");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "El cliente se elimino con exito");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Client client = null;
        Map<String, Object> response = new HashMap<>();
        try {
            client = clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al obtener el cliente");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> update(@RequestBody Client client, BindingResult result, @PathVariable Long id) {
        Client clientActual = clientService.findById(id);
        Client clientUpdated = null;
        Map<String, Object> response = new HashMap<>();

        try {
            clientActual.setLastName(client.getLastName());
            clientActual.setFirstName(client.getFirstName());
            clientActual.setEmail(client.getEmail());
            clientActual.setPhone(client.getPhone());
            clientActual.setDni(client.getDni());
            clientActual.setCountry(client.getCountry());

            clientUpdated = clientService.save(clientActual);
        } catch (DataAccessException e) {
            response.put("message", "Error al actualizar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "El cliente ha sido actualizado con éxito!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
}
