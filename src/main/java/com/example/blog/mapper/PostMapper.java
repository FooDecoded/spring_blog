package com.example.blog.mapper;

import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.model.Post;
import com.example.blog.model.Topic;
import com.example.blog.model.User;
import com.example.blog.model.Vote;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.VoteRepository;
import com.example.blog.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Topic topic, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "isVoted", expression = "java(isPostVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    int commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    boolean isPostVoted(Post post) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.isPresent();
        }
        return false;
    }

}
