package com.amarsoft.batch_demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


@Configuration
public class JobFlowDemo1 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowDemoJob1() {
        return jobBuilderFactory.get("flowDemoJob1")
                .start(step1())
                .build();
    }
    @Bean
    public Step step1() {//普通Tasklet的写法
        return stepBuilderFactory.get("firstStepDemo")//Step的名称
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                })//Step具体实现类
                .build();
    }
    @Bean
    public Step chunStep(){//创建chunk示例的Step
        return stepBuilderFactory.get("endStep")
                .<String,String>chunk(2)//每次处理两条数据
                .reader(readerDemo())//输入
                .processor(processorDemo())//处理
                .writer(writeDemo())//输出
                .build();
    }
    @Bean
    @StepScope
    public ItemReader<String> readerDemo() {//创建一个数据源
        return new ListItemReader<String>(Arrays.asList("a","b","c","d","e","f","g"));
    }

    @Bean
    @StepScope
    public ItemProcessor<String,String> processorDemo() {//获取到读取到的数据一笔一笔处理
        return (ItemProcessor<String, String>)  item -> {
            System.out.println("=========正在处理数据：" + item);
            return item;
        };
    }
    @Bean
    @StepScope
    public ItemWriter<String> writeDemo() {//将处理完成的数据进行输出
        return items -> {
            for (String str : items)
                System.out.println("======正在输出数据" + str);
        };
    }
    @Bean
    @StepScope
    public ItemWriter writeDemo1() {//将处理完成的数据进行输出
        return  new ItemWriter(){
            @Override
            public void write(List list) throws Exception {

            }
        };

    }


}
