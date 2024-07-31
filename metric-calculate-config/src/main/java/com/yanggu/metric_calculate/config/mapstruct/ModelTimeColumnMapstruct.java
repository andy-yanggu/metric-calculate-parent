package com.yanggu.metric_calculate.config.mapstruct;

import com.yanggu.metric_calculate.config.base.mapstruct.BaseMapstruct;
import com.yanggu.metric_calculate.config.domain.dto.ModelTimeColumnDTO;
import com.yanggu.metric_calculate.config.domain.entity.ModelTimeColumnEntity;
import com.yanggu.metric_calculate.config.domain.vo.ModelTimeColumnVO;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelTimeColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Named("ModelTimeColumnMapstruct")
@Mapper(componentModel = SPRING)
public interface ModelTimeColumnMapstruct extends BaseMapstruct<ModelTimeColumnEntity, ModelTimeColumnVO, ModelTimeColumnDTO> {

    @Named("toCoreTimeColumn")
    @Mapping(source = "modelColumnName", target = "columnName")
    @Mapping(source = "timeFormat", target = "timeFormat")
    ModelTimeColumn toCoreTimeColumn(ModelTimeColumnEntity modelTimeColumn);

}
