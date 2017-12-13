package de.misterY;

/**
 * A 2-dimensional vector that provides some useful vector-specific methods.
 *
 */
public class Vector2D {
	private double x;
	private double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/**
	 * Returns the x-component of this vector multiplied by the given width as an
	 * integer to get an x-position to draw with.
	 * 
	 * @param width
	 *            The width.
	 * @return The x-position to draw with.
	 */
	public int getDrawX(int width) {
		return (int) (x * width);
	}

	/**
	 * Returns the y-component of this vector multiplied by the given height as an
	 * integer to get an y-position to draw with.
	 * 
	 * @param height
	 *            The height.
	 * @return The y-position to draw with.
	 */
	public int getDrawY(int height) {
		return (int) (y * height);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLength() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	/**
	 * Sets the length of this vector by normalizing it and multiply it by length.
	 * 
	 * @param length
	 *            The target length.
	 * @return this
	 */
	public Vector2D setLength(double length) {
		normalize();
		multiply(length);
		return this;
	}

	/**
	 * Normalizes this vector
	 * 
	 * @return this
	 */
	public Vector2D normalize() {
		double length = getLength();
		x /= length;
		y /= length;
		return this;
	}

	/**
	 * Multiplies this vector by the given number.
	 * 
	 * @param m
	 *            The number to multiply this vector with.
	 * @return this
	 */
	public Vector2D multiply(double m) {
		x *= m;
		y *= m;
		return this;
	}

	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param vec
	 *            The vector to add to this vector.
	 * @return this
	 */
	public Vector2D add(Vector2D vec) {
		x += vec.x;
		y += vec.y;
		return this;
	}

	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param vec
	 *            The vector to subtract from this vector.
	 * @return this
	 */
	public Vector2D subtract(Vector2D vec) {
		x -= vec.x;
		y -= vec.y;
		return this;
	}

	/**
	 * Rotates this vector by the given angle in radians.
	 * 
	 * @param radians
	 *            The angle in radians to rotate the vector by.
	 * @return this
	 */
	public Vector2D rotate(double radians) {
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		double ox = x;
		double oy = y;
		x = cos * ox - sin * oy;
		y = sin * ox + cos * oy;
		return this;
	}
	
	/**
	 * Returns the distance between this vector and the given vector
	 * 
	 * @param vec
	 * @return
	 */
	public double getDistance(Vector2D vec) {
		return getClone().subtract(vec).getLength();
	}

	/**
	 * Returns a clone of this vector.
	 * 
	 * @return A clone of this vector.
	 */
	public Vector2D getClone() {
		return (Vector2D) clone();
	}

	@Override
	public boolean equals(Object obj) {
		Vector2D vec = (Vector2D) obj;
		return vec.x == x && vec.y == y;
	}

	@Override
	protected Object clone() {
		return new Vector2D(x, y);
	}
}
