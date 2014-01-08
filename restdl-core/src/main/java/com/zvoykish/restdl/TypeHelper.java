package com.zvoykish.restdl;

import com.zvoykish.restdl.objects.AnObject;
import com.zvoykish.restdl.objects.TypedObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 1/7/14
 * Time: 01:10
 */
public interface TypeHelper {
    TypedObject typeToAType(Type paramType);

    List<AnObject> fieldsToAnObjects(Field[] allFields);
}
