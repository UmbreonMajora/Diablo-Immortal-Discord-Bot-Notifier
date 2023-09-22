package net.purplegoose.didnb.news;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleDTO {

    private final String id;
    private final String url;
    private final String category;
}
