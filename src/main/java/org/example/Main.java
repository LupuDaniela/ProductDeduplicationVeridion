package org.example;


import com.fasterxml.jackson.databind.*;
import org.example.deduplicator.Merger;
import org.example.model.Product;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            String input  = args.length > 0
                    ? args[0]
                    : "src/main/resources/veridion-product-deduplication-challenge.json";
            String output = args.length > 1
                    ? args[1]
                    : "output-deduped.json";

            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            MappingIterator<Product> it = mapper
                    .readerFor(Product.class)
                    .readValues(new File(input));
            List<Product> products = it.readAll();
            System.out.println("► Loaded " + products.size() + " products.");


            Map<String, List<Product>> byUnspsc = products.stream()
                    .collect(Collectors.groupingBy(
                            p -> Optional.ofNullable(p.getUnspsc()).orElse("unknown")
                    ));


            List<Product> merged = byUnspsc.values().stream()
                    .map(Merger::mergeCategoryMajority)
                    .collect(Collectors.toList());
            System.out.println("► Reduced to " + merged.size() + " products (one per UNSPSC).");


            File outFile = new File(output);
            if (outFile.exists() && !outFile.delete()) {
                System.err.println("Could not delete existing output: " + outFile.getAbsolutePath());
            }
            mapper.writeValue(outFile, merged);
            System.out.println("► Wrote " + merged.size() + " products to " + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
