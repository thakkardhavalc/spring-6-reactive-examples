package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created By dhaval on 2023-06-30
 */
public interface PersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
