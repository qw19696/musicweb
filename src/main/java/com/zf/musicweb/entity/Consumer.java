package com.zf.musicweb.entity;import com.baomidou.mybatisplus.annotation.*;import java.time.LocalDateTime;import java.io.Serializable;import java.util.Date;import com.fasterxml.jackson.annotation.JsonFormat;import io.swagger.annotations.ApiModel;import io.swagger.annotations.ApiModelProperty;import lombok.Data;import lombok.EqualsAndHashCode;/** * <p> * * </p> * * @author ${author} * @since 2022-01-05 */@Data@EqualsAndHashCode(callSuper = false)@TableName("consumer")@ApiModel(value="Consumer对象", description="")public class Consumer implements Serializable {    private static final long serialVersionUID = 1L;    @TableId(value = "id", type = IdType.AUTO)    private Integer id;    private String username;    private String password;    private Byte sex;    private String phoneNum;    private String email;    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")    private Date birth;    private String introduction;    private String location;    private String avator;    private Date createTime;    private Date updateTime;}