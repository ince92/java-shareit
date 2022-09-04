package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(name = "comment_text")
    private String text;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "comment_item_id")
    private Item item;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "comment_author_id")
    private User author;
    @Column(name = "comment_created")
    private LocalDateTime created;

}
