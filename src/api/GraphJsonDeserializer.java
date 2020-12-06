package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        directed_weighted_graph g =new DWGraph_DS();
        JsonObject nodesJson = jsonObject.get("Nodes").getAsJsonObject();
        for(Map.Entry<String,JsonElement> tmpNode :nodesJson.entrySet()){
            String hashKey = tmpNode.getKey();
            JsonElement jasonValueElement = tmpNode.getValue();
            int NodeKey = jasonValueElement.getAsJsonObject().get("id").getAsInt();
            node_data n = new NodeData(NodeKey);//do a constructor that get node with a key
            g.addNode(n);

        }
        return null;
    }
}
