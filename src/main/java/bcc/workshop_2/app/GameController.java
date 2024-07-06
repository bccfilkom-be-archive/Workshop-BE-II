package bcc.workshop_2.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/save/{key}/{value}/{duration}")
    public String save(
            @PathVariable("key") String key,
            @PathVariable("value") String value,
            @PathVariable("duration") Long duration
    ){
        return this.gameService.save(key, value, duration);
    }

    @GetMapping("/get/{key}")
    public String get(
            @PathVariable("key") String key
    ){
        return this.gameService.get(key);
    }

    @GetMapping("/get/video/mysql")
    public Map<String, String> getVideoWithoutRedis(){
        return this.gameService.getVideosWithoutRedis();
    }

    @GetMapping("/get/video/redis")
    public Map<String, String> getVideoWithRedis(){
        return this.gameService.getVideosWithRedis();
    }
}
