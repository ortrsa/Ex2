package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        directed_weighted_graph g = new DWGraph_DS();
        JsonElement GraphCheck = jsonObject.get("Graph");
        if (GraphCheck != null) {
            JsonObject nodesJson = jsonObject.get("Graph").getAsJsonObject();
            for (Map.Entry<String, JsonElement> tmpNode : nodesJson.entrySet()) {
                String hashKey = tmpNode.getKey();
                JsonElement jasonValueElement = tmpNode.getValue();
                int NodeKey = jasonValueElement.getAsJsonObject().get("key").getAsInt();
                int NodeTag = jasonValueElement.getAsJsonObject().get("tag").getAsInt();
                double NodeWeight = jasonValueElement.getAsJsonObject().get("Weight").getAsDouble();
                String NodeInfo = jasonValueElement.getAsJsonObject().get("Info").getAsString();
                double NodeX = jasonValueElement.getAsJsonObject().get("X").getAsDouble();
                double NodeY = jasonValueElement.getAsJsonObject().get("Y").getAsDouble();
                double NodeZ = jasonValueElement.getAsJsonObject().get("Z").getAsDouble();

                node_data n = new NodeData(NodeX, NodeY, NodeZ, NodeKey, NodeInfo, NodeTag, NodeWeight);
                g.addNode(n);
            }
            JsonObject edgesJson = jsonObject.get("Edges").getAsJsonObject();
            for (Map.Entry<String, JsonElement> tmpEdge : edgesJson.entrySet()) {
                JsonElement EdgeValue = tmpEdge.getValue();
                for (Map.Entry<String, JsonElement> tmpK : EdgeValue.getAsJsonObject().entrySet()) {
                    JsonElement tmpEd = tmpK.getValue();
                    int EdSrc = tmpEd.getAsJsonObject().get("Src").getAsInt();
                    int EdDest = tmpEd.getAsJsonObject().get("Dest").getAsInt();
                    double EdWeight = tmpEd.getAsJsonObject().get("Weight").getAsDouble();
                    g.connect(EdSrc, EdDest, EdWeight);
                }
            }
            return (DWGraph_DS) g;
        } else {
            JsonObject nodesJson = jsonObject.get("Nodes").getAsJsonObject();
            for (Map.Entry<String, JsonElement> tmpNode : nodesJson.entrySet()) {
                String hashKey = tmpNode.getKey();
                JsonElement jasonValueElement = tmpNode.getValue();
                int NodeKey = jasonValueElement.getAsJsonObject().get("id").getAsInt();
                String NodePos = jasonValueElement.getAsJsonObject().get("pos").getAsString();
                String[] PosArr = NodePos.split(",");
                double[] DoublePos = new double[3];
                for (int i = 0; i < PosArr.length; i++) {
                    DoublePos[i] = Double.parseDouble(PosArr[i]);
                }
                node_data n = new NodeData(DoublePos[0], DoublePos[1], DoublePos[2], NodeKey);
                g.addNode(n);
            }
            JsonObject edgesJson = jsonObject.get("Edges").getAsJsonObject();
            for (Map.Entry<String, JsonElement> tmpEdge : edgesJson.entrySet()) {
                JsonElement EdgeValue = tmpEdge.getValue();
                for (Map.Entry<String, JsonElement> tmpK : EdgeValue.getAsJsonObject().entrySet()) {
                    JsonElement tmpEd = tmpK.getValue();
                    int EdSrc = tmpEd.getAsJsonObject().get("src").getAsInt();
                    int EdDest = tmpEd.getAsJsonObject().get("dest").getAsInt();
                    double EdWeight = tmpEd.getAsJsonObject().get("w").getAsDouble();
                    g.connect(EdSrc, EdDest, EdWeight);
                }
            }

            return (DWGraph_DS) g;
        }

    }
}