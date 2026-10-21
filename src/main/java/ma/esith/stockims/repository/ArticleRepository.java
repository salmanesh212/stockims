package ma.esith.stockims.repository;

import ma.esith.stockims.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Article a where a.id = :id")
	Optional<Article> verrouiller(@Param("id") Long id);
}
