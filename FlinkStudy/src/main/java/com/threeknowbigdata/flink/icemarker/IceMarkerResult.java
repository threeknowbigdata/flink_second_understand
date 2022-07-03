package com.threeknowbigdata.flink.icemarker;

/**
 * 类描述：
 *
 * @ClassName OrderResult
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/1 下午2:47
 */
public class IceMarkerResult {
    private Long orderId;
    private String resultState;

    public IceMarkerResult() {
    }

    public IceMarkerResult(Long orderId, String resultState) {
        this.orderId = orderId;
        this.resultState = resultState;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "orderId=" + orderId +
                ", resultState='" + resultState + '\'' +
                '}';
    }
}
