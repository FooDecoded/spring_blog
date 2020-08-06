package com.example.blog.mapper;

import com.example.blog.dto.TopicDto;
import com.example.blog.model.Post;
import com.example.blog.model.Topic;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    @Mapping(target = "numberOfPosts", expression = "java(countPosts(topic.getPosts()))")
    TopicDto mapTopicToDto(Topic topic);

    default Integer countPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Topic mapDtoToTopic(TopicDto subredditDto);
}
