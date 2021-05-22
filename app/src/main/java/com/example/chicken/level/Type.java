package com.example.chicken.level;

public enum Type {
    moveBy, column, circle, row;

    public Type next() {
        switch (this.ordinal()) {
            case 0:
                return Type.column;
            case 1:
                return Type.circle;
            case 2:
                return Type.row;
            case 3:
                return Type.moveBy;
            default:
                return Type.row;
        }
    }
}
