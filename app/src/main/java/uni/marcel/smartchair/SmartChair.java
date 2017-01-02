package uni.marcel.smartchair;

public class SmartChair {

    private Sensor[] sensors;
    private int THRESHOLD = 50;

    public SmartChair() {

    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public void setSensors(Sensor[] sensors) {
        if(sensors != null) {
            this.sensors = sensors;
        }
    }

    public void addSensor(Sensor s) {
        if(s != null) {
            Sensor[] temp = new Sensor[sensors.length + 1];
            for(int i = 0; i < sensors.length; i++) {
                temp[i] = sensors[i];
            }
            temp[sensors.length] = s;
            this.sensors = temp;
        }
    }

    public Sensor getSensor(int id) {
        if(id >= 0) {
            for(Sensor s : sensors) {
                if(s.getId() == id) {
                    return s;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }

    //TODO messung der last in seat klasse
    public Sensor measureLoad() {
        Sensor sensor = null;



        return sensor;
    }
}
