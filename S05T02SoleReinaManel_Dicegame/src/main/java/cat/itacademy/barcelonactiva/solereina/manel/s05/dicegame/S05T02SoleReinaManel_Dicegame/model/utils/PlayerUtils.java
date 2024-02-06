package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.utils;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class PlayerUtils {
    public static final String DEFAULT_NAME = "ANÒNIM";
/*
    public static PlayerDTO toDTO(PlayerEntity entity) {
        ModelMapper mapper = new ModelMapper();
       // TypeMap<PlayerEntity, PlayerDTO> propertyMapper = mapper.createTypeMap(PlayerEntity.class, PlayerDTO.class);
        //propertyMapper.addMapping(PlayerEntity::getTimestamp, PlayerDTO::setSuccessRate);
        PlayerDTO dto = new PlayerDTO();

        mapper.map(entity, dto);
        return dto;
    }

    public static PlayerEntity toEntity(PlayerDTO dto) {
        ModelMapper mapper = new ModelMapper();
        PlayerEntity entity = new PlayerEntity();

        mapper.map(dto, entity);
        return entity;
    }
*/
}
