package com.developer.ceiapi.models.service;

import com.developer.ceiapi.models.entity.Client;

import java.util.List;

public interface IClientService {
    public List<Client> findAll();

    public Client save(Client client);

    public void delete(Long id);

    public Client findById(Long id);
}
