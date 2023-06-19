/**
 * @author lin
 * @version 1.0
 */
public class FreePartitions {
    private String name;
    private int addr;
    private int size;
    private char state;

    public FreePartitions() {
    }

    public FreePartitions(String name, int addr, int size, char state) {
        this.name = name;
        this.addr = addr;
        this.size = size;
        this.state = state;
    }

    /**
     * 获取
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return addr
     */
    public int getAddr() {
        return addr;
    }

    /**
     * 设置
     *
     * @param addr
     */
    public void setAddr(int addr) {
        this.addr = addr;
    }

    /**
     * 获取
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 获取
     *
     * @return state
     */
    public char getState() {
        return state;
    }

    /**
     * 设置
     *
     * @param state
     */
    public void setState(char state) {
        this.state = state;
    }

    public String toString() {
        return "name = " + name + ", addr = " + addr + ", size = " + size + ", state = " + state;
    }
}
