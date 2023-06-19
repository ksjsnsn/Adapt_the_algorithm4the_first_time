/**
 * @author lin
 * @version 1.0
 */
public class JCB {
    private String name;
    private char state;
    private int size;
    private int addr;

    public JCB() {
    }

    public JCB(String name, char state, int needTime, int size, int addr) {
        this.name = name;
        this.state = state;
        this.size = size;
        this.addr = addr;
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

    public String toString() {
        return "name = " + name + ", addr = " + addr + ", size = " + size + ", state = " + state;
    }
}
