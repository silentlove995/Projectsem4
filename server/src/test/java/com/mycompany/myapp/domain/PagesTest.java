package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PagesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pages.class);
        Pages pages1 = new Pages();
        pages1.setId(1L);
        Pages pages2 = new Pages();
        pages2.setId(pages1.getId());
        assertThat(pages1).isEqualTo(pages2);
        pages2.setId(2L);
        assertThat(pages1).isNotEqualTo(pages2);
        pages1.setId(null);
        assertThat(pages1).isNotEqualTo(pages2);
    }
}
