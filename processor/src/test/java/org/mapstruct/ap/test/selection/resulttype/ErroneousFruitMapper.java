/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.selection.resulttype;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Sjaak Derksen
 */
@Mapper(uses = ConflictingFruitFactory.class)

public interface ErroneousFruitMapper {

    ErroneousFruitMapper INSTANCE = Mappers.getMapper( ErroneousFruitMapper.class );

    @Mapping(target = "type", ignore = true)
    Fruit map(FruitDto source);

}
