package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PostsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Posts} and its DTO {@link PostsDTO}.
 */
@Mapper(componentModel = "spring", uses = {PagesMapper.class})
public interface PostsMapper extends EntityMapper<PostsDTO, Posts> {

    @Mapping(source = "pages.id", target = "pagesId")
    PostsDTO toDto(Posts posts);

    @Mapping(source = "pagesId", target = "pages")
    Posts toEntity(PostsDTO postsDTO);

    default Posts fromId(Long id) {
        if (id == null) {
            return null;
        }
        Posts posts = new Posts();
        posts.setId(id);
        return posts;
    }
}
