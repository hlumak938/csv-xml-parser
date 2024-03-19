package org.hlumak.bom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {

    private String text;

    private List<String> answers;

    public Comment(String text) {
        this.text = text;
    }

}
