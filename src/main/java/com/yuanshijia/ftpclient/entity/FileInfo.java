package com.yuanshijia.ftpclient.entity;

import java.util.Date;

/**
 * @author yuan
 * @date 2019/10/21
 * @description
 */
public class FileInfo {

    /**
     * 文件id
     */
    private String uuid;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 保存路径
     */
    private String savePath;

    public FileInfo(){

    }


    public FileInfo(String uuid, long size, String type, String name, Date createTime, String savePath) {
        this.uuid = uuid;
        this.size = size;
        this.type = type;
        this.name = name;
        this.createTime = createTime;
        this.savePath = savePath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "uuid='" + uuid + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
