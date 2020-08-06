package com.example.blog.config;

import com.example.blog.model.Post;
import com.example.blog.model.Topic;
import com.example.blog.model.User;
import com.example.blog.repository.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@AllArgsConstructor
@Profile("!jwt-auth")
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
//    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("#### Database Intialization Started ####");
        User user = seedUser();
        Topic topic = seedTopic(user);
        Post post = seedPost(user, topic);
        logger.info("#### Database Intialization Sucesseded ####");
    }

    private User seedUser(){
        User user = User.builder()
                .username("asem")
                .password("pass")
                .build();
        return userRepository.save(user);
    }

    private Topic seedTopic(User user){
        Topic topic = Topic.builder()
                .name("topic-1")
                .description("topic-1 desc")
                .subscribedUsers(Arrays.asList(user))
                .build();
        return topicRepository.save(topic);
    }

    private Post seedPost(User user, Topic topic){
        Post post = Post.builder()
                .postName("topic-1 post-1")
                .description("topic-1 desc")
                .url("/asem")
                .user(user)
                .topic(topic)
                .voteCount(0)
                .build();
        return postRepository.save(post);
    }

    private void seedComments(){}

    private void seedVotes(){}
}
