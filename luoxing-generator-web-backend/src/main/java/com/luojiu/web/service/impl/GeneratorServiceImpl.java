package com.luojiu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luojiu.web.model.entity.User;
import com.luojiu.web.common.ErrorCode;
import com.luojiu.web.constant.CommonConstant;
import com.luojiu.web.exception.BusinessException;
import com.luojiu.web.exception.ThrowUtils;
import com.luojiu.web.mapper.GeneratorMapper;
import com.luojiu.web.model.dto.generator.GeneratorEsDTO;
import com.luojiu.web.model.dto.generator.GeneratorQueryRequest;
import com.luojiu.web.model.entity.Generator;
import com.luojiu.web.model.vo.GeneratorVO;
import com.luojiu.web.model.vo.UserVO;
import com.luojiu.web.service.GeneratorService;
import com.luojiu.web.service.UserService;
import com.luojiu.web.utils.SqlUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * 生成器服务实现
 */
@Service
@Slf4j
public class GeneratorServiceImpl extends ServiceImpl<GeneratorMapper, Generator> implements GeneratorService {

    @Resource
    private UserService userService;

//    @Resource
//    private GeneratorThumbMapper generatorThumbMapper;
//
//    @Resource
//    private GeneratorFavourMapper generatorFavourMapper;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Override
    public void validGenerator(Generator generator, boolean add) {
        if (generator == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = generator.getName();
        String description = generator.getDescription();
        String tags = generator.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() >256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param generatorQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest) {
        QueryWrapper<Generator> queryWrapper = new QueryWrapper<>();
        if (generatorQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = generatorQueryRequest.getSearchText();
        String sortField = generatorQueryRequest.getSortField();
        String sortOrder = generatorQueryRequest.getSortOrder();
        Long id = generatorQueryRequest.getId();
        String name = generatorQueryRequest.getName();
        String description = generatorQueryRequest.getDescription();
        Long userId = generatorQueryRequest.getUserId();
        Long notId = generatorQueryRequest.getNotId();
        String basePackage = generatorQueryRequest.getBasePackage();
        String version = generatorQueryRequest.getVersion();
        String author = generatorQueryRequest.getAuthor();
        List<String> tags = generatorQueryRequest.getTags();
        String distPath = generatorQueryRequest.getDistPath();
        Integer status = generatorQueryRequest.getStatus();
        int current = generatorQueryRequest.getCurrent();
        int pageSize = generatorQueryRequest.getPageSize();

        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.ne(ObjectUtils.isNotEmpty(basePackage), "basePackage", basePackage);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "version", version);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "author", author);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "distPath", distPath);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

   /* @Override
    public Page<Generator> searchFromEs(GeneratorQueryRequest generatorQueryRequest) {
        Long id = generatorQueryRequest.getId();
        Long notId = generatorQueryRequest.getNotId();
        String searchText = generatorQueryRequest.getSearchText();
        String name = generatorQueryRequest.getName();
        String description = generatorQueryRequest.getDescription();
        List<String> tagList = generatorQueryRequest.getTags();
        List<String> orTagList = generatorQueryRequest.getOrTags();
        Long userId = generatorQueryRequest.getUserId();
        // es 起始页为 0
        long current = generatorQueryRequest.getCurrent() - 1;
        long pageSize = generatorQueryRequest.getPageSize();
        String sortField = generatorQueryRequest.getSortField();
        String sortOrder = generatorQueryRequest.getSortOrder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (notId != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任何一个标签即可
        if (CollUtil.isNotEmpty(orTagList)) {
            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String tag : orTagList) {
                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
            }
            orTagBoolQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.filter(orTagBoolQueryBuilder);
        }
        // 按关键词检索
        if (StringUtils.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("name", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.isNotBlank(name)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("name", name));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(description)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", description));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
        SearchHits<GeneratorEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, GeneratorEsDTO.class);
        Page<Generator> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Generator> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<GeneratorEsDTO>> searchHitList = searchHits.getSearchHits();
            List<Long> generatorIdList = searchHitList.stream().map(searchHit -> searchHit.getDescription().getId())
                    .collect(Collectors.toList());
            List<Generator> generatorList = baseMapper.selectBatchIds(generatorIdList);
            if (generatorList != null) {
                Map<Long, List<Generator>> idGeneratorMap = generatorList.stream().collect(Collectors.groupingBy(Generator::getId));
                generatorIdList.forEach(generatorId -> {
                    if (idGeneratorMap.containsKey(generatorId)) {
                        resourceList.add(idGeneratorMap.get(generatorId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(generatorId), GeneratorEsDTO.class);
                        log.info("delete generator {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        return page;
    }
*/
    public GeneratorVO getGeneratorVO(Generator generator, HttpServletRequest request) {
        GeneratorVO generatorVO = GeneratorVO.objToVo(generator);
        long generatorId = generator.getId();
        // 1. 关联查询用户信息
        Long userId = generator.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        generatorVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
           /* // 获取点赞
            QueryWrapper<GeneratorThumb> generatorThumbQueryWrapper = new QueryWrapper<>();
            generatorThumbQueryWrapper.in("generatorId", generatorId);
            generatorThumbQueryWrapper.eq("userId", loginUser.getId());
            GeneratorThumb generatorThumb = generatorThumbMapper.selectOne(generatorThumbQueryWrapper);
            generatorVO.setHasThumb(generatorThumb != null);
            // 获取收藏
            QueryWrapper<GeneratorFavour> generatorFavourQueryWrapper = new QueryWrapper<>();
            generatorFavourQueryWrapper.in("generatorId", generatorId);
            generatorFavourQueryWrapper.eq("userId", loginUser.getId());
            GeneratorFavour generatorFavour = generatorFavourMapper.selectOne(generatorFavourQueryWrapper);
            generatorVO.setHasFavour(generatorFavour != null);*/
        }
        return generatorVO;
    }
    @Override
    public Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage, HttpServletRequest request) {
        List<Generator> generatorList = generatorPage.getRecords();
        Page<GeneratorVO> generatorVOPage = new Page<>(generatorPage.getCurrent(), generatorPage.getSize(), generatorPage.getTotal());
        if (CollUtil.isEmpty(generatorList)) {
            return generatorVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = generatorList.stream().map(Generator::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> generatorIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> generatorIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> generatorIdSet = generatorList.stream().map(Generator::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            /*// 获取点赞
            QueryWrapper<GeneratorThumb> generatorThumbQueryWrapper = new QueryWrapper<>();
            generatorThumbQueryWrapper.in("generatorId", generatorIdSet);
            generatorThumbQueryWrapper.eq("userId", loginUser.getId());
            List<GeneratorThumb> generatorGeneratorThumbList = generatorThumbMapper.selectList(generatorThumbQueryWrapper);
            generatorGeneratorThumbList.forEach(generatorGeneratorThumb -> generatorIdHasThumbMap.put(generatorGeneratorThumb.getGeneratorId(), true));
            // 获取收藏
            QueryWrapper<GeneratorFavour> generatorFavourQueryWrapper = new QueryWrapper<>();
            generatorFavourQueryWrapper.in("generatorId", generatorIdSet);
            generatorFavourQueryWrapper.eq("userId", loginUser.getId());
            List<GeneratorFavour> generatorFavourList = generatorFavourMapper.selectList(generatorFavourQueryWrapper);
            generatorFavourList.forEach(generatorFavour -> generatorIdHasFavourMap.put(generatorFavour.getGeneratorId(), true));*/
        }
        // 填充信息
        List<GeneratorVO> generatorVOList = generatorList.stream().map(generator -> {
            GeneratorVO generatorVO = GeneratorVO.objToVo(generator);
            Long userId = generator.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            generatorVO.setUser(userService.getUserVO(user));
//            generatorVO.setHasThumb(generatorIdHasThumbMap.getOrDefault(generator.getId(), false));
//            generatorVO.setHasFavour(generatorIdHasFavourMap.getOrDefault(generator.getId(), false));
            return generatorVO;
        }).collect(Collectors.toList());
        generatorVOPage.setRecords(generatorVOList);
        return generatorVOPage;
    }

}




