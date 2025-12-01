package example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestId, Principal principal) {
        CashCard cashCard = cashCardRepository.findByIdAndOwner(requestId, principal.getName());
        if(cashCard != null) {
            return ResponseEntity.ok(cashCard);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    private ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findAllByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                )
        );

        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    private ResponseEntity<Void> create(@RequestBody CashCard cashCard, UriComponentsBuilder ucb, Principal principal) {

        CashCard newCashCard = new CashCard(null, cashCard.getAmount(), principal.getName());
        CashCard savedCashCard = cashCardRepository.save(newCashCard);

        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @PutMapping("/{requestId}")
    private ResponseEntity<Void> update(@PathVariable Long requestId, @RequestBody CashCard cashCardUpdate, Principal principal) {

        CashCard cashCard = cashCardRepository.findByIdAndOwner(requestId, principal.getName());
        if (cashCard != null) {
            CashCard updatedCashCard = new CashCard(cashCard.getId(), cashCardUpdate.getAmount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{requestId}")
    private ResponseEntity<Void> delete(@PathVariable Long requestId, Principal principal) {

        if (cashCardRepository.existsByIdAndOwner(requestId, principal.getName())) {
            cashCardRepository.deleteById(requestId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
