package common;

public enum ErrorCode {
    Failure(-1,"操作失败！"),Success(0,"操作成功！");
    private int code=-1;
    private String message="操作失败！";

    private ErrorCode(int code) {
        this.code = code;
    }


    private ErrorCode(int code, String message) {
        this.code = code;
        this.message=message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

}
