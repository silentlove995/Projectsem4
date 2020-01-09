package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Pages;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Pages} entity.
 */
public interface PagesSearchRepository extends ElasticsearchRepository<Pages, Long> {
}
