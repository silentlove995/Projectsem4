package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Posts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Posts} entity.
 */
public interface PostsSearchRepository extends ElasticsearchRepository<Posts, Long> {
}
