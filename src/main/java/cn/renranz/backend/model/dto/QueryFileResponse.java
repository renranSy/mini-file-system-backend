package cn.renranz.backend.model.dto;

import cn.renranz.backend.model.domain.File;
import lombok.Data;

import java.util.List;

@Data
public class QueryFileResponse {
    List<File> list;
}
