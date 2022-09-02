package ru.practicum.shareit.comments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "COMMENTS")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;
    @Column(name = "COMMENT_TEXT")
    private String text;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "COMMENT_ITEM_ID")
    private Item item;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "COMMENT_AUTHOR_ID")
    private User author;
    @Column(name = "COMMENT_CREATED")
    private LocalDateTime created;

}
