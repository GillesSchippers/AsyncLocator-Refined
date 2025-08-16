package brightspark.asynclocator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class AsyncMapTracker {
    private static AsyncMapTracker INSTANCE;
    private final Map<UUID, MapLocationOperation> trackedOperations = new ConcurrentHashMap<>();

    public static AsyncMapTracker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AsyncMapTracker();
        }
        return INSTANCE;
    }

    public void addLocationOperation(UUID operationId, MapLocationOperation operation) {
        trackedOperations.put(operationId, operation);
    }

    public void completeLocationOperation(UUID operationId, net.minecraft.core.BlockPos foundPosition) {
        MapLocationOperation operation = trackedOperations.remove(operationId);
        if (operation != null && operation.completionHandler != null) {
            operation.completionHandler.accept(foundPosition);
        }
    }

    public MapLocationOperation getOperation(UUID operationId) {
        return trackedOperations.get(operationId);
    }

    public static class MapLocationOperation {
        private final ResourceKey<Level> dimension;
        private final int scale;
        private final Holder<MapDecorationType> decorationType;
        private final Consumer<BlockPos> completionHandler;

        public MapLocationOperation(ResourceKey<Level> dimension, int scale, 
                                  Holder<MapDecorationType> decorationType, 
                                  Consumer<BlockPos> completionHandler) {
            this.dimension = dimension;
            this.scale = scale;
            this.decorationType = decorationType;
            this.completionHandler = completionHandler;
        }

        public MapLocationOperation(ResourceKey<Level> dimension, int scale, 
                                  Holder<MapDecorationType> decorationType) {
            this(dimension, scale, decorationType, null);
        }

        public ResourceKey<Level> getDimension() {
            return dimension;
        }

        public int getScale() {
            return scale;
        }

        public Holder<MapDecorationType> getDecorationType() {
            return decorationType;
        }

        public Consumer<BlockPos> getCompletionHandler() {
            return completionHandler;
        }
    }
}
