package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.ClientDto;
import com.ikadjate.backend.model.Client;
import com.ikadjate.backend.repository.ClientRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client introuvable"));
    }

  
    
    public Client createClient(ClientDto request) {
         

        // Création du client
        Client client = new Client();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setObservation(request.getObservation());
        client.setAdresse(request.getAdresse());
        

        return clientRepository.save(client);
    }


    public Client updateClient(Long id, Client clientDetails) {
        Client client = getClientById(id);
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setTelephone(clientDetails.getTelephone());
        client.setAdresse(clientDetails.getAdresse());
        client.setObservation(clientDetails.getObservation());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}

