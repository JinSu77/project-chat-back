package com.example.api.services.Channel;

import java.util.List;
import java.util.Optional;

import com.example.api.models.Channel;

public interface IChannelService {
    List<Channel> findAllChannels();
    Optional<Channel> findChannelById(Integer channelId);
}
