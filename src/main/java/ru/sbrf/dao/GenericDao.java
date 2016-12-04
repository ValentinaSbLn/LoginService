package ru.sbrf.dao;

import java.io.Serializable;

/**
 * Created by Valentina on 04.12.2016.
 */
public interface GenericDao<T, PK extends Serializable> {
    /**
     * Сохранить объект newInstance в базе данных
     */
    PK create(T newInstance);

    /**
     * Извлечь объект, предварительно сохраненный в базе данных, используя
     * указанный id в качестве первичного ключа
     */
    PK read(T newInstance);

}
