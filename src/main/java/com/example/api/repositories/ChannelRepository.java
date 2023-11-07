package com.example.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.api.models.Channel;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
}
