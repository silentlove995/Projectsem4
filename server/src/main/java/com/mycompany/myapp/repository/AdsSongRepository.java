package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AdsSong;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AdsSong entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdsSongRepository extends JpaRepository<AdsSong, Long>, JpaSpecificationExecutor<AdsSong> {

}
