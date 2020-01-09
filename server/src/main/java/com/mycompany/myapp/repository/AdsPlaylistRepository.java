package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AdsPlaylist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AdsPlaylist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdsPlaylistRepository extends JpaRepository<AdsPlaylist, Long>, JpaSpecificationExecutor<AdsPlaylist> {

}
