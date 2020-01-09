package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AdsPlaylistDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdsPlaylistDTO.class);
        AdsPlaylistDTO adsPlaylistDTO1 = new AdsPlaylistDTO();
        adsPlaylistDTO1.setId(1L);
        AdsPlaylistDTO adsPlaylistDTO2 = new AdsPlaylistDTO();
        assertThat(adsPlaylistDTO1).isNotEqualTo(adsPlaylistDTO2);
        adsPlaylistDTO2.setId(adsPlaylistDTO1.getId());
        assertThat(adsPlaylistDTO1).isEqualTo(adsPlaylistDTO2);
        adsPlaylistDTO2.setId(2L);
        assertThat(adsPlaylistDTO1).isNotEqualTo(adsPlaylistDTO2);
        adsPlaylistDTO1.setId(null);
        assertThat(adsPlaylistDTO1).isNotEqualTo(adsPlaylistDTO2);
    }
}
