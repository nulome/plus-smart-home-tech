package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.repository.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
