package com.app.gest.immo.mapper;

import com.app.gest.immo.dto.PersonneDTO;
import com.app.gest.immo.entities.Personne;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T14:23:14+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.8 (Arch Linux)"
)
@Component
public class IPersonneMapperImpl implements IPersonneMapper {

    @Override
    public Personne toEntity(PersonneDTO personneDTO) {
        if ( personneDTO == null ) {
            return null;
        }

        Personne personne = new Personne();

        personne.setId( personneDTO.getId() );

        return personne;
    }

    @Override
    public PersonneDTO toDTO(Personne personne) {
        if ( personne == null ) {
            return null;
        }

        PersonneDTO personneDTO = new PersonneDTO();

        personneDTO.setId( personne.getId() );

        return personneDTO;
    }
}
