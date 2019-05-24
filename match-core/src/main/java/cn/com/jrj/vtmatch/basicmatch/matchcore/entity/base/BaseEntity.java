/*
 * Copyright (c) 2018 JRJ, Inc.
 * 中国金融在线
 * http://www.jrj.com.cn/
 * All rights reserved.
 */

package cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * 基础 entity.
 *
 * @param <T> the type parameter
 * @author 1
 * @date 2018-09-27 15:07:37
 */
public class BaseEntity<T extends Model> extends Model<T> implements Serializable {

    /**
     * The Id.
     */
    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    /**
     * Instantiates a new Base entity.
     */
    public BaseEntity() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * Instantiates a new Base entity.
     *
     * @param id the id
     */
    public BaseEntity(Long id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
