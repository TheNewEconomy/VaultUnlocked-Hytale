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

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.cfh.vault.VaultUnlocked;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * VaultConvertCommand
 *
 * @author creatorfromhell
 * @since 2.18.0
 */
public class VaultConvertCommand extends CommandBase {


  private final RequiredArg<String> fromArg;
  private final RequiredArg<String> targetArg;

  public VaultConvertCommand() {
    super("vault-convert", "from target - Converts from one Economy to another");

    this.fromArg = this.withRequiredArg("from", "The economy you're converting from.", ArgTypes.STRING);
    this.targetArg = this.withRequiredArg("target", "The economy you're converting to.", ArgTypes.STRING);
  }

  @Override
  protected void executeSync(@Nonnull final CommandContext ctx) {

    final Optional<Economy> fromEconomy = VaultUnlocked.services().economy(this.fromArg.get(ctx));
    final Optional<Economy> targetEconomy = VaultUnlocked.services().economy(this.targetArg.get(ctx));

    if(fromEconomy.isEmpty() || targetEconomy.isEmpty()) {
      ctx.sendMessage(Message.raw("One or both economies are not configured."));
      return;
    }

    for(final Map.Entry<UUID, String> entry : fromEconomy.get().getUUIDNameMap().entrySet()) {

      final BigDecimal balance = fromEconomy.get().balance("VaultUnlocked", entry.getKey());

      final EconomyResponse response = targetEconomy.get().set("VaultUnlocked", entry.getKey(), balance);
      if(!response.transactionSuccess()) {
        ctx.sendMessage(Message.raw("Failed to convert " + entry.getValue() + "'s balance. Reason: " + response.errorMessage));
      }
    }

    ctx.sendMessage(Message.raw("Conversion successful!"));
  }
}