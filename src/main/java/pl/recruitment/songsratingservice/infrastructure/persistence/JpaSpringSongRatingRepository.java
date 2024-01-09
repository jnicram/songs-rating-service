package pl.recruitment.songsratingservice.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.repository.Repository;

interface JpaSpringSongRatingRepository extends Repository<SongRatingEntity, UUID> {

}
