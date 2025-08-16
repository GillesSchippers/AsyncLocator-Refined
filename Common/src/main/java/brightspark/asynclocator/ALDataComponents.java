package brightspark.asynclocator;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;

import java.util.function.UnaryOperator;

public class ALDataComponents {
    // Simple component acting as a boolean flag using Unit (no value needed)
    public static final DataComponentType<Unit> LOCATING = register("locating", builder -> builder
        .persistent(Codec.unit(Unit.INSTANCE))
        .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        DataComponentType.Builder<T> builder = DataComponentType.builder();
        builder = operator.apply(builder);
        return Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ALConstants.MOD_ID + ":" + name,
            builder.build()
        );
    }

    public static void init() {
        ALConstants.logInfo("Registered data components");
    }
} 