package HYU.FishShip.Common.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // AI 작업 큐 이름들
    public static final String AI_WORK_QUEUE = "ai.work.queue";
    public static final String AI_RESULT_QUEUE = "ai.result.queue";
    public static final String AI_EXCHANGE = "ai.exchange";
    public static final String AI_WORK_ROUTING_KEY = "ai.work";
    public static final String AI_RESULT_ROUTING_KEY = "ai.result";

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // AI Exchange 생성
    @Bean
    public TopicExchange aiExchange() {
        return new TopicExchange(AI_EXCHANGE);
    }

    // AI 작업 큐
    @Bean
    public Queue aiWorkQueue() {
        return QueueBuilder.durable(AI_WORK_QUEUE).build();
    }

    // AI 결과 큐
    @Bean
    public Queue aiResultQueue() {
        return QueueBuilder.durable(AI_RESULT_QUEUE).build();
    }

    // AI 작업 큐 바인딩
    @Bean
    public Binding aiWorkBinding() {
        return BindingBuilder
                .bind(aiWorkQueue())
                .to(aiExchange())
                .with(AI_WORK_ROUTING_KEY);
    }

    // AI 결과 큐 바인딩
    @Bean
    public Binding aiResultBinding() {
        return BindingBuilder
                .bind(aiResultQueue())
                .to(aiExchange())
                .with(AI_RESULT_ROUTING_KEY);
    }
}
