package cz.kotu.ld29;

/**
 * @author tkotula
 */
class Store {

    FieldType content = FieldType.VOID;

    boolean isEmpty() {
        return content == null || content == FieldType.VOID;
    }

    boolean isPickable() {
        return !isEmpty() && (content != FieldType.BORDER);
    }

}
