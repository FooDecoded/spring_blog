package com.example.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Topic name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "topic_subscribtion",
            joinColumns = { @JoinColumn(name = "topic_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> subscribedUsers;

}
