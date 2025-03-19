package com.example.ecommerce.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {

    @InjectMocks
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Test
    void testQueueConfiguration() {
        // Set the required properties
        ReflectionTestUtils.setField(rabbitMQConfig, "queueName", "order-status-queue");
        
        // Test queue creation
        Queue queue = rabbitMQConfig.queue();
        assertNotNull(queue);
        assertEquals("order-status-queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void testExchangeConfiguration() {
        // Set the required properties
        ReflectionTestUtils.setField(rabbitMQConfig, "exchangeName", "order-status-exchange");
        
        // Test exchange creation
        TopicExchange exchange = rabbitMQConfig.exchange();
        assertNotNull(exchange);
        assertEquals("order-status-exchange", exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void testBindingConfiguration() {
        // Set the required properties
        ReflectionTestUtils.setField(rabbitMQConfig, "queueName", "order-status-queue");
        ReflectionTestUtils.setField(rabbitMQConfig, "exchangeName", "order-status-exchange");
        ReflectionTestUtils.setField(rabbitMQConfig, "routingKey", "order.status.#");
        
        // Create queue and exchange
        Queue queue = rabbitMQConfig.queue();
        TopicExchange exchange = rabbitMQConfig.exchange();
        
        // Test binding creation
        Binding binding = rabbitMQConfig.binding(queue, exchange);
        assertNotNull(binding);
        assertEquals("order.status.#", binding.getRoutingKey());
        assertEquals("order-status-queue", binding.getDestination());
        assertEquals("order-status-exchange", binding.getExchange());
    }

    @Test
    void testMessageConverter() {
        MessageConverter converter = rabbitMQConfig.messageConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof Jackson2JsonMessageConverter);
    }

    @Test
    void testRabbitTemplate() {
        RabbitTemplate template = rabbitMQConfig.rabbitTemplate(connectionFactory);
        
        assertNotNull(template);
        assertEquals(connectionFactory, template.getConnectionFactory());
        assertNotNull(template.getMessageConverter());
        assertTrue(template.getMessageConverter() instanceof Jackson2JsonMessageConverter);
    }
} 