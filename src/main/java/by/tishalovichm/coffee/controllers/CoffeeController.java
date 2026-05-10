package by.tishalovichm.coffee.controllers;

import by.tishalovichm.coffee.dtos.in.CoffeeDtoIn;
import by.tishalovichm.coffee.entities.Coffee;
import by.tishalovichm.coffee.repositories.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/coffees")
@CrossOrigin(origins = "http://localhost:9090")
public class CoffeeController {

    private final CoffeeRepository repository;

    @GetMapping
    public Iterable<Coffee> getCoffees() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Coffee getCoffees(@PathVariable long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Coffee createCoffee(@RequestBody CoffeeDtoIn coffeeDtoIn) {
        var coffee = new Coffee(null, coffeeDtoIn.name(), coffeeDtoIn.description());
        return repository.save(coffee);
    }

    @GetMapping("proposal")
    public String proposal() {
        String username = SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal().toString();

        return "It's time to taste latte, %s!".formatted(username);
    }

    @PutMapping("{id}")
    public Coffee updateCoffee(@PathVariable Long id, @RequestBody CoffeeDtoIn coffeeDtoIn) {
        var coffee = new Coffee(id, coffeeDtoIn.name(), coffeeDtoIn.description());
        return repository.save(coffee);
    }
}
