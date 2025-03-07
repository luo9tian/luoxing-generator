package com.luojiu.web.job.once;

import com.luojiu.web.model.entity.Generator;
import com.luojiu.web.model.dto.generator.GeneratorEsDTO;
import com.luojiu.web.service.GeneratorService;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.CommandLineRunner;

/**
 * 全量同步帖子到 es
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private GeneratorService generatorService;

    @Resource
//    private GeneratorEsDao postEsDao;

    @Override
    public void run(String... args) {
        List<Generator> postList = generatorService.list();
        if (CollUtil.isEmpty(postList)) {
            return;
        }
//        List<GeneratorEsDTO> generatorEsDTOList = postList.stream().map(GeneratorEsDTO::objToDto).collect(Collectors.toList());
//        final int pageSize = 500;
//        int total = generatorEsDTOList.size();
//        log.info("FullSyncGeneratorToEs start, total {}", total);
//        for (int i = 0; i < total; i += pageSize) {
//            int end = Math.min(i + pageSize, total);
//            log.info("sync from {} to {}", i, end);
//            postEsDao.saveAll(generatorEsDTOList.subList(i, end));
//        }
//        log.info("FullSyncGeneratorToEs end, total {}", total);
    }
}
