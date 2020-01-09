package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class SongsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SongsDTO.class);
        SongsDTO songsDTO1 = new SongsDTO();
        songsDTO1.setId(1L);
        SongsDTO songsDTO2 = new SongsDTO();
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
        songsDTO2.setId(songsDTO1.getId());
        assertThat(songsDTO1).isEqualTo(songsDTO2);
        songsDTO2.setId(2L);
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
        songsDTO1.setId(null);
        assertThat(songsDTO1).isNotEqualTo(songsDTO2);
    }
}
