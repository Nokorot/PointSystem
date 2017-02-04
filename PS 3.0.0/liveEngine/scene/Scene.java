package scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
	
	public static interface Renderer {
		public void render(List<Entity> entities);
	}
	
	public static abstract class Rendrable extends Entity {
		protected Renderer renderer;
	}
	
	private Map<Renderer, List<Entity>> rendramles = new HashMap<>();
	
	public Scene(){	}
	
	public void addRendrable( Rendrable rendrable ) {
		Renderer renderer = rendrable.renderer;
		
		if (!rendramles.containsKey(renderer))
			rendramles.put(renderer, new ArrayList<>());
		rendramles.get(renderer).add(rendrable);
	}
	
	public void addEntity( Renderer renderer , Entity entity ){
		if (!rendramles.containsKey(renderer))
			rendramles.put(renderer, new ArrayList<>());
		rendramles.get(renderer).add(entity);
	}
	
	public void render() {
		for (Renderer renderer : rendramles.keySet() )
			renderer.render(rendramles.get(renderer));
	}
	
}
