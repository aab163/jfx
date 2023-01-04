/*
 * Copyright (c) 2011, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalContentBinding;
import com.sun.javafx.property.PropertyHelper;
import javafx.beans.binding.SetExpression;
import javafx.collections.ObservableSet;

import java.util.Objects;
import java.util.Set;

/**
 * Superclass for all readonly properties wrapping an {@link javafx.collections.ObservableSet}.
 *
 * @see javafx.collections.ObservableSet
 * @see javafx.beans.value.ObservableSetValue
 * @see javafx.beans.binding.SetExpression
 * @see ReadOnlyProperty
 *
 * @param <E> the type of the {@code Set} elements
 * @since JavaFX 2.1
 */
public abstract class ReadOnlySetProperty<E> extends SetExpression<E> implements ReadOnlyProperty<ObservableSet<E>>  {

    /**
     * The constructor of {@code ReadOnlySetProperty}.
     */
    public ReadOnlySetProperty() {
    }

    /**
     * Creates a bidirectional content binding between the {@link javafx.collections.ObservableSet} that is
     * wrapped in this {@code ReadOnlySetProperty} and another {@code ObservableSet}.
     * <p>
     * A bidirectional content binding ensures that the content of the two sets is the same.
     * If the content of one of the sets changes, the content of the other set will be updated automatically.
     *
     * @param other the {@code ObservableSet} this property should be bound to
     * @throws NullPointerException if {@code other} is {@code null}
     * @throws IllegalArgumentException if {@code other} is the set wrapped in this {@code ReadOnlySetProperty}
     */
    public void bindContentBidirectional(ObservableSet<E> other) {
        BidirectionalContentBinding.bind(this, other);
    }

    /**
     * Removes the bidirectional content binding that was established with
     * {@link #bindContentBidirectional(ObservableSet)}.
     * <p>
     * Bidirectional content bindings can be removed by calling this method on either of the two endpoints:
     * <pre>{@code
     * property1.bindContentBidirectional(property2);
     * property2.unbindContentBidirectional(property1);
     * }</pre>
     * The content of the wrapped set will remain unchanged.
     * If this property is not bidirectionally content-bound, calling this method has no effect.
     *
     * @param other the {@code ObservableSet} to which the bidirectional content binding should be removed
     * @throws NullPointerException if {@code other} is {@code null}
     * @throws IllegalArgumentException if {@code other} is the set wrapped in this {@code ReadOnlySetProperty}
     * @since 18
     */
    public void unbindContentBidirectional(ObservableSet<E> other) {
        BidirectionalContentBinding.unbind(this, other);
    }

    /**
     * Removes the bidirectional content binding that was established with
     * {@link #bindContentBidirectional(ObservableSet)}.
     * <p>
     * Bidirectional content bindings can be removed by calling this method on either of the two endpoints:
     * <pre>{@code
     * property1.bindContentBidirectional(property2);
     * property2.unbindContentBidirectional(property1);
     * }</pre>
     * The content of the wrapped set will remain unchanged.
     * If this property is not bidirectionally content-bound, calling this method has no effect.
     *
     * @param other the {@code Object} to which the bidirectional content binding should be removed
     * @throws NullPointerException if {@code other} is {@code null}
     * @throws IllegalArgumentException if {@code other} is the set wrapped in this {@code ReadOnlySetProperty}
     * @deprecated use {@link #unbindContentBidirectional(ObservableSet)} instead
     */
    @Deprecated(since = "18", forRemoval = true)
    public void unbindContentBidirectional(Object other) {
        Objects.requireNonNull(other);
        if (other instanceof ObservableSet<?>) {
            BidirectionalContentBinding.unbind(this, (ObservableSet<E>)other);
        }
    }

    /**
     * Creates a content binding between the {@link javafx.collections.ObservableSet} that is wrapped
     * in this {@code ReadOnlySetProperty} (the <em>bound set</em>) and another {@code ObservableSet}
     * (the <em>source set</em>).
     * <p>
     * A content binding ensures that the content of the bound set is the same as that of the source set.
     * If the content of the source set changes, the content of the bound set will be updated automatically.
     * In contrast, a regular binding will replace the bound set instance with the source set instance,
     * which means that only a single set instance exists for both properties.
     * <p>
     * Once a content binding is established, the bound set becomes effectively read-only: any attempt to
     * change the content of the bound set by calling a mutating method of {@link ObservableSet} will cause
     * the content binding to fail. In this case, the content binding is removed because the bound set and
     * the source set may be out-of-sync.
     *
     * @param source the source {@code ObservableSet} this property should be bound to
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws IllegalArgumentException if {@code source} is the set wrapped in this {@code ReadOnlySetProperty}
     */
    public void bindContent(ObservableSet<E> source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the content binding that was established with {@link #bindContent(ObservableSet)}.
     * <p>
     * The content of the wrapped set will remain unchanged.
     * If this property is not content-bound, calling this method has no effect.
     *
     * @since 18
     */
    public void unbindContent() {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the content binding that was established with {@link #bindContent(ObservableSet)}.
     * <p>
     * The content of the wrapped set will remain unchanged.
     * If this property is not content-bound, calling this method has no effect.
     *
     * @param source the content binding source
     * @throws NullPointerException if {@code source} is {@code null}
     * @throws IllegalArgumentException if {@code source} is the set wrapped in this {@code ReadOnlySetProperty}
     * @deprecated use {@link #unbindContent()}
     */
    @Deprecated(since = "18", forRemoval = true)
    public void unbindContent(Object source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether this property is bound by a unidirectional content binding that was
     * established by calling {@link #bindContent(ObservableSet)}.
     * <p>
     * Note that this method does not account for bidirectional content bindings that were
     * established by calling {@link #bindContentBidirectional(ObservableSet)}.
     *
     * @return whether this property is unidirectionally content-bound
     * @since 18
     */
    public boolean isContentBound() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set<?> otherSet) || otherSet.size() != size()) {
            return false;
        }

        try {
            return containsAll(otherSet);
        } catch (ClassCastException | NullPointerException unused)   {
            return false;
        }
    }

    /**
     * Returns a hash code for this {@code ReadOnlySetProperty} object.
     * @return a hash code for this {@code ReadOnlySetProperty} object.
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            if (e != null)
                h += e.hashCode();
        }
        return h;
    }

    /**
     * Returns a string representation of this {@code ReadOnlySetProperty} object.
     * @return a string representation of this {@code ReadOnlySetProperty} object.
     */
    @Override
    public String toString() {
        return PropertyHelper.toString(this, ReadOnlySetProperty.class);
    }

}
