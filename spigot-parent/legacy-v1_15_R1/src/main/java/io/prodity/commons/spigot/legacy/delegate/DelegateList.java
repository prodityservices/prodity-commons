package io.prodity.commons.spigot.legacy.delegate;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface DelegateList<T> extends List<T> {

    List<T> getDelegateList();

    @Override
    default int size() {
        return this.getDelegateList().size();
    }

    @Override
    default boolean isEmpty() {
        return this.getDelegateList().isEmpty();
    }

    @Override
    default boolean contains(Object o) {
        return this.getDelegateList().contains(o);
    }

    @Override
    default Iterator<T> iterator() {
        return this.getDelegateList().iterator();
    }

    @Override
    default Object[] toArray() {
        return this.getDelegateList().toArray();
    }

    @Override
    default <T1> T1[] toArray(T1[] a) {
        return this.getDelegateList().toArray(a);
    }

    @Override
    default boolean add(T t) {
        return this.getDelegateList().add(t);
    }

    @Override
    default boolean remove(Object o) {
        return this.getDelegateList().remove(o);
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        return this.getDelegateList().containsAll(c);
    }

    @Override
    default boolean addAll(Collection<? extends T> c) {
        return this.getDelegateList().addAll(c);
    }

    @Override
    default boolean addAll(int index, Collection<? extends T> c) {
        return this.getDelegateList().addAll(index, c);
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        return this.getDelegateList().removeAll(c);
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        return this.getDelegateList().retainAll(c);
    }

    @Override
    default void replaceAll(UnaryOperator<T> operator) {
        this.getDelegateList().replaceAll(operator);
    }

    @Override
    default void sort(Comparator<? super T> c) {
        this.getDelegateList().sort(c);
    }

    @Override
    default void clear() {
        this.getDelegateList().clear();
    }

    @Override
    default T get(int index) {
        return this.getDelegateList().get(index);
    }

    @Override
    default T set(int index, T element) {
        return this.getDelegateList().set(index, element);
    }

    @Override
    default void add(int index, T element) {
        this.getDelegateList().add(index, element);
    }

    @Override
    default T remove(int index) {
        return this.getDelegateList().remove(index);
    }

    @Override
    default int indexOf(Object o) {
        return this.getDelegateList().indexOf(o);
    }

    @Override
    default int lastIndexOf(Object o) {
        return this.getDelegateList().lastIndexOf(o);
    }

    @Override
    default ListIterator<T> listIterator() {
        return this.getDelegateList().listIterator();
    }

    @Override
    default ListIterator<T> listIterator(int index) {
        return this.getDelegateList().listIterator(index);
    }

    @Override
    default List<T> subList(int fromIndex, int toIndex) {
        return this.getDelegateList().subList(fromIndex, toIndex);
    }

    @Override
    default Spliterator<T> spliterator() {
        return this.getDelegateList().spliterator();
    }

    @Override
    default boolean removeIf(Predicate<? super T> filter) {
        return this.getDelegateList().removeIf(filter);
    }

    @Override
    default Stream<T> stream() {
        return this.getDelegateList().stream();
    }

    @Override
    default Stream<T> parallelStream() {
        return this.getDelegateList().parallelStream();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        this.getDelegateList().forEach(action);
    }

}