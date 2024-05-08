package org.hlumak.converter.factory;

import org.hlumak.converter.strategy.ConverterStrategy;

public interface ConverterStrategyFactory {
    ConverterStrategy createConverter();
}
