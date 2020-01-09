package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FavoriteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {


    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "removeSong", ignore = true)
    Favorite toEntity(FavoriteDTO favoriteDTO);

    default Favorite fromId(Long id) {
        if (id == null) {
            return null;
        }
        Favorite favorite = new Favorite();
        favorite.setId(id);
        return favorite;
    }
}
