package soloProject.soloProject.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soloProject.soloProject.todo.entity.Todos;

@Repository
public interface TodoRepository extends JpaRepository<Todos, Integer> {
}
