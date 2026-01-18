package net.cfh.vault;

/*
    This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.milkbowl.vault2.chat.ChatUnlocked;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.permission.PermissionUnlocked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * VaultUnlockedServicesManager
 *
 * @author creatorfromhell
 * @since 2.18.0
 */
public class VaultUnlockedServicesManager {

  private static VaultUnlockedServicesManager instance;
  //maintain this for insertion order.
  private final LinkedList<String> economyProviderNames = new LinkedList<>();
  private final LinkedList<String> permissionProviderNames = new LinkedList<>();
  private final LinkedList<String> chatProviderNames = new LinkedList<>();
  private final Map<String, Economy> economyProviders = new ConcurrentHashMap<>();
  private final Map<String, PermissionUnlocked> permissionProviders = new ConcurrentHashMap<>();
  private final Map<String, ChatUnlocked> chatProviders = new ConcurrentHashMap<>();

  private VaultUnlockedServicesManager() {
  }

  /**
   * Retrieves the singleton instance of the {@code VaultUnlockedServicesManager}.
   * If no instance exists, a new one is created.
   *
   * @return the singleton instance of {@code VaultUnlockedServicesManager}.
   */
  public static VaultUnlockedServicesManager get() {
    if (instance == null) {
      instance = new VaultUnlockedServicesManager();
    }
    return instance;
  }

  /**
   * Retrieves the list of names of available economy providers managed by this service.
   *
   * @return a {@link LinkedList} of strings containing the names of registered economy providers.
   */
  public LinkedList<String> economyProviderNames() {
    return economyProviderNames;
  }

  /**
   * Registers a new {@link Economy} provider to be managed by this service.
   * The provider is stored in a map using its name as the unique identifier.
   *
   * @param provider the {@link Economy} instance to be registered.
   *                 Must not be {@code null}.
   */
  public void economy(final @NotNull Economy provider) {
    economyProviderNames.add(provider.getName());
    economyProviders.put(provider.getName(), provider);
  }

  /**
   * Removes the specified economy provider from the internal lists of providers.
   *
   * @param provider the economy provider to be removed; must not be null.
   */
  public void unregister(final @NotNull Economy provider) {
    economyProviderNames.remove(provider.getName());
    economyProviders.remove(provider.getName());
  }

  /**
   * Retrieves the primary {@link Economy} instance currently managed by this service.
   *
   * @return an {@link Optional} containing the managed {@link Economy} instance,
   *         or an empty {@link Optional} if no economy implementation is available.
   */
  public Optional<Economy> economy() {
    synchronized (economyProviderNames) {

      for(final String economyProviderName : economyProviderNames) {

        if(economyProviders.containsKey(economyProviderName)) {

          final Economy economy = economyProviders.get(economyProviderName);
          if(economy.isEnabled()) {
            return Optional.of(economy);
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Retrieves the primary {@link Economy} instance currently managed by this service.
   * This method returns the managed economy instance or {@code null}
   * if no suitable economy implementation is available.
   *
   * @return the managed {@link Economy} instance, or {@code null} if no economy implementation is available.
   */
  public Economy economyObj() {

    return economy().orElse(null);
  }

  /**
   * Retrieves an {@link Optional} containing the {@link Economy} instance
   * associated with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding
   *                   {@link Economy} implementation.
   * @return an {@link Optional} containing the {@link Economy} instance if
   *         available, or an empty {@link Optional} if no economy implementation
   *         is associated with the given identifier.
   */
  public Optional<Economy> economy(final @NotNull String identifier) {
    return Optional.ofNullable(economyProviders.get(identifier));
  }

  /**
   * Retrieves the {@link Economy} instance associated with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding economy implementation.
   * @return the {@link Economy} instance corresponding to the identifier, or {@code null} if not available.
   */
  @Nullable
  public Economy economyObj(final @NotNull String identifier) {
    return economyProviders.get(identifier);
  }

  /**
   * Retrieves the list of names of available permission providers managed by this service.
   *
   * @return a {@link LinkedList} of strings containing the names of registered permission providers.
   */
  public LinkedList<String> permissionProviderNames() {
    return permissionProviderNames;
  }

  /**
   * Registers a new {@link PermissionUnlocked} provider to be managed by this service.
   * The provider is stored in a map using its name as the unique identifier.
   *
   * @param provider the {@link PermissionUnlocked} instance to be registered.
   *                 Must not be {@code null}.
   */
  public void permission(final @NotNull PermissionUnlocked provider) {
    permissionProviderNames.add(provider.getName());
    permissionProviders.put(provider.getName(), provider);
  }

  /**
   * Unregisters the specified permission provider by removing its associated
   * entries from the internal collections.
   *
   * @param provider the permission provider to be unregistered,
   *                 must not be null
   */
  public void unregister(final @NotNull PermissionUnlocked provider) {
    permissionProviderNames.remove(provider.getName());
    permissionProviders.remove(provider.getName());
  }

  /**
   * Retrieves the primary {@link PermissionUnlocked} instance currently managed by this service.
   * The method sequentially evaluates the available permission providers in the order
   * of their registration and returns the first enabled provider if available.
   *
   * @return an {@link Optional} containing the managed {@link PermissionUnlocked} instance,
   *         or an empty {@link Optional} if no enabled permission implementation is present.
   */
  public Optional<PermissionUnlocked> permission() {
    synchronized (permissionProviderNames) {

      for(final String permissionProviderName : permissionProviderNames) {

        if(permissionProviders.containsKey(permissionProviderName)) {

          final PermissionUnlocked permission = permissionProviders.get(permissionProviderName);
          if(permission.isEnabled()) {
            return Optional.of(permission);
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Retrieves the primary {@link PermissionUnlocked} instance currently managed by this service.
   * The method evaluates the available permission providers in the order of their registration
   * and returns the first enabled provider if available.
   *
   * @return the managed {@link PermissionUnlocked} instance, or {@code null} if no enabled
   *         permission implementation is available.
   */
  public PermissionUnlocked permissionObj() {

    return permission().orElse(null);
  }

  /**
   * Retrieves an {@link Optional} containing the {@link PermissionUnlocked} instance associated
   * with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding
   *                   {@link PermissionUnlocked} implementation. Must not be {@code null}.
   * @return an {@link Optional} containing the {@link PermissionUnlocked} instance if available,
   *         or an empty {@link Optional} if no permission implementation is associated with
   *         the given identifier.
   */
  public Optional<PermissionUnlocked> permission(final @NotNull String identifier) {
    return Optional.ofNullable(permissionProviders.get(identifier));
  }

  /**
   * Retrieves the {@link PermissionUnlocked} instance associated with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding
   *                   {@link PermissionUnlocked} implementation. Must not be {@code null}.
   * @return the {@link PermissionUnlocked} instance corresponding to the identifier,
   *         or {@code null} if no implementation is associated with the given identifier.
   */
  @Nullable
  public PermissionUnlocked permissionObj(final @NotNull String identifier) {
    return permissionProviders.get(identifier);
  }

  /**
   * Retrieves the list of names of available chat providers managed by this service.
   *
   * @return a {@link LinkedList} of strings containing the names of registered chat providers.
   */
  public LinkedList<String> chatProviderNames() {
    return chatProviderNames;
  }

  /**
   * Registers a new {@link ChatUnlocked} provider to be managed by this service.
   * The provider is stored in a map using its name as the unique identifier.
   *
   * @param provider the {@link ChatUnlocked} instance to be registered.
   *                 Must not be {@code null}.
   */
  public void chat(final @NotNull ChatUnlocked provider) {
    chatProviderNames.add(provider.getName());
    chatProviders.put(provider.getName(), provider);
  }

  /**
   * Unregisters a given chat provider by removing its name and reference
   * from the internal collections.
   *
   * @param provider The chat provider to unregister. Must not be null.
   */
  public void unregister(final @NotNull ChatUnlocked provider) {
    chatProviderNames.remove(provider.getName());
    chatProviders.remove(provider.getName());
  }

  /**
   * Retrieves the primary {@link ChatUnlocked} instance currently managed by this service.
   * The method evaluates the available chat providers in the order of their registration
   * and returns the first enabled provider if available.
   *
   * @return an {@link Optional} containing the managed {@link ChatUnlocked} instance,
   *         or an empty {@link Optional} if no enabled chat implementation is available.
   */
  public Optional<ChatUnlocked> chat() {
    synchronized (chatProviderNames) {

      for(final String chatProviderName : chatProviderNames) {

        if(chatProviders.containsKey(chatProviderName)) {

          final ChatUnlocked chat = chatProviders.get(chatProviderName);
          if(chat.isEnabled()) {
            return Optional.of(chat);
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Retrieves the primary {@link ChatUnlocked} instance currently managed by this service.
   *
   * @return the managed {@link ChatUnlocked} instance, or {@code null} if no chat implementation is available.
   */
  public ChatUnlocked chatObj() {

    return chat().orElse(null);
  }

  /**
   * Retrieves an {@link Optional} containing the {@link ChatUnlocked} instance
   * associated with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding
   *                   chat implementation. Must not be {@code null}.
   * @return an {@link Optional} containing the {@link ChatUnlocked} instance if
   *         available, or an empty {@link Optional} if no chat implementation
   *         is associated with the given identifier.
   */
  public Optional<ChatUnlocked> chat(final @NotNull String identifier) {
    return Optional.ofNullable(chatProviders.get(identifier));
  }

  /**
   * Retrieves the {@link ChatUnlocked} instance associated with the specified identifier.
   *
   * @param identifier the unique identifier used to retrieve the corresponding chat implementation.
   *                   Must not be {@code null}.
   * @return the {@link ChatUnlocked} instance corresponding to the identifier, or {@code null}
   *         if no chat implementation is associated with the given identifier.
   */
  @Nullable
  public ChatUnlocked chatObj(final @NotNull String identifier) {
    return chatProviders.get(identifier);
  }
}