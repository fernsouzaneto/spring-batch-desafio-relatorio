package com.hyperflame.desafio;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Object, Object> {
    @Override
    public Object process(Object item) throws Exception {
        return item;
    }
}
