package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Songs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Songs} entity.
 */
public interface SongsSearchRepository extends ElasticsearchRepository<Songs, Long> {
}
