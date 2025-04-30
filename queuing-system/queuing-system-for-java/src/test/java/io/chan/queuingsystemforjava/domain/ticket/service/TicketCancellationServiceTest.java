package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TicketRepository.class, })
class TicketCancellationServiceTest {

}