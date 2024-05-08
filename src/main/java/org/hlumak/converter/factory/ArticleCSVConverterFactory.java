package org.hlumak.converter.factory;

import org.hlumak.converter.strategy.ArticleCSVConverterStrategy;
import org.hlumak.converter.strategy.ConverterStrategy;

public class ArticleCSVConverterFactory implements ConverterStrategyFactory{
    @Override
    public ConverterStrategy createConverter() {
        return new ArticleCSVConverterStrategy();
    }
}
