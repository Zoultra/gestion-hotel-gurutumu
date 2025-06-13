package com.ikadjate.backend.controller;

 

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.CommandeClientDto;
import com.ikadjate.backend.dto.CommandeFournisseurDto;
import com.ikadjate.backend.dto.LigneCommandeClientDto;
import com.ikadjate.backend.dto.LigneCommandeFournisseurDto;
import com.ikadjate.backend.model.Client;
import com.ikadjate.backend.model.CommandeClient;
import com.ikadjate.backend.model.CommandeFournisseur;
import com.ikadjate.backend.service.CommandeClientService;
import com.ikadjate.backend.service.CommandeFournisseurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.COMMANDECLIENT)
 
public class CommandeClientController {

    private final CommandeClientService commandeClientService;

    public CommandeClientController(CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @PostMapping
    public ResponseEntity<CommandeClientDto> createCommandeClient(@RequestBody CommandeClientDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le corps de la commande est vide !");
        }
        return ResponseEntity.ok(commandeClientService.save(dto));
    }
    @GetMapping
    public List<CommandeClient> getAllCommandesClient() {
        return commandeClientService.getCommandesClient();
    }
    
    @GetMapping("/{id}")
    public CommandeClient CommandeClientById(@PathVariable Long id)  {
        return commandeClientService.getCommandesClientById(id);
    }
}
