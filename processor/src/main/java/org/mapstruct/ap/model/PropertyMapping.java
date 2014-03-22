/**
 *  Copyright 2012-2014 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.model;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.ap.model.common.ModelElement;
import org.mapstruct.ap.model.common.Type;
import org.mapstruct.ap.model.TargetAssignment.AssignmentType;
/**
 * Represents the mapping between a source and target property, e.g. from
 * {@code String Source#foo} to {@code int Target#bar}. Name and type of source
 * and target property can differ. If they have different types, the mapping
 * must either refer to a mapping method or a conversion.
 *
 * @author Gunnar Morling
 */
public class PropertyMapping extends ModelElement {

    private final String sourceBeanName;
    private final String sourceName;
    private final String sourceAccessorName;
    private final Type sourceType;

    private final String targetName;
    private final String targetAccessorName;
    private final Type targetType;
    private final boolean isTargetAccessorSetter;
    private final String targetReadAccessorName;

    private final TargetAssignment propertyAssignment;


    public PropertyMapping(String sourceBeanName, String sourceName, String sourceAccessorName, Type sourceType,
                           String targetName, String targetAccessorName, Type targetType,
                           TargetAssignment propertyAssignment ) {

        this.sourceBeanName = sourceBeanName;
        this.sourceName = sourceName;
        this.sourceAccessorName = sourceAccessorName;
        this.sourceType = sourceType;

        this.targetName = targetName;
        this.targetAccessorName = targetAccessorName;
        this.targetType = targetType;
        this.isTargetAccessorSetter = targetAccessorName.startsWith( "set" );
        this.targetReadAccessorName =
            this.isTargetAccessorSetter ? "get" + targetAccessorName.substring( 3 ) : targetAccessorName;

        this.propertyAssignment = propertyAssignment;
    }

    public String getSourceBeanName() {
        return sourceBeanName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceAccessorName() {
        return sourceAccessorName;
    }

    public Type getSourceType() {
        return sourceType;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getTargetAccessorName() {
        return targetAccessorName;
    }

    public Type getTargetType() {
        return targetType;
    }

    public TargetAssignment getPropertyAssignment() {
        return propertyAssignment;
    }

    /**
     * Whether the target accessor is a setter method or not. The only case where it is not a setter but a getter is a
     * collection-typed property without a getter, to which elements are set by adding the source elements to the
     * collection retrieved via the getter.
     *
     * @return {@code true} if the target accessor is a setter, {@code false} otherwise
     */
    public boolean isTargetAccessorSetter() {
        return isTargetAccessorSetter;
    }

    /**
     * @return the read-accessor for the target property (i.e. the getter method)
     */
    public String getTargetReadAccessorName() {
        return targetReadAccessorName;
    }

    @Override
    public Set<Type> getImportTypes() {
        Set<Type> importTypes = new HashSet<Type>();
        if ( propertyAssignment != null ) {
            if ( isTargetAccessorSetter()
                    && propertyAssignment.getAssignmentType().equals( AssignmentType.ASSIGNMENT )
                    && ( targetType.isCollectionType() || targetType.isMapType() ) ) {
                importTypes.addAll( targetType.getImportTypes() );
            }

            if ( !propertyAssignment.getAssignmentType().equals( AssignmentType.ASSIGNMENT ) ) {
                importTypes.addAll( propertyAssignment.getImportTypes() );
            }
        }
        return importTypes;
    }

    @Override
    public String toString() {
        return "PropertyMapping {" +
            "\n    sourceName='" + sourceAccessorName + "\'," +
            "\n    sourceType=" + sourceType + "," +
            "\n    targetName='" + targetAccessorName + "\'," +
            "\n    targetType=" + targetType + "," +
            "\n    propertyAssignment=" + propertyAssignment +
            "\n}";
    }
}
