package cn.bcs.common.core.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author m
 * @date 2023/5/10 09:20
 */
@Data
@Accessors(chain = true)
public class FileVO {
    private String url;
    private String fileName;
    private String newFileName;
    private String originalFilename;
}
