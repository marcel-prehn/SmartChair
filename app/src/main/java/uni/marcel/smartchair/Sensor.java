package uni.marcel.smartchair;

public class Sensor {

    private int id;
    private int value;

    public Sensor(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public void setId(int id) {
        if(id > -1) {
            this.id = id;
        }
    }

    public int getId() {
        return this.id;
    }

    public void setValue(int value) {
        if(value > -1) {
            this.value = value;
        }
    }

    public int getValue() {
        return this.value;
    }
}
