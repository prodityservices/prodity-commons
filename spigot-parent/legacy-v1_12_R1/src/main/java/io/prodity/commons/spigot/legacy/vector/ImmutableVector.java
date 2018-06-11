package io.prodity.commons.spigot.legacy.vector;

import org.bukkit.util.Vector;

public class ImmutableVector extends Vector {

    public ImmutableVector() {
        super();
    }

    public ImmutableVector(Vector vector) {
        super(vector.getX(), vector.getY(), vector.getZ());
    }

    public ImmutableVector(int x, int y, int z) {
        super(x, y, z);
    }

    public ImmutableVector(double x, double y, double z) {
        super(x, y, z);
    }

    public ImmutableVector(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public Vector add(Vector vector) {
        return this.toVector().add(vector);
    }

    @Override
    public Vector subtract(Vector vec) {
        return this.toVector().subtract(vec);
    }

    @Override
    public Vector multiply(Vector vec) {
        return this.toVector().multiply(vec);
    }

    @Override
    public Vector divide(Vector vec) {
        return this.toVector().divide(vec);
    }

    @Override
    public Vector copy(Vector vec) {
        return this.toVector().copy(vec);
    }

    @Override
    public Vector midpoint(Vector other) {
        return this.toVector().midpoint(other);
    }

    @Override
    public Vector multiply(float m) {
        return this.toVector().multiply(m);
    }

    @Override
    public Vector multiply(double m) {
        return this.toVector().multiply(m);
    }

    @Override
    public Vector multiply(int m) {
        return this.toVector().multiply(m);
    }

    @Override
    public Vector crossProduct(Vector o) {
        return this.toVector().crossProduct(o);
    }

    @Override
    public Vector normalize() {
        return this.toVector().normalize();
    }

    @Override
    public Vector zero() {
        return this.toVector().zero();
    }

    @Override
    public Vector setX(int x) {
        return this.toVector().setX(x);
    }

    @Override
    public Vector setX(float x) {
        return this.toVector().setX(x);
    }

    @Override
    public Vector setX(double x) {
        return this.toVector().setX(x);
    }

    @Override
    public Vector setY(int y) {
        return this.toVector().setY(y);
    }

    @Override
    public Vector setY(float y) {
        return this.toVector().setY(y);
    }

    @Override
    public Vector setY(double y) {
        return this.toVector().setY(y);
    }

    @Override
    public Vector setZ(int z) {
        return this.toVector().setZ(z);
    }

    @Override
    public Vector setZ(float z) {
        return this.toVector().setZ(z);
    }

    @Override
    public Vector setZ(double z) {
        return this.toVector().setZ(z);
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

}