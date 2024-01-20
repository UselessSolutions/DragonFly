package useless.dragonfly.model.blockstates.data;

import java.util.HashMap;
import java.util.Map;

public class Condition {
	public String id = "and"; // can be 'and' or 'or''
	public Map<String, String> conditions;
	public Condition(String id, Map<String, String> conditions){
        this.id = id;
        this.conditions = conditions;
    }
	public boolean match(HashMap<String, String> blockState){
		if (conditions == null || conditions.isEmpty()) return true;
		switch (id){
			case "or":
				for (Map.Entry<String, String > entry: conditions.entrySet()) {
					String stateValue = blockState.get(entry.getKey());
					if (stateValue == null){
//						DragonFly.LOGGER.warn("Could not find corresponding value for '" + entry.getKey() + "' in model '" + baseModel.namespaceId + "'!");
						continue;
					}
					if(stateValue.equals(entry.getValue())){
						return true;
					}
				}
				return false;
			case "and":
				boolean stateMet = true;
				for (Map.Entry<String, String > entry: conditions.entrySet()) {
					String stateValue = blockState.get(entry.getKey());
					if (stateValue == null){
//						DragonFly.LOGGER.warn("Could not find corresponding value for '" + entry.getKey() + "' in model '" + baseModel.namespaceId + "'!");
						stateMet = false;
						continue;
					}
					stateMet &= stateValue.equals(entry.getValue());
				}
				return stateMet;
		}
		throw new RuntimeException("Could not match any conditions for '" + id + "' with " + conditions);
	}
}
