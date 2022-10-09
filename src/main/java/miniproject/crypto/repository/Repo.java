package miniproject.crypto.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import miniproject.crypto.models.CryptoSentiment;

@Repository
public class Repo {

    @Autowired
    @Qualifier("repository")
    private RedisTemplate<String, String> repo;

    @Value("${spring.redis.cachetime}")
    private Long cachetime;

    public void save(CryptoSentiment cryptoModel) {
        ValueOperations<String, String> ops = repo.opsForValue();
        String key = cryptoModel.getSymbol();
        String value = cryptoModel.getSentiment();
        Duration timeout = Duration.ofMinutes(cachetime);
        System.out.print("Saving to Redis");
        ops.set(key, value, timeout);
    }

    public Optional<String> get(String symbol) {
        ValueOperations<String, String> ops = repo.opsForValue();
        String sentiment = ops.get(symbol.toLowerCase());

        if (null == sentiment) {
            return Optional.empty();
        } else {
            return Optional.of(sentiment);
        }
    }
}
