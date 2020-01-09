package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AdsSongDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdsSong} and its DTO {@link AdsSongDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdsSongMapper extends EntityMapper<AdsSongDTO, AdsSong> {



    default AdsSong fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdsSong adsSong = new AdsSong();
        adsSong.setId(id);
        return adsSong;
    }
}
