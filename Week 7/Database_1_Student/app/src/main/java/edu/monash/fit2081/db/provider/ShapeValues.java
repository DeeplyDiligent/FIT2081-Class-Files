/*
This class carries all the instance variable values about a shape instance (rectangle or circle)
It allows us to create and pass around all this data using a single object reference
rather than manipulating the instance variables one by one
 */
package edu.monash.fit2081.db.provider;

public class ShapeValues {
    private int id;
    private String shapeType;
    private int x;
    private int y;
    private int border;
    private int radius;
    private int width;
    private int height;
    private String color;

    //CONSTRUCTORS
    public ShapeValues() { //block default, no parameter constructor
    }

    public ShapeValues(String shapeType, int x, int y, int border, int radius, int width, int height, String color) {
        this.shapeType = shapeType;
        this.x = x;
        this.y = y;
        this.border = border;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.color=color;
    }

    //ACCESSORS
    public int getId() {
        return id;
    }

    public String getShapeType() {
        return shapeType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBorder() {
        return border;
    }

    public int getRadius() {
        return radius;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getColor() {
        return color;
    }


    //MUTATORS
    public void setId(int id) {
        this.id = id;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColor(String color) {
        this.color = color;
    }


    //toString
    @Override
    public String toString() {
        return "ShapeValues{" +
                "shapeType='" + shapeType + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", border=" + border +
                ", radius=" + radius +
                ", width=" + width +
                ", height=" + height +
                ", color='" + color + '\'' +
                '}';
    }
}
