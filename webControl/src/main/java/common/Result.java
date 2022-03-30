package common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "返回的对象", description = "code=0　表示数据正常，　-1表示异常，其它状态可自定义")
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "code=0　表示数据正常，　-1表示异常，其它状态可自定义", name = "code", example = "")
    private int code = -1;
    @ApiModelProperty(value = "返回消息", name = "message", example = "")
    private String message;

    @ApiModelProperty(value = "当出现多个自定义状态时候，可用此字段标记成功", name = "success", example = "")
    private boolean success = false;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @ApiModelProperty(value = "返回数据", name = "data", example = "")
    private T data;

    public Result() {

    }

    public Result(T data) {
        this.code = ErrorCode.Success.getCode();
        this.message = ErrorCode.Success.getMessage();
        this.data = data;
    }

    public Result(Exception e) {
        this.code = ErrorCode.Failure.getCode();
        this.message = e.getMessage();
    }


    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        success = (code == ErrorCode.Success.getCode());
        return success;
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static <T> Result<T> newSuccessResult() {
        return new Result<>(ErrorCode.Success.getCode(), ErrorCode.Success.getMessage());
    }

    public Result fail(String message) {
        this.message = message;
        return this;
    }

    public Result<T> fail(T data) {
        Result result = new Result();
        result.setCode(Code.FAIL);
        result.setMessage(ErrorCode.Failure.getMessage());
        result.setData(data);
        return result;
    }

    public Result<T> success(T data) {
        Result result = new Result();
        result.setCode(Code.SUCCESS);
        result.setMessage(ErrorCode.Success.getMessage());
        result.setData(data);
        return result;
    }

    public Result<T> success() {
        Result result = new Result();
        result.setCode(Code.SUCCESS);
        result.setMessage(ErrorCode.Success.getMessage());
        return result;
    }

    public static class Message {
        public static final String RESULT_IS_EMPTY = "结果为空";
        public static final String PARAM_IS_EMPTY = "参数为空";
        public static final String PARAM_IS_ERROR = "参数错误";
        public static final String RESULT_IS_ERROR = "结果失败";
        public static final String USERNAME_IS_REPEAT = "用户名重复";
        public static final String MOBILE_IS_REPEAT = "手机号重复";
        public static final String USERNAME_IS_EXISTED = "用户名已存在";
        public static final String MOBILE_IS_EXISTED = "手机号已存在";
        public static final String MOBILE_IS_ERROR = "请输入正确的手机号";
        public static final String CODE_IS_ERROR = "请输入正确的验证码";
        public static final String PASSWORD_IS_ERROR = "密码错误";
        public static final String COMPANY_IS_EMPTY = "公司为空";
        public static final String DEPARTMENT_IS_EMPTY = "部门为空";
        public static final String CARRER_IS_EMPTY = "岗位为空";
        public static final String COMPANYDEGREE_IS_ERROR = "公司等级错误";
        public static final String FILE_IS_EMPTY = "文件为空";
        public static final String EMPLOYEE_IS_EMPTY = "用户不存在";
        public static final String CODE_SEND_ERROR = "短信发送失败";
        public static final String IMAGE_UID_IS_EMPTY = "图形码ID为空";
        public static final String UID_IS_EMPTY = "UID为空";
        public static final String TOKEN_IS_EMPTY = "Token is Empty";
        public static final String USERTYPE_IS_EMPTY = "管理员类型为空";
        public static final String USERTYPE_IS_ERROR = "管理员类型有误";
        public static final String COMPANY_IS_ERROR = "公司信息有误";
        public static final String DEPARTMENT_IS_ERROR = "部门信息有误";
    }

    public static class Code {
        public static final Integer SUCCESS = 0;
        public static final Integer FAIL = -1;
    }
}
