package com.example.api.services.Channel;

import java.util.List;
import java.util.Optional;

import com.example.api.models.Channel;
import com.example.api.models.Message;

public interface IChannelService {
    List<Channel> findAllChannels();
    Optional<Channel> findChannelById(Integer channelId);
    Channel save(Channel channel);
    void updateMessageRelationship(Channel channel, Message message);
}
