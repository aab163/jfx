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

package javafx.scene.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.css.Styleable;
import javafx.geometry.HPos;
import javafx.util.Callback;

/**
 * HBox lays out its children in a single horizontal row.
 * If the hbox has a border and/or padding set, then the contents will be laid
 * out within those insets.
 * <p>
 * HBox example:
 * <pre>{@code
 *     HBox hbox = new HBox(8); // spacing = 8
 *     hbox.getChildren().addAll(new Label("Name:), new TextBox());
 * }</pre>
 *
 * HBox will resize children (if resizable) to their preferred widths and uses its
 * fillHeight property to determine whether to resize their heights to
 * fill its own height or keep their heights to their preferred (fillHeight defaults to true).
 * The alignment of the content is controlled by the alignment property,
 * which defaults to Pos.TOP_LEFT.
 * <p>
 * If an hbox is resized larger than its preferred width, by default it will keep
 * children to their preferred widths, leaving the extra space unused.  If an
 * application wishes to have one or more children be allocated that extra space
 * it may optionally set an hgrow constraint on the child.  See "Optional Layout
 * Constraints" for details.
 * <p>
 * HBox lays out each managed child regardless of the child's
 * visible property value; unmanaged children are ignored.</p>
 *
 * <h2>Resizable Range</h2>
 *
 * <p>
 * An hbox's parent will resize the hbox within the hbox's resizable range
 * during layout.   By default the hbox computes this range based on its content
 * as outlined in the table below.
 * </p>
 * <table border="1">
 * <caption>HBox Resize Table</caption>
 * <tr><td></td><th scope="col">width</th><th scope="col">height</th></tr>
 * <tr><th scope="row">minimum</th>
 * <td>left/right insets plus the sum of each child's min width plus spacing between each child.</td>
 * <td>top/bottom insets plus the largest of the children's min heights.</td></tr>
 * <tr><th scope="row">preferred</th>
 * <td>left/right insets plus the sum of each child's pref width plus spacing between each child.</td>
 * <td>top/bottom insets plus the largest of the children's pref heights.</td></tr>
 * <tr><th scope="row">maximum</th>
 * <td>Double.MAX_VALUE</td><td>Double.MAX_VALUE</td></tr>
 * </table>
 * <p>
 * An hbox's unbounded maximum width and height are an indication to the parent that
 * it may be resized beyond its preferred size to fill whatever space is assigned
 * to it.
 * <p>
 * HBox provides properties for setting the size range directly.  These
 * properties default to the sentinel value USE_COMPUTED_SIZE, however the
 * application may set them to other values as needed:
 * <pre><code>
 *     <b>hbox.setPrefWidth(400);</b>
 * </code></pre>
 * Applications may restore the computed values by setting these properties back
 * to USE_COMPUTED_SIZE.
 * <p>
 * HBox does not clip its content by default, so it is possible that children's
 * bounds may extend outside its own bounds if a child's min size prevents it from
 * being fit within the hbox.</p>
 *
 * <h2>Optional Layout Constraints</h2>
 *
 * <p>
 * An application may set constraints on individual children to customize HBox's layout.
 * For each constraint, HBox provides a static method for setting it on the child.
 * </p>
 *
 * <table border="1">
 * <caption>HBox Constraint Table</caption>
 * <tr><th scope="col">Constraint</th><th scope="col">Type</th><th scope="col">Description</th></tr>
 * <tr><th scope="row">hgrow</th><td>javafx.scene.layout.Priority</td><td>The horizontal grow priority for the child.</td></tr>
 * <tr><th scope="row">margin</th><td>javafx.geometry.Insets</td><td>Margin space around the outside of the child.</td></tr>
 * </table>
 * <p>
 * For example, if an hbox needs the TextField to be allocated all extra space:
 * <pre><code>
 *     HBox hbox = new HBox();
 *     TextField field = new TextField();
 *     <b>HBox.setHgrow(field, Priority.ALWAYS);</b>
 *     hbox.getChildren().addAll(new Label("Search:"), field, new Button("Go"));
 * </code></pre>
 *
 * If more than one child has the same grow priority set, then the hbox will
 * allocate equal amounts of space to each.  HBox will only grow a child up to
 * its maximum width, so if the child has a max width other than Double.MAX_VALUE,
 * the application may need to override the max to allow it to grow.
 * For example:
 * <pre><code>
 *     HBox hbox = new HBox();
 *     Button button1 = new Button("Add");
 *     Button button2 = new Button("Remove");
 *     <b>HBox.setHgrow(button1, Priority.ALWAYS);
 *     HBox.setHgrow(button2, Priority.ALWAYS);
 *     button1.setMaxWidth(Double.MAX_VALUE);
 *     button2.setMaxWidth(Double.MAX_VALUE);</b>
 *     hbox.getChildren().addAll(button1, button2);
 * </code></pre>
 * @since JavaFX 2.0
 */
public class HBox extends Pane {

    private boolean biasDirty = true;
    private double minBaselineComplement = Double.NaN;
    private double prefBaselineComplement = Double.NaN;
    private Orientation bias;
    private double[][] tempArray;

    /* ******************************************************************
     *  BEGIN static methods
     ********************************************************************/
    private static final Priority[] GROW_PRIORITY = new Priority[] {Priority.ALWAYS, Priority.SOMETIMES};
    private static final String MARGIN_CONSTRAINT = "hbox-margin";
    private static final String HGROW_CONSTRAINT = "hbox-hgrow";

    /**
     * Sets the horizontal grow priority for the child when contained by an hbox.
     * If set, the hbox will use the priority value to allocate additional space if the
     * hbox is resized larger than its preferred width.
     * If multiple hbox children have the same horizontal grow priority, then the
     * extra space will be split evenly between them.
     * If no horizontal grow priority is set on a child, the hbox will never
     * allocate any additional horizontal space for that child.
     * <p>
     * Setting the value to {@code null} will remove the constraint.
     * @param child the child of an hbox
     * @param value the horizontal grow priority for the child
     */
    public static void setHgrow(Node child, Priority value) {
        setConstraint(child, HGROW_CONSTRAINT, value);
    }

    /**
     * Returns the child's hgrow constraint if set.
     * @param child the child node of an hbox
     * @return the horizontal grow priority for the child or null if no priority was set
     */
    public static Priority getHgrow(Node child) {
        return (Priority)getConstraint(child, HGROW_CONSTRAINT);
    }

    /**
     * Sets the margin for the child when contained by an hbox.
     * If set, the hbox will layout the child with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child mode of the hbox
     * @param value the margin of space around the child
     */
    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of an hbox
     * @return the margin for the child or null if no margin was set
     */
    public static Insets getMargin(Node child) {
        return (Insets)getConstraint(child, MARGIN_CONSTRAINT);
    }

    private static final Callback<Node, Insets> marginAccessor = n -> getMargin(n);

    /**
     * Removes all hbox constraints from the child node.
     * @param child the child node
     */
    public static void clearConstraints(Node child) {
        setHgrow(child, null);
        setMargin(child, null);
    }

    /* ******************************************************************
     *  END static methods
     ********************************************************************/

    /**
     * Creates an {@code HBox} layout with {@code spacing = 0}.
     */
    public HBox() {
        super();
    }

    /**
     * Creates an {@code HBox} layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     */
    public HBox(double spacing) {
        this();
        setSpacing(spacing);
    }

    /**
     * Creates an {@code HBox} layout with {@code spacing = 0}.
     * @param children the initial set of children for this pane
     * @since JavaFX 8.0
     */
    public HBox(Node... children) {
        super();
        getChildren().addAll(children);
    }

    /**
     * Creates an {@code HBox} layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     * @param children the initial set of children for this pane
     * @since JavaFX 8.0
     */
    public HBox(double spacing, Node... children) {
        this();
        setSpacing(spacing);
        getChildren().addAll(children);
    }

    /**
     * The amount of horizontal space between each child in the hbox.
     * @return the amount of horizontal space between each child in the hbox
     */
    public final DoubleProperty spacingProperty() {
        if (spacing == null) {
            spacing = new StyleableDoubleProperty() {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public CssMetaData getCssMetaData () {
                    return StyleableProperties.SPACING;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
                }

                @Override
                public String getName() {
                    return "spacing";
                }
            };
        }
        return spacing;
    }

    private DoubleProperty spacing;
    public final void setSpacing(double value) { spacingProperty().set(value); }
    public final double getSpacing() { return spacing == null ? 0 : spacing.get(); }

    /**
     * The overall alignment of children within the hbox's width and height.
     * @return the overall alignment of children within the hbox's width and
     * height
     */
    public final ObjectProperty<Pos> alignmentProperty() {
        if (alignment == null) {
            alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public CssMetaData<HBox, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }
            };
        }
        return alignment;
    }

    private ObjectProperty<Pos> alignment;
    public final void setAlignment(Pos value) { alignmentProperty().set(value); }
    public final Pos getAlignment() { return alignment == null ? Pos.TOP_LEFT : alignment.get(); }
    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    /**
     * Whether or not resizable children will be resized to fill the full height of the hbox
     * or be resized to their preferred height and aligned according to the <code>alignment</code>
     * vpos value.   Note that if the hbox vertical alignment is set to BASELINE, then this
     * property will be ignored and children will be resized to their preferred heights.
     * @return true if resizable children will be resized to fill the full
     * height of the hbox
     */
    public final BooleanProperty fillHeightProperty() {
        if (fillHeight == null) {
            fillHeight = new StyleableBooleanProperty(true) {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public CssMetaData<HBox, Boolean> getCssMetaData() {
                    return StyleableProperties.FILL_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return HBox.this;
                }

                @Override
                public String getName() {
                    return "fillHeight";
                }
            };
        }
        return fillHeight;
    }

    private BooleanProperty fillHeight;
    public final void setFillHeight(boolean value) { fillHeightProperty().set(value); }
    public final boolean isFillHeight() { return fillHeight == null ? true : fillHeight.get(); }

    private boolean shouldFillHeight() {
        return isFillHeight() && getAlignmentInternal().getVpos() != VPos.BASELINE;
    }

    /**
     *
     * @return null unless one of its children has a content bias.
     */
    @Override public Orientation getContentBias() {
        if (biasDirty) {
            bias = null;
            final List<Node> children = getManagedChildren();
            for (Node child : children) {
                Orientation contentBias = child.getContentBias();
                if (contentBias != null) {
                    bias = contentBias;
                    if (contentBias == Orientation.HORIZONTAL) {
                        break;
                    }
                }
            }
            biasDirty = false;
        }
        return bias;
    }

    @Override protected double computeMinWidth(double height) {
        Insets insets = getInsets();
        return snapSpaceX(insets.getLeft()) +
               computeContentWidth(getManagedChildren(), height, true) +
               snapSpaceX(insets.getRight());
    }

    @Override protected double computeMinHeight(double width) {
        Insets insets = getInsets();
        List<Node>managed = getManagedChildren();
        double contentHeight = 0;
        if (width != -1 && getContentBias() != null) {
            double[][] prefWidths = computeChildrenWidths(managed, -1, false);
            adjustChildrenWidths(managed, prefWidths, width, -1);
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else {
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        }
        return snapSpaceY(insets.getTop()) +
               contentHeight +
               snapSpaceY(insets.getBottom());
    }

    @Override protected double computePrefWidth(double height) {
        Insets insets = getInsets();
        return snapSpaceX(insets.getLeft()) +
               computeContentWidth(getManagedChildren(), height, false) +
               snapSpaceX(insets.getRight());
    }

    @Override protected double computePrefHeight(double width) {
        Insets insets = getInsets();
        List<Node>managed = getManagedChildren();
        double contentHeight = 0;
        if (width != -1 && getContentBias() != null) {
            double[][] prefWidths = computeChildrenWidths(managed, -1, false);
            adjustChildrenWidths(managed, prefWidths, width, -1);
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else {
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        }
        return snapSpaceY(insets.getTop()) +
               contentHeight +
               snapSpaceY(insets.getBottom());
    }

    /**
     * Calculates the preferred or minimum width for each child.
     * The returned widths are snapped to pixels in the horizontal direction.
     */
    private double[][] computeChildrenWidths(List<Node> managed, double height, boolean minimum) {
        // height could be -1
        double[][] temp = getTempArray(managed.size());
        final double insideHeight = height == -1? -1 : height -
                                     snapSpaceY(getInsets().getTop()) - snapSpaceY(getInsets().getBottom());
        final boolean shouldFillHeight = shouldFillHeight();
        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            Insets margin = getMargin(child);
            if (minimum) {
                temp[0][i] = computeChildMinAreaWidth(child, getMinBaselineComplement(), margin, insideHeight, shouldFillHeight);
            } else {
                temp[0][i] = computeChildPrefAreaWidth(child, getPrefBaselineComplement(), margin, insideHeight, shouldFillHeight);
            }
        }
        return temp;
    }

    /**
     * Adjusts the children widths (within their min-max limits) to fit the provided space.
     * This is necessary when the HBox is constrained to be larger or smaller than the combined preferred
     * widths of its children. In this case, we grow or shrink the children until they fit the HBox exactly.
     *
     * @return the pixel-snapped content width, which is the combined width
     *         of all children as well as the spacing between them
     */
    private double adjustChildrenWidths(List<Node> managed, double[][] childrenWidths, double width, double height) {
        Insets insets = getInsets();
        double top = snapSpaceY(insets.getTop());
        double bottom = snapSpaceY(insets.getBottom());

        double refHeight = shouldFillHeight() && height != -1 ? height - top - bottom : -1;
        double totalSpacing = (managed.size() - 1) * snapSpaceX(getSpacing());
        double contentWidth = snapSpaceX(sum(childrenWidths[0], managed.size())) + totalSpacing;
        double targetWidth = width - snapSpaceX(insets.getLeft()) - snapSpaceX(insets.getRight());

        if (contentWidth < targetWidth) {
            growChildrenWidths(managed, childrenWidths, targetWidth, refHeight);
        } else if (contentWidth > targetWidth) {
            shrinkChildrenWidths(managed, childrenWidths, targetWidth, refHeight);
        }

        return snapSpaceX(sum(childrenWidths[0], managed.size())) + totalSpacing;
    }

    /**
     * Shrinks all children widths to fit the target width.
     * In contrast to growing, shrinking does not require two phases of processing.
     */
    private void shrinkChildrenWidths(List<Node> managed, double[][] childrenWidths, double targetWidth, double height) {
        double[] usedWidths = childrenWidths[0];
        double[] minWidths = childrenWidths[1];
        boolean shouldFillHeight = shouldFillHeight();

        for (int i = 0, size = managed.size(); i < size; i++) {
            final Node child = managed.get(i);
            minWidths[i] = computeChildMinAreaWidth(
                child, getMinBaselineComplement(), getMargin(child), height, shouldFillHeight);
        }

        adjustWidthsWithinLimits(managed, usedWidths, minWidths, targetWidth, managed.size());
    }

    /**
     * Grows all children widths to fit the target width.
     * Growing is a two-phase process: first, only children with {@link Priority#ALWAYS} are eligible
     * for adjustment. If the first adjustment didn't suffice to fit the target width, children with
     * {@link Priority#SOMETIMES} are also eligible for adjustment.
     */
    private void growChildrenWidths(List<Node> managed, double[][] childrenWidths, double targetWidth, double height) {
        double[] currentWidths = childrenWidths[0];
        double[] maxWidths = childrenWidths[1];
        boolean shouldFillHeight = shouldFillHeight();

        for (Priority priority : GROW_PRIORITY) {
            int adjustingNumber = 0;

            for (int i = 0, size = managed.size(); i < size; i++) {
                final Node child = managed.get(i);

                // If the child is eligible to grow (as indicated by its horizontal grow priority),
                // we count it towards the 'adjustingNumber', which represents the number of children
                // that can grow in this phase.
                if (getHgrow(child) == priority) {
                    maxWidths[i] = computeChildMaxAreaWidth(
                        child, getMinBaselineComplement(), getMargin(child), height, shouldFillHeight);
                    ++adjustingNumber;
                } else {
                    maxWidths[i] = -1;
                }
            }

            // Adjust the children that are eligible in this phase and return early if the children
            // fit the target width (so no second phase is required).
            if (adjustWidthsWithinLimits(managed, currentWidths, maxWidths, targetWidth, adjustingNumber)) {
                return;
            }
        }
    }

    /**
     * Resizes the children widths to fit the target width, while taking into account the resize limits
     * for each child (their minimum and maximum width). This method will be called once when shrinking,
     * and may be called twice when growing.
     *
     * @param managed the managed children
     * @param currentWidths the current children widths
     * @param limitWidths the max or min widths for each child, depending on whether we are growing or shrinking;
     *                    a value of -1 means the child cannot be resized
     * @param targetWidth sum of child widths and spacing
     * @param adjustingNumber a number that indicates how many children can be resized
     * @return {@code true} if the child widths were successfully resized to fit the target width;
     *         {@code false} otherwise
     */
    private boolean adjustWidthsWithinLimits(
            List<Node> managed, double[] currentWidths, double[] limitWidths, double targetWidth, int adjustingNumber) {
        double totalSpacing = (managed.size() - 1) * snapSpaceX(getSpacing());

        // Current total width and current delta are two important numbers that we continuously
        // update as this method converges towards a solution.
        double currentTotalWidth = snapSpaceX(sum(currentWidths, managed.size())) + totalSpacing;
        double currentDelta = targetWidth - currentTotalWidth;

        // We repeatedly apply the following algorithm as long as we have space left to
        // distribute (currentDelta), as well as children that are eligible to grow or
        // shrink (adjustingNumber).
        while ((currentDelta > Double.MIN_VALUE || currentDelta < -Double.MIN_VALUE) && adjustingNumber > 0) {
            // The amount of space that, in the ideal case, we need to add to or subtract from
            // each eligible child in order to fit the children into the target width.
            double idealChange = snapPortionX(currentDelta / adjustingNumber);

            for (int i = managed.size() - 1; i >= 0; i--) {
                // If the child is not eligible for adjustment, skip it.
                if (limitWidths[i] == -1) {
                    continue;
                }

                // The actual amount of space that we add to or remove from the child is restricted
                // by its minimum and maximum width.
                double maxChange = limitWidths[i] - currentWidths[i];
                double actualChange = currentDelta > 0 ? Math.min(maxChange, idealChange) : Math.max(maxChange, idealChange);

                // Update the child width and snap the updated width to pixels in the horizontal direction.
                // Since snapping affects the total width, we need to recompute the current total width to
                // know how much space we have left to distribute.
                double oldWidth = currentWidths[i];
                currentWidths[i] = snapSizeX(oldWidth + actualChange);
                currentTotalWidth = snapSpaceX(currentTotalWidth + (currentWidths[i] - oldWidth));

                // Update the amount of space we still need to grow or shrink (currentDelta) for the
                // remaining children. If we overshoot our target, we're done because we can't resize
                // any further.
                double newDelta = targetWidth - currentTotalWidth;
                if (Math.abs(newDelta) > Math.abs(currentDelta)) {
                    currentWidths[i] = oldWidth;
                    return true;
                }

                currentDelta = newDelta;

                // If the actual change for the current child was restricted (as evidenced by its smaller
                // magnitude when compared to the ideal change), we've reached the limit for this child and
                // need to exclude it from further consideration.
                if (Math.abs(actualChange) < Math.abs(idealChange)) {
                    limitWidths[i] = -1;
                    adjustingNumber--;
                }
            }
        }

        return false;
    }

    /**
     * Calculates the preferred or minimum content width.
     * The content width is the total preferred or minimum width of all children,
     * including spacing between the children.
     */
    private double computeContentWidth(List<Node> managedChildren, double height, boolean minimum) {
        double[] childrenWidths = computeChildrenWidths(managedChildren, height, minimum)[0];
        return snapSpaceX(sum(childrenWidths, managedChildren.size())) +
               (managedChildren.size() - 1) * snapSpaceX(getSpacing());
    }

    private static double sum(double[] array, int size) {
        int i = 0;
        double res = 0;
        while (i != size) {
            res += array[i++];
        }
        return res;
    }

    @Override public void requestLayout() {
        biasDirty = true;
        bias = null;
        minBaselineComplement = Double.NaN;
        prefBaselineComplement = Double.NaN;
        baselineOffset = Double.NaN;
        super.requestLayout();
    }

    private double getMinBaselineComplement() {
        if (Double.isNaN(minBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE) {
                minBaselineComplement = getMinBaselineComplement(getManagedChildren());
            } else {
                minBaselineComplement = -1;
            }
        }
        return minBaselineComplement;
    }

    private double getPrefBaselineComplement() {
        if (Double.isNaN(prefBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE) {
                prefBaselineComplement = getPrefBaselineComplement(getManagedChildren());
            } else {
                prefBaselineComplement = -1;
            }
        }
        return prefBaselineComplement;
    }

    private double baselineOffset = Double.NaN;

    @Override
    public double getBaselineOffset() {
        List<Node> managed = getManagedChildren();
        if (managed.isEmpty()) {
            return BASELINE_OFFSET_SAME_AS_HEIGHT;
        }
        if (Double.isNaN(baselineOffset)) {
            VPos vpos = getAlignmentInternal().getVpos();
            if (vpos == VPos.BASELINE) {
                double max = 0;
                for (int i =0, sz = managed.size(); i < sz; ++i) {
                    final Node child = managed.get(i);
                    double offset = child.getBaselineOffset();
                    if (offset == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                        baselineOffset = BASELINE_OFFSET_SAME_AS_HEIGHT;
                        break;
                    } else {
                        Insets margin = getMargin(child);
                        double top = margin != null ? margin.getTop() : 0;
                        max = Math.max(max, top + child.getLayoutBounds().getMinY() + offset);
                    }
                }
                baselineOffset = max + snappedTopInset();
            } else {
                baselineOffset = BASELINE_OFFSET_SAME_AS_HEIGHT;
            }
        }
        return baselineOffset;
    }

    @Override protected void layoutChildren() {
        List<Node> managed = getManagedChildren();
        Insets insets = getInsets();
        Pos align = getAlignmentInternal();
        HPos alignHpos = align.getHpos();
        VPos alignVpos = align.getVpos();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpaceY(insets.getTop());
        double left = snapSpaceX(insets.getLeft());
        double bottom = snapSpaceY(insets.getBottom());
        double right = snapSpaceX(insets.getRight());
        double space = snapSpaceX(getSpacing());
        boolean shouldFillHeight = shouldFillHeight();

        final double[][] actualChildrenWidths = computeChildrenWidths(managed, height, false);
        double contentWidth = adjustChildrenWidths(managed, actualChildrenWidths, width, height);
        double contentHeight = height - top - bottom;

        double x = left + computeXOffset(width - left - right, contentWidth, align.getHpos());
        double y = top;
        double baselineOffset = -1;
        if (alignVpos == VPos.BASELINE) {
            double baselineComplement = getMinBaselineComplement();
            baselineOffset = getAreaBaselineOffset(managed, marginAccessor, i -> actualChildrenWidths[0][i],
                    contentHeight, shouldFillHeight, baselineComplement);
        }

        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            Insets margin = getMargin(child);
            layoutInArea(child, x, y, actualChildrenWidths[0][i], contentHeight,
                    baselineOffset, margin, true, shouldFillHeight,
                    alignHpos, alignVpos);
            x += actualChildrenWidths[0][i] + space;
        }
    }

    private double[][] getTempArray(int size) {
        if (tempArray == null) {
            tempArray = new double[2][size]; // First array for the result, second for temporary computations
        } else if (tempArray[0].length < size) {
            tempArray = new double[2][Math.max(tempArray.length * 3, size)];
        }
        return tempArray;

    }

    /* *************************************************************************
     *                                                                         *
     *                         Stylesheet Handling                             *
     *                                                                         *
     **************************************************************************/

     /*
      * Super-lazy instantiation pattern from Bill Pugh.
      */
     private static class StyleableProperties {

         private static final CssMetaData<HBox,Pos> ALIGNMENT =
             new CssMetaData<>("-fx-alignment",
                 new EnumConverter<>(Pos.class),
                 Pos.TOP_LEFT) {

            @Override
            public boolean isSettable(HBox node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(HBox node) {
                return (StyleableProperty<Pos>)node.alignmentProperty();
            }

         };

         private static final CssMetaData<HBox,Boolean> FILL_HEIGHT =
             new CssMetaData<>("-fx-fill-height",
                 BooleanConverter.getInstance(), Boolean.TRUE) {

            @Override
            public boolean isSettable(HBox node) {
                return node.fillHeight == null ||
                        !node.fillHeight.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(HBox node) {
                return (StyleableProperty<Boolean>)node.fillHeightProperty();
            }

         };

         private static final CssMetaData<HBox,Number> SPACING =
             new CssMetaData<>("-fx-spacing",
                 SizeConverter.getInstance(), 0.0){

            @Override
            public boolean isSettable(HBox node) {
                return node.spacing == null || !node.spacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(HBox node) {
                return (StyleableProperty<Number>)node.spacingProperty();
            }

         };

         private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
         static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(Pane.getClassCssMetaData());
            styleables.add(FILL_HEIGHT);
            styleables.add(ALIGNMENT);
            styleables.add(SPACING);
            STYLEABLES = Collections.unmodifiableList(styleables);
         }
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the
     * {@code CssMetaData} of its superclasses.
     * @return the {@code CssMetaData}
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     *
     * @since JavaFX 8.0
     */


    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}
