package com.example.blog.service;

import com.example.blog.dto.TopicDto;
import com.example.blog.exception.GeneralException;
import com.example.blog.mapper.TopicMapper;
import com.example.blog.model.Topic;
import com.example.blog.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public TopicDto save(TopicDto topicDto) {
        Topic savedTopic = topicRepository.save(topicMapper.mapDtoToTopic(topicDto));
        topicDto.setId(savedTopic.getId());
        return topicDto;
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getAll() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::mapTopicToDto)
                .collect(Collectors.toList());
    }

    public TopicDto getTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new GeneralException("No subreddit found with ID - " + id));
        return topicMapper.mapTopicToDto(topic);
    }
}
