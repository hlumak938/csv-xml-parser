package org.hlumak.converter.factory;

import org.hlumak.converter.strategy.ArticleSAXConverterStrategy;
import org.hlumak.converter.strategy.ConverterStrategy;

public class ArticleSAXConverterFactory implements ConverterStrategyFactory{
    @Override
    public ConverterStrategy createConverter() {
        return new ArticleSAXConverterStrategy();
    }
}
