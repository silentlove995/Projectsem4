package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AdsSongMapperTest {

    private AdsSongMapper adsSongMapper;

    @BeforeEach
    public void setUp() {
        adsSongMapper = new AdsSongMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(adsSongMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(adsSongMapper.fromId(null)).isNull();
    }
}
