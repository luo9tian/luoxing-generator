package com.luojiu.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luojiu.web.exception.BusinessException;
import com.luojiu.web.exception.ThrowUtils;
import com.luojiu.web.model.entity.Post;
import com.luojiu.web.model.entity.User;
import com.luojiu.web.model.vo.GeneratorVO;
import com.luojiu.web.common.BaseResponse;
import com.luojiu.web.common.ErrorCode;
import com.luojiu.web.common.ResultUtils;
import com.luojiu.web.model.dto.generator.GeneratorQueryRequest;
import com.luojiu.web.model.dto.postfavour.PostFavourAddRequest;
import com.luojiu.web.model.dto.postfavour.PostFavourQueryRequest;
import com.luojiu.web.service.PostFavourService;
import com.luojiu.web.service.GeneratorService;
import com.luojiu.web.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子收藏接口
 *
 */
@RestController
@RequestMapping("/post_favour")
@Slf4j
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private GeneratorService generatorService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
/*
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody PostFavourAddRequest postFavourAddRequest,
            HttpServletRequest request) {
        if (postFavourAddRequest == null || postFavourAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long postId = postFavourAddRequest.getPostId();
        int result = postFavourService.doPostFavour(postId, loginUser);
        return ResultUtils.success(result);
    }*/

    /**
     * 获取我收藏的帖子列表
     *
     * @param generatorQueryRequest
     * @param request
     */
/*

    @PostMapping("/my/list/page")
    public BaseResponse<Page<GeneratorVO>> listMyFavourPostByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                  HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest), loginUser.getId());
        return ResultUtils.success(generatorService.getPostVOPage(postPage, request));
    }
*/

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
/*
    @PostMapping("/list/page")
    public BaseResponse<Page<GeneratorVO>> listFavourPostByPage(@RequestBody PostFavourQueryRequest postFavourQueryRequest,
                                                                HttpServletRequest request) {
        if (postFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = postFavourQueryRequest.getCurrent();
        long size = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                generatorService.getQueryWrapper(postFavourQueryRequest.getGeneratorQueryRequest()), userId);
        return ResultUtils.success(generatorService.getPostVOPage(postPage, request));
    }*/
}
