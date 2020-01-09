package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Songs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Songs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SongsRepository extends JpaRepository<Songs, Long>, JpaSpecificationExecutor<Songs> {

}
