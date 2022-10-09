package miniproject.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfiguration {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.database}")
    private String redisdb;

    @Value("${spring.redis.username}")
    private String redisUserName;

    /*
     * For Redis password, use this CLI command:
     * export REDIS_PASSWORD="<password here>"
     * at the root folder before performing ./mvnw package and/or ./mvnw
     * spring-boot:run
     */
    @Value("${REDIS_PASSWORD}")
    private String redisPassword;

    @Bean("repository")
    public RedisTemplate initRedisTemplate() {
        // 1st Step: Config Redis Database
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(Integer.parseInt(redisPort));
        redisConfig.setDatabase(Integer.parseInt(redisdb));
        redisConfig.setUsername(redisUserName);
        redisConfig.setPassword(redisPassword);

        // 2nd Step: Create instance of Jedis Driver
        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().build();

        // 3rd Step: Create factory for jedis connection
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(redisConfig, jedisConfig);
        jedisFac.afterPropertiesSet();

        // Last Step: Create new redis template
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}