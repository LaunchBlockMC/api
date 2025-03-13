package gg.launchblock.template.utils;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public final class MutinyUtil {

    public static <T, R> Uni<List<R>> uniListFailFast(@NonNull final List<T> items, @NonNull final Function<? super T, Uni<R>> mapper) {
        if (ObjectUtils.isEmpty(items)) {
            return Uni.createFrom().item(Collections.emptyList());
        }

        final List<Uni<R>> listOfUnis = items.stream()
                .map(mapper)
                .toList();

        return Uni.join().all(listOfUnis).andFailFast();
    }

    public static <T, R, E> Uni<Map<T, E>> uniListFailFast(@NonNull final List<R> items, @NonNull final Function<? super R, T> keyMapper, @NonNull final Function<? super R, Uni<E>> valueMapper) {
        if (ObjectUtils.isEmpty(items)) {
            return Uni.createFrom().item(Map.of());
        }

        final List<Uni<Tuple2<T, E>>> listOfUnis = items.stream()
                .map(item -> valueMapper.apply(item)
                        .map(value -> Tuple2.of(keyMapper.apply(item), value)))
                .toList();

        return Uni.join().all(listOfUnis)
                .andFailFast()
                .map(list -> list.stream()
                        .collect(Collectors.toMap(Tuple2::getItem1, Tuple2::getItem2)));
    }
}
