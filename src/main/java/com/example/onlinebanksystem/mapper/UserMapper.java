package com.example.onlinebanksystem.mapper;

import com.example.onlinebanksystem.model.dto.SignupRequest;
import com.example.onlinebanksystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // SignupRequest içindəki passwordu encode etməliyik, amma MapStruct-da bunu yazmaq qarışıqdır,
    // ona görə encode etməyi service-də edərik.
    @Mapping(target = "id", ignore = true) // id avtomatik təyin olunur, DTO-da yoxdu
    @Mapping(target = "password", ignore = true) // password encoding servisdə olacaq
    User toEntity(SignupRequest signupRequest);

    SignupRequest toDto(User user);
}
