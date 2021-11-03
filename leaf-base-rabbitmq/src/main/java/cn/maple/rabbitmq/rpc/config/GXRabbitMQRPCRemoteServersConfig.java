package cn.maple.rabbitmq.rpc.config;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@PropertySource(value = {"classpath:/${spring.profiles.active}/rabbit.yml"},
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "rabbit.rpc-server.remote")
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXRabbitMQRPCRemoteServersConfig {
    private Map<String, Map<String, Object>> servers = new HashMap<>();
}