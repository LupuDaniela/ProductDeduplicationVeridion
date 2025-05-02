package org.example.deduplicator;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.example.model.Product;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Merger {
    private static final JaroWinklerSimilarity JW = new JaroWinklerSimilarity();
    private static final double TITLE_THRESHOLD = 0.85;

    public static Product mergeCategoryMajority(List<Product> items) {
        if (items == null || items.isEmpty()) return null;
        Product merged = new Product();

        merged.setUnspsc(items.get(0).getUnspsc());

        BiFunction<List<Product>, java.util.function.Function<Product, String>, List<String>> pickMajority =
                (list, extractor) -> {
                    Map<String, Long> freq = list.stream()
                            .map(extractor)
                            .filter(Objects::nonNull)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
                    long maxCount = freq.values().stream().mapToLong(Long::longValue).max().orElse(0);
                    return freq.entrySet().stream()
                            .filter(e -> e.getValue() == maxCount)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
                };


        List<String> topSummaries = pickMajority.apply(items, Product::getProductSummary);
        String summary = String.join(" \\ ", topSummaries);
        merged.setProductSummary(summary.isEmpty() ? null : summary);


        List<String> rootDomains = items.stream()
                .map(Product::getRootDomain)
                .filter(Objects::nonNull).filter(s->!s.isEmpty())
                .distinct().collect(Collectors.toList());
        String rootsJoined = String.join(" \\ ", rootDomains);
        merged.setRootDomain(rootsJoined.isEmpty()?null:rootsJoined);


        List<String> pageUrls = items.stream()
                .map(Product::getPageUrl)
                .filter(Objects::nonNull).filter(s->!s.isEmpty())
                .distinct().collect(Collectors.toList());
        String pagesJoined = String.join(" \\ ", pageUrls);
        merged.setPageUrl(pagesJoined.isEmpty()?null:pagesJoined);


        List<String> allTitles = items.stream()
                .map(Product::getProductTitle)
                .filter(Objects::nonNull).map(String::trim)
                .filter(s->!s.isEmpty())
                .distinct()
                .collect(Collectors.toList());


        List<List<String>> titleClusters = new ArrayList<>();
        for (String t : allTitles) {
            boolean placed = false;
            for (List<String> cl : titleClusters) {
                if (JW.apply(t, cl.get(0)) >= TITLE_THRESHOLD) {
                    cl.add(t);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                titleClusters.add(new ArrayList<>(List.of(t)));
            }
        }


        List<String> repTitles = titleClusters.stream()
                .map(cl -> cl.stream()
                        .max(Comparator.comparingInt(String::length))
                        .orElse(cl.get(0)))
                .collect(Collectors.toList());


        String mergedTitle = String.join(" \\ ", repTitles);
        merged.setProductTitle(mergedTitle.isEmpty()?null:mergedTitle);


        List<String> topNames = pickMajority.apply(items, Product::getProductName);
        String name = String.join(" \\ ", topNames);
        merged.setProductName(name.isEmpty() ? null : name);


        List<String> ids = items.stream()
                .flatMap(p -> {
                    List<String> list = p.getProductIdentifier();
                    return list==null ? Stream.<String>empty() : list.stream();
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        merged.setProductIdentifier(ids);


        List<String> topBrands = pickMajority.apply(items, Product::getBrand);
        String brand = String.join(" \\ ", topBrands);
        merged.setBrand(brand.isEmpty() ? null : brand);

        return merged;
    }
}
