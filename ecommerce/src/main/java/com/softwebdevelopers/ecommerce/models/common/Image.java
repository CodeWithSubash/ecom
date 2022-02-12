package com.softwebdevelopers.ecommerce.models.common;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column
    private String uuid;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String fileType;
}
