package org.hlumak.bom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Article {

    private final int id;

    private String title;

    private String content;

    private Date time;

    private Category category;

    private List<String> authors;

    private Comment comment;
}
