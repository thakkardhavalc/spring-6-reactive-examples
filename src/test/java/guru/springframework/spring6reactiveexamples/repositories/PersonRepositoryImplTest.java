package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Created By dhaval on 2023-07-04
 */

@Slf4j
class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testGetByIdFound() {
        Mono<Person> personMono = personRepository.getById(3);

        assertThat(personMono.hasElement().block()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testGetByIdFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            log.info(person.getFirstName());
        });
    }

    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMono = personRepository.getById(6);

        assertThat(personMono.hasElement().block()).isFalse();
    }

    @Test
    void testGetByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(6);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            log.info(person.getFirstName());
        });
    }

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();
        assert person != null;
        log.info(person.toString());
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            log.info(person.toString());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(Person::getFirstName).subscribe(log::info);
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        assert person != null;
        log.info(person.toString());
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
           log.info(person.toString());
        });
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(log::info);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(list -> {
            list.forEach(person -> log.info(person.getFirstName()));
        });
    }

    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> log.info(person.getFirstName()));
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .next();

        fionaMono.subscribe(person -> log.info(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> Objects.equals(person.getId(), id)).single()
                .doOnError(throwable -> {
                    log.info("Error occurred in the flux");
                    log.info(throwable.toString());
                });

        personMono.subscribe(person -> {
            log.info(person.toString());
        }, throwable -> {
            log.info("Error occurred in the mono");
            log.info(throwable.toString());
        });
    }
}