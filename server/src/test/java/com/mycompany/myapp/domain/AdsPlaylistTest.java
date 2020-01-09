package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AdsPlaylistTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdsPlaylist.class);
        AdsPlaylist adsPlaylist1 = new AdsPlaylist();
        adsPlaylist1.setId(1L);
        AdsPlaylist adsPlaylist2 = new AdsPlaylist();
        adsPlaylist2.setId(adsPlaylist1.getId());
        assertThat(adsPlaylist1).isEqualTo(adsPlaylist2);
        adsPlaylist2.setId(2L);
        assertThat(adsPlaylist1).isNotEqualTo(adsPlaylist2);
        adsPlaylist1.setId(null);
        assertThat(adsPlaylist1).isNotEqualTo(adsPlaylist2);
    }
}
