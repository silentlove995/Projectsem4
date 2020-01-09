package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PagesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pages} and its DTO {@link PagesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PagesMapper extends EntityMapper<PagesDTO, Pages> {


    @Mapping(target = "titles", ignore = true)
    @Mapping(target = "removeTitle", ignore = true)
    Pages toEntity(PagesDTO pagesDTO);

    default Pages fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pages pages = new Pages();
        pages.setId(id);
        return pages;
    }
}
