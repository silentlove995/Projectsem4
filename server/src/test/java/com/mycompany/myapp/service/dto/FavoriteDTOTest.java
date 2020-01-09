package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class FavoriteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteDTO.class);
        FavoriteDTO favoriteDTO1 = new FavoriteDTO();
        favoriteDTO1.setId(1L);
        FavoriteDTO favoriteDTO2 = new FavoriteDTO();
        assertThat(favoriteDTO1).isNotEqualTo(favoriteDTO2);
        favoriteDTO2.setId(favoriteDTO1.getId());
        assertThat(favoriteDTO1).isEqualTo(favoriteDTO2);
        favoriteDTO2.setId(2L);
        assertThat(favoriteDTO1).isNotEqualTo(favoriteDTO2);
        favoriteDTO1.setId(null);
        assertThat(favoriteDTO1).isNotEqualTo(favoriteDTO2);
    }
}
