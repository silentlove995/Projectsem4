package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagesRepository extends JpaRepository<Pages, Long>, JpaSpecificationExecutor<Pages> {

}
