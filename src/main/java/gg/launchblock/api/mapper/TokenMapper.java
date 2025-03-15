package gg.launchblock.api.mapper;

import gg.launchblock.api.entities.TokenEntity;
import gg.launchblock.api.models.tokens.response.ExposedTokenResponseModel;
import gg.launchblock.api.models.tokens.response.TokenResponseModel;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TokenMapper {

    ExposedTokenResponseModel toExposedToken(TokenEntity token);

    TokenResponseModel toModel(TokenEntity token);

    List<TokenResponseModel> toModels(final List<TokenEntity> tokenEntityList);

}
