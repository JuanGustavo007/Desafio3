package com.devsuperior.desafio3.controllers;

import com.devsuperior.desafio3.dto.ClientDto;
import com.devsuperior.desafio3.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/clients")
public class ClienteController {

    @Autowired
    private ClientService clientService;


    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id){
        ClientDto clientDto = clientService.findById(id);
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAll(Pageable pageable){
        Page<ClientDto> clientDtos = clientService.findAll(pageable);
        return ResponseEntity.ok(clientDtos);
    }

    @PostMapping
    public ResponseEntity<ClientDto> insert(@Valid @RequestBody ClientDto clientDto){
        ClientDto insertClientDto = clientService.insert(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(insertClientDto.getId()).toUri();
        return ResponseEntity.created(uri).body(insertClientDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable Long id,@Valid @RequestBody ClientDto clientDto){
        clientDto = clientService.update(id, clientDto);
        return ResponseEntity.ok(clientDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClientDto> delete(@PathVariable Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
