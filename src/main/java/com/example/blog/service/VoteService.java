package com.example.blog.service;

import com.example.blog.dto.VoteDto;
import com.example.blog.exception.GeneralException;
import com.example.blog.exception.PostNotFoundException;
import com.example.blog.model.Post;
import com.example.blog.model.Vote;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post  = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndUser.isPresent()){
            throw new GeneralException("You have already voted that post idiot!");
        } else {
            post.setVoteCount(post.getVoteCount() + 1);
        }
        postRepository.save(post);
        voteRepository.save(mapToVote(post));
    }

    private Vote mapToVote( Post post) {
        return Vote.builder()
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
