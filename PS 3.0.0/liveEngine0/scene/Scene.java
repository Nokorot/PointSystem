package scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ICamera;
import utils.Vector3f;

public class Scene {

	private Map<Model, List<Entity>> entities = new HashMap<>();
	
	private ICamera camera;
	private Vector3f lightDirection = new Vector3f(0, -1, 0);
	
	public Scene(ICamera camera) {
		this.camera = camera;
	}

	public void setLightDirection(Vector3f direction) {
		direction.normalize();
		this.lightDirection.set(direction);
	}
	
	public void addEntity(Entity entity) {
		Model model = entity.getModel();
		if (!entities.containsKey(model))
			entities.put(model, new ArrayList<>());
		entities.get(model).add(entity);
	}

	public Vector3f getLightDirection() {
		return lightDirection;
	}

	public ICamera getCamera() {
		return camera;
	}
	
	public Map<Model, List<Entity>> getEntities() {
		return entities;
	}

	public void delete() {
		for (Model model : entities.keySet())
			for (Entity entity : entities.get(model)) 
				entity.delete();
	}

}
