package no.nokorot.serialization;

import java.util.List;

public class NBFolder {

	private NBFolder parrent;
	private List<NBFolder> folders;
	private List<NBObject> objects; 
	
	public NBFolder(NBFolder parrent){
		this.parrent = parrent;
	}
	
	private int getSize() {
		int size = 0;
		for (NBFolder folder : folders)
			size += folder.getSize();
		for (NBObject object : objects)
			size += object.getSize();
		return size;
	}
	
	public NBFolder getParrent() {
		return parrent;
	}
	
	public List<NBFolder> getFolders() {
		return folders;
	}
	
	public List<NBObject> getObjects() {
		return objects;
	}
	
}
