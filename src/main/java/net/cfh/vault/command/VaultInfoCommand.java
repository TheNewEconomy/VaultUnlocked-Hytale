package net.cfh.vault.command;

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

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.Universe;
import net.cfh.vault.VaultUnlocked;
import net.cfh.vault.VaultUnlockedServicesManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault2.chat.ChatUnlocked;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.permission.PermissionUnlocked;

import javax.annotation.Nonnull;

/**
 * VaultInfoCommand
 *
 * @author creatorfromhell
 * @since 2.18.0
 */
public class VaultInfoCommand extends CommandBase {


  public VaultInfoCommand() {
    super("vault-info", "Displays information about Vault");
    this.setPermissionGroup(GameMode.Adventure);
  }

  @Override
  protected void executeSync(@Nonnull final CommandContext ctx) {

    final String name = VaultUnlocked.instance().getManifest().getName();

    final Economy economy = VaultUnlocked.economyObj();
    final PermissionUnlocked permission = VaultUnlocked.permissionObj();
    final ChatUnlocked chat = VaultUnlocked.chatObj();

    final String registeredEconomies = (VaultUnlockedServicesManager.get().economyProviderNames().isEmpty())?
                                       "" : String.join(", ", VaultUnlockedServicesManager.get().economyProviderNames());
    final String registeredPermissions = (VaultUnlockedServicesManager.get().permissionProviderNames().isEmpty())?
                                       "" : String.join(", ", VaultUnlockedServicesManager.get().permissionProviderNames());
    final String registeredChats = (VaultUnlockedServicesManager.get().chatProviderNames().isEmpty())?
                                       "" : String.join(", ", VaultUnlockedServicesManager.get().chatProviderNames());

    ctx.sendMessage(Message.raw(String.format("[%s] Vault v%s Information", name, VaultUnlocked.instance().getManifest().getVersion().toString())));
    ctx.sendMessage(Message.raw(String.format("[%s] Economy: %s%s", name, (economy == null)? "None" : economy.getName(), registeredEconomies)));
    ctx.sendMessage(Message.raw(String.format("[%s] Permission: %s%s", name, (permission == null)? "None" : permission.getName(), registeredPermissions)));
    ctx.sendMessage(Message.raw(String.format("[%s] Chat: %s%s", name, (chat == null)? "None" : chat.getName(), registeredChats)));
  }
}