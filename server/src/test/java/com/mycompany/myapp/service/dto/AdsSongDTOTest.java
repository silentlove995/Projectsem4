package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AdsSongDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdsSongDTO.class);
        AdsSongDTO adsSongDTO1 = new AdsSongDTO();
        adsSongDTO1.setId(1L);
        AdsSongDTO adsSongDTO2 = new AdsSongDTO();
        assertThat(adsSongDTO1).isNotEqualTo(adsSongDTO2);
        adsSongDTO2.setId(adsSongDTO1.getId());
        assertThat(adsSongDTO1).isEqualTo(adsSongDTO2);
        adsSongDTO2.setId(2L);
        assertThat(adsSongDTO1).isNotEqualTo(adsSongDTO2);
        adsSongDTO1.setId(null);
        assertThat(adsSongDTO1).isNotEqualTo(adsSongDTO2);
    }
}
