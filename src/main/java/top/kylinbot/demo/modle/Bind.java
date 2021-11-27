package top.kylinbot.demo.modle;

public class Bind {
    private long key;

    public Bind(){

    }

    public Bind(Integer i){
        this.key = i;
    }

    @Override
    public String toString(){
        return "key{" +
                "bind:" + key +
                "}";
    }


}
