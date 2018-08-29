package ru.shishmakov.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.domain.Book;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoDbSpringIntegrationTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BookRepository authorRepository;

//    private MongodExecutable mongodExecutable;
//
//
//    @Before
//    public void setup() throws Exception {
//        String ip = "localhost";
//        int port = 27017;
//
//        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
//                .net(new Net(ip, port, Network.localhostIsIPv6()))
//                .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongodExecutable.start();
//        mongoTemplate = new MongoTemplate(new MongoClient(ip, port), "test");
//    }
//
//    @After
//    public void clean() {
//        mongodExecutable.stop();
//    }

    @Test
    public void test1() {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        mongoTemplate.save(objectToSave, "collection");

        // then
        Assertions.assertThat(mongoTemplate.findAll(DBObject.class, "collection"))
                .extracting("key")
                .containsOnly("value");
    }

    @Test
    public void test2() {
        // given
        Book book = Book.builder().isbn("1212-01").title("title 1").build();

        // when
        authorRepository.save(book);

        // then
        List<Book> all = authorRepository.findAll();
        Assertions.assertThat(all)
                .isNotNull()
                .isNotEmpty();
    }
}
