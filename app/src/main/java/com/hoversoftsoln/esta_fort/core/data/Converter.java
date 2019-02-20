package com.hoversoftsoln.esta_fort.core.data;

import java.util.Map;

public interface Converter<T> {

    T maptoData(Map<String, Object> _datamap);

    Map<String, Object> datatoMap(T _data);
}
