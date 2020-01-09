package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AdsSongTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdsSong.class);
        AdsSong adsSong1 = new AdsSong();
        adsSong1.setId(1L);
        AdsSong adsSong2 = new AdsSong();
        adsSong2.setId(adsSong1.getId());
        assertThat(adsSong1).isEqualTo(adsSong2);
        adsSong2.setId(2L);
        assertThat(adsSong1).isNotEqualTo(adsSong2);
        adsSong1.setId(null);
        assertThat(adsSong1).isNotEqualTo(adsSong2);
    }
}
