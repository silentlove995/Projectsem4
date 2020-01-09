package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PagesMapperTest {

    private PagesMapper pagesMapper;

    @BeforeEach
    public void setUp() {
        pagesMapper = new PagesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(pagesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pagesMapper.fromId(null)).isNull();
    }
}
