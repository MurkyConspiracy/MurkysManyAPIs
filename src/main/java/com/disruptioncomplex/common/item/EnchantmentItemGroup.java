package com.disruptioncomplex.common.item;

import com.disruptioncomplex.MurkysManyAPIs;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// Add these imports
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.*;

/**
 * Manages a shared item group for enchanted books across different mods.
 * This class handles the registration and display of enchanted books from authorized mods
 * in a unified item group interface.
 */
public class EnchantmentItemGroup {

    /**
     * Constructs a new EnchantmentItemGroup instance.
     * This constructor is private to prevent instantiation as this class only provides static methods
     * for managing the shared enchantment item group.
     */
    private EnchantmentItemGroup() {
    }

    /**
     * Stores registered enchantments from different mods.
     * Key is the mod ID, value is a list of enchantment entries from that mod.
     */
    private static final Map<String, List<EnchantmentEntry>> REGISTERED_ENCHANTMENTS = new HashMap<>();

    // Add this to your SharedItemGroups class
    /**
     * Set of mod IDs that are authorized to register enchantments in the shared group.
     * Only mods listed here can add their enchantments to the shared item group.
     */
    private static final Set<String> AUTHORIZED_MOD_IDS = new HashSet<>(List.of(
            "murkys-many-fish"
            // Add more authorized mod IDs as needed
    ));

    /**
     * The shared item group that contains enchanted books from all registered and authorized mods.
     * This group is registered with the game's item group registry using the mod's ID.
     * <p>
     * The group uses an enchanted book as its icon and displays a translatable name.
     * All registered enchantments are added to this group through the {@link #addAllEnchantedBooks} method.
     * <p>
     * Only mods that are listed in {@link #AUTHORIZED_MOD_IDS} can register their enchantments
     * to appear in this shared group.
     */
    @SuppressWarnings("unused")
    public static final ItemGroup SHARED_ENCHANTMENTS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MurkysManyAPIs.MOD_ID, "shared_enchantments"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
                    .displayName(Text.translatable("itemgroup.murkys-common-lib.shared_enchantments"))
                    .entries(EnchantmentItemGroup::addAllEnchantedBooks).build());


    /**
     * Adds all registered enchanted books to the shared item group.
     *
     * @param displayContext The display context provided by the game
     * @param entries        The entries collection to add items to
     */
    private static void addAllEnchantedBooks(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        var enchantmentRegistry = displayContext.lookup().getOrThrow(RegistryKeys.ENCHANTMENT);

        // Loop through all mods that registered enchantments
        for (List<EnchantmentEntry> enchantmentEntries : REGISTERED_ENCHANTMENTS.values()) {
            for (EnchantmentEntry entry : enchantmentEntries) {
                // Check if the enchantment exists in the registry using getOptional instead of contains
                if (enchantmentRegistry.getOptional(entry.enchantmentKey).isPresent()) {
                    for (int level = 1; level <= entry.maxLevel; level++) {
                        entries.add(createEnchantedBook(enchantmentRegistry, entry.enchantmentKey, level));
                    }
                }
            }
        }
    }

    /**
     * Creates an enchanted book ItemStack with the specified enchantment and level.
     *
     * @param enchantmentRegistry The registry wrapper containing enchantment data
     * @param enchantmentKey      The registry key of the enchantment to apply
     * @param level               The level of the enchantment
     * @return An ItemStack containing the enchanted book
     */
    private static ItemStack createEnchantedBook(net.minecraft.registry.RegistryWrapper<Enchantment> enchantmentRegistry,
                                                 RegistryKey<Enchantment> enchantmentKey, int level) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        // Get the enchantment entry from the registry wrapper
        RegistryEntry<Enchantment> enchantment = enchantmentRegistry.getOrThrow(enchantmentKey);

        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        builder.add(enchantment, level);
        book.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());

        return book;
    }

    /**
     * Register enchantments from a mod to be included in the shared enchantment group
     * @param modId The mod ID registering enchantments
     * @param enchantments List of enchantment entries to register
     * @return true if registration succeeded, false if not authorized
     */
    @SuppressWarnings("unused")
    public static boolean registerEnchantments(String modId, List<EnchantmentEntry> enchantments) {
        // Check if mod is in the authorized list
        if (!AUTHORIZED_MOD_IDS.contains(modId)) {
            // Get the mod container for the calling mod
            Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        
            if (modContainer.isEmpty()) {
                MurkysManyAPIs.LOGGER.warn("Unknown mod attempted to register enchantments: " + modId);
                return false;
            }
        
            MurkysManyAPIs.LOGGER.warn("Unauthorized mod attempted to register enchantments: " + modId);
            return false;
        }
    
        REGISTERED_ENCHANTMENTS.put(modId, enchantments);
        MurkysManyAPIs.LOGGER.info("Registered " + enchantments.size() + " enchantments from " + modId);
        return true;
    }

    /**
     * Initializes and registers all shared item groups.
     * Called during mod initialization.
     */
    public static void registerItemGroups() {
        MurkysManyAPIs.LOGGER.info("Registering Shared Item Groups");
    }


    /**
     * Represents an enchantment entry in the shared enchantment system.
     * This class holds information about an enchantment and its maximum level
     * that can be displayed in the shared enchantment item group.
     */
    public static class EnchantmentEntry {
        /**
         * The registry key that uniquely identifies this enchantment
         */
        private final RegistryKey<Enchantment> enchantmentKey;

        /**
         * The maximum level allowed for this enchantment
         */
        private final int maxLevel;

        /**
         * Creates a new enchantment entry.
         *
         * @param enchantmentKey The registry key of the enchantment
         * @param maxLevel       The maximum level this enchantment can reach
         */
        public EnchantmentEntry(RegistryKey<Enchantment> enchantmentKey, int maxLevel) {
            this.enchantmentKey = enchantmentKey;
            this.maxLevel = maxLevel;
        }
    }
}