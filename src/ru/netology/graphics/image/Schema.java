package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    final private char[] chars = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        int i = color / 32;
        return chars[i];
    }
}
