/*
 * Copyright (c) 2018 JRJ, Inc.
 * 中国金融在线
 * http://www.jrj.com.cn/
 * All rights reserved.
 */

package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Page data.
 *
 * @param <T> the type parameter
 * @author 1
 * @date 2018-09-27 15:10:09
 */
@Getter
@Setter
public class PageData<T> {

    private Long total;
    private Long size;
    private Long current;
    private List<T> data;

}
