package com.developer.ceiapi.models.dao;

import com.developer.ceiapi.models.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface IClientDao extends CrudRepository<Client, Long> {
}
