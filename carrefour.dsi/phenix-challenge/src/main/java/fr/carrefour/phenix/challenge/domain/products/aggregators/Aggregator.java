package fr.carrefour.phenix.challenge.domain.products.aggregators;

/**
 * functor that merges two values of the same type (returns a new instance)
 *
 * @param <T> type of the values that are merges/returned
 */
public interface Aggregator<T> {
    T apply(T v1, T v2);
}
