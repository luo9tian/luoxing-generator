package com.luojiu.web.esdao;

import com.luojiu.web.model.dto.generator.GeneratorEsDTO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 */
public interface PostEsDao extends ElasticsearchRepository<GeneratorEsDTO, Long> {

    List<GeneratorEsDTO> findByUserId(Long userId);
}