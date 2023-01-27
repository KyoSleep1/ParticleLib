package dev.sleep.particlecore.client.component.expiration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.joml.Vector3f;
import software.bernie.geckolib.core.molang.MolangException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ComponentExpireBlocks extends AbstractComponent {

    public final List<Block> blocks = new ArrayList<>();
    private final BlockPos POSITION = new BlockPos(0, 0, 0);

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (element.isJsonArray()) {
            for (JsonElement value : element.getAsJsonArray()) {
                ResourceLocation location = new ResourceLocation(value.getAsString());
                if (!BuiltInRegistries.BLOCK.containsKey(location)) {
                    continue;
                }

                Block block = BuiltInRegistries.BLOCK.get(location);
                this.blocks.add(block);
            }
        }

        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray();

        for (Block block : this.blocks) {
            Optional<ResourceKey<Block>> optionalResource = BuiltInRegistries.BLOCK.getResourceKey(block);
            if(optionalResource.isEmpty()){
                continue;
            }

            array.add(optionalResource.get().location().toString());
        }

        return array;
    }

    public Block getBlock(AbstractParticleEmitter emitter, EnhancedParticle particle) {
        if (emitter.world == null) {
            return Blocks.AIR;
        }

        Vector3f position = particle.getGlobalPosition(emitter);
        BlockPos.MutableBlockPos mutableBlockPos = this.POSITION.mutable();

        mutableBlockPos.set(position.x, position.y, position.z);
        return emitter.world.getBlockState(POSITION).getBlock();
    }
}
