package application.infrastructure.adapter.mongo;

import org.bson.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * Generates sequential {@code long} ids for MongoDB collections through an atomic
 * upsert-and-increment on a dedicated {@code sequences} collection (Mongo does not
 * auto-generate Long ids). One counter document per target collection.
 */
@Component
@Profile("!in-memory")
public class MongoIdGenerator {

    /** Collection holding one counter document per target collection. */
    private static final String SEQUENCES = "sequences";

    private final MongoTemplate mongoTemplate;

    public MongoIdGenerator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public long nextId(String counterName) {
        Document counter = mongoTemplate.findAndModify(
                new Query(Criteria.where("_id").is(counterName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().upsert(true).returnNew(true),
                Document.class,
                SEQUENCES);
        Object seq = counter != null ? counter.get("seq") : null;
        return seq instanceof Number number ? number.longValue() : 1L;
    }
}
