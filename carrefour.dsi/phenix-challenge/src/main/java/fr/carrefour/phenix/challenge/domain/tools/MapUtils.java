package fr.carrefour.phenix.challenge.domain.tools;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MapUtils {

    /**
     * find the n topmost values inside a map, using a limit and a custom comparator
     *
     * @param map input map
     * @param n limit of top entries to be returned
     * @param comparator function used to compare map values
     * @param <K> type of the map key
     * @param <V> type of the map value
     * @return a new hash map containing the topmost n entries (highest ranking based on comparator)
     */
    public static <K, V> Map<K, V> getTopEntries(Map<K, V> map, int n, Comparator<? super V> comparator)
    {
        Comparator<? super Map.Entry<K, V>> comp =
                (Comparator<Map.Entry<K, V>>) (e0, e1) -> comparator.compare(e0.getValue(), e1.getValue());

        PriorityQueue<Map.Entry<K, V>> highest = new PriorityQueue<>(n, comp);

        for (Map.Entry<K, V> entry : map.entrySet())
        {
            highest.offer(entry);
            while (highest.size() > n)
            {
                highest.poll();
            }
        }

        Map<K, V> result = new HashMap<>();
        while (highest.size() > 0)
        {
            Map.Entry<K, V> entry = highest.poll();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
