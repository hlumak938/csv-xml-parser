package org.hlumak.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Article {
    private int id;
    private String title;
    private String content;
    private Date time;
    private Category category;
    private List<String> authors;
    private Comment comment;
}
