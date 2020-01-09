package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Album} entity.
 */
public interface AlbumSearchRepository extends ElasticsearchRepository<Album, Long> {
}
