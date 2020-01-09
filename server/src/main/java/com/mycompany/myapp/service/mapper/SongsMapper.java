package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SongsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Songs} and its DTO {@link SongsDTO}.
 */
@Mapper(componentModel = "spring", uses = {AdsSongMapper.class, PlaylistMapper.class, AlbumMapper.class, FavoriteMapper.class})
public interface SongsMapper extends EntityMapper<SongsDTO, Songs> {

    @Mapping(source = "ads.id", target = "adsId")
    @Mapping(source = "playlist.id", target = "playlistId")
    @Mapping(source = "album.id", target = "albumId")
    @Mapping(source = "favorite.id", target = "favoriteId")
    SongsDTO toDto(Songs songs);

    @Mapping(source = "adsId", target = "ads")
    @Mapping(source = "playlistId", target = "playlist")
    @Mapping(source = "albumId", target = "album")
    @Mapping(source = "favoriteId", target = "favorite")
    Songs toEntity(SongsDTO songsDTO);

    default Songs fromId(Long id) {
        if (id == null) {
            return null;
        }
        Songs songs = new Songs();
        songs.setId(id);
        return songs;
    }
}
