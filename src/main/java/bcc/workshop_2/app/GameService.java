package bcc.workshop_2.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

//@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final RedisTemplate<String, String> redisTemplate;

    public String save(String key, String value, Long duration){
        var ops = redisTemplate.opsForValue();
        if(duration != null){
            ops.set(key, value, Duration.ofSeconds(duration));
            return "OK";
        } else {
            return "No duration found!";
        }
    }

    public String get(String key){
        var ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    public Map<String, String> getVideosWithoutRedis(){
        return this.fetchDataFromMySQL();
    }

    @SneakyThrows
    public Map<String, String> getVideosWithRedis(){
        // kita simpan ke dalam cache namanya video-game
        String key = "video-game";
        var objectMapper = new ObjectMapper();
        var ops = this.redisTemplate.opsForValue();

        var redisResult = ops.get(key);

        if(redisResult == null){ // cache miss berarti harus minta dari database MySQL
            log.info("cache miss!");
            var data = fetchDataFromMySQL();
            ops.set(key, objectMapper.writeValueAsString(data), Duration.ofSeconds(30));
            return data;
        } else {
            log.info("cache hit!");
            return objectMapper.readValue(
                    redisResult,
                    new TypeReference<Map<String, String>>() {}
            );
        }
    }

    @SneakyThrows
    private Map<String, String> fetchDataFromMySQL(){
        var data = Map.ofEntries(
                Map.entry("id", UUID.randomUUID().toString()),
                Map.entry("name", "Anonymous")
        );

        log.info("query: SELECT * FROM game_video LIMIT 10000");

        // asumsikan bahwa proses memproses data butuh 3 detik
        Thread.sleep(3000);

        return data;
    }
}
