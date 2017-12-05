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
	 */
	public void setLength(double length) {
		normalize();
		multiply(length);
	}

	/**
	 * Normalizes this vector
	 */
	public void normalize() {
		double length = getLength();
		x /= length;
		y /= length;
	}

	/**
	 * Multiplies this vector by the given number.
	 * 
	 * @param m
	 *            The number to multiply this vector with.
	 */
	public void multiply(double m) {
		x *= m;
		y *= m;
	}

	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param vec
	 *            The vector to add to this vector.
	 */
	public void add(Vector2D vec) {
		x += vec.x;
		y += vec.y;
	}

	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param vec
	 *            The vector to subtract from this vector.
	 */
	public void subtract(Vector2D vec) {
		x -= vec.x;
		y -= vec.y;
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
