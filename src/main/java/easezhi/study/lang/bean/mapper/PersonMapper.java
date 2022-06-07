package easezhi.study.lang.bean.mapper;

import easezhi.study.resource.pojo.*;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "gender", target = "sex")
    PersonDto toDto(Person person);
}
