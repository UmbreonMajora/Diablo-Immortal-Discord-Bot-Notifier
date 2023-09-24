package net.purplegoose.didnb.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleDTO {

    private final int id;
    private final String url;
    private final String category;
}
