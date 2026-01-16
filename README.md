# VaultUnlocked - Abstraction Library for Hytale - [![Build Status](https://ci.codemc.io/job/creatorfromhell/job/VaultUnlocked/badge/icon)](https://ci.codemc.io/job/creatorfromhell/job/VaultUnlocked/)

## For Developers:
Please see the [VaultUnlockedAPI](https://www.github.com/TheNewEconomy/VaultUnlockedAPI) page for
information on developing with VaultUnlocked's API. In the past, you would use the same
artifact as servers installed, but the API has now been split from the main
project and is under a different artifact name. Please make sure you accommodate
this change in your build process.

## Installing
Installing VaultUnlocked is as simple as copying the provided "VaultUnlocked.jar" to your
"<hytale-install-dir>/plugins" directory, and the rest is automatic! If you
wish to perform configuration changes, this can be done via a configuration
file but should not be necessary in most cases. See the "Advanced
Configuration" section for more information.


## Why VaultUnlocked?
I have no preference regarding which library is best suited for
your plugin development efforts. I believe a central suite (or "Vault")
of solutions is a more effective approach than focusing on a single
category of plugins. This is the concept behind VaultUnlocked.

### Key Features You'll Appreciate

* **No Source Code Integration Needed**
  VaultUnlocked operates as a standalone plugin, so you only need to obtain an instance of it. This prevents conflicts with multiple plugins using the same namespaces. Simply include VaultUnlocked.jar in your download zip file for seamless integration!
* **Extensive Plugin Support**
  VaultUnlocked provides an abstraction layer not just for Economic plugins but for Permission plugins as well, ensuring broad compatibility.
* **Freedom of Choice**
  One of the best aspects of Bukkit is the freedom to choose what to use. More options benefit developers, so here’s to embracing choice!

### Enhanced Features of VaultUnlocked

* **Multi-Currency Support**
* **More Friendly PR Acceptance**

Let me know if you need any further modifications!

## Permissions
* vault.admin
  - Determines if a player should have access to the administrative tools.
* vault.admin.info
  - Determines if a player should have access to the "/vault-info" command.
* vault.admin.convert
  - Determines if a player should have access to the "/vault-convert" command.
* vault.update
  - Determines if a player should receive the update notices.

## 📘 VaultUnlocked PlaceholderAPI Placeholders (UUID-Based)

Note: These only work if your economy plugin supports the modern API of VaultUnlocked.

### 💰 Personal Balance (Player Required)

| Placeholder                                                                 | Description                                                             |
|-----------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `%vaultunlocked_balance%`                                                  | Player’s balance in the default world                                  |
| `%vaultunlocked_balance_<world>%`                                          | Player’s balance in a specific world                                   |
| `%vaultunlocked_balance_currency_<currency>%`                              | Balance in a specific currency (default world)                         |
| `%vaultunlocked_balance_currency_<currency>_world_<world>%`                | Balance in a specific currency and world                               |
| `%vaultunlocked_balanceformatted%`                                         | Formatted balance (e.g., `$1,234.56`)                                  |
| `%vaultunlocked_balanceformatted_currency_<currency>%`                     | Formatted balance in a specific currency                               |
| `%vaultunlocked_balanceformatted_currency_<currency>_world_<world>%`       | Formatted balance in a specific currency and world                     |

### 🏦 Shared Account Balance (Account UUID Required)

| Placeholder                                                                                         | Description                                                             |
|-----------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `%vaultunlocked_account_<uuid>%`                                                                    | Shared account balance (default currency/world)                        |
| `%vaultunlocked_account_<uuid>_status%`                                                             | Whether the player has access to the shared account                    |
| `%vaultunlocked_account_<uuid>_currency_<currency>%`                                                | Shared account balance in a specific currency                          |
| `%vaultunlocked_account_<uuid>_currency_<currency>_world_<world>%`                                  | Shared account balance in a specific currency and world                |
| `%vaultunlocked_account_<uuid>_currency_<currency>_formatted%`                                      | Formatted balance in a specific currency                               |
| `%vaultunlocked_account_<uuid>_currency_<currency>_world_<world>_formatted%`                        | Formatted balance in a specific currency and world                     |


### ✅ Capability Checks (Player & Account UUID Required)

Please note: The response is yes or no, for true or false respectively.

| Placeholder                              | Description                                                    |
|------------------------------------------|----------------------------------------------------------------|
| `%vaultunlocked_can_deposit_<uuid>%`     | Whether the player can deposit into the account with UUID.     |
| `%vaultunlocked_can_withdraw_<uuid>%`    | Whether the player can withdraw from the account with UUID.    |
| `%vaultunlocked_can_balance_<uuid>%`     | Whether the player can view the balance of the account.        |
| `%vaultunlocked_can_transfer_<uuid>%`    | Whether the player can transfer ownership of the account.      |
| `%vaultunlocked_can_invite_<uuid>%`      | Whether the player can invite members to the account.          |
| `%vaultunlocked_can_remove_<uuid>%`      | Whether the player can remove members from the account.        |
| `%vaultunlocked_can_modify_<uuid>%`      | Whether the player can modify member permissions.              |
| `%vaultunlocked_can_delete_<uuid>%`      | Whether the player can delete the account.                     |

### 📂 Account List / Info (Player Required)

| Placeholder                         | Description                                             |
|-------------------------------------|---------------------------------------------------------|
| `%vaultunlocked_accounts%`         | List of accessible shared account UUIDs                |
| `%vaultunlocked_accounts_count%`   | Count of accessible shared accounts                    |

### 💱 Currency Metadata (No Player Required)

| Placeholder                           | Description                             |
|---------------------------------------|-----------------------------------------|
| `%vaultunlocked_currency%`            | Singular form of the default currency   |
| `%vaultunlocked_currencyplural%`      | Plural form of the default currency     |

### 🔡 Encoding Notes for Currency Names

If a currency contains spaces or symbols, encode them:

| Character | Use `%` Encoding |
|-----------|------------------|
| Space     | `%20`            |
| `$`       | `%24`            |
| `+`       | `%2B`            |
| `&`       | `%26`            |
| `/`       | `%2F`            |
| `=`       | `%3D`            |

## Support
- Issues: https://github.com/TheNewEconomy/VaultUnlocked-Hytale/issues

## License
Copyright (C) 2025 Daniel "creatorfromhell" Vidmar

VaultUnlocked is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

VaultUnlocked is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
with VaultUnlocked. If not, see <http://www.gnu.org/licenses/>.

## Building
VaultUnlocked comes with all libraries needed to build from the current branch and
also comes with an Apache Ant build file (build.xml) and a Maven build file
(pom.xml). Maven is currently the preferred build method.


## Dependencies
Because VaultUnlocked provides a bridge to other plugins, their binaries will be
required to build from. To ease this, they have been included in the lib
folder and will be updated from time to time. For plugin developers, it
is not necessary to use these libraries when implementing VaultUnlocked. You will
only need to compile against VaultUnlocked.


## Supported Plugins
VaultUnlocked provides abstraction for the following categories and plugins. If
you have your own plugin that you believe should be supported, you'll need
to add your own connector within your plugin as VaultUnlocked no longer maintains
new plugin connectors.
