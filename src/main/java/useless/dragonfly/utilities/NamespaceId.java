package useless.dragonfly.utilities;

public class NamespaceId {
	public static final String coreNamespaceId = "minecraft"; // This is also used as the default namespace if one is not provided
	private final String namespace;
	private final String id;
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
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public static NamespaceId idFromString(String formattedString){
		formattedString = formattedString.toLowerCase();
		String namespace = coreNamespaceId;
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
