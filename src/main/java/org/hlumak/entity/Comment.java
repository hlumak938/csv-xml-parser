package org.hlumak.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {
    private final String text;
    private List<String> answers;
}
