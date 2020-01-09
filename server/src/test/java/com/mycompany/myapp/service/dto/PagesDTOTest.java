package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PagesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagesDTO.class);
        PagesDTO pagesDTO1 = new PagesDTO();
        pagesDTO1.setId(1L);
        PagesDTO pagesDTO2 = new PagesDTO();
        assertThat(pagesDTO1).isNotEqualTo(pagesDTO2);
        pagesDTO2.setId(pagesDTO1.getId());
        assertThat(pagesDTO1).isEqualTo(pagesDTO2);
        pagesDTO2.setId(2L);
        assertThat(pagesDTO1).isNotEqualTo(pagesDTO2);
        pagesDTO1.setId(null);
        assertThat(pagesDTO1).isNotEqualTo(pagesDTO2);
    }
}
