package useless.dragonfly.utilities;

public class NamespaceId {
	public static final String coreNamepaceId = "minecraft";
	private String namespace;
	private String id;
	public NamespaceId(String namespace, String id){
		this.namespace = namespace.toLowerCase();
		this.id = id.toLowerCase();
	}
	public String getNamespace(){
		return namespace;
	}
	public String getId(){
		return id;
	}
	public String toString(){
		return (namespace + ":" + id).toLowerCase();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String){
			return this.toString().equals(((String) obj).toLowerCase());
		}
		if (obj instanceof NamespaceId){
			return this.namespace.equals(((NamespaceId) obj).namespace) && this.id.equals(((NamespaceId) obj).id);
		}
		return super.equals(obj);
	}

	public static NamespaceId namespaceFromFormattedString(String formattedString){
		formattedString = formattedString.toLowerCase();
		String namespace = coreNamepaceId;
		String id;
		if (formattedString.contains(":")){
			namespace = formattedString.split(":")[0];
			id = formattedString.split(":")[1];
		} else {
			id = formattedString;
		}

		if (namespace.contains(":") || namespace.isEmpty()) throw new IllegalArgumentException("Namespace '" + namespace + "' is not formatted correctly!");
		if (id.contains(":") || id.isEmpty()) throw new IllegalArgumentException("Id '" + id + "' is not formatted correctly!");

		return new NamespaceId(namespace, id);
	}
}
