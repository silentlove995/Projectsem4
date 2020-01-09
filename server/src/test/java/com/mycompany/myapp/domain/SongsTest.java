package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class SongsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Songs.class);
        Songs songs1 = new Songs();
        songs1.setId(1L);
        Songs songs2 = new Songs();
        songs2.setId(songs1.getId());
        assertThat(songs1).isEqualTo(songs2);
        songs2.setId(2L);
        assertThat(songs1).isNotEqualTo(songs2);
        songs1.setId(null);
        assertThat(songs1).isNotEqualTo(songs2);
    }
}
