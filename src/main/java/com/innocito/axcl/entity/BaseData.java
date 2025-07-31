package com.innocito.axcl.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@FieldNameConstants
@Data
@MappedSuperclass
public class BaseData {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedOn;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
    private String deletedBy;
}