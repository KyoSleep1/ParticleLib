package dev.sleep.particlelib.client.loading;

import dev.sleep.particlelib.Main;
import dev.sleep.particlelib.Reference;
import dev.sleep.particlelib.client.loading.object.ParticleScheme;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LoadingCache {

    @Getter
    private static Map<ResourceLocation, ParticleScheme> CachedSchemes = Collections.emptyMap();

    public static void loadResourcesAndCache() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new IdentifiableResourceReloadListener() {
                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation(Reference.MODID, "schemes");
                    }

                    @Override
                    public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager,
                                                          ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor,
                                                          Executor applyExecutor) {
                        return LoadingCache.reload(synchronizer, manager, prepareExecutor, applyExecutor);
                    }
                });
    }

    private static CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, Executor backgroundExecutor, Executor gameExecutor) {
        Map<ResourceLocation, ParticleScheme> schemes = new Object2ObjectOpenHashMap<>();
        return CompletableFuture.allOf(
                        loadSchemes(backgroundExecutor, resourceManager, schemes::put))
                .thenCompose(stage::wait).thenAcceptAsync(empty -> CachedSchemes = schemes, gameExecutor);
    }

    private static CompletableFuture<Void> loadSchemes(Executor backgroundExecutor, ResourceManager resourceManager, BiConsumer<ResourceLocation, ParticleScheme> elementConsumer) {
        return loadResources(backgroundExecutor, resourceManager, resource -> {
            ParticleScheme scheme = SchemeFileLoader.loadScheme(resource, resourceManager);

            if (!scheme.getFormatVersion().equals("1.10.0")) {
                Main.warnCritical("Unsupported scheme json version. Supported versions: 1.10.0", null);
            }

            return scheme;
        }, elementConsumer);
    }

    private static <T> CompletableFuture<Void> loadResources(Executor executor, ResourceManager resourceManager, Function<ResourceLocation, T> loader, BiConsumer<ResourceLocation, T> map) {
        return CompletableFuture.supplyAsync(
                        () -> resourceManager.listResources("schemes", fileName -> fileName.toString().endsWith(".json")), executor)
                .thenApplyAsync(resources -> {
                    Map<ResourceLocation, CompletableFuture<T>> tasks = new Object2ObjectOpenHashMap<>();

                    for (ResourceLocation resource : resources.keySet()) {
                        tasks.put(resource, CompletableFuture.supplyAsync(() -> loader.apply(resource), executor));
                    }

                    return tasks;
                }, executor)
                .thenAcceptAsync(tasks -> {
                    for (Map.Entry<ResourceLocation, CompletableFuture<T>> entry : tasks.entrySet()) {
                        map.accept(entry.getKey(), entry.getValue().join());
                    }
                }, executor);
    }
}
