package io.prodity.commons.config.load;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.Config;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConfigLoadData<T> {

    private final Class<T> annotatedClass;
    private final Config annotation;
    private String internalPath;
    private File file;

    private Map<?, ?> rawValues;
    private T configObject;
    private List<Field> applicableFields;
    private List<Method> applicableMethods;

    public ConfigLoadData(@Nonnull Class<T> annotatedClass, @Nonnull Config annotation) {
        Preconditions.checkNotNull(annotatedClass, "annotatedClass");
        Preconditions.checkNotNull(annotation, "annotation");
        this.annotatedClass = annotatedClass;
        this.annotation = annotation;
    }

    @Nonnull
    public Class<T> getAnnotatedClass() {
        return this.annotatedClass;
    }

    @Nonnull
    public Config getAnnotation() {
        return this.annotation;
    }

    @Nullable
    public String getInternalPath() {
        return this.internalPath;
    }

    public void setInternalPath(@Nonnull String internalPath) {
        Preconditions.checkNotNull(internalPath, "internalPath");
        this.internalPath = internalPath;
    }

    @Nullable
    public File getFile() {
        return this.file;
    }

    public void setFile(@Nonnull File file) {
        Preconditions.checkNotNull(file, "file");
        this.file = file;
    }

    @Nullable
    public Map<?, ?> getRawValues() {
        return this.rawValues;
    }

    public void setRawValues(@Nonnull Map<?, ?> rawValues) {
        Preconditions.checkNotNull(rawValues, "rawValues");
        this.rawValues = rawValues;
    }

    @Nullable
    public T getConfigObject() {
        return this.configObject;
    }

    public void setConfigObject(@Nonnull T configObject) {
        Preconditions.checkNotNull(configObject, "configObject");
        this.configObject = configObject;
    }

    @Nullable
    public List<Field> getApplicableFields() {
        return this.applicableFields;
    }

    public void setApplicableFields(@Nonnull List<Field> applicableFields) {
        Preconditions.checkNotNull(applicableFields, "applicableFields");
        this.applicableFields = applicableFields;
    }

    @Nullable
    public List<Method> getApplicableMethods() {
        return this.applicableMethods;
    }

    public void setApplicableMethods(@Nonnull List<Method> applicableMethods) {
        Preconditions.checkNotNull(applicableMethods, "applicableMethods");
        this.applicableMethods = applicableMethods;
    }

}