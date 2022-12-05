package dev.sleep.particlecore.client.component.appearance;

import com.eliotlash.mclib.utils.Interpolations;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public class ComponentAppearanceBillboard extends AbstractComponent {

    public MolangValue sizeW = MolangParser.ZERO, sizeH = MolangParser.ZERO, uvX = MolangParser.ZERO,
            uvY = MolangParser.ZERO, uvW = MolangParser.ZERO, uvH = MolangParser.ZERO, maxFrame = MolangParser.ZERO;
    public CameraFacing facing = CameraFacing.LOOKAT_XYZ;

    public int textureWidth = 128;
    public int textureHeight = 128;

    public float stepX;
    public float stepY;
    public float fps;

    public boolean flipbook = false;
    public boolean stretchFPS = false;
    public boolean loop = false;

    protected float w;
    protected float h;

    protected float u1;
    protected float v1;

    protected float u2;
    protected float v2;

    private final Matrix4f TRANSFORMATION = new Matrix4f();
    private final Matrix4f ROTATION = new Matrix4f();
    private final Vector4f[] VERTICES = new Vector4f[]{
            new Vector4f(0, 0, 0, 1),
            new Vector4f(0, 0, 0, 1),
            new Vector4f(0, 0, 0, 1),
            new Vector4f(0, 0, 0, 1)
    };
    private final Vector3f VECTOR = new Vector3f();

    @Override
    public AbstractComponent fromJson(JsonElement element) throws MolangException {
        if (!element.isJsonObject()) {
            return this;
        }

        JsonObject jsonObjectElement = element.getAsJsonObject();
        if (jsonObjectElement.has("size") && jsonObjectElement.get("size").isJsonArray()) {
            JsonArray size = jsonObjectElement.getAsJsonArray("size");

            if (size.size() >= 2) {
                this.sizeW = MolangParser.parseJson(size.get(0));
                this.sizeH = MolangParser.parseJson(size.get(1));
            }
        }

        if (jsonObjectElement.has("facing_camera_mode")) {
            this.facing = CameraFacing.fromString(jsonObjectElement.get("facing_camera_mode").getAsString());
        }

        if (jsonObjectElement.has("uv") && jsonObjectElement.get("uv").isJsonObject()) {
            this.parseUv(jsonObjectElement.get("uv").getAsJsonObject());
        }

        return this;
    }

    private void parseUv(JsonObject object) throws MolangException {
        if (object.has("texture_width")) {
            this.textureWidth = object.get("texture_width").getAsInt();
        }

        if (object.has("texture_height")) {
            this.textureHeight = object.get("texture_height").getAsInt();
        }

        if (object.has("uv") && object.get("uv").isJsonArray()) {
            JsonArray uv = object.getAsJsonArray("uv");

            if (uv.size() >= 2) {
                this.uvX = MolangParser.parseJson(uv.get(0));
                this.uvY = MolangParser.parseJson(uv.get(1));
            }
        }

        if (object.has("uv_size") && object.get("uv_size").isJsonArray()) {
            JsonArray uv = object.getAsJsonArray("uv_size");

            if (uv.size() >= 2) {
                this.uvW = MolangParser.parseJson(uv.get(0));
                this.uvH = MolangParser.parseJson(uv.get(1));
            }
        }

        if (object.has("flipbook") && object.get("flipbook").isJsonObject()) {
            this.flipbook = true;
            this.parseFlipbook(object.get("flipbook").getAsJsonObject());
        }
    }

    private void parseFlipbook(JsonObject flipbook) throws MolangException {
        if (flipbook.has("base_UV") && flipbook.get("base_UV").isJsonArray()) {
            JsonArray uv = flipbook.getAsJsonArray("base_UV");

            if (uv.size() >= 2) {
                this.uvX = MolangParser.parseJson(uv.get(0));
                this.uvY = MolangParser.parseJson(uv.get(1));
            }
        }

        if (flipbook.has("size_UV") && flipbook.get("size_UV").isJsonArray()) {
            JsonArray uv = flipbook.getAsJsonArray("size_UV");

            if (uv.size() >= 2) {
                this.uvW = MolangParser.parseJson(uv.get(0));
                this.uvH = MolangParser.parseJson(uv.get(1));
            }
        }

        if (flipbook.has("step_UV") && flipbook.get("step_UV").isJsonArray()) {
            JsonArray uv = flipbook.getAsJsonArray("step_UV");

            if (uv.size() >= 2) {
                this.stepX = uv.get(0).getAsFloat();
                this.stepY = uv.get(1).getAsFloat();
            }
        }

        if (flipbook.has("frames_per_second")) {
            this.fps = flipbook.get("frames_per_second").getAsFloat();
        }

        if (flipbook.has("max_frame")) {
            this.maxFrame = MolangParser.parseJson(flipbook.get("max_frame"));
        }

        if (flipbook.has("stretch_to_lifetime")) {
            this.stretchFPS = flipbook.get("stretch_to_lifetime").getAsBoolean();
        }

        if (flipbook.has("loop")) {
            this.loop = flipbook.get("loop").getAsBoolean();
        }
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray size = new JsonArray();
        JsonObject uv = new JsonObject();

        size.add(this.sizeW.toJson());
        size.add(this.sizeH.toJson());

        /* Adding "uv" properties */
        uv.addProperty("texture_width", this.textureWidth);
        uv.addProperty("texture_height", this.textureHeight);

        if (!this.flipbook && !this.uvX.isZero() || !this.uvY.isZero()) {
            JsonArray uvs = new JsonArray();
            uvs.add(this.uvX.toJson());
            uvs.add(this.uvY.toJson());

            uv.add("uv", uvs);
        }

        if (!this.flipbook && !this.uvW.isZero() || !this.uvH.isZero()) {
            JsonArray uvs = new JsonArray();
            uvs.add(this.uvW.toJson());
            uvs.add(this.uvH.toJson());

            uv.add("uv_size", uvs);
        }

        if (this.flipbook) {
            JsonObject flipbook = new JsonObject();

            if (!this.uvX.isZero() || !this.uvY.isZero()) {
                JsonArray base = new JsonArray();
                base.add(this.uvX.toJson());
                base.add(this.uvY.toJson());

                flipbook.add("base_UV", base);
            }

            if (!this.uvW.isZero() || !this.uvH.isZero()) {
                JsonArray uvSize = new JsonArray();
                uvSize.add(this.uvW.toJson());
                uvSize.add(this.uvH.toJson());

                flipbook.add("size_UV", uvSize);
            }

            if (this.stepX != 0 || this.stepY != 0) {
                JsonArray step = new JsonArray();
                step.add(this.stepX);
                step.add(this.stepY);

                flipbook.add("step_UV", step);
            }

            if (this.fps != 0) {
                flipbook.addProperty("frames_per_second", this.fps);
            }

            if (!this.maxFrame.isZero()) {
                flipbook.add("max_frame", this.maxFrame.toJson());
            }

            if (this.stretchFPS) {
                flipbook.addProperty("stretch_to_lifetime", true);
            }

            if (this.loop) {
                flipbook.addProperty("loop", true);
            }

            uv.add("flipbook", flipbook);
        }

        object.add("size", size);
        object.addProperty("facing_camera_mode", this.facing.id);
        object.add("uv", uv);
        return object;
    }

    @Override
    public void render(AbstractParticleEmitter emitter, EnhancedParticle particle, PoseStack poseStack, BufferBuilder builder, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        this.calculateUVs(particle, partialTicks);

        double px = Interpolations.lerp(particle.prevPosition.x, particle.position.x, partialTicks);
        double py = Interpolations.lerp(particle.prevPosition.y, particle.position.y, partialTicks);
        double pz = Interpolations.lerp(particle.prevPosition.z, particle.position.z, partialTicks);
        float angle = Interpolations.lerp(particle.prevRotation, particle.rotation, partialTicks);

        if (particle.useRelativePosition && particle.useRelativeRotation) {
            this.VECTOR.set((float) px, (float) py, (float) pz);
            emitter.rotation.transform(this.VECTOR);

            px = this.VECTOR.x;
            py = this.VECTOR.y;
            pz = this.VECTOR.z;

            px += emitter.lastGlobal.x;
            py += emitter.lastGlobal.y;
            pz += emitter.lastGlobal.z;
        }

        boolean lookAt = this.facing == CameraFacing.LOOKAT_XYZ || this.facing == CameraFacing.LOOKAT_Y;
        /**if (emitter.perspective == 2) {
         this.w = -this.w;
         } else if (emitter.perspective == 100 && !lookAt) {
         entityYaw = 180 - entityYaw;

         this.w = -this.w;
         this.h = -this.h;
         } **/

        if (lookAt) {
            /** double dX = entityX - px;
             double dY = entityY - py;
             double dZ = entityZ - pz;
             double horizontalDistance = Mth.sqrt((float) (dX * dX + dZ * dZ)); **/

            //entityYaw = 180 - (float) (Mth.atan2(dZ, dX) * (180D / Math.PI)) - 90.0F;
            //entityPitch = (float) (-(Mth.atan2(dY, horizontalDistance) * (180D / Math.PI))) + 180;
        }

        this.VERTICES[0].set(-this.w / 2, -this.h / 2, 0, 1);
        this.VERTICES[1].set(this.w / 2, -this.h / 2, 0, 1);
        this.VERTICES[2].set(this.w / 2, this.h / 2, 0, 1);
        this.VERTICES[3].set(-this.w / 2, this.h / 2, 0, 1);
        this.TRANSFORMATION.identity();

        if (this.facing == CameraFacing.ROTATE_XYZ || this.facing == CameraFacing.LOOKAT_XYZ) {
            // this.rotation.rotY(entityYaw / 180 * (float) Math.PI);
            this.TRANSFORMATION.mul(this.ROTATION);
            //this.rotation.rotX(entityPitch / 180 * (float) Math.PI);
            this.TRANSFORMATION.mul(this.ROTATION);
        } else if (this.facing == CameraFacing.ROTATE_Y || this.facing == CameraFacing.LOOKAT_Y) {
            //this.rotation.rotY(entityYaw / 180 * (float) Math.PI);
            this.TRANSFORMATION.mul(this.ROTATION);
        }

        //this.rotation.rotZ(angle / 180 * (float) Math.PI);
        this.TRANSFORMATION.mul(this.ROTATION);
        this.TRANSFORMATION.setTranslation(new Vector3f((float) px, (float) py, (float) pz));

        for (Vector4f vertex : this.VERTICES) {
            this.TRANSFORMATION.transform(vertex);
        }

        int lightColor = this.getLightColor(emitter, particle, partialTicks);

        float u1 = this.u1 / (float) this.textureWidth;
        float u2 = this.u2 / (float) this.textureWidth;
        float v1 = this.v1 / (float) this.textureHeight;
        float v2 = this.v2 / (float) this.textureHeight;

        builder.vertex(this.VERTICES[0].x, this.VERTICES[0].y, this.VERTICES[0].z).uv(u1, v1).color(particle.color.x, particle.color.y, particle.color.z, particle.color.w).uv2(lightColor).endVertex();
        builder.vertex(this.VERTICES[1].x, this.VERTICES[1].y, this.VERTICES[1].z).uv(u2, v1).color(particle.color.x, particle.color.y, particle.color.z, particle.color.w).uv2(lightColor).endVertex();
        builder.vertex(this.VERTICES[2].x, this.VERTICES[2].y, this.VERTICES[2].z).uv(u2, v2).color(particle.color.x, particle.color.y, particle.color.z, particle.color.w).uv2(lightColor).endVertex();
        builder.vertex(this.VERTICES[3].x, this.VERTICES[3].y, this.VERTICES[3].z).uv(u1, v2).color(particle.color.x, particle.color.y, particle.color.z, particle.color.w).uv2(lightColor).endVertex();
    }

    public void calculateUVs(EnhancedParticle particle, float partialTicks) {
        this.w = (float) this.sizeW.get() * 2.25F;
        this.h = (float) this.sizeH.get() * 2.25F;

        float u = (float) this.uvX.get();
        float v = (float) this.uvY.get();
        float w = (float) this.uvW.get();
        float h = (float) this.uvH.get();

        if (this.flipbook) {
            int index = (int) (particle.getAge(partialTicks) * this.fps);
            int max = (int) this.maxFrame.get();

            if (this.stretchFPS) {
                float lifetime = particle.lifetime <= 0 ? 0 : (particle.age + partialTicks) / particle.lifetime;

                index = (int) (lifetime * max);
            }

            if (this.loop && max != 0) {
                index = index % max;
            }

            if (index > max) {
                index = max;
            }

            u += this.stepX * index;
            v += this.stepY * index;
        }

        this.u1 = u;
        this.v1 = v;
        this.u2 = u + w;
        this.v2 = v + h;
    }

    public int getLightColor(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTick) {
        if (emitter.world == null) {
            return 0;
        }

        BlockPos blockPos = new BlockPos(particle.position.x, particle.position.y, particle.position.z);
        if (emitter.world.getChunkAt(blockPos) != null) {
            return LevelRenderer.getLightColor(emitter.world, blockPos);
        }

        return 0;
    }
}
