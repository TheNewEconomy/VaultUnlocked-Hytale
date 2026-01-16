package net.cfh.vault;
/*
 * IslandSurvival
 * Copyright (C) 2025 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.cfh.vault.command.VaultConvertCommand;
import net.cfh.vault.command.VaultInfoCommand;
import net.milkbowl.vault2.chat.ChatUnlocked;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.permission.PermissionUnlocked;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * VaultUnlocked
 *
 * @author creatorfromhell
 * @since 0.0.1.0
 */
public class VaultUnlocked extends JavaPlugin {

  private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
  private static VaultUnlocked instance;
  private VaultUnlockedServicesManager services;


  public VaultUnlocked(@Nonnull final JavaPluginInit init) {
    super(init);
    LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    instance = this;

  }

  @Override
  protected void setup() {
    LOGGER.atInfo().log("Setting up plugin " + this.getName());
    this.getCommandRegistry().registerCommand(new VaultConvertCommand());
    this.getCommandRegistry().registerCommand(new VaultInfoCommand());

    //TODO: Update checking.
  }

  /**
   * Provides access to the VaultUnlockedServicesManager instance.
   * This method is used for interacting with the service manager responsible for managing
   * various integrations such as economy, permissions, and chat.
   *
   * @return the VaultUnlockedServicesManager instance
   */
  public static VaultUnlockedServicesManager services() {
    return VaultUnlockedServicesManager.get();
  }

  /**
 * Retrieves the economy service if available.
 * This method provides access to an optional Economy instance managed by the VaultUnlockedServicesManager.
 *
 * @return an Optional containing the Economy instance if available, or an empty Optional if the economy service is not configured
 */
  public static Optional<Economy> economy() {
    return VaultUnlockedServicesManager.get().economy();
  }

  /**
   * Retrieves the currently configured economy object instance.
   * This method provides direct access to the Economy object managed by the VaultUnlockedServicesManager.
   *
   * @return the Economy instance, or null if no economy object is configured
   */
  @Nullable
  public static Economy economyObj() {
    return VaultUnlockedServicesManager.get().economyObj();
  }

  /**
   * Retrieves the permission service if available.
   * This method provides access to an optional PermissionUnlocked instance managed by the VaultUnlockedServicesManager.
   *
   * @return an Optional containing the PermissionUnlocked instance if available, or an empty Optional if the permission service is not configured
   */
  public static Optional<PermissionUnlocked> permission() {
    return VaultUnlockedServicesManager.get().permission();
  }

  /**
   * Retrieves the currently configured permission object instance.
   * This method provides direct access to the PermissionUnlocked object managed by the VaultUnlockedServicesManager.
   *
   * @return the PermissionUnlocked instance, or null if no permission object is configured
   */
  @Nullable
  public static PermissionUnlocked permissionObj() {
    return VaultUnlockedServicesManager.get().permissionObj();
  }

  /**
   * Retrieves the chat service if available.
   * This method provides access to an optional ChatUnlocked instance managed by the VaultUnlockedServicesManager.
   *
   * @return an Optional containing the ChatUnlocked instance if available, or an empty Optional if the chat service is not configured
   */
  public static Optional<ChatUnlocked> chat() {
    return VaultUnlockedServicesManager.get().chat();
  }

  /**
   * Retrieves the currently configured chat object instance.
   * This method provides direct access to the ChatUnlocked object, if available.
   *
   * @return the ChatUnlocked instance, or null if no chat object is configured
   */
  @Nullable
  public static ChatUnlocked chatObj() {
    return VaultUnlockedServicesManager.get().chatObj();
  }

  public static VaultUnlocked instance() {
    return instance;
  }
}