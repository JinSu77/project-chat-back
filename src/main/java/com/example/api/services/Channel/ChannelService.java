package com.example.api.services.Channel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Channel;
import com.example.api.models.Message;
import com.example.api.repositories.ChannelRepository;

@Service
public class ChannelService implements IChannelService {
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<Channel> findAllChannels() {
        Iterable<Channel> channels = channelRepository.findAll();

        return (List<Channel>) channels;
    }

    @Override
    public Optional<Channel> findChannelById(Integer channelId) {
        return channelRepository.findById(channelId);
    }

    @Override
    public Channel save(Channel channel) {
        return channelRepository.save(channel);
    }

    public void updateMessageRelationship(Channel channel, Message message) {
        channel.getMessages().add(message);

        channelRepository.save(channel);
    }
}
