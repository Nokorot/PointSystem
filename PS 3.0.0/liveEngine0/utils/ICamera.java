package utils;
public interface ICamera {
	
	public Vector3f getPosition();
	public Matrix4f getViewMatrix();
	public void reflect(float height);
	public Matrix4f getProjectionMatrix();
	public Matrix4f getProjectionViewMatrix();

}
