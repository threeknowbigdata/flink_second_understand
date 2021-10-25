package com.threeknowbigdata.flink.datastream.function.udaf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.AggregateFunction;

/**
 * 类描述：
 *
 * @ClassName UDAFSum
 * @Description: 自定义UDAF，进行多行输入，一行输出
 * @Author: 土哥
 * @Date: 2021/3/29 10:17
 */

@FunctionHint(input = @DataTypeHint("INT"),output = @DataTypeHint("INT"))
public class UDAFSum extends AggregateFunction<Integer,UDAFSum.SumAccumulator> {


    //定义一个Accumulator,存放聚合的中间结果
    public static class SumAccumulator{
        public int sumPrice;
    }

    /**
     * 初始化Accumulator
     * @return
     */
    @Override
    public SumAccumulator createAccumulator() {
        SumAccumulator sumAccumulator = new SumAccumulator();
        sumAccumulator.sumPrice = 0;
        return  sumAccumulator;
    }

    /**
     * 定义如何根据输入更新Accumulator
     * @param accumulator
     * @param price
     */
    public void accumulate(SumAccumulator accumulator,int price){
        accumulator.sumPrice += price;

    }

    /**
     * 返回聚合的最终结果
     * @param accumulator
     * @return
     */
    @Override
    public Integer getValue(SumAccumulator accumulator) {

        return accumulator.sumPrice;
    }


}
