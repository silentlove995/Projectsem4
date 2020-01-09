package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.AdsSong;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AdsSong} entity.
 */
public interface AdsSongSearchRepository extends ElasticsearchRepository<AdsSong, Long> {
}
