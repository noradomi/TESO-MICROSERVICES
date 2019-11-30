package vn.hcmus.fit.truyenfull.data.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by Asus on 11/29/2019.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Tạo Standalone Connection tới Redis
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // tạo ra một RedisTemplate
        // Với Key là Object
        // Value là Object
        // RedisTemplate giúp chúng ta thao tác với Redis
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        configureSerializers(template);
        return template;
    }

    private void configureSerializers(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<String> serializerKey = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializerKey);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    }


//    @Bean
//    public Timer pushNoticationNearExpire(){
//        Calendar dayTime = Calendar.getInstance();
//        dayTime.set(Calendar.HOUR_OF_DAY,11);
//        dayTime.set(Calendar.MINUTE,0);
//        dayTime.set(Calendar.SECOND,00);
//        dayTime.set(Calendar.MILLISECOND,0);
//
//        Date daySchedule = dayTime.getTime();
////        So giay trong 1 gio
//        long period = 3600*24;
//
//        TimerTask dayTask = new TimerTask() {
//            @Override
//            public void run() {
//               RedisCache r = new RedisCache();
//               r.updateCache();
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(dayTask,daySchedule,period);
//        return timer;
//    }
}
