package dev.sleep.particlelib.core.impl.common.registry;

import dev.sleep.particlelib.Reference;
import dev.sleep.particlelib.core.impl.common.item.EmitterWandItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRegistry {

    public static final EmitterWandItem EMITTER_WAND = registerItem("emitter_wand", new EmitterWandItem());

    public static final CreativeModeTab ITEMS_GROUP = FabricItemGroup
            .builder(new ResourceLocation(Reference.MODID, "items_group"))
            .icon(() -> new ItemStack(ItemRegistry.EMITTER_WAND))
            .displayItems((enabledFeatures, entries, operatorEnabled) -> entries.accept(ItemRegistry.EMITTER_WAND)).build();

    public static <I extends Item> I registerItem(String name, I item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Reference.MODID, name), item);
    }
}
