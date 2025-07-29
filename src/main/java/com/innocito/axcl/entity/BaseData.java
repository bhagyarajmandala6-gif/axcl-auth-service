package com.innocito.axcl.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@FieldNameConstants
@Data
public class BaseData {
    @CreatedDate
    private Date createdOn;
    @LastModifiedDate
    private Date updatedOn;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
}