package application.infrastructure.adapter.mongo;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Generates sequential {@code long} ids for MongoDB collections through an atomic
 * upsert-and-increment on a counters document (Mongo does not auto-generate Long ids).
 */
@Component
@Profile("!in-memory")
public class MongoIdGenerator {

    private final MongoTemplate mongoTemplate;

    public MongoIdGenerator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public long nextId(String collectionName) {
        Long value = mongoTemplate.findAndModify(
                new Query(Criteria.where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().upsert(true).returnNew(true),
                Long.class);
        return value != null ? value : 1L;
    }
}
