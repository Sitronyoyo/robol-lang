import java.util.HashMap;

class ActivationRecord {
    private HashMap<String, Integer> localBindings;

    public ActivationRecord() {
        localBindings = new HashMap<>();
    }

    public void setBinding(String name, Integer value) {
        localBindings.put(name, value);
    }

    public Integer getValue(String name) {
        if (localBindings.containsKey(name)) {
            return localBindings.get(name);
        }
        return null;
    }
}
