package sample.wjj.rxandroidglidedemo.bean;

/**
 * Created by jiajiewang on 16/6/3.
 */
public class SampleModel {

    public String name;
    public String content;

    public SampleModel(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
