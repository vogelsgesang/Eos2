package de.lathanda.eos.robot.gui;

public class Camera {
	private double[] cameraPositionXYZ = new double[] { 0d, -10, 6 };
	private double cameraRotationX     =  -60; // 360°
	private double cameraRotationZ     =    0;
	public double getCameraPositionX() {
		return cameraPositionXYZ[0];
	}
	public double getCameraPositionY() {
		return cameraPositionXYZ[1];
	}
	public double getCameraPositionZ() {
		return cameraPositionXYZ[2];
	}
	public double getCameraRotationX() {
		return cameraRotationX;
	}
	public double getCameraRotationZ() {
		return cameraRotationZ;
	}
	public void setCameraPositionX(double cameraPositionX) {
		this.cameraPositionXYZ[0] = cameraPositionX;
	}
	public void setCameraPositionY(double cameraPositionY) {
		this.cameraPositionXYZ[1] = cameraPositionY;
	}
	public void setCameraPositionZ(double cameraPositionZ) {
		this.cameraPositionXYZ[2] = cameraPositionZ;
	}
	public void setCameraRotationX(double cameraRotationX) {
		this.cameraRotationX = cameraRotationX;
	}
	public void setCameraRotationZ(double cameraRotationZ) {
		this.cameraRotationZ = cameraRotationZ;
	}
	public void moveCamera(double dx, double dy, double dz) {
		cameraPositionXYZ[0] += dx;
		cameraPositionXYZ[1] += dy;
		cameraPositionXYZ[2] += dz;
	}
	public void rotateCamera(double rx, double rz) {
		cameraRotationX += rx;
		cameraRotationZ += rz;
	}
	
}
