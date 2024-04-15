package org.hlumak.bom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Comment {

    private String text;

    private List<String> answers = new ArrayList<>();

    public Comment(String text) {
        this.text = text;
    }

}
