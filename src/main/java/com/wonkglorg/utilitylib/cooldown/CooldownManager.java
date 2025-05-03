package com.wonkglorg.utilitylib.cooldown;

import com.wonkglorg.utilitylib.converter.time.TimeBuilder;
import org.bukkit.entity.Player;

import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@ThreadSafe
public final class CooldownManager{
	/**
	 * A cooldown map that is split into different cooldowns, ehere each entry is a list of players and an entry of "time of addition + cooldown"
	 */
	private final Map<String, Map<UUID, Cooldown>> cooldowns = new ConcurrentHashMap<>();
	
	public CooldownManager() {
		//Singleton Constructor
	}
	
	/**
	 * Gets all cooldowns for a specific key
	 */
	public synchronized Map<UUID, Cooldown> getCooldowns(String key) {
		return cooldowns.getOrDefault(key, null);
	}
	
	/**
	 * Clears all cooldowns
	 */
	public synchronized void clear() {
		cooldowns.clear();
	}
	
	/**
	 * Creates a new cooldown entry for the specified key
	 *
	 * @param key the key to create a cooldown for
	 */
	public synchronized void createCooldown(String key) {
		cooldowns.putIfAbsent(key, new HashMap<>());
	}
	
	/**
	 * Adds a cooldown for the specified player
	 * @param key the key to add the cooldown to
	 * @param uuid the player to add the cooldown for
	 * @param duration the duration of the cooldown
	 */
	public synchronized void addCooldown(String key, UUID uuid, Duration duration) {
		createCooldown(key);
		cooldowns.get(key).put(uuid, new Cooldown(duration));
	}
	
	/**
	 * Adds a cooldown for the specified player
	 * @param key the key to add the cooldown to
	 * @param uuid the player to add the cooldown for
	 * @param cooldown the cooldown to add
	 */
	public synchronized void addCooldown(String key, UUID uuid, Cooldown cooldown) {
		createCooldown(key);
		cooldowns.get(key).put(uuid, cooldown);
	}
	
	/**
	 * Adds a new cooldown for the specified player
	 * @param key the key to add the cooldown for
	 * @param player the player to add the cooldown for
	 * @param duration the duration of the cooldown in seconds
	 *  @param unit the time unit to use (If the resulting units size in milliseconds exceeds the
	 */
	public synchronized void addCooldown(String key, Player player, long duration, ChronoUnit unit) {
		addCooldown(key, player.getUniqueId(), new Cooldown(duration, unit));
	}
	
	/**
	 * Removes the cooldown for the specified player
	 *
	 * @param key  the key to remove the cooldown from
	 * @param uuid the player to remove the cooldown from
	 */
	public synchronized void removeCooldown(String key, UUID uuid) {
		createCooldown(key);
		getCooldowns(key).remove(uuid);
	}
	
	/**
	 * Removes the cooldown for the specified player
	 *
	 * @param key    the key to remove the cooldown from
	 * @param player the player to remove the cooldown from
	 */
	public synchronized void removeCooldown(String key, Player player) {
		removeCooldown(key, player.getUniqueId());
	}
	
	/**
	 * Checks if the player has a cooldown left for the specified key
	 *
	 * @param key  the key to check
	 * @param uuid the player to check
	 * @return boolean true if the player has a cooldown left false if no entry exists or it expired.
	 */
	public synchronized boolean isCooldown(String key, UUID uuid) {
		createCooldown(key);
		return getCooldown(key, uuid).isExpired();
	}
	
	/**
	 * Checks if the player has a cooldown left for the specified key
	 *
	 * @param key    the key to check
	 * @param player the player to check
	 * @return boolean true if the player has a cooldown left false if no entry exists or it expired.
	 */
	public synchronized boolean isCooldown(String key, Player player) {
		return isCooldown(key, player.getUniqueId());
	}
	
	/**
	 * gets the remaining cooldown for the player or an empty cooldown with 0 time if no entry exists
	 *
	 * @param key  the key to check
	 * @param uuid the player to check
	 * @return the remaining cooldown
	 */
	public synchronized Cooldown getCooldown(String key, UUID uuid) {
		createCooldown(key);
		return cooldowns.get(key).getOrDefault(uuid, new Cooldown(0));
	}
	
	/**
	 * gets the remaining cooldown for the player or an empty cooldown with 0 time if no entry exists
	 *
	 * @param key    the key to check
	 * @param player the player to check
	 * @return the remaining cooldown
	 */
	public synchronized Cooldown getCooldown(String key, Player player) {
		return getCooldown(key, player.getUniqueId());
	}
	
	/**
	 * @param key    the key to check
	 * @param player the player to check
	 * @return the remaining time in milliseconds from the current timestamp to its expiration if the
	 */
	public synchronized String getCooldownAsString(String key, UUID player) {
		return TimeBuilder.toTimeString().inputMillie(getCooldown(key, player).getRemainingTime()).build();
	}
	
	public Map<String, Map<UUID, Cooldown>> getCooldownMap() {
		return cooldowns;
	}
	
}
