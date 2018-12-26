package spring2.response;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/16 0016 13:44
 */
public class CommonReturnType {
    // 表明对应的请求的返回处理结构"success"或"fail"
    private String status;

    // 若status=success，则data内返回前端需要的json数据
    // 若status=fail，则data内返回前端通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
