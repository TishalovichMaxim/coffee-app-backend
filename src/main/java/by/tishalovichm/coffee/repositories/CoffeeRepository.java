package by.tishalovichm.coffee.repositories;

import by.tishalovichm.coffee.entities.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
