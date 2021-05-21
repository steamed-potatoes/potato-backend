package com.potato.domain.image;


import com.potato.domain.image.repository.BoardImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long>, BoardImageRepositoryCustom {
}
