package dev.sleep.particlelib.impl.common.item;

import dev.sleep.particlelib.impl.common.network.NetworkManager;
import dev.sleep.particlelib.impl.common.network.packet.PacketEmitterPlaced;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class EmitterWandItem extends Item {

    public EmitterWandItem() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.particlelib.emitter_wand.desc"));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(player.getCommandSenderWorld().isClientSide()) {
            return InteractionResult.FAIL;
        }

        NetworkManager.sendToTrackingEntityAndSelf(new PacketEmitterPlaced(), interactionTarget);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(!(context.getLevel() instanceof ServerLevel serverLevel)){
            return InteractionResult.FAIL;
        }

        NetworkManager.sendToEntitiesTrackingChunk(new PacketEmitterPlaced(), serverLevel, context.getClickedPos());
        return InteractionResult.SUCCESS;
    }
}