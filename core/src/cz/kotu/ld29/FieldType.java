package cz.kotu.ld29;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author tkotula
 */
enum FieldType {
    VOID,
    GROUND,
    STONE,
    SUPPORT,
    // non-destructible
    BORDER,
    BEDROCK,
    LEAF,;

    private static final Set<FieldType> supportsAbove = EnumSet.of(GROUND, STONE, SUPPORT, BORDER, BEDROCK);
    private static final Set<FieldType> blocksLight = EnumSet.of(GROUND, STONE, SUPPORT, BORDER, BEDROCK);
    private static final Set<FieldType> isStatic = EnumSet.of(GROUND, BORDER, BEDROCK);

    boolean blocksLight() {
        return blocksLight.contains(this);
    }

    boolean supportsAbove() {
        return supportsAbove.contains(this);
    }

    boolean isStatic() {
        return isStatic.contains(this);
    }

}
