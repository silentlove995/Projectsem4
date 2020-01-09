package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.AdsPlaylist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AdsPlaylist} entity.
 */
public interface AdsPlaylistSearchRepository extends ElasticsearchRepository<AdsPlaylist, Long> {
}
