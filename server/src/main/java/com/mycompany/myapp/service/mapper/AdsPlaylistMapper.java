package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AdsPlaylistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdsPlaylist} and its DTO {@link AdsPlaylistDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdsPlaylistMapper extends EntityMapper<AdsPlaylistDTO, AdsPlaylist> {



    default AdsPlaylist fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdsPlaylist adsPlaylist = new AdsPlaylist();
        adsPlaylist.setId(id);
        return adsPlaylist;
    }
}
