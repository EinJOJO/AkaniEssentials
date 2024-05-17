package it.einjojo.akani.essentials.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public record TpaService(JedisPool pool) {
    private static final String REDIS_PREFIX = "akani:essentials:tpa:"; // uuid -> tpa-sender
    private static final int TPA_TIMEOUT = 60; // 60 seconds


    public void storeTpa(UUID sender, UUID receiver) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(REDIS_PREFIX + receiver, sender.toString(), SetParams.setParams().ex(TPA_TIMEOUT));
        }
    }

    public UUID getTpa(UUID receiver) {
        try (Jedis jedis = pool.getResource()) {
            String sender = jedis.get(REDIS_PREFIX + receiver);
            if (sender == null) {
                return null;
            }
            return UUID.fromString(sender);
        }
    }

    public void removeTpa(UUID receiver) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(REDIS_PREFIX + receiver);
        }
    }

}
