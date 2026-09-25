package application.infrastructure.adapter.inmemory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Shared in-memory storage used by every {@code InMemory*Repository}. Assigns
 * sequential ids on the first save, the same way an auto-increment column
 * would, so entities keep the {@code assignId} contract.
 *
 * <p>These adapters are development stand-ins for the real MySQL/MongoDB
 * adapters: they keep the application runnable while the persistence layer is
 * not implemented. Swap them for JPA/Mongo adapters without touching the
 * domain or the application services.</p>
 */
public abstract class InMemoryRepository<T> {

    private final Map<Long, T> rows = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);
    private final Function<T, Long> idGetter;
    private final BiConsumer<T, Long> idSetter;

    protected InMemoryRepository(Function<T, Long> idGetter, BiConsumer<T, Long> idSetter) {
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    protected T persist(T entity) {
        Long id = idGetter.apply(entity);
        if (id == null) {
            id = sequence.getAndIncrement();
            idSetter.accept(entity, id);
        }
        rows.put(id, entity);
        return entity;
    }

    protected Optional<T> row(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(rows.get(id));
    }

    protected List<T> all() {
        return List.copyOf(rows.values());
    }
}
