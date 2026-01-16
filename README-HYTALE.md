# VaultUnlocked (Hytale)
**Version:** 2.18.0  
**Platform:** Hytale  
**Category:** API / Library / Developer Tools

---

## What is VaultUnlocked?

**VaultUnlocked** is the next-generation Vault-style API that provides a **unified abstraction layer** for **economy**, **permissions**, and **chat** systems.

This Hytale release delivers the **same great VaultUnlocked API** already used on **Paper, Spigot, and Bukkit** — now available for **Hytale** so developers can support multiple platforms **without rewriting their integrations**.

Same API.  
Same concepts.  
Same integrations.  
Now on Hytale.

---

## For Server Owners

### Why use VaultUnlocked?

VaultUnlocked allows plugins to work together without directly depending on a specific economy, permissions, or chat plugin.

Instead of hard dependencies, plugins interact through VaultUnlocked, giving you:

- Freedom to switch economy or permissions plugins
- Fewer compatibility issues
- Cleaner plugin stacks
- A standardized and future-proof API

VaultUnlocked **does not replace** your economy, permissions, or chat plugin — it simply **connects them**.

---

## For Developers

### One API, Multiple Platforms

VaultUnlocked was built with **cross-platform compatibility** as a core design goal.

If your plugin already supports VaultUnlocked on:
- Paper
- Spigot
- Bukkit

Then it will feel **instantly familiar on Hytale**.

No platform-specific rewrites.  
No duplicated abstractions.  
Minimal or zero code changes required.

---

## API Changes in 2.18.0 (Important)

### Service Registration Model Update

Starting with **VaultUnlocked 2.18.0**, services are now registered and accessed through:

### VaultUnlockedServicesManager

This replaces the traditional *registered service provider* approach and aligns better with Hytale’s lifecycle and architecture.

Benefits include:
- Explicit service ownership
- Predictable lifecycle management
- Platform-agnostic behavior
- Improved clarity and debugging

---

## Registering Services

Economy, permissions, and chat implementations are now registered directly with the service manager.

```java
VaultUnlockedServicesManager services = VaultUnlockedServicesManager.get();

services.economy(myEconomyImplementation);
services.permission(myPermissionImplementation);
services.chat(myChatImplementation);
````

Each service is explicitly registered, making behavior consistent across platforms.

---

## Accessing Services

Plugins can retrieve active services at any time using the same manager.

```java
VaultUnlockedServicesManager services = VaultUnlockedServicesManager.get();

Economy economy = services.economyObj();
PermissionUnlocked permission = services.permissionObj();
ChatUnlocked chat = services.chatObj();
```

If a service is unavailable, VaultUnlocked safely handles the absence so plugins can gracefully degrade.

---

## Why This Change?

Hytale introduces a different plugin lifecycle and service model than Bukkit-based platforms.

VaultUnlockedServicesManager:

* Removes reliance on platform-specific registries
* Keeps the API explicit and predictable
* Enables future expansion beyond economy, permissions, and chat

Despite the internal change, the **developer-facing API remains familiar and Vault-like**.

---

## What VaultUnlocked Is (and Is Not)

### VaultUnlocked **IS**

* A compatibility layer
* A service abstraction API
* A developer-focused interoperability tool

### VaultUnlocked **IS NOT**

* An economy plugin
* A permissions plugin
* A chat formatting plugin

You choose the implementations — VaultUnlocked connects them.

---

## Version Compatibility

* **VaultUnlocked (Hytale):** 2.18.0
* API parity with Bukkit / Spigot / Paper VaultUnlocked
* Designed for forward compatibility with future Hytale APIs

---

## Who Should Use VaultUnlocked?

* Plugin developers targeting Hytale
* Server owners running multiple gameplay plugins
* Developers maintaining cross-platform plugins
* Anyone who wants a clean, standardized service API

---

## Summary

VaultUnlocked for Hytale brings **consistency, stability, and familiarity** to a new ecosystem — without forcing developers to relearn or rewrite.
- Same API.
- Same philosophy.
- Now for Hytale.