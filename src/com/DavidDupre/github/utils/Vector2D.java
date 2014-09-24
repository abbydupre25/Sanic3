package com.DavidDupre.github.utils;

public class Vector2D {
	public double x, y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D() {
		this(0, 0);
	}

	public Vector2D translate(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector2D subtract(Vector2D p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}

	public Vector2D add(Vector2D p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}

	public Vector2D multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	public double distSquared(Vector2D p) {
		double dX = p.x - x, dY = p.y - y;
		return (dX * dX) + (dY * dY);
	}

	public double dist(Vector2D p) {
		return (double) Math.sqrt(distSquared(p));
	}

	public double magnitude() {
		return (double) Math.sqrt((x * x) + (y * y));
	}

	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector2D) {
			Vector2D p = (Vector2D) o;
			double xD = p.x - x, yD = p.y - y;
			return xD == 0 && yD == 0;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Vector2D translate(Vector2D trans) {
		return translate(trans.x, trans.y);
	}

	public Vector2D normalize() {
		double dist = dist(new Vector2D(0, 0));
		if (dist != 0) {
			x /= dist;
			y /= dist;
		}
		return this;
	}

	public static Vector2D normalize(Vector2D p) {
		return p.clone().normalize();
	}

	public static double dotProduct(Vector2D u, Vector2D v) {
		return (u.x * v.x) + (u.y * v.y);
	}

	public static Vector2D negative(Vector2D p) {
		return new Vector2D(-p.x, -p.y);
	}

	public static Vector2D projectOntoPlane(Vector2D planeNormal,
			Vector2D vector) {
		return vector.clone().subtract(
				planeNormal.clone().multiply(
						Vector2D.dotProduct(vector, planeNormal)));
	}

	/**
	 * Spherical linear interpolation from a to b, at time t in [0, 1]
	 */
	public static Vector2D slerp(Vector2D a, Vector2D b, double t) {
		double angle = (double) Math.acos(dotProduct(a.clone(), b.clone()));
		double weightA = (double) (Math.sin((1 - t) * angle) / Math.sin(angle));
		double weightB = (double) (Math.sin(t * angle) / Math.sin(angle));
		return a.clone().multiply(weightA).add(b.clone().multiply(weightB));
	}

	public Vector2D reverse() {
		x = -x;
		y = -y;
		return this;
	}

	public Vector2D abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}

	public void set(double i, double j) {
		this.x = i;
		this.y = j;
	}

	public Vector2D set(Vector2D v) {
		set(v.x, v.y);
		return this;
	}

	public double mag2() {
		return (x * x) + (y * y);
	}
}
