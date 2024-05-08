package org.hlumak.converter.factory;

import org.hlumak.converter.strategy.ArticleDOMConverterStrategy;
import org.hlumak.converter.strategy.ConverterStrategy;

public class ArticleDOMConverterFactory implements ConverterStrategyFactory{
    @Override
    public ConverterStrategy createConverter() {
        return new ArticleDOMConverterStrategy();
    }
}
